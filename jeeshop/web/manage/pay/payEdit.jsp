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
	<s:form action="pay" namespace="/manage" theme="simple">
		<table class="table table-bordered">
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>支付方式编辑</strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">支付方式</td>
				<td style="text-align: left;">
					<s:select list="#{'alipay':'支付宝'}" name="e.code"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">状态</td>
				<td style="text-align: left;">
<%-- 					<s:select list="#{'n':'禁用','y':'启用'}" name="e.status"/> --%>
					<s:select list="#{'y':'启用'}" name="e.status"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">卖家账号</td>
				<td style="text-align: left;"><s:textfield name="e.seller" data-rule="卖家账号:required;seller;length[1~45];"  
						id="seller" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">合作身份者ID</td>
				<td style="text-align: left;"><s:textfield name="e.partner" data-rule="合作身份者ID:required;partner;length[1~45];"  
						id="partner" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">商户的私钥</td>
				<td style="text-align: left;"><s:textfield name="e.key1" data-rule="商户的私钥:required;key1;length[1~45];"  
						id="key1" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">排序</td>
				<td style="text-align: left;"><s:textfield name="e.order1" data-rule="排序:integer;order1;"   
						id="order1" /></td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;"><s:if test="e.id=='' or e.id==null">
						<button method="pay!insert.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 新增
						</button>
					</s:if> 
					<s:else>
						<button method="pay!update.action" class="btn btn-success">
							<i class="icon-ok icon-white"></i> 保存
						</button>
					</s:else>
				</td>
			</tr>
		</table>
	</s:form>
</body>
</html>
