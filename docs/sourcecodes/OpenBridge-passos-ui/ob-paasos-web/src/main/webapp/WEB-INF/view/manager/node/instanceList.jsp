<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<table class="table_ob">
	<thead>
		<tr>
			<th>实例名称</th>
			<th>实例IP</th>
			<th>部署镜像</th>
			<th>状态</th>
			<th>启动时间</th>
<!-- 			<th>所属组织</th> -->
			<th>日志</th>
			<th>事件</th>
			<th>控制台</th>
		</tr>
	</thead>
	<tbody>
	<c:forEach items="${pods }" var="pod">
		<tr>
			<td>${pod.name }/${pod.containerName }</td>
			<td>${pod.hostIp }</td>
			<td>${pod.deployName }</td>
			<td>${pod.status }</td>
			<td>${pod.startTime }</td>
			<td><a href="javascript:viewInstLog('${pod.name }','${pod.namespace }');">查看</a></td>
			<td><a href="javascript:void(0);" attrId="${pod.name }" name-space="${pod.namespace }" class="event-button">查看</a></td>
			<td><a href="javascript:loginSsh('${pod.webSSHUri }');">进入</a></td>
		</tr>
	</c:forEach>
	</tbody>
</table>
<template:include file="../../project/deploy/footer.jsp"></template:include>
<script type="text/javascript">
	function viewInstLog(name,namespace){
		showLogInDialog("getInstLog.do",{
			name : name,
			namespace : namespace
		});
	}
 	function loginSsh(uri){
//  		window.open(uri);
//  		common.dialogIframe("SSH控制台",uri,$(window).width()*0.8,$(window).height()*0.8);
 		layer.open({
 			type:2,
 			area : [$(window).width()*0.8,$(window).height()*0.8],
 			title:"SSH控制台",
 			content: uri,
 			maxmin:true
 		});
 	}
</script>
