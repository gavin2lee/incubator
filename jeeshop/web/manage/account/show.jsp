<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/manage/system/common.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jquery-jquery-ui/themes/base/jquery.ui.all.css">
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/kindeditor-4.1.7/themes/default/default.css" />
<%-- <%@ include file="/resource/common_rateit_plug.jsp"%> --%>
</head>

<body>
<s:form action="account" namespace="/manage" theme="simple" id="form">

<div id="tabs">
	<ul>
		<li><a href="#tabs-1">基本信息</a></li>
	</ul>
	<div id="tabs-1">
		<div class="alert alert-info" style="margin-bottom: 2px;text-align: left;">
			<strong>会员信息：</strong>
		</div>
		<table class="table table-bordered">
					<tr style="display: none;">
						<td>id</td>
						<td><s:hidden name="e.id" label="id" id="id"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">会员类型</td>
						<td style="text-align: left;">
							<s:if test="e.accountType.equals(\"qq\")">
								<span class="badge badge-warning"><s:property value="e.accountTypeName"/></span>
								<img alt="" src="<%=request.getContextPath() %>/resource/images/mini_qqLogin.png">
							</s:if>
							<s:elseif test="e.accountType.equals(\"sinawb\")">
								<span class="badge badge-warning"><s:property value="e.accountTypeName"/></span>
								<img alt="" src="<%=request.getContextPath() %>/resource/images/mini_sinaWeibo.png">
							</s:elseif>
							<s:elseif test="e.accountType.equals(\"alipay\")">
								<span class="badge badge-warning">alipay</span>
								<img alt="" src="<%=request.getContextPath() %>/resource/images/alipay_fastlogin.jpg">
							</s:elseif>
							<s:else>
								<span class="badge badge-warning">jeeshop</span>
							</s:else>
						
						</td>
					</tr>
					<tr>
						<td style="text-align: right;width: 200px;">昵称</td>
						<td style="text-align: left;"><s:property value="e.nickname"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">账号</td>   
						<td style="text-align: left;"><s:property value="e.account"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">城市</td>
						<td style="text-align: left;"><s:property value="e.city"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">联系地址</td>
						<td style="text-align: left;"><s:property value="e.address"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">消费额</td>
						<td style="text-align: left;"><s:property value="e.amount"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">电话</td>
						<td style="text-align: left;"><s:property value="e.tel"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">Email地址</td>
						<td style="text-align: left;"><s:property value="e.email"/>
							<s:if test="e.emailIsActive.equals(\"y\")"><span class="badge badge-success">已激活</span></s:if>
							<s:else><span class="badge badge-success">未激活</span></s:else>
						</td>
					</tr>
					<tr>
						<td style="text-align: right;">是否冻结</td>
						<td style="text-align: left;">
							<s:if test="e.freeze.equals(\"y\")">
								<span class="badge badge-important">
									已冻结(
									<s:if test="e.freezeStartdate==null and e.freezeEnddate==null">永久</s:if>
									<s:else>
										<s:property value="e.freezeStartdate"/> ~ <s:property value="e.freezeEnddate"/>
									</s:else>)
								</span>
								
							</s:if>
							<s:else><span class="badge badge-success">未冻结</span></s:else>
						</td>
					</tr>
					
					<tr>
						<td style="text-align: right;">最后登陆时间</td>
						<td style="text-align: left;"><s:property value="e.lastLoginTime"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">最后登陆IP</td>
						<td style="text-align: left;"><s:property value="e.lastLoginIp"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">最后登陆位置</td>
						<td style="text-align: left;"><s:property value="e.lastLoginIp"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">注册日期</td>
						<td style="text-align: left;"><s:property value="e.regeistDate"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">积分</td>
						<td style="text-align: left;"><s:property value="e.score"/></td>
					</tr>
					<tr>
						<td style="text-align: right;">等级</td>
						<td style="text-align: left;">
							
							<s:property value="e.rankName"/>(<s:property value="e.rank"/>)
<!-- 							<div class="rateit"></div> -->
							
<!-- 							<div id="rateit13" class="rateit bigstars" data-rateit-value="4.2" data-rateit-ispreset="true" data-rateit-readonly="false"  -->
<!-- data-rateit-resetable="false" data-rateit-min="1" data-rateit-max="5" -->
<!-- data-rateit-starwidth="32" data-rateit-starheight="32"></div> -->

						</td>
					</tr>
				</table>
	</div>
</div>
</s:form>

<%-- <script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/jquery-1.5.1.js"></script> --%>
<script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/ui/jquery.ui.core.js"></script>
<script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/ui/jquery.ui.widget.js"></script>
<script src="<%=request.getContextPath() %>/resource/jquery-jquery-ui/ui/jquery.ui.tabs.js"></script>

<script type="text/javascript">
$(function() {
	$( "#tabs" ).tabs({
		//event: "mouseover"
	});
	
});

</script>
</body>
</html>
