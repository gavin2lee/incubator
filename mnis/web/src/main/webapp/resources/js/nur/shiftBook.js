var deptId    = null,
	nurseId   = null,
	cacheData = null;
var treeData = [
	{
	    "id":1,
	    "text":"个人模板",
    	"children":[{
	        "id":13,
	        "text":"个人模板一"
	    },{
	        "id":14,
	        "text":"个人模板二"
	    },{
	        "id":15,
	        "text":"个人模板三"
	    }]
		},{
	    "id":1,
	    "text":"科室模板",
	    "children":[{
	        "id":13,
	        "text":"个人模板一"
	    },{
	        "id":14,
	        "text":"个人模板二"
	    },{
	        "id":15,
	        "text":"个人模板三"
	    },{
	        "id":14,
	        "text":"个人模板二"
	    },{
	        "id":15,
	        "text":"个人模板三"
	    },{
	        "id":14,
	        "text":"个人模板二"
	    },{
	        "id":15,
	        "text":"个人模板三"
	    },{
	        "id":14,
	        "text":"个人模板二"
	    },{
	        "id":15,
	        "text":"个人模板三"
    	}]
	},{
    "id":1,
    "text":"全院模板",
    "children":[{
        "id":13,
        "text":"个人模板一"
    },{
        "id":14,
        "text":"个人模板二"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    },{
        "id":15,
        "text":"个人模板三"
    }]
}];

$.extend($.fn.datagrid.defaults.editors, {    
    text: {    
        init: function(container, options){    
            var input = $('<input type="text" class="datagrid-editable-input">').appendTo(container);    
            return input;    
        },    
        getValue: function(target){    
            return $(target).val();    
        },    
        setValue: function(target, value){    
            $(target).val(value);    
        },    
        resize: function(target, width){    
            var input = $(target);    
            if ($.boxModel == true){    
                input.width(width - (input.outerWidth() - input.width()));    
            } else {    
                input.width(width);    
            }    
        }    
    }    
});  

