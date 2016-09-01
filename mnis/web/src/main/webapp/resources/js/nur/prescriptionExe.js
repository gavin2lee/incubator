var _data = {"total":1,"rows":[
	{
		"id":"",
		"bedId":"1",
		"pName":"金秀芝秀",
		"presCate":"1",
		"presType":"",
		"planExeTime":"",
		"presInfo":"",
		"standard":"",
		"dosage":"",
		"usage":"",
		"frequency":"",
		"openTime":"",
		"openDoc":"",
		"comment":"",
		"action":""
	}
]};
var editIndex = undefined;
var editRowData = undefined;
var orderDetail = [];
var patientIdList;
function setTime(){
	$("#setTimeBox").show();
}

function getBedCode(val,row){
	var x = val.indexOf('_');
	return parseInt(val.substring(x+1));
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

function getHandlerBtn(val){
	//return '<input type="button" value="修改计划时间" onclick="editorPlanDate()"><br><input type="button" value="医嘱补执行">'

}

function editrow(index){
    if (endEditing()){
        $('#info-tab').datagrid('selectRow', index)
                .datagrid('editCell', {index:index,field:'_planDate'});
        editIndex = index;
    }
}

function editorPlanDate(){
	console.log($("#info-tab").datagrid('getSelections'));
}

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

function editCellBox(index){
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
	//console.log(patientIdList);
	var startDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var condition1 = $("#select1").val();
	var condition2 = $("#select2").val();
	orderDetail = [];
	try{
		$.post(ay.contextPath+'/nur/orderManager/getAllPendingOrder.do?patientIdList='+patientIdList+'&startTime='+startDate+'&endTime='+endDate+'&orderTypeCode='+condition1+'&orderExecTypeCode='+condition2,{},function(data){
			tempArr = data.lst;
			for(var i=0;i<tempArr.length;i++){
				var standard = getStandard(tempArr[i].orderGroup.orderItems);
				var dosage = getDosage(tempArr[i].orderGroup.orderItems);
				var planDate = getPlanDate(tempArr[i].orderExecLog);
				tempArr[i].orderGroup._standard = standard;
				tempArr[i].orderGroup._dosage = dosage;
				tempArr[i].orderGroup._planDate = planDate;
				orderDetail.push(tempArr[i].orderGroup);
			}
			$("#info-tab").datagrid({
				/*data : orderDetail,*/
				fit : true,
				pagination:true,
				pageSize:20,
				singleSelect : true,
				remoteSort:false,
				/*pagination:true,*/
				sortName:"_planDate",
				rownumbers:true,
				columns : [ [
				{
					field : 'patientBedCode',
					title : '床号',
					width : 50,
					sortable:true,
					formatter:getBedCode
				},{
					field : 'patientName',
					title : '患者',
					width : 60
				}, {
					field : 'orderTypeName',
					title : '医嘱类型',
					width : 60
				}, {
					field : 'fl',
					title : '医嘱分类',
					width : 60
				}, {
					field : '_planDate',
					title : '计划执行时间',
					editor:'text',
					sortable:true,
					width : 150
				},{
					field:'zxr',
					title:'执行人'
				},{
					field:'zxsj',
					title:'执行时间'
				},{
					field : 'orderItems',
					title : '医嘱内容',
					width : 100,
					formatter:getOrderBaseInfo
				},{
					field : '_standard',
					title : '规格',
					width : 100
				},{
					field : '_dosage',
					title : '剂量',
					width : 100
				},{
					field : 'usageName',
					title : '用法',
					width : 100
				},{
					field : 'delicerFreq',
					title : '频次',
					width : 100
				},{
					field : 'createDate',
					title : '开立时间',
					width : 100
				},{
					field : 'createDoctorName',
					title : '开立医生',
					width : 100
				},{
					field : 'comment',
					title : '备注',
					width : 100
				},{
					field : 'c',
					title : '操作',
					width : 120,
					formatter:function(value,row,index){
	                    var e = '<a href="#" onclick="editCellBox('+index+')">修改计划时间</a> ';
	                    var d = '<a href="#" onclick="">打印条码</a>';
	                    var c = '<a href="#" onclick="">医嘱执行</a>';
	                    return e+'<br>'+c;
	            	}
				}
				 ]]
			});
			$("#info-tab").datagrid({loadFilter:pagerFilter}).datagrid('loadData',orderDetail);
		});
	}
	catch(e){

	}
	
}

$(function(){

	//设置content区域高
	console.log($(window).height());
	$('.content').height( $(window).height() - $('.top-tools').height() );
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
