<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
<template:replace name="title">
		PaaSOS--用户密码修改
	</template:replace>
	<template:replace name="body">
	<style type="text/css">
	.row {
    padding: 10px;
    clear: both;
    }
    .row label {
    width: 100px;
    text-align: left;
    display: inline-block;
    font-size: 14px;
    padding-left: 30px;
    color: #555;
    float: left;
    line-height: 24px;
    margin-top: 1px;
}
	</style>
	<div class="page-body">
	<form action="${ WEB_APP_PATH }/sys/user/AdminPassword.do" method="post" id="editpwdForm">
		<input type="hidden" id="userId" value="${user.userId}">
		<input type="hidden" id="password" value="${user.loginPassword}">
		<div class="row" style="margin-top:6px;margin-bottom=6px;">
			<div >
				<label for="loginPassword"> 新密码： </label>
			</div>
			<div >
				<input type="text" id="loginPassword" name="loginPassword"
					placeholder="请输入新密码" ><font style="color: red;">*</font>
			</div>
			<div class="row" style="margin-top: 8px;">
					
					<div style="text-align:center; ">
						<button class="btn btn-default btn-yellow f16  mt10" type="button" id="btnSave">
							 保存
						</button>
					</div>
				</div>
		</div>		 
	</form> 
		<script>
			$(document.body).css("overflow","hidden");
			function dataValidation(){
				var pwdfilter = /^(\w){6,20}$/;
        		if(!pwdfilter.test($("#loginPassword").val())){
        			common.tips('密码只能为数字、字母和下划线，且长度为6-20个字符!');
					return false;
        		}
        		return true;
			}
			$(function(){
				$('#btnSave').bind('click',function(){
					var url = $("#editpwdForm").attr("action");
			    	var obj ={};
					obj.userId =$("#userId").val();
					obj.originPassword =$("#password").val();
					obj.loginPassword= $("#loginPassword").val();
			    	if(dataValidation()){
				    	var loading = common.loading(); 
				    	$.getJSON(url,obj,function(json){ 
				    		if(json.code==0){
				    			common.tips("保存成功",1,function(){
				    				frameElement.callback();
				    			});
				    		}else{
				    			loading.close();
				    			common.tips(json.msg);
				    		}
				    	});
			    	}
				});
			});
		</script>
		</div>
	</template:replace>
</template:include>