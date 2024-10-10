from locust import HttpUser, task, between

class WebsiteUser(HttpUser):
    wait_time = between(1, 3)  # 각 요청 사이의 대기 시간 (1~3초 사이 랜덤)

    @task
    def load_test(self):
        self.client.get("/1")  # `/1` 경로에 대해 GET 요청을 보냄
