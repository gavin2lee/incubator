jsPlugin.plugin("patientMenu", function(params) {
  var oUlH = $('#mainLayout').layout('panel', 'west').panel('options').height - 32;
  var jumpUrlTemp = localStorage.getItem("jumpUrlTemp", jumpUrlTemp) || null;
  // $("#mainMenu").height(oUlH);

  console.log($(window), params);

  var treeOptions = {
    // url: ay.contextPath + "/patientMenuAction/getMenus.do?patientId="+params.patientId+"&hospitalNo="+params.hospitalNo+"&departmentId="+params.departmentId,
    dnd: false,
    lines: true,
    state: 'closed',
    onSelect: function(node) {
      window.treeNode = node;
      var accessUrl = ay.contextPath;

      // 各种情况的判定
      if ("labTest" == node.id) {
        accessUrl = accessUrl + '/nur/labTestRecord/labTestRecordMain.do?patientId=' + params.patientId;
      } else if ("medicalOrder" == node.id) {
        accessUrl = accessUrl + '/nur/patOrderDetail/prescriptionAll.do?patientId=' + params.patientId;
      } else if ("executiveOrder" == node.id) {
        accessUrl = accessUrl + '/nur/patOrderDetail/prescriptionAll.do?patientId=' + params.patientId;
      } else if ("operation" == node.id) {
        accessUrl = accessUrl + '/nur/operationOrder/' + params.patientId + '/operationOrderMain.do';
      } else if ("inspectRpt" == node.id) {
        accessUrl = accessUrl + '/nur/inspectionRpt/inspectionRptMain.do?patientId=' + params.patientId;
      } else if ("pCrisisValue" == node.id) {
        accessUrl = accessUrl + '/nur/crisisValue/patCrisisValueMain.do?patientId=' + params.patientId;
      } else if ("liquor" == node.id) {
        accessUrl = accessUrl + '/toliquor?patientId=' + params.patientId;
      } else if ("persral" == node.id) {
        accessUrl = accessUrl + '/topersral?patientId=' + params.patientId;
      } else if ("infusion" == node.id) {
        accessUrl = accessUrl + '/toinfusion?patientId=' + params.patientId;
      } else if ("ward" == node.id) {
        accessUrl = accessUrl + '/toward.do?patientId=' + params.patientId;
      } else if ("tempSheet" == node.id) {
        accessUrl = accessUrl + "/nur/bodySign/" + params.patientId + "/bodyTempSheetPdf.do";
        $('#mainFrame').attr('src', accessUrl);
      }

      // 处理护理评估与护理记录   文书类型
      else if ('fixed' == node['show_type']) {
        if (!node.type) {
          return;
        }
        var recordId = node.recordId ? node.recordId : "";
        accessUrl = accessUrl + "/nur/doc/toDocMain?type=1&recordId=" + recordId + "&id=evaluate&patientId=" + params.patientId + "&template_id=" + node.t_id + "&show_type=" + node['show_type'];
      } else if ('list' == node['show_type']) {
        if (!node.type) {
          return;
        }
        recordId = node.recordId ? node.recordId : "";
        var type = node.type;
        //if (2 == type) {
        var day = new Date().format("yyyy-MM-dd");
        //}
        if ("day" == type) {
          if ("report_type" in node) {
            accessUrl = accessUrl + "/nur/doc/toDocMain?day=" + node.text + "&type=" + type + "&recordId=" + recordId + "&id=" + node.id + "&patientId=" + params.patientId + "&template_id=" + node.t_id + "&report_type=" + node.report_type + "&show_type=" + type;
          } else {
            accessUrl = accessUrl + "/nur/doc/toDocMain?day=" + node.text + "&type=" + type + "&recordId=" + recordId + "&id=" + node.id + "&patientId=" + params.patientId + "&template_id=" + node.t_id + "&report_type=" + node.report_type + "&show_type=" + type;
          }
        } else if ("time" == type) {
          accessUrl = accessUrl + "/nur/doc/toDocMain?day=" + node.day + "&time=" + node.text + "&type=" + type + "&recordId=" + recordId + "&id=" + node.id + "&patientId=" + params.patientId + "&template_id=" + node.t_id + "&report_type=" + node.report_type;
        } else {
          window.dayList = node.dayList;
          var startDate = '',
            endDate = '',
            dayLen = node.dayList ? node.dayList.length : 0;
          if (dayLen > 0) {
            var reduceNum = dayLen % 3 === 0 ? 3 : dayLen % 3;
          }
          if (node.dayList && dayLen > 2) {
            startDate = node.dayList[dayLen - 1];
            endDate = node.dayList[dayLen - reduceNum];
          } else {
            if (node.dayList && node.dayList.length > 0) {
              startDate = node.dayList[dayLen - 1];
              endDate = node.dayList[0];
            }
          }
          /*if (node.report_type === '1' && node.dayList.length > 0) {
              //return;
          }*/
          if (node.dayList && node.report_type === '1') {
            accessUrl = accessUrl + "/nur/doc/toDocMain?day=" + day + "&type=" + type + "&recordId=" + recordId + "&id=" + node.id + "&patientId=" + params.patientId + "&template_id=" + node.t_id + "&report_type=" + node.report_type + "&show_type=" + node['show_type'] + '&startDate=' + startDate + '&endDate=' + endDate;
          } else {
            accessUrl = accessUrl + "/nur/doc/toDocMain?day=" + day + "&type=" + type + "&recordId=" + recordId + "&id=" + node.id + "&patientId=" + params.patientId + "&template_id=" + node.t_id + "&report_type=" + node.report_type + "&show_type=" + node['show_type'];
          }
        }
      } else {
        return;
      }

      // 添加历史文书参数
      accessUrl = addHistoryParam(accessUrl, params);

      // 如果是查询历史文书，则文书外的菜单不跳转
      if (params.isHistoryDoc) {
        if (!node.show_type) return;
      }

      jumpUrlTemp = node.id;

      if ("1" == jumpUrlTemp || "2" == jumpUrlTemp) {
        $('#mainFrame').attr('src', accessUrl);
        jumpUrlTemp = null;
      } else {
        $('#mainFrame').attr('src', accessUrl);
        jumpUrlTemp = node.id;
        // var documentUrl = ay.contextPath +"/nurseDocAction/entryTemplate.do?templateId="+node.attributes.obj.templateRefid+"&patientId="+patient.patientId
        //  +"&hospitalNo="+patient.hospitalNo+"&numberType="+node.attributes.obj.numberType;
        // $('#mainFrame').attr('src', documentUrl);
        // jumpUrlTemp = null;
      }
      localStorage.setItem("jumpUrlTemp", jumpUrlTemp);
    },
    onLoadSuccess: function(node, data) {

      // 渲染完成查找上次选择过的触发选择事件
      if (jumpUrlTemp) {
        var node = $('#mainMenu').tree('find', jumpUrlTemp);
        if (node) {
          //延迟100毫秒执行防止出现无法适应整个宽高情况
          setTimeout(function() {
            $('#mainMenu').tree('select', node.target);
          }, 100);
        }
      }
    },
    onExpand: function(node) {},
    onCollapse: function(node) {},
    onContextMenu: function(e, node) {
      console.log(node);

      window.toDefaultList = node;
      // debugger;
      if (node.report_type === '1' && node.type === '2') {
        $('#mm').menu('show', {
          left: e.pageX,
          top: e.pageY
        });
      }

      e.preventDefault();
      // $("#dkbTree_M").tree("select", node.target);
      // $("#dkbMenu").menu("show", {
      //     left: e.pageX,
      //     top: e.pageY
      // });
      // var menuItemEIAddSibling = $("#menuAddSibling")[0];
      // var menuItemEIAddChild = $("#menuAddChild")[0];
      // var menuItemEIDeleteNode = $("#menuDeleteNode")[0];
      // var menuItemEIMoveUp = $("#menuMoveUp")[0];
      // var menuItemEIMoveDown = $("#menuMoveDown")[0];
      // var menuItemEIMoveLeft = $("#menuMoveLeft")[0];
      // var menuItemEIMoveRight = $("#menuMoveRight")[0];
      // if (node.id == PUBLIC_ROOT_ID || node.id == PRIVATE_ROOT_ID || node.id == INSTITUTION_ROOT_ID) {
      //     $("#dkbMenu").menu("disableItem", menuItemEIAddSibling);
      //     $("#dkbMenu").menu("enableItem", menuItemEIAddChild);
      //     $("#dkbMenu").menu("disableItem", menuItemEIDeleteNode);
      //     $("#dkbMenu").menu("disableItem", menuItemEIMoveUp);
      //     $("#dkbMenu").menu("disableItem", menuItemEIMoveDown);
      //     $("#dkbMenu").menu("disableItem", menuItemEIMoveLeft);
      //     $("#dkbMenu").menu("disableItem", menuItemEIMoveRight);
      // } else {
      //     if (node.attributes.NodeType == NODE_TYPE_D) {
      //         $("#dkbMenu").menu("enableItem", menuItemEIAddSibling);
      //         $("#dkbMenu").menu("enableItem", menuItemEIAddChild);
      //         $("#dkbMenu").menu("enableItem", menuItemEIDeleteNode);
      //     } else if (node.attributes.NodeType == NODE_TYPE_F) {
      //         $("#dkbMenu").menu("enableItem", menuItemEIAddSibling);
      //         $("#dkbMenu").menu("disableItem", menuItemEIAddChild);
      //         $("#dkbMenu").menu("enableItem", menuItemEIDeleteNode);
      //     }
      //     var prevSiblingNode = getPrevSiblingNode(node);
      //     if (prevSiblingNode) {
      //         $("#dkbMenu").menu("enableItem", menuItemEIMoveUp);
      //     } else {
      //         $("#dkbMenu").menu("disableItem", menuItemEIMoveUp);
      //     }
      //     if (getNextSiblingNode(node)) {
      //         $("#dkbMenu").menu("enableItem", menuItemEIMoveDown);
      //     } else {
      //         $("#dkbMenu").menu("disableItem", menuItemEIMoveDown);
      //     }
      //     if (node.attributes.ParentRefid == PUBLIC_ROOT_ID || node.attributes.ParentRefid == PRIVATE_ROOT_ID || node.attributes.ParentRefid == INSTITUTION_ROOT_ID) {
      //         $("#dkbMenu").menu("disableItem", menuItemEIMoveLeft);
      //     } else {
      //         $("#dkbMenu").menu("enableItem", menuItemEIMoveLeft);
      //     }
      //     if (prevSiblingNode && prevSiblingNode.attributes.NodeType == NODE_TYPE_D) {
      //         $("#dkbMenu").menu("enableItem", menuItemEIMoveRight);
      //     } else {
      //         $("#dkbMenu").menu("disableItem", menuItemEIMoveRight);
      //     }
      // }
    }
  };

  $("#mainMenu").tree(treeOptions);

  function getType(id, arr) {
    var i = 0,
      pureId = id.replace(/\s/g, '');
    for (i; i < arr.length; i += 1) {
      if (arr[i]['id'] == pureId) {
        return arr[i]['id'];
      }
    }

    return null;
  }


  function addHistoryParam(path, param) {
    var _path = path;
    if (path.indexOf('?') < 0) {
      _path = path + '?';
    } else {
      _path = path + '&';
    }
    return _path + 'isHistoryDoc=' + (param.isHistoryDoc ? param.isHistoryDoc : '');
  }
  getMenuJson(params);

  function getMenuJson(params) {
    // debugger;
    var hashObj = ay.hashToObj(location.href);
    var hashDeptCode = hashObj && hashObj.deptCode;

    // 获取菜单列表
    window.fromTemplate = [];
    $.when(
      $.get(ay.contextPath + "/patientMenuAction/getMenus.do?patientId=" + params.patientId + "&hospitalNo=" + params.hospitalNo + "&departmentId=" + params.departmentId),
      $.get(ay.contextPath + "/nur/doc/getTree", {
        'dept_code': params.departmentId != "" ? localStorage.getItem('deptCode') || hashDeptCode : '',
        'valid': 'PC',
        'inpatient_no': params.patientId,
        'docQueryType': (function() {
          if (params.isHistoryDoc) {
            return 2;
          }

          if (params.isLeave) {
            return 1;
          }

          return 0;
        })()
      })
    ).then(function(m1, m2) {

      var menu1 = m1[0], // 患者信息列表
        menu2 = m2[0].data, // 护理文书列表
        i, docMenu = [],
        docMenuCache = [],
        idCode,
        resData;

      // 转换文书菜单列表
      for (i = 0; i < menu2.length; i++) {
        docMenu.push({});
        if (menu2[i]['show_type'] === 'fixed') {
          //if (menu2[i].type_id == 1 || menu2[i].type_id == 4) {
          docMenu[i].id = menu2[i].type_id;
          docMenu[i].text = menu2[i].type_name;
          //docMenu[i].iconCls="icon-save";
          docMenuCache = menu2[i].data;

          idCode = getType(docMenu[i].id, menu1);

          if (docMenuCache.length > 0) {
            docMenu[i].children = []
          }
          for (var i1 = 0; i1 < docMenuCache.length; i1++) {
            if (docMenuCache[i1].record_id) {
              docMenu[i].children.push({
                "id": idCode,
                "t_id": docMenuCache[i1].template_id,
                "text": docMenuCache[i1].template_name,
                "recordId": docMenuCache[i1].record_id,
                "type": menu2[i].type_id,
                "show_type": menu2[i]['show_type']
              });
            } else {
              docMenu[i].children.push({
                "id": idCode,
                "t_id": docMenuCache[i1].template_id,
                "text": docMenuCache[i1].template_name,
                "recordId": docMenuCache[i1].record_id,
                "type": menu2[i].type_id,
                "show_type": menu2[i]['show_type']
                  /*,
                                                           "iconCls":"icon-emp"*/
              });
            }
          }
        } else if (menu2[i]['show_type'] === 'list') {
          docMenu[i].id = menu2[i].type_id;
          docMenu[i].text = menu2[i].type_name;
          //docMenu[i].iconCls="icon-save";
          docMenuCache = menu2[i].data;

          idCode = getType(docMenu[i].id, menu1);

          if (docMenuCache.length > 0) {
            docMenu[i].children = []
          }
          for (var i1 = 0; i1 < docMenuCache.length; i1++) {
            var obj_temp = {
              "id": idCode,
              "t_id": docMenuCache[i1].template_id,
              "text": docMenuCache[i1].template_name,
              "recordId": docMenuCache[i1].record_id,
              "type": menu2[i].type_id,
              "report_type": docMenuCache[i1].report_type || '',
              "show_type": menu2[i]['show_type']
            };

            // 缓存护理记录单templateID 用于出院记录单打印时得到护理记录的页码.
            if (docMenuCache[i1].report_type == '1') {
              window.fromTemplate[0] = docMenuCache[i1].template_id;
            }

            docMenu[i].children.push(obj_temp);

            if (docMenuCache[i1].data.length > 0) {
              docMenu[i].children.pop();

              obj_temp.dayList = docMenuCache[i1].data.map(function(item) {
                return item.data_value;
              });
              docMenu[i].children.push(obj_temp);

              docMenu[i].children[i1].children = [];


              for (var i2 = 0; i2 < docMenuCache[i1].data.length; i2++) {
                docMenu[i].children[i1].children.push({
                  "id": idCode,
                  "dayList": '',
                  "t_id": docMenuCache[i1].data[i2].template_id,
                  "pId": docMenuCache[i1].data[i2].pat_id,
                  "text": docMenuCache[i1].data[i2].data_value,
                  "recordId": docMenuCache[i1].data[i2].record_id,
                  "show_type": menu2[i]['show_type'],
                  "type": "day",
                  "state": "closed" // 默认关闭
                });
                console.log(docMenuCache);
                if ("report_type" in docMenuCache[i1]) {
                  console.log(docMenuCache[i1].report_type);
                  docMenu[i].children[i1].children[i2].report_type = docMenuCache[i1].report_type;
                  //docMenu[i].children[i1].report_type = docMenuCache[i1].report_type;
                }
                if (docMenuCache[i1].data[i2].data.length > 0) {
                  docMenu[i].children[i1].children[i2].children = [];
                  for (var i3 = 0; i3 < docMenuCache[i1].data[i2].data.length; i3++) {
                    docMenu[i].children[i1].children[i2].children.push({
                      "day": docMenuCache[i1].data[i2].data_value,
                      "id": idCode,
                      "t_id": docMenuCache[i1].data[i2].template_id,
                      "pId": docMenuCache[i1].data[i2].pat_id,
                      "text": docMenuCache[i1].data[i2].data[i3].data_value,
                      "recordId": docMenuCache[i1].data[i2].data[i3].record_id,
                      "show_type": menu2[i]['show_type'],
                      "type": "time"
                    });
                  }
                }
              }
            }
          }
        }
      }

      for (i = 0; i < menu1.length; i++) {

        if (menu1[i].id && !isNaN(Number(menu1[i].id)) && docMenu[0] && docMenu[0].children) {
          resData = $.grep(docMenu, function(menuItem, idx) {
            return menuItem['id'] == menu1[i].id;
          });

          if (resData.length !== 0 && resData[0].children) {
            menu1[i].children = resData[0].children;
          }
        }
        /*if (menu1[i].id && menu1[i].id == "2" && docMenu[1] && docMenu[1].children) {
         menu1[i].children = docMenu[1].children;
         }
         if (menu1[i].id && menu1[i].id == "3" && docMenu[1] && docMenu[1].children) {
         menu1[i].children = docMenu[2].children;
         }
         if (menu1[i].id && menu1[i].id == "4" && docMenu[1] && docMenu[1].children) {
         menu1[i].children = docMenu[3].children;*/
        //}
      }

      console.log(menu1);

      $("#mainMenu").attr('data-options', 'data:' + JSON.stringify(menu1) + ';' + JSON.stringify(treeOptions).replace(/[{]|[}]/g, ''));

      $("#mainMenu").tree({
        data: menu1
      });
    });
  }
});


