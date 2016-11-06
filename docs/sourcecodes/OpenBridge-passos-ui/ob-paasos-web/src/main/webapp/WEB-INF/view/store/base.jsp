<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="store">
	<template:replace name="content">
		<%-- <div class="left_sub_menu left_sub_menu_pas">
            <ul>
            	<li>
                	<a class="${ param['nav-left'] eq 'overview' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/overview/index.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico00"></i>概述</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'build' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/build/index.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico00"></i>构建</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'deploy' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/deploy/index.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico01"></i>部署</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'monitor' ? 'select' : '' }" href="${ WEB_APP_PATH }/project/monitor.do?projectId=${project.projectId}">
                    <i class="left_menu_ico l_ico02"></i>监控</a>
                </li>
            </ul>
        </div> --%>
        <script type="text/javascript" src="${WEB_APP_PATH }/assets/js/image/build.js"></script>
		<template:block name="content-body">
			
		</template:block>
	</template:replace>
</template:include>