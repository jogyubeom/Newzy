import logging
import random

from newzy.utils.crawler import crawl_news, crawl_news_detail, crawl_news_by_button


def yna(driver, society_article_count_by_hour, society_yna_news_link_list,
        start_times, end_times, difficulty_distribution,
        start_date, end_date
        ):

    logging.info(f"연합뉴스: {start_date} - {end_date}")
    url_set = set()
    crawl_news(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="연합 뉴스",
        url_template="https://www.yna.co.kr/society/all/{page}",
        ul_type="div",
        ul_class="list-type038",
        li_type="li",
        time_selector='span.txt-time',
        title_type="strong",
        title_selector="tit-news",
        link_selector='a.tit-wrap',
        link_prefix='',
        max_pages=20,  # 고정
        article_count_by_hour=society_article_count_by_hour,
        news_link_list=society_yna_news_link_list
    )
    logging.info(f"연합 뉴스 개수: {len(society_yna_news_link_list)}")

    random.shuffle(society_yna_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in society_yna_news_link_list:
        if insert_count >= 10:
            logging.info(f"삽입된 기사의 개수가 10개가 되어 종료합니다.")
            break

        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=1,
            publisher="연합 뉴스",
            title_type='h1',
            title_selector='tit',
            content_type='article',
            content_selector='story-news article'
        )

        if success:
            insert_count += 1


def ytn(driver, society_article_count_by_hour, society_ytn_news_link_list,
        start_times, end_times, difficulty_distribution,
        start_date, end_date
        ):
    logging.info(f"YTN: {start_date} - {end_date}")
    url_set = set()
    crawl_news_by_button(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="YTN",
        url_template="https://www.ytn.co.kr/news/list.php?mcd=0103",
        ul_type="div",
        ul_class="news_list_wrap",
        li_type="div",
        time_selector='div.date',
        title_type="div",
        title_selector="title",
        link_selector='div.title a',
        link_prefix='',
        max_pages=7,  # 고정
        article_count_by_hour=society_article_count_by_hour,
        news_link_list=society_ytn_news_link_list,
        button_selector="//a[@class='btn_white_arr_down']"
    )
    logging.info(f"YTN 개수: {len(society_ytn_news_link_list)}")

    random.shuffle(society_ytn_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in society_ytn_news_link_list:
        if insert_count >= 10:
            logging.info(f"삽입된 기사의 개수가 10개가 되어 종료합니다.")
            break

        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=1,
            publisher="YTN",
            title_type='h2',
            title_selector='news_title',
            content_type='div',
            content_selector='content'
        )

        if success:
            insert_count += 1


def channelA(driver, society_article_count_by_hour, society_channelA_news_link_list,
             start_times, end_times, difficulty_distribution,
             start_date, end_date
             ):
    logging.info(f"채널 A: {start_date} - {end_date}")
    url_set = set()
    crawl_news_by_button(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="채널 A",
        url_template="https://www.ichannela.com/news/main/news_part.do?catecode=000404",
        ul_type="div",
        ul_class="newspart_article_list",
        li_type="li",
        time_selector='div.date',
        title_type="span",
        title_selector="title",
        link_selector='a.btn_movie_detail',
        link_prefix='https://www.ichannela.com',
        max_pages=20,
        article_count_by_hour=society_article_count_by_hour,
        news_link_list=society_channelA_news_link_list,
        button_selector='//*[@id="wrapper"]/section[2]/div/div[4]/div/button'
    )
    logging.info(f"채널 A 개수: {len(society_channelA_news_link_list)}")

    random.shuffle(society_channelA_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in society_channelA_news_link_list:
        if insert_count >= 10:
            logging.info(f"삽입된 기사의 개수가 10개가 되어 종료합니다.")
            break

        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=1,
            publisher="채널 A",
            title_type='strong',
            title_selector='title',
            content_type='div',
            content_selector='news_page_txt'
        )

        if success:
            insert_count += 1
