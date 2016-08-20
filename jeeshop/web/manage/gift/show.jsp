<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<style>
#insertOrUpdateMsg{
border: 0px solid #aaa;margin: 0px;position: fixed;top: 0;width: 100%;
background-color: #d1d1d1;display: none;height: 30px;z-index: 9999;font-size: 18px;color: red;
}
.btnCCC{
	background-image: url("../img/glyphicons-halflings-white.png");
	background-position: -288px 0;
}
</style>
</head>

<body>
		<table class="table table-bordered">
			<tr style="background-color: #dff0d8">
				<td colspan="2" style="background-color: #dff0d8;text-align: center;">
					<strong>商品赠品信息 </strong>
				</td>
			</tr>
			<tr style="display: none;">
				<td>id</td>
				<td><s:hidden name="e.id" label="id" /></td>
			</tr>
			<tr>
				<td style="text-align: right;width: 80px;">赠品名称</td>
				<td style="text-align: left;">
					<s:property value="e.giftName" escape="false"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;width: 80px;">赠品价值</td>
				<td style="text-align: left;">
					<s:property value="e.giftPrice" escape="false"/>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;width: 80px;">状态</td>
				<td style="text-align: left;">
					<s:if test="e.status.equals(\"up\")">已上架</s:if>
					<s:elseif test="e.status.equals(\"down\")">已下架</s:elseif>
				</td>
			</tr>
			<tr>
				<td style="text-align: right;">主图</td>   
				<td style="text-align: left;" colspan="3">
					<s:if test="e.picture!=null">
						<a target="_blank" href="<%=SystemManager.systemSetting.getImageRootPath()%><s:property value="e.picture" />">
							<img src="<%=SystemManager.systemSetting.getImageRootPath()%><s:property value="e.picture" />">
						</a>
					</s:if>
				</td>
			</tr>
			
			<s:if test="e.createAccount!=null">
				<tr>
					<td style="text-align: right;">添加</td>
					<td style="text-align: left;">
						添加人：<s:property value="e.createAccount"/><br>
						添加时间：<s:property value="e.createtime"/><br>
					</td>
				</tr>
			</s:if>
			
			<s:if test="e.updateAccount!=null">
				<tr>
					<td style="text-align: right;">最后修改</td>
					<td style="text-align: left;">
						修改人：<s:property value="e.updateAccount"/><br>
						修改时间：<s:property value="e.updatetime"/><br>
					</td>
				</tr>
			</s:if>
		</table>
</body>
</html>
