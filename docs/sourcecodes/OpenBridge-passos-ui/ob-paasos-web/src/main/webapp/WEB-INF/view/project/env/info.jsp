<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<style>
	.pods-wd{
				margin: 5px;
			}
			.pods-wd td{
				padding: 5px;
			}
			.log-area label{
				display: inline-block;
				width: 100px;
				padding-right: 10px;
				text-align: right;
			}
			.log-area div{
				margin-top: 10px;
			}

</style>
<div class="hj_block"> 
     <!--生产环境-资源集开始-->
     <div class="r_block">
         <div class="r_title">
             <h3 class="f14 h3_color h3_btm_line"><i class="icons ico_title_list mr5"></i>${ env.envName }-依赖资源</h3> 
             <div class="title_tab">
                 <ul>
                     <li>
                         <h5 class="f14">
                             <a class="active_green" href="javascript:void(0)" onclick="projectEnvEnvironment();">
                                 环境变量
                             </a>
                         </h5>
                     </li>
                     <li>
                         <h5 class="f14">
                             <a class="active_green" href="javascript:void(0)" onclick="projectEnvAddResource();">
                                 <i class="icons add_ico_yellow mr5"></i>添加资源
                             </a>
                         </h5>
                     </li>
                 </ul>
             </div>
             <div class="title_line"></div>
         </div>
         <div class="r_con p10_0">
           	<c:if test="${ resourceList == null or fn:length(resourceList) <= 0 }">
							<p class="text-center p20">暂无配置内容</p>
						</c:if>
						<c:if test="${ resourceList != null and fn:length(resourceList) > 0 }">
							<div class="r_con p10_0"><div class="details_nr"><div class="mirro_list">
								<c:forEach items="${resourceList}" var="resourceItem" varStatus="resourceStatus">
									<c:set scope="request" var="resourceItem" value="${resourceItem}"></c:set>
									<c:set scope="request" var="resourceStatus" value="${resourceStatus}"></c:set> 
									<c:import url="./resource/info.jsp"></c:import> 
								</c:forEach>
							</div></div></div>
						</c:if>
         </div>
     </div>
     <!--生产环境-资源集结束-->
     
     
     
     
	<!--生产环境-实例开始-->
	<div class="r_block">
         <div class="r_title">
             <h3 class="f14 h3_color h3_btm_line"><i class="icons ico_title_list mr5"></i>${ env.envName }-部署列表</h3>
             <div class="title_tab">
                 <ul>
                     <li>
                         <h5 class="f14">
                             <a class="active_green" href="${ WEB_APP_PATH }/project/deploy/create.do?projectId=${project.projectId}&envId=${env.envId}" >
                                 <i class="icons add_ico_yellow mr5"></i>新建部署
                             </a>
                         </h5>
                     </li>
                 </ul>
             </div>
             <div class="title_line"></div>
         </div>
         <div class="r_con p10_0" id="deployList"  extraKey="application">
  					<p class="text-center p20">暂无配置内容</p>
		 </div>
     </div>
     <!--生产环境-实例结束-->
     <!--生产环境-负载均衡开始-->
     <div class="r_block">
         <div class="r_title">
             <h3 class="f14 h3_color h3_btm_line"><i class="icons ico_title_list mr5"></i>${ env.envName }-访问代理</h3> 
             <div class="title_tab">
                 <ul>
                     <li>
                         <h5 class="f14">
                         <c:if test="${project.projectType eq 'store'}"><!-- taoshuangxi 目前只有预置应用可以添加代理 -->
                             <a class="active_green" href="javascript:void(0)" onclick="projectEnvAddLoadbalance()">
                                 <i class="icons add_ico_yellow mr5"></i>添加代理
                             </a>
                         </c:if>
                         </h5>
                     </li>
                 </ul>
             </div>
             <div class="title_line"></div>
         </div>
         <div class="row mirro_list" id="nginxList" extraKey="nginx">
 			<c:if test="${ loadbalanceList == null or fn:length(loadbalanceList) <= 0 }">
				<p class="text-center p20">暂无配置内容</p>
			</c:if>
			<c:if test="${ loadbalanceList != null and fn:length(loadbalanceList) > 0 }">
				<c:forEach items="${ loadbalanceList }" var="loadbalanceItem" varStatus="loadbalanceStatus">
					<c:set scope="request" var="loadbalanceItem" value="${loadbalanceItem}"></c:set>
					<c:set scope="request" var="loadbalanceStatus" value="${loadbalanceStatus}"></c:set> 
					<c:import url="./loadbalance/info.jsp"></c:import>
				</c:forEach>
			</c:if>
 		</div> 
     </div>
     <!--生产环境-负载均衡结束-->
