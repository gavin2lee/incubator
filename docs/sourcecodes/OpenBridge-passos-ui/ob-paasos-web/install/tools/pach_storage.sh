#!/bin/bash

echo 'DOCKER_STORAGE_OPTIONS="-s devicemapper --storage-opt dm.datadev=/dev/vgdata/docker-data --storage-opt dm.metadatadev=/dev/vgdata/docker-metadata"' \
  >> /etc/sysconfig/docker

systemctl stop docker
rm -rf /var/lib/docker/*
systemctl restart docker
systemctl status docker
echo "pach docker success."