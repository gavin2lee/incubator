#!/bin/bash

if [ "$1" == "2" ]; then
  echo "正在下载flanneld..."
  curl -o bin/flanneld http://192.168.1.60/v2/bin/flanneld
  chmod +x bin/flanneld
  
  curl -o bin/docker-connector http://192.168.1.60/v2/bin/docker-connector
  chmod +x bin/docker-connector
fi

cd install

echo "正在打包kube bin 文件..."
tar zvcf kube-master.tar.gz    bin/flanneld bin/kubectl bin/kube-apiserver bin/kube-controller-manager bin/kube-scheduler bin/docker-connector
tar zvcf kube-node.tar.gz      bin/flanneld bin/kubectl bin/kube-proxy bin/kubelet
#tar zvcf open-falcon.tar.gz    bin/flanneld bin/kubectl bin/kube-proxy bin/kubelet

if [ "$1" == "2" ]; then
  echo "正在上传文件: 192.168.1.60/v2/ ..."
  scp -i ~/id_rsa *.tar.gz root@192.168.1.60:/usr/share/nginx/html/v2/install/
  
  echo "清理打包文件...."
  rm -f *.tar.gz 
fi

cd ..
