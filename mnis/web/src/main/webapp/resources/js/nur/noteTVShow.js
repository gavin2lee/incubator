/**
 * Created by lin on 16/1/8.
 */
var log = console.log.bind(console);
(function($) {
  $.fn.autoScroll = function(options) {
    var defaultOpt = {
      speed: 100
    };
    this.each(function(i, v) {
      var vHeight = $(v).height(),
        sHeight = v.scrollHeight - 12,
        isDown = true;

      if (sHeight <= vHeight) return true;
      setInterval(function() {
        var offsetTop = v.scrollTop;

        if (vHeight + offsetTop === sHeight) {
          isDown = false;
        }
        if (offsetTop === 0) {
          isDown = true;
        }
        if (isDown) {
          $(v).animate({
            scrollTop: offsetTop + 1
          }, 0);
        } else {
          $(v).animate({
            scrollTop: offsetTop - 1
          }, 0);
        }

      }, ((options && options.speed) || defaultOpt.speed));
    })
  }

  // 初始化combobox
  ;
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
          editable: false
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

  // 自动轮播
  ;
  $.fn.autoSlide = function(options) {
    var that = this,
      defaultOpt = {
        speed: 10000
      },
      opts = options || defaultOpt,
      len = this.length,
      count = 1,
      titleArr = [],
      controllerHtml = '',
      timer;

    if (note.hadSlide) return;

    note.hadSlide = true;

    function timers() {
      timer = setInterval(function() {
        if (count > len - 1) count = 0;
        that.eq(count).fadeIn(1000);
        that.eq(count).siblings().fadeOut(1000);

        $('.slide-ctr a').eq(count).addClass('active').siblings().removeClass('active');
        count++;
      }, opts.speed);
    }

    this.each(function(i, c) {
      titleArr.push($(c).data('title'));
    });

    controllerHtml = titleArr.map(function(c, i, a) {
      if (i === 0) {
        return ['<div class="slide-ctr">', '<a class="active" href="javascript:;" data-index="' + i + '">', c, '</a>'].join('');
      }

      if (i === a.length - 1) {
        return ['<a href="javascript:;" data-index="' + i + '">', c, '</a>', '</div>'].join('');
      }
      return ['<a href="javascript:;" data-index="' + i + '">', c, '</a>'].join('');
    }).join('');

    log(controllerHtml);

    $('.slide-ctr').remove();
    $(document.body).append(controllerHtml);

    timers();

    $('.slide-ctr').off('click', 'a', '**');
    $('.slide-ctr').on('click', 'a', function() {
      var $this = $(this),
        idx = $this.data('index');

      $this.addClass('active').siblings().removeClass('active');

      that.eq(idx).fadeIn(1000);
      that.eq(idx).siblings().fadeOut(1000);

      clearInterval(timer);
      timers();
    })
  }
})(jQuery);

