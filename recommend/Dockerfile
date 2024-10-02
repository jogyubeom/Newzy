# Python 3.8 이미지 사용 (slim 버전은 경량화된 이미지)
FROM python:3.8-slim

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일을 컨테이너 내 /app 디렉토리로 복사
COPY . /app/

# 디렉토리 구조 확인
RUN echo "Displaying /app directory contents:" && ls -al /app

# Python 패키지 설치
RUN pip install --no-cache-dir -r requirements.txt

# 또 다시 디렉토리 구조 확인
RUN echo "After installing packages, displaying /app directory contents again:" && ls -al /app

# 외부에서 접근할 포트 8000 오픈
EXPOSE 8001

# Django 서버 시작 명령어 설정
CMD ["python", "manage.py", "runserver", "0.0.0.0:8001"]
