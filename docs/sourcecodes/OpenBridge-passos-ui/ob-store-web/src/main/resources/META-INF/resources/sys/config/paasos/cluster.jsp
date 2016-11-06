<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>K8s Api Server 地址</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['paasos.k8s.apiserver'] }" name="config['paasos.k8s.apiserver']" />
			<p class="help_text" >
				<span>K8s Api Server的地址,配置如<em>http://127.0.0.1:88</em>
				 其中IP及端口地址为实际部署的服务器IP及端口</span></p>
	</div>
	</div>
   <div class="form_block">
		<label>K8s Etcd 地址</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['paasos.k8s.etcdserver'] }" name="config['paasos.k8s.etcdserver']" />
			<p class="help_text" >
				<span>K8s Etcd的地址,配置如<em>http://127.0.0.1:88</em>
				 其中IP及端口地址为实际部署的服务器IP及端口</span></p>
	</div>
	</div>
</div>
<div class="line3"></div>
<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>资源分配服务REST</label>
		<div class="form-right">
			<input type="text" style="width: 400px;"
				value="${ config['paasos.resRestfulUrl'] }"
				name="config['paasos.resRestfulUrl']" />

		</div>
	</div>
	<div class="form_block">
		<label>PHPMyAdmin</label>
		<div class="form-right">
			<input type="text" style="width: 400px;" value="${ config['paasos.phpMyAdminUrl'] }" name="config['paasos.phpMyAdminUrl']" />
            <p class="help_text" >
       			<span>PHPMyAdmin地址<em>http://192.168.10.141:8000/phpmyadmin</em>
       			其中IP及端口地址为实际PHPMyAdmin部署的服务器IP及端口</span>
       		</p>
		</div>
	</div>
</div>
<div class="line3"></div>