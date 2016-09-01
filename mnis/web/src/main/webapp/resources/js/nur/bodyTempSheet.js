var contextPath = $('#tempScript').attr('contextPath'),
    patientId = null,
    currentDay = null,
    recordDay = null,
    tempInputTimeShow = null,
    tempSelectData = null,
    config = $.parseJSON(sessionStorage.getItem("config")),
    tempSelectDataByDate = null,
    tempSelectDataByTime = null;
LODOP = null,
dic = JSON.parse(sessionStorage.getItem('dic'));

var isSaving;
var hasSelects = ['totalInput', 'otherOutput', 'OtherOne', 'OtherTwo'];
var cmUnitArr = ['fw', 'sg'];
var noUnitArr = ['ts', 'gtb', 'tszlone', 'tszltwo', 'tszl'];
var originalData;
var validateObj = {
    temperatureType: {
        validType: 'numRange[\'temperatureType\',34,42]',
        tipPosition: 'right'
    },
    heartRateType: {
        validType: 'isNumber[\'heartRateType\']',
        tipPosition: 'right'
    },
    pulseType: {
        validType: 'isNumber[\'pulseType\']',
        tipPosition: 'right'
    },
    bloodPressType: {
        validType: 'numRange[\'bloodPressType\']',
        tipPosition: 'right'
    },
    breathType: {
        validType: 'isNumber[\'breathType\']',
        tipPosition: 'right'
    },
    oxygenSaturationType: {
        validType: 'isNumber[\'oxygenSaturationType\']',
        tipPosition: 'right'
    },
    otherOutputType: {
        validType: 'isNumber[\'otherOutputType\']',
        tipPosition: 'right'
    }
};

function transformData(data) {
    return {
        onceItem: data.filter(function(c, i) {
            if (i === 0) {
                return c;
            }
        })[0],
        timeList: data.filter(function(c, i) {
            if (i > 0) {
                return c;
            }
        })
    }
}

function getData(dp, syncTime) {
    var url = ay.contextPath + '/nur/bodySign/getBodySignRecordVosToSheet';
    var params = {
        patId: $("#patientId").val(),
        // date: $('#recordDay').val()
        date: dp ? dp.cal.getNewDateStr() : $('#recordDay').val()
    };

    if (typeof syncTime !== 'undefined') {
        url = ay.contextPath + '/nur/doc/getDocRecordInfoWithMaxTemprature';
        params = {
            patID: $("#patientId").val(),
            deptCode: ay.getLocalData('deptCode'),
            date: dp ? dp.cal.getNewDateStr() : $('#recordDay').val() || '',
            time: syncTime
        }
    }
    $.get(url, params).done(function(res) {
        if (res['rslt'] === '0') {
            console.log(res);
            var temperatureTpl = Handlebars.compile($("#formTpl").html()),
                rst = typeof syncTime !== 'undefined' ? res['data'] : res['data']['lst'];
            var dataTemp;
            var data = transformData(rst);

            // 缓存非同步数据
            if (typeof syncTime === 'undefined') {
                originalData = $.extend(true, {}, rst);
            } else {
              var originalDataTemp = $.extend(true, {}, originalData);
                dataTemp = rst.map(function(item, i) {
                    if (item.bodySignRecord.bodySignItemList.length === 0) {
                        return originalData[i];
                    } else {
                        // 如果同步数据没有值，则不更新
                        item.bodySignRecord.bodySignItemList.forEach(function (detailItem, idx) {
                          // 找到相同项目的item
                          var eIndex;
                          var isExisted = originalDataTemp[i].bodySignRecord.bodySignItemList.find(function (oItem, oIdx) {
                            if (oItem.bodySignDict.itemCode == detailItem.bodySignDict.itemCode) {
                              eIndex = oIdx;
                              return true;
                            }
                          })
                        // 存在相同项目时，把原来的项目删除
                          if (isExisted) {
                            originalDataTemp[i].bodySignRecord.bodySignItemList.splice(eIndex, 1);
                          }
                        });

                        // 去重后，初始数据和同步数据合并
                        item.bodySignRecord.bodySignItemList = item.bodySignRecord.bodySignItemList.concat(originalDataTemp[i].bodySignRecord.bodySignItemList);

                        return item;
                    }
                });

                data = transformData(dataTemp);
            }

            $('#sheetForm').html(temperatureTpl({
                data: data,
                dic: dic
            }));
            console.log(dic)

            // 单位设置
            var selectors = hasSelects.map(function(item) {
                return '#' + item;
            }).join(',');
            $(selectors).each(function(i, c) {
                var $c = $(c),
                    $unit = $c.siblings('[name="bodySignDict.itemUnit"]'),
                    unit = $unit.eq($unit.length - 1).val(),
                    $label = $c.siblings('label');

                /*// 有一些历史数据没有单位
                 if (noUnitArr.indexOf(v['measureNoteCode']) < 0) {
                 $unit = 'ml';
                 } else {
                 $unit = '';
                 }*/

                $label.text(unit);
            });

            var tsElem;

            tsElem = $('#sheetForm select[data-key="temperature"]');
            // 渲染完后进行数据验证

            addValidator(tsElem);

            //cooledTemperature
            $('#sheetForm select[data-key="cooledTemperature"][type="text"]').validatebox({
                validType: 'numRange[\'temperatureType\',34,42]',
                tipPosition: 'right'
            });

            //heartRate
            tsElem = $('#sheetForm select[data-key="heartRate"]');
            addValidator(tsElem);

            //pulse
            tsElem = $('#sheetForm select[data-key="pulse"]');
            addValidator(tsElem);

            //breath
            tsElem = $('#sheetForm select[data-key="breath"]');
            addValidator(tsElem);

            //bloodPress
            tsElem = $('#sheetForm select[data-key="bloodPress"]');
            addValidator(tsElem);

            //oxygenSaturation
            $('#sheetForm input[data-key="oxygenSaturation"]').validatebox({
                validType: 'isNumber[\'oxygenSaturationType\']',
                tipPosition: 'right'
            });

            //otherOutput
            // $('#sheetForm #otherOutput').validatebox({
            //     validType: 'isNumber[\'otherOutputType\']',
            //     tipPosition: 'right'
            // });

            //totalInput
            $('#sheetForm #totalInput').validatebox({
                validType: 'isNumber[\'totalInputType\']',
                tipPosition: 'right'
            });

            //urine
            $('#sheetForm #urine').validatebox({
                validType: 'isNumber[\'urineType\']',
                tipPosition: 'right'
            });

            /*//stool
             $('#sheetForm #stool').validatebox({
             validType: 'isNumber[\'stoolType\']',
             tipPosition: 'right'
             });*/

            /*//weight
             $('#sheetForm #weight').validatebox({
             validType: 'specialNum[\'weightType\']',
             tipPosition: 'right'
             });*/

            var noValidArr = ['bs', 'out', 'qj', 'jc', 'cbc', 'shit', 'sj', 'rggm', 'pc', 'wc', 'zxpb0', 'gch0', 'gch1', 'db1gch2', 'gch2', 'gch3'/*, 'tfzhx', 'fzhx'*/ ];
            $('select[name="measureNoteCode"]').each(function(i, v) {
                var selectedIndex = v.selectedIndex,
                    selectedKey = $(v).data('key'),
                    timeId = $(v).attr('id').replace(/\w*[_]/g, ''),
                    selectedValue = $(v).children('option').eq(selectedIndex).val();

                if ($.inArray(selectedValue, noValidArr) > -1) {
                    $('#' + selectedKey + (isNaN(Number(timeId)) ? '' : '_' + timeId)).validatebox({
                        validType: '',
                        tipPosition: 'right'
                    })
                }
            });

            function addValidator(elemArr) {
                elemArr.each(function(i, n) {
                    var $item = $(n);
                    var selectorText = $item.attr('id').replace('Type', '');
                    var $input;

                    if ($item.val() !== 'ts') {
                        $input = $('#' + selectorText);

                        if (['请假', '外出'].indexOf($input.val()) >= 0) {
                            return;
                        }
                        $input.validatebox(validateObj[$item.data('key') + 'Type']);
                    }
                });
            }
        } else {
            $.messager.alert('警告', '服务器错误!');
        }
    });
}

