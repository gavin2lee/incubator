<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>ZooKeeper测试环境</label>
		<div class="form-right">
			<input type="text" style="width: 400px;"
				value="${ config['zookeeper.test'] }" name="config['zookeeper.test']" />
			<p class="help_text" ><span>ZooKeeper测试环境的地址,配置如
			                         <em>zookeeper:http://127.0.0.1:88</em>
			                          其中IP及端口地址为实际ZooKeeper测试环境的服务器IP及端口</span>
			               </p>
		</div>
	</div>
	<div class="form_block">
		<label>ZooKeeper生产环境</label>
		<div class="form-right">
			<input type="text" style="width: 400px;"
				value="${ config['zookeeper.live'] }" name="config['zookeeper.live']" />
			<p class="help_text" ><span>ZooKeeper生产环境的地址,配置如
			                         <em>zookeeper:http://127.0.0.1:88</em>
			                          其中IP及端口地址为实际ZooKeeper生产环境的服务器IP及端口</span>
			               </p>
		</div>
	</div>
</div>
<div class="line3"></div>
