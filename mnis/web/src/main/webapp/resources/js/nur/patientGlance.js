var sexMap = getSexDictionary();
var tendMap = getTendMap();
var chargeTypeMap = getChargeTypeDictionary();
var patientList = [];
/*var cacheMsg = null;*/
var attention = 1;
var currentDay = null;
var jumpUrlTemp = localStorage.getItem("jumpUrlTemp", jumpUrlTemp) || null;
/* 检验拓展 */
$.extend($.fn.validatebox.defaults.rules, {
  equals: {
    validator: function(value, param) {
      return value == $(param[0]).val();
    },
    message: '两次密码不一致'
  }
});

function search() {
  var condition = $("#searchCondition").val();
  var keyword = $("#keyword").val().trim();
  var patientList = $(".patientBox");
  if (!keyword) {
    patientList.show('normal');

  } else {

    patientList.each(function() {

      if (condition == 'searchBedCode' && ($(this).attr('data-' + condition) == keyword || $(this).attr('data-bedcode') == keyword || $(this).attr('data-' + condition).indexOf(keyword) >= 0)) {
        $(this).show('normal');
      } else if (condition == 'patientname' && $(this).attr('data-' + condition).indexOf(keyword) >= 0) {
        $(this).show('normal');
      } else if (condition == 'inHospitalNo' && $(this).attr('data-' + condition).indexOf(keyword) >= 0) {
        $(this).show('normal');
      } else {
        $(this).hide('normal');
      }
    });
  }

  //清除左侧的信息
  $('#view_patName').text('');
  $('#view_bedCode').text('');
  $('#view_hospitalNo').text('');
  $('#view_patBcod').text('');
  $('#view_gender').text('');
  $('#view_age').text('');
  $('#view_birthday').text('');
  /*$('#view_address').text('');
  $('#view_telephoneNo').text('');
  $('#view_contactPerson').text('');
  $('#view_relationship').text('');
  $('#view_contactTelephone').text('');*/
  $('#view_admitDate').text('');
  /*$('#view_adminDoc').text('');*/
  $('#view_adminDiag').text('');
  $('#view_workUnitName').text('');
  $('#view_dutyDoctorName').text('');
  $('#view_dutyNurseName').text('');
  $('#view_diet').text('');
  $('#view_tendLevel').text('');
  $('#view_allergyDrugs').text('');
  $('#view_evaluate').text('');
  //$('#view_chargeType').text(chargeTypeMap.get(patInfo.chargeType));
  $('#view_chargeType').text('');
  $('#view_feePaid').text('');
  $('#view_feeUsed').text('');
  $('#view_restAmt').text('');
  $('#view_remark').text('');

}

function getMessage(isFirst, speed) {
  /*var deptId = $("#deptId").val();
  var nurseId = $("#nurseId").val();
  var dateTime = $("#noticeMsgDate").val();
  var url = ay.contextPath + '/nur/task/notice/list.do?nurseId='+nurseId+'&deptId='+deptId+'&date='+dateTime;
  //第一次装载
  if(isFirst){
    $.get(url, function(result) {

      try {
        cacheMsg = result.lst;
        $('#showMessage').empty();
        var hour;
        var min;
        var date = result.lst;
        var str = "<ul>";
        for(var i=0; i<date.length;i++){
          var d = new Date(date[i].time);
          d.getHours() < 10 ? hour = "0" + d.getHours() : hour = d.getHours();
          d.getMinutes() < 10 ? min = "0" + d.getMinutes() : min = d.getMinutes();
          str = "<li><span class='fl time'>"+hour+":"+min+"</span><span class='smsg fl' title='"+date[i].msgText+"'>"+date[i].nurseName+"： "+date[i].msgText+"</span></li>"
          $('#showMessage').prepend(str);
        }
      } catch (e) {
        $.messager.alert('提示', e);
      }
      setTimeout("getMessage(false,"+speed+")", speed);
    });
    return ;
  }
  else{
    $.get(url, function(result) {
      try {
        var resultLength = result.lst.length
        if(resultLength-cacheMsg.length<=0){
          console.log("no add");
          setTimeout("getMessage(false,"+speed+")", speed);
          return ;
        }
        var str = '';
        for(var i=cacheMsg.length;i<resultLength;i++){
          console.log("add:1");
          var d = new Date(result.lst[i].time);
          d.getHours() < 10 ? hour = "0" + d.getHours() : hour = d.getHours();
          d.getMinutes() < 10 ? min = "0" + d.getMinutes() : min = d.getMinutes();
          str = "<li><span class='fl time'>"+hour+":"+min+"</span><span class='smsg fl' title='"+result.lst[i].msgText+"'>"+result.lst[i].nurseName+"： "+result.lst[i].msgText+"</span></li>"
          $('#showMessage').prepend(str);
        }
        cacheMsg = result.lst;
        setTimeout("getMessage(false,"+speed+")", speed);
        return ;
      } catch (e) {
        $.messager.alert('提示', e);
      }
    });
    return ;
  }*/
}

