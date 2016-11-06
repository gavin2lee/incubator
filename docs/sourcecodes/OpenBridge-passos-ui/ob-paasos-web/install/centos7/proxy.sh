#!/bin/bash

# Copyright 2014 The Kubernetes Authors All rights reserved.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.


MASTER_ADDRESS=${1:-"8.8.8.18"}

NODE_ADDRESS=$host_ip


cat <<EOF >${kube_cfg}/kube-proxy.conf
# --logtostderr=true: log to standard error instead of files
KUBE_LOGTOSTDERR="--logtostderr=true"

#  --v=0: log level for V logs
KUBE_LOG_LEVEL="--v=4"

# --hostname-override="": If non-empty, will use this string as identification instead of the actual hostname.
NODE_HOSTNAME="--hostname-override=${NODE_ADDRESS}"

# --master="": The address of the Kubernetes API server (overrides any value in kubeconfig)
KUBE_MASTER="--master=http://${MASTER_ADDRESS}:8080"

# --proxy-mode=userspace
#KUBE_OPTION="--proxy-mode=userspace"
EOF

KUBE_PROXY_OPTS="   \${KUBE_LOGTOSTDERR} \\
                    \${KUBE_LOG_LEVEL}   \\
                    \${NODE_HOSTNAME}    \\
                    \${KUBE_MASTER}     \\
                    \${KUBE_OPTION}"

cat <<EOF >/usr/lib/systemd/system/kube-proxy.service
[Unit]
Description=Kubernetes Proxy
After=network.target

[Service]
EnvironmentFile=-${kube_cfg}/kube-proxy.conf
ExecStart=${kube_bin}/kube-proxy ${KUBE_PROXY_OPTS}
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl stop kube-proxy
systemctl disable kube-proxy
systemctl enable kube-proxy
systemctl start kube-proxy
systemctl status kube-proxy

echo "proxy安装完成."
