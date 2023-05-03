import requests, json
tmpData = {
    "id" : "222",
    "pw" : "111"
}
jsonData = json.dumps(tmpData)
r = requests.post("http://127.0.0.1:8000/users/login", data=jsonData)
print(r)

# with open("d:\\err.html", "w", encoding="utf-8") as f:
#    f.write(r.text)