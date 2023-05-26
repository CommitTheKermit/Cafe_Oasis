
import pandas as pd #csv를 읽고 dataframe을 사용하기 위한 pandas

def concatAll():
    dfs = list()
    
    for i in range(16):
        try:
            dfs.append(pd.read_csv(f"cafe_reviews{i}.csv", low_memory=False))
        except:
            continue

    dfAll = pd.concat(dfs, ignore_index=True)
    # dfAll = dfAll.drop(['Unnamed: 0'], axis=1)
    # dfAll = dfAll.sort_values(by=['주소'])
    print(dfAll.shape)
    
    print(dfAll.shape)
    dfAll.rename(columns={'Unnamed: 0':'user_name'}, inplace=True)
    dfAll = dfAll.drop_duplicates(['user_name'], keep='first')
    print(dfAll.shape)

    # for idx in dfAll.index:
    #     dfAll.loc[idx,'사업장명'] = dfAll.loc[idx,'사업장명'].split(sep=" ", maxsplit=1)[1]
    # for idx in dfAll.index:
    #     if dfAll.loc[idx,'키워드'] in ["초기값", "리뷰 수 부족"] or\
    #           dfAll.loc[idx, '카페이름'] == "검색결과 없음":
    #         dfAll.drop([idx], inplace=True)

    # dfAll.reset_index(inplace=True)
    # dfAll.drop(['index'], axis=1, inplace=True)
    # dfAll.drop_duplicates(['전화번호'], inplace=True, ignore_index=True)




    dfAll.to_csv("cafe_reviews_daegu.csv", encoding="utf-8-sig")

    return dfAll

def df_div_hexa(dfAll):
    maxIndex = dfAll.shape[0]
    
    tempIndex = int(maxIndex * (1 /16))
    dfTemp = dfAll.iloc[ 0: tempIndex ]
    dfTemp.to_csv('cafe_info_hexa0.csv', encoding="utf-8-sig")

    for i in range(1,16):
        startIndex = int(maxIndex * (i / 16))
        endIndex = int(maxIndex * ((i + 1) / 16))

        dfAll.iloc[startIndex : endIndex].to_csv(f"cafe_info_hexa{i}.csv", encoding="utf-8-sig")

dfAll = concatAll()
# df_div_hexa(dfAll)