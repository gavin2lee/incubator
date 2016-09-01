var log = console.log.bind(console);

(function($) {
  $.fn.initCombo = function(isTemp) {
    var modalWidth = $('.form-list').width(),
      titleWidth = $('.label-span').eq(0).width() + 30,
      deleteBtnWidth = $('.remove-this').eq(0).outerWidth(),
      outerInputWidth = modalWidth / 2 - titleWidth;

    this.each(function(i, v) {
      var $v = $(v),
        type = $v.attr('comboname') || $v.attr('name'),
        innerData = note.temData[$v.data('dataid')],
        isOnlyBedCode = $v.data('isbed'),
        isReadonly = $v.data('readonly'),
        code,
        itemData,
        data = isTemp ?
        (innerData || (note.internalData ? note.internalData[type] : null)) :
        (note.internalData ? note.internalData[type] : null),

        options = {
          data: data,
          valueField: 'id',
          multiple: JSON.parse($v.data('multi')),
          multiline: true,
          textField: 'text',
          panelHeight: 'auto',
          height: 30,
          editable: false,
          readonly: isReadonly
        };

      if (!data) {
        throw 'note对象未初始化!';
      }

      if (!type) {
        throw '没有type';
      }

      if (type === 'freq') {
        options.textField = 'id';
      }

      if (type === 'item') {
        code = $v.data('code');
        options.data = [];
        itemData = $.grep(note.internalData['item'], function(n, i) {
          return n['id'] === code;
        });

        if (itemData.length > 0) {
          for (var prop in itemData[0]['text']) {
            if (itemData[0]['text'].hasOwnProperty(prop)) {
              options.data.push({
                id: prop,
                text: itemData[0]['text'][prop]
              })
            }
          }
        }
      }

      if (type === 'patient') {
        if (isOnlyBedCode) {
          options.data = note.temData['onlyBedCod'];
        }

        if (!$v.data('inner')) {
          options.width = outerInputWidth;
        } else {
          options.width = (outerInputWidth - deleteBtnWidth) / 3;
        }
      } else {
        options.width = (outerInputWidth - deleteBtnWidth) / 3;
      }

      $v.combobox(options);
    })
  }
})(jQuery);

