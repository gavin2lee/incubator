var datagridBody = $('.datagrid-body');
var initDataArr = [
    ['temperatureType', 'yw'],
    ['pulseType', 'moren'],
    ['heartRateType', 'moren'],
    ['breathType', 'moren'],
    ['bloodPressType', 'sz'],
    ['oxygenSaturationType', 'moren'],
    ['stoolType', 'zxpb'],
    ['totalInputType', 'moren'],
    ['urineType', 'moren'],
    ['weightType', 'moren']
];
var origData;
var origDataForBtn;
var foucusIndex;
var currentEndIndex;
var clickCellField;
var isUnsaved;
var originalJsonObj;
var unSavedIndex = [];
var isSyncDoc = false;


function editorCheck(index, field) {
    var opts = dataGridObj.dataGridOptions[field].opts;
    var rows = $('#info-tab').datagrid('getRows');
    var ed = null;
    var val = rows[index][field + 'Type'] || false;

    if (!val) {
        //设置默认选项
        if (fields == 'cooledTemperature') {
            infoTab.datagrid('getRows')[editIndex]['cooled'] = '是';
            if (opts) {
                rows[index][field + 'Type'] = opts[1].code;
            }
        } else if (fields == 'cooled') {
            infoTab.datagrid('endEdit', editIndex);
            //ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:'cooled'});
            if (infoTab.datagrid('getRows')[editIndex]['cooled'] == '否') {
                infoTab.datagrid('getRows')[editIndex]['cooledTemperature'] = '';
            }
            if (opts) {
                rows[index][field + 'Type'] = opts[1].code;
            }
        } else {
            rows[index][field + 'Type'] = opts[1].code;
        }

        //修复combobox提前关闭回调的hidepanel方法无法调用
        /*ed = $('#info-tab').datagrid('getEditor', {index:editIndex,field:field+'Type'});
         if(ed.type == 'combobox'){
         $(ed.target).combo('hidePanel');
         }
         console.log(ed);*/


        //$('#info-tab').datagrid('endEdit', editIndex);

        //判断是否输入值，若为空将Type内的已选项清除
        //TODO
        /*if(!rows[index][field]){
         rows[index][field+'Type'] = '';
         }*/

        // 刷新 没有结束editor状态的值无法应用到datagrid上
        //$('#info-tab').datagrid('refreshRow', index);
        //$('#info-tab').datagrid('endEdit', index);
        return 1;
    }
    for (var i = 0; i < opts.length; i++) {
        if (opts[i].code == val) {
            return opts[i].editFlag;
        }
    }
}

function endEditing(isSaveBtn) {
    $.messager.progress('close');
    infoTab.datagrid('endEdit', editIndex);
    if (editIndex == undefined) {
        return true
    }
    if (infoTab.datagrid('validateRow', editIndex)) {
        var ed = null;
        if (fields == 'drugName') {
            ed = infoTab.datagrid('getEditor', {
                index: editIndex,
                field: 'drugName'
            });
            if (ed) {
                var drugName = $(ed.target).combobox('getText');
                $(ed.target).combobox('setValue', drugName);
                infoTab.datagrid('getRows')[editIndex]['drugName'] = drugName;
            }
        }


        //$('#info-tab').datagrid('endEdit', editIndex);
        currentEndIndex = editIndex;

        editIndex = undefined;
        fields = undefined;


        // 结束编辑时保存  2016-06-28  by gary
        if (!isSaveBtn) {
            save();
        }
        return true;
    } else {
        return false;
    }
}

function onClickCell(index, field, params) {
    // index 行索引
    // field 列名 例如: pulse
    // params 值

    //缓存当前单击的cell的field
    //var cacheField = field;
    var rowData = infoTab.datagrid('getData').rows[index];
    fields = field;
    clickCellField = field;

    //过滤数组，
    var filterArray = [];

    log(index);

    // 手动转抄操作
    if (field === 'isCopy') {
        doCopy([rowData.patientId]);
    }

    origData = $.extend({}, infoTab.datagrid('getData').rows[editIndex]);

    origDataForBtn = $.extend({}, rowData);
}

function doCopy(patIds) {
    var params = {
        patIDs: JSON.stringify(patIds),
        deptCode: ay.getLocalData('deptCode'),
        date: new Date().format('yyyy-MM-dd'),
        time: new Date().format('hh:mm')
    };

    $.post(ay.contextPath + '/nur/bodySign/copyBodySignRecordsToDoc', params).done(function (response) {
        if (response.rslt == 0) {
            $.messager.show({
                title: '提示',
                msg: response.msg,
                timeout: 2000,
                showType: 'fade',
                style: {
                    right: '',
                    top: document.body.scrollTop + document.documentElement.scrollTop + 50,
                    bottom: ''
                }
            });
        }
    }).fail(function (err) {
        $.messager.alert('警告', '访问出错!');
    });
}

function initComboSection($field) {
    var gridBody = $('.datagrid-view2 .datagrid-body .datagrid-row');
    var rowIndex;

    initDataArr.forEach(function (item, i) {
        var $item = $('[field="' + item[0] + '"]').find('.combobox-f');

        if ($item.length !== 0) {

            if (!rowIndex) {
                rowIndex = $item.parents('.datagrid-row').attr('datagrid-row-index');
            }

            var selectedValue = $item.combo('getValue');

            if (selectedValue === '') {
                $item.combobox('select', item[1]);
            }
        }
    });

    gridBody.eq(editIndex).find('td[field="' + ($field || clickCellField || fields) + '"]').eq(0).find('input').focus();
}

function onClickRow(index, row) {
    bodyTempSheetCom.selectedPatientIndex = index;
    var time1, time2;

    if (editIndex != index) {
        time1 = new Date().getTime();

        editStart(index);
    }


}

function editStart(index) {
    if (endEditing()) {
        infoTab.datagrid('selectRow', index)
            .datagrid('beginEdit', index);

        origData = $.extend({}, infoTab.datagrid('getData').rows[currentEndIndex]);

        editIndex = index;

        initComboSection();
    } else {
        infoTab.datagrid('selectRow', editIndex);
    }

}

function comboFomatterFunc(n) {
    var idx = n;
    return function (value, row, index) {
        if (value == '') {
            return;
        }
        for (var i = 0; i < dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts.length; i++) {
            if (dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts[i].code == value) {
                return dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts[i].text;
            }
        }
    }
}

function selectFomatterFunc(n) {
    var idx = n;
    return function (record) {
        if (record.editFlag == 1) {
            var row = infoTab.datagrid('getSelected');
            var rindex = infoTab.datagrid('getRowIndex', row);
            onClickCell(rindex, dataGridObj.dataGridField[idx], {
                "disabled": false
            });
        } else {
            var row = infoTab.datagrid('getSelected');
            var rindex = infoTab.datagrid('getRowIndex', row);
            onClickCell(rindex, dataGridObj.dataGridField[idx], {
                "disabled": true,
                "val": record.text
            });
        }
    }
}


/**
 * 初始化表格默认数据
 */
