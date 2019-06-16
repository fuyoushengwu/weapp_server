#!/bin/sh
ALL_WEAPP_IMAGES=`docker images --filter=reference='registry.cn-hangzhou.aliyuncs.com/fuyoushengwu/*:*' --format={{.Repository}}:{{.Tag}}`
docker rmi $ALL_WEAPP_IMAGES

SHELL_FOLDER=$(cd "$(dirname "$0")";pwd)
POM_FILE=$SHELL_FOLDER/../sources/pom.xml
WEAPP_DATA=/root/weapp.tar.gz
rm -rf $WEAPP_DATA
mvn clean install -Dmaven.test.skip=true -f $POM_FILE

LATEST_WEAPP_IMAGES=`docker images --filter=reference='registry.cn-hangzhou.aliyuncs.com/fuyoushengwu/*:latest' --format={{.Repository}}:{{.Tag}}`
docker save $LATEST_WEAPP_IMAGES -o $WEAPP_DATA


nodes="192.168.0.103 192.168.0.104 192.168.0.105 192.168.0.106"
for node in $nodes
do
    ssh root@$node "rm -rf $WEAPP_DATA"
    for image in $LATEST_WEAPP_IMAGES
    do
        ssh root@$node "docker rmi $image"
    done 
    scp $WEAPP_DATA root@$node:/root/
    ssh root@$node "docker load -i $WEAPP_DATA"
done