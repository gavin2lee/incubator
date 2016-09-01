var config = JSON.parse(sessionStorage.getItem('config'));

(function($) {
  var timer;
  var selection;
  var timeCount;
  var config = JSON.parse(sessionStorage.getItem('config'));
  // debugger;
  timeCount = (config && config.data.system.criticalTaskTime && parseInt(config.data.system.criticalTaskTime)) * 1000 || 60000;

  function pagerFilter(data) {
    if (typeof data.length == 'number' && typeof data.splice == 'function') { // is array
      data = {
        total: data.length,
        rows: data
      }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
      onSelectPage: function(pageNum, pageSize) {
        opts.pageNumber = pageNum;
        opts.pageSize = pageSize;
        pager.pagination('refresh', {
          pageNumber: pageNum,
          pageSize: pageSize
        });
        dg.datagrid('loadData', data);
      }
    });
    if (!data.originalRows) {
      data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
  }

  function checkCrisis() {
    try {
      $.get(ay.contextPath + '/nur/crisisValue/getCriticalValueRecord.do', {
        deptCode: localStorage.getItem('deptCode'),
        startDate: new Date().format('yyyy-MM-dd') + ' 00:00:00',
        isAll: false
      }).done(function(res) {
        var dataTemp;

        if (res.rslt === '0') {
          if (res.data.records.length > 0) {
            dataTemp = res.data.records;
            //if
            $('.crisis-value').show().dialog('open');

            // 画数据
            dataTemp.filter(function(dataItem) {
              var temp = dataItem;

              temp.barcode = dataItem.criticalOperRecord.barcode;
              return temp.deptNo === localStorage.getItem('deptCode');
            });

            createCrisisMainTable(dataTemp);
          } else {
            $('.crisis-value').dialog('close');
          }
        } else {

        }
        // debugger;
      });
    } catch (err) {
      console.log(err);
    }
  }

  function showCrisisDetail(index, row) {
    var $table = $('#crisis-value-detail');
    var data = row.labTestItems;
    selection = $('#crisis-value-table').datagrid('getSelections');

    if (selection.length > 0) {
      window.clearInterval(timer);
    } else {
      timer = setInterval(intervalFunc, 5000);
    }

    data = data.map(function(dataItem) {
      dataItem.value = dataItem.result + dataItem.resultUnit;

      return dataItem;
    });

    $table.datagrid({
      height: 240,
      columns: [
        [{
          field: 'itemName',
          title: '项目名',
          // width: width
        }, {
          field: 'masterId',
          title: '主码',
          // width: width
        }, {
          field: 'normalFlag',
          title: '正常标示',
          styler: function(value, row, index) {
              if (value == 'L') {
                return 'background: blue;color: #fff;';
              } else {
                return 'background: red; color: #fff;'
              }
            }
            // width: width
        }, {
          field: 'ranges',
          title: '范围',
          // width: width
        }, {
          field: 'value',
          title: '结果',
          // width: width
        }]
      ],
      data: data,
      loadFilter: pagerFilter,
      pagination: true,

      pageSize: 10
    });
  }

  function createCrisisMainTable(data) {
    var $table = $('#crisis-value-table');
    var toolbar = [{
      text: '处理',
      iconCls: 'icon-ok',
      handler: function() {
        if (!selection || selection.length === 0) {
          $.messager.alert('提示', '请选择危机项');
          return;
        }
        $('#check-crisis-form')[0].reset();
        $('.handle-dialog').dialog('open');
      }
    }];

    $table.datagrid({
      height: 300,
      columns: [
        [{
          field: 'criticalValue',
          title: '危机值',
          // width: width
        }, {
          field: 'sendTime',
          title: '发送日期',
          // width: width
        }, {
          field: 'sendPeople',
          title: '发送人'
        }, {
          field: 'patId',
          title: '患者编号',
          // width: width
        }, {
          field: 'patientName',
          title: '患者姓名',
          // width: width
        }, {
          field: 'bedCode',
          title: '床号',
          // width: width
        }, {
          field: 'barcode',
          title: '条码号'
        }, {
          field: 'dispose',
          title: '处理医生'
        }]
      ],
      data: data,
      loadFilter: pagerFilter,
      // singleSelect: true,
      pagination: true,
      pageSize: 10,
      toolbar: toolbar,
      onSelect: showCrisisDetail,
      onUnselect: function() {
        $('#crisis-value-detail').datagrid({
          data: []
        });
      }
    });
  }

  function handleCrisis() {
    var isValid = $('#check-crisis-form').form('validate');
    var params = [];
    var operTime = new Date().format('yyyy-MM-dd hh:mm:ss');

    if (!isValid) {
      return;
    }

    params = selection.map(function(selectedItem) {
      return {
        operTime: operTime,
        barcode: selectedItem.barcode,
        receiveName: $('#receive-username').val(),
        doctorName: $('#report-doctor').val(),
        operValue: $('#handle-notice').val(),
        isRepeat: $('#isRepeat')[0].checked
      };
    });
    // debugger;
    // return;

    $.post(ay.contextPath + '/nur/crisisValue/disposeCriticalRecords', {
      criticalOperRecords: JSON.stringify(params),
      loginName: $('#handle-username').val(),
      password: $('#handle-password').val()
    }).done(function(res) {
      if (res.rslt === '0') {
        $.messager.alert('提示', '处理完成！');
        $('.handle-dialog').dialog('close');
        // 刷新列表
        setTimeout(checkCrisis, 2000);
      } else {
        $.messager.alert('提示', res.msg);
      }
    }).fail(function() {
      $.messager.alert('警告', '处理失败，请稍后重试！');
    });
  }

  function intervalFunc() {
    checkCrisis();
  }

  //获取配置文件
  $(function() {
    //菜单隐藏操作
    if (!config || !config.data.menu || config.rslt != '0') {
      $.get(ay.contextPath + '/nur/system/getConfig.do', function(config) {
        //将配置文件缓存至浏览器端
        localStorage.setItem("config", JSON.stringify(config));
        sessionStorage.setItem("config", JSON.stringify(config));
        config = $.parseJSON(sessionStorage.getItem("config"));
        setMenu(config);
      });
    } else {
      setMenu(config)
    }
    // 输入工号时获取接报者姓名
    $('#handle-username').on('keyup change', function () {
      var userCode = $(this).val();

      $.get(ay.contextPath + '/index/getUserInfo', {
        LoginID: userCode
      }).done(function (res) {
        if (res.rslt == 0) {
          // debugger
          $('#receive-username').val(res.data.name)
        } else {
          $('#receive-username').val('')
        }
      })
    })
    // 点击用户名文本框等，则下面的框显示隐藏切换
    $(".user").click(function() {
      $(this).nextAll("div").toggle();
      return false;
    });

    $('#check-crisis-form #handle-username, #check-crisis-form #handle-password').validatebox({
      required: true,
      tipPosition: 'right'
    });

    localStorage.setItem('deptCode', $('#deptId').val());

    window.onbeforeunload = unloadFn;

    function unloadFn() {
      window.isUnsaved = true;
      var confirmText = '您正在关闭这个页面，确认推出系统？';


      if (document.activeElement.tagName !== 'A' && document.activeElement.tagName !== 'SELECT') {
        e.returnValue = confirmText;
        return confirmText;
      }
    }

    $('.crisis-value').dialog({
      title: '危机值列表',
      closable: false,
      width: 800,
      top: 80,
      height: 582,
      modal: true
    });
    $('.handle-dialog .panel-body').show();
    $('.handle-dialog').show().dialog({
      title: '填写处理信息',
      width: 500,
      modal: true,
      buttons: [{
        text: '处理',
        handler: handleCrisis
      }, {
        text: '取消',
        handler: function() {
          $('.handle-dialog').dialog("close");
        }
      }]
    });

    $('.crisis-value').dialog('close');
    $('.handle-dialog').dialog('close');

    intervalFunc();
    timer = setInterval(intervalFunc, timeCount);

    /* $('#goTempSheet').on('click', function (e) {
         var pId = ay.getLocalData('firstPatientId');
         if (pId) {
             window.location.href = ay.contextPath + '/index/patientMain.do?id=' + pId;
         }
     });*/

    // 奇数次点击页眉的用户名，背景和边框显示；偶数次点击页眉的用户名，背景和边框隐藏
    $("#user").toggle(
      function() {
        $(this).parent().addClass("account2");
        $(this).nextAll("div").show();
        $("#newPassword").val('');
        $("#error").html('');
      },
      function() {
        $(this).parent().removeClass("account2");
        $(this).nextAll("div").hide();
      })

    // 点击页面非用户名部分，则下面的框隐藏
    $(".user-tools").click(function() {
      //阻止事件冒泡
      return false;
    });
    $("body").click(function() {
      $("#user").trigger("click");
      //$("p.p, p.p2").hide();  //不知有何用
      $("#user").parent().removeClass("account2");
      $(".user-tools").find('i').removeClass('click');
    });

    $(document).on('click', '.patientBox', function(e) {
      var $this = $(this);
      $this.addClass('on').siblings().removeClass('on');
    });

    // 点击下拉框里的内容，使用户名文本框获得其内容
    $("p.pMenu").find("em").click(function() {
      var text = $(this).text();
      $(this).parent().prevAll("input.textGet:first").val(text);
    })

    // 点击切换科室，出现阴影和弹出框
    $("#changeKs").click(function() {
      $(".user-tools").find('i').removeClass('click');
      $(this).find("i").addClass('click');
      $("#change-ks").show().dialog({
        title: '切换科室',
        // width: 250,
        // height: 150,
        modal: true,
        buttons: [{
          text: '确定',
          handler: confirmChangeKs
        }, {
          text: '取消',
          handler: closeChangeKs
        }]
      });
      /*$("#shadow,.ua-box").hide();
       $("#shadow, #change-ks").show();
       fnGotoCenter("change-ks");*/
    })

    // 点击切换账户，出现阴影和弹出框
    $("#changeAccount").click(function() {
      $(".user-tools").find('i').removeClass('click');
      $(this).find("i").addClass('click');
      $("#prompt1").show();
      $("#prompt1").dialog({
        title: '切换账户',
        width: 300,
        height: 180,
        modal: true,
        buttons: [{
          text: '确定',
          handler: confirmPrompt1
        }, {
          text: '取消',
          handler: closePrompt1
        }]
      });
      /*$("#shadow,.ua-box").hide();
       $("#shadow, #change-user").show();
       fnGotoCenter("change-user");*/
    });
    $("#change-user .btn-save").click(function() {
      console.log(1)
    })

    // 点击退出登录，出现阴影和弹出框
    $("#quit").click(function() {

      $(".user-tools").find('i').removeClass('click');
      $(this).find("i").addClass('click');
      //$("#shadow,.ua-box").hide();

      $("#quitBox").show().dialog({
        title: '退出登录',
        width: 200,
        height: 150,
        modal: true,
        buttons: [{
          text: '确定',
          handler: confirmQuitBox
        }, {
          text: '取消',
          handler: closeQuitBox
        }]
      });
      /*$("#shadow").show();
       $("#logout").show();
       fnGotoCenter("logout");*/
    });

    $("#logout .btn-save").click(function() {
      alert("您已经成功退出");

      $("#shadow,.ua-box").hide();
    })

    // 点击修改密码，出现阴影和弹出框
    $("#modifyPsd").click(function() {
      $(".user-tools").find('i').removeClass('click');
      $(this).find("i").addClass('click');
      $("#changePsw").show().dialog({
        title: '修改密码',
        width: 300,
        height: 230,
        modal: true,
        buttons: [{
          text: '确定',
          handler: confirmChangePsw
        }, {
          text: '取消',
          handler: closeChangePsw
        }]
      });
      /*$("#shadow, #reset-password").show();
       fnGotoCenter("reset-password");*/
    });
    $("#reset-password .btn-save").click(function() {
      if ($("#curpsw").val() == '') {
        $("#curpsw").nextAll('span').addClass('err').text('请输入密码');
        $("#curpsw").focus();
        return false;
      } else {
        $("#curpsw").nextAll('span').removeClass('err').text('');
      }
      if ($("#newpsw").val() == '') {
        $("#newpsw").nextAll('span').addClass('err').text('请输入新密码');
        $("#newpsw").focus();
        return false;
      } else {
        $("#newpsw").nextAll('span').removeClass('err').text('');
        if ($("#cfmpsw").val() == '') {
          $("#cfmpsw").nextAll('span').addClass('err').text('请输入新密码');
          $("#cfmpsw").focus();
          return false;
        }
        if ($("#newpsw").val() != $("#cfmpsw").val()) {
          $("#cfmpsw").nextAll('span').addClass('err').text('两次密码不相同');
          $("#cfmpsw").focus();
          return false;
        }
      }

      $("#shadow,.ua-box").hide();
    })

    $(".patientBox").bind('dblclick', function() {
      var id = $(this).attr('data-patientid');
      if (id) {
        window.location.href = ay.contextPath + '/index/patientMain.do?id=' + id;
      }
    });

    /*// 点击登录或确定或取消按钮，阴影和弹出框隐藏
     $("form.prompt").find("a.bg_btn3, a.bg_btn4").click(function() {
     $("#shadow, form.prompt").hide();
     })

     // 点击保存按钮，密码非空且输入一致时阴影和弹出框隐藏
     $("#prompt3").find("a.bg_btn3").click(function() {
     if($("input.password").val() == "" || $("#password4").val() != $("#password3").val()) {
     $("#shadow, #prompt3").show();
     } else {
     $("#shadow, #prompt3").hide();
     }
     })

     // 两次输入密码不一致时，将确认密码清空
     $("#password4").blur(function() {
     if($(this).val() != $("#password3").val()) {
     $(this).val("");
     }
     })
     */
    // new 点击关闭和取消关闭弹出层
    $("#edit").click(function() {
      /*$("#shadow, #edit-patient-info").show();
       fnGotoCenter("edit-patient-info");*/
      $("#edit-patient-info").dialog('open');

    });
    $(".ua-box").click(function() {
      return false;
    });
    $("#shadow").click(function() {
      return false;
    });
    $(".btn-close").click(function() {
      $("#shadow,.ua-box").hide();
      return false;
    });
  });

  function submitForm() {
    var form = $("#newLoginForm");
    var userId = $("#newUserName").val();
    $("#error").html('');
    if (userId == null || $.trim(userId) == '') {
      //$.messager.alert("用户名不能为空!");
      $("#error").html('用户名不能为空!');
      return;
    }
    /*if (password == null || $.trim(password) == '' ) {
     //$.messager.alert("密码不能为空!");
     $("#error").html('密码不能为空!');
     return;
     }*/
    form.submit();
  }



  var log = console.log.bind(console);
})(jQuery);

