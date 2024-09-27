import logging
import os
from datetime import datetime

import pytz
from apscheduler.schedulers.background import BackgroundScheduler
from django.utils import timezone
from django_apscheduler.jobstores import DjangoJobStore

from newzy.tasks import run_crawl

# Define constants for clarity
TIME_ZONES = 'Asia/Seoul'
# BATCH_TIMES = [0, 4, 8, 10, 12, 14, 16, 18, 20]

BATCH_TIMES = [11, 12, 13, 14, 15, 16, 17, 18, 19]


def start_scheduler():
    if os.environ.get('RUN_MAIN') != 'true':  # Prevent Django double execution
        scheduler = BackgroundScheduler()
        scheduler.add_jobstore(DjangoJobStore(), "default")

        # Register tasks if not already added
        for time in BATCH_TIMES:
            job_id = f"batch_task_{time}"
            if not scheduler.get_job(job_id):  # Check if job exists
                scheduler.add_job(schedule_batch_task, 'cron',
                                  hour=time, minute=0, id=job_id,
                                  replace_existing=True)

        scheduler.start()


def schedule_batch_task():
    seoul_tz = pytz.timezone(TIME_ZONES)
    now = timezone.localtime(datetime.now(seoul_tz))

    end_date = now.replace(minute=0, second=0, microsecond=0)

    end_index = BATCH_TIMES.index(end_date.hour)

    start_date = end_date.replace(hour=BATCH_TIMES[end_index - 1])

    # if end_index == 0:
    #     start_date = (end_date - timedelta(days=1)).replace(hour=20)
    # else:
    #     start_date = end_date.replace(hour=BATCH_TIMES[end_index - 1])

    logging.info(
        f"########### Batch task started. Start date: {start_date}, End date: {end_date} ###########")

    run_crawl(start_date, end_date)
