import selenium
import pandas as pd
from selenium import webdriver
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.support import expected_conditions as EC #selenium에서 사용할 모듈 import
from bs4 import BeautifulSoup
from selenium.common.exceptions import NoSuchElementException,StaleElementReferenceException,TimeoutException

####크롤링중 에러가 발생했다면 다시 실행해 주세요#####
##############################################
maxRun = 100000 #<-한번에 돌릴양
##############################################
fileIndex = "4"  #<--여기에 파일 이름에 붙어있는 숫자 입력해 주세요 ex) 음식점0.csv 이면 0입력, 음식점_서울0 이면 _서울0
##############################################
folderPath = "./활동데이터" #<-------여기에 파일이 있는 폴더 위치 지정해주세요
##############################################
filePath = folderPath+"/서울구미"+str(fileIndex)+".csv"
data = pd.read_csv(filePath,encoding="utf-8")
driver = webdriver.Chrome(ChromeDriverManager().install())
driver.get("https://map.naver.com/v5/")
try:
   element = WebDriverWait(driver, 10).until(
       EC.presence_of_element_located((By.CLASS_NAME, "input_search"))
   ) #입력창이 뜰 때까지 대기
finally:
   pass

#찾아야하는 태그 목록들
resultListClass = "UEzoS"
#resultListTitleClass = "tzwk0"
resultListTitleClass = "P7gyV"
resultTargetTitleDiv = "YouOG"
#resultTitleClick = "#_pcmap_list_scroll_container > ul > li > div.CHC5F > a"
resultTitleClick = "#_pcmap_list_scroll_container > ul > li > div.ouxiq > a"
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

def search(flag):
    if flag:
        #address = row["도로명주소"].split(" ",maxsplit=1)
        address = row["주소"].split(" ",maxsplit=1)
        keyword = row["상호명"]+" "+address[1]
    else:
        driver.switch_to.default_content()
        #driver.find_element(By.CLASS_NAME,"button_clear").click()
        driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER)
        #address = row["도로명주소"].split(" ")
        #keyword = row["상호명"]+" "+address[1]+" "+address[2]
        keyword = row["상호명"]+" "+row["시군구주소"]
    search_box = driver.find_element(By.CLASS_NAME,"input_search")
    search_box.send_keys(keyword)
    search_box.send_keys(Keys.ENTER)
    
def checkIsList(checkCount):
    try:
        driver.switch_to.default_content()
        WebDriverWait(driver,1.5).until(
        EC.presence_of_element_located((By.ID, searchFrame))
        )
        driver.switch_to.frame(searchFrame)    
        WebDriverWait(driver,1).until(
            EC.presence_of_element_located((By.CLASS_NAME, resultListTitleClass))
        )
        driver.switch_to.default_content()
        return 0
    except TimeoutException:
        #검색결과 바로 식당정보로 이동했을때
        try:
            driver.switch_to.default_content()
            WebDriverWait(driver,1.5).until(
            EC.presence_of_element_located((By.ID, entryFrame))
            )
            driver.switch_to.frame(entryFrame)
            WebDriverWait(driver,1).until(
                EC.presence_of_element_located((By.CLASS_NAME, resultTargetTitleDiv))
            )
            driver.switch_to.default_content()
            return 1
        #검색결과가 없을때
        except TimeoutException:
            
            #처음이면 검색어를 다시 구성해서 검색
            if(checkCount==0):
                return 2
            #검색어를 다시 구성했는데도 없으면 없는가게
            elif(checkCount==1):
                return 3
            
