import torch
import logging

from newzy.models import User, News

# 로깅 설정
logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')


class NCFModel(torch.nn.Module):  # torch.nn.Module : PyTorch에서 모든 신경망 모델의 기본 클래스.
    def __init__(self, num_users, num_news, embedding_size=50):
        super(NCFModel, self).__init__()
        logging.info(
            f"NCFModel 초기화: num_users={num_users}, num_news={num_news}, embedding_size={embedding_size}")

        self.user_embedding = torch.nn.Embedding(num_users + 1, embedding_size)  # +1을 해서 범위 초과 방지
        self.news_embedding = torch.nn.Embedding(num_news + 1, embedding_size)  # +1을 해서 범위 초과 방지

        self.fc1 = torch.nn.Linear(embedding_size * 2, 128)
        self.fc2 = torch.nn.Linear(128, 64)
        self.fc3 = torch.nn.Linear(64, 1)

    def forward(self, user_id, news_id):
        logging.info(f"Forward 함수 호출: user_id={user_id}, news_id={news_id}")

        user_embed = self.user_embedding(user_id)
        news_embed = self.news_embedding(news_id)

        user_embed_expanded = user_embed.expand_as(news_embed)

        x = torch.cat([user_embed_expanded, news_embed], dim=-1)
        x = torch.relu(self.fc1(x))
        x = torch.relu(self.fc2(x))
        x = torch.sigmoid(self.fc3(x))

        logging.info("Forward 결과 계산 완료")
        return x


class NCFRecommender:
    def __init__(self, cluster_id):
        self.cluster_id = cluster_id
        self.model = self._load_model()

    def _load_model(self):
        # user의 개수와 최대 news_id를 사용
        num_users = User.objects.count()
        num_news = News.objects.count()

        logging.info(f"모델 로드: num_users={num_users}, num_news={num_news}")
        model = NCFModel(num_users, num_news)
        return model

    def recommend(self, user_id):
        logging.info(f"추천 계산 시작: user_id={user_id}")

        user = torch.tensor([user_id])
        news_ids = [news.news_id for news in News.objects.all()]
        news_ids_tensor = torch.tensor(news_ids)

        with torch.no_grad():
            scores = self.model(user, news_ids_tensor).squeeze()
            recommended_news = torch.topk(scores, 5).indices

        logging.info(f"추천 완료: user_id={user_id}, 추천된 뉴스: {recommended_news.tolist()}")
        return recommended_news.tolist()


def recommend_news_by_cluster(cluster_id):
    logging.info(f"군집별 뉴스 추천 시작: cluster_id={cluster_id}")

    recommender = NCFRecommender(cluster_id=cluster_id)
    cluster_users = User.objects.filter(cluster_id=cluster_id)

    for user in cluster_users:
        logging.info(f"유저 {user.nickname} ({user.user_id})에게 추천 시작")
        recommended_news = recommender.recommend(user.user_id)
        logging.info(f"유저 {user.nickname}에게 추천된 기사: {recommended_news}")
