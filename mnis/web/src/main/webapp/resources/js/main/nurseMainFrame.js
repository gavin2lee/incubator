var peopleLen = 0,
  peopleList = [],
  fullWidth = 0,
  notFullWidth = 0,
  tempShowState = false,
  isQueryPatientList = false,
  currentDay = null,
  config = $.parseJSON(sessionStorage.getItem('config')), //获取配置文件
  leftWidth = 0;

var log = console.log.bind(console);
$(function() {
  setNurseMenu(config);
  if (menuId != "") {
    $("#" + menuId).click();
  }
  $('#nurseLayout').layout('panel', 'center').panel({
    onResize: function(width, height) {
      ay.setIframeHeight('nurMainFrame', $('#nurseLayout').layout('panel', 'center').panel('options').height);
      ay.setIframeWidth('nurMainFrame', $('#nurseLayout').layout('panel', 'center').panel('options').width);
    }
  });

  ay.setIframeHeight('nurMainFrame', $('#nurseLayout').layout('panel', 'center').panel('options').height);
  ay.setIframeWidth('nurMainFrame', $('#nurseLayout').layout('panel', 'center').panel('options').width);

});

function clearNav() {
  $("#nav1 > li").each(function() {
    if ($(this).hasClass('checked')) {
      $(this).removeClass('checked');
      $(this).find('li').each(function() {
        if ($(this).hasClass('checked')) {
          $(this).removeClass('checked');
        }
      })
    }
  });
}

function openNurseSubTab(pluginKey, pluginUrl, mainFrameUrl, params) {
  jsPlugin.loadPlugin(pluginUrl, function() {
    jsPlugin.doPlugin(pluginKey, params);
  });
  $('.layout-panel-west').css('left', "0px")
  if (notFullWidth) {
    $('.layout-panel-west').css({ 'display': 'block' });
    $('.layout-panel-center').css('width', notFullWidth);
    $('.layout-panel-center .layout-body').css('width', notFullWidth);
    ay.setIframeWidth('nurMainFrame', notFullWidth);
    $('.layout-panel-center').css('left', leftWidth + "px");
    notFullWidth = 0;
  }
  /*if( params.menuId == 'metadataManager' && params.menuId == 'patCrisValue' ){
   if(!notFullWidth){
   notFullWidth = parseInt($('.layout-panel-center').css('width'));
   leftWidth    = parseInt($('.layout-panel-center').css('left'));
   fullWidth = parseInt($('.layout-panel-center').css('left'))+parseInt($('.layout-panel-center').css('width'));
   }

   $('.layout-panel-west').css({'display':'none'});
   $('.layout-panel-center').css('width',fullWidth);
   $('.layout-panel-center .layout-body').css('width',fullWidth);
   ay.setIframeWidth('nurMainFrame', fullWidth);
   $('.layout-panel-center').css('left',0);
   }*/
  if (params && params.menuId) {
    var jumpUrl = $(window)[0].location.search.substring(0, $(window)[0].location.search.indexOf('=') + 1) + params.menuId;
    if (window.history.pushState) {
      window.history.pushState('', '', jumpUrl);
    }
    clearNav();
    $("#" + params.menuId).parent().addClass('checked').parent().parent().addClass('checked');
  }
  if (mainFrameUrl) {
    $('#nurMainFrame').attr('src', mainFrameUrl);
  }
}

/*
 * 得到病人列表
 * @params 地址
 * @params callback
 * @returns fn
 */
