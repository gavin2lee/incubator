<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="/common/template/dialog.jsp">
	<template:replace name="body">
		<style type="text/css">
			.form_block{
				padding:5px;
			}
			.form_block label {
		    	width: 100px;
		    }
			.specList{
				margin-left: 150px;
			}
			.u-flavor {
			    padding-bottom: 5px; 
			    height: auto;
			    overflow: hidden;
			    padding-left: 1px;
			}
		</style>
		<div class="plate">
            <div class="project_r">
                <!--高速缓存实例信息开始-->
                  <div class="form_control">
                      <div class="form_block">
                          <label>实例名称</label>
                           <div class="specList">
                          		<input type="text" readonly="readonly" value="${redisInfo.redisName }"/>
                          </div>
                      </div>
                      <div class="form_block">
                          <label>Redis版本</label>
                          <div class="specList">
                              <ul class="u-flavor">
                            	<li class="selected">${redisInfo.version }</li>	                                
                              </ul>
                          </div>
                      </div>
                      <div class="form_block">
                          <label>环境类型</label>
                          <div class="specList">
                              <ul class="u-flavor">
                               <c:forEach items="${envTypes }" var="item">
                            	<li class="${redisInfo.envType eq item.key ? 'selected' : '' }">${item.value}</li>	                                
                            </c:forEach>
                              </ul>
                          </div>
                      </div>
                      <div class="form_block">
                          <label>内存大小</label>
                          <div class="specList">
                           	<ul class="u-flavor">
                           		<li class="selected">${redisInfo.allocateEnvInfo}</li>	
                           	</ul>
                          </div>
                      </div>
                      <div class="form_block">
                          <label>连接参数</label>
                          <div class="specList">
                          	<textarea readonly="readonly" rows="5" cols="40">${redisInfo.connectionInfo}</textarea>
                          </div>
                      </div>
                      <div class="form_block">
                          <label>运行状态</label>
                          <div class="specList">
                          	<span  id="redisStatus"><img src="${WEB_APP_PATH}/assets/images/loading.gif" /></span>
                          </div>
                      </div>
                      <div class="form_block">
                          <label>租户组织</label>
                          <div class="specList">
                          	<span>${redisInfo.attach['tenantName'] }</span>
                          </div>
                      </div>
                      <div class="form_block">
                        <label>创建人</label>
                        <div class="specList">
                        	<span>${redisInfo.attach['createrName'] }</span>
                        </div>
                     </div>
                  </div>
                <!--高速缓存实例信息结束-->
            </div>
        </div>		
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/resource_event.js"></script>
		<script>
	var resId = '${redisInfo.redisId}';
	
	$(queryRedisStatus());
	
	function queryRedisStatus(){
		$("#redisStatus").empty().append('<img src="${WEB_APP_PATH}/assets/images/loading.gif" />');
		var url = "${WEB_APP_PATH}/resource/redis/queryRedisStatusInfo.do";
		$.get(url,{"resId":resId},function(json){
			var data = {};
			if(json.code==0){
				data = json.data;
			}else{
				data.status = "无法获取";
			}
			renderPaasRedisStatus(data)
		});
	}
	
	function renderPaasRedisStatus(data){
		var obj = $("#redisStatus");
		if(data.status=='运行中'){
			obj.empty().append("<span class='text-green2'>"+data.status+"</span>");
 		}else if(data.status=='已停止'){
 			obj.empty().append("<span class='text-yellow zt_span'>"+data.status+"</span>");
 		}else if(data.status=='启动中'){
 			obj.empty().append("<span class='txt-safe'>"+data.status+"</span>");
 		}else{
 			obj.empty().append(data.status);
 		}
		obj.append('&nbsp;&nbsp;&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="queryRedisStatus()" title="重新查询Redis状态">刷新</a>');
		if(data.podName && data.namespace){
			obj.append('&nbsp;&nbsp;&nbsp;&nbsp;<a class="action_event" href="javascript:void(0)" class="btn btn-link yellow" onclick="showResEvent(\''+data.podName+'\',\''+data.namespace+'\')" title="查看事件">事件</a>');
		}
	}
	
	</script>
	</template:replace>
</template:include>