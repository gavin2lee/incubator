var relationReport = [],
  compareData = {},
  _columns = [],
  checkArr = [],
  _data, R = {},
  isFit = true;

/*
 * 数据表格加载完毕后触发 初始化tooltip
 */
function loadComplete() {
  var relationTip = $(".relationTip");

  if (!relationTip || relationTip.length == 0) {
    return;
  }

  // 销毁被创建的tip panel释放内存 并解决对比数据乱入无法操作的情况
  $(".tooltip-left").each(function() {
    var that = $(this);
    that.find('.tooltipPanel').each(function() {
      $(this).panel('destroy');
    });
    that.tooltip('destroy');
  });
  relationTip
    .each(function(index) {
      var that = $(this);
      that
        .tooltip({
          position: 'left',
          content: $('<div class="tooltipPanel"></div>'), // 赋予一个class便于查找
          onDestroy: function() {
            // 删除后操作
          },
          onUpdate: function(content) {
            var i = index;
            var d = $("#info-tab").datagrid('getRows');
            var row = d[i];
            var relationArr = getRelateReport(row.hospNo,
              row.subject);
            var relationStr = ''; // 关联的报告字符串 返回给content
            var len = relationArr[0].length;
            if (len <= 1) {
              relationStr = '<center>无对比数据</center>';
            } else {
              relationStr += '<div id="relationBox' + i + '"><ul>';
              for (var idx = 0; idx < len; idx++) {
                if (row.masterId == relationArr[0][idx][0]) {
                  relationStr += '<li class="relationItem"><div class="chkbox-wrapper"><input class="chkbox" type="checkbox" value="' + relationArr[0][idx][0] + '" id="' + relationArr[0][idx][0] + i + '" checked="checked" disabled="disabled"><label for="' + relationArr[0][idx][0] + i + '"></label></div><label for="' + relationArr[0][idx][0] + i + '"><span style="display:inline-block;">' + relationArr[1][idx][0] + '</span></label></li>'
                } else {
                  relationStr += '<li class="relationItem"><div class="chkbox-wrapper"><input class="chkbox" type="checkbox" value="' + relationArr[0][idx][0] + '" id="' + relationArr[0][idx][0] + i + '" ><label for="' + relationArr[0][idx][0] + i + '"></label></div><label for="' + relationArr[0][idx][0] + i + '"><span style="display:inline-block;">' + relationArr[1][idx][0] + '</span></label></li>';
                }
              }
              relationStr += '</ul></div>';
              relationStr += '<div style="text-align:right;"><a class="_btn" href="#"  style="width:50px;" onclick="compare(' + i + ')">对比</a></div>'
            }
            content.panel({
              width: 160,
              border: false,
              content: relationStr
            });

          },
          onShow: function() {
            var t = $(this);
            // TODO tip框显示位置的不合理

            // t.tooltip('hide');
            // var h = $(".content").height();
            // var pY = t.offset().top;
            /*
             * if( pY > h/2){ $(this).tooltip({
             * position:'top' });
             *  } else{ $(this).tooltip({ position:'bottom'
             * }); } t.tooltip('show');
             */
            /*
             * t.tooltip('tip').css({
             * backgroundColor:'#e0e4e8' });
             */
            t.tooltip('tip').unbind().bind('mouseenter',
              function() {
                t.tooltip('show');
              }).bind('mouseleave', function() {
              t.tooltip('hide');
            });
          }
        });
    });
}

