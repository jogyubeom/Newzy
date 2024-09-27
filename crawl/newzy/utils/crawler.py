import logging
import time

from bs4 import BeautifulSoup
from selenium.common.exceptions import TimeoutException
from selenium.webdriver.common.by import By
from selenium.webdriver.support.wait import WebDriverWait

from newzy.db_operations import insert_news_to_db
from newzy.utils.analyzer import analyze_difficulty_of_news
from newzy.utils.parser import parse_relative_time, extract_times


def crawl_news(driver,
               start_times, end_times,
               start_date, end_date, url_set,
               publisher, url_template,
               ul_type, ul_class,
               li_type, time_selector,
               title_type, title_selector,
               link_selector, link_prefix,
               max_pages, article_count_by_hour,
               news_link_list):
    start_times[publisher] = time.time()

    page = 1
    if publisher == "세계 일보":
        page = 0

    while True:
        url = url_template.format(page=page)
        try:
            driver.set_page_load_timeout(10)
            driver.get(url)
            WebDriverWait(driver, 5).until(
                lambda d: d.execute_script('return document.readyState') == 'complete')
        except TimeoutException:
            logging.error(f"URL {url} 접속 시 타임아웃이 발생했습니다.")
            return False
        except Exception as e:
            logging.error(f"URL 접속할 때 오류가 발생하였습니다. {e}")
            return False

        soup = BeautifulSoup(driver.page_source, 'html.parser')
        ul_tag = soup.find(ul_type, class_=ul_class)
        if not ul_tag:
            logging.warning(f">>> {publisher} : 더 이상 기사가 없습니다.")
            break

        li_tags = ul_tag.find_all(li_type)
        for li in li_tags:
            title_tag = li.select_one(title_selector)
            skip_keywords = ["[숏폼]", "[표]"]

            if title_tag and any(
                    keyword in title_tag.get_text(strip=True) for keyword in skip_keywords):
                continue  # 제목에 skip_keywords 중 하나가 포함된 경우 건너뜀

            time_tag = li.select_one(time_selector)
            if time_tag:
                # 뉴시스 :: p.time 태그 속 span 태그를 제거
                if time_tag.find('span'):
                    time_tag.find('span').decompose()

                time_text = time_tag.get_text(strip=True)

                # 머니투데이 :: 기자 이름 제거
                if publisher == "머니 투데이":
                    # 기자 이름 및 불필요한 문자 제거 (공백 및 여러 개의 공백 포함)
                    time_text = time_text.split()[-2:]  # 시간 정보가 항상 마지막 두 요소에 위치
                    time_text = " ".join(time_text)

                article_time = parse_relative_time(time_text)

                if start_date <= article_time:
                    if article_time > end_date:
                        logging.info(f"{end_date} 보다 이후에 작성된 기사입니다.")
                        continue
                    hour = article_time.strftime('%H')
                    article_count_by_hour[hour] += 1

                    link_tag = li.select_one(link_selector)
                    if link_tag and 'href' in link_tag.attrs:
                        full_link = link_prefix + link_tag['href']

                        if full_link in url_set:
                            continue

                        url_set.add(full_link)

                        img_link = None  # 기본적으로 이미지 링크를 None으로 설정
                        img_tag = li.select_one('img')
                        # 이미지 링크가 있는 경우와 'data:'로 시작하지 않는 경우만 처리
                        if img_tag and 'src' in img_tag.attrs:
                            img_src = img_tag['src'].strip()
                            if not img_src.startswith('data:'):  # base64 이미지인지 확인
                                img_link = img_src  # 실제 이미지 링크만 추가

                        # 이미지 링크가 있든 없든 한 번만 리스트에 추가
                        news_link_list.append([full_link, img_link])

                else:
                    logging.warning(
                        f">>> {publisher} : 범위를 벗어난 기사가 발견되었습니다. 기사 작성 시간: {article_time}")
                    end_times[publisher] = time.time()
                    return
        if page >= max_pages:
            logging.info(f">>> {publisher} : {max_pages}개의 페이지 조회가 완료되었습니다.")
            break
        page += 1
    end_times[publisher] = time.time()


