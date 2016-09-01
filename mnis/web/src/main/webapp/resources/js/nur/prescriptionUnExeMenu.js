jsPlugin.plugin("prescriptionUnExe",function(params){
	$("#menuHeadDiv").empty();
	$("#menuHeadDiv").append('<div class="insp f_yahei">	<input type="checkbox" id="checkAllPatient" style="left:10px;"><h4>患者列表</h4></div>');
	$("#mainMenu").empty();
	$("#menuDiv").empty();
	console.log(params);
	// TODO 隐藏左边菜单
	var list = '';
	peopleList = [];
	var url = ay.contextPath +"/nur/patOrderDetail/prescriptionUnExe.do";
	var deptCode = $("#deptCode").val();
	var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode='+deptCode;
	getBedList(peopleListUrl,'getPatOrderDetail',{
		cb 			  : null,
		isSetAllCheck : false,
		isSingle	  : true
	});
	$('#nurMainFrame').attr('src', url);
});
