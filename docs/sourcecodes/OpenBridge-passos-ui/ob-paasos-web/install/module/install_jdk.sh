#!/bin/bash
## Install JDK ##

INSTALL_PACKAGE=./software/jdk-8u60-linux-x64.tar.gz
INSTALL_BASE=/opt/java

## Install ##
mkdir -p ${INSTALL_BASE}
tar zvxf ${INSTALL_PACKAGE} -C ${INSTALL_BASE}

## Config ##
cat <<EOF >> ~/.bashrc

JAVA_HOME=/opt/java/jdk1.8.0_60
PATH=\$JAVA_HOME/bin:\$PATH
export PATH
EOF
source ~/.bashrc

echo $JAVA_HOME
java -version
