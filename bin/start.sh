#!/bin/bash

#Description: 项目启动脚本
#version: v1.0.0
#author: ShiJinPu
#date: 2020/11/23

SERVER_NAME="lottery"
SERVER_PORT=9099
JAVA_OPTS=" -server -Xmx2g -Xms2g -XX:MetaspaceSize=256m -Xss256k -XX:+UseG1GC -XX:MaxGCPauseMillis=20 -XX:InitiatingHeapOccupancyPercent=35 "
JAR_NAME="lottery-server-1.0.jar"
LOG_NAME="lottery-server-1.0.log"
LOG_NAME_PRE="lottery-server-1.0"
LOG_BACK_NAME="log_bak"


#判断jar包是否已运行
PIDS=`ps -ef | grep java | grep "$SERVER_NAME" |grep -v grep|awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "ERROR: The $SERVER_NAME already started!"
    echo "PID: $PIDS"
    exit 1
fi

#判断端口是否被占用
if [ -n "$SERVER_PORT" ]; then
    SERVER_PORT_COUNT=`netstat -tln | grep $SERVER_PORT | wc -l`
    if [ $SERVER_PORT_COUNT -gt 0 ]; then
        echo "ERROR: The $SERVER_NAME port $SERVER_PORT already used!"
        exit 1
    fi
fi

#判断日志备份文件夹是否存在，不存在在创建新文件夹
echo -e "Backup log ......"
if [ ! -d "${LOG_BACK_NAME}" ]; then
    mkdir "${LOG_BACK_NAME}"
fi

#将日志移到log_bak文件夹
echo -e "Backup log to ${LOG_BACK_NAME}..."
if [ -f "log/${LOG_NAME}" ]; then
    DATE=`date +%Y%m%d%H%M%S`
    mv "log/${LOG_NAME}" "log/${LOG_NAME_PRE}${DATE}.log"
    mv "log/${LOG_NAME_PRE}${DATE}.log" "${LOG_BACK_NAME}"
fi

#创建log目录
if [ ! -d "log" ]; then
    mkdir "log"
fi

#启动jar包
echo -e "Starting the $SERVER_NAME ..."
nohup java $JAVA_OPTS -jar $JAR_NAME --server.port=$SERVER_PORT >log/$LOG_NAME 2>&1 &

#判断jar包是否启动成功
sleep 5
if [ -n "`ps -ef | grep java | grep "$SERVER_NAME" |grep -v grep|awk '{print $2}'`" ];then
        echo "start ok!"
        echo "$SERVER_NAME PID:`ps -ef | grep java | grep "$SERVER_NAME" |grep -v grep|awk '{print $2}'`"
else
        echo "start failed!"
        echo "ERROR log:"
        tail -100 log/$LOG_NAME
        exit 1
fi
