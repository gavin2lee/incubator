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
</head>
<body>
<div class="container">
	<div class="row">
		<div class="span12">
			
			<table class="table table-bordered">
				<tr><td colspan="11">订单信息</td></tr>
				<tr>
					<th nowrap="nowrap">订单号</th>
					<th nowrap="nowrap"><s:property value="e.id"/></th>
					<th nowrap="nowrap">订单总金额</th>
					<th nowrap="nowrap"><s:property value="e.amount"/></th>
					<th nowrap="nowrap">数量</th>
					<th nowrap="nowrap"><s:property value="e.quantity"/></th>
				</tr>
				<tr>
					<th nowrap="nowrap">下单日期</th>
					<td nowrap="nowrap"><s:property value="e.createdate"/></td>
					<th nowrap="nowrap">配送费</th>
					<th nowrap="nowrap"><s:property value="e.fee"/></th>
					<th nowrap="nowrap"></th>
					<th nowrap="nowrap"></th>
				</tr>
				<tr>
					<th nowrap="nowrap">交易平台</th>
					<td nowrap="nowrap" colspan="21"><%=SystemManager.systemSetting.getName() %>
						(<%=SystemManager.systemSetting.getWww() %>)
					</td>
				</tr>
				<tr>
					<th nowrap="nowrap">收货人</th>
					<td colspan="21">
						<s:property value="e.ordership.province" />,
						<s:property value="e.ordership.city" />,
						<s:property value="e.ordership.area" />,
						<s:property value="e.ordership.shipaddress" />,
						<s:property value="e.ordership.shipname" />(收)
					</td>
				</tr>
				
				<tr><td colspan="11">&nbsp;</td></tr>
				
				<tr>
					<td width="50px">序号</td>
		<!-- 			<td>商品编号</td> -->
					<td>商品名称</td>
					<td>数量</td>
					<td>单价</td>
					<td>配送费</td>
					<td>小计</td>
				</tr>
				<s:iterator value="e.orderdetail" status="i" var="odetail">
					<tr>
						<td>&nbsp;<s:property value="#i.index+1" /></td>
						<td width="400px"><s:property value="productName"/></td>
						<td><s:property value="number" /></td>
						<td>￥<s:property value="price" /></td>
						<td>￥<s:property value="fee" /></td>
						<td>￥<s:property value="total0" /></td>
						</td>
					</tr>
				</s:iterator>
			</table>
	
		</div>
	</div>	
</div>
	
</body>
</html>
