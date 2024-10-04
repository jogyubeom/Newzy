import json
import logging

import numpy as np
from django.core.cache import cache

from newzy.db_operations import save_recommended_news_by_cluster, \
    get_cluster_count, get_cluster_info
from newzy.create_quiz.create_quiz import create_quiz_from_news
from newzy.models import User
from newzy.recommend.cluster_user import cluster_user
from newzy.recommend.ncf_recommender import NCFRecommender
from newzy.redis_operation import get_redis_key, save_news_info_to_redis, \
    save_recommended_news_list_to_redis
from newzy.summary.summarize_content import summarize_news

logging = logging.getLogger('my_logger')


def find_central_user_in_cluster(cluster_id: int):
    logging.info(f"군집의 중앙 유저 찾기: cluster_id={cluster_id}")

    # 해당 클러스터의 모든 유저 가져오기
    cluster_users = User.objects.filter(cluster_id=cluster_id)
    if cluster_users.exists():
        # 유저의 주요 속성 (예: 경제/사회/세계 어휘력 점수) 정보를 리스트로 추출
        user_attributes = [
            [user.economy_score, user.society_score, user.international_score]
            for user in cluster_users
        ]

        # 중앙값을 계산하여 중앙 유저 선택
        user_attributes_np = np.array(user_attributes)
        median_attributes = np.median(user_attributes_np, axis=0)

        # 중앙값에 가장 가까운 유저 찾기
        closest_user = None
        min_distance = float('inf')
        for user, attributes in zip(cluster_users, user_attributes_np):
            distance = np.linalg.norm(attributes - median_attributes)
            if distance < min_distance:
                min_distance = distance
                closest_user = user

        if closest_user:
            return closest_user
        else:
            logging.warning(f"해당 군집(cluster_id: {cluster_id})에 중앙 유저를 찾을 수 없습니다.")
            return None
    else:
        logging.error(f"해당 군집(cluster_id: {cluster_id}) 에 속하는 유저가 없습니다.")


# 군집별 추천 알고리즘 적용
def recommend_news_by_cluster(cluster_id: int):
    logging.info(f"군집별 뉴스 추천 시작: cluster_id={cluster_id}")
    # redis key 세팅
    cache_key = get_redis_key(cluster_id)
    # 캐시에 추천 결과가 이미 있는지 확인
    cached_data = cache.get(cache_key)
    if cached_data:
        logging.info(f"Redis에서 캐시된 추천 결과 사용: cluster_id={cluster_id}")
        recommended_news_list = json.loads(cached_data)['list']
        logging.info(recommended_news_list)
        # for idx, recommended_news in enumerate(recommended_news_list):
        #     summary = summarize_news(cluster_id=cluster_id, news_id=recommended_news)
        #     # 뉴스 정보 레디스에 저장
        #     save_news_info_to_redis(recommended_news, cluster_id, summary)
        #     if idx < 7:  # 상위 10개는 데일리 기사 후보 -> 데일리 퀴즈 생성
        #         create_quiz_from_news(recommended_news)
        return recommended_news_list

    # 추천 모델
    recommender = NCFRecommender(cluster_id=cluster_id)
    # 군집의 중앙값
    mid_user = find_central_user_in_cluster(cluster_id=cluster_id)

    if mid_user:
        recommended_news_list = recommender.recommend(mid_user.user_id)
        logging.info(f"군집 {cluster_id}의 중앙값 {mid_user.user_id} 에 대한 추천 완료")
        # 추천 결과 cache 에 저장
        if recommended_news_list:
            save_recommended_news_list_to_redis(cache_key, recommended_news_list)

        for idx, recommended_news in enumerate(recommended_news_list):
            # 추천 결과 table 에 저장 (만약을 대비하여)
            save_recommended_news_by_cluster(cluster_id=cluster_id, news_id=recommended_news)
            # 추천 뉴스에 대한 요약 생성
            summary = summarize_news(cluster_id=cluster_id, news_id=recommended_news)
            # 뉴스 정보 레디스에 저장
            save_news_info_to_redis(recommended_news, cluster_id, summary)
            if idx < 7:  # 상위 10개는 데일리 기사 후보 -> 데일리 퀴즈 생성
                create_quiz_from_news(recommended_news)
        return recommended_news_list
    else:
        return None


# 군집별 추천 알고리즘 적용 - batch task
def recommend_news():
    # 유저 군집화 먼저
    cluster_user()

    cluster_cnt = get_cluster_count()
    logging.info(f"군집별 뉴스 추천 알고리즘 시작: 군집 개수: {cluster_cnt}")

    for idx in range(1, cluster_cnt + 1):
        cluster = get_cluster_info(idx)  # pk 의 값이 id인 cluster가 있는지 조회
        if cluster:
            recommend_news_by_cluster(cluster_id=cluster['cluster_id'])
        else:
            logging.error(f"cluster_id: {idx} 와 일치하는 Cluster가 없어 건너뜁니다.")
            continue
