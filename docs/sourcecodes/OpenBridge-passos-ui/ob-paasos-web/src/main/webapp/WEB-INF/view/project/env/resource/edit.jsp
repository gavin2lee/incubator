<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="title">
		申请资源
	</template:replace>
	<template:replace name="body">
		<style type="text/css">
			body {
				overflow: auto;
			}
			.title01 {
			    font-size: 20px;
			    padding: 8px;
			}
			.form-group {
				
			}
			.form-group label.col-sm-3 {
			 	float: left;
			 	padding-left: 10px;
			 	vertical-align: top;
    			line-height: 35px;
			}
			.form-group div.col-sm-9 {
			 	float: left;
			 	padding-left: 10px;
			 	vertical-align: top;
    			line-height: 35px;
			}
			.table {
				width: 90%;
			}
			.table th,.table td{
				padding: 8px;
				border:1px solid #f1f1f1;
			}
			.table th,.table td input{
				width: 200px;
			}
			.centerAlign {
				text-align: center;
			}
		</style>
		<div class="row">
			<div class="col-xs-12">
			 	<div class="page-header">
					<h4 class="pull-left title01">
						<c:if test="${ env.envType eq 'dev' }">
							开发环境
						</c:if> 
						<c:if test="${ env.envType eq 'test' }">
							测试环境
						</c:if> 
						<c:if test="${ env.envType eq 'live' }">
							生产环境
						</c:if>
						>> 申请资源 >>
						<small> 
							${ resource.name }
						</small>
					</h4>
				</div>
			 
				<div class="clearfix"></div>
				<form class="form-horizontal" style="margin-top:20px;" role="form" id="bindForm"
					action="${ WEB_APP_PATH }/app/env/resource/update.do?appId=${app.appId}&resId=${ resource.id }&recordId=${envRes.recordId}&envId=${env.envId}"
					method="post">
					<c:set scope="request" var="resourceConfig" value="${ envRes.config }"></c:set> 
					<c:import url="${ resource.config }?scene=edit"></c:import> 
					
					<div class="clearfix"></div>
					<div class="form-actions" style="margin-top: 30px;">
						<div class="col-md-offset-3 col-md-9" style="padding-left:5px;">
							<button type="button" onclick="bindResource();" class="btn btn-info" type="button" id="saveServiceDefineBtn">
								<i class="ace-icon fa fa-check bigger-110"></i> 保存
							</button>
							&nbsp; &nbsp; &nbsp;
							<!--设置按钮类型，防止错误触发表单提交事件  -->
							<button class="btn" type="button" onclick="common.goto('${WEB_APP_PATH}/app/env/resource/add.do?appId=${app.appId}&envId=${env.envId}')">
								<i class="ace-icon fa fa-undo bigger-110"></i> 返回
							</button>
						</div>
					</div>
				</form>
			 	<br>
			 </div>
		</div>
	</template:replace>
	<template:replace name="bottom">
		<script>
			function bindResource(serviceId){
				var url = $("#bindForm").attr("action");
				var data = $("#bindForm").serialize();
		    	var loading = top.common.loading(); 
				$.getJSON(url,data,function(json){
	    			loading.close();
					if(json.code == 0){
						top.common.tips("修改资源配置",1,function(){
		    				frameElement.callback();
		    			});
					} else{
						top.common.alert(json.msg);
					}
				});
			}
		</script>
	</template:replace>
</template:include>