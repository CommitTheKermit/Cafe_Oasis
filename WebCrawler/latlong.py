import selenium #셀레니움
import pandas as pd #csv를 읽고 dataframe을 사용하기 위한 pandas
import time
from bs4 import BeautifulSoup #브라우저 태그를 가져오고 파싱하기 위함
import re #정규식

from selenium import webdriver #브라우저를 띄우고 컨트롤하기 위한 webdriver
from selenium.webdriver.common.keys import Keys #브라우저에 키입력 용
from selenium.webdriver.common.by import By #webdriver를 이용해 태그를 찾기 위함
from selenium.webdriver.support.ui import WebDriverWait #Explicitly wait을 위함
from selenium.webdriver.support import expected_conditions as EC #브라우저에 특정 요소 상태 확인을 위해
from selenium.common.exceptions import NoSuchElementException,StaleElementReferenceException,TimeoutException #예외처리를 위한 예외들 
from webdriver_manager.chrome import ChromeDriverManager #크롬에서 크롤링 진행 크롬 웹 드라이버 설치시 필요
from selenium.webdriver import ActionChains

import geopy
# Nominatim 서비스 객체에 대한 핸들러 가져 오기
service = geopy.Nominatim(user_agent = "myGeocoder")
# Nominatim (예 : OSM) 서비스를 사용하여 도시 이름을 지오 코딩합니다.
service.geocode("Berlin, Germany")

df = pd.read_csv("dataset_without_menu.csv")
loc_list = df["주소"].to_list()

# for i, loc in enumerate(df["주소"]):

#     if pd.isna(loc):
#         df.loc[i, '위도'] = "nan"
#         df.loc[i, '경도'] = "nan"
#         continue
    
coord = service.geocode("")
df.loc[i, '위도'] = coord[0]
df.loc[i, '경도'] = coord[1]

