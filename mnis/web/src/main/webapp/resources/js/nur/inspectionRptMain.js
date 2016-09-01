var relationReport = [],
	compareData = {},
	_columns = [],
	checkArr = [],
	_data=[],
	R = {},
	isFit = true;
function initMethod(){

}
function sta(value,index,row){
	var str = '';
	switch(value){
		case 'WAIT':
			str = '待执行';
			break;
		case 'EXE':
			str = '已执行未报告';
			break;
		default:
			str = '已报告';
	}
	return str;
}

/*
* 数据表格加载完毕后触发
* 初始化tooltip	
*/
function loadComplete(data){
	var relationTip = $(".relationTip");

	if(!relationTip || relationTip.length == 0){
		return ;
	}
	relationTip.each(function(index){
		var that = $(this);
		if( $(this).attr('class').indexOf('tooltip') >= 0 ){
			//销毁被创建的tip panel释放内存 并解决对比数据乱入的情况
			$(".tooltipPanel").each(function(){
				$(this).panel('destroy');
			});
			that.tooltip('destroy');
		}
		$(this).tooltip({
			position:'left',
		    content: $('<div class="tooltipPanel"></div>'),//赋予一个class便于查找
		    onDestroy:function(){
		    	//删除后操作
		    },
		    onUpdate: function(content){
		    	var i = index;
		    	var d = $("#info-tab").datagrid('getRows');
		    	var row = d[i];
		    	var relationArr = getRelateReport(row.inHospNo,row.subject);
		    	var relationStr = '';//关联的报告字符串 返回给content
		    	var len = relationArr[0].length;
		    	if(len <= 1){
		    		relationStr = '<center>无对比数据</center>';
		    	}
		    	else{
		    		relationStr += '<div id="relationBox'+i+'" class="relationBox"><ul>';
		    		for(var idx=0;idx<len;idx++){
		    			if( row.masterId == relationArr[0][idx][0] ){
		    				relationStr += '<li class="relationItem"><input type="checkbox" value="'+relationArr[0][idx][0]+'" id="'+relationArr[0][idx][0]+i+'" checked="checked" disabled="disabled"><label for="'+relationArr[0][idx][0]+i+'"><span style="display:inline-block;">'+relationArr[1][idx][0]+'</span></label></li>'
		    			}
		    			else{
		    				relationStr += '<li class="relationItem"><input type="checkbox" value="'+relationArr[0][idx][0]+'" id="'+relationArr[0][idx][0]+i+'" ><label for="'+relationArr[0][idx][0]+i+'"><span style="display:inline-block;">'+relationArr[1][idx][0]+'</span></label></li>';	
		    			}	
		    		}
		    		relationStr += '</ul></div>';
		    		relationStr += '<div style="text-align:right;"><a href="#" class="easyui-linkbutton" style="width:50px;" onclick="compare('+i+')">对比</a></div>'
		    	}
		    	content.panel({
		            width: 160,
		            border: false,
		            content:relationStr
		        });
		        
		    },
		    onShow: function(e){
		        var t = $(this);
		        //TODO tip框显示位置的不合理

		        //t.tooltip('hide');
		        //var h = $(".content").height();
		        //var pY = t.offset().top;
		        /*if( pY > h/2){
		        	$(this).tooltip({
		        		position:'top'
		        	});
		        	
		        }
		        else{
		        	$(this).tooltip({
		        		position:'bottom'
		        	});
		        }
		        t.tooltip('show');*/
		        /*t.tooltip('tip').css({
		        	backgroundColor:'#e0e4e8'
		        });*/
		        t.tooltip('tip').unbind().bind('mouseenter', function(){
		            t.tooltip('show');
		        }).bind('mouseleave', function(){
		            t.tooltip('hide');
		        });
		    }
		});
	});
}

function action(value,row,index){
	return '<div class="relationTip" style="width:50px;text-align:center;border:1px solid #95B8E7;padding:0px 2px;">关联对比</div>'
}

