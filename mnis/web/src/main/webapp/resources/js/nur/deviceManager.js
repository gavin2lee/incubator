/**
 * Created by lin on 15/9/23.
 */
var log = console.log.bind(console);

var getDeviceUrl = ay.contextPath + '/nur/infusionmanager/getInfusionDevices/',
    getDeviceSettingInfoUrl = ay.contextPath + '/nur/infusionmanager/getInfusionDeviceSetInfoByMacaddress',
    addDeviceInfo = ay.contextPath + '/nur/infusionmanager/getInfusionDepts/',
    saveDeviceUrl = ay.contextPath + '/nur/infusionmanager/saveInfusionDevice/',
    settingDeviceUrl = ay.contextPath + '/nur/infusionmanager/saveInfusionDeviceSetInfo/',
    delDeviceUrl = ay.contextPath + '/nur/infusionmanager/delDevice',
    deviceUnbindUrl = ay.contextPath + '/nur/infusionmanager/setMathDevicePatient/',
    $selection = $('#departmentList'),
    isNew = false,
    seqId;

var responseData;

function getData(url, type, keyword) {

    $('#device-data-grid').datagrid({
        remoteSort: false,
        rownumbers: true,
        singleSelect: true,
        fitColumns: true,
        columns: [[
            {
                field: 'deviceName',
                title: "设备名称",
                width: 200,
                /*formatter:function(value){
                 if(value){
                 return "已打印";
                 }
                 else{
                 return "未打印";
                 }
                 },*/
                styler: function (value, row, index) {
                    if (value) {
                        return 'color:#000000';
                    } else {
                        return 'color:#ff0000';
                    }
                }
            },
            {
                field: 'deviceModel',
                title: "型号",
                width: 200
            },
            {
                field: 'deviceSn',
                title: '设备号',
                width: 300
            },
            {
                field: 'deptName',
                title: "所属科室",
                width: 200
            },
            {
                field: 'opBtns',
                title: "操作",
                width: 300
            }
        ]],
        pageNumber: 1,
        pagination: true,
        pageSize: 20
    });

    $.get(url, {
        keyType: type || '',
        keyword: keyword || ''
    }).done(function (data) {
        //log(data);
        var list = data['data']['list'];
        buildData(list);
        responseData = list;
    });
}

/*
 * http://localhost:8919/mnis/nur/infusionmanager/getInfusionDeviceInfoByMacaddress?macAddress=G2:19:10:T5:92:DD
 * */

