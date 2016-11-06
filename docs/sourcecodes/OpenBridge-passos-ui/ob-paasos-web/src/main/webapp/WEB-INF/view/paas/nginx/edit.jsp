<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../../sys/manager.jsp" active="nginx">
	<template:replace name="title">
		系统参数配置
	</template:replace>
	<template:replace name="detail">
		<style type="text/css">
		.p0 {
			padding-left:0px;
		}
		.lhp label {
			line-height: 25px;
		    padding-top: 5px;
		}
		
		input {
			width: auto;
		}
		</style>
		<form class="form-horizontal" role="form" id="editNginxForm"  action="${WEB_APP_PATH}/paas/nginx/${action}.do" enctype="application/x-www-form-urlencoded" method="post">
		<div class="p20">
		    <div class="r_title">
		        <h3 class="f14 h3_color h3_btm_line blue">
		            <a href="#">
		                <i class="icons add_ico mr5"></i>编辑Nginx资源
		            </a>
		        </h3>
		        <div class="title_line"></div>
		    </div>
	    	<div class="r_con p10_0">
		 		<input name="hostId" value="${sysHost.hostId}" type="hidden" />
		 		<div class="form_block">
					<label class="col-sm-3 control-label no-padding-right"
						for="serviceName">服务器IP</label>
					<div class="col-sm-9">
						<input type="text" id="hostIp" name="hostIp" value="${sysHost.hostIp}"
						placeholder="SSH IP"	class="col-xs-10 col-sm-5">
					</div>
				</div> 
				<div class="form_block">
					<label class="col-sm-3 control-label no-padding-right"
						for="serviceName">SSH端口</label>
					<div class="col-sm-9">
						<input type="text" id="hostPort" name="hostPort" value="${sysHost.hostPort}"
						placeholder="SSH Port"	class="col-xs-10 col-sm-5">
					</div>
				</div> 
				<div class="form_block">
					<label class="col-sm-3 control-label no-padding-right"
						for="serviceName">服务器用户名</label>
					<div class="col-sm-9">
						<input type="text" id="hostUser" name="hostUser" value="${sysHost.hostUser}"
						placeholder="SSH User"	class="col-xs-10 col-sm-5">
					</div>
				</div> 
				<div class="form_block" style="display: none;">
					<label class="col-sm-3 control-label no-padding-right"
						for="serviceName">服务器类型</label>
					<div class="col-sm-9 lhp">
						<label>
							<input name="hostType" value="nginx" type="radio" ${sysHost.hostType eq 'nginx' ? 'checked' :''}>
							负载均衡
						</label>
						<label>
							<input name="hostType" value="docker" type="radio" ${sysHost.hostType eq 'docker' ? 'checked' :''}>
							Docker主机
						</label>
					</div>
				</div> 
				<div class="form_block" style="overflow: hidden;">
					<label>环境类型</label>
					<div> 
						<label class="p0">
							<input name="envType" value="test" type="radio" checked />
							测试环境
						</label>
						<label class="p0">
							<input name="envType" value="live" type="radio" ${sysHost.envType eq 'live' ? 'checked' :''} />
							生产环境
						</label>
					</div>
				</div> 
				<div class="form_block" style="overflow: hidden;">
					<label for="hostPlatform">应用类型</label>
					<div> 
						<label class="p0">
							<input name="hostPlatform" value="api" type="radio" ${sysHost.hostPlatform eq 'api' ? 'checked' :''} />
							APIManager
						</label>
						<label class="p0">
							<input name="hostPlatform" value="app" type="radio" ${sysHost.hostPlatform eq 'app' or sysHost.hostPlatform eq '' or empty sysHost.hostPlatform ? 'checked' :''} />
							APPFactory
						</label>
						<label class="p0">
							<input name="hostPlatform" value="store" type="radio" ${sysHost.hostPlatform eq 'store' ? 'checked' :''} />
							预置应用
						</label>
					</div>
				</div>
				<div class="form_block">
					<label for="backupHost">备用服务器IP</label>
					<div>
						<input type="text" id="backupHost" name="backupHost" value="${sysHost.backupHost}"
						placeholder="备用SSH IP"	class="col-xs-10 col-sm-5">&nbsp;&nbsp;<span style="color:red;">备用服务器，如没有备用服务器，请保持和主服务器ip一致</span>
					</div>
				</div>
				<div class="form_block">
					<label for="virtualHost">虚拟IP</label>
					<div>
						<input type="text" id="virtualHost" name="virtualHost" value="${sysHost.virtualHost}"
						placeholder="dns解析IP"	class="col-xs-10 col-sm-5">&nbsp;&nbsp;<span style="color:red;">dns解析ip，如果不需要高可用，请保持和主服务器ip一致</span>
					</div>
				</div>
				<div class="form_block">
					<label for="directoryName">配置文件目录</label>
					<div>
						<input type="text" id="directoryName" name="directoryName" value="${sysHost.directoryName}"
						placeholder="本地配置文件存放目录"	class="col-xs-10 col-sm-5">&nbsp;&nbsp;<span style="color:red;">rsync该目录下的配置文件到nginx服务器，建议唯一</span>
					</div>
				</div>
				<div class="clearfix form-actions">
					<div style="padding:20px 40px;">
						<button class="btn btn-info"  type="button"   id="btnSave" >保存</button>
					</div>
				</div>
			</div>
		</div>
		</form>
			<script>
		function checkHostIP(ip,type){
			if(ip==""){
	    		common.tips(type+"IP不能为空");
	    		return false;
	    	}
			var re=/^(\d+)\.(\d+)\.(\d+)\.(\d+)$/;//正则表达式     
	    	if(!re.test(ip))     
	    	{     
	    	    common.tips(type+"IP格式有误！");
				return false;  
			}
			return true;
		}
			
		function dataValidation(){
			var hostIp = $.trim($("#hostIp").val());
			if(!checkHostIP(hostIp,"服务器")){
				return false;
			}  
			if($.trim($("#hostPort").val())==""){
	    		common.tips("SSH端口不能为空");
	    		return false;
	    	}
			var reg = new RegExp("^[0-9]*$");
			var obj = $.trim($("#hostPort").val());
			   if(!reg.test(obj)){
				  common.tips("SSH端口为数字!");
			      return false;
			    }
	    	if($.trim($("#hostUser").val())==""){
	    		common.tips("服务器用户名不能为空");
	    		return false;
	    	}
	    	var backHost = $.trim($("#backupHost").val());
	    	if(!checkHostIP(backHost,"备用服务器")){
				return false;
			}
            var virtualHost = $.trim($("#virtualHost").val());
            if(!checkHostIP(virtualHost,"虚拟")){
				return false;
			}
            var directoryName = $.trim($("#directoryName").val());
            if(directoryName!=""){
            	var filter = /^[A-Za-z0-9]+$/;
            	if(!filter.test(directoryName)){
            		common.alert("配置文件目录只能为数字和字母");
            		return false;
            	}
            }else{
            	common.alert("配置文件目录不能为空");
            	return false;
            }
	    	return true;
	    }
		$(function(){
			$('#btnSave').bind('click',function(){
				var form = $("#editNginxForm");
		    	var url = form.attr("action");
		    	var params = form.serialize();
		    	if(dataValidation()){
			    	var loading = common.loading(); 
			   
			    	$.getJSON(url,params,function(json){ 
			    		if(json.code==0){
			    			common.tips("保存成功",1,function(){
				    			common.forward("${WEB_APP_PATH}/paas/nginx/host.do");
			    			});
			    		}else{
			    			loading.close();
			    			common.alert(json.msg);
			    		}
			    	});
		    	}
			});
		});
			
		</script>
	</template:replace>
</template:include>