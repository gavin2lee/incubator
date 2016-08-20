<%@page import="net.jeeshop.services.manage.order.bean.Order"%>
<%@page import="net.jeeshop.core.KeyValueHelper"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page session="false"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jquery-jquery-ui/themes/base/jquery.ui.all.css">
<style>
.simpleOrderReport{
	font-weight: 700;font-size: 16px;color: #f50;
}
</style>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
</head>
<body>
<s:form action="order" namespace="/manage" method="post" theme="simple" name="form1">
	<s:hidden name="e.id"/>
<%-- 	<s:hidden name="e.type"/> --%>
<%-- 	<s:if test="e.type.equals('update')"> --%>
		<div id="buttons" style="text-align: center;border-bottom: 1px solid #ccc;padding: 5px;">
		<div id="updateMsg"><font color='red'><s:property value="updateMsg" /></font></div>
			
			<s:if test="e.paystatus.equals(\"y\")">
				<s:if test="e.status.equals(\"init\")">
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
						value="已审核" class="btn btn-primary"><%=Order.order_status_pass_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
						value="已发货" class="btn" disabled="disabled"><%=Order.order_status_send_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
						value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
						value="已归档" class="btn" disabled="disabled"><%=Order.order_status_file_chinese %></a>
					
					<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning"/>
					<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning"/>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
				</s:if>
				<s:elseif test="e.status.equals(\"pass\")">
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
						value="已审核" class="btn" disabled="disabled"><%=Order.order_status_pass_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
						value="已发货" class="btn btn-primary"><%=Order.order_status_send_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
						value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
						value="已归档" class="btn" disabled="disabled"><%=Order.order_status_file_chinese %></a>
					
					<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning" disabled="disabled"/>
					<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning" disabled="disabled"/>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
				</s:elseif>
				<s:elseif test="e.status.equals(\"send\")">
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
						value="已审核" class="btn" disabled="disabled"><%=Order.order_status_pass_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
						value="已发货" class="btn" disabled="disabled"><%=Order.order_status_send_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
						value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
						value="已归档" class="btn" disabled="disabled"><%=Order.order_status_file_chinese %></a>
					
					<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning" disabled="disabled"/>
					<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning" disabled="disabled"/>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" disabled="disabled" 
						value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
				</s:elseif>
				<s:elseif test="e.status.equals(\"sign\")">
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
						value="已审核" class="btn" disabled="disabled"><%=Order.order_status_pass_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
						value="已发货" class="btn" disabled="disabled"><%=Order.order_status_send_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
						value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
						value="已归档" class="btn btn-primary"><%=Order.order_status_file_chinese %></a>
					
					<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning" disabled="disabled"/>
					<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning" disabled="disabled"/>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" disabled="disabled" 
						value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
				</s:elseif>
				<s:elseif test="e.status.equals(\"cancel\")">
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
						value="已审核" class="btn" disabled="disabled"><%=Order.order_status_pass_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
						value="已发货" class="btn" disabled="disabled"><%=Order.order_status_send_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
						value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
						value="已归档" class="btn btn-primary" disabled="disabled"><%=Order.order_status_file_chinese %></a>
					
					<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning" disabled="disabled"/>
					<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning" disabled="disabled"/>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" disabled="disabled" 
						value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
				</s:elseif>
				<s:elseif test="e.status.equals(\"file\")">
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
						value="已审核" class="btn" disabled="disabled"><%=Order.order_status_pass_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
						value="已发货" class="btn" disabled="disabled"><%=Order.order_status_send_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
						value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
						value="已归档" class="btn" disabled="disabled"><%=Order.order_status_file_chinese %></a>
					
					<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning" disabled="disabled"/>
					<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning" disabled="disabled"/>
					<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" disabled="disabled" 
						value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
				</s:elseif>
			</s:if>
			<s:elseif test="e.status.equals(\"cancel\")">
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
					value="已审核" class="btn" disabled="disabled"><%=Order.order_status_pass_chinese %></a>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
					value="已发货" class="btn" disabled="disabled"><%=Order.order_status_send_chinese %></a>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
					value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
					value="已归档" class="btn" disabled="disabled"><%=Order.order_status_file_chinese %></a>
				
				<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning" disabled="disabled"/>
				<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning" disabled="disabled"/>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" disabled="disabled" 
						value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
			</s:elseif>
			<s:else>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=pass" onclick="return onSubmit(this);" 
					value="已审核" class="btn" disabled="disabled"><%=Order.order_status_pass_chinese %></a>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=send" onclick="return onSubmit(this);" 
					value="已发货" class="btn" disabled="disabled"><%=Order.order_status_send_chinese %></a>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=sign" onclick="return onSubmit(this);" 
					value="已签收" class="btn" disabled="disabled"><%=Order.order_status_sign_chinese %></a>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=file" onclick="return onSubmit(this);" 
					value="已归档" class="btn" disabled="disabled"><%=Order.order_status_file_chinese %></a>
				
				<input type="button" id="addPayBtn" onclick="return addPayFunc(this);" value="添加支付记录" class="btn btn-warning"/>
				<input type="button" id="updatePayMoneryBtn" onclick="return updatePayMoneryFunc(this);" value="修改订单总金额" class="btn btn-warning"/>
				<a href="order!updateOrderStatus.action?e.id=<s:property value="e.id"/>&e.status=cancel" onclick="return onSubmit(this);" 
						value="取消订单" class="btn btn-danger"><%=Order.order_status_cancel_chinese %></a>
			</s:else>
		</div>