/**
 * 动态生成菜单
 * @param config
 */
function setMenu(config) {
  var menus = [];
  if (!config) {
    //设置默认值,病人一览,医嘱,生命体征,临床,条码,更多
    var menusJson = '[{"id":1,"code":"patientManager","name":"病人一览","orderNo":1,"children":[{"id":9,"code":"bedListSubMenu","name":"床位列表","parentCode":"patientManager","url":"/nur/patientGlance/patientGlanceMain.do","orderNo":1},{"id":10,"code":"outDeptSubMenu","name":"出院转科","parentCode":"patientManager","url":"/index/patientMain2.do","orderNo":2}]},{"id":2,"code":"orderManager","name":"医嘱管理","orderNo":2,"children":[{"id":11,"code":"prescriptionAllManager","name":"医嘱查看","parentCode":"orderManager","url":"/index/nurseMain.do?menuId=prescriptionAllManager","orderNo":1,"resourceUrl":"/resources/js/nur/prescriptionAllMenu.js"},{"id":12,"code":"prescriptionUnExeManager","name":"未执行医嘱","parentCode":"orderManager","url":"/index/nurseMain.do?menuId=prescriptionUnExeManager","orderNo":2,"resourceUrl":"/resources/js/nur/prescriptionUnExeMenu.js"}]},{"id":3,"code":"BodySignManager","name":"生命体征","orderNo":3,"children":[{"id":13,"code":"bodyTempSheetManager","name":"体温单批量录入","parentCode":"BodySignManager","url":"/index/nurseMain.do?menuId=bodyTempSheetManager","orderNo":1,"resourceUrl":"/resources/js/nur/bodyTempSheetMenu.js"},{"id":14,"code":"goTempSheetManager","name":"体温单文书","parentCode":"BodySignManager","url":"/index/patientMain.do?id=","orderNo":2,"urlType":"0"}]},{"id":4,"code":"clinicReportManager","name":"临床报告","orderNo":4,"children":[{"id":15,"code":"labTestRecordManager","name":"检验报告","parentCode":"clinicReportManager","url":"/index/nurseMain.do?menuId=labTestRecordManager","orderNo":1,"resourceUrl":"/resources/js/nur/labTestRecordMenu.js"},{"id":16,"code":"inspectionRptMainManager","name":"检查报告","parentCode":"clinicReportManager","url":"/index/nurseMain.do?menuId=inspectionRptMainManager","orderNo":2,"resourceUrl":"/resources/js/nur/inspectionRptMainMenu.js"},{"id":17,"code":"patCrisValueManager","name":"危急值","parentCode":"clinicReportManager","url":"/index/nurseMain.do?menuId=patCrisValueManager","orderNo":3,"resourceUrl":"/resources/js/nur/patCrisValueMenu.js"}]},{"id":6,"code":"barcodeManager","name":"条码打印","orderNo":6,"children":[{"id":18,"code":"wristBarcodeManager","name":"打印腕带","parentCode":"barcodeManager","url":"/index/nurseMain.do?menuId=wristBarcodeManager","orderNo":1,"resourceUrl":"/resources/js/nur/wristBarcodeMenu.js"},{"id":19,"code":"labelBarcodeManager","name":"打印瓶签","parentCode":"barcodeManager","url":"/index/nurseMain.do?menuId=labelBarcodeManager","orderNo":2,"resourceUrl":"/resources/js/nur/labelBarcodeMenu.js"}]},{"id":8,"code":"moreManager","name":"更多","orderNo":8,"children":[{"id":25,"code":"nurseWhiteBoardManager","name":"编辑小白版","parentCode":"moreManager","url":"/nur/nurseWhiteBoard/nurseWhiteBoardMain.do","orderNo":4}]}]';
    menus = $.parseJSON(menusJson);
  } else {
    menus = config.data.menu || [];
  }
  var navbar = $('#navbarRight');
  navbar.html(_.template($('#navTpl').html())({
    datas: menus,
    urlPrefix: window.location.href.substring(0, window.location.href.indexOf(ay.contextPath) + ay.contextPath.length),
    iconMap: {
      'patientManager': 'patient',
      'orderManager': 'doctor',
      'BodySignManager': 'life',
      'clinicReportManager': 'clinical',
      'moreManager': 'more'
    },
    firstPatId: ay.getLocalData('firstPatientId')
  }));

  var menuDocument = $("#nav1");
  //如果已添加菜单，不添加
  if (menuDocument.children().length > 0) {
    return;
  }

  //首层li
  var menuTags = '';
  var locationHref = window.location.href;
  //获取URL的域,端口,虚拟目录
  var menuUrl = locationHref.substring(0, window.location.href.indexOf(ay.contextPath) + ay.contextPath.length);
  var defaultHref = 'javascript:void(0);';
  // debugger;
  $.each(menus, function(i, item) {
    //首层li中子节点ul标签,是否checked,首层li中子节点li的href超链接
    var ulTags = '',
      isChecked = '',
      liHref = defaultHref;
    if (i == 0) {
      isChecked = ' checked';
    }
    if (item.url && item.url != '') {
      liHref = menuUrl + item.url;
    }
    //首层li
    menuTags = '<li class="fl menu-' + (i + 1) + isChecked + '" id="menu-' + (i + 1) + '">' + '<a href="' + liHref + '" ><span class="icon"></span><span class="font">' + item.name + '</span></a>' + '</li>';
    menuDocument.append(menuTags);
    var subMenus = item.children;
    if (!subMenus || subMenus.lenght == 0) {
      return true;
    }
    ulTags = '<ul class="clearfix f_yahei nav2">';
    $.each(subMenus, function(j, subItem) {
      if (i == 0 && j == 0) {
        isChecked = 'class="checked"';
      } else {
        isChecked = '';
      }

      var onClickTags = '',
        firstPatId = '';
      if (subItem.urlType && subItem.urlType == '0') {
        firstPatId = ay.getLocalData('firstPatientId');
      }
      ulTags += '<li ' + isChecked + '><a href= "' + menuUrl + subItem.url + firstPatId + '" ' + onClickTags + ' class="first">' + subItem.name + '</a></li>';
    });
    ulTags += '</ul>';
    $("#menu-" + (i + 1)).append(ulTags);
  });
}
//弹出层的操作方法
function closeQuitBox() {
  $('#quitBox').dialog('close');
}