function openDialog(event) {
    //changeType();
    var $this = $(this);
    $('#recordDay').val($this.data('date'));
    getData();
    $('#dialog-newTemp').dialog('open');
    $('#dialog-newTemp').parents('.window').css({
        'top': '10px'
    });

    $('#dialog-newTemp').parents('.window').siblings('.window-shadow').css({
        'top': '10px'
    });

    $('html, body').animate({
        scrollTop: 0
    }, 300);
}

// handlebars helpers
window.Handlebars.registerHelper('select', function(bodySignItemList, options) {
    var that = this;
    var $el = $('<select />').html(options.fn(that));
    if (!bodySignItemList) return $el.html();
    var key = $el.find('option').eq(0).data('key') || '';
    if ($.isArray(bodySignItemList)) {
        $.each(bodySignItemList, function(i, v) {
            var sKey = v['bodySignDict']['itemCode'],
                selectedValue = v['measureNoteCode'];
            if ($el.find('[value="' + selectedValue + '"]').length === 0) {
                return true;
            }
            if (key !== '' && sKey === key) {
                $el.find('[value="' + selectedValue + '"]').attr({
                    'selected': 'selected'
                });
            }
        });
    } else {
        var selectedValue = bodySignItemList['eventCode'] || bodySignItemList['drugCode'];

        $el.find('[value="' + selectedValue + '"]').attr({
            'selected': 'selected'
        });
    }
    return $el.html();
});


window.Handlebars.registerHelper('input', function(bodySignItemList, options) {
    var that = this,
        $el = $('<li />').html(options.fn(that)),
        unit = 'ml';

    if (!bodySignItemList) return $el.html();
    var key = $el.find('input').data('key');


    if ($.isArray(bodySignItemList)) {
        $.each(bodySignItemList, function(i, v) {
            var sKey = v['bodySignDict']['itemCode'],
                sValue = v['itemValue'];

            unit = v['unit'] || v['bodySignDict']['itemUnit'] || '';

            // 有一些历史数据没有单位
            if (noUnitArr.indexOf(v['measureNoteCode']) < 0) {
                cmUnitArr.forEach(function(itemCode) {
                    if (v['measureNoteCode'] && v['measureNoteCode'].indexOf(itemCode) >= 0) {
                        unit = 'cm';
                    }
                });
                if (unit == '') {
                    unit = 'ml';
                }
            } else {
                unit = '';
            }
            if (sKey === key) {
                $el.find('input').val(sValue).attr('value', sValue);
                if (hasSelects.indexOf(sKey) >= 0) {
                    $el.append('<input type="hidden" name="bodySignDict.itemUnit" value="' + unit + '"/>')
                }
            }
        });

        if (bodySignItemList.length === 0) {
            if (hasSelects.indexOf(key) >= 0) {
                $el.append('<input type="hidden" name="bodySignDict.itemUnit" value="' + unit + '"/>')
            }
        }
    } else {
        var sValue = bodySignItemList[key];

        if (key == 'skinTestInfo_drugName' && bodySignItemList.drugCode == 'ts') {
            $el.find('input').show().val();
            sValue = bodySignItemList.drugName;
        }

        $el.find('input').val(sValue).attr('value', sValue);
    }

    return $el.html();
});

window.Handlebars.registerHelper('radio', function(bodySignItemList, options) {
    var that = this,
        $el = $('<div />').html(options.fn(that));
    if (!bodySignItemList) {
        $el.find('input').eq(0).attr('checked', 'checked')[0].checked = true;
        return $el.html();
    }
    var key = $el.find('input').eq(0).attr('name');
    var sValue = bodySignItemList[key];
    if (sValue) {
        $el.find('input[value="' + sValue + '"]').attr('checked', 'checked')[0].checked = true;
    } else {
        $el.find('input').eq(0).attr('checked', 'checked')[0].checked = true;
    }
    return $el.html();
});

window.Handlebars.registerHelper('checkbox', function(bodySignItemList, options) {
    var that = this,
        $el = $('<div />').html(options.fn(that));
    if (!bodySignItemList) {
        return $el.html();
    }

    var key = $el.find('input').data('key');

    if ($.isArray(bodySignItemList)) {
        $.each(bodySignItemList, function(i, v) {
            var sKey = v['bodySignDict']['itemCode'],
                sValue = v['itemValue'];
            if (sKey === key) {
                $el.find('input').attr('checked', 'checked')[0].checked = true;
            }
        });
    }

    return $el.html();
});