function action() {
  return '<div class="relationTip" style="width:50px;text-align:center;border:1px solid #95B8E7;padding:0 2px;">关联对比</div>'
}
/*
 * function op(value,row,index){
 * 
 * var tmpArr = getRelateReport(row.hospNo,row.subject); //console.log(tmpArr);
 * var sHtml = ''; if( tmpArr.length == 1 ){ R[index] = '<li><input
 * class="relateCheckItems" type="checkbox" id="'+ tmpArr[0][i] +'"><label
 * for="'+tmpArr[0][i]+'"><span style="display:inline-block;"> '+ tmpArr[1][i] + '</span></label></li><li><span>无对比数据</span></li>';
 * //$("#related-report-box ul").html('<li><span>无对比数据</span></li>'); }
 * else{ var relateList = ''; for(var i=0;i<tmpArr[0].length;i++){
 * //console.log(tmpArr[0][i] +','+ row.masterId) if(tmpArr[0][i] ==
 * row.masterId){ relateList +='<li><input class="relateCheckItems"
 * type="checkbox" id="'+ tmpArr[0][i] +'" disabled="disabled"
 * checked="checked"><label for="'+tmpArr[0][i]+'"><span
 * style="display:inline-block;"> '+ tmpArr[1][i] + '</span></label></li>'; }
 * else{ relateList +='<li><input class="relateCheckItems" type="checkbox"
 * id="'+ tmpArr[0][i] +'"><label for="'+tmpArr[0][i]+'"><span
 * style="display:inline-block;"> '+ tmpArr[1][i] + '</span></label></li>'; } }
 * R[index] = relateList; //$("#related-report-box ul").html(relateList); } var
 * btnStr = '<div><input type="button" value="关联对比" class="related-report"
 * data-value="'+ index +'" style="float:left;height:20px;margin-right:5px;"/> ';
 * return btnStr +"</div>"; }
 */

function sta(value, index, row) {
  var str = '';
  switch (value) {
    case 'W':
      str = '待执行';
      break;
    case 'E':
      str = '已执行未报告';
      break;
    default:
      str = '已报告';
  }
  return str;
}

function initMethod() {

  /*
   * $.plot($("#flot-placeholder"), dataset,options);
   * $('.related-report').click(function(){ // 通过ID来搞定
   * 
   * });
   */
}

