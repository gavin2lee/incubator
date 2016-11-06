<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="details_nr">
	<table class="table_ob">
		<tbody>
			<tr>
				<th>应用图标</th>
				<th>应用名称</th>
				<th>应用版本</th>
				<th>状态</th>
				<th style="width:35%;">部署</th>
				<th>操作</th>
			</tr>
			<c:forEach items="${page }" var="app">
				<tr>
					<td><div class="img-container"><img src="${WEB_APP_PATH }/paas/file/view.do?filePath=${app.iconPath}"></div></td>
					<td id="appName"><a href="detail.do?id=${app.id }" >${app.appName }</a></td>
					<td>${app.version }</td>
					<td>
					<c:choose>
						<c:when test="${app.status == 0}"><i class="pas_ico zt_ico fail_ico"></i>构建失败</c:when>
						<c:when test="${app.status == 1}"><i class="pas_ico zt_ico doing_ico"></i>未构建</c:when>
						<c:when test="${app.status == 5}"><i class="pas_ico zt_ico doing_ico"></i>构建中</c:when>
						<c:when test="${app.status == 6}"><i class="pas_ico zt_ico doing_ico"></i>删除中</c:when>
						<c:when test="${app.status == 10}"><i class="pas_ico zt_ico done_ico"></i>可部署</c:when>
						<c:when test="${app.status == 11}"><i class="pas_ico zt_ico fail_ico"></i>删除失败</c:when>
					</c:choose>
					</td>
					<td data-id="${app.id }">
					<script>
					$.get(WEB_APP_PATH+"/project/getProjectByBusId.do",{
						businessId : '${app.id }'
					},function(projList){
						var p = $("td[data-id='${app.id}']");
            			if(projList && projList.length>0){
       						var projCon = "";
       						$.each(projList,function(i,v){
       							if(i>0)	projCon += "、";
       							projCon += "<a href='"+WEB_APP_PATH+"/project/overview/index.do?projectId="+v.projectId+"'>"+v.projectName+"</a>"
       						})
       						p.html(projCon);
            			}else{
            				p.html("无");
            			}
					});
					</script>
					</td>
					
					<td>
					<auth:validator value="functionValidator(functionId=paasos.store.deploy)">					
						<c:if test="${app.status==1 || app.status==0 || app.status==10 }">
							<a href="javascript:buildApp('${app.id }');" >构建</a>
						</c:if>
						<c:if test="${app.status==10}">
							<a href="javascript:deployApp('${app.id }','${app.appName }');" >部署</a>
						</c:if></auth:validator>
						<auth:validator value="functionValidator(functionId=paasos.store.manager)">
						<a href="add_app.do?id=${app.id }" >编辑</a>
						<c:if test="${app.status!=5 && app.status!=6 }">
							<a href="javascript:deleteApp('${app.id }');" >删除</a>
						</c:if>
						</auth:validator>	
						<c:if test="${app.status!=1 && app.status!=6}">
							<a class="log-button" href="javascript:viewLog('${app.id }');">日志</a>
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