function getBedList(url, method, params) {
  $.post(ay.contextPath + url, {}, function(data) {
    if (data.obj) {
      bedInfoList = data.obj.bedInfoList;
    } else {
      bedInfoList = data.data.bedInfoList;
    }
    bedInfoList.sort(compare('showBedCode'));
    bedInfoListStr = getBedInfoListStr(bedInfoList, peopleLen);
    var list = $(bedInfoListStr);
    list.find('input[type=checkbox]').chkbox();
    $("#mainMenu").append(list);
    //设置事件
    setPatientEvent(method, params.isSetAllCheck, params.isSingle, params.sendRequest);
    //获取关注
    /*var nurseId  = $("#nurseId").val();
     var deptCode = $("#deptId").val();
     var attenPeopleListUrl = '/nur/task/attention/get.do?nurseId=' + nurseId+"&deptCode="+deptCode;
     if(!$("#patientId").val()){
     $.get(ay.contextPath+attenPeopleListUrl,function(data){
     attentionPatient = data.data.list;

     if(attentionPatient.length > 0){
     for( var i=0;i<attentionPatient.length;i++){

     var pId = $(".patient[data-bedcode='"+attentionPatient[i].bedCode+"']").attr('checked',true).val();
     //console.log(pId);
     peopleList = addToPeopleList(peopleList,pId);
     }
     if(peopleList.length>0){
     $('#nurMainFrame')[0].contentWindow[method](peopleList.toString());
     }
     }
     });
     }*/
  });
}

/*
 * 设置点击事件
 *
 */