function compare(idx) {
  // 初始化
  var sendName = '无',
    sendDate = '无',
    testName = '无',
    testDate = '无',
    reportName = '无',
    reportDaTe = '无';

  // $("#related-report-box").hide();
  checkArr = [], compareData = {};
  var rl = $("#relationBox" + idx + " ul li");
  rl.each(function() {
    var node = $(this).find('input[type="checkbox"]');
    if (node.attr('checked') == 'checked') {
      checkArr.push(node.val());
    }
  })

  // datagrid参数
  _columns = [
    [
      /* {field:'cc',checkbox:true,rowspan:2}, */
      {
        field: 'itemName',
        title: '检验项目',
        align: 'center',
        width: 160,
        rowspan: 2
      }
    ],
    [

    ]
  ];
  var tmpArr = [],
    tmpObj = {},
    tmpIndex = 0;
  // 是否自适应
  isFit = (checkArr.length > 3) ? false : true;
  // 开始循环
  for (var i = 0; i < checkArr.length; i++) {

    for (var n = 0; n < _data.length; n++) {
      if (_data[n].masterId == checkArr[i]) {
        if (!compareData.specimen) {
          compareData.specimen = _data[n].specimen;
          compareData.bedCode = _data[n].bedCode;
          compareData.patientName = _data[n].patientName;
          compareData.hospNo = _data[n].hospNo;
          compareData.subject = _data[n].subject;
          compareData.specimen = _data[n].specimen;
          compareData.masterId = [];
          compareData.reportTime = [];
          compareData.itemList = [];
        }
        compareData.masterId.push(_data[n].masterId);
        compareData.reportTime.push(_data[n].reportTime);
        var reg = /^(\-)?[0-9]*[\.]?[0-9]*$/;
        for (var m = 0; m < _data[n].itemList.length; m++) {
          // 组装数据
          if (typeof tmpObj[_data[n].itemList[m].itemName] != 'number') {
            tmpIndex = (typeof getMaxVal(tmpObj) == 'number') ? getMaxVal(tmpObj) + 1 : 0;
            // console.log(tmpIndex)
            compareData.itemList.push({});
            for (var kdx = 0; kdx < checkArr.length; kdx++) {
              compareData.itemList[tmpIndex]['result' + kdx] = '';
            }
            compareData.itemList[tmpIndex]['itemName'] = _data[n].itemList[m].itemName;
            compareData.itemList[tmpIndex]['resultUnit'] = _data[n].itemList[m].resultUnit;
            compareData.itemList[tmpIndex]['result' + i] = _data[n].itemList[m].result;
            compareData.itemList[tmpIndex]['ranges'] = _data[n].itemList[m].ranges;
            tmpObj[_data[n].itemList[m].itemName] = tmpIndex;
          } else {
            compareData.itemList[tmpObj[_data[n].itemList[m].itemName]]['result' + i] = _data[n].itemList[m].result;
            compareData.itemList[tmpObj[_data[n].itemList[m].itemName]]['resultUnit' + i] = _data[n].itemList[m].resultUnit;
            compareData.itemList[tmpObj[_data[n].itemList[m].itemName]]['ranges' + i] = _data[n].itemList[m].ranges;
          }
        }
        if (_data[n].orderDoctorName) {
          sendName = _data[n].orderDoctorName;
        }

        if (_data[n].requestDate) {
          sendDate = _data[n].requestDate.substring(5);
        }

        if (_data[n].testName) {
          testName = _data[n].testName;
        }


        if (_data[n].testDate) {
          testDate = _data[n].testDate.substring(5);
        }

        if (_data[n].reporterName) {
          reportName = _data[n].reporterName;
        }

        if (_data[n].reportTime) {
          reportDate = _data[n].reportTime.substring(5);
        }

        tmpArr[i] = '<p>送验者：<span style="display: inline-block;width: 40px;">' + sendName + '</span> 时间：' + sendDate + '</p>' + '<p>检验者：<span style="display: inline-block;width: 40px;">' + testName + '</span> 时间：' + testDate + '</p><p>审核者：<span style="display: inline-block;width: 40px;">' + reportName + '</span> 时间：' + reportDate + '</p>';
      }
    }

    // datagrid参数
    _columns[0].push({
      title: tmpArr[i],
      width: 230,
      align: 'center'
    })
    _columns[1].push({
      field: 'result' + i,
      title: '结果',
      width: 230,
      align: 'center',
      formatter: compareFormat
    });
  }
  _columns[0].push({
    field: 'resultUnit',
    title: '单位',
    width: 80,
    align: 'center',
    rowspan: 2
  }, {
    field: 'ranges',
    title: '参考值',
    width: 80,
    align: 'center',
    rowspan: 2,
    formatter: function(value, row) {
      if (!value) {
        return '';
      }
      value = value.replace('~~', '—');
      return value;
    }
  });
  console.log(compareData);
  // 数据组装完成
  $("#bedNumber").html(compareData.bedCode + '床');
  $("#pName").html(compareData.patientName);
  $("#hospital").html(compareData.hospNo);
  $("#testItem").html(compareData.subject);
  $("#sm").html(compareData.specimen);
  $("#related-report-compare").dialog('open');
  $("#compare-tab")
    .datagrid({
      fit: true,
      singleSelect: true,
      collapsible: true,
      striped: true,
      nowrap: false,
      data: compareData.itemList,
      columns: _columns,
      /*
       * onClickRow:function(){ showGraph(); },
       */
      onBeforeLoad: function(data) {
        // 获取头部列属性 修改宽度换行 根据field来选择
        var dhr1 = $('#related-report-compare .datagrid-htable .datagrid-header-row:eq(0)');
        dhr1.find("td[rowspan!=2] div p").css('margin-top',
          '0px');
        dhr1.find("td[rowspan!=2] div").css({
          'height': '100%',
          'white-space': 'normal'
        });
      }
    });

}

