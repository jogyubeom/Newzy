# Python 3.8 이미지 사용 (slim 버전은 경량화된 이미지)
FROM python:3.8-slim

# 시스템 패키지 업데이트 및 OpenJDK 17 설치
RUN apt-get update && apt-get install -y \
    openjdk-17-jdk \
    curl \
    && apt-get clean

# 환경 변수 설정 (Java 17 설치 경로)
ENV JAVA_HOME /usr/lib/jvm/java-17-openjdk-amd64
ENV PATH $JAVA_HOME/bin:$PATH

# 작업 디렉토리 설정
WORKDIR /app

# 프로젝트 파일을 컨테이너 내 /app 디렉토리로 복사
COPY . /app/

# Python 패키지 설치
RUN pip install --no-cache-dir -r requirements.txt

# 외부에서 접근할 포트 8000 오픈
EXPOSE 8000

# Django 서버 시작 명령어 설정
CMD ["python", "manage.py", "runserver", "0.0.0.0:8000"]