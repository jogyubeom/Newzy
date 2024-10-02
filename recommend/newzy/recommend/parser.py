from bs4 import BeautifulSoup


def extract_text_from_html(html_content):
    # html_content가 None이거나 문자열이 아닌 경우 처리
    if html_content is None:
        return ""

    # html_content가 문자열이 아니면 문자열로 변환
    if not isinstance(html_content, str):
        html_content = str(html_content)

    soup = BeautifulSoup(html_content, 'html.parser')
    text = soup.get_text(separator=' ', strip=True)
    return text