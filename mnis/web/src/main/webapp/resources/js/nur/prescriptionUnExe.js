var editIndex = undefined;
var editRowData = undefined;
var select2 = null;
var orderDetail = [];
var patientIdList;
function setTime(){
	$("#setTimeBox").show();
}

/*function getBedCode(val,row){
	var x = val.indexOf('_');
	return parseInt(val.substring(x+1));
}*/

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
				
				if(val[i].riskFlag == 1){
					str = '<span class="risk risk'+val[i].riskLevel+'"></span>' + val[i].orderName;
				}
				else{
					str += val[i].orderName;
				}
				return str;
			}
			
			if(val[i].riskFlag == 1){
				str += '<span class="risk risk'+val[i].riskLevel+'">' + val[i].orderName +'</span><br>';	
			}
			else{
				str += val[i].orderName +'<br>';
			}
		}
		return str;
	}
}
function getStandard(val){
	var len = val.length,
		str = '';
	if( 1 == len){
		//str =  val[0].dosageUnit;
		if(val[0].hasOwnProperty('drugSpec')){
			str =  val[0].drugSpec;
		}
		return str;
	}
	else{
		for(var i=0;i<len;i++){
			if(!val[i].hasOwnProperty('drugSpec')){
				continue;
			}
			if(i == (len-1) ){
				str += val[i].drugSpec;
				return str;
			}
			str += val[i].drugSpec +'<br>';		
		}
		return str;
	}
}
function getDosage(val){
	var len = val.length,
		str = '';
	if( 1 == len){
		
		if(val[0].hasOwnProperty('dosage')){
			str = val[0].dosage;
		}
		
		if(val[0].hasOwnProperty('dosageUnit')){
			str += val[0].dosageUnit;
		}
		return str;
	}
	else{
		for(var i=0;i<len;i++){
			if(!val[i].hasOwnProperty('dosage')){
				continue;
			}
			var dosageUnit = "";
			if(val[i].hasOwnProperty('dosageUnit')){
				dosageUnit = val[i].dosageUnit;
			}
			if(i == (len-1) ){
				str += val[i].dosage + dosageUnit;
				return str;
			}
			str += val[i].dosage + dosageUnit +'<br>';		
		}
		return str;
	}
}
function getPlanDate(val){
	return val.planDate;
	/*var len = val.length,
		str = '';
	if( 1 == len){
		str =  val.planDate;
		return str;
	}
	else{
		for(var i=0;i<len;i++){
			if(i == (len-1) ){
				str += val[i].planDate;
				return str;
			}
			str += val[i].planDate +'<br>';		
		}
		return str;
	}*/
}

/*
function getHandlerBtn(val){
	//return '<input type="button" value="修改计划时间" onclick="editorPlanDate()"><br><input type="button" value="医嘱补执行">'
}
*/

/*function editrow(index){
    if (endEditing()){
        $('#info-tab').datagrid('selectRow', index)
                .datagrid('editCell', {index:index,field:'_planDate'});
        editIndex = index;
    }
}*/

//function editorPlanDate(){
//	console.log($("#info-tab").datagrid('getSelections'));
//}

function savePlanDate(){
	var index = $("#info-tab").datagrid('getRowIndex',$("#info-tab").datagrid('getSelected'));
	var editPanelList = $("#editorPlanDate .editPanel input");
	var dateArr ='';
	editPanelList.each(function(ix){
		if(ix==editPanelList.length-1){
			dateArr += $(this).val();
		}
		else{
			dateArr += $(this).val()+'<br>';	
		}
	});
	$("#info-tab").datagrid('updateRow',{
		index:index,
		row:{
			_planDate:dateArr
		}
	});
	// 执行与服务器的交互操作
	$('#editorPlanDate').dialog('close');
}
function closeEditorPlanDateBox(){
	$('#editorPlanDate').dialog('close');
}