/*function op(value,row,index){
	
	var tmpArr = getRelateReport(row.inHospNo,row.subject);
	var sHtml = '';
	if( tmpArr.length == 1 ){
		R[index] = '<li><input class="relateCheckItems" type="checkbox" id="'+ tmpArr[0][i] +'"><label for="'+tmpArr[0][i]+'"><span style="display:inline-block;"> '+ tmpArr[1][i] + '</span></label></li><li><span>无对比数据</span></li>';
		
	}
	else{
		var relateList = '';
		for(var i=0;i<tmpArr[0].length;i++){
			//console.log(tmpArr[0][i] +','+ row.masterId)
			if(tmpArr[0][i] == row.masterId){
				relateList +='<li><input class="relateCheckItems" type="checkbox" id="'+ tmpArr[0][i] +'" disabled="disabled" checked="checked"><label for="'+tmpArr[0][i]+'"><span style="display:inline-block;"> '+ tmpArr[1][i] + '</span></label></li>';
			}
			else{
				relateList +='<li><input class="relateCheckItems" type="checkbox" id="'+ tmpArr[0][i] +'"><label for="'+tmpArr[0][i]+'"><span style="display:inline-block;"> '+ tmpArr[1][i] + '</span></label></li>';
			}
		}
		R[index] = relateList;
		//$("#related-report-box ul").html(relateList);
	}
	var btnStr = '<div><input type="button" value="关联对比" class="related-report" data-value="'+ index +'" style="float:left;height:20px;margin-right:5px;"/> ';
	return btnStr +"</div>";
}
*/
function follow1(val,row){
	var sHtml = '<span class="starbox"><i class="fl star"></i>'+ val +'</span>'
	return sHtml;
}
function relatedReport(val,row){
	var sHtml = '<span class="related-report">'+ val +'<i class="related-report-arrow"></i></span>';
	return sHtml;
}

/*
* 对比
*/
function compare(idx){
	//初始化
	checkArr = [],compareData = {};
	var rl = $("#relationBox"+idx+" ul li");
	rl.each(function(){
		var node = $(this).find('input[type="checkbox"]');
		if( node.attr('checked') == 'checked' ){
			checkArr.push(node.val());
		}
	});
	// datagrid参数
	_columns = [[
			{field:'bodyParts',align:'center',title:'检查部位',width:120,rowspan:2,styler:function(value,row,index){return 'line-height:20px;font-size:14px;';}}
		],[

		]
	];
	var tmpStr = '',
		tmpArr = [],
		tmpObj = {},
		reporter='',
		applicant='',
		applicantTime = '',
		auditor='',
		auditorTime='',
		tmpIndex = 0;
	//是否自适应
	isFit = (checkArr.length > 2) ? false : true;
	//开始循环
	console.log(_data);
	for(var i=0;i<checkArr.length;i++){
		for(var n=0;n<_data.length;n++){
			if( _data[n].masterId == checkArr[i]){
				tmpIndex = n;
				if( !compareData.subject ){
					
					compareData.bedCode    = _data[n].bedNo;
					compareData.subject    = _data[n].subject;
					compareData.inHospNo   = _data[n].inHospNo;
					compareData.patName    = _data[n].patName;
					compareData.itemList   = [{}];
					compareData.itemList[0].bodyParts = _data[n].bodyParts;
				}
				compareData.itemList[0]['inspectionResult'+i] = _data[n].detailList[0].inspectionResult;
				compareData.itemList[0]['inspSuggestion'+i] = _data[n].detailList[0].inspSuggestion;
			}
		}
		
		tmpStr =reporter +' '+_data[i].reportTime.substring(5);

		applicant = _data[tmpIndex].applicant ? _data[tmpIndex].applicant : '无';
		applicantTime =  _data[tmpIndex].applicantTime ? _data[tmpIndex].applicantTime.substring(5) : '无';
		reporter = _data[tmpIndex].reporter ? _data[tmpIndex].reporter : '无';
		reportTime = _data[tmpIndex].reportTime ? _data[tmpIndex].reportTime.substring(5) : '无';
		//目前审核者暂定为报告者
		auditor = _data[tmpIndex].checkName ? _data[tmpIndex].checkName : '无';
		auditorTime = _data[tmpIndex].checkDate ? _data[tmpIndex].checkDate.substring(5) : '无';
		tmpArr = '<p>申请者：<span style="display: inline-block;min-width: 40px;">'+applicant+'</span> 时间：'+applicantTime+'</p>'+'<p>报告者：<span style="display: inline-block;min-width: 40px;">'+reporter+'</span> 时间：'+reportTime+'</p><p>审核者：<span style="display: inline-block;min-width: 40px;">'+auditor+'</span> 时间：'+auditorTime+'</p>';
		_columns[0].push({title:tmpArr,colspan:2,width:300,align:'center'});
		_columns[1].push(
			{field:'inspectionResult'+i,title:'检查所见',width:170,sortable:true,align:'left',halign:'center',styler:function(value,row,index){return 'line-height:20px;padding-top:8px;padding-bottom:8px';}},
			{field:'inspSuggestion'+i,title:'检查意见',width:150,align:'left',halign:'center'}
		);
	}

	$("#cPatientBedCode").html(compareData.bedCode+'床');
	$("#cPatientName").html(compareData.patName);
	$("#cPatientId").html(compareData.inHospNo);
	$("#cCheckItem").attr('title',compareData.subject).html(compareData.subject);
	//$("#cBodyParts").attr('title',compareData.itemList[0].bodyParts).html('检查部位：'+compareData.itemList[0].bodyParts);
	$("#related-report-compare").dialog('open');
	$("#compare-tab").datagrid({
		height:365,
		fitColumns:isFit,
		singleSelect : true,
		nowrap:false,
		collapsible: true,
		striped:true,
		data:compareData.itemList,
		columns:_columns,
		onBeforeLoad:function(data){
			//获取头部列属性 修改宽度换行  根据field来选择
			var dhr1 = $('#related-report-compare .datagrid-htable .datagrid-header-row:eq(0)');
			dhr1.find("td[colspan=2] div p").css('margin-top','0px');
			dhr1.find("td[colspan=2] div").css({
				'height':'100%',
				'white-space': 'normal'
			});
		}
	});
}