<%-- 	</s:if> --%>
	
	<div id="addPayDiv" style="display: none;">
		<table class="table">
			<tr>
				<td colspan="2">
					<h4>添加支付记录</h4>
				</td>
			</tr>
			<tr>
				<td>支付方式</td>
				<td>
					<select name="e.orderpay.paymethod">
						<option value="zfb">支付宝</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>支付金额</td>
				<td>
					<input name="e.orderpay.payamount">
				</td>
			</tr>
			<tr>
				<td>备注</td>
				<td>
					<div class="controls"><input name="e.orderpay.remark" value="后台添加"></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:submit type="button" method="insertOrderpay" onclick="return onSubmit(this);" value="确认" cssClass="btn btn-primary"/>
					<input id="cancelPayBtn" type="button" value="取消" class="btn"/>
				</td>
			</tr>
		</table>
	</div>
	
	<div id="updatePayMoneryDiv" style="display: none;">
		<table class="table">
			<tr>
				<td colspan="2">
					<h4>修改订单总金额</h4>
				</td>
			</tr>
			<tr>
				<td>支付金额</td>
				<td>
					<input name="e.amount">
				</td>
			</tr>
			<tr>
				<td>备注</td>
				<td>
					<div class="controls"><input name="e.updatePayMoneryRemark" placeholder="修改订单金额备注"></div>
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<s:submit type="button" method="updatePayMonery" onclick="javascript:return confirm('确认本次操作?');" value="确认" cssClass="btn btn-primary"/>
					<input id="cancelUpdatePayMoneryBtn" type="button" value="取消" class="btn"/>
				</td>
			</tr>
		</table>
	</div>

	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">订单信息</a></li>
			<li><a href="#tabs-2">订单明细<s:if test="e.lowStocks.equals(\"y\")"><font color="red">【缺货】</font></s:if></a></li>
			<li><a href="#tabs-3">订单日志</a></li>
		</ul>
		<div id="tabs-1">
			<s:if test="e.refundStatusStr!=null and e.refundStatusStr!=''">
				<div class="alert alert-danger" style="margin-bottom: 2px;">
					<strong>退款状态：</strong><s:property value="e.refundStatusStr"/>(<s:property value="e.refundStatus"/>)
					<br>
					【请立刻去<a href="http://www.alipay.com" target="_blank">支付宝</a>处理此订单的退款事宜】
				</div>
			</s:if>
			
			<div class="alert alert-info" style="margin-bottom: 2px;">
				<strong>订单信息</strong>
				<s:if test="e.score>0">
					<span class="badge badge-success" style="margin-left:20px;">赠送<s:property value="e.score" />个积分点</span>
				</s:if>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<s:if test="e.amountExchangeScore>0">
					<span class="badge badge-default" style="margin-left:20px;">消耗掉会员<s:property value="e.amountExchangeScore" />个积分点</span>
				</s:if>
				&nbsp;&nbsp;&nbsp;&nbsp;
				<s:if test="e.hasGift">
					<span class="badge badge-success" style="margin-left:20px;">【订单含赠品】</span>
				</s:if>
			</div>
			<table class="table table-bordered">
				<tr>
					<th>订单编号</th>
					<th>数量</th>
					<th>创建日期</th>
					<th>订单状态</th>
					<th>支付状态</th>
					<th>订单总金额</th>
					<th>商品总金额</th>
					<th>总配送费</th>