$(function() {

    $("body").height($(window).height() - 2 + "px");
    $(".tsp-mid").css("height", $("body").height() - $(".top-tools").height() + "px");
    recordDay = new Date().format("yyyy-MM-dd");
    currentDay = recordDay;
    loadPdf({
        "currentWeek": $("#currentWeek")
    }, null);

    $('#getSyncData').on('click', function() {
        getData(null, $('#getSyncTime').combobox('getValue'));
    });

    // 新建记录
    $('#dialog-newTemp').dialog({
        title: '添加数据',
        width: 1000,
        height: 650,
        modal: true,
        closed: true,
        onClose: function() {
            $('#recordDay').val(new Date().format('yyyy-MM-dd'));
        }
    });
    if (!window.parent.isHeaderVisible) {
        $(document).on('click', '#newTempRecord', openDialog);
        $(document).on('dblclick', '.mask-date-wrapper > div', openDialog);
        $(document).on('click', '#closeDialog', function(e) {
            $('#dialog-newTemp').dialog('close');
        });
    } else {
        $('#newTempRecord').hide();
    }


    $('#svaBtn').on('click', function(e) {
        var peh = validateData();
        if (peh !== '') {
            $.messager.alert('警告', peh, 'info', function() {
                saveBodySheet(this);
            });
            return;
        }
        if (isSaving) {
            $.messager.alert('警告', '正在保存数据, 请勿重复保存! ');
            return;
        }
        isSaving = true;
        saveBodySheet(this);
    });

    $('#sheetForm').on('click', '[name="tempDown"]', function() {
        if ($(this).attr('checked')) {
            $(this).closest('li').find('[type=text]').removeAttr('disabled');
            $(this).closest('li').find('[type=text]').validatebox({
                novalidate: false,
                required: true
            });
        } else {
            $(this).closest('li').find('[type=text]').attr('disabled', 'disabled');
            $(this).closest('li').find('[type=text]').val('');
            $(this).closest('li').find('[type=text]').validatebox({
                novalidate: true
            });
        }
    });

    $('#sheetForm').on('change', '[data-key="eventCode"]', function(e) {
        var $this = $(this),
            timeId = $this.attr('id').replace(/\w*[_]/g, ''),
            $val = $this.val(),
            recordDate = $this.next('[data-key="recordDate"]'),
            timeNow = $('#time_' + timeId).val();
        $val === '' ? recordDate.val('') : (recordDate.val() === '' && recordDate.val(timeNow));
    });

    console.log(config);

    initSkinTestDrugs();
});

function loadPdf(cb) {
    patientId = $("#patientId").val();
    /*var selectDate = $("#selectDate");
     selectDate.html('<option data-index="">日期</option>');*/
    var tempInputTimeSelector = config.data.system.tempInputTimeSelector.split(',');
    tempInputTimeShow = config.data.system.tempInputTimeShow;
    //设置可录入的时段
    /*var timeArr = [];
     var nowHour = new Date().format('hh');
     $(tempInputTimeSelector).each(function (index, n) {

     if (parseInt(nowHour) > 0 && parseInt(nowHour) < parseInt(n)) {
     timeArr[timeArr.length - 1].selected = true;
     nowHour = 24;
     }
     timeArr.push({"id": n, "text": n});

     });*/
    // combobox 形式的数据加载
    /*$('#recordSelectTime').combobox({
     data: timeArr,
     valueField: 'id',
     textField: 'text',
     onChange: function (nv, ov) {
     if (tempInputTimeShow == "true") {
     $('#recordTime').timespinner('setValue', nv);
     }
     if (nv) {
     $(this).combobox('select', nv);
     }
     else {
     $(this).combobox('select', ov);
     }
     }
     });*/
    if (tempInputTimeShow == "true") {
        $("#recordTime").show();
        $("#recordTime").timespinner({
            showSeconds: false
        });
    }
    //设置时间
    //setTimeCommon();
    //获取体温单数据
    $.post(ay.contextPath + '/nur/bodySign/getBodyTempSheet?id=' + patientId + '&date=' + currentDay, {}, function(data) {
        //设置最小日期
        /*$.get(testUrl,function(data){
         data = $.parseJSON(data);*/
        if (data.rslt !== '0') {
            $.messager.alert('提示', data.msg);
            return;
        }
        var admitDate = data.data.patientInfo.inDate;

        if (admitDate) {
            $("#recordDay").click(function() {
                WdatePicker({
                    readOnly: true,
                    dateFmt: 'yyyy-MM-dd',
                    minDate: admitDate
                });
            });
        }

        tempSelectData = data.data.bodySignDailyRecordList;
        var len = tempSelectData.length;
        /*for (var i = 0; i < len; i++) {
         selectDate.append('<option data-index=' + i + ' value=' + tempSelectData[i].recordDate + '>' + tempSelectData[i].recordDate + '</option>');
         }*/
        DP(data, config.data, cb);
        if (!$("#lastWeek").val()) {
            $("#lastWeek").val($("#currentWeek").val());
        }
        if ($("#currentWeek").val() == 1) {
            $("#prevBtn").attr('disabled', 'disabled');
            $("#firstWeekSheetPdf").attr('disabled', 'disabled');
        }
        //});
    });

}

function printPage() {
    var win = $('#printFrame')[0].contentWindow;
    var html = $("#showPanel").html();
    html = html.replace(/zoom\:\s[0-9]*\.?[0-9]*/, 'zoom:1');
    printHtml(html, win);
}

function printHtml(html, w) {
    w.document.body.innerHTML = html;
    $(w.document.body).find('.mask-date-wrapper').remove();
    w.print();
}


function loadDate(that) {
    var index = $(that).find("option:checked").attr('data-index');
    if (index == '') {
        return;
    }
    var selectTime = $("#selectTime"),
        str = '';
    tempSelectDataByDate = tempSelectData[index];
    selectTime.html('');
    for (var i = 0; i < tempSelectDataByDate.bodySignRecordList.length; i++) {
        if (!!tempSelectDataByDate.bodySignRecordList[i].recordTime) {
            str += ['<tr>',
                '                        <td>',
                '<span class="dlg-title">' + tempSelectDataByDate.bodySignRecordList[i].recordTime + '</span>',
                '                            <a data-index="' + i + '" data-time="' + tempSelectDataByDate.bodySignRecordList[i].recordTime + '" href="javascript:;" onclick="loadTime(this);" class="btn">修改</a>',
                '                        </td>',
                '                    </tr>'
            ].join("");
            selectTime.html(str);
        }
    }

}

