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

# driver = webdriver.Chrome(ChromeDriverManager().install()) # 드라이버 자동 설치
driver = webdriver.Chrome('/Users/commitKermit/Desktip/vscode/grad_design/crawling/chromedriver_win32')
# C:\Users\commitKermit\Desktop\vscode\grad_design\crawling\chromedriver_win32
driver.get("https://map.kakao.com/")

input_file = "vil_name1.csv"
output_file = "cafe_names1.csv"

try:
   element = WebDriverWait(driver, 10).until(
    #    EC.presence_of_element_located((By.CSS_SELECTOR, "div. boxsearchbar query tf_keyword"))
    EC.presence_of_element_located((By.CLASS_NAME, "tf_keyword"))
   ) #입력창이 뜰 때까지 대기
finally:
   pass

df = pd.read_csv(input_file)
vil_name_list = df["동명"].to_list()

cafe_name_set = set()

for vil_name in vil_name_list:
   try:
      search_box = driver.find_element(By.CLASS_NAME, "tf_keyword")
      for i in range(30):
         search_box.send_keys(Keys.BACKSPACE)
      search_box.send_keys(f"대구 {vil_name} 카페") #검색창에 "대구 북구 {카페이름}" 입력
      search_box.send_keys(Keys.ENTER)  #키보드 엔터 누르기
      more = driver.find_element(By.CSS_SELECTOR, "div.keywordSearch a[id='info.search.place.more']")
      time.sleep(0.5)
      action = ActionChains(driver)
      action.move_to_element(more).perform()
      more.send_keys(Keys.ENTER)
      index = 1
      
      while(True):
         
         time.sleep(0.5)
         items = driver.find_elements(By.CLASS_NAME, "link_name")
         temp_set = set()
         for item in items:
            cafe_name_set.add(vil_name + " " + item.text)
            temp_set.add(vil_name + " " + item.text)
         

         try:
            index += 1
            driver.find_element(By.CSS_SELECTOR, f"div.pageWrap > a[id='info.search.page.no{index}']").send_keys(Keys.ENTER)
         except:
            try:
               if(index != 6 or len(items) != 15 or len(cafe_name_set.intersection(temp_set)) >= 15):
                  break
               index = 1
               driver.find_element(By.CSS_SELECTOR, "div.Info div.pages > div.pageWrap > button[id='info.search.page.next']").send_keys(Keys.ENTER)
            except:
               break
            
         
         print()

   except Exception as e:
      print("error: "+str(e))
      print()

cafe_name_list = list(cafe_name_set)

cafe_name_review_df = pd.DataFrame(cafe_name_list, columns=["카페이름"])

cafe_name_review_df.to_csv(output_file, encoding="utf-8-sig")