function compareFormat(value, row) {
  if (!value) {
    return '';
  }
  var field = $(this)[0].field;
  if (field.indexOf('.') >= 0) {
    var fieldArr = field.split('.');
    var ranges = row[fieldArr[0]].ranges;
    if (!ranges) {
      return value;
    }
  } else if (!row.ranges) {
    var index = field.indexOf('0') > 0 ? 0 : 1;
    if (!row['ranges' + index]) {
      return value;
    }
    var ranges = row['ranges' + index];
  } else {
    var ranges = row.ranges;
  }

  var reg = /^(\-)?[0-9]*[\.]?[0-9]*$/;
  if (typeof parseFloat(value) == 'number' && !parseFloat(value)) {
    return value;
  }
  value = parseFloat(value);
  if (ranges.indexOf('~~') >= 0) {
    var rangeArr = ranges.split('~~');
    if (rangeArr.length == 2) {
      if (reg.test(rangeArr[0]) && reg.test(rangeArr[1])) {
        if (value > parseFloat(rangeArr[1])) {
          return '<font style="color:red">' + value + '↑</font>'
        }
        if (value < parseFloat(rangeArr[0])) {
          return '<font style="color:red">' + value + '↓</font>'
        }
      }
    }
  } else if (ranges.indexOf('>') >= 0) {
    var rangeArr = ranges.split('>');
    if (reg.test(rangeArr[1])) {
      if (value < parseFloat(rangeArr[1])) {
        return '<font style="color:red">' + value + '↓</font>';
      }
    } else {
      // return '<font style="color:red">'+value+'</font>';
    }
  } else if (ranges.indexOf('<') >= 0) {
    var rangeArr = ranges.split('<');
    if (reg.test(rangeArr[1])) {
      if (value > parseFloat(rangeArr[1])) {
        return '<font style="color:red">' + value + '↑</font>';
      }
    } else {

    }
  } else {

  }
  return value;
}

function checkItems(val, row) {
  return val
}

function follow1(val, row) {
  var sHtml = '<span class="starbox"><i class="fl star"></i>' + val + '</span>'
  return sHtml;
}
/*
 * 获取当前检验单在关联报告中的索引 -1 为不存在 其他值为已存在索引
 */
function isHas(hospNo, checkItem) {
  var index = -1;
  for (var i = 0; i < relationReport.length; i++) {
    if (hospNo == relationReport[i].hospNo && checkItem == relationReport[i].checkItem) {
      index = i;
      return index;
    }
  }
  return index;
}

/*
 * 获取关联报告列表 hospno 住院号 checkitem 项目名字 t 传入的时间
 */
function getRelateReport(hospNo, checkItem, t) {
  // 存储列表的对象
  var rsltObj = {};
  var arr = [
    [],
    []
  ]

  for (var i = 0; i < relationReport.length; i++) {
    if (hospNo == relationReport[i].hospNo && checkItem == relationReport[i].subject) {
      arr[0].push(relationReport[i].list);
      arr[1].push(relationReport[i].reportTime);
    }
  }
  return arr;
}

/*
 * 搜索
 */
