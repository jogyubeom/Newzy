import logging
import pickle

import torch
from django.conf import settings
from pymongo import MongoClient

from newzy.models import User, News

logging = logging.getLogger('my_logger')

# MongoDB 연결
mongo_client = MongoClient(
    host=settings.MONGO_DB['HOST'],
    port=settings.MONGO_DB['PORT'],
    username=settings.MONGO_DB.get('USER'),
    password=settings.MONGO_DB.get('PASSWORD')
)

mongo_db = mongo_client[settings.MONGO_DB['NAME']]
collection = mongo_db['models']  # 모델을 저장할 컬렉션


class NCFModel(torch.nn.Module):
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

    def save_model_to_mongo(self, cluster_id):
        model_data = pickle.dumps(self.state_dict())
        document = {
            'cluster_id': cluster_id,
            'model_data': model_data
        }
        collection.update_one({'cluster_id': cluster_id}, {"$set": document}, upsert=True)
        logging.info(f"모델 MongoDB에 저장 완료: cluster_id={cluster_id}")

    def load_model_from_mongo(self, cluster_id):
        document = collection.find_one({'cluster_id': cluster_id})
        if document and 'model_data' in document:
            model_data = document['model_data']
            self.load_state_dict(pickle.loads(model_data))
            logging.info(f"모델 MongoDB에서 로드 완료: cluster_id={cluster_id}")
            return self
        else:
            logging.warning(f"MongoDB에 저장된 모델 없음: cluster_id={cluster_id}")
            return None

    def train(self, user_ids, news_ids, ratings, num_epochs=10, learning_rate=0.001):
        logging.info(f"모델 학습 시작: num_epochs={num_epochs}, learning_rate={learning_rate}")
        optimizer = torch.optim.Adam(self.parameters(), lr=learning_rate)
        criterion = torch.nn.MSELoss()

        for epoch in range(num_epochs):
            self.train()

            # Forward pass
            predictions = self(user_ids, news_ids).squeeze()
            loss = criterion(predictions, ratings)

            # Backward pass and optimization
            optimizer.zero_grad()
            loss.backward()
            optimizer.step()

            logging.info(f"Epoch [{epoch + 1}/{num_epochs}], Loss: {loss.item()}")


class NCFRecommender:
    def __init__(self, cluster_id):
        self.cluster_id = cluster_id
        self.model = self._load_model()

    def _load_model(self):
        num_users = User.objects.count()
        num_news = News.objects.count()
        model = NCFModel(num_users, num_news)
        # logging.info(f"모델 로드 시도: cluster_id={self.cluster_id}")
        # MongoDB에서 모델을 로드하려고 시도
        # model = NCFModel(num_users, num_news).load_model_from_mongo(self.cluster_id)
        #
        # # MongoDB에 저장된 모델이 없는 경우 새로운 모델 생성
        # if not model:
        #     logging.warning(f"MongoDB에 저장된 모델 없음, 새 모델 생성: cluster_id={self.cluster_id}")
        #     model = NCFModel(num_users, num_news)
        #     model.save_model_to_mongo(self.cluster_id)
        return model

    def train_model(self, user_ids, news_ids, ratings):
        logging.info(f"모델 학습 시작: cluster_id={self.cluster_id}")
        self.model.train(user_ids, news_ids, ratings)
        self.model.save_model_to_mongo(self.cluster_id)

    def recommend(self, user_id):
        logging.info(f"추천 계산 시작: user_id={user_id}")

        user = torch.tensor([user_id])
        news_ids = [news.news_id for news in News.objects.all()]
        news_ids_tensor = torch.tensor(news_ids)

        with torch.no_grad():
            scores = self.model(user, news_ids_tensor).squeeze()
            recommended_news = torch.topk(scores, 30).indices

        logging.info(f"추천 완료: user_id={user_id}, 추천된 뉴스: {recommended_news.tolist()}")
        return recommended_news.tolist()
