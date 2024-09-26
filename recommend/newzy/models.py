from django.db import models


class Cluster(models.Model):
    cluster_id = models.AutoField(primary_key=True)
    cluster_name = models.CharField(max_length=30)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True, null=True, blank=True)

    class Meta:
        managed = False
        db_table = 'cluster'

    def __str__(self):
        return self.cluster_name


class User(models.Model):
    user_id = models.BigAutoField(primary_key=True)
    image_id = models.BigIntegerField(default=1)
    nickname = models.CharField(max_length=20, null=True, blank=True)
    email = models.CharField(max_length=100, null=True, blank=True)
    password = models.CharField(max_length=255, null=True, blank=True)
    birth = models.DateField(null=True, blank=True)
    info = models.CharField(max_length=300, null=True, blank=True)
    exp = models.IntegerField(null=True, blank=True)
    economy_score = models.PositiveSmallIntegerField(null=True, blank=True)
    society_score = models.PositiveSmallIntegerField(null=True, blank=True)
    international_score = models.PositiveSmallIntegerField(null=True, blank=True)
    state = models.PositiveSmallIntegerField(default=0)  # 기본, 탈퇴, 정지 상태
    cluster = models.ForeignKey(Cluster, on_delete=models.SET_NULL, null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True, null=True, blank=True)

    class Meta:
        managed = False
        db_table = 'user'

    def __str__(self):
        return self.nickname


class News(models.Model):
    news_id = models.BigAutoField(primary_key=True)
    link = models.URLField(max_length=255, unique=True)  # VARCHAR(255), 링크는 URLField로 변경 가능
    title = models.CharField(max_length=255)  # VARCHAR(255)
    content = models.TextField()  # TEXT
    difficulty = models.PositiveSmallIntegerField()  # TINYINT
    category = models.PositiveSmallIntegerField()  # TINYINT
    publisher = models.CharField(max_length=100)  # VARCHAR(100)
    created_at = models.DateTimeField(null=True, blank=True)  # TIMESTAMP, NULL 허용
    updated_at = models.DateTimeField(null=True, blank=True)  # TIMESTAMP, NULL 허용
    crawled_at = models.DateTimeField(auto_now_add=True)  # TIMESTAMP, DEFAULT CURRENT_TIMESTAMP
    hit = models.IntegerField(default=0)  # INT, 기본값 0
    thumbnail = models.URLField(max_length=255, null=True, blank=True)  # VARCHAR(255)

    class Meta:
        managed = False
        db_table = 'news'

    def __str__(self):
        return self.link
