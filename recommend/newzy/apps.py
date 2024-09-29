from django.apps import AppConfig


class NewzyConfig(AppConfig):
    default_auto_field = 'django.db.models.BigAutoField'
    name = 'newzy'
    def ready(self):
        from newzy.schedule import start_scheduler
        start_scheduler()  # 앱이 준비된 후에 스케줄러 시작
