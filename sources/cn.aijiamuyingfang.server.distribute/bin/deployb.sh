#!/bin/sh
. $SHELL_FOLDER/commons.sh

stopProcess cn.aijiamuyingfang.server.goods
echo "nohup java -jar /root/weapp/services/goods/cn.aijiamuyingfang.server.goods-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/goods.log 2>&1 &"
nohup java -jar /root/weapp/services/goods/cn.aijiamuyingfang.server.goods-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/goods.log 2>&1 &

stopProcess cn.aijiamuyingfang.server.shopcart
echo "nohup java -jar /root/weapp/services/shopcart/cn.aijiamuyingfang.server.shopcart-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shopcart.log 2>&1 &"
nohup java -jar /root/weapp/services/shopcart/cn.aijiamuyingfang.server.shopcart-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shopcart.log 2>&1 &

stopProcess cn.aijiamuyingfang.server.wxservice
echo "nohup java -jar /root/weapp/services/wxservice/cn.aijiamuyingfang.server.wxservice-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/wxservice.log 2>&1 &"
nohup java -jar /root/weapp/services/wxservice/cn.aijiamuyingfang.server.wxservice-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/wxservice.log 2>&1 &