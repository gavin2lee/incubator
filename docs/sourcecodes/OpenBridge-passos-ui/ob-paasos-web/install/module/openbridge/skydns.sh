#!/bin/bash

# 安装openbridge-skydns
# wangxinxiang@yihecloud.com
# 2016-07-29

OPTS=$(getopt -o : --long registry:,etcd:,domain:,dns: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="2.5"
domain="paas.os"
dns="114.114.114.114"

etcd=


while true; do
  case "$1" in
  --registry) registry=$2; shift 2;;
  --etcd) etcd=$2; shift 2;;
  --domain) domain=$2; shift 2;;
  --dns) dns=$2; shift 2;;
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
  --etcd <etcd ip> , eg: x.x.x.x:4012
  --registry <docker registry>, default: docker.yihecloud.com:443
  --domain <private network domain>, default: paas.os
  --dns <private network dns>, default: 114.114.114.114
"
}

# check options
check_opt "registry"
check_opt "etcd"

# init
curl -XPUT "http://$etcd/v2/keys/skydns/config" \
    -d value="{\"dns_addr\":\"0.0.0.0:53\",\"ttl\":60, \"nameservers\": [\"$dns:53\",\"8.8.8.8:53\"], \"domain\":\"$domain\"}"

# run docker image
docker run -dti --name skydns \
  --restart=always \
  -p 53:53/TCP \
  -p 53:53/UDP \
  $registry/openbridge/skydns:$version --machines="http://$etcd"

# show status
docker ps |grep "skydns"
