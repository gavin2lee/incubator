<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="project">
	<template:replace name="content">
		<div class="left_sub_menu left_sub_menu_pas">
            <ul>
            	<li>
                	<a class="${ param['nav-left'] eq 'overview' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/overview/index.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico00"></i>项目概述</a>
                </li>
                <c:if test="${ project.projectType ne 'store'}"> 
                <li>
                	<a class="${ param['nav-left'] eq 'build' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/build/index.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico_jcjx"></i>项目镜像</a>
                </li> 
                </c:if>
                <li>
                	<a class="${ param['nav-left'] eq 'deploy' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/env/index.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico_bs"></i>项目部署</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'monitor' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/monitor/index.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico_jk"></i>资源监控</a>
                </li>
            </ul>
        </div>
		<template:block name="content-body">
			
		</template:block>
	</template:replace>
</template:include>