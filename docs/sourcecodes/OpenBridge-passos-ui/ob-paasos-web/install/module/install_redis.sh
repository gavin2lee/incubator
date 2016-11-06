#!/bin/bash

epel=(`rpm -qa|grep epel`)
if [ "$epel" == "" ]; then
  yum install -y epel-release
fi
yum install -y redis

systemctl enable redis
systemctl start redis
systemctl status redis
