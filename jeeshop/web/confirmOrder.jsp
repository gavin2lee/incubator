<%@page import="net.jeeshop.core.util.TokenUtil"%>
<%@page import="net.jeeshop.core.front.SystemManager"%>
<%@page import="net.jeeshop.services.front.product.bean.Product"%>
<%@page import="net.jeeshop.services.front.product.ProductService"%>
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
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

<!DOCTYPE html>
<html class="no-js">
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/resource/common_css.jsp"%>
<style type="text/css">
.totalPayMonery{
	color: red;font-weight: bold;font-size:22px;
}
</style>
</head>

<body>
<div id="wrap">
	<%@ include file="indexMenu.jsp"%>
	<s:form action="/order/pay.html" namespace="/" method="post" theme="simple" onsubmit="return submitOrder();">
		<div class="container">
			<div class="row">
				<div class="col-xs-12" style="font-size: 14px;font-weight: normal;">
					<span class="label label-default" style="font-size:100%;">
						1.查看购物车
					</span>
					&nbsp;<span class="glyphicon glyphicon-circle-arrow-right"></span>
					<span class="label label-success" style="font-size:100%;">
						2.确认订单信息
					</span>
					&nbsp;<span class="glyphicon glyphicon-circle-arrow-right"></span>
					<span class="label label-default" style="font-size:100%;">
						3.确认收货
					</span>
					&nbsp;<span class="glyphicon glyphicon-circle-arrow-right"></span>
					<span class="label label-default" style="font-size:100%;">
						4.评价
					</span>
				</div>
			</div>
			<hr style="margin-bottom: 5px;">
			
			<div class="panel panel-primary">
				<div class="panel-heading">订单确认</div>
				
				<ul class="list-group">
					<li class="list-group-item">
						<a id="addressTips" href="#" data-toggle="tooltip" title="请选择收货地址！">
							<span class="glyphicon glyphicon-user"></span>&nbsp;请选择收货地址
						</a>
					</li>
					<li class="list-group-item">
						<s:if test="#session.myCart!=null and #session.myCart.productList.size!=0">
						<s:if test="#session.myCart!=null and #session.myCart.addressList.size!=0">
							<div class="row">
								<div class="col-xs-12" style="line-height: 20px;" id="adressListDiv">
<%-- 									defaultAddessID=<s:property escape="false" value="#session.myCart.defaultAddessID"/> --%>
									<!-- 
									<div class="alert alert-danger fade in">
								        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
								        <strong>提示：</strong>请选择收货地址!
								      </div>
									 -->
									<s:iterator value="#session.myCart.addressList">
										<s:if test="id.equals(#session.myCart.defaultAddessID)">
											<div address="address" class="col-xs-3 alert alert-info" style="border: 1px solid;text-align: left;margin-right: 10px;width: 200px;line-height: 20px;cursor: pointer;">
<%-- 												id:<s:property value="id" escape="false"/><br> --%>
												<s:property value="name" escape="false"/>,<s:property escape="false" value="phone"/>
												<input type="radio" name="e.selectAddressID" checked="checked"  value="<s:property escape="false" value="id"/>"/>
												<br>
												<s:property escape="false" value="address"/><br>
											</div>
										</s:if>
										<s:else>
											<div address="address" class="col-xs-3 alert" style="border: 1px solid;text-align: left;margin-right: 10px;width: 200px;line-height: 20px;cursor: pointer;">
