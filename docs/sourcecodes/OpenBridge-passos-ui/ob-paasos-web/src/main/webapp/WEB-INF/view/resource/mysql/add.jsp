<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<c:if test="${empty param.dialog || !param.dialog }">
	<c:set var="isDialog" value="false"/>
		<template:include file="/WEB-INF/view/resource/base.jsp" nav-left="mysql">
			<template:replace name="title">
				PaaSOS--资源服务
			</template:replace>
			<template:replace name="content-path">
				<em>&gt;</em>
				<p class="app_a"> <a href="${WEB_APP_PATH}/resource/mysql/index.do">数据库</a> </p>
				<em>&gt;</em>
				<p class="app_a">创建数据库实例</p>
			</template:replace>
			<template:replace name="content-body">
				<%@include file="addContent.jsp" %>
			</template:replace>
			<template:replace name="bottom">
				<template:super/>
				
			</template:replace>
		</template:include>
	</c:if>
<c:if test="${not empty param.dialog && param.dialog }">
	<c:set var="isDialog" value="true"/>
	<%@include file="addContent.jsp" %>
<%-- 	<c:import url="createContent.jsp"></c:import> --%>
</c:if>

