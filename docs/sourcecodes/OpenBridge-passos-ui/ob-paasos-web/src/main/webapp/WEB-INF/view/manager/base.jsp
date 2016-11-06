<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="manager">
	<template:replace name="content">
		<div class="left_sub_menu left_sub_menu_pas">
            <ul>
            <auth:validator value="functionValidator(functionId=paasos.tenant.manager)">
            	<li>
                	<a class="${ param['nav-left'] eq 'tenant' ? 'select' : '' }" href="${ WEB_APP_PATH }/manager/tenant/list.do">
                    <i class="left_menu_ico l_ico_zzgl"></i>组织管理</a>
                </li>
                </auth:validator>
                <auth:validator value="functionValidator(functionId=paasos.node.manager)">
                <li>
                	<a class="${ param['nav-left'] eq 'node' ? 'select' : '' }" href="${ WEB_APP_PATH }/manager/node/list.do">
                    <i class="left_menu_ico l_ico_jdgl"></i>节点管理</a>
                </li>
                </auth:validator>
                <auth:validator value="functionValidator(functionId=paasos.baseimage.manager)">
                <li>
                	<a class="${ param['nav-left'] eq 'baseimage' ? 'select' : '' }" href="${ WEB_APP_PATH }/manager/baseimage/list.do">
                    <i class="left_menu_ico l_ico_jcjx"></i>基础镜像</a>
                </li>
                </auth:validator>
                <auth:validator value="functionValidator(functionId=paasos.deploy.manager)">
                 <li>
                	<a class="${ param['nav-left'] eq 'deploy' ? 'select' : '' }" href="${ WEB_APP_PATH }/manager/deploy/list.do">
                    <i class="left_menu_ico l_ico_jcjx"></i>部署列表</a>
                </li> 
                <li>
                	<a class="${ param['nav-left'] eq 'envdeploy' ? 'select' : '' }" href="${ WEB_APP_PATH }/manager/deploy/env/list.do">
                    <i class="left_menu_ico l_ico_jcjx"></i>环境视图</a>
                </li> 
                </auth:validator>
            </ul>
        </div>
        <script type="text/javascript" src="${WEB_APP_PATH }/assets/js/image/build.js"></script>
        
		<template:block name="content-body">
			
		</template:block>
	</template:replace>
</template:include>