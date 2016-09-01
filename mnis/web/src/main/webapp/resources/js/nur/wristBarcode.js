// 腕带打印JS 控制列表生成 打印的控制
$(function(){
	/*var bedCard =
		 "{{#data}}"+
	    "<div class=\"xk_bed-card\">" + 
	        "<div class=\"header\">" + 
	        	"<div>"+
	        		"<img class=\"hospitalName\" src=\"/mnis/resources/img/xk_hospital_name.png\">"+
	        		"<span class=\"title\">{{title}}</span>"+
	        	"</div>"+
	       " </div>"+
	       " <div class=\"body\">"+
	        	"<div>"+
	        		"<span class=\"deptName\">{{deptName}}</span>"+
	        		"<span class=\"inHospNo\">{{inHospNo}}</span>"+
	        		
	        	"</div>"+
	        	"<div>"+
	        		"<span class=\"name\">姓名:{{name}}</span>"+
	        		"<span class=\"age\">年龄:{{age}}</span>"+
	        		"<span class=\"sex\">性别:{{sex}}</span>"+
	        	"</div>"+
	        	"<div>"+
	        		"<span class=\"bedCode\">床号:{{bedCode}}</span>"+
	        		"<span class=\"chargeType\">医保类型:{{chargeType}}</span>"+
	        		
	        	"</div>"+
	        	"<div>"+
	        		"<span class=\"admitDate\">住院:{{admitDate}}</span>"+
	        		"<span class=\"tendLevel\">护理级别:{{tendLevel}}</span>"+
	        	"</div>"+
	        	"<div>"+
	        		"<span class=\"transferDate\">转科:</span>"+
	        		"<span class=\"backDate\">召回:</span>"+
	        	"</div>"+
	        	"<div>"+
	        		"<div class=\"lineDisplay\">"+
	        			"<div>"+
	        			"	<span class=\"doctor\">主治医生:{{doctor}}</span>"+
	        			"</div>"+
	        			"<div>"+
	        			"	<span class=\"dutyNurseName\">责任护士:{{dutyNurseName}}</span>"+
	        			"</div>"+
	        			"<div>"+
	        				"<span class=\"diet\">饮食:{{diet}}</span>"+
	        			"</div>"+
	        			"<div>"+
	        			"	<span class=\"allergy\">过敏史:{{allergy}}</span>"+
	        			"</div>"+
	        		"</div>"+
				"	<div class=\"qr lineDisplay\"><img src=\"{{qr}}\"></div>"+
	        	"</div>"+
	       " </div>"+
	   " </div>"+
	    "{{/data}}";
		
		$("#bedCard").append(bedCard);*/
	initPrintStyle();
	initListTable();
	getTablePatientData();
//	initTablePatientData(data);
	$("#printWristBarcode").bind('click',function(){
		printWristBarcode();
	});
	$("#printBedCard").bind("click",function(){
		printBedCard();
	});
});

/**
 * 初始化打印样式
 */
function initPrintStyle(){
	var bedCardStyle = "",wristStyle = "",
	config = $.parseJSON(sessionStorage.getItem("config"));
	
	if(config && config.data && config.data.pcSystem){
		bedCardStyle = config.data.pcSystem.bedCard || "" ;
		wristStyle = config.data.pcSystem.wrist || "" ;
	}
	//1.床头卡
	$("#bedCard").append(bedCardStyle);
	//2.腕带
	$("#handCard").append(wristStyle);
	
}

/**
 * 获取表格数据
 */
function getTablePatientData(){
	var deptCode = $("#deptCode",window.parent.document).val();
	var peopleListUrl = ay.contextPath+'/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode='+deptCode;
	try{
		$.get(peopleListUrl,function(data){
			loadTablePatientData(data);
		});
	}
	catch(e){
		console.log(e);
	}
}

/**
 * 加载表格数据
 * @param data
 */
function loadTablePatientData(data){
	var tableData = getListTableData(data);
	 $("#wristBarcodeTable").datagrid('loadData',tableData);
}

/*
	打印床头卡
*/
function printBedCard(){
	var selected = getSelectedList();
	if(!selected || selected.length == 0){
		$.messager.alert("提示","请选择需要打印的床头卡!","info");
		return;
	}
	var config = $.parseJSON(sessionStorage.getItem("config"));
	var hospitalName = '';
	if(config && config.data){
		hospitalName = config.data.system.hospitalName ;
	}
	var tpl = $("#bedCard").html();
	var template = Handlebars.compile(tpl);
	var context  = {
		data:[]
	};
	$("#printPreviewList").html("");
	context = createXkPatientInfo(selected,context,hospitalName);
	var html = template(context);
	$("#printPreviewList").append(html);
	setPrinterToIframe($("#printFrame"),$("#printPreviewList"),selected,false,printIframeHtml);
}

