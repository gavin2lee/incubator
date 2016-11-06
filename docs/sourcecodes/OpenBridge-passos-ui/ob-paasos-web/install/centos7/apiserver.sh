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

SERVICE_CLUSTER_IP_RANGE=${1:-"10.1.128.0/20"}
ADMISSION_CONTROL=${2:-""}

NODE_ADDRESS=$host_ip
MASTER_ADDRESS='0.0.0.0'
ETCD_SERVERS="http://${host_ip}:4012"


cat <<EOF >${kube_cfg}/kube-apiserver.conf
# --logtostderr=true: log to standard error instead of files
KUBE_LOGTOSTDERR="--logtostderr=true"

# --v=0: log level for V logs
KUBE_LOG_LEVEL="--v=0"

# --etcd-servers=[]: List of etcd servers to watch (http://ip:port), 
# comma separated. Mutually exclusive with -etcd-config
KUBE_ETCD_SERVERS="--etcd-servers=${ETCD_SERVERS}"

# --insecure-bind-address=127.0.0.1: The IP address on which to serve the --insecure-port.
KUBE_API_ADDRESS="--insecure-bind-address=${MASTER_ADDRESS}"

# --insecure-port=8080: The port on which to serve unsecured, unauthenticated access.
KUBE_API_PORT="--insecure-port=8080"

# --kubelet-port=10250: Kubelet port
NODE_PORT="--kubelet-port=10250"

# --advertise-address=<nil>: The IP address on which to advertise 
# the apiserver to members of the cluster.
KUBE_ADVERTISE_ADDR="--advertise-address=${MASTER_ADDRESS}"

# --allow-privileged=false: If true, allow privileged containers.
KUBE_ALLOW_PRIV="--allow-privileged=false"

# --service-cluster-ip-range=<nil>: A CIDR notation IP range from which to assign service cluster IPs. 
# This must not overlap with any IP ranges assigned to nodes for pods.
KUBE_SERVICE_ADDRESSES="--service-cluster-ip-range=${SERVICE_CLUSTER_IP_RANGE}"

# --admission-control="AlwaysAdmit": Ordered list of plug-ins 
# to do admission control of resources into cluster. 
# Comma-delimited list of: 
#   LimitRanger, AlwaysDeny, SecurityContextDeny, NamespaceExists, 
#   NamespaceLifecycle, NamespaceAutoProvision,
#   AlwaysAdmit, ServiceAccount, ResourceQuota
KUBE_ADMISSION_CONTROL="--admission-control=${ADMISSION_CONTROL}"

# --client-ca-file="": If set, any request presenting a client certificate signed
# by one of the authorities in the client-ca-file is authenticated with an identity
# corresponding to the CommonName of the client certificate.
#KUBE_API_CLIENT_CA_FILE="--client-ca-file=/srv/kubernetes/ca.crt"

# --tls-cert-file="": File containing x509 Certificate for HTTPS.  (CA cert, if any,
# concatenated after server cert). If HTTPS serving is enabled, and --tls-cert-file
# and --tls-private-key-file are not provided, a self-signed certificate and key are
# generated for the public address and saved to /var/run/kubernetes.
#KUBE_API_TLS_CERT_FILE="--tls-cert-file=/srv/kubernetes/server.cert"

# --tls-private-key-file="": File containing x509 private key matching --tls-cert-file.
#KUBE_API_TLS_PRIVATE_KEY_FILE="--tls-private-key-file=/srv/kubernetes/server.key"

## --service_account_key_file=/etc/kube-serviceaccount.key
KUBE_OPTS="--service-node-port-range=20000-40000"

#
KUBE_API_SERVICE_ACCOUNT_KEY_FILE="--service_account_key_file=/etc/kube-serviceaccount.key"
EOF

KUBE_APISERVER_OPTS="   \${KUBE_LOGTOSTDERR}         \\
                        \${KUBE_LOG_LEVEL}           \\
                        \${KUBE_ETCD_SERVERS}        \\
                        \${KUBE_API_ADDRESS}         \\
                        \${KUBE_API_PORT}            \\
                        \${NODE_PORT}                \\
                        \${KUBE_ADVERTISE_ADDR}      \\
                        \${KUBE_ALLOW_PRIV}          \\
                        \${KUBE_SERVICE_ADDRESSES}   \\
                        \${KUBE_ADMISSION_CONTROL}   \\
                        \${KUBE_API_CLIENT_CA_FILE}  \\
                        \${KUBE_API_TLS_CERT_FILE}   \\
                        \${KUBE_OPTS}                \\
                        \${KUBE_API_SERVICE_ACCOUNT_KEY_FILE} \\
                        \${KUBE_API_TLS_PRIVATE_KEY_FILE}"
                        

cat <<EOF >/usr/lib/systemd/system/kube-apiserver.service
[Unit]
Description=Kubernetes API Server
Documentation=https://github.com/kubernetes/kubernetes

[Service]
EnvironmentFile=${kube_cfg}/kube-apiserver.conf
ExecStart=${kube_bin}/kube-apiserver ${KUBE_APISERVER_OPTS}
Restart=on-failure

[Install]
WantedBy=multi-user.target
EOF

systemctl daemon-reload
systemctl stop kube-apiserver
systemctl disable kube-apiserver
systemctl enable kube-apiserver
systemctl start kube-apiserver
systemctl status kube-apiserver

echo "apiserver安装完成."

