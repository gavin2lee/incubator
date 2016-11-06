<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="details_nr">
	<table class="table_ob">
		<tbody>
			<tr>
				<th width="20%">组织名称</th>
				<th width="30%">包含的开发人员</th>
				<th>部署数</th>
				<th>创建时间</th>
				<th>管理员</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${page }" var="tenant">
				<tr id="${tenant.tenantId }">
					<td><a href="detail.do?id=${tenant.tenantId}">${tenant.tenantName }</a></td>
					<td><c:forEach items="${tenant.member }" var="mem"
							varStatus="status">
							<a href="javascript:void('${mem.userId }');">${mem.userName }</a>
							<c:if test="${!status.last }">、</c:if>
						</c:forEach></td>
					<td>${tenant.deployCount}</td>
					<td><fmt:formatDate value="${tenant.createTime}"
							pattern="yyyy-MM-dd HH:mm:ss" /></td>
					<td>${tenant.admin.userName }</td>
					<td>
<%-- 						<a class="mr5" href="javascript:deleteTenant('${tenant.tenantId }');">删除</a>  --%>
						<a class="mr5" href="javascript:updateTenant('${tenant.tenantId }');">修改</a>
					</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<c:if test="${empty page || fn:length(page)==0 }">
		<p style="line-height: 30px; text-align: center; font-size: 14px;">没有相关内容。</p>
	</c:if>
	<c:if test="${not empty page}">
		<ui:pagination data="${page }"
			href="javascript:loadItems(!{pageNo},!{pageSize});" id="tenantPage"></ui:pagination>
	</c:if>
</div>