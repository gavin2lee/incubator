jsPlugin.plugin("prescriptionCheck",function(params){
	$("#menuHeadDiv").empty();
	$("#menuHeadDiv").append('<div class="insp f_yahei"><input type="checkbox" id="checkAllPatient" style="left:10px;"><h4>患者列表</h4></div>');
	$("#mainMenu").empty();
	$("#menuDiv").empty();
	console.log(params);
	// TODO 隐藏左边菜单
	var list = '',
		isCheckedCheckBox = [];
	peopleList = [];
	var attentionPatient = [];
	var url = ay.contextPath +"/nur/orderManager/check.do";
	var deptCode = $("#deptCode").val();
	var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode='+deptCode;
	getBedList(peopleListUrl,'getPatOrderDetail');
	$('#nurMainFrame').attr('src', url);	
});

/*
* 左侧全选按钮
*
*/