function getDeviceInfo(url, macAddr, callback) {
    $.getJSON(url, {
        macAddress: macAddr
    }).done(function (data) {
        log(data);
        if (data['rslt'] === '0' && !!data['data']) {

            callback(data['data']);
        } else {
            alert('出现错误！');
        }
        log(data);
    }).fail(function (err) {
        alert('服务器错误');
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

function buildData(data) {
    var listData = [];
    $.each(data, function (i, val) {
        listData.push({
            deviceName: val['deviceName'],
            deviceLnk: val['deviceLnk'],
            deviceModel: val['deviceModel'],
            deviceSn: val['deviceSn'],
            inHospNo: val['inHospNo'],
            deptName: val['deptName'],
            opBtns: '<a type="button" href="javascript:;" class="btn update-this" onclick="updateItem(this)">编辑</a>' +
            '<a type="button" class="btn delete-this" href="javascript:;" onclick="deleteItem(this)">删除</a>' +
            '<a type="button" class="btn setting-this" href="javascript:;" onclick="settingItem(this)">设置</a>'
        });
    });

    //$('#device-data-grid').datagrid('loadData', listData);
    $('#device-data-grid').datagrid({
        loadFilter:pagerFilter,
        data: listData,
        view: detailview,
        detailFormatter: function (index, row) {
            return '<div class="d-wrapper" style="padding:5px 0"></div>';
        },
        onExpandRow: function (index, row) {
            var detailObj = data[index];
            var ddv = $(this).datagrid('getRowDetail', index).find('div.d-wrapper');

            ddv.panel({
                content: '<ul class="device-detail-list clearfix">' +
                '			<li class="fl">                           ' +
                '				<span class="item-name device-name">设备名称：</span>            ' +
                '				<span class="item-value">' + detailObj['deviceName'] + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">型号：</span>            ' +
                '				<span class="item-value">' + detailObj['deviceModel'] + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">设备序列号：</span>            ' +
                '				<span class="item-value">' + detailObj['deviceSn'] + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">蓝牙mac地址：</span>            ' +
                '				<span class="item-value dev-mac-addr">' + detailObj['macAddress'] + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">所属科室：</span>            ' +
                '				<span class="item-value">' + detailObj['deptName'] + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">绑定患者：</span>            ' +
                '				<span class="item-value">' + (detailObj['patName'] || '--') + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">剩余电量：</span>            ' +
                '				<span class="item-value">' + detailObj['restKwh'] + '％</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">连接状态：</span>            ' +
                '				<span class="item-value">' + (detailObj['deviceLnk'] === '1' ? '<span style="color: #6DAF07;">在线</span>' : '<span style="color: red;">离线</span>') + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">负重：</span>            ' +
                '				<span class="item-value">' + (detailObj['bearloadStatus'] === '0' ? '无' : detailObj['bearloadStatus'] + 'g' ) + '</span>            ' +
                '			</li>                          ' +
                '			<li class="fl">                           ' +
                '				<span class="item-name">连接蓝桥：</span>            ' +
                '				<span class="item-value">' + (detailObj['flyOverDevModel'] === '' || 'undefined' ? '--' : detailObj['flyOverDevModel']) + '/' + (detailObj['flyOverDevNum'] === '' || 'undefined' ? '--' : detailObj['flyOverDevNum']) + '</span>            ' +
                '			</li>                          ' +
                '	</ul>'
            });
            $('#device-data-grid').datagrid('fixDetailRowHeight', index);
        }
    });
}

function deleteItem(context, isDel) {
    var index = $(context).parents('tr').index() / 2,
        macAddr = responseData[index]['macAddress'],
        devName = responseData[index]['deviceName'];

    $('#confirm-dev-name').text(devName).data('macaddr', macAddr);
    $('#delConfirm').dialog('open');
}

function updateItem(context) {
    var titleText = $(context).parents('tr').children('td')[0].textContent,
        index = $(context).parents('tr').index() / 2,
        options = $('#departmentList').children('option'),
        resdata = responseData[index],
        addWrapper = $('#addDev-wrapper'),
        bedList,
        str = '';

    isNew = false;
    seqId = resdata['seqId'];

    $('#addDeviceForm').find('[name="deviceName"]').val(resdata['deviceName']);
    $('#addDeviceForm').find('[name="macAddress"]').val(resdata['macAddress']);

    $.each(options, function (i, val) {
        log($(val).text(),resdata['deptName']);
        if ($(val).text() === resdata['deptName']) {
            val.selected = true;
            $(val).trigger('click');
            bedList = $('#bedList').children('option');
            $.each(bedList, function (i, val) {
                if ($(val).val() === resdata['bedCode']) {
                    val.selected = true;
                }
            });
        }//deptName
    });

    $('#addNewDevice').dialog({
        title: titleText + ' - 编辑设备'
    });
    $('#addNewDevice').dialog('open');
}

/*
 http://localhost:8919/mnis/nur/infusionmanager/getInfusionDeviceSetInfoByMacaddress?macAddress=G2:19:10:T5:92:DD*/


function settingItem(context) {
    var titleText = $(context).parents('tr').children('td')[0].textContent,
        index = $(context).parents('tr').index() / 2,
        macAddr = responseData[index]['macAddress'],
        settingWrapper = $('#setting-wrapper'),
        str = '';

    getDeviceInfo(getDeviceSettingInfoUrl, macAddr, function (data) {
        var alarmSwitch = data['alarmSwitch'] !== '0' ? 'checked' : '';
        log(responseData[index]['deviceLnk']);

        str += '<h3 class="dialog-sub-title">设备信息</h3>' +
            '                            <ul class="form-ele clearfix">' +
            '                                <li class="fl">' +
            '                                    型号：' + responseData[index]['deviceModel'] +
            '                                </li>' +
            '                                <li class="fl">' +
            '                                    设备号：' + responseData[index]['deviceSn'] +
            '                                </li>' +
            '                                <li class="fl">' +
            '                                    <input type="hidden" value=' + macAddr + ' name="macAddress">' +
            '                                    蓝牙mac地址：' + macAddr +
            '                                </li>';
        if (responseData[index]['deviceLnk'] === '1') {
            str += '                                <li class="fl">' +
                '                                    <a href="javascript:;" data-bind="1" id="bind-toggle" class="btn">解除绑定</a>' +
                '                                </li>';
        }
        str += '                            </ul><h3 class="dialog-sub-title">报警设置</h3>' +
            '                            <ul class="form-ele clearfix">' +
            '                                <li class="fl alone">' +
            '                                    <input id="isAlarm" type="checkbox" name="alarmSwitch" ' + alarmSwitch + '>' +
            '                                    <label for="isAlarm">报警开启</label>' +
            '                                </li>' +
            '                                <li class="fl">' +
            '                                    剩余液量' +
            '                                    <input type="tel" class="easyui-validatebox" name="restMlAlerm" data-options="required:true" value="' + data['restMlAlerm'] + '">' +
            '                                    ml' +
            '                                </li>' +
            '                                <li class="fl">' +
            '                                    剩余时间' +
            '                                    <select name="restTimeAlarm" class="easyui-validatebox" data-options="required:true">' +
            '                                        <option value="5" selected="selected">' + data['restTimeAlarm'] + '分钟</option>' +
            '                                        <option value="10">10分钟</option>' +
            '                                    </select>' +
            '                                </li>' +
            '                                <li class="fl">' +
            '                                    滴速范围' +
            '                                    <input type="tel" name="dropSpeedFloor" class="easyui-validatebox" data-options="required:true" value="' + data['dropSpeedFloor'] + '">' +
            '                                    -' +
            '                                    <input type="tel" name="dropSpeedUpper" class="easyui-validatebox" data-options="required:true" value="' + data['dropSpeedUpper'] + '">' +
            '                                    滴／分' +
            '                                </li>' +
            '                                <li class="fl">' +
            '                                    点滴系数' +
            '                                    <select name="idc" class="easyui-validatebox" data-options="required:true">' +
            '                                        <option value="15" selected="selected">' + data['idc'] + '</option>' +
            '                                        <option value="20">20</option>' +
            '                                    </select>' +
            '                                </li>' +
            '                                <li class="fl alone">' +
            '                                    <input name="isDefault" id="defaultSetting" type="checkbox">' +
            '                                    <label for="defaultSetting">设为所有设备默认设置</label>' +
            '                                </li>' +
            '                            </ul>';

        settingWrapper.html(str);

        $('#deviceSetting').dialog({
            title: titleText + '报警设置'
        });
        $('#deviceSetting').dialog('open');
    });
}

function saveDeviceInfo(url) {
    var formData = ay.serializeObject($('#addDeviceForm'));

    if (isNew) {
        formData['isNewDev'] = 1;
    } else {
        formData['seqId'] = seqId;
        formData['isNewDev'] = 0;
    }

    var isValid = $('#addDeviceForm').form('validate');

    if (isValid) {
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
                getData(getDeviceUrl)
            }
            if (isNew) {
                $('#addDeviceForm')[0].reset();
            }
        }).fail(function (err) {
            alert('服务器错误！');
        });
    }
}