/**
 * 生成胸科床头卡信息
 * @param context
 */
function createXkPatientInfo(selected,context,hospitalName){
	var tendLevelArr = ["特","一","二","三"];
	$.each(selected,function(i,n){
		var qrImg = $("#qrImg").qrcode({
		    "render": "image",
		    "width": 200,
		    "height":200,
		    "color": "#000",
		    "text": n.barcode
		});
		context.data.push({
			title:"床尾卡",
			deptName:n.deptName,
			inHospNo:n.inHospitalNo,
			bedCode:n.showBedCode,
			name:n.patientName,
			sex:n.sex,
			age:n.age,
			blood:n.blood,
			allergy:n.allergyDrugs,
			tendLevel:tendLevelArr[n.tendLevel],
			chargeType:n.chargeType,
			admitDate:n.admitDate,
			doctor:n.doctor,
			dutyNurseName:n.dutyNurseName,
			diet:n.diet,
			hospitalName:hospitalName,
			qr:qrImg.children().attr("src")
		});
		qrImg.html("");
	});
	
	return context;
}

/**
 * 生成普通床头卡信息
 * @param context
 */
function createNormalPatientInfo(selected,context,hospitalName){
	var tendLevelArr = ["特级护理","一级护理","二级护理","三级护理"];
	$.each(selected,function(i,n){
		var qrImg = $("#qrImg").qrcode({
		    "render": "image",
		    "width": 200,
		    "height":200,
		    "color": "#000",
		    "text": n.barcode
		});
		context.data.push({
			title:"床头卡",
			admitDate:n.admitDate,
			name:n.patientName,
			sex:n.sex,
			age:n.age,
			dutyNurseName:n.dutyNurseName,
			admitDiagnosis:n.admitDiagnosis,
			inHospitalNo:n.inHospitalNo,
			bedCode:n.showBedCode,
			allergyDrugs:n.allergyDrugs,
			diet:n.diet,
			tendLevel:tendLevelArr[n.tendLevel],
			qr:qrImg.children().attr("src")
		});
		qrImg.html("");
	});
}


/*
	打印腕带
*/
function printWristBarcode(){
	var selected = getSelectedList();
	if(!selected || selected.length == 0){
		$.messager.alert("提示","请选择需要打印的腕带!","info");
		return;
	}
	var tpl = $("#handCard").html();
	var template = Handlebars.compile(tpl);
	var context  = {
		data:[]
	};
	
	$("#printPreviewList").html("");
	$.each(selected,function(i,n){
		var qrImg = $("#qrImg").qrcode({
		    "render": "image",
		    "width": 200,
		    "height":200,
		    "color": "#000",
		    "text": n.barcode
		});
		//腕带值
		context.data.push({
			title:"LACHESIS联新科技",
			inHospitalNo:n.inHospitalNo,
			inpNo:n.inpNo,
			deptName:n.deptName.substring(0,3),
			showBedCode:n.showBedCode,
			barcode:n.barcode,
			sex:n.sex,
			age:n.age,
			name:n.patientName,
			admitDate:n.admitDate,
			qr:qrImg.children().attr("src")
		});
		qrImg.html("");
	});
	var html = template(context);
	$("#printPreviewList").append(html);
	setPrinterToIframe($("#printFrame"),$("#printPreviewList"),selected,true,printIframeHtml);
}


function getSelectedList(){
	var wristBarcodeTable = $("#wristBarcodeTable");
	var selected = wristBarcodeTable.datagrid("getSelections");
	return selected;
}

/*
	打印
*/
function setPrinterToIframe(iframe,target,selected,isBarcode,cb){
    var win  = iframe[0].contentWindow;
    var html = target.html();
    cb(html,win,selected,isBarcode);
}

function printIframeHtml(html,w,selected,isBarcode) {
    w.document.body.innerHTML = html;
    w.document.head.innerHTML ='<link rel="stylesheet" type="text/css" href="'+ay.contextPath+'/resources/css/public.css"><link rel="stylesheet" type="text/css" href="'+ay.contextPath+'/resources/css/wristBarcode.css">';
    var printWin = $(w.document.body);
    printWin.addClass("print");
    setTimeout(function(){
    	w.print();
    	updatePatientPrint(selected,isBarcode);
    },1000);
}


