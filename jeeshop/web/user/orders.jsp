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
.simpleOrderReport{
font-weight: 700;font-size: 16px;color: #f50;
}
</style>
</head>

<body>
	<%@ include file="/indexMenu.jsp"%>
	<div class="container">
		<div class="row">
			<div class="col-xs-3">
				<%@ include file="userLeft.jsp"%>
			</div>
			
			<div class="col-xs-9">
				<div class="row">
					<s:if test="pager.list!=null and pager.pagerSize>=1">
						<div class="panel panel-default">
							<div class="panel-heading"><span class="glyphicon glyphicon-th"></span>我的订单列表</div>
							<div class="panel-body">
								<s:if test="orderSimpleReport!=null">
									<s:if test="orderSimpleReport.orderCompleteCount!=0">
										<span class="glyphicon glyphicon-ok"></span>&nbsp;<span class="simpleOrderReport"><s:property value="orderSimpleReport.orderCompleteCount"/></span>个交易完成.
									</s:if>
									<s:if test="orderSimpleReport.orderCancelCount!=0">
										<span class="glyphicon glyphicon-remove"></span>&nbsp;<span class="simpleOrderReport"><s:property value="orderSimpleReport.orderCancelCount"/></span>个取消.
									</s:if>
									<s:if test="orderSimpleReport.orderWaitPayCount!=0">
										<span class="glyphicon glyphicon-time"></span>&nbsp;<span class="simpleOrderReport"><s:property value="orderSimpleReport.orderWaitPayCount"/></span>个等待付款.
									</s:if>
								</s:if>
								<s:else>
									无任何订单数据！
								</s:else>
							</div>
							<table class="table">