//define.amd.jQjuery = true;
$(function() {
  var oH = $(window).height();
  var oh1 = oH - 120;
  $(".content").height(oh1 - 27);
  $("#middle").height(oh1 - 57);
  var currDate = new Date();
  currentDay = currDate.getDate();
  var recordDay = currDate.format("yyyy-MM-dd");
  $("#noticeMsgDate").val(recordDay);
  patientList = $('.patientBox');
  //getMessage(true,5000);
  if (!config || config.rslt != '0') {
    $.get(ay.contextPath + '/nur/system/getConfig.do', function(config) {
      //将配置文件缓存至浏览器端
      localStorage.setItem("config", JSON.stringify(config));
    });
  }

  //当回到首页存在跳转ID时清楚该ID
  if (jumpUrlTemp) {
    jumpUrlTemp = null;
    localStorage.setItem("jumpUrlTemp", jumpUrlTemp);
  }

  //用于盛京演示，点击logo，清除测试数据数据
  /*$("#logo").click(function() {
    var url = ay.contextPath + "/nur/patientGlance/deleteTestDate.do";
    $.get(url,function(data){

    });
  });*/

  // 点击导航条的超链接，背景显示且文字变色
  $("ul.nav").find("a").click(function() {
    $(this).addClass("click");
    $(this).parent().siblings().children().removeClass("click");
  });

  // 点击病人一览，导航条2显示且中间部分高度变小
  $("#nav1").find("a:first").click(function() {
    $("#nav2").show();
    //$("#middle").css("height", "582px");
  });

  // 点击导航条1中除病人一览外的超链接，导航条2隐藏且中间部分高度变成原高度
  $("#nav1").find("a").not(":first").click(function() {
    $("#nav2").hide();
    $("#middle").css("height", "");
  });

  // 点击编辑图标，背景改变
  $('#edit').hide();
  $("#edit").click(function() {
    $(this).addClass("click");
    loadEditPatientInfo();
  });

  // 点击喇叭处，消息弹出框显示
  $("#speaker").click(function() {
    $("#msgPrompt").show();
  });

  $("#note").click(function() {
    var url = ay.contextPath + '/nur/nurseWhiteBoard/noteShow.do';
    //    var url = ay.contextPath + '/nur/nurseWhiteBoard/nurseWhiteBoardShowMain.do';
    //    var url = ay.contextPath + '/nur/nursingMemo/oldNoteMain.do';
    window.open(url);
  });

  // 点击通知消息或任务清单，当前元素文字变色，背景改变
  $("#noticeMsg, #taskList").click(function() {
    $(this).addClass("click");
    $(this).siblings("span.bg").removeClass("click");
  });

  // 点击任务清单，内容1隐藏、内容2显示
  $("#taskList").click(function() {
    //读取

    $("#content1").hide();
    $("#content2").show();
  });

  // 通知消息日期选择按钮
  $("#content1 .chooseDateBtn").click(function() {
    var cDate;
    if ($(this).hasClass('arrow_l')) {
      cDate = new Date($("#noticeMsgDate").val());
      day = cDate.getDate() - 1;
      cDate.setDate(day);
      $("#noticeMsgDate").val(cDate.getFullYear() + '-' + (cDate.getMonth() + 1) + '-' + cDate.getDate());
    } else {
      cDate = new Date($("#noticeMsgDate").val());
      day = cDate.getDate() + 1;
      if (day > currentDay) {
        return;
      }
      cDate.setDate(day);
      $("#noticeMsgDate").val(cDate.getFullYear() + '-' + (cDate.getMonth() + 1) + '-' + cDate.getDate());
    }
  });
  $("#content2 .chooseDateBtn").click(function() {
    var cDate;
    if ($(this).hasClass('arrow_l')) {
      cDate = new Date($("#taskListDate").val());
      day = cDate.getDate() - 1;
      cDate.setDate(day);
      $("#taskListDate").val(cDate.getFullYear() + '-' + (cDate.getMonth() + 1) + '-' + cDate.getDate());

    } else {
      cDate = new Date($("#taskListDate").val());
      day = cDate.getDate() + 1;
      cDate.setDate(day);
      $("#taskListDate").val(cDate.getFullYear() + '-' + (cDate.getMonth() + 1) + '-' + cDate.getDate());

    }
  });

  // 点击通知消息，内容2隐藏、内容1显示
  $("#noticeMsg").click(function() {
    $("#content2").hide();
    $("#content1").show();
  });

  // 点击消息弹出框的单选按钮，当前元素选中、另外两个不选中
  $("#msgPrompt").find("span.circle").click(function() {
    $(this).addClass("circle2");
    $(this).siblings().removeClass("circle2");
  });

  // 点击关闭按钮，消息弹出框隐藏
  $("#close").click(function() {
    $("#msgPrompt").hide();
  });

  // 奇数次点击展开/收缩箭头，右边两部分内容收缩；偶数次点击展开/收缩箭头，右边两部分内容还原
  $("#arrow").toggle(
    function() {
      $(".main_r").animate({ 'width': '1218px' }, 500);
      $(".main_r .middle ul").css({ "paddingLeft": "6.4px", "marginLeft": 0 });
      $(".main_r .middle ul li").css("marginRight", "4px");
      $(".list-view-top").css({ "width": "1210px", "marginLeft": "6.4px" });
      $(this).css({
        "backgroundPositionY": "0px"
      });
    },
    function() {
      $(".main_r").animate({ 'width': '1054px' }, 500);
      $(".main_r .middle ul").css("paddingLeft", "12px");
      $(".main_r .middle ul li").css("marginRight", "10px");
      $(".list-view-top").css({ "width": "1040px", "marginLeft": "12px" });
      $(this).css({
        "backgroundPositionY": "-34px"

      });
    })

  $('.choice-group').on('click', '.choice-item', function() {
    if ($(this).hasClass('choice-people')) {
      patientListStatus = 0;
      $("#keyword").val('');
      getPersonAttention();
    } else if ($(this).hasClass('choice-hospital')) {
      $("#keyword").val('');
      getAllPatient();
    } else if ($(this).hasClass('choice-table')) {
      $("#middle").removeClass('lists-view');
    } else if ($(this).hasClass('choice-list')) {
      $("#middle").addClass('lists-view');
    }
  });

  // 点击五角星，背景来回切换
  $("span.star").click(function() {
    var nurseId = $("#nurseId").val();
    var deptCode = $("#deptId").val();
    var bedCode = $(this).attr('data-bedCode');
    if (!attention) {
      return;
    }
    attention = 0;
    if ($(this).hasClass('active')) {
      try {
        var url = ay.contextPath + "/nur/task/attention/del.do";
        $.post(url, {
          nurseId: nurseId,
          bedCode: bedCode,
          deptCode: deptCode
        }, function(data) {
          if (data && data.rslt == 0) {
            attention = 1;
            $(this).toggleClass("active");
            $.messager.alert('提示', '取消关注成功！');
            if (!$(".choice-hospital").hasClass('active')) {
              getPersonAttention();
            }
          }
        });
      } catch (e) {
        $.messager.alert('提示', '错误：' + e);
        attention = 1;
      }
    } else {
      try {
        var url = ay.contextPath + "/nur/task/attention/add.do";
        $.post(url, {
          nurseId: nurseId,
          bedCode: bedCode,
          deptCode: deptCode
        }, function(data) {
          console.log(data);
          if (data && data.rslt == 0) {
            attention = 1;
            $(this).toggleClass("active");
            $.messager.alert('提示', '关注成功！');
          }
        });
      } catch (e) {
        $.messager.alert('提示', '错误：' + e);
        attention = 0;
      }
    }
    $(this).toggleClass("active");
    return false;
  });

  $('#savePatientBtn').click(function() {
    updatePatientInfo();
  });
  $("#keyword").one('focusin', function() {
    $(document).keypress(function(e) {
      if (event.keyCode == 13) {
        $(".search").click();
      }
      //return false;
    });
  });

});

