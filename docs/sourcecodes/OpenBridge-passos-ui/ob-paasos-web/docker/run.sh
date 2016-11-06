#!/bin/sh

source /opt/env.sh

rm -rf /data/webapps/*
unzip /opt/ROOT.war -d /data/webapps/${PREFIX}/ 
dockerize -template /opt/application.tmpl:/data/webapps/${PREFIX}/WEB-INF/classes/application.properties

/opt/apache-tomcat-8.0.33/bin/catalina.sh run
