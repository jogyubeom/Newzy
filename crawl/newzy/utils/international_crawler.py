import logging
import random

from newzy.utils.crawler import crawl_news, crawl_news_detail, crawl_news_by_button


def newsis(driver, international_article_count_by_hour, international_newsis_news_link_list,
           start_times, end_times, difficulty_distribution,
           start_date, end_date
           ):
    logging.info(f"뉴시스: {start_date} - {end_date}")
    url_set = set()
    crawl_news(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="뉴시스",
        url_template="https://www.newsis.com/world/list/?cid=10100&scid=10101&page={page}",
        ul_type="ul",
        ul_class="articleList2",
        li_type="li",
        time_selector='p.time',
        title_type="p",
        title_selector="tit",
        link_selector='a',
        link_prefix='https://www.newsis.com',
        max_pages=10,  # 고정
        article_count_by_hour=international_article_count_by_hour,
        news_link_list=international_newsis_news_link_list
    )
    logging.info(f"뉴시스 개수: {len(international_newsis_news_link_list)}")

    random.shuffle(international_newsis_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in international_newsis_news_link_list:
        if insert_count >= 10:
            logging.info(f"삽입된 기사의 개수가 10개가 되어 종료합니다.")
            break
        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=2,
            publisher="뉴시스",
            title_type='h1',
            title_selector='tit title_area',
            content_type='article',
            content_selector=''
        )
        if success:
            insert_count += 1


def segye(driver, international_article_count_by_hour, international_segye_news_link_list,
          start_times, end_times, difficulty_distribution,
          start_date, end_date
          ):
    logging.info(f"세계 일보: {start_date} - {end_date}")
    url_set = set()
    crawl_news(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="세계 일보",
        url_template="https://segye.com/newsList/0101040100000?page={page}",
        ul_type="ul",
        ul_class="listBox",
        li_type="li",
        time_selector='small.date',
        title_type="strong",
        title_selector="tit",
        link_selector='a',
        link_prefix='https://segye.com',
        max_pages=50,
        article_count_by_hour=international_article_count_by_hour,
        news_link_list=international_segye_news_link_list
    )
    logging.info(f"세계 일보 개수: {len(international_segye_news_link_list)}")

    random.shuffle(international_segye_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in international_segye_news_link_list:
        if insert_count >= 10:
            logging.info(f"삽입된 기사의 개수가 10개가 되어 종료합니다.")
            break
        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=2,
            publisher="세계 일보",
            title_type='h3',
            title_selector='title_sns',
            content_type='article',
            content_selector='viewBox2'
        )
        if success:
            insert_count += 1


def sbsbiz(driver, international_article_count_by_hour, international_sbs_biz_news_link_list,
           start_times, end_times, difficulty_distribution,
           start_date, end_date
           ):
    logging.info(f"SBS BIZ: {start_date} - {end_date}")
    url_set = set()
    crawl_news_by_button(
        driver=driver,
        start_times=start_times,
        end_times=end_times,
        start_date=start_date,
        end_date=end_date,
        url_set=url_set,
        publisher="SBS BIZ",
        url_template="https://biz.sbs.co.kr/news/list.html?menu=j1_8",
        ul_type="ul",
        ul_class="news_article_content",
        li_type="li",
        time_selector='span.md_article_day',
        title_type='strong',
        title_selector='md_alc_title',
        link_selector='a.md_al_cont',
        link_prefix='',
        max_pages=10,
        article_count_by_hour=international_article_count_by_hour,
        news_link_list=international_sbs_biz_news_link_list,
        button_selector="//button[@id='cnbc-front-articleListContent-more']"
    )
    logging.info(f"SBS BIZ 개수: {len(international_sbs_biz_news_link_list)}")

    random.shuffle(international_sbs_biz_news_link_list)  # 랜덤 샘플링
    insert_count = 0

    for news in international_sbs_biz_news_link_list:
        if insert_count >= 10:
            logging.info(f"삽입된 기사의 개수가 10개가 되어 종료합니다.")
            break
        success = crawl_news_detail(
            driver=driver,
            difficulty_distribution=difficulty_distribution,
            url=news[0],
            thumbnail=news[1],
            category=2,
            publisher="SBS BIZ",
            title_type='h3',
            title_selector='ah_big_title',
            content_type='div',
            content_selector='ab_text'
        )
        if success:
            insert_count += 1