function loadTime(that) {
    var index = $(that).attr('data-index');
    if (index == '') {
        return;
    }
    tempSelectDataByTime = tempSelectDataByDate.bodySignRecordList[index];
    //console.log(tempSelectDataByTime);
    if (tempInputTimeShow == "true") {
        $('#recordTime').timespinner('setValue', tempSelectDataByTime.recordTime.substring(0, tempSelectDataByTime.recordTime.lastIndexOf(':')));
        $("#recordSelectTime").combo('disable');
        $("#recordTime").timespinner('disable');
    } else {
        $("#recordSelectTime").combobox('select', tempSelectDataByTime.recordTime.substring(0, tempSelectDataByTime.recordTime.lastIndexOf(':')));
        $("#recordSelectTime").combo('disable');
    }

    $("#svaBtn").attr('data-type', 'update');
    $("#recordDay").attr('disabled', 'disabled');
    //$("#recordTime").combo('disable');
    //fillData(tempSelectDataByTime);
    $('#dialog-newTemp').dialog('open');
    //$("#recordTime").val(tempSelectDataByTime.recordTime.substring(0,tempSelectDataByTime.recordTime.lastIndexOf(':')));

    //$(".info-panel-title p").html('体温单更新&nbsp;<input type="button" value="录入" onclick="changeType();"></button>');
}

/*function fillData(data) {
 clearData();
 $("#recordDay").val(tempSelectDataByTime.recordDay);
 $("[name='recordTime']").siblings('input').val(tempSelectDataByTime.recordTime);
 log(data);
 var bodySignItemList = data.bodySignItemList;
 if (data.copyFlag == 'Y') {
 $("#copyTo").attr('checked', 'checked');
 }
 else {
 $("#copyTo").removeAttr('checked');
 }
 if (data.cooled == 1) {
 $("#tempDown").attr('checked', 'checked');
 $("#cooledTemperature").removeAttr('disabled');
 }
 if (data.event) {
 $("#eventSelect").find('option[value="' + data.event.eventCode + '"]').attr('selected', 'selected');
 //        $("#event_recordDate").val(data.event.recordDate.substring(data.event.recordDate.lastIndexOf(' ') + 1, data.event.recordDate.lastIndexOf(':')));
 if (data.event.recordDate) {
 $("#event_recordDate").val(data.event.recordDate/!*.substring(data.event.recordDate.lastIndexOf(' ') + 1, data.event.recordDate.lastIndexOf(':'))*!/);
 }

 //$("#event_recordDate").combobox('select',data.event.recordDate.substring(data.event.recordDate.lastIndexOf(' ')+1,data.event.recordDate.lastIndexOf(':')));
 } else {
 $("#eventSelect").find('option[value=""]').attr('selected', 'selected');
 $("#event_recordDate").val('');
 }
 if (data.skinTestInfo) {
 $.each($('#drugName_list').find('option'), function (i, e) {
 if ($(e).text() === data.skinTestInfo['drugName']) {
 $(e).attr('selected', 'selected');
 }
 });
 //$('#drugName_list').find('option[value="' + bodySignItemList[i].measureNoteCode + '"]').attr('selected', 'selected');
 $("#skinTestInfo_drugName").val(data.skinTestInfo.drugName);
 $("input[name='testResult']").each(function () {
 $(this).removeAttr('checked');
 if ($(this).val() == data.skinTestInfo.testResult) {
 $(this).attr('checked', 'checked');
 }
 });
 }
 for (var i = 0; i < bodySignItemList.length; i++) {
 $("#" + bodySignItemList[i].bodySignDict.itemCode).val(bodySignItemList[i].itemValue);
 if (bodySignItemList[i].bodySignDict.itemCode && bodySignItemList[i].bodySignDict.itemCode != 'cooledTemperature') {
 log(bodySignItemList[i].bodySignDict.itemCode + 'Type' + ':', bodySignItemList[i].measureNoteCode);
 $("#" + bodySignItemList[i].bodySignDict.itemCode + 'Type').find('option[value="' + bodySignItemList[i].measureNoteCode + '"]').attr('selected', 'selected');
 }

 }
 }*/

function checkIsForm() {
    var state = 1;

    if (!$('#sheetForm').form('enableValidation').form('validate')) {
        state = 0;
    }
    return state;
}

function clearData() {
    /*$("input[name='itemValue']").each(function () {
     $(this).val('');
     $(this).validatebox('validate');
     if ($(this).attr('id') == 'cooledTemperature') {
     $(this).validatebox({novalidate: true});
     }
     });
     $("select[name='measureNoteCode']").each(function () {
     $(this).find('option:first').attr('selected', true);
     });
     $("#tempDown").removeAttr('checked');
     $("#cooledTemperature").attr('disabled', 'disabled');
     $("input[name='testResult']:eq(0)").attr('checked', 'checked');
     //设置默认可转抄
     $("#copyTo").attr('checked', true);*/
    $('#sheetForm')[0].reset();

    //setTimeCommon();
}

function setTimeCommon() {
    //设置时间
    var date = new Date().format('yyyy-MM-dd');
    var time = new Date().format("hh:mm");

    $('#recordDay').val(date);
    $("#recordSelectTime").combo('setText', $("#recordSelectTime").combo('getValue'));
    //    $("#recordTime").val(time);
    //$("#recordSelectTime").next().find('input').val(time);
    //    $("[name='recordTime']").siblings('input').val(time.substr(0, 3) + '00');
    $("#event_recordDate").val(time);
}

function changeType() {
    $(".info-panel-title p").html("体温单录入");
    $("#svaBtn").removeAttr('data-type');
    clearData();
    $("#selectTime").find('option:contains("时间")').attr('selected', true);
    $("#skinTestInfo_drugName").val('');
    $("#eventSelect option:first").attr('selected', 'selected');
    $("#recordDay").removeAttr('disabled');
    if (tempInputTimeShow == "true") {
        $("#recordTime").timespinner('enable');
        $("#recordSelectTime").combo('enable');
    } else {
        $("#recordSelectTime").combo('enable');
    }

    $("#tempDown").removeAttr('checked');
    $("#cooledTemperature").attr('disabled', 'disabled');
    $("#copyTo").attr('checked', 'checked');

    //setTimeCommon();
}

