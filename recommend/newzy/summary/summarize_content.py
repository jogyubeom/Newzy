import logging

import openai  # https://github.com/openai/openai-python
from decouple import config

from newzy.db_operations import get_news, get_cluster_info, save_news_summary
from newzy.recommend.parser import extract_text_from_html

openai.api_key = config("OPENAI_API_KEY")
logging = logging.getLogger('my_logger')

def generate_prompt(cluster_id: int, content: str) -> str:
    cluster = get_cluster_info(cluster_id)
    return f"""
    '{cluster['interest_category']}'에 관심 있는 '{cluster['age_group']}'을(를) 위한 요약을 작성해주세요.
    사용자는 해당 기사를 평균 {cluster['page_stay_time']}초 동안 읽을 것으로 예상됩니다.
    그 시간을 고려하여, 주요 정보를 빠르게 전달할 수 있도록 간결하고 흥미로운 요약을 작성해주세요.
    기사 내용: {content}
    """


def summarize_news_by_cluster(news_id: int, cluster_id: int):
    news = get_news(news_id)
    content = extract_text_from_html(news['content'])
    prompt = generate_prompt(cluster_id, content)
    try:
        response = openai.chat.completions.create(
            model="gpt-3.5-turbo",  # 사용할 모델 선택
            messages=[
                {"role": "system", "content": "You are a news article summarizer."},
                {"role": "user", "content": prompt}
            ],
            max_tokens=300,  # 요약에 사용할 최대 토큰 수
            temperature=0.5,  # 텍스트의 창의성 정도
        )
        # logging.info(response)
        # 응답에서 요약된 텍스트 추출
        summary = response.choices[0].message.content
        return summary

    except Exception as e:
        print(f"Error summarizing content: {e}")
        return None


def summarize_news(cluster_id: int, news_id: int) -> str:
    summary = summarize_news_by_cluster(news_id, cluster_id)
    save_news_summary(news_id, cluster_id, summary)
    logging.info(f"{cluster_id} - {summary}")
