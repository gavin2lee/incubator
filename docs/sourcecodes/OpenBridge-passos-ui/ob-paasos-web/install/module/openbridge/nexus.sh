#!/bin/bash

# 安装openbridge-nexus
# wangxinxiang@yihecloud.com
# 2016-07-26

OPTS=$(getopt -o : --long registry:,data_dir:: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="2.0"
data_dir="/data/nexus"


while true; do
  case "$1" in
  --registry) registry=$2; shift 2;;
  --data_dir) data_dir=$2;   shift 2;;
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
  --data_dir <nexus data directory>, default: /data/nexus
"
}


# check options
check_opt "registry"


# run docker image
docker run -dti --name nexus \
  --restart=always \
  -v $data_dir:/data/ \
  -p 9901:8081 \
  $registry/openbridge/nexusservice:$version


# show status
docker ps |grep "nexus"