<!-- 					<th>支付方式</th> -->
<!-- 					<th>订单备注</th> -->
					<th>收货人信息</th>
					<th>收货人地址</th>
					<th>物流信息</th>
				</tr>
				<tr>
					<td>&nbsp;<s:property value="e.id" /></td>
					<td>&nbsp;<s:property value="e.quantity" /></td>
					<td>&nbsp;<s:property value="e.createdate" /></td>
					<td>
						<s:if test="e.status.equals(\"sign\") or e.status.equals(\"file\")">
							<span class="badge badge-success"><s:property value="e.statusStr" /></span>
						</s:if>
						<s:else>
							<span class="badge badge-important"><s:property value="e.statusStr" /></span>
						</s:else>
					</td>
					<td>
						<s:if test="e.paystatus.equals(\"y\")">
							<span class="badge badge-success"><s:property value="e.paystatusStr" /></span>
						</s:if>
						<s:else>
							<span class="badge badge-important"><s:property value="e.paystatusStr" /></span>
						</s:else>
					</td>
					<td>&nbsp;<span class="simpleOrderReport"><s:property value="e.amount" /></span>
						<s:if test="e.updateAmount.equals(\"y\")"><font color="red">【修】</font></s:if>
					</td>
					<td>&nbsp;<s:property value="e.ptotal" /></td>
					<td>&nbsp;<s:property value="e.fee" /></td>