function initDataGridColumns(data) {
    if (!dataGridColumns) {
        dataGridColumns = [
            [],
            []
        ];
        /*dataGridColumns[0].push({
         field: 'bedCode',
         align: 'center',
         rowspan: 2,
         width: '55',
         title: '床号',
         frozen: true
         });
         dataGridColumns[0].push({
         field: 'patientName',
         align: 'center',
         rowspan: 2,
         width: '55',
         title: '姓名',
         frozen: true
         });*/
        for (var idx = 0; idx < dataGridObj.dataGridField.length; idx++) {

            //var strLen = ay.getStrActualLen(dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].name);
            var strLen = dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].name.length;
            //var unitLen = ay.getStrActualLen(dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].unit);
            var unitLen = dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].unit.length;

            var lagestLen = Math.max(strLen, unitLen);
            var cellWidth = lagestLen * 13 + 8;

            /*if (initNameArr.indexOf(dataGridObj.dataGridField[idx]) > -1) {

             }*/

            if (['date', 'time'].indexOf(dataGridObj.dataGridField[idx]) >= 0) {
                continue;
            }
            ;

            if (dataGridObj.dataGridField[idx] == 'cooledTemperature') {
                dataGridColumns[0].push({
                    align: 'center',
                    title: '降温体温',
                    width: cellWidth
                });
                dataGridColumns[1].push({
                    field: 'cooledTemperature',
                    align: 'center',
                    title: '℃',
                    editor: {
                        type: 'validatebox',
                        options: {
                            validType: 'cooledTemp[35,42]'
                        }
                    }
                });
                /* dataGridColumns[1].push({field:'cooled',align:'center',width:'30',title:'是否',editor:{type:'checkbox',options:{on:'是',off:'否'}}}); */
            } else if (dataGridObj.dataGridField[idx] == 'copyFlag') {
                dataGridColumns[0].push({
                    field: 'copyFlag',
                    align: 'center',
                    title: '是否转抄',
                    rowspan: '2',
                    width: cellWidth,
                    editor: {
                        type: 'checkbox',
                        options: {
                            on: '是',
                            off: '否'
                        }
                    }
                });
            } else if (dataGridObj.dataGridField[idx] == 'skinTest') {
                dataGridColumns[0].push({
                    //colspan: 2,
                    align: 'center',
                    title: '皮试'
                });
                dataGridColumns[1]
                    .push({
                        field: 'drugName',
                        align: 'center',
                        width: cellWidth,
                        title: '药物名',
                        formatter: function (value, row) {
                            if (value == '') {
                                return '';
                            }
                            for (var i = 0; i < dataGridObj.dataGridOptions['skinTestInfo'].opts.length; i++) {
                                if (dataGridObj.dataGridOptions['skinTestInfo'].opts[i].code == value) {
                                    return dataGridObj.dataGridOptions['skinTestInfo'].opts[i].text;
                                }
                            }
                        },
                        editor: {

                            type: 'combobox',
                            //validType:"skinTest['skinTest']",
                            options: {
                                valueField: 'code',
                                textField: 'text',
                                onSelect: function (value) {
                                    /*if (value.text != '') {
                                     onClickCell(editIndex, 'recordDate')
                                     } else {
                                     if (infoTab.datagrid('getSelected').recordDate) {
                                     infoTab.datagrid('getSelected').recordDate = '';
                                     $('#info-tab').datagrid(
                                     'endEdit', editIndex);
                                     $('#info-tab')
                                     .datagrid('refreshRow',
                                     editIndex);
                                     }
                                     }*/
                                },
                                data: dataGridObj.dataGridOptions['skinTestInfo'].opts
                            }
                        }
                    });
                dataGridColumns[1].push({
                    field: 'testResult',
                    align: 'center',
                    width: cellWidth,
                    title: '结果',
                    formatter: function (value) {
                        var str = '';
                        str = value;
                        if (value == '阳') {
                            str = '<font color=red>' + value + '</font>';
                        }
                        return str;
                    },
                    editor: {
                        type: 'checkbox',
                        options: {
                            on: '阳',
                            off: '阴'
                        }
                    }
                });
            }
            /* else if (dataGridObj.dataGridField[idx] == 'OtherOne') {
             dataGridColumns[0].push({
             field: 'OtherOne',
             rowspan: 2,
             align: 'center',
             width: '80',
             title: '其他①',
             editor: {
             type: 'validatebox',
             options: {
             validType: "remark['remark']",
             invalidMessage: ""
             }
             }
             });
             } else if (dataGridObj.dataGridField[idx] == 'OtherTwo') {
             dataGridColumns[0].push({
             field: 'OtherTwo',
             rowspan: 2,
             align: 'center',
             width: '80',
             title: '其他②',
             editor: {
             type: 'validatebox',
             options: {
             validType: "remark['remark']",
             invalidMessage: ""
             }
             }
             });
             } */
            else if (dataGridObj.dataGridField[idx] == 'remark') {
                dataGridColumns[0].push({
                    field: 'remark',
                    rowspan: 2,
                    align: 'center',
                    width: '80',
                    title: '备注',
                    editor: {
                        type: 'validatebox',
                        options: {
                            validType: "remark['remark']",
                            invalidMessage: ""
                        }
                    }
                });
            } else if (dataGridObj.dataGridField[idx] == 'coolway') {
                dataGridColumns[0].push({
                    colspan: 1,
                    align: 'center',
                    title: '降温方式'
                });
                dataGridColumns[1]
                    .push({
                        field: 'coolwayCode',
                        align: 'center',
                        width: cellWidth,
                        title: '',
                        formatter: function (value, row) {
                            if (value == '') {
                                return '';
                            }
                            for (var i = 0; i < dataGridObj.dataGridOptions['coolway'].opts.length; i++) {
                                if (dataGridObj.dataGridOptions['coolway'].opts[i].code == value) {
                                    return dataGridObj.dataGridOptions['coolway'].opts[i].text;
                                }
                            }
                        },
                        editor: {
                            type: 'combobox',
                            options: {
                                valueField: 'code',
                                textField: 'text',
                                editable: false,
                                onSelect: function (value) {
                                    /* if (value.text != '空') {
                                     onClickCell(editIndex, 'recordDate')
                                     } else {
                                     if (infoTab.datagrid('getSelected').recordDate) {
                                     infoTab.datagrid('getSelected').recordDate = '';
                                     $('#info-tab').datagrid(
                                     'endEdit', editIndex);
                                     $('#info-tab')
                                     .datagrid('refreshRow',
                                     editIndex);
                                     }
                                     }*/
                                },
                                data: dataGridObj.dataGridOptions['coolway'].opts
                            }
                        }
                    });
            } else if (dataGridObj.dataGridField[idx] == 'pain') {
                // 事件
                dataGridColumns[0].push({
                    colspan: 1,
                    align: 'center',
                    title: '疼痛'
                });
                dataGridColumns[1]
                    .push({
                        field: 'painCode',
                        align: 'center',
                        width: cellWidth,
                        title: '',
                        formatter: function (value, row) {
                            if (value == '') {
                                return '';
                            }
                            for (var i = 0; i < dataGridObj.dataGridOptions['pain'].opts.length; i++) {
                                if (dataGridObj.dataGridOptions['pain'].opts[i].code == value) {
                                    return dataGridObj.dataGridOptions['pain'].opts[i].text;
                                }
                            }
                        },
                        editor: {
                            type: 'combobox',
                            options: {
                                valueField: 'code',
                                textField: 'text',
                                editable: false,
                                onSelect: function (value) {
                                    /*if (value.text != '空') {
                                     onClickCell(editIndex, 'recordDate')
                                     } else {
                                     if (infoTab.datagrid('getSelected').recordDate) {
                                     infoTab.datagrid('getSelected').recordDate = '';
                                     $('#info-tab').datagrid(
                                     'endEdit', editIndex);
                                     $('#info-tab')
                                     .datagrid('refreshRow',
                                     editIndex);
                                     }
                                     }*/
                                },
                                data: dataGridObj.dataGridOptions['pain'].opts
                            }
                        }
                    });
                // 其他
            } else if (dataGridObj.dataGridField[idx] == 'PPD') {
                // 事件
                dataGridColumns[0].push({
                    colspan: 1,
                    align: 'center',
                    title: 'PPD'
                });
                dataGridColumns[1]
                    .push({
                        field: 'PPDCode',
                        align: 'center',
                        width: cellWidth,
                        title: '',
                        formatter: function (value, row) {
                            if (value == '') {
                                return '';
                            }
                            for (var i = 0; i < dataGridObj.dataGridOptions['PPD'].opts.length; i++) {
                                if (dataGridObj.dataGridOptions['PPD'].opts[i].code == value) {
                                    return dataGridObj.dataGridOptions['PPD'].opts[i].text;
                                }
                            }
                        },
                        editor: {
                            type: 'combobox',
                            options: {
                                valueField: 'code',
                                textField: 'text',
                                editable: false,
                                onSelect: function (value) {
                                    /* if (value.text != '空') {
                                     onClickCell(editIndex, 'recordDate')
                                     } else {
                                     if (infoTab.datagrid('getSelected').recordDate) {
                                     infoTab.datagrid('getSelected').recordDate = '';
                                     $('#info-tab').datagrid(
                                     'endEdit', editIndex);
                                     $('#info-tab')
                                     .datagrid('refreshRow',
                                     editIndex);
                                     }
                                     }*/
                                },
                                data: dataGridObj.dataGridOptions['PPD'].opts
                            }
                        }
                    });
                // 其他
            } else if (dataGridObj.dataGridField[idx] == 'event') {
                // 事件
                dataGridColumns[0].push({
                    //colspan: 2,
                    align: 'center',
                    title: '事件'
                });
                dataGridColumns[1]
                    .push({
                        field: 'eventCode',
                        align: 'center',
                        width: cellWidth,
                        title: '事件名',
                        formatter: function (value, row) {
                            if (value == '') {
                                return '';
                            }
                            for (var i = 0; i < dataGridObj.dataGridOptions['event'].opts.length; i++) {
                                if (dataGridObj.dataGridOptions['event'].opts[i].code == value) {
                                    return dataGridObj.dataGridOptions['event'].opts[i].text;
                                }
                            }
                        },
                        editor: {
                            type: 'combobox',
                            options: {
                                valueField: 'code',
                                textField: 'text',
                                editable: false,
                                onSelect: function (value) {
                                    if (value.text != '空') {
                                        onClickCell(editIndex, 'recordDate')
                                    } else {
                                        if (infoTab.datagrid('getSelected').recordDate) {
                                            infoTab.datagrid('getSelected').recordDate = '';
                                            $('#info-tab').datagrid(
                                                'endEdit', editIndex);
                                            $('#info-tab')
                                                .datagrid('refreshRow',
                                                    editIndex);
                                        }
                                    }
                                },
                                data: dataGridObj.dataGridOptions['event'].opts
                            }
                        }
                    });
                dataGridColumns[1].push({
                    field: 'recordDate',
                    align: 'center',
                    width: cellWidth,
                    title: '时间',
                    editor: {
                        type: 'timespinner'
                    }
                });
                // 其他

            } else {
                console.log(dataGridObj.dataGridField[idx]);
                try {
                    dataGridColumns[0]
                        .push({
                            align: 'center',
                            //colspan: 2,
                            title: dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].name
                        });

                    var objTemp = {
                        field: dataGridObj.dataGridField[idx],
                        align: 'center',
                        width: cellWidth,
                        title: dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].unit,
                        editor: {
                            type: 'validatebox',
                            options: {
                                validType: dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].validFunction,
                                invalidMessage: dataGridObj.dataGridFieldName[dataGridObj.dataGridField[idx]].invalidMsg
                            }
                        }
                    }


                    dataGridColumns[1]
                        .push(objTemp);
                    /*dataGridColumns[1]
                     .push({
                     field: dataGridObj.dataGridField[idx] + 'Type',
                     align: 'center',
                     width: '55',
                     title: '类型',
                     formatter: comboFomatterFunc(idx),
                     editor: {
                     type: 'combobox',
                     options: {
                     editable: false,
                     valueField: 'code',
                     textField: 'text',
                     onSelect: selectFomatterFunc(idx),
                     data: dataGridObj.dataGridOptions[dataGridObj.dataGridField[idx]].opts
                     }
                     }
                     });*/
                } catch (er) {
                    console.log(er);
                }
            }
        }
    }
}

