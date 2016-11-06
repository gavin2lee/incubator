var previous_version='previous_version';
var previous_analysis='previous_analysis';
var previous_days='days';
//获取period,返回数字
function getPeriod(jsonData,pk){
	if(jsonData){
		for(var key in jsonData){
//			if(/p[0-9]/.test(key) && jsonData[key]==pk){
			if(key == pk){
				return {
//					nb : key.replace('p',''),
					p : jsonData[key+'p'] ? jsonData[key+'p'] : "",
					date : jsonData[key+'d'],
					desc : jsonData[pk].replace('_',' ')
				};
			}
		}
	}
	return null;
}
__metrics = null;
function getSonarMetrics(metrics,callback) {
	var resultJson = findIssuesInfoByAjax(
			"GET",
			WEB_APP_PATH + "/app/version/codereport/getSonarMetrics.do",
			{
				projectKey : projectKey,
				metrics : metrics,
				includetrends : true
			}, "json");
	__metrics = {};
	var duplicationJson = JSON.parse(resultJson.historyDuplicationJson); 
	initHistoryDuplicationSelected(duplicationJson);
	var compareJson = JSON.parse(resultJson.historyCompareJson); 
	var typeArr = ['INT','PERCENT','FLOAT','RATING','WORK_DUR','PERCENT'];
	$.each(compareJson.metrics, function(i, v) {
		if(!v.hidden && /*v.domain == "Issues" && */v.key.indexOf("new") == -1){
			var foo = __metrics[v.domain];
			if (!foo)
				foo = [];
			if (typeArr.indexOf(v.type) > -1) {
				foo.push(v);
				__metrics[v.domain] = foo;
			}
		}
	});
	if (typeof callback === 'function') {
		callback();
	}
}

function initHistoryDuplicationSelected(json){
	if(typeof json == undefined){
		throw "json数据异常!!!";
	}
	var resultHtml = "";
	if(json.length == 0){
		return ;
	}
	var msrArr = json[0].msr;
	for(var i = 0;i<msrArr.length;i++){
		if(msrArr[i].hidden == "false"){
			resultHtml = resultHtml + "<option value="+msrArr.key+">"+msrArr.key+"</option>";
		}
	}
	$("#duplicationMetricsOptgroup").html(resultHtml);
}

//历史比较图双方metric，可在页面覆盖以自定义
/*var __cur_metric = {
		origin : {
			containerId : 'duplicationMetrics',
			key : "duplicated_lines_density",
			name : "Duplicated lines (%)",
			domain : "Duplication",
			type : "PERCENT"
		},
		comparer : {
			containerId : 'allMetrics', //下拉框ID
			key : "",
			name : "",
			domain : "",
			type : ""
		}
};*/
function LineChartComparer(opt){
	this.cur_metric = opt.cur_metric;
	this.metrics = opt.metrics;
	this.container = opt.container;
	this.init();
}