def getInfo(row):
    #todo
    #영업시간 가져오기
    timeStr =data.loc[idx,"영업시간"] if type(data.loc[idx,"영업시간"]) is str else ""
    telStr=data.loc[idx,"전화번호"] if type(data.loc[idx,"전화번호"]) is str else ""
    descStr=data.loc[idx,"설명"] if type(data.loc[idx,"설명"]) is str else ""
    reviewStr=data.loc[idx,"리뷰"] if type(data.loc[idx,"리뷰"]) is str else ""
    ratingStr=data.loc[idx,"별점"] if type(data.loc[idx,"별점"]) is str else ""
    imgUrl=data.loc[idx,"사진"] if type(data.loc[idx,"사진"]) is str else ""
    try:
        #driver.find_element(By.CSS_SELECTOR,timeOpenerSelector).click()
        driver.find_element(By.CSS_SELECTOR,timeOpenerSelector).send_keys(Keys.ENTER)
    except NoSuchElementException:
        pass
    try:
        #driver.find_element(By.CSS_SELECTOR,descOpenerSelector).click()
        driver.find_element(By.CSS_SELECTOR,descOpenerSelector).send_keys(Keys.ENTER)
    except NoSuchElementException:
        pass
    
    html = driver.page_source
    soup = BeautifulSoup(html,'html.parser')
    
    try:
        WebDriverWait(driver,2).until(
            EC.presence_of_element_located((By.CLASS_NAME, addressSpan))
        )
        address = soup.find("span",addressSpan).text
    except (NoSuchElementException,TimeoutException):
        data.loc[idx,"리뷰"] = "noResult"
        print(str(idx)+". "+data.loc[idx,"상호명"]+" no result")
        return
    #if data.loc[idx,"행정동주소"].split(" ")[1] not in address:
    if data.loc[idx,"시군구주소"].split(" ")[1] not in address:
        data.loc[idx,"리뷰"] = "noResult"
        print(str(idx)+". "+data.loc[idx,"상호명"]+" no result")
        return
        
    #시간가져오기
    #time = soup.select("#app-root > div > div > div > div:nth-child(6) > div > div.place_section.no_margin.vKA6F > div > ul > li.SF_Mq.Sg7qM > div > a > div > div > span > div")
    day = soup.find_all("span",daySpan)
    time = soup.find_all("div",timeDiv)
    if(len(day)!=len(time)):
        time = soup.find_all("span",timeDiv)
    
    for t in range(len(day)):
        timeStr = ""
        for br in time[t].find_all("br"):
            br.replace_with("||")
        timeStr = timeStr+ day[t].text+": "+time[t].text+"//"

    #전화번호 가져오기
    tel= soup.find_all("span",telSpan)
    for i in range(len(tel)):
        telStr =  tel[i].text+".||."
    #소개 가져오기
    desc = soup.find_all("span",descSpan)
    for i in range(len(desc)):
        for br in desc[i].find_all("br"):
            br.replace_with(".||.")
        descStr = descStr + desc[i].text+".||."
        
    #리뷰탭으로 이동
    menuIndex = len(soup.find_all("a", "_tab-menu"))
    reviewTabOpenerSelector = "#app-root > div > div > div > div.place_fixed_maintab > div > div > div > div > a:nth-child("+str(menuIndex-1)+")"
    #reviewTabOpenerSelector = "#app-root > div > div > div > div.place_fixed_maintab.place_stuck.place_tab_shadow > div > div > div > div > a:nth-child(
    
    try:
        #driver.find_element(By.CSS_SELECTOR,reviewTabOpenerSelector).click()
        driver.find_element(By.CSS_SELECTOR,reviewTabOpenerSelector).send_keys(Keys.ENTER)
        WebDriverWait(driver,2).until(
            EC.presence_of_element_located((By.CLASS_NAME, reviewSpan))
        )
        reviewOpeners = driver.find_elements(By.CLASS_NAME,reviewOpenerA)
        for opener in reviewOpeners:
            #opener.click()
            opener.send_keys(Keys.ENTER)
        html = driver.page_source
        soup = BeautifulSoup(html,'html.parser')
        reviews = soup.find_all("span",reviewSpan)
    except (TimeoutException, NoSuchElementException):
        reviews = []
    finally:
        html = driver.page_source
        soup = BeautifulSoup(html,'html.parser')
    for i in range(len(reviews)):
        reviewStr = reviewStr+ reviews[i].text +"[>*}"
        
    try:
        ratingStr = soup.find("svg",ratingSvg).next_sibling.next_sibling.text
    except:
        pass
    imgUrl = soup.find("div",imgDiv)
       
    data.loc[idx,"영업시간"] = timeStr.replace(",","").replace("\n",".||.")
    data.loc[idx,"전화번호"] = telStr.replace(",","").replace("\n",".||.")
    data.loc[idx,"설명"] = descStr.replace(",","").replace("\n",".||.")
    data.loc[idx,"리뷰"] = reviewStr.replace(",","").replace("\n",".||.")
    data.loc[idx,"별점"] = ratingStr.replace(",","").replace("\n",".||.")
    try:
        data.loc[idx,"사진"] = imgUrl.attrs["style"].split('"')[1]
    except AttributeError:
        data.loc[idx,"사진"] = ""
    print(str(idx)+". " +data.loc[idx,"상호명"]+" find")


cnt=0
for idx,row in data.iterrows():
#for idx in range(len(data)):
    
    if cnt == maxRun:
        break
    
    if type(data.loc[idx,"리뷰"]) is str:
        continue
    flag = True
    checkResult = -1
    try:
        for i in range(2):     
            search(flag)
            checkResult = checkIsList(i)
            if checkResult == 0:   #결과가 리스트로 나옴
                #todo
                #맞는지 검사하고 맞으면 크롤링 진행, 틀리면 
                #선택하고
                driver.switch_to.default_content()
                driver.switch_to.frame(searchFrame)
                #driver.find_element(By.CSS_SELECTOR,resultTitleClick).click()
                driver.find_element(By.CSS_SELECTOR,resultTitleClick).send_keys(Keys.ENTER)
                driver.switch_to.default_content()
                WebDriverWait(driver,4).until(
                EC.presence_of_element_located((By.ID, entryFrame))
                )
                driver.switch_to.frame(entryFrame)
                getInfo(row)
                driver.switch_to.default_content()
                break
            elif checkResult ==1:  #결과가 바로 식당정보로 나옴
                #바로 정보뽑기
                driver.switch_to.default_content()
                WebDriverWait(driver,4).until(
                    EC.presence_of_element_located((By.ID, entryFrame))
                )
                driver.switch_to.frame(entryFrame)
                getInfo(row)
                break
            elif checkResult ==2:  #처음인데 결과가 안나옴
                flag = False
                continue
            elif checkResult ==3: #두번째인데도 결과가 안나옴
                data.loc[idx,"리뷰"] = "noResult"
                print(str(idx)+". "+data.loc[idx,"상호명"]+" no result")
    except Exception as e:
        print("error: "+str(idx)+"번 데이터",e)
        data.loc[idx,"리뷰"] = "error"
    #다음 검색을 위해 검색어 지우기
    driver.switch_to.default_content()
    driver.find_element(By.CLASS_NAME,"button_clear").send_keys(Keys.ENTER)
    #driver.find_element(By.CLASS_NAME,"button_clear").click()
    cnt+=1
    if cnt%500==0:
        print("저장")
        data.to_csv(filePath,mode='w',index=False, encoding="utf-8")
data.to_csv(filePath,mode='w',index=False, encoding="utf-8")
####크롤링 한거까지 저장, 크롤링 끝내고 꼭 눌러주세요 ####
###############################################################


data.to_csv(filePath,mode='w',index=False, encoding="utf-8")