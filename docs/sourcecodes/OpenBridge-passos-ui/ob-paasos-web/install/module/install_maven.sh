#!/bin/bash
## Install Maven ##

INSTALL_PACKAGE=./software/apache-maven-3.3.9-bin.tar.gz
INSTALL_BASE=/opt/maven

## Install ##
mkdir -p ${INSTALL_BASE}
tar zvxf ${INSTALL_PACKAGE} -C ${INSTALL_BASE}

## Config ##
# echo -e "\n\nMAVEN_HOME=/opt/maven/apache-maven-3.3.3" | tee -a ~/.bashrc
# echo -e "\nJAVA_HOME=/opt/java/jdk1.8.0_60" | tee -a ~/.bashrc
# echo -e "\nPATH=\$JAVA_HOME/bin:\$MAVEN_HOME/bin:\$PATH" | tee -a ~/.bashrc
# echo -e "\nexport PATH" | tee -a ~/.bashrc
cat <<EOF >> ~/.bashrc

MAVEN_HOME=/opt/maven/apache-maven-3.3.9
PATH=\$MAVEN_HOME/bin:\$PATH
export PATH
EOF
source ~/.bashrc

echo $MAVEN_HOME
mvn -v
