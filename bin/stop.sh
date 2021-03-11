#!/bin/sh

#Description: 项目停止脚本
#version: v1.0.0
#author: ShiJinPu
#date: 2020/11/23

SERVER_NAME="lottery"

PIDS=`ps -ef | grep java | grep "$SERVER_NAME" |awk '{print $2}'`
if [ -z "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME does not started!"
    exit 0
fi

echo -e "Stopping the $SERVER_NAME ...\c"
for PID in $PIDS ; do
    kill $PID > /dev/null 2>&1
done


COUNT=0
while [ $COUNT -lt 1 ]; do
    echo -e ".\c"
    sleep 3
    COUNT=1
    for PID in $PIDS ; do
        PID_EXIST=`ps -f -p $PID | grep java`
        if [ -n "$PID_EXIST" ]; then
            COUNT=0
            ps -ef|grep $SERVER_NAME|grep -v grep |awk '{print $2}'|xargs kill -9
            break
        fi
    done
done

echo "stop ok!"