$.extend(true,LineChartComparer.prototype,{
	genSelector : function(metric,needNull){
		var allMetrics = '';
		var _this = this;
		if(needNull){
			allMetrics = '<option value="">--对比--</option>';
		}
		for(var key in _this.metrics){
			if(!needNull && metric.domain.indexOf(key) == -1){
				continue;
			}
			allMetrics += '<optgroup label="'+key+'">';
			$.each(_this.metrics[key],function(i,v){
				allMetrics += '<option value="'+v.key+'" metric-type="'+v.type+
				'" metric-domain="'+v.domain+'">'+v.name+'</option>';
			});
			allMetrics += '</optgroup>';
		};
		allMetrics += '</optgroup>';
		$("#"+metric.containerId).html(allMetrics);
		$("#"+metric.containerId).change(function(){
			var foo = $("#"+metric.containerId).find("option:selected");
			metric.key = foo.val();
			metric.name = foo.html();
			metric.domain = foo.attr('metric-domain');
			metric.type = foo.attr('metric-type');
			if(!needNull){
				_this.cur_metric.origin = metric;
			}else{
				_this.cur_metric.comparer = metric;
			}
			_this.genLineChart();
		});
	},
	init : function(){
		this.genSelector(this.cur_metric.origin,false);
		this.genSelector(this.cur_metric.comparer,true);
		this.genLineChart();
	},
	genLineChart : function (){
		var _this = this;
		var metrics = this.cur_metric;
		var metricStr = metrics.origin.key+(metrics.comparer.key ? (","+metrics.comparer.key) : "");
		$.get(WEB_APP_PATH+"/app/version/codereport/getDuplicationHistoryData.do",{
			projectKey : projectKey,
			metrics : metricStr
		},function(data){
			var historyData = [];
			var cols = data[0].cols;
			var cells = data[0].cells;
			var dataArray = [];
			var timeArray = [];
			$.each(cells,function(i,v){
				$.each(v.v,function(j,w){
					if(!dataArray[j])	dataArray[j] = new Array();
					dataArray[j].push([new Date(v.d).getTime(),w]);
				});
				timeArray.push(v.d);
			});
			$(_this.container).html('');
			$.each(cols,function(i,v){
				var metric = metrics.origin;
				if(v.metric != metrics.origin.key){
					metric = metrics.comparer;
				}
				var chartData = [{
					name : v.metric,
					data : dataArray[i]
				}];
				var id = "lineChart"+i;
				$(_this.container).append('<div id="'+id+'"></div>');
				$("#"+id).highcharts({
					chart : {
						height: 275
					},
			        title: {
			            text: ''
			        },
			        xAxis: {
			        	type: 'datetime',
			            dateTimeLabelFormats: { // don't display the dummy year
			            	minute: '%H:%M',
			            	hour: '%H:%M',
			            	day: '%e %b',
			            	week: '%e %b',
			            	month: '%b \'%y',
			            	year: '%Y'
			            },
			            title: {
			                text: ''
			            }
			        }, 
			        yAxis: {
			        	labels: {
			        		formatter : function(){
			        			if(metric.type == 'PERCENT'){
			        				return this.value+"%";
			        			}else{
			        				return this.value;
			        			}
			        		}
			        	},
			            title: {
			                text: ''
			            }
			        },
			        legend: {
			            layout: 'vertical',
			            align: 'right',
			            verticalAlign: 'middle',
			            borderWidth: 0,
			            enabled : false
			        },
			        tooltip: {
			            headerFormat: '<b>{series.name}</b><br>',
			            pointFormatter: function(){
			            	var foo = new Date(this.x).format("yyyy年M月d日")+'<br/>'+
			            		(metric.type == 'PERCENT' ? this.y+"%" : this.y);
			            	return foo;
			            }
			        },
			        series: chartData
			    });
			});
			removeHighchartLogo();
		});
	}
});
function genBubble(container){
	$.get(WEB_APP_PATH+"/app/version/codereport/getDuplicationFilesBubble.do",{
		projectKey : projectKey
	},function(data){
		var bubbleData = [];
		for(var i=0;i<data.length;i++){
			var obj = data[i];
			if(obj.qualifier != 'FIL')	continue;
			var x=0,y=0,z=0;
			if(obj.msr){
				$.each(obj.msr,function(i,v){
					if(v.key == 'ncloc')	x = v.val;
					if(v.key == 'duplicated_lines')		y = v.val;
					if(v.key == 'duplicated_blocks')	z = v.val;
				});
			}
			bubbleData.push({
					id : obj.id,
					name: obj.name,
					uuid : obj.uuid,
					key : obj.key,
					x : x,
					y : y,
					z : z
			});
		}
	    $(container).highcharts({
	        chart: {
	            type: 'bubble',
	            plotBorderWidth: 1,
	            zoomType: 'xy'
	        },
	        legend: {
	            enabled: false
	        },
	        title: {
	            text: ''
	        },
	        subtitle: {
	            text: ''
	        }, 
	        xAxis: {
	            gridLineWidth: 1,
	            title: {
	                text: '代码行数'
	            },
	            labels: {
	                format: '{value}'
	            }
	        },
	        yAxis: {
	            startOnTick: false,
	            endOnTick: false,
	            title: {
	                text: '重复行数'
	            },
	            labels: {
	                format: '{value}'
	            },
	            maxPadding: 0.2
	        },
	        tooltip: {
	            useHTML: true,
	            headerFormat: '<table>',
	            pointFormat: '<tr><th colspan="2"><h3>{point.name}</h3></th></tr>' +
	                '<tr><th>代码行数:</th><td>{point.x}</td></tr>' +
	                '<tr><th>重复行数:</th><td>{point.y}</td></tr>' +
	                '<tr><th>重复块:</th><td>{point.z}</td></tr>',
	            footerFormat: '</table>',
	            followPointer: false
	        },
	        series: [{
	            data: bubbleData,
	            events : {
	            	click : function(dom){
	            		var options = dom.point.options;
	            		var data = {
	            				title : "文件问题详情",
	            				key : options.key,
	            				uuid : options.uuid,
	            				projectKey : projectKey
	            		};
	            		FileIssues.sourcesShow(data);
	            	},
	            	cursor : "pointer"
	            }
	        }]
	    });
	    removeHighchartLogo();
	});
}