/*
	渲染信息列表
*/
function initListTable(data){
//	var tableData = getListTableData(data);
	var wristBarcodeTable = $("#wristBarcodeTable");
	wristBarcodeTable.datagrid({
//		data:tableData,
		fit : true,
		remoteSort:false,
		rownumbers:true,
		columns : [[
			{
				field : 'isPrintBarcode',
				title : "腕带标记",
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
			},
			{
				field : 'isPrintBed',
				title : "床头卡标记",
				formatter:function(value){
					if(value){
						return "已打印";
					}
					else{
						return "未打印";
					}
				}
			},
			{
				field : 'checkbox',
				title : '',
				checkbox:true
			},
			{
				field : 'deptName',
				title : "科室",
				width : 80	
			},
			{
				field : 'patientName',
				title : "姓名",
				width : 40	
			},
			{
				field : 'showBedCode',
				title : "床号",
				width : 40	
			},
			{
				field : 'barcode',
				title : "条码",
				width : 80	
			},
			{
				field : 'inHospitalNo',
				title : "住院号",
				width : 60	
			},
			{
				field : 'sex',
				title : "性别",
				width : 30	
			},
			{
				field : 'age',
				title : "年龄",
				width : 30	
			}
			,	
			{
				field : 'chargeType',
				title : "医保类型",
				width : 40	
			},
			{
				field : 'diet',
				title : "饮食",
				width : 80	
			},
			{
				field : 'admitDate',
				title : "入院日期",
				width : 80	
			}
			,	
			{
				field : 'doctor',
				title : "主治医生",
				width : 40	
			}	
			,	
			{
				field : 'dutyNurseName',
				title : "责任护士",
				width : 40	
			}	
			,	
			{
				field : 'admitDiagnosis',
				title : "入院诊断",
				width : 80	
			}	
			,	
			{
				field : 'blood',
				title : "血型",
				hidden:true
			}
		]]
	});
}

/*
	获取转换后的列表数据
*/
function getListTableData(data){
	var listData = [];
	var bedInfoList = data.data.bedInfoList;
	$.each(bedInfoList,function(i,n){
		listData.push({
			isPrint:"N",
			patientName:n.patientName,
			sex:(n.sex == "F" ? "女" : "男"),
			age:n.age,
			showBedCode:n.showBedCode,
			barcode:n.barcode,
			inHospitalNo:n.inHospitalNo,
			diet:n.diet,
			admitDiagnosis:n.admitDiagnosis,
			dutyNurseName:n.dutyNurseName,
			doctor:n.doctor,
			admitDate:n.admitDate,
			allergyDrugs:n.allergyDrugs,
			tendLevel:n.tendLevel,
			isPrintBarcode:n.isPrintBarcode,
			isPrintBed:n.isPrintBed,
			patientId:n.patientId,
			deptName:n.deptName,
			inpNo:n.inpNo,
			chargeType:n.chargeType,
			blood:''
		});
	});
	return listData;
}

function updatePatientPrint(selected,isBarcode){
	var patientPrintsObject = setPatientPrints(selected,isBarcode);
	if(!patientPrintsObject || patientPrintsObject.length == 0){
		return;
	}
	
	var patientPrints = JSON.stringify(setPatientPrints(selected,isBarcode));
	
	var url = ay.contextPath+'/nur/patientGlance/savePatientPrint.do?patientPrints=' + patientPrints + '&isBarcode=' + isBarcode;
	
	$.get(url,function(data){
		console.log(data.rslt);
		if(data.rslt == 0){
//			$("#wristBarcodeTable").datagrid('reload'); 
			getTablePatientData();
		}else{
			$.messager.alert("警告",data.msg,'info');
		}
		
	});
}

function setPatientPrints(selected,isBarcode){
	var patientPrints = [];
	$.each(selected,function(i,n){
		var patientPrint = {};
		patientPrint.printType='patient';
		patientPrint.printDataId = n.patientId;
		if(isBarcode){
			if(n.isPrintBarcode){
				console.log(n.patientName + '--腕带已打印');
				return true;
			}
			patientPrint.isPrintBarcode = true;
		}else{
			if(n.isPrintBed){
				console.log(n.patientName + '--床头卡已打印');
				return true;
			}
			patientPrint.isPrintBed = true;
		}
		patientPrints.push(patientPrint);
	});
	
	return patientPrints;
}