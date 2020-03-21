#!/bin/sh

APP_NAME=WebConsole

start(){
rm -f tpid
nohup java -jar ${APP_NAME}.jar  >/dev/null 2>&1
echo $! > tpid
echo Start Success!

}
check(){
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
        echo 'App is running.'
else
        echo 'App is NOT running.'
fi
}
stop(){
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Stop Process...'
    kill -15 $tpid
fi
sleep 5
tpid=`ps -ef|grep $APP_NAME|grep -v grep|grep -v kill|awk '{print $2}'`
if [ ${tpid} ]; then
    echo 'Kill Process!'
    kill -9 $tpid
else
    echo 'Stop Success!'
fi
}

restart(){
  stop
  sleep 2
  start
}

case $1 in 
  start)
        start
        ;;
  stop)
        stop
        ;;
  check)
        check
        ;;
  restart)
        restart
        ;;
  *)
        echo "USAGE:$0 {start|stop|restart|check}"
        exit 1
esac