function reloadData() {
  var startDate = $("#startDate").val();
  var endDate = $("#endDate").val();
  var status = $("#status").val();
  var pl = parent.peopleList;
  // console.log(pl)
  if (_patientId) {
    pl = [_patientId];
  }
  // console.log(pl)
  // console.log(startDate +','+endDate+','+parent.peopleList);
  if (startDate) {
    startDate = '&startDate=' + startDate;
  }
  if (endDate) {
    endDate = '&endDate=' + endDate;
  }
  if (status) {
    status = '&status=' + status;
  }
  if (!pl || pl.length == 0) {
    $('#info-tab').datagrid('loadData', {
      total: 0,
      rows: []
    });
    return;
  }
  $
    .post(
      ay.contextPath + '/nur/labTestRecord/getLabTestRdsByCond.do?patientIds=' + pl + startDate + endDate, {},
      function(data) {

        _data = data.data ? data.data.list : [];

        relationReport = [];
        for (var i = 0; i < _data.length; i++) {
          if (i == 0) {
            relationReport.push({
              "hospNo": _data[i].hospNo,
              "subject": _data[i].subject,
              "reportTime": [_data[i].reportTime],
              "list": [_data[i].masterId]
            });
          } else {

            var num = isHas(_data[i].hospNo,
              _data[i].subject);
            if (num >= 0) {
              relationReport[num].list
                .push(_data[i].masterId);
              relationReport[num].reportTime
                .push(_data[i].reportTime);
            } else {
              relationReport.push({
                "hospNo": _data[i].hospNo,
                "subject": _data[i].subject,
                "reportTime": [_data[i].reportTime],
                "list": [_data[i].masterId]
              });
            }
          }
        }
        // console.log(relationReport);
        $("#info-tab")
          .datagrid({
            fit: true,
            fitColumns: true,
            singleSelect: true,
            remoteSort: false,
            sortOrder: 'desc',
            sortName: 'reportTime',
            data: _data,
            onCheck: function(index, reportData) {
              var sendName = '无',
                sendDate = '无',
                testName = '无',
                testDate = '无',
                reportTime = '无',
                reporterName = '无';
              if (reportData.orderDoctorName) {
                sendName = reportData.orderDoctorName;
              }

              if (reportData.requestDate) {
                sendDate = reportData.requestDate;
              }

              if (reportData.testName) {
                testName = reportData.testName;
              }


              if (reportData.testDate) {
                testDate = reportData.testDate.substring(5);
              }

              if (reportData.reportTime) {
                reportTime = reportData.reportTime.substring(5);
              }

              if (reportData.reporterName) {
                reporterName = reportData.reporterName;
              }


              var reportInfo = '<p style="line-height:35px;"><span style="margin-right:15px;">送验者：<span style="display: inline-block;min-width: 30px;">' + sendName + '</span> 时间：' + sendDate + '</span>' + '<span style="margin-right:15px;">检验者：<span style="display: inline-block;min-width: 30px;">' + testName + '</span> 时间：' + testDate + '</span><span>审核者：<span style="display: inline-block;min-width: 30px;">' + reporterName + '</span> 时间：' + reportTime + '</span></p>';
              // var reportInfo =
              // '报告人：'+data1.reporterName+"
              // 报告时间："+data1.reportTime;
              // 信息格式序列化
              var itemList = [];
              for (var i = 0; i < reportData.itemList.length; i++) {

                if (i % 2 == 0) {
                  itemList.push({});
                  itemList[itemList.length - 1].left = reportData.itemList[i];
                }
                if (i % 2 == 1) {
                  itemList[itemList.length - 1].right = reportData.itemList[i];
                }

              }
              // console.log(itemList);
              // 信息格式化结束

              $("#report-info-box").dialog({
                title: '报告详情'
              });
              $("#patientBedCode").html(
                reportData.bedCode + '床');
              $("#patName").html(
                reportData.patientName);
              $("#hospNo").html(reportData.hospNo);
              $("#checkItem").html(
                reportData.subject);
              $("#checkItem").attr('title',
                reportData.subject);
              $("#specimen")
                .html(
                  '样本类型：' + reportData.specimen);
              // $("#report-info-box").show();
              $("#report-info-box").dialog(
                'open');
              $("#report-info")
                .datagrid({
                  fit: true,
                  singleSelect: true,
                  data: itemList,
                  nowrap: false,
                  columns: [
                    [{
                      title: reportInfo,
                      colspan: 8
                    }],
                    [{
                      field: 'left.itemName',
                      title: '检验项目',
                      width: 205,
                      align: 'center'
                    }, {
                      field: 'left.result',
                      title: '结果',
                      width: 80,
                      align: 'center',
                      formatter: compareFormat
                    }, {
                      field: 'left.resultUnit',
                      title: '单位',
                      width: 60,
                      align: 'center'
                    }, {
                      field: 'left.ranges',
                      title: '参考值',
                      width: 70,
                      align: 'center',
                      formatter: function(
                        value,
                        row) {
                        if (!value) {
                          return '';
                        }
                        value = value
                          .replace(
                            '~~',
                            '—');
                        return value;
                      }
                    }, {
                      field: 'right.itemName',
                      title: '检验项目',
                      width: 205,
                      align: 'center'
                    }, {
                      field: 'right.result',
                      title: '结果',
                      width: 80,
                      align: 'center',
                      formatter: compareFormat
                    }, {
                      field: 'right.resultUnit',
                      title: '单位',
                      width: 60,
                      align: 'center'
                    }, {
                      field: 'right.ranges',
                      title: '参考值',
                      width: 70,
                      align: 'center',
                      formatter: function(
                        value,
                        row) {
                        if (!value) {
                          return '';
                        }
                        value = value
                          .replace(
                            '~~',
                            '—');
                        return value;
                      }
                    }]
                  ],
                  onBeforeLoad: function(
                    data) {
                    // 获取头部列属性
                    // 修改宽度换行
                    // 根据field来选择
                    var dhr1 = $('#report-info-box .datagrid-htable .datagrid-header-row:eq(0)');
                    dhr1
                      .find(
                        "td[colspan=8] div p")
                      .css(
                        'margin-top',
                        '0px');
                    dhr1
                      .find(
                        "td[colspan=8] div")
                      .css({
                        'height': '100%',
                        'white-space': 'normal'
                      });
                  }
                });
            }
          });
        /*
         * $('.related-report').click(function(){
         * $('.related-report-box
         * ul').html(R[$(this).attr('data-value')]); var pos =
         * $(this).position(); var ch = $('.content').height();
         * var top = 0; if( ($('#related-report-box').height() +
         * pos.top + 20) > ch ){ $('#related-report-box').css({
         * display:'block',
         * top:pos.top+40-$('#related-report-box').height(),
         * right:20 }); } else{ top =
         * pos.top+44+$(this).height();
         * $('.related-report-box').css({ display:'block',
         * top:top, right:20 }); } return false; });
         */
      });
}

