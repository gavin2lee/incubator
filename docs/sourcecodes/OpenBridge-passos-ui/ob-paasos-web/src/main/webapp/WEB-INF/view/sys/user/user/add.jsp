<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
<template:replace name="title">
		PaaSOS--用户新增
	</template:replace>
	<template:replace name="body">
	<style type="text/css">
	.row {
    padding: 10px;
    clear: both;
    }
    .row label {
    width: 140px;
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
	<form action="${ WEB_APP_PATH }/sys/user/addUser.do" method="post" id="addUserForm">
		<div class="row" >
			<div>
				<label for="userName"> 用户名称： </label>
			</div>
			<div >
				<input type="text" id="userName" name="userName"
					placeholder="请输入用户名名称" >
					<font style="color: red;">*</font>
			</div>
		</div>
		<div class="row" >
			<div >
				<label for="loginName"> 登录帐号： </label>
			</div>
			<div >
				<input type="text" id="loginName" name="loginName"
					placeholder="请输入登录名称" ><font style="color: red;">*</font>
			</div>
		</div>
		<div class="row" style="margin-top:6px;margin-bottom=6px;">
			<div>
				<label for="loginPassword"> 登录密码： </label>
			</div>
			<div >
				<input type="text" id="loginPassword" name="loginPassword"
					placeholder="请输入密码" ><font style="color: red;">*</font>
			</div>
		</div>
		<div class="row" style="margin-top:6px;margin-bottom=6px;">
			<div >
				<label for="email"> 邮箱： </label>
			</div>
			<div >
				<input type="text" id="email" name="email"
					placeholder="请输入邮箱" ><font style="color: red;">*</font>
			</div>
		</div>
	    <div class="row" style="margin-top:6px;margin-bottom=6px;">
			<div >
				<label for="mobile"> 手机： </label>
			</div>
			<div >
				<input type="text" id="mobile" name="mobile"
					placeholder="请输入手机号" >
			</div>
		</div>
 		<div class="row" style="margin-top:6px;margin-bottom=6px;">
			<div >
				<label for="activate"> 是否启用： </label>
			</div>
			<div >
				<div>
					<input type="radio" name="activate" value="true" checked="checked"/>&nbsp;&nbsp;启用&nbsp;&nbsp;
	                <input type="radio" name="activate" value="false"/>&nbsp;&nbsp;停用&nbsp;&nbsp;
	                <input type="hidden" id="activate" value="true"/>
				</div>
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
			function dataValidation(){
				if(!$.trim($("#userName").val())){
					common.tips('请输入用户名称');
					return false;
				}
				if($.trim($("#userName").val()).length>30){
        			common.tips('用户名长度不能超过30字符!');
					return false;
        		}
				if(!$.trim($("#loginName").val())){
					common.tips('请输入登录名');
					return false;
				}

				//用于验证手机号
				var mobileFileter = /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
				var myMobile = $('#mobile').val();
				if(myMobile&&(!mobileFileter.test(myMobile))){
					common.tips('手机号格式不正确！');
					return false;
				}
				if($("#loginName").val().length>30){
        			common.tips('登录名长度不能超过30字符!');
					return false;
        		}
				var pwdfilter = /^(\w){6,20}$/;
        		if(!pwdfilter.test($("#loginPassword").val())){
        			common.tips('密码只能为数字、字母和下划线，且长度为6-20个字符!');
					return false;
        		}
				
				var emailfilter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
        		if(!emailfilter.test($("#email").val())){
        			common.tips('邮件格式不正确!');
					return false;
        		}
        		return true;
			}
			$(function(){
				$('#btnSave').bind('click',function(){
					var form = $("#addUserForm");
					var url = $("#addUserForm").attr("action");
			    	var params = form.serialize();
			    	if(dataValidation()){
				    	var loading = common.loading(); 
				    	$.getJSON(url,params,function(json){ 
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