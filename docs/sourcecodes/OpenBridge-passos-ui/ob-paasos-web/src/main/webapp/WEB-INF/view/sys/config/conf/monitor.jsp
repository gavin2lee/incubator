<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>Open-Falcon Dashboard</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['paasos.monitor.url'] }" name="config['paasos.monitor.url']" />
			<p class="help_text" >
				<span>Open-Falcon Dashboard的地址,配置如<em>http://127.0.0.1:88</em>
				 其中IP及端口地址为实际部署的服务器IP及端口</span></p>
		</div>
	</div>
	<div class="form_block">
		<label>Open-Falcon Transfer</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['paasos.monitor.transfer'] }" name="config['paasos.monitor.transfer']"  />
			<p class="help_text" >
				<span>Open-Falcon Transfer的地址,配置如<em>http://127.0.0.1:88</em>
				 其中IP及端口地址为实际部署的服务器IP及端口</span></p>
		</div>
	</div>
	<div class="form_block">
		<label>Open-Falcon Graph</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['paasos.monitor.graph'] }" name="config['paasos.monitor.graph']"  />
				<p class="help_text" >
				<span>Open-Falcon Graph的地址,配置如<em>http://127.0.0.1:88</em>
				 其中IP及端口地址为实际部署的服务器IP及端口</span></p>
		</div>
	</div>
	<div class="form_block">
		<label>Open-Falcon Query</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			 value="${ config['paasos.monitor.query'] }" name="config['paasos.monitor.query']"  />
			 <p class="help_text" >
				<span>Open-Falcon Query的地址,配置如<em>http://127.0.0.1:88</em>
				 其中IP及端口地址为实际部署的服务器IP及端口</span></p>
		</div>
	</div>
</div>
<div class="line3"></div>
