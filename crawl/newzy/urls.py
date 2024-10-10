from django.urls import path

from . import views

urlpatterns = [
    path('', views.home, name='home'),  # 홈 페이지로 연결될 기본 URL
]