$(function () {
    getData(getDeviceUrl);

    $('#addNewDevice').dialog({
        title: '新增设备',
        width: 400,
        height: 300,
        modal: true,
        closed: true,
        buttons: [{
            text: '保存',
            handler: function () {
                saveDeviceInfo(saveDeviceUrl);
            }
        }, {
            text: '取消',
            handler: function () {
                $('#addNewDevice').dialog('close');
            }
        }]
    });

    $('#deviceSetting').dialog({
        title: '设备报警设置',
        width: 400,
        height: 380,
        modal: true,
        closed: true,
        buttons: [{
            text: '保存',
            handler: function () {
                var formData = ay.serializeObject($('#setDeviceForm'));
                formData['alarmSwitch'] === 'on' ? formData['alarmSwitch'] = '1' : formData['alarmSwitch'] = '0';
                formData['isDefault'] === 'on' ? formData['isDefault'] = '1' : formData['isDefault'] = '0';

                var isValid = $('#setDeviceForm').form('validate');

                log(isValid)

                if (isValid) {
                    $.post(settingDeviceUrl, {
                        itemDeviceSet: JSON.stringify(formData)
                    }).done(function (data) {
                        log(data);
                        if (data['rslt'] === '0') {
                            $('#deviceSetting').dialog('close');
                            getData(getDeviceUrl);
                            alert(data['msg']);
                        } else {
                            alert('请检查数据是否填写正确。');
                        }
                    }).fail(function (err) {
                        alert('服务器错误!');
                    });
                }
            }
        }, {
            text: '取消',
            handler: function () {
                $('#deviceSetting').dialog('close');
            }
        }]
    });

    $('#delConfirm').dialog({
        title: '警告',
        width: 400,
        height: 120,
        modal: true,
        closed: true,
        buttons: [{
            text: '确认',
            handler: function () {
                $.post(delDeviceUrl, {
                    macAddress: $('#confirm-dev-name').data('macaddr')
                }).done(function (data) {
                    log(data);
                    getData(getDeviceUrl);
                    $('#delConfirm').dialog('close');
                    alert(data['msg']);
                }).fail(function (err) {
                    alert('服务器错误!');
                });
            }
        }, {
            text: '取消',
            handler: function () {
                $('#delConfirm').dialog('close');
            }
        }]
    });

    // 新增设备
    //设备信息

    $.get(addDeviceInfo).done(function (data) {
        //log(data);
        var list = data['data']['list'],
            str = '<option selected="selected" value="">--请选择--</option>',
            bedArray = [];

        $.each(list, function (i, val) {
            var bedList = JSON.stringify(val['bedLstFormat']);
            //log(typeof bedList);
            str += '<option data-deptcode=' + val['code'] + ' data-bedlist=' + bedList + ' value=' + val['name'] + '>' + val['name'] + '</option>';
        });


        $selection.html(str);
        $('#departmentList-select').html(str);
        log(str);

    });

    $selection.on('change', function (e) {
        // chrome不支持option点击事件
        $(this).find('option').eq(this.selectedIndex).trigger('click');
    });

    $selection.on('click', 'option', function (e) {
        log($(this).data('bedlist'));
        var list = $(this).data('bedlist'),
            str = '<option selected="selected" value="">--请选择--</option>';
        $.each(list, function (i, val) {
            str += '<option value=' + val + '>' + val + '</option>';
        });

        $('[name="deptCode"]').val($(this).data('deptcode'));

        log(str);

        $('#bedList').html(str);
    });

    $('#addNewDeviceBtn').on('click', function (e) {
        $('#addDeviceForm')[0].reset();
        isNew = true;
        $('#addNewDevice').dialog('open');
    });

    $('table').on('click', '.delete-this', function (e) {
        alert('正在删除...');
    });

    $('body').on('click', '#bind-toggle', function (e) {

        var macAddr = $('#setDeviceForm').find('[name="macAddress"]').val();

        $.get(deviceUnbindUrl, {
            doType: 0,
            mathType: 'DB',
            macAddress: macAddr
        }).done(function (data) {
            log(data);

            if (data['rslt'] === '0') {
                getData(getDeviceUrl);
                $('#bind-toggle').remove();
                alert(data['msg']);
            }
        });
    });

    $('#search').on('click', function (e) {
        var type = $('#keytype-select').val(),
            keyword = $('#keyword').val();
        getData(getDeviceUrl, type, keyword);
    });

});