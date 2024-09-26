import logging
import os
from datetime import datetime, timedelta

import pytz
from apscheduler.schedulers.background import BackgroundScheduler
from django.utils import timezone
from django_apscheduler.jobstores import DjangoJobStore

from newzy.tasks import run_crawl

# Define constants for clarity
TIME_ZONES = 'Asia/Seoul'
BATCH_TIMES = [0, 4, 8, 10, 12, 14, 16, 18, 20]


def start_scheduler():
    if os.environ.get('RUN_MAIN') != 'true':  # Prevent Django double execution
        scheduler = BackgroundScheduler()
        scheduler.add_jobstore(DjangoJobStore(), "default")

        # Register tasks if not already added
        for time in BATCH_TIMES:
            job_id = f"batch_task_{time}"
            if not scheduler.get_job(job_id):  # Check if job exists
                scheduler.add_job(schedule_batch_task, 'cron', hour=time, minute=0, id=job_id,
                                  replace_existing=True)

        # TEST
        # job_id_test = "test_task_15_24"
        # if not scheduler.get_job(job_id_test):
        #     scheduler.add_job(schedule_batch_task, 'cron', hour=15, minute=24, id=job_id_test,
        #                       replace_existing=True)

        scheduler.start()


def schedule_batch_task():
    seoul_tz = pytz.timezone(TIME_ZONES)
    now = timezone.localtime(datetime.now(seoul_tz))

    # Set end_date as the current time when the batch runs
    end_date = now.replace(minute=0, second=0, microsecond=0)

    # Find the index of end_date in BATCH_TIMES
    end_index = BATCH_TIMES.index(end_date.hour)

    # Set start_date based on the previous time in BATCH_TIMES or previous day's 20:00
    if end_index == 0:
        start_date = (end_date - timedelta(days=1)).replace(hour=20)
    else:
        start_date = end_date.replace(hour=BATCH_TIMES[end_index - 1])

    # start_date = end_date.replace(hour=13, minute=0, second=0, microsecond=0)
    logging.info(
        f"########### Batch task started. Start date: {start_date}, End date: {end_date} ###########")

    run_crawl(start_date, end_date)