<%-- 												id:<s:property value="id" escape="false"/><br> --%>
												<s:property escape="false" value="name"/>,<s:property value="phone" escape="false"/>
												<input type="radio" name="e.selectAddressID" value="<s:property escape="false" value="id"/>"/>
												<br>
												<s:property value="address" escape="false"/><br>
											</div>
										</s:else>
									</s:iterator>
								</div>
							</div>
						</s:if>
						<s:else>
			<%-- 				<div class="alert alert-danger">暂时还没有收获地址！<a href="<%=request.getContextPath() %>/user/address.html">设置</a></div> --%>
							<s:if test="#session.user_info!=null">
								<div class="bs-callout bs-callout-danger author" style="text-align: left;font-size: 14px;margin: 2px 0px;">
									暂时还没有收获地址！<a style="text-decoration: underline;" href="<%=request.getContextPath() %>/user/address.html">点此设置</a>
								</div>
							</s:if>
						</s:else>
					</s:if>
					</li>
					<li class="list-group-item">
						<a href="#" data-toggle="tooltip" title="请选择配送方式！" id="expressTips">
							<span class="glyphicon glyphicon-send"></span>&nbsp;请选择配送方式
						</a>
					</li>
					<li class="list-group-item">
						<div class="row">
							<div class="col-xs-12">
								<!-- 
								<div class="alert alert-danger fade in">
							        <button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>
							        <strong>提示：</strong>请选择配送方式!
							      </div>
								 -->
								<%application.setAttribute("expressMap", SystemManager.expressMap); %>
								<table class="table table-bordered table-hover" id="expressTable">
									<s:iterator value="#application.expressMap">
										<tr style="cursor: pointer;">
											<td width="400px">
											<input type="radio" name="e.expressCode" value="<s:property escape="false" value="key" />" fee="<s:property escape="false" value="value.fee" />"/>
											<s:property escape="false" value="value.name" /></td>
											<td><s:property escape="false" value="value.fee" /></td>
										</tr>
									</s:iterator>
								</table>
							</div>
						</div>
					</li>
					<li class="list-group-item"><span class="glyphicon glyphicon-shopping-cart"></span>&nbsp;购买到的商品</li>
					<li class="list-group-item">
						<div class="row">
							<div class="col-xs-12">
								<table class="table table-bordered table-hover">
									<tr>
										<th width="400px">商品名称</th>
										<th >单价(元)</th>
										<th >数量</th>
			<!-- 							<th >优惠方式(元)</th> -->
										<th >小计(元)</th>
									</tr>
									<s:iterator value="#session.myCart.productList">
										<tr>
											<td style="display: none;">&nbsp;<s:property escape="false" value="id" /></td>
											<td>&nbsp;<a target="_blank" href="<%=request.getContextPath() %>/product/<s:property escape="false" value="id" />.html"><s:property escape="false" value="name" /></a>
												<a name="stockErrorTips" productid="<s:property escape="false" value="id" />" href="#" data-toggle="tooltip" title="" data-placement="right" data-original-title="商品库存不足20个，"></a>
												<s:if test="exchangeScore!=0">
													<p>
														<span id="totalExchangeScoreSpan" class="label label-default">兑换积分:<s:property escape="false" value="exchangeScore" />
														</span>
													</p>
												</s:if>
											</td>
											<td>
												<s:if test="totalExchangeScore!=0">
													<span style="text-decoration: line-through;">
												</s:if>
												<s:else>
													<span>
												</s:else>
													<s:property escape="false" value="nowPrice" />
												</span>
											</td>
											<td>
												<s:property escape="false" value="buyCount" />												
											</td>
											<td>&nbsp;<s:property escape="false" value="total0" /></td>
										</tr>
									</s:iterator>
								</table>
							</div>
						</div>
					</li>
				</ul>
				
				<div class="panel-footer primary" align="right">
					<div class="row">
						<div class="col-xs-12">
							<input id="productTotalMonery" type="hidden" value="<s:property escape="false" value="#session.myCart.amount"/>"/>		
							合计：<span class="totalPayMonery" id="totalPayMonery"><s:property escape="false" value="#session.myCart.amount"/></span>
						</div>
					</div>
					<div class="row">
						<div class="col-xs-12">
							<s:if test="#session.myCart.totalExchangeScore!=0">
								<h4>所需积分：<span style="color: red;font-weight: bold;" id="totalExchangeScore"><s:property escape="false" value="#session.myCart.totalExchangeScore"/></span>
								</h4>
							</s:if>
						</div>
					</div>
							
					<div class="row">
						<div class="col-xs-6">
							<input name="e.otherRequirement" class="form-control" placeholder="此处您可以输入您的附加要求，以便我们提供更好的服务。" size="50" maxlength="50"/>	
						</div>
						<div class="col-xs-6">
							<button type="submit" class="btn btn-success" value="提交订单" id="confirmOrderBtn" disabled="disabled">
								<span class="glyphicon glyphicon-ok"></span>提交订单
							</button>
						</div>
					</div>
				</div>
			</div>			
			
		</div>
	</s:form>
</div>
<%@ include file="foot.jsp"%>

<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jquery-1.4.2.min.js"></script> --%>
<script type="text/javascript">
$(function() {
	$("div[address=address]").click(function(){
		$("div[address=address]").removeClass("alert-info");
		
		$(this).addClass("alert-info");
		$(this).find("input[type=radio]").attr("checked",true);
	});
	
	$("#expressTable tr").each(function(){
		var _tr = $(this);
		_tr.click(function(){
			var _radio = _tr.find("input[type=radio]");
			console.log("选中的快递费用为="+_radio.attr("fee"));
			_radio.attr("checked",true);
			var _totalPayMonery = parseFloat($("#productTotalMonery").val())+parseFloat(_radio.attr("fee"));
			$("#totalPayMonery").text(_totalPayMonery.toFixed(2));
			
			$('#expressTips').tooltip('hide');
		});
	});
	
	$("#confirmOrderBtn").removeAttr("disabled");
});

function submitOrder(){
	console.log("提交订单...");
	//console.log($("#adressListDiv").find(":checked").size());
	//console.log($("#expressTable").find(":checked").size());
	var submitFlg = true;
	if($("#adressListDiv").find(":checked").size()==0){
		$('#addressTips').tooltip('show');
		submitFlg =false;
	}else{
		$('#addressTips').tooltip('hide');
	}
	if($("#expressTable").find(":checked").size()==0){
		$('#expressTips').tooltip('show');
		submitFlg =false;
	}else{
		$('#expressTips').tooltip('hide');
	}
	console.log("提交订单...submitFlg= " + submitFlg);
	if(!submitFlg){
		return false; 
	}
	//ajax验证待提交支付的商品库存数量是否存在超卖或下架之类的情况
	//tips
	//$("a[name=stockErrorTips]").tooltip('show');
	var aaa=checkStockLastTime();
	console.log("aaa="+aaa);
	if(!aaa){console.log("not ok");
		return false;
	}
	console.log("ok");
	return true;
}

</script>
</body>
</html>
