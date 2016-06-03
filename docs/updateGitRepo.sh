#!/bin/bash

repos=("exercise" "accidents-dc" "incubator")
baseDir=`pwd`
echo $baseDir

for var in ${repos[@]};
do
  echo gotten into $var
  echo "going with git pull"
  cd "$baseDir/$var"
  git pull
done

cd $baseDir
