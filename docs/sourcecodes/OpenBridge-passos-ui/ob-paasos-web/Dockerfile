FROM docker.yihecloud.com/paasos/web/base:2.0

WORKDIR /opt

COPY /docker/env.sh /opt/
COPY /docker/run.sh /opt/
COPY /docker/application.tmpl /opt/

RUN chmod +x /opt/run.sh \
	&& chmod +x /opt/run.sh \
	&& echo -e "\nexport PAASOS_UI_VERSION=\"3.0_`date +%Y%m%d%H%M%S`\"" >> /opt/env.sh

COPY /target/openbridge-paasos.war /opt/ROOT.war

ENV PREFIX os
CMD /opt/run.sh
