<%@page import="net.jeeshop.services.front.product.bean.Product"%>
<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@page import="net.jeeshop.core.ManageContainer"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<style>

.fdsfsdf{
	-moz-box-shadow:5px 5px 5px #ccc;              
    -webkit-box-shadow:5px 5px 5px #ccc;           
    box-shadow:5px 5px 5px #ccc;    
}
</style>
<%
String queryType = request.getParameter("queryType");
//out.println("queryType="+queryType);
String _indexProductList_title = null;
List<Product> _productList = SystemManager.activityProductMap.get(queryType);
if(_productList==null || _productList.size()==0){
	//out.println("_productList=0");
	request.setAttribute("_activity_indexProductList_productList", null);
}else{
	//out.println("_productList="+_productList.size());
	request.setAttribute("_activity_indexProductList_productList", _productList);
}


if(StringUtils.isBlank(queryType)){
	throw new NullPointerException("queryType is null");
}else if(queryType.equals("r")){
	_indexProductList_title = "降价促销";
}else if(queryType.equals("d")){
	_indexProductList_title = "打折促销";
}else if(queryType.equals("s")){
	_indexProductList_title = "买就赠送双倍积分";
}
request.setAttribute("queryType", queryType);
%>

<!-- 首页不同商品的展示 -->
<s:if test="#request._activity_indexProductList_productList!=null">

	<div class="row" class="col-xs-12" style="padding: 5px;">
		<div class="alert alert-success" style="margin-bottom: 5px;margin-top: 5px;display: none;">
			<s:if test="#request.queryType.equals(\"r\")">
				<span class="glyphicon glyphicon-arrow-down"></span>
			</s:if>
			<s:elseif test="#request.queryType.equals(\"d\")">
				<span class="glyphicon glyphicon-flash"></span>
			</s:elseif>
			<s:elseif test="#request.queryType.equals(\"s\")">
				<span class="glyphicon glyphicon-plus"></span>
			</s:elseif>
			&nbsp;<%=_indexProductList_title %>
<%-- 			<a href="<%=request.getContextPath() %>/special/<%=queryType %>.html" target="_blank"> --%>
<%-- 				<span style="float:right">[更多]</span> --%>
<!-- 			</a> -->
		</div>
		<div class="page-header fdsfsdf" style="border-bottom: 2px solid #e33a3d;margin: 20px 0 20px;">
		  <h5>
		  <s:if test="#request.queryType.equals(\"r\")">
				<span class="glyphicon glyphicon-arrow-down"></span>
			</s:if>
			<s:elseif test="#request.queryType.equals(\"d\")">
				<span class="glyphicon glyphicon-flash"></span>
			</s:elseif>
			<s:elseif test="#request.queryType.equals(\"s\")">
				<span class="glyphicon glyphicon-plus"></span>
			</s:elseif>
		  <strong><%=_indexProductList_title %></strong></h5>
<%-- 		  <h3><strong><small><%=_indexProductList_title %></small></strong></h3> --%>
		</div>

	</div>
<div class="row" style="border:0px solid red;">
		<s:iterator value="#request._activity_indexProductList_productList" status="i" var="row">
			<div class="col-xs-3" style="padding: 5px;text-align: center;">
				<div class="thumbnail" style="width: 100%; display: block;margin-bottom: 10px;">
					<div style="height: 200px;border: 0px solid;">
						<a href="<%=request.getContextPath() %>/product/<s:property escape="false" value="id" />.html" target="_blank">
							<img class="lazy" style="border: 0px;display: block;margin: auto;max-height: 100%;max-width: 100%;"  
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
							<s:if test="#request.queryType.equals(\"d\")">
								<span class="badge pull-left">折扣价
									<b style="font-weight: bold;">
										￥<s:property escape="false" value="finalPrice" />
									</b>
								</span>
							</s:if>
							<s:elseif test="#request.queryType.equals(\"r\")">
								<s:if test="expire">
									<span class="label label-default">促销价
										<b style="font-weight: bold;">
											￥<s:property escape="false" value="finalPrice" />
										</b>
									</span>
								</s:if>
								<s:else>
									<span class="label label-danger">促销价
										<b style="font-weight: bold;">
											￥<s:property escape="false" value="finalPrice" />
										</b>
									</span>
								</s:else>
							</s:elseif>
							<s:elseif test="#request.queryType.equals(\"s\")">
								<b style="font-weight: bold;">
									￥<s:property escape="false" value="nowPrice" />
								</b>
							</s:elseif>
							
						</div>
						<div class="col-xs-6" style="text-align: right;">
							<b style="text-decoration: line-through;font-weight: normal;font-size: 11px;color: #a5a5a5;">
								￥<s:property escape="false" value="price" />
							</b>
						</div>
					</div>
					
					<div class="row">
						<div class="col-xs-12">
							<s:if test="expire">
								活动已到期
							</s:if>
							<s:else>
							
								<s:if test="#request.queryType.equals(\"d\")">
									还剩<div style="display: inline;" timer="activityEndDateTime" activityEndDateTime="<s:property escape="false" value="activityEndDateTime" />"></div>
									<span class="badge pull-right" style="background-color:red;"><s:property escape="false" value="discountFormat" />折</span>
								</s:if>
								<s:elseif test="#request.queryType.equals(\"r\")">
									还剩<div style="display: inline;" timer="activityEndDateTime" activityEndDateTime="<s:property escape="false" value="activityEndDateTime" />"></div>
								</s:elseif>
								<s:elseif test="#request.queryType.equals(\"s\")">
									还剩<div style="display: inline;" timer="activityEndDateTime" activityEndDateTime="<s:property escape="false" value="activityEndDateTime" />"></div>
									<span class="label label-success">双倍积分</span>
								</s:elseif>
								
							</s:else>
						</div>
					</div>
					
				</div>
			</div>
		</s:iterator>
	</div>

</s:if>
<s:else>
<!-- 	此活动暂未发布商品！敬请期待！ -->
</s:else>
