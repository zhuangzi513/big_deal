import requests
import websocket
import json
import zlib
import time

import jvQuant as jv
import pandas as pd


token = "f250cf505bce71336b416ab9e9747e93"
year = 2024
filename = "2024_lv2_data.zip"

def download_history_lv2_data(token, year):
    timestamp = int(time.time())
    url = f"http://jvquant.com/query/history?stamp={timestamp}&token={token}&year={year}.zip"
    #response = requests.get(url)
    print("url:", url)
    response = requests.get(url, stream=True)
    if response.status_code == 206:
        print("206")
        with open(filename, 'ab') as f:
            for chunk in response.iter_content(chunk_size=1024):
                if chunk:
                    f.write(chunk)
    elif response.status_code == 200:
        print("200")
        with open(filename, 'wb') as f:
            for chunk in response.iter_content(chunk_size=1024):
                print("writ 1024")
                if chunk:
                    f.write(chunk)
    else:
        print(f"Failed to download: HTTP {response.status_code}")

def get_server_address():
    url = f"http://jvquant.com/server?market=ab&type=websocket&token={token}"
    print("get server address:", url)
    response = requests.get(url)
    if response.status_code == 200:
        return response.json()["server"]
    else:
        raise Exception("Failed to get server address")

def connect_websocket():
    server_address = get_server_address()
    print("server_addr:", server_address)
    ws = websocket.WebSocket()
    print("connect web socket:")
    print(f"{server_address}?token={token}")
    ws.connect(f"{server_address}?token={token}")
    return ws

def subscribe_lv2(ws, codes):
    for code in codes:
        ws.send(f"add=lv2_{code}")

def get_today_lv2_data(code):
    today = datetime.now().strftime("%Y%m%d")
    
    try:
        lv2_snapshot = jv.l2_quote(code, today)
        
        lv2_trans = jv.l2_trans(code, today)
        
        lv2_orders = jv.l2_order(code, today)
        
        lv2_ten_levels = jv.l2_ten(code, today)
        
        return {
            "snapshot": lv2_snapshot,
            "transactions": lv2_trans,
            "orders": lv2_orders,
            "ten_levels": lv2_ten_levels
        }
    except Exception as e:
        return None

def parse_lv2_data(data):
    print(data)
    for line in data.split("\n"):
        if line.startswith("lv2_"):
            parts = line.split("=")
            code = parts[0].split("_")[1]
            trades = parts[1].split("|")
            for trade in trades:
                fields = trade.split(",")
                print(f"Code: {code}, Time: {fields[0]}, Trade ID: {fields[1]}, Price: {fields[2]}, Volume: {fields[3]}")

if __name__ == "__main__":
    #download_history_lv2_data(token, year)
    ws = connect_websocket()
    #lv2_data = get_today_lv2_data(stock_code)
    subscribe_lv2(ws, ["600519", "000001"])
    try:
        while True:
            data = ws.recv()
            if isinstance(data, bytes):
                string_data = data.decode('utf-8')
                print(string_data)
            else:
                print(data)
            #decompressed_data = zlib.decompress(data)
            #parse_lv2_data(decompressed_data.decode("utf-8"))
            #time.sleep(0.1)
    except KeyboardInterrupt:
        print("crash")
    finally:
        ws.close()