function setPatientEvent(method, isSetAllCheck, isSingle, sendRequest, checkCallback) {
  $("#checkAllPatient").unbind('click');
  $('.patient').unbind('click');
  //绑定全选事件
  if (isSetAllCheck) {
    $("#checkAllPatient").click(function() {
      isClickFullBtn = true;
      if ($(this).attr('checked')) {
        peopleList = getAllPatientNum([], $('.patient'));
      } else {
        peopleList = [];
        $('.patient').each(function() {
          $(this).attr('checked', false);
        });
      }
      //隐藏字页面打开的combo下拉框
      $(".combo-p", $("#nurMainFrame")[0].contentWindow['document']).hide();

      if (sendRequest != false) {
        $('#nurMainFrame')[0].contentWindow[method](peopleList.toString());
      }
      isClickFullBtn = false;
    });
  } else {
    $("#checkAllPatient").hide();
  }

  //单选事件绑定
  $('.patient').bind('click', function() {
    var isHasBed = $('#nurMainFrame')[0].contentWindow['document'].getElementById('nurPatientInfo') ? true : false;
    console.log(isHasBed)
    if (peopleList.length == 0) {
      var len = $('.patient').length;
      for (var i = 0; i < len; i++) {
        peopleList[i] = '';
      }
    }
    if ($(this).attr("checked") == "checked") {
      //console.log($('#nurMainFrame')[0].contentWindow['document'].getElementById());
      //切换或设置选中人床号和姓名
      if (isHasBed) {
        var id = $(this).attr('value');
        //获取患者信息
        $.post(ay.contextPath + "/nur/patientGlance" + "/getInpatInfoByPatId.do", {
          id: id
        }, function(data) {
          if (!data) {
            return;
          }
          var patientInfoWrap = $('#nurMainFrame')[0].contentWindow['document'].getElementById('nurPatientInfo');
          var d = data.data.inpatientInfo;
          $(patientInfoWrap).html('');
          var gender = d.gender == 'M' ? '男' : '女';
          var admitDiagnosis = d.admitDiagnosis ? d.admitDiagnosis : '无';
          var diet = d.diet ? d.diet : '无';
          var allergens = d.allergen ? d.allergen : '';
          var patientName = d.name ? d.name : '无';
          var age = d.age ? d.age : '无';
          $(patientInfoWrap).css('left', '0px');
          $(patientInfoWrap).append('<span style="max-width:45px;">' + d.bedCode + '床</span>' +
            '<span id="patientName" style="max-width:58px;">' + patientName + '</span>' +
            '<span style="max-width:145px;">住院号：' + d.inHospNo + '</span>' +
            '<span style="max-width:20px;">' + gender + '</span>' +
            '<span style="max-width:38px;">' + age + '</span>' +
            '<span style="max-width:100px;" title="' + admitDiagnosis + '">诊断：' + admitDiagnosis + '</span>' +
            '<span style="max-width:135px;" title="' + diet + '">饮食：' + diet + '</span>' +
            '<span style="max-width:175px;" title="' + allergens + '">药物过敏：<font style="color:#f00;">' + allergens + '</font></span>'
          );
          if ($(patientInfoWrap).width() >= $(patientInfoWrap).parent().width()) {
            var leftNum = $(patientInfoWrap).width() - $(patientInfoWrap).parent().width() + 25;
            var infoCoverBtn = $("#info-cover", $('#nurMainFrame')[0].contentWindow['document']);
            infoCoverBtn.show();
            infoCoverBtn.html('&lt;');
            infoCoverBtn.unbind('click').bind('click', function() {
              if ($(this).html() == '&lt;') {
                $(this).html('&gt;');
                $(patientInfoWrap).animate({ 'left': -leftNum + 'px' }, 400);
              } else {
                $(patientInfoWrap).animate({ 'left': '0px' }, 400);
                $(this).html('&lt;');
              }

            });
          } else {
            $("#info-cover", $('#nurMainFrame')[0].contentWindow['document']).hide();
          }
        });
        /*$('#nurMainFrame')[0].contentWindow['document'].getElementById('checkedBedCode').innerHTML='床号：'+$(this).attr('data-showbedcode');
         $('#nurMainFrame')[0].contentWindow['document'].getElementById('checkedPatientName').innerHTML='姓名：'+$(this).next().find('span[class="bed-name"]').html();
         if( !$('#nurMainFrame')[0].contentWindow['document'].getElementById('checkedHosNo') ){
         $( $('#nurMainFrame')[0].contentWindow['document'].getElementById('checkedBedCode') ).css('margin-right',"15px").after("<span id='checkedHosNo'></span>");
         }
         $('#nurMainFrame')[0].contentWindow['document'].getElementById('checkedHosNo').innerHTML='住院号：'+$(this).attr('value');*/
      }
      //切换选中项
      if (isSingle) {
        $(".patient[value=" + peopleList[0] + "]").attr('checked', false);
        window.peopleList = peopleList = [$(this).attr('value')];
      } else {
        addToPeopleList(peopleList, $(this).attr('value'), $(this).closest('li').index());
      }
    } else {

      //清空患者信息
      var patientInfoWrap = $('#nurMainFrame')[0].contentWindow['document'].getElementById('nurPatientInfo');
      if (isHasBed) {
        $(patientInfoWrap).html('');
        var infoCoverBtn = $("#info-cover", $('#nurMainFrame')[0].contentWindow['document']);
        infoCoverBtn.hide();
        infoCoverBtn.unbind('click');
      }

      if ($("#checkAllPatient").attr("checked") == "checked") {
        $("#checkAllPatient").attr('checked', false);
      }
      if (isSingle) {
        $(".patient[value=" + peopleList[0] + "]").attr('checked', false);
        peopleList = [];
      } else {
        removePeopleList(peopleList, $(this).closest('li').index());
      }
    }
    //隐藏字页面打开的combo下拉框
    $(".combo-p", $("#nurMainFrame")[0].contentWindow['document']).hide();
    if (sendRequest != false) {
      $('#nurMainFrame')[0].contentWindow[method](peopleList.toString());
    }

  });
}

/*
 * 得到左侧床位列表
 *
 * @params  peopleList
 * @params  peopleLen
 * @returns String
 */
function getBedInfoListStr(d, l) {
  l = l ? l : d.length;
  var oUlH = $('#nurseLayout').layout('panel', 'west').panel('options').height - $("#menuHeadDiv").height() - 45;
  $('#mainMenu').height(oUlH);
  var str = '<div class="insp-patient-list f_yahei" style=""><ul>';
  for (var i = 0; i < l; i++) {
    str += '<li><input type="checkbox" class="patient" value="' + d[i].patientId + '" id="bed' + d[i].patientId + '" data-bedcode="' + d[i].bedCode + '" data-showbedcode="' + d[i].showBedCode + '"><label for="bed' + d[i].patientId + '"><span class="bed-num">' + d[i].showBedCode + '</span>床<span class="bed-name">' + d[i].patientName + '</span></label></li>'
  }
  str += '</ul></div>';
  return str;
}

