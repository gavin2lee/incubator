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
	<s:form action="gift" namespace="/manage" method="post" theme="simple" id="form" name="form">
		<s:hidden name="e.type"/>
		<input type="hidden" value="<s:property value="e.catalogID"/>" id="catalogID"/>
		<table class="table table-bordered">
			<tr>
				<td>赠品名称</td>
				<td><s:textfield cssClass="input-medium search-query" name="e.giftName"/></td>
				<td>状态</td>
				<td><s:select cssStyle="width:100px;"
						list="#{'':'','up':'可用','down':'不可用'}"
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
					<button method="gift!selectList.action" class="btn btn-primary" onclick="selectList(this)">
						<i class="icon-search icon-white"></i> 查询
					</button>
						
					<s:a method="toAdd" cssClass="btn btn-success">
						<s:param name="type" value="e.type"></s:param>
						<i class="icon-plus-sign icon-white"></i> 添加
					</s:a>
						
					<button method="gift!deletes.action" class="btn btn-danger" onclick="return submitIDs(this,'确定删除选择的记录?');">
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
				<th width="120px">ID</th>
				<th>赠品名称</th>
				<th>赠品价值</th>
				<th>状态</th>
				<th width="60px;">操作</th>
			</tr>
			
			<s:iterator value="pager.list">
				<tr>
					<td><input type="checkbox" name="ids"
						value="<s:property value="id"/>" /></td>
					<td ><s:property value="id"/></td>
					<td class="aCss">
					  <s:a href="gift!toEdit.action?e.id=%{id}" ><s:property value="giftName"/></s:a>
					</td>
					<td>&nbsp;<s:property value="giftPrice" /></td>
					<td>&nbsp;
						<s:if test="status.equals(\"up\")">
							<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
						</s:if>
						<s:elseif test="status.equals(\"down\")">
							<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
						</s:elseif>
						<s:else>
							未知
						</s:else>
					</td>
					<td>
						<s:a href="gift!toEdit.action?e.id=%{id}">编辑</s:a>
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
