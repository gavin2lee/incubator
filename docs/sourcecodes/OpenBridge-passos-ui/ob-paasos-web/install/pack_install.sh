#!/bin/bash

echo "==> Unpack install script..."
tar zvxf install.tar.gz 
cp -f install-centos7.tar.gz install/install-centos7.tar.gz

echo "==> Packing install script...."
tar zvcf install/db-schema.tar.gz        install/sql
tar zvcf install/install-etcd.tar.gz     install/etcd.tar.gz         install/env_init.sh             install/install_etcd.sh
tar zvcf install/install-monitor.tar.gz  install/open-falcon.tar.gz  install/env_init.sh             install/install_monitor.sh  install/sql
tar zvcf install/install-master.tar.gz   install/kube-master.tar.gz  install/install-centos7.tar.gz  install/env_init.sh         install/install_master.sh
tar zvcf install/install-node.tar.gz     install/kube-node.tar.gz    install/install-centos7.tar.gz  install/env_init.sh         install/install_node.sh

echo "==> Packing images..."
tar zvcf install/images.tar.gz images
tar zvcf install/install-service.tar.gz  install/images.tar.gz       install/install-centos7.tar.gz  install/env_init.sh         install/install_service.sh

echo "==> Pack clear..."
rm -rf install/install-centos7.tar.gz
rm -rf install/install
rm -rf install/sql

echo "==> Pack all in one..."
tar zvcf install-ob.tar.gz \
    install/install-etcd.tar.gz \
    install/install-service.tar.gz \
    install/install-monitor.tar.gz \
    install/install-master.tar.gz \
    install/install-node.tar.gz \
    install/install-module.tar.gz 

# 安装步骤
echo <<EOF
curl -o install-etcd.tar.gz http://192.168.1.60/v2/install/install-etcd.tar.gz && tar zvxf install-etcd.tar.gz
sudo sh install_etcd.sh

curl -o install-service.tar.gz http://192.168.1.60/v2/install/install-service.tar.gz && tar zvxf install-service.tar.gz
sudo sh install_service.sh 192.168.1.139:4012  192.168.1.139

curl -o install-master.tar.gz http://192.168.1.60/v2/install/install-master.tar.gz && tar zvxf install-master.tar.gz
sudo sh install_master.sh --etcd_servers=IP:4012, --insecure_registry=IP:5000

curl -o install-node.tar.gz http://192.168.1.60/v2/install/install-node.tar.gz && tar zvxf install-node.tar.gz
sudo sh install_node.sh --etcd_servers=IP:4012, --insecure_registry=IP:5000 --api_server=IP --host_name=namexx

curl -o install-monitor.tar.gz http://192.168.1.60/v2/install/install-monitor.tar.gz && tar zvxf install-monitor.tar.gz
EOF