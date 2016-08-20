<%@page import="net.jeeshop.core.ManageContainer"%>
<%@page import="net.jeeshop.core.system.bean.User"%>
<%@page import="net.jeeshop.core.PrivilegeUtil"%>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript">
$(function(){
	$("#result_table tr").mouseover(function(){
		$(this).addClass("over");}).mouseout(function(){
		$(this).removeClass("over");});
	
	//全选、反选
	$("#checkAll").click(function() {
        $('input[type=checkbox]').attr("checked",$(this).attr("checked")?true:false); 
    });
});
function deleteSelect(){
	if($("input:checked").size()==0){
		return false;
	}
	return confirm("确定删除选择的记录?");
}
</script>
</head>

<body>
<s:form action="role" namespace="/manage" method="post" theme="simple">
			<table class="table table-bordered">
				<tr  >
					<td>
						<%if(PrivilegeUtil.check(request.getSession(), "role!selectList.action")){%>
<%-- 							<s:submit method="selectList" value="查询" cssClass="btn btn-primary"/> --%>
							<s:a method="selectList" cssClass="btn btn-primary">
								<i class="icon-search icon-white"></i> 查询
							</s:a>
						<%} %>
						<%if(PrivilegeUtil.check(request.getSession(), "role!insert.action")){%>
<%-- 							<s:submit method="toAdd" value="添加" cssClass="btn btn-success"/> --%>
							<s:a method="toAdd" cssClass="btn btn-success">
								<i class="icon-plus-sign icon-white"></i> 添加
							</s:a>
						<%} %>
						<%if(PrivilegeUtil.check(request.getSession(), "role!deletes.action")){%>
<%-- 							<s:submit method="deletes" onclick="return deleteSelect();" value="删除" cssClass="btn btn-danger"/> --%>
						<%} %>
						
						<div style="float: right;vertical-align: middle;bottom: 0px;top: 10px;">
								<%@ include file="/manage/system/pager.jsp"%>
						</div>
					</td>
				</tr>
			</table>
			
			<table class="table table-bordered table-hover">
				<tr style="background-color: #dff0d8">
					<th width="20">
						<input type="checkbox" id="checkAll"/>
					</th>
					<th  style="display: none;">rid</th>
					<th>角色名称</th>
					<th>角色描述</th>
					<th>数据库权限</th>
					<th>状态</th>
					<th width="50px">操作</th>
				</tr>
				<s:iterator value="pager.list">
					<tr>
						<td><s:if test="id!=1"><input type="checkbox" name="ids" value="<s:property value="id"/>"/></s:if></td>
						<td  style="display: none;">&nbsp;<s:property value="rid"/></td>
						<td>&nbsp;<s:property value="role_name"/></td>
						<td>&nbsp;<s:property value="role_desc"/></td>
						<td>&nbsp;<s:property value="role_dbPrivilege"/></td>
						<td>&nbsp;
							<s:if test="status.equals(\"y\")">
								<img alt="显示" src="<%=request.getContextPath() %>/resource/images/action_check.gif">
							</s:if>
							<s:else>
								<img alt="不显示" src="<%=request.getContextPath() %>/resource/images/action_delete.gif">
							</s:else>
						</td>
						<td>
							<!-- 系统角色只能是超级管理员编辑 -->
<%-- 							<s:if test="id==1"> --%>
								<%
									User user = (User)request.getSession().getAttribute(ManageContainer.manage_session_user_info);
									if(user.getUsername().equals("admin")){
								%>
								<s:a href="role!toEdit.action?id=%{id}">编辑</s:a>
								<%} %>
<%-- 							</s:if> --%>
<%-- 							<s:else> --%>
<%-- 								<s:a href="role!toEdit.action?id=%{id}">编辑</s:a> --%>
<%-- 							</s:else> --%>
						</td>
					</tr>
				</s:iterator>
				<tr>
								<td colspan="15" style="text-align:center;"><%@ include file="/manage/system/pager.jsp"%></td>
							</tr>
			</table>
</s:form>
</body>
</html>