/*
 * 减少选中people
 * @params 选中数组
 * @params 被减少的项(number)
 * @returns Array
 */
function removePeopleList(arr, m) {
  arr[m] = '';
  /*var tmp = [];
   for(var i=0;i<arr.length;i++){
   if( arr[i] == n ){

   if( arr.length == 1 ){
   arr = [0];
   }
   else{
   tmp = arr.slice(0,i).concat(arr.slice(i+1));
   }
   }
   }
   return tmp;*/
}
/*
 * 增加选中people
 * @params 选中数组
 * @params 当前选中的数
 * @returns Array
 */
function addToPeopleList(arr, n, m) {
  arr[m] = n;
  /*for(var i=0;i<arr.length;i++){
   if( arr[i] == n ){
   s = 0;
   }
   }
   if(s){
   arr.push(n);
   }*/
  console.log(peopleList);
  //return arr//.sort(_Sort);
}

function _Sort(a, b) {
  return a - b;
}
//排序
function compare(k) {
  return function(a, b) {
    var v1 = a[k];
    var v2 = b[k];
    if (/^[0-9]/.test(v1) && /^[0-9]/.test(v2)) {
      return v1 - v2;
    } else if (!/^[0-9]/.test(v1) && !/^[0-9]/.test(v2)) {
      var v1 = v1.match(/[0-9]+/);
      var v2 = v2.match(/[0-9]+/);
      return v1 - v2;
    } else if (!/^[0-9]/.test(v1)) {
      return 1;
    } else if (!/^[0-9]/.test(v2)) {
      return -1;
    }

  }
}
/*
 * 得到所有病人ID
 * @params []
 * @params list
 * @returns Array
 */
function getAllPatientNum(arr, obj) {
  arr = [];
  obj.each(function() {
    arr.push($(this).attr('value'));
    $(this).attr('checked', 'checked');
  });
  return arr;
}

/*
 * 隐藏左侧菜单
 */
function hideWestMenu() {
  notFullWidth = parseInt($('.layout-panel-center').css('width'));
  leftWidth = parseInt($('.layout-panel-center').css('left'));
  fullWidth = parseInt($('.layout-panel-center').css('left')) + parseInt($('.layout-panel-center').css('width'));
  console.log(leftWidth + ',' + fullWidth);
  $('.layout-panel-west').css({ 'display': 'none' });
  $('.layout-panel-center').css({ 'width': fullWidth + 'px' });
  $('.layout-panel-center .layout-body').css('width', fullWidth);
  ay.setIframeWidth('nurMainFrame', fullWidth);
  $('.layout-panel-center').css('left', 0);
}

/*
 *  护士视角部分页面患者信息容器设置
 *
 */

function setPatientInfoWrap() {
  //初始化病人信息容器宽度
  var nurPatientInfo = $('#nurPatientInfo', $('#nurMainFrame')[0].contentWindow['document']);
  var topToolsBox = $('.top-tools-box', $('#nurMainFrame')[0].contentWindow['document']);
  topToolsBox.children('.fl').width(topToolsBox.width() - topToolsBox.find('.top-title').outerWidth(true) - topToolsBox.find('.btn-box').outerWidth(true) - 30 + 'px');
  topToolsBox.children('.fl').height('40px');
}

//显示病人体温单