/*function editCellBox(index){
	$("#info-tab").datagrid('selectRow',index);
	var planDate = $("#info-tab").datagrid('getSelected')._planDate;
	var arr = planDate.split('<br>');
	var str = '';
	for(var i=0;i<arr.length;i++){
		str += '<input class="Wdate" onclick="WdatePicker({readOnly:true,dateFmt:\'yyyy-MM-dd HH:mm:ss\'})" readonly="readonly" value="'+ arr[i] +'" ><br>'
	}
	console.log(str);
	$("#editorPlanDate .editPanel").html(str);
	$("#editorPlanDate").dialog('open');
}*/



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
	//console.log(patientIdList);
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var condition1 = $("#select1").val();
	select2 = $("#select2").val();
	if(condition1){
		condition1 = '&orderTypeCode='+condition1;
	}
	orderDetail = [];
	try{
		$.post(ay.contextPath+'/nur/patOrderDetail/queryPendingOrderGroupList.do?patientId='+patientIdList+condition1,{},function(data){
			tempArr = data.data ? data.data.list : [];
			for(var i=0;i<tempArr.length;i++){
				var standard = getStandard(tempArr[i].orderGroup.orderItems);
				var dosage = getDosage(tempArr[i].orderGroup.orderItems);
				var planDate = getPlanDate(tempArr[i].orderExecLog);
				tempArr[i].orderGroup._standard = standard;
				tempArr[i].orderGroup._dosage = dosage;
				tempArr[i].orderGroup._planDate = planDate;
				tempArr[i].orderGroup.orderExecBarcode = tempArr[i].orderExecBarcode;
				orderDetail.push(tempArr[i].orderGroup);
			}
			$("#info-tab").datagrid({loadFilter:pagerFilter}).datagrid('loadData',orderDetail);
		});
	}
	catch(e){

	}
	
}



$(function(){

	//设置content区域高
	$('.content').height( $(window).height() - $('.top-tools').height() );
	var recordDay = new Date().format("yyyy-MM-dd");
	$("#startDate").val(recordDay);
	$("#endDate").val(recordDay);
	addUnexecOrderTable();

	//设置患者信息容器宽度
	if(parent.setPatientInfoWrap){
		parent.setPatientInfoWrap();
	}
	if(_patientId){
		getPatOrderDetail();
	}
});

