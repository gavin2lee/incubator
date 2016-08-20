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
	<s:form action="attribute" namespace="/manage" method="post" theme="simple">
				<s:hidden name="e.pid" />
				<table class="table table-bordered">
					<tr>
						<td style="text-align: right;">
							商品分类
						</td>
						<td>
							
							<input type="hidden" id="catalogID2" value="<s:property value="e.catalogID"/>"/>
<%-- 							<input id="cc" class="easyui-combotree" name="e.catalogID" value="<s:property value="e.catalogID"/>" --%>
<%-- 							data-options="url:'<%=request.getContextPath() %>/manage/catalog/catalog!getRootWithTreegrid.action?e.type=p',method:'get',required:false"  --%>
<!-- 							> -->
							<%
							application.setAttribute("catalogs", SystemManager.catalogs);
							%>
							<select onchange="catalogChange(this)" name="e.catalogID" id="catalogSelect">
								<option></option>
								<s:iterator value="#application.catalogs">
									<option pid="0" value="<s:property value="id"/>"><font color='red'><s:property value="name"/></font></option>
									<s:iterator value="children">
										<option value="<s:property value="id"/>">&nbsp;&nbsp;&nbsp;&nbsp;<s:property value="name"/></option>
									</s:iterator>
								</s:iterator>
							</select>
							(请选择子目录查询。)
						</td>
					</tr>
					<tr>
						<td colspan="2">
<%-- 							<s:submit method="selectList" value="查询" cssClass="btn btn-primary" /> --%>
<%-- 							<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 								<i class="icon-search icon-white"></i> 查询 -->
<%-- 							</s:a> --%>
							<button method="attribute!selectList.action" class="btn btn-primary" onclick="selectList(this)">
								<i class="icon-search icon-white"></i> 查询
							</button>
					
<%-- 							<s:submit method="toAdd" value="添加" cssClass="btn btn-success" /> --%>
							<s:a method="toAdd" cssClass="btn btn-success">
								<s:param name="e.pid" value="e.pid"/>
								<i class="icon-plus-sign icon-white"></i> 添加
							</s:a>
							
<%-- 							<s:submit method="deletes" onclick="return deleteSelect();" value="删除" cssClass="btn btn-danger" /> --%>
							
<%-- 							<s:a method="deletes" cssClass="btn btn-danger" onclick="deleteSelect();"> --%>
<!-- 								<i class="icon-remove-sign icon-white"></i> 删除 -->
<%-- 							</s:a> --%>
							<button method="attribute!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
								<i class="icon-remove-sign icon-white"></i> 删除
							</button>
							
							<s:if test="e.pid==0">
								<span class="badge badge-important">商品属性列表</span>
							</s:if>
							<s:else>
								<span class="badge badge-info">商品参数列表</span>
							</s:else>
							
							
							<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
								<%@ include file="/manage/system/pager.jsp"%>
							</div>		
						</td>
					</tr>
				</table>
				
				<table class="table table-bordered  table-hover">
					<tr style="background-color: #dff0d8">
						<th width="20"><input type="checkbox" id="firstCheckbox" /></th>
						<th style="display: none;">编号</th>
						<th nowrap="nowrap">名称</th>
						<th >子项</th>
						<th nowrap="nowrap">所属类别</th>
						<th nowrap="nowrap">顺序</th>
						<th nowrap="nowrap">操作</th>
					</tr>
					<s:iterator value="pager.list">
						<tr>
							<td><input type="checkbox" name="ids"
								value="<s:property value="id"/>" /></td>
							<td style="display: none;">&nbsp;<s:property value="id" /></td>
							<td nowrap="nowrap">&nbsp;<s:property value="name" /></td>
							<td>&nbsp;<s:property value="nameBuff" /></td>
							<td nowrap="nowrap">&nbsp;<s:property value="catalogName" /></td>
							<td nowrap="nowrap">&nbsp;<s:property value="order1" /></td>
							<td nowrap="nowrap"><s:a href="attribute!toEdit.action?e.id=%{id}">编辑</s:a></td>
						</tr>
					</s:iterator>

					<tr>
						<td colspan="7" style="text-align: center;"><%@ include
								file="/manage/system/pager.jsp"%></td>
					</tr>
				</table>

	</s:form>
	
<script type="text/javascript">
	$(function() {
		selectDefaultCatalog();
	});
	
	function selectDefaultCatalog(){
		var _catalogID = $("#catalogID2").val();
		if(_catalogID!='' && _catalogID>0){
			$("#catalogSelect").attr("value",_catalogID);
		}
	}
</script>
</body>
</html>