/**
* @author weiyujia
* @Description: 组装水平柱状图入口
* @param container 所需要组装的容器
* @param jsonData  所需要组装的数据源
* @param paramObject 属性设置参数，具体属性含义请到www.hcharts.cn/api/index.php查询(暂时只支持部分参数，后期有需要会完善)
* @date 2016年3月8日 下午8:08:37     
*/
function genBarChar(container, jsonData , paramObject) {
	$(container).highcharts({
		chart : {
			type : 'bar'
		},
		title: {                                                           
            text: null                    
        }, 
        subtitle: {                                                        
            text: null                                 
        },
		xAxis : {
			gridLineWidth : 0,
			categories : jsonData.categories,
			labels:{
				name : null,
		        enabled:true
		    }
			
		},
		yAxis: {
			lineWidth: 0,
			gridLineWidth : 0,
			title: {
                text: null
            },
		    labels:{
		        enabled:false
		    }
		},
		plotOptions : {
			bar : {
				dataLabels : {
					enabled : true,
					formatter: function() {
						if(jsonData.percent){
							return this.y+'%';
						}else{
							return this.y
						}
	                }
				}
				
			}
		},
		legend: {
            enabled : false,
            layout: 'horizontal',
        },
		credits : {
			enabled : true
		},
		series : [ {
			name : paramObject ? paramObject.series.name : " ",
			data : jsonData.data
		} ]
	});
	removeHighchartLogo();
}

/**
* @author weiyujia
* @Description: 组装垂直柱状图入口
* @param container 所需要组装的容器
* @param jsonData  所需要组装的数据源
* @param paramObject 属性设置参数，具体属性含义请到www.hcharts.cn/api/index.php查询
* @date 2016年3月8日 下午8:08:37     
*/
function genColumnChar(container, jsonData) {
	var params = {
		chart : {
			type : 'column'
		},
		title : {
			text : null
		},
		/*tooltip : {
			formatter : function() {
				var tipDoom = this.x + " functions have complexity arount "
						+ this.y;
				return tipDoom;
			}
		},*/
		/*subtitle : {
			text : null
		},*/
		xAxis : {
			gridLineWidth : 0,
			categories : jsonData.categories
		},
		yAxis : {
			lineWidth : 0,
			enabled : true,
			min : 0,
			gridLineWidth : 0,
			title : {
				text : null
			}
		},
		plotOptions : {
			column : {
				pointPadding : 0.2,
				borderWidth : 0
			},
			bar : {
				dataLabels : {
					enabled : true
				}
			}
		},
		legend : {
			enabled : false
		},
		series : [ {
			name : null,
			data : jsonData.data

		} ]
	}
	if(typeof jsonData.tooltip === "function"){
		params.tooltip = {
				formatter : jsonData.tooltip
		}
	}
	$(container).highcharts(params);
	removeHighchartLogo();
}


//初始化重复率占比饼状图

function getPieChar(val,container,size) {
	if (val) {
		val = parseFloat(val.replace("%", ""));
	    $(container).radialIndicator({
            barColor: '#87CEEB',
            radius:size,
            barWidth: 3,
            initValue: 0,
            roundCorner : true,
            percentage: true
        });
        var radialObj = $(container).data('radialIndicator');
        radialObj.animate(val);
	}
}