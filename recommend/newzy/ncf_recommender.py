
class NCFModel(torch.nn.Module):
    def __init__(self, num_users, num_articles, embedding_size=50):
        super(NCFModel, self).__init__()
        self.user_embedding = torch.nn.Embedding(num_users, embedding_size)
        self.article_embedding = torch.nn.Embedding(num_articles, embedding_size)

        self.fc1 = torch.nn.Linear(embedding_size * 2, 128)
        self.fc2 = torch.nn.Linear(128, 64)
        self.fc3 = torch.nn.Linear(64, 1)

    def forward(self, user_id, article_id):
        user_embed = self.user_embedding(user_id)
        article_embed = self.article_embedding(article_id)

        x = torch.cat([user_embed, article_embed], dim=-1)
        x = torch.relu(self.fc1(x))
        x = torch.relu(self.fc2(x))
        x = torch.sigmoid(self.fc3(x))

        return x


class NCFRecommender:
    def __init__(self):
        self.model = self._load_model()

    def _load_model(self):
        num_users = User.objects.count()
        num_articles = Article.objects.count()
        model = NCFModel(num_users, num_articles)
        # 모델을 학습하거나 저장된 모델을 로드
        return model

    def recommend(self, user_id):
        user = torch.tensor([user_id])
        article_ids = [article.article_id for article in Article.objects.all()]
        article_ids_tensor = torch.tensor(article_ids)

        with torch.no_grad():
            scores = self.model(user, article_ids_tensor).squeeze()
            recommended_articles = torch.topk(scores, 5).indices

        return recommended_articles.tolist()
