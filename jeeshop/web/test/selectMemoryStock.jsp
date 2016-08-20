<%@page import="net.jeeshop.services.front.product.bean.ProductStockInfo"%>
<%@page import="java.util.concurrent.ConcurrentMap"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
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

<body style="text-align: center;">
	<table class="table table-bordered" style="width: 90%;margin: auto;">
		<tr>
			<td>商品ID</td>
			<td>商品库存数</td>
			<td>库存是否有所改变</td>
		</tr>
		<%
		ConcurrentMap<String, ProductStockInfo> productStockMap = SystemManager.productStockMap;
		request.setAttribute("productStockMap", productStockMap);
		%>
		<tr>
			<td colspan="3">
				商品数：<s:property value="#request.productStockMap.size"/>
			</td>
		</tr>
		
		<s:if test="#request.productStockMap!=null">
			<s:iterator value="#request.productStockMap" status="i" var="row">
				<tr>
					<td><s:property value="value.id"/></td>
					<td><s:property value="value.stock"/></td>
					<td><s:property value="value.changeStock"/></td>
				</tr>
			</s:iterator>
		</s:if>
		<s:else>
		</s:else>
	</table>
</body>
</html>
