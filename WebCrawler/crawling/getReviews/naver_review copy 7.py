import selenium #셀레니움
import pandas as pd #csv를 읽고 dataframe을 사용하기 위한 pandas
import time
from bs4 import BeautifulSoup #브라우저 태그를 가져오고 파싱하기 위함
import re #정규식
import copy

from selenium import webdriver #브라우저를 띄우고 컨트롤하기 위한 webdriver
from selenium.webdriver.common.keys import Keys #브라우저에 키입력 용
from selenium.webdriver.common.by import By #webdriver를 이용해 태그를 찾기 위함
from selenium.webdriver.support.ui import WebDriverWait #Explicitly wait을 위함
from selenium.webdriver.support import expected_conditions as EC #브라우저에 특정 요소 상태 확인을 위해
from selenium.common.exceptions import NoSuchElementException,StaleElementReferenceException,TimeoutException #예외처리를 위한 예외들 
from webdriver_manager.chrome import ChromeDriverManager #크롬에서 크롤링 진행 크롬 웹 드라이버 설치시 필요
from selenium.webdriver import ActionChains

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

def designate_review_tab_and_click(tab_name : str) -> str:
    try:
        driver.switch_to.window(window_name = driver.window_handles[-1])

        WebDriverWait(driver,2).until(\
            EC.presence_of_element_located((By.ID, "onlyHasMedia")))

        # time.sleep(0.2)

        driver.switch_to.default_content()

        check = driver.find_element(By.ID, "onlyHasMedia")
        # driver.execute_script("arguments[0].scrollIntoView(true);", check)
        
        if check.is_selected():
            driver.find_element(By.CLASS_NAME, "_17IcDv").click()
            
        WebDriverWait(driver,2).until(\
            EC.presence_of_element_located((By.CSS_SELECTOR, "div.flicking-camera > span._3Llacl button[class='wTaI4v _1tvQCN']")))
        tabs = driver.find_elements(By.CSS_SELECTOR, "div.flicking-camera > span._3Llacl button[class='wTaI4v _1tvQCN']")

        if len(tabs) == 0:
            return -1

        for tab in tabs:
            if tab.text.startswith(tab_name):
                attempt = int(tab.text[2:])
                tab.click()
        
                return attempt
    except Exception as e:
        print(e)
        return -1
    return -1
        
def get_reviewed_cafe_names(attempt : int):

    doScrollDown(attempt // 10)

    driver.switch_to.default_content()
    WebDriverWait(driver,1.5).until(\
        EC.presence_of_element_located((By.CLASS_NAME, "_1SR1vP")))
    addresses_names = driver.find_elements(By.CLASS_NAME, "_1SR1vP")
    
    adresses_names_set = set()
    for element in addresses_names:
        address = element.find_elements(By.CLASS_NAME, "bT3yCK")
        if len(address) == 0:
            continue
            
        name = element.find_element(By.CLASS_NAME, "_1z_opo")
        comb = address[0].text + ":" + name.text
        adresses_names_set.add(comb)

    return adresses_names_set



def get_review():
    designate_tab_and_click("리뷰")

    try:
        WebDriverWait(driver,1.5).until(\
            EC.presence_of_element_located((By.CLASS_NAME, "YeINN")))
        
        while True:
            try:
                arrow = driver.find_element(By.CLASS_NAME, "fvwqf").send_keys(Keys.ENTER)
                time.sleep(0.2)
            except:
                break

        elements = driver.find_elements(By.CLASS_NAME, "Hazns")
            
        action = ActionChains(driver)
        length = len(elements)
        count = 0
        print(f"리뷰 수 : {len(elements)}, {cafe_name_global}, index={cafe_name_list.index(cafe_name_global)}")
        
        for _ in range(length):
            if count % 10 == 0:
                print(count)
            element = elements[count]
            nickname = element.text.split(sep='\n')[0]
            if nickname in nickname_visited_dict:
                count += 1
                continue

            action.move_to_element(element).perform()
            element.send_keys(Keys.ENTER)

            


            attempt = designate_review_tab_and_click("카페")
            if attempt == -1:
                driver.close()
                driver.switch_to.window(window_name = driver.window_handles[0])
                driver.switch_to.default_content()
                driver.switch_to.frame(entryFrame)
                elements = driver.find_elements(By.CLASS_NAME, "Hazns")
                count += 1
                continue

            visited_cafe_names = get_reviewed_cafe_names(attempt)
            nickname = driver.find_element(By.CLASS_NAME, "_2SUqsh")
            nickname_visited_dict[nickname.text] = visited_cafe_names

            driver.close()
            driver.switch_to.window(window_name = driver.window_handles[0])
            driver.switch_to.default_content()
            driver.switch_to.frame(entryFrame)
            elements = driver.find_elements(By.CLASS_NAME, "Hazns")
            count += 1
    except Exception as e:
        return -1
    return nickname_visited_dict

def doScrollDown(attempt):
    for _ in range(attempt):
        driver.execute_script('window.scrollTo(0, document.body.scrollHeight);')
        time.sleep(0.2)


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

input_file = "cafe_info_hexa7.csv"
output_file = "cafe_reviewshexa7.csv"


df = pd.read_csv(input_file)


# print(df["사업장명"])

cafe_name_list = df["사업장명"].to_list()
cafe_name_review_list = []
# cafe_name_list = list()
# cafe_name_list.append("소소우드")
review_list = list()
user_name_set = set()
nickname_visited_dict = dict()


for cafe_name in cafe_name_list:
    cafe_name_global = cafe_name
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
            
            
            
            returned_nickname_visited_dict = get_review()

            driver.switch_to.default_content()

        elif checkResult ==1:  #결과가 바로 식당정보로 나옴
            #바로 정보뽑기
            driver.switch_to.default_content()
            WebDriverWait(driver,4).until(
                EC.presence_of_element_located((By.ID, entryFrame)))
            driver.switch_to.frame(entryFrame)

            returned_nickname_visited_dict = get_review()
            
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
                
                returned_nickname_visited_dict = get_review()

            else:
                # keyword_count_dict = "검색결과 없음"
                # cafe_name_review_list.append([cafe_name, keyword_count_dict])
                driver.switch_to.default_content()
                driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER) #검색창 클리어
                continue

    except Exception as e:
        print("error: "+str(cafe_name_list.index(cafe_name))+"번 데이터",e)
        keyword_count_dict = "리뷰 없음"
    
    driver.switch_to.default_content()
    
    driver.switch_to.window(window_name = driver.window_handles[0])
    driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER) #검색창 클리어

    if returned_nickname_visited_dict == -1:
        continue

    nickname_visited_dict.update(returned_nickname_visited_dict)

cafe_name_review_df = pd.DataFrame.from_dict(nickname_visited_dict, orient='index')

cafe_name_review_df.to_csv(output_file, encoding="utf-8-sig")