$.extend($.fn.datagrid.methods, {
    editCell: function(jq,param){
        return jq.each(function(){
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields',true).concat($(this).datagrid('getColumnFields'));
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field){
                    col.editor = null;
                }
            }
            $(this).datagrid('beginEdit', param.index);
            for(var i=0; i<fields.length; i++){
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});

function exec(index,eId,pId){
	try{
		$.post(ay.contextPath+'/nur/patOrderDetail/execOrderNda',{orderExecId:eId,patientId:pId},function(data){
			if(data.rslt=='0'){
				$.messager.alert('提示','执行成功');
				$("#info-tab").datagrid('deleteRow',index);
				getPatOrderDetail();
			}
		});
	}
	catch(e){
		console.log(e);
	}
}

function endEditing(){
    if (editIndex == undefined){return true}
    if ($('#info-tab').datagrid('validateRow', editIndex)){
        $('#info-tab').datagrid('endEdit', editIndex);
        editIndex = undefined;
        return true;
    } else {
        return false;
    }
}
function onClickCell(index, field){
    if (endEditing()){
        $('#info-tab').datagrid('selectRow', index)
                .datagrid('editCell', {index:index,field:field});
        editIndex = index;
    }
}

/*
* 客户端分页
*/
function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){    // is array
        
        //过滤数据
        if( select2 ){
        	for(var i=0;i<data.length;i++){
	        	if(data[i].orderExecTypeCode != select2){
	        		data.splice(i,1);
	        		i -= 1;
	        	}
	        }
        }
        
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

/*function searchPatCrisValue(){
	var condition1 = $("#select1").val()+"医嘱";
	//var condition2 = $("#select2").val()+"医嘱";
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
}*/

function addUnexecOrderTable(){
	$("#info-tab").datagrid({
		/*data : orderDetail,*/
		nowrap:false,
		fit : true,
		fitColumns:true,
		pagination:true,
		pageSize:20,
		singleSelect : true,
		remoteSort:false,
		/*pagination:true,*/
		sortName:"_planDate",
		rownumbers:true,
		/*frozenColumns:[[
			
		]],*/
		columns : [ [
		{
			field:'itemState',
			title:'',
			formatter:function(value,row){
				var str = '';
				if( row.emergent ){
					str += '<span class="adviceState bgcred">急</span>';
				}
				//过期未执行
				var nowDate = new Date().format("yyyy-MM-dd").toString().split('-');;
				var pDate   = new Date(row.planExecTime).format('yyyy-MM-dd').toString().split('-');
				var isPast = false;
				if( nowDate[0] > pDate[0] ){
					isPast = true;
				}
				else if( nowDate[0] == pDate[0] && nowDate[1] > pDate[1] ){
					isPast = true;
				}
				else if( nowDate[0] == pDate[0] && nowDate[1] == pDate[1] && nowDate[2] - pDate[2] >= 1 ){
					isPast = true;
				}
				
				if( row.orderStatusCode == 'N' ){
					if(isPast){
						str += '<span class="adviceState bgcorg">过</span>';
					}else{
						str += '<span class="adviceState bgcgreen">新</span>';
					}
					
				}else if( row.orderStatusCode == 'F' ){
					str += '<span class="adviceState bgcblue">已</span>';
				}else if( row.orderStatusCode == 'S' ){
					str += '<span class="adviceState bgcgray">停</span>';
				}else{
					//过期未执行
					if(isPast){
						str += '<span class="adviceState bgcorg">过</span>';
					}
				}
				return str;
			}
		},
		{
			field : 'orderTypeCode',
			title : '医嘱类型',
			width : 60,
			formatter:function(value,row){
				if(value){ return row.orderTypeName;}
					return '';
			}
		}, {
			field : 'orderExecTypeCode',
			title : '医嘱分类',
			width : 60,
			formatter:function(value,row){
				if(value){ return row.orderExecTypeName}
					return '';
			}
		}, {
			field : '_planDate',
			title : '计划执行时间',
			align:'center',
			editor:'text',
			sortable:true,
			width : 130
		},
		/*{
			field:'zxr',
			title:'执行人'
		},{
			field:'zxsj',
			title:'执行时间'
		},*/{
			field : 'orderItems',
			title : '医嘱内容',
			width : 140,
			formatter:getOrderBaseInfo
		},{
			field : '_standard',
			title : '规格',
			width : 80,
			formatter:function(value,row){
				if(value){
					return value;
				}else{
					return '';
				}
			}
		},{
			field : '_dosage',
			title : '剂量',
			width : 80
		},{
			field : 'usageName',
			title : '用法',
			width : 60
		},{
			field : 'deliverFreq',
			title : '频次',
			align:'center',
			width : 70
		},{
			field : 'createDate',
			title : '开立时间',
			align:'center',
			width : 130
		},{
			field : 'createDoctorName',
			title : '开立医生',
			align:'center',
			width : 70
		},{
			field : 'mark',
			title : '备注',
            align:'center',
			width : 100
		}/*,{
			field : 'c',
			title : '操作',
			align:'center',
			width : 70,
			formatter:function(value,row,index){
                var e = '<a href="#" onclick="editCellBox('+index+')">修改计划时间</a> ';
                var d = '<a href="#" onclick="">打印条码</a>';
                var c = '<a href="#" onclick="exec('+index+',\''+row.orderExecBarcode+'\',\''+row.patientId+'\')">医嘱执行</a>';
                return c;
        	}
		}*/
		 ]]
	});
}
