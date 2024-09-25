from datetime import datetime

import pandas as pd
import pytz
from django.utils import timezone
from kmodes.kprototypes import KPrototypes
from matplotlib import pyplot as plt

from newzy.models import User


def visualize_with_age(df):
    plt.rcParams['font.family'] = 'Malgun Gothic'
    # 3D scatter plot으로 시각화 (나이까지 색상으로 표현)
    fig = plt.figure(figsize=(10, 8))
    ax = fig.add_subplot(111, projection='3d')

    # 군집 이름을 색상으로, 나이를 크기로 표현
    scatter = ax.scatter(
        df['economy_score'], df['international_score'], df['society_score'],
        c=df['cluster'], cmap='Set2', s=df['age'], alpha=0.6, depthshade=True
    )

    # 각 축의 라벨 설정
    ax.set_xlabel('Economy Score')
    ax.set_ylabel('International Score')
    ax.set_zlabel('Society Score')

    # 그래프 제목 설정
    ax.set_title('3D Cluster Visualization: Including Age as Point Size')

    # 군집 이름을 범례로 추가
    handles, labels = scatter.legend_elements()
    cluster_labels = [f"{name}" for name in df['cluster_name'].unique()]  # 군집 이름 사용
    legend = ax.legend(handles, cluster_labels, title="Clusters")
    ax.add_artist(legend)

    # 그래프 표시
    plt.show()


def assign_cluster_names(df):
    # 군집별 이름 지정
    cluster_names = {
        0: '경제 관심 나이 젊음',
        1: '경제 관심 나이 중간',
        2: '경제 관심 나이 많음',
        3: '사회 관심 나이 젊음',
        4: '사회 관심 나이 중간',
        5: '사회 관심 나이 많음',
        6: '세계 관심 나이 젊음',
        7: '세계 관심 나이 중간',
        8: '세계 관심 나이 많음'
    }

    # 각 군집의 이름을 추가
    df['cluster_name'] = df['cluster'].map(cluster_names)

    return df


def cluster_user():
    # User 테이블에서 필요한 데이터를 가져오기
    users = User.objects.values('economy_score', 'society_score', 'international_score', 'birth')

    # DataFrame으로 변환
    df = pd.DataFrame(users)

    # NaN 값이 있으면 제거
    df = df.dropna()

    # 나이 계산
    seoul_tz = pytz.timezone('Asia/Seoul')
    df['age'] = df['birth'].apply(lambda x: timezone.localtime(
        datetime.now(seoul_tz)))  # Aware (UTC) → Aware (TIME_ZONE).year - x.year)

    # 군집화를 위한 데이터 준비
    X = df[['economy_score', 'society_score', 'international_score', 'age']].values

    # K-prototypes 모델 생성 및 군집화 수행
    kproto = KPrototypes(n_clusters=9, init='Cao', verbose=2)
    clusters = kproto.fit_predict(X, categorical=[3])  # 'age'는 범주형으로 처리

    # 군집 결과를 데이터프레임에 추가
    df['cluster'] = clusters

    # 군집 이름 추가
    df = assign_cluster_names(df)

    # 시각화 함수 호출
    visualize_with_age(df)
