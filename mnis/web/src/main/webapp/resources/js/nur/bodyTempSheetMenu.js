jsPlugin.plugin("bodyTempSheet", function(params) {
  $("#menuHeadDiv").empty();
  $("#menuHeadDiv").append('<div class="insp f_yahei" style="text-align: left; padding: 0 10px;"><input type="checkbox" id="checkAllPatient" style="left:10px;"><span style="margin-left: 10px; font-size: 14px;">全选</h4></div>');
  $("#mainMenu").empty();
  $("#menuDiv").empty();
  $("#menuHeadDiv").find('input[type=checkbox]').chkbox();
  // TODO 隐藏左边菜单
  var list = '',
    isCheckedCheckBox = [];
  peopleList = [];
  var attentionPatient = [];
  var url = ay.contextPath + "/nur/bodySign/bodyTempSheet.do";
  var deptCode = $("#deptCode").val();
  var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode=' + deptCode;
  getBedList(peopleListUrl, 'addInputItems', {
    cb: null,
    isSetAllCheck: true,
    isSingle: false
  });
  $('#nurMainFrame').attr('src', url);
});

/*
 * 左侧全选按钮
 */
