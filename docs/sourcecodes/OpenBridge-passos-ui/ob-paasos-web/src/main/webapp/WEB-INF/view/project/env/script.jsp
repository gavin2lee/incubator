<%@ page language="java" pageEncoding="UTF-8"%>
<script>
	//env
	function projectEnvCreate(envType){
		var dialog = common.dialogIframe("创建环境","${WEB_APP_PATH}/project/env/create.do?projectId=${project.projectId}&envType="+envType,400,280,function(isOk){
			dialog.close();
    		if(isOk){ 
   				common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envType=${envType}");
    		}
		});
	}
	function projectEnvEdit(){
		var url ="${WEB_APP_PATH}/project/env/edit.do?projectId=${project.projectId}&envId=${envId}";
		var x = common.dialogIframe("修改环境名称",url,400,320,function(isOk){
			x.close();
			if(isOk){ 
				common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envId=${envId}");
			}
		});
	}
	function projectEnvClone(){
		
	}
	function projectEnvDel(){
		var url ="${WEB_APP_PATH}/project/env/del.do?projectId=${project.projectId}&envId=${envId}";
		common.confirm("你确定要删除该环境以及所有信息？",function(isOk){
			if(isOk){ 
				var load = common.loading(); 
				$.getJSON(url,function(json){
					load.close();
					if(json.code==0){
						common.tips("删除成功",1,function(){
							common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envType=${envType}");
						});
					}else{
						common.alert("删除失败<br>"+json.msg,2);
					}
				});
			};
		});
	} 
	// resource
	function projectEnvAddResource(){
		var url ="${WEB_APP_PATH}/project/env/resource/add.do?projectId=${project.projectId}&envId=${envId}";
		var x = common.dialogIframe("添加资源",url,700,500,function(json){
			x.close();
			common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envId=${envId}");
		});
	}
	function projectEnvViewResource(recordId){
		var url ="${WEB_APP_PATH}/project/env/resource/view.do?projectId=${project.projectId}&envId=${envId}&recordId="+recordId;
		var x = common.dialogIframe("查看资源",url,700,500,function(json){
			 
		});
	}
	function projectEnvEditResource(recordId){
		var url ="${WEB_APP_PATH}/project/env/resource/edit.do?projectId=${project.projectId}&envId=${envId}&recordId="+recordId;
		var x = common.dialogIframe("查看资源",url,700,500,function(json){
			 
		});
	}
	function projectEnvEnvironment(){
		var url ="${WEB_APP_PATH}/project/env/resource/environment.do?projectId=${project.projectId}&envId=${envId}";
		var x = common.dialogIframe("查看资源",url,700,500,function(json){
			 
		});
	}
	function projectEnvDelResource(recordId){
		var url ="${WEB_APP_PATH}/project/env/resource/del.do?projectId=${project.projectId}&envId=${envId}&recordId="+recordId;
		common.confirm("你确定要删除该资源？",function(isOk){
			if(isOk){ 
				var load = common.loading(); 
				$.getJSON(url,function(json){
					if(json.code==0){
						common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envId=${envId}");
					}else{
						common.tips(json.msg);
					}
				});
			};
		});
	}
	// loadbalance
	function projectEnvAddLoadbalance(){
		var url ="${WEB_APP_PATH}/project/env/loadbalance/add.do?projectId=${project.projectId}&envId=${envId}";
		var x = common.dialogIframe("添加访问代理",url,700,500,function(json){
			x.close();
			common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envId=${envId}");
		});
	}
	function projectEnvEditLoadbalance(loadbalanceId){
		var url ="${WEB_APP_PATH}/project/env/loadbalance/edit.do?projectId=${project.projectId}&envId=${envId}&loadbalanceId="+loadbalanceId;
		var x = common.dialogIframe("配置访问代理",url,700,500,function(json){
			x.close();
			common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envId=${envId}");
		});
	}
	function projectEnvDelLoadbalance(loadbalanceId){
		var url ="${WEB_APP_PATH}/project/env/loadbalance/del.do?projectId=${project.projectId}&envId=${envId}&loadbalanceId="+loadbalanceId;
		common.confirm("你确定要删除该代理？",function(isOk){
			if(isOk){ 
				var load = common.loading(); 
				$.getJSON(url,function(json){
					common.forward("${WEB_APP_PATH}/project/env/index.do?projectId=${project.projectId}&envId=${envId}");
				});
			};
		});
	}
</script>