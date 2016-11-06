测试环境的安装规划 至少2台服务器
host1: etcd,docker,docker仓库,flanneld,kubernetes-master
host2: docker,flanneld,kubernetes-node

生产环境的安装规划 至少4台服务器+1个节点
host1: docker仓库
host2: etcd,kubernetes-master
host3: etcd,kubernetes-master
host4: etcd,kubernetes-master

node-1:docker,flanneld,kubernetes-node
node-2:docker,flanneld,kubernetes-node
...
node-n:docker,flanneld,kubernetes-node
 
################################################################################################
############################################安装方法#############################################
################################################################################################

安装步骤：
1. 安装etcd
#curl -o install-etcd.tar.gz http://192.168.1.60/v2/install/install-etcd.tar.gz
#tar zvxf install-etcd.tar.gz
#sudo sh install_etcd.sh

2. 安装公共服务
#curl -o install-service.tar.gz http://192.168.1.60/v2/install/install-service.tar.gz
#tar zvxf install-service.tar.gz
#sudo sh install_service.sh

3. 安装kube-master
#curl -o install-master.tar.gz http://192.168.1.60/v2/install/install-master.tar.gz
#tar zvxf install-master.tar.gz
#sudo sh install_master.sh --etcd_servers=<etcd服务器IP>:4012, --insecure_registry=<docker镜像仓库IP>:5000

4. 安装kube-node
#curl -o install-node.tar.gz http://192.168.1.60/v2/install/install-node.tar.gz
#tar zvxf install-node.tar.gz
#sudo sh install_node.sh --etcd_servers=<etcd服务器IP>:4012, --insecure_registry=<docker镜像仓库IP>:5000 --api_server=<kube-master服务器IP> --host_name=节点名称


################################################################################################
################################################################################################
################################################################################################
 
安装脚本文件说明 

#env_init.sh                                                     修改计算机名,关闭防火墙,关闭selinux
                                   
#install_etcd.sh                                                 安装etcd服务

#install_service.sh                                              Docker仓库，WebSSH，样例镜像等公共资源
 
#install_master.sh                                               安装Kubernetes Master 节点 
 
#install_node.sh                                                 安装kubernetes Node 节点

安装之后的信息介绍

ETCD服务器上的文件
/opt/etcd/bin/etcd                                                ETCD程序文件
/opt/etcd/conf/etcd.conf                                          ETCD配置文件
/data/etcd-data                                                   数据存储目录

Docker仓库服务器上的文件
/data/registry                                                    Docker 仓库数据存储目录

Master 节点上的文件
/opt/openbridge/bin/kube-apiserver                                Kubernetes Master 程序文件
/opt/openbridge/bin/kube-controller-manager                       Kubernetes Master 程序文件
/opt/openbridge/bin/kube-scheduler                                Kubernetes Master 程序文件
/opt/openbridge/bin/kubectl                                       Kubernetes Master 命令行客户端

/opt/openbridge/conf/kube-apiserver.conf                          Kubernetes配置文件
/opt/openbridge/conf/kube-controller-manager.conf                 Kubernetes配置文件
/opt/openbridge/conf/kube-scheduler.conf                          Kubernetes配置文件

Node 节点上的文件
/opt/openbridge/bin/kubelet                                       Kubernetes Node 程序文件
/opt/openbridge/bin/kube-proxy                                    Kubernetes Node 程序文件

/opt/openbridge/conf/kubelet.conf                                 Kubernetes配置文件
/opt/openbridge/conf/kube-proxy.conf                              Kubernetes配置文件  
 
/opt/openbridge/install/xxx                                       组件安装脚本