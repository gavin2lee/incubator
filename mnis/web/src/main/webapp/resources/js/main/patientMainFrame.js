$(function() {
  var deptId = $("#deptId").val();
  //获取病人列表
  var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode=' + deptId;
  if (typeof isHistoryDoc !== 'undefined' || !isHistoryDoc) {
    $.post(ay.contextPath + peopleListUrl, {}, function(data) {
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
          if (peopleList[i].patientId == $("#patientId").val()) {
            peopleListStr = '<option selected="selected" value="' + peopleList[i].patientId + '">' + peopleList[i].showBedCode + '&nbsp;&nbsp;床&nbsp;&nbsp;' + peopleList[i].patientName + ' </option>' + peopleListStr;
          } else {
            peopleListStr += '<option value="' + peopleList[i].patientId + '">' + peopleList[i].showBedCode + '&nbsp;&nbsp;床&nbsp;&nbsp;' + peopleList[i].patientName + ' </option>';
          }
        }
      }
      $("#patientList").html(peopleListStr);
      $("#patientList").bind('change', function() {
        var currUrl = window.location.href.split('=')[0];
        var path = window.location.href.split('?')[0];
        var $location = path + '?';
        var searchObj = ay.hashToObj(window.location.href);
        var count = 0;

        if ('id' in searchObj) {
          searchObj.id = $(this).val();
        }

        if ('patId' in searchObj) {
          searchObj.patId = $(this).val();
        }
        for (var prop in searchObj) {
          if (searchObj.hasOwnProperty(prop)) {
            if (count === 0) {
              $location = $location + prop + '=' + searchObj[prop];
            } else {
              $location = $location + '&' + prop + '=' + searchObj[prop];
            }
            count += 1;
          }
        }
        //currUrl += '=' + $(this).val();
        window.location.href = $location;
      });
    });
  } else {
    var $historyTable = $('#history-patient-table');
    var $historyDialog = $('.dialog-history-patient');


  }

  if (isHistoryDoc) {
    $('#search-history-btn').on('click', getPatientHistory);
  }

  $('#mainLayout').layout('panel', 'center').panel({
    onResize: function(width, height) {
      ay.setIframeHeight('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').height);
      ay.setIframeWidth('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').width);
    }
  });
  //初始化大小
  ay.setIframeHeight('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').height - $('#mainLayout').layout('panel', 'north').panel('options').height);
  ay.setIframeWidth('mainFrame', $('#mainLayout').layout('panel', 'center').panel('options').width - $('#mainLayout').layout('panel', 'west').panel('options').width);
  //监听页面缩放 是否显示信息左右滑动按钮
  $(window).resize(function() {
    winw = $(window).width();
    if (winw < 1300) {
      $(".info-content ul").addClass('lf0');
      $(".info-cover").show();
    } else {
      $(".info-content ul").removeClass('lf0');
      $(".info-cover").hide();
    }
  })

  //监听信息滑动按钮
  $(".info-cover").click(function() {
    var arg1, arg2;
    if ($(this).text() == '>') {
      arg1 = 'left';
      arg2 = -(1310 - $(window).width()) + "px";
      $(".info-content ul").animate({
        left: -(1310 - $(window).width()) + "px"
      }, 500, function() {
        $(".info-cover").html("<");
      });

    } else {
      arg1 = 'right';
      arg2 = "0px";
      $(".info-content ul").animate({
        left: "0px"
      }, 500, function() {
        $(".info-cover").html(">");
      });

    }
  });
  $(window).trigger("resize");
});

$(document).keydown(function(e) {
  if (e.keyCode === 37 || e.keyCode === 39) {
    e.preventDefault();
    var target = $('#mainFrame')[0].contentDocument.getElementById('editBox');
    if (target) {
      if (e.keyCode === 37) {
        target.scrollLeft = 0;
      } else if (e.keyCode === 39) {
        target.scrollLeft = $(target).width();
      }
    }
  }
});

function getPatientHistory() {
  var type = $('.search-type option:selected').val();
  var keyWord = $('#keyword').val();
  var param;

  if (keyWord === '') return;

  switch (type) {
    case 'patId':
      param = {
        patId: keyWord
      };
      break;
    case 'inHospNo':
      param = {
        inHospNo: keyWord
      };
      break;
    case 'patName':
      param = {
        patName: keyWord
      };
      break;
    default:
      break;
  }

  param.deptCode = localStorage.getItem('deptCode');

  $.get(ay.contextPath + '/nur/patientGlance/getPatientBaseInfoByPatInfo.do', param).done(function(res) {
    if (res.rslt === '0') {
      createTable(res.data.patients);
      debugger;
    } else {
      $.messager.alert('提示', res.msg);
    }
  }).fail(function(err) {

  });
}

function createTable(data) {
  var $historyTable = $('#history-patient-table');
  var $historyDialog = $('.dialog-history-patient');

  $historyDialog.dialog({
    title: '患者住院历史',
    width: 500,
    height: 300,
    modal: true,
    closed: true,
    buttons: [{
      text: '关闭',
      handler: function() {
        $historyDialog.dialog("close");
      }
    }]
  });

  $historyTable.datagrid({
    columns: [
      [{
        field: 'bedCode',
        title: '床号',
        width: 42
      }, {
        field: 'name',
        title: '姓名',
        width: 51
      }, {
        field: 'patId',
        title: '病人编号',
        width: 125
      }, {
        field: 'inHospNo',
        title: '住院号',
        width: 91
      }, {
        field: 'inDate',
        title: '住院日期',
        width: 180
      }]
    ],
    height: 280,
    data: data
  });

  $historyTable.datagrid({
    onDblClickRow: intoHistory
  });

  $historyDialog.show().dialog('open');
}

function intoHistory(index, patient) {
  jsPlugin.loadPlugin(ay.contextPath + '/resources/js/main/patientMenu.js', function() {
    isHistoryMethod.loadMenu(patient);
  });

  $('.dialog-history-patient').dialog('close');
}

//排序
function compare(k) {
  return function(a, b) {
    var v1 = a[k];
    var v2 = b[k];
    if (/^[0-9]/.test(v1) && /^[0-9]/.test(v2)) {
      return v1 - v2;
    } else if (!/^[0-9]/.test(v1)) {
      return 1;
    } else if (!/^[0-9]/.test(v2)) {
      return -1;
    }
  }
}

function loadMenu() {
  // debugger;
  jsPlugin.doPlugin('patientMenu', {
    'patientId': patient.patientId,
    'hospitalNo': patient.hospitalNo,
    'departmentId': patient.departmentId,
    'isHistoryDoc': isHistoryDoc
  });
}

var isHistoryMethod = (function($) {
  function loadMenu(patientItem) {
    var _patient = patientItem || patient;
    // debugger;
    jsPlugin.doPlugin('patientMenu', {
      'patientId': _patient.patId,
      'hospitalNo': _patient.inHospNo,
      'departmentId': _patient.deptCode,
      'isHistoryDoc': isHistoryDoc
    });
  }

  return {
    loadMenu: loadMenu
  }
})(jQuery);
