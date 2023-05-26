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

#찾아야하는 태그 목록들
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

NO_INFO = "NO INFO"
class Infos:
    cafe_name : str = "초기값"
    rating : str = "초기값"
    visitor_review_count : str = "초기값"
    blog_review_count : str = "초기값"
    cafe_address : str = "초기값"
    business_hour : str = "초기값"
    phone_number : str = "초기값"
    linked_hypherlink : str = "초기값"
    description : str = "초기값"
    menus : dict = "초기값"
    keyword_count_dict : dict = "초기값"
    
        
    

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
        
def getInfo() -> Infos:
    # html = driver.page_source 
    infos = Infos()
    
    infos = get_basic()

    keywords = get_keyword()[1:-3]
    keyword_count_dict = keywords_process(keywords)
    infos.keyword_count_dict = keyword_count_dict

    # infos.menus = get_menu()
    infos.menus = "공사중"

    return infos

    

    
def get_basic() -> Infos:
    infos = Infos()
    infos.cafe_name = driver.find_element(By.CLASS_NAME, "Fc1rA").text
    
    info_str_list = list(driver.find_element(By.CLASS_NAME, "dAsGb").text)

    for i in range(len(info_str_list)):
        if info_str_list[i].isdigit() and info_str_list[i + 1].isalpha():
            info_str_list.insert(i + 1, "\n")
        elif info_str_list[i] == " " and info_str_list[i + 1].isdigit():
            info_str_list[i] = "\n"

    info_str = ''.join(map(str, info_str_list))
    info_list = info_str.split("\n")
    info_dict = dict()

    for i in range(len(info_list) // 2):
        info_dict[info_list[i * 2]] = info_list[i * 2 + 1]
    

    if "별점" in info_dict:
        infos.rating = info_dict["별점"][:-2]
    if "방문자리뷰" in info_dict:
        infos.visitor_review_count = int(info_dict["방문자리뷰"].replace(',', ''))
    if "블로그리뷰" in info_dict:
        infos.blog_review_count = int(info_dict["블로그리뷰"].replace(',', ''))

    infos.cafe_address = driver.find_element(By.CLASS_NAME, address_classname).text
    
    arrow_list = list(driver.find_elements(By.CLASS_NAME, "_UCia"))

    try:
        for i in range(1, len(arrow_list)):
            time.sleep(0.3)
            arrow_list[i].click()
        try:
            buisiness_hour_list = list(driver.find_elements(By.CLASS_NAME, business_hour_classname))
            
            if len(buisiness_hour_list) < 1:
                infos.business_hour = driver.find_element(By.CLASS_NAME, "H3ua4").text
            else:
                infos.business_hour = buisiness_hour_list[0].text
        except:
            infos.business_hour = NO_INFO
    except:
        infos.business_hour = NO_INFO

    try:
        infos.phone_number = driver.find_element(By.CLASS_NAME, phone_number_classname).text
    except:
        infos.phone_number = NO_INFO

    try:
        infos.linked_hypherlink = driver.find_element(By.CLASS_NAME, linked_hypherlink_classname).text
    except:
        infos.linked_hypherlink = NO_INFO

    try:
        try:
            driver.find_element(By.CLASS_NAME, "rvCSr").click()
        except:
            pass

        infos.description = driver.find_element(By.CSS_SELECTOR, description_selector).text
    except Exception as e:
        infos.description = NO_INFO

    return infos

# def get_menu():
#     designate_tab_and_click("메뉴")
#     drink_dict = dict()

    
#     for menu_keyword in menu_keyword_set:
#         try:
#             WebDriverWait(driver, timeout = 1).until(
#             EC.presence_of_element_located((By.CLASS_NAME, menu_keyword)))

#             if menu_keyword == "store_delivery":
#                 menu_list = driver.find_elements(By.CLASS_NAME, menu_keyword)


#             break
#         except:
#             continue
    
    
    

def designate_tab_and_click(tab_name : str) -> webdriver.Chrome():
    tabs = driver.find_elements(By.CSS_SELECTOR, review_tab_selector)
    for tab in tabs:
        if tab.text == tab_name:
            tab.click()
            break

def get_keyword():
    keyword_text_undeddited = ""

    designate_tab_and_click("리뷰")

    while True:
        try:
            WebDriverWait(driver,1.5).until(\
                EC.presence_of_element_located((By.CLASS_NAME, arrow_mark_classname)))
            driver.find_element(By.CLASS_NAME, arrow_mark_classname).click()
        except:
            keyword_text_undeddited = driver.find_element(\
                By.CLASS_NAME, review_keywords_classname).text
            break

    return keyword_text_undeddited.split("\n")

def keywords_process(keywords : list[str]) -> dict:
    keyword_count_dict = dict()
    if keywords[0][1:-1] not in keyword_set:
         return "리뷰 수 부족"
    for i in range(len(keywords) // 3):
        keyword_count_dict[keywords[i * 3][1:-1]] = keywords[i * 3 + 2]

    return keyword_count_dict
    
keyword_set = {'커피가 맛있어요', '인테리어가 멋져요', '주차하기 편해요', '디저트가 맛있어요',
                '음료가 맛있어요', '매장이 청결해요', '대화하기 좋아요', '친절해요',
                  '좌석이 편해요', '화장실이 깨끗해요', '사진이 잘 나와요', '뷰가 좋아요',
                    '집중하기 좋아요', '특별한 메뉴가 있어요', '가성비가 좋아요'}

basic_info_set = {'별점', '방문자리뷰', '블로그리뷰'}

menu_keyword_set = {'store_delivery', 'd_menu_list', 'P_Yxm'}
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

input_file = "cafe_names4.csv"
output_file = "cafe_infos4.csv"


df = pd.read_csv(input_file)


# print(df["사업장명"])

phone_num_set = set()
cafe_name_list = df["카페이름"].to_list()
cafe_name_review_list = list()
cafe_name_global = str()
for cafe_name in cafe_name_list:
    cafe_name_global = cafe_name
    flag = True
    checkResult = -1
    infos = Infos()
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
            
            infos = getInfo()
            
            driver.switch_to.default_content()
        elif checkResult ==1:  #결과가 바로 식당정보로 나옴
            #바로 정보뽑기
            driver.switch_to.default_content()
            WebDriverWait(driver,4).until(
                EC.presence_of_element_located((By.ID, entryFrame)))
            driver.switch_to.frame(entryFrame)

            infos = getInfo()
            
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
                
                infos = getInfo()
            else:
                keyword_count_dict = "검색결과 없음"
                cafe_name_review_list.append([cafe_name, keyword_count_dict])
                driver.switch_to.default_content()
                driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER) #검색창 클리어
                continue
    except Exception as e:
        print("error: "+str(cafe_name_list.index(cafe_name))+"번 데이터",e)
        keyword_count_dict = "리뷰 없음"
    
    driver.switch_to.default_content()
    driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER) #검색창 클리어
    if infos.phone_number in phone_num_set and infos.phone_number != NO_INFO and infos.phone_number != "초기값":
        continue
    else:
        phone_num_set.add(infos.phone_number)
        cafe_name_review_list.append([cafe_name, infos.cafe_name, infos.rating, infos.visitor_review_count,
                                  infos.blog_review_count, infos.cafe_address, infos.business_hour, \
                                    infos.phone_number, infos.linked_hypherlink, infos.description, \
                                        infos.menus, infos.keyword_count_dict])


cafe_name_review_df = pd.DataFrame(cafe_name_review_list, columns=["사업장명", "카페이름", \
                        "별점", '방문자리뷰수', '블로그리뷰수', '주소', \
                            '영업시간', '전화번호', '연결사이트', '설명',\
                                '메뉴', '키워드'])

cafe_name_review_df.to_csv(output_file, encoding="utf-8-sig")