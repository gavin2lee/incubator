var log = console.log.bind(console);
var editIndex = undefined;
var $table = $('.config-table');
var noteConfig = {
  metaData: null,
  editData: null,
  editTypes: [],
  temp: {},
  isUpdate: false,
  editingObj: {},
  allItems: null,
  checkItemObj: {},
  subItemArr: [],
  checkItemArr: [],

  init: function(callback) {
    var that = noteConfig,
      $dialog = $('.edit-dialog'),
      $addDialog = $('.add-item-dialog');

    that.getData(callback);

    $(document).on('click', '.edit-this', function(e) {
      that.editOpen(this);
    });

    $('.selecte-wrapper_left').on('click', '.check-item', function() {
      noteConfig.addItem(this, null);
    });

    // 全选
    noteConfig.checkAllObj = ay.checkAll({
      wrapper: '.selecte-wrapper_left',
      checkAllTarget: '#checkAll',
      subTarget: '.check-item',
      allCallback: function(context) {
        if (context.checked) {
          $('.check-item').each(function(i, c) {
            noteConfig.addItem(c, null);
          })
        } else {
          $('.form-wrapper_selected').html('');
          noteConfig.checkItemObj = {}
        }
      }
    });

    // 添加子项目
    $(document).on('click', '.btn-add-sub', that.addSubItem);

    $('.selecte-wrapper_right').on('click', '.delete-arrow', function() {
      noteConfig.removeItem(this, null);
    });

    $('[name="showType"]').on('change', function(e) {
      var selectedVal = $(this.selectedOptions).val(),
        $subItem = $('#subItem');

      if (selectedVal === 'tableScroll') {
        $('.data-select').parents('li').show();
      } else {
        $('.data-select').parents('li').hide();
        $subItem.parents('li').hide();
      }
    });

    $(document).on('click', '[name="dataType"]', function(e) {
      //log($(this));
      var _checked = this.checked,
        code = $(this).attr('id');

      if (_checked) {
        $('#isMulti-' + code).removeAttr('disabled');
      } else {
        $('#isMulti-' + code).attr('disabled', 'disabled');
        if (code !== 'customText') {
          $('#isMulti-' + code)[0].checked = false;
        }
      }
    });

    $(document).on('click', '#item', function(e) {
      //log($(this));
      var _checked = this.checked,
        $subItem = $('#subItem');

      if (_checked) {
        $subItem.parents('li').show();
      } else {
        $subItem.parents('li').hide();
      }
    });

    $dialog.dialog({
      title: '编辑项目',
      width: 900,
      height: 420,
      modal: true,
      closed: true,
      buttons: [{
        text: '保存',
        handler: function() {
          $dialog.dialog("close");
          noteConfig.save();
        }
      }, {
        text: '关闭',
        handler: function() {
          $dialog.dialog("close");
        }
      }]
    });

    $addDialog.dialog({
      title: '添加项目',
      width: 900,
      height: 420,
      modal: true,
      closed: true,
      buttons: [{
        text: '保存',
        handler: function() {
          $addDialog.dialog("close");
          noteConfig.saveItems();
        }
      }, {
        text: '关闭',
        handler: function() {
          $addDialog.dialog("close");
          noteConfig.checkItemObj = {};
        }
      }]
    })
  },

  transformData: function(data) {
    var that = noteConfig,
      temp = $.extend(true, [], data);
    // 数据处理/过滤
    temp = temp.filter(function(c) {
      // 子节点过滤
      if (c['parentId'] === '' || !c['parentId']) {
        c['isBedCode'] = c['isBedCode'] ? '是' : '否';
        c['isEdit'] = c['isEdit'] ? '是' : '否';
        c['isColspan'] = c['isColspan'] ? '是' : '否';
        c['isShowTitle'] = c['isShowTitle'] ? '是' : '否';
        c['isShowData'] = c['isShowData'] ? '是' : '否';
        /*c['isDosage'] = c['isDosage'] ? '是' : '否';*/
        c['isShowLineR'] = c['isShowLineR'] ? '是' : '否';
        c['isShowLineB'] = c['isShowLineB'] ? '是' : '否';
        c['isShowLineT'] = c['isShowLineT'] ? '是' : '否';
        c['isInner'] = c['nurseWhiteBoardEditTypes'].length > 1 ? '是' : '否';
        c['operate'] = '<button class="_btn _btn-tn edit-this" onclick="noteConfig.editOpen(this)">编辑</button><button class="_btn _btn-tn delete-this" style="margin-left: 4px;" onclick="noteConfig.deleteItem(this)">删除</button>';
        if (c['showType']) c['showType'] = that.editData['showStyle'][c['showType']];

        // 提取子项目items
        if (c['items'].length > 0) {
          that.temp.items = [];
          c['items'].forEach(function(c) {
            that.temp.items.push({
              code: c['code'],
              name: c['name'],
              parentId: c['parentId']
            })
          })
        }
        return c;
      }
    });

    return temp;
  },

  getData: function(callback) {
    var that = noteConfig;
    /*
     * 模板获取小白板元数据列表信息
     接口：/nur/nurseWhiteBoard/getAllNwbMetadatas
     入参：String deptCode
     * */

    $.when(
      $.get(ay.contextPath + '/nur/nurseWhiteBoard/getNurseWhiteBoardMetadatas', {
        deptCode: localStorage.getItem('deptCode')
      }),
      $.get(ay.contextPath + '/nur/nurseWhiteBoard/getNwbMetadataDics', {
        deptCode: localStorage.getItem('deptCode')
      })
    ).then(function(res1, res2) {
      log(res1, res2);
      var temp;
      if (res1[1] === 'success' && res2[1] === 'success') {
        that.metaData = res1[0]['data']['list'];
        that.editData = res1[0]['data']['freq'];
        that.template = res2[0]['data']['template'];
        that.templateSelected = res2[0]['data']['templateSelect'];
        temp = that.transformData(that.metaData);

        that.allItems = res2[0]['data']['data'];

        if (callback) callback(temp);
      }
    }).fail(function(err) {
      $.messager.alert('info', '服务器异常!')
    })
  },

  fillAllItems: function(data) {
    var dataArr = data || noteConfig.allItems,
      $ul = $('.form-wrapper_item'),
      $rightUl = $('.form-wrapper_selected'),
      $check = $('.check-wrapper'),
      $template = $('[name="template"]'),
      $options = [],
      $titleBg = $('[name="background"]'),
      $liArr = [];

    $('.form-wrapper_item, .form-wrapper_selected').html('');

    $liArr = dataArr.map(function(c) {
      if (c['isSelected']) {
        noteConfig.addItem(null, c);
      }
      return [
        '<li>',
        '<input type="checkbox" class="check-item"', (c['isSelected'] ? 'checked' : ''), ' id="', c['code'], '">',
        '<label for="', c['code'], '">', c['name'], '</label>',
        '</li>'
      ].join('');
    });

    // 填充总体设置数据
    for (var _name in noteConfig.template) {
      if (noteConfig.template.hasOwnProperty(_name)) {
        $check.find('[name="' + _name + '"]').val(noteConfig.template[_name]);
      }
    }

    // 填充已选模板
    for (var _prop in noteConfig.templateSelected) {
      if (noteConfig.templateSelected.hasOwnProperty(_prop)) {
        if (noteConfig.template['id'] === _prop) {
          $options.push(['<option value="' + _prop + '" selected>', noteConfig.templateSelected[_prop], '</option>'].join(''))
        } else {
          $options.push(['<option value="' + _prop + '">', noteConfig.templateSelected[_prop], '</option>'].join(''))
        }
      }
    }

    if (!noteConfig.templateSelected) {
      $template.parents('li').remove();
    }

    // 标题背景色
    $titleBg.spectrum({
      color: (noteConfig.template ? noteConfig.template['background'] || '#273c4f' : '#273c4f'),
      preferredFormat: "hex"
    });

    $ul.html($liArr.join(''));
    $template.html($options.join(''));
    $('#checkAll')[0].checked = $('.form-wrapper_item').find(':checked').length === noteConfig.allItems.length;
    $ul.find('input[type=checkbox]').chkbox();
  },

  fillEditForm: function(data) {
    var that = noteConfig,
      showProp,
      styleObj = that.editData['showStyle'],
      dataTypeObj = that.editData['editData'],
      typeProp,
      styleArr = [],
      editTypeArr = [],
      subItem = data['items'],
      subItemArr = [],
      $bg = $('[name=backgroundColor]');

    that.editingObj = data;

    log(data);

    if (!data) {
      $('.form-wrapper')[0].reset();
      $('[name="columnNo"]').removeAttr('readOnly');
    } else {
      $('[name="name"], [name="code"]').attr('readOnly', true);
    }

    //填充templateId数据
    $("#templateId").val(data.templateId);

    // 填充显示类型
    for (showProp in styleObj) {
      if (styleObj.hasOwnProperty(showProp)) {
        if (data && data['showType'] === showProp) {
          styleArr.push(['<option selected value="', showProp, '">', styleObj[showProp], '</option>'].join(''))
        } else {
          styleArr.push(['<option value="', showProp, '">', styleObj[showProp], '</option>'].join(''))
        }
      }
    }

    $('[name="showType"]').html(styleArr.join(''));

    // 填充选择数据项

    for (typeProp in dataTypeObj) {
      if (dataTypeObj.hasOwnProperty(typeProp)) {
        editTypeArr.push([
          '<li>',
          '<input type="checkbox" id="', typeProp, '" name="dataType">',
          '<label for="', typeProp, '">',
          dataTypeObj[typeProp],
          '</label>',
          '<div style="margin-left: 12px;">',
          '<input disabled id="isMulti-', typeProp, '" type="checkbox">',
          '<label for="isMulti-', typeProp, '">多选</label>',
          '</div>',
          '</li>'
        ].join(''))
      }
    }

    editTypeArr.push([
      '<li>',
      '<input type="checkbox" id="customText" name="dataType">',
      '<label for="customText">普通文本</label>',
      '<div style="margin-left: 12px;">',
      '</div>',
      '</li>'
    ].join('\n'));
    $('.data-select').html(editTypeArr.join(''));

    if (data) {
      data['showType'] === 'tableScroll' ? $('.data-select').parents('li').show() : $('.data-select').parents('li').hide();
      if (data['showType'] === 'tableScroll') {
        data['nurseWhiteBoardEditTypes'].forEach(function(c) {
          $('#' + c['code']).attr('checked', 'checked');

          if (c['code'] === 'item') {
            $('#subItem').parents('li').show();
          } else {
            $('#subItem').parents('li').hide();
          }

          // 下拉框多选
          if (c['isMulti']) {
            $('#isMulti-' + c['code']).removeAttr('disabled').attr('checked', 'checked')
          }

          if (c['type'] === 'text') {
            $('#customText').attr('checked', 'checked');
          }
        })
      } else {
        $('#subItem').parents('li').hide();
      }
    } else {
      $('#subItem').parents('li').hide();
    }

    // 填充子项目
    //if (subItem) {
    subItem.forEach(function(c) {
      subItemArr.push([
        '<input type="checkbox" checked id="', c['code'], '" name="items">',
        '<label for="', c['code'], '">', c['name'], '</label>'
      ].join('\n'))
    });
    //} else {
    if ($('.add-box').length === 0) {
      $('#subItem').parent().append([
        '<div class="add-box">',
        '<input type="text" id="code-sub-item" placeholder="元数据">',
        '<input type="text" id="name-sub-item" class="input-sm" placeholder="名称">',
        '<a href="javascript:;" class="btn btn-add-sub">添加</a>',
        '</div>'
      ].join('\n'));
    }
    //}


    $('#subItem').html(subItemArr.join(''));

    //  填充其它数据
    for (var prop in data) {
      if (data.hasOwnProperty(prop)) {
        // 文本类型
        if (typeof data[prop] === 'string' || typeof data[prop] === 'number') {
          $('[name="' + prop + '"]').val(data[prop]);
        }
        // 布尔型/单选
        if (typeof data[prop] === 'boolean') {
          if ($('[name="' + prop + '"]').length > 0) {
            $('[name="' + prop + '"]')[0].checked = data[prop];
          }
        }
      }
    }

    if (!('backgroundColor' in data)) {
      $bg.val('#eaf2ff');
    }

    $bg.spectrum({
      color: $bg.val(),
      preferredFormat: "hex"
    });

    // 必填项验证
    $('[name="name"]').validatebox({
      required: true
    });

    $('[name="code"]').validatebox({
      required: true
    });

    $('[name="rowNo"]').validatebox({
      required: true,
      validType: 'number'
    });

    $('[name="width"]').validatebox({
      required: true,
      validType: 'number'
    });
    $('[name="titleWidth"]').validatebox({
      required: true,
      validType: 'number'
    });

    $('[name="height"]').validatebox({
      required: true,
      validType: 'number'
    });
  },

  editOpen: function(context) {
    var $this = $(context),
      that = noteConfig,
      editIndex = $this.parents('tr').attr('datagrid-row-index'),
      $dialog = $('.edit-dialog'),
      editData;

    that.editIndex = editIndex;

    editData = that.metaData[editIndex];

    $dialog.dialog('open');
    //$('.iframe-dialog').show();
    noteConfig.fillEditForm(editData);
  },

  saveItems: function() {
    var that = noteConfig,
      paramArr = [],
      prop,
      deptCode = localStorage.getItem('deptCode'),
      globalSetObj = {
        deptCode: deptCode,
        rowCount: $('[name="rowCount"]').val(),
        title: $('[name="title"]').val(),
        //id: $('[name="template"]:selected').val(),
        titleFts: $('[name="titleFts"]').val() == "" ? null : $('[name="titleFts"]').val()
      };

    if ($('[name="template"]').length > 0) {
      globalSetObj.id = $('[name="template"] option:selected').val() || null;
    }

    for (prop in that.checkItemObj) {
      if (that.checkItemObj.hasOwnProperty(prop)) {
        paramArr.push({
          deptCode: deptCode,
          code: prop,
          name: that.checkItemObj[prop]
        })
      }
    }

    $.post(ay.contextPath + '/nur/nurseWhiteBoard/batchSaveNwbMetadatas', {
      template: JSON.stringify(globalSetObj),
      nwbMetadatas: JSON.stringify(paramArr)
    }).done(function(res) {
      log(res);
      $.messager.alert('提示', res['msg']);
      if (res['rslt'] === '0') {
        that.refresh(res['data']['data']);

        // 更新项目
        log(that.checkItemObj);
        that.allItems.forEach(function(c) {
          if (that.checkItemObj[c['code']]) {
            c['isSelected'] = true;
          } else {
            c['isSelected'] = false;
          }
        });

        that.template = globalSetObj;

        if ($('[name="template"]').length > 0 && globalSetObj['id']) {
          that.templateSelected[globalSetObj['id']] = $('[name="template"] option:selected').text();
        }

        log(that.checkItemArr);
      }
    }).fail(function(err) {
      $.messager.alert('警告', '服务器开小差了:(');
    })
  },

  save: function() {
    var that = noteConfig,
      currentObj = that.editingObj,
      //            isAddNew = $('#isAddNew')[0].checked,
      isAddNew = false,
      paramObj = ay.serializeObject($('.form-wrapper')),
      url = that.isUpdate ? '/nur/nurseWhiteBoard/updateNwbMetadata' : '/nur/nurseWhiteBoard/saveNwbMetadata';

    url = that.isUpdate ? url : url + '?isAddRow=' + isAddNew;

    paramObj['deptCode'] = localStorage.getItem('deptCode');
    paramObj['fontSize'] = 0;
    paramObj['items'] = $('#subItem input:checked').toArray().map(function(c, i) {
      var $c = $(c);

      return {
        deptCode: localStorage.getItem('deptCode'),
        code: $c.attr('id'),
        name: $c.next().text(),
        parentId: paramObj['code']
      }
    });
    paramObj['id'] = currentObj && currentObj.id ? currentObj.id : null;
    paramObj['isBedCode'] = $('[name="isBedCode"]')[0].checked;
    paramObj['isColspan'] = $('[name="isColspan"]')[0].checked;
    paramObj['isEdit'] = $('[name="isEdit"]')[0].checked;
    paramObj['isShowTitle'] = $('[name="isShowTitle"]')[0].checked;
    paramObj['isShowData'] = $('[name="isShowData"]')[0].checked;
    paramObj['isDosage'] = $('[name="isDosage"]')[0].checked;
    paramObj['isShowLineR'] = $('[name="isShowLineR"]')[0].checked;
    paramObj['isShowLineB'] = $('[name="isShowLineB"]')[0].checked;
    paramObj['isShowLineT'] = $('[name="isShowLineT"]')[0].checked;
    paramObj['width'] = paramObj['width'] === '' ? 0 : parseInt(paramObj['width']);
    paramObj['titleWidth'] = paramObj['titleWidth'] === '' ? 0 : parseInt(paramObj['titleWidth']);
    paramObj['templateId'] = $("#templateId").val() === '' ? null : $("#templateId").val();
    //paramObj['height'] = paramObj['height'] === '' ? 0 : parseInt(paramObj['height']);

    if (paramObj.showType !== 'tableScroll') {
      delete paramObj.dataType;
      paramObj.nurseWhiteBoardEditTypes = [{
        id: currentObj && currentObj.nurseWhiteBoardEditTypes ? currentObj.nurseWhiteBoardEditTypes[0].id : null,
        isBedCode: $('[name="isBedCode"]')[0].checked,
        isMulti: paramObj.showType !== 'singleText',
        code: (paramObj.showType === 'listScroll' ? 'patient' : ''),
        isInner: false,
        templateId: $("#templateId").val() === '' ? null : $("#templateId").val(),
        type: (function() {
          switch (paramObj.showType) {
            case 'listScroll':
              return 'dropbox';
              break;

            case 'singleText':
            case 'multiText':
              return 'text';
              break;
          }
        })()
      }]
    } else {
      var nwbEditTypes = [];
      $('[name="dataType"]').each(function(i, c) {
        var code = $(c).attr('id');
        if (c.checked) {
          nwbEditTypes.push({
            code: code,
            id: currentObj.nurseWhiteBoardEditTypes[i] ? currentObj.nurseWhiteBoardEditTypes[i].id : null,
            isInner: true,
            isBedCode: $('[name="isBedCode"]')[0].checked,
            isMulti: code === 'customText' ? true : $('#isMulti-' + code)[0].checked,
            templateId: $("#templateId").val() === '' ? null : $("#templateId").val(),
            type: code === 'customText' ? 'text' : 'dropbox'
          });
        }
      });
      paramObj.nurseWhiteBoardEditTypes = nwbEditTypes;
    }

    log(ay.serializeObject($('.form-wrapper')), paramObj);

    // 提交前数据处理
    //return ;
    $.post(ay.contextPath + url, {
      nwbMetadata: JSON.stringify(paramObj)
    }).done(function(res) {
      log(res);
      if (res['rslt'] === '0') {
        $.messager.alert('提示', res['msg']);
        that.refresh(res['data']['data']);
      } else {
        $.messager.alert('提示', res['msg']);
      }
    }).fail(function(err) {
      $.messager.alert('警告', '服务器异常:(');
    })
  },

  preview: function() {
    var dWidth = $(document).width(),
      dHeight = $(document).height(),
      pIframe = $('#preview-iframe');

    pIframe[0].src = 'noteShow.do';
    pIframe.width(dWidth);
    pIframe.height(dHeight);

    /*$('.iframe-dialog').dialog({
     title: '预览',
     width: $(document).width() - 30,
     height: $(document).height(),
     onClose: function () {
     $('.iframe-dialog').hide()
     }
     });*/
    $('.iframe-dialog').show();
  },

  refresh: function(data) {
    var nData = this.transformData(data);
    noteConfig.metaData = data;
    $table.datagrid('loadData', nData);
  },

  deleteItem: function(context) {
    var that = noteConfig,
      editIndex = $(context).parents('tr').attr('datagrid-row-index');

    $.messager.confirm('警告', '确定移除这个项目吗?', function(r) {
      if (r) {
        $.get(ay.contextPath + '/nur/nurseWhiteBoard/deleteNwbMetadataById', {
          deptCode: localStorage.getItem('deptCode'),
          id: that.metaData[editIndex]['id']
        }).done(function(res) {
          $.messager.alert('提示', res['msg']);
          if (res['rslt'] === '0') {
            if (res['data']) {
              that.refresh(res['data']['data']);
            }
          }
        }).fail(function(err) {
          $.messager.alert('警告', '服务器开小差了:(');
        })
      }
    });
  },

  addSubItem: function() {
    var $code = $('#code-sub-item'),
      $name = $('#name-sub-item'),
      code = $code.val(),
      name = $name.val(),
      $subItem = $('#subItem'),
      subItemArr = noteConfig.subItemArr,
      eIndex,
      htmlStr = ['<input type="checkbox" checked id="', $code.val(), '" value="', name, '">', '<label style="margin-right: 6px;" for="', code, '">', name, '</label>'];

    eIndex = $.inArray(code, subItemArr);

    if (eIndex < 0 && code !== '' && name !== '') {
      subItemArr.push(code);
      $subItem.append(htmlStr.join(''));

      $code.val('');
      $name.val('');
    }
  },

  addItem: function(_this, dataObj) {
    var $this,
      $id,
      selectedText,
      $right = $('.selecte-wrapper_right > ul'),
      $li;

    if (_this) {
      $this = $(_this);
      $id = $this.attr('id');
      selectedText = $this.closest('li').text();
      $li = $('<li data-id="' + $id + '" data-value="' + $id + '"><span class="s-label">' + selectedText + '<span class="delete-arrow">×</span></span></li>');

      if (!($id in noteConfig.checkItemObj)) {
        noteConfig.checkItemObj[$id] = selectedText;
        $li.appendTo($right);
      }
      if (!_this.checked) {
        noteConfig.removeItem($right.find('[data-id="' + $id + '"]')[0], null);
      }
    } else if (dataObj) {
      $id = dataObj['code'];
      selectedText = dataObj['name'];
      $li = $('<li data-id="' + $id + '" data-value="' + $id + '"><span class="s-label">' + selectedText + '<span class="delete-arrow">×</span></span></li>');
      noteConfig.checkItemObj[$id] = selectedText;

      $li.appendTo($right);
    }
  },


  removeItem: function(_this, dataObj) {
    var $li = _this.tagName === 'LI' ? $(_this) : $(_this).parents('li'),
      $id = $li.data('id');

    if (dataObj) {
      $id = dataObj['code'];
      $li = $('.form-wrapper_selected').find('li[data-id="' + $id + '"]');
    }

    $('#' + $id)[0].checked = false;
    delete noteConfig.checkItemObj[$id];
    $li.remove();

    if (_this) {
      if (noteConfig.checkAllObj) {
        noteConfig.checkAllObj.subFunc();
      }
    }
  }
};


