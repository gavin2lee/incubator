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
	<s:form action="navigation" namespace="/manage" theme="simple" name="form" id="form">
		<table class="table table-bordered">
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>导航菜单编辑</strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">名称</td>
				<td style="text-align: left;"><s:textfield name="e.name" data-rule="名称:required;name;length[1~45];" 
						id="name" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">打开方式</td>
				<td style="text-align: left;">_blank
					
					<!-- 
					<s:select disabled="disabled" list="#{'_blank':'_blank','_self':'_self'}" 
					id="target" name="e.target"  cssClass="input-medium" 
						listKey="key" listValue="value"  />
					 -->
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">位置</td>
				<td style="text-align: left;">bottom
					<!-- 
					<s:select disabled="disabled" list="#{'top':'top','center':'center','bottom':'bottom'}" 
					id="position" name="e.position"  cssClass="input-medium" 
						listKey="key" listValue="value"  />
					 -->
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">顺序</td>
				<td style="text-align: left;"><s:textfield name="e.order1" data-rule="顺序:integer;order1;length[1~5];"   
						id="order1" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">地址</td>
				<td style="text-align: left;"><s:textfield name="e.http" data-rule="名称:required;http;length[1~70];"  
						id="http" /><br>
					(输入的地址不带http://)
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
					<s:if test="e.id=='' or e.id==null">
<%-- 						<s:submit method="insert" value="新增" cssClass="btn btn-primary"/> --%>
<%-- 						<s:a method="insert" cssClass="btn btn-success" onclick="doSubmitFunc(this)"> --%>
<!-- 							<i class="icon-plus-sign icon-white"></i> 新增 -->
<%-- 						</s:a> --%>
						<button method="navigation!insert.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 新增
						</button>
					</s:if> 
					<s:else>
<%-- 						<s:submit method="update" value="保存" cssClass="btn btn-primary"/> --%>
<%-- 						<s:a method="update" cssClass="btn btn-success" onclick="doSubmitFunc(this)"> --%>
<!-- 							<i class="icon-ok icon-white"></i> 保存 -->
<%-- 						</s:a> --%>
						<button method="navigation!update.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 保存
						</button>
					</s:else>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>
