import logging

import pytz
from django.utils import timezone

from newzy.models import News


def insert_news_to_db(article: dict):
    news = News(
        link=article['link'],
        title=article['title'],
        content=article['content'],
        content_text=article['content_text'],
        difficulty=article['difficulty'],
        created_at=article['created_at'],
        updated_at=article['updated_at'],
        category=article['category'],
        publisher=article['publisher'],
        thumbnail=article['thumbnail']
    )
    # logging.info(article)
    try:
        news.save()
    except Exception as e:
        logging.error(e)


def update_difficulty_in_db(link: str, difficulty: int):
    try:
        news = News.objects.get(link=link)
        news.difficulty = difficulty
        news.save()
    except News.DoesNotExist:
        logging.error(f"{link} 뉴스가 존재하지 않습니다.")


def check_timezone_in_db():
    try:
        news_list = News.objects.filter(news_id__gte=1300)
        seoul_tz = pytz.timezone('Asia/Seoul')

        for news in news_list:
            created_at_seoul = timezone.localtime(news.created_at, seoul_tz)
            updated_at_seoul = timezone.localtime(news.updated_at, seoul_tz)
            crawled_at_seoul = timezone.localtime(news.crawled_at, seoul_tz)

            logging.info(
                f"{news.news_id}, {created_at_seoul}, {updated_at_seoul}, {crawled_at_seoul}")
    except Exception as e:
        logging.error(e)
