#!/bin/bash

# 安装openbridge-agent
# wangxinxiang@yihecloud.com
# 2016-08-08

OPTS=$(getopt -o s: --long registry: -- "$@")
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  usage;
  exit 1
fi

eval set -- "$OPTS"

registry="docker.yihecloud.com:443"
version="1.9"

monitor_ip=


while true; do
  case "$1" in
  -s) monitor_ip=$2; shift 2;;
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
  -s <monitor server ip> , eg: x.x.x.x
  --registry <docker registry>, default: docker.yihecloud.com:443
"
}

# check options
check_opt "registry"
check_opt "monitor_ip"

# 获取本机IP地址，支持动态和静态IP
IP=
host_ips=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $2}' | cut -f1 -d '/'`)
if [ "${host_ips[0]}" == "" ]; then
  echo "[ERROR] get ip address error"
  exit 1
else
  IP=${host_ips[0]}
  echo "[INFO] use host ip address: $IP"
fi

# 清理
docker rm -f agent
docker rmi -f $registry/agent:1.9

# run docker image
docker run -d --restart=always \
  -e HOSTNAME="\"$IP\"" \
  -e TRANSFER_ADDR="[\"$monitor_ip:8433\",\"$monitor_ip:8433\"]" \
  -e TRANSFER_INTERVAL="60" \
  -e HEARTBEAT_ENABLED="true" \
  -e HEARTBEAT_ADDR="\"$monitor_ip:6030\"" \
  -v /:/rootfs:ro \
  -v /var/run:/var/run:rw \
  -v /sys:/sys:ro \
  -v /var/lib/docker/:/var/lib/docker:ro \
  -p 1988:1988 \
  --name agent \
  $registry/openbridge/agent:$version

# show status
docker ps |grep "agent"

sleep 3;
docker logs -f agent
