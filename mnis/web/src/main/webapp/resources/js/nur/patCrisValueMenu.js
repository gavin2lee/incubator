jsPlugin.plugin("patCrisValue",function(params){
	$("#menuHeadDiv").empty();
	$("#menuHeadDiv").append('<div class="insp f_yahei"><input type="checkbox" id="checkAllPatient" style="left:10px;"><h4>患者列表</h4></div>');
	$("#mainMenu").empty();
	$("#menuDiv").empty();
	console.log(params);
	// TODO 隐藏左边菜单
	hideWestMenu();
	var list = '',
		isCheckedCheckBox = [],
		peopleList = [];
	var url = ay.contextPath +"/nur/crisisValue/patCrisisValueMain.do";
	$('#nurMainFrame').attr('src', url);	
});

/*
* 左侧全选按钮
*
*/