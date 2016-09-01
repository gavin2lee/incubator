jsPlugin.plugin("workLoadMain",function(params){
	$("#menuHeadDiv").empty();
	$("#mainMenu").empty();
	$('#mainMenu').append('<li class="checkAllLi"><input type="checkbox" id="checkAllPatient"><label for="checkAllPatient">全选</label></li>');
	$("#menuHeadDiv").append('<div class="insp f_yahei">' +
		'<div class="btn-nm backBedList"><a href="/mnis/nur/patientGlance/patientGlanceMain.do"><span class="btm">返回床位列表</span> </a></div>');
	$("#menuDiv").empty();
	console.log(params);
	debugger;
	// TODO 隐藏左边菜单
	var list = '';
	peopleList = [];
	var url = ay.contextPath +"/nur/workload/workLoadMain.do";
	var deptCode = $("#deptCode").val();
	var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode='+deptCode;
	getBedList(peopleListUrl,'getWorkLoadDetail',{
		cb 			  : null,
		isSetAllCheck : true,
		isSingle	  : false,
		checkCallback : function () {

		}
	});
	$('#nurMainFrame').attr('src', url);
});
