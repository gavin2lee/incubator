<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
.form_block {
	padding: 5px;
}

.form_block label {
	width: 100px;
}

.specList {
	margin-left: 150px;
}

.u-flavor {
	padding-bottom: 5px;
	height: auto;
	overflow: hidden;
	padding-left: 1px;
}
</style>
		<div class="plate">
			<div class="project_r">
				<!--网络存储信息开始-->
				<div class="form_control">
					<div class="form_block">
						<input type="hidden" name="instanceId" value="${nasInfo.nasId}" />
						<label>实例名称</label>
						<div class="specList">
							<input type="text" readonly="readonly"
								value="${nasInfo.instanceName}" />
						</div>
					</div>
					<div class="form_block">
						<label>环境类型</label>
						<div class="specList">
							<ul class="u-flavor">
								<c:forEach items="${envTypes}" var="item">
									<li class="${nasInfo.envType eq item.key ? 'selected' : '' }">${item.value}</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					<div class="form_block">
						<label>存储类型</label>
						<div class="specList">
							<c:choose>
								<c:when test="${nasInfo.nasType eq 'NFS'}">
									<span>NFS 共享存储</span>
								</c:when>
								<c:when test="${nasInfo.nasType eq 'ISCSI'}">
									<span>ISCSI卷存储</span>
								</c:when>
							</c:choose>
						</div>
					</div>
					<div class="form_block">
						<label>存储空间</label>
						<div class="specList">
							<ul class="u-flavor">
								<li class="selected">${nasInfo.allocateStorageInfo}</li>
							</ul>
						</div>
					</div> 
					<div class="form_block">
						<label>挂载路径信息</label>
						<div class="specList">
							<textarea style="width: 300px; height: 90px;"
								readonly="readonly">${nasInfo.iscsiInfo}</textarea>
						</div>
					</div> 
					<div class="form_block">
						<label>租户组织</label>
						<div class="specList">
							<span>${nasInfo.attach['tenantName'] }</span>
						</div>
					</div>
					<div class="form_block">
						<label>创建人</label>
						<div class="specList">
							<span>${nasInfo.attach['createrName'] }</span>
						</div>
					</div>
				</div>
				<!--网络存储实例信息结束-->
			</div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super /> 
	</template:replace>
</template:include>

