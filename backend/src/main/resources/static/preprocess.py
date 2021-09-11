import json

nameList = ["biology", "chemistry", "chinese", "english", "geo", "history", "math", "physics", "politics"]
dict = []

for name in nameList:
    print(name)
    with open(name + "_instanceName.json", 'r') as f:
        params = json.load(f)
        for str in params:
            if " " in str:
                params.remove(str)
                print(str)
        dict = params
        f.close()
    with open(name + "_instanceName.json", 'w') as f:
        json.dump(dict, f)
        f.close()