<%-- 					<td>&nbsp;<s:property value="e.payType" /></td> --%>
<%-- 					<td>&nbsp;<s:property value="e.remark" /></td> --%>
					
					<td>
						姓名：<s:property value="e.ordership.shipname" /><br>
						性别：<s:property value="e.ordership.sex" /><br>
						手机：<s:property value="e.ordership.phone" /><br>
						座机：<s:property value="e.ordership.tel" /><br>
					</td>
					<td style="width: 200px;">
						省份：<s:property value="e.ordership.province" /><br>
						城市：<s:property value="e.ordership.city" /><br>
						区域：<s:property value="e.ordership.area" /><br>
						详细地址：<s:property value="e.ordership.shipaddress" /><br>
						邮编：<s:property value="e.ordership.zip" /><br>
						备注：<s:property value="e.ordership.remark" /><br>
						
						<s:if test="e.status.equals(\"init\")">
							<a class="btn" href="<%=SystemManager.systemSetting.getManageHttp()%>/manage/order!selectOrdership.action?orderid=<s:property value="e.id" />">修改收货人配送信息</a>
						</s:if>
						<s:else>
							<a class="btn" href="<%=SystemManager.systemSetting.getManageHttp()%>/manage/order!selectOrdership.action?orderid=<s:property value="e.id" />" disabled="disabled" onclick="javascript:return false;">修改收货人配送信息</a>
						</s:else>
					</td>
					<td style="width: 150px;">
						<s:property value="e.expressCode" /><br>
						<s:property value="e.expressCompanyName" /><br>
						物流单号：<s:property value="e.expressNo" /><br>
					</td>
				</tr>
				<s:if test="e.otherRequirement!=null">
					<tr>
						<td colspan="20">附加要求:<s:property value="e.otherRequirement"/></td>
					</tr>
				</s:if>
			</table>
			
			<div class="alert alert-success" style="margin-bottom: 2px;">
				<strong>订单支付记录</strong>
			</div>
			<table class="table table-bordered">
				<tr>
					<th width="50px">序号</th>
					<th nowrap="nowrap">商户订单号</th>
					<th nowrap="nowrap">支付方式</th>
					<th nowrap="nowrap">支付金额</th>
					<th nowrap="nowrap">支付时间</th>
					<th nowrap="nowrap">支付状态</th>
					<th nowrap="nowrap">支付宝交易号</th>
					<th nowrap="nowrap">备注</th>
				</tr>
				<s:iterator value="e.orderpayList" status="i" var="orderpay">
					<tr>
						<td>&nbsp;<s:property value="#i.index+1" /></td>
						<td>&nbsp;<s:property value="id" /></td>
						<td><s:property value="paymethod" />
							<s:if test="paymethod.equals(\"n\")">
								<%=KeyValueHelper.get("orderpay_paymethod_n")%>
							</s:if>
							<s:elseif test="paymethod.equals(\"y\")">
								<%=KeyValueHelper.get("orderpay_paymethod_y")%>
							</s:elseif>
						</td>
						<td>&nbsp;<s:property value="payamount" /></td>
						<td>&nbsp;<s:property value="createtime" /></td>
						<td>
							<s:if test="paystatus.equals(\"n\")">
								<%=KeyValueHelper.get("orderpay_paystatus_n")%>
							</s:if>
							<s:elseif test="paystatus.equals(\"y\")">
								<span class="badge badge-success"><%=KeyValueHelper.get("orderpay_paystatus_y")%></span>
							</s:elseif>
						</td>
						<td>&nbsp;<s:property value="tradeNo" /></td>
						<td>&nbsp;<s:property value="remark" /></td>
					</tr>
				</s:iterator>
			</table>
			
		</div>
		
		<div id="tabs-2">
			<table class="table table-bordered">
				<tr style="background-color: #dff0d8">
					<th width="50px">序号</th>
					<th>商品编号</th>
					<th>商品名称</th>
					<th>购买的商品规格</th>
<!-- 					<th>赠送积分</th> -->
					<th>数量</th>
					<th>单价</th>
<!-- 					<th>配送费</th> -->
					<th>小计</th>
				</tr>
				<s:iterator value="e.orderdetail" status="i" var="odetail">
					<tr>
						<td>&nbsp;<s:property value="#i.index+1" /></td>
						<td nowrap="nowrap">
							<s:a target="_blank" style="text-decoration: underline;" href="product!toEdit.action?e.id=%{productID}">
								<s:property value="productID" />
							</s:a>
							<s:if test="lowStocks.equals(\"y\")"><font color="red">【缺货】</font></s:if>
						</td>
						<td><a target="_blank" style="text-decoration: underline;" href="<%=SystemManager.systemSetting.getWww()%>/product/<s:property value="productID" />.html"><s:property value="productName"/></a>
							<br>
							<s:if test="giftID!=null and giftID!=''">
								<s:a target="_blank" style="text-decoration: underline;" href="gift!show.action?e.id=%{giftID}">
									【查看赠品】
								</s:a>
							</s:if>
						</td>
						<td>&nbsp;<s:property value="specInfo" /></td>
