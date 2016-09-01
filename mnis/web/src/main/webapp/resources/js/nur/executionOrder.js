var log = console.log.bind(console);
var execution = {
    internalData: null,
    allUsageArr: [],
    config: null,
    queryParam: {
        deptCode: localStorage.getItem('deptCode'),
        startDate: new Date().format('yyyy-MM-dd'),
        endDate: new Date(new Date().getTime() + 3600 * 1000 * 24).format('yyyy-MM-dd'),
        queryDate: new Date().format('yyyy-MM-dd'),
        patIds: ''
    },

    unPrintObj: {},

    filterParam: {
        usage: [],
        type: null
    },

    init: function (callback) {
        var that = this,
            $startDate = $('#startDate'),
            $endDate = $('#endDate');

        /*$startDate.datebox({
         onChange: function () {
         if (that.queryParam.patIds !== '') {
         that.lastData();
         }
         }
         });*/

        // 打印确认
        $('.print-confirm').dialog({
            title: '打印结果确认',
            modal: true,
            closed: true,
            buttons: [{
                text: '是的，已打印',
                handler: function () {
                    that.sendPrint(null, $('.table-wrapper'));
                }
            }, {
                text: '没有',
                handler: function () {
                    $('.print-confirm').dialog('close');
                    $('.lazy-removed').html('');

                    if ($('.print-date').data('printdate')) {
                        $('.print-date').text($('.print-date').data('printdate'))
                    }
                    if ($('.print-nurse').data('printname')) {
                        $('.print-nurse').text($('.print-date').data('printname'))
                    }

                    that.printParam = '';
                }
            }]
        });

        $('.remark').dialog({
            title: '备注',
            width: 240,
            height: 200,
            modal: true,
            closed: true,

            buttons: [{
                text: '保存',
                handler: function () {
                	that.saveRemark();
                }
            }, {
                text: '取消',
                handler: function () {
                    $('.remark').dialog('close');
                }
            }]
        });

        $startDate.datebox('setValue', new Date().format('yyyy-MM-dd'));
        $endDate.datebox('setValue', new Date().format('yyyy-MM-dd'));

        // 全选
        ay.checkAll({
            checkAllTarget: '#usage__all',
            subTarget: 'input[name="usage"]',
            allCallback: function (context) {
                var checkedBtn = $('.usage [name="usage"]:checked'),
                    arr,
                    checkedStyle = $('input[name="style"]:checked').data('style');
                if (context.checked) {
                    arr = checkedBtn.toArray().map(function (checkboxItem) {
                        var usageCode = $(checkboxItem).data('usage');
                        if (usageCode !== '') {
                            return $(checkboxItem).data('usage').toString().toUpperCase();
                        }
                    });
                }

                that.filterParam.usage = context.checked ? arr : [];

                that.filterData(checkedStyle);
            },

            subCallback: function (context) {
                var checkedBtn = $('.usage [name="usage"]:checked'),
                    checkedStyle = $('input[name="style"]:checked').data('style');

                that.filterParam.usage = checkedBtn.toArray().map(function (checkboxItem, index) {
                    var usageCode = $(checkboxItem).data('usage');
                    if (usageCode !== '') {
                        return $(checkboxItem).data('usage').toString().toUpperCase();
                    }
                });

                that.filterData(checkedStyle);
            }
        });

        // 核对
        $(document).on('click', '.check-this', function (e) {
            execution.checkFn(this);
        });

        // 单个药物核对
        $(document).on('click', '.check-single', function (e) {
            execution.checkFn(this, 'single');
        });

        //打印
        $('#print').on('click', function (e) {
            that.filterPrint();
            window.setTimeout(function () {
                $('.print-confirm').dialog('open');
//            	$('.remark').dialog('open');
            }, 500)
        });

        $('#refresh').on('click', function (e) {
            that.lastData();
        });

        $('input[name="type"]').on('click', function (e) {
            that.filterData()
        });

        /*$('[name="style"]').on('click', function (e) {
         if ($(this).data('style') === 'bottle') {
         $('.is-printed').show();
         } else {
         $('.is-printed').hide();
         }
         });*/

        $('#unprinted').on('click', function (e) {
            that.filterData();
        });

    $('#nurseLayout').find('input[type=checkbox]').chkbox();
    $('#nurseLayout').find('input[type=radio]').rdobox();

    that.getConfig();
  },

    checkFn: function (context, type) {
      var that = execution,
          $this = $(context),
          $parent = $this.parent(),
          $tableItem = $this.parents('.table-wrapper'),
          checkNurCode = localStorage.getItem('nurseCode'),
          dateNow = new Date().format('yyyy-MM-dd hh:mm:ss'),
          checkNurName = localStorage.getItem('nurseName'),
          checkedStyle = $('input[name="style"]:checked').data('style'),
          docType,
          orderNos = $tableItem.find('tbody').find('tr').toArray().map(function (item) {
            return $(item).data('orderNo');
          }),
          operateDate = $tableItem.data('oper-date') ? new Date($tableItem.data('oper-date')).format('yyyy-MM-dd hh:mm:ss') : '',
          itemObj = {
              patId: $tableItem.data('patid') || null,
              deptCod: $tableItem.data('deptcod') || null
          };

      switch (checkedStyle) {
          case 'exec':
              docType = 'EXECUTION';
              break;

          case 'infusion':
              docType = 'INF_CARD';
              break;

          case 'normal':
              docType = 'NORMAL';
              break;
      }
      if (type !== 'single') {
        itemObj.chkNurCod = checkNurCode;
        itemObj.chkNurNm = checkNurName;
        itemObj.chkDate = dateNow;
        itemObj.ordDocType = docType;
        itemObj.ordType = $tableItem.data('ordtype').toString() || null;
        itemObj.ordUsgType = (checkedStyle === 'exec' ? $tableItem.data('usgcod') : (checkedStyle === 'infusion') ? $tableItem.data('execcod') : '');
        itemObj.operDate = operateDate;
        itemObj.orderNos = orderNos || [];
        itemObj.ordType = $tableItem.data('ordtype').toString() || null;

        $tableItem.find('tr').each(function () {
          var $tr = $(this);
          var $li = $tr.find('td').eq(0).find('li').eq(0);

          if ($(this).find('.checked-text').length === 0) {
            $('<span class="checked-text" style="color:#0018ff;">[核]</span>').insertBefore($li);
          }
        });

        $parent.html(['<span class="fb check-nurse" data-ckdate="' + dateNow + '" data-cknurcode="' + checkNurCode + '">', checkNurName, '</span>'].join(''));

        if ($tableItem.data('printid')) {
            itemObj.printId = $tableItem.data('printid');
        }

        $tableItem.find('.check-single').hide();

        that.sendPrint([itemObj], $tableItem);
      } else {
        $('<span class="checked-text" style="color:#0018ff;">[核]</span>').insertBefore($this.parents('tr').find('td').eq(0).find('li').eq(0));
        $this.hide();
        itemObj.orderNo = $this.data('orderNo');
        itemObj.approveType = $tableItem.data('execcod') || '';
        itemObj.approveNurseCode = checkNurCode;
        itemObj.approveNurseName = checkNurName;
        itemObj.approveDate = new Date().format('yyyy-MM-dd hh:mm:ss');

        that.singleSendPrint([itemObj]);
      }
    },

    // "docSta":[
    //   "2016-09-09":[
    //     {7:00,value:},{ 15:00,value}
    //
    //
    // ]

    getConfig: function () {
        /*
         * /nur/ patOrderDetail/getOrderConfigData.do
         * */

        var that = this,
            $inputs,
            $btns;

        $.get(ay.contextPath + '/nur/patOrderDetail/getOrderConfigData.do').done(function (res) {
            log(res);

            that.config = res['data'];

            var usageData = res['data']['ordExecTypes'],
                prop,
                arr = [];

            for (prop in usageData) {
                if (usageData.hasOwnProperty(prop)) {
                    arr.push([
                        '<input type="checkbox" checked="checked" id="usage__' + prop.toString().toLowerCase() + '" data-usage="' + prop + '" name="usage">',
                        '<label for="usage__' + prop.toString().toLowerCase() + '" class="mgr10">',
                        usageData[prop],
                        '</label>'
                    ].join(''));
                }
            }

      $('.usage').append(arr.join('\n'));
      $('.usage').find('input[type=checkbox]').chkbox();

            $inputs = $('.filter-wrapper').find('input, button');
            $btns = $('.exec-wrapper').find('button');

            $inputs.attr('disabled', 'disabled');
            $btns.attr('disabled', 'disabled').addClass('disabled');

            log(arr);
        }).fail(function (err) {
            $.messager.alert('提示', '获取配置失败！');
        })
    },

    singleSendPrint: function (customParam) {
      $.post(ay.contextPath + '/nur/patOrderDetail/saveOrdApprove.do', {
          ordApproves: JSON.stringify(customParam)
      }).done(function (res) {
          log(res);
          // var printIdArr = res['data']['printIds'] || null;

          if (res.rslt == '0') {
            $.messager.alert('提示', customParam ? '已核对！' : res['msg']);
          }

      }).fail(function (err) {
          $('.print-confirm').dialog('close');
          $.messager.alert('提示', '请稍候，管理员正在处理...');
      });
    },

    sendPrint: function (customParam, $table) {
        /*
         * /nur/ patOrderDetail/ saveOrdExecDocPrtInfo.do
         * */
        if (customParam || execution.printParam) {
            $.post(ay.contextPath + '/nur/patOrderDetail/saveOrdExecDocPrtInfo.do', {
                ordExecDocPrtInfos: JSON.stringify(customParam || execution.printParam)
            }).done(function (res) {
                var printIdArr = res['data']['printIds'] || null;

                if (printIdArr || printIdArr.length > 0) {
                    $table.each(function (i, tableItem) {
                        var $item = $(tableItem);

                        $item.data('printid', printIdArr[i]);
                    });
                }

                $('.print-confirm').dialog('close');

                $.messager.alert('提示', customParam ? '已核对！' : res['msg']);
            }).fail(function (err) {
                $('.print-confirm').dialog('close');
                $.messager.alert('提示', '请稍候，管理员正在处理...');
            })
        } else {
            $.messager.alert('提示', '参数有误！');
            throw 'execution.printParam 为空!';
        }
    },
    saveRemark:function(){
    	var remarkText = $("#remarkText").val(),
    	barcode = $("#remarkBarcode").val();

    	$.post(ay.contextPath + '/nur/patOrderDetail/savePatOrderRemark.do', {
            barcode:barcode,value:remarkText,type:1
        }).done(function (res) {

            $('.remark').dialog('close');
            $("#remark-" + barcode).html(remarkText);

        }).fail(function (err) {
            $('.remark').dialog('close');
            $.messager.alert('提示', '请稍候，管理员正在处理...');
        })
    },

    filterPrint: function () {
        var bodyHtml,
            iframeWindow = $(window.parent.document).find('#printFrame')[0],
            printDoc = $(iframeWindow.contentWindow.document.body),
            that = execution,
            nurseCode = localStorage.getItem('nurseCode'),
            nurseName = localStorage.getItem('nurseName'),
            deptCode = localStorage.getItem('deptCode'),
            param = [],
            dateNow = new Date().format('yyyy-MM-dd hh:mm:ss'),
            $tables = $('.table-wrapper'),
            checkedStyle = $('input[name="style"]:checked').data('style'),
            docType;

        switch (checkedStyle) {
            case 'exec':
                docType = 'EXECUTION';
                break;

            case 'infusion':
                docType = 'INF_CARD';
                break;

            case 'normal':
                docType = 'NORMAL';
                break;
        }

        //填写打印时间和打印签名
        $tables.each(function (index, tableItem) {
            var $tableItem = $(tableItem),
                $checkNur = $tableItem.find('.check-nurse'),
                $printNur = $tableItem.find('.print-nurse'),
                $printDate = $tableItem.find('.print-date'),
                operateDate = $tableItem.data('oper-date') ? new Date($tableItem.data('oper-date')).format('yyyy-MM-dd hh:mm:ss') : '';

            $printNur.text(nurseName);
            //$printNur.data(nurseName);
            $printDate.text(dateNow);

            var itemObj = {
                nurCod: nurseCode,
                nurNm: nurseName,
                prtDate: dateNow,
                chkNurCod: $checkNur.data('cknurcode') || null,
                chkNurNm: $checkNur.text(),
                chkDate: $checkNur.data('ckdate') || null,
                ordDocType: docType,
                ordUsgType: (checkedStyle === 'exec' ? $tableItem.data('usgcod') : (checkedStyle === 'infusion') ? $tableItem.data('execcod') : ''),
                ordType: $tableItem.data('ordtype').toString() || null,
                operDate: operateDate,
                patId: $tableItem.data('patid') || null,
                deptCod: $tableItem.data('deptcod') || null
            };

            if ($tableItem.data('printid')) {
                itemObj.printId = $tableItem.data('printid');
            }

            param.push(itemObj);
        });

        that.printParam = param;

        iframeWindow.contentWindow.document.head.innerHTML = '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/normalize.css"><link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/public.css">';

        bodyHtml = $('.exec-content').html();

        printDoc.html(bodyHtml);
        printDoc.find('.removed').remove();

        setTimeout(function () {
            iframeWindow.contentWindow.print();
        }, 200)
    },

    getQueryParams: function () {
        var that = this;

        that.queryParam.startDate = $('#startDate').datebox('getValue');

        var selectedEndDate =  $('#endDate').datebox('getValue');

        that.queryParam.endDate = new Date(new Date(selectedEndDate).getTime() +
        		3600 * 24 * 1000).format('yyyy-MM-dd');
    },

    lastData: function (callback) {
        var that = execution,
            checkedStyle = $('input[name="style"]:checked').data('style');

    $.loading();

        that.getQueryParams();

        switch (checkedStyle) {
            case 'exec':
                that.getExecData(callback, 1);
                break;

            case 'infusion':
                that.getInfusionData(callback);
                break;

            case 'normal':
                that.getBottleData(callback, 0);
                break;

            default :
                break;
        }
    },

    // 筛选数据
    filterData: function () {
        var that = execution,
            resData = [],
            checkedStyle = $('input[name="style"]:checked').data('style'),
            isPrintedAll = $('#is-printed_all')[0].checked;

        var checkedBtn = $('.usage [name="usage"]:checked');

        that.filterParam.usage = checkedBtn.toArray().map(function (checkboxItem, index) {
            var usageCode = $(checkboxItem).data('usage');
            if (usageCode !== '') {
                return $(checkboxItem).data('usage').toString().toUpperCase();
            }
        });

        that.filterParam.isPrinted = $('#is-printed_yes')[0].checked;

        that.filterParam.type = $('[name="type"]:checked').data('type').toString();

        log(that.filterParam);

        if (!that.internalData) return;

        that.internalData['ordExecs'].forEach(function (dataItem, index) {
            var dataItemTemp,
                pat_info = {},
                typeTemp = that.filterParam.type,
                usageTemp = that.filterParam.usage,
                exec_temp;

            for (var prop in dataItem) {
                if (dataItem.hasOwnProperty(prop)) {
                    if (prop !== 'ordExecDocOrdInf') {
                        pat_info[prop] = dataItem[prop];
                    }
                }
            }

            dataItemTemp = $.grep(dataItem['ordExecDocOrdInf'], function (dataItemTemp_item, idx) {
                var eIndex = $.inArray(dataItemTemp_item['ordExecCod'], that.filterParam.usage);

                // 选择长嘱或临嘱之一／用法之一
                if (typeTemp !== 'all' && usageTemp.length > 0) {
                    if (that.filterParam.type === dataItemTemp_item['ordType'] && eIndex > -1) {
                        // 筛选已打印
                        // if (that.filterParam.unPrinted) {
                        if (isPrintedAll) return true;
                        return that.filterParam.isPrinted === dataItemTemp_item['isPrt'];
                        //}

                        //return true;
                    }
                }

                // 选择全部类型／没有用法
                if (typeTemp === 'all' && usageTemp.length === 0) {
                    if (isPrintedAll) return false;
                    return that.filterParam.isPrinted === dataItemTemp_item['isPrt'];
                }

                if (typeTemp === 'all' && usageTemp.length > 0) {
                    if (isPrintedAll) return eIndex > -1;
                    return eIndex > -1 && that.filterParam.isPrinted === dataItemTemp_item['isPrt'];
                }

                // 选择长嘱或临嘱之一／没有用法
                if (typeTemp !== 'all' && usageTemp.length === 0) {
                    if (isPrintedAll) return that.filterParam.type === dataItemTemp_item['ordType'];
                    return that.filterParam.type === dataItemTemp_item['ordType'] && that.filterParam.isPrinted === dataItemTemp_item['isPrt'];
                }
            });

            if (checkedStyle === 'exec') {
                if (dataItemTemp.length > 0) {
                    dataItemTemp.forEach(function (c) {
                        exec_temp = $.extend(true, {}, pat_info);
                        exec_temp['ordExecDocOrdInf'] = [c];

                        resData.push(exec_temp);
                    });
                }
            } else {
                pat_info['ordExecDocOrdInf'] = dataItemTemp;
                resData.push(pat_info);
            }
        });

    log(resData);
    that.drawTable(resData, checkedStyle);
    $.loading('close');
  },

    drawTable: function (data, tableType) {
        var that = execution,
            tableHeader = [''],
            htmlArr = [],
            emptyStr = '<div style="color: #fff">empty<br/>empty</div>',
            oneLineEmpty = '<div style="color: #fff">empty</div>';

        switch (tableType) {
            case 'exec':
                $.each(data, function (index, execItem) {
                    var tableHtml = [];
                    if (execItem['ordExecDocOrdInf'].length === 0) {
                        return true;
                    }

                    execItem['ordExecDocOrdInf'].forEach(function (item, index2) {
                        tableHtml = [];

                        tableHtml.push(['<div class="table-wrapper exec_' + index + '" data-deptcod="',
                            execItem['deptCod'],
                            '" data-printid="',
                            item['ordPrtInf'] && item['ordPrtInf']['printId'],
                            '" data-usgcod="',
                            item['usgCod'],
                            '" data-execcod="',
                            item['ordExecCod'],
                            '" data-ordtype="',
                            item['ordType'],
                            '" data-oper-date="',
                            item['ordExecDate'],
                            '" data-patid="',
                            execItem['patId'],
                            '">',
                            '<div class="table-wrapper__header">',
                            '<div>病区：',
                            '                    <span class="tb-value fb">',
                            execItem['deptNm'],
                            '                    </span>',
                            '                </div>',
                            '                <div>床号：',
                            '                    <span class="tb-value fb">',
                            execItem['bedCod'],
                            '                    </span>',
                            '                </div>',
                            '                <div>姓名：',
                            '                    <span class="tb-value fb">',
                            execItem['patNm'],
                            '                    </span></div>',
                            '                <div class="fb">【',
                            (item['usgNm'] || ''),
                            '】</div>',
                            '                <div>执行日期：',
                            '                    <span class="fb tb-value">',
                            item['ordExecDate'],
                            '                    </span></div>',
                            '                <div class="fb fr">【',
                            (item['ordType'] === '1' ? '长嘱' : '临嘱'),
                            '】</div>',
                            '</div>',
                            '<table class="table-wrapper__body">',
                            '                <thead>',
                            '                <tr>',
                            '                    <th style="width: 27em;">药物／项目</th>',
                            '                    <th width="9%">剂量</th>',
                            '                    <th style="width:6em;"">频次</th>',
                         /*   '                    <th width="8%">计划时间</th>',*/
                            '                    <th width="8%">执行时间</th>',
                            '                    <th width="11%">执行核对签名</th>',
                            '                    <th width="11%">备注</th>',
                            '                    <th style="width: 3em" class="removed"><input type="checkbox" checked="checked" class="print-checkAll-' + index + '"></th>',
                            '                    <th style="width: 4em" class="removed">审核</th>',
                            '                </tr>',
                            '                </thead>',
                            '                <tbody>',
                            '            </div>'].join(''));
                        var ordDetail = item['ordDetailInfs'];

                        // 药物
                        ordDetail.forEach(function (detailItem, index2) {
                            var drugData = detailItem['ordItems'],
                                innerData = detailItem['ordGroupInfs'],
                                ordStatus = parseInt(detailItem['ordStatus']),
                                ordNo = detailItem['ordNo'],
                                stopBackground = '',
                                stopLabel = '',
                                approveLabel = '',
                                planTime = [],
                                execTime = [],
                                checkerList = [],
                                remarkList = [],
                                forMatStr = 'hh:mm'
                            /*checkBoxList = []*/;

                            function isCurrentDay() {
                                return new Date().getDate() === new Date(item['ordExecDate']).getDate();
                            }

                            if (!isCurrentDay()) {
                                forMatStr = 'MM-dd hh:mm';
                            }

                            innerData.forEach(function (innerItem, index3) {
                            	var remarkId = ' id="remark-' + innerItem['ordGroupNo'] + '"'
                            	+ ' ondblclick="remarkDbClick(this)"' + ' style="color:#f00;"';
                             /*   planTime.push(['<li>', '<div>', (innerItem['perfSchd']), '</div>', '</li>'].join(''));*/
                                execTime.push(['<li>', (innerItem['ordExecLog'] && innerItem['ordExecLog']['execDate'] && new Date(innerItem['ordExecLog']['execDate']).format(forMatStr).replace(' ', '<br/>') || ''), '</li>'].join(''));
                                checkerList.push(['<li>', (innerItem['ordExecLog'] && innerItem['ordExecLog']['execNurseName'] || ''), '</li>'].join(''));
                                remarkList.push(['<li',remarkId,'>', (innerItem['remark']), '</li>'].join(''));
                                //(innerItem['remark'])
                                //checkBoxList.push(['<li><input class="isPrint-item" data-trindex="' + index2 + '" data-liindex="' + index3 + '" checked="checked" type="checkbox"></li>'].join(''));
                            });

                            if(ordStatus == 3){
                            	stopBackground = 'style= "background:#eee"';
                            	stopLabel = '<span style="color:#f00;">[停]</span>';
                            }

                            if (detailItem.isApproved || (item['ordPrtInf'] && (typeof item['ordPrtInf']['chkNurCod'] !== 'undefined'))) {
                              approveLabel = '<span class="checked-text" style="color:#0018ff;">[核]</span>'
                                // return ['<span class="fb check-nurse" data-ckdate="' + (item['ordPrtInf']['chkDate'] || '') + '" data-cknurcode="' + (item['ordPrtInf']['chkNurCod'] || '') + '">', item['ordPrtInf']['chkNurNm'], '</span>'].join('')
                            }

                            tableHtml.push(['<tr ' + stopBackground + ' data-order-no="' + ordNo + '">',
                                '                    <td>',
                                '                        <ul class="drug-list">' + stopLabel + approveLabel,
                                (drugData.map(function (drugItem) {
                                    return ['<li>', drugItem['orderName'], '</li>'].join('');
                                }).join('')),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td>',
                                '                        <ul class="list-dosage">',
                                (drugData.map(function (drugItem) {
                                    return ['<li>', drugItem['dosage'], drugItem['dosageUnit'], '</li>'].join('');
                                }).join('')),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td>',
                                '                        <div class="freq-td">',
                                (innerData[0] && innerData[0]['freq'] || ''),
                                '                        </div>',
                                '                    </td>',
                                /*'                    <td class="has-border for-print">',
                                '                        <ul class="list-plan-time">',
                                planTime.join(''),
                                '                        </ul>',
                                '                    </td>',*/
                                '                    <td class="has-border for-print color-blue">',
                                '                        <ul class="list-exec-time">',
                                execTime.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td class="has-border for-print color-blue">',
                                '                        <ul class="list-checker">',
                                checkerList.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td class="has-border for-print color-blue">',
                                '                        <ul class="list-remark">',
                                remarkList.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td class="tc removed has-border">',
                                '                        <ul class="list-checkbox">',
                                ['<li><input class="isPrint-item" data-trindex="' + index2 + '" checked="checked" type="checkbox"></li>'].join(''),
                                '                        </ul>',
                                '                    </td>',
                                '<td class="tc removed has-border">',
                                '                        <ul class="list-checkbox">',
                                (function () {
                                  if (detailItem.isApproved || (item['ordPrtInf'] && (typeof item['ordPrtInf']['chkNurCod'] !== 'undefined'))) {
                                    // return '[核]';
                                  } else {
                                    return ['<li><a href="javascript:;" data-order-no="' + ordNo + '" class="btn check-single removed">核对</a></li>'].join('');
                                  }
                                })(),
                                // ,
                                '                        </ul>',
                                '</td>',
                                '                </tr>'].join(''));
                        });

                        // 尾部

                        tableHtml.push([' </tbody>',
                            '            </table>',
                            '            <div class="table-wrapper__footer">',
                            '                <div>打印结束：',
                            '                    <span class="fb print-date ',
                            (item['ordPrtInf'] && item['ordPrtInf']['prtDate'] ? '' : 'lazy-removed'),
                            '" data-printdate="' + (item['ordPrtInf'] && item['ordPrtInf']['prtDate'] || null) + '">',
                            (item['ordPrtInf'] && item['ordPrtInf']['prtDate'] || ''),
                            '                    </span>',
                            '                </div>',
                            '                <div class="mgl15">打印者签名：',
                            '                    <span class="fb print-nurse ',
                            (item['ordPrtInf'] && item['ordPrtInf']['nurCod'] ? '' : 'lazy-removed'),
                            '" data-printname="' + (item['ordPrtInf'] && item['ordPrtInf']['nurNm'] || null) + '">',
                            (item['ordPrtInf'] && item['ordPrtInf']['nurNm'] || ''),
                            '</span>',
                            '                </div>',
                            '                <div class="mgl15">核对者签名：',
                            (function () {
                                if (item['ordPrtInf'] && (typeof item['ordPrtInf']['chkNurCod'] !== 'undefined')) {
                                    return ['<span class="fb check-nurse" data-ckdate="' + (item['ordPrtInf']['chkDate'] || '') + '" data-cknurcode="' + (item['ordPrtInf']['chkNurCod'] || '') + '">', item['ordPrtInf']['chkNurNm'], '</span>'].join('')
                                } else {
                                    return '<span><a href="javascript:;" class="btn check-this removed">核对</a></span>'
                                }
                            })(),
                            '</div>',
                            '            </div></div>'].join(''));
                    });


                    htmlArr.push(tableHtml.join('\n'));

                    ay.checkAll({
                        checkAllTarget: '.print-checkAll-' + index,
                        subTarget: '.exec_' + index + ' .isPrint-item',
                        allCallback: function (context) {
                            var $checkbox = $(context),
                                $table = $checkbox.parents('.table-wrapper'),
                                $isPintUl = $table.find('.for-print').children('ul');

                            if (!context.checked) {
                                $isPintUl.addClass('removed');
                            } else {
                                $isPintUl.removeClass('removed');
                            }
                        },

                        // 选择不打印的行，给li添加removed类名
                        subCallback: function (context) {
                            var $checkbox = $(context),
                                $table = $checkbox.parents('tbody'),
                                thisTrIndex = $checkbox.data('trindex'),
                                $checkedTr = $table.find('tr').eq(thisTrIndex),
                                $isPintUl = $checkedTr.find('.for-print').children('ul');

                            if (!context.checked) {
                                $isPintUl.addClass('removed');
                            } else {
                                $isPintUl.removeClass('removed');
                            }
                        }
                    });
                });

                break;

            case 'infusion':
                $.each(data, function (index, infusionItem) {
                    var tableHtml = [];
                    if (infusionItem['ordExecDocOrdInf'].length === 0) return true;

                    infusionItem['ordExecDocOrdInf'].forEach(function (item, index2) {
                        tableHtml = [];
                        tableHtml.push([
                            '<h2 class="tc" style="margin: 0 0 6px 0">', JSON.parse(localStorage.getItem('config'))['data']['system']['hospitalName'], '<span style="margin-left: 3em;">输液单</span></h2>',
                            '<div class="table-wrapper infusion_' + index + '" data-deptcod="',
                            infusionItem['deptCod'],
                            '" data-printid="',
                            item['ordPrtInf'] && item['ordPrtInf']['printId'],
                            '" data-usgcod="',
                            item['usgCod'],
                            '" data-execcod="',
                            item['ordExecCod'],
                            '" data-ordtype="',
                            item['ordType'],
                            '" data-oper-date="',
                            item['ordExecDate'],
                            '" data-patid="',
                            infusionItem['patId'],
                            '">',
                            '<div class="table-wrapper__header">',
                            '<div>病区：',
                            '                    <span class="tb-value fb">',
                            infusionItem['deptNm'],
                            '                    </span>',
                            '                </div>',
                            '                <div>床号：',
                            '                    <span class="tb-value fb">',
                            infusionItem['bedCod'],
                            '                    </span>',
                            '                </div>',
                            '                <div>姓名：',
                            '                    <span class="tb-value fb">',
                            infusionItem['patNm'],
                            '                    </span></div>',
                            '                <div class="fb">住院号：',
                            infusionItem['inHospNo'],
                            '</div>',
                            '                <div>执行日期：',
                            '                    <span class="fb tb-value">',
                            item['ordExecDate'],
                            '                    </span></div>',
                            '                <div class="fb fr">【',
                            (item['ordType'] === '1' ? '长嘱' : '临嘱'),
                            '】</div>',
                            '</div>',
                            '<table class="table-wrapper__body">',
                            '                <thead>',
                            '                <tr>',
                            '                    <th style="width: 27em;">药物／项目</th>',
                            '                    <th width="9%">剂量</th>',
                            '                    <th width="9%">用法</th>',
                            '                    <th style="width: 6em;">频次</th>',
                            '                    <th width="9%">计划时间</th>',
                            '                    <th width="9%">巡视时间</th>',
                            '                    <th width="9%">速度</th>',
                            '                    <th width="9%">签名</th>',
                            '                    <th style="width: 3em;" class="removed"><input type="checkbox" checked="checked" class="print-checkAll-' + index + '"></th>',
                            '                </tr>',
                            '                </thead>',
                            '                <tbody>',
                            '            </div>'].join(''));


                        var ordDetail = item['ordDetailInfs'];

                        // 药物
                        ordDetail.forEach(function (detailItem, index2) {
                            var drugData = detailItem['ordItems'],
                                innerData = detailItem['ordGroupInfs'],
                                ordStatus = parseInt(detailItem['ordStatus']),
                                stopBackground = '';
                             	stopLabel = '',
                                planTime = [],
                                patrolTime = [],
                                speedList = [],
                                checkerList = [],  //recordNurseName
                                checkBoxList = [];

                            innerData.forEach(function (innerItem, index3) {
                                var patrolData = innerItem['infMntRecs'],
                                    patrolTemp = [],
                                    speedTemp = [],
                                    checkerTemp = [],
                                    checkboxTemp = [];

                                planTime.push(['<li>', (innerItem['perfSchd'] || ''), '</li>'].join(''));

                                patrolData.forEach(function (patrolItem, index4) {
                                    patrolTemp.push(['<li>', (patrolItem['recordDate'] && new Date(patrolItem['recordDate']).format('hh:mm') || ''), '</li>'].join(''));
                                    speedTemp.push(['<li>', (patrolItem['deliverSpeed'] || ''), (patrolItem['speedUnit'] || (Number(patrolItem['deliverSpeed']) !== 0 && ' d/分' || '')), '</li>'].join(''));
                                    checkerTemp.push(['<li>', (patrolItem['recordNurseName'] || ''), '</li>'].join(''));
                                    checkboxTemp.push(['<li><input class="isPrint-item" data-trindex="' + index2 + '" data-liindex="' + index4 + '" checked="checked" type="checkbox"></li>'].join(''));
                                });

                                patrolTime.push(patrolTemp.length === 0 ? '<li></li>' : patrolTemp.join(''));
                                speedList.push(speedTemp.length === 0 ? '<li></li>' : speedTemp.join(''));
                                checkerList.push(checkerTemp.length === 0 ? '<li></li>' : checkerTemp.join(''));
                                checkBoxList.push(checkboxTemp.length === 0 ? '<li><input class="isPrint-item" data-liindex="' + index3 + '" data-trindex="' + index2 + '" checked="checked" type="checkbox"></li>' : checkboxTemp.join(''));
                            });

                            if(ordStatus == 3){
                            	stopBackground = 'style= "background:#eee"';
                            	stopLabel = '<span style="color:#f00;">[停]</span>';
                            }
                            tableHtml.push(['<tr ' + stopBackground + '>',
                                '                    <td>',
                                '                        <ul class="drug-list">' + stopLabel,
                                (drugData.map(function (drugItem) {
                                    return ['<li>', drugItem['orderName'], '</li>'].join('');
                                }).join('')),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td>',
                                '                        <ul class="list-dosage">',
                                (drugData.map(function (drugItem) {
                                    return ['<li>', drugItem['dosage'], drugItem['dosageUnit'], '</li>'].join('');
                                }).join('')),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td>',
                                '                        <div class="usage-td">',
                                (drugData[0] && drugData[0]['usageName'] || ''),
                                '                        </div>',
                                '                    </td>',
                                '                    <td>',
                                '                        <div class="freq-td">',
                                (innerData[0] && innerData[0]['freq'] || ''),
                                '                        </div>',
                                '                    </td>',
                                '                    <td class="has-border for-print">',
                                '                        <ul class="list-plan-time">',
                                planTime.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td class="has-border for-print color-blue">',
                                '                        <ul class="list-exec-time">',
                                patrolTime.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td class="has-border for-print color-blue">',
                                '                        <ul class="list-checker">',
                                speedList.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td class="has-border for-print color-blue">',
                                '                        <ul class="list-checker">',
                                checkerList.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                    <td class="tc removed has-border">',
                                '                        <ul class="list-checkbox">',
                                checkBoxList.join(''),
                                '                        </ul>',
                                '                    </td>',
                                '                </tr>'].join(''));
                        });
                        // 尾部
                        tableHtml.push([' </tbody>',
                            '            </table>',
                            '            <div class="table-wrapper__footer">',
                            '                <div>拔针时间：',
                            '                    <span class="fb">',
                            '',
                            '                    </span>',
                            '                </div>',
                            '                <div class="mgl15">护士签名：',
                            '                    <span class="fb"></span>',
                            '                </div>',
                            '                <div class="mgl15">患者/家属签名：<span class="fb"></span></div>',
                            '            </div></div>'].join(''));

                        htmlArr.push(tableHtml.join('\n'));
                    });


                    ay.checkAll({
                        checkAllTarget: '.print-checkAll-' + index,
                        subTarget: '.infusion_' + index + ' .isPrint-item',
                        allCallback: function (context) {
                            var $checkbox = $(context),
                                $table = $checkbox.parents('.table-wrapper'),
                                $isPintUl = $table.find('.for-print').children('ul');

                            if (!context.checked) {
                                $isPintUl.addClass('removed');
                            } else {
                                $isPintUl.removeClass('removed');
                            }
                        },

                        // 选择不打印的行，给li添加removed类名
                        subCallback: function (context) {
                            var $checkbox = $(context),
                                $table = $checkbox.parents('tbody'),
                                thisTrIndex = $checkbox.data('trindex'),
                                thisLiIndex = $checkbox.data('liindex'),
                                $checkedTr = $table.find('tr').eq(thisTrIndex),
                                $isPintTd = $checkedTr.find('.for-print');

                            log(thisTrIndex, thisLiIndex);
                            if (!context.checked) {
                                $isPintTd.each(function (idx2, tdItem) {
                                    $(tdItem).find('li').eq(thisLiIndex).addClass('removed');
                                });
                            } else {
                                $isPintTd.each(function (idx2, tdItem) {
                                    $(tdItem).find('li').eq(thisLiIndex).removeClass('removed');
                                });
                            }
                        }
                    });
                });

                //htmlArr.unshift('');
                break;

            case 'normal':
                $.each(data, function (index, execItem) {
                    var tableHtml = [],
                        ordType,execDates=[],
                        //所有的表头信息
                        topHtmlObject={topHtml:[],execDate:[]},
                        //内容信息
                        contentHtmlObject={contentHtml:[],execDate:[]},
                        //表尾信息
                        footerHtmlObject={footerHtml:[],execDate:[]};

                    if (execItem['ordExecDocOrdInf'].length === 0) return true;

                    execItem['ordExecDocOrdInf'].forEach(function (item, index2) {
                    	var ordExecDate = item['ordExecDate'];
                    	//唯一的日期加入
                    	if($.inArray(ordExecDate,execDates) == -1 ){
                    		execDates.push(ordExecDate);
                    		topHtmlObject.execDate.push(ordExecDate);
                    		footerHtmlObject.execDate.push(ordExecDate);
                    		topHtmlObject.topHtml.push(['<div class="table-wrapper exec_' + index + '" data-deptcod="',
                    		                            execItem['deptCod'],
                    		                            '" data-printid="',
                    		                            item['ordPrtInf'] && item['ordPrtInf']['printId'],
                    		                            '" data-usgcod="',
                    		                            item['usgCod'],
                    		                            '" data-execcod="',
                    		                            item['ordExecCod'],
                    		                            '" data-ordtype="',
                    		                            item['ordType'],
                    		                            '" data-oper-date="',
                    		                            item['ordExecDate'],
                    		                            '" data-patid="',
                    		                            execItem['patId'],
                    		                            '">',
                    		                            '<div class="table-wrapper__header">',
                    		                            '<div>病区：',
                    		                            '                    <span class="tb-value fb">',
                    		                            execItem['deptNm'],
                    		                            '                    </span>',
                    		                            '                </div>',
                    		                            '                <div>床号：',
                    		                            '                    <span class="tb-value fb">',
                    		                            execItem['bedCod'],
                    		                            '                    </span>',
                    		                            '                </div>',
                    		                            '                <div>姓名：',
                    		                            '                    <span class="tb-value fb">',
                    		                            execItem['patNm'],
                    		                            '                    </span></div>',
                    		                            '                <div>执行日期：',
                    		                            '                    <span class="fb tb-value">',
                    		                            item['ordExecDate'],
                    		                            '                    </span></div>',
                    		                            /*'                <div class="fb fr">【',
                    		                             ordType,
                    		                             '】</div>',*/
                    		                            '</div>',
                    		                            '<table class="table-wrapper__body">',
                    		                            '                <thead>',
                    		                            '                <tr>',
                    		                            '                    <th></th>',
                    		                            '                    <th></th>',
                    		                            '                    <th width="26em">药物／项目</th>',
                    		                            '                    <th>剂量</th>',
                    		                            '                    <th>频次</th>',
                    		                            '                    <th>用法</th>',
                    		                            '                    <th>计划时间</th>',
                    		                            '                    <th>执行信息</th>',
                    		                            '                    <th>配液信息</th>',
                    		                            '                    <th>审核信息</th>',
                    		                            '                    <th>备药信息</th>',
                    		                            '                    <th class="removed"><input type="checkbox" checked="checked" class="print-checkAll-' + index + '"></th>',
                    		                            '                </tr>',
                    		                            '                </thead>',
                    		                            '                <tbody>',
                    		                            '            </div>'].join(''));


                    		footerHtmlObject.footerHtml.push([' </tbody>',
                    		                               '            </table>',


                    		                               '            </div>'].join(''));
                    	}
                        var ordDetail = item['ordDetailInfs'];

                        // 药物
                        ordDetail.forEach(function (detailItem, index2) {
                            var drugData = detailItem['ordItems'],
                                innerData = detailItem['ordGroupInfs'],
                                ordStatus = detailItem['ordStatus'],
                                stopBackground = '',
                                stopLabel = '',
                                planTime = [],
                                execInfo = [],
                                checkerList = [],
                                equipInfo = [],
                                remarkList = [],
                                prepareInfo = [],
                                verifyInfo = []
                            /*checkBoxList = []*/;

                            ordType = item['ordType'] === '1' ? '长嘱' : '临嘱';

                            innerData.forEach(function (innerItem, index3) {
                                var exec_info = '',
                                    plan_time = '',
                                    prepare_info = '',
                                    verify_info = '',
                                    equip_info = '';

                                if (innerItem['ordExecLog'] && innerItem['ordExecLog']['execDate']) {
                                    exec_info = new Date(innerItem['ordExecLog']['execDate']).format('hh:mm') + '<br/>' + innerItem['ordExecLog']['execNurseName'];
                                }

                                if (innerItem['perfSchd']) {
                                    plan_time = innerItem['perfSchd'];
                                }

                                if (innerItem['ordLiqItems'].length > 0) {
                                    if (innerItem['ordLiqItems'][0]['prepareTime']) {
                                        prepare_info = [new Date(innerItem['ordLiqItems'][0]['prepareTime']).format('hh:mm'), innerItem['ordLiqItems'][0]['prepareNurseName']].join('<br>');
                                    }

                                    if (innerItem['ordLiqItems'][0]['verifyTime']) {
                                        verify_info = [new Date(innerItem['ordLiqItems'][0]['verifyTime']).format('hh:mm'), innerItem['ordLiqItems'][0]['verifyNurseName']].join('<br>');
                                    }

                                    if (innerItem['ordLiqItems'][0]['execTime']) {
                                        verify_info = [new Date(innerItem['ordLiqItems'][0]['execTime']).format('hh:mm'), innerItem['ordLiqItems'][0]['execNurseName']].join('<br>');
                                    }
                                }

                                prepareInfo.push(['<li>', prepare_info, '</li>'].join(''));
                                equipInfo.push(['<li>', equip_info, '</li>'].join(''));
                                verifyInfo.push(['<li>', verify_info, '</li>'].join(''));
                                planTime.push(['<li>', plan_time, '</li>'].join(''));
                                execInfo.push(['<li>', exec_info, '</li>'].join(''));
                                checkerList.push(['<li>', (innerItem['ordExecLog'] && innerItem['ordExecLog']['execNurseName'] || ''), '</li>'].join(''));
                                remarkList.push(['<li>', (innerItem['ordExecLog'] && innerItem['ordExecLog']['execNurseName'] || ''), '</li>'].join(''));
                                //checkBoxList.push(['<li><input class="isPrint-item" data-trindex="' + index2 + '" data-liindex="' + index3 + '" checked="checked" type="checkbox"></li>'].join(''));
                            });

                            if(ordStatus == 3){
                            	stopBackground = 'style= "background:#eee"';
                            	stopLabel = '<p style="color:#f00;">[停]</p>';
                            }
                            //多个内容信息加入
                            contentHtmlObject.execDate.push(ordExecDate);
                       		contentHtmlObject.contentHtml.push(['<tr ' + stopBackground +' >',
                       		                                 '<td style="width: 2em;">',
                       		                                ordType,
                       		                                '</td>',
                       		                                '<td style="width: 2em;">',
                       		                                item['ordExecNm'],
                       		                                '</td>',
                       		                                '                    <td style="width: 20em;">',
                       		                                '                        <ul class="drug-list">' + stopLabel,
                       		                                (drugData.map(function (drugItem) {
                       		                                    return ['<li>', drugItem['orderName'], '</li>'].join('');
                       		                                }).join('')),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                '                    <td>',
                       		                                '                        <ul class="list-dosage">',
                       		                                (drugData.map(function (drugItem) {
                       		                                    return ['<li>', drugItem['dosage'], drugItem['dosageUnit'], '</li>'].join('');
                       		                                }).join('')),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                '                    <td>',
                       		                                '                        <div class="freq-td">',
                       		                                (innerData[0] && innerData[0]['freq'] || ''),
                       		                                '                        </div>',
                       		                                '                    </td>',
                       		                                '                    <td style="width: 2em;">',
                       		                                '                        <ul class="list-usage">',
                       		                                item['usgNm'],
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                '                    <td class="has-border for-print" style="width: 4.5em">',
                       		                                '                        <ul class="list-plan-time">',
                       		                                planTime.join(''),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                '                    <td class="has-border for-print color-blue">',
                       		                                '                        <ul class="list-exec-info">',
                       		                                execInfo.join(''),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                // 配液信息
                       		                                '<td class="has-border for-print color-blue">',
                       		                                '                        <ul class="list-exec-info">',
                       		                                equipInfo.join(''),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                '                    <td class="has-border for-print color-blue">',
                       		                                '                        <ul class="list-checker">',
                       		                                verifyInfo.join(''),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                // 备药信息
                       		                                '                    <td class="has-border for-print color-blue">',
                       		                                '                        <ul class="list-remark">',
                       		                                prepareInfo.join(''),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                '                    <td class="tc removed has-border">',
                       		                                '                        <ul class="list-checkbox">',
                       		                                ['<li><input class="isPrint-item" data-trindex="' + index2 + '" checked="checked" type="checkbox"></li>'].join(''),
                       		                                '                        </ul>',
                       		                                '                    </td>',
                       		                                '                </tr>'].join(''));
                       		                        });

                    });

                    //同一人，同一日期的信息放入一个表格
                    topHtmlObject.topHtml.forEach(function (topHtmlItem, indexTop){
                    	tableHtml = [];
                    	tableHtml.push(topHtmlItem);
                    	contentHtmlObject.execDate.forEach(function (execDateItem, indexCont){
                    		//判断日期相同 内容
                    		if(topHtmlObject.execDate[indexTop] == execDateItem){
                    			tableHtml.push(contentHtmlObject.contentHtml[indexCont]);
                    		}

                    	});
                    	tableHtml.push(footerHtmlObject.footerHtml[indexTop]);
                    	htmlArr.push(tableHtml.join('\n'));
                    });

                    ay.checkAll({
                        checkAllTarget: '.print-checkAll-' + index,
                        subTarget: '.exec_' + index + ' .isPrint-item',
                        allCallback: function (context) {
                            var $checkbox = $(context),
                                $table = $checkbox.parents('.table-wrapper'),
                                $isPintUl = $table.find('.for-print').children('ul');

                            if (!context.checked) {
                                $isPintUl.addClass('removed');
                            } else {
                                $isPintUl.removeClass('removed');
                            }
                        },

                        // 选择不打印的行，给li添加removed类名
                        subCallback: function (context) {
                            var $checkbox = $(context),
                                $table = $checkbox.parents('tbody'),
                                thisTrIndex = $checkbox.data('trindex'),
                                $checkedTr = $table.find('tr').eq(thisTrIndex),
                                $isPintUl = $checkedTr.find('.for-print').children('ul');

                            if (!context.checked) {
                                $isPintUl.addClass('removed');
                            } else {
                                $isPintUl.removeClass('removed');
                            }
                        }
                    });
                });

                break;
        }

        log(htmlArr);

        $('.exec-content').html(htmlArr.join(''));

        // 设置行高
        /*$('.has-border ul').each(function (i, c) {
         var $c = $(c),
         $li = $c.find('li'),
         liLen = $li.length,
         $td = $c.parents('td'),
         lineHeight = $td.outerHeight() / liLen + 'px';

         $li.each(function (i2, c2) {
         c2.style.height = lineHeight;
         })
         })*/
    },

    // 获取执行单数据
    getExecData: function (callback, docType) {
        var that = this;
        /*
         * /nur/ patOrderDetail/ getOrdExecDocInfos.do
         * */
        /*
         * docType: '',
         execType: $('[name="isExeced"]:checked').data('type')
         * */

        if (typeof docType !== 'undefined') {
            that.queryParam['docType'] = docType;
        }

        that.queryParam['execType'] = $('[name="isExeced"]:checked').data('type');
        $.get(ay.contextPath + '/nur/patOrderDetail/getOrdExecDocInfos.do', that.queryParam).done(function (res) {
            log(res);
            that.internalData = res['data'];

            //that.internalData = $.extend([], res['data']);

            that.filterData();
        }).fail(function (err) {

        })
    },

    // 获取输液卡数据
    getInfusionData: function (callback) {
        var that = this;
        /*
         * /nur/ patOrderDetail/ getOrdExecDocInfosOnInfuCard.do
         * */
        $.get(ay.contextPath + '/nur/patOrderDetail/getOrdExecDocInfosOnInfuCard.do', that.queryParam).done(function (res) {
            log(res);
            that.internalData = res['data'];
            //that.internalData = $.extend([], res['data']);

            that.filterData();
        }).fail(function (err) {

        })
    },

    // 获取普通样式数据
    getBottleData: function (callback, docType) {
        var that = this;

        that.getExecData(callback, docType);
        /*
         * /nur/ patOrderDetail/ getOrdExecDocInfosOnLabel.do
         * */
        /*$.get(ay.contextPath + '/nur/patOrderDetail/getOrdExecDocInfosOnLabel.do', that.queryParam).done(function (res) {
         log(res);
         that.internalData = res['data'];
         //that.internalData = $.extend([], res['data']);

         that.filterData();
         }).fail(function (err) {

         })*/
    }
};