/*
 更新非表格类型rId
 */
function updateTreeInfo(rId) {
  var node = $("#mainMenu").tree("getSelected");
  if (node) {
    $("#mainMenu").tree("update", {
      target: node.target,
      recordId: rId
    });
  }
}

// 右键菜单设置默认记录单
function setDefaultList() {
  var t = $('#mainMenu');
  var node = window.toDefaultList;
  console.log(node);

  $.post(ay.contextPath + '/nur/doc/setPatDefaultTempl', {
    patID: patient.patientId,
    templateID: node.t_id,
    deptCode: localStorage.getItem('deptCode')
  }).done(function(res) {
    debugger;
    if (res.rslt === '0') {
      $.messager.alert('提示', '设置成功！');
    } else {
      $.messager.alert('提示', res.msg);
    }
  }).fail(function(err) {
    $.messager.alert('提示', '服务器错误');
  });
}

/*
 更新表格类型树 数据
 @params (obj) 包含日期层次和时间点层次
 */
function updateTreeTableInfo(params) {
  console.log(params);
  var checked = $("#mainMenu").tree("getSelected"),
    i;
  var day = params.text,
    time = params.children.text;
  if (checked.type === "2") {
    //appendDayNode(params,checked.target);
    checked = $("#mainMenu").tree("getParent", checked.target);
    return;
  } else if (checked.type === "day") {
    checked = $("#mainMenu").tree("getParent", checked.target);

  } else if (checked.type === "time") {
    checked = $("#mainMenu").tree("getParent", $("#mainMenu").tree("getParent", checked.target).target);
  } else {

  }
  var children = $("#mainMenu").tree("getChildren", checked.target);
  if (!time) {
    appendDayNode(params, checked.target);
    return;
  }
  for (i = 0; i < children.length; i++) {
    if (children[i].type === "day" && children[i].text === day) {
      appendTimeNode(params, children[i].target);
      return;
    }
  }
  appendDayAndTimeNode(params, checked.target);
  return;
}

