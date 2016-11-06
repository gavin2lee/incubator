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