function showTempSheetPanel(cb, date, patId) {

  //设置体温单查看窗口宽高
  if (!tempShowState) {
    var H = $(document).height();
    var W = $(document).width();
    $("#showTempSheet").show().dialog({
      modal: true,
      height: H * 0.95,
      width: W * 0.7,
      onClose: function() {
        tempShowState = false;
      }
    });
  }
  var startDate = null;
  if (date) {
    startDate = date;
    $("#selectTempDate").val(date);
  } else {
    startDate = $("#startDate", $('#nurMainFrame')[0].contentWindow['document']).val();
  }
  currentDay = startDate;
  if (patId && "" != patId) {
    $("#patList").show();
    $("#selectTempPatId").val(patId);
    if (!isQueryPatientList) {
      setPatSelect(startDate, patId);
    } else {
      //            $("select option[value='"+value+"']").attr("selected", "selected");  
      $("#patList option[value='" + patId + "']").attr("selected", "selected");
    }

  } else {
    $("#patList").hide();
    $('#nurMainFrame')[0].contentWindow['getPatBedId']();
    patId = parent.peopleList;
  }

  $.post(ay.contextPath + '/nur/bodySign/getBodyTempSheet?id=' + patId + '&date=' + startDate, {}, function(data) {
    //绘制数据
    if (!data || !data.data) {
      $("#showTempSheet").dialog('close');
      return;
    }
    $("#showTempSheet").dialog('open');
    if (!tempShowState) {
      tempShowState = true;
    }
    DP(data, config.data, cb);
  });

}

function prevWeekSheetPdf() {
  var currWeek = $("#currentWeek").val();
  if (currWeek <= 1) {
    $("#prevBtn").attr('disabled', 'disabled');
    return;
  }
  currWeek -= 1;
  $("#currentWeek").val(currWeek);
  var date = $("#startDate", $('#nurMainFrame')[0].contentWindow['document']).val();
  if ("" != $("#selectTempDate").val()) {
    date = $("#selectTempDate").val();
  }

  var patId = $("#selectTempPatId").val();
  if ("" == patId) {
    patId = null;
  }
  var prevWeek = new Date(date);
  prevWeekTime = prevWeek.getTime();
  prevWeekTime -= 86400000 * 7;
  prevWeek.setTime(prevWeekTime);
  prevWeek = prevWeek.format("yyyy-MM-dd");
  if (!patId) {
    $("#startDate", $('#nurMainFrame')[0].contentWindow['document']).val(prevWeek);
    currentDay = prevWeek;
    prevWeek = null;
  }
  //$("#LODOP_OB").css('visibility','hidden');
  showTempSheetPanel(null, prevWeek, patId);
}

function nextWeekSheetPdf() {
  var currWeek = parseInt($("#currentWeek").val());
  if (currWeek == 1) {
    $("#prevBtn").removeAttr('disabled');
  }
  currWeek += 1;
  $("#currentWeek").val(currWeek);
  //日期
  var date = $("#startDate", $('#nurMainFrame')[0].contentWindow['document']).val();
  if ("" != $("#selectTempDate").val()) {
    date = $("#selectTempDate").val();
  }
  //患者
  var patId = $("#selectTempPatId").val();
  if ("" == patId) {
    patId = null;
  }
  var currWeekDate = new Date(date).getTime();
  /*if(currWeekDate+86400000*7 > new Date($('#nurMainFrame')[0].contentWindow['currentDate']).getTime()){
   currWeekDate = new Date($('#nurMainFrame')[0].contentWindow['currentDate']).getTime();
   $("#nextBtn").attr('disabled','disabled');
   }
   else{*/
  currWeekDate += 86400000 * 7;
  $("#currentWeek").val(currWeek);
  var tempCurrWeekDate = new Date();
  tempCurrWeekDate.setTime(currWeekDate);
  currWeekDate = tempCurrWeekDate.format("yyyy-MM-dd");
  if (!patId) {
    $("#startDate", $('#nurMainFrame')[0].contentWindow['document']).val(currWeekDate);
    currentDay = currWeekDate;
    currWeekDate = null;
  }
  showTempSheetPanel(null, currWeekDate, patId);
  //}
}
/**
 *  打印体温单
 */