$(document).ready(function() {
  /*var url = ay.contextPath
  + "/nur/patientGlance/queryDeptSummary.do?workUnitType=1&workUnitCode=1005";
  $.get(url, function(result) {
    try {
      var date = result.lst.workUnitStatistics;
      var str='<em>'+(date.inpatientCount-date.emptyBedCount)+'</em>';
      $('#zyrs').append(str);
      str='<em>'+date.emptyBedCount+'</em>';
      $('#kcs').append(str);
      str='<em>'+date.inDebtCount+'</em>'
      $('#qf').append(str);
      str='<em>'+date.highTempCount+'</em><br />';
      $('#frrs').append(str);
      str=date.tendLevelSuperCount;
      $('#tjhl').append(str);
      str=date.tendLevelOneCount;
      $('#yjhl').append(str);
      str=date.tendLevelTwoCount;
      $('#ejhl').append(str);
      str=date.tendLevelThreeCount;
      $('#sjhl').append(str);
    } catch (e) {
      $.messager.alert('提示', e);
    }
  });*/

  var d = new Date();
  var sD = d.getFullYear() + '-0' + (parseInt(d.getMonth()) + 1) + '-0' + d.getDay();
  $("#msgTime").val(sD);

  localStorage.setItem('firstPatientId', $('.patientBox').eq(0).data('patientid'));
  localStorage.setItem('deptCode', $('#deptId').val());
  localStorage.setItem('nurseCode', $('#nurseId').val());
  localStorage.setItem('nurseName', $('#nurseName').text());
  //console.log(sD);

  //获得全科关注病人
  var nurseId = $("#nurseId").val();
  var deptCode = $("#deptId").val();
  var attentionUrl = ay.contextPath + "/nur/task/attention/get.do?nurseId=" + nurseId + "&deptCode=" + deptCode;
  $.get(attentionUrl, function(data) {
    try {
      var lst = data.data.list;
      var starList = $(".star");
      for (var i = 0; i < lst.length; i++) {
        starList.each(function() {
          if ($(this).attr('data-patientId') == lst[i].patientId) {
            $(this).addClass('active');
          }
        })
      }
    }
    //getPersonAttention();
    catch (e) {
      $.messager.alert('提示', '错误：' + e);
    }
  });



});

