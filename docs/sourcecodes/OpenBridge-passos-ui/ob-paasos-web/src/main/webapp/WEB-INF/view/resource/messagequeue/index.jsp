<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/WEB-INF/view/resource/base.jsp" nav-left="messageQueue">
	<template:replace name="title">
		PaaSOS--资源服务
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a"> 消息中间件 </p>
	</template:replace>
	<template:replace name="content-body">
		<div class="plate" style="height: 142px;">
            <div class="project_r">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="${WEB_APP_PATH}/resource/messagequeue/add.do" class="btn btn-default btn-oranger f16 mr20"> 创建实例 </a>
                    <em class="mr10 f14">|</em>
                    <span class="f14">消息中间件的实现采用RabbitMQ，是一个在AMQP基础上完整的，可复用的企业消息系统。遵循Mozilla Public License开源协议。
                       </span>
                </div>
                <!--消息中间件实例开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <!-- <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="javascript:void(0);">
                                <i class="icons ico_title_list mr5"></i>我的消息中间件实例
                            </a>
                        </h3> -->
                        <h3 class="f14 h3_color h3_btm_line  ${param['tab'] == 'tenant' ? ' gray noactive' : ' blue '}">
	                            <a href="${param['tab'] == 'tenant' ? (requestScope.WEB_APP_PATH.concat('/resource/messagequeue/index.do')) : 'javascript:void(0);'}">
	                                <i class="icons cy_ico mr5"></i>我的消息中间件实例
	                            </a>
	                        </h3>
	                        <h3 class="f14 h3_color h3_btm_line   ${param['tab'] == 'tenant' ? ' blue ' : ' gray noactive'}">
	                            <a href="${param['tab'] == 'tenant' ? 'javascript:void(0);' : (requestScope.WEB_APP_PATH.concat('/resource/messagequeue/index.do?tab=tenant')) }">
	                                <i class="icons cy_ico mr5"></i>组织的消息中间件实例
	                            </a>
	                        </h3>
                        <div class="title_line"></div>
                        <div class="title_tab">
                        	<h5 class="f14">
                            	<a class="active_green" href="javascript:void(0);" onclick="queryMQsInfo()">
		<i class="icons refresh_ico_yellow mr5"></i>刷新</a>
                        	</h5>
                        </div>
                    </div>
                    <div id="mqlist" class="r_con p10_0">
                     </div>
                </div>
                <!--消息中间件实例结束-->
            </div>
        </div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/verify.js"></script>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/resource_event.js"></script>
	<script>
		$(queryMQsInfo());
		
		function queryMQsInfo(pageSize, pageNo){
			var url= "${WEB_APP_PATH}/resource/messagequeue/list.do";
			var params = {};
	    	params.listType = "${param['tab'] == 'tenant' ? 'tenant' : 'notenant'}";
			if(typeof pageSize !='undefined' && !isNaN(pageSize)){
				url = url +"?pageSize="+pageSize
			}
			if(typeof pageNo !='undefined' && !isNaN(pageNo)){
				url = url + "&pageNo="+pageNo
			}
			$("#mqlist").html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' />&nbsp; 正在加载数据中.....")
			.load(url,params,function(responseTxt,statusTxt,xhr){
				if(statusTxt=="error")
					common.alert("Error: "+xhr.status+": "+xhr.statusText);
				if(xhr.readyState==4 && xhr.status==200){
					var array = $(".mqInfo");
					if(array && array.length >0){
						for(var i=0; i< array.length;i++){
							var tr = $(array[i]);
							queryPaasMQStatus(tr);
						}
					}
				}
			});
		}
		
		function queryPaasMQStatus(tr){
			var resId = tr.attr("data");
			var mqId = tr.attr("id");
			if(resId==''){
				tr.find("."+mqId).html("创建中");
				return;
			}
			tr.find("."+mqId).empty().html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' style='vertical-align:-2px;margin:0px;' />");
			var url = "${WEB_APP_PATH}/resource/messagequeue/queryMQStatusInfo.do";
			$.get(url,{"resId":mqId},function(json){
				var data = {};
				if(json.code==0){
					data = json.data;
				}else{
					data.status = "无法获取";
				}
				renderPaasRabbitMQStatus(data,mqId)
			});
		}
		
		function gotoPage(pageSize, pageNo){
			queryMQsInfo(pageSize, pageNo)
		}
		
		function deletePaasMQ(mqId){
			var callback = new deleteResourceCallBack(mqId);
			var verify = new Verify(callback,"RabbitMQ");
			verify.pwdConfirm();
		}
		
		function deleteResourceCallBack(mqId){
			this.mqId = mqId;
		}
		deleteResourceCallBack.prototype.exec = function(){
			var url = "${WEB_APP_PATH}/resource/messagequeue/delete.do";
			var load = common.loading();
			$.post(url,{"resId":this.mqId},function(json){
				load.close();
				if(json.code==0){
					common.goto("${WEB_APP_PATH}/resource/messagequeue/index.do");
				}else{
					common.tips("删除消息队列失败");
				}
			});
		}
		
		function showMQInfo(mqId){
			var url ="${WEB_APP_PATH}/resource/messagequeue/info.do?mqId="+mqId;
			common.dialogIframe( '消息队列实例',url,500,500);
		}
		
		function renderPaasRabbitMQStatus(data,resId){
			var tr = $("."+resId).closest("tr");
			tr.find(">td:last").find(".action_zt").remove();
			tr.find(">td:last").find(".action_event").remove();
			if(data.status=='运行中'){
				tr.find("."+resId).empty().html("<span class='text-green2'>"+data.status+"</span>");
				tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="stopRabbitMQ(\''+resId+'\')" title="停止运行消息队列">停止</a>');
			}else if(data.status=='已停止'){
				tr.find("."+resId).empty().html("<span class='text-yellow zt_span'>"+data.status+"</span>");
				tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="startRabbitMQ(\''+resId+'\')" title="启动消息队列">启动</a>');
			}else if(data.status=='启动中'){
				tr.find("."+resId).empty().html("<span class='txt-safe'>"+data.status+"</span>");
	 		}else{
				tr.find("."+resId).empty().html("<span class='text-yellow zt_span'>"+data.status+"</span>");
			}
			if(data.podName && data.namespace){
				tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_event" href="javascript:void(0)" class="btn btn-link yellow" onclick="showResEvent(\''+data.podName+'\',\''+data.namespace+'\')" title="查看事件">事件</a>');
			}
		}
		
		function startRabbitMQ(resId){
			var load = common.loading();
			var url = "${WEB_APP_PATH}/resource/messagequeue/startRabbitMQ.do";
			$.post(url,{"resId":resId},function(json){
				load.close();
				if(json.code==0){
					var data ={};
					data.status='启动中';
					renderPaasRabbitMQStatus(data,resId)
				}else{
					common.tips("启动消息队列失败");
				}
			});
		}
		
		function stopRabbitMQ(resId){
			var load = common.loading();
			var url = "${WEB_APP_PATH}/resource/messagequeue/stopRabbitMQ.do";
			$.post(url,{"resId":resId},function(json){
				load.close();
				if(json.code==0){
					var data ={};
					data.status='已停止';
					renderPaasRabbitMQStatus(data,resId)
				}else{
					common.tips("停止消息队列失败");
				}
			});
		}
		
	</script>
	</template:replace>
</template:include>
	
