<%@page import="net.jeeshop.services.manage.order.bean.Order"%>
<%@page import="net.jeeshop.core.KeyValueHelper"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ page session="false"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%-- <%@ include file="/manage/system/common.jsp"%> --%>

<link rel="stylesheet" href="<%=request.getContextPath()%>/resource/bootstrap/css/bootstrap.min.css"  type="text/css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/resource/css/base.css"  type="text/css">


<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/bootstrap/js/bootstrap.min.js"></script>

<%-- <script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/manage.js"></script> --%>

<%@ include file="/resource/common_html_validator.jsp"%>
<link rel="stylesheet" href="<%=request.getContextPath() %>/resource/jquery-jquery-ui/themes/base/jquery.ui.all.css">
</head>
<body>
	<s:form id="form" name="form" method="post" theme="simple" >
		<input type="hidden" name="WIDtrade_no" value="<s:property value="e.tradeNo"/>"/>
		<input type="hidden" id="orderid" name="orderid" value="<s:property value="e.id"/>"/>
		<div class="alert alert-info" style="margin-bottom: 2px;text-align: left;">
			<strong>确认发货：点击【确认发货】按钮后，信息将请求到支付宝发货接口，并且同一时间本系统会通过短消息告知到客户货物已发送。</strong>
		</div>
		<table class="table table-bordered">
			<tr>
				<th nowrap="nowrap" width="150px">物流方式</th>
				<td nowrap="nowrap">
					<div class="alert alert-info" id="WIDtransport_type_msg">
						发货10天后，若对方没有确认收货，交易结束，金额自动进入您的账户。
					</div>
					<s:select list="#{'EXPRESS':'快递','POST':'平邮','no':'不需要物流 '}" name="WIDtransport_type" id="WIDtransport_type" 
					onchange="changeFunc(this)"/>
				</td>
			</tr>
			<tr id="companyTR">
				<th nowrap="nowrap">物流公司名称</th>
				<th nowrap="nowrap">
					<div style="display: none;" id="youzhengDIV">中国邮政</div>
					<%
					application.setAttribute("_expressMap", SystemManager.manageExpressMap);
					%>
					<s:select list="#application._expressMap" listKey="key" listValue="value"
					name="expressCompanyName" id="expressCompanyName"/>
					<!-- ,'华强物流':'华强物流','其它':'其它' -->
				</th>
			</tr>
			<tr>
				<th nowrap="nowrap">运单号</th>
				<th nowrap="nowrap">
					<s:textfield name="WIDinvoice_no" id="WIDinvoice_no" data-rule="运单号:required;WIDinvoice_no;length[1~20];" maxlength="20" size="20"/>
				</th>
			</tr>
			<tr>
				<th nowrap="nowrap">确认发货备注</th>
				<th nowrap="nowrap">
					<s:textfield name="confirmSendProductRemark" id="confirmSendProductRemark" 
					data-rule="确认发货备注:confirmSendProductRemark;length[1~50];" maxlength="50" size="50" cssStyle="width:60%;"/>
				</th>
			</tr>
			<tr>
				<th nowrap="nowrap" colspan="2" style="text-align: center;">
<%-- 					<s:submit value="确认发货" cssClass="btn btn-primary" /> --%>

					<div class="alert alert-info">
						如遇收货地址不符发生争议时，支付宝一律以买家在支付宝交易中填写的收货地址为准。
					</div>
					<input type="button" class="btn btn-primary" value="确认发货" onclick="return sendProduct()"/>
<!-- 					<input type="submit" class="btn btn-primary" value="确认发货" onclick="javascript:return confirm('发货操作确认？');"></button> -->
					<input type="button" value="返回" class="btn btn-default btn-sm" onclick="javascript:history.go(-1);"/>
<%-- 					<s:submit method="toEdit" value="返回" cssClass="btn btn-default btn-sm"/> --%>
				</th>
			</tr>
		</table>
	</s:form>
	
	<script type="text/javascript">
	function changeFunc(obj){
		var selectVal = $(obj).val();
		console.log("selectVal="+selectVal);
		$("#companyTR").show();
		if("EXPRESS"==selectVal || "no"==selectVal){
			$("#WIDtransport_type_msg").text("发货10天后，若对方没有确认收货，交易结束，金额自动进入您的账户。");
			if("no"==selectVal){
				$("#companyTR").hide();
			}else{
				$("#expressCompanyName").show();
				$("#youzhengDIV").hide();
			}
		}else if("POST"==selectVal){
			$("#WIDtransport_type_msg").text("发货30天后，若对方没有确认收货，交易结束，金额自动进入您的账户。");
			$("#expressCompanyName").hide();
			$("#youzhengDIV").show();
		}
	}
	
	function sendProduct(){
		//$("form").on("valid.form", function(e, form){
			if($("#WIDinvoice_no").val()==''){
				alert("运单号不能为空！");
				$("#WIDinvoice_no").focus();
				return false;
			}
			
			console.log("验证通过..");
			$.blockUI({ message: "正在请求支付宝，请稍候...",css: { 
	            border: 'none', 
	            padding: '15px', 
	            backgroundColor: '#000', 
	            '-webkit-border-radius': '10px', 
	            '-moz-border-radius': '10px', 
	            opacity: .5, 
	            color: '#fff' 
	        }});
	        
	        var _orderid = $("#orderid").val();
	        var _url = window.location.href ;
	        //alert("_url="+_url);
	        $("#form").action = _url;
	        var cc = $("#form").serialize();
	        console.log("cc="+cc);
	        $.ajax({
	            url: '<%=request.getContextPath()%>/manage/order/notifyAlipaySendProduct.jsp',
	            type: 'post',
	            data: cc,
	            dataType: 'json',
	            //async : false,
	            success: function(d){
	            	console.log("success="+d+",code="+d.code+",error="+d.error);
	            	if(d.code=="0"){
	            		alert("发货成功！");
	            		//页面专项明细页面
	            		window.location.href = "<%=request.getContextPath()%>/manage/order!toEdit.action?e.id="+_orderid;
	            	}else if(d.code=="1"){
	            		var errorStr = d.error;
	            		if(d.error=="TRADE_NOT_EXIST"){
	            			errorStr = "支付宝中不存在此交易号！";
	            		}else{
	            			
	            		}
	            		alert("请求发货失败！原因："+errorStr);
	            		//window.location.href = _url;
	            		//window.location.reload();
	            	}
	            	jQuery.unblockUI();
	            	return false;
	            },
	            error:function(d){
	            	console.log("error="+d+",code="+d.code+",error="+d.error);
	            	alert("发送请求失败！请联系站点管理员！");
	            	jQuery.unblockUI();
	            }
	        });
	        
			//var ccc = 1/0;
	        //return false;
		//});
	}
	
	$(function(){
	});
	</script>
</body>
</html>
