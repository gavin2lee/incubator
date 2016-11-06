<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../../sys/manager.jsp" active="nginx">
	<template:replace name="title">
		Nginx 实例
	</template:replace>
	<template:replace name="detail">
         <div class="p20">
             <div class="r_title">
                 	<c:import url="../nav.jsp?tab=instances"></c:import>
             </div>
             <div class="r_con p10_0">
             	<table class="table_ob">
	               <tr>
	                   <th>名称</th>
	                   <th>服务器IP</th>
	                   <th>配置内容</th>
	                   <th>应用类型</th>
	                   <th>环境</th>
	               </tr>
				<c:forEach items="${ pageData }" var="row" varStatus="index">
	               <tr>
	               		<td>${ row.nginxName }</td>
	               		<td>${ row.attach['hostIp'] }</td>
	               		<td>${ row.confContent }</td>
	               		<td>${ row.businessType }</td>
	               		<td>
	               			<c:choose>
								<c:when test="${ row.envType eq 'dev' }">
									开发
								</c:when>
								<c:when test="${ row.envType eq 'test' }">
									测试环境
								</c:when>
								<c:when test="${ row.envType eq 'live' }">
									生产环境
								</c:when>
								<c:otherwise>
									${ row.envType }
								</c:otherwise>
							</c:choose>
	               		</td>
	               	</tr>
	            </c:forEach>
	            </table> 
			 
				<ui:pagination id="userPagging" data="${ pageData }" href="/paas/nginx/page.do">
				</ui:pagination> 
			</div>
		</div>
	</template:replace>
</template:include>