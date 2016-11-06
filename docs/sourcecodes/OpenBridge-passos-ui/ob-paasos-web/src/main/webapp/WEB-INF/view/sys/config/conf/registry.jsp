<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>Docker 仓库地址</label> 
		<div class="form-right"><input type="text" style="width: 400px;"
			value="${ config['paasos.docker.registry'] }" name="config['paasos.docker.registry']"   />
			<p class="help_text">
				<span><em>填写内容不需要包含http://</em></span>
			</p>
		</div>
	</div>
</div>
<div class="line3"></div>
