from django.db import models


class Cluster(models.Model):
    cluster_id = models.AutoField(primary_key=True)
    cluster_name = models.CharField(max_length=30)
    # 속성
    age_group = models.CharField(max_length=20, help_text="연령대 예: 젊은 층, 중장년층, 노년층")
    interest_category = models.CharField(max_length=50, help_text="관심 있는 카테고리 예: 경제, 사회, 세계")
    page_stay_time = models.IntegerField(default=0, help_text="페이지 체류 시간 (초 단위)")

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

class NewsSummary(models.Model):
    news_summary_id = models.BigAutoField(primary_key=True)
    news = models.ForeignKey(News, on_delete=models.SET_NULL, null=True, blank=True)
    cluster = models.ForeignKey(Cluster, on_delete=models.SET_NULL, null=True, blank=True)
    summary = models.TextField()
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True, null=True, blank=True)

    class Meta:
        managed = False
        db_table = 'news_summary'
    def __str__(self):
        return self.summary

class RecommendedNews(models.Model):
    recommended_news_id = models.BigAutoField(primary_key=True)
    cluster = models.ForeignKey(Cluster, on_delete=models.SET_NULL, null=True, blank=True)
    news = models.ForeignKey(News, on_delete=models.SET_NULL, null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True, null=True, blank=True)

    class Meta:
        managed = False
        db_table = 'recommended_news'

class DailyQuiz(models.Model):
    daily_quiz_id = models.BigAutoField(primary_key=True)
    news = models.ForeignKey(News, on_delete=models.SET_NULL, null=True, blank=True)
    question = models.CharField(max_length=300, null=True, blank=True)
    option1 = models.CharField(max_length=100, null=True, blank=True)
    option2 = models.CharField(max_length=100, null=True, blank=True)
    option3 = models.CharField(max_length=100, null=True, blank=True)
    option4 = models.CharField(max_length=100, null=True, blank=True)
    answer = models.IntegerField(null=True, blank=True)
    created_at = models.DateTimeField(auto_now_add=True)
    updated_at = models.DateTimeField(auto_now=True, null=True, blank=True)
    class Meta:
        managed = False
        db_table = 'daily_quiz'