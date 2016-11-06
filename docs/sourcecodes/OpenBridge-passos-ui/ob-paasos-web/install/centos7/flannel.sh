#!/bin/bash

# example:
# sudo sh flannel.sh --etcd_servers=192.168.1.62:4012, 
OPTS=$(getopt -o : --long etcd_servers:,flannel_network_ip_range:,flannel_subnet_len: -- "$@" )
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  exit 1
fi

eval set -- "$OPTS"
etcd_servers=""
flannel_network_ip_range=""
flannel_subnet_len=24

while true; do
  case "$1" in
  --etcd_servers) etcd_servers=$2; shift 2;;
  --flannel_network_ip_range) flannel_network_ip_range=$2; shift 2;;
  --flannel_subnet_len) flannel_subnet_len=$2; shift 2;;
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
  flannel_k8s_config="{\"Network\": \"${flannel_network_ip_range}\", \"SubnetLen\": ${flannel_subnet_len}, \"Backend\": {\"Type\": \"vxlan\", \"VNI\": 1}}"
  # flannel_k8s_config="{\"Network\": \"${flannel_network_ip_range}\", \"SubnetLen\": ${flannel_subnet_len}}"
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
  # 下载文件
  #curl -o flanneld $download_path/bin/flanneld
  #curl -o mk-docker-opts.sh $download_path/bin/mk-docker-opts.sh
  #chmod +x ./flanneld && chmod +x ./mk-docker-opts.sh

  mkdir -p /usr/libexec/flannel
  mkdir -p /run/flannel
  mkdir -p /usr/lib/systemd/system/docker.service.d
  cp -f $dest_path_bin/flanneld /usr/bin/flanneld
  #cp -f $dest_path_bin/mk-docker-opts.sh /usr/libexec/flannel/mk-docker-opts.sh
  cp -f $dest_path_script/mk-docker-opts.sh /usr/libexec/flannel/mk-docker-opts.sh
  
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

#重启服务
systemctl stop docker
systemctl stop flanneld
systemctl disable flanneld
ip link delete docker0
ip link delete flannel0
ip link delete flannel.1
systemctl daemon-reload
systemctl enable flanneld
systemctl start flanneld
sleep 3
systemctl status -l flanneld
