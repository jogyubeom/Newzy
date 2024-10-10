import random

from django.core.management.base import BaseCommand
from faker import Faker

from newzy.models import Cluster, User


class Command(BaseCommand):
    help = '더미 유저를 생성하는 명령어'

    def handle(self, *args, **kwargs):
        fake = Faker()
        Faker.seed(0)  # To make results reproducible
        clusters = list(Cluster.objects.all())  # Fetch available clusters

        for _ in range(1000):
            # Creating a dummy user
            user = User(
                image_id=1,  # Keeping default image_id as 1
                nickname=fake.user_name(),
                email=fake.email(),
                password=fake.password(length=10),
                birth=fake.date_of_birth(minimum_age=14, maximum_age=80),
                info=fake.sentence(nb_words=10),
                exp=random.randint(0, 1000),
                economy_score=random.randint(0, 100),
                society_score=random.randint(0, 100),
                international_score=random.randint(0, 100),
                state=random.choice([0, 1, 2]),  # 기본, 탈퇴, 정지 상태
                cluster=random.choice(clusters) if clusters else None
            )

            # Saving user to the database
            user.save()