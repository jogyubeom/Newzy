from konlpy.tag import Okt
from selenium import webdriver
from webdriver_manager.chrome import ChromeDriverManager

def create_webdriver():
    chrome_options = webdriver.ChromeOptions()
    chrome_options.add_argument('--headless')
    chrome_options.add_argument('--disable-cache')
    chrome_options.add_argument('--no-sandbox')
    chrome_options.add_argument('--disable-dev-shm-usage')
    chrome_options.add_experimental_option("excludeSwitches", ["enable-logging"])

    driver = webdriver.Chrome(ChromeDriverManager().install(), options=chrome_options)
   
    return driver


def get_okt():
    return Okt()
