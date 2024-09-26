import logging
from datetime import datetime

import numpy as np
import pandas as pd
from kmodes.kprototypes import KPrototypes
from matplotlib import pyplot as plt

from newzy.models import User, Cluster


def calculate_distance(point, centroid):
    # 유클리드 거리 계산
    return np.linalg.norm(point - centroid)


def visualize_with_age(df):
    plt.rcParams['font.family'] = 'Malgun Gothic'
    fig = plt.figure(figsize=(10, 8))
    ax = fig.add_subplot(111, projection='3d')

    scatter = ax.scatter(
        df['economy_score'], df['international_score'], df['society_score'],
        c=df['cluster'], cmap='Set2', s=df['age'], alpha=0.6, depthshade=True
    )

    ax.set_xlabel('Economy Score')
    ax.set_ylabel('International Score')
    ax.set_zlabel('Society Score')
    ax.set_title('3D Cluster Visualization: Including Age as Point Size')

    handles, labels = scatter.legend_elements()
    cluster_labels = [f"{name}" for name in df['cluster_name'].unique()]
    legend = ax.legend(handles, cluster_labels, title="Clusters")
    ax.add_artist(legend)

    plt.show()


def assign_cluster_names(df):
    cluster_names = {
        1: '경제 관심 나이 젊음',
        2: '경제 관심 나이 중간',
        3: '경제 관심 나이 많음',
        4: '사회 관심 나이 젊음',
        5: '사회 관심 나이 중간',
        6: '사회 관심 나이 많음',
        7: '세계 관심 나이 젊음',
        8: '세계 관심 나이 중간',
        9: '세계 관심 나이 많음'
    }
    df['cluster_name'] = df['cluster'].map(cluster_names)
    return df


def cluster_user():
    users = User.objects.values('user_id', 'economy_score', 'society_score', 'international_score',
                                'birth')
    df = pd.DataFrame(users)
    df = df.dropna()
    df['age'] = df['birth'].apply(lambda x: datetime.now().year - x.year)

    X = df[['economy_score', 'society_score', 'international_score', 'age']].values
    kproto = KPrototypes(n_clusters=9, init='Huang', verbose=2)
    clusters = kproto.fit_predict(X, categorical=[3])

    df['cluster'] = clusters

    zero_cluster_data = df[df['cluster'] == 0]
    if not zero_cluster_data.empty:
        # 0으로 분류된 데이터와 다른 군집의 중심 간 거리를 계산하여 가장 가까운 군집 찾기
        for idx, row in zero_cluster_data.iterrows():
            min_distance = float('inf')
            closest_cluster = None

            row_numeric = row[['economy_score', 'society_score', 'international_score']].values  # 숫자형 변수만
            for cluster_idx, centroid in enumerate(kproto.cluster_centroids_):
                if cluster_idx == 0:  # 0번 군집은 제외
                    continue

                centroid_numeric = centroid[:3]  # 나이를 제외한 숫자형 데이터
                distance = calculate_distance(row_numeric, centroid_numeric)  # 나이 제외하고 거리 계산
                if distance < min_distance:
                    min_distance = distance
                    closest_cluster = cluster_idx

            if closest_cluster is not None:
                df.at[idx, 'cluster'] = closest_cluster  # 가장 가까운 군집으로 재할당

    df = assign_cluster_names(df)

    for index, row in df.iterrows():
        if row['cluster'] <= 0 or row['cluster'] > 9:
            logging.error(f"{row['user_id']}의 군집 번호가 유효하지 않습니다.")
            continue
        user = User.objects.get(user_id=row['user_id'])
        cluster = Cluster.objects.get(cluster_id=row['cluster'])
        user.cluster_id = cluster
        user.save()

    logging.info(f">>> 군집화 성공")
    visualize_with_age(df)