/*function prevWeekSheet(){
 var patId = $('#patientId').val();
 var offsetWeek = parseInt($('#offsetWeek').val()) - 1;
 var url = contextPath + '/nur/bodySign/' + patId + '/getBodySheetChart.do';
 $.post(url, {
 offsetWeek: offsetWeek
 }, function(result) {
 try {
 if (result.success) {
 var chartUrl = result.obj;
 if (chartUrl != null) {
 $('#offsetWeek').val(offsetWeek);
 $('#sheetImg').attr('src', chartUrl);
 }
 } else {
 alert('提示: '+result);
 }
 } catch (e) {
 alert(result);
 }
 });
 }*/

/*function nextWeekSheet(){
 var patId = $('#patientId').val();
 var offsetWeek = parseInt($('#offsetWeek').val()) + 1;
 var url = contextPath + '/nur/bodySign/' + patId + '/getBodySheetChart.do';
 $.post(url, {
 offsetWeek: offsetWeek
 }, function(result) {
 try {
 if (result.success) {
 var chartUrl = result.obj;
 if (chartUrl != null) {
 $('#offsetWeek').val(offsetWeek);
 $('#sheetImg').attr('src', chartUrl);
 }
 } else {
 alert('提示: '+result);
 }
 } catch (e) {
 alert('错误: '+result);
 }
 });
 }*/

function firstWeekSheetPdf() {
    var currentWeek = parseInt($('#currentWeek').val());
    var offsetWeek = currentWeek - 1;
    if (currentWeek == 1) {
        return;
    }
    currentWeek = 1;
    $("#prevBtn").attr('disabled', 'disabled');
    $("#firstBtn").attr('disabled', 'disabled');
    $('#currentWeek').val(1);
    var TempWeek = new Date(currentDay);
    TempWeekTime = TempWeek.getTime();
    TempWeekTime -= 86400000 * 7 * offsetWeek;
    TempWeek.setTime(TempWeekTime);
    TempWeek = TempWeek.format("yyyy-MM-dd");
    currentDay = TempWeek;
    $('#selectTime').html('');
    loadPdf({}, null);
}

function lastWeekSheetPdf() {
    var currentWeek = parseInt($('#currentWeek').val());
    var lastWeek = parseInt($('#lastWeek').val());
    if (currentWeek >= lastWeek) {
        return;
    }
    var tempNum = lastWeek - currentWeek;
    currentWeek = lastWeek;
    $("#nextBtn").attr('disabled', 'disabled');
    $("#lastBtn").attr('disabled', 'disabled');
    $('#currentWeek').val(currentWeek);
    $("#prevBtn").removeAttr('disabled');
    $("#firstBtn").removeAttr('disabled');
    var TempWeek = new Date(currentDay);
    TempWeekTime = TempWeek.getTime();
    TempWeekTime += 86400000 * 7 * tempNum;
    TempWeek.setTime(TempWeekTime);
    TempWeek = TempWeek.format("yyyy-MM-dd");
    currentDay = TempWeek;
    $('#selectTime').html('');
    loadPdf({}, null);
}

function prevWeekSheetPdf() {
    var currentWeek = parseInt($('#currentWeek').val());
    if (currentWeek == 1) {
        return;
    }
    currentWeek -= 1;
    if (currentWeek == 1) {
        $("#prevBtn").attr('disabled', 'disabled');
        $("#firstBtn").attr('disabled', 'disabled');
    }
    $('#currentWeek').val(currentWeek);
    $("#nextBtn").removeAttr('disabled');
    $("#lastBtn").removeAttr('disabled');
    var TempWeek = new Date(currentDay);
    TempWeekTime = TempWeek.getTime();
    TempWeekTime -= 86400000 * 7;
    TempWeek.setTime(TempWeekTime);
    TempWeek = TempWeek.format("yyyy-MM-dd");
    currentDay = TempWeek;
    $('#selectTime').html('');
    loadPdf(null);
}

function nextWeekSheetPdf() {
    var currentWeek = parseInt($('#currentWeek').val());
    currentWeek += 1;
    $('#currentWeek').val(currentWeek);
    $("#prevBtn").removeAttr('disabled');
    $("#firstBtn").removeAttr('disabled');
    var TempWeek = new Date(currentDay);
    var TempWeekTime = TempWeek.getTime();
    TempWeekTime += 86400000 * 7;
    TempWeek.setTime(TempWeekTime);
    TempWeek = TempWeek.format("yyyy-MM-dd");
    currentDay = TempWeek;
    $('#selectTime').html('');
    loadPdf(null);
}

/*function queryByInput(){
 var inputVal = $('#searchWeek').val();
 if ( !inputVal) {
 return;
 }

 if ( !ay.isUnsignedInteger(inputVal)) {
 alert('提示: 请输入数字');
 return;
 }

 var lastWeek = parseInt($('#lastWeek').val());
 var week = parseInt(inputVal);
 if ( week < 1 || week > lastWeek) {
 alert('提示: 请输入大于1且小于' + lastWeek + '之间的数字');
 return;
 }
 if(week == 1){
 if(!$("#prevBtn").hasClass('btn-disable')){
 $("#prevBtn").addClass('btn-disable');
 $("#firstBtn").addClass('btn-disable');
 }
 }
 if( week == lastWeek){
 if(!$("#nextBtn").hasClass('btn-disable')){
 $("#nextBtn").addClass('btn-disable');
 $("#lastBtn").addClass('btn-disable');
 }
 }
 if (week > 1) {
 $('#prevBtn').attr('disabled', false);
 $("#prevBtn").removeClass('btn-disable');
 $("#firstBtn").removeClass('btn-disable');
 }
 if (week < lastWeek) {
 $('#nextBtn').attr('disabled', false);
 $("#nextBtn").removeClass('btn-disable');
 $("#lastBtn").removeClass('btn-disable');
 }
 $('#currentWeek').val(inputVal);
 var patId = $('#patientId').val();
 var offsetWeek = week - parseInt(lastWeek);
 var url = contextPath + '/nur/bodySign/' + patId + '/getBodyTempSheetPdf.do';
 $.post(url, {
 offsetWeek: offsetWeek
 }, function(result) {
 try {
 if (result.success) {
 var chartUrl = contextPath + '/common/pdfView.do?fileName=' + result.obj;
 if (chartUrl != null) {
 $('#tempSheetFrame').attr('src', encodeURI(chartUrl));
 }
 } else {
 alert('提示: '+result);
 }
 } catch (e) {
 alert('错误: '+ result);
 }
 });
 }*/

