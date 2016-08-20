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
	<s:form action="email!selectList.action" namespace="/manage" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td style="text-align: right;" nowrap="nowrap">账号</td>
				<td style="text-align: left;"><s:textfield name="e.account" cssClass="search-query input-small"
						id="account" /></td>
				<td style="text-align: right;" nowrap="nowrap">发送状态</td>
				<td style="text-align: left;">
					<s:select list="#{'':'','y':'发送成功','n':'发送失败'}" id="sendStatus" name="e.sendStatus"  cssClass="input-medium" 
						listKey="key" listValue="value"  />
				</td>
			</tr>
			<tr>
				<td colspan="28">
<%-- 					<s:submit method="selectList" value="查询" cssClass="btn btn-primary"/> --%>
<%-- 					<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 						<i class="icon-search icon-white"></i> 查询 -->
<%-- 					</s:a> --%>
					<button method="email!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
					
					<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
						<%@ include file="/manage/system/pager.jsp"%>
					</div>
				</td>
			</tr>
		</table>
		
		<table class="table table-bordered table-hover">
			<tr style="background-color: #dff0d8">
				<th width="20"><input type="checkbox" id="firstCheckbox" /></th>
				<th nowrap="nowrap">帐号</th>
				<th nowrap="nowrap">模板名称</th>
				<th nowrap="nowrap">创建时间</th>
				<th nowrap="nowrap">发送状态</th>
				<th nowrap="nowrap">链接状态</th>
<!-- 				<th nowrap="nowrap">url</th> -->
<!-- 				<th style="width: 115px;">操作</th> -->
			</tr>
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td nowrap="nowrap">&nbsp;
						<s:a target="_blank" href="account!show.action?account=%{account}"><s:property value="account" />
						</s:a>
					</td>
					<td nowrap="nowrap">&nbsp;
						<s:property value="notifyTemplateName" />
					</td>
					<td nowrap="nowrap">&nbsp;<s:property value="createdate" /></td>
					<td nowrap="nowrap">&nbsp;
						<s:if test="sendStatus.equals(\"y\")">
							<img alt="发送成功" src="<%=request.getContextPath() %>/resource/images/action_check.gif">发送成功
						</s:if>
						<s:elseif test="sendStatus.equals(\"n\")">
							<img alt="发送失败" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">发送失败
						</s:elseif>
						<s:else>
							发送中...
						</s:else>
					</td>
					<td nowrap="nowrap">&nbsp;
						<s:if test="status.equals(\"y\")">
							<img alt="已失效" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">已失效
						</s:if>
						<s:else>
							<img alt="未失效" src="<%=request.getContextPath() %>/resource/images/action_check.gif">未失效
						</s:else>
					</td>
<%-- 					<td nowrap="nowrap">&nbsp;<s:property value="url" /></td> --%>
<!-- 					<td nowrap="nowrap"> -->
<%-- 						<s:a href="email!toEdit.action?e.id=%{id}">编辑</s:a> --%>
<!-- 					</td> -->
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
</script>
</body>
</html>
