#!/bin/bash

# 安装openbridge-jenkins
# wangxinxiang@yihecloud.com
# 2016-07-26

OPTS=$(getopt -o : --long registry:,data_dir:,nginx_ip:,svn_user:,svn_pwd:,sonar_user:,sonar_pwd:: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="1.651.3"
data_dir="/data/jenkins"

svn_user="svn"
svn_pwd="svn"
sonar_user="admin"
sonar_pwd="admin"

nginx_ip=

while true; do
  case "$1" in
  --registry) registry=$2; shift 2;;
  --data_dir) data_dir=$2; shift 2;;
  --nginx_ip)   nginx_ip=$2;   shift 2;;
  --svn_user)   svn_user=$2;   shift 2;;
  --svn_pwd)    svn_pwd=$2;    shift 2;;
  --sonar_user) sonar_user=$2; shift 2;;
  --sonar_pwd)  sonar_pwd=$2;  shift 2;;
  --) shift; break;;
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
  --registry <docker registry>, default: docker.yihecloud.com:443
  --data_dir <jenkins data directory>, default: /data/jenkins
  --nginx_ip <nginx server ip>, eg: 192.168.1.71 ,use the internal network IP.
  --svn_user <svn server username>, default: svn
  --svn_pwd <svn server password>, default: svn
  --sonar_user <sonar server username>, default: admin
  --sonar_pwd <sonar server password>, default: admin
"
}

# check options
check_opt "registry"
check_opt "nginx_ip"


# run docker image
app_uri=http://$nginx_ip/app
api_uri=http://$nginx_ip/api
app_svn=http://$nginx_ip/svn1
api_svn=http://$nginx_ip/svn2
sonar_url=http://$nginx_ip/sonar

docker run -dti --name jenkins \
  --restart=always \
  -e APP_SVN_SERVER_URI=$app_svn \
  -e APP_SERVER_URI=$app_uri \
  -e API_SVN_SERVER_URI=$api_svn \
  -e API_SERVER_URI=$api_uri \
  -e SONAR_HOST_URL=$sonar_url \
  -e SONAR_LOGIN=$sonar_user \
  -e SONAR_PASSWORD=$sonar_pwd \
  -e SONAR_SVN_USERNAME=$svn_user \
  -e SONAR_SVN_PASSWORD=$svn_pwd \
  -v $data_dir:/mnt/jenkins_home \
  -p 8080:8080 \
  -p 50000:50000 \
  $registry/openbridge/jenkins:$version

# show status
docker ps |grep "sonar"
