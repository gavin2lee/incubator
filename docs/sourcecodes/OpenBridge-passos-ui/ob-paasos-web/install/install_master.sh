#!/bin/sh

# 安装kubenetes主节点
# wangxinxiang@yihecloud.com
# 2016-04-26

# example
# sudo sh install_master.sh --etcd_servers=192.168.1.84:4012, --insecure_registry=192.168.1.84:5000

echo "请使用以下安装命令"
echo "sudo sh install_master.sh --etcd_servers=192.168.1.84:4012, --insecure_registry=192.168.1.84:5000"
echo "=================安装日志===================="

OPTS=$(getopt -o : --long etcd_servers:,insecure_registry:,cluster_ip_range:,flannel_network_ip_range:,flannel_subnet_len: -- "$@" )
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  exit 1
fi

eval set -- "$OPTS"
etcd_servers=""
insecure_registry=""
cluster_ip_range=""
flannel_network_ip_range=""
flannel_subnet_len=""

while true; do
  case "$1" in
  --etcd_servers) etcd_servers=$2; shift 2;;
  --insecure_registry) insecure_registry=$2; shift 2;;
  --cluster_ip_range) cluster_ip_range=$2; shift 2;;
  --flannel_network_ip_range) flannel_network_ip_range=$2; shift 2;;
  --flannel_subnet_len) flannel_subnet_len=$2; shift 2;;
  --) shift; break;;
  esac
done

if [ "$etcd_servers" == "" ]; then
  echo "[WARN] --etcd_servers 参数缺失!"
  exit 1
fi
if [ "$insecure_registry" == "" ]; then
  echo "[WARN] --insecure_registry 参数缺失!"
  exit 1
fi
if [ "$cluster_ip_range" == "" ]; then
  echo "[WARN] --cluster_ip_range use default: 172.20.0.0/16"
  cluster_ip_range="172.20.0.0/16"
fi
if [ "$flannel_network_ip_range" == "" ]; then
  echo "[WARN] --flannel_network_ip_range use default: 10.1.0.0/16"
  flannel_network_ip_range="10.1.0.0/16"
fi
if [ "$flannel_subnet_len" == "" ]; then
  echo "[WARN] --flannel_subnet_len use default: 24"
  flannel_subnet_len=24
fi

yum install openssl -y
openssl genrsa -out /etc/kube-serviceaccount.key 2048 2>/dev/null

#初始化安装环境
source ./env_init.sh

#下载文件
#echo "downlaod $download_path/install-centos7.tar.gz ... "
#curl -o install-centos7.tar.gz $download_path/install-centos7.tar.gz
tar zvxf install-centos7.tar.gz -C $dest_path

#echo "downlaod $download_path/kube-master.tar.gz ... "
#curl -o kube-master.tar.gz $download_path/kube-master.tar.gz
tar zvxf kube-master.tar.gz -C /opt/openbridge

# Installl flannel  and init etcd config
source $dest_path_script/flannel.sh --etcd_servers=$etcd_servers --flannel_network_ip_range=$flannel_network_ip_range --flannel_subnet_len=$flannel_subnet_len

# Install docker
#source $dest_path_script/docker.sh --insecure_registry=$insecure_registry

# 安装kube-master
api_adim_ctrl="NamespaceLifecycle,NamespaceExists,LimitRanger,SecurityContextDeny,ServiceAccount,ResourceQuota"
#api_adim_ctrl="NamespaceLifecycle,NamespaceExists,LimitRanger,SecurityContextDeny,ResourceQuota"
source $dest_path_script/apiserver.sh $cluster_ip_range $api_adim_ctrl
source $dest_path_script/controller-manager.sh $host_ip
source $dest_path_script/scheduler.sh $host_ip
source $dest_path_script/connector.sh
echo "=================Kubenetes master安装完成================"

cp -f $dest_path_bin/kubectl /usr/bin/
ln -sf /usr/bin/kubectl /usr/bin/kc

echo "=================Kubenetes服务列表================"
echo "waitting 10 seconds..."
sleep 10
kubectl get svc