function setEnterTime(h) {
    var timeArr = dataGridObj.dataGridTime;
    var str = '';
    var temp = 24;
    var index = 0;
    for (var i = 0; i < timeArr.length; i++) {
        if (temp > Math.abs(parseInt(timeArr[i]) - h)) {
            temp = Math.abs(parseInt(timeArr[i]) - h);
            index = i;
        }
        str += '<option value="' + timeArr[i] + '">' + timeArr[i] + '</option>';
    }
    $("#time").append(str);
    $("#time").find('option:eq(' + index + ')').attr('selected', 'selected');
}

function getOptName(type, val) {
    var str = '';
    var data = dataGridObj.dataGridOptions[type];
    if (data) {
        var opts = data.opts;
        for (var i = 0; i < opts.length; i++) {
            if (opts[i].code == val) {
                return opts[i].text;
            }
        }
    }
}

/**
 * 选择患者查询生命体征
 * @param json
 */
function getPatBedId(selectJson) {
    selectPatJson = selectJson;
    //初始化查询日期
    var date = initQueryDate($("#startDate").datebox('getValue'), $("#selectTime").combobox('getValue'));
    getBodySignRecord(selectJson, date);
}

/**
 * 设置默认时间
 */
function setDefaultTime() {

}

/**
 * 检查值是否改变
 * @returns {boolean}
 */
function checkValueIsChanged(isSaveBtn) {
    var lastData = infoTab.datagrid('getData').rows[currentEndIndex];
    var prop;
    var isChanged = false;
    var comparedData = isSaveBtn ? origDataForBtn : origData;

    for (prop in lastData) {
        if (lastData.hasOwnProperty(prop)) {
            if (lastData[prop] !== comparedData[prop]) {
                if (typeof comparedData[prop] === 'undefined' && lastData[prop] === '') {
                    isChanged = false;
                } else {
                    return true;
                }
            }
        }
    }
}


/*
 * 增加输入行
 */

