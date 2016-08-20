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
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
<style type="text/css">
.titleCss {
	background-color: #e6e6e6;
	border: solid 1px #e6e6e6;
	position: relative;
	margin: -1px 0 0 0;
	line-height: 32px;
	text-align: left;
}

.aCss {
	overflow: hidden;
	word-break: keep-all;
	white-space: nowrap;
	text-overflow: ellipsis;
	text-align: left;
	font-size: 12px;
}

.liCss {
	white-space: nowrap;
	text-overflow: ellipsis;
	overflow: hidden;
	height: 30px;
	text-align: left;
	margin-left: 10px;
	margin-right: 10px;
}
</style>
<script type="text/javascript">
	$(function() {
		function c1(f) {
			$(":checkbox").each(function() {
				$(this).attr("checked", f);
			});
		}
		$("#firstCheckbox").click(function() {
			if ($(this).attr("checked")) {
				c1(true);
			} else {
				c1(false);
			}
		});
	});
	function deleteSelect() {
		if ($("input:checked").size() == 0) {
			return false;
		}
		return confirm("确定删除选择的记录?");
	}
	function updateInBlackList() {
		if ($("input:checked").size() == 0) {
			return false;
		}
		return confirm("确定将选择的记录拉入新闻黑名单吗?");
	}
</script>
</head>

<body>
	<s:form action="order" namespace="/manage" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td>订单号</td>
				<td><s:textfield name="e.id" cssClass="search-query input-small"/></td>
				<td>订单状态</td>
				<td><s:select cssClass="search-query input-small"
						list="#{'':'','init':'未审核','pass':'已审核','send':'已发货','sign':'已签收','cancel':'已取消','file':'已归档'}" 
						name="e.status" id="status" listKey="key" listValue="value" /></td>
				<td>支付状态</td>
				<td><s:select cssClass="search-query input-small"
						list="#{'':'','n':'未支付','y':'完全支付'}" 
						name="e.paystatus" id="paystatus" listKey="key" listValue="value" /></td>
				<td>用户账号</td>
				<td><s:textfield cssClass="search-query input-small" name="e.account"/></td>
				<td>时间范围</td>
				<td><input id="d4311" class="Wdate search-query input-small" type="text" name="e.startDate"
					value="<s:property value="e.startDate" />"
					onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/>
					~ 
					<input id="d4312" class="Wdate search-query input-small" type="text" name="e.endDate" 
					value="<s:property value="e.endDate" />"
					onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
				</td>
			</tr>
			<tr>
				<td>退款状态</td>
				<td colspan="21"><s:select cssClass="search-query "
						list="#{'':'','WAIT_SELLER_AGREE':'等待卖家同意退款','WAIT_BUYER_RETURN_GOODS':'卖家同意退款，等待买家退货','WAIT_SELLER_CONFIRM_GOODS':'买家已退货，等待卖家收到退货','REFUND_SUCCESS':'退款成功，交易关闭'}" 
						name="e.refundStatus" id="refundStatus" listKey="key" listValue="value" /></td>
			</tr>
			<tr>
				<td colspan="14">
<%-- 					<s:submit action="order" method="selectList" value="查询" cssClass="btn btn-primary" />  --%>
<%-- 						<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 							<i class="icon-search icon-white"></i> 查询 -->
<%-- 						</s:a> --%>
						<button method="order!selectList.action" class="btn btn-primary" onclick="selectList(this)">
							<i class="icon-search icon-white"></i> 查询
						</button>
<%-- 					<s:submit action="order"  method="importXls" value="导出" cssClass="btn btn-success" />  --%>
					<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
						<%@ include file="/manage/system/pager.jsp"%>
					</div>
				</td>
			</tr>
		</table>
				
		<table class="table table-bordered table-hover">
			<tr style="background-color: #dff0d8">
				<th width="20"><input type="checkbox" id="firstCheckbox" /></th>
				<th>订单号</th>
				<th>订单总金额</th>
				<th>商品总金额</th>
				<th>配送费</th>
				<th>数量</th>
				<th>会员</th>
<!-- 				<th>折扣</th> -->
				<th>创建日期</th>
				<th>订单状态</th>
				<th>支付状态</th>
				<th width="60px">操作</th>
			</tr>
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td>
						<s:property value="id" />
						<s:if test="lowStocks.equals(\"y\")"><font color="red">【缺货】</font></s:if>
					</td>
					<td><s:property value="amount" />
						<s:if test="updateAmount.equals(\"y\")"><font color="red">【修】</font></s:if>
					</td>
					<td><s:property value="ptotal" /></td>
					<td><s:property value="fee" /></td>
					<td align="center"><s:property value="quantity" /></td>
					<td><s:a target="_blank" href="account!show.action?account=%{account}"><s:property value="account" /></s:a></td>
<%-- 					<td><s:property value="rebate" /></td> --%>
					<td><s:property value="createdate" /></td>
					<td><s:property value="statusStr" />
						<s:if test="status.equals(\"cancel\")">
							<img src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
						</s:if>
						<s:elseif test="status.equals(\"file\")">
							<img src="<%=request.getContextPath() %>/resource/images/action_check.gif">
						</s:elseif>
						<s:elseif test="status.equals(\"init\")">
							<img src="<%=request.getContextPath() %>/resource/images/action_add.gif">
						</s:elseif>
					</td>
					<td><s:property value="paystatusStr" />
						<s:if test="paystatus.equals(\"y\")">
							<img src="<%=request.getContextPath() %>/resource/images/action_check.gif">
						</s:if>
						<s:elseif test="paystatus.equals(\"n\")">
							<img src="<%=request.getContextPath() %>/resource/images/action_add.gif">
						</s:elseif>
					</td>
					<td><s:a target="_blank" href="order!toEdit.action?e.id=%{id}">编辑</s:a>|
					<s:a target="_blank" href="order!toPrint.action?e.id=%{id}">打印</s:a>
					</td>
				</tr>
			</s:iterator>

			<tr>
				<td colspan="55" style="text-align: center;"><%@ include
						file="/manage/system/pager.jsp"%></td>
			</tr>
		</table>
		
		<div class="alert alert-info" style="text-align: left;font-size: 14px;margin: 2px 0px;">
			图标含义：<BR>
			<img alt="新增" src="<%=request.getContextPath() %>/resource/images/action_add.gif">：未审核、未支付
			<img alt="已上架" src="<%=request.getContextPath() %>/resource/images/action_check.gif">：已归档
			<img alt="已下架" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">：已取消
		</div>

	</s:form>
</body>
</html>
