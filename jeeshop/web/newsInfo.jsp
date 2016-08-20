<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@page import="net.jeeshop.core.FrontContainer"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="no-js">
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/resource/common_css.jsp"%>
</head>

<body>
	<%@ include file="/indexMenu.jsp"%>
	<div class="container">
		<div class="row">
			<div class="col-xs-9">
				<div class="row">
					<div class="col-xs-12">
						<ol class="breadcrumb">
						  <li><a href="<%=request.getContextPath() %>">首页</a></li>
						  <li><a href="<%=request.getContextPath() %>/news/list.html">公告新闻</a></li>
						  <li class="active"><s:property value="e.title" escapeHtml="false"/></li>
						</ol>
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
<!-- 						<blockquote> -->
						  	<h3><strong style="color:#dd4814;"><s:property value="e.title" escapeHtml="false"/></strong></h3>
						  	<div style="text-align: right;"><small>jeeshop 发布于:<s:property value="e.createtime"/></small></div>
<!-- 						</blockquote> -->
						<hr style="margin-top: 5px;">
					</div>
				</div>
				<div class="row">
					<div class="col-xs-12">
						<%String newsInfoUrl = request.getAttribute("newsInfoUrl").toString();%>
						<jsp:include flush="true" page="<%=newsInfoUrl %>"></jsp:include>
					</div>
				</div>
			</div>
			
			<!-- 右边公共部分 -->
			<div class="col-xs-3">
				<div class="row">
				<%@ include file="/index_notice_slide.jsp"%>
				</br>
				<%@ include file="productlist_left_picScroll.jsp"%>
				</div>
			</div>
		</div>
	</div>
	<%@ include file="foot.jsp"%>
	<%@ include file="index_superSlide_js.jsp"%>
</body>
</html>
