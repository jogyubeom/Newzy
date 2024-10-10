import logging

import pytz
from django.utils import timezone

from newzy.models import NewsSummary, News, Cluster, RecommendedNews
from .models import User  # News 모델이 정의된 위치에 따라 경로를 수정하세요


def get_news(news_id: int) -> dict:
    try:
        news = News.objects.get(pk=news_id)

        seoul_tz = pytz.timezone('Asia/Seoul')
        created_at_seoul = timezone.localtime(news.created_at, seoul_tz)
        updated_at_seoul = timezone.localtime(news.updated_at, seoul_tz)
        crawled_at_seoul = timezone.localtime(news.crawled_at, seoul_tz)

        news_dict = {
            'news_id': news.news_id,
            'link': news.link,
            'title': news.title,
            'content': news.content,
            'difficulty': news.difficulty,
            'category': news.category,
            'publisher': news.publisher,
            'created_at': created_at_seoul.strftime('%Y-%m-%d %H:%M:%S'),
            'updated_at': updated_at_seoul.strftime('%Y-%m-%d %H:%M:%S'),
            'crawled_at': crawled_at_seoul.strftime('%Y-%m-%d %H:%M:%S'),
            'hit': news.hit,
            'thumbnail': news.thumbnail,
        }
        return news_dict

    except News.DoesNotExist:
        logging.error(f"news_id: {news_id} 인 데이터가 존재하지 않습니다.")
        return None
    except Exception as e:
        logging.error(f"{e}")
        return None


def get_cluster(user_id: int) -> int:
    try:
        user = User.objects.get(pk=user_id)
        return user.cluster.cluster_id
    except User.DoesNotExist:
        logging.error(f"user_id: {user_id} 인 데이터가 존재하지 않습니다.")
        return None
    except Exception as e:
        logging.error(f"{e}")
        return None

def get_cluster_info(cluster_id: int) -> dict:
    try:
        cluster = Cluster.objects.get(pk=cluster_id)
        cluster_dict = {
            'cluster_id': cluster.cluster_id,
            'cluster_name': cluster.cluster_name,
            'age_group': cluster.age_group,
            'interest_category': cluster.interest_category,
            'page_stay_time': cluster.page_stay_time,
        }
        return cluster_dict
    except Cluster.DoesNotExist:
        logging.error(f"cluster_id: {cluster_id} 인 데이터가 존재하지 않습니다. ")
        return None
    except Exception as e:
        logging.error(f"{e}")
        return None

def get_cluster_count() -> int:
    return Cluster.objects.count()

def save_news_summary(news_id: int, cluster_id: int, summary: str) -> bool:
    try:
        news = News.objects.get(pk=news_id)
        cluster = Cluster.objects.get(pk=cluster_id)

        news_summary = NewsSummary(
            news=news,
            cluster=cluster,
            summary=summary,
        )
        news_summary.save()
        return True
    except News.DoesNotExist:
        print(f"뉴스 ID {news_id}에 해당하는 뉴스가 존재하지 않습니다.")
        return False
    except Cluster.DoesNotExist:
        print(f"클러스터 ID {cluster_id}에 해당하는 클러스터가 존재하지 않습니다.")
        return False
    except Exception as e:
        print(f"뉴스 요약 저장 중 오류 발생: {e}")
        return False

def save_recommended_news_by_cluster(cluster_id: int, news_id: int) -> bool:
    try:
        cluster = Cluster.objects.get(pk=cluster_id)
        news = News.objects.get(pk=news_id)
        recommended_news = RecommendedNews(
            cluster=cluster,
            news=news,
        )
        recommended_news.save()
        return True
    except News.DoesNotExist:
        print(f"뉴스 ID {news_id}에 해당하는 뉴스가 존재하지 않습니다.")
        return False
    except Cluster.DoesNotExist:
        print(f"클러스터 ID {cluster_id}에 해당하는 클러스터가 존재하지 않습니다.")
        return False
    except Exception as e:
        print(f"군집 별 추천 뉴스 저장 중 오류 발생: {e}")
        return False