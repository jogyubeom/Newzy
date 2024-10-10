from django.http import JsonResponse

from newzy.recommend.recommend_news import recommend_news, logging

# Create your views here.

"""
유저의 추천 기사 목록 조회 요청
-> 유저가 속한 군집 확인
-> 군집에 대한 추천 알고리즘 적용
-> 10개 조회
"""
def recommend_news_view(request, user_id):
    logging.info(f"{user_id}에 대한 추천 뉴스 목록 조회~~")
    news = recommend_news(user_id)
    return JsonResponse(list(news), safe=False)
