<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page session="false" %>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
</head>

<body>
<s:form action="indexImg" namespace="/manage" method="post" theme="simple">
			<table class="table table-bordered">
				<tr>
					<td colspan="4">
<%-- 						<s:submit method="selectList" value="查询" cssClass="btn btn-primary"/> --%>
						<s:a method="selectList" cssClass="btn btn-primary">
							<i class="icon-search icon-white"></i> 查询
						</s:a>
					
<%-- 						<s:submit method="toAdd" value="添加" cssClass="btn btn-success"/> --%>
						<s:a method="toAdd" cssClass="btn btn-success">
							<i class="icon-plus-sign icon-white"></i> 添加
						</s:a>
					
<%-- 						<s:submit method="deletes" onclick="return deleteSelect();" value="删除" cssClass="btn btn-danger"/> --%>
						<button method="indexImg!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
<%-- 						<s:a method="deletes" cssClass="btn btn-danger" onclick="return deleteSelect();"> --%>
<!-- 							<i class="icon-remove-sign icon-white"></i> 删除 -->
<%-- 						</s:a> --%>
						<button method="indexImg!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
							<i class="icon-remove-sign icon-white"></i> 删除
						</button>
					
						<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
								<%@ include file="/manage/system/pager.jsp"%>
						</div>
					</td>
				</tr>
			</table>
			
			<div class="alert alert-info">
				注意：图片尺寸请尽量保持在630*180，否则超出的部分会显示不出来。
			</div>
			
			<table class="table table-bordered">
				<tr style="background-color: #dff0d8">
					<th width="20"><input type="checkbox" id="firstCheckbox"/></th>
					<th style="display: none;">id</th>
					<th nowrap="nowrap">标题</th>
					<th>图片</th>
					<th>排序</th>
<!-- 					<th>描述</th> -->
					<th style="width: 50px;">操作</th>
				</tr>
				<s:iterator value="pager.list">
					<tr>
						<td><input type="checkbox" name="ids" value="<s:property value="id"/>"/></td>
						<td style="display: none;">&nbsp;<s:property value="id"/></td>
						<td>&nbsp;<s:property value="title"/></td>
						<td>&nbsp;
							<a href="<%=SystemManager.systemSetting.getImageRootPath() %><s:property value="picture"/>" target="_blank">
								<img style="max-width: 100px;max-height: 100px;" alt="" src="<%=SystemManager.systemSetting.getImageRootPath() %><s:property value="picture"/>">
							</a>
							<br>
							<div>图片链接：</div>
							<a target="_blank" href="<s:property value="link"/>"><s:property value="link"/></a>
						</td>
						<td>&nbsp;<s:property value="order1"/></td>
<%-- 						<td>&nbsp;<s:property value="desc1"/></td> --%>
						<td>
							<s:a href="indexImg!toEdit.action?e.id=%{id}">编辑</s:a>
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
