import selenium #셀레니움
import pandas as pd #csv를 읽고 dataframe을 사용하기 위한 pandas
import time
from bs4 import BeautifulSoup #브라우저 태그를 가져오고 파싱하기 위함
import re #정규식
import copy
import urllib.request

from selenium import webdriver #브라우저를 띄우고 컨트롤하기 위한 webdriver
from selenium.webdriver.common.keys import Keys #브라우저에 키입력 용
from selenium.webdriver.common.by import By #webdriver를 이용해 태그를 찾기 위함
from selenium.webdriver.support.ui import WebDriverWait #Explicitly wait을 위함
from selenium.webdriver.support import expected_conditions as EC #브라우저에 특정 요소 상태 확인을 위해
from selenium.common.exceptions import NoSuchElementException,StaleElementReferenceException,TimeoutException #예외처리를 위한 예외들 
from webdriver_manager.chrome import ChromeDriverManager #크롬에서 크롤링 진행 크롬 웹 드라이버 설치시 필요
from selenium.webdriver import ActionChains

from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive

input_file = "cafe_name_big0.csv"
output_file = "cafe_url_big0.csv"


resultListClass = "UEzoS"
resultListTitleClass = "tzwk0"
# resultListTitleClass = "P7gyV"
resultTargetTitleDiv = "YouOG"
resultTitleClick = "#_pcmap_list_scroll_container > ul > li > div.CHC5F > a"
# resultTitleClick = "#_pcmap_list_scroll_container > ul > li > div.ouxiq > a"
resultListFirstTitle = "#_pcmap_list_scroll_container > ul > li >div.qbGlu > div.ouxiq >a.P7gyV > div.ApCpt > div.C6RjW > span.place_bluelink"
searchFrame = "searchIframe"
entryFrame = "entryIframe"
addressSpan = "IH7VW"
daySpan = "kGc0c"
timeDiv = "qo7A2"
telSpan = "dry01"
descSpan = "zPfVt"
reviewOpenerA = "xHaT3"
reviewSpan = "zPfVt"
ratingSvg = "GWkzU"
imgDiv = "K0PDV"
timeOpenerSelector = "#app-root > div > div > div > div:nth-child(6) > div > div.place_section.no_margin.vKA6F > div > ul > li.SF_Mq.Sg7qM > div > a"
descOpenerSelector = "#app-root > div > div > div > div > div > div.place_section.no_margin.vKA6F > div > ul > li.SF_Mq.I5Ypx > div > a"

basic_info_classname = "zD5Nm f7aZ0"
review_tab_selector = "#app-root > div > div > div  div.ngGKH span.veBoZ"
arrow_mark_classname = "Tvx37"
review_keywords_classname = "BBfpo"
address_classname = "LDgIH"
business_hour_classname = "xmQdY"
phone_number_classname = "xlx7Q"
linked_hypherlink_classname = "CHmqa"
description_selector = "#app-root > div > div > div div.dRAr1 span.zPfVt"
category_selector = "div.naver_order_contents div.order_list div.order_list_tit span.title"
menu_name_selector = "div.naver_order_contents div.order_list div.info_detail div.tit"
count = 0

def checkIsList():
    try:#리스트에서 딱 하나 나올 때
        driver.switch_to.default_content()
        WebDriverWait(driver,1.5).until(
            EC.presence_of_element_located((By.ID, searchFrame)))
        driver.switch_to.frame(searchFrame)    
        WebDriverWait(driver,1).until(
            EC.presence_of_element_located((By.CLASS_NAME, resultListTitleClass)))
        driver.switch_to.default_content()
        return 0
    except TimeoutException:
        #검색결과 바로 식당정보로 이동했을때
        try:
            driver.switch_to.default_content()
            WebDriverWait(driver,1.5).until(
            EC.presence_of_element_located((By.ID, entryFrame)))
            driver.switch_to.frame(entryFrame)
            WebDriverWait(driver,1).until(
                EC.presence_of_element_located((By.CLASS_NAME, resultTargetTitleDiv)))
            driver.switch_to.default_content()
            return 1
        #검색결과가 없을때 혹은 검색결과 2개 이상일때
        except TimeoutException:
            driver.switch_to.default_content()
            time.sleep(0.2)
            return 2
        
def designate_tab_and_click(tab_name : str):
    tabs = driver.find_elements(By.CSS_SELECTOR, review_tab_selector)
    for tab in tabs:
        if tab.text == tab_name:
            tab.click()
            break

        
