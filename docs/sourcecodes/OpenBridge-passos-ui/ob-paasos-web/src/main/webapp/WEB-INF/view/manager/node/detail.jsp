<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="form_block">
	<label>主机名称</label>

	<p class="form_text">${node.name }&nbsp;</p>
</div>

<div class="form_block">
	<label>操作系统</label>

	<p class="form_text">${node.operateSystem }&nbsp;</p>
</div>
<div class="form_block">
	<label>所属环境</label>

	<p class="form_text">${node.envType }&nbsp;</p>
</div>
<div class="form_block">
	<label>状态</label>

	<p class="form_text">${node.status }&nbsp;</p>
</div>
<div class="form_block">
	<label>IP</label>

	<p class="form_text">${node.ip }&nbsp;</p>
</div>
<div class="form_block">
	<label>CPU总量</label>

	<p class="form_text">${empty node.cpu ? '0' : node.cpu } 个</p>
</div>
<div class="form_block">
	<label>内存总量</label>

	<p class="form_text">${empty node.ram ? '0' : node.ram }MB</p>
</div>
<div class="form_block">
	<label>运行实例</label>

	<p class="form_text">${node.instanceCount } 个</p>
</div>
<div class="form_block">
	<label>关联租户</label>

	<p class="form_text">
		<a
			href="${WEB_APP_PATH }/manager/tenant/detail.do?id=${tenant.tenantId }">${tenant.tenantName }&nbsp;</a>
	</p>
</div>