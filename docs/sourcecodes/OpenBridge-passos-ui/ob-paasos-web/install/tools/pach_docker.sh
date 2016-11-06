#!/bin/bash
registry=""

cat <<EOF > /etc/sysconfig/docker
  DOCKER_OPTS="--insecure-registry=127.0.0.1:5000 -s devicemapper --storage-opt dm.datadev=/dev/vgdata/loopdata --storage-opt dm.metadatadev=/dev/vgdata/metadata"
  DOCKER_OPTS="--insecure-registry=127.0.0.1:5000"
  DOCKER_STORAGE_OPTIONS="-s devicemapper --storage-opt dm.datadev=/dev/vgdata/docker-data --storage-opt dm.metadatadev=/dev/vgdata/docker-metadata"
EOF

cat <<EOF > /lib/systemd/system/docker.service
[Unit]
Description=Docker Application Container Engine
Documentation=https://docs.docker.com
After=network.target docker.socket
Requires=docker.socket

[Service]
Type=notify
# the default is not to use systemd for cgroups because the delegate issues still
# exists and systemd currently does not support the cgroup feature set required
# for containers run by docker
#ExecStart=/usr/bin/docker daemon -H fd://
EnvironmentFile=/etc/sysconfig/docker
ExecStart=/usr/bin/docker daemon \$DOCKER_OPTS \\
	\$DOCKER_STORAGE_OPTIONS \\
	\$DOCKER_NETWORK_OPTIONS \\
	\$ADD_REGISTRY \\
	\$BLOCK_REGISTRY \\
	\$INSECURE_REGISTRY
MountFlags=slave
LimitNOFILE=1048576
LimitNPROC=1048576
LimitCORE=infinity
TimeoutStartSec=0
# set delegate yes so that systemd does not reset the cgroups of docker containers
Delegate=yes

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl restart docker
systemctl status docker
