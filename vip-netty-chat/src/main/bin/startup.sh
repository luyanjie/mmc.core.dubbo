#!/bin/bash

## java env
## vim /etc/profile 查看环境变量中java的配置地址
export JAVA_HOME=/usr/local/java/jdk1.8.0_161
export JRE_HOME=$JAVA_HOME/jre

## service name
APP_NAME=service

SERVICE_DIR=/data/wwwroot/netty/$APP_NAME
SERVICE_NAME=vip-netty-chat

MAIN_CLASS="com.maochong.xiaojun.server.NettyServer"
PATH="classes"

process_Id=`/usr/sbin/lsof -i tcp:8011|/usr/bin/awk '{print $2}'|/usr/bin/sed '/PID/d'`
# PPID=`/usr/bin/cat $SERVICE_DIR/$PID`

LOGS=/data/wwwroot/netty/logs
CLASSPATH=${SERVICE_DIR}/$PATH

for i in $SERVICE_DIR/lib/*.jar;do
    CLASSPATH="$i:$CLASSPATH"
done

export CLASSPATH

PID=$SERVICE_NAME\.pid

cd $SERVICE_DIR

case "$1" in
    start)
        if [ $process_Id ];then
	   kill -9 $process_Id
        fi
        $JRE_HOME/bin/java -Xms256m -Xmx512m -classpath $CLASSPATH $MAIN_CLASS  >/dev/null 2>$1 &
       # $JRE_HOME/bin/java -Xms256m -Xmx512m -classpath $CLASSPATH $MAIN_CLASS  >${LOGS}/log.txt 2>${LOGS}/log.txt &

       #  echo $! > $SERVICE_DIR/$PID
        echo "=== start $SERVICE_NAME"
        ;;
    stop)
      # if [ $PPID ]
      # then
      #     kill `/usr/bin/cat $SERVICE_DIR/$PID`
      #    /usr/bin/rm -rf $SERVICE_DIR/$PID
     #  fi
       echo "=== stop $SERVICE_NAME"
       if [ $process_Id ];then
         kill -9 $process_Id
       fi
       # sleep 5
       # P_ID=`ps -ef | grep -w "$SERVICE_NAME" | grep -v "grep" | awk '{print $2}'`
       # if [ "$P_ID" == "" ]; then
       #     echo "=== $SERVICE_NAME process not exists or stop success"
       # else
       #     echo "=== $SERVICE_NAME process pid is:$P_ID"
       #     echo "=== begin kill $SERVICE_NAME process, pid is:$P_ID"
       #     kill -9 $P_ID
       # fi
        ;;

    restart)
        $0 stop
        sleep 2
        $0 start
        echo "=== restart $SERVICE_NAME"
        ;;

    *)
        ## restart
        $0 stop
        sleep 2
        $0 start
        ;;

esac
exit 0
