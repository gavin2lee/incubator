#!/bin/bash
version="v2"

function pack_k8s() {
  echo "正在打包脚本..."
  cd ../
  tar zvcf install-centos7.tar.gz install/centos7
  tar zvcf install.tar.gz install/sql \
      install/env_init.sh \
      install/install_etcd.sh \
      install/install_master.sh \
      install/install_node.sh \
      install/install_service.sh \
      install/install_monitor.sh
  
  echo "上传打包文件: 192.168.1.60/v2/ ..."
  scp -i ~/id_rsa *.tar.gz root@192.168.1.60:/usr/share/nginx/html/v2
  
  rm -f *.tar.gz 
  cd install

  echo "上传打包脚本: 192.168.1.60/v2/ ..."
  scp -i ~/id_rsa pack_*.sh root@192.168.1.60:/usr/share/nginx/html/v2
}

function pack_module() {
  echo "正在打包组件安装文件..."
  cd ../
  tar zvcf install-module.tar.gz install/module
  scp -i ~/id_rsa *.tar.gz root@192.168.1.60:/usr/share/nginx/html/v2/install
  
  rm -f *.tar.gz
  cd install
}

function pack_binary() {
  echo "正在打包安装bin文件..."
  ssh -i ~/id_rsa root@192.168.1.60 'cd /usr/share/nginx/html/v2; dos2unix pack_bin.sh; chmod +x pack_bin.sh; ./pack_bin.sh'
}

function pack_install() {
  echo "正在打包安装脚本..."
  ssh -i ~/id_rsa root@192.168.1.60 'cd /usr/share/nginx/html/v2; dos2unix pack_install.sh; chmod +x pack_install.sh; ./pack_install.sh'
}

if [ "$1" == "" ]; then
  echo "Usage: $0 bin|k8s|mod|tar|all"
fi

if [ "$1" == "bin" ]; then
  pack_binary
fi

if [ "$1" == "k8s" ]; then
  pack_k8s
fi

if [ "$1" == "mod" ]; then
  pack_module
fi

if [ "$1" == "tar" ]; then
  pack_k8s
  pack_install
fi

if [ "$1" == "all" ]; then
  pack_binary
  pack_k8s
  pack_module
  pack_install
fi
