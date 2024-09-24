import logging
import re
from datetime import datetime, timedelta

import pytz
from bs4 import BeautifulSoup
from django.utils import timezone


def extract_text_from_html(html_content):
    # html_content가 None이거나 문자열이 아닌 경우 처리
    if html_content is None:
        return ""

    # html_content가 문자열이 아니면 문자열로 변환
    if not isinstance(html_content, str):
        html_content = str(html_content)

    soup = BeautifulSoup(html_content, 'html.parser')
    text = soup.get_text(separator=' ', strip=True)
    return text


def extract_times(soup, structure_type):
    try:
        if structure_type == 'datetime':
            try:
                created_at = soup.find('span', class_='txt-date').get_text(strip=True)
                updated_at = soup.find_all('span', class_='txt-date')[1].get_text(strip=True)
            except (IndexError, AttributeError):
                updated_at = created_at if 'created_at' in locals() else None

        elif structure_type == 'time_area':
            try:
                created_at = soup.find('dl', class_='registration').find('dd').get_text(strip=True)
                try:
                    updated_at = soup.find_all('dl', class_='registration')[1].find('dd').get_text(
                        strip=True)
                except IndexError:
                    updated_at = created_at  # 없을 경우 created_at으로 설정
            except AttributeError:
                created_at = updated_at = None

        elif structure_type == 'update_time':
            try:
                created_at = \
                    soup.find('p', class_='update-time').get_text(strip=True).split('송고시간')[
                        1].strip()
                updated_at = created_at  # 동일하게 설정
            except (IndexError, AttributeError):
                created_at = updated_at = None

        elif structure_type == 'ah_info':
            try:
                created_at = soup.find('span', class_='ahi_date').get_text(strip=True).replace(
                    '입력 ', '')
                updated_at = soup.find('span', class_='ahi_modify').get_text(strip=True).replace(
                    '수정 ', '')
            except AttributeError:
                created_at = updated_at = None

        elif structure_type == 'viewInfo':
            try:
                text = soup.find('p', class_='viewInfo').get_text(strip=True)
                created_at = text.split('입력 : ')[1].split('수정 : ')[0].strip()  # '수정 :' 앞까지 추출
                updated_at = text.split('수정 : ')[1].strip()  # '수정 :' 이후의 시간 정보만 추출
            except (IndexError, AttributeError):
                created_at = updated_at = None

        elif structure_type == 'channelA':
            try:
                date = soup.find('span', class_='date').get_text(strip=True)
                time = soup.find('span', class_='time').get_text(strip=True)
                created_at = f"{date} {time}"
                updated_at = created_at  # 동일하게 설정
            except AttributeError:
                created_at = updated_at = None

        elif structure_type == 'infoLine':
            try:
                created_at = soup.find('span').get_text(strip=True).replace('등록 ', '')
                updated_at = created_at  # 동일하게 설정
            except AttributeError:
                created_at = updated_at = None

        elif structure_type == 'moneyToday':
            try:
                created_at = soup.find('li', class_='date').get_text(strip=True)
                updated_at = created_at  # 동일하게 설정
            except AttributeError:
                created_at = updated_at = None

        elif structure_type == 'ytn':
            try:
                created_at = soup.find('div', class_='date').get_text(strip=True)
                updated_at = created_at  # 동일하게 설정
            except AttributeError:
                created_at = updated_at = None

        else:
            created_at = updated_at = None

        # 시간 파싱
        created_at = parse_relative_time(created_at)
        updated_at = parse_relative_time(updated_at)

        # 하나가 None일 경우, 다른 값을 동일하게 설정
        if created_at and not updated_at:
            updated_at = created_at
        elif updated_at and not created_at:
            created_at = updated_at

    except Exception as e:
        logging.error(f"시간 추출 중 오류 발생: {e}")
        created_at = updated_at = None  # 예외 발생 시 None 반환

    return created_at, updated_at

def parse_relative_time(relative_time):
    # 현재 시간 가져오기
    seoul_tz = pytz.timezone('Asia/Seoul')
    current_time = timezone.localtime(datetime.now(seoul_tz))  # Aware (UTC) → Aware (TIME_ZONE)

    if relative_time == '방금 전':
        return current_time

    # 불필요한 공백 제거 및 초가 없는 시간 처리
    relative_time = re.sub(r'\s+', ' ', relative_time).strip()  # 불필요한 공백 제거
    relative_time = relative_time.replace('.', '-').strip()

    # '시간 전' 또는 '분 전'으로 끝나는지 확인하고 값 추출
    match = re.match(r'(\d+)(시간|분) 전', relative_time)
    if match:
        value = int(match.group(1))
        unit = match.group(2)

        # 시간 단위 처리
        if unit == '시간':
            absolute_time = current_time - timedelta(hours=value)
        elif unit == '분':
            absolute_time = current_time - timedelta(minutes=value)

        return absolute_time

    try:
        # 오전/오후 처리
        if '오전' in relative_time or '오후' in relative_time:
            is_pm = '오후' in relative_time
            relative_time = relative_time.replace('오전', '').replace('오후', '').strip()

            if len(relative_time.split(':')) == 2:
                relative_time += ":00"  # 초가 없는 경우 00초 추가

            dt = datetime.strptime(relative_time, '%Y-%m-%d %H:%M:%S')
            if is_pm and dt.hour < 12:  # 오후일 경우 12시간을 더해줌
                dt += timedelta(hours=12)

            # 타임존 추가
            return pytz.timezone('Asia/Seoul').localize(dt)

        # 시간 문자열에 초가 없는 경우 처리
        if len(relative_time.split(':')) == 2:  # '시:분' 형식이면
            relative_time += ":00"  # 초를 00으로 추가

        # 시간 문자열에 연이 없는 경우 처리
        if len(relative_time.split('-')) == 2:
            relative_time = str(current_time.year) + '-' + relative_time

        # 연-월-일-시간 형식일 경우 처리
        if len(relative_time.split('-')) == 4:
            parts = relative_time.split('-')
            relative_time = f"{parts[0]}-{parts[1]}-{parts[2]} {parts[3]}"

        # 타임존 추가
        dt = datetime.strptime(relative_time, '%Y-%m-%d %H:%M:%S')
        return pytz.timezone('Asia/Seoul').localize(dt)

    except Exception as e:
        logging.error(f"시간 변환 과정 중 예외 발생: {e}")
        return relative_time  # 형식이 맞지 않으면 원래 값을 반환
