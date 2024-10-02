from django.core.management.base import BaseCommand

from newzy.recommend.recommend_news import recommend_news


class Command(BaseCommand):
    help = '유저에게 뉴스를 추천하는 명령어 (by. 유저의 군집)'

    def handle(self, *args, **kwargs):
        recommend_news(1)
