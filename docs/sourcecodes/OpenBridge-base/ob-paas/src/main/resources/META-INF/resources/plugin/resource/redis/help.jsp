<%@ page language="java" pageEncoding="UTF-8"%>
<p>Redis是开源的，基于Key-Value的存储服务。</p>
<br/>
<p>PAASOS_REDIS_HOST_IP(${PAASOS_REDIS_HOST_IP })是集群环境下程序中访问redis主机的IP地址</p>
<p>PAASOS_REDIS_HOST_PORT(${PAASOS_REDIS_HOST_PORT })是集群环境下程序中访问redis主机的端口</p>
<p>PAASOS_REDIS_USERNAME(${PAASOS_REDIS_USERNAME})是程序访问redis的用户名</p>
<p>PAASOS_REDIS_PASSWORD(${PAASOS_REDIS_PASSWORD})是程序访问redis的密码</p>
<br/>
<p>${externalIP }是外部访问redis主机的IP地址</p>
<p>${externalPort }是外部访问redis主机的端口</p>
<br/>
<p>http://${manageIP}:${managePort}/是管理redis的web控制台</p>