#!/bin/sh

# Example: sudo sh start_etcd.sh 1

#init 

# STEP 01: check arguments
node_id=$1
use_default=""
cluster_state="new"

if [ "$1" == "" ]; then
 node_id=1
 use_default=1
 echo "[INFO] Usage: sudo sh start_etcd.sh <number>"
 echo "[WARNING] use default arguments!"
fi
tmp=`echo $node_id | sed 's/[0-9]//g'`
if [ -n "${tmp}" ]; then
  echo "[ERROR] Argument must be integer"
  exit 1
fi
if [ $node_id -lt 1 ]; then
  echo "[ERROR] number $node_id is less than 1"
  exit 1
fi

# STEP 02: assign append arguments
etcd_dest_path=/opt/etcd
conf_file=$etcd_dest_path/conf/etcd.conf
conf_list_file=$etcd_dest_path/conf/etcd_node_list.conf
etcd_file=$etcd_dest_path/bin/etcd
data_dir=/data/etcd-data
cluster_token='openbridge-etcd-cluster'
name_prefix='obetcd'
peer_port=4010
client_port=4012

mkdir -p $etcd_dest_path/bin
mkdir -p $etcd_dest_path/conf
mkdir -p $data_dir

#获取本机ip
IP=
host_ips=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $2}' | cut -f1 -d '/'`)
if [ "${host_ips[0]}" == "" ]; then
  echo "[ERROR] get ip address error"
  exit 1
else
  IP=${host_ips[0]}
  echo "[INFO] use host ip address: $IP"
fi

#默认安装生成本机IP
if [ "$use_default" == "1" ]; then
  echo $IP > $conf_list_file
fi

if [ $node_id -gt 1 ]; then
  echo "start clusters node..."
  cluster_state="existing"
fi

#下载
#curl -o etcd $download_path/bin/etcd
tar zvxf etcd.tar.gz
mv -f etcd $etcd_file

# STEP 03: get all node ips
node_ips=()
node_len=0
clusters=
while IFS='' read -r line || [[ -n "$line" ]]; do
  node_ips[$node_len]=$line
  node_len=$((node_len+1))
  if [ $node_len -eq 1 ]; then
    clusters=$name_prefix$node_len=http://$line:$peer_port
  else
    clusters=$clusters,$name_prefix$node_len=http://$line:$peer_port
  fi
done < $conf_list_file
if [ $node_id -gt $node_len ]; then
  echo "[ERROR] number $node_id is out of range"
  exit 1
fi

# STEP 04: compare selected node ip with local ips
local_ips=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $2}' | cut -f1 -d '/'`)
exist_ip=0
for i in ${local_ips[@]}
do
  if [ "$i" == "${node_ips[$((node_id-1))]}" ]; then
    exist_ip=1
    break
  fi
done
if [ $exist_ip -eq 0 ]; then
  echo "[ERROR] IP ${node_ips[$((node_id-1))]} is not node IP"
  exit 1
fi

# STEP 05: install etcd
echo "# configure file for etcd.service
# -name
ETCD_NAME='-name $name_prefix$node_id'
# -initial-advertise-peer-urls
INITIAL_ADVERTISE_PEER_URLS='-initial-advertise-peer-urls http://${node_ips[$((node_id-1))]}:$peer_port'
# -listen-peer-urls
LISTEN_PEER_URLS='-listen-peer-urls http://0.0.0.0:$peer_port'
# -advertise-client-urls
ADVERTISE_CLIENT_URLS='-advertise-client-urls http://${node_ips[$((node_id-1))]}:$client_port'
# -listen-client-urls
LISTEN_CLIENT_URLS='-listen-client-urls http://0.0.0.0:$client_port'
# -initial-cluster-token
INITIAL_CLUSTER_TOKEN='-initial-cluster-token $cluster_token'
# -initial-cluster
INITIAL_CLUSTER='-initial-cluster $clusters'
# -initial-cluster-state
INITIAL_CLUSTER_STATE='-initial-cluster-state $cluster_state'
# other parameters
ETCD_OPTS='--data-dir $data_dir'
" > $conf_file
echo "[Unit]
Description=ETCD
[Service]
Type=notify
EnvironmentFile=${conf_file}
ExecStart=$etcd_file \$ETCD_NAME \\
          \$INITIAL_ADVERTISE_PEER_URLS \\
          \$LISTEN_PEER_URLS \\
          \$ADVERTISE_CLIENT_URLS \\
          \$LISTEN_CLIENT_URLS \\
          \$INITIAL_CLUSTER_TOKEN \\
          \$INITIAL_CLUSTER \\
          \$INITIAL_CLUSTER_STATE \\
          \$ETCD_OPTS
Restart=always

[Install]
WantedBy=multi-user.target
" > /lib/systemd/system/etcd.service

# STEP 06: start etcd
systemctl stop etcd
systemctl daemon-reload
systemctl start etcd
systemctl status -l etcd
systemctl enable etcd
