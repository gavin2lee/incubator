
$(function(){

	var date = new Date(),
	startDate = new Date(date.format("yyyy-MM-dd")).format("yyyy-MM-dd"),
	endDate   = new Date(new Date(date.format("yyyy-MM-dd")+" 00:00:00").getTime()+86400*1000).format("yyyy-MM-dd hh:mm:ss");

	$("#startDate").val(startDate);
	$("#endDate").val(endDate);

	addUnexecOrderTable();
	hideTableColumn();
	if(parent.setPatientInfoWrap){
		parent.setPatientInfoWrap();
	}
	$("#printLabelBarcode").bind("click",function(){
		getSelectOrder();
	});
	//searchOrder();
});

/*
	获取选中的医嘱
	@param {str} 类型
*/
function getSelectOrder(type){
	var selected = $('#labelBarcodeTable').datagrid("getSelections");
	//console.log(selected);
	var conf = $.parseJSON(sessionStorage.getItem("config")).data.system;
	setOrderCardListEmpty();
	drawOrderCard(selected,conf);
}

/*
	清空list
*/
function setOrderCardListEmpty(){
	$("#printPreviewList").html("");
}

/*
	绘制输液卡
	@param {obj} 选中的医嘱
	@param {obj} 保存到浏览器的配置信息
*/
function drawOrderCard(selected,conf){
	if(!selected || selected.length == 0){
		$.messager.alert("提示","请选择需要打印的瓶签!","info");
		return;
	}
	var patId = selected[0].patientId;
	var tpl = $("#transCardTpl").html();
	var context = {
		data:[]
	};
	context.data = getOrderCardData(selected,conf);
	var template = Handlebars.compile(tpl);
	var html = template(context);
	$("#printPreviewList").append(html);
	setOrderCardToIframe($("#printFrame"),$("#printPreviewList"),selected,patId,printIframeHtml);
}

/*
	获取转换后与handlebars相匹配的打印信息
	@param {obj} 选中的行
	@param {obj} 配置信息
	@return {obj} 对应的数据
*/
function getOrderCardData(selected,conf){
	var data = [];
	for(var i=0;i<selected.length;i++){
		var qrImg = $("#qrImg").qrcode({
		    "render": "image",
		    "width": 200,
		    "height":200,
		    "color": "#000",
		    "text": selected[i].orderGroupNo
		});
		data.push({
			inHospNo:selected[i].inHospNo,
			barcode:selected[i].orderGroupNo,
			bednum:selected[i].bedCode,
			name:selected[i].patName,
			index:"1",
			count:"1",
			sex:selected[i].gender,
			age:selected[i].age,
			deptName:selected[i].deptName,
			time:new Date(selected[i]._planDate).format("hh") + '时',
			moment:parseInt(new Date(selected[i]._planDate).format("hh")) <= 12 ? "AM" : "PM",
			date:getDate(selected[i]._planDate),
			qr:qrImg.children().attr("src"),
			usageName:selected[i].usageName + "(" + getOrderType(selected[i]) + ")",
			deliverFreq:selected[i].deliverFreq,
			orderType:getOrderType(selected[i])
		});
		qrImg.html("");
		var orderItems = selected[i].orderItems;
		if(orderItems.length>0){
			var drugList = data[data.length-1].drugList = [];
			for(var j=0;j<orderItems.length;j++){
				drugList.push({
					drugname:orderItems[j].orderName + getBarcodeDrugSpec(orderItems[j].drugSpec),
					standard:selected[i]._dosage.split("<br>")[j]
					//standard:getBarcodeDosage(j,selected[i]._dosage.split("<br>")[j],selected[i].deliverFreq)
					//standard:selected[i].deliverFreq.split("<br>")
				});
				//getBarcodeDosage(j,selected[i]._dosage
				//,selected[i].deliverFreq)
			}
		}
	}
	return data;
}

/**
 * 医嘱类型
 * @param selected
 * @returns {String}
 */
function getOrderType(selected){
	var orderType = "临";
	if("CZ" == selected.orderTypeCode){
		orderType = "长";
	}
	
//	orderType = orderType + "-" + selected.orderNo;
	return orderType;
}

