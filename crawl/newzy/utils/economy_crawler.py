import logging
import random

from newzy.utils.crawler import crawl_news, crawl_news_detail, crawl_news_by_button

logging = logging.getLogger('my_logger')


def hankyung(driver, economy_article_count_by_hour, economy_hankyung_news_link_list,
             start_times, end_times, difficulty_distribution,
             start_date, end_date
             ):
    logging.info(f"한국 경제: {start_date} - {end_date}")
    url_set = set()
    crawl_news(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="한국 경제",
        url_template="https://www.hankyung.com/economy?page={page}",
        ul_type="ul",
        ul_class="news-list",
        li_type="li",
        time_selector='span.txt-date',
        title_type="h3",
        title_selector="news-tit",
        link_selector='a',
        link_prefix='',
        max_pages=50,
        article_count_by_hour=economy_article_count_by_hour,
        news_link_list=economy_hankyung_news_link_list
    )
    logging.info(f"한국 경제 개수: {len(economy_hankyung_news_link_list)}")

    random.shuffle(economy_hankyung_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in economy_hankyung_news_link_list:
        if insert_count >= 5:
            logging.info(f"삽입된 기사의 개수가 5개가 되어 종료합니다.")
            break
        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=0,
            publisher="한국 경제",
            title_type='h1',
            title_selector='headline',
            content_type='div',
            content_selector='article-body-wrap'
        )

        if success:
            insert_count += 1


def mk(driver, economy_article_count_by_hour, economy_mk_news_link_list,
       start_times, end_times, difficulty_distribution,
       start_date, end_date
       ):
    logging.info(f"매일 경제: {start_date} - {end_date}")
    url_set = set()
    crawl_news_by_button(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="매일 경제",
        url_template="https://www.mk.co.kr/news/economy/",
        ul_type="ul",
        ul_class="latest_news_list",
        li_type="li",
        time_selector='p.time_info',
        title_type="h3",
        title_selector="news_ttl",
        link_selector='a.news_item',
        link_prefix='',
        max_pages=10,
        article_count_by_hour=economy_article_count_by_hour,
        news_link_list=economy_mk_news_link_list,
        button_selector='//*[@id="container"]/section/div[2]/div/div/div[1]/section[2]/div/div/div/button'
    )
    logging.info(f"매일 경제 개수: {len(economy_mk_news_link_list)}")

    random.shuffle(economy_mk_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in economy_mk_news_link_list:
        if insert_count >= 5:
            logging.info(f"삽입된 기사의 개수가 5개가 되어 종료합니다.")
            break
        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=0,
            publisher="매일 경제",
            title_type='h2',
            title_selector='news_ttl',
            content_type='div',
            content_selector='news_cnt_detail_wrap'
        )

        if success:
            insert_count += 1


def mt(driver, economy_article_count_by_hour, economy_mt_news_link_list,
       start_times, end_times, difficulty_distribution,
       start_date, end_date
       ):
    logging.info(f"머니 투데이: {start_date} - {end_date}")
    url_set = set()
    crawl_news(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="머니 투데이",
        url_template="https://news.mt.co.kr/newsList.html?type=1&comd=&pDepth=news&pDepth1=politics&pDepth2=Ptotal&page={page}",
        ul_type="ul",
        ul_class="conlist_p1",
        li_type="li",
        time_selector='span.etc',
        title_type="strong",
        title_selector="subject",
        link_selector='a',
        link_prefix='',
        max_pages=50,
        article_count_by_hour=economy_article_count_by_hour,
        news_link_list=economy_mt_news_link_list
    )
    logging.info(f"머니 투데이 개수: {len(economy_mt_news_link_list)}")

    random.shuffle(economy_mt_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in economy_mt_news_link_list:
        if insert_count >= 10:
            logging.info(f"삽입된 기사의 개수가 10개가 되어 종료합니다.")
            break
        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=0,
            publisher="머니 투데이",
            title_type='h1',
            title_selector='subject',
            content_type='div',
            content_selector='article_view'
        )

        if success:
            insert_count += 1
