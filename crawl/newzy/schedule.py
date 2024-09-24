import logging
import os
from datetime import datetime, timedelta

import pytz
from apscheduler.schedulers.background import BackgroundScheduler
from django.utils import timezone
from django_apscheduler.jobstores import DjangoJobStore

from newzy.tasks import run_crawl

times = [0, 4, 8, 10, 12, 14, 16, 18, 20]


def start_scheduler():
    if os.environ.get('RUN_MAIN') != 'true':  # Django의 더블 실행 방지
        scheduler = BackgroundScheduler()
        scheduler.add_jobstore(DjangoJobStore(), "default")

        # 시간에 맞춰 작업을 예약
        for time in times:
            scheduler.add_job(schedule_batch_task, 'cron', hour=time, minute=0)

        # TEST
        scheduler.add_job(schedule_batch_task, 'cron', hour=14, minute=35)
        scheduler.start()


def schedule_batch_task():
    seoul_tz = pytz.timezone('Asia/Seoul')
    now = timezone.localtime(datetime.now(seoul_tz))

    # end_date는 현재 시간 (배치가 실행되는 순간)
    end_date = now.replace(minute=00, second=0, microsecond=0)

    # 현재 end_date가 times 리스트에서 몇 번째 인덱스인지 찾음
    end_index = times.index(end_date.hour)

    # start_date는 end_index의 이전 값, 만약 end_index가 0이면 전날 20시로 설정
    if end_index == 0:
        start_date = (end_date - timedelta(days=1)).replace(hour=20)
    else:
        start_date = end_date.replace(hour=times[end_index - 1])

    # TEST
    # start_date = (end_date - timedelta(days=1)).replace(hour=16)
    start_date = end_date.replace(hour=13)
    
    logging.info(
        f"########### Batch task started. Start date: {start_date}, End date: {end_date} ###########")

    run_crawl(start_date, end_date)