/*
* 获取检查报告
* 
*/
function getInspectionRpt(){

	_data = [];
	var startDate = $("#startDate").val();
	var endDate   = $("#endDate").val();
	var status    = $("#status").val();
	var pl 		  = parent.peopleList;
	if(_patientId){
		pl = [_patientId];
	}
	if(startDate){
		startDate = '&startDate='+startDate;
	}
	if(endDate){
		endDate = '&endDate='+endDate;
	}
	if(status){
		status = '&status='+status;
	}
	if(!pl){
		return;
	}

	//销毁relationBox
	destroyRelationBox($(".relationTip"));

	$.post(ay.contextPath+'/nur/inspectionRpt/getInspectionRdsByCond.do?patientIds='+pl+startDate+endDate,{},function(data){
		var list = data.data ? data.data.list : [];
		for( var idx=0;idx<list.length;idx++){
			_data.push(list[idx]);
			_data[idx].bodyParts = _data[idx].detailList[0].bodyParts
		}
		relationReport = [];
		for( var i=0;i<_data.length;i++){
			if( i == 0 ){
				relationReport.push({
					"inHospNo"  : _data[i].inHospNo,
					"subject"  : _data[i].subject,
					"reportTime" : [_data[i].reportTime],
					"list"	     : [_data[i].masterId],
					"bodyParts"  : _data[i].bodyParts,
				});
			}
			else{
				var num = isHas(_data[i].inHospNo,_data[i].subject,_data[i].bodyParts);
				if(  num >= 0 ){
					relationReport[num].list.push(_data[i].masterId);
					relationReport[num].reportTime.push(_data[i].reportTime);
				}
				else{
					relationReport.push({
						"inHospNo": _data[i].inHospNo,
						"subject": _data[i].subject,
						"reportTime" : [_data[i].reportTime],
						"list"	   : [_data[i].masterId],
						"bodyParts"  : _data[i].bodyParts,
					});
				}
			}
		}
		$("#info-tab").datagrid({
			fit:true,
			fitColumns:true,
			singleSelect : true,
			data:_data,
			onCheck:function(index,data1){

				applicant = data1.applicant ? data1.applicant : '无';
				var reporter = data1.reporter ? data1.reporter : '无';
				auditor = data1.checkName ? data1.checkName : '无';
				auditorTime = data1.checkDate ? data1.checkDate : '无';
				inspectionTime = data1.inspectionTime ? data1.inspectionTime : '无';
				var applicantTime = data1.applicantTime ? data1.applicantTime : '无';
				reportTime = data1.reportTime ? data1.reportTime : '无';
				var tmpStr = '<span style="margin-left:20px;">申请者：<span style="display: inline-block;min-width: 20px;">'+applicant+'</span> 申请时间：'+applicantTime+'</span><span style="margin-left:20px;">'+'报告者：<span style="display: inline-block;min-width: 20px;margin-left:5px;">'+reporter+'</span> 报告时间：'+reportTime+'</span>';
				$("#report-info-box").dialog('open');
				$("#reporterInfo").html(tmpStr);
				$("#patientBedCode").html((data1.bedNo ? data1.bedNo : '')+'床');
				$("#patName").html((data1.patName ? data1.patName : ''));
				$("#patientId").html((data1.inHospNo ? data1.inHospNo : ''));
				$("#checkItem").attr('title',data1.subject).html(data1.subject);
				$("#report-info-box").show();
				$("#inspectionResult").val(data1.detailList[0].inspectionResult)
				$("#inspSuggestion").val(data1.detailList[0].inspSuggestion)
			}
		});
		/*$('.related-report').click(function(){
			$('.related-report-box ul').html(R[$(this).attr('data-value')]);
			var pos = $(this).position();
			$('.related-report-box').css({
				display:'block',
				top:pos.top+44+$(this).height(),
				right:20
			});
			return false;
		});*/
	});	
}

