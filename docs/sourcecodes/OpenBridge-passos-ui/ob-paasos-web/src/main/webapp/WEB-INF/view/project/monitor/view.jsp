<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="${empty param.source? '../base.jsp' : '../../manager/base.jsp'}"  nav-left="${empty param.source? 'monitor' : 'node'}">
	<template:replace name="title">
		${empty param.source? '容器资源' : '节点资源'}
	</template:replace>
	<template:replace name="content-body">
		<style>
			.sl_block {
			    width: 49%;
			    float: left;
			    margin: .5% .5% 15px;
			}
			
			.sl_content {
			    overflow: hidden;
			    height: 300px;
			    background: #fff;
			}
		</style>
	     <div class="app_name">
            <a href="${WEB_APP_PATH }"><i class="icons go_back_ico"></i></a>
			
            
			
			<c:if test="${empty  param.source}">
				<p class="app_a"><a href="${WEB_APP_PATH }/project/overview/index.do?projectId=${param.projectId }">项目管理</a></p>
				<em>&gt;</em>
	            <p class="app_a">
	            	<a href="${WEB_APP_PATH }/project/overview/index.do?projectId=${param.projectId }">${project.projectName }&nbsp;</a>
	            </p>
	            <em>&gt;</em>
				<p class="app_a"><a href="${WEB_APP_PATH }/project/monitor/index.do?projectId=${param.projectId }">监控</a></p>
			</c:if>
			<c:if test="${not empty  param.source}">
				<p class="app_a"><a href="${WEB_APP_PATH }/manager/tenant/list.do">环境管理</a></p>
				<em>&gt;</em>
				<p class="app_a"><a href="${WEB_APP_PATH }/manager/node/list.do">节点管理</a></p>
			</c:if>
            <em>&gt;</em>
			<p class="app_a"><a href="javascript:void(0);">监控信息</a></p>
        </div>
        
        <c:import url="viewinfo.jsp"></c:import>
	</template:replace>
</template:include>