/*
 * 清空datagrid
 */
function clearDataGrid() {
  $("#info-tab").datagrid({
    data: []
  });
}

/*
 * 取消优先级 @params 行号 (datagrid行号)
 */
function cancelPRI(lineNum) {
  var id = $("#info-tab").datagrid('getData').rows[lineNum].masterId;
  $.get(ay.contextPath + '/nur/labTestRecord/setLabTestRdsPriFlag.do?id=' + id + '&priFlag=0', function(data) {
    if (data.success) {
      $.messager.alert('提示', '取消优先成功');
      reloadData();
    }
  });
  window.event.cancelBubble = true;
}

/*
 * 设置优先级 @params 行号 (datagrid行号)
 */
function setPRI(lineNum) {
  var id = $("#info-tab").datagrid('getData').rows[lineNum].masterId;
  $.get(ay.contextPath + '/nur/labTestRecord/setLabTestRdsPriFlag.do?id=' + id + '&priFlag=1', function(data) {
    if (data.success) {
      $.messager.alert('提示', '设置优先成功');
      reloadData();
    }
  });
  window.event.cancelBubble = true;
}

/*
 * 显示趋势图
 */
function showGraph() {
  var compareData = $("#compare-tab").datagrid('getSelected'),
    cateArr = [],
    resultUnit = null,
    seriesArr = [];
  if (resultUnit) {
    resultUnit = compareData.resultUnit;
  }
  if (!compareData) {
    $.messager.alert('提示', '无对比数据,请选择！');
    return;
  }
  var itemName = compareData.itemName;
  // console.log(compareData);
  var allRelateReport = getRelateReport($("#hospital").html(), $("#testItem")
    .html());
  var tmpArr = [];
  for (var pdx = 0; pdx < allRelateReport[0].length; pdx++) {
    // 获取分组信息
    cateArr.push(allRelateReport[1][pdx][0]);
    tmpArr.push(allRelateReport[0][pdx][0]);
  }
  for (var idx = 0; idx < tmpArr.length; idx++) {
    for (var ddx = 0; ddx < _data.length; ddx++) {
      if (tmpArr[idx] == _data[ddx].masterId) {
        for (var bdx = 0; bdx < _data[ddx].itemList.length; bdx++) {
          if (itemName == _data[ddx].itemList[bdx].itemName) {
            if (seriesArr[0]) {
              seriesArr[0].data
                .push(parseFloat(_data[ddx].itemList[bdx].result));
            } else {
              seriesArr
                .push({
                  name: itemName,
                  data: [parseFloat(_data[ddx].itemList[bdx].result)]
                });
            }
          }
        }
      }
    }
  }
  /*allRelateReport = tmpArr;
  var ds = [{}],tmp=null;
  for(var idx=0;idx<_data.length;idx++){
  	for(var cdx=0;cdx<allRelateReport.length;cdx++){
  		if(allRelateReport[cdx] == _data[idx].masterId){
  			for(var bdx=0;bdx<_data[idx].itemList.length;bdx++){
  				if(itemName == _data[idx].itemList[bdx].itemName){
  					if(!ds[0].label){
  						ds[0].label = itemName;
  						ds[0].data = [];
  					}
  					ds[0].data.push([_data[idx].reportTime,_data[idx].itemList[bdx].result]);
  				}	
  			}
  		}
  	}
  }	
  //格式化数据
  for(var tdx=0;tdx<ds[0].data.length;tdx++){
  	ds[0].data[tdx][0] = new Date(ds[0].data[tdx][0]).getTime();
  }*/
  /*var dataset = [
      {
      	label: ds[0].label,
          data:ds[0].data
      }
  ];
  var options = {
      series: { 
          lines: { show: true },     
          points: {
              radius: 3,
              show: true
          }
      },
      xaxis: {
          mode: "time",
          timeformat: "%Y/%m/%d"
      },
      grid: {
  		hoverable: true
  	},
  	legend:{
   */
  /*labelFormatter: function(label, series) {
  				   // series is the series object for the label  
  				   return '<a href="#' + label + '" mce_href="#' + label + '">' + label + '</a>';  
  				}*/
  /*
  			}
  		};*/
  $('#qst').dialog('open');
  $("#chartBox").highcharts({
    charts: {
      type: 'column'
    },
    title: {

      text: ''
    },
    xAxis: {
      categories: cateArr
        //指定x轴分组，X轴按照时间来分组
    },
    yAxis: {
      align: 'high',
      title: {
        text: resultUnit
      }
    },
    legend: {
      layout: 'vertical',
      align: 'right',
      verticalAlign: 'middle',
      borderWidth: 0
    },
    series: seriesArr
  });

  /*var plot = $.plot($("#flot-placeholder"), dataset, options);*/

  //设置数据点hover事件
  /*$("#flot-placeholder").bind("plothover", function (event, pos, item) {

  	if (item) {
  		var x = new Date(item.datapoint[0]).format('yyyy-MM-dd hh:mm:ss'),
  			y = item.datapoint[1];

  		$("#flotTooltip").html("<span>结果："+y+"</span><br><span>时间："+x+"</span>")
  			.css({top: item.pageY+5, left: item.pageX+5})
  			.show();
  	} else {
  		$("#flotTooltip").hide();
  	}

  });*/
}

