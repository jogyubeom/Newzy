from django.core.management.base import BaseCommand

from newzy.create_quiz.create_quiz import create_quiz_from_news


class Command(BaseCommand):
    help = '뉴스에서 퀴즈를 생성하는 명령어'

    def handle(self, *args, **kwargs):
        create_quiz_from_news(1)
