import logging

from decouple import config
from langchain.chat_models import ChatOpenAI
from langchain.prompts import PromptTemplate
from langchain.schema import HumanMessage
from pydantic import BaseModel, Field, validator

from newzy.db_operations import get_news
from newzy.recommend.parser import extract_text_from_html
from newzy.redis_operation import save_quiz_to_redis

logging = logging.getLogger('my_logger')


# 1. Pydantic 모델 정의 (퀴즈 데이터)
class QuizData(BaseModel):
    question: str = Field(description="퀴즈의 질문")
    option1: str = Field(description="선택지 1")
    option2: str = Field(description="선택지 2")
    option3: str = Field(description="선택지 3")
    option4: str = Field(description="선택지 4")
    answer: str = Field(description="정답")

    @validator("question")
    def validate_question(cls, field):
        if not field or field.strip() == "":
            raise ValueError("Question must not be empty")
        return field

    @validator("option1")
    def validate_option1(cls, field):
        if not field or field.strip() == "":
            raise ValueError("Option 1 must not be empty")
        return field

    @validator("option2")
    def validate_option2(cls, field):
        if not field or field.strip() == "":
            raise ValueError("Option 2 must not be empty")
        return field

    @validator("option3")
    def validate_option3(cls, field):
        if not field or field.strip() == "":
            raise ValueError("Option 3 must not be empty")
        return field

    @validator("option4")
    def validate_option4(cls, field):
        if not field or field.strip() == "":
            raise ValueError("Option 4 must not be empty")
        return field

    @validator("answer")
    def validate_answer(cls, field):
        if not field or field.strip() == "":
            raise ValueError("Answer must not be empty")
        if field not in ["1", "2", "3", "4"]:
            raise ValueError("Answer must be one of '1', '2', '3', '4'")
        return field


# 2. 프롬프트 템플릿 생성 함수
def create_prompt(template: str) -> PromptTemplate:
    """
    프롬프트 템플릿을 생성하는 함수.
    Args:
        template: 프롬프트 템플릿 문자열.
    Returns:
        PromptTemplate 객체.
    """
    prompt = PromptTemplate(
        template=template,
        input_variables=["content"]
    )
    return prompt


# 3. 뉴스로부터 퀴즈 생성 및 저장 함수
def create_quiz_from_news(news_id: int):
    """
    뉴스 기사를 기반으로 퀴즈를 생성하고 데이터베이스에 저장하는 함수.
    Args:
        news_id: 뉴스 기사의 ID.
    """
    # 3.1 프롬프트 템플릿 생성
    template = """
    다음 기사를 읽고 제대로 이해하였는지 퀴즈를 생성하고 싶어.
    기사에서 전달하고자 하는 핵심 내용을 바탕으로 문제, 선택지 4개, 정답을 JSON 형식으로 생성해줘.
    기사 내용:
    {content}
    형식은 다음과 같이 줘:
    {{
      "question": "질문 내용",
      "option1": "선택지 1",
      "option2": "선택지 2",
      "option3": "선택지 3",
      "option4": "선택지 4",
      "answer": "정답 선택지 번호"
    }}
    """
    prompt = create_prompt(template)

    # 3.2 뉴스 데이터 가져오기 및 기사 본문 추출
    news = get_news(news_id)
    if not news:
        logging.error(f"news_id: {news_id} 에 해당하는 뉴스 기사가 없습니다.")
        return None

    content = extract_text_from_html(news['content'])
    if not content:
        logging.error(f"news_id: {news_id} 의 본문 내용이 없습니다.")
        return None

    # 3.3 OpenAI 모델 설정
    openai_api_key = config("OPENAI_API_KEY")
    openai_llm = ChatOpenAI(model_name="gpt-3.5-turbo", temperature=0.7,
                            openai_api_key=openai_api_key)

    # 3.4 Human 메시지 생성
    human_message = HumanMessage(content=prompt.format(content=content))

    # 3.5 퀴즈 생성
    try:
        response = openai_llm.invoke([human_message])
        quiz_data_text = response.content
    except Exception as e:
        logging.error(f"OpenAI 모델 호출 중 오류 발생: {e}")
        return None

    # 3.6 Pydantic 모델로 결과 파싱
    try:
        quiz_data = QuizData.parse_raw(quiz_data_text)
    except Exception as e:
        logging.error(f"퀴즈 파싱 중 오류 발생: {e}")
        return None

    logging.info(f"생성된 퀴즈 데이터: {quiz_data}")
    save_quiz_to_redis(news_id, quiz_data)
