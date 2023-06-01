import requests
from requests.auth import HTTPBasicAuth
def current_weather():
    pass
def access():
    api_key = "1d253ef1d23a40f1990142008233005"
    api_url = "'http://api.weatherapi.com/v1/current.json?key="+api_key
    return api_url
def get_weather():
    base_url = access()
    headers = {    
                'Content-Type': 'application/json'
    }
    response = requests.get(base_url)
    print(response)
get_weather()