/*
 新增时间节点
 @params {obj} 传入的参数
 @target {obj} 父节点
 #return -
 */
function appendDayNode(params, target) {
  $('#mainMenu').tree("append", {
    parent: target,
    data: [{
      "id": params.id,
      "t_id": params.tId,
      "type": "day",
      "text": params.text,
      "pId": params.pId,
      "children": []
    }]
  });
}

/*
 更新时间与日期
 */
function updateDateAndTime(params, target) {
  var checked = $("#mainMenu").tree("getSelected"),
    i;
  var day = params.text,
    time = params.children.text;
}

/*
 新增时间节点
 @params {obj} 传入的参数
 @target {obj} 父节点
 #return -
 */
function appendTimeNode(params, target) {
  $('#mainMenu').tree("append", {
    parent: target,
    data: [{
      "day": params.text,
      "id": params.id,
      "t_id": params.tId,
      "pId": params.pId,
      "text": params.children.text,
      "type": "time",
      "recordId": params.children.rId
    }]
  });
}

/*
 新增日期和时间节点
 @params {obj} 传入的参数
 @target {obj} 父节点
 #return -
 */
function appendDayAndTimeNode(params, target) {

  var data = [];

  $('#mainMenu').tree("append", {
    parent: target,
    data: [{
      "id": params.id,
      "t_id": params.tId,
      "type": "day",
      "text": params.text,
      "pId": params.pId,
      "children": [{
        "day": params.text,
        "id": params.id,
        "t_id": params.tId,
        "pId": params.pId,
        "text": params.children.text,
        "type": "time",
        "recordId": params.children.rId
      }]
    }]
  });
}


