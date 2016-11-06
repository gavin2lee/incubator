<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>Web SSH服务地址</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['paasos.webssh.url'] }" name="config['paasos.webssh.url']"  />
			<p class="help_text" >
       			<span>Web SSH服务地址<em>http://192.168.1.54:4200/exec</em>
       			其中IP及端口地址为实际部署的服务器IP及端口</span>
       		</p>
		</div>
	</div>
</div>
<div class="line3"></div>