/*
* 取消优先级
* @params 行号 (datagrid行号)
*/
function cancelPRI(lineNum){
	var id = $("#info-tab").datagrid('getData').rows[lineNum].masterId;
	$.get(ay.contextPath+'/nur/inspectionRpt/setInspectionRdsPriFlag.do?id='+id+'&priFlag=0',function(data){
		if(data.success){
			$.messager.alert('提示','取消优先成功');
			getInspectionRpt();
		}
	});
	window.event.cancelBubble=true;
}

/*
* 销毁relationBox
*
*/
function destroyRelationBox(elm){
	if( !elm.each ){
		elm = $(elm);
	}
	elm.each(function(index,n){
		if($(this).tooltip){
			$(this).tooltip('destroy');	
		}
	});
}

/*
* 设置优先级
* @params 行号 (datagrid行号)
*/
function setPRI(lineNum){
	var id = $("#info-tab").datagrid('getData').rows[lineNum].masterId;
	$.get(ay.contextPath+'/nur/inspectionRpt/setInspectionRdsPriFlag.do?id='+id+'&priFlag=1',function(data){
		if(data.success){
			$.messager.alert('提示','设置优先成功');
			getInspectionRpt();
		}
	});
	window.event.cancelBubble=true;
}
/*
* 获取关联报告列表
* hospno 住院号 checkitem 项目名字 t 传入的时间
*/
function getRelateReport(hospNo,checkItem){
	//console.log(relationReport)
	var arr = [[],[]]
	for(var i=0;i<relationReport.length;i++){
		if(hospNo == relationReport[i].inHospNo && checkItem == relationReport[i].subject){
			arr[0].push(relationReport[i].list);
			arr[1].push(relationReport[i].reportTime);
		}
	}
	return arr;
}
/*
* 获取当前检查单在关联报告中的索引
* -1 为不存在  
* 其他值为已存在索引
*/
function isHas(hospNo,checkItem,bodyPart){
	//console.log(hospNo);
	var index = -1;
	for(var i=0;i<relationReport.length;i++){
		if(hospNo == relationReport[i].hospNo && checkItem == relationReport[i].subject && bodyPart == relationReport[i].bodyParts){
			index = i;
			return index;
		}
	}
	return index;
}

function closeReportCompare(){
	$("#related-report-compare").dialog('close');
}

$(function(){
	document.body.removeChild(document.body.childNodes[0]);
	$('.content').height( $(window).height() - $('.top-tools').height() );
	//设置患者信息容器宽度
	if(parent.setPatientInfoWrap){
		parent.setPatientInfoWrap();
	}
	if(_patientId){
		getInspectionRpt();
	}
	var recordDay = new Date().format("yyyy-MM-dd");
	$("#startDate").val(recordDay);
	$("#endDate").val(recordDay);

})