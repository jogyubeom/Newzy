from django.core.management.base import BaseCommand
from newzy.tasks import run_crawl
from datetime import datetime
from django.utils import timezone  # 타임존 처리를 위해 추가


class Command(BaseCommand):
    help = '뉴스를 크롤링하여 저장하는 명령어'

    def add_arguments(self, parser):
        # 날짜 인자 추가 (옵션으로 받음)
        parser.add_argument(
            '--start_date',
            type=str,
            help='크롤링 시작 날짜 (YYYY-MM-DD HH:MM:SS 형식)',
            required=False
        )
        parser.add_argument(
            '--end_date',
            type=str,
            help='크롤링 종료 날짜 (YYYY-MM-DD HH:MM:SS 형식)',
            required=False
        )

    def handle(self, *args, **kwargs):
        # 인자로 받은 start_date와 end_date를 처리
        start_date_str = kwargs.get('start_date')
        end_date_str = kwargs.get('end_date')

        # 문자열을 datetime 객체로 변환
        if start_date_str:
            start_date = datetime.strptime(start_date_str, '%Y-%m-%d %H:%M:%S')
            # naive datetime을 aware로 변환
            start_date = timezone.make_aware(start_date, timezone.get_default_timezone())
        else:
            start_date = None

        if end_date_str:
            end_date = datetime.strptime(end_date_str, '%Y-%m-%d %H:%M:%S')
            # naive datetime을 aware로 변환
            end_date = timezone.make_aware(end_date, timezone.get_default_timezone())
        else:
            end_date = None

        # run_crawl 함수에 start_date와 end_date 전달
        run_crawl(start_date=start_date, end_date=end_date)