function getBarcodeDosage(i,dosage,deliverFreqCode){
	//if(i == 0){
		dosage += "/" + deliverFreqCode;
	//}
	return dosage;
}

function getBarcodeDrugSpec(drugSpec){
	var barcodeDrugSpec = "";
	if(drugSpec){
		barcodeDrugSpec = "[" + drugSpec + "]";
	}
	return barcodeDrugSpec;
}
/*
	打印
*/
function setOrderCardToIframe(iframe,target,selected,patId,cb){
    var win  = iframe[0].contentWindow;
    var html = target.html();
    cb(html,win,selected,patId);
}

function printIframeHtml(html,w,selected,patId) {
    w.document.body.innerHTML = html;
    w.document.head.innerHTML ='<link rel="stylesheet" type="text/css" href="'+ay.contextPath+'/resources/css/public.css"><link rel="stylesheet" type="text/css" href="'+ay.contextPath+'/resources/css/prescriptionExe.css">'
    	+ '<link rel="stylesheet" type="text/css" href="'+ay.contextPath+'/resources/css/labelBarcode_zhizhong.css">';
    var printWin = $(w.document.body);
    printWin.addClass("print");
    setTimeout(function(){
    	w.print();
    	updatePatientPrint(selected,patId);
    	
    },1000);
}

/*
* 客户端分页
*/
function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){    // is array

        var select2 = $("#select2").val();
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
        };
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

