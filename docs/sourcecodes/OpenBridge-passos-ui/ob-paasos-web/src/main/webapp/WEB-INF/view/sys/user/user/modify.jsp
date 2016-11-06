<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		PaaSOS--用户修改
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
		<form action="${ WEB_APP_PATH }/sys/user/save.do" method="post" id="saveUser">
			<input type="hidden" id="userId" value="${user.userId}">
			<input type="hidden" id="originDepId" value="${userDepartment}"/>
			<div class="profile-activity2 clearfix">
			
				<div class="row" style="margin-top: 8px;">
					<div ><label for="userName">用户名称：</label></div>
					<div><input type="text" id="userName" name="userName" value="${user.userName}" />
					<font style="color: red;">*</font></div>
				</div>
				<div class="row" style="margin-top: 8px;">
					<div ><label for="loginName">登录帐号：</label></div>
					<div ><input type="text" id="loginName" name="loginName" value="${user.loginName}" />
					<font style="color: red;">*</font></div>
				</div>
				<div class="row" style="margin-top: 8px;">
					<div ><label>邮箱：</label></div>
					<div ><input type="text" id="email" name="email" value="${user.email}" />
					<font style="color: red;">*</font></div>
				</div>
				<div class="row" style="margin-top: 8px;">
					<div ><label>手机：</label></div>
					<div ><input type="text" id="mobile" name="mobile" value="${user.mobile}" />
				</div>
				<div class="row" style="margin-top: 8px;">
					<div><label>是否启用：</label></div>
					<div>
						<div id="checkActivate">
	                    	<input type="hidden" id="hiddenActivate" value="${user.activate}">
	                       	<c:choose>
	                           	<c:when test="${user.activate}">
	                            	<input type="radio" name="active" value="true" checked="checked" />是 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                               	<input type="radio" name="active" value="false"/>否
	                            </c:when>
	                            <c:otherwise>
	                            	<input type="radio" name="active" value="true" />是&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
	                               	<input type="radio" name="active" value="false" checked="checked" />否
	                            </c:otherwise>
	                    	</c:choose>
						</div>
					</div>
				</div>
				<div class="row" style="margin-top: 8px;">
					<div style="text-align:center; " >
						<button class="btn btn-default btn-yellow f16  mt10"  type="button" id="saveUserUpdateBtn">
						 保存
						</button>
					</div>
				</div>
				
			</div>
				<br>
		</form> 
		<script type="text/javascript">
			$(function(){
  				$("input[type='radio']").click(function(){
     				$(this).attr('checked','checked');
     				$("#hiddenActivate").attr('value',$(this).val());
  				});
			});

		
			$(function() {
				$("#saveUserUpdateBtn").bind('click',
					function() {
						if(!$.trim($("#userName").val())){
							common.tips('请输入用户名称');
							return false;
						}
						if(!$.trim($("#loginName").val())){
							common.tips('请输入登录名');
							return false;
						}

						//用于验证手机号
						var mobileFileter = /^((0\d{2,3}-\d{7,8})|(1[3584]\d{9}))$/;
						var myMobile = $.trim($('#mobile').val());
						if(myMobile&&(!mobileFileter.test(myMobile))){
							common.tips('手机号格式不正确！');
							return false;
						}

						
						var emailfilter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
                		if(!emailfilter.test($("#email").val())){
                			common.tips('邮件格式不正确!');
							return false;
                		}
						
						var roles="";
						var obj = $("#saveUser input[name=checkBox]");    
			            for(var i=0;i<obj.length;i++){
			                 if(obj[i].checked) //取到对象数组后，我们来循环检测它是不是被选中
			                 	roles+=obj[i].value+',';   //如果选中，将value添加到变量s中    
			            }
						if (roles.length>0){
							roles = roles.substring(0,roles.length-1);
						}
						var data = {};
						data.userId =$("#userId").val();
						data.userName =$.trim($("#userName").val());
						data.loginName=$.trim($("#loginName").val());
						data.mobile=$.trim($("#mobile").val());
						data.roles =roles;
						data.originDepId= $("#originDepId").val();
						data.department ="";
						data.email=$.trim($("#email").val());
						data.activate=$("#hiddenActivate").val();
						var url = $("#saveUser").attr("action");
						var load = common.loading();
						$.getJSON(url,data,function(json){
							if(json.code == 0){
								common.tips("保存成功",1,function(){
									frameElement.callback();
								});
							}else{
								load.close();
				    			common.tips(json.msg);
							}
						});
					});
			});
		</script>
		<Br><Br><Br><Br><Br><Br><Br><Br>
		</div>
		
	</template:replace>
</template:include>