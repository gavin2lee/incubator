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
	<s:form action="navigation" namespace="/manage" method="post" theme="simple">
		<table class="table table-bordered">
			<tr>
				<td colspan="6">
<%-- 					<s:submit method="selectList" value="查询" cssClass="btn btn-primary" /> --%>
<%-- 					<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 						<i class="icon-search icon-white"></i> 查询 -->
<%-- 					</s:a> --%>
					<button method="navigation!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
					
<%-- 					<s:submit method="toAdd" value="添加" cssClass="btn btn-success" /> --%>
					<s:a method="toAdd" cssClass="btn btn-success">
						<i class="icon-plus-sign icon-white"></i> 添加
					</s:a>
					
<%-- 					<s:submit method="deletes" onclick="return deleteSelect();" value="删除" cssClass="btn btn-danger" /> --%>
<%-- 					<s:a method="deletes" cssClass="btn btn-danger" onclick="return deleteSelect();"> --%>
<!-- 						<i class="icon-remove-sign icon-white"></i> 删除 -->
<%-- 					</s:a> --%>
					<button method="navigation!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
						<i class="icon-remove-sign icon-white"></i> 删除
					</button>
					
					<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
						<%@ include file="/manage/system/pager.jsp"%>
					</div>
				</td>
			</tr>
		</table>
		
		<div class="alert alert-info" style="margin-bottom: 2px;text-align: left;">友情链接会自动显示到门户的最底部。友情链接的地址不要以“http://”开头。</div>
		<table class="table table-bordered table-hover">
			<tr style="background-color: #dff0d8">
				<th width="20"><input type="checkbox" id="firstCheckbox" /></th>
				<th style="display: none;">编号</th>
				<th >名称</th>
				<th >链接</th>
				<th >打开方式</th>
				<th >位置</th>
				<th >顺序</th>
				<th nowrap="nowrap">操作</th>
			</tr>
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td style="display: none;">&nbsp;<s:property value="id" /></td>
					<td>&nbsp;<a href="http://<s:property value="http"/>" target="_blank"><s:property value="name" /></a></td>
					<td>&nbsp;<s:property value="http" /></td>
					<td>&nbsp;<s:property value="target" /></td>
					<td>&nbsp;<s:property value="position" /></td>
					<td>&nbsp;<s:property value="order1" /></td>
					<td><s:a href="navigation!toEdit.action?e.id=%{id}">编辑</s:a></td>
				</tr>
			</s:iterator>

			<tr>
				<td colspan="71" style="text-align: center;"><%@ include
						file="/manage/system/pager.jsp"%></td>
			</tr>
		</table>

	</s:form>
</body>
</html>
