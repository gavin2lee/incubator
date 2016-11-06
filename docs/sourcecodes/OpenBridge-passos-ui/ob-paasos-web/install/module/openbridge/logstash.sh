#!/bin/bash

# 安装openbridge-logstash
# wangxinxiang@yihecloud.com
# 2016-07-26

OPTS=$(getopt -o: --long registry:,data_dir:,elastic_ip:,nginx_ip:: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="2.3.1"
data_dir="/data/logstash"
elastic_ip="127.0.0.1"

nginx_ip=

while true; do
  case "$1" in
  --registry) registry=$2; shift 2;;
  --data_dir) data_dir=$2;  shift 2;;
  --elastic_ip) elastic_ip=$2;  shift 2;;
  --nginx_ip) nginx_ip=$2;  shift 2;;
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
  --data_dir <logstash data directory>, default: /data/logstash
  --elastic_ip <elastic server ip>, default: 127.0.0.1
  --nginx_ip <nginx server ip>, eg: 192.168.0.71
"
}

# check options
check_opt "registry"
check_opt "nginx_ip"

# run docker image
api_url="$nginx_ip/api"
elastic_cluster_1="$elastic_ip:9201"

docker run -dti --name logstash \
  --restart=always \
  -e PAASOS_API_URL=$api_url \
  -e ELASTIC_CLUSTER_1=$elastic_cluster_1 \
  -v $data_dir:/data \
  -p 5044:5044  \
  $registry/openbridge/logstash:$version /opt/startup.sh

# show status
docker ps |grep "logstash"
