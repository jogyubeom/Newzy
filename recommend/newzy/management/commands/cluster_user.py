from django.core.management.base import BaseCommand

from newzy.recommend.cluster_user import cluster_user


class Command(BaseCommand):
    help = '유저를 군집화하는 명령어'

    def handle(self, *args, **kwargs):
        cluster_user()
