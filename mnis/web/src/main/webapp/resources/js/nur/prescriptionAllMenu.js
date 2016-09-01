jsPlugin.plugin("prescriptionAll", function (params) {
    $("#menuHeadDiv").empty();
    $("#menuHeadDiv").append('<div class="insp f_yahei"><h4>患者列表</h4></div>');
    $("#mainMenu").empty();
    $("#menuDiv").empty();
    console.log(params);
    // TODO 隐藏左边菜单
    var list = '',
        isCheckedCheckBox = [];
    peopleList = [];
    var attentionPatient = [];
    var url = ay.contextPath + "/nur/patOrderDetail/prescriptionAll.do";
    var deptCode = $("#deptCode").val();
    var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode=' + deptCode;
    getBedList(peopleListUrl, 'getPatOrderDetail', {
        cb: null,
        isSetAllCheck: false,
        isSingle: true
    });
    $('#nurMainFrame').attr('src', url);
});

/*
 * 左侧全选按钮
 *
 */