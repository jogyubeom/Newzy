from django.core.management.base import BaseCommand
from newzy.tasks import run_crawl

class Command(BaseCommand):
    help = '뉴스를 크롤링하여 저장하는 명령어'

    def handle(self, *args, **kwargs):
        run_crawl()  # 크롤링 작업 실행
