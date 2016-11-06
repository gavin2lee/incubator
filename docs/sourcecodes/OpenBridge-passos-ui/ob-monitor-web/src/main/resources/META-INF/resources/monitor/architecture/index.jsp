<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<template:include file="../base.jsp" nav-left="app">
	<template:replace name="title">
		应用状态
	</template:replace>
	<template:replace name="content-body">
		<div class="app_name">
			<a href="${ WEB_APP_PATH }/"><i class="icons go_back_ico"></i></a>

			<p class="app_a">
				<a href="javascript:void(0);">平台监控</a>
			</p>

		</div>
		<div class="plate">
			<div class="project_r">

				<!--项目概述开始-->
				<div class="r_block p20">
					<!--来源2-->
					<div id="content_view">
						<div id="graph-area"
							style="style: width:100%; height: 100%; position: relative;">

						</div>
					</div>
					<!--来源2结束-->
				</div>
				<!--项目概述结束-->
			</div>
			<script src="${ WEB_APP_PATH }/assets/js/raphael-min.js"></script>
			<script src="${ WEB_APP_PATH }/assets/js/architecture.js"></script>
			<script>

			
			var def = $.ajax({
				'url' : '${ WEB_APP_PATH }/monitor/architecture/graph.do',
				'type' : 'POST',
				'cache' : false,
				'dataType' : 'json',
				'data' : {
					'business_type' : 'all'
				}
			}); 
			def['done'](function(msg){
				var data = msg.data;
				var m = new Graph({
					'id' : 'graph-area',
					'data' : data,
					'webapp' : '${ WEB_APP_PATH }',
					'status_url' : '${ WEB_APP_PATH }/monitor/architecture/node/status.do',
					'afterChangeTop' : function(nodes){
						var source = nodes[1][0]; 
						source.setTop(source.getChildren()[0].getTop()+(source.getChildren()[source.getChildren().length-1].getTop()-source.getChildren()[0].getTop())/2);
					},
					'afterXLineEnd' : function(node){
						if(node.get('name')=='App Factory'){
							return true;
						}
						return false;
					},
					'beforeChangeSource' : function(node){
						if(node.get('name')=='App Factory'){
							return false;
						}
						return true;
					},
					'isDraw' : function(node){
						if(node.get('name')=='all'){
							return false;
						}
						return true;
					},
					'isDrawLeft' : function(node){
						if(node.get('name')=='Api Manager' || node.get('name')=='App Factory'){
							return false;
						}
						return true;
					},
					'errorClick' : function(node,pro,msg){
						var html="<div style='width:500px;height:100px'><table style='width:100%;height:100%'>";
						html+="<tr><td>"+"测试类型："+pro.protocol+"协议"+"</td></tr>";
						if(pro.protocol=="tcp"){
							var pa = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,6})";
							var url=pro.url;
							var ma = url.match(pa);
							if(ma!=null){
							    url = ma[0];
							    var is = url.split(":");
						     	html+="<tr><td>"+"测试参数："+"url:"+is[0]+";端口:"+is[1]+"</td></tr>";
							}
						} 
						if(pro.protocol=="http"){
						     if(pro.url!=undefined){
						         html+="<tr><td>"+"测试参数："+pro.url+"</td></tr>";
					        	}
						}
						if(msg!=null&&msg!=""){
						 html+="<tr><td>"+"测试结果："+msg+"</td></tr>";
						}
						if(pro.msg!=undefined){
						    html+="<tr><td>"+"测试结果："+pro.msg+"</td></tr>";
						}
						html+="</table></div>";
						common.tooltip(html,$("#"+$(this).attr('id').replace("_prefix","_suffix")),{guide: 1,time: 2000,maxWidth:300});
					}
					/* 'mouseover' : function(node,pro,msg){
						var html="<div style='width:500px;height:100px'><table style='width:100%;height:100%'>";
						html+="<tr><td>"+"测试类型："+pro.protocol+"协议"+"</td></tr>";
						if(pro.protocol=="tcp"){
							var pa = "([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}:[0-9]{1,6})";
							var url=pro.url;
							var ma = url.match(pa);
							if(ma!=null){
							    url = ma[0];
							    var is = url.split(":");
						     	html+="<tr><td>"+"测试参数："+"url:"+is[0]+";端口:"+is[1]+"</td></tr>";
							}
						} 
						if(pro.protocol=="http"){
						     if(pro.url!=undefined){
						         html+="<tr><td>"+"测试参数："+pro.url+"</td></tr>";
					        	}
						}
						if(msg!=null){
						 html+="<tr><td>"+"测试结果："+msg+"</td></tr>";
						}
						if(pro.msg!=undefined){
						    html+="<tr><td>"+"测试结果："+pro.msg+"</td></tr>";
						}
						html+="</table></div>";
						common.tooltip(html,$("#"+$(this).attr('id').replace("_prefix","_suffix")),{guide: 1,time: 2000,maxWidth:300});
					} */
				});
				
			});
			
		</script>
	</template:replace>
</template:include>