function showTemplate(templateId, url) {
  var templateUrl = ay.contextPath + "/nurseDocAction/editNurseDoc.do?nurseDocId=" + templateId;
  $('#nurMainFrame').attr('src', templateUrl);

}

function dkbMenuHandler(item) {
  var id = item.id;
  if (id == "menuAddSibling") {
    addSibling();
  } else if (id == "menuAddChild") {
    addChild();
  } else if (id == "menuDeleteNode") {
    deleteNode();
  } else if (id == "menuMoveUp") {
    moveUp();
  } else if (id == "menuMoveDown") {
    moveDown();
  } else if (id == "menuMoveLeft") {
    moveLeft();
  } else if (id == "menuMoveRight") {
    moveRight();
  }
}

function addSibling() {
  if (!selectedDKBNode) {
    $.messager.alert("提示", "请选择节点");
    return;
  }
  action = "addSibling";
  dkb_url = DKB_URL_PREFIX + "AddSibling";
  $("#dkb_info_M").text("添加同级节点");
  $("#dkb_refid_M").val(selectedDKBNode.id);
  $("#dkb_parent_refid_M").val(selectedDKBNode.attributes.ParentRefid);
  $("#dkb_base_type_M").val(selectedDKBNode.attributes.BaseType);
  $("#dkb_path_M").val(selectedDKBNode.attributes.Path);
  $("#dkb_name_M").val("");
  $("#dkb_description_M").val("");
  $("#dkb_conclusion_M").val("");
  $("#dkb_node_type_M").combobox("enable");
}

