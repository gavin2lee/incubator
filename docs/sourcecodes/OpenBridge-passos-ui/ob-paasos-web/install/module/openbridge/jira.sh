#!/bin/bash

# 安装openbridge-jira
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
version="7.1.9"
data_dir="/data/jira"


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
  --data_dir <jira data directory>, default: /data/jira
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
jira_data="$data_dir/jira_data"
#jira_home="$data_dir/jira_home"
chown daemon:daemon -R  $jira_data

# ENV JIRA_HOME     /var/atlassian/jira
# ENV JIRA_INSTALL  /opt/atlassian/jira
docker run -dti --name jira \
  --restart=always \
  -v $jira_data:/var/atlassian/jira \
  -p 8080:8080 \
  $registry/openbridge/jira:$version


# show status
docker ps |grep "jira"


# for test.
echo "WARN: You can visit http://$IP:8080 config jiar."
