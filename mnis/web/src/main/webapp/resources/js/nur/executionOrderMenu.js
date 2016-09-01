jsPlugin.plugin("executionOrder", function(params) {
  $("#menuHeadDiv").empty();
  $("#mainMenu").empty();
  $('#mainMenu').append('<li class="checkAllLi"><input type="checkbox" id="checkAllPatient"><label for="checkAllPatient"> 全选</label></li>');
  $('#mainMenu').find('input[type=checkbox]').chkbox();
  $("#menuHeadDiv").append('<div class="_btn _btn-lg"> <a href="#url(\'/nur/patientGlance/patientGlanceMain.do\')"> <span>返回床位列表</span> </a> </div>');
  $("#menuDiv").empty();
  console.log(params);
  // TODO 隐藏左边菜单
  var list = '';
  peopleList = [];
  var url = ay.contextPath + "/nur/patOrderDetail/executionOrder.do";
  var deptCode = $("#deptCode").val();
  var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode=' + deptCode;
  getBedList(peopleListUrl, 'getExecOrderDetail', {
    cb: null,
    isSetAllCheck: true,
    isSingle: false,
    checkCallback: function() {

    }
  });
  $('#nurMainFrame').attr('src', url);
});
