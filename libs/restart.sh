#!/bin/sh

echo "===  restart tomcat ===="
echo `dirname $0`
echo `pwd`

cd /opt/tomcat9/bin
echo `pwd`
#trap "`echo 'errror'`;" 1 2 3 8 9 14 15
sh shutdown.sh 2>/dev/null

if [ "$?" = "0" ]; then
     echo shutdown
else
     echo shutdown error
fi

sh clear.sh
echo cleared

sh startup.sh
echo "===  restart finished ==="