function addInputItems() {

    //新增输入列表时 结束editor保存数据到录入控件
    if (editIndex >= 0) {
        $('#info-tab').datagrid('endEdit', editIndex);
    }
    //选中的患者列表保存在父层内
    var peopleList = parent.peopleList;
    var bedInfoList = parent.bedInfoList;

    if (peopleList.length == 0) {
        json = null;
    } else {

        var arr = peopleList.toString().split(",");
        var datagridEnterData = infoTab.datagrid('getRows');

        var isInDatagrid = false;
        var json = "{\"total\":10,\"rows\":[";
        for (var i = 0; i < arr.length; i++) {
            for (var j = 0; j < bedInfoList.length; j++) {
                if (bedInfoList[j].patientId == arr[i]) {
                    for (var dax = 0; dax < datagridEnterData.length; dax++) {
                        if (datagridEnterData[dax].patientId == bedInfoList[j].patientId) {
                            json += "{";
                            for (x in datagridEnterData[dax]) {
                                json += '"' + x + '":"' + datagridEnterData[dax][x] + '",';
                            }
                            json = json.substring(0, json.length - 1);
                            json += "},";
                            isInDatagrid = true;
                        }
                    }
                    if (!isInDatagrid) {
                        json += "{\"bedCode\":\"" + bedInfoList[j].bedCode + "\",\"patientName\":\"" + bedInfoList[j].patientName + "\",\"inHospitalNo\":\"" + bedInfoList[j].inHospitalNo + "\",\"copyFlag\":\"是\",\"patientId\":\"" + bedInfoList[j].patientId + "\"},";
                    }
                    isInDatagrid = false;
                    //json = json + "{\"bedCode\":\""+bedInfoList[j].bedCode+"\",\"patientName\":\""+bedInfoList[j].patientName+"\",\"inHospitalNo\":\""+bedInfoList[j].inHospitalNo+"\",\"cooled\":\"否\",\"patientId\":\""+bedInfoList[j].patientId+"\"},";
                }
            }
        }
        json = json.substring(0, json.length - 1);
        if (json.length < 25) {
            json = json + "[]}";
        } else {
            json = json + "]}";
        }
    }
    json = $.parseJSON(json);
    getPatBedId(json);
}

/**
 * 重新初始化数据
 * @param json
 * @returns
 */
function initSelectJson(json) {
    if (json == null) {
        return null;
    }

    var processJson = "{\"total\":10,\"rows\":[";
    for (var i = 0; i < json.rows.length; i++) {
        processJson += "{\"bedCode\":\"" + json.rows[i].bedCode +
            "\",\"patientName\":\"" + json.rows[i].patientName +
            "\",\"inHospitalNo\":\"" + json.rows[i].inHospitalNo +
            "\",\"copyFlag\":\"是\",\"patientId\":\"" +
            json.rows[i].patientId + "\"},";
    }
    processJson = processJson.substring(0, processJson.length - 1);
    if (processJson.length < 25) {
        processJson = processJson + "[]}";
    } else {
        processJson = processJson + "]}";
    }
    processJson = $.parseJSON(processJson);
    return processJson;
}


function reset() {
    var selectRow = infoTab.datagrid('getSelected');
    var index = infoTab.datagrid('getRowIndex', selectRow);
    if (endEditing()) {
        $('#info-tab').datagrid('selectRow', index).datagrid('editCell', {
            index: editIndex,
            field: fields
        });
    }
    selectRow = infoTab.datagrid('getRows')[index];
    if (!selectRow) {
        $.messager.alert('提示', '没有选中的数据！');
        return;
    }
    var bedCode = selectRow.bedCode,
        copyFlag = selectRow.copyFlag,
        inHospitalNo = selectRow.inHospitalNo,
        patientId = selectRow.patientId,
        patientName = selectRow.patientName;
    for (x in selectRow) {
        if ('bedCode,copyFlag,inHospitalNo,patientId,patientName'.indexOf(x) < 0) {
            delete selectRow[x];
        }
    }
    infoTab.datagrid('acceptChanges');
    infoTab.datagrid('updateRow', {
        index: index,
        row: selectRow
    });
}

function checkValidType() {
    var editors = $('#info-tab').datagrid('getEditors', editIndex);
    var len = editors.length;
    var i;

    for (i = 0; i < len - 1; i += 1) {
        var tg = editors[i].target;
        var _value = editors[i].actions.getValue(tg);
        var typeValue;

        if (editors[i].field.indexOf('Type') < 0) {
            if (_value !== '') {
                if (editors[i + 1].field.indexOf(editors[i].field) > -1) {
                    typeValue = editors[i + 1].actions.getValue(editors[i + 1].target);
                    if (typeValue == '') {
                        return editors[i];
                    }
                }
            }
        }
    }

    return false;
}