function addChild() {
  if (!selectedDKBNode) {
    $.messager.alert("提示", "请选择节点");
    return;
  }
  action = "addChild";
  dkb_url = DKB_URL_PREFIX + "AddChild";
  $("#dkb_info_M").text("添加子节点");
  $("#dkb_refid_M").val("");
  $("#dkb_parent_refid_M").val(selectedDKBNode.id);
  $("#dkb_base_type_M").val(selectedDKBNode.attributes.BaseType);
  $("#dkb_path_M").val(selectedDKBNode.attributes.Path);
  $("#dkb_name_M").val("");
  $("#dkb_description_M").val("");
  $("#dkb_conclusion_M").val("");
  $("#dkb_node_type_M").combobox("enable");
}

function deleteNode() {
  if (!selectedDKBNode) {
    $.messager.alert("提示", "请选择节点");
    return;
  }
  $.messager.confirm("提示", "确定要删除此记录吗?", function(r) {
    if (r) {
      $.post(DKB_URL_PREFIX + "DeleteEntity", {
        Refid: selectedDKBNode.id,
        Path: selectedDKBNode.attributes.Path
      }, function(result) {
        try {
          if (result.success) {
            var parentNode = $("#dkbTree_M").tree("getParent", selectedDKBNode.target);
            $("#dkbTree_M").tree("remove", selectedDKBNode.target);
            locateDKB(parentNode.id, "dkbTree_M");
            scrollToDKB(parentNode.target, "dkbContainer_M");
            setTreeWidth("dkbTree_M");
            setDKBTreeOperationBackground($("#dkbTree_M li div"));
            writeLog(result.message);
          } else {
            $.messager.alert("警告", result.message, "warning", function() {
              location.reload();
            });
          }

        } catch (e) {
          $.messager.alert("提示", result);
        }
      });
    }
  });
}