function getMaxVal(obj) {
  var maxNum, tmpNum;
  for (x in obj) {
    if (typeof tmpNum != 'number') {
      tmpNum = obj[x]
      maxNum = tmpNum;
    } else {
      if (tmpNum < obj[x]) {
        tmpNum = obj[x];
        maxNum = tmpNum;
      }
    }
  }
  return maxNum;
}

function resizeDatagrid() {
  console.log(width + ',' + height)
    //$("#compare-tab").datagrid('resize', {height:height,width:width});
}

function closeReportCompare() {
  $("#related-report-compare").dialog('close');
}

$(function() {
  document.body.removeChild(document.body.childNodes[0]);
  $('.content').height($(window).height() - $('.top-tools').height());
  var recordDay = new Date().format("yyyy-MM-dd");
  $("#startDate").val(recordDay);
  $("#endDate").val(recordDay);
  //设置患者信息容器宽度
  if (parent.setPatientInfoWrap) {
    parent.setPatientInfoWrap();
  }
  $("#info-tab").datagrid({

  });
  if (_patientId) {
    reloadData();
  }
  $("<div id='flotTooltip'></div>").css({
    position: "absolute",
    display: "none",
    border: "1px solid #fdd",
    padding: "2px",
    "background-color": "#fee",
    opacity: 0.80,
    "font-size": "14px",
    "z-index": 9999,
    "line-height": "16px"

  }).appendTo("body");
})
