/**
 * Created by lin on 15/9/23.
 */
var log = console.log.bind(console);

function getData(url, deptCode, startDate, endDate) {

    $('#device-data-grid').datagrid({
        loadFilter:pagerFilter,
        remoteSort: false,
        rownumbers: true,
        singleSelect: true,
        fitColumns: true,
        columns: [[
            {
                field: 'alarmTime',
                title: "报警时间",
                width: 200,
                sortable: true,
                styler: function (value, row, index) {
                    if (value) {
                        return 'color:#000000';
                    } else {
                        return 'color:#ff0000';
                    }
                }
            },
            {
                field: 'alarmType',
                title: "类型",
                width: 200,
                styler: function (value, row, index) {
                    log(value);
                    switch (value) {
                        case '一级警告':
                            return 'color:#de4037';
                        case '二级警告':
                            return 'color:#fb3ab1';
                        case '三级警告':
                            return 'color:#36adef';
                        default :
                            break;
                    }
                }
            },
            {
                field: 'alarmContent',
                title: '报警内容',
                width: 300
            },
            {
                field: 'operPersonName',
                title: "处理人",
                width: 200
            },
            {
                field: 'operTime',
                title: "处理时间",
                width: 300,
                sortable: true
            }
        ]],
        pageNumber: 1,
        pagination: true,
        pageSize: 20
    });

    $.get(url, {
        deptCode: deptCode || 'all',
        startDate: startDate || '',
        endDate: endDate || ''
    }).done(function (data) {
        log(data);
        var list = data['data']['list'];
        buildData(list);
        //$('#device-data-grid').datagrid('loadData', list);
    });
}

function buildData(data) {
    var listData = [];
    log(data);
    $.each(data, function (i, val) {
        listData.push({
            alarmTime: val['alarmTime'],
            alarmType: (function () {
                var name = val['alarmType'];
                log(name);
                switch (name) {
                    case 'REST_CAP':
                    case 'DROP_SPEED_HIGH':
                    case 'DROP_SPEED_DOWN':
                    case 'REST_TIME':
                    case 'SKIN_TEST_TIME':
                        return '一级警告';
                    case 'PAUSE_ALARM':
                    case 'STOP_ALARM':
                        return '二级警告';
                    default:
                        return '三级警告';
                }
            })(),
            alarmContent: val['alarmContent'],
            operPersonName: val['operPersonName'],
            operTime: val['operTime']
        });
    });

    $('#device-data-grid').datagrid('loadData', listData);
}

function saveDeviceInfo(url) {
    var formData = ay.serializeObject($('#addDeviceForm'));

    //$('#addDeviceForm').form('validate');

    $.post(url, {
        itemDevice: JSON.stringify(formData)
    }).done(function (data) {
        log(data);
        var resCode = data['rslt'],
            resMsg = data['msg'];

        if (resCode === '-1') {
            alert('数据错误！' + resMsg);
        } else {
            $('#addNewDevice').dialog('close');
            alert(resMsg);
        }
        $('#addDeviceForm')[0].reset();
    }).fail(function (err) {
        alert('服务器错误！');
    });
}

function pagerFilter(data){
    if (typeof data.length == 'number' && typeof data.splice == 'function'){	// is array
        data = {
            total: data.length,
            rows: data
        }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
        onSelectPage:function(pageNum, pageSize){
            opts.pageNumber = pageNum;
            opts.pageSize = pageSize;
            pager.pagination('refresh',{
                pageNumber:pageNum,
                pageSize:pageSize
            });
            dg.datagrid('loadData',data);
        }
    });
    if (!data.originalRows){
        data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber-1)*parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
}

$(function () {

    var getAlarmInfoUrl = ay.contextPath + '/nur/infusionmanager/getInfusionAlarms',
        addDeviceInfo = ay.contextPath + '/nur/infusionmanager/getInfusionDepts/',
        $selection = $('#departmentList');

    var recordDay = new Date().format("yyyy-MM-dd"),
        year = new Date().getFullYear(),
        lastmonth = new Date().getMonth() - 1,
        yesterday = new Date().getDay();

    $("#startDate").val(new Date(year, lastmonth, yesterday).format("yyyy-MM-dd"));
    $("#endDate").val(recordDay);

    getData(getAlarmInfoUrl, '', '');

    $.get(addDeviceInfo).done(function (data) {
        log(data);
        var list = data['data']['list'],
            str = '<option selected="selected" value="">--请选择--</option>',
            bedArray = [];

        $.each(list, function (i, val) {
            var bedList = JSON.stringify(val['bedLstFormat']);
            log(typeof bedList);
            str += '<option data-deptcode=' + val['code'] + ' data-bedlist=' + bedList + ' value=' + val['id'] + '>' + val['name'] + '</option>';
        });

        $selection.html(str);
        log(str);

    });

    $('#search').on('click', function (e) {
        var deptCode = $selection.val(),
            startDate = $('#startDate').val(),
            endDate = $('#endDate').val();
        getData(getAlarmInfoUrl, deptCode, startDate, endDate);
    })

});