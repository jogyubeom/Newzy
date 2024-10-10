import logging
from collections import Counter

from newzy.config import get_okt
from newzy.models import GeneralWord
from newzy.utils.parser import extract_text_from_html


logging = logging.getLogger('my_logger')
def get_tokens_from_content(text):
    okt = get_okt()
    return okt.nouns(text)


def get_word_difficulty_scores(tokens):
    word_frequencies = Counter(tokens)
    difficulty_scores = {}

    for word, freq in word_frequencies.items():
        word_entry = GeneralWord.objects.filter(word=word).first()
        if word_entry:
            difficulty_scores[word] = word_entry.grade
        else:
            difficulty_scores[word] = 8  # 기본 단어가 아니면 난이도 8로 설정

    return difficulty_scores, word_frequencies


def calculate_difficulty_score(difficulty_scores, word_frequencies, total_count):
    total_difficulty_score = sum(
        difficulty_scores[word] * word_frequencies[word] for word in word_frequencies)
    return total_difficulty_score / total_count if total_count > 0 else 0


def determine_grade(average_difficulty_score, difficulty_distribution):
    normalized_difficulty = (average_difficulty_score / 8) * 100

    # 기사 난이도 분포 확인을 위한 dictionary에 저장
    rounded_diff = round(normalized_difficulty, 2)

    if rounded_diff in difficulty_distribution:
        difficulty_distribution[rounded_diff] += 1
    else:
        difficulty_distribution[rounded_diff] = 1

    if normalized_difficulty >= 67:
        grade = 5
    elif normalized_difficulty >= 62:
        grade = 4
    elif normalized_difficulty >= 55:
        grade = 3
    elif normalized_difficulty >= 50:
        grade = 2
    else:
        grade = 1

    logging.info(f"전체 난이도: {normalized_difficulty}, 최종 등급: {grade}")

    return grade


def analyze_difficulty_of_news(html_content: str, difficulty_distribution):
    # text 추출
    text = extract_text_from_html(html_content)

    # 형태소 분석
    tokens = get_tokens_from_content(text)

    # 본문이 없으면 난이도 0 반환
    total_cnt = len(tokens)
    if total_cnt == 0:
        logging.error("본문의 내용이 없습니다.")
        return 0

    difficulty_scores, word_frequencies = get_word_difficulty_scores(tokens)
    average_difficulty_score = calculate_difficulty_score(difficulty_scores, word_frequencies,
                                                          total_cnt)
    grade = determine_grade(average_difficulty_score, difficulty_distribution)

    return grade