function save(isSaveBtn) {
    var isChanged;
    if (isSaveBtn) {
        if (endEditing(isSaveBtn)) {
            $('#info-tab').datagrid('selectRow', editIndex).datagrid('editCell', {
                index: editIndex,
                field: clickCellField
            });
        } else {
            var ed = $('#info-tab').datagrid('getEditor', {
                index: editIndex,
                field: clickCellField
            });
            $(ed.target).focus();
            return;
        }
    }
    isChanged = checkValueIsChanged(isSaveBtn);

    if (unSavedIndex.length > 0) {
        if (!isChanged && !isSaveBtn) {
            return;
        }
    }

    if (!isChanged && unSavedIndex.length === 0) {
        if (isSaveBtn) {
            $.messager.alert('提示', '您未做任何更改.');
        }
        return;
    }

    var checkedEditor = checkValidType();
    if (checkedEditor) {
        checkedEditor.target.focus();
        $.messager.alert('警告', ('<strong>“' + dataGridObj.dataGridFieldName[checkedEditor.field].name + '”</strong>' || '') + '有值则其类型不能为空!');
        return;
    }
    //$.messager.progress();
    //让未退出编辑的编辑框退出编辑模式
    $('#info-tab').datagrid('endEdit', editIndex);

    var recordDay = $("#startDate").datebox("getValue");
    if (tempInputTimeShow == "true") {
        var recordTime = $("#time").timespinner('getValue');
    } else {
        var recordTime = $("#selectTime").combobox('getValue');
    }

    if (!recordTime || !recordDay) {
        $.messager.alert('提示', "请选择时间或录入时间点！");
        return;
    }
    recordTime += ":00";
    //var recordDate = $("#recordDate").val();

    var recordNurseName = $("#userName", window.parent.document).val();
    var recordNurseCode = $("#nurseCode", window.parent.document).val();
    var data = infoTab.datagrid('getData').rows;
    var itemTemp;
    var subItemTemp;
    //为空不保存
    if (len <= 0) {
        $.messager.alert('提示', "请选择需要体温单录入患者！");
        return;
    }
    var item = [];
    //var bodySignItemList = [];
    var rowData = null;
    //console.log(data);
    //var unSavedDataArr = [];
    var unSavedDataArr = [];
    var originalIndexArr = [];
    if (isSaveBtn) {
        unSavedDataArr = data.filter(function (dataItem, i) {
            if (dataItem.unSaved) {
                originalIndexArr.push(i);
                return true;
            }
            ;
        });
    }
    originalIndexArr.push(currentEndIndex);
    unSavedDataArr.push(data[currentEndIndex]);
    var len = unSavedDataArr.length;

    for (var i = 0; i < len; i++) {

        rowData = unSavedDataArr[i];

        /**
         * 2016-06-28 体征保存修改 by gary
         */
        itemTemp = {
            bedCode: rowData.bedCode,
            patientId: rowData.patientId,
            patientName: rowData.patientName,
            recordDay: recordDay,
            recordNurseCode: recordNurseCode,
            recordNurseName: recordNurseName,
            recordTime: recordTime,
            remark: '',
            copyFlag: '',
            cooled: '0',
            bodySignItemList: []
        };

        var defaultMeasure = {
            temperature: {
                code: 'yw',
                name: '腋温'
            },
            stool: {
                code: 'zxpb',
                name: '自主排便'
            },
            bloodPress: {
                code: 'sz',
                name: ' 上肢'
            },
            otherOutput: {
                code: 'otw',
                name: '呕吐物'
            }
        };

        //bodySignItemList = '"bodySignItemList":[';
        if (rowData.skinTestInfo) {
            var testResult = rowData.testResult == '阳' ? 'p' : 'n';

            itemTemp.skinTestInfo = {
                "drugName": rowData.skinTestInfo,
                //"drugCode": $("#drugName_list").val(),
                "orderGroupNo": "-1",
                "index": 0,
                "patientId": rowData.patientId,
                "patientName": rowData.patientName,
                "testNurseId": recordNurseCode,
                "testNurseName": recordNurseName,
                "testResult": testResult
            };

            /*item += '"skinTestInfo": {"drugName": "' + rowData.skinTestInfo + '","orderGroupNo": "-1",' +
             '"patientId": "' + rowData.patientId + '","patientName": "' + rowData.patientName + '",' +
             '"testNurseId": "' + recordNurseCode + '","testNurseName": "' + recordNurseName + '",' +
             '"testResult": "' + testResult + '"},';*/
        }


        for (var prop in rowData) {
            if (rowData.hasOwnProperty(prop)) {
                if (!(prop in itemTemp) &&
                    prop.indexOf('Type') == -1 &&
                    prop in dataGridObj.dataGridFieldName
                ) {
                    var typeName = dataGridObj.dataGridFieldName[prop].typeName;

                    if (rowData[prop] == '' && typeof originalJsonObj[originalIndexArr[i]][prop] !== 'undefined' && originalJsonObj[originalIndexArr[i]][prop] != '') {
                        subItemTemp = {
                            "abnormalFlag": 0,
                            "bodySignDict": {
                                "itemCode": prop,
                                "itemName": dataGridObj.dataGridFieldName[prop].name,
                                "itemUnit": dataGridObj.dataGridFieldName[prop] && dataGridObj.dataGridFieldName[prop].unit || null
                            },
                            "index": 0,
                            "measureNoteCode": rowData[typeName] == 'undefined' || !rowData[typeName] ? null : rowData[typeName],
                            "measureNoteName": getOptName(prop, rowData[typeName]) || null,
                            "unit": dataGridObj.dataGridFieldName[prop] && dataGridObj.dataGridFieldName[prop].unit || null,
                            "itemValue": rowData[prop],
                            "recordDate": rowData.recordDate && rowData.recordDate[prop] || recordDay + ' ' + recordTime
                        };

                        // 需要传默认类型的体征项
                        if (prop in defaultMeasure) {
                            subItemTemp.measureNoteCode = defaultMeasure[prop].code;
                            subItemTemp.measureNoteName = defaultMeasure[prop].name;
                        }

                        // 如果是降温体温
                        if (prop === 'cooledTemperature') {
                            itemTemp.cooled = 1;
                        }

                        itemTemp.bodySignItemList.push(subItemTemp);
                    }

                    if (rowData[prop] !== '') {
                        subItemTemp = {
                            "abnormalFlag": 0,
                            "bodySignDict": {
                                "itemCode": prop,
                                "itemName": dataGridObj.dataGridFieldName[prop].name,
                                "itemUnit": dataGridObj.dataGridFieldName[prop] && dataGridObj.dataGridFieldName[prop].unit || null
                            },
                            "index": 0,
                            "measureNoteCode": rowData[typeName] == 'undefined' || !rowData[typeName] ? null : rowData[typeName],
                            "measureNoteName": getOptName(prop, rowData[typeName]) || null,
                            "unit": dataGridObj.dataGridFieldName[prop] && dataGridObj.dataGridFieldName[prop].unit || null,
                            "itemValue": rowData[prop],
                            "recordDate": rowData.recordDate && rowData.recordDate[prop] || recordDay + ' ' + recordTime
                        };

                        // 需要传默认类型的体征项
                        if (prop in defaultMeasure) {
                            subItemTemp.measureNoteCode = defaultMeasure[prop].code;
                            subItemTemp.measureNoteName = defaultMeasure[prop].name;
                        }

                        // 如果是降温体温
                        if (prop === 'cooledTemperature') {
                            itemTemp.cooled = 1;
                        }

                        itemTemp.bodySignItemList.push(subItemTemp);
                    }

                }

                // 包含code的类型是类似于"降温方式"的下拉类型
                if (!(prop in itemTemp) &&
                    prop.indexOf('Type') == -1 && prop.indexOf('Code') >= 0 &&
                    rowData[prop] !== '') {
                    var _code = prop.replace('Code', '');
                    subItemTemp = {
                        "abnormalFlag": 0,
                        "bodySignDict": {
                            "itemCode": _code,
                            "itemName": _code,
                            "itemUnit": ''
                        },
                        "index": 0,
                        "measureNoteCode": rowData[prop],
                        "measureNoteName": getOptName(_code, rowData[prop]),
                        "itemValue": getOptName(_code, rowData[prop]),
                        "recordDate": rowData.recordDate && rowData.recordDate[prop] || recordDay + ' ' + recordTime
                    };

                    itemTemp.bodySignItemList.push(subItemTemp);

                }

            }
        }

        item.push(itemTemp);

        /**
         * end
         */

    }
    //item = item.substring(0, item.length - 1) + "]";
    //if (item.length < 223 * len) {
    //    $.messager.alert('提示', '您没有录入任何内容');
    //    $.messager.progress('close');
    //    return;
    //}
    var url = ay.contextPath + '/nur/bodySign/saveBodySignRecords.do';
    try {
        $.post(url, {
            records: JSON.stringify(item)
        }).done(function (data) {
            //超时等情况直接返回
            if (!data) {
                $.messager.alert('提示', '服务器响应超时');
                return;
            }
            if (data.rslt == 0) {

                $.messager.show({
                    title: '提示',
                    msg: data.msg,
                    timeout: 2000,
                    showType: 'fade',
                    style: {
                        right: '',
                        top: document.body.scrollTop + document.documentElement.scrollTop + 30,
                        bottom: ''
                    }
                });

                $.messager.progress('close');

                // 刷新界面, 保存失败的背景在保存成功后应该恢复
                unSavedIndex.forEach(function (idx) {
                    infoTab.datagrid('refreshRow', idx);
                });

                var existIndex = unSavedIndex.indexOf(currentEndIndex);
                if (existIndex >= 0) {
                    unSavedIndex.splice(existIndex, 1);
                }
                //addInputItems();
            } else {
                $.messager.show({
                    title: '提示',
                    msg: data.msg,
                    timeout: 2000,
                    showType: 'fade',
                    style: {
                        right: '',
                        top: document.body.scrollTop + document.documentElement.scrollTop + 30,
                        bottom: ''
                    }
                });
                setUnsavedStatus(isChanged);
            }
        }).fail(function (err) {
            $.messager.show({
                title: '错误',
                msg: '保存失败! ' + err.status + ' ' + err.statusText,
                timeout: 2000,
                showType: 'fade',
                style: {
                    right: '',
                    top: document.body.scrollTop + document.documentElement.scrollTop + 30,
                    bottom: ''
                }
            });
            setUnsavedStatus(isChanged);
        });
    } catch (e) {
        $.messager.alert('提示', e);
    }
}

