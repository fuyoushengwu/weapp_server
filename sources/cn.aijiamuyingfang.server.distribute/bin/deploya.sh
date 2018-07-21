#!/bin/sh
yues|cp -rf /root/weapp/nginx/* /etc/nginx/
mkdir -p /etc/nginx/static/work
mkdir -p /etc/nginx/static/images

SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
. $SHELL_FOLDER/commons.sh


stopProcess cn.aijiamuyingfang.server.goods
echo "nohup java -jar /root/weapp/services/goods/cn.aijiamuyingfang.server.goods-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/goods.log 2>&1 &"
nohup java -jar /root/weapp/services/goods/cn.aijiamuyingfang.server.goods-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/goods.log 2>&1 &

stopProcess cn.aijiamuyingfang.server.user
echo "nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.user-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/user.log 2>&1 &"
nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.user-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/user.log 2>&1 &

stopProcess cn.aijiamuyingfang.server.coupon
echo "nohup java -jar /root/weapp/services/coupon/cn.aijiamuyingfang.server.coupon-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/coupon.log 2>&1 &"
nohup java -jar /root/weapp/services/coupon/cn.aijiamuyingfang.server.coupon-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/coupon.log 2>&1 &

echo "service nginx restart"
service nginx restart