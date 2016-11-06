<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>

<div class="form_control form_control_setting p20">
	<div class="form_block">
		<label>镜像构建服务</label>
		<div class="form-right">
			<input type="text" style="width: 400px;" value="${ config['paasos.build.url'] }"
				name="config['paasos.build.url']" />
			<p class="help_text">
				<span><em>镜像构建服务器服务器地址，填写内容不需要包含http://</em></span>
			</p>
		</div>
	</div>
</div>
<div class="line3"></div>