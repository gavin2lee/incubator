<%@page import="net.jeeshop.services.front.catalog.bean.Catalog"%>
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
</head>

<body>
	<%@ include file="/indexMenu.jsp"%>
	<div class="container" style="margin-top: 0px;padding-top: 0px;">
		<div class="row">
			<div class="col-xs-3" style="min-height: 300px;">
				<%@ include file="userLeft.jsp"%>
			</div>
			
			<div class="col-xs-9" style="min-height: 200px;">
				<div class="row">
					<s:form role="form" id="form" cssClass="form-horizontal" action="/user/saveAddress.html" theme="simple">   
					  <s:hidden name="address.id"/>
					  <div class="form-group">
					    <label for="name" class="col-lg-2 control-label">收货人姓名</label>
					    <div class="col-lg-6">
					    	<s:textfield name="address.name" type="text" 
						    cssClass="form-control" id="name" data-rule="收货人姓名:required;length[2~8];name;" placeholder="请输入收货人姓名" maxlength="8" size="8"/>
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="name" class="col-lg-2 control-label">地址区域</label>
					    <div class="col-lg-3">
					    	<%
					    	application.setAttribute("_areaMap", SystemManager.areaMap);
					    	%>
					    	<s:select list="#application._areaMap" listKey="key" listValue="value.name" onchange="changeProvince()"
					    	headerKey="" headerValue="--选择省份--" name="address.province" id="province" cssClass="form-control" />
					    </div>
					    <div class="col-lg-3">
					    	<s:if test="address.city==null">
						    	<select class="form-control" id="citySelect" name="address.city" style="display:none" onchange="changeCity()">
						    		<option value="">--选择城市--</option>
						    	</select>
					    	</s:if>
					    	<s:else>
<%-- 					    		address.city=<s:property value="address.city"/> --%>
					    		<s:iterator value="#application._areaMap" status="i" var="item">
<%-- 					    			code=<s:property value="value.code"/> --%>
					    			<s:if test="value.code==address.province">
					    				<s:if test="address.id==null">
						    				<s:select list="value.children" listKey="code" listValue="name" cssStyle="display:none" onchange="changeCity()" 
						    				headerKey="" headerValue="--选择城市--" name="address.city" id="citySelect" cssClass="form-control" />
					    				</s:if>
					    				<s:else>
					    					<s:select list="value.children" listKey="code" listValue="name" onchange="changeCity()" 
						    				headerKey="" headerValue="--选择城市--" name="address.city" id="citySelect" cssClass="form-control" />
					    				</s:else>
					    			</s:if>
					    		</s:iterator>
					    	</s:else>
					    </div>
					    
					    <div class="col-lg-3">
