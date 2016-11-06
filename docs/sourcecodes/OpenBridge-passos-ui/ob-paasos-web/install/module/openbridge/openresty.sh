#!/bin/bash

# 安装openbridge-openresty
# wangxinxiang@yihecloud.com
# 2016-07-26

OPTS=$(getopt -o h:P:u:p: --long registry:,data_dir:,logstash_ip:: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="2.0"
data_dir="/data/openresty"

logstash_ip=

while true; do
  case "$1" in
  --registry) registry=$2; shift 2;;
  --data_dir) data_dir=$2;   shift 2;;
  --logstash_ip) logstash_ip=$2; shift 2;;
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
  --data_dir <openresty data directory>, default: /data/openresty
  --logstash_ip <logstash server ip>, eg: 192.168.0.2
"
}

# check options
check_opt "registry"
check_opt "logstash_ip"

# run docker image
logstash_addr="$logstash_ip:5544"
mkdir -p $data_dir/config
mkdir -p $data_dir/logs

docker run -dti --name openresty \
  --restart=always \
  -e NGINX_WORKER_PROCESSES=1 \
  -e NGINX_DNS_RESOLVER=127.0.0.1 \
  -e DOCKER_LOGSTASH_IP=$logstash_addr \
  -v $data_dir:/data \
  -p 80:80 \
  $registry/openbridge/openresty:$version /program/startNginx.sh

# show status
docker ps |grep "openresty"

# for test.
curl -s http://127.0.0.1/

