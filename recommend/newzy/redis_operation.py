import json
import logging
from datetime import datetime, timedelta

import pytz
from django.core.cache import cache
from django.utils import timezone

from newzy.db_operations import get_news

logging = logging.getLogger('my_logger')

def get_redis_key(cluster_id: int):
    # 현재 시간을 서울 시간대로 가져옴
    seoul_tz = pytz.timezone('Asia/Seoul')
    current_time = timezone.localtime(datetime.now(seoul_tz))
    # 다음날 추천 결과 저장을 위한 key
    next_day = current_time.date() + timedelta(days=1)
    # Redis 키 생성 (날짜와 cluster_id 포함)
    date_str = next_day.strftime('%Y-%m-%d')
    key = f"recommended_news:{date_str}:cluster_{cluster_id}"

    return key
def save_recommended_news_list_to_redis(cache_key, recommended_news_list):
    cache.set(cache_key, {'list': recommended_news_list})

def save_news_info_to_redis(news_id: int, cluster_id: int, summary: str):
    news = get_news(news_id)
    if news is None:
        logging.error(f"news_id: {news_id}에 해당하는 뉴스 정보를 찾을 수 없습니다.")
        return

    # Redis에 저장할 데이터 구성
    cache_key = f"news_info:{news_id}:cluster_{cluster_id}"
    news_info = {
        'news_id': news['news_id'],
        'link': news['link'],
        'summary': summary,
        'thumbnail': news['thumbnail'],
    }

    # Redis에 JSON 형식으로 저장
    try:
        cache.set(cache_key, news_info)
        logging.info(f"뉴스 정보가 Redis에 저장되었습니다: {cache_key}")
    except Exception as e:
        logging.error(f"뉴스 정보를 Redis에 저장하는 중 오류 발생: {e}")

def save_quiz_to_redis(news_id: int, quiz_data):
    try:
        quiz_key = f"quiz:{news_id}"
        cache.set(quiz_key, quiz_data.dict())
        logging.info(f"퀴즈 데이터가 Redis에 저장되었습니다: {quiz_key}")
    except Exception as e:
        logging.error(f"퀴즈 데이터를 Redis에 저장하는 중 오류 발생: {e}")