//得到个人关注病人
function getPersonAttention() {
  var list = $(".star");
  patientList = [];
  list.each(function() {
    if (!$(this).hasClass('active')) {
      $(this).parent().parent().parent().fadeOut('normal');
    } else {
      patientList.push($(this).parent().parent().parent()[0]);
    }
  });
  patientList = $(patientList);
  console.log(patientList)
}

/*
 * 得到全科病人
 */
function getAllPatient() {
  $(".patientBox").fadeIn('normal');
  patientList = $('.patientBox');
}

function getInPatInfoById(id) {
  $('#edit').fadeIn();
  $('#currPatientId').val(id);
  $.post(ay.contextPath + "/nur/patientGlance/getInpatInfoByPatId", {
    id: id
  }, function(result) {
    console.log(result);
    var patInfo = result.data == null ? null : result.data.inpatientInfo;
    if (patInfo != null) {
      $('#view_patName').text(patInfo.name);
      $('#view_bedCode').text(patInfo.bedCode);
      $('#view_hospitalNo').text(patInfo.inHospNo);
      $('#view_patBcod').text(patInfo.barCode);
      $('#view_gender').text(sexMap.get(patInfo.gender));
      $('#view_age').text(patInfo.age);
      $('#view_birthday').text(patInfo.birthday == null ? "" : patInfo.birthday.substring(0, 10));
      /*$('#view_address').text('');
      $('#view_telephoneNo').text('');
      $('#view_contactPerson').text('');
      $('#view_relationship').text('');
      $('#view_contactTelephone').text('');*/
      $('#view_admitDate').text(patInfo.inDate);
      /*$('#view_adminDoc').text('');*/
      $('#view_adminDiag').text(patInfo.inDiag == null ? '' : patInfo.inDiag);
      $('#view_workUnitName').text(patInfo.deptName);
      $('#view_dutyDoctorName').text(patInfo.doctorName == null ? '' : patInfo.doctorName);
      $('#view_dutyNurseName').text(patInfo.dutyNurseName == null ? '' : patInfo.dutyNurseName);
      $('#view_diet').text(patInfo.diet == null ? '' : patInfo.diet);
      $('#view_tendLevel').text(tendMap.get(patInfo.tendLevel));
      $('#view_allergyDrugs').text('');
      $('#view_allergyDrugs').text(patInfo.allergen);
      $('#view_evaluate').text('');
      //$('#view_chargeType').text(chargeTypeMap.get(patInfo.chargeType));
      $('#view_chargeType').text(patInfo.chargeType == null ? '' : patInfo.chargeType);
      $('#view_feePaid').text(patInfo.prepayCost.toFixed(2) + '元');
      $('#view_feeUsed').text(patInfo.ownCost.toFixed(2) + '元');
      var rest = parseFloat(patInfo.prepayCost) - parseFloat(patInfo.ownCost);
      /*if (rest > 0.0) {
        $('#view_restAmt').text(rest + '元');
      } else {
        $('#view_restAmt').text('0.00元');
      }*/
      $('#view_restAmt').text(rest.toFixed(2) + '元');
      $('#view_remark').text('');
    }
  });
}
/*function exportExcel() {
    var url =  $('#exportForm').attr('action');
    url = url + "?bedCode=" + $('#search').val();
    $('#exportForm').attr('action', url);
    $('#exportForm').submit();
}*/
function forwardPatientMain(id) {
  if (!id) {
    $.messager.alert('提示', '请选择患者');
    return;
  }
  window.location.href = ay.contextPath + '/index/patientMain.do?id=' + id;
}

