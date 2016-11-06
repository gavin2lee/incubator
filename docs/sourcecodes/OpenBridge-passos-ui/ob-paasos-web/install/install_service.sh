#!/bin/sh

# 安装registry,webssh,dns
# wangxinxiang@yihecloud.com
# 2016-04-26

echo "=================安装日志===================="
echo "请使用以下安装命令"
echo "sudo sh install_service.sh <kube_master_ip>"
echo "安装示例:#sh install_service.sh x.x.x.x"

kube_master=$1 

if [ "$1" == "" ]; then
  echo "[ERROR] 请配置kube-master主机参数！"
  exit 1
fi

# init
source ./env_init.sh

insecure_registry="127.0.0.1:5000"

tar zvxf install-centos7.tar.gz -C $dest_path

#安装docker
source $dest_path_script/docker.sh --insecure_registry=$insecure_registry

# 下载
echo "解压镜像文件"
tar -zvxf images.tar.gz

# 启动镜像
echo "启动镜像存储"
mkdir -p /data/registry2  #$(pwd)
docker load < images/registry2.tar
docker run -d --restart=always --name docker_hub  -v /data/registry2:/var/lib/registry -p 5000:5000 127.0.0.1:5000/registry:2.4.1


echo "启动webssh"
docker load < images/websshd.tar
docker run -dti --name webssh --restart=always -e ADDR="$kube_master" -p 4200:4200 127.0.0.1:5000/websshd

echo "waitting 3 seconds..."
sleep 3

# 创建基础镜像
docker load < images/pause.tar
docker load < images/dashboard.tar
docker load < images/agent.tar
docker load < images/nginx.tar

docker push "${insecure_registry}/google_containers/pause:0.8"
docker push "${insecure_registry}/dashboard:1.0"
docker push "${insecure_registry}/agent:1.9"
docker push "${insecure_registry}/nginx"
docker push "${insecure_registry}/websshd"
docker push "${insecure_registry}/registry:2.4.1"

echo "==============镜像导入完成=============="
echo "本地镜像："
docker images
echo "仓库镜像："
curl -s http://$insecure_registry/v2/_catalog
