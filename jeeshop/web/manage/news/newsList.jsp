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
	<s:form action="news" namespace="/manage" method="post" theme="simple" id="form" name="form">
		<s:hidden name="e.type"/>
		<input type="hidden" value="<s:property value="e.catalogID"/>" id="catalogID"/>
		<table class="table table-bordered">
			<tr>
				<td>标题</td>
				<td><s:textfield cssClass="input-medium search-query" name="e.title"/></td>
				<s:if test="e.type.equals(\"help\")">
					<td>文章类型</td>
					<td>
<%-- 							<input id="cc" class="easyui-combotree" name="e.catalogID" value="<s:property value="e.catalogID"/>" --%>
<%-- 							data-options="url:'<%=request.getContextPath() %>/manage/catalog/catalog!getRootWithTreegrid.action?type=a',method:'get',required:false"  --%>
<!-- 							> -->
						<%
						application.setAttribute("catalogs", SystemManager.catalogsArticle);
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
					</td>
				</s:if>
				<td>状态</td>
				<td><s:select cssStyle="width:100px;"
						list="#{'':'','y':'显示','n':'不显示'}"
						name="e.status" id="status" listKey="key" listValue="value" /></td>
					<!-- 
				<td>时间范围</td>
				<td>
					<input id="d4311" class="Wdate search-query input-small" type="text" name="e.createtime"
					value="<s:property value="e.createtime" />"
					onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/>
					~ 
					<input id="d4312" class="Wdate search-query input-small" type="text" name="e.createtimeEnd" 
					value="<s:property value="e.createtimeEnd" />"
					onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
					
					<s:textfield class="Wdate input" 
					name="e.createtime" cssStyle="width:80px;"
					onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
					~ <s:textfield class="Wdate input" type="text" name="e.createtimeEnd" cssStyle="width:80px;"
					onFocus="WdatePicker({isShowClear:false,readOnly:true,dateFmt:'yyyy-MM-dd'})" />
				</td>
					 -->
			</tr>
			<tr>
				<td colspan="16">
<%-- 					<s:submit method="selectList" value="查询" cssClass="btn btn-primary"> --%>
<%-- 						<s:param name="btnSelect" value="1">1</s:param> --%>
<!-- 						<i class="icon-search icon-white"></i> 查询 -->
<%-- 					</s:submit> --%>
					
<%-- 					<s:a method="selectList" cssClass="btn btn-primary"> --%>
<!-- 						<i class="icon-search icon-white"></i> 查询 -->
<%-- 					</s:a> --%>
					<button method="news!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
						
<%-- 					<s:submit method="toAdd" action="news" value="添加" cssClass="btn btn-success" /> --%>
					<s:a method="toAdd" cssClass="btn btn-success">
						<s:param name="type" value="e.type"></s:param>
						<i class="icon-plus-sign icon-white"></i> 添加
					</s:a>
						
<%-- 					<s:submit method="deletes" onclick="return deleteSelect('确定删除选择的记录?');" value="删除" cssClass="btn btn-danger" /> --%>
					<button method="news!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
						<i class="icon-remove-sign icon-white"></i> 删除
					</button>
					
<%-- 					<s:submit method="updateStatusY" value="显示" onclick="return deleteSelect('确定让选择的记录审核通过?这样选择的记录将会出现在门户上。');" cssClass="btn btn-warning" /> --%>
					<button method="news!updateStatusY.action" class="btn btn-warning" onclick="return submitIDs(this,'确定让选择的记录审核通过?这样选择的记录将会出现在门户上。');">
						<i class="icon-arrow-up icon-white"></i> 显示
					</button>
						
<%-- 					<s:submit method="updateStatusN" value="不显示" cssClass="btn btn-warning" onclick="return deleteSelect('执行该操作后,选择的记录将不会出现在门户上。确定要执行?');"/> --%>
					<button method="news!updateStatusN.action" class="btn btn-warning" onclick="return submitIDs(this,'执行该操作后,选择的记录将不会出现在门户上。确定要执行?');">
						<i class="icon-arrow-down icon-white"></i> 不显示
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
				<th width="120px">ID</th>
				<th>标题</th>
				<s:if test="e.type.equals(\"help\")">
					<th>code</th>
					<th width="130px">显示顺序</th>
				</s:if>
				<th width="80px">最后操作时间</th>
				<th width="60px;">显示状态</th>
				<th width="60px;">操作</th>
			</tr>
			
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td ><s:property value="id"/></td>
					<td class="aCss">
					  <s:a href="news!toEdit.action?e.id=%{id}" 
								><s:property value="title"/></s:a>
					</td>
					<s:if test="e.type.equals(\"help\")">
						<td nowrap="nowrap">&nbsp;<s:property value="code" /></td>
						<td nowrap="nowrap">&nbsp;<s:property value="order1" /></td>
					</s:if>
					<td>&nbsp;<s:property value="updatetime" /></td>
					<td>&nbsp;
						<s:if test="status.equals(\"y\")">
							<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
						</s:if>
						<s:else>
							<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
						</s:else>
					</td>
					<td>
						<s:a href="news!toEdit.action?e.id=%{id}">编辑</s:a>|
						<s:if test="type.equals(\"help\")">
							<a target="_blank" href="<%=SystemManager.systemSetting.getWww()%>/help/<s:property value="code" />.html" >查看</a>
						</s:if>
						<s:else>
							<a target="_blank" href="<%=SystemManager.systemSetting.getWww()%>/news/<s:property value="id" />.html" >查看</a>
						</s:else>
					</td>
				</tr>
			</s:iterator>

			<tr>
				<td colspan="17" style="text-align: center;font-size: 12px;"><%@ include
						file="/manage/system/pager.jsp"%></td>
			</tr>
		</table>
		
		<div class="alert alert-info" style="text-align: left;font-size: 14px;margin: 2px 0px;">
			图标含义：<BR>
			<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">：显示到门户上
			<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">：不显示到门户上
		</div>

	</s:form>
	

<SCRIPT type="text/javascript">
$(function(){
	selectDefaultCatalog();
});
function selectDefaultCatalog(){
	var _catalogID = $("#catalogID").val();
	console.log("_catalogID="+_catalogID);
	if(_catalogID!='' && _catalogID>0){
		$("#catalogSelect").attr("value",_catalogID);
	}
}
</SCRIPT>
</body>
</html>
