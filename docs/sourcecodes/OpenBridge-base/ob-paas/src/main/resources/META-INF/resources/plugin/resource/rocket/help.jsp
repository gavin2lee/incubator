<%@ page language="java" pageEncoding="UTF-8"%>
<p>消息中间件的实现采用RabbitMQ，是一个在AMQP基础上完整的，可复用的企业消息系统。遵循Mozilla Public License开源协议。</p>
<br/>
<p>PAASOS_RABBIT_HOST_IP(${PAASOS_RABBIT_HOST_IP })是集群环境下程序中访问消息队列主机的IP地址</p>
<p>PAASOS_RABBIT_HOST_PORT(${PAASOS_RABBIT_HOST_PORT })是集群环境下程序中访问消息队列主机的端口</p>
<br/>
<p>${externalIP }是外部访问消息队列主机的IP地址</p>
<p>${externalPort }是外部访问消息队列主机的端口</p>
<br/>
<p>http://${manageIP}:${managePort}/是管理消息队列的web控制台</p>