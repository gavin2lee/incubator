<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<script type="text/javascript">
	$(function() {
		$("#title").focus();
	});

	function onSubmit() {
		return true;
	}
</script>
</head>

<body>
	<s:form action="commentType" namespace="/manage" theme="simple" onsubmit="return onSubmit();">
		<table class="table table-bordered">
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>评论插件编辑</strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" /></td>
			</tr>
			<tr>
				<td style="text-align: right;">选择插件</td>
				<td style="text-align: left;">
					<s:select list="#{'default':'系统默认','duoshuo':'多说评论插件'}" name="e.code"/>
				</td>
			</tr>
			<tr>
				<td colspan="2" style="text-align: center;"><s:if test="e.id=='' or e.id==null">
						<s:submit method="insert" value="新增" cssClass="btn btn-primary"/>
					</s:if> 
					<s:else>
						<s:submit method="update" 
							value="保存" cssClass="btn btn-primary"/>
					</s:else>
					<s:submit
						method="back" value="返回" cssClass="btn btn-inverse"/></td>
			</tr>
		</table>
	</s:form>
</body>
</html>
