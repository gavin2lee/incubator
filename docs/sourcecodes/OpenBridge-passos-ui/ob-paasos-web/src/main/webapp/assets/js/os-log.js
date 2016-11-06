/*
 用于日志查看功能
 传入选项 
*/
var logShow = function LogShow(){
		var type_table_template = [];
		type_table_template.push('{$<div style="padding:10px;"><table class="table_ob details_nr" style="width:100%;table-layout:fixed;">');
		type_table_template.push('	<thead>');
		type_table_template.push('		<tr>');
		type_table_template.push('			<td >事件类别</td>');
		type_table_template.push('			<td >发起时间</td>');
		type_table_template.push('			<td >发起人</td>');
		type_table_template.push('			<td >查看</td>');
		type_table_template.push('		</tr>');
		type_table_template.push('	</thead>');
		type_table_template.push('	<tbody>$}');
		type_table_template.push('		for(var i=0;i<data.data.length;i++){');
		type_table_template.push('			{$');
		type_table_template.push('			<tr >');
		type_table_template.push('				<td>{%data.data[i].type%}</td>');
		type_table_template.push('				<td>$} var d=data.data[i].createDate;d=new Date(d);d=d.getFullYear()+"-"+(d.getMonth()+1)+"-"+d.getDate()+'+
				'" "+d.getHours()+":"+d.getMinutes()+":"+d.getSeconds();{${%d%}</td>');
		type_table_template.push('				<td>{%data.data[i].createUser%}</td>');
		type_table_template.push('				<td><a attrId="{%data.data[i].id%}" href="javascript:void(0)" class="os-log-info">查看</a></td>');
		type_table_template.push('			</tr>$}');
		type_table_template.push('			');
		type_table_template.push('		}');
		type_table_template.push('	{$');
		type_table_template.push('	</tbody>');
		type_table_template.push('</table></div>');
		type_table_template.push('$}');
		
		var type_monitor_template=[];
		type_monitor_template.push('{$ <div id="{%dialog_id%}" style="height: 523px;width:1400px;"> ');
		type_monitor_template.push('	<div class=""> $}');
		type_monitor_template.push('  for(var k in last){ ');		
		type_monitor_template.push('{$ 		<div class="r_block p20" style="clear:both;"> ');
		type_monitor_template.push('			<div class="r_block"> ');
		type_monitor_template.push('				<div class="r_title"> ');
		type_monitor_template.push('					<h3 class="f14 h3_color h3_btm_line"> ');
		type_monitor_template.push('						<i class="icons add_ico_blue mr5"></i> ');
		type_monitor_template.push('						{%k%} ');
		type_monitor_template.push('					</h3> ');
		type_monitor_template.push('					<div class="title_line"></div> ');
		type_monitor_template.push('				</div> ');
		type_monitor_template.push('				<div class="r_con p10_0"> $}');
		type_monitor_template.push('  				for(var j=0;j<last[k].length;j++){ ');		
		type_monitor_template.push('{$				<div class="sl_block" style="width: 32%"> ');
		type_monitor_template.push('						<div class="sl_content border01  highChart-grid" id="{%last[k][j].id%}" > ');
		type_monitor_template.push('						</div> ');
		type_monitor_template.push('					</div>$} ');
		type_monitor_template.push('	 				} ');
		type_monitor_template.push('	{$		</div> ');
		type_monitor_template.push('			</div> ');
		type_monitor_template.push('		</div>$} ');
		type_monitor_template.push('	 } ');
		type_monitor_template.push('	{$</div> ');
		type_monitor_template.push('</div> $}');
		
		
		var _default = {
				target_expression : '#deployList .os_log_button',
				target_monitor_expression : '#deployList .os_monitor_button',
				type_table_url : '',
				type_table_key : 'deploy_id',
				type_table_data : {},
				type_info_url : '',
				load_image_url : '',
				errorTip : function(msg){
					alert(msg);
				},
				openDialog : function(html){
					return null;
				}
		};
		var fetchLogAjax=function(_id,options,_isFirst){
			var _data = $.extend(true,{},options.type_table_data,{
				'logId' : _id,
				'first' : _isFirst
			});
			var def = $.ajax({
				url : options.type_info_url,
				method : 'post',
				dataType : 'json',
				data : _data
			});
			return def;
		};
		var fetchLog=function(_id,_aid,options){
			var logid = _id;
			var textId = _aid;
			(function(lid,_f,_options,_textId){
				function r(_logid,__options,__textId,_isFirst){
					var _lid=_logid;
					var req = _f(_lid,__options,_isFirst);
					req['done'](function(msg){
						if($('#'+__textId).size()==0){
							//window close
							return ;
						}
						if(msg.code==0){
							for(var i=0; i<msg.data.data.length;i++){
								$('#'+__textId).append('<div style="margin-top:5px;"><span style="line-height: 15px;font-size: 12px;color: #5e5e5e;">'+msg.data.data[i].message+'</span></div>');
							}
							if(msg.data.latest!=null){
								_lid = msg.data.latest;
							}
							if(msg.data.isEnd!=null && msg.data.isEnd){
								$('#'+__textId+'_load').remove();
								return ;
							}
						}
						setTimeout(function(){
							r(_lid,__options,__textId,false);
						},3000);
					});
				}
				r(lid,_options,_textId,true);
			})(logid,fetchLogAjax,options,textId);
		};
		var _historyInfo = function(options){
			$('.os-log-info').bind('click',function(){
				var _id = $(this).attr('attrId');
				var _aid = 'log_area_'+_id;
				options.openDialog({
					'title' : '日志详情',
					'html' : '<div style="width:100%;height:100%;"><div id="'+_aid+'"  style="padding-left:15px;">'+
						'</div><div id="'+_aid+'_load" style="padding-left:15px;"><img src="'+options.load_image_url+'"/></div></div>',
					'width' : 700,
					'height' : 600
				});
				fetchLog(_id,_aid,options);
				
			});
		};
		var _init = function(options,_type){
			options = $.extend(true,{},_default,options);
			
			var _pd = options.target_expression.split(' ');
			if( _type!=null&&_type=='monitor'){
				_pd = options.target_monitor_expression.split(' ');
			}
			$(_pd[0]).delegate(_pd[1],'click',null,function(){
				var _self = this;
				var _data = {};
				for(var i=0;i<_self.attributes.length;i++){
					var _n = _self.attributes.item(i).name;
					var _v = _self.attributes.item(i).value;
					if(_n==null || _n.indexOf('data_')!=0){
						continue ;
					}
					var _k = _n.substring(5);
					_data[_k] = _v;
				}
				_data = $.extend(true,{},options.type_table_data,_data);
				var _param = {
						url : options.type_table_url,
						method : 'post',
						dataType : 'json',
						data : _data
				};
				if(_type !=null && _type=='monitor'){
					_param = $.extend(true,_param,{
						url : options.type_monitor_pre_url
					});
				}
				var def = $.ajax(_param);
				var load = common.loading('获取数据......');
				def['done'](function(json){
					load.close();
					if(json.code==0){
						if(_type!=null && _type=='log'){
							var _html = (new Tmpl(type_table_template.join(''))).render(json);
							options.openDialog({
								'title' : '操作日志',
								'html' : _html,
								'width' : 500,
								'height' : 400
							});
							_historyInfo(options);
						}
						else if(_type !=null && _type=='monitor'){
							renderMonitor(json,options);
						}
					}
					else{
						options.errorTip(json.msg);
					}
				});
				
			});
		};
		function fetch(lastest,counts,_options){
			var _d = $.extend(true,{},{
				'endpointCounters' : counts
			},_options.type_table_data);
			var _o = {
					url : _options.type_monitor_data_url,
					cache : false,
					data : _d,
					dataType : 'json',
					type : 'POST'
			};
			if(lastest!=null){
				_o = $.extend(true,_o,{
					data : {
						'start' : lastest
					}
				});
			}
			
			var req = $.ajax(_o);
			return req;
        }
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
		function renderMonitor(data,options){
	//		console.log(data);
			var _j = data.data;
			var _dialog_id = (new Date()).getTime();
			_j.dialog_id = _dialog_id;
			options.dialog_id = _dialog_id;
			var _html = (new Tmpl(type_monitor_template.join(''))).render(_j);
			options.openDialog({
				'title' : '容器监控信息',
				'html' : _html,
				'width' : 990,
				'height' : 600
			});
			for(var k in data.data.last){
				for(var j=0;j<data.data.last[k].length;j++){
					var _id = data.data.last[k][j].id;
					var _name = data.data.last[k][j].name;
					var _lines = data.data.last[k][j].lines;
					
					var $dom=$('#'+_id);
					var _series = [];
	
					for(var _lk in _lines){
						var _info = {
								'name' : _lines[_lk],
             	'id' : _lk,
             	'data' : []
						};
						_series.push(_info);
					}
					var charOptions = (function(_graph,__series){
						var _c = {
							chart: {
		 						events : {
		 							load: function () {
		 								var id = _graph.id;
		 								var series = this.series;
		 								for(var i=0;i<series.length;i++){
		 									var _i = series[i].options.id.replace(/\.|\/|=/g,'_');
		 									$('#'+id).data(_i,series[i]);
		 								}
		 							}
		 						}
		 					},
		 					series : __series,
		 					title: {
		 						text: _graph.name,
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
						if(_graph.name.indexOf('内存')>-1){
							_c = $.extend(true,_c,{
								yAxis : {
									labels : {
										formatter : function(){
											var _m = (parseFloat(this.value)/1024)/1024;
											return roundFun(_m,3)+'M';
										}
									}
								}
							});
						}
						return _c;
					})(data.data.last[k][j],_series);
					$dom.data('option',charOptions);
				}
			}
			
			var req  = fetch(null,data.data.counts,options);
			req['done'](function(msg){
				if(msg.code==0){
					for(var i=0;i<msg.data.length;i++){
						var info = msg.data[i];
						var _endpoint = info['endpoint'];
						var _counter = info['counter'];
						var _id = data.data.graph2Id[_endpoint+'_'+_counter];
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
					options.errorTip(msg.info?msg.info:'获取数据出错，重新刷新页面');
				}
        	});
			req['done'](function(msg){
				if(msg.code==0){
					(function(_data){
						function next(ll){
						//	console.log($('#'+options.dialog_id));
							if($('#'+options.dialog_id).size()==0){
								return ;
							}
							var req1  = fetch(ll,data.data.counts,options);
							req1['done'](function(resp){
								if(resp.code==0){
									for(var i=0;i<resp.data.length;i++){
										var _info = resp.data[i];
										var _endpoint = _info['endpoint'];
										var _counter = _info['counter'];
										var _id = data.data.graph2Id[_endpoint+'_'+_counter];
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
		}
		return {
			init : function(options){
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
				Highcharts.setOptions({
					global: {
						useUTC: false
					}
				});
				setTimeout(function(){
					_init(options,'log');
				},1);
				setTimeout(function(){
					_init(options,'monitor');
				},1);
				
			}
		}
}();

