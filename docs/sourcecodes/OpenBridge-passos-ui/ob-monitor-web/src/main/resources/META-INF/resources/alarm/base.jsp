<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="alarm">
	<template:replace name="content">
        <div class="left_sub_menu left_sub_menu_pas">
            <ul>
                <li><a class="${ param['nav-left'] eq 'group' ? 'select' : '' }" href="${ WEB_APP_PATH }/groups">
                    <i class="left_menu_ico l_ico_jk"></i>节点组</a></li>
                <li><a class="${ param['nav-left'] eq 'template' ? 'select' : '' }" href="${ WEB_APP_PATH }/templates">
                    <i class="left_menu_ico l_ico_jdgl"></i>策略</a></li>
                <li> <a class="${ param['nav-left'] eq 'team' ? 'select' : '' }" href="${ WEB_APP_PATH }/teams">
                    <i class="left_menu_ico l_ico_zjj"></i>用户组</a></li>
            </ul>
        </div>
		<template:block name="content-body">
			
		</template:block>
	</template:replace>
</template:include>