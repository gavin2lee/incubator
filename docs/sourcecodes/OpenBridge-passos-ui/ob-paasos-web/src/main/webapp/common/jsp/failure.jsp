<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/body.jsp">
	<template:replace name="title">
		操作失败
	</template:replace>
	<template:replace name="page-content">
 
	操作失败

	<a href="history.back();">返回</a>
		 
	</template:replace>
</template:include>