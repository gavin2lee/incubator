#!/bin/sh

# 安装和启动kubenetes节点
# wangxinxiang@yihecloud.com
# 2016-04-18

# example
# sudo sh install_node.sh --etcd_servers=192.168.1.84:4012, --insecure_registry=192.168.1.84:5000 --api_server=192.168.1.84 --host_name=testnode1
echo "请使用以下安装命令"
echo "sudo sh install_node.sh --etcd_servers=192.168.1.84:4012, --insecure_registry=192.168.1.84:5000 --api_server=192.168.1.84 --host_name=node01"
echo "=================安装日志===================="

OPTS=$(getopt -o : --long host_name:,etcd_servers:,api_server:,insecure_registry: -- "$@" )
if [ $? != 0 ]; then
  echo "[ERROR] 参数错误！"
  exit 1
fi

eval set -- "$OPTS"
host_name=""
etcd_servers=""
api_server=""
insecure_registry=""

while true; do
  case "$1" in
  --host_name) host_name=$2; shift 2;;
  --etcd_servers) etcd_servers=$2; shift 2;;
  --api_server) api_server=$2; shift 2;;
  --insecure_registry) insecure_registry=$2; shift 2;;
  --) shift; break;;
  esac
done

#source ./.env
if [ "$host_name" == "" ]; then
  echo "[ERROR] --host_name 参数缺失！"
  exit 1
fi
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

#安装数据卷驱动
yum install -y nfs-utils iscsi-initiator-utils

echo "=================安装组件===================="
source ./env_init.sh

#下载文件
#echo "downlaod $download_path/install-centos7.tar.gz ... "
#curl -o install-centos7.tar.gz $download_path/install-centos7.tar.gz
tar zvxf install-centos7.tar.gz -C $dest_path

#echo "downlaod $download_path/kube-node.tar.gz ... "
#curl -o kube-node.tar.gz $download_path/kube-node.tar.gz
tar zvxf kube-node.tar.gz -C $dest_path

#更改主机名
echo "change_hostname to: $host_name"
source $dest_path_script/change_hostname.sh $host_name

# 安装flannel，docker
echo "install flanneld, docker ..."
source $dest_path_script/flannel.sh --etcd_servers=$etcd_servers 
source $dest_path_script/docker.sh --insecure_registry=$insecure_registry

# 安装kubelete node
echo "install kube-proxy, kubelet..."
source $dest_path_script/proxy.sh $api_server
source $dest_path_script/kubelet.sh $api_server $insecure_registry

cp -f $dest_path_bin/kubectl /usr/bin/
ln -sf /usr/bin/kubectl /usr/bin/kc

echo "waitting 3 seconds..."
sleep 3

echo "create task..."
#定时每天晚上00:清理退出运行的k8s container
cat <<EOF >/etc/cron.daily/docker.cron
#!/bin/bash
docker rm $(docker ps -a|grep Exited|grep k8s|awk '{print $1}')
EOF

echo "=================Kubenetes node安装完成================"