function clearBodySheet() {
    $("#copyTo").attr('checked', 'checked');
    $('#sheetForm').find("input[type=text]").each(function() {
        if ($(this).attr('class') && $(this).attr('class').indexOf('combo-text') >= 0) {
            return;
        } else if ($(this).attr('id') == 'recordTime' || $(this).attr('id') == 'event_recordDate') {
            return;
        }
        $(this).val("");

    });
}


function validateData() {
    var msg = '';
    var heartRate = $('[id*="heartRate_"]');
    var pulse = $('[id*="pulse_"]');

    heartRate.each(function(i, v) {
        var $el = $(v),
            timeId = $el.attr('id').replace(/\w*[_]/g, ''),
            heartRateValue = $el.val(),
            pulseValue = $('#pulse_' + timeId).val();
        if (heartRateValue !== '' && pulseValue !== '' && heartRateValue != pulseValue) {
            msg = '脉搏与心率不一致!';
            return false;
        }
    });
    return msg;
}

function saveBodySheet(that) {
    /*if (checkIsForm() == 0) {
        isSaving = false;
        return;
    }*/

    var type = $(that).attr('data-type');
    var url = ay.contextPath;
    var params = null;
    var event_recordDate = $("#event_recordDate").val() + ':00';
    /*if (tempInputTimeShow == "true") {
     var recordTime = $("#recordTime").timespinner('getValue') + ":00";
     }
     else {
     var recordTime = $("#recordSelectTime").combobox('getValue') + ':00';
     }*/

    // var event_recordDate = $("#event_recordDate").combo('getValue')+':00';
    //if (type == 'update') {
    url += '/nur/bodySign/batchSaveBodySignRecord';
    //url += '/nur/bodySign/updateBodySignRecord.do';
    params = [];

    // 组装数据
    var list = $('.list-item');
    var patient = new parent.Patient();
    var patientId = patient.patientId;
    var patientName = patient.patientName;
    var nurseName = $("#nurseName", parent.window.document).text();
    var nurseId = $("#nurseId", parent.window.document).val();
    var recordDay = $("#recordDay").val();
    var isCopyTo = $("#copyTo").attr('checked') ? 'Y' : 'N';
    var remark = $("#remark").val();
    var emptyNum = 0;

    // 一天一次体征
    var onceItem = {
        "bodySignItemList": null,
        "patientId": patientId,
        "patientName": patientName,
        "recordDay": recordDay,
        "recordNurseCode": nurseId,
        "recordNurseName": nurseName,
        "recordName": '00:00',
        "recordTime": '00:00:00',
        // "copyFlag": isCopyTo,
        "cooled": "0"
    };

    // 皮试
    if ($("#drugName_list").val()) {
        onceItem.skinTestInfo = {
            "drugName": $("#drugName_list").val() === 'ts' ? $('#skinTestInfo_drugName').val() : $("#drugName_list option").eq($("#drugName_list")[0].selectedIndex).text(),
            "drugCode": $("#drugName_list").val(),
            "orderGroupNo": "-1",
            "index": 0,
            "patientId": patientId,
            "patientName": patientName,
            "testNurseId": nurseId,
            "testNurseName": nurseName
        };
        $("input[name='testResult']").each(function() {
            if ($(this).attr('checked')) {
                onceItem.skinTestInfo.testResult = $(this).val();
            }
        });
    }

    $('.table-0 [name="measureNoteCode"]').each(function(i, v) {
        var $unitLabel = $(v).siblings('[for="bodySignDict.itemUnit"]');
        var $unitInput = $(v).siblings('[name="bodySignDict.itemUnit"]');
        var measureNoteCode = $(v).children(":selected").val(),
            measureNoteName = measureNoteCode === '' ? '' : ($(v).siblings('[name="itemValue"]').length === 0 ? '默认' : $(v).children(":selected").html()),
            itemValue = $(v).siblings('[name="itemValue"]').length === 0 ? $(v).children(":selected").html() : $(v).siblings('[name="itemValue"]').val(),
            itemCode = $(v).siblings('[name="bodySignDict.itemCode"]').length === 0 ? $(v).data('key') : $(v).siblings('[name="bodySignDict.itemCode"]').val(),
            itemName = $(v).siblings('[name="bodySignDict.itemName"]').length === 0 ? $(v).data('key') : $(v).siblings('[name="bodySignDict.itemName"]').val(),
            itemUnit;

        if ($unitLabel.length === 0 && $unitInput.length === 0) {
            itemUnit = 'ml';
        } else {
            if ($unitLabel.length === 0) {
                itemUnit = $unitInput.val();
            } else {
                itemUnit = $unitLabel.text();
            }
        }

        if (measureNoteCode === '' || itemValue === '') return true;

        if (!onceItem.bodySignItemList) {
            onceItem.bodySignItemList = [];
        }
        onceItem.bodySignItemList.push({
            "abnormalFlag": 0,
            "bodySignDict": {
                "itemCode": itemCode,
                "itemName": itemName,
                "itemUnit": itemUnit
            },
            "index": 0,
            "measureNoteCode": measureNoteCode,
            "measureNoteName": measureNoteName,
            "unit": itemUnit,
            "itemValue": itemValue
        });
    });

    /*// 其它排出量
     if ($("#otherOutput").val()) {
     if (!onceItem.bodySignItemList) {
     onceItem.bodySignItemList = [];
     }
     onceItem.bodySignItemList.push({
     "abnormalFlag": 0,
     "bodySignDict": {
     "itemCode": "otherOutput",
     "itemName": "其它排出量",
     "itemUnit": 'ml'
     },
     "index": 0,
     "measureNoteCode": 'otherOutput',
     "measureNoteName": '其它排出量',
     "itemValue": $("#otherOutput").val()
     });
     }*/

    // 皮试
    if ($("#drugName_list").val()) {
        params.skinTestInfo = {
            "drugName": $("#drugName_list").val() === 'ts' ? $('#skinTestInfo_drugName').val() : $("#drugName_list option").eq($("#drugName_list")[0].selectedIndex).text(),
            "drugCode": $("#drugName_list").val(),
            "orderGroupNo": "-1",
            "index": 0,
            "patientId": patientId,
            "patientName": patientName,
            "testNurseId": nurseId,
            "testNurseName": nurseName
        };
        $("input[name='testResult']").each(function() {
            if ($(this).attr('checked')) {
                params.skinTestInfo.testResult = $(this).val();
            }
        });
    }

    params.push(onceItem);

    list.each(function(i, el) {
        var $el = $(el),
            obj = {},
            timeId = $el.data('id'),
            event_recordDate = $("#event_recordDate_" + timeId).val(),
            recordTime = $el.find('[name="recordName"]').val() + ':00',
            unit = '';
        obj = {
            "bodySignItemList": null,
            "patientId": patientId,
            "patientName": patientName,
            "recordDay": recordDay,
            "recordNurseCode": nurseId,
            "recordNurseName": nurseName,
            "recordName": $el.find('[name="recordName"]').val(),
            "recordTime": recordTime,
            // "copyFlag": isCopyTo,
            "cooled": "0"
        };

        // 事件
        if ($("#eventSelect_" + timeId).val()) {
            obj.event = {
                "confirmed": false,
                "eventCode": $("#eventSelect_" + timeId).val(),
                "eventName": $("#eventSelect_" + timeId).children(":selected").html(),
                "index": 0,
                "patientId": patientId,
                "patientName": patientName,
                "recordDate": recordDay + ' ' + event_recordDate
            }
        }

        // 疼痛
        if ($("#painType_" + timeId).val()) {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "pain",
                    "itemName": "疼痛",
                    "itemUnit": ""
                },
                "index": 0,
                "itemValue": $("#painType_" + timeId).children(":selected").html(),
                "measureNoteCode": $("#painType_" + timeId).val(),
                "measureNoteName": '默认'
            });
        }

        // 血氧
        if ($("#oxygenSaturation_" + timeId).val()) {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "oxygenSaturation",
                    "itemName": "血氧饱和度",
                    "itemUnit": "%"
                },
                "index": 0,
                "itemValue": $("#oxygenSaturation_" + timeId).val(),
                "measureNoteCode": 'moren',
                "measureNoteName": '默认'
            });
        }

        // 血压 bloodPress
        if ($("#bloodPress_" + timeId).val()) {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "bloodPress",
                    "itemName": "血压",
                    "itemUnit": "mmHg"
                },
                "index": 0,
                "unit": 'mmHg',
                "itemValue": $("#bloodPress_" + timeId).val(),
                "measureNoteCode": $("#bloodPressType_" + timeId).children(":selected").val(),
                "measureNoteName": $("#bloodPressType_" + timeId).children(":selected").html(),
            });
        }

        // 呼吸 breath
        if ($("#breath_" + timeId).val()) {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "breath",
                    "itemName": "呼吸",
                    "itemUnit": "次/分"
                },
                "index": 0,
                "unit": '次/分',
                "itemValue": $("#breath_" + timeId).val(),
                "measureNoteName": $("#breathType_" + timeId).children(":selected").html(),
                "measureNoteCode": $("#breathType_" + timeId).children(":selected").val()
            });
        }

        // 心率 heartRate
        if ($("#heartRate_" + timeId).val()) {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "heartRate",
                    "itemName": "心率",
                    "itemUnit": "次/分"
                },
                "index": 0,
                "unit": '次/分',
                "itemValue": $("#heartRate_" + timeId).val(),
                "measureNoteName": $("#heartRateType_" + timeId).children(":selected").html(),
                "measureNoteCode": $("#heartRateType_" + timeId).children(":selected").val()
            });
        }

        // 脉搏 pulse
        if ($("#pulse_" + timeId).val()) {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "pulse",
                    "itemName": "脉搏",
                    "itemUnit": "次/分"
                },
                "index": 0,
                "unit": '次/分',
                "itemValue": $("#pulse_" + timeId).val(),
                "measureNoteName": $("#pulseType_" + timeId).children(":selected").html(),
                "measureNoteCode": $("#pulseType_" + timeId).children(":selected").val()
            });
        }

        // 降温 coolwayType
        if ($("#coolwayType_" + timeId).val()) {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "coolway",
                    "itemName": "降温方式"
                },
                "index": 0,
                "itemValue": $("#coolwayType_" + timeId).children(":selected").html(),
                "measureNoteCode": $("#coolwayType_" + timeId).children(":selected").val(),
                "measureNoteName": '默认'
            });
        }

        // 降温后 cooledTemperature
        if ($("#tempDown_" + timeId).attr('checked') == 'checked' && $("#cooledTemperature_" + timeId).val()) {
            obj.cooled = 1;
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "cooledTemperature",
                    "itemName": "降温后体温",
                    "itemUnit": "°C"
                },
                "index": 0,
                "unit": '°C',
                "itemValue": $("#cooledTemperature_" + timeId).val(),
                "measureNoteCode": 'moren',
                "measureNoteName": '默认'
            });
        }

        // 体温 temperature
        if ($("#temperatureType_" + timeId).val() && $('#temperature_' + timeId).val() !== '') {
            if (!obj.bodySignItemList) {
                obj.bodySignItemList = [];
            }
            obj.bodySignItemList.push({
                "abnormalFlag": 0,
                "bodySignDict": {
                    "itemCode": "temperature",
                    "itemName": "体温",
                    "itemUnit": "°C"
                },
                "unit": '°C',
                "index": 0,
                "itemValue": $("#temperature_" + timeId).val(),
                "measureNoteName": $("#temperatureType_" + timeId).children(":selected").html(),
                "measureNoteCode": $("#temperatureType_" + timeId).children(":selected").val()
            });
        }

        params.push(obj);
    });

    /*$.each(params, function (i, v) {
     if ((!v['bodySignItemList'] && !v['event'] && !v['skinTestInfo'])) {
     emptyNum += 1;
     }
     });

     if (emptyNum === 7) {
     $.messager.alert('提示', "请选择添加的体征项!");
     return;
     }*/

    log(params);

    params = JSON.stringify(params);
    console.log(params, $('[name="bodySignDict.itemUnit"]'));
    //return;
    $.post(url, {
        item: params,
        copyFlag: isCopyTo,
        isSave: true
    }, function(result) {
        console.log(result);
        isSaving = false;
        try {
            if (result.rslt == 0) {
                $.messager.alert('提示', result.msg);
                clearData();
                $('#selectTime').html('');
                $('#dialog-newTemp').dialog('close');
            } else {
                $.messager.alert('提示', result.msg);
            }

            loadPdf(null);
        } catch (e) {
            $.messager.alert('提示', e);
        }
    });
}


