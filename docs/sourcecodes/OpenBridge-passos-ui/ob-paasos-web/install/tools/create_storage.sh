#!/bin/bash

if [ ! -d "/dev/vgdata" ]; then

  pvcreate /dev/sdb
  vgcreate vgdata /dev/sdb

  lvcreate -n docker-metadata -L 2G vgdata
  lvcreate -n docker-data -l 100%FREE vgdata

  echo "create blk disk success."
else
  echo "blk disk exits!"
fi
