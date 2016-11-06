#!/bin/bash

# 安装openbridge-elasticsearch
# wangxinxiang@yihecloud.com
# 2016-07-26

OPTS=$(getopt -o c:n: --long registry:,data_dir:: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="2.3.3"
data_dir="/data/elasticsearch"
code="yhxy"
name="node-1"


while true; do
  case "$1" in
  -c) code=$2; shift 2;;
  -n) name=$2; shift 2;;
  --registry) registry=$2; shift 2;;
  --data_dir) data_dir=$2; shift 2;;
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
  -c <code>, default: yhxy
  -n <name>, default: node-1
  --registry <docker registry>, default: docker.yihecloud.com:443
  --data_dir <elasticsearch data directory>, default: /data/elasticsearch
"
}

# check options
check_opt "registry"

# 获取本机IP
IP=
host_ips=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $2}' | cut -f1 -d '/'`)
if [ "${host_ips[0]}" == "" ]; then
  echo "[ERROR] get ip address error"
  exit 1
else
  IP=${host_ips[0]}
  echo "[INFO] use host ip address: $IP"
fi



# run docker image
el_network="$IP:9300"

docker run -dti --name elasticsearch \
  --restart=always \
  -e PROJECT_CODE=$code \
  -e NODE_NAME=$name \
  -e PATH_DATA=/data/data \
  -e PATH_LOGS=/data/logs  \
  -e EL_NETWORK=$el_network  \
  -v $data_dir:/data \
  -p 9200:9200 \
  -p 9300:9300 \
  -p 9400:9400 \
  $registry/openbridge/elasticsearch:$version /opt/startup.sh

# show status
docker ps |grep "elasticsearch"

# try test.
sleep 10
curl -s $IP:9200/_cat/nodes?v
