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
/*     border: 1px solid #ccc; */
}
</style>
<%
String queryType = request.getParameter("queryType");
//out.println("queryType="+queryType);
String _indexProductList_title = null;
List<Product> _productList = SystemManager.activityScoreProductList;
if(_productList==null || _productList.size()==0){
	//out.println("_productList=0");
	request.setAttribute("_activity_indexProductList_productList", null);
}else{
	//out.println("_productList="+_productList.size());
	request.setAttribute("_activity_indexProductList_productList", _productList);
}


if(StringUtils.isBlank(queryType)){
	throw new NullPointerException("queryType is null");
}else if(queryType.equals("j")){
	_indexProductList_title = "积分商城";
}
request.setAttribute("queryType", queryType);
%>

<!-- 首页不同商品的展示 -->
<s:if test="#request._activity_indexProductList_productList!=null">

	<div class="row" class="col-xs-12" style="padding: 5px;">
		<div class="alert alert-success" style="margin-bottom: 5px;margin-top: 5px;display: none;">
			<s:if test="#request.queryType.equals(\"j\")">
				<span class="glyphicon glyphicon-time"></span>
			</s:if>
			<%=_indexProductList_title %>
<%-- 			<a href="<%=request.getContextPath() %>/special/<%=queryType %>.html" target="_blank"> --%>
<%-- 				<span style="float:right">[更多]</span> --%>
<!-- 			</a> -->
		</div>
		
		<div class="page-header fdsfsdf" style="border-bottom: 2px solid #e33a3d;margin: 20px 0 20px;">
		  <h5>
		  <s:if test="#request.queryType.equals(\"j\")">
				<span class="glyphicon glyphicon-time"></span>
			</s:if>
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
							<s:if test="#request.queryType.equals(\"j\")">
								<s:if test="expire">
									<span class="label label-default">兑换积分:
										<b style="font-weight: bold;">
											<s:property escape="false" value="exchangeScore" />
										</b>
									</span>
								</s:if>
								<s:else>
									<span class="label label-danger">兑换积分:
										<b style="font-weight: bold;">
											<s:property escape="false" value="exchangeScore" />
										</b>
									</span>
								</s:else>
							</s:if>
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
							
								还剩<div style="display: inline;" timer="activityEndDateTime" activityEndDateTime="<s:property escape="false" value="activityEndDateTime" />"></div>
								
							</s:else>
						</div>
					</div>
					
				</div>
			</div>
		</s:iterator>
	</div>

</s:if>
