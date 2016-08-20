<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<style>
	.td_right{text-align: right;}
</style>
<script type="text/javascript">
	$(function() {
		 $("#username").focus();
	});
</script>
</head>

<body>
<s:if test="e.id=='' or e.id==null">
	<s:set name="formAction" value="'insert'"/>
</s:if>
<s:else>
	<s:set name="formAction" value="111"/>
</s:else>

<%-- formAction=<s:property value="#formAction"/><br> --%>
<%-- formAction2=<s:property value="#request.formAction"/><br> --%>
	<s:form action="user!" namespace="/" theme="simple" id="form">
		<table class="table table-bordered">
			<tr>
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>帐号编辑</strong>
				</td>
			</tr>
			<tr style="display:none;">
				<th>id</th>
				<td><s:hidden name="e.id" /></td>
			</tr>
			<tr>
				<th class="td_right">帐号</th>
				<td style="text-align: left;">
					<s:if test="e.id=='' or e.id==null">
						<s:textfield name="e.username" id="username" readonly="false" data-rule="帐号:required;username;length[4~20];remote[user!unique.action]"/>
					</s:if>
					<s:else>
						<s:property value="e.username"/>
					</s:else>
				</td>
			</tr>
			<tr>
				<th class="td_right">昵称</th>
				<td style="text-align: left;">
				<s:textfield name="e.nickname" id="nickname" readonly="false" data-rule="昵称:required;nickname;length[2~20];remote[user!unique.action]"/>
					</td>
			</tr>
			
			<s:if test="e.id=='' or e.id==null">
				<tr>
					<th class="td_right">密码</th>
					<td style="text-align: left;"><s:password name="e.password" data-rule="密码:required;password;length[6~20];"
							id="password" />
					</td>
				</tr>
				<tr>
					<th class="td_right">确认密码</th>
					<td style="text-align: left;"><s:password name="e.newpassword2" data-rule="确认密码:required;match(e.password)"
							id="newpassword2" />
					</td>
				</tr>
			</s:if>
			<s:else>
				<tr>
					<th class="td_right">密码</th>
					<td style="text-align: left;"><s:password name="e.password" data-rule="密码:password;length[6~20];" 
							id="password" />
							<br>(不输入表示不修改密码)
					</td>
				</tr>
				<tr>
					<th class="td_right">确认密码</th>
					<td style="text-align: left;"><s:password name="e.newpassword2" data-rule="确认密码:match(e.password)" 
							id="newpassword2" />
					</td>
				</tr>
			</s:else>
			
			
			<tr>
				<th class="td_right">选择角色</th>
				<td style="text-align: left;">
					<s:select name="rid" value="e.rid" list="roleList" id="rid" 
						listKey="id" listValue="role_name"></s:select>
				</td>
			</tr>
			<s:if test="e.username==null or e.username==''  or !e.username.equals(\"admin\")">
				<tr>
					<th class="td_right">状态</th>
					<td style="text-align: left;" >
						<s:select list="#{'y':'启用','n':'禁用'}" id="status" name="e.status"  cssClass="input-small" 
							listKey="key" listValue="value"  />
					</td>
				</tr>
			</s:if>
			<tr >
				<td colspan="2" style="text-align: center;">
					<s:if test="e.id=='' or e.id==null">
<%-- 						<s:submit method="insert" value="新增" cssClass="btn btn-primary" /> --%>
						<button method="user!insert.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 新增
						</button>
<!-- 						<button class="btn btn-success" type="submit" > -->
<!-- 							<i class="icon-plus-sign icon-white"></i> 新增 -->
<!-- 						</button> -->
<%-- 						<s:a method="insert" cssClass="btn btn-success"> --%>
<!-- 							<i class="icon-plus-sign icon-white"></i> 新增 -->
<%-- 						</s:a> --%>
<!-- 						<a href="javascript:void(0)" onclick="$('#form').submit()" class="btn btn-success"> -->
<!-- 							<i class="icon-plus-sign icon-white"></i> 新增 -->
<!-- 						</a> -->
					</s:if>
					<s:else>
<%-- 						<s:submit method="update" value="修改" cssClass="btn btn-primary"/> --%>
						<button method="user!update.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 保存
						</button>
<%-- 						<s:a  method="update" cssClass="btn btn-success"> --%>
<!-- 							<i class="icon-ok icon-white"></i> 保存 -->
<%-- 						</s:a> --%>
					</s:else>
<%-- 					 <s:submit method="back" value="返回" cssClass="btn btn-inverse"/> --%>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>