function setUnsavedStatus(isCurrentChanged) {
    var rowObj = infoTab.datagrid('getData').rows[currentEndIndex];

    if (!isCurrentChanged) return;

    if ($.inArray(currentEndIndex, unSavedIndex) == -1) {
        unSavedIndex.push(currentEndIndex);
    }

    rowObj.unSaved = true;

    infoTab.datagrid('updateRow', {
        index: currentEndIndex,
        rows: rowObj
    });

    infoTab.datagrid('refreshRow', currentEndIndex);
}

/** 皮试药物初始化
 *    "skinTestInfo":{
    			"opts":[
    				{ "val":"","text":"无"},
    				{ "val":"青霉素","text":"青霉素"},
    				{ "val":"链霉素","text":"链霉素"},
    				{ "val":"破伤风抗毒素血清","text":"破伤风抗毒素血清"}
    			]
    		}
 * **/
function initSkinTestDrugsData() {
    var skinTestDrugs = config.data.system.comSkinTestDrugs ? config.data.system.comSkinTestDrugs.split(",") : [];
    var stDrugs = [];

    if (!dataGridObj.dataGridOptions['skinTestInfo']) return;

    $(skinTestDrugs).each(function (index, value) {
        var drug = {};
        if ("无" == value) {
            drug.code = "";
        } else {
            drug.code = value;
        }
        drug.text = value;
        stDrugs.push(drug);
    });

    dataGridObj.dataGridOptions['skinTestInfo'].opts = stDrugs;
}

/**
 * 初始化体征数据
 * @param data            :返回生命体征值
 * @param originalJson    :原始患者信息集合
 */
function initBodySignRecord(data, originalJson, patIds) {
    var cacheData = data.data ? data.data.lst : [];
    // 记录有生命体征的患者
    var json = "{\"total\":10,\"rows\":[";
    var bodySignRecordList = cacheData;
    // 把json拼接改成对象   2016-07-07  by gary
    var jsonObj = {
        total: 10,
        rows: []
    };
    originalJsonObj = [];
    var objTemp;

    //
    //               坑已填平
    /************------------***************
     *           *   巨坑    *
     *            *       *
     *           	  *   *
     * *******************************＊＊＊*/

    // 删除包含生命体征记录的患者Id
    var originalRows = originalJson.rows;
    // 添加没有体征记录的数据
    for (var i = 0; i < originalRows.length; i++) {
        //记录添加记录的位置
        var index = -1;
        for (var x = 0; x < bodySignRecordList.length; x++) {
            // 当获取已添加的信息
            if (bodySignRecordList[x].patientId == originalRows[i].patientId) {
                index = x;
                break;
            }
        }
        if (index == -1) {
            // 添加没有体征记录的数据
            objTemp = {
                bedCode: originalRows[i].bedCode,
                patientId: originalRows[i].patientId,
                patientName: originalRows[i].patientName
            };

            if (!isSyncDoc) {
                objTemp.isCopy = '<a href=\'javascript:;\' class=\'_btn _btn-proper copyTo\'>转抄</a>';
            }

            jsonObj.rows.push(objTemp);
            originalJsonObj.push(objTemp);
        } else {
            objTemp = {
                bedCode: originalRows[i].bedCode,
                patientId: originalRows[i].patientId,
                patientName: originalRows[i].patientName,
                recordTime: bodySignRecordList[index].recordTime,
                masterRecordId: bodySignRecordList[index].masterRecordId,
                recordDate: {}
            };

            if (!isSyncDoc) {
                objTemp.isCopy = '<a href=\'javascript:;\' class=\'_btn _btn-proper copyTo\'>转抄</a>';
            }

            var bodySignItemList = bodySignRecordList[index].bodySignItemList;
            if (bodySignItemList)
                for (var y = 0; y < bodySignItemList.length; y++) {
                    if (bodySignItemList[y].bodySignDict &&
                            // typeof bodySignItemList[y].measureNoteCode !== 'undefined' &&
                        bodySignItemList[y].itemValue != 'undefined') {

                        //if ()

                        if (bodySignItemList[y].recordDate) {
                            objTemp.recordDate[bodySignItemList[y].bodySignDict.itemCode] = bodySignItemList[y].recordDate;
                        }
                        var typeName = dataGridObj.dataGridFieldName[bodySignItemList[y].bodySignDict.itemCode] && dataGridObj.dataGridFieldName[bodySignItemList[y].bodySignDict.itemCode].typeName;

                        objTemp[bodySignItemList[y].bodySignDict.itemCode] = bodySignItemList[y].itemValue;

                        if (typeName) {
                            objTemp[typeName] = bodySignItemList[y].measureNoteCode;
                        }
                    }
                }

            var recordTime = bodySignRecordList[index].recordTime;

            objTemp.recordDay = bodySignRecordList[index].recordDay;
            objTemp.recordTime = recordTime.substring(0, recordTime.lastIndexOf(':'));
            objTemp.copyFlag = bodySignRecordList[index].copyFlag == 'Y' ? "是" : '否';

            if (bodySignRecordList[index].skinTestInfo &&
                bodySignRecordList[index].skinTestInfo.drugName) {
                objTemp.drugName = bodySignRecordList[index].skinTestInfo.drugName;
                objTemp.testResult = bodySignRecordList[index].skinTestInfo.testResult == 'p' ? '阳' : '阴';
            }
            if (bodySignRecordList[index].event &&
                bodySignRecordList[index].event.eventCode) {
                objTemp.eventCode = bodySignRecordList[index].event.eventCode;

                var eventRecordDate = bodySignRecordList[index].event.recordDate;
                if (eventRecordDate) {
                    objTemp.recordDate = eventRecordDate;
                }
            }
            if (bodySignRecordList[index].remark) {
                objTemp.remark = bodySignRecordList[index].remark;
            }

            jsonObj.rows.push(objTemp);
            originalJsonObj.push($.extend(true, {}, objTemp));

            if (bodySignRecordList[index].skinTestInfo &&
                bodySignRecordList[index].skinTestInfo.drugName) {

                var skinResult = bodySignRecordList[index].skinTestInfo.testResult,
                    skindrugName = bodySignRecordList[index].skinTestInfo.drugName;
                if (skinResult != 'undefined') {
                    if (skinResult == "N") {
                        skinResult = skindrugName + "-阴";
                    } else {
                        skinResult = skindrugName + "-阳";
                    }
                    skinResult += "-" +
                        bodySignRecordList[index].skinTestInfo.drugBatchNo;
                    skinResult += "-" +
                        bodySignRecordList[index].skinTestInfo.drugCode;
                } else {
                    skinResult = "";
                }
            }
        }
    }
    if (json == null) {
        $('#info-tab').datagrid('loadData', {
            total: 0,
            rows: []
        });
    } else {
        $("#info-tab").datagrid({
            data: jsonObj.rows
        });
    }
}

/**
 * 根据时间点和患者(,隔开的字符串)获取生命体征
 */
