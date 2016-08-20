<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html class="no-js">
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/resource/common_css.jsp"%>
<style type="text/css">
.centerImageCss{
	width: 560px;
	height: 180px;
}
</style>
<script>
function defaultProductImg(){ 
	var img=event.srcElement; 
	img.src="<%=SystemManager.systemSetting.getDefaultProductImg() %>"; 
	img.onerror=null; //控制不要一直跳动 
}
</script>
</head>

<body>
	<%@ include file="/indexMenu.jsp"%>
	<div class="container">
		<div class="row">
			<div class="col-xs-3">
				<%@ include file="userLeft.jsp"%>
			</div>
			
			<div class="col-xs-9">
				<div class="row">
					<div class="panel panel-default">
						<div class="panel-heading"><b>订单详情</b></div>
						<div class="panel-body" style="line-height: 30px;">
							<table>
								<tr>
									<td align="right">订单编号：</td>
									<td><s:property value="e.id" /></td>
								</tr>
								<tr>
									<td align="right">创建日期：</td>
									<td><s:property value="e.createdate" /></td>
								</tr>
								<tr>
									<td align="right">订单总金额：</td>
									<td style="font-weight: 700;font-size: 16px;color: #f50;"><s:property value="e.amount" /></td>
								</tr>
								<tr>
									<td align="right">配送费：</td>
									<td style="font-weight: 700;font-size: 16px;color: #f50;"><s:property value="e.fee" /></td>
								</tr>
								<tr>
									<td align="right">配送方式：</td>
									<td><s:property value="e.expressName" /></td>
								</tr>
							</table>
						</div>
						<ul class="list-group">
							<li class="list-group-item"><b>配送信息：</b>
								<s:property value="e.ordership.shipname" />,
								<s:property value="e.ordership.shipaddress" />,
								<s:property value="e.ordership.phone" />,
								<s:property value="e.ordership.zip" />
							</li>
							
							<s:if test="status.equals(\"send\") or status.equals(\"sign\")">
								<li class="list-group-item">
									<b>快递信息：</b>
									<a target="_blank" href="http://www.kuaidi100.com/chaxun?com=<s:property value="e.expressCompanyName" />&nu=<s:property value="e.expressNo" />">快递物流</a>
									<div style="display: none;">
										<s:if test="e.kuaid100Info!=null">
											<s:property value="e.kuaid100Info.message" /><br>
											<s:property value="e.kuaid100Info.status" /><br>
											<s:property value="e.kuaid100Info.state" /><br>
											
											<s:if test="e.kuaid100Info.data!=null">
												<s:iterator value="e.kuaid100Info.data">
													<s:property value="time" /><s:property value="context" /><br>
												</s:iterator>
											</s:if>
										</s:if>
										<s:else>
											没有查询到快递信息.
										</s:else>
									</div>
								</li>
							</s:if>
						</ul>
						<table class="table ">
							<tr>
								<th style="text-align: left;">商品</th>
								<th style="text-align: center;" nowrap="nowrap">数量</th>
								<th style="text-align: center;" nowrap="nowrap">单价</th>
							</tr>
							<s:iterator value="e.orders">
								<tr>
									<td>
										<div style="width:50px;height: 50px;border: 0px solid;float: left;margin-left: 20px;">
											<a href="<%=request.getContextPath() %>/product/<s:property value="productID" />.html" target="_blank">
												<img style="width: 100%;height: 100%;border: 0px;" alt="" src="<%=SystemManager.systemSetting.getImageRootPath()%><s:property value="picture" />" onerror="defaultProductImg()"/>
											</a>
										</div>
										<div style="float: left;">&nbsp;<s:property value="productName" /></div>
									</td>
									<td style="text-align: center;">&nbsp;<s:property value="productNumber" /></td>
									<td style="text-align: center;">&nbsp;<s:property value="price" /></td>
								</tr>
							</s:iterator>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	
<%@ include file="/foot.jsp"%>
<script type="text/javascript">
$(function() {
});
</script>
</body>
</html>