function loadEditPatientInfo() {
  var patientId = $('#currPatientId').val();
  $.post('./getInpatInfoByPatId.do', {
    id: patientId
  }, function(result) {
    try {
      if (result.success) {
        var patInfo = result.obj == null ? null : result.obj.inpatientInfo;
        if (patInfo != null) {
          $('#edit_patientId').val(patInfo.patientBaseInfo.patientId);
          $('#edit_patientName').val(patInfo.patientBaseInfo.patientName);
          $('#l_edit_patientName').text(patInfo.patientBaseInfo.patientName);
          $('#edit_bedCode').val(patInfo.bedCode);
          $('#l_edit_bedCode').text(patInfo.bedCode);
          $('#edit_inHospitalNo').val(patInfo.inHospitalNo);
          $('#l_edit_inHospitalNo').text(patInfo.inHospitalNo);
          $('#edit_patientBarcode').val(patInfo.patientBarcode);
          $('#l_edit_patientBarcode').text(patInfo.patientBarcode);
          $('#edit_gender').val(patInfo.patientBaseInfo.gender);
          $('#l_edit_gender').text(sexMap.get(patInfo.patientBaseInfo.gender));
          $('#l_edit_age').text(patInfo.patientBaseInfo.age);
          $('#edit_birthday').val(patInfo.patientBaseInfo.birthday);
          $('#edit_admitDate').val(patInfo.admitDate);
          $('#edit_workUnit').combobox('setValue', patInfo.workUnitCode);
          $('#edit_diet').val(patInfo.diet == null ? '' : patInfo.diet);
          $('#edit_dutyDoctor').combobox('setValue', patInfo.dutyDoctorCode);
          $('#edit_tendLevel').combobox('setValue', patInfo.tendLevel);
          $('#l_edit_admitDiag').text(patInfo.admitDiagnosis);
          $('#edit_patient_diags').val(result.obj.admitDiags);
          $('#l_edit_allergys').text(patInfo.allergyDrugs);
          $('#edit_patient_allergys').val(result.obj.allergyStr);
          $('#edit_chargeType').val(patInfo.chargeType);
          $('#l_edit_chargeType').text(chargeTypeMap.get(patInfo.chargeType));
          $('#edit_freePaid').val(patInfo.feePaid);
          $('#l_edit_freePaid').text(patInfo.feePaid.toFixed(2) + '元');
          $('#edit_feeUsed').val(patInfo.feeUsed);
          $('#l_edit_feeUsed').text(patInfo.feeUsed.toFixed(2) + '元');
          var rest = parseFloat(patInfo.feePaid) - parseFloat(patInfo.feeUsed);
          /*if (rest > 0.0) {
            $('#l_edit_restAmt').text(rest + '元');
          } else {
            $('#l_edit_restAmt').text('0.00元');
          }*/
          $('#l_edit_restAmt').text(rest.toFixed(2) + '元');
        }
      } else {
        $.messager.alert('错误', result, 'error');
      }
    } catch (e) {
      $.messager.alert('提示', result);
    }
  });
}

