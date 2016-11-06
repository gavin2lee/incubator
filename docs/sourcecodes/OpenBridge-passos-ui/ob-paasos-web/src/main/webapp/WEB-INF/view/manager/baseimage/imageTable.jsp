<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="details_nr">
	<table class="table_ob">
		<tbody>
			<tr>
				<th>镜像图标</th>
				<th>镜像名称</th>
				<th>镜像版本</th>
				<th>状态</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${page }" var="image">
				<tr>
					<td><div class="img-container"><img src="${WEB_APP_PATH }/paas/file/view.do?filePath=${image.iconPath}"></div></td>
					<td><a href="view.do?id=${image.id }">${image.name }</a></td>
					<td>${image.version }</td>
					<td>
					<c:choose>
						<c:when test="${image.status == 0}"><i class="pas_ico zt_ico fail_ico"></i>构建失败</c:when>
						<c:when test="${image.status == 1}"><i class="pas_ico zt_ico doing_ico"></i>未构建</c:when>
						<c:when test="${image.status == 5}"><i class="pas_ico zt_ico doing_ico"></i>构建中</c:when>
						<c:when test="${image.status == 6}"><i class="pas_ico zt_ico doing_ico"></i>删除中</c:when>
						<c:when test="${image.status == 10}"><i class="pas_ico zt_ico done_ico"></i>构建成功</c:when>
						<c:when test="${image.status == 11}"><i class="pas_ico zt_ico fail_ico"></i>删除失败</c:when>
					</c:choose>
					</td>
					<td>
						<c:if test="${image.status==1 || image.status==0 || image.status==10 }">
							<a class="mr5" href="javascript:buildImage('${image.id }');">构建</a>
						</c:if>
<%-- 						<c:if test="${image.status==1 || image.status==0 }"> --%>
							<a class="mr5" href="javascript:updateImage('${image.id }');">修改</a>
<%-- 						</c:if> --%>
						<c:if test="${image.status!=5 && image.status!=6 }">
							<a class="mr5" href="javascript:deleteImage('${image.id }');">删除</a>
						</c:if>
						<c:if test="${image.status!=1 && image.status!=6}">
							<a class="mr5" href="javascript:viewLog('${image.id }');">日志</a>
						</c:if>
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
			href="javascript:loadItems(!{pageNo},!{pageSize});"
			id="baseImagePage"></ui:pagination>
	</c:if>
</div>