var note = {
  params: [],
  internalData: {
    deptCode: localStorage.getItem('deptCode'),
    nurseCode: localStorage.getItem('nurseCode'),
    nurseName: localStorage.getItem('nurseName'),
    subBodySign: [],
    patient: [],
    freq: []
  },
  metaData: null,
  recordData: null,
  freq: null,
  temp: {},
  temData: {},
  addElemTemp: {},
  nameForCode: {},

  init: function(callback) {
    var that = note;
    this.getTemplateData();
  },

  getTemplateData: function(callback) {
    var that = this;

    /*
     * 获取数据新接口：
     url: /nur/nurseWhiteBoard/getNurseWhiteBoardDatas
     入参：String deptCode, String code
     * */
    $.get(ay.contextPath + '/nur/nurseWhiteBoard/getNurseWhiteBoardDatas', {
      deptCode: localStorage.getItem('deptCode'),
      isCache: true
    }).done(function(res) {
      log(res);
      if (res['rslt'] === '0') {
        that.metaData = res.data['metadatas'];
        that.recordData = res.data['records'];
        that.freq = res.data.freq;

        that.parseEditTemplate(function() {
          // 重置只显示床位数据
          //window.setTimeout(function () {
          var newArr;
          if (that.temData['onlyBedCod']) {
            newArr = that.temData['onlyBedCod'].map(function(bedItem) {
              return {
                id: bedItem['id'],
                text: bedItem['text'].indexOf('-') > -1 ? bedItem['text'].split('-')[0] : bedItem['text']
              }
            });

            that.temData['onlyBedCod'] = newArr;

            // 回调函数 初始化combobox
            //$('[class*="combo"]').initCombo(true, true);
          }
          $('[class*="combo"]').initCombo(true, true);
          //}, 500);
        });
      } else {
        $.messager.alert('提示', '服务器错误!');
      }
    }).fail(function() {
      $.messager.alert('提示', '请稍候，管理员正在处理...');
    });
  },

  parseEditTemplate: function(callback) {
    var that = note;

    if (that.internalData.patient.length === 0) {
      $.get(ay.contextPath + '/nur/nurseWhiteBoard/getBaseDataByDeptCode.do', {
        deptCode: localStorage.getItem('deptCode')
      }).done(function(res) {
        var data = res['data'],
          prop,
          subProp,
          thirdProp;

        for (prop in data) {
          if (data.hasOwnProperty(prop)) {
            for (subProp in data[prop]) {
              if (data[prop].hasOwnProperty(subProp)) {
                if (prop === 'patient') {
                  if (!that.internalData[prop]) {
                    that.internalData[prop] = [];
                  }
                  if ("" != data[prop][subProp]['bedCode']) {
                    // key床号,value姓名/数组
                    that.nameForCode[data[prop][subProp]['patId']] = data[prop][subProp]['name'];

                    that.internalData[prop].push({
                      id: data[prop][subProp]['patId'],
                      text: data[prop][subProp]['bedCode'] + '-' + data[prop][subProp]['name']
                    });
                  }
                } else if (prop === 'freq') {
                  for (thirdProp in data[prop][subProp]) {
                    if (data[prop][subProp].hasOwnProperty(thirdProp)) {
                      if (!that.internalData[subProp]) {
                        that.internalData[subProp] = [];
                      }
                      that.internalData[subProp].push({
                        id: thirdProp,
                        text: data[prop][subProp][thirdProp]
                      });
                    }
                  }
                } else {
                  if (!that.internalData[prop]) {
                    that.internalData[prop] = [];
                  }
                  that.internalData[prop].push({
                    id: subProp,
                    text: data[prop][subProp]
                  });
                }
              }
            }
          }
        }

        log(that.internalData);
        that.fillEditTemplate(callback);
      });
    }
  },

  // 填充编辑模板数据
  fillEditTemplate: function(callback) {
    var that = this,
      record = that.recordData,
      templateData = that.metaData,
      freqData = that.internalData['freq'],
      bloodFreq = that.internalData['bloodGluFreq'],
      $form = $('.form-wrapper'),
      lisArr = [],
      $ul = $('.form-list'),
      modalWidth = $('.form-list').width(),
      titleWidth = $('.label-span').eq(0).outerWidth() + 120,
      deleteBtnWidth = $('.remove-this').eq(0).outerWidth(),
      outerInputWidth = modalWidth / 2 - titleWidth;


    $ul.html('');
    $.each(templateData, function(index, metaItem) {
      var inputsArr = metaItem['nurseWhiteBoardEditTypes'],
        rowNo = metaItem['rowNo'],
        showType = metaItem['showType'],
        isColSpan = metaItem['isColspan'],
        code = metaItem['code'],
        columnType = Number(metaItem['columnType']),
        columnNo = Number(metaItem['columnNo']),
        showName = metaItem['name'],
        isShowData = metaItem['isShowData'],
        recordItem,
        isOnlyBedCode = metaItem['isBedCode'],
        isEdit = metaItem['isEdit'] ? '' : 'readonly',
        isReadonly = metaItem['isEdit'] ? false : true,
        itemInfos,
        onlyBedCodeArr;
      //prop = 'li-' + index;

      // 查找code对应结果数据
      recordItem = that.findRecordItem(code);
      itemInfos = recordItem && recordItem['nurseWhiteBoardRecordFreqInfos'] || [];

      // 例如: 日班护士下有A1班, 日班护士不显示
      //if (!isShowData || (showType === 'multiText' && !isColSpan)) return true;

      lisArr[index] = [];
      lisArr[index].push([
        '<li>',
        '<span class="label-span" data-recordcode="' + code + '" data-recordname="' + showName + '">',
        showName,
        '：',
        '</span>',
        '<div class="input-div" style="width: ' + outerInputWidth + 'px">',
        '<ul>',
        (function() {
          var initialDom = inputsArr.map(function(inputItem, idx) {
            var inputType = inputItem['type'],
              itemCode = inputItem['code'],
              //itemName = inputItem['name'] || '哈哈',
              isMulti = inputItem['isMulti'],
              isInner = inputItem['isInner'],
              // 唯一索引
              timestamp = Math.floor(Math.random() * 10e16);

            switch (inputType) {
              case 'dropbox':
                log(recordItem);
                return '<input data-code="' + code + '" data-dataid="id' + timestamp + '" name="' + itemCode + '" data-readonly="' + isReadonly + '" data-isbed="' + isOnlyBedCode + '" data-multi="' + isMulti + '" data-inner="' + isInner + '" class="combobox-' + itemCode + '"/>';
                break;

              case 'text':
                if (isMulti) {
                  return '<textarea name="content"  ' + isEdit + ' rows="4" name="content" placeholder="' + showName + '"></textarea>';
                } else {
                  return '<input ' + isEdit + ' type="text" name="content" value="" placeholder="' + showName + '"/>'
                }
                break;

              default:
                break;
            }
          });

          if (showType === 'tableScroll') {
            initialDom.push('<a href="javascript:;" class="remove-this">删除</a>');
          }

          initialDom.join('\n');

          // 缓存添加条目类型的DOM数据
          that.addElemTemp[code] = $.extend([], initialDom);

          if (itemInfos.length > 0) {
            return itemInfos.map(function(itemInfo) {
              return [
                '<li>',
                (inputsArr.map(function(inputItem, idx) {
                  var inputType = inputItem['type'],
                    itemCode = inputItem['code'],
                    //itemName = inputItem['name'] || '哈哈',
                    isMulti = inputItem['isMulti'],
                    isInner = inputItem['isInner'],
                    //isOnlyBedCode = inputItem['isBedCode'],
                    patientArr,
                    freqArr,
                    bodySignArr,
                    // 唯一索引
                    timestamp = Math.floor(Math.random() * 10e16);

                  //缓存结果
                  that.temp = that.clone.call(that.internalData);

                  if (isOnlyBedCode) {
                    if (!that.temData['onlyBedCod']) {
                      that.temData['onlyBedCod'] = $.extend([], that.temp['patient']);
                      $.each(that.temData['onlyBedCod'], function(idx, val) {
                        if (isOnlyBedCode) val.text = setBedCode(val.text.split('-')[0]);
                      });

                    }
                  }

                  switch (inputType) {
                    case 'dropbox':
                      log(recordItem);

                      //that.temp = $.extend({}, that.internalData);
                      if (that.temp[itemCode]) {
                        // 病人需要单独提取
                        if (itemCode === 'patient') {
                          patientArr = itemInfo['nurseWhiteBoardRecordFreqPatInfos'].map(function(c) {
                            if (c['patInfo']) {
                              return (c['patInfo'].indexOf('-') > 0 ? c['patInfo'].split('-')[0].replace('床', '') : (c['patInfo'].replace('床', '')));
                            }
                          });

                          $.each(that.temp['patient'], function(idx, val) {
                            if ($.inArray(val['text'].split('-')[0].replace('床', ''), patientArr) > -1) {
                              val.selected = true;
                            }

                            if (isOnlyBedCode) val.text = setBedCode(val.text.split('-')[0]);
                          });
                        }

                        if (itemCode === 'freq' || itemCode === 'bloodGluFreq') {
                          freqArr = itemInfo['performSchedule'];

                          if (freqArr) {
                            $.each(that.temp[itemCode], function(idx3, val3) {
                              if (val3['id'] === freqArr) {
                                val3.selected = true;
                              }
                            });
                          }
                        }

                        if (itemCode === 'item') {
                          bodySignArr = itemInfo['nurseWhiteBoardRecordFreqItems'].map(function(c) {
                            return c['recordItemCode'];
                          });

                          if (bodySignArr) {
                            $.each(that.temp[itemCode], function(idx3, val3) {
                              if ($.inArray(val3['id'], bodySignArr) > -1) {
                                val3.selected = true;
                              }
                            });
                          }
                        }
                      }

                      that['temData']['id' + timestamp] = $.extend([], that.temp[itemCode]);

                      return '<input data-dataid="id' + timestamp + '" name="' + itemCode + '" data-readonly="' + isReadonly + '" data-multi="' + isMulti + '" data-inner="' + isInner + '" class="combobox-' + itemCode + '"/>';
                      break;

                    case 'text':
                      if (isMulti) {
                        if (isColSpan) {
                          return '<textarea class="input-colspan" ' + isEdit + ' rows="4" name="content" placeholder="' + showName + '">' + itemInfo['recordValue'] + '</textarea>';
                        }
                        return '<textarea name="content" ' + isEdit + '  placeholder="' + showName + '">' + itemInfo['recordValue'] + '</textarea>';
                      } else {
                        return '<input ' + isEdit + ' data-edit="' + metaItem['isEdit'] + '" type="text" name="content" value="' + itemInfo['recordValue'] + '" placeholder="' + showName + '"/>'
                      }
                      break;

                    default:
                      break;
                  }
                }).join('\n')),
                (function() {
                  if (showType === 'tableScroll') {
                    return '<a href="javascript:;" class="remove-this">删除</a>';
                  } else {
                    return '';
                  }
                })(),
                '</li>'
              ].join('');
            }).join('\n');
          } else {
            initialDom.unshift('<li>');
            initialDom.push('</li>');

            if (isOnlyBedCode) {
              if (!that.temData['onlyBedCod']) {
                that.temData['onlyBedCod'] = $.extend([], that.temp['patient']);
                $.each(that.temData['onlyBedCod'], function(idx, val) {
                  if (isOnlyBedCode) val.text = setBedCode(val.text.split('-')[0]);
                });

              }
            }

            return initialDom;
          }
        })(),
        '</ul>',
        // 如果是内置滚动表格,设置"添加"按钮
        (function() {
          if (showType === 'tableScroll') {
            return '<a href="javascript:;" data-code="' + code + '" class="addBtn add-' + code + '-item">添加</a>';
          } else {
            return '';
          }
        })(),
        '</div>',
        '</li>'
      ]);
    });
    console.log(lisArr);
    $ul.append(lisArr.join('').replace(/[,]/g, ''));
    if (callback) callback();
  },

  addItem: function(code, context) {
    var ul = $(context).siblings('ul'),
      removeBtn = $(context).siblings('.remove-item'),
      domData = note.addElemTemp || null,
      timestamp = new Date().getTime(),
      itemData = ['<li class="li_' + timestamp + '">'];

    if (!domData) throw '缓存的DOM字符串不存在!';

    itemData.push(domData[code].join('\n'), '</li>');

    ul.append(itemData.join('\n'));

    $('.li_' + timestamp).find('input[class*="combo"]').each(function(i, v) {
      $(v).initCombo(false);
    });
  },

  save: function() {
    $.loading();
    var that = note,
      timeNow = new Date().format('yyyy-MM-dd hh:mm:ss'),
      item,
      tdFormItem = $('.input-div');

    that.params = [];

    $.each(tdFormItem, function(i, v) {
      var $v = $(v),
        elem = $v.find('li').length === 0 ? ($v.find('[class*="combobox"]').length === 0 ? $v.find('[name]') : $v.find('[class*="combobox"]')) : $v.find('li'),
        itemValue = {},
        tdLabel = $v.prev('.label-span'),
        formItem = tdLabel.data('recordcode'),
        liTemp = {};

      item = {
        deptCode: that.internalData.deptCode,
        itemList: [],
        recordCode: formItem,
        recordDate: timeNow,
        recordName: tdLabel.data('recordname'),
        recordNurseName: that.internalData.nurseName,
        recordNurseCode: that.internalData.nurseCode
      };

      //只有一个条目
      if ($v.find('li').length === 0) {

        if (elem.length === 0) return true;

        itemValue = {
          itemCode: $(elem[0]).attr('comboname'),
          //itemName: $(elem[0]).data('itemname'),
          itemValues: []
        };

        var isCombo = elem.hasClass('combobox-f') > 0,
          textArr = isCombo ? elem.combobox('getText').split(',') : [];

        if (isCombo && elem.combobox('getValues').length !== 0) {
          itemValue.itemValues.push(elem.combobox('getValues').map(function(c, i) {
            return [c, textArr[i]].join(',');
          }).join('|'));
        } else {
          elem.each(function(idx, curr) {
            var $curr = $(curr),
              sValue = $curr.val();

            if (sValue === '') return true;
            itemValue.itemValues.push(sValue);
          });
        }

        item.itemList.push(itemValue);
      } else { // 可添加多个条目

        // 过滤只读表单元素
        if (typeof elem.find('input').data('edit') !== 'undefined' && !elem.find('input').data('edit')) return true;

        elem.each(function(idx, curr) {
          // 如果有文本域
          var contentInput = $(curr).find('[name="content"]');
          if (contentInput.length) {
            if ('content' in liTemp) {
              liTemp.content.push(contentInput.val());
            } else {
              liTemp.content = [];
              liTemp.content.push(contentInput.val());
            }
          }

          var formElem = $(curr).find('[class*="combobox"]');

          formElem.each(function(i1, v1) {
            var formName = $(v1).attr('comboname'),
              vValue = $(v1).combobox('getValues'),
              vText = $(v1).combobox('getText').split(',');

            if (formName in liTemp) {

              if (formName === 'patient') {
                vValue = vValue.map(function(c, i) {
                  var _name;

                  if (vText[i].split('-').length < 2) {
                    _name = vText[i] + '-' + that.nameForCode[c];
                  } else {
                    _name = vText[i];
                  }

                  return [c, _name].join(',');
                }).join('|');

                liTemp[formName].push(vValue);
              } else {
                liTemp[formName].push(vValue.join('|'));
              }
            } else {
              liTemp[formName] = [];
              if (formName === 'patient') {
                vValue = vValue.map(function(c, i) {
                  var _name;

                  if (vText[i].split('-').length < 2) {
                    _name = vText[i] + '-' + that.nameForCode[c];
                  } else {
                    _name = vText[i];
                  }

                  return [c, _name].join(',');
                }).join('|');

                liTemp[formName].push(vValue);
              } else {
                liTemp[formName].push(vValue.join('|'));
              }
            }
          });
        });

        for (var prop in liTemp) {
          if (liTemp.hasOwnProperty(prop)) {
            item.itemList.push({
              itemCode: prop === 'bloodGluFreq' ? 'freq' : prop,
              itemName: $(elem).find('[comboname="' + prop + '"]').attr('placeholder'),
              itemValues: liTemp[prop]
            });
          }
        }
      }
      that.params.push(item);

      log(that.params);
    });

    $.post(ay.contextPath + '/nur/nurseWhiteBoard/batchSaveNurseWhiteBoardRecord', {
      recordInfo: JSON.stringify(that.params)
    }).done(function(res) {
      $.loading('close');
      log(res);
      if (res['rslt'] === '0') {
        $.messager.alert("提示", res['msg'], 'info', function() {
          window.location.reload();
        });
      } else {
        $.messager.alert("提示", res['msg']);
      }
    });
  },

  // TODO 查找做缓存可优化
  findRecordItem: function(name) {
    var recordData = this.recordData,
      result = null;

    $.each(recordData, function(index, obj) {
      var recordCode = obj['recordCode'];
      if (recordCode === name) {
        result = obj;
      }
    });

    return result;
  },
  // 浅拷贝
  clone: function() {
    var copy = (this instanceof Array) ? [] : {},
      that = note,
      attr;
    for (attr in this) {
      if (!this.hasOwnProperty(attr)) continue;
      copy[attr] = (typeof this[attr] == "object") ? that.clone.call(this[attr]) : this[attr];
    }
    return copy;
  }
};

$(function() {
  note.init();

  $(document).on('click', '.remove-this', function(e) {
    var $this = $(this);

    $this.parent().remove();
  });

  $(document).on('click', '.addBtn', function(e) {
    var $this = $(this),
      code = $this.data('code');

    note.addItem(code, this);
  });
  $('#save').on('click', note.save);
});

function setBedCode(bedCode) {
  if (bedCode.indexOf("床") >= 0) {
    return bedCode;
  }

  return bedCode + '床';
}