<%-- 					    	address.areaList=<s:property value="address.areaList"/> --%>
					    	<s:if test="address.area==null">
						    	<select class="form-control" id="areaSelect" name="address.area" style="display:none">
						    		<option value="">--选择区域--</option>
						    	</select>
					    	</s:if>
					    	<s:else>
					    		<s:select list="address.areaList" listKey="code" listValue="name" 
					    				headerKey="" headerValue="--选择区域--" name="address.area" id="areaSelect" cssClass="form-control" />
					    	</s:else>
					    </div>
					    
					  </div>
					  <div class="form-group">
					    <label for="address" class="col-lg-2 control-label">地址</label>
					    <div class="col-lg-6">
					    	<s:textfield name="address.address" type="text" 
						    cssClass="form-control" id="address" data-rule="地址:required;length[0~70];address;" placeholder="请输入收货人地址" maxlength="70" size="70"/>
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="zip" class="col-lg-2 control-label">邮编</label>
					    <div class="col-lg-6">
					    	<s:textfield name="address.zip" type="text" 
						    cssClass="form-control" id="zip" data-rule="邮编:required;length[6];zip;" placeholder="请输入收货人邮编" size="6" maxlength="6"/>
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="mobile" class="col-lg-2 control-label">手机</label>
					    <div class="col-lg-6">
					    	<s:textfield name="address.mobile" type="text" 
						    cssClass="form-control" id="mobile" data-rule="手机:required;length[10~15];mobile;" placeholder="请输入收货人手机" maxlength="15"/>
					    </div>
					  </div>
					  <div class="form-group">
					    <label for="phone" class="col-lg-2 control-label">电话号码</label>
					    <div class="col-lg-6">
					    	<s:textfield name="address.phone" type="text" 
						    cssClass="form-control" id="phone" data-rule="电话号码:required;length[0~15];phone;" placeholder="请输入收货人座机号码" maxlength="15"/>
					    </div>
					  </div>
					  <div class="form-group">
					    <div class="col-lg-offset-2 col-lg-6">
					      <button type="submit" class="btn btn-success btn-sm" value="保存">
					      	 <span class="glyphicon glyphicon-ok"></span>&nbsp;保存
					      </button>
					    </div>
					  </div>
					</s:form>
				
					<s:if test="addressList!=null and addressList.size!=0">
						<table class="table table-bordered table-hover" style="margin-bottom: 10px;">
							<tr style="background-color: #dff0d8">
								<th width="20px" style="display: none;"><input type="checkbox" id="firstCheckbox" /></th>
								<th nowrap="nowrap" style="text-align: center;">收货人</th>
								<th style="text-align: left;">所在区域</th>
								<th style="text-align: left;">街道地址</th>
								<th style="text-align: center;">邮编</th>
								<th style="text-align: center;">电话号码</th>
								<th style="text-align: center;">手机号</th>
								<th nowrap="nowrap" style="text-align: center;">设为默认</th>
								<th style="text-align: center;">操作</th>
							</tr>
							<s:iterator value="addressList" status="i" var="item">
								<tr>
									<td style="display: none;"><s:property value="id"/></td>
									<td style="text-align: center;"><s:property value="name"/></td>
									<td style="text-align: left;"><s:property value="pcadetail"/></td>
									<td style="text-align: left;"><s:property value="address"/></td>
									<td style="text-align: center;"><s:property value="zip"/></td>
									<td style="text-align: center;"><s:property value="phone"/></td>
									<td style="text-align: center;"><s:property value="mobile"/></td>
									<td nowrap="nowrap" style="text-align: center;">
										<s:if test="isdefault!=null and isdefault.equals(\"y\")">
											<input type="radio" name="setDefaultRadio" value="<s:property value="id"/>" checked="checked"/>
										</s:if>
										<s:else>
											<input type="radio" name="setDefaultRadio" value="<s:property value="id"/>"/>
										</s:else>
									</td>
									<td style="text-align: center;" nowrap="nowrap">
										<a href="<%=request.getContextPath() %>/user/editAddress.html?id=<s:property value="id" />">
											修改
										</a>|
										<a onclick="return deletes();" href="<%=request.getContextPath() %>/user/deleteAddress.html?id=<s:property value="id"/>">
											删除
										</a>
									</td>
								</tr>
							</s:iterator>
						</table>
					</s:if>
					<s:else>
						<!-- 
						<div class="bs-callout bs-callout-danger author" style="text-align: left;font-size: 14px;margin: 2px 0px;">
							还没有任何配送信息！赶紧添加吧。
						</div>
						 -->
						
						<div class="col-xs-12">
							<hr>
							<div class="row">
								<div class="col-xs-12" style="font-size: 14px;font-weight: normal;">
									<div class="panel panel-default">
							              <div class="panel-body" style="font-size: 16px;font-weight: normal;text-align: center;">
								              <div class="panel-body" style="font-size: 16px;font-weight: normal;text-align: center;">
								              		<span class="glyphicon glyphicon-user"></span>亲，还没有任何配送信息哦！赶紧添加吧。
								              </div>
							              </div>
									</div>
									<hr>
								</div>
							</div>
							
						</div>
						
					</s:else>
					
				</div>
			</div>
		</div>
	</div>
	
<%@ include file="/foot.jsp"%>
<%@ include file="/resource/common_html_validator.jsp"%>
<script type="text/javascript">
$(function() {
	$("input[name=setDefaultRadio]").click(function(){
		var _url = "user/setAddressDefault.html?id="+$(this).val();
		//alert(_url);
		$.ajax({
		  type: 'POST',
		  url: _url,
		  data: {},
		  success: function(data){
			  alert("修改默认地址成功！");
		  },
		  dataType: "json",
		  error:function(){
			alert("操作失败，请联系管理员或更换浏览器再试!");				  
		  }
		});
	});
});
function search(){
	var _key = $.trim($("#key").val());
	if(_key==''){
		return false;
	}
	$("#searchForm").submit();
}
function deletes(){
	return confirm("确定删除选择的记录?");
}
function changeProvince(){
	var selectVal = $("#province").val();
	if(!selectVal){
		console.log("return;");
		return;
	}
	var _url = "selectCitysByProvinceCode.html?provinceCode="+selectVal;
	console.log("_url="+_url);
	$("#citySelect").empty().show().append("<option value=''>--选择城市--</option>");
	$("#areaSelect").empty().hide().append("<option value=''>--选择区域--</option>");
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

function changeCity(){
	var selectProvinceVal = $("#province").val();
	var selectCityVal = $("#citySelect").val();
	if(!selectProvinceVal || !selectCityVal){
		console.log("return;");
		return;
	}
	var _url = "selectAreaListByCityCode.html?provinceCode="+selectProvinceVal+"&cityCode="+selectCityVal;
	console.log("_url="+_url);
	$("#areaSelect").empty().show().append("<option value=''>--选择区域--</option>");
	$.ajax({
	  type: 'POST',
	  url: _url,
	  data: {},
	  dataType: "json",
	  success: function(data){
		  //console.log("changeProvince success!data = "+data);
		  $.each(data,function(index,value){
			  //console.log("index="+index+",value="+value.code+","+value.name);
			  $("#areaSelect").append("<option value='"+value.code+"'>"+value.name+"</option>");
		  });
	  },
	  error:function(er){
		  console.log("changeCity error!er = "+er);
	  }
	});
}
</script>
</body>
</html>
