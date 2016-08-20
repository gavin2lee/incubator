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
	<s:form action="express" namespace="/manage" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td colspan="8">
<%-- 					<s:submit method="selectList" value="查询" cssClass="btn btn-primary"/> --%>
					<button method="express!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
					
<%-- 					<s:submit method="toAdd" value="添加" cssClass="btn btn-success" /> --%>
					<s:a method="toAdd" cssClass="btn btn-success">
						<i class="icon-plus-sign icon-white"></i> 添加
					</s:a>
							
<%-- 					<s:submit method="deletes" onclick="return deleteSelect('确定删除选择的记录?');" value="删除" cssClass="btn btn-danger" /> --%>
<%-- 					<s:a method="deletes" cssClass="btn btn-danger" onclick="return deleteSelect('确定删除选择的记录?');"> --%>
<!-- 						<i class="icon-remove-sign icon-white"></i> 删除 -->
<%-- 					</s:a> --%>
					<button method="express!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
						<i class="icon-remove-sign icon-white"></i> 删除
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
				<th nowrap="nowrap">快递编码</th>
				<th nowrap="nowrap">名称</th>
				<th nowrap="nowrap">费用</th>
				<th nowrap="nowrap">顺序</th>
				<th style="width: 115px;">操作</th>
			</tr>
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="code" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="name" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="fee" /></td>
					<td nowrap="nowrap">&nbsp;<s:property value="order1" /></td>
					<td nowrap="nowrap">
						<s:a href="express!toEdit.action?e.id=%{id}">编辑</s:a>
					</td>
				</tr>
			</s:iterator>
			<tr>
				<td colspan="16" style="text-align: center;"><%@ include
						file="/manage/system/pager.jsp"%></td>
			</tr>
		</table>
	</s:form>
</body>
</html>