<!-- 							<table class="table table-bordered"> -->
								<tr>
									<td colspan="21"><span class="text-primary">【确认收货】操作只能登陆支付宝来进行。</span></td>
								</tr>
								<tr>
									<th style="text-align: left;">
	<!-- 									<input type="checkbox" id="firstCheckbox" />&nbsp;&nbsp; -->
										商品</th>
									<th style="text-align: center;" nowrap="nowrap">数量</th>
									<th style="text-align: center;" nowrap="nowrap">单价</th>
									<th style="text-align: center;" nowrap="nowrap">订单状态</th>
									<th style="text-align: center;width: 100px;">操作</th>
								</tr>
								
								<s:iterator value="pager.list">
									<tr class="warning">
										<td colspan="11">
											<div class="row">
												<div class="col-xs-3">
	<%-- 												<input type="checkbox" name="ids" value="<s:property value="id"/>" /> --%>
													订单号:<s:property value="id" escape="false"/>
												</div>
												<div class="col-xs-3">
													成交时间:<s:property value="createdate" escape="false"/>
												</div>
												<div class="col-xs-3">
													合计:<b class="simpleOrderReport"><s:property value="amount" escape="false"/></b>
												</div>
												<div class="col-xs-3">
													<s:if test="score!=0">
														<span class="label label-default">获赠：<s:property value="score" escape="false"/>个积分</span>
													</s:if>
												</div>
											</div>
										</td>
									</tr>
									<s:iterator value="orders" status="i">
										<tr>
											<td>&nbsp;
												<div style="width:50px;height: 50px;border: 0px solid;float: left;margin-left: 20px;">
													<a href="<%=request.getContextPath() %>/product/<s:property value="productID" />.html" target="_blank" title="<s:property value="productName" />">
														<img style="width: 100%;height: 100%;border: 0px;" alt="" src="<%=SystemManager.systemSetting.getImageRootPath()%><s:property value="picture" />" onerror="nofind()"/>
													</a>
												</div>
												<div style="float: left;">&nbsp;<s:property value="productName" /></div>
											</td>
											<td style="text-align: center;">&nbsp;<s:property value="productNumber" /></td>
											<td style="text-align: center;">&nbsp;<s:property value="price" /></td>
											<s:if test="#i.index==0">
												<td style="text-align: center;border-left:1px solid #ddd;vertical-align: middle;" rowspan="<s:property value="quantity" />">
													
													<s:if test="paystatus.equals(\"y\")">
														<s:if test="status.equals(\"init\")">
															等待发货
														</s:if>
														<s:elseif test="status.equals(\"pass\")">
															等待发货
														</s:elseif>
														<s:elseif test="status.equals(\"send\")">
															已发货
														</s:elseif>
														<s:elseif test="status.equals(\"sign\")">
															已签收
														</s:elseif>
														<s:else>
															交易完成
														</s:else>
													</s:if>
													<s:elseif test="status.equals(\"cancel\")">
														已取消
													</s:elseif>
													<s:else>
														等待付款
													</s:else>
													<br>
													
													<s:if test="status.equals(\"file\")">
														<s:if test="isComment.equals(\"y\")">
															已评价<br>
														</s:if>
														<s:else>
														</s:else>
													</s:if>
												</td>
											</s:if>
											
											
											<s:if test="#i.index==0">
												<td style="text-align: center;border-left:1px solid #ddd;vertical-align: middle;" rowspan="<s:property value="quantity" />">
													
													<s:if test="paystatus.equals(\"y\")">
														<s:if test="status.equals(\"init\")">
															<!-- 等待发货 -->
														</s:if>
														<s:elseif test="status.equals(\"pass\")">
															<!-- 等待发货 -->
														</s:elseif>
														<s:elseif test="status.equals(\"send\")">
															<a target="_blank" href="http://www.alipay.com" class="btn btn-primary btn-sm">确认收货</a>
														</s:elseif>
														<s:elseif test="status.equals(\"sign\")">
															<!-- 已签收 -->
														</s:elseif>
														<s:else>
															<!-- 交易完成 -->
														</s:else>
													</s:if>
													<s:elseif test="status.equals(\"cancel\")">
														<!-- 已取消 -->
													</s:elseif>
													<s:else>
														<a target="_blank" href="<%=request.getContextPath()%>/order/toPay.html?id=<s:property value="id"/>" class="btn btn-success btn-sm">确认付款</a>
													</s:else>
													<br>
													
													<s:if test="status.equals(\"sign\") or status.equals(\"file\")">
														<s:if test="closedComment.equals(\"y\")">
															<!-- 已评价 -->
														</s:if>
														<s:else>
															<a target="_blank" href="<%=request.getContextPath()%>/order/rate.html?orderid=<s:property value="id"/>" class="btn btn-danger btn-sm">我来评价</a><br>
														</s:else>
													</s:if>
													<a target="_blank" href="<%=request.getContextPath()%>/order/orderInfo.html?id=<s:property value="id" />">订单详情</a>
													<br>
													
													<s:if test="status.equals(\"send\") or status.equals(\"sign\")">
														<a target="_blank" href="http://www.kuaidi100.com/chaxun?com=<s:property value="e.expressCompanyName" />&nu=<s:property value="e.expressNo" />">快递物流</a>
													</s:if>
													
												</td>
											</s:if>
										</tr>
									</s:iterator>
								</s:iterator>
							</table>
						</div>
						
						<div style="text-align: right;"><%@ include file="/pager.jsp"%></div>
					</s:if>
					<s:else>
						
						<div class="col-xs-12">
							<div class="row">
								<div class="col-xs-12">
									<ol class="breadcrumb">
									  <li class="active">我的订单</li>
									</ol>
								</div>
							</div>
							
							<hr>
							
							<div class="row">
								<div class="col-xs-12" style="font-size: 14px;font-weight: normal;">
									<div class="panel panel-default">
							              <div class="panel-body" style="font-size: 16px;font-weight: normal;text-align: center;">
								              <div class="panel-body" style="font-size: 16px;font-weight: normal;text-align: center;">
								              		<span class="glyphicon glyphicon-user"></span>亲，你还没有任何订单信息！赶紧去下个单吧。
								              </div>
							              </div>
									</div>
									<hr>
								</div>
							</div>
							
						</div>
			
						<!--  
							<div class="bs-callout bs-callout-danger author" 
							style="text-align: left;font-size: 34px;margin: 2px 0px;vertical-align: middle;
							margin: auto;margin-top:50px;">
								<span class="glyphicon glyphicon-exclamation-sign" style="font-size: 34px;"></span>
								<span style="font-size: 16px;">还没有任何订单信息！赶紧去下个单吧。</span>
							</div>-->
					</s:else>
				</div>
			</div>
		</div>
	</div>
	
<%@ include file="/foot.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<script type="text/javascript">
$(function() {
});
</script>
</body>
</html>
