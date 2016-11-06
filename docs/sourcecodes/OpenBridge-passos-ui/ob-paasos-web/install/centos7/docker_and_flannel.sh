#!/bin/bash

# example:
# sudo sh docker.sh --etcd_servers=192.168.1.62:4012, --insecure_registry=192.168.1.62:5000
OPTS=$(getopt -o : --long etcd_servers:,flannel_network_ip_range:,flannel_subnet_len:,insecure_registry: -- "$@" )
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  exit 1
fi

eval set -- "$OPTS"
etcd_servers=""
flannel_network_ip_range=""
flannel_subnet_len=20
insecure_registry="10.1.129.1:5000"

while true; do
  case "$1" in
  --etcd_servers) etcd_servers=$2; shift 2;;
  --flannel_network_ip_range) flannel_network_ip_range=$2; shift 2;;
  --flannel_subnet_len) flannel_subnet_len=$2; shift 2;;
  --insecure_registry) insecure_registry=$2; shift 2;;
  --) shift; break;;
  esac
done

if [ "$etcd_servers" == "" ]; then
  echo "[ERROR] --etcd_servers 参数缺失！"
  exit 1
fi

#安装配置flannel
#-etcd-endpoints=http://192.168.1.61:4012 -etcd-prefix=/flannel/network -iface=ens160

flannel_prefix="/flannel/network"
flannel_opts=""

# init flannel config to etcd
if [ "$flannel_network_ip_range" != "" ]; then
  echo "[INFO] init flannel config to etcd...！"
  #flannel_k8s_config="{\"Network\": \"${flannel_network_ip_range}\", \"SubnetLen\": ${flannel_subnet_len}, \"Backend\": {\"Type\": \"vxlan\", \"VNI\": 1}}"
  flannel_k8s_config="{\"Network\": \"${flannel_network_ip_range}\", \"SubnetLen\": ${flannel_subnet_len}}"
  single_etcd_server=$(echo $etcd_servers | cut -f1 -d ',')
  echo $single_etcd_server
  echo $flannel_k8s_config
  curl -L $single_etcd_server/v2/keys$flannel_prefix/config -XPUT -d value="${flannel_k8s_config}"
fi

# STEP 01: install and configure flannel
if command_exists flanneld && [ -e /usr/libexec/flannel/mk-docker-opts.sh ]; then
  echo "flanneld command already exists on this system."
  echo "/etc/sysconfig/flanneld /usr/lib/systemd/system/docker.service.d/flannel.conf and /lib/systemd/system/flanneld.service files will be reset"
  echo "You may press Ctrl+C now to abort this script."
  echo "waitting..."
  sleep 1
else
  mkdir -p /usr/libexec/flannel
  mkdir -p /run/flannel
  mkdir -p /usr/lib/systemd/system/docker.service.d
  mv -f $current_path/flanneld /usr/bin/flanneld
  mv -f $current_path/mk-docker-opts.sh /usr/libexec/flannel/mk-docker-opts.sh
fi

# check http:// prefix of etcd address
flannel_etcd_servers=
flannel_etcds=(${etcd_servers//,/ })
for i in ${flannel_etcds[@]}
do
  if [[ $i =~ "http://" ]] || [[ $i =~ "https://" ]]; then
    if [ "$flannel_etcd_servers" == "" ]; then
      flannel_etcd_servers=$i
    else
      flannel_etcd_servers=$flannel_etcd_servers,$i
    fi
  else
    if [ "$flannel_etcd_servers" == "" ]; then
      flannel_etcd_servers=http://$i
    else
      flannel_etcd_servers=$flannel_etcd_servers,http://$i
    fi
  fi
done
echo "FLANNEL_ETCD=\"$flannel_etcd_servers\"
FLANNEL_ETCD_KEY=\"$flannel_prefix\"
FLANNEL_OPTIONS=\"$flannel_opts -iface=$flannel_iface\"
" > /etc/sysconfig/flanneld
echo "[Service]
EnvironmentFile=-/run/flannel/docker" > /usr/lib/systemd/system/docker.service.d/flannel.conf
echo "[Unit]
Description=Flanneld overlay address etcd agent
After=network.target
After=network-online.target
Wants=network-online.target
After=etcd.service
Before=docker.service

[Service]
Type=notify
EnvironmentFile=-/etc/sysconfig/flanneld
EnvironmentFile=-/etc/sysconfig/docker-network
ExecStart=/usr/bin/flanneld -etcd-endpoints=\${FLANNEL_ETCD} -etcd-prefix=\${FLANNEL_ETCD_KEY} \$FLANNEL_OPTIONS
ExecStartPost=/usr/libexec/flannel/mk-docker-opts.sh -k DOCKER_NETWORK_OPTIONS -d /run/flannel/docker
Restart=always

[Install]
WantedBy=multi-user.target
RequiredBy=docker.service
" > /lib/systemd/system/flanneld.service
if command_exists flanneld && [ -e /usr/libexec/flannel/mk-docker-opts.sh ]; then
  echo "[OK] flannel environment is ready"
else
  echo "[ERROR] flannel environment is not ready"
  exit 1
fi

#安装docker
#/usr/bin/docker daemon --log-level=warn --bip=172.26.48.1/20 --ip-masq=true --mtu=1450 --insecure-registry 192.168.1.62:5000
#docker_url="http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/docker-engine/internet"
docker_url="https://get.docker.com/"
docker_opts="--log-level=warn"

# STEP 02: install and configure docker
if command_exists docker; then
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
  curl -sSL $docker_url | sh
  sudo usermod -aG `whoami`
fi
docker_opts="DOCKER_OPTS=\"$docker_opts\""
echo $docker_opts > /etc/sysconfig/docker
if [ "$insecure_registry" != "" ]; then
  insecure_registry=$(echo $insecure_registry | sed -e 's/https:\/\///g')
  insecure_registry=$(echo $insecure_registry | sed -e 's/http:\/\///g')
  insecure_registry="INSECURE_REGISTRY=\"--insecure-registry=$insecure_registry\""
  echo $insecure_registry >> /etc/sysconfig/docker
fi

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
if command_exists docker; then
  echo "[OK] docker environment is ready"
else
  echo "[ERROR] docker environment is not ready"
  exit 1
fi

#重启服务
systemctl stop docker
systemctl stop flanneld
systemctl disable flanneld
ip link delete docker0
ip link delete flannel.1
systemctl daemon-reload
systemctl enable flanneld
systemctl start flanneld
sleep 3
systemctl status -l flanneld

# STEP 11: start docker
systemctl enable docker
systemctl start docker
sleep 3
systemctl status -l docker