function updatePatientInfo() {
  var params = ay.serializeObject($('#patientInfoForm'));
  if ($("#edit_diet").val().length > 80) {
    $.messager.alert('错误', "饮食字数不能超过80！", 'error');
    return;
  }
  var url = ay.contextPath + '/nur/patientGlance/updatePatInfo.do';
  $.post(url,
    params,
    function(result) {
      try {
        if (result.success) {
          $.messager.alert('提示', '修改患者信息成功!', 'info');
          closeEditPatientWindow();
        } else {
          $.messager.alert('提示', result.msg);
        }
      } catch (e) {
        $.messager.alert('错误', result, 'error');
      }
    });
}

function closeEditPatientWindow() {
  $("#shadow,.ua-box").hide();
  var id = $('#currPatientId').val();
  currCheckedPatientId = null;
  getInPatInfoById(id);
}

function setPatientAge() {
  var birthday = $('#edit_birthday').val();
  var url = ay.contextPath + '/nur/patientGlance/getPatientAge.do';
  $.post(url, { birthday: birthday }, function(result) {
    try {
      if (result.success) {
        $('#l_edit_age').text(result.obj);
      } else {
        $.messager.alert('提示', result.msg);
      }
    } catch (e) {
      $.messager.alert('错误', result, 'error');
    }
  });
}

function selectDept(element) {
  var deptName = element.name;
  $('#edit_workUnitName').val(deptName);
}

function selectDoctor(element) {
  var emplName = element.name;
  $('#edit_dutyDoctorName').val(emplName);
}

