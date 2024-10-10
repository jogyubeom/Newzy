from django.db import models


class GeneralWord(models.Model):
    general_word_id = models.AutoField(primary_key=True)
    grade = models.PositiveSmallIntegerField(null=True,
                                             blank=True)  # TINYINT -> PositiveSmallIntegerField 사용
    word = models.CharField(max_length=20, null=True, blank=True)  # VARCHAR(20)
    word_type = models.CharField(max_length=10, null=True,
                                 blank=True)  # VARCHAR(10), 'type'은 예약어이므로 'word_type'으로 변경

    class Meta:
        managed = False
        db_table = 'general_word'

    def __str__(self):
        return self.word


class News(models.Model):
    news_id = models.BigAutoField(primary_key=True)
    link = models.URLField(max_length=255, unique=True)  # VARCHAR(255), 링크는 URLField로 변경 가능
    title = models.CharField(max_length=255)  # VARCHAR(255)
    content = models.TextField()  # TEXT
    content_text = models.TextField()
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
