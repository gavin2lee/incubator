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
                <!--数据库实例信息开始-->
                     <div class="form_control">
                         <div class="form_block">
                         	<input type="hidden" name="instanceId" value="${mysqlInfo.mysqlId}"/>
                             <label>实例名称</label>
                             <div class="specList">
                             	<input type="text" readonly="readonly" value="${mysqlInfo.instanceName}"/>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>MySQL版本</label>
                             <div class="specList">
                                 <ul class="u-flavor">
                               		<li class="selected">${mysqlInfo.version}</li>
                                 </ul>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>环境类型</label>
                             <div class="specList">
                                 <ul class="u-flavor">
                                  <c:forEach items="${envTypes }" var="item">
                               		<li class="${mysqlInfo.envType eq item.key ? 'selected' : '' }">${item.value}</li>	                                
                               </c:forEach>
                                 </ul>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>服务模式</label>
                             <div class="specList">
                                 <ul class="u-flavor" id="mysqlType">
                                     <!--Regular list-->
                                     <li class="${mysqlInfo.mysqlType eq 'mysql' ? 'selected' : '' }">单机</li>
<%--                                         <li class="${mysqlInfo.mysqlType eq 'mysqlShare' ? 'selected' : '' }">共享</li> --%>
                                     <li class="${mysqlInfo.mysqlType eq 'mysqlCluster' ? 'selected' : '' }">集群</li>
                                 </ul>
                                 <div class="jq_set" style="display:none;" id="mysqlTypeAttach">
                                     <span class="mr5"><input class="w100" name="mysqlTypeAttachMain" type="text" value="1"> 主</span>
                                     <span class="mr10"><input class="w100" name="mysqlTypeAttachBackup" type="text" value="2"> 从</span>
                                 </div>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>内存大小</label>
                             <div class="specList">
                                 <ul class="u-flavor" id="mysqlMemory">
                                 	<li class="selected">${mysqlInfo.allocateMemInfo}</li>
                                 </ul>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>存储空间</label>
                             <div class="specList">
                                 <ul class="u-flavor" id="mysqlStorage">
                                 	<li class="selected">${mysqlInfo.allocateStorageInfo}</li>
                                 </ul>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>连接参数</label>
                             <div class="specList">
                             	<textarea readonly="readonly" rows="4" cols="40">${mysqlInfo.connectionInfo}</textarea>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>运行状态</label>
                             <div class="specList">
                             	<span id="mysqlStatus"><img src="${WEB_APP_PATH}/assets/images/loading.gif" /></span>
                             </div>
                         </div>
                         <div class="form_block">
                             <label>租户组织</label>
                             <div class="specList">
                             	<span>${mysqlInfo.attach['tenantName'] }</span>
                             </div>
                         </div>
                         <div class="form_block">
	                        <label>创建人</label>
	                        <div class="specList">
	                        	<span>${mysqlInfo.attach['createrName'] }</span>
	                        </div>
	                     </div>
                </div>
                <!--数据库实例信息结束-->
            </div>
        </div>		
	</template:replace>
	<template:replace name="bottom">
		<template:super/>
		<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/resource_event.js"></script>
		<script>
		var resId = '${mysqlInfo.mysqlId}';
		
		$(function(){
			queryPaasMysqlStatus();
		});
		
		function queryPaasMysqlStatus(){
			$("#mysqlStatus").empty().append("<img src='${WEB_APP_PATH}/assets/images/loading.gif' />");
			var url = "${WEB_APP_PATH}/resource/mysql/queryMysqlStatusInfo.do";
			$.get(url,{"resId":resId},function(json){
				var data = {};
				if(json.code==0){
					data = json.data;
				}else{
					data.status = "无法获取";
				}
				renderPaasMysqlStatus(data)
			});
		}
		
		function renderPaasMysqlStatus(data){
			var obj = $("#mysqlStatus");
			if(data.status=='运行中'){
				obj.empty().append("<span class='text-green2'>"+data.status+"</span>");
			}else if(data.status=='已停止'){
				obj.empty().append("<span class='text-yellow zt_span'>"+data.status+"</span>");
			}else if(data.status=='启动中'){
	 			obj.empty().append("<span class='txt-safe'>"+data.status+"</span>");
	 		}else{
				obj.empty().append(data.status);
			}
			
			obj.append('&nbsp;&nbsp;&nbsp;&nbsp;<a class="action_zt" href="javascript:void(0)" class="btn btn-link yellow" onclick="queryPaasMysqlStatus()" title="重新查询mysql状态">刷新</a>');
			if(data.podName && data.namespace){
				obj.append('&nbsp;&nbsp;&nbsp;&nbsp;<a class="action_event" href="javascript:void(0)" class="btn btn-link yellow" onclick="showResEvent(\''+data.podName+'\',\''+data.namespace+'\')" title="查看事件">事件</a>');
			}
		}
		
		</script>
	</template:replace>
</template:include>
	
