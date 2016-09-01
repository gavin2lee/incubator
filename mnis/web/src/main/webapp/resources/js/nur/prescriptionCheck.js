var orderDetail = [],
	_state      = 0,
	tempArr = [],
	cmenu = null,
	isClickFullBtn = false,
	patientIdList;

$(function(){
	$("#info-tab").datagrid();
});

function addCheckBox(val,row){
	var sHtml = '<input type="checkbox" style="position:relative;top:2px;margin:0 3px;">'+ val;
	return sHtml;
}

function setTime(){
	var deptCode = $("#deptCode", window.parent.document).val();
	var url = ay.contextPath + '/n.dour/orderManager/getFreqDefaultPlanTime.do?deptCode='+deptCode;
	$.get(url, function(data) {
		try {
			if(data.rslt==0){
				var result = data.lst;
				for(var i=0;i<result.length;i++){
					if(result[i].freqCode=="QD"){
						$("#qdStartDate").val(result[i].planTime);
					}else if(result[i].freqCode=="BID"){
						var arr = result[i].planTime.split(",");
						$("#bidStartDate1").val(arr[0]);
						$("#bidStartDate2").val(arr[1]);
					}else if(result[i].freqCode=="TID"){
						var arr = result[i].planTime.split(",");
						$("#tidStartDate1").val(arr[0]);
						$("#tidStartDate2").val(arr[1]);
						$("#tidStartDate3").val(arr[2]);
					}else if(result[i].freqCode=="QID"){
						var arr = result[i].planTime.split(",");
						$("#qidStartDate1").val(arr[0]);
						$("#qidStartDate2").val(arr[1]);
						$("#qidStartDate3").val(arr[2]);
						$("#qidStartDate4").val(arr[3]);
					}
				}
			}			
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});	
	$("#setTimeBox").dialog('open')
}

function searchPatCrisValue(){
	var condition1 = $("#select1").val()+"医嘱";
	var condition2 = $("#select2").val()+"医嘱";
	var arr = [];
	if(condition1=="全部医嘱"){
		arr = orderDetail;
	}else{
		for(var i=0,j=0;i<orderDetail.length;i++){
			if(orderDetail[i].orderTypeName==condition1){
				arr[j++]=orderDetail[i];
			}
		}
	}	
	$("#info-tab").datagrid({
		data : arr		
	});
}

function change(obj){

}

function searchOrder(){	
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var arr = [];
	if(startDate.length == 0){
		$.messager.alert('提示', '请选择开始时间！');
	}else{
		if(endDate.length ==0){
			endDate = new Date().format("yyyy-MM-dd hh:mm");
		}
		if(endDate<startDate){
			$.messager.alert('提示', '结尾时间不能比开始时间早！');
			return;
		}
		$('#info-tab').datagrid('loadData',{total:0,rows:[]}); 
		for(var i=0,j=0;i<orderDetail.length;i++){
			if(startDate<=orderDetail[i].createDate && orderDetail[i].createDate<=endDate){
				arr[j++]=orderDetail[i];
			}
		}
		$("#info-tab").datagrid({
			data : arr
		});
	}	
}

function decomposeOrder(){
	var row = "";
	row = $('#info-tab').datagrid('getSelections');
	if(row.length == 0){
		$.messager.alert('提示', '未选择或无可审核医嘱。');
		return;
	}
	var orderGroupNo="";
	for(var i=0;i<row.length;i++){
		orderGroupNo += row[i].orderGroupNo+",";
	}
	orderGroupNo = orderGroupNo.substring(0,orderGroupNo.length-1);	
	var url = ay.contextPath + '/nur/orderManager/decomposeOrderFromGroupNo.do';
	$.post(url, {			
		orderGroupNo : orderGroupNo			
	}, function(data) {
		try {
			$.messager.alert('提示', data.msg);	
			getPatOrderDetail(patientIdList);
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});	
}

function cancel(){
	$("#setTimeBox").dialog('close');
}

function getBedCode(val,row){
	/*var x = val.indexOf('_');
	return parseInt(val.substring(x+1));*/
	return '';
}

function getOrderBaseInfo(val,row){
	var len = val.length,
		str = '';
	if( 1 == len){
		str =  val[0].orderName;
		return str;
	}
	else{
		for(var i=0;i<len;i++){
			if(i == (len-1) ){
				str += val[i].orderName;
				return str;
			}
			str += val[i].orderName +'<br>';		
		}
		return str;
	}
}
function getStandard(val){
	var len = val.length,
		str = '';
	if( 1 == len){
		//str =  val[0].dosageUnit;
		str =  val[0].drugSpec;
		return str;
	}
	else{
		for(var i=0;i<len;i++){
			if(i == (len-1) ){
				//str += val[i].dosageUnit;
				str += val[i].drugSpec;
				return str;
			}
			//str += val[i].dosageUnit +'<br>';
			str += val[i].drugSpec +'<br>';		
		}
		return str;
	}
}
function getDosage(val){
	var len = val.length,
		str = '';
	if( 1 == len){
		str =  val[0].dosage;
		return str;
	}
	else{
		for(var i=0;i<len;i++){
			if(i == (len-1) ){
				str += val[i].dosage;
				return str;
			}
			str += val[i].dosage +'<br>';		
		}
		return str;
	}
}

function getTime(){
	var qdStarDate = $("#qdStartDate").val();
	var bidStartDate1 = $("#bidStartDate1").val();
	var bidStartDate2 = $("#bidStartDate2").val();
	var tidStartDate1 = $("#tidStartDate1").val();
	var tidStartDate2 = $("#tidStartDate2").val();
	var tidStartDate3 = $("#tidStartDate3").val();
	var qidStartDate1 = $("#qidStartDate1").val();
	var qidStartDate2 = $("#qidStartDate2").val();
	var qidStartDate3 = $("#qidStartDate3").val();
	var qidStartDate4 = $("#qidStartDate4").val();
	var result = "{\"orderFreqList\":[";
	var deptCode = $("#deptCode", window.parent.document).val();
	var deptName = $("#deptName", window.parent.document).val();
	var strDate = new Date().format("yyyy-MM-dd hh:mm:ss");
	if(qdStarDate.length != 0){
		result += "{\"freqCode\":\"QD\",\"deptCode\":\""+deptCode+"\",\"deptName\":\""+deptName+"\",\"planTime\":\""+qdStarDate+"\",\"createTime\":\""+strDate+"\"},";
	}
	if(bidStartDate1.length!=0 && bidStartDate2.length!=0){
		result += "{\"freqCode\":\"BID\",\"deptCode\":\""+deptCode+"\",\"deptName\":\""+deptName+"\",\"planTime\":\""+bidStartDate1+","+bidStartDate2+"\",\"createTime\":\""+strDate+"\"},";		
	}else if(bidStartDate1.length==0 && bidStartDate2.length==0){		
	}else{
		$.messager.alert('提示', 'BID必须全填写或全不填！');
		return;
	}
	if(tidStartDate1.length!=0 && tidStartDate2.length!=0 && tidStartDate3.length!=0){
		result += "{\"freqCode\":\"TID\",\"deptCode\":\""+deptCode+"\",\"deptName\":\""+deptName+"\",\"planTime\":\""+tidStartDate1+","+tidStartDate2+","+tidStartDate3+"\",\"createTime\":\""+strDate+"\"},";				
	}else if(tidStartDate1.length==0 && tidStartDate2.length==0 && tidStartDate3.length==0){		
	}else{
		$.messager.alert('提示', 'TID必须全填写或全不填！');
		return;
	}
	if(qidStartDate1.length!=0 && qidStartDate2.length!=0 && qidStartDate3.length!=0 && qidStartDate4.length!=0){
		result += "{\"freqCode\":\"QID\",\"deptCode\":\""+deptCode+"\",\"deptName\":\""+deptName+"\",\"planTime\":\""+qidStartDate1+","+qidStartDate2+","+qidStartDate3+","+qidStartDate4+"\",\"createTime\":\""+strDate+"\"},";				
	}else if(qidStartDate1.length==0 && qidStartDate2.length==0 && qidStartDate3.length==0 && qidStartDate4.length==0){		
	}else{
		$.messager.alert('提示', 'QID必须全填写或全不填！');
		return;
	}
	result = result.substring(0,result.length-1);
	result += "]}";
	var url = ay.contextPath + '/nur/orderManager/setFreqDefaultPlanTime.do';
	$.post(url, {			
		lst : result			
	}, function(data) {
		try {
			alert(data.msg);
			$("#setTimeBox").dialog('close');
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function getPatOrderDetail(s){
	if(s){
		patientIdList = s;
	}
	if(parent.peopleList){
		patientIdList = parent.peopleList.toString();
	}
	if(_patientId){
		patientIdList = _patientId;
	}
	if(patientIdList.length == 0){
		$('#info-tab').datagrid('loadData',{total:0,rows:[]}); 
		return;
	}
	orderDetail = [];
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var condition1 = $("#select1").val();
	var condition2 = $("#select2").val();
	$.post(ay.contextPath+'/nur/patOrderDetail/getOrderBaseGroupList.do?'+'patientId='+patientIdList,{
			//"patientId":patientIdList,
			//"startTime":startDate,
			//"endTime":endDate,
			//"orderTypeCode":'',//condition1,
			//"state":condition2
		},function(data){
		tempArr = data.data.list;
		for(var i=0;i<tempArr.length;i++){
			var standard = getStandard(tempArr[i].orderItems);
			var dosage = getDosage(tempArr[i].orderItems);
			tempArr[i]._standard = standard;
			tempArr[i]._dosage = dosage;
			orderDetail.push(tempArr[i]);
		}
		console.log(orderDetail);
		$("#info-tab").datagrid({
			/*data : orderDetail,*/
			fit : true,
			pagination:true,
			pageSize:20,
			remoteSort:false,
			sortName:"patientBedCode",
			rownumbers:true,
			columns : [ [ 
			{
				field:'cc',
				checkbox:true
			},
			{
				field : 'patientBedCode',
				title : '床号',
				width : 40,
				sortable:true,
				formatter:getBedCode
			},{
				field : 'patientName',
				title : '患者',
				sortable:true,
				width : 80
			}, {
				field : 'patientId',
				title : '住院号',
				sortable:true,
				width : 60
			}, {
				field : 'orderTypeName',
				title : '医嘱类型',
				sortable:true,
				width : 80
			}, {
				field : 'createDate',
				title : '开立时间',
				sortable:true,
				width : 150
			}, {
				field : 'createDoctorName',
				title : '开立医生',
				sortable:true,
				width : 70
			},{
				field : 'orderItems',
				title : '医嘱内容',
				sortable:true,
				width : 250,
				formatter:getOrderBaseInfo
			},{
				field : '_standard',
				title : '规格',
				sortable:true,
				width : 60
			},{
				field : '_dosage',
				title : '剂量',
				sortable:true,
				width : 100
			},{
				field : 'usageName',
				title : '用法',
				sortable:true,
				width : 60
			},{
				field : 'deliverFreq',
				title : '频次',
				sortable:true,
				width : 70
			}
			,{
				field : 'stopDate',
				title : '停止时间',
				sortable:true,
				width : 100
			},{
				field : 'stopDoctorName',
				title : '停止医师',
				sortable:true,
				width : 70
			},{
				field : 'comment',
				title : '备注',
				sortable:true,
				width : 100
			},{
				field : 'planExecTime',
				title : '执行时间',
				sortable:true,
				width : 100
			},{
				field : 'planFirstExecTime',
				title : '首次执行时间',
				sortable:true,
				width : 120
			},{
				field : 'c',
				title : '操作',
				width : 120,
				formatter:function(value,row,index){
                    var e = '<a href="#" onclick="editCellBox('+index+')">执行时间</a> ';
                    var d = '<a href="#" onclick="editFirstTime('+index+')">首次时间</a>';
                    var c = '';
                    return e+'<br>'+d+'<br>'+c;
            	}
			},{
				field : 'orderGroupNo',
				title : '分组号',
				hidden: true
			}
			 ] ],/*,
			 onClickRow:function(rowIndex, rowData){
			 	alert(rowIndex);
			 }*/
			onHeaderContextMenu: function(e, field){
                e.preventDefault();
                if (!cmenu){
                    createColumnMenu();
                }
                cmenu.menu('show', {
                    left:e.pageX,
                    top:e.pageY
                });
            }
		});
		$("#info-tab").datagrid({loadFilter:pagerFilter}).datagrid('loadData',orderDetail);
	})
}

function editFirstTime(index){
	$("#time1").empty();
	var str = "<input id=\"setFirstTime\" name=\"setFirstTime\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\">";
	$("#time1").append(str);
	var planFirstExecTime = $("#info-tab").datagrid('getData').rows[index].planFirstExecTime;
	if(planFirstExecTime!= null && planFirstExecTime.length!=0){	
		$("#setFirstTime").val(planFirstExecTime);
	}
	$("#time1").dialog({
		title : '设置执行时间',
		width : 300,
		height : 200,
		buttons : [ {
			text : '保存',
			handler : function() {	
				date = $("#setFirstTime").val();
				var url = ay.contextPath + '/nur/orderManager/setOrderDefaultPlanTime.do';
				var orderGroupNo = $("#info-tab").datagrid('getData').rows[index].orderGroupNo;
				var firstPlanExecTime = date;
				$.post(url, {			
					orderGroupNo : orderGroupNo,
					firstPlanExecTime : firstPlanExecTime
				}, function(data) {
					try {
					} catch (e) {
						$.messager.alert('提示', e);
					}
				});	
				$("#info-tab").datagrid('updateRow', {
					index : index,
					row : {
						planFirstExecTime : date
					}
				});
				$("#time1").dialog('close');
			}
		}, {
			text : '关闭',
			handler : function() {
				$("#time1").dialog('close');
			}
		} ]

	});		
}

function editCellBox(index) {
	$("#time1").empty();
	var str;
	var frq;
	var date;
	var planExecTime = $("#info-tab").datagrid('getData').rows[index].planExecTime;
	var arrTime;
	if(planExecTime !=null && planExecTime.length!=0){	
		arrTime = planExecTime.split(",");
	}
	if($("#info-tab").datagrid('getData').rows[index].deliverFreq.substring(0,2)=="QD"){
		frq="QD";
		str="<input id=\"setTime1\" name=\"setTime1\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\">";
		$("#time1").append(str);
		$("#setTime1").val(arrTime);
	}else if($("#info-tab").datagrid('getData').rows[index].deliverFreq.substring(0,3)=="BID"){
		frq="BID";
		str="<input id=\"setTime1\" name=\"setTime1\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\"><br/>";
		str+="<input id=\"setTime2\" name=\"setTime2\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\">";
		$("#time1").append(str);
		$("#setTime1").val(arrTime[0]);
		$("#setTime2").val(arrTime[1]);
	}else if($("#info-tab").datagrid('getData').rows[index].deliverFreq.substring(0,3)=="TID"){
		frq="TID";
		str="<input id=\"setTime1\" name=\"setTime1\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\"><br/>";
		str+="<input id=\"setTime2\" name=\"setTime2\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\"><br/>";
		str+="<input id=\"setTime3\" name=\"setTime3\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\">";
		$("#time1").append(str);
		$("#setTime1").val(arrTime[0]);
		$("#setTime2").val(arrTime[1]);
		$("#setTime3").val(arrTime[2]);
	}else if($("#info-tab").datagrid('getData').rows[index].deliverFreq.substring(0,3)=="QID"){
		frq="QID";
		str="<input id=\"setTime1\" name=\"setTime1\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\"><br/>";
		str+="<input id=\"setTime2\" name=\"setTime2\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\"><br/>";
		str+="<input id=\"setTime3\" name=\"setTime3\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\"><br/>";
		str+="<input id=\"setTime4\" name=\"setTime4\" class=\"Wdate\" onclick=\"WdatePicker({readOnly:true,dateFmt:'HH:mm'})\" readonly=\"readonly\" style=\"width: 120px;\">";
		$("#time1").append(str);
		$("#setTime1").val(arrTime[0]);
		$("#setTime2").val(arrTime[1]);
		$("#setTime3").val(arrTime[2]);
		$("#setTime4").val(arrTime[3]);
	}	
	$("#time1").dialog({
		title : '设置执行时间',
		width : 300,
		height : 200,
		buttons : [ {
			text : '保存',
			handler : function() {
				if(frq=="QD"){
					if($("#setTime1").val().length != 0){
						date = $("#setTime1").val();
					}
				}else if(frq=="BID"){
					var result = isEmpty1($("#setTime1").val(),$("#setTime2").val());
					if(result == 1){
						date = $("#setTime1").val()+","+$("#setTime2").val();
					}else if(result == 0){
					}
					else{
						$.messager.alert('提示', 'BID必须全填写或全不填！');
						return;
					}
				}else if(frq=="TID"){
					var result = isEmpty2($("#setTime1").val(),$("#setTime2").val(),$("#setTime3").val());
					if(result == 1){
						date = $("#setTime1").val()+","+$("#setTime2").val()+","+ $("#setTime3").val();
					}else if(result == 0){
					}
					else{
						$.messager.alert('提示', 'TID必须全填写或全不填！');
						return;
					}
				}else if(frq=="QID"){
					var result = isEmpty3($("#setTime1").val(),$("#setTime2").val(),$("#setTime3").val(),$("#setTime4").val());
					if(result == 1){
						date = $("#setTime1").val()+","+$("#setTime2").val()+","+ $("#setTime3").val()+","+$("#setTime4").val();
					}else if(result == 0){
					}
					else{
						$.messager.alert('提示', 'QID必须全填写或全不填！');
						return;
					}
				}
				var url = ay.contextPath + '/nur/orderManager/setOrderDefaultPlanTime.do';
				var orderGroupNo = $("#info-tab").datagrid('getData').rows[index].orderGroupNo;
				var planExecTime = date;
				$.post(url, {			
					orderGroupNo : orderGroupNo,
					planExecTime : planExecTime
				}, function(data) {
					try {
					} catch (e) {
						$.messager.alert('提示', e);
					}
				});					
				$("#info-tab").datagrid('updateRow', {
					index : index,
					row : {
						planExecTime : date
					}
				});
				$("#time1").dialog('close');
			}
		}, {
			text : '关闭',
			handler : function() {
				$("#time1").dialog('close');
			}
		} ]

	});		
}

function isEmpty1(num1,num2){
	if(num1.length != 0 && num2.length != 0){
		return 1;
	}else if(num1.length == 0 && num2.length == 0){
		return 0;
	}else{
		return -1;
	}
}

function isEmpty2(num1,num2,num3){
	if(num1.length != 0 && num2.length != 0 && num3.length != 0){
		return 1;
	}else if(num1.length == 0 && num2.length == 0 && num3.length == 0){
		return 0;
	}else{
		return -1;
	}
}

function isEmpty3(num1,num2,num3,num4){
	if(num1.length != 0 && num2.length != 0 && num3.length != 0 && num4.length != 0){
		return 1;
	}else if(num1.length == 0 && num2.length == 0 && num3.length == 0 && num4.length == 0){
		return 0;
	}else{
		return -1;
	}
}

function xhrGetPatOrderDetail(n){
	$.post(ay.contextPath+'/nur/patOrderDetail/'+n+'/queryOrderGroupList.do',{},function(data){
		tempArr.push(data.obj.orderBaseGroupList);
		
	})
}

function createColumnMenu(){
    cmenu = $('<div/>').appendTo('body');
    cmenu.menu({
        onClick: function(item){
            if (item.iconCls == 'icon-ok'){
                $('#info-tab').datagrid('hideColumn', item.name);
                cmenu.menu('setIcon', {
                    target: item.target,
                    iconCls: 'icon-empty'
                });
            } else {
                $('#info-tab').datagrid('showColumn', item.name);
                cmenu.menu('setIcon', {
                    target: item.target,
                    iconCls: 'icon-ok'
                });
            }
        }
    });
    var fields = $('#info-tab').datagrid('getColumnFields');
    for(var i=0; i<fields.length; i++){
        var field = fields[i];
        var col = $('#info-tab').datagrid('getColumnOption', field);
        cmenu.menu('appendItem', {
            text: col.title,
            name: field,
            iconCls: 'icon-ok'
        });
    }
}

/*
* 客户端分页
*/
function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){    // is array
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if (!data.originalRows){
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
}

$(function(){
	//设置content区域高
	$('.content').height( $(window).height() - $('.top-tools').height() );
	if(_patientId){
		getPatOrderDetail();
	}
});

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}