function moveUp() {
  $("#moveUpBtn").attr("disabled", true);
  if (!selectedDKBNode) {
    $.messager.alert("提示", "请选择节点");
    return;
  }
  var targetNode = getPrevSiblingNode(selectedDKBNode, "dkbTree_M");
  if (targetNode) {
    $.post(DKB_URL_PREFIX + "UpdatePosition", {
      targetRefid: targetNode.id,
      sourceRefid: selectedDKBNode.id,
      action: "moveUp",
      position: null
    }, function(result) {
      try {
        if (result.success) {
          //交换NodeOrders
          var targetOrders = targetNode.attributes.NodeOrders;
          var sourceOrders = selectedDKBNode.attributes.NodeOrders;
          targetNode.attributes.NodeOrders = sourceOrders;
          selectedDKBNode.attributes.NodeOrders = targetOrders;
          //更改节点位置
          var tempData = $("#dkbTree_M").tree("pop", selectedDKBNode.target);
          var id = selectedDKBNode.id; //insert操作后选中节点会改变，保存之前选中节点的id
          $("#dkbTree_M").tree("insert", {
            before: targetNode.target,
            data: tempData
          });
          locateDKB(id, "dkbTree_M");
          scrollToDKB(selectedDKBNode.target, "dkbContainer_M");
          setTreeWidth("dkbTree_M");
          setDKBTreeOperationBackground($("#dkbTree_M li div"));
          writeLog(result.message);
        } else {
          $.messager.alert("警告", result.message, "warning", function() {
            location.reload();
          });
        }

      } catch (e) {
        $.messager.alert("提示", result);
      }
    });
  }
}

