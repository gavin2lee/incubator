#!/bin/sh

# change hostname for centos
# sh change_hostname.sh <new hostname>
# xiaoshengxu@sohu-inc.com
# 2016-01-11

network_file=/etc/sysconfig/network
hostname_file=/etc/hostname
hosts_file=/etc/hosts
new_hostname=$1

# STEP1: check parameters
if [ "$1" == "" ]; then
  echo "Usage: sudo sh change_hostname.sh <new-hostname>"
  exit 1
else
  echo "new hostname will be $1"
fi

# STEP2: modify network configure file
true > network_tmp
while read -a network_info
do
  value=${network_info[@]}
  var=`echo $value | awk '{split($0,a,"="); print a[1]}'`
  if [[ "$var" = HOSTNAME ]]; then
    value=$var=$new_hostname
  fi
  echo $value >> network_tmp
done < $network_file
mv network_tmp $network_file

#STEP3: modify hostname configure file
echo $new_hostname > $hostname_file

#STEP4: modify hosts
ips=(`ip addr show | grep inet | grep -v inet6 | grep brd | awk '{print $2}' | cut -f1 -d '/'`)
if [ "${ips[0]}" == "" ]; then
  echo "[ERROR] get ip address error"
  exit 1
else
  ip=${ips[0]}
fi
exist_hosts=0
while IFS='' read -r line || [[ -n "$line" ]]; do
  ip_tmp=$(echo $line | cut -f1 -d ' ')
  hostname_tmp=$(echo $line | cut -f2 -d ' ')
  if [ "$ip" == "$ip_tmp" ]; then
    if [ "$new_hostname" == "$hostname_tmp" ]; then
      exist_hosts=1
      break
    fi
  fi
done < $hosts_file
if [ $exist_hosts -eq 0 ]; then
  echo $ip $new_hostname >> $hosts_file
fi

#STEP5: set hostname and restart network
hostname $new_hostname
/etc/init.d/network restart