# '더보기'버튼으로 다음 페이지 넘어가는 크롤링 함수
def crawl_news_by_button(driver,
                         start_times, end_times,
                         start_date, end_date, url_set,
                         publisher, url_template,
                         ul_type, ul_class,
                         li_type, time_selector,
                         title_type, title_selector,
                         link_selector, link_prefix,
                         max_pages, article_count_by_hour,
                         news_link_list,
                         button_selector=None):
    start_times[publisher] = time.time()

    try:
        driver.set_page_load_timeout(10)
        driver.get(url_template)
        WebDriverWait(driver, 5).until(
            lambda d: d.execute_script('return document.readyState') == 'complete')
    except TimeoutException:
        logging.error(f"URL {url_template} 접속 시 타임아웃이 발생했습니다.")
        return False
    except Exception as e:
        logging.error(f"URL 접속할 때 오류가 발생하였습니다. {e}")
        return False

    page = 1
    while page <= max_pages:
        # 더보기 버튼을 클릭하는 부분을 먼저 처리
        try:
            more_button = driver.find_element(By.XPATH, button_selector)
            driver.execute_script("arguments[0].click();", more_button)
            time.sleep(2)
        except Exception as e:
            logging.warning(f">>> {publisher} : 더보기 버튼을 찾지 못했습니다: {e}")
            break

        page += 1

    # 모든 페이지가 로드된 후에 기사를 크롤링
    soup = BeautifulSoup(driver.page_source, 'html.parser')
    ul_tag = soup.find(ul_type, class_=ul_class)
    if not ul_tag:
        logging.warning(f">>> {publisher} : 더 이상 기사가 없습니다.")
    else:
        li_tags = ul_tag.find_all(li_type)
        for li in li_tags:
            title_tag = li.select_one(title_selector)
            skip_keywords = ["[숏폼]", "[표]"]

            if title_tag and any(
                    keyword in title_tag.get_text(strip=True) for keyword in skip_keywords):
                continue  # 제목에 skip_keywords 중 하나가 포함된 경우 건너뜀

            time_tag = li.select_one(time_selector)
            if time_tag:
                # SBS BIZ :: strong 태그 제거
                strong_tags = time_tag.find_all('strong')
                for strong in strong_tags:
                    strong.decompose()

                time_text = time_tag.get_text(strip=True)
                article_time = parse_relative_time(time_text)

                if start_date <= article_time:
                    if article_time > end_date:
                        logging.info(f"{end_date} 보다 이후에 작성된 기사입니다.")
                        continue

                    hour = article_time.strftime('%H')
                    article_count_by_hour[hour] += 1

                    link_tag = li.select_one(link_selector)
                    if link_tag and 'href' in link_tag.attrs:
                        full_link = link_prefix + link_tag['href']

                        if full_link in url_set:
                            continue

                        url_set.add(full_link)

                        img_link = None  # 기본적으로 이미지 링크를 None으로 설정
                        img_tag = li.select_one('img')
                        # 이미지 링크가 있는 경우와 'data:'로 시작하지 않는 경우만 처리
                        if img_tag and 'src' in img_tag.attrs:
                            img_src = img_tag['src'].strip()
                            if not img_src.startswith('data:'):  # base64 이미지인지 확인
                                img_link = img_src  # 실제 이미지 링크만 추가

                        news_link_list.append([full_link, img_link])
                else:
                    logging.warning(
                        f">>> {publisher} : 범위를 벗어난 기사가 발견되었습니다. 기사 작성 시간: {article_time}")
                    end_times[publisher] = time.time()
                    return
    end_times[publisher] = time.time()


def crawl_news_detail(driver, url: str,
                      difficulty_distribution: dict,
                      thumbnail: str,
                      category: int, publisher: str,
                      title_type: str, title_selector: str,
                      content_type: str, content_selector: str):
    try:
        driver.set_page_load_timeout(10)
        driver.get(url)
        WebDriverWait(driver, 5).until(
            lambda d: d.execute_script('return document.readyState') == 'complete')
    except TimeoutException:
        logging.error(f"URL {url} 접속 시 타임아웃이 발생했습니다.")
        return False
    except Exception as e:
        logging.error(f"URL 접속할 때 오류가 발생하였습니다. {e}")
        return False

    soup = BeautifulSoup(driver.page_source, 'html.parser')

    # 제목 추출
    if publisher == '세계 일보':
        title_tag = soup.find(title_type, id=title_selector)
    else:
        title_tag = soup.find(title_type, class_=title_selector)

    if title_tag:
        title = title_tag.get_text(strip=True)

    # 등록 및 수정 시간 추출
    if publisher == '한국 경제':
        register_time, update_time = extract_times(soup, 'datetime')
    elif publisher == '매일 경제':
        register_time, update_time = extract_times(soup, 'time_area')
    elif publisher == '연합 뉴스':
        register_time, update_time = extract_times(soup, 'update_time')
    elif publisher == 'SBS BIZ':
        register_time, update_time = extract_times(soup, 'ah_info')
    elif publisher == '세계 일보':
        register_time, update_time = extract_times(soup, 'viewInfo')
    elif publisher == '채널 A':
        register_time, update_time = extract_times(soup, 'channelA')
    elif publisher == '뉴시스':
        register_time, update_time = extract_times(soup, 'infoLine')
    elif publisher == '머니 투데이':
        register_time, update_time = extract_times(soup, 'moneyToday')
    elif publisher == 'YTN':
        register_time, update_time = extract_times(soup, 'ytn')
    else:
        register_time = update_time = None  # 정의되지 않은 경우 None 처리

    # 본문 추출
    if content_selector:  # selector가 있을 때
        content = soup.find(content_type, class_=content_selector)
    else:  # selector가 없을 때
        content = soup.find(content_type)  # content_type만으로 찾기
    if not content:
        logging.error(f">>> {url} : 본문이 없습니다.")
        return False  # 실패 반환

    content_text = content.get_text(strip=True)

    # content 길이 체크
    min_length = 500  # 최소 길이 설정
    if len(content_text) < min_length:
        logging.warning(f">>> {url} : 본문 길이가 {min_length} 이하입니다. 건너뜁니다.")
        return False  # 실패 반환

    article = {
        'link': url,
        'title': title,
        'created_at': register_time,
        'updated_at': update_time,
        'content': str(content),
        'thumbnail': thumbnail,
        'category': category,
        'publisher': publisher
    }

    if article['content']:
        article['difficulty'] = analyze_difficulty_of_news(article['content'],
                                                           difficulty_distribution)
    else:
        logging.error(f"Skipping analysis for URL: {article['link']} due to missing content.")
        return False  # 실패 반환

    # DB에 삽입
    insert_news_to_db(article)
    return True  # 성공 반환
