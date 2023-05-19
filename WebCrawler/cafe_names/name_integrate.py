import selenium #셀레니움
import pandas as pd #csv를 읽고 dataframe을 사용하기 위한 pandas
import time
from bs4 import BeautifulSoup #브라우저 태그를 가져오고 파싱하기 위함
import re #정규식

input_file = "vil_name1.csv"
cafe_name_set = set()

for i in range(1, 5):
    df = pd.read_csv(f"cafe_names{i}.csv")
    vil_name_list = df["카페이름"].to_list()
    temp_set = set(vil_name_list)

    cafe_name_set = cafe_name_set.union(temp_set)
    
    

cafe_name_list = list(cafe_name_set)

cafe_name_review_df = pd.DataFrame(cafe_name_list, columns=["카페이름"])

cafe_name_review_df.to_csv("daegu_cafe_names.csv", encoding="utf-8-sig")