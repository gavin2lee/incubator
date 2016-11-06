<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/WEB-INF/view/project/base.jsp" nav-left="overview">
	<template:replace name="title">
		APP 概述
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
            <a href="mirrorRepo.html"><i class="icons go_back_ico"></i></a> 
            <p class="app_a">项目管理</p>
            <em>&gt;</em>
            <p class="app_a">项目x</p>
            <em>&gt;</em>
            <p class="app_a">概述</p>
        </div>
        
        <div class="plate">
            <div class="project_r">
            </div>
        </div>
	</template:replace>
</template:include>