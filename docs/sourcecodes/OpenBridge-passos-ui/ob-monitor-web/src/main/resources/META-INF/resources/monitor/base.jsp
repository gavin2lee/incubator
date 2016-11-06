<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="monitor">
	<template:replace name="content">
		<div class="left_sub_menu left_sub_menu_pas">
            <ul>
            	<li>
                	<a class="${ param['nav-left'] eq 'app' ? 'select' : '' }" href="${ WEB_APP_PATH }/monitor/architecture/index">
                    <i class="left_menu_ico l_ico00"></i>平台监控</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'resources' ? 'select' : '' }" href="${ WEB_APP_PATH }/monitor/resources/index">
                    <i class="left_menu_ico l_ico_jcjx"></i>资源监控</a>
                </li> 
                 <li>
                	<a class="${ param['nav-left'] eq 'application' ? 'select' : '' }" href="#">
                    <i class="left_menu_ico l_ico_jcjx"></i>应用监控</a>
                </li>
               <%--  <li>
                	<a class="${ param['nav-left'] eq 'node' ? 'select' : '' }" href="${ WEB_APP_PATH }/monitor/node/list">
                    <i class="left_menu_ico l_ico_jcjx"></i>节点监控</a>
                </li>  --%>
            </ul>
        </div>
		<template:block name="content-body">
			
		</template:block>
	</template:replace>
</template:include>