function confirmQuitBox() {
  var str = window.location.toString();
  str = str.substring(0, str.indexOf("mnis")) + "mnis/index/login.do";
  window.location.href = str;
}

function closeChangeKs() {
  $('#change-ks').dialog('close');
}

function confirmChangeKs() {
  //alert();
  try {
    $.post(ay.contextPath + '/index/doLogin.do', {
      userName: sessionStorage.getItem('userName'),
      password: sessionStorage.getItem('pwd'),
      deptCode: $('#deptListWrapper option:selected').val(),
      isDefault: $("#isDefaultDept")[0].checked
    }).done(function(response) {
      console.log(response);
      window.location.href = ay.contextPath + '/nur/patientGlance/patientGlanceMain';
    });
  } catch (err) {
    console.log(err);
  }

}

function closePrompt1() {
  $('#prompt1').dialog('close');
}

function confirmPrompt1() {
  submitForm();
}

function closeChangePsw() {
  $('#changePsw').dialog('close');
}

// fnDivcenter 弹出层水平垂直居中屏幕功能
function fnDivcenter(id) {
  var _height = $(window).height();
  var _width = $(window).width();
  var div_height = document.getElementById(id).scrollHeight;
  var div_width = document.getElementById(id).scrollWidth;
  var _top = (_height - div_height) / 2 + $(document).scrollTop();
  var _left = (_width - div_width) / 2 + document.body.scrollLeft;
  $('#' + id).css('left', _left);
  $('#' + id).css('top', _top);
  $('.pop_mask,.iframe_box').height($(document).height());
}

//切换Tab页
function cTab(liID, infoID, cName, number, index) {
  for (var i = 1; i <= number; i++) {
    document.getElementById(liID + i).className = ""
    document.getElementById(infoID + i).style.display = "none"
  }
  document.getElementById(liID + index).className = cName
  document.getElementById(infoID + index).style.display = "block"
}

// 弹出层垂直功能
function fnGotoCenter(id) {
  var oNode = $("#" + id)
  var nHeight = oNode.height();
  var nWidth = oNode.width();
  console.log(oNode);
  console.log(nHeight)
  oNode.css({
    "marginLeft": -(nWidth / 2) + "px",
    "marginTop": -(nHeight / 2) + "px"
  });
}

function confirmChangePsw() {

}
