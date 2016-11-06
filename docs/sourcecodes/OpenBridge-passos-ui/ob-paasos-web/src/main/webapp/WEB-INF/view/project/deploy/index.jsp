<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="deploy">
	<template:replace name="title">
		项目部署
	</template:replace>
	<template:replace name="content-body">
		<style>
			.pods-wd{
				margin: 5px;
			}
			.pods-wd td{
				padding: 5px;
			}
			.log-area label{
				display: inline-block;
				width: 130px;
				padding-right: 10px;
				text-align: right;
			}
			.log-area div{
				margin-top: 10px;
			}
			.log-area{
				padding: 10px;
			}
		</style>
	     <div class="app_name">
            <a href="${ WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

            <p class="app_a"><a href="${ WEB_APP_PATH }/project/index.do">项目管理</a></p>
            <em>&gt;</em>

            <p class="app_a">
            	<a href="${ WEB_APP_PATH }/project/overview/index.do?projectId=${project.projectId}">
            	${project.projectName }&nbsp;
            	</a>
            </p>
            <em>&gt;</em>

            <p class="app_a">项目部署</p>
        </div>
        <div class="plate">
        <!--  tab -->
         <div class="tab_title">
             <div class="title_tab title_tab_item">
                 <ul>
                     <li>
                         <h5 class="f14">
                             <a class="${ envType eq 'test' ? 'active' : ''}" href="${WEB_APP_PATH}/project/deploy/index.do?projectId=${project.projectId}&envType=test"><i class="tab_ico tab_ico07 mr5"></i>测试环境</a>
                         </h5>
                     </li>
                     <li>
                         <h5 class="f14">
                             <a class="${ envType eq 'live' ? 'active' : ''}" href="${WEB_APP_PATH}/project/deploy/index.do?projectId=${project.projectId}&envType=live"><i class="tab_ico tab_ico06 mr5"></i>生产环境</a>
                         </h5>
                     </li>
                 </ul>
                 <div class="title_line"></div>
             </div>
         </div>
         <!-- tab -->
        
        
            <div class="project_r">
                <div class="pas_info mb10 p20" style="background:#f0f0f0;">
                    <a href="${ WEB_APP_PATH }/project/deploy/create.do?projectId=${project.projectId}" class="btn btn-default btn-oranger f16 mr20"> 新建部署 </a>
                    <em class="mr10 f14">|</em>
                    <span class="f14">在该页面您可以将项目打包好的镜像进行部署发布。
                        </a></span>
                </div>
                <!--项目概述开始-->
                <div class="r_block p20">
                    <div class="r_title">
                        <h3 class="f14 h3_color h3_btm_line blue">
                            <a href="#">
                                <i class="icons add_ico mr5"></i>项目部署
                            </a>
                        </h3>
                        <div class="title_tab title_tab_radio">
                            <ul>
                                <li>
                                    <h5 class="f14">
                                        <input ${ ( empty viewType) || viewType=="table" ? "checked=true" : ""  }  value="preview"  type="radio" name="viewType" class="mr10"> 预览视图
                                    </h5>
                                </li>
                                <li>
                                    <h5 class="f14">
                                        <input ${ ! empty viewType && viewType=="preview" ? "checked=true" : ""  } value="table" type="radio" name="viewType" class="mr10"> 列表视图</h5>
                                </li>
                            </ul>
                        </div>
                        <div class="title_line"></div>
                    </div>
                    <!--来源2-->
                    <div id="content_view">
                    
                    </div>
                    <!--来源2结束-->
                </div>
                <!--项目概述结束-->
            </div>

        </div>

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
        		$('#content_view').trigger('content-change',[pageSize,pageNo])
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
        			a.push('<div style="padding-left:140px;">该地址用于PaaSOS集群内部的相互访问</div>');
        		}
        		if(publicIp!=null && publicIp!=''){
        			/* var h = "http://"+publicIp+":"+targetPort;
        			a.push('<div><label>外网访问地址:</label><a href="'+h+'" target="_blank">'+h+'</a></div>');
        			a.push('<div><label></label>外网访问地址</div>'); */
        		}
        		for(var i=0;i<data.data.items.length;i++){
        			var item = data.data.items[i];
        			var hostIp = item.status.hostIP;
        			var h = "http://"+hostIp+":"+nodePort;
        			if(i==0){
        				a.push('<div><label>外网访问地址:</label><a href="'+h+'" target="_blank">'+h+'</a></div>');
        			}
        			else{
        				a.push('<div><label></label><a href="'+h+'" target="_blank">'+h+'</a></div>');
        			}
        			if(i==(data.data.items.length-1)){
        				a.push('<div style="padding-left:140px;">该地址用于PaaSOS集群外（办公区）访问</div>');
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
        		$('#content_view').bind('content-change',function(event,pageSize,pageNo){
        			var viewType  =$('input[name="viewType"]:checked').val();
        			pageSize = pageSize==null ? 10 : pageSize;
        			pageNo = pageNo==null ?  1 : pageNo;
        			$('#content_view').load('${WEB_APP_PATH}/project/deploy/list.do?viewType='+viewType+
        					'&pageNo='+pageNo+'&pageSize='+pageSize+'&projectId=${project.projectId}'+'&_r='+(new Date()).getTime());
        		});
        		$('input[name="viewType"]').bind('click',function(){
        			$('#content_view').trigger('content-change');
        		});
        		$('#content_view').trigger('content-change');
        		
        		
        		
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
        <template:include file="footer.jsp"></template:include>
        
	</template:replace>
</template:include>