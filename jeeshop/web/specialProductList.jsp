<%@page import="net.jeeshop.services.front.attribute.bean.Attribute"%>
<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@page import="net.jeeshop.services.front.product.bean.Product"%>
<%@page import="net.jeeshop.services.front.product.ProductService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.core.FrontContainer"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="no-js">
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/resource/common_css.jsp"%>
<style type="text/css">
img{border: 0px;}

.thumbnail_css{
	border-color: red;
}
.attr_css{
	font-size: 100%;
	float: left;
}
.left_product{
	font-size: 12px;max-height: 35px;overflow: hidden;text-overflow: ellipsis;-o-text-overflow: ellipsis;
}
.lazy {
  display: none;
}
</style>
<script type="text/javascript">
function defaultProductImg(){ 
	var img=event.srcElement; 
	img.src="<%=SystemManager.systemSetting.getDefaultProductImg() %>"; 
	img.onerror=null; //控制不要一直跳动 
}
</script>
</head>

<body>
	<%@ include file="/indexMenu.jsp"%>

	<div class="container">
	
		<div class="row">
			
			<div class="col-xs-3" style="margin-top: -15px;">
					<%@ include file="/catalog_superMenu.jsp"%>
					</br>
					<%@ include file="productlist_left_picScroll.jsp"%>
			</div>
			
			<div class="col-xs-9">
				<!-- 导航栏 -->
				<div class="row">
					<%
						Map<String,String> map = new HashMap<String,String>();
						map.put("hot", "热门");
						map.put("sale", "特价");
						map.put("newest", "新品");
						application.setAttribute("map", map);
					%>
					<div>
						<span style="margin:5px;font-weight: bold;">类型：</span>
						<s:iterator value="#application.map" status="i" var="row">
							<s:if test="key==special">
								<span class="label label-success" style="margin-right:5px;font-size:100%;">
									<a href="<%=request.getContextPath() %>/special/<s:property escape="false" value="key"/>.html"><s:property escape="false" value="value" /></a>
								</span>
							</s:if>
							<s:else>
								<span class="label" style="margin-right:5px;font-size:100%;">
									<a href="<%=request.getContextPath() %>/special/<s:property escape="false" value="key"/>.html"><s:property escape="false" value="value" /></a>
								</span>
							</s:else>
						</s:iterator>
					</div>
					<hr>
				</div>
				
				<div class="row">
					<s:iterator value="productList" status="i" var="item">
						<div class="col-xs-3" style="padding: 5px;text-align: center;">
							<div class="thumbnail" style="width: 100%; display: block;">
								<div style="height: 150px;border: 0px solid;">
									<a href="<%=request.getContextPath() %>/product/<s:property escape="false" value="id" />.html" target="_blank">
										
										<img class="lazy" style="border: 0px;display: block;margin: auto;max-height: 100%;max-width: 100;"  
										border="0" src="<%=SystemManager.systemSetting.getDefaultProductImg()%>" 
										data-original="<%=SystemManager.systemSetting.getImageRootPath()%><s:property escape="false" value="picture" />">
										
									</a>
								</div>
								<div style="height: 40px;">
									<div class="col-xs-12 left_product">
										<div class="row">
											<a style="cursor: pointer;" href="<%=request.getContextPath() %>/product/<s:property escape="false" value="id" />.html" target="_blank" 
											title="<s:property escape="false" value="name" />">
												<s:property escape="false" value="name" />
											</a>
										</div>
									</div>
								</div>
								<div class="row">
									<div class="col-xs-6">
										<b style="font-weight: bold;color: #cc0000;">
											￥<s:property escape="false" value="nowPrice" />
										</b>
									</div>
									<div class="col-xs-6">
										<b style="text-decoration: line-through;font-weight: normal;font-size: 11px;color: #a5a5a5;">
											￥<s:property escape="false" value="price" />
										</b>
									</div>
								</div>
								<div class="row" style="display: none;">
									<div class="col-xs-12">
										<s:if test="isnew==1">
											<div>1天前上架</div>
										</s:if>
										<s:else>
											<div>活动已结束!</div>
										</s:else>
										<s:if test="sale==1">
											<div>还剩余12小时结束</div>
										</s:if>
										<s:else>
											<div>活动已结束!</div>
										</s:else>
									</div>
								</div>
							</div>
						</div>
					</s:iterator>
					
					<s:if test="productList==null or productList.size==0">
					没有找到<font color='red'><%=request.getAttribute("key")!=null?request.getAttribute("key").toString():"" %></font>相关的宝贝!
					<%request.setAttribute("key",null); %>
					</s:if>
				</div>
				<br style="clear: both;">
				<div style="text-align: right;">
					<s:if test="!(productList==null or productList.size==0)">
						<%@ include file="pager.jsp"%>
					</s:if>
				</div>
		
			</div>
		</div>
	</div>
	
<%@ include file="foot.jsp"%>
<%@ include file="index_superSlide_js.jsp"%>
<script type="text/javascript">
$(function() {
	//商品鼠标移动效果
	$("div[class=thumbnail]").hover(function() {
		$(this).addClass("thumbnail_css");
	}, function() {
		$(this).removeClass("thumbnail_css");
	});
});
</script>
</body>
</html>