function printPage() {
  var win = $('#printFrame')[0].contentWindow;
  var html = $("#showPanel").html();
  html = html.replace(/zoom\:\s[0-9]*\.?[0-9]*/, 'zoom:1');
  printHtml(html, win);
}

function printHtml(html, w) {
  w.document.body.innerHTML = html;
  w.print();
}

function setPatSelect(date, patId) {
  var deptId = $("#deptId").val();
  //获取病人列表
  var peopleListUrl = '/nur/patientGlance/queryBedPatientList.do?workUnitType=1&workUnitCode=' + deptId;
  $.post(ay.contextPath + peopleListUrl, {}, function(data) {
    isQueryPatientList = true;
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
        if (peopleList[i].patientId == patId) {
          peopleListStr = '<option selected="selected" value="' + peopleList[i].patientId + '">' + peopleList[i].showBedCode + '&nbsp;&nbsp;床&nbsp;&nbsp;' + peopleList[i].patientName + ' </option>' + peopleListStr;
        } else {
          peopleListStr += '<option value="' + peopleList[i].patientId + '">' + peopleList[i].showBedCode + '&nbsp;&nbsp;床&nbsp;&nbsp;' + peopleList[i].patientName + ' </option>';
        }
      }
    }
    $("#patList").html(peopleListStr);
    $("#patList").bind('change', function() {
      showTempSheetPanel(null, date, $(this).val());
    });
  });
}

/**
 * 动态生成菜单
 * @param config
 */
