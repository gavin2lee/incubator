<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@page import="net.jeeshop.services.front.product.bean.Product"%>
<%@page import="net.jeeshop.services.front.product.ProductService"%>
<%@page
	import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@page import="net.jeeshop.core.ManageContainer"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!-- 浏览过的商品历史列表，仅限于当前session中存储 -->
<div class="row" >
	<h4 class="topCss">浏览过的商品</h4>
</div>

<s:iterator value="#session.history_product_map" status="i" var="item">
	<div class="row">
		<div class="col-xs-3">
			<a href="<%=request.getContextPath() %>/product/<s:property escape="false" value="value.id" />.html" target="_blank" title="<s:property escape="false" value="value.name" />">
				<img class="lazy" style="border: 0px;display: block;margin: auto;width: 50px;height: 50px;" 
									src="<%=SystemManager.systemSetting.getDefaultProductImg()%>" 
									data-original="<%=SystemManager.systemSetting.getImageRootPath() %><s:property escape="false" value="value.picture" />" />
			</a>
		</div>
		<div class="col-xs-9">
			<h4>
				<div class="left_product">
					<a title="<s:property escape="false" value="value.name" />" href="<%=request.getContextPath() %>/product/<s:property escape="false" value="value.id" />.html" target="_blank">
						<s:property escape="false" value="value.name" />
					</a>
				</div>
			</h4>
			<div class="row">
				<div class="col-xs-6">
					<b style="font-weight: bold;color: #cc0000;">
						￥<s:property escape="false" value="value.nowPrice" />
					</b>
				</div>
				<div class="col-xs-6">
					<b style="text-decoration: line-through;font-weight: normal;font-size: 11px;color: #a5a5a5;">
						￥<s:property escape="false" value="value.price" />
					</b>
				</div>
			</div>
		</div>
	</div>
	<br>
</s:iterator>
