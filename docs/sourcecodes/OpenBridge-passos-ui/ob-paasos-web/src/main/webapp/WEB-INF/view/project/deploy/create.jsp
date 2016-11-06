<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<c:if test="${empty param.dialog || !param.dialog }">
	<c:set var="isDialog" value="false"/>
	<template:include file="../base.jsp"  nav-left="deploy">
		<template:replace name="title">
			项目概述
		</template:replace>
		<template:replace name="content-body">
			<div class="app_name">
	            <a href="proiect_list.html"><i class="icons go_back_ico"></i></a>
	
	            <p class="app_a"><a href="${ WEB_APP_PATH }/project/index.do">项目管理</a></p>
	            <em>&gt;</em>
	
	            <p class="app_a">
	            	<a href="${ WEB_APP_PATH }/project/overview/index.do?projectId=${project.projectId}">
	            	${project.projectName }&nbsp;
	            	</a>
	            </p>
	            <em>&gt;</em>
	
	            <p class="app_a">创建应用部署</p>
	        </div>
			<%@include file="createContent.jsp" %>
	        
		</template:replace>
	</template:include>
</c:if>
<c:if test="${not empty param.dialog && param.dialog }">
	<c:set var="isDialog" value="true"/>
	<%@include file="createContent.jsp" %>
<%-- 	<c:import url="createContent.jsp"></c:import> --%>
</c:if>

