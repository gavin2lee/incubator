<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@page import="net.jeeshop.services.front.catalog.bean.Catalog"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="java.util.LinkedList"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.Collections"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@page import="java.util.List"%>
<%@page import="net.jeeshop.core.ManageContainer"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<link rel="stylesheet"
	href="<%=request.getContextPath()%>/resource/js/superMenu/css/css.css"
	type="text/css">

<div id="sidebar">
	<s:iterator value="#application.catalogs" status="i" var="superType">
       <div class="sidelist">
			<span>
				<h3>
					<a href="<%=request.getContextPath() %>/catalog/<s:property escape="false" value="code" />.html"><s:property escape="false" value="name" /></a>
				</h3>
			</span>
			<div class="i-list">
				<ul>
					<s:iterator value="children" status="i2" var="smallType">
						<li>
		          			<a href="<%=request.getContextPath() %>/catalog/<s:property escape="false" value="code" />.html"><s:property escape="false" value="name" /></a>
						</li>
		           </s:iterator>
				</ul>
				<div style="clear: both;"></div>
				
				<s:if test="superMenuProducts!=null">
					<div style="border-top: 1px solid #f40;clear: both;" class="hotText">
						<div style="font-weight: bold;padding-top: 5px;padding-bottom: 5px;">推荐热卖：</div>
						
						<s:iterator value="superMenuProducts" status="ii" var="product">
							<div style="margin-top: 5px;">
								&gt;<a title="<s:property escape="false" value="name" />" target="_blank" href="<%=SystemManager.systemSetting.getWww() %>/product/<s:property escape="false" value="id" />.html">
									<s:property escape="false" value="name" />
								</a>
							</div>
						</s:iterator>
					</div>
				</s:if>
			</div>
		</div>
	
  	</s:iterator>
</div>