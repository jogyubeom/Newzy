from django.core.management.base import BaseCommand

from newzy.models import News
from newzy.utils.parser import remove_ad_divs, extract_text_from_html


class Command(BaseCommand):
    help = '크롤링한 뉴스에서 광고 부분 제거하는 명령어'

    def handle(self, *args, **kwargs):
        news_list = News.objects.all()
        for news in news_list:
            news.content = remove_ad_divs(news.content)
            news.content_text = extract_text_from_html(news.content)
            news.save()
