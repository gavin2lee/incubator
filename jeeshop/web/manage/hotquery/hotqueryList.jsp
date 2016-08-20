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
<%@ include file="/resource/common_html_validator.jsp"%>
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/zTree3.1/css/zTreeStyle/zTreeStyle.css" type="text/css">
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
</head>

<body>
	<s:form action="hotquery" namespace="/manage" method="post" theme="simple" id="form" name="form">
		<s:hidden name="e.type"/>
		<input type="hidden" value="<s:property value="e.catalogID"/>" id="catalogID"/>
		<table class="table table-bordered">
			<tr>
				<td colspan="16">
					<button method="hotquery!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
						
					<s:a method="toAdd" cssClass="btn btn-success">
						<s:param name="type" value="e.type"></s:param>
						<i class="icon-plus-sign icon-white"></i> 添加
					</s:a>
						
					<button method="hotquery!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
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
				<th width="20px"><input type="checkbox" id="firstCheckbox" /></th>
				<th width="100px">ID</th>
				<th>热门查询关键字</th>
				<th>链接</th>
				<th width="60px;">操作</th>
			</tr>
			
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td ><s:property value="id"/></td>
					<td class="aCss">
					  <s:a href="hotquery!toEdit.action?e.id=%{id}" ><s:property value="key1"/></s:a>
					</td>
					<td>&nbsp;<a target="_blank" href="<s:property value="url" />"><s:property value="url" /></a></td>
					<td>
						<s:a href="hotquery!toEdit.action?e.id=%{id}">编辑</s:a>
					</td>
				</tr>
			</s:iterator>

			<tr>
				<td colspan="17" style="text-align: center;font-size: 12px;"><%@ include
						file="/manage/system/pager.jsp"%></td>
			</tr>
		</table>
		
	</s:form>
	

</body>
</html>
