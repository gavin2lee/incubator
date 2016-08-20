<%@page import="net.jeeshop.core.ManageContainer"%>
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
	<s:form action="emailNotifyProduct" namespace="/manage" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td style="text-align: right;" nowrap="nowrap">账号</td>
				<td style="text-align: left;"><s:textfield name="e.account" cssClass="search-query input-small"
						id="account" /></td>
				<td style="text-align: right;" nowrap="nowrap">接收到货的邮箱</td>
				<td style="text-align: left;"><s:textfield name="e.receiveEmail" cssClass="input-small"
						id="receiveEmail" /></td>
				<td style="text-align: right;" nowrap="nowrap">是否已发送通知</td>
				<td style="text-align: left;">
					<s:select list="#{'':'','y':'是','n':'否'}" id="status" name="e.status"  cssClass="input-medium" 
						listKey="key" listValue="value"  />
				</td>
			</tr>
			<tr>
				<td colspan="28">
<%-- 					<s:submit method="selectList" value="查询" cssClass="btn btn-primary"/> --%>
<%-- 					<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 						<i class="icon-search icon-white"></i> 查询 -->
<%-- 					</s:a> --%>
					<button method="emailNotifyProduct!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
							
<%-- 					<s:submit method="autoNotify" value="autoNotify" cssClass="btn btn-primary"/> --%>
					<button method="emailNotifyProduct!autoNotify.action" class="btn btn-success" onclick="submitNotValid2222(this)">
						<i class="icon-ok icon-white"></i> 触发通知
					</button>
					
<%-- 					<s:checkbox name="systemNotify" value="true" fieldValue="Y"/> 系统自动通知 --%>
					<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
						<%@ include file="/manage/system/pager.jsp"%>
					</div>
				</td>
			</tr>
		</table>
		
		<div class="alert alert-info">
			发送失败3次的系统将不再尝试发送!
		</div>
		
		<table class="table table-bordered table-hover">
			<tr style="background-color: #dff0d8">
				<th width="20"><input type="checkbox" id="firstCheckbox" /></th>
				<th nowrap="nowrap">帐号</th>
				<th nowrap="nowrap">接收到货的邮箱</th>
				<th nowrap="nowrap">商品ID</th>
				<th nowrap="nowrap">商品名称</th>
				<th nowrap="nowrap">申请时间</th>
				<th nowrap="nowrap">通知时间</th>
				<th nowrap="nowrap">是否已发送通知</th>
			</tr>
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td nowrap="nowrap">
						<s:a target="_blank" href="account!show.action?account=%{account}"><s:property value="account" />
						</s:a>
					</td>
					<td nowrap="nowrap">&nbsp;<s:property value="receiveEmail" /></td>
					<td nowrap="nowrap">&nbsp;
						<a href="<%=SystemManager.systemSetting.getManageHttp()%>/manage/product!toEdit.action?e.id=<s:property value="productID" />.html" target="_blank">
							<s:property value="productID" />
						</a>
					</td>
					<td nowrap="nowrap">&nbsp;
						<a href="<%=SystemManager.systemSetting.getWww()%>/product/<s:property value="productID" />.html" target="_blank" title="<s:property value="productName" />">
						<s:property value="productName" /></a>
					</td>
					<td nowrap="nowrap">&nbsp;<s:property value="createdate" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="notifydate" /></td>
					<td nowrap="nowrap" style="text-align: center;">
						<s:if test="status.equals(\"y\")">
							<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
						</s:if>
						<s:else>
							<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
							(失败:<s:property value="sendFailureCount" />次)
						</s:else>
					</td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="16" style="text-align: center;"><%@ include
						file="/manage/system/pager.jsp"%></td>
			</tr>
		</table>
	</s:form>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
	$(function() {
	});
	function deleteSelect() {
		if ($("input:checked").size() == 0) {
			return false;
		}
		return confirm("确定删除选择的记录?");
	}
</script>
</body>
</html>