function updateAllergyBtn() {
  url = ay.contextPath + '/nur/patientGlance/patAllergHistEdit.do';
  $.modalDialog({
    collapsible: false,
    minimizable: false,
    maximizable: false,
    width: 620,
    height: 450,
    closed: false,
    resizable: false,
    href: url,
    method: "POST",
    title: '选择药物',
    onLoad: function() {
      bindDialogEvent();
      var allergyList = ay.stringToList($('#l_edit_allergys').text());
      $.modalDialog.handler.find('input[type=checkbox]').each(function(i) {
        var allergyTxt = $(this).val();
        if ($.inArray(allergyTxt, allergyList) >= 0) {
          $(this).attr('checked', true);
        }
      });
    }
  });
}

function bindDialogEvent() {
  $.modalDialog.handler.find('#selectAllergBtn').bind("click", function(event) {
    var selectStr = '';
    var allergyArray = [];
    var selectObjStr = '';
    $.modalDialog.handler.find('input[type=checkbox]:checked').each(function(i) {
      var name = $(this).val();
      var code = $(this).attr('id');
      var allergyRecord = {
        drugCode: code,
        drugName: name
      };
      allergyArray.push(allergyRecord);
      selectStr = selectStr + name + ',';
    });

    if (allergyArray && allergyArray.length > 0) {
      selectStr = selectStr.substr(0, selectStr.length - 1);
      selectObjStr = JSON.stringify(allergyArray);
    }
    $('#l_edit_allergys').text(selectStr);
    $('#edit_patient_allergys').val(selectObjStr);
    var d = $.modalDialog.handler.closest('.window-body');
    d.dialog('destroy');
  });

}

/*function updateDiagsis() {
  url = ay.contextPath + '/nur/patientGlance/patientDiagEdit.do';
  $.modalDialog({
        collapsible: false,
        minimizable: false,
        maximizable: false,
        width: 620,
        height: 450,
        closed: false,
        resizable: false,
        href: url,
        method: "POST",
        bodyCls:'aaaa',
        title: '选择诊断',
        onLoad: function () {
            bindDiagDialogEvent();
            var diagList = ay.stringToList($('#l_edit_admitDiag').text());
            $.modalDialog.handler.find('input[type=checkbox]').each(function(i){
              var name = $(this).val();
              if ( $.inArray(name, diagList) >= 0) {
                $(this).attr('checked', true);
              }
            });
        }
    });
}*/

function bindDiagDialogEvent() {
  $.modalDialog.handler.find('#selectDiagBtn').bind("click", function(event) {
    var selectStr = '';
    var selectObjStr = '';
    var diagArray = [];
    $.modalDialog.handler.find('input[type=checkbox]:checked').each(function(i) {
      var name = $(this).val();
      var code = $(this).attr('id');
      var diagRecord = {
        diagCode: code,
        diagName: name
      };
      diagArray.push(diagRecord);
      selectStr = selectStr + name + ',';
    });

    if (diagArray && diagArray.length > 0) {
      selectStr = selectStr.substr(0, selectStr.length - 1);
      selectObjStr = JSON.stringify(diagArray);
    }
    $('#l_edit_admitDiag').text(selectStr);
    $('#admitDiagStr').val(selectObjStr);
    var d = $.modalDialog.handler.closest('.window-body');
    d.dialog('destroy');
  });

}

Date.prototype.format = function(format) {
  var o = {
    "M+": this.getMonth() + 1, // month
    "d+": this.getDate(), // day
    "h+": this.getHours(), // hour
    "m+": this.getMinutes(), // minute
    "s+": this.getSeconds(), // second
    "q+": Math.floor((this.getMonth() + 3) / 3), // quarter
    "S": this.getMilliseconds()
      // millisecond
  };
  if (/(y+)/.test(format))
    format = format.replace(RegExp.$1, (this.getFullYear() + "")
      .substr(4 - RegExp.$1.length));
  for (var k in o)
    if (new RegExp("(" + k + ")").test(format))
      format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
  return format;
}
