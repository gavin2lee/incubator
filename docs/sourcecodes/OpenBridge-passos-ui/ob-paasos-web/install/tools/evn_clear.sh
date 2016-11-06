#!/bin/bash

echo "clear docker..."
yum remove docker* -y

find /|grep docker |xargs rm -rf 

echo "clear openbridge..."
rm -rf /opt/openbridge/

echo "clear done."
