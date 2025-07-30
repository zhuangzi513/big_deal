import jvQuant
import logging
from dataclasses import dataclass, asdict

def logHandle(log: str):
    print("应用日志:", log)


def dataHandle(data: str):
    print("Binary解析结果:", data)


def ab_lv1_handle(lv1: jvQuant.websocket_client.parse.AbLv1):
    print("level1推送:", lv1.get_map())


def ab_lv2_handle(lv2: jvQuant.websocket_client.parse.AbLv2):
    #print("level2推送:", lv2.get_map())
    print("level2: ", string(lv2))


def ab_lv10_handle(lv10: jvQuant.websocket_client.parse.AbLv10):
    print("十档推送:", lv10.get_map())


market = "ab"
token = "f250cf505bce71336b416ab9e9747e93"
log_level = logging.DEBUG
# log_level = logging.INFO

ws = jvQuant.websocket_client

#wsclient = ws.Construct(market=market, token=token, log_level=log_level, log_handle=logHandle, data_handle=dataHandle,
#                        ab_lv1_handle=ab_lv1_handle, ab_lv2_handle=ab_lv2_handle, ab_lv10_handle=ab_lv10_handle)
wsclient = ws.Construct(market=market, token=token, log_level=log_level, ab_lv2_handle=ab_lv2_handle)

#wsclient.add_lv1(["600519", "000001", "i000001"])
wsclient.add_lv2(["600519", "000001", "i000001"])
#wsclient.add_lv10(["600519", "000001", "i000001"])

wsclient.thread_join()
