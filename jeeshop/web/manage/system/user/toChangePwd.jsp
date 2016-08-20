<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<script type="text/javascript">
	$(function() {
		 $("#password").focus();
	});

	function onSubmit() {
		if ($.trim($("#password").val()) == "") {
			alert("旧密码不能为空!");
			$("#password").focus();
			return false;
		}
		if ($.trim($("#newpassword").val()) == "") {
			alert("密码不能为空!");
			$("#newpassword").focus();
			return false;
		}
		if ($.trim($("#newpassword2").val()) == "") {
			alert("密码不能为空!");
			$("#newpassword2").focus();
			return false;
		}
		if ($.trim($("#newpassword2").val()) != $.trim($("#newpassword").val())) {
			alert("两次输入的密码不一致!");
			return false;
		}
	}
</script>
</head>

<body style="text-align: center;">
	<s:form action="user!updateChangePwd.action" namespace="/" theme="simple">
		<table class="table table-bordered" >
			<tr>
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>修改密码</strong>
				</td>
			</tr>
			<tr style="display:none;">
				<th>id</th>
				<td><s:hidden name="e.id" /></td>
			</tr>
			<tr>
				<th style="text-align: right;">旧密码</th>
				<td style="text-align: left;"><s:password name="e.password" data-rule="旧密码:required;password;length[6~20];remote[user!checkOldPassword.action];" 
						id="password" /></td>
			</tr>
			<tr>
				<th style="text-align: right;">新密码</th>
				<td style="text-align: left;"><s:password name="e.newpassword" data-rule="新密码:required;newpassword;length[6~20];" 
						id="newpassword" /></td>
			</tr>
			<tr>
				<th style="text-align: right;">确认新密码</th>
				<td style="text-align: left;"><s:password name="e.newpassword2" data-rule="确认密码:required;match(e.newpassword)"  
						id="newpassword2" /></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
<%-- 					<s:submit method="updateChangePwd" value="保存" cssClass="btn btn-primary"/> --%>
<%-- 					<s:a method="updateChangePwd" cssClass="btn btn-success"> --%>
<!-- 						<i class="icon-ok icon-white"></i> 保存 -->
<%-- 					</s:a> --%>
					<button method="user!updateChangePwd.action" class="btn btn-success">
						<i class="icon-ok icon-white"></i> 确认修改
					</button>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>
