#!/bin/sh
. ./commons.sh
stopProcess cn.aijiamuyingfang.server.goods
stopProcess cn.aijiamuyingfang.server.shopcart
stopProcess cn.aijiamuyingfang.server.order
stopProcess cn.aijiamuyingfang.server.user
stopProcess cn.aijiamuyingfang.server.coupon
stopProcess cn.aijiamuyingfang.server.wxservice
echo 'stop nginx'
service nginx stop