$(document).ready(function(){
	//设置当天日期
	$("#startDate").val(new Date().format("yyyy-MM-dd"));
	deptId   = $("#deptId").val(),
	nurseId  = $("#nurseId").val();
	// 绑定护理记录左侧点击切换事件
    $('ul.left-items').find('li').each(function(){
        //设置标示项
        //$(this).attr('data-id',i+1);
        var parentNode = $(this).parent();
        $(this).bind('click',function(){
                parentNode.find('li').each(function(){
                if($(this).hasClass('checked')){
                    $(this).removeClass('checked')
                    return ;
                }
            });
            //通过标识项更改右侧框框
            $(this).addClass('checked');
        });
    })

    // 人数信息
	var url = ay.contextPath+ "/nur/patientGlance/queryDeptSummary.do?workUnitType=1&workUnitCode="+deptId;
	$.get(url, function(result) {
		try {
			var data = result.data;	
			var nowNum = data.inpatientCount-data.emptyBedCount;
			var str="<li>原有："+data.inpatientCount+"人</li>" +
					"<li>现有："+nowNum+"人</li>" +
					"<li>入院："+data.newPatientCount+"人</li>" +
					"<li>出院："+data.dischargeCount+"人</li>" +
					"<li>转入："+data.transInPatientCount+"人</li>" +
					"<li>转出："+data.transOutPatientCount+"人</li>" +
					"<li>死亡："+data.deadPatientCount+"人</li>" +
					"<li>分娩："+data.inDeliveryPatientCount+"人</li>" +
					"<li>病重："+data.seriousPatientCount+"人</li>" +
					"<li>病危："+data.criticalPatientCount+"人</li>" +
					"<li>手术："+data.inSurgeryPatientCount+"人</li>";
			$('#tjxx').append(str);
			str=data.tendLevelSuperCount;
			$('#tjhl').append(str);
			str=data.tendLevelOneCount;
			$('#yjhl').append(str);
			str=data.tendLevelTwoCount;
			$('#ejhl').append(str);
			str=data.tendLevelThreeCount;
			$('#sjhl').append(str);
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});

	//床位病人
	url = ay.contextPath+ "/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode="+deptId;
	$.get(url, function(result) {
		try{
			console.log(result);
			var str="";
			var idList="";
			var bedInfoList = result.data.bedInfoList;
			for(var i=0;i<bedInfoList.length;i++){
				str+="<option id='"+bedInfoList[i].bedCode+"' value='"+bedInfoList[i].patientId+"' patientName='"+bedInfoList[i].patientName+"' inHospitalNo='"+bedInfoList[i].inHospitalNo+"'>"+bedInfoList[i].bedCode+"床&nbsp;&nbsp;"+bedInfoList[i].patientName+"</option>";
//				str+="<option value='"+bedInfoList[i].patientId+"-"+bedInfoList[i].patientName+"-"+bedInfoList[i].inHospitalNo+"'>"+bedInfoList[i].bedCode+"床&nbsp;&nbsp;"+bedInfoList[i].patientName+"</option>";
			}
			$("#selPatient").append(str);
		}catch (e) {
			$.messager.alert('提示', e);
		}
	});

	search();

    $("#existTemplate").tree({
        data:treeData,
        lines:true,
        cascadeCheck:false
    });
    $("#tree2").tree({
        data:treeData,
        lines:true,
        cascadeCheck:false
    });
    $("#createTempBtn").click(function(){
        $("#createTemp").dialog({
            title:'新建模板',
            width:750,
            height:537,
            buttons:[{
                text:'保存',
                handler:function(){}
            },{
                text:'清除',
                handler:function(){}
            },{
                text:'取消',
                handler:function(){}
            }]
        })
    });
});

function loadRecordSuccess( data ) {
}
function clearing(){
	$("#shiftDesc").val("");
}

function exportExcl() {
	/*var url = ay.contextPath + '/nur/shiftBook/exportExcel.do';
	$.post(url, {
		startDate : $("#startDate").val(),
		endDate : $("#endDate").val()
	}, function(data) {
		try {
			$.messager.alert('提示', '保存成功！');			
			search();
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});*/
}

function saveData(){
	var sbId=$("#shiftRecordId").val();
	var sbTime=$("#shiftTime").val();
	var problem = $("#shiftDesc").val().trim();
	if(problem.length == 0){
		$.messager.alert('提示', '请输入病情变化！');
		return;
	}
	var deptCode     =  $("#deptId").val();
	var deptName     =  $("#user em").html();
	var nurseId      =  $("#nurseId").val().trim();
	var patientId    =  $("#selPatient").val();
	var nowDate      =  new Date().format('yyyy-MM-dd hh:mm:ss');

	if(sbId){
		try{
			var record = '{"shiftRecordId":"'+sbId+'","shiftRecordNurseId": "'+nurseId+'","shiftRecordNurseName": "'+deptName+'","shiftRecordTime": "'+sbTime+'","shiftRecordData": "'+problem+'","patientId": "'+patientId+'","deptCode": "'+deptCode+'"}'
			$.post(ay.contextPath +'/nur/shiftBook/updateNurseShiftRecord.do',{shiftRecord:record},function(data){
				console.log(data);
				if(data != false){
					$.messager.alert('提示', '更新成功');
					$("#shiftDesc").val("");
					$("#editorTime").hide();
					$("#shiftRecordId").val('');
					search();
				}
			});
		}
		catch(e){
			$.messager.alert('提示', e);
			$("#editorTime").hide();
			$("#shiftBookId").val('');
		}
		return;
	}
	var url = ay.contextPath + '/nur/shiftBook/saveNurseShiftRecord.do';
	//set data
	var record = '{"shiftRecordNurseId": "'+nurseId+'","shiftRecordNurseName": "'+deptName+'","shiftRecordTime": "'+nowDate+'","shiftRecordData": "'+problem+'","patientId": "'+patientId+'","deptCode": "'+deptCode+'"}'
	$.post(url, {			
		shiftRecord:record
	}, function(data) {
		console.log(data);
		try {
			$.messager.alert('提示', '保存成功！');
			$("#shiftDesc").val("");
			search();
		} catch (e) {
			$.messager.alert('提示', e);
		}
	});
}

function loadReferenceNurseDoc(templateType,beginTime,endTime){
	
}
function searchshiftBooks(){
	var hospitalNo = $('#selPatient').find("option:selected").attr("inhospitalno");
	var templateType = $('#templateType').val();
    var beginDate = $('#referenceBeginDate').val();
    var endDate = $('#referenceEndDate').val();
    if(beginDate==""){
    	beginDate=undefined;
    }
    if(endDate==""){
    	endDate=undefined;
    }
    var url = ay.contextPath + '/nurseDocAction/getRefList.do';
    $.ajax({
		type: "POST",
		url: url,
		async:false,
		dataType : "json",
		data:{templateType:templateType,hospitalNo:hospitalNo,beginDateStr:beginDate,endDateStr:endDate},
		success:function(vo){
			if(vo.rslt=="0"){
				$('#referenceTable').datagrid('loadData', vo.lst);
				$('#referenceTable').datagrid('selectAll');				
			}
		},
		error:function(e){
			hasError = true;
			$.messager.alert("提示",e);
		}
	});
}

function hideAllUaBox(){
    $('.ua-box').hide();
}

function showPrescription(){
    hideAllUaBox();
    $('#add-prescription-box').show();
    $('#yz').datagrid({
        /*data:_data,*/
        fit:true,
        singleSelect:true,
        columns:[[    
            {field:'bedCode',title:'开立时间',width:100},    
            {field:'patientName',title:'开立医生',width:70},    
            {field:'diagName',title:'医嘱内容',width:100},
            {field:'problem',title:'剂量',width:80},
            {field:'diagName',title:'用法',width:100},
            {field:'problem',title:'频次',width:70},
            {field:'problem',title:'执行时间',width:100},
            {field:'problem',title:'执行护士',width:80}
        ]]    
    });
}   

function showCheckReport(){
    hideAllUaBox();
    $("#check-report-box").show();
    $('#cr').datagrid({
        /*data:_data,*/
        fit:true,
        singleSelect:true,
        columns:[[    
            {field:'bedCode',title:'检验项目',width:100},    
            {field:'patientName',title:'结果',width:70},    
            {field:'diagName',title:'单位',width:100},
            {field:'problem',title:'参考值',width:80},
            {field:'diagName',title:'检验项目',width:100},
            {field:'problem',title:'结果',width:70},
            {field:'problem',title:'单位',width:100},
            {field:'problem',title:'参考值',width:80}
        ]]    
    });  
    $("#check-report-box").show();
    allItems = $(".check-report-list ul.left-items").find('li').length;
    d = 250;
    moveObj = $(".check-report-list ul");
    pagenum = Math.ceil(allItems / 5);
}

function showNurPlan(){
    hideAllUaBox();
    $('#nur-plan-box').show();
}

function showReferenceNurseDoc(templateType){
	hideAllUaBox();
	
	// 获取当天引用数据，构造页面
	$('#startDate').val();
	$('#endDate').val();
	$('#templateType').val(templateType);
	searchshiftBooks();	
	
	// 显示窗口
	$('#referenceDlg').dialog("open");
}


function showInspectionReport(){
    hideAllUaBox();
    $("#inspection-report-box").show();
    allItems = $(".inspection-report-list ul.left-items").find('li').length;
    d = 250;
    moveObj = $(".inspection-report-list ul");
    pagenum = Math.ceil(allItems / 5);
}

function showTemplate(){
    $("#template-box").show();
}

function showNurRecord(){
    hideAllUaBox();
    $("#nur-record-box").show();
    allItems = $(".nur-record-list ul.left-items").find('li').length;
    d = 280;
    moveObj = $(".nur-record-list ul");
    pagenum = Math.ceil(allItems / 7);
}

function printShiftBooks(){
	var htmlAllStr = document.body.innerHTML;
	document.body.innerHTML = $('#shiftBookContent').html();
	window.print();
	document.body.innerHTML = htmlAllStr;
}

function exportExcel() {	
    window.location.href = ay.contextPath + "/nur/shiftBook/exportExcel.do";
}


var curIndex = 1,                   //当前页数
    allItems = 0,                  //总条目数
    pagenum = Math.ceil(allItems/7),//总页面数
    d = 0,                          //位移距离
    moveObj = null;

function moveTop(){
    var state = (curIndex - 1) <= 0 ? 0 : 1;
    if(state){
        curIndex -= 1;
        var dis = -((curIndex-1)*d)+"px";
        moveObj.css('top',dis);
    }
}

function moveDown(){
    var state = (curIndex + 1) > pagenum ? 0 : 1;
    if(state){
        curIndex += 1;
        var dis = -((curIndex-1)*d)+"px";
        moveObj.css('top',dis);
    }
}

// 对内容进行选择
function formatReferenceContent(value,row,index){
	var html="";
	var spaceHtml="&nbsp;&nbsp;&nbsp;&nbsp;";
	if (value){
		$.each(value.split(";"),function(idx,str){
			if(html!=""){
				html+=spaceHtml;
			}
			html += '<input class="refBox" type="checkbox" name="refBox'+index+'" value="'+str+'"/>'+str;
		});
		return html;
	} else {
		return value;
	}
}

function insertReference(){
	// 获取所有引用数据
	var refContent = "";
	var refs = $('#referenceTable').datagrid('getSelections');
	$.each(refs,function(idx,ref){
		if(refContent!=""){
			refContent += ", ";
		}
		refContent += ref.refValue;
	});
	
	// 追加到交班本
	if(refContent!=""){
		$("#shiftDesc")[0].innerHTML += refContent;
	}
	
	
	$('#referenceDlg').dialog("close");
}

function del(){
	//var sbId = $("#shiftBookId").val();
	var row = $("#shiftBookTable").datagrid('getSelected');
	if(!row){
		$.messager.alert('提示','未选择项目！');
		return;
	}
	var shiftRecordId = row.shiftRecordId;
	var index = $("#shiftBookTable").datagrid('getRowIndex',row);
	if(!window.confirm("确认要删除该行记录吗？")){
		return ;
	}
	try{
		$.get(ay.contextPath+'/nur/shiftBook/deleteNurseShiftRecord.do',{shiftRecordId:shiftRecordId},function(data){
			$.messager.alert('提示','删除成功！');
			console.log(index);
			$("#shiftBookTable").datagrid('deleteRow',index);
			$("#shiftDesc").val("");
			$("#editorTime").hide();
			$("#shiftBookId").val('');
		});
	}
	catch(e){
		console.log("e:"+e);
	}
	

}

function search(){
	var startDate = $("#startDate").val()+' 00:00:00';
	var endDate   = new Date(startDate);
	endDate.setDate( endDate.getDate() + 1 );
	endDate = endDate.format('yyyy-MM-dd hh:mm:ss');
	console.log(endDate)
	if(startDate.length!=0&&endDate.length!=0){
		var url = ay.contextPath + '/nur/shiftBook/getNurseShiftList.do?rangeType=1&deptId='+deptId+'&startDate='+startDate+'&endDate='+endDate+'';
		$.get(url, function(data) {			
			console.log(data);
			var list = data.data ? data.data.list : [];
			var d = [];
			var recordTime = null;
			var p = '',t='',n='';
			//组装数据
			for(var i=0;i<list.length;i++){
				for(var j=0;j<list[i].nurseShiftRecordEntities.length;j++){
					if(j == 0){
						d.push({"bedNo":list[i].bedNo,"patientName":list[i].patientName,"diagnose":list[i].diagnose,"shiftStatus":list[i].shiftStatus});	
					}
					else{
						d.push({"shiftStatus":list[i].shiftStatus});	
					}
					
					d[d.length-1].shiftRecordNurseId = list[i].nurseShiftRecordEntities[j].shiftRecordNurseId;
					d[d.length-1].nurseShiftId = list[i].nurseShiftRecordEntities[j].nurseShiftId;
					d[d.length-1].shiftRecordId = list[i].nurseShiftRecordEntities[j].shiftRecordId;
					recordHour = new Date(list[i].nurseShiftRecordEntities[j].shiftRecordStartDate).getHours();
					switch(recordHour){
						case 0:
							p='problem1';
							t='time1';
							n='nursName1'
							break;
						case 8:
							p='problem2';
							t='time2';
							n='nursName2'
							break;
						case 16:
							p='problem3';
							t='time3';
							n='nursName3'
							break;
						default:
							break;
					}
					d[d.length-1][p] = list[i].nurseShiftRecordEntities[j].shiftRecordData;
					d[d.length-1][t] = list[i].nurseShiftRecordEntities[j].shiftRecordTime;
					d[d.length-1][n] = list[i].nurseShiftRecordEntities[j].shiftRecordNurseName;
				}
			}
			$('#shiftBookTable').datagrid({
				nowrap:false,
				data:d,
				onClickCell: function(index,field,value){
					var lineData = $('#shiftBookTable').datagrid('getData').rows[index];
					console.log(lineData);
					$("#selPatient option[id='"+lineData.bedNo+"']").attr("selected",true);
					if( !lineData.bedNo ){
						return;
					}
					if(field.indexOf('problem')<0){
						$("#shiftBookId").val('');
						$("#operationType").html('新增');
						$("#editorTime").hide();
						$("#shiftDesc").val('');
						return ;
					}
					$("#operationType").html('更新');

					if(!value){
						$("#shiftBookId").val('');
						$("#operationType").html('新增');
						$("#editorTime").hide();
						$("#shiftDesc").val('');
						return ;
					}
					else{
						$("#shiftRecordId").val(lineData.shiftRecordId);
						$("#editorTime").show();
						$("#shiftTime").val(lineData['time'+field.substring(field.indexOf('problem')+7,field.length)]);
						$("#shiftDesc").val(value);
					}
				}
			})
			/*$('#shiftBookTable').datagrid({
				nowrap:false,
				data:data,
				onClickCell: function(index,field,value){
					var lineData = $('#shiftBookTable').datagrid('getData').rows[index];
					console.log(lineData);
					$("#selPatient option[id='"+lineData.bedCode+"']").attr("selected",true);
					if( !lineData.bedCode ){
						return;
					}
					if(field.indexOf('problem')<0){
						$("#shiftBookId").val('');
						$("#operationType").html('新增');
						$("#shiftDesc").val('');
						return ;
					}
					$("#operationType").html('更新');
					if(!value){
						$("#shiftBookId").val('');
						$("#operationType").html('新增');
						$("#shiftDesc").val('');
						return ;
					}
					else{
						var sbId = 'shiftBookId'+ field.substring(field.length-1,field.length); 
						$("#shiftBookId").val(lineData[sbId]);
						$("#shiftDesc").val(value);
					}
				}
			});*/
		});			
	}else{
		$('#shiftBookTable').datagrid({data:null});
		$.messager.alert('提示', "请选择正确的时间范围！");
	}
}

function shiftTime(value,row){
	if(!value){
		return '';
	}
	var str = value.toString().substring(value.indexOf(' '),value.length);
	return str;
}

function shiftStatus(value,row){
	switch(value){
		case '0':
			return '未交班';
			break;
		case '1':
			return '已交班';
			break;
		case '2':
			return '已接班';
			break;
		default:
			return '未交班';
			break;
	}
}



var LODOP; //声明为全局变量
function PrintMytable(){
	// 检查时间范围，必须是同一天的
	if($("#startDate").val()!=$("#endDate").val()){
		$.messager.alert("提示", "一次只允许打印一天交班本");
		return ;
	}
	
	$("#printDate")[0].innerHTML = $("#endDate").val();
	fillPrintTableHtml();
	var strBodyStyle="<style>"+document.getElementById("style1").innerHTML+"</style>";

	LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
	LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_打印表格");
	LODOP.SET_PRINT_PAGESIZE(2,0,0,'A4');
	LODOP.ADD_PRINT_TABLE(70,20,1050,600,document.getElementById("printTableDiv").innerHTML);
	LODOP.ADD_PRINT_HTM(0,20,1052,100,strBodyStyle+document.getElementById("printHeadDiv").innerHTML);
	LODOP.PREVIEW();	// 预览
	//LODOP.PRINT();		// 直接打印
}

//组合打印表格内容
function fillPrintTableHtml(){
// 获取交班本记录 
var datas = $('#shiftBookTable').datagrid('getRows');

var html = "";

// 生成病人统计摘要


// 生成交班本内容
$.each(datas,function(idx,data){
	var rowHtml="<tr>";	
	rowHtml += "<td>"+(data.bedCode==undefined?'&nbsp;':data.bedCode)+"</td>";
	rowHtml += "<td>"+(data.patientName==undefined?'&nbsp;':data.patientName)+"</td>";
	rowHtml += "<td>"+(data.diet==undefined?'&nbsp;':data.diet)+"</td>";
	rowHtml += "<td>"+(data.state==undefined?'&nbsp;':data.state)+"</td>";
	rowHtml += "<td>"+(data.problem1==undefined?'&nbsp;':data.problem1)+"</td>";
	rowHtml += "<td>"+(data.nursName1==undefined?'&nbsp;':data.nursName1)+"</td>";
	rowHtml += "<td>"+(data.time1==undefined?'&nbsp;':data.time1)+"</td>";
	rowHtml += "<td>"+(data.problem2==undefined?'&nbsp;':data.problem2)+"</td>";
	rowHtml += "<td>"+(data.nursName2==undefined?'&nbsp;':data.nursName2)+"</td>";
	rowHtml += "<td>"+(data.time2==undefined?'&nbsp;':data.time2)+"</td>";
	rowHtml += "<td>"+(data.problem3==undefined?'&nbsp;':data.problem3)+"</td>";
	rowHtml += "<td>"+(data.nursName3==undefined?'&nbsp;':data.nursName3)+"</td>";
	rowHtml += "<td>"+(data.time3==undefined?'&nbsp;':data.time3)+"</td>";    
	rowHtml +="</tr>";	
	html += rowHtml;
});

$("#printTableBody")[0].innerHTML = html;
}

