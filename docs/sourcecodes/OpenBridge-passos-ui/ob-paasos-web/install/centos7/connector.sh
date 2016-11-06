#!/bin/bash

# 安装kubenetes主节点
# wangxinxiang@yihecloud.com
# 2016-06-15

# 安装connector
cat <<EOF >/lib/systemd/system/docker-connector.service
[Unit]
Description=dockerk connector initialization
[Service]
ExecStart=/bin/bash -c '/opt/openbridge/bin/docker-connector'
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl stop docker-connector
systemctl disable docker-connector
systemctl enable docker-connector
systemctl start docker-connector
systemctl status docker-connector

echo "docker connector安装完成."