function handleSelectChange(inputObj, selectObj) {
    var typeValue = $(selectObj).find('option:selected').val(),
        typeName = $(selectObj).find('option:selected').text(),
        objectName = inputObj.replace(/[_]\d+/g, ''),
        key = $('#' + inputObj).data('key'),
        _key = $(selectObj).find('option').eq(0).data('key');

    var editArray = ['phlegm', 'sweat', 'hydrothorax', 'csf', 'ylw', 'dz', 'ox', 'kx', 'yw', 'gw', 'kw', 'ts', 'hxj', 'zjhx', 'sz', 'xz', 'moren', 'kf', 'urine', 'ylw', 'zjpn', 'dn', 'gch', 'fs'];
    var selectArray = ['bs', 'kn', 'out', 'qj', 'jc', 'cbc', 'shit', 'sj', 'rggm', 'ly', 'pc', 'wc', 'zxpb0', 'gch0', 'gch1', 'db1gch2', 'gch2', 'gch3' /*'tfzhx','fzhx'*/ ];
    var unitStr;
    var timeId = inputObj.replace(/\w*[_]/, '');
    var inputsName = ['bloodPress', 'temperature', 'pulse', 'heartRate', 'breath', 'bloodPress'];

    /*if ($.inArray(typeValue, editArray) >= 0) {
     $('#' + inputObj).removeAttr("readonly");
     $('#' + inputObj).val('');
     $('#' + inputObj).focus();
     if (objectName == 'temperature') {
     $('#' + inputObj).validatebox({
     validType: 'numRange[\'temperatureType\',34,42]',
     novalidate: false
     });
     }
     else if (objectName == 'bloodPress') {
     $('#' + inputObj).validatebox({
     validType: 'numRange[\'bloodPressType\']',
     novalidate: false
     });
     }
     else if (objectName == 'height' || inputObj == 'weight') {
     $('#' + inputObj).validatebox({
     validType: 'specialNum[\'' + inputObj + 'Type\']',
     novalidate: false
     });
     }
     /!*else {
     $('#' + inputObj).validatebox({
     validType: 'isNumber[\'' + inputObj + 'Type\']',
     novalidate: false
     });
     }*!/
     }*/

    if (objectName === 'pat_status') {
        var $inputs = inputsName.map(function(item) {
            return '#' + item + '_' + timeId;
        }).join(',');
        //  $(inputsName.map())

        $($inputs).val(typeName);
        if (typeName == '') return;
        $($inputs).each(function() {
            $(this).validatebox({
                novalidate: true
            });
        });
    }

    log($(selectObj));

    if (_key && _key === 'skinTestInfo') {
        if ($(selectObj).attr('id') === 'drugName_list' && typeValue === 'ts') {
            $('#' + inputObj).validatebox({
                novalidate: true
            });

            $('#skinTestInfo_drugName').show().focus();
        } else {
            $('#skinTestInfo_drugName').hide();
        }
    }

    if ($.inArray(typeValue, selectArray) >= 0) {
        $('#' + inputObj).attr("readonly", "readonly");

        /*if ( typeValue === 'sj' ) {
         $('#' + inputObj).val('失禁(*)');
         } else {
         $('#' + inputObj).val(typeName);
         }*/

        log(typeValue);

        switch (typeValue) {
            case 'sj':
            case 'rggm':
                $('#' + inputObj).val('*');
                $('#sheetForm input[data-key="urine"]').validatebox({
                    validType: '',
                    tipPosition: 'right'
                });
                break;

            /*case 'tfzhx':
                $('#' + inputObj).val('停辅');
                $('#sheetForm input[data-key="breath"]').validatebox({
                    validType: '',
                    tipPosition: 'right'
                });
                break;*/

            /*case 'fzhx':
                $('#' + inputObj).val('辅助');
                $('#sheetForm input[data-key="breath"]').validatebox({
                    validType: '',
                    tipPosition: 'right'
                });
                break;*/

            case '':
                break;

            default:
                if (objectName === 'pat_status') break;

                $('#' + inputObj).val(typeName);
                if (key) {
                    $('#sheetForm input[data-key="' + key + '"]').validatebox({
                        validType: '',
                        tipPosition: 'right'
                    });
                }

                break;
        }

        $('#' + inputObj).focus();
        $('#' + inputObj).validatebox({
            novalidate: true
        });
    } else {
        $('#' + inputObj).removeAttr("readonly");

        if (typeValue === 'ts' || typeValue.indexOf('tszl') > -1) {
            $('#' + inputObj).validatebox({
                novalidate: true
            });
        } else {
            $('#' + inputObj).validatebox(validateObj[key + 'Type']);
        }
    }
    //tl24
    if ($(selectObj).siblings('input').length > 1) {
        if (typeof $(selectObj).find(':selected').data('unit') !== 'undefined') {
            unitStr = $(selectObj).find(':selected').data('unit');
        } else {
            unitStr = $(selectObj).siblings('[name="bodySignDict.itemUnit"]').val();
        }

        $(selectObj).siblings('label').text(unitStr);
        $(selectObj).siblings('[name="bodySignDict.itemUnit"]').val(unitStr);
    }


    /*switch (selectObj.value) {
     case 'dn':
     unitStr = 'ml/c';
     //$('#measure-unit').text(unitStr);
     break;

     case 'tszlone':
     case 'tszltwo':
     unitStr = '';
     break;

     case 'sgone':
     case 'sgtwo':
     case 'fwone':
     case 'fwtwo':
     unitStr = 'cm';
     //$('#else1-unit').text('cm');
     //$('[name="bodySignDict.itemUnit"]').val(unitStr);
     break;

     default :
     unitStr = $(selectObj).siblings('label').text();
     }*/
}


/** 皮试药物初始化**/
function initSkinTestDrugs() {
    var skinTestDrugs = config.data.system.comSkinTestDrugs ? config.data.system.comSkinTestDrugs.split(",") : [];
    var drugHtml = "";
    $(skinTestDrugs).each(function(index, value) {
        drugHtml += '<option label="" value=' + value + '/>';
    });
    if (drugHtml != "")
        $("#drugName_list").append(drugHtml);
}