function getPlanDate(val){
	return val.planDate;
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

/*
	渲染表格
*/
function addUnexecOrderTable(){
	$("#labelBarcodeTable").datagrid({
		/*data : orderDetail,*/
		nowrap:false,
		fit : true,
		fitColumns:true,
		pagination:true,
		pageSize:0,
		pageList:[500],
		remoteSort:false,
		rownumbers:true,
		columns : [ [
		{
			field:'check',
			title:'',
			checkbox:true
		},
		{
			field:'isPrintLabel',
			title:'瓶签标记',
			width:60,
			formatter:function(value){
				if(value){
					return "已打印";
				}
				else{
					return "未打印";
				}
			},
			styler: function(value,row,index){
				if (value){
					return 'color:#000000';
				}else{
					return 'color:#ff0000';
				}
			}

		},{
			field : 'bedCode',
			title : '床号',
			width : 25,
			styler: function(value,row,index){
				if (value){
					return "";
				}else{
					return 'background-color:#ededed';
				}
			}
		},{
			field : 'patientName',
			title : '姓名',
			width : 50,
			styler: function(value,row,index){
				if (value){
					return "";
				}else{
					return 'background-color:#ededed';
				}
			}
		},{
			field : 'inHospNo',
			title : '住院号',
			width : 60,
			hidden:true
		},{
			field : 'BedCode',
			title : '床位',
			width : 60,
			hidden:true
		},{
			field : 'gender',
			title : '姓别',
			width : 60,
			hidden:true
		},{
			field : 'age',
			title : '年龄',
			width : 60,
			hidden:true
		},{
			field : 'patName',
			title : '姓名',
			width : 60,
			hidden:true
		},{
			field : 'orderGroupNo',
			title : '医嘱条码',
			width : 60,
			hidden:true
		},{
			field : 'orderNo',
			title : '医嘱组号',
			width : 60,
			hidden:true
		},{
			field : 'orderTypeCode',
			title : '医嘱类型',
			width : 60,
			formatter:function(value,row){
				if(value){ return row.orderTypeName;}
					return '';
			}
		},{
			field : 'createDate',
			title : '开立时间',
			align:'center',
			width : 120
		},{
			field : 'createDoctorName',
			title : '开立医生',
			align:'center',
			width : 70
		}, {
			field : '_planDate',
			title : '计划执行时间',
			align:'center',
			width : 120
		},
		{
			field : 'orderItems',
			title : '医嘱内容',
			width : 160,
			formatter:getOrderBaseInfo
		},{
			field : '_dosage',
			title : '剂量',
			width : 80
		}, {
			field : 'orderExecTypeCode',
			title : '医嘱分类',
			width : 60,
			formatter:function(value,row){
				if(value){ return row.orderExecTypeName;}
					return '';
			}
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
			field : 'deliverFreqCode',
			title : '频次',
			align:'center',
			hidden:true,
			width : 70
		}
		 ]]
	});
}

var patientIdList;
/*
	获取未执行医嘱并进行处理
*/
function getPatOrderDetail(){
	showOverlay();
	var myArray=new Array()
	var myArrayIndex = 0;
	if(parent.peopleList){
		patientIdList = parent.peopleList;
		for (var x=0;x< patientIdList.length;x++){
			if(patientIdList[x] && patientIdList[x] != ''){
				myArray[myArrayIndex] = patientIdList[x];
				myArrayIndex ++;
			}
		}
	}
		
	/*if(myArray.length == 0){
		$.messager.alert('提示', "您没有选择患者，请先选择患者！");
		$('#info-tab').datagrid('loadData',{total:0,rows:[]});
		return;
	}*/
	
	var myStr = myArray.join(',');
	
	var startDate = $("#startDate").val(),
		endDate   = $("#endDate").val();

	var condition1 = $("#select1").val();
	var condition2 = $("#select2").val();
	var condition3 = null;
	hideTableColumn();
	
	if(condition1){
		condition1 = '&orderTypeCode='+condition1;
	}
	if(condition2){
		condition2 = '&orderExecTypeCode=' + condition2;
	}
	
	if ($('#printSelect').attr("checked")) {
        condition3 = '&orderPrint=' + 0;
    }else{
    	condition3 = '&orderPrint=' + 1;
    }
		
	orderDetail = [];
	var orderGroup  = {};				
	try{
		//$.post(ay.contextPath+'/nur/patOrderDetail/queryPendingOrderGroupList.do?patientId='+patientIdList + condition1 + condition2,{},function(data){
		var deptCode = $("#deptCode",window.parent.document).val();
		$.post(ay.contextPath+"/nur/patOrderDetail/getOrderPrintInfos.do?deptCode="+deptCode+condition1+condition2+condition3+"&startDate="+startDate+"&patIds=" + myStr,{},function(data){
			tempArr = data.data ? data.data.list : [];
			for(var i=0;i<tempArr.length;i++){
				var orderGroups = tempArr[i].orderGroups;
				for(var j=0;j<orderGroups.length;j++){
						orderGroup.patientName = orderGroups[j].patientName;
						orderGroup.bedCode = orderGroups[j].patientBedCode;

					var standard = getStandard(orderGroups[j].orderItems);
					var dosage = getDosage(orderGroups[j].orderItems);
					var planDate = orderGroups[j].planExecTime;
					orderGroup.inHospNo = orderGroups[j].inHospNo;
					orderGroup.patName = orderGroups[j].patientName;
					if(tempArr[i].gender == 'M'){
						orderGroup.gender = '男';
					}else if(tempArr[i].gender == 'F'){
						orderGroup.gender = '女';
					}else{
						orderGroup.gender = ''
					}
					orderGroup.deptName = tempArr[i].deptName;
					orderGroup.age = tempArr[i].age;
					orderGroup.orderItems = orderGroups[j].orderItems;
					orderGroup.orderExecTypeCode = orderGroups[j].orderExecTypeCode;
					orderGroup.orderExecTypeName = orderGroups[j].orderExecTypeName;
					orderGroup.orderTypeCode = orderGroups[j].orderTypeCode;
					orderGroup.orderTypeName = orderGroups[j].orderTypeName;
					orderGroup.isPrintLabel = orderGroups[j].isPrintLabel;
					orderGroup.orderGroupNo = orderGroups[j].orderGroupNo;
					orderGroup.orderNo = orderGroups[j].orderNo;

					orderGroup.usageName = orderGroups[j].usageName;
					orderGroup.deliverFreq = orderGroups[j].deliverFreq;
					orderGroup.deliverFreqCode = orderGroups[j].deliverFreqCode;
					orderGroup.createDate = orderGroups[j].createDate;
					orderGroup.createDoctorName = orderGroups[j].createDoctorName;

					orderGroup._standard = standard;
					orderGroup._dosage = dosage;
					orderGroup._planDate = planDate;
					
					orderDetail.push(orderGroup);
					orderGroup  = {};
				}			
			}
			$("#labelBarcodeTable").datagrid({loadFilter:pagerFilter}).datagrid('loadData',orderDetail);
			hideOverlay();
		});
		
	}
	catch(e){
		hideOverlay();
	}
	
}

function searchOrder(){
	getPatOrderDetail();
}

function orderTypeFunc(obj){
	getPatOrderDetail();
}

function orderExecTypeFunc(obj){
	getPatOrderDetail();
}

function orderPrintFunc(obj){
	getPatOrderDetail();
}

function updatePatientPrint(selected,patId){
	var patientPrints = JSON.stringify(setPatientPrints(selected));
		
	
	var url = ay.contextPath+'/nur/patientGlance/savePatientPrint.do?patientPrints=' + patientPrints;
	
	$.get(url,function(data){
		console.log(data.rslt);
		if(data.rslt == 0){
//			$("#wristBarcodeTable").datagrid('reload'); 
//			 location.reload();
			getPatOrderDetail();
		}else{
			$.message.alert("警告",data.msg,'info');
		}
		
	});
}

function setPatientPrints(selected){
	var patientPrints = [];
	$.each(selected,function(i,n){
		if(selected.isPrintBarcode){
			return true;
		}
		var patientPrint = {};
		patientPrint.printType='order';
		patientPrint.printDataId = n.orderGroupNo;
		patientPrint.isPrintLabel = true;
		patientPrints.push(patientPrint);
	});
	
	return patientPrints;
}

function hideTableColumn(){
	var orderTypeCode = $("#select1").val();
	if("LZ" == orderTypeCode){
		//隐藏：开始时间，结束时间，停止时间，停止医生
		/*$("#labelBarcodeTable").datagrid('hideColumn','beginDate');
		$("#labelBarcodeTable").datagrid('hideColumn','endDate');
		$("#labelBarcodeTable").datagrid('hideColumn','stopDate');
		$("#labelBarcodeTable").datagrid('hideColumn','stopDoctorName');*/
		$("#labelBarcodeTable").datagrid('hideColumn','deliverFreq');
//		$("#labelBarcodeTable").datagrid('hideColumn','_planDate');
		
	}else{
		/*$("#labelBarcodeTable").datagrid('showColumn','beginDate');
		$("#labelBarcodeTable").datagrid('showColumn','endDate');
		$("#labelBarcodeTable").datagrid('showColumn','stopDate');
		$("#labelBarcodeTable").datagrid('showColumn','stopDoctorName');*/
		$("#labelBarcodeTable").datagrid('showColumn','deliverFreq');
//		$("#labelBarcodeTable").datagrid('showColumn','_planDate');
//		$('#labelBarcodeTable').datagrid('getColumnOption', '_planDate').width=120;   
	}
}

/**
 * 设置瓶签日期(当长嘱计划时间为空时(当前时间大于指定打印时间,日期为后一天
 * 反正,为当天))
 * @param barcodeDate
 * @returns {String}
 */
function getDate(barcodeDate){
	var date ='';
	var printTime = $.parseJSON(sessionStorage.getItem("config")).data.system.CZPrintTime || "18:00";
	if(!barcodeDate){
		barcodeDate = new Date();
		var barcodeTime = new Date(barcodeDate).format("hh:mm");
		//根据时间点判断日期
		if(barcodeTime > printTime){
			barcodeDate.setDate(barcodeDate.getDate() + 1);
		}
	}
	date = new Date(barcodeDate).format("yyyy-MM-dd hh");
	return date;
}

/* 显示遮罩层 */
function showOverlay() {
    $("#overlay").height(pageHeight());
    $("#overlay").width(pageWidth());

    // fadeTo第一个参数为速度，第二个为透明度
    // 多重方式控制透明度，保证兼容性，但也带来修改麻烦的问题
    $("#overlay").fadeTo(200, 0.5);
}

/* 隐藏覆盖层 */
function hideOverlay() {
    $("#overlay").fadeOut(200);
}

/* 当前页面高度 */
function pageHeight() {
    return document.body.scrollHeight;
}

/* 当前页面宽度 */
function pageWidth() {
    return document.body.scrollWidth;
}