$(function () {
    var deptId = $("#deptId").val();
    //获取病人列表
    var peopleListUrl = '/nur/patientGlance/queryOutPatientList.do?workUnitType=1&workUnitCode=' + deptId;
    $.post(ay.contextPath + peopleListUrl, {}, function (data) {
        if (data.obj) {
            var peopleList = data.obj.bedInfoList;
        } else {
            var peopleList = data.data.bedInfoList;
        }
        var peopleListStr = '';
        if (peopleList && peopleList.length) {

            //排序
            peopleList.sort(compare('showBedCode'));


            for (var i = 0; i < peopleList.length; i++) {
                console.log(peopleList[i].patientId);
                if (!peopleList[i].patientId) continue;
                if (peopleList[i].patientId == $("#patientId").val()) {
                    peopleListStr = '<option selected="selected" value="'
                        + peopleList[i].patientId + '">'
                        + peopleList[i].showBedCode + '&nbsp;&nbsp;床&nbsp;&nbsp;' + peopleList[i].patientName + ' </option>'
                        + peopleListStr;
                } else {
                    peopleListStr += '<option value="' + peopleList[i].patientId
                        + '">' + peopleList[i].showBedCode + '&nbsp;&nbsp;床&nbsp;&nbsp;' + peopleList[i].patientName + ' </option>';
                }
            }
        }

        $("#patientList").html(peopleListStr);
        $("#patientList").bind('change', function () {
            var currUrl = window.location.href.split('?')[0];
            currUrl += '?id=' + $(this).val();
            window.location.href = currUrl
        });

        window.onbeforeunload = function () {};

        if ($("#patientList option").length > 0 && !window.location.search) {
            $("#patientList option").eq(0).trigger('change');
        }
    });

    $('#mainLayout').layout('panel', 'center').panel({
        onResize: function (width, height) {
            ay.setIframeHeight('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').height);
            ay.setIframeWidth('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').width);
        }
    });
    //初始化大小
    ay.setIframeHeight('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').height - $('#mainLayout').layout('panel', 'north').panel('options').height);
    ay.setIframeWidth('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').width - $('#mainLayout').layout('panel', 'west').panel('options').width);
    //监听页面缩放 是否显示信息左右滑动按钮
    $(window).resize(function () {
        winw = $(window).width();
        if (winw < 1300) {
            $(".info-content ul").addClass('lf0');
            $(".info-cover").show();
        }
        else {
            $(".info-content ul").removeClass('lf0');
            $(".info-cover").hide();
        }
    })

    //监听信息滑动按钮
    $(".info-cover").click(function () {
        var arg1, arg2;
        if ($(this).text() == '>') {
            arg1 = 'left';
            arg2 = -(1310 - $(window).width()) + "px";
            $(".info-content ul").animate({
                left: -(1310 - $(window).width()) + "px"
            }, 500, function () {
                $(".info-cover").html("<");
            });

        }
        else {
            arg1 = 'right';
            arg2 = "0px";
            $(".info-content ul").animate({
                left: "0px"
            }, 500, function () {
                $(".info-cover").html(">");
            });

        }
    });
    $(window).trigger("resize");
});

//排序
function compare(k) {
    return function (a, b) {
        var v1 = a[k];
        var v2 = b[k];
        if (/^[0-9]/.test(v1) && /^[0-9]/.test(v2)) {
            return v1 - v2;
        }
        else if (!/^[0-9]/.test(v1)) {
            return 1;
        }
        else if (!/^[0-9]/.test(v2)) {
            return -1;
        }
    }
}

function loadMenu() {
    // debugger;
    jsPlugin.doPlugin('patientMenu', {
        'patientId': patient.patientId,
        'hospitalNo': patient.hospitalNo,
        'departmentId': patient.departmentId
    });
}