var note = {
  metaData: null,
  recordData: null,
  freq: null,
  today: null,
  temp: {},
  hadSlide: false,
  temData: {},
  addElemTemp: {},
  nameForCode: {},

  internalData: {
    deptCode: $('#deptCode').val(),
    item: [],
    patient: [],
    freq: []
  },


  timers: (function() {
    var whiteBoardRefreshTime = [];
    return {
      slideTime: whiteBoardRefreshTime && whiteBoardRefreshTime[1] * 1000 || 0,
      refreshTime: whiteBoardRefreshTime && whiteBoardRefreshTime[0] * 1000 || 80000
    }
  })(),

  init: function() {
    var that = this;
    this.getTemplateData(that.parseTemplate);
  },

  getTemplateData: function(callback) {
    var that = this;

    /*
     * 获取数据新接口：
     url: /nur/nurseWhiteBoard/getNurseWhiteBoardDatas
     入参：String deptCode, String code
     * */
    $.get(ay.contextPath + '/nur/nurseWhiteBoardTV/getNwbTVDatas', {
      deptCode: $("#deptCode").val()
    }).done(function(res) {
      log(res);
      if (res['rslt'] === '0') {
        that.metaData = res.data['metadatas'];
        that.recordData = res.data['records'];
        that.freq = res.data.freq;
        that.today = res.data.today;

        callback();
      } else {
        /*    $.messager.alert('提示', '服务器错误!');*/
      }
    }).fail(function() {
      //            $.messager.alert('提示', '请稍候，管理员正在处理...');
      log(res);
    });
  },

  parseTemplate: function() {
    var that = note,
      metaData = that.metaData,
      freq = that.freq,
      htmlStr = '',
      rowClassName = 'row',
      colOuterClassName = 'col',
      innerClassName = 'inside-table',
      rowParentArr = [],
      colParentArr = [],
      innerCount = 1,
      windowWidth = $(window).width(),
      pageToday = $("#today").html();

    //重新设置页面日期
    if (pageToday != that.today) {
      $("#today").html(that.today);
    }
    // 白板预览数据
    $.each(metaData, function(index, obj) {
      var showType = obj['showType'],
        rowNo = obj['rowNo'],
        code = obj['code'],
        columnType = Number(obj['columnType']),
        showName = obj['name'] || '',
        subProp = obj['nurseWhiteBoardEditTypes'],
        rowSubObj,
        serverWidth = obj['width'] * windowWidth / 100,
        //serverHeight = obj['height'],
        recordItem,
        innerLabelName = 'singleText',
        patientOnlyBed = obj['isBedCode'],
        // 单列
        isColspan = obj['isColspan'],
        labelBgColor = (obj && obj['backgroundColor']) || '#eaf2ff',
        bgText = 'style="background: ' + labelBgColor + '"';

      // 查找对应code的记录数据
      recordItem = that.findRecordItem(code);

      // 解析模板时把结果数据加上,一次渲染,加快渲染速度.
      switch (showType) {
        case 'singleText':
          // 缓存每行模板数据
          if (!rowParentArr[rowNo]) {
            rowParentArr[rowNo] = ['<table class="' + rowClassName + rowNo + ' normal"><tbody><tr>'];
          }

          rowSubObj = rowParentArr[rowNo];

          if (columnType > 0) {
            innerLabelName = 'innerTd';
            bgText = '';
          }

          htmlStr = ['<td class="' + innerLabelName + '" data-width="' + serverWidth + '" style="width: ' + serverWidth + 'px;">',
            '<span class="text-label" ' + bgText + '>',
            showName,
            '</span>',
            '<div class="content-wrapper">',
            ((recordItem && recordItem['nurseWhiteBoardRecordFreqInfos'][0]['recordValue']) || ''),
            '</div>',
            '</td>'
          ].join('');

          break;

        case 'listScroll':
          // 缓存每行模板数据
          if (!rowParentArr[rowNo]) {
            rowParentArr[rowNo] = ['<table class="' + rowClassName + rowNo + ' normal"><tbody><tr>'];
          }

          rowSubObj = rowParentArr[rowNo];

          htmlStr = ['<td data-width="' + serverWidth + '" style="width: ' + serverWidth + 'px;">',
            '<span class="text-label ' + code + '" style="background: ' + labelBgColor + '">',
            showName,
            '</span>',
            '<div class="content-wrapper auto-scroll">',
            '<ul>',
            (function() {
              if (recordItem) {
                return recordItem['nurseWhiteBoardRecordFreqInfos'].map(function(current) {
                  return current['nurseWhiteBoardRecordFreqPatInfos'].map(function(current2, index2) {
                    var patStr = patientOnlyBed ? current2['patInfo'].split('-')[0].replace('床', '') : current2['patInfo'];
                    return ['<li>', patStr, (index2 === current['nurseWhiteBoardRecordFreqPatInfos'].length - 1 ? '' : '*'), '</li>'].join('');
                  }).join('\n');
                }).join('');
              }
            })(),
            '</ul>',
            '</div>',
            '</td>'
          ].join('');

          break;

        case 'tableScroll':
          // 缓存每行模板数据
          if (!rowParentArr[rowNo]) {
            rowParentArr[rowNo] = ['<div class="' + rowClassName + rowNo + ' tableScroll clearfix">'];
            // 标示内部表格
            rowParentArr[rowNo].isClumn = true;

            innerCount = 1;
          }

          rowSubObj = rowParentArr[rowNo];
          var propArr = [];

          // 根据元数据得到需要显示的数据record
          propArr = subProp.map(function(current) {
            var code = current['code'];
            if (!code) return null;
            switch (code) {
              case 'patient':
                return 'nurseWhiteBoardRecordFreqPatInfos';
                break;

              case 'item':
                return 'nurseWhiteBoardRecordFreqItems';
                break;

              case 'freq':
              case 'bloodGluFreq':
                return 'performSchedule';

              case 'text':
                return 'recordValue';

              default:
                return 'undefinedName';
                break;
            }
          });

          if (!recordItem) {
            htmlStr = '';
            // 嵌套表格组装 : 空表格
            if (columnType > 0) {
              htmlStr = '<div style="width: ' + serverWidth + 'px;" class="inner-table" data-innerno="' + columnType + '"><h2 class="sub-title" style="background: ' + labelBgColor + '">' + showName + '</h2><div class="auto-scroll"><table class="' + innerClassName + '"><tbody></tbody></table></div></div>';
            } else {
              htmlStr = '<div style="width: ' + serverWidth + 'px;" class="inline-table"><h2 class="sub-title">' + showName + '</h2><div class="auto-scroll"><table class="' + innerClassName + '"><tbody></tbody></table></div></div>';
            }

            if (rowSubObj) {
              rowSubObj.push(htmlStr);
            }

            return true;
          }

          htmlStr = recordItem['nurseWhiteBoardRecordFreqInfos'].map(function(item) {
            var p,
              arr = [];
            for (p in item) {
              if (item.hasOwnProperty(p)) {
                switch (p) {
                  case 'nurseWhiteBoardRecordFreqPatInfos':
                    arr.push(['<td>', item[p].map(function(patientItem) {
                      return patientOnlyBed ? patientItem['patInfo'].split('-')[0].replace('床', '') : patientItem['patInfo'];
                    }).join('，'), '</td>'].join(''));
                    break;

                  case 'nurseWhiteBoardRecordFreqItems':
                    arr.push(['<td>', item[p].map(function(patientItem) {
                      return patientItem['recordItemName'];
                    }).join('，'), '</td>'].join(''));
                    break;

                  case 'performSchedule':
                    arr.push(['<td>', that.freq.freq[item[p]] || that.freq['bloodGluFreq'][item[p]], '</td>'].join(''));
                    break;

                  case 'recordValue':
                    arr.push(['<td>', item[p], '</td>'].join(''));
                    break;

                  default:
                    console.info('注意:未知属性!');
                    break;
                }
              }
            }

            return [
              '<tr>',
              arr.join('\n'),
              '</tr>'
            ].join('\n');
          });

          // 嵌套表格组装
          if (columnType > 0) {
            htmlStr.unshift('<div style="width: ' + serverWidth + 'px;" class="inner-table" data-innerno="' + columnType + '"><h2 class="sub-title" style="background: ' + labelBgColor + '">' + showName + '</h2><div class="auto-scroll"><table class="' + innerClassName + '"><tbody>');
          } else {
            htmlStr.unshift('<div style="width: ' + serverWidth + 'px;" class="inline-table"><h2 class="sub-title">' + showName + '</h2><div class="auto-scroll"><table class="' + innerClassName + '"><tbody>');
          }

          innerCount++;

          break;

        case 'multiText':
          // 单列单独处理
          if (isColspan) {
            //colParentArr = colParentArr || [];
            colParentArr.push([
              '<div class="alone fl" style="width: ' + serverWidth + 'px;" data-innerno="' + columnType + '"><h2 class="sub-title" style="background: ' + labelBgColor + '">' + ((recordItem && recordItem['recordName']) || showName) + '</h2><div style="padding: 10px;font-size: 18pt; line-height: 1.8em" class="auto-scroll content-wrapper">',
              ((recordItem && recordItem['nurseWhiteBoardRecordFreqInfos'][0]['recordValue']) || ''),
              '</div>',
              '</div>'
            ].join('\n'));

            break;
          }

          if (!rowParentArr[rowNo]) {
            rowParentArr[rowNo] = ['<table class="' + rowClassName + rowNo + ' normal"><tbody><tr>'];
          }

          rowSubObj = rowParentArr[rowNo];

          htmlStr = ['<td class="multiText">',
            '<span class="text-label">',
            showName,
            '</span>',
            '<div class="content-wrapper">',
            ((recordItem && recordItem['nurseWhiteBoardRecordFreqInfos'][0]['recordValue']) || ''),
            '</div>',
            '</td>'
          ].join('');

          break;
      }

      //htmlTemp.html = htmlStr;

      //rowSubObj.push(htmlTemp);
      if (rowSubObj) {
        rowSubObj.push(htmlStr);
      }

    });

    // 加上结束标签
    that.drawPage(rowParentArr, colParentArr);
  },

  drawPage: function(rowArr, colArr) {
    var that = this,
      i,
      len = rowArr.length,
      htmlArray = ['<div class="rowOnly">'];

    $('#white-board').html('');

    if (!rowArr) throw 'DOM结构对象不存在!';

    for (i = 1; i <= len; i++) {
      if (rowArr[i]) {
        if (typeof rowArr[i].isClumn !== 'undefined' && rowArr[i].isClumn) {
          $.each(rowArr[i], function(index, tableItem) {
            //if (typeof tableItem === 'object') {
            if (Array.isArray(tableItem)) {
              tableItem.push(['</tbody>', '</table></div></div>'].join(''));
              rowArr[i][index] = tableItem.join('');
            }
          });
          // div 添加结束标签
          rowArr[i].push('</div>');
        } else {
          rowArr[i].push(['</tr>', '</tbody>', '</table>'].join(''));
        }

        htmlArray.push(rowArr[i]);
      }
    }

    htmlArray.push('</div>');

    // 加上单独列
    if (colArr && Array.isArray(colArr)) {
      htmlArray.push('<div class="colOnly">');
      htmlArray.push(colArr);
      htmlArray.push('</div>');
    }

    log(htmlArray);

    $('#white-board').append(htmlArray.join('').replace(/[,]/g, '').replace(/[*]/g, ',&nbsp'));

    that.setSize();
  },

  setSize: function() {
    var that = note,
      windowHeight = $(window).height(),
      headerHeight = $('header').height(),
      windowWidth = $(window).width(),
      $noScroll = $('.normal'),
      $tableScroll = $('.tableScroll'),
      // 同一列表格
      $inners = $('.inner-table'),
      // 同一列的表格个数
      innersLen = $('.inner-table[data-innerno="1"]').length,
      $scrollItems = $tableScroll.find('.inline-table'),
      scrollLen = $scrollItems.length,
      normalsTotalHeight = 0,
      scrollWrapperHeight = 0,
      //单列宽度
      $aloneTable = $('.alone'),
      normalLen = $noScroll.length,
      aloneLen = $aloneTable.length,
      normalHeight = (windowHeight - headerHeight) / normalLen,
      aloneWidth = $('.colOnly').length !== 0 ? $('.colOnly').width() : windowWidth / (3 * aloneLen === 0 ? 1 : 3 * aloneLen),
      restWidth = windowWidth - aloneLen * aloneWidth - 1,
      scrollItemWidth = restWidth / (scrollLen + innersLen),
      subTitleHeight = $('.sub-title').eq(0).height(),
      contentWidth,
      cutHeight = 0,
      _32Len = 0;

    //$aloneTable.width(aloneWidth - 1);
    $('.rowOnly').width(restWidth);
    //$('.colOnly').width(aloneLen * aloneWidth);

    // 设置字号大小
    var fontObj = note.freq['font'];

    if (fontObj) {
      $('.w-title').css({
        'font-size': fontObj['titleFont'] + 'pt'
      });

      $('.text-label, .sub-title').css({
        'font-size': fontObj['itemTitleFont'] + 'pt'
      });

      $('.content-wrapper').css({
        'font-size': fontObj['itemContFont'] + 'pt'
      });
    }

    $noScroll.each(function(i, normalItem) {
      var $td = $(normalItem).find('td'),
        tdLen = $td.length,
        reduceWidth = 160,
        reduceNum = 0,
        dynamicReduceWidth = 0,
        dynamicReduceNum = 0,
        normalHeight,
        rowSetWidth = 0,
        innerNum = $(normalItem).find('.innerTd').length,
        multiNum = $(normalItem).find('.multiText').length,
        tdIndexArr = [];

      $td.each(function(tdIndex, tdItem) {
        var $tdItem = $(tdItem),
          $title = $tdItem.find('.text-label'),
          titleWidth = $title.outerWidth(),
          $content = $tdItem.find('.content-wrapper'),
          contentHeight = $content.height(),
          tdWidth = 0,

          $prevTd = $tdItem.prev(),
          prevTdWidth = $prevTd.outerWidth();

        dynamicReduceWidth = titleWidth + 20;

        if ($tdItem.hasClass('innerTd')) {
          reduceWidth = 160;
          $tdItem.width(reduceWidth);
          reduceNum += 1;

        } else if ($tdItem.hasClass('multiText')) {
          $tdItem.css({
            'border-right': 0
              //'border-left': 0
          });
        } else {
          // 计算之前td宽度之和
          var $prevTds = $tdItem.prevAll(),
            prevTotalWidth = 0,
            //nextLen =
            averangeWidth = restWidth / $tdItem.parent().find('td').length;



          /*$prevTds.each(function (i, prevTdItem) {
            var _width = $(prevTdItem).width();
            
            prevTotalWidth += _width;
          });*/

          if ($tdItem.data('width') == 0) {
            tdIndexArr.push({
              className: '.' + $(normalItem)[0].className.split(' ')[0],
              _0tdLen: $(normalItem).find('[data-width="0"][class!="innerTd"]').length,
              _index: tdIndex,
              restWidth: restWidth - rowSetWidth
            });
          } else {
            tdWidth = $tdItem.outerWidth();
            rowSetWidth += tdWidth;

            $tdItem.width(tdWidth < 204 ? 204 : tdWidth);

            contentWidth = $tdItem.outerWidth() - titleWidth - 10;
            $content.width(contentWidth < 80 ? 80 : contentWidth);
          }
          //  prevTdWidth = $prevTd.width();
          //$prevTd.find('.content-wrapper').width(prevTdWidth - titleWidth - 8);
        }

        $tdItem.attr('data-rest-width', restWidth - rowSetWidth);
      });

      // 设置width=0的td的宽度
      tdIndexArr.forEach(function(c) {
        var _tds = $(c['className']).find('td'),
          _lastWidth = _tds.last().data('rest-width'),
          _titleWidth = _tds.eq(c['_index']).find('.text-label').outerWidth(),
          _contentWrapper = _tds.eq(c['_index']).find('.content-wrapper'),
          _currTd = _tds.eq(c['_index']),
          innerTds = _currTd.nextAll('.innerTd'),
          innerLen = innerTds.length,
          innerWidth = innerLen > 0 ? (_lastWidth / c['_0tdLen'] - _titleWidth - 20) / innerLen : 0;

        if (c['_0tdLen'] !== 0) {

          if (innerLen > 0) {
            _contentWrapper.width(0);
            _currTd.width(_titleWidth + 20).css({
              'border-right': 0
            });

            innerTds.width(innerWidth);
          } else {
            _currTd.width(_lastWidth / c['_0tdLen']);
            _contentWrapper.width(_lastWidth / c['_0tdLen'] - _titleWidth - 22);
          }
        }
      });
      // 计算非滚动区域高度
      normalsTotalHeight += $(normalItem).height();

      normalHeight = $(normalItem).height()
      if (normalHeight > 40) {
        cutHeight += normalHeight;
      } else {
        $(normalItem).addClass('_32')
        _32Len += 1;
      }

    });

    scrollWrapperHeight = windowHeight - normalsTotalHeight - headerHeight;

    $scrollItems.width(scrollItemWidth - 1);
    $inners.width(scrollItemWidth - 1);

    $scrollItems.find('.auto-scroll').height(scrollWrapperHeight - subTitleHeight);
    if ($scrollItems.length === 0 || $scrollItems.find('.auto-scroll').length === 0) {
      //$('.tableScroll').height(scrollWrapperHeight - subTitleHeight);
      $('._32').height((windowHeight - headerHeight - cutHeight) / _32Len);
    }
    $inners.find('.auto-scroll').height(Math.ceil(scrollWrapperHeight / (2 * innersLen)) - subTitleHeight);
    $('.colOnly .content-wrapper').height($('.rowOnly').height() - $('.colOnly .sub-title').height() - 22);

    $('#white-board').height(windowHeight - headerHeight);

    // 设置标题垂直居中
    $('.rowOnly').find('.text-label').each(function(i, v) {
      var $this = $(this),
        tdHeight = $this.parents('td').height(),
        titleHeight = $this.height(),
        restHeight = (tdHeight - titleHeight > 0 ? tdHeight - titleHeight : 0),
        $contentWrapper = $this.next(),
        nextHeight = $contentWrapper.height();

      $this.css({
        'padding': restHeight / 2 + 'px 6px'
      });

      if (nextHeight < 40 && nextHeight > 0) {
        $contentWrapper.css({
          'padding': restHeight / 2 + 'px 0px ' + restHeight / 2 + 'px 6px'
        })
      }
    })


    // 自动滚动
    $('.auto-scroll').autoScroll();
    if (note.timers.slideTime !== 0) {
      $('.note > div').autoSlide({
        speed: note.timers.slideTime
      });
    }

    that.refreshTimer = setTimeout(function() {
      note.init();
      //window.location.reload();
      clearTimeout(that.refreshTimer);
    }, note.timers.refreshTime);


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

  parseEditTemplate: function(callback) {
    var that = note;

    if (that.internalData.patient.length === 0) {
      $.get(ay.contextPath + '/nur/nurseWhiteBoardTV/getBaseDataByDeptCode.do', {
        deptCode: $("#deptCode").val()
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

                  // key床号,value姓名/数组
                  that.nameForCode[data[prop][subProp]['patId']] = data[prop][subProp]['name'];

                  that.internalData[prop].push({
                    id: data[prop][subProp]['patId'],
                    text: data[prop][subProp]['bedCode'] + '-' + data[prop][subProp]['name']
                  });
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
        recordItem,
        isOnlyBedCode = metaItem['isBedCode'],
        isEdit = metaItem['isEdit'] ? '' : 'readonly',
        itemInfos,
        onlyBedCodeArr;
      //prop = 'li-' + index;

      // 查找code对应结果数据
      recordItem = that.findRecordItem(code);
      itemInfos = recordItem && recordItem['nurseWhiteBoardRecordFreqInfos'] || [];

      // 例如: 日班护士下有A1班, 日班护士不显示
      if (showType === 'multiText' && !isColSpan) return true;

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
                return '<input data-code="' + code + '" data-isbed="' + isOnlyBedCode + '" data-dataid="id' + timestamp + '" name="' + itemCode + '" data-multi="' + isMulti + '" data-inner="' + isInner + '" class="combobox-' + itemCode + '"/>';
                break;

              case 'text':
                if (isMulti) {
                  return '<textarea name="content" rows="4" name="content" placeholder="' + showName + '"></textarea>';
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
                        if (isOnlyBedCode) val.text = parseInt(val.text.split('-')[0]) + '床';
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
                            return (c['patInfo'].indexOf('-') > 0 ? parseInt(c['patInfo'].split('-')[0]) : parseInt(c['patInfo'].replace('床', '')));
                          });

                          $.each(that.temp['patient'], function(idx, val) {
                            if ($.inArray(parseInt(val['text'].split('-')[0]), patientArr) > -1) {
                              val.selected = true;
                            }

                            if (isOnlyBedCode) val.text = parseInt(val.text.split('-')[0]) + '床';
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

                      return '<input data-dataid="id' + timestamp + '" name="' + itemCode + '" data-multi="' + isMulti + '" data-inner="' + isInner + '" class="combobox-' + itemCode + '"/>';
                      break;

                    case 'text':
                      if (isMulti) {
                        if (isColSpan) {
                          return '<textarea class="input-colspan" rows="4" name="content" placeholder="' + showName + '">' + itemInfo['recordValue'] + '</textarea>';
                        }
                        return '<textarea name="content" placeholder="' + showName + '">' + itemInfo['recordValue'] + '</textarea>';
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
                  if (isOnlyBedCode) val.text = parseInt(val.text.split('-')[0]) + '床';
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
    log(lisArr);
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
                  //return [c, vText[i]].join(',');
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
                  //return [c, vText[i]].join(',');
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

  $('.edit-dialog').dialog({
    title: '编辑小白板',
    width: $(window).width() - 100,
    height: $(window).height() - 100,
    modal: true,
    closed: true,
    buttons: [{
      text: '保存',
      handler: function() {
        note.save();
      }
    }, {
      text: '取消',
      handler: function() {
        $('.edit-dialog').dialog('close');
        note.refreshTimer = setTimeout(function() {
          window.location.reload();
        }, note.timers.refreshTime);
      }
    }]
  });

  // 弹出编辑框
  $(document).on('dblclick', function() {
    note.parseEditTemplate(function() {
      // 回调函数 初始化combobox


      // 重置只显示床位数据
      //window.setTimeout(function () {
      var newArr;
      if (note.temData['onlyBedCod']) {
        newArr = note.temData['onlyBedCod'].map(function(bedItem) {
          return {
            id: bedItem['id'],
            text: bedItem['text'].indexOf('-') > -1 ? bedItem['text'].split('-')[0] : bedItem['text']
          }
        });

        note.temData['onlyBedCod'] = newArr;
        $('[class*="combo"]').initCombo(true, true);
      }
      //}, 500);
    });
    $('.edit-dialog').dialog('open');
    window.clearTimeout(note.refreshTimer);
  });

  $(document).on('click', '.addBtn', function(e) {
    var $this = $(this),
      code = $this.data('code');

    note.addItem(code, this);
  });
});
