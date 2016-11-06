#!/bin/sh

# 安装和启动kubenetes节点
# wangxinxiang@yihecloud.com
# 2016-04-18

# example
# sudo sh install_node.sh --etcd_servers=192.168.1.84:4012, --insecure_registry=192.168.1.84:5000 --api_server=192.168.1.84 --host_name=testnode1
echo "请使用以下安装命令"
echo "sudo sh install_node.sh --etcd_servers=192.168.1.84:4012, --insecure_registry=192.168.1.84:5000 --api_server=192.168.1.84"
echo "=================安装日志===================="

OPTS=$(getopt -o : --long host_name:,etcd_servers:,api_server:,insecure_registry: -- "$@" )
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  exit 1
fi

eval set -- "$OPTS"
etcd_servers=""
api_server=""
insecure_registry=""
download_path=""

while true; do
  case "$1" in
  --etcd_servers) etcd_servers=$2; shift 2;;
  --api_server) api_server=$2; shift 2;;
  --insecure_registry) insecure_registry=$2; shift 2;;
  --) shift; break;;
  esac
done

#source ./.env
if [ "$etcd_servers" == "" ]; then
  echo "[ERROR] --etcd_servers 参数缺失！"
  exit 1
fi
if [ "$api_server" == "" ]; then
  echo "[ERROR] --api_server 参数缺失！"
  exit 1
fi
if [ "$insecure_registry" == "" ]; then
  echo "[ERROR] --insecure_registry 参数缺失！"
  exit 1
fi

echo "=================安装日志===================="
#!/bin/bash

echo "正在初始化安装环境！"
echo "强制关闭selinux...，若修改不生效，请重启计算机或手动关闭！"
setenforce 0
#sed -i '/SELINUX/s/enforcing/disabled/' /etc/selinux/config 
sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/sysconfig/selinux
sed -i "s/SELINUX=enforcing/SELINUX=disabled/g" /etc/selinux/config

#关闭防火墙
echo "关闭系统防火墙"
systemctl stop iptables
systemctl stop firewalld
systemctl disable iptables
systemctl disable firewalld

#全局配置
os_type="centos7"
dest_path="/opt/openbridge"
dest_path_script="$dest_path/install/$os_type"
dest_path_bin="$dest_path/bin"
dest_path_conf="$dest_path/conf"
download_path="http://192.168.1.60"
current_path=$(pwd)

kube_cfg=$dest_path_conf
kube_bin=$dest_path_bin

# 获取本机IP地址，支持动态和静态IP
host_ip=
host_ips=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $2}' | cut -f1 -d '/'`)
if [ "${host_ips[0]}" == "" ]; then
  echo "[ERROR] get ip address error"
  exit 1
else
  host_ip=${host_ips[0]}
  echo "[INFO] use host ip address: $host_ip"
fi

# 获取默认物理网卡名称
flannel_iface=
host_iface=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $7}'`)
# 获取到dynamic
if [ "${host_iface[0]}" == "dynamic" ]; then
  host_iface=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $8}'`)
fi
if [ "${host_iface[0]}" == "" ]; then
  echo "[ERROR] get iface error"
  exit 1
else
  flannel_iface=${host_iface[0]}
  echo "[INFO] use $flannel_iface as flannel iface"
fi

command_exists() {
  command -v "$@" > /dev/null 2>&1
}

mkdir -p $dest_path_conf

#=====================================================================

#下载文件
echo "downlaod $download_path/install-centos7.tar.gz ... "
curl -o install-centos7.tar.gz $download_path/install-centos7.tar.gz
tar zvxf install-centos7.tar.gz -C $dest_path

echo "downlaod $download_path/kube-node.tar.gz ... "
curl -o kube-node.tar.gz $download_path/kube-node.tar.gz
tar zvxf kube-node.tar.gz -C $dest_path

#更改主机名
#echo "change_hostname to: $host_name"
#source $dest_path_script/change_hostname.sh $host_name

# 安装flannel，docker
echo "install flanneld, docker ..."
source $dest_path_script/flannel.sh --etcd_servers=$etcd_servers 
source $dest_path_script/docker.sh --insecure_registry=$insecure_registry

# 安装kubelete node
echo "install kube-proxy, kubelet..."
source $dest_path_script/proxy.sh $api_server
source $dest_path_script/kubelet.sh $api_server $insecure_registry
