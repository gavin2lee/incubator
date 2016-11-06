#!/bin/sh

git pull

subv=`date +%Y%m%d%H%M%S`

mvn clean package -Dmaven.test.skip=true

docker build -t docker.yihecloud.com/paasos/os:3.0_${subv} .

docker push docker.yihecloud.com/paasos/os:3.0_${subv}