function getExecOrderDetail(patientStr) {
    var patientList = patientStr.split(',') || '',
        startDate = $('#startDate').datebox('getValue'),
        endDate = $('#endDate').datebox('getValue'),
        $inputs = $('.filter-wrapper').find('input'),
        $btns = $('.exec-wrapper').find('button');
    //$btns = $('.filter-wrapper').find('button');

    if (patientStr.replace(/[,]/g, '') === '') {
        $inputs.attr('disabled', 'disabled');
        $btns.attr('disabled', 'disabled').addClass('disabled');
        execution.queryParam.patIds = '';
        $('.exec-content').html('');
    } else {
        $inputs.removeAttr('disabled');
        $btns.removeAttr('disabled').removeClass('disabled');

        execution.queryParam.patIds = patientList.filter(function (curr) {
            if (curr !== '') return true;
        }).join(',');

        //execution.lastData();
    }
}

function checkAll(context) {
    var $this = $(context),
        $checkBoxs = $this.siblings('input'),
        thisChecked = context.checked;

    $checkBoxs.each(function (i, checkboxItem) {
        checkboxItem.checked = thisChecked;
    });
}

function remarkDbClick(param){
	$("#remarkBarcode").val(param.id.split("-")[1]);
	$('.remark').dialog('open');
	$("#remarkText").val(param.innerText);

}

$(function () {
    execution.init();
    var dParent = window.parent.document,
        dA = $(dParent).find('.checkAllLi'),
        pLen;

    window.setTimeout(function () {
        pLen = $(dParent).find('.insp-patient-list li').length;
        dA.append(['<span>总共：', pLen, '人</span>'].join(''));
    }, 200);
});
