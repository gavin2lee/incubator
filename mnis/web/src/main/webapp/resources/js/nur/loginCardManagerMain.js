/**
 * Created by gary on 16/5/17.
 */
var code = {
  fields: [],
  childrenTypes: [],
  data: [],
  deptCode: localStorage.getItem('deptCode'),
  deptName: $('#deptName').text(),
  nurseCode: localStorage.getItem('nurseCode'),
  nurseName: localStorage.getItem('nurseName'),

  init: function() {
    var that = this;
    var $setTime = $('#set-time');
    var $startDate = $('#startDate');
    var $endDate = $('#endDate');

    $startDate.datetimebox('setValue', new Date().format('yyyy-MM-dd hh:mm:ss'));
    $endDate.datetimebox('setValue', new Date(new Date().getTime() + 30 * 24 * 1000 * 3600).format('yyyy-MM-dd hh:mm:ss'));

    that.getLoginCardManagers();
    that.getLoginCodeInfo();

    $('[name="nurseName"]').on('keyup', function(e) {
      var $this = $(this);
      var nurseName = $this.val();
      var result = code.filterData(nurseName);
      if (nurseName === '') {
        code.drawTable(code.reviewData);
      } else {
        code.drawTable(result);
      }
    });

    // 选择时间
    $setTime.show().dialog({
      title: '设置登录卡有效期',
      width: 500,
      modal: true,
      closed: true,
      buttons: [{
        text: '生成',
        handler: code.createCode
      }, {
        text: '取消',
        handler: function() {
          $setTime.dialog("close");
        }
      }]
    });
  },
  drawTable: function(data) {
    var review = $('#login-review');
    var width = 1240 / 8 - 3;

    review.datagrid({
      remoteSort: false,
      columns: [
        [{
          field: 'deptName',
          title: '科室',
          width: width
        }, {
          field: 'nurseCode',
          title: '工号',
          width: width
        }, {
          field: 'nurseName',
          title: '姓名',
          width: width
        }, {
          field: 'startDate',
          title: '开始时间',
          width: width
        }, {
          field: 'endDate',
          title: '结束时间',
          width: width
        }, {
          field: 'operaDate',
          title: '操作时间',
          width: width,
          sortable: true,
          sorter: function(a, b) {
            var _a = new Date(a).getTime();
            var _b = new Date(b).getTime();
            if (_a > _b) {
              return 1;
            } else if (_a === _b) {
              return 0;
            } else {
              return -1;
            }
          }
        }, {
          field: 'operaNurseName',
          title: '操作护士',
          width: width
        }, {
          field: 'status',
          title: '状态',
          width: width,
          styler: function(value, row, index) {
            if (value == '正常') {
              return 'color:#69CA00;';
              // the function can return predefined css class and inline style
              // return {class:'c1',style:'color:red'}
            } else if (value == '失效') {
              return 'color:#999;'
            } else {
              return 'color: red';
            }
          }
        }]
      ],
      data: data,
      loadFilter: pagerFilter,
      singleSelect: false,
      multiSelect: false,
      pagination: true,
      pageSize: 20
    });
  },

  getLoginCodeInfo: function() {
    var that = code;
    var nurseCode = $('#nurseId').val();
    var deptCode = $('#deptId').val();
    var param = {
      deptCode: deptCode,
      //nurseCode: nurseCode,
      nurseName: ''
    };

    return $.get(ay.contextPath + '/nur/loginCard/getLoginCardInfos.do', param).done(function(res) {
      if (res.rslt === '0') {
        that.reviewData = res.data.loginCardInfos.map(function(item) {
          var temp = item;
          switch (temp.status) {
            case 0:
              temp.status = '正常';
              break;
            case 1:
              temp.status = '作废';
              break;
            case 2:
              temp.status = '失效';
              break;
            default:
              break;
          }

          return temp;
        });

        that.drawTable(that.reviewData);
      } else {
        $.messager.alert(res.msg);
        return null;
      }

    }).fail(function(err) {

    });
  },
  invalidLoginCard: function() {
    var data = [];
    var selection = code.seletion;

    if (selection.length > 0) {
      selection.forEach(function(selectedItem) {
        if (selectedItem.loginCardCode) {
          data.push({
            "loginCardCode": selectedItem.loginCardCode,
            "operaNurseCode": code.nurseCode,
            "operaNurseName": code.nurseName,
            "operaDate": new Date().format('yyyy-MM-dd hh:mm:ss')
          });
        }
      });
      // debugger;
      $.post(ay.contextPath + '/nur/loginCard/invalidLoginCardInfos', {
        loginCardInfos: JSON.stringify(data)
      }).done(function(res) {
        if (res.rslt === '0') {
          code.getLoginCardManagers();
          code.getLoginCodeInfo();
          $.messager.show({
            title: '提示',
            msg: res.msg,
            timeout: 0,
            showType: 'fade',
            style: {
              right: '',
              top: document.body.scrollTop + document.documentElement.scrollTop + 100,
              bottom: ''
            }
          });
        } else {
          $.messager.alert('info', res.msg);
        }
      }).fail(function(err) {

      });
    }


  },
  createCode: function() {
    var data;
    var selection = code.seletion;
    var startDate = $('#startDate').datetimebox('getValue');
    var endDate = $('#endDate').datetimebox('getValue');

    if (selection.length > 0) {
      data = selection.map(function(selectedItem) {
        return {
          "loginCardCode": code.guid(),
          "deptCode": selectedItem.deptCode,
          "deptName": selectedItem.deptName,
          "operaNurseCode": code.nurseCode,
          "operaNurseName": code.nurseName,
          "nurseCode": selectedItem.nurseCode,
          "nurseName": selectedItem.nurseName,
          "operaDate": new Date().format('yyyy-MM-dd hh:mm:ss'),
          "startDate": startDate,
          "endDate": endDate
        };
      });
      code.printCode(data);

      // debugger;
      // return;
      $.post(ay.contextPath + '/nur/loginCard/saveLoginCardInfo', {
        loginCardInfos: JSON.stringify(data)
      }).done(function(res) {
        if (res.rslt == '0') {
          code.getLoginCardManagers();
          code.getLoginCodeInfo();
          $('#set-time').dialog('close');
          $.messager.show({
            title: '提示',
            msg: res.msg,
            timeout: 1200,
            showType: 'fade',
            style: {
              right: '',
              top: document.body.scrollTop + document.documentElement.scrollTop + 100,
              bottom: ''
            }
          });
        } else {
          $.messager.alert('info', res.msg);
        }

      }).fail(function(err) {

      });
    }

  },
  getLoginCardManagers: function(isRefresh) {
    var that = code;
    var nurseCode = $('#nurseId').val();
    var deptCode = $('#deptId').val();
    var param = {
      deptCode: deptCode,
      //nurseCode: nurseCode,
      nurseName: ''
    };

    return $.get(ay.contextPath + '/nur/loginCard/getLoginCardManagers.do', param).done(function(res) {
      if (res.rslt === '0') {

        that.data = that.transformData(res.data.loginCardManagers);
        that.createTable();
        //return that.data;
      } else {
        $.messager.alert(res.msg);
        return null;
      }

    }).fail(function(err) {

    });
  },
  createTable: function() {
    var that = this;
    var $setTime = $('#set-time');
    var table = $('#login-table');
    var review = $('#login-review');
    var data = code.data;
    var width = 1220 / 5;
    var toolbar = [{
      text: '作废',
      iconCls: 'icon-remove',
      handler: function() {
        that.seletion = $('#login-table').datagrid('getSelections');
        if (that.seletion.length === 0) {
          $.messager.alert('提示', '请选择护士');
          return;
        }
        that.invalidLoginCard();
      }
    }, '-', {
      text: '生成二维码',
      iconCls: 'icon-print',
      handler: function() {
        that.seletion = $('#login-table').datagrid('getSelections');
        if (that.seletion.length === 0) {
          $.messager.alert('提示', '请选择护士');
          return;
        }
        $setTime.dialog('open');
      }
    }];

    table.datagrid({
      remoteSort: false,
      columns: [
        [{
          field: 'isPrint',
          title: '是否打印',
          width: width,
          styler: function(value, row, index) {
            if (value == '是') {
              return 'color:#69CA00;';
              // the function can return predefined css class and inline style
              // return {class:'c1',style:'color:red'}
            } else {
              return 'color:#f60;'
            }
          }
        }, {
          field: 'deptName',
          title: '科室',
          width: width
        }, {
          field: 'nurseCode',
          title: '工号',
          width: width
        }, {
          field: 'nurseName',
          title: '姓名',
          width: width
        }, {
          field: 'operaDate',
          title: '打印时间',
          width: width,
          sortable: true,
          sorter: function(a, b) {
            var _a = new Date(a).getTime();
            var _b = new Date(b).getTime();
            // debugger;
            if (_a > _b) {
              return 1;
            } else if (_a === _b) {
              return 0;
            } else {
              return -1;
            }
          }
        }]
      ],
      data: data,
      singleSelect: false,
      sortName: 'operaDate',
      toolbar: toolbar,
      pagination: true,
      pageSize: 20,
      loadFilter: pagerFilter
    });
  },
  transformData: function(data) {
    var temp = [];

    data.forEach(function(item) {
      var tempObj = {
        nurseCode: item.nurseCode,
        nurseName: item.nurseName,
        deptName: item.deptName,
        deptCode: item.deptCode,
        isPrint: item.isPrint ? '是' : '否',
        operaDate: item.operaDate
      };

      if (item.loginCardCode) {
        tempObj.loginCardCode = item.loginCardCode;
      }

      temp.push(tempObj);
    });

    return temp;
  },
  guid: function() {
    function s4() {
      return Math.floor((1 + Math.random()) * 0x10000)
        .toString(16)
        .substring(1);
    }

    return 'U' + s4() + s4() + s4() + s4() +
      s4() + s4() + s4() + s4();
  },
  printCode: function(data) {
    var printWindow = $('#printFrame')[0].contentWindow,
      printBody = $(printWindow.document.body);
    var loginCodeSize;
    printBody.html('');
    printWindow.document.head.innerHTML =
      '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/normalize.css">' +
      '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/public.css">' +
      '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/printCode.css">';

    try {
      loginCodeSize = JSON.parse(localStorage.getItem('config')).data.system.loginCardSize.split(',');
    } catch (err) {
      loginCodeSize = [200, 160];
    }
    printBody.append('<div id="print-temp"></div>');
    var $print = printBody.find('#print-temp');

    data.forEach(function(dataItem, i) {
      var $codeItem = $([
        '<div class="code-item-wrapper code-' + i + ' ' + (function() {
          if ((i + 1) % 4 === 0) {
            return 'page-break';
          }
          return '';
        })() + '" style="width: ' + loginCodeSize[0] + 'px; height: ' + loginCodeSize[1] + 'px;">',
        '<div class="code-content fl"></div>',
        '<div class="nurse-info fl">',
        '<p>科室：',
        dataItem.deptName,
        '</p>',
        '<p>工号：',
        dataItem.nurseCode,
        '</p>',
        '<p>姓名：',
        dataItem.nurseName,
        '</p>',
        '</div>',
        '</div>'
      ].join(''));
      $print.append($codeItem);
      printBody.find('.code-' + i + ' .code-content').qrcode({
        render: 'image',
        width: 57,
        height: 57,
        text: dataItem.loginCardCode
      });
    });

    setTimeout(function() {
      printWindow.print();
    }, 100);
  },
  filterData: function(value) {
    var data = code.reviewData;

    var temp = $.grep(data, function(item) {
      return item.nurseName == value;
    });

    return temp;
  }
};

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

$(function() {
  code.init();
  $('.login-content').height($(window).height() - 140);
});
