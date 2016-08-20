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
<s:form action="account!updateFreeze.action" namespace="/manage" theme="simple" id="form">
	<table class="table table-bordered">
			<tr>
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>冻结会员登陆账号</strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" id="id"/></td>
			</tr>
			<tr>
				<td style="text-align: right;">昵称</td>
				<td style="text-align: left;"><s:property value="e.nickname"/></td>
			</tr>
			<tr>
				<td style="text-align: right;">账号</td>   
				<td style="text-align: left;"><s:property value="e.account"/></td>
			</tr>
			<tr>
				<td style="text-align: right;">是否冻结</td>
				<td style="text-align: left;">
					<s:select list="#{'y':'是','n':'否'}" id="freeze" name="e.freeze"
						listKey="key" listValue="value"  />
				</td>
			</tr>
			<tr>
				<td width="200px" style="text-align: right;">
					冻结时间范围
				</td>
				<td>
					<input id="d4311" class="Wdate search-query input-small" type="text" name="e.freezeStartdate"
					value="<s:property value="e.freezeStartdate" />"
					onFocus="WdatePicker({maxDate:'#F{$dp.$D(\'d4312\')||\'2020-10-01\'}'})"/>
					~ 
					<input id="d4312" class="Wdate search-query input-small" type="text" name="e.freezeEnddate" 
					value="<s:property value="e.freezeEnddate" />"
					onFocus="WdatePicker({minDate:'#F{$dp.$D(\'d4311\')}',maxDate:'2020-10-01'})"/>
					
					(注：不填写时间范围将永久冻结此账号！)
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;">
<%-- 					<s:submit method="updateFreeze" value="提交" cssClass="btn btn-primary"/> --%>
<%-- 					<s:a method="updateFreeze" cssClass="btn btn-success"> --%>
<!-- 						<i class="icon-ok icon-white"></i> 保存 -->
<%-- 					</s:a> --%>
					<button method="account!updateFreeze.action" class="btn btn-success" onclick="submitNotValid2222(this)">
						<i class="icon-ok icon-white"></i> 保存
					</button>
<%-- 					<s:submit method="back" value="返回" cssClass="btn btn-inverse"/> --%>
				</td>
			</tr>
		</table>
</s:form>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
</body>
</html>
