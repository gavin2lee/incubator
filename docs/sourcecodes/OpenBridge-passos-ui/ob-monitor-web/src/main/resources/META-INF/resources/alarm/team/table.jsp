<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="details_nr">
	<table class="table_ob">
		<tbody>
			<tr>
				<th>接收组名称</th>
				<th>创建人</th>
				<th>用户名</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${page }" var="team">
				<tr id="${team.id }" >
					<td>${team.name }</td>
					<td>${team.creatorUserName }</td>
					<td><c:forEach items="${team.member }" var="mem"
							varStatus="status">
							<a href="javascript:detail('${mem.userId }');">${mem.userName }</a>
							<c:if test="${!status.last }">;</c:if>
						</c:forEach></td>
					<td>
						<a class="mr5" href="javascript:updateTeam('${team.id }');">修改</a>
						<a class="mr5" href="javascript:deleteTeam('${team.id }');">删除</a>
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
			href="javascript:loadItems(!{pageNo},!{pageSize});" id="teamPage"></ui:pagination>
	 </c:if> 
</div>