FROM docker.yihecloud.com/base/java:8_60

MAINTAINER liyong yli@yihecloud.com

WORKDIR /program

COPY /docker/startup.sh /program/startup.sh
RUN chmod +x /program/startup.sh

COPY /target/openbridge-monitor*.jar /program/app.jar

CMD /program/startup.sh