$(function() {
  $('.form-list').find('input[type=checkbox]').chkbox();
  $('.selecte-wrapper_left').find('input[type=checkbox]').chkbox();
  noteConfig.init(function(data) {
    $table.datagrid({
      data: data,

      iconCls: 'icon-edit',
      singleSelect: true,
      toolbar: [{
        text: '添加项目',
        iconCls: 'icon-add',
        handler: function() {
          noteConfig.isUpdate = false;
          noteConfig.fillAllItems();
          $('.add-item-dialog').dialog('open');
        }
      }, '-', {
        text: '预览',
        iconCls: 'icon-save',
        handler: noteConfig.preview
      }],
      //rownumbers: true,

      columns: [
        [{
          field: 'id',
          title: 'ID',
          align: 'center'
        }, {
          field: 'deptCode',
          title: '部门编号',
          align: 'center',
          width: 60
        }, {
          field: 'code',
          title: '项目编号',
          align: 'center'
        }, {
          field: 'name',
          title: '项目名称',
          align: 'center',
          width: 100
        }, {
          field: 'rowNo',
          title: '行号',
          align: 'center',
          width: 30
        }, {
          field: 'columnNo',
          title: '列号',
          align: 'center',
          width: 30
        }, {
          field: 'includeRow',
          title: '所占行数',
          align: 'center'
        }, {
          field: 'width',
          title: '宽度',
          align: 'center',
          width: 30
        }, {
          field: 'titleWidth',
          title: '标题宽度',
          hidden: true,
          align: 'center',
          width: 30
        }, {
          field: 'isColspan',
          title: '整列显示',
          align: 'center'
        }, {
          field: 'isShowTitle',
          title: '显示标题',
          align: 'center',
          width: 60
        }, {
          field: 'isShowData',
          title: '显示数据',
          align: 'center',
          width: 60
        }, {
          field: 'isShowLineR',
          title: '右分割线',
          align: 'center',
          width: 55
        }, {
          field: 'isShowLineB',
          title: '下分割线',
          align: 'center',
          width: 55
        }, {
          field: 'isShowLineT',
          title: '标题与内容分割线',
          align: 'center',
          width: 100
        }, {
          field: 'showType',
          title: '显示类型',
          align: 'center',
          width: 60
        }, {
          field: 'isEdit',
          title: '是否可编辑',
          align: 'center'
        }, {
          field: 'isBedCode',
          title: '仅显示床位',
          align: 'center'
        }, {
          field: 'isInner',
          title: '含多个子项目',
          align: 'center'
        }, {
          field: 'backgroundColor',
          title: '背景色',
          align: 'center',
          width: 40,
          styler: function(value, row, index) {
            var bgText = value;

            if (!value || value === '') {
              bgText = '#eaf2ff';
            }

            return 'background-color:' + bgText + ';';
          },
          formatter: function(value, row, index) {
            //if (value === '' || !value) {
            return '';
            //}
          }
        }, {
          field: 'operate',
          title: '操作',
          align: 'left',
          width: 120
        }, {
          field: 'templateId',
          title: '模板id',
          hidden: true
        }]
      ],
      onClickRow: function() {
          noteConfig.isUpdate = true;
        }
        /*onEndEdit: noteConfig.onEndEdit*/
    });

  });

  $('.close-iframe').on('click', function(e) {
    $('.iframe-dialog').hide();
    $('#preview-iframe').removeAttr('src');
  });

  $('#default-color').on('click', function(e) {
    var $bg = $('[name="backgroundColor"]');

    $bg.val('#eaf2ff');

    $bg.spectrum({
      color: '#eaf2ff',
      preferredFormat: "hex"
    });
  });

  $('#default-color-title').on('click', function(e) {
    var $bg = $('[name="background"]');

    $bg.val('#273c4f');

    $bg.spectrum({
      color: '#273c4f',
      preferredFormat: "hex"
    });
  })

});
