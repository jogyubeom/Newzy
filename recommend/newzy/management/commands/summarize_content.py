from django.core.management.base import BaseCommand

from newzy.summary.summarize_content import summarize_news

class Command(BaseCommand):
    help = '군집별로 뉴스를 요약하는 명령어'

    def handle(self, *args, **kwargs):
        summarize_news(63)
