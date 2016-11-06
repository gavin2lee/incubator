<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/WEB-INF/view/resource/base.jsp" nav-left="redis">
	<template:replace name="title">
		PaaSOS--资源服务
	</template:replace>
	<template:replace name="content-path">
		<em>&gt;</em>
		<p class="app_a"> 高速缓存 </p>
	</template:replace>
	<template:replace name="content-body">
		<div class="plate" style="height: 142px;">
            <div class="project_r">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="${WEB_APP_PATH}/resource/redis/add.do" class="btn btn-default btn-oranger f16 mr20"> 创建实例 </a>
                    <em class="mr10 f14">|</em>
                    <span class="f14">高速缓存的实现采用Redis，Redis是开源的，基于Key-Value的存储服务。
                    	</span>
                </div>
                <!--高速缓存实例开始-->
                <div class="r_block p20">
                    <div class="r_title">
                       <!--  <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="javascript:void(0);">
                                <i class="icons ico_title_list mr5"></i>我的redis实例
                            </a>
                        </h3> -->
                          <h3 class="f14 h3_color h3_btm_line  ${param['tab'] == 'tenant' ? ' gray noactive' : ' blue '}">
	                            <a href="${param['tab'] == 'tenant' ? (requestScope.WEB_APP_PATH.concat('/resource/redis/index.do')) : 'javascript:void(0);'}">
	                                <i class="icons cy_ico mr5"></i>我的redis实例
	                            </a>
	                        </h3>
	                        <h3 class="f14 h3_color h3_btm_line   ${param['tab'] == 'tenant' ? ' blue ' : ' gray noactive'}">
	                            <a href="${param['tab'] == 'tenant' ? 'javascript:void(0);' : (requestScope.WEB_APP_PATH.concat('/resource/redis/index.do?tab=tenant')) }">
	                                <i class="icons cy_ico mr5"></i>组织的redis实例
	                            </a>
	                        </h3>
                        <div class="title_line"></div>
                        <div class="title_tab">
                        	<h5 class="f14">
                            	<a  class="active_green" href="javascript:void(0);" onclick="queryRedissInfo()"><i class="icons refresh_ico_yellow mr5"></i>刷新</a>
                        	</h5>
                        </div>
                    </div>
                    <div id="redislist" class="r_con p10_0">
                     </div>
                </div>
                <!--高速缓存实例结束-->
            </div>
        </div>
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/verify.js"></script>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/resource_event.js"></script>
		<script>
			$(queryRedissInfo());
			
			function queryRedissInfo(pageSize, pageNo){
				var url= "${WEB_APP_PATH}/resource/redis/list.do";
				var params = {};
		    	params.listType = "${param['tab'] == 'tenant' ? 'tenant' : 'notenant'}";
				if(typeof pageSize !='undefined' && !isNaN(pageSize)){
					url = url +"?pageSize="+pageSize
				}
				if(typeof pageNo !='undefined' && !isNaN(pageNo)){
					url = url + "&pageNo="+pageNo
				}
				$("#redislist").html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' />&nbsp; 正在加载数据中.....")
				.load(url,params,function(responseTxt,statusTxt,xhr){
					if(statusTxt=="error")
						common.alert("Error: "+xhr.status+": "+xhr.statusText);
					if(xhr.readyState==4 && xhr.status==200){
						var array = $(".redisInfo");
						if(array && array.length >0){
							for(var i=0; i< array.length;i++){
								var tr = $(array[i]);
								queryPaasRedisStatus(tr);
							}
						}
					}
				});
			}
			
			function queryPaasRedisStatus(tr){
				var redisId = tr.attr("data");
				var resId = tr.attr("id");
				if(resId==''){
					tr.find("."+redisId).html("无法获取");
					return;
				}
				tr.find("."+redisId).empty().html("<img src='"+WEB_APP_PATH+"/assets/images/loading.gif' style='vertical-align:-2px;margin:0px;'/>");
				var url = "${WEB_APP_PATH}/resource/redis/queryRedisStatusInfo.do";
				$.get(url,{"resId":redisId},function(json){
					var data = {};
					if(json.code==0){
						var data = json.data;
					}else{
						data.status = "暂时无法获取redis状态";
					}
					renderPaasRedisStatus(data,redisId);
				});
			}
			
			function gotoPage(pageSize, pageNo){
				queryRedissInfo(pageSize, pageNo)
			}
			
			function deletePaasRedis(redisId){
				var callback = new deleteResourceCallBack(redisId);
				var verify = new Verify(callback,"Redis");
				verify.pwdConfirm();
			}
			
			function deleteResourceCallBack(redisId){
				this.redisId = redisId;
			}
			
			deleteResourceCallBack.prototype.exec = function(){
				var url = "${WEB_APP_PATH}/resource/redis/delete.do";
				var load = common.loading();
				$.post(url,{"resId":this.redisId},function(json){
					load.close();
					if(json.code==0){
						common.goto("${WEB_APP_PATH}/resource/redis/index.do");
					}else{
						common.tips("删除redis失败");
					}
				});
			}
			
			function showRedisInfo(redisId){
				var url ="${WEB_APP_PATH}/resource/redis/info.do?redisId="+redisId;
				common.dialogIframe( '高速缓存实例',url,500,440);
			}
			
			function renderPaasRedisStatus(data,redisId){
				var tr = $("."+redisId).closest("tr");
				tr.find(">td:last").find(".action_zt").remove();
				tr.find(">td:last").find(".action_event").remove();
		 		if(data.status=='运行中'){
		 			$("."+redisId).empty().html("<span class='text-green2'>"+data.status+"</span>");
		 			tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="stopRedis(\''+redisId+'\')" title="停止运行Redis">停止</a>');
		 		}else if(data.status=='已停止'){
		 			$("."+redisId).empty().html("<span class='text-yellow zt_span'>"+data.status+"</span>");
		 			tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="startRedis(\''+redisId+'\')" title="启动Redis">启动</a>');
		 		}else if(data.status=='启动中'){
		 			$("."+redisId).empty().html("<span class='txt-safe'>"+data.status+"</span>");
		 		}else{
		 			$("."+redisId).empty().html(data.status);
		 		}
		 		if(data.podName && data.namespace){
					tr.find(">td:last").append('&nbsp;&nbsp;<a class="action_event" href="javascript:void(0)" class="btn btn-link yellow" onclick="showResEvent(\''+data.podName+'\',\''+data.namespace+'\')" title="查看事件">事件</a>');
				}
			}
			
			function startRedis(redisId){
				var load = common.loading();
				var url = "${WEB_APP_PATH}/resource/redis/startRedis.do";
				$.post(url,{"resId":redisId},function(json){
					load.close();
					if(json.code==0){
						var data ={};
						data.status='启动中';
						renderPaasRedisStatus(data,redisId)
					}else{
						common.tips("启动redis失败");
					}
				});
			}
			
			function stopRedis(redisId){
				var load = common.loading();
				var url = "${WEB_APP_PATH}/resource/redis/stopRedis.do";
				$.post(url,{"resId":redisId},function(json){
					load.close();
					if(json.code==0){
						var data ={};
						data.status='已停止';
						renderPaasRedisStatus(data,redisId)
					}else{
						common.tips("停止redis失败");
					}
				});
			}
		</script>
	</template:replace>
</template:include>