def get_image():
    global cafe_url_list
    count = 0
    designate_tab_and_click("사진")
    # drive_file_list = drive.ListFile({'q': "'root' in parents and trashed=false"}).GetList()
    # drive_file_set = set()
    # for file in drive_file_list:
    #     drive_file_set.add(file['title'])

    try:
        WebDriverWait(driver,1.5).until(\
            EC.presence_of_element_located((By.CLASS_NAME, "K0PDV")))

        elements = driver.find_elements(By.CLASS_NAME, "K0PDV")
        
        for element in elements:
            if count > 0:
                return
            if element.text == "업체":
                temp = element.get_attribute("style")
                start = temp.find("url(\"")
                end = temp.find("\");")
                img_url = temp[start + 5 : end]
                urllib.request.urlretrieve(img_url, f'image{count}_002.jpg') #이미지 다운로드
                
                # if f"{cafe_name_global} image{count}.jpg" in drive_file_set:
                #     continue
                file = drive.CreateFile({"title": f"{cafe_name_global} image{count}.jpg"})  # Create GoogleDriveFile instance with title 'image.jpg'
                file.SetContentFile(f"image{count}_002.jpg")  # Set content of the file from given local path
                file.Upload()
                

                permission = file.InsertPermission({
                        'type': 'anyone',
                        'value': 'anyone',
                        'role': 'reader'})
                
                drive_url = "https://drive.google.com/uc?id=" + file['id']
                count += 1
                cafe_url_dict.setdefault(cafe_name_global, set())
                cafe_url_dict[cafe_name_global].add(drive_url)
        
        
    except Exception as e:
        return -1

def doScrollDown(attempt):
    for _ in range(attempt):
        driver.execute_script('window.scrollTo(0, document.body.scrollHeight);')
        time.sleep(0.2)

gauth = GoogleAuth()
# Creates local webserver and auto handles authentication.
gauth.LocalWebserverAuth()

drive = GoogleDrive(gauth)

# driver = webdriver.Chrome(ChromeDriverManager().install()) # 드라이버 자동 설치
driver = webdriver.Chrome('/Users/commitKermit/Desktip/vscode/grad_design/crawling/chromedriver_win32')
# C:\Users\commitKermit\Desktop\vscode\grad_design\crawling\chromedriver_win32
driver.get("https://map.naver.com/v5/")


try:
   element = WebDriverWait(driver, 10).until(
       EC.presence_of_element_located((By.CLASS_NAME, "input_search"))
   ) #입력창이 뜰 때까지 대기
finally:
   pass


cafe_url_dict = dict()
# cafe_url_list = list()

df = pd.read_csv(input_file)

for idx in df.index:

    cafe_original_name = df.loc[idx, '카페이름']
    cafe_name = df.loc[idx, '사업장명']
    cafe_name_global = cafe_name

    if(idx % 10 == 0):
        print(f"cafe = {cafe_name}, idx = {idx}")
        
    flag = True
    checkResult = -1
    try:
        search_box = driver.find_element(By.CLASS_NAME, "input_search")
        search_box.send_keys(f"대구 {cafe_name}") #검색창에 "대구 {카페이름}" 입력
        search_box.send_keys(Keys.ENTER)  #키보드 엔터 누르기

        checkResult = checkIsList()

        if checkResult == 0:   #결과가 리스트로 나옴
            #맞는지 검사하고 맞으면 크롤링 진행, 틀리면 
            #선택하고
            driver.switch_to.default_content()
            driver.switch_to.frame(searchFrame)
            #driver.find_element(By.CSS_SELECTOR,resultTitleClick).click()
            driver.find_element(By.CSS_SELECTOR, resultTitleClick).send_keys(Keys.ENTER)
            driver.switch_to.default_content()
            WebDriverWait(driver,4).until(
                EC.presence_of_element_located((By.ID, entryFrame)))#상세탭 나오는거 기다리기
            driver.switch_to.frame(entryFrame)#상세탭 이동
            
            get_image()

            driver.switch_to.default_content()

        elif checkResult ==1:  #결과가 바로 식당정보로 나옴
            #바로 정보뽑기
            driver.switch_to.default_content()
            WebDriverWait(driver,4).until(
                EC.presence_of_element_located((By.ID, entryFrame)))
            driver.switch_to.frame(entryFrame)

            get_image()
            
            driver.switch_to.default_content()

        elif checkResult == 2:
            driver.switch_to.default_content()
            driver.switch_to.frame(searchFrame)
            list_first = driver.find_element(By.CSS_SELECTOR, resultListFirstTitle)
            temp = cafe_name_global.split(sep=" ", maxsplit=1)[1]
            if list_first.text == temp :
                list_first.click()
                driver.switch_to.default_content()
                WebDriverWait(driver,4).until(
                    EC.presence_of_element_located((By.ID, entryFrame)))#상세탭 나오는거 기다리기
                driver.switch_to.frame(entryFrame)#상세탭 이동
                
                get_image()

            else:
                # keyword_count_dict = "검색결과 없음"
                # cafe_name_review_list.append([cafe_name, keyword_count_dict])
                driver.switch_to.default_content()
                driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER) #검색창 클리어
                continue

    except Exception as e:
        print("error: "+str(e))
    
    driver.switch_to.default_content()
    
    driver.switch_to.window(window_name = driver.window_handles[0])
    driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER) #검색창 클리어

# cafe_name_image = df.copy()
# cafe_name_image['URL'] = cafe_url_list
# cafe_name_image.to_csv(output_file, encoding="utf-8-sig")

cafe_name_review_df = pd.DataFrame.from_dict(cafe_url_dict, orient='index')

cafe_name_review_df.to_csv(output_file, encoding="utf-8-sig")