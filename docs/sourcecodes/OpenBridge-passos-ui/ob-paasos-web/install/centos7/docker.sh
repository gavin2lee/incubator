#!/bin/bash

# example:
# sudo sh docker.sh --insecure_registry=192.168.1.62:5000
OPTS=$(getopt -o : --long insecure_registry: -- "$@" )
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  exit 1
fi

eval set -- "$OPTS"
insecure_registry=""

while true; do
  case "$1" in
  --insecure_registry) insecure_registry=$2; shift 2;;
  --) shift; break;;
  esac
done

if [ "$insecure_registry" == "" ]; then
  echo "[ERROR] --insecure_registry 参数缺失！"
  exit 1
fi

cmd_exists() {
  command -v "$@" > /dev/null 2>&1
}


#安装docker
#/usr/bin/docker daemon --log-level=warn --bip=172.26.48.1/20 --ip-masq=true --mtu=1450 --insecure-registry 192.168.1.62:5000
#docker_url="http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/docker-engine/internet"
docker_url="https://get.docker.com/"
docker_opts="--log-level=warn --log-driver=json-file --log-opt max-size=64m --log-opt max-file=2"
#docker_opts="--log-level=warn"

# STEP 02: install and configure docker
if cmd_exists docker; then
  echo "docker command alrealy exists on this system."
  echo "/etc/sysconfig/docker and /lib/systemd/system/docker.service files will be reset."
  echo "You may press Ctrl+C now to abort this script."
  echo "waitting 1 seconds..."
  sleep 1
else
  echo "Install docker environment..."
  # TODO network offline
  #yum install -y docker-engine-selinux-*.rpm
  #yum install -y docker-engine-*.rpm
  sudo yum install -y --nogpgcheck \
    http://mirrors.aliyun.com/docker-engine/yum/repo/main/centos/7/Packages/docker-engine-selinux-1.10.3-1.el7.centos.noarch.rpm \
    http://mirrors.aliyun.com/docker-engine/yum/repo/main/centos/7/Packages/docker-engine-1.10.3-1.el7.centos.x86_64.rpm
 
  #curl -sSL $docker_url | sh
  sudo usermod -aG docker `whoami`
fi
docker_opts="DOCKER_OPTS=\"$docker_opts\""
echo $docker_opts > /etc/sysconfig/docker
if [ "$insecure_registry" != "" ]; then
  insecure_registry=$(echo $insecure_registry | sed -e 's/https:\/\///g')
  insecure_registry=$(echo $insecure_registry | sed -e 's/http:\/\///g')
  insecure_registry_str="INSECURE_REGISTRY=\"--insecure-registry=$insecure_registry\""
  echo $insecure_registry_str >> /etc/sysconfig/docker
  echo "DOCKER_NETWORK_OPTIONS=\"-H tcp://0.0.0.0:2375 -H unix:///var/run/docker.sock\"" >> /etc/sysconfig/docker
fi

#echo "DOCKER_STORAGE_OPTIONS=-s devicemapper --storage-opt dm.datadev=/dev/vgdata/docker-data --storage-opt dm.metadatadev=/dev/vgdata/docker-metadata" >> /etc/sysconfig/docker

if [ "$secure_docker_registry" != "" ]; then
  secure_docker_registry=$(echo $secure_docker_registry | sed -e 's/https:\/\///g')
  secure_docker_registry=$(echo $secure_docker_registry | sed -e 's/http:\/\///g')
  mkdir -p $registry_crt_path/$secure_docker_registry
  cp $docker_registry_crt $registry_crt_path/$secure_docker_registry/registry.crt
  if [ -f $registry_crt_path/$secure_docker_registry/registry.crt ]; then
    echo "[OK] install docker secure registry certification"
  else
    echo "[ERROR] install docker secure registry certification failed"
    exit 1
  fi
  echo "[OK] install docker registry certification"
fi
echo "[Unit]
Description=Docker Application Container Engine
Documentation=https://docs.docker.com

[Service]
Type=notify
EnvironmentFile=/etc/sysconfig/docker
ExecStart=/usr/bin/docker daemon \$DOCKER_OPTS \\
\$DOCKER_STORAGE_OPTIONS \\
\$DOCKER_NETWORK_OPTIONS \\
\$ADD_REGISTRY \\
\$BLOCK_REGISTRY \\
\$INSECURE_REGISTRY

MountFlags=slave
LimitNOFILE=1048576
LimitNPROC=1048576
LimitCORE=infinity

[Install]
WantedBy=multi-user.target
" > /lib/systemd/system/docker.service
if cmd_exists docker; then
  echo "[OK] docker environment is ready"
else
  echo "[ERROR] docker environment is not ready"
  exit 1
fi

#重启服务
systemctl daemon-reload
systemctl stop docker
systemctl enable docker
systemctl start docker
sleep 1
systemctl status -l docker