<%-- 						<td>&nbsp;<s:property value="score" /></td> --%>
						<td>&nbsp;<s:property value="number" /></td>
						<td>&nbsp;￥<s:property value="price" /></td>
<%-- 						<td>&nbsp;￥<s:property value="fee" /></td> --%>
						<td>&nbsp;￥<s:property value="total0" /></td>
						</td>
					</tr>
				</s:iterator>
			</table>
		</div>
		
		<div id="tabs-3">
			<table class="table table-bordered">
				<tr style="background-color: #dff0d8">
					<th width="50px">序号</th>
					<th>操作人</th>
					<th>操作人类型</th>
					<th>时间</th>
					<th>日志</th>
				</tr>
				<s:iterator value="e.orderlogs" status="i" var="orderlog">
					<tr>
						<td>&nbsp;<s:property value="#i.index+1" /></td>
						<td nowrap="nowrap">&nbsp;
							<s:if test="accountType.equals(\"w\")">
								<s:a target="_blank" href="account!show.action?account=%{account}"><s:property value="account" /></s:a>
							</s:if>
							<s:elseif test="accountType.equals(\"m\")">
								<s:a target="_blank" href="user!show.action?account=%{account}"><s:property value="account" /></s:a>
							</s:elseif>
							<s:elseif test="accountType.equals(\"p\")">
								第三方支付系统
							</s:elseif>
							<s:else>
								未知
							</s:else>
						</td>
						<td>&nbsp;
							<s:if test="accountType.equals(\"w\")">
								客户
							</s:if>
							<s:elseif test="accountType.equals(\"m\")">
								<%=SystemManager.systemSetting.getSystemCode() %>(系统)
							</s:elseif>
							<s:elseif test="accountType.equals(\"p\")">
								支付宝
							</s:elseif>
							<s:else>
								未知
							</s:else>
						</td>
						<td>&nbsp;<s:property value="createdate" /></td>
						<td>&nbsp;<s:property value="content" /></td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
</s:form>

<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jquery-easyui-1.3.4/themes/default/easyui.css"> --%>
<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jquery-easyui-1.3.4/themes/icon.css"> --%>
<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/resource/jquery-easyui-1.3.4/demo/demo.css"> --%>
<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/resource/jquery-easyui-1.3.4/jquery.min.js"></script> --%>
<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/resource/jquery-easyui-1.3.4/jquery.easyui.min.js"></script> --%>

<script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/jquery-1.5.1.js"></script>
<script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/ui/jquery.ui.core.js"></script>
<script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/ui/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/ui/jquery.ui.tabs.js"></script>



<script>
$(function() {
	$( "#tabs" ).tabs({
		//event: "mouseover"
	});
	$("#cancelPayBtn").click(function(){
		$("#addPayDiv").slideUp();
		$("#addPayBtn").show();
		//$("#buttons").find("input[type=button]").each(function(){
			//$(this).attr("disabled","");
		//});
		return false;
	});
	$("#cancelUpdatePayMoneryBtn").click(function(){
		$("#updatePayMoneryDiv").slideUp();
		$("#updatePayMoneryBtn").show();
		return false;
	});
});
function addPayFunc(){
	$("#addPayDiv").slideDown();
	$("#addPayBtn").hide();
	//$("#buttons").find("input[type=button]").each(function(){
		//$(this).attr("disabled","disabled");
	//});
	return false;
}
function updatePayMoneryFunc(){
	$("#updatePayMoneryDiv").slideDown();
	$("#updatePayMoneryBtn").hide();
	return false;
}
function onSubmit(obj){
	//if(confirm("确认本次操作?")){
		//document.form1.action = "order!"+$(obj).attr("method")+".action";
		//document.form1.submit();
	//}
	if($(obj).attr("disabled")=='disabled'){//alert("disabled不可点击"+$(obj).attr("disabled"));
		return false;
	}
	return confirm("确认本次操作?");
}
</script>
</body>
</html>
