#!/bin/bash

# 安装openbridge-webssh
# wangxinxiang@yihecloud.com
# 2016-07-29

OPTS=$(getopt -o s: --long registry: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="1.2"

kube_master=

while true; do
  case "$1" in
  -s) kube_master=$2; shift 2;;
  --registry) registry=$2; shift 2;;
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
  -s <kube master ip> , eg: x.x.x.x
  --registry <docker registry>, default: docker.yihecloud.com:443
"
}

# check options
check_opt "registry"
check_opt "kube_master"

# run docker image
docker run -dti --name webssh \
  --restart=always  \
  -e ADDR="$kube_master"  \
  -p 4200:4200 \
  $registry/openbridge/webssh:$version

# show status
docker ps |grep "webssh"
