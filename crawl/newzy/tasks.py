import logging
import os
from datetime import datetime

import psutil

from newzy.config import create_webdriver
from newzy.utils.economy_crawler import hankyung, mk, mt
from newzy.utils.international_crawler import newsis, segye, sbsbiz
from newzy.utils.society_crawler import yna, ytn, channelA

logging = logging.getLogger('my_logger')


def economy(driver, start_times, end_times, difficulty_distribution, start_date, end_date):
    logging.info(f"경제 카테고리 기사 크롤링 시작 - start_date: {start_date}, end_date: {end_date}")

    economy_article_count_by_hour = {f'{hour:02d}': 0 for hour in range(24)}
    economy_hankyung_news_link_list = []  # 1. 한국 경제
    economy_mk_news_link_list = []  # 2. 매일 경제
    economy_mt_news_link_list = []  # 3. 머니 투데이

    hankyung(driver, economy_article_count_by_hour, economy_hankyung_news_link_list, start_times,
             end_times, difficulty_distribution, start_date, end_date)
    mk(driver, economy_article_count_by_hour, economy_mk_news_link_list, start_times, end_times,
       difficulty_distribution, start_date, end_date)
    mt(driver, economy_article_count_by_hour, economy_mt_news_link_list, start_times, end_times,
       difficulty_distribution, start_date, end_date)

    return


def society(driver, start_times, end_times, difficulty_distribution, start_date, end_date):
    logging.info(f"사회 카테고리 기사 크롤링 시작 - start_date: {start_date}, end_date: {end_date}")

    society_article_count_by_hour = {f'{hour:02d}': 0 for hour in range(24)}
    society_yna_news_link_list = []  # 1. 연합 뉴스
    society_ytn_news_link_list = []  # 2. YTN
    society_channelA_news_link_list = []  # 3. 채널 A

    yna(driver, society_article_count_by_hour, society_yna_news_link_list, start_times, end_times,
        difficulty_distribution, start_date, end_date)
    ytn(driver, society_article_count_by_hour, society_ytn_news_link_list, start_times, end_times,
        difficulty_distribution, start_date, end_date)
    channelA(driver, society_article_count_by_hour, society_channelA_news_link_list, start_times,
             end_times, difficulty_distribution, start_date, end_date)

    return


def international(driver, start_times, end_times, difficulty_distribution, start_date, end_date):
    logging.info(f"세계 카테고리 기사 크롤링 시작 - start_date: {start_date}, end_date: {end_date}")

    international_article_count_by_hour = {f'{hour:02d}': 0 for hour in range(24)}
    international_newsis_news_link_list = []  # 1. 뉴시스
    international_segye_news_link_list = []  # 2. 세계일보
    international_sbs_biz_news_link_list = []  # 3. SBS Biz

    newsis(driver, international_article_count_by_hour, international_newsis_news_link_list,
           start_times, end_times,
           difficulty_distribution, start_date, end_date)
    segye(driver, international_article_count_by_hour, international_segye_news_link_list,
          start_times, end_times,
          difficulty_distribution, start_date, end_date)
    sbsbiz(driver, international_article_count_by_hour, international_sbs_biz_news_link_list,
           start_times, end_times,
           difficulty_distribution, start_date, end_date)

    return


def kill_driver_processes():
    # 현재 프로세스의 PID를 구함 (run_crawl의 프로세스 ID)
    current_pid = os.getpid()

    # 현재 프로세스에서 실행 중인 모든 하위 프로세스를 검색
    for proc in psutil.process_iter(['pid', 'name', 'ppid']):
        try:
            # 프로세스 이름이 'chrome' 또는 'chromedriver'인지 확인하고
            # 부모 프로세스 ID(ppid)가 current_pid와 일치하는 경우만 종료
            if (proc.info['ppid'] == current_pid) and (
                    'chromedriver' in proc.info['name'] or 'chrome' in proc.info['name']):
                proc.kill()
                print(f"Killed process {proc.info['name']} with PID {proc.info['pid']}")
        except (psutil.NoSuchProcess, psutil.AccessDenied, psutil.ZombieProcess):
            pass


def run_crawl(start_date, end_date):
    start_times = {}
    end_times = {}
    difficulty_distribution = {}

    driver = create_webdriver()  # 드라이버를 한 번만 생성

    try:
        # 여러 메서드에서 같은 driver를 공유
        economy(driver, start_times, end_times, difficulty_distribution, start_date, end_date)
        society(driver, start_times, end_times, difficulty_distribution, start_date, end_date)
        international(driver, start_times, end_times, difficulty_distribution, start_date, end_date)
    finally:
        # 모든 작업이 끝난 후에 드라이버 종료
        driver.quit()
        kill_driver_processes()

        # 각 프로세스의 시작과 종료 시간 출력 및 총 소요 시간 계산
    for process_name in start_times.keys():
        start_time = start_times.get(process_name)
        end_time = end_times.get(process_name, None)

        if end_time:
            elapsed_time = end_time - start_time
            start_time_fmt = datetime.fromtimestamp(start_time).strftime('%Y-%m-%d %H:%M:%S')
            end_time_fmt = datetime.fromtimestamp(end_time).strftime('%Y-%m-%d %H:%M:%S')
            elapsed_time_fmt = round(elapsed_time, 2)

            # 결과 출력
            logging.info(f"{process_name} 시작 시간: {start_time_fmt}")
            logging.info(f"{process_name} 종료 시간: {end_time_fmt}")
            logging.info(f"{process_name} 총 소요 시간: {elapsed_time_fmt} 초")
            logging.info("-" * 40)  # 구분선 추가
        else:
            logging.warning(f"{process_name} 종료 시간을 기록하지 못했습니다.")
            logging.info("-" * 40)  # 구분선 추가

# TODO [강윤서] Multiprocessing
# multiprocessing.set_start_method('fork')
#
# # Manager 객체를 사용하여 프로세스 간 공유 변수를 생성
# with multiprocessing.Manager() as manager:
#     start_times = manager.dict()
#     end_times = manager.dict()
#     difficulty_distribution = manager.dict()

#     start_date = datetime(2024, 9, 21, 0, 0)
#     end_date = datetime.now()
#
#     process1 = multiprocessing.Process(target=economy, args=(
#         start_times, end_times, difficulty_distribution, start_date, end_date))
#     process2 = multiprocessing.Process(target=society, args=(
#         start_times, end_times, difficulty_distribution, start_date, end_date))
#     process3 = multiprocessing.Process(target=international, args=(
#         start_times, end_times, difficulty_distribution, start_date, end_date))
#
#     # 프로세스 시작
#     process1.start()
#     process2.start()
#     process3.start()
#
#     # 프로세스 종료 대기
#     process1.join()
#     process2.join()
#     process3.join()
#
