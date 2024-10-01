import logging
import os

from apscheduler.schedulers.background import BackgroundScheduler
from django_apscheduler.jobstores import DjangoJobStore

from newzy.recommend.recommend_news import recommend_news

TIME_ZONES = 'Asia/Seoul'

logging = logging.getLogger('my_logger')


def start_scheduler():
    if os.environ.get('RUN_MAIN') != 'true':  # Prevent Django double execution
        scheduler = BackgroundScheduler()
        scheduler.add_jobstore(DjangoJobStore(), "default")

        time = 23  # 매일 23시마다 군집 별 추천 알고리즘 적용 -> 캐시에 저장
        job_id = f"recommend_batch_task_{time}_daily"
        scheduler.add_job(schedule_batch_task, 'cron', hour=time, minute=0, id=job_id,
                          replace_existing=True)
        scheduler.start()


def schedule_batch_task():
    logging.info(f"########### Batch task started.  ###########")
    recommend_news()