function moveDown() {
  $("#moveDownBtn").attr("disabled", true);
  if (!selectedDKBNode) {
    $.messager.alert("提示", "请选择节点");
    return;
  }
  var targetNode = getNextSiblingNode(selectedDKBNode, "dkbTree_M");
  if (targetNode) {
    $.post(DKB_URL_PREFIX + "UpdatePosition", {
      targetRefid: targetNode.id,
      sourceRefid: selectedDKBNode.id,
      action: "moveDown",
      position: null
    }, function(result) {
      try {
        if (result.success) {
          //交换NodeOrders
          var targetOrders = targetNode.attributes.NodeOrders;
          var sourceOrders = selectedDKBNode.attributes.NodeOrders;
          targetNode.attributes.NodeOrders = sourceOrders;
          selectedDKBNode.attributes.NodeOrders = targetOrders;
          //更改节点位置
          var tempData = $("#dkbTree_M").tree("pop", selectedDKBNode.target);
          var id = selectedDKBNode.id; //insert操作后选中节点会改变，保存之前选中节点的id
          $("#dkbTree_M").tree("insert", {
            after: targetNode.target,
            data: tempData
          });
          locateDKB(id, "dkbTree_M");
          scrollToDKB(selectedDKBNode.target, "dkbContainer_M");
          setTreeWidth("dkbTree_M");
          setDKBTreeOperationBackground($("#dkbTree_M li div"));
          writeLog(result.message);
        } else {
          $.messager.alert("警告", result.message, "warning", function() {
            location.reload();
          });
        }

      } catch (e) {
        $.messager.alert("提示", result);
      }
    });
  }
}

function moveLeft() {
  $("#moveLeftBtn").attr("disabled", true);
  if (!selectedDKBNode) {
    $.messager.alert("提示", "请选择节点");
    return;
  }
  var id = selectedDKBNode.id;
  $.post(DKB_URL_PREFIX + "UpdatePosition", {
    sourceRefid: id,
    action: "moveLeft",
    position: null
  }, function(result) {
    try {
      if (result.success) {
        var node = $("#dkbTree_M").tree("find", id);
        var parent = $("#dkbTree_M").tree("getParent", node.target);
        //parent后续节点序号加1
        var nextSiblingNode = getNextSiblingNode(parent, "dkbTree_M");
        while (nextSiblingNode) {
          nextSiblingNode.attributes.NodeOrders += 1;
          nextSiblingNode = getNextSiblingNode(nextSiblingNode, "dkbTree_M");
        }

        $("#dkbTree_M").tree("remove", node.target);
        $("#dkbTree_M").tree("insert", {
          after: parent.target,
          data: result.list[0]
        });
        locateDKB(id, "dkbTree_M");
        scrollToDKB(selectedDKBNode.target, "dkbContainer_M");
        setTreeWidth("dkbTree_M");
        setDKBTreeOperationBackground($("#dkbTree_M li div"));
        writeLog(result.message);
      } else {
        $.messager.alert("警告", result.message, "warning", function() {
          location.reload();
        });
      }
    } catch (e) {
      $.messager.alert("提示", result);
    }
  });
}

