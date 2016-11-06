#!/bin/sh

git pull
 
mvn clean deploy -Dmaven.test.skip=true
 