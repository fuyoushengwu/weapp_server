#!/bin/sh
yues|cp -rf /root/weapp/nginx/* /etc/nginx/

. $SHELL_FOLDER/commons.sh

stopProcess cn.aijiamuyingfang.server.order
echo "nohup java -jar /root/weapp/services/shoporder/cn.aijiamuyingfang.server.shoporder-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shoporder.log 2>&1 &"
nohup java -jar /root/weapp/services/shoporder/cn.aijiamuyingfang.server.shoporder-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/shoporder.log 2>&1 &

stopProcess cn.aijiamuyingfang.server.user
echo "nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.user-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/user.log 2>&1 &"
nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.user-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/user.log 2>&1 &

stopProcess cn.aijiamuyingfang.server.coupon
echo "nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.coupon-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/coupon.log 2>&1 &"
nohup java -jar /root/weapp/services/user/cn.aijiamuyingfang.server.coupon-0.0.1-boot.jar $WEAPP_OPTS >/root/weapp/logs/coupon.log 2>&1 &

echo "service nginx restart"
service nginx restart