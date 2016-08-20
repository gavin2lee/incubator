<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
</head>

<body>
	<s:form action="keyvalue" namespace="/manage" theme="simple" id="form" name="form">
			<table class="table table-bordered">
				<tr style="background-color: #dff0d8">
					<td colspan="2" style="background-color: #dff0d8;text-align: center;">
						<strong>键值对编辑</strong>
					</td>
				</tr>
				<tr style="display: none;">
					<td>id</td>
					<td><s:hidden name="e.id" label="id" /></td>
				</tr>
				<tr>
					<td style="text-align: right;">键</td>
					<td style="text-align: left;"><s:textfield name="e.key1" data-rule="键:required;key1;length[1~45];"  
							id="key1" /></td>
				</tr>
				<tr>
					<td style="text-align: right;">值</td>
					<td style="text-align: left;"><s:textfield name="e.value" data-rule="值:required;value;length[1~45];"   
							id="value" /></td>
				</tr>
				<tr>
					<td colspan="2" style="text-align: center;"><s:if test="e.id=='' or e.id==null">
<%-- 								<s:submit method="insert" value="新增" cssClass="btn btn-success" /> --%>
							<button method="keyvalue!insert.action" class="btn btn-success" >
								<i class="icon-ok icon-white"></i> 新增
							</button>
						</s:if> 
						<s:else>
<%-- 								<s:submit method="update" value="保存" cssClass="btn btn-success"/> --%>
							<button method="keyvalue!update.action" class="btn btn-success" >
								<i class="icon-ok icon-white"></i> 保存
							</button>
						</s:else>
					</td>
				</tr>
			</table>
	</s:form>
</body>
</html>