</div> 

 <script type="text/javascript" src="${ WEB_APP_PATH }/assets/js/tmpl.js"></script>
  <link href="${WEB_APP_PATH}/assets/css/jquery.spinner.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${WEB_APP_PATH}/assets/js/jquery.spinner.js"></script>
  <script type="text/javascript" src="${WEB_APP_PATH}/assets/js/highcharts.js"></script>
		<script type="text/javascript" src="${WEB_APP_PATH}/assets/js/highcharts-more.js"></script>
  <script>
     // 对Date的扩展，将 Date 转化为指定格式的String 
		// 月(M)、日(d)、小时(h)、分(m)、秒(s)、季度(q) 可以用 1-2 个占位符， 
		// 年(y)可以用 1-4 个占位符，毫秒(S)只能用 1 个占位符(是 1-3 位的数字) 
		// 例子： 
		// (new Date()).Format("yyyy-MM-dd hh:mm:ss.S") ==> 2006-07-02 08:09:04.423 
		// (new Date()).Format("yyyy-M-d h:m:s.S")      ==> 2006-7-2 8:9:4.18 
			Date.prototype.Format = function(fmt){ //author: meizz 
				var o = { 
				    "M+" : this.getMonth()+1,                 //月份 
				    "d+" : this.getDate(),                    //日 
				    "h+" : this.getHours(),                   //小时 
				    "m+" : this.getMinutes(),                 //分 
				    "s+" : this.getSeconds(),                 //秒 
				    "q+" : Math.floor((this.getMonth()+3)/3), //季度 
				    "S"  : this.getMilliseconds()             //毫秒 
				  }; 
				  if(/(y+)/.test(fmt)) 
				    fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length)); 
				  for(var k in o) 
				   	 if(new RegExp("("+ k +")").test(fmt)) 
				  fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length))); 
				  return fmt; 
			};
        	function loadMyApp(pageNo,pageSize){
        		$('#deployList').trigger('content-change',[pageSize,pageNo])
        	}
        	function arriveRender(data,element){
        		var publicIp = data.data.publicIp;
        		var serviceIp = data.data.serviceIp;
        		var deployId = data.data.deployId;
        		var ports = data.data.ports;
        		var nodePort = null;
        		var targetPort = null;
        		if(ports!=null){
        			if(ports.length==1){
        				nodePort = ports[0].nodePort;
        				targetPort = ports[0].targetPort;
        			}
        			else{
        				for(var i=0 ; i<ports.length;i++){
        					if((ports[i].portKey!=null && ports[i].portKey.toUpperCase()=='HTTP') || ports[i].portProtocol.toUpperCase()=='HTTP'){
        						nodePort = ports[i].nodePort;
        						targetPort = ports[i].targetPort;
        						break ;
        					}
        				}
        			}
        		}
        		var a = [];
        		a.push('<div class="log-area">');
        		if(serviceIp!=null && serviceIp!=''){
        			var h = "http://"+serviceIp+":"+targetPort;
        			a.push('<div><label>内网访问地址:</label><a href="'+h+'" target="_blank">'+h+'</a></div>');
        			a.push('<div><label></label>该地址用于PaaSOS集群内部的相互访问</div>');
        		}
        		if(publicIp!=null && publicIp!=''){
        			/* var h = "http://"+publicIp+":"+targetPort;
        			a.push('<div><label>外网访问地址:</label><a href="'+h+'" target="_blank">'+h+'</a></div>');
        			a.push('<div><label></label>外网访问地址</div>'); */
        		}
        		var _already = {};
        		for(var i=0;i<data.data.items.length;i++){
        			var item = data.data.items[i];
        			var hostIp = item.status.hostIP;
        			
		        	var h = "http://"+hostIp+":"+nodePort;
		        	if(i==0){
		        		_already[hostIp] = 'true';
		        		a.push('<div><label>外网访问地址:</label><a href="'+h+'" target="_blank">'+h+'</a></div>');
		        			}
		        	else{
		        		if(_already[hostIp]==null){
	        				_already[hostIp] = 'true';
	        				a.push('<div><label></label><a href="'+h+'" target="_blank">'+h+'</a></div>');
			        			}
		        			}
		        	if(i==(data.data.items.length-1)){
		        		a.push('<div><label></label>该地址用于PaaSOS集群外（办公区）访问</div>');
		        			}
		        		}
        		a.push('</div>');
        		var cd = common.dialog({
        			title:'访问信息',
        			type: 1,
        			area: [400+'px', 300+'px'],
       			    fix: false, //不固定
       			    maxmin: false,
       			    btn:[],
       			    content: a.join('')
        		});
        	}
        	$(function(){
           $('#deployList').bind('status-change',function(){
							 var _r = [];
				 			 $(this).find('a[deploy-id]').each(function(domIndex,domEle){
									var _status = $(domEle).attr('deploy-status');
									if(_status==null || _status!="10"){
										return true;
									}
									var _id = $(domEle).attr('deploy-id');
									_r.push(_id);
					 		  });
					 		 if(_r.length ==0){
									return ;
							  }
					 		 var def = $.ajax({
									url : '${WEB_APP_PATH}/project/deploy/status.do',
									type : 'POST',
									data : {
										projectId : "${project.projectId}",
										deployIds : _r.join(',')
									},
									cache : false,
									dataType : 'json'
							 });
					 		def['done'](function(msg){
						 		//console.log(msg);
								if(msg.code !=0 || msg.data==null 
										|| msg.data.list ==null || msg.data.list.length==0){
									return ;
								}
								
								for(var i=0;i<msg.data.list.length;i++){
									var _info = msg.data.list[i];
									var _did = _info['deployId'];
									var _result = [];
									for(var j=0;j<_info.pods.length;j++){
										var _status = _info.pods[j].status;
										if(_status==null || _status!='running'){
											_result.push('<span style="color:;">'+_status+'</span>');
											continue ;
										}
										for(var v=0;v<_info.pods[j].container.length;v++){
											var __status = _info.pods[j].container[v].status;
											if(__status==null || __status!='running'){
												_result.push('<span style="color:#407cbd;">'+__status+'</span>');
												continue ;
											}
											_result.push('<span style="color:#407cbd;">'+__status+'</span>');
										}
									}
									if(_result.length>0){
										$('#'+_did+'_status').html('<a href="${WEB_APP_PATH}/project/deploy/view.do?deployId='+_did+'&projectId=${project.projectId}&tab=instance">('+_result.join('|')+')<a>');
									}
									
								}
							});
       					})
        		$('#deployList').bind('content-change',function(event,pageSize,pageNo){
        			var viewType  =$('input[name="viewType"]:checked').val();
        			if(viewType==null){
        				viewType='';
        					}
        			pageSize = pageSize==null ? 10 : pageSize;
        			pageNo = pageNo==null ?  1 : pageNo;
        			$('#deployList').load('${WEB_APP_PATH}/project/deploy/list.do?viewType='+viewType+
        					'&pageNo='+pageNo+'&pageSize='+pageSize+'&envId=${env.envId}&projectId=${project.projectId}'+'&_r='+(new Date()).getTime(),function(){
        						$('#deployList').trigger('status-change');
            					});
        		});
        		$('input[name="viewType"]').bind('click',function(){
        			$('#deployList').trigger('content-change');
        		});
        		$('#deployList').trigger('content-change');
        		
        		
        		
        		$('.visit-button').live('click',function(){
        			
        			var $ptd = $(this).parents('.row-td');
        		//	var pods = $ptd.data('pods');
        			var _self =this;
        		//	if(pods!=null){
        		//		arriveRender(json,_self);
        		//		return ;
        		//	}
        			var _deployId = $(this).attr('attrId');
        			var param = {
        					projectId : "${project.projectId}",
        					deployId : _deployId
        			};
        			var option = {
 							url : '${WEB_APP_PATH}/project/deploy/getPods.do',
 							type : 'POST',
 							data : param,
 							cache : false,
 							dataType : 'json'
 					};
 					var load = common.loading('获取访问信息......');
 					var def = $.ajax(option);
 					def['done'](function(json){
 						load.close();
 						if(json.code==0){
 							$ptd.data('pods',json);
 							arriveRender(json,_self);
			    		}else{
			    			common.tips(json.msg);
			    		}
 					});
        		});
        		
				$('.log-button').live('click',function(){
        			var _deployId = $(this).attr('deployid');
        			var _podName = $(this).attr('attrId');
        			var param = {
        					projectId : "${project.projectId}",
        					podName : _podName,
        					deployId : _deployId
        			};
        			var option = {
 							url : '${WEB_APP_PATH}/project/deploy/getPodLog.do',
 							type : 'POST',
 							data : param,
 							cache : false,
 							dataType : 'json'
 					};
 					var load = common.loading('获取pod日志信息......');
 					var def = $.ajax(option);
 					def['done'](function(json){
 						load.close();
 						if(json.code==0){
 							var cd = common.dialog({
 			        			title:'pod日志信息',
 			        			type: 1,
 			        			area: [800+'px', 500+'px'],
 			       			    fix: false, //不固定
 			       			    maxmin: false,
 			       			    btn:[],
 			       			    content: '<div style="padding:10px;"><pre>'+json.data+'</pre></div>'
 			        		});
			    		}else{
			    			common.tips(json.msg);
			    		}
 					});
        		});
        	});