function getBodySignRecord(selectJson, date) {
    bodyTempSheetCom.patArr = [];

    //未选择患者，显示空列表
    if (selectJson == null) {
        infoTab.datagrid({
            data: []
        });
        return;
    }

    var rows = selectJson.rows;
    var patIds = [];
    for (var i = 0; i < rows.length; i++) {
        bodyTempSheetCom.patArr[Number(rows[i].bedCode)] = rows[i].patientId;
        patIds.push(rows[i].patientId);
    }
    var patId = patIds.join("-");


    if (patId == "") {
        infoTab.datagrid({
            data: []
        });
        return;
    }

    editIndex = undefined;

    var url = ay.contextPath + '/nur/bodySign/getFixTimeBodySignRecords.do?patIds=' + patId + "&date=" + date + "&isAllItems=" + true;
    $.get(url, {}, function (data) {
        if (data && data.data.lst.length >= 0) {
            initBodySignRecord(data, selectJson, patIds);
        }
    });
}

/**
 * 初始化查询日期
 */
function initQueryDate(date, time) {
    if (!date || date == "") {
        date = new Date().format("yyyy-MM-dd");
    }

    var queryDate = date + " " + time + ":00";
    return queryDate;
}
/**
 * 查看体温单
 */
function showPatientTempSheet() {
    var rowSelected = $('#info-tab').datagrid('getSelected'),
        selectedBedCode = rowSelected &&
            rowSelected['bedCode'],
        selectedPat = bodyTempSheetCom.patArr[Number(selectedBedCode)];

    if (!selectedBedCode) {
        if (parent.peopleList.length == 0) {
            $.messager.alert('提示', "您没有选择患者，请先选择患者！");
            return;
        }
        // 默认选择第一个患者
        for (var i = 0; i < parent.peopleList.length; i++) {
            if ("" != parent.peopleList[i]) {
                selectedPat = parent.peopleList[i];
                break;
            }
        }
    }
    var startDate = $("#startDate").datebox('getValue');

    window.parent.window.showTempSheetPanel(null, startDate, selectedPat);
}


/*
 * 删除行
 *
 */
function deleteInfo() {
    /*	var skinArr;
     var eventArr;*/
    var selectRow = $("#info-tab").datagrid('getSelected');
    if (!selectRow) {
        $.messager.alert('提示', '请选择要删除的数据行!');
        return;
    }

    $.messager.confirm('提醒', '确定要删除该行数据吗?', function (r) {
        if (r) {
            var index = $("#info-tab").datagrid('getRowIndex', selectRow);
            var record = {};
            record.patientId = selectRow.patientId;
            record.fullDateTime = selectRow.recordDay + " " + selectRow.recordTime + ":00";
            $.post(ay.contextPath + '/nur/bodySign/delete.do', {
                record: JSON.stringify(record)
            }, function (data) {
                console.log(data);
                try {
                    if (data.rslt == 0) {
                        //						$('#info-tab').datagrid('deleteRow',index);
                        var date = initQueryDate($("#startDate").datebox('getValue'), $("#selectTime").combobox('getValue'));
                        getBodySignRecord(initSelectJson(selectPatJson), date);
                        $.messager.alert('提示', '删除成功');
                    }
                } catch (e) {
                    console.log("error:" + e);
                }
            });
        }
    });
}

//(function ($) {
var editIndex = undefined, //编辑行索引
    fields = '', //编辑单元格field名
    config = $.parseJSON(localStorage.getItem("config")) || null,
    tempInputTimeShow = null,
    infoTab = null,
    selectPatJson = null,
/*	selectOldValue = {},*/
/*	oldPeopleList = [],*/
/*	isChecked = false,*/
    dataGridColumns = null; //数据表格渲染项

var log = console.log.bind(console);
var bodyTempSheetCom = {
    selectedPatientIndex: null,
    patArr: []
};
var getConfigUrl = ay.contextPath + '/nur/bodySign/getBodySignConfigByDeptCode.do';

var dataGridObj = {
    dataGridTime: ['01:00', '03:00', '05:00', '07:00', '09:00', '11:00', '13:00', '15:00', '17:00', '19:00', '21:00', '23:00'],
    dataGridField: [],
    dataGridFieldName: {},
    dataGridOptions: {}
};
$.extend($.fn.datagrid.defaults.editors, {
    timespinner: {
        init: function (container, options) {
            var input = $('<input class="easyuitimespinner">').appendTo(container);
            return input.timespinner(options);
        },
        destroy: function (target) {
            $(target).timespinner('destroy');
        },
        getValue: function (target) {
            return $(target).timespinner('getValue');
        },
        setValue: function (target, value) {
            $(target).timespinner('setValue', value);
        },
        resize: function (target, width) {
            $(target).timespinner('resize', width);
        }
    }
});
$.extend($.fn.datagrid.methods, {
    keyCtr: function (jq) {
        return jq.each(function () {
            var grid = $(this);
            var $focus;
            grid.datagrid('getPanel').panel('panel').attr('tabindex', 1).bind('keydown', function (e) {
                $focus = $(document.activeElement);
                var $focusTd = $focus.parents('[field]');
                var $filed = $focusTd.attr('field');

                clickCellField = $filed;

                switch (e.keyCode) {
                    case 38: // up
                        var selected = grid.datagrid('getSelected');
                        origData = $.extend({}, infoTab.datagrid('getData').rows[editIndex]);
                        if (endEditing()) {

                            if (selected) {
                                var index = grid.datagrid('getRowIndex', selected);
                                grid.datagrid('endEdit', index);
                                grid.datagrid('selectRow', index - 1).datagrid('beginEdit', index - 1);
                                editIndex = index - 1;
                            } else {
                                var rows = grid.datagrid('getRows');
                                grid.datagrid('endEdit', rows.length - 1);
                                grid.datagrid('selectRow', rows.length - 1).datagrid('beginEdit', rows.length - 1);
                                editIndex = rows.length - 1;
                            }
                            initComboSection($filed);
                        }
                        origData = $.extend({}, infoTab.datagrid('getData').rows[editIndex]);
                        break;
                    case 40: // down
                        var selected = grid.datagrid('getSelected');
                        origData = $.extend({}, infoTab.datagrid('getData').rows[editIndex]);
                        if (endEditing()) {
                            if (selected) {
                                var index = grid.datagrid('getRowIndex', selected);
                                grid.datagrid('endEdit', index);
                                grid.datagrid('selectRow', index + 1).datagrid('beginEdit', index + 1);
                                editIndex = index + 1;
                            } else {
                                grid.datagrid('selectRow', 0).datagrid('beginEdit', 0);
                                editIndex = 0;
                            }
                            initComboSection($filed);
                        }
                        origData = $.extend({}, infoTab.datagrid('getData').rows[editIndex]);
                        break;

                    case 37: // left
                        $focusTd.prev().find('input').focus();
                        break;

                    case 39: // right
                        $focusTd.next().find('input').focus();
                        break;
                    case 13:
                    case 108: // enter 保存
                        var selected = grid.datagrid('getSelected');
                        origData = $.extend({}, infoTab.datagrid('getData').rows[editIndex]);

                        if (endEditing()) {

                            if (selected) {
                                var index = grid.datagrid('getRowIndex', selected);
                                grid.datagrid('endEdit', index);
                                grid.datagrid('selectRow', index + 1).datagrid('beginEdit', index + 1);
                                editIndex = index + 1;
                            } else {
                                grid.datagrid('selectRow', 0).datagrid('beginEdit', 0);
                                editIndex = 0;
                            }

                            initComboSection(dataGridObj.dataGridField[0]);
                        }
                        origData = $.extend({}, infoTab.datagrid('getData').rows[editIndex]);
                        break;
                }
            });
        });
    }
});
$.extend($.fn.datagrid.methods, {
    editCell: function (jq, param) {
        return jq.each(function () {
            var opts = $(this).datagrid('options');
            var fields = $(this).datagrid('getColumnFields', true).concat($(this).datagrid('getColumnFields'));
            for (var i = 0; i < fields.length; i++) {
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor1 = col.editor;
                if (fields[i] != param.field) {
                    col.editor = null;
                }
            }
            // debugger;
            $(this).datagrid('beginEdit', param.index);
            for (var i = 0; i < fields.length; i++) {
                var col = $(this).datagrid('getColumnOption', fields[i]);
                col.editor = col.editor1;
            }
        });
    }
});

