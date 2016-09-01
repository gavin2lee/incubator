var _data = [];
var isLoading;
function loadRecordSuccess(data) {
}

function searchPatCrisValue() {
    getPatCrisValue();
}

/*function printPatCrisValueTable(){
 var htmlAllStr = document.body.innerHTML;
 document.body.innerHTML = $('#patCrisValContent').html();
 window.print();
 document.body.innerHTML = htmlAllStr;
 }*/

function getPatCrisValue() {
    $.loading();
    if (isLoading) {
        $.messager.alert('警告', '正在查询, 请勿重复操作! ');
        return;
    }
    isLoading = true;
    var startDate = $("#startDate").val();
    var endDate = $("#endDate").val();
    var status = $("#status").val();
    var pl = '';
    if (_patientId.length != 0) {
        pl = 'patientId=' + [_patientId] + '&';

    }
    if (!pl) {
        $("#select1").show();
        $("#searchVal").show();
    }
    if (startDate) {
        startDate = 'startDate=' + startDate + " 00:00:00" + '&';
    }
    if (endDate) {
        endDate = 'endDate=' + endDate + " 23:59:59" + '&';
    }
    var dataUrl = ay.contextPath + '/nur/crisisValue/getCriticalValue.do?' + pl + startDate + endDate;
    dataUrl = dataUrl.substring(0, dataUrl.length - 1);
    $.post(dataUrl, {}, function (data) {
        isLoading = false;
        $.loading('close');
        _data = data.data.list;
        for (var idx = 0; idx < _data.length; idx++) {
            /*if(_data[idx].criticalValue.split(':').length>1){
             _data[idx].checkName  =  _data[idx].criticalValue.split(':')[0];
             _data[idx].checkValue =  _data[idx].criticalValue.split(':')[1];
             }
             else{
             _data[idx].checkName  =  _data[idx].criticalValue.split('：')[0];
             _data[idx].checkValue =  _data[idx].criticalValue.split('：')[1];
             }*/
            //得到所有化验项目
            _data[idx].subject = '';
            if (_data[idx].labtestRecords.length != 0) {
                for (var sdx = 0; sdx < _data[idx].labtestRecords.length; sdx++) {
                    _data[idx].subject += _data[idx].labtestRecords[sdx].subject + ';';
                }
            }
            // detailData[idx] = _data[idx].labtestRecords;
        }
        $("#patCrisValueTable").datagrid({
            fit: true,
            fitColumns: true,
            pagination: true,
            pageSize: 50,
            remoteSort: false,
            sortName: "sendTime",
            sortOrder: "asc",
            rownumbers: true,
            singleSelect: true,
            columns: [[
                {
                    field: 'bedCode',
                    title: '床号',
                    sortable: true,
                    width: 60
                }, {
                    field: 'patientName',
                    title: '姓名',
                    sortable: true,
                    width: 80
                }, {
                    field: 'inHospNo',
                    title: '住院号',
                    sortable: true,
                    width: 80/*,
                     styler:function(value,row,index){
                     $("#patCrisValueTable").datagrid('')
                     }*/
                }, {
                    field: 'subject',
                    title: '检验项目',
                    sortable: true,
                    width: 150,
                    formatter: formateCriticalItem
                }, {
                    field: 'criticalValue',
                    title: '结果',
                    sortable: true,
                    width: 150
                }, {
                    field: 'sendPeople',
                    title: '发送人',
                    sortable: true,
                    width: 80
                }, {
                    field: 'sendTime',
                    title: '发送时间',
                    sortable: true,
                    width: 120
                }, {
                    field: 'dispose',
                    title: '处置人',
                    sortable: true,
                    width: 80
                }, {
                    field: 'doctorName',
                    title: '报告医生',
                    sortable: true,
                    width: 80
                }, {
                    field: 'receiveName',
                    title: '接报者',
                    sortable: true,
                    width: 80
                }, {
                    field: 'disposeTime',
                    title: '处置时间',
                    sortable: true,
                    width: 120
                }]],
            onHeaderContextMenu: function (e, field) {
                e.preventDefault();
                if (!cmenu) {
                    createColumnMenu();
                }
                cmenu.menu('show', {
                    left: e.pageX,
                    top: e.pageY
                });
            },
            onLoadSuccess: function (data) {
                console.log($(this).datagrid('getPanel').find('div.datagrid-view2 div.datagrid-body tr.datagrid-row'));
                var rows = $(this).datagrid('getPanel').find('div.datagrid-view2 div.datagrid-body tr.datagrid-row');
                rows.each(function (index) {
                    var maxHeight = $($(this).find('td div')[3]).height();
                    if (maxHeight > 25) {
                        $(this).find('td div').each(function () {
                            $(this).height(maxHeight);
                        });
                    }
                });

            },
            onClickRow: function (index, value) {
                $("#patCrisValBox").dialog('open');
                $("#patCrisDetail").datagrid({
                    data: value.labtestRecords,
                    fit: true,
                    fitColumns: true,
                    singleSelect: true,
                    view: detailview,
                    detailFormatter: function (index, row) {
                        return '<div style="padding:2px"><table class="ddv"></table></div>';
                    },
                    onClickRow: function (index, data) {
                        //console.log(index);
                    },
                    onExpandRow: function (index, row) {
                        var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
                        ddv.datagrid({
                            data: row.itemList,
                            fitColumns: true,
                            singleSelect: true,
                            rownumbers: true,
                            loadMsg: '',
                            height: 'auto',
                            columns: [[
                                {
                                    field: 'itemName',
                                    title: '项目名',
                                    sortable: true,
                                    width: 160
                                }, {
                                    field: 'itemCode',
                                    title: '项目编码',
                                    sortable: true,
                                    width: 80
                                }, {
                                    field: 'result',
                                    title: '结果',
                                    sortable: true,
                                    width: 80
                                }, {
                                    field: 'resultUnit',
                                    title: '单位',
                                    sortable: true,
                                    width: 80
                                }, {
                                    field: 'ranges',
                                    title: '范围',
                                    sortable: true,
                                    width: 100,
                                    formatter: function (value, row) {
                                        if (!value) {
                                            return '';
                                        }
                                        return value.replace('~~', '—');
                                    }
                                }, {
                                    field: 'normalFlag',
                                    title: '状态',
                                    sortable: true,
                                    width: 40,
                                    formatter: detailStatus,
                                    styler: detailColor
                                }]],
                            onResize: function () {
                                $('#patCrisDetail').datagrid('fixDetailRowHeight', index);
                            },
                            onLoadSuccess: function () {
                                setTimeout(function () {
                                    $('#patCrisDetail').datagrid('fixDetailRowHeight', index);
                                }, 0);
                            }
                        });
                        $('#patCrisDetail').datagrid('fixDetailRowHeight', index);
                    },
                    columns: [[
                        {
                            field: 'subject',
                            title: '项目名',
                            width: 160
                        }, {
                            field: 'specimen',
                            title: '项目样本',
                            width: 60
                        }, {
                            field: 'orderDoctorName',
                            title: '出单医师',
                            width: 60
                        }, {
                            field: 'requestDate',
                            title: '请求时间',
                            width: 140
                        }, {
                            field: 'reporterName',
                            title: '报告人',
                            width: 60
                        }, {
                            field: 'checkerName',
                            title: '检查人',
                            width: 80
                        }, {
                            field: 'testDate',
                            title: '测试日期',
                            width: 140
                        }
                    ]]
                })

            }
        });
        $("#patCrisValueTable").datagrid({loadFilter: pagerFilter}).datagrid('loadData', _data);
    });
}
function formateCriticalItem(val, row, index) {
    if (!val) {
        return '';
    }
    var tempArr = val.split(';'), tempStr = '';
    for (var i = 0; i < tempArr.length; i++) {
        tempStr += tempArr[i] + '<br>';
    }
    tempStr = tempStr.substring(0, tempStr.length - 4);
    return tempStr;
}
function detailColor(val, row, index) {
    if (val != 'N') {
        return 'color:#f00;';
    }
}
function detailStatus(val, row, index) {
    var statusStr = '';
    switch (val) {
        case 'N':
            statusStr = '正常';
            break;
        default:
            statusStr = '超标';
            break;
    }
    return statusStr;
}

function pagerFilter(data) {
    if (typeof data.length == 'number' && typeof data.splice == 'function') {    // is arrayw
        var dObj = {
            "bed": "bedCode",
            "name": "patientName",
            "hosNo": "inHospNo"
        }
        var select1 = $("#select1").val();
        var select1 = dObj[select1];
        var sv = $("#searchVal").val();
        if (select1 && sv) {
            for (var i = 0; i < data.length; i++) {
                if ('patientName' == select1) {
                    if (data[i][select1].indexOf(sv) < 0) {
                        data.splice(i, 1);
                        i -= 1;
                    }
                }
                else if (data[i][select1] != sv) {
                    data.splice(i, 1);
                    i -= 1;
                }
                else {

                }
            }
        }

        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage: function (pageNum, pageSize) {
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

function patCrisValBoxBtn() {
    $("#patCrisValBox").dialog('close');
}

$(function () {
    var recordDay = new Date().format("yyyy-MM-dd");
    $("#startDate").val(recordDay);
    $("#endDate").val(recordDay);
    var pid = $('#pid').val();
    getPatCrisValue();
    $(document).keypress(function (e) {
        var keyCode = e.which;
        if ('13' == keyCode) {
            searchPatCrisValue();
        }
    })
})