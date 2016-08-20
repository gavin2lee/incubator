<%@ page contentType="text/html; charset=UTF-8"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.util.*"%>
<%@page import="net.jeeshop.services.front.news.bean.News"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%@ taglib uri="http://jsptags.com/tags/navigation/pager" prefix="pg"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<%@ include file="/resource/common_html_meat.jsp"%>
<%@ include file="/resource/common_css.jsp"%>

</head>

<body>
	<%@ include file="/indexMenu.jsp"%>
	<div class="container">
		<div class="row">
			<div class="col-xs-3">
				<%@ include file="userLeft.jsp"%>
			</div>
			
			<div class="col-xs-9">
			
				<s:form role="form" id="form" cssClass="form-horizontal" action="/user/saveSetting.html" theme="simple">   
				  <input type="hidden" name="e.id" value="<s:property value="#session.user_info.id"/>"/>
				  <div class="form-group">
				    <label for="account" class="col-lg-2 control-label">昵称：</label>
				    <div class="col-lg-6">
				    	<label class="radio-inline" style="padding-left: 0px;">
				    		<s:property value="#session.user_info.nickname"/>
				    		<s:if test="#session.user_info.accountType==null">(<s:property value="#session.user_info.account"/>)</s:if>
				    		
				    		<s:if test="#session.user_info.accountType==null"></s:if>
				    		<s:elseif test="#session.user_info.accountType.equals(\"qq\")">(QQ登陆)</s:elseif>
				    		<s:elseif test="#session.user_info.accountType.equals(\"sinawb\")">(新浪微博)</s:elseif>
				    		<s:elseif test="#session.user_info.accountType.equals(\"alipay\")">(支付宝快捷)</s:elseif>
				    	</label>
				    </div>
				  </div>
				  <div class="form-group">
				    <label for="account" class="col-lg-2 control-label">真实姓名：</label>
				    <div class="col-lg-6">
				    	<s:textfield name="e.trueName" type="text" cssClass="form-control" id="trueName" placeholder="请输入真实姓名"/>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label for="account" class="col-lg-2 control-label">性别：</label>
				    <div class="col-lg-8">
				    	<s:radio list="#{'m':'男','f':'女','s':'保密'}" name="e.sex"></s:radio>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label for="account" class="col-lg-2 control-label">生日：</label>
				    <div class="col-lg-6">
				    	<input id="birthday" name="e.birthday" class="Wdate form-control" value="<s:property value="e.birthday"/>" 
				    	type="text" onFocus="WdatePicker({isShowClear:false,readOnly:true})"/>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label for="account" class="col-lg-2 control-label">邮箱：</label>
				    <div class="col-lg-6">
				    	<s:property value="e.email"/>
				    	<a href="<%=request.getContextPath()%>/user/changeEmail.html">修改邮箱</a>
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <label for="account" class="col-lg-2 control-label">所在地：</label>
				    <div class="col-lg-3">
					    	<%
					    	application.setAttribute("_areaMap", SystemManager.areaMap);
					    	%>
					    	<s:select list="#application._areaMap" listKey="key" listValue="value.name" onchange="changeProvince()"
					    	headerKey="" headerValue="--选择省份--" name="e.province" id="province" cssClass="form-control" />
					    </div>
					    <div class="col-lg-3">
					    	<s:if test="e.city==null">
						    	<select class="form-control" id="citySelect" name="e.city">
						    		<option value="">--选择城市--</option>
						    	</select>
					    	</s:if>
					    	<s:else>
					    		<s:iterator value="#application._areaMap" status="i" var="item">
					    			<s:if test="value.code==e.province">
					    				<s:select list="value.children" listKey="code" listValue="name" 
					    				headerKey="" headerValue="--选择城市--" name="e.city" id="citySelect" cssClass="form-control" />
					    			</s:if>
					    		</s:iterator>
					    	</s:else>
					    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="col-lg-offset-2 col-lg-6">
				      <s:submit type="submit" cssClass="btn btn-success btn-sm" value="保存"/>
				    </div>
				  </div>
				</s:form>
				
			</div>
		</div>
	</div>
	
<%@ include file="/foot.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<script type="text/javascript" src="<%=request.getContextPath() %>/resource/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/resource/My97DatePicker/WdatePicker.js"></script>
<script type="text/javascript">
$(function() {
	//$("#birthday").addClass("form-control");
});
function changeProvince(){
	var selectVal = $("#province").val();
	if(!selectVal){
		console.log("return;");
		return;
	}
	var _url = "selectCitysByProvinceCode.html?provinceCode="+selectVal;
	console.log("_url="+_url);
	$("#citySelect").empty().append("<option value=''>--选择城市--</option>");
	$.ajax({
	  type: 'POST',
	  url: _url,
	  data: {},
	  dataType: "json",
	  success: function(data){
		  //console.log("changeProvince success!data = "+data);
		  $.each(data,function(index,value){
			  //console.log("index="+index+",value="+value.code+","+value.name);
			  $("#citySelect").append("<option value='"+value.code+"'>"+value.name+"</option>");
		  });
	  },
	  error:function(er){
		  console.log("changeProvince error!er = "+er);
	  }
	});
}
</script>
</body>
</html>
