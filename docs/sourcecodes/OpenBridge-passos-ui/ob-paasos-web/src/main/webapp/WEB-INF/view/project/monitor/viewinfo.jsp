<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ include file="/common/header.jsp"%>
<div class="plate">
            <div class="project_r">
            	<c:forEach items="${last }"  var="row">
	                <div class="r_block p20 p5">
	                    <div class="r_block">
	                        <div class="r_title">
	                            <h3 class="f14 h3_color h3_btm_line">
	                            	<i class="icons add_ico_blue mr5"></i>
	                            	${row.key}
	                            </h3>
								<div class="title_line"></div>
							 </div>
							 <div class="r_con p10_0" style="margin: 0 -1%"> 
							 	<c:if test="${! empty row.value }">
							 		<c:forEach items="${row.value }" var="row1"> 
							 			<div class="sl_block"   style="width: ${empty param.source? '30%' : '48%'};margin:0.5% 1%;">
							 				 <div class="sl_content border01  highChart-grid"  id="${row1.id}">
				                                 <p class="text-center f14 l28">${row1.name}</p>
				                             </div>
				                             <script>
				                             
				                             	$(function(){
				                             		var id = '${row1.id}';
				                             		var $dom = $('#'+id);
				                             		var _series = [];
				                             		var _info =null;
				                             		<c:forEach items="${row1.lines }" var="row2"> 
				                             			_info = {
				                             					'name' : '${row2.value}',
				                             					'id' : '${row2.key}',
				                             					'data' : []
				                             			};
				                             			_series.push(_info);
				                             		</c:forEach>
				                             		var charOptions = {
				            			 					chart: {
				            			 						events : {
				            			 							load: function () {
				            			 								var id = '${row1.id}';
				            			 								var series = this.series;
				            			 								for(var i=0;i<series.length;i++){
				            			 									var _i = series[i].options.id.replace(/\.|\/|=/g,'_');
				            			 									$('#'+id).data(_i,series[i]);
				            			 								}
				            			 							}
				            			 						}
				            			 					},
				            			 					series : _series,
				            			 					title: {
				            			 			            text: '${row1.name}',
				            			 			            x: -20 //center
				            			 			        },
				            			                    xAxis: {
				            			                        type: 'datetime'
				            			                    },
				            			                    yAxis: {
				            			                    			min: 0,
				            			                        title: {
				            			                            text: ''
				            			                        },
				            			                        <c:if test="${fn:indexOf(row1.name, '内存')>-1 }">
				            			                        labels : {
				        		      				        		formatter : function(){
				        		      				        			var _m = (parseFloat(this.value)/1024)/1024;
				        		      				        			return roundFun(_m,3)+'M';
				        		      				        		}
				        		      				        	},
				            			                        </c:if>
				            			                        plotLines: [{
				            			                            value: 0,
				            			                            width: 1,
				            			                            color: '#808080'
				            			                        }]
				            			                    },
				            			                    tooltip: {
				            			                        formatter: function () {
				            			                            return Highcharts.dateFormat('%Y-%m-%d %H:%M:%S', this.x) + '<br/>' +Highcharts.numberFormat(this.y, 4);
				            			                        }
				            			                    }
				            			            };
				                             		$dom.data('option',charOptions);
				                             	});
				                             </script>
				                         </div>
							 		</c:forEach>
							 	</c:if> 
		                     </div>
	            		</div>
	            	</div>
            	</c:forEach>
            </div>
      </div>
        <script type="text/javascript" src="${WEB_APP_PATH}/assets/js/highcharts.js"></script>
		<script type="text/javascript" src="${WEB_APP_PATH}/assets/js/highcharts-more.js"></script>
        <script>
        	var graph2Id = eval('('+'${empty graph2Id ? "{}" : graph2Id}'+')');
        	var graph2Name = eval('('+'${empty graph2Name ? "{}" : graph2Name}'+')');
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
			function   roundFun(numberRound,roundDigit) {   
				if(numberRound>=0){   
					var   tempNumber   =   parseInt((numberRound   *   Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);   
					return   tempNumber;   
			  	}else{   
					numberRound1=-numberRound   
					var   tempNumber   =   parseInt((numberRound1   *   Math.pow(10,roundDigit)+0.5))/Math.pow(10,roundDigit);   
					return   -tempNumber;   
				}   
			}  
	        $(function(){
	        	
	        	Highcharts.setOptions({
	                global: {
	                    useUTC: false
	                }
	            });

	        	
	        	function fetch(lastest){
	            	var req = $.ajax({
	            		url : '${WEB_APP_PATH}/project/monitor/data.do?projectId=${project.projectId}',
	            		cache : false,
	            		data : {
	            			'start' : lastest,
	    	     		    'endpointCounters' : eval('('+'${counts}'+')')
	            		},
	            		dataType : 'json',
	            		type : 'POST'
	            	});
	            	return req;
	            }
	        	var req  = fetch();
	        	req['done'](function(msg){
	        		if(msg.code==0){
						for(var i=0;i<msg.data.length;i++){
							var info = msg.data[i];
							var _endpoint = info['endpoint'];
							var _counter = info['counter'];
							var _id = graph2Id[_endpoint+'_'+_counter];
							var _op = $('#'+_id).data('option');
							for(var j=0;j<_op.series.length;j++){
								var _se = _op.series[j];
								if(_se.id == _counter){
									_se.data = info.data;
									break ;
								}
							}
						}
						$('.highChart-grid').each(function(domIndex,domEle){
							var options = $(domEle).data('option');
							$(domEle).highcharts(options);
						});
					}
					else{
						common.alert(msg.info?msg.info:'获取数据出错，重新刷新页面');
					}
	        	});
	        	req['done'](function(msg){
	        		if(msg.code==0){
	        			(function(_data){
							function next(ll){
								var req1  = fetch(ll);
								req1['done'](function(resp){
									if(resp.code==0){
										for(var i=0;i<resp.data.length;i++){
											var _info = resp.data[i];
											var _endpoint = _info['endpoint'];
											var _counter = _info['counter'];
											var _id = graph2Id[_endpoint+'_'+_counter];
									//		console.log(_id);
									//		console.log(_counter);
									//		console.log($('#'+_id).data());
											var _ser = $('#'+_id).data(_counter.replace(/\.|\/|=/g,'_'));
											setTimeout(function(arg){
												var _data1=arg[0];
												var _ser = arg[1];
												for(var j=0;j<_data1.length;j++){
													_ser.addPoint([_data1[j].x, _data1[j].y]);
												}
											},100,[_info.data,_ser]);
										}
									}
								});
								req1['done'](function(resp){
									
									if(resp.code==0){
										setTimeout(function(){
											next(resp.end);
										},2000);
									}
									else{
										common.alert(resp.info?resp.info:'获取数据出错，重新刷新页面');
									}
								});
							}
							next(_data.end);
						})(msg);
	        		}
	        	});
	        	if('${param.isInfo}'){
		        	$("#detailsDiv").addClass("p5");
		        	$(".p5").css({"padding":"5px"});
		        }

	        	var req1 = $.ajax({
            		url : '${WEB_APP_PATH}/project/monitor/last/data.do?projectId=${project.projectId}',
            		cache : false,
            		data : {
    	     		    'endpointCounters' : eval('('+'${counts}'+')')
	            				},
	            	dataType : 'json',
	            	type : 'POST'
	            		});
	        	req1['done'](function(msg){
							console.log(msg);
		        		});
	        });
        </script>