function setNurseMenu(config) {
  var menus = [];
  if (!config) {
    //设置默认值,病人一览,医嘱,生命体征,临床,条码,更多
    var menusJson = '[{"id":1,"code":"patientManager","name":"病人一览","orderNo":1,"children":[{"id":9,"code":"bedListSubMenu","name":"床位列表","parentCode":"patientManager","url":"/nur/patientGlance/patientGlanceMain.do","orderNo":1},{"id":10,"code":"outDeptSubMenu","name":"出院转科","parentCode":"patientManager","url":"/index/patientMain2.do","orderNo":2}]},{"id":2,"code":"orderManager","name":"医嘱管理","orderNo":2,"children":[{"id":11,"code":"prescriptionAllManager","name":"医嘱查看","parentCode":"orderManager","url":"/index/nurseMain.do?menuId=prescriptionAllManager","orderNo":1,"resourceUrl":"/resources/js/nur/prescriptionAllMenu.js"},{"id":12,"code":"prescriptionUnExeManager","name":"未执行医嘱","parentCode":"orderManager","url":"/index/nurseMain.do?menuId=prescriptionUnExeManager","orderNo":2,"resourceUrl":"/resources/js/nur/prescriptionUnExeMenu.js"}]},{"id":3,"code":"BodySignManager","name":"生命体征","orderNo":3,"children":[{"id":13,"code":"bodyTempSheetManager","name":"体温单批量录入","parentCode":"BodySignManager","url":"/index/nurseMain.do?menuId=bodyTempSheetManager","orderNo":1,"resourceUrl":"/resources/js/nur/bodyTempSheetMenu.js"},{"id":14,"code":"goTempSheetManager","name":"体温单文书","parentCode":"BodySignManager","url":"/index/patientMain.do?id=","orderNo":2,"urlType":"0"}]},{"id":4,"code":"clinicReportManager","name":"临床报告","orderNo":4,"children":[{"id":15,"code":"labTestRecordManager","name":"检验报告","parentCode":"clinicReportManager","url":"/index/nurseMain.do?menuId=labTestRecordManager","orderNo":1,"resourceUrl":"/resources/js/nur/labTestRecordMenu.js"},{"id":16,"code":"inspectionRptMainManager","name":"检查报告","parentCode":"clinicReportManager","url":"/index/nurseMain.do?menuId=inspectionRptMainManager","orderNo":2,"resourceUrl":"/resources/js/nur/inspectionRptMainMenu.js"},{"id":17,"code":"patCrisValueManager","name":"危急值","parentCode":"clinicReportManager","url":"/index/nurseMain.do?menuId=patCrisValueManager","orderNo":3,"resourceUrl":"/resources/js/nur/patCrisValueMenu.js"}]},{"id":6,"code":"barcodeManager","name":"条码打印","orderNo":6,"children":[{"id":18,"code":"wristBarcodeManager","name":"打印腕带","parentCode":"barcodeManager","url":"/index/nurseMain.do?menuId=wristBarcodeManager","orderNo":1,"resourceUrl":"/resources/js/nur/wristBarcodeMenu.js"},{"id":19,"code":"labelBarcodeManager","name":"打印瓶签","parentCode":"barcodeManager","url":"/index/nurseMain.do?menuId=labelBarcodeManager","orderNo":2,"resourceUrl":"/resources/js/nur/labelBarcodeMenu.js"}]},{"id":8,"code":"moreManager","name":"更多","orderNo":8,"children":[{"id":25,"code":"nurseWhiteBoardManager","name":"编辑小白版","parentCode":"moreManager","url":"/nur/nurseWhiteBoard/nurseWhiteBoardMain.do","orderNo":4}]}]';
    menus = $.parseJSON(menusJson);
  } else {
    menus = config.data.menu || [];
  }

  var menuDocument = $("#nav1");
  // 如果已添加菜单，不添加
  if (menuDocument.children().length > 0) {
    return;
  }
  // 首层li
  var menuTags = '';
  var locationHref = window.location.href;
  // 获取URL的域,端口,虚拟目录(http://localhost:8919/mnis)
  var menuUrl = locationHref.substring(0, locationHref
    .indexOf(ay.contextPath) + ay.contextPath.length);
  var defaultHref = 'javascript:void(0);';
  $.each(menus, function(i, item) {
    // 首层li中子节点ul标签,是否checked,首层li中子节点li的href超链接
    var ulTags = '',
      isChecked = '',
      liHref = defaultHref;
    if (item.url && item.url != '') {
      liHref = menuUrl + item.url;
    }
    // 首层li
    menuTags = '<li class="fl menu-' + (i + 1) + isChecked + '" id="menu-' + (i + 1) + '">' + '<a href="' + liHref + '" ><span class="icon"></span><span class="font">' + item.name + '</span></a>' + '</li>';
    menuDocument.append(menuTags);
    var subMenus = item.children;
    if (!subMenus || subMenus.lenght == 0) {
      return true;
    }
    ulTags = '<ul class="clearfix f_yahei nav2">';
    $.each(subMenus, function(j, subItem) {

      var onClickTags = '', // onclick事件
        idTags = '', // id
        subLiHref = defaultHref, // href
        resourceUrl = subItem.resourceUrl, // 资源路径
        firstPatId = ''; //第一个患者id
      var vmName = subItem.code.substring(0, subItem.code
        .indexOf('Manager'));
      //urlType存在,且类型为0
      if (subItem.urlType && subItem.urlType == '0') {
        firstPatId = ay.getLocalData('firstPatientId');
      }
      if (!resourceUrl) {
        subLiHref = menuUrl + subItem.url + firstPatId;
      } else {
        idTags = 'id=' + subItem.code;
        onClickTags = "onclick=\"openNurseSubTab(\'" + vmName + "','" + contextPath + subItem.resourceUrl + "',undefined,{'menuId':'" + subItem.code + "'});\"";
      }

      ulTags += '<li ' + isChecked + '><a href= "' + subLiHref + '" ' + onClickTags + ' class="first" ' + idTags + '>' + subItem.name + '</a></li>';
    });
    ulTags += '</ul>';
    $("#menu-" + (i + 1)).append(ulTags);
  });
}