</script>
<template:include file="../deploy/footer.jsp"></template:include>
<script type="text/javascript" src="${ WEB_APP_PATH }/common/js/resource_event.js"></script>
 <script>
 	function projectEnvViewResource(recordId){
		var url ="${WEB_APP_PATH}/project/env/resource/view.do?projectId=${project.projectId}&envId=${envId}&recordId="+recordId;
		var x = common.dialogIframe("查看资源",url,700,500,function(json){
			 
		});
	}
 	function projectEnvResourceHelp(recordId){
		common.dialogIframe("帮助","${WEB_APP_PATH}/project/env/resource/help.do?projectId=${project.projectId}&recordId="+recordId,800,500);
	}
	function showProjectEnvResourceStatus(recordId){
		var span = $("#resourceStatus_"+recordId).find("span:first");
		span.empty().append("<img src='${WEB_APP_PATH}/assets/images/loading.gif' />");
		var url="${WEB_APP_PATH}/project/env/resource/status.do?projectId=${project.projectId}&recordId="+recordId;
		$.get(url,function(json){
			var data = json.data;
			if(json.code !=0){
				span.empty().append("<i class='fa fa-exclamation'></i>无法获取资源状态");
			}else{
				if(data.status=='运行中'){
					span.empty().append("<i class='fa fa-check'></i>正常");
				}else{
					span.empty().append("<i class='fa fa-exclamation'></i>"+data.status);
				}
				if(data.podName && data.namespace){
					span.append('&nbsp;&nbsp;<a href="javascript:void(0)" onclick="showResEvent(\''+data.podName+'\',\''+data.namespace+'\')">事件</a>');
				}
			}
		});
	}
	
 </script>
 
 				<script src="${ WEB_APP_PATH }/assets/js/os-log.js"></script>
        <script>
					$(function(){
						logShow.init({
							type_table_url : '${ WEB_APP_PATH }/project/log/history.do',
							type_info_url : '${ WEB_APP_PATH }/project/log/info.do',
							errorTip : function(msg){
								common.tips(msg);
							},
							openDialog : function(msg){
								var cd = common.dialog({
	 			        			title : msg.title,
	 			        			type: 1,
	 			        			area: [msg.width+'px', msg.height+'px'],
	 			       			  fix: false, //不固定
	 			       			  maxmin: false,
	 			       			  btn:[],
	 			       			  content: msg.html
	 			        		});
							},
							load_image_url : '${ WEB_APP_PATH }/assets/images/loading.gif',
							type_table_data : {
								projectId : "${project.projectId}"
							},
							type_monitor_pre_url : '${ WEB_APP_PATH }/project/monitor/monitorPreData.do',
							type_monitor_data_url :	'${WEB_APP_PATH}/project/monitor/data.do'
						});
					});
        </script>