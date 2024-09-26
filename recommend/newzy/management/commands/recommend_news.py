from django.core.management.base import BaseCommand

from newzy.ncf_recommender import recommend_news_by_cluster


class Command(BaseCommand):
    help = '군집별로 뉴스를 추천하는 명령어'

    def handle(self, *args, **kwargs):
        recommend_news_by_cluster(1)
