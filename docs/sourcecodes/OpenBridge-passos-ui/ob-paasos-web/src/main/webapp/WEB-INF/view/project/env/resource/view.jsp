<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		资源信息查看
	</template:replace>
	<template:replace name="body">
		<style type="text/css">
		body {
			overflow: auto;
		}
		</style>
		<div class="r_block p20">
			<div class="panel-heading">
				<h3 class="f14 h3_color h3_btm_line">
					资源参数
				</h3> 
				<div class="title_line"></div>
			</div>
			<div class="panel-body">
				<table class="table_ob">
					<thead>
						<tr>
							<th style="width: 260px;">Key</th>
							<th>Value</th>
						</tr>
					</thead>
					<tbody id="configInfo">
					</tbody>
				</table>
			</div>
			<c:if test="${not (resource.resourceId eq 'define') }">
			<div class="panel-heading" style="padding-top:12px;">
				<h3 class="f14 h3_color h3_btm_line">
					申请参数 
				</h3> 
				<div class="title_line"></div>
			</div>
			<div class="panel-body">
				<table class="table_ob">
					<thead>
						<tr>
							<th style="width: 260px;">Key</th>
							<th>Value</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${ resource.config }" var="row">
							<tr>
								<td>${row.key}</td>
								<td>${row.value}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
			</c:if>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<script>
			$(function(){
				var table=$("#configInfo");
				table.append("<tr><td colspan='2'><image src='${WEB_APP_PATH}/assets/images/loading.gif'/></td></tr>");
				var recordId= '${resource.recordId}';
				var url = "${WEB_APP_PATH}/project/env/resource/queryResourceInfo.do?recordId="+recordId;
				$.get(url,function(json){
					if(json.code==0){
						var data = json.data;
						table.empty();
						for(var key in data){
							var value= data[key];
							table.append("<tr><td>"+key+"</td><td>"+value+"</td></tr>");
						}
					}else{
						table.empty().append("<tr><td colspan='2'>无法获资源取参数</td></tr>");
					}
				});
			});
		</script>
	</template:replace>
</template:include>