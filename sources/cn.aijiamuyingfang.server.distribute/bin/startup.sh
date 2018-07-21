#!/bin/sh
declare WEAPP_OPTS
if [ $# > 0 ]&&[ -n "$1" ]; then
    WEAPP_OPTS="$WEAPP_OPTS --spring.profiles.active=$1"
else
    WEAPP_OPTS="$WEAPP_OPTS --spring.profiles.active=prod"
fi

SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)

. $SHELL_FOLDER/commons.sh
cd /root/weapp/services/goods/
stopProcess cn.aijiamuyingfang.server.goods
echo "nohup java -jar /root/weapp/services/goods/cn.aijiamuyingfang.server.goods-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/goods.log 2>&1 &"
nohup java -jar /root/weapp/services/goods/cn.aijiamuyingfang.server.goods-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/goods.log 2>&1 &

cd /root/weapp/services/shopcart
stopProcess cn.aijiamuyingfang.server.shopcart
echo "nohup java -jar /root/weapp/services/shopcart/cn.aijiamuyingfang.server.shopcart-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shopcart.log 2>&1 &"
nohup java -jar /root/weapp/services/shopcart/cn.aijiamuyingfang.server.shopcart-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shopcart.log 2>&1 &

cd /root/weapp/services/shoporder
stopProcess cn.aijiamuyingfang.server.order
echo "nohup java -jar /root/weapp/services/shoporder/cn.aijiamuyingfang.server.shoporder-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shoporder.log 2>&1 &"
nohup java -jar /root/weapp/services/shoporder/cn.aijiamuyingfang.server.shoporder-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shoporder.log 2>&1 &

cd /root/weapp/services/user
stopProcess cn.aijiamuyingfang.server.user
echo "nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.user-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/user.log 2>&1 &"
nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.user-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/user.log 2>&1 &

cd /root/weapp/services/coupon
stopProcess cn.aijiamuyingfang.server.coupon
echo "nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.coupon-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/coupon.log 2>&1 &"
nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.coupon-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/coupon.log 2>&1 &

cd /root/weapp/services/wxservice
stopProcess cn.aijiamuyingfang.server.wxservice
echo "nohup java -jar /root/weapp/services/wxservice/cn.aijiamuyingfang.server.wxservice-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/wxservice.log 2>&1 &"
nohup java -jar /root/weapp/services/wxservice/cn.aijiamuyingfang.server.wxservice-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/wxservice.log 2>&1 &

echo "service nginx restart"
service nginx restart