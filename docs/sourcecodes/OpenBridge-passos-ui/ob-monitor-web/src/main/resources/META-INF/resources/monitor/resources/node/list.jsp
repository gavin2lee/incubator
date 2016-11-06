<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
body {
	overflow: auto;
}
</style>
		<div class="form_control">
			<div class="choose_table_list">
				<table class="table_ob" id="dataTable">
					<thead>
						<tr>
							<th>节点名称</th>
							<th>CPU总量（个）</th>
							<th>内存总量（MB）</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${nodes }" var="node">
							<c:if test="${not empty node.id }">
								<tr>
									<td>${node.name }</td>
									<%-- <td>${node.cpu }</td>
									<td>${node.ram }</td> --%>
									<td class="resourceProgress" max="${node.cpu}"
										endpoint="${node.ip}" counter="cpu.busy"></td>
									<td class="resourceProgress" max="${node.ram}"
										endpoint="${node.ip}" counter="mem.memused"></td> 
								</tr>
							</c:if>
						</c:forEach>
						<c:forEach items="${nodes }" var="node">
							<c:if test="${empty node.id }">
								<tr>
									<td>${node.name }</td>
									<td class="resourceProgress" max="${node.cpu}"
										endpoint="${node.ip}" counter="cpu.busy"></td>
									<td class="resourceProgress" max="${node.ram}"
										endpoint="${node.ip}" counter="mem.memused"></td>
								</tr>
							</c:if>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<template:super />
		 <script type="text/javascript"
			src="${WEB_APP_PATH}/assets/js/resources.js"></script>
		<script>
			$(function() {
				ResourceProgress.init({
					'webapp' : '${WEB_APP_PATH}',
					'parentId' : 'dataTable'
				});
			});
		</script> 
	</template:replace>
</template:include>