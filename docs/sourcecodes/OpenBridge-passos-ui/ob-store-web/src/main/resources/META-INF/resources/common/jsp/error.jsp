<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.Enumeration"%>
<%@ page import="java.lang.Exception"%>
<%@ page import="java.io.PrintWriter"%>
<%@ include file="/common/header.jsp"%>
<%@ include file="/common/include/head.jsp"%>
<style type="text/css">
body{
	overflow:auto!important;
}
</style>
<%-- <template:include file="/common/template/body.jsp"> --%>
<%-- 	<template:replace name="title"> --%>
		<title>出错了 </title>
<%-- 	</template:replace> --%>
<%-- 	<template:replace name="page-content"> --%>
	<style>
	h4{
		line-height:20px;
		border-bottom: 1px solid gray;
	}
	
	.error_cls {
/* 		width: 50px; */
/* 	    height: 50px; */
	    background-color: #ccc;
	    display: block;
/* 	    position: absolute; */
	    right: 0px;
	    top: 0px;
	    cursor: pointer;
	    padding: 0 5px;
	    float:right;
/* 	    line-height:20px; */
	}
	</style>
	<%
		Exception ex = (Exception) request.getAttribute("exception");
		if(ex != null){
	%>
	<H4>抱歉出现了错误了:<% out.print(ex.getMessage()); %>
<!-- 	<hr> -->
	<span class="error_cls" onclick="document.getElementById('errorMsg').style.display == 'none' ? document.getElementById('errorMsg').style.display = '' : document.getElementById('errorMsg').style.display = 'none'">详细</span>
	</H4>
	<pre id="errorMsg" style="display: none;">
	<%
			ex.printStackTrace(new PrintWriter(out));
		}
	%>
	</pre>
<%-- 	</template:replace> --%>
<%-- </template:include> --%>