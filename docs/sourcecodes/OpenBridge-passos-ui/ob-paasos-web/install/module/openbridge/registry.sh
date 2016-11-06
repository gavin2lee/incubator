#!/bin/bash

# 安装openbridge-registry
# wangxinxiang@yihecloud.com
# 2016-07-29

OPTS=$(getopt -o : --long registry: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="2.4.1"

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
  --registry <docker registry>, default: docker.yihecloud.com:443
"
}

# check options
check_opt "registry"

# run docker image
mkdir -p /data/registry2  #$(pwd)

docker run -dti --name registry2 \
  --restart=always \
  -v /data/registry2:/var/lib/registry \
  -p 5000:5000 \
  $registry/openbridge/registry:$version

# show status
docker ps |grep "registry2"
