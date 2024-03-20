import requests
from requests.auth import HTTPBasicAuth
import geocoder

 

g = geocoder.ip('me')
print(g.latlng)
def current_weather():
    pass
def access():
    api_key = "95723526089a152f6fc08159191af257"
    api_url = "'http://api.weatherapi.com/v1/current.json?key="+api_key
    return api_url
def get_weather():
    base_url = access()
    headers = {    
                'Content-Type': 'application/json'
    }
    response = requests.get(base_url)
    print(response)