function moveRight() {
  $("#moveRightBtn").attr("disabled", true);
  if (!selectedDKBNode) {
    $.messager.alert("提示", "请选择节点");
    return;
  }

  var id = selectedDKBNode.id;
  var prevSiblingNode = getPrevSiblingNode(selectedDKBNode, "dkbTree_M");
  $.post(DKB_URL_PREFIX + "UpdatePosition", {
    targetRefid: prevSiblingNode.id,
    sourceRefid: id,
    action: "moveRight",
    position: null
  }, function(result) {


    try {
      if (result.success) {
        var node = $("#dkbTree_M").tree("find", id);
        prevSiblingNode = getPrevSiblingNode(node);
        $("#dkbTree_M").tree("remove", node.target);
        $("#dkbTree_M").tree("append", {
          parent: prevSiblingNode.target,
          data: result.list
        });
        locateDKB(id, "dkbTree_M");
        scrollToDKB(selectedDKBNode.target, "dkbContainer_M");
        setTreeWidth("dkbTree_M");
        setDKBTreeOperationBackground($("#dkbTree_M li div"));
        writeLog(result.message);
      } else {
        $.messager.alert("警告", result.message, "warning", function() {
          location.reload();
        });
      }

    } catch (e) {
      $.messager.alert("提示", result);
    }
  });
}

function submitForm() {
  $("#form").form("submit", {
    url: dkb_url,
    success: function(obj) {
      var result = $.parseJSON(obj);
      if (action == "addSibling") {
        try {
          if (result.success) {
            var nextSiblingNode = getNextSiblingNode(selectedDKBNode, "dkbTree_M");
            while (nextSiblingNode) {
              nextSiblingNode.attributes.NodeOrders += 1;
              nextSiblingNode = getNextSiblingNode(nextSiblingNode, "dkbTree_M");
            }

            $("#dkbTree_M").tree("insert", {
              after: selectedDKBNode.target,
              data: result.list[0]
            });

            var node = locateDKB(result.list[0].id, "dkbTree_M");
            scrollToDKB(node.target, "dkbContainer_M");
            setTreeWidth("dkbTree_M");
            setDKBTreeOperationBackground($("#dkbTree_M li div"));
            writeLog(result.message);
          } else {
            $.messager.alert("警告", result.message, "warning", function() {
              location.reload();
            });
          }

        } catch (e) {
          $.messager.alert("提示", result);
        }
      } else if (action == "addChild") {
        try {
          if (result.success) {
            $("#dkbTree_M").tree("append", {
              parent: selectedDKBNode.target,
              data: result.list
            });
            var node = locateDKB(result.list[0].id, "dkbTree_M");
            scrollToDKB(node.target, "dkbContainer_M");
            setTreeWidth("dkbTree_M");
            setDKBTreeOperationBackground($("#dkbTree_M li div"));
            writeLog(result.message);
          } else {
            $.messager.alert("警告", result.message, "warning", function() {
              location.reload();
            });
          }

        } catch (e) {
          $.messager.alert("提示", result);
        }
      } else if (action == "update") {
        try {
          if (result.success) {
            selectedDKBNode.text = $("#dkb_name_M").val();
            selectedDKBNode.attributes.Description = $("#dkb_description_M").val();
            selectedDKBNode.attributes.Conclusion = $("#dkb_conclusion_M").val();
            $("#dkbTree_M").tree("update", {
              target: selectedDKBNode.target,
              text: selectedDKBNode.text
            });
            setTreeWidth("dkbTree_M");
            setDKBTreeOperationBackground($("#dkbTree_M li div"));
            writeLog(result.message);
          } else {
            $.messager.alert("警告", result.message, "warning", function() {
              location.reload();
            });
          }

        } catch (e) {
          $.messager.alert("提示", result);
        }
      }
    }
  });
}
