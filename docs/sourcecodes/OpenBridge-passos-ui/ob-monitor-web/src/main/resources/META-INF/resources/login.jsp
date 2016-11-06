<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<% response.addHeader("login", "true"); %>
<template:include file="/common/template/simple.jsp">
	<template:replace name="title">
		系统登陆
	</template:replace>
	<template:replace name="body">
	
    <link rel="stylesheet" href="${WEB_APP_PATH }/assets/css/onepage-scroll.css"/>
		<script type="text/javascript" src="${ WEB_APP_PATH }/assets/js/jquery.onepage-scroll.min.js"></script>
		<script>
		$(function () {
		$('.main').onepage_scroll({
		sectionContainer: '.page',
		direction: 'horizontal'
		});
		});
		</script>

		<div class="warp">
		<div class="cloud-login">
		<div class="login-cont">
		<div class="login-img">
		<img src="${WEB_APP_PATH }/assets/images/login/paas-1.png">
		</div>
		<form id="form1" name="form1" method="post" action="${ WEB_APP_PATH }/login.do">
		<c:if test="${ not empty loginError }">
			<div style="color: red;height: 40px;text-align: center;">
			${ loginError }
			</div>
		</c:if>
		<div class="login-text">
		<input name="j_username" value="${param['j_username']}" type="text" placeholder="用户名" />
		<input name="j_password" type="password" class="form-control" placeholder="密码" />
		</div>
		<div class="login-password">
		</div>

		<input type="hidden" name="targetUrl" value="${ param['targetUrl'] }">
		<div class="login-btn">
		<button type="submit" class="login-button">登&nbsp;&nbsp;录</button>
		</div>
		</form>
		</div>
		</div>
		</div>
		<div class="main">
		<div class="page page1">
		</div>
		<div class="page page2">
		</div>
		</div>
	</template:replace>
</template:include>
