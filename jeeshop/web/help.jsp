<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@page import="net.jeeshop.core.ManageContainer"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="no-js">
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/resource/common_css.jsp"%>
<style type="text/css">
.centerImageCss{
	width: 560px;
	height: 180px;
}
</style>
</head>

<body>
<div id="wrap">
	<%@ include file="/indexMenu.jsp"%>
	<div class="container">
		
		<div class="row">
			<div class="col-xs-3">
				<%@ include file="/helpCatalog2.jsp"%>
			</div>
			<s:if test="helpCode.equals(\"index\")">
				<div class="col-xs-9">
					<div class="row">
						<strong>帮助中心首页</strong>
					</div>
					<div class="row"><hr></div>
					<div class="row">
						帮助中心首页内容
					</div>
				</div>
			</s:if>
			<s:else>
				<div class="col-xs-9">
					<!-- 导航写 -->
					<div class="row">
						<div class="col-xs-12">
							<ol class="breadcrumb">
							  <li><a href="<%=request.getContextPath() %>">首页</a></li>
							  <s:if test="helpCode.equals(\"index\")">
							      <li class="active">帮助中心</li>
							  </s:if>
							  <s:else>
							  	  <li><a href="<%=request.getContextPath() %>/help/index.html">帮助中心</a></li>
								  <li class="active"><s:property value="news.title" escapeHtml="false"/></li>
							  </s:else>
							</ol>
						</div>
					</div>
		
					<div class="row">
						<div class="col-xs-12">
							<strong><s:property value="news.title" escapeHtml="false"/></strong>
							<hr>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<%String newsInfoUrl = request.getAttribute("newsInfoUrl").toString();%>
							<jsp:include flush="true" page="<%=newsInfoUrl %>"></jsp:include>
						</div>
					</div>
				</div>
			</s:else>
		</div>
	</div>
</div>	
	<%@ include file="foot.jsp"%>
</body>
</html>