function init(callback) {
    getConfig().then(function (response) {
        if (response.rslt == 0) {
            transformTempConfig(response.data.bodySignConfig);
            initDataGridColumns(response.data.bodySignConfig);
            initSkinTestDrugsData();
            isSyncDoc = response.data.isSyncDoc;
            callback();
        } else {
            $.messager.alert('提示', '获取配置失败');
        }
    })
}

function transformTempConfig(data) {
    var obj = {};

    data.forEach(function (dataItem) {
        var code = dataItem.code;

        // 坑 ! otherOne和otherTwo在客户端首字母都是大写, 需要转换!
        if (dataItem.code === 'otherOne' || dataItem.code === 'otherTwo') {
            code = code.replace(/^\w{1}/g, function (match) {
                return match.toUpperCase()
            });
        }

        dataGridObj.dataGridFieldName[code] = dataItem.fields;
        dataGridObj.dataGridOptions[code] = {};
        dataGridObj.dataGridOptions[code].opts = dataItem.opts;
        dataGridObj.dataGridField.push(code);

        //obj[code] = dataItem.fields;
        //obj[code] = dataItem.fields;
    });

    return obj;
}

/*
 * 获取批量体征的初始化数据
 * */
function getConfig() {
    return $.get(getConfigUrl, {
        deptCode: localStorage.getItem('deptCode')
    }).done(function (response) {
        return response;
    })
}

/**
 * 进入界面加载
 */
$(function () {
    //dataGridObj.dataGridField = (config.data.system.tempInputItemsBase + ',' + config.data.system.tempInputItemsOther).split(',');
    init(function () {
        //initDataGridColumns();
        infoTab = $("#info-tab");
        var columns = [{
            field: 'bedCode',
            align: 'center',
            rowspan: 2,
            width: '55',
            title: '床号'
        }, {
            field: 'patientName',
            align: 'center',
            rowspan: 2,
            width: '55',
            title: '姓名'
        }];
        var contentHeight = $(window).height() - $('.top-tools').height();
        var recordDay = new Date().format("yyyy-MM-dd");

        $("#startDate").datebox({
            onChange: function (newValue, oldValue) {
                getBodySignRecord(initSelectJson(selectPatJson), initQueryDate(newValue, $("#selectTime").combobox('getValue')));
            }
        });
        //设置默认日期
        $("#startDate").datebox('setValue', recordDay);
        //时间配置项目
        tempInputTimeShow = config.data.system.tempInputTimeShow;
        var timeSelector = config.data.system.tempInputTimeSelector.split(',');
        var timeSelectObj = [];
        for (var i = 0; i < timeSelector.length; i++) {
            timeSelectObj.push({
                "id": timeSelector[i],
                "text": timeSelector[i]
            });
        }

        if (isSyncDoc) {
            $('#copyAll').hide();
        } else {
            columns.unshift({
                field: 'isCopy',
                align: 'center',
                rowspan: 2,
                width: '55',
                title: '转抄'
            });
        }

        // 全部转抄操作
        $('#copyAll').on('click', function () {
            doCopy(parent.peopleList);
        });


        $("#selectTime").combobox({
            data: timeSelectObj,
            valueField: 'id',
            textField: 'text',
            onChange: function (nv, ov) {
                if (tempInputTimeShow == "true") {
                    $('#time').timespinner('setValue', nv);
                }
                if (nv) {
                    $(this).combobox('select', nv);
                } else {
                    $(this).combobox('select', ov);
                }
                // 获取体征数据
                getBodySignRecord(initSelectJson(selectPatJson), initQueryDate($("#startDate").datebox("getValue"), nv));
            }

        });
        //设置默认时间
        $("#selectTime").combobox('select', timeSelectObj[1].id);


        if (tempInputTimeShow == "true") {
            $("#time").show();
            $("#time").timespinner({
                showSeconds: false
            });
        } else {

        }
        //setEnterTime(new Date().getHours());
        //$("#time").val(time);
        $("#recordDate").val(recordDay);
        if (document.body.childNodes[0].nodeType == 3) {
            document.body.removeChild(document.body.childNodes[0]);
        }
        $('.content').height(contentHeight);

        infoTab.datagrid({
            resizable: false,
            //fit: true,
            singleSelect: true,
            frozen: true,
            onClickCell: onClickCell, // 编辑单元
            onClickRow: onClickRow, // 编辑行
            frozenColumns: [
                columns,
                []
            ],
            height: contentHeight - 22,
            columns: dataGridColumns,
            rowStyler: function (index, row) {
                if (row.unSaved) {
                    return 'background: #ffc4c4';
                }
            },
            onBeginEdit: function (index, row ,changes) {
              var $row = $('[datagrid-row-index="' + index + '"]').eq(1);
              var $invalidInputs = $row.find('.validatebox-invalid');

              $invalidInputs.each(function () {
                $(this).validatebox('disableValidation');
              });
              // debugger;
            }
        });
        infoTab.datagrid('keyCtr');
    });

});

(function ($) {
    function getCacheContainer(t) {
        var view = $(t).closest('div.datagrid-view');
        var c = view.children('div.datagrid-editor-cache');
        if (!c.length) {
            c = $('<div class="datagrid-editor-cache" style="position:absolute;display:none"></div>').appendTo(view);
        }
        return c;
    }

    function getCacheEditor(t, field) {
        var c = getCacheContainer(t);
        return c.children('div.datagrid-editor-cache-' + field);
    }

    function setCacheEditor(t, field, editor) {
        var c = getCacheContainer(t);
        c.children('div.datagrid-editor-cache-' + field).remove();
        var e = $('<div class="datagrid-editor-cache-' + field + '"></div>').appendTo(c);
        e.append(editor);
    }

    var editors = $.fn.datagrid.defaults.editors;
    for (var editor in editors) {
        var opts = editors[editor];
        (function () {
            var init = opts.init;
            opts.init = function (container, options) {
                var field = $(container).closest('td[field]').attr('field');
                var ed = getCacheEditor(container, field);
                if (ed.length) {
                    ed.appendTo(container);
                    return ed.find('.datagrid-editable-input');
                } else {
                    return init(container, options);
                }
            }
        })();
        (function () {
            var destroy = opts.destroy;
            opts.destroy = function (target) {
                if ($(target).hasClass('datagrid-editable-input')) {
                    var field = $(target).closest('td[field]').attr('field');
                    setCacheEditor(target, field, $(target).parent().children());
                } else if (destroy) {
                    destroy(target);
                }
            }
        })();
    }
})(jQuery);

//})(jQuery);

/*var patternEditorId = "patternEditor";*/
