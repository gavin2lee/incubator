#!/bin/bash

# 安装paasos-os
# wangxinxiang@yihecloud.com
# 2016-07-25

OPTS=$(getopt -o h:P:u:p: --long registry:,svc_ips:: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="3.0"
db_port="3306"
svc_ips="172.20.0.2"

db_host=
db_user=
db_pswd=


while true; do
  case "$1" in
  -h) db_host=$2; shift 2;;
  -P) db_port=$2; shift 2;;
  -u) db_user=$2; shift 2;;
  -p) db_pswd=$2; shift 2;;
  --registry) registry=$2; shift 2;;
  --svc_ips) svc_ips=$2;   shift 2;;
  --) shift; break;;
  #*) echo "Internal error!" ; exit 1 ;;
  esac
done

function check_opt() {
  arg="\$$1"
  if [ "$(eval echo $arg)"  = "" ]; then
    echo "[ERROR] <$1> 参数缺失！"
    usage;
    exit 1
  fi
}

function usage() {
	echo "
Usage: $0 
  -h <db_host> 
  -P <db port>, default: 3306
  -u <db_user> 
  -p <db_pswd> 
  --registry <docker registry>, default: docker.yihecloud.com:443
  --svc_ips <kuberlet service ip range>, default: 172.20.0.2
"
}

# check options
check_opt "registry"
check_opt "db_host"
check_opt "db_user"
check_opt "db_port"
check_opt "db_pswd"

# run docker image
docker run -d --name paasos \
    --restart=always \
    -p 8080:8080 \
    -e DB_HOST=$db_host \
    -e DB_PORT=$db_port \
    -e DB_USER=$db_user \
    -e DB_PSWD=$db_pswd \
    -e OS_SVR_IPS=$svc_ips \
    -v /mnt/resource:/data/resource \
    -v /data/logs-tomcat:/opt/apache-tomcat-8.0.33/logs \
    -v /data/logs:/opt/logs \
    $registry/paasos/os:$version

# show status
docker ps |grep "paasos"
