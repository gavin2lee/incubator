#!/bin/bash
## Install ZooKeeper ##

function usage() {
	echo "Install ZooKeeper" 1>&2;
	echo "Usage: $0 [-i <id>] [-s <servers>]" 1>&2;
	exit 1;
}

function show_config() {
	echo "Id = ${ID}";
	echo "Servers = ${SERVERS}";
}

INSTALL_PACKAGE=./software/zookeeper-3.4.8.tar.gz			
INSTALL_VERSION=zookeeper-3.4.8
INSTALL_BASE=/opt/zookeeper
INSTALL_DATADIR=/data/zookeeper-data
INSTALL_CONFIG=${INSTALL_BASE}/${INSTALL_VERSION}/conf/zoo.cfg
INSTALL_STARTUP_SCRIPT=/lib/systemd/system/zookeeper.service
INSTALL_CLIENT_PORT=2181

ID="1"
SERVERS=

while getopts ":i:s:" arg; do
	case $arg in
	i)	ID=$OPTARG;;
	s)	SERVERS=$OPTARG;;
	?)	usage; exit 1;;
	esac
done

shift $[$OPTIND-1]

show_config;

function make_servers_config() {
	local servers=${SERVERS//,/ }
	local i=1
	for server in $servers   
	do  
   		echo "server.${i}=${server}:2888:3888"
		i=$((${i}+1))  
	done
}


## Install Java ##
#yum install -y java

## Install ##
mkdir -p ${INSTALL_BASE}
tar xzf ${INSTALL_PACKAGE} -C ${INSTALL_BASE}


## Make Servers ##
SERVERS=${SERVERS//,/ } 


## Make Config ##
mkdir -p $INSTALL_DATADIR/data
mkdir -p $INSTALL_DATADIR/logs

SERVERS_CFG=$(make_servers_config)

echo "
# The number of milliseconds of each tick
tickTime=2000
# The number of ticks that the initial 
# synchronization phase can take
initLimit=10
# The number of ticks that can pass between 
# sending a request and getting an acknowledgement
syncLimit=5
# the directory where the snapshot is stored.
# do not use /tmp for storage, /tmp here is just 
# example sakes.
dataDir=${INSTALL_DATADIR}/data
dataLogDir=${INSTALL_DATADIR}/logs
# the port at which the clients will connect
clientPort=${INSTALL_CLIENT_PORT}
# the maximum number of client connections.
# increase this if you need to handle more clients
#maxClientCnxns=60
#
# Be sure to read the maintenance section of the 
# administrator guide before turning on autopurge.
#
# http://zookeeper.apache.org/doc/current/zookeeperAdmin.html#sc_maintenance
#
# The number of snapshots to retain in dataDir
#autopurge.snapRetainCount=3
# Purge task interval in hours
# Set to \"0\" to disable auto purge feature
#autopurge.purgeInterval=1


# servers
${SERVERS_CFG}

" > ${INSTALL_CONFIG}

## Make Auto Start Script ##

echo "
[Unit]
Description=ZooKeeper
Before=
After=network.target

[Service]
Type=forking
EnvironmentFile=
ExecStart=${INSTALL_BASE}/${INSTALL_VERSION}/bin/zkServer.sh start

Restart=on-abort

[Install]
WantedBy=multi-user.target

" > ${INSTALL_STARTUP_SCRIPT}

## Set Id ##
if [ "${ID}" != "" ]; then
	echo "${ID}" > ${INSTALL_DATADIR}/data/myid
fi


## Start ##

systemctl daemon-reload
systemctl start zookeeper
systemctl enable zookeeper

## Show Status ##

${INSTALL_BASE}/${INSTALL_VERSION}/bin/zkServer.sh status

#netstat -anp | grep ${INSTALL_CLIENT_PORT}
systemctl status zookeeper
