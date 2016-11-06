<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/main.jsp" nav-header="resource">
	<template:replace name="content">
		<div class="left_sub_menu left_sub_menu_pas" style="height: 244px;">
            <ul>
            	<li>
                	<a class="${ param['nav-left'] eq 'mysql' ? 'select' : '' }" href="${ WEB_APP_PATH }/resource/mysql/index.do">
                    <i class="left_menu_ico l_ico_sjk"></i>数据库</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'messageQueue' ? 'select' : '' }" href="${ WEB_APP_PATH }/resource/messagequeue/index.do">
                    <i class="left_menu_ico l_ico_zjj"></i>消息中间件</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'redis' ? 'select' : '' }" href="${ WEB_APP_PATH }/resource/redis/index.do">
                    <i class="left_menu_ico l_ico_hc"></i>高速缓存</a>
                </li>
                <li>
                	<a class="${ param['nav-left'] eq 'nas' ? 'select' : '' }" href="${ WEB_APP_PATH }/resource/nas/nfs/index.do">
                    <i class="left_menu_ico l_ico_cc"></i>数据存储</a>
                </li>
            </ul>
        </div>
        <div class="app_name">
            <a href="javascript:void(0);"><i class="icons go_back_ico"></i></a>
            <p class="app_a">资源服务</p>
            <template:block name="content-path">
			
			</template:block>
        </div>
		<template:block name="content-body">
		
		</template:block>
	</template:replace>
</template:include>