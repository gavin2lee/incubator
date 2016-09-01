var log = console.log.bind(console);
var beforeData = 300;
var breakTime = ''; //已更改为数据获取
// 文书基础类对象
var doc = {
    Data: null, //缓存数据

    getTempConfigStatus: false, //获取模板配置信息状态
    isTableType: true, //是否是表格类型 默认为true

    inpatient_info: null, // 患者信息
    ref_list: null, // 反写数据

    pageIndex: 0, //页面索引

    containter: null, //当前容器
    bands: [], // bands的缓存
    bandsConfig: null, //bands配置
    detailHeight: 0, //detail区域的高度
    footerTop: 0, //footer区域的高度

    nurse: {}, //护士信息

    prevIndex: 0, // 滚动加载时上一次加载的最后一条索引
    loadDataParams: {},
    loadCount: 0,

    breakTime: '',
    breakTimeAfternoon: '',

    clipData: null,
    isHistoryDoc: false, // 历史文书

    detail: {}, //表格循环体缓存数据 params：参数  cellTemp:dom模板
    unDetail: {}, //非表格类型 带有子项的缓存数据

    lastTablePrintHeader: null, //最后一个表格的打印表头
    page: {}, //page信息
    SelTextObj: {
        "ARAB": [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12],
        "ENGLISH": ["A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T"],
        "english": ["a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t"],
        "ROMAN": ["Ⅰ", "Ⅱ", "Ⅲ", "Ⅳ", "Ⅴ", "Ⅵ", "Ⅶ", "Ⅷ", "Ⅸ", "Ⅹ", "Ⅺ", "Ⅻ"],
        "RING": ["①", "②", "③", "④", "⑤", "⑥", "⑦", "⑧", "⑨", "⑩", "⑪", "⑫"]
    },
    fixDetailHeight: false, // 是否修复detail高度，使得detail有正确的高度

    editInfo: {
        targetList: [], //存储行编辑所有的cellNode;不可编辑为undefined;
        tdIndex: null,
        trIndex: null,
        pageIndex: null, //当前页行索引
        thIndex: null,
        target: null,
        saveStatus: false,
        editIndex: null
    },
    fireEventStack: [], // 事件触发栈

    loadDataSt: null, //加载数据时的间歇调用方法
    loadDataStatus: false,
    data_item_list: [],
    filterPassWidget: [], //无须编辑的录入控件
    tablePrintType: "CHANGE",
    contextMenuData: [
        [{
            text: '复制',
            func: function () {
                var that = this;
                $(this).select();
                doc.clipData = that.value;
            }
        }],
        [{
            text: '粘贴',
            func: function () {
                var that = this;
                if (!doc.clipData && doc.clipData === '') {
                    $.messager.alert('提示', '没有复制内容！');
                    return;
                }

                $(that).val(doc.clipData);
            }
        }]
    ],
    inTotal: [],
    outTotal: [],
    outName: [],
    prevDayData: [],
    preOutName: [],
    preOutNameCache: [],
    prevDataCache: {},
    preOutTOtal: [],
    pageStart: 2,
    statistic: []  // 上午和下午的统计数据：由后端计算
};

doc.init = function () {
    // 重置信息
    this.reset();
    this.initBaseModule();
    this.fireEvent();
    this.initEvent();
    $('input[type="text"]').smartMenu(doc.contextMenuData, {
        name: 'clipboard'
    });
}


/*
 加载基础模块
 */
doc.initBaseModule = function () {
    this.initDataSysTem();
    this.setBaseData();
    this.initTemplate(this.url);
    this.loadData(this.url);
}

/*
 fire栈中的顺序
 */
doc.fireEvent = function () {
    var i = 0,
        length = this.fireEventStack.length;
    if (length === 0) {
        return;
    }

    // var fes = null;
    // while(this.fireEventStack.length>0){
    //  fes = fireEventStack.shift();
    //  if(fes!=null){
    //      fes.target.trigger(fes.event);
    //  }
    //  fes = null;
    // }

    for (; i < length; i++) {
        this.fireEventStack[i].target.trigger(this.fireEventStack[i].event);
    }
    this.fireEventStack = [];
    /*$('input[type="text"]').smartMenu(doc.contextMenuData, {
     name: 'clipboard'
     });*/
}

/*
 请求数据
 @params {obj} url传递过来的参数
 */
doc.loadData = function (params, startDate, endDate) {
    var that = this,
        reqUrl;

    that.loadDataParams = params;
    that.isLoading = true;

    if (!this.loadTempStatus) {
        this.loadDataSt = setTimeout(function () {
            that.loadData(params);
        }, 100);
        return;
    }

    var data = null,
        rId = params.recordId,
        pId = params.patientId,
        tId = params.template_id,
        isHistoryDoc = params.isHistoryDoc === "" ? false : params.isHistoryDoc,
        day = new Date().format('yyyy-MM-dd'),
        report_type = params.report_type || null;
    var isNurseDoc = params.report_type == '1';

    this.isNurseDoc = isNurseDoc;

    //弹出加载进度提示
    if (params.type) {
        $.loading();
    }
    var time1 = new Date().getTime();
    switch (params['show_type']) {
        //case "1":
        case "fixed":
            if (!rId) {
                $.loading('close');

                // 将第一次获取的信息数据填充到模板中
                var inpatient_info = this.inpatient_info;
                var ref_list = this.ref_list;
                //for(var i=0;i<inpatient_info.length;i++){
                //$(".inputItem input[data-name='"+inpatient_info[i].key+"']").val(inpatient_info[i].value);
                that.setInverseData(inpatient_info);
                if (ref_list) {
                    that.setInverseData(ref_list, "record_");
                }
                //}


                //当初次进入模板时 无创建时间和创建护士时 增加这两项数据
                if (this.nurse.createNurse) {
                    this.nurse.createNurse.attr("data-value", this.nurse.id).attr("data-text", this.nurse.name).val(this.nurse.name);
                    $(".inputItem[itemname=CREATE_TIME]").find("input:eq(0)").datetimebox({
                        value: this.tempConfig.now_time
                    });
                    $(".inputItem[itemname=APPROVE_TIME]").find("input:eq(0)").datetimebox({
                        //disabled: false
                    });
                }
                this.dataToView(null, false);
                break;
            }

            // 当有保存数据时一定会有创建时间和创建人，将创建时间设置为不可更改
            /*$(".inputItem[itemname=CREATE_TIME]").find("input:eq(0)").datetimebox({
             disabled: true
             });*/
            /*$(".inputItem[itemname=APPROVE_TIME]").find("input:eq(0)").datetimebox({
             disabled: true
             });*/

            try {
                $.ajax({
                    type: "GET",
                    url: "./loadData?inpatient_no=" + pId + "&template_id=" + tId + "&record_id=" + rId + "&isHistory=" + isHistoryDoc,
                    success: function (msg) {
                        if (msg.rslt === "0" || msg.rslt === 0) {
                            that.parseData(msg.data);
                        }
                    },
                    async: false
                });
            } catch (e) {
                console.log("error: " + e);
            }
            break;
        //case "2":
        case "list":
            var dayList = window.parent.dayList;
            var _startDate = startDate ? startDate : (params.startDate || '');
            var _endDate = endDate ? endDate : (params.endDate || '');

            //文书打印模板的ID格式为：print_xxx, 因此以是否包含‘pint’字符串为判断标准
            if (tId.indexOf('print') >= 0) {
                reqUrl = './loadData?isprint=Y&inpatient_no=' + pId + "&template_id=" + tId + "&data_value=" + '&report_type=' + report_type + "&isHistory=" + isHistoryDoc;
            } else {
                if (params.report_type === '1') {
                    reqUrl = './loadData?inpatient_no=' + pId + "&template_id=" + tId + '&isNurseRecord=' + isNurseDoc + '&report_type=' + report_type + '&startDate=' + _startDate + '&endDate=' + _endDate + "&isHistory=" + isHistoryDoc;
                } else {
                    reqUrl = './loadData?inpatient_no=' + pId + "&template_id=" + tId + '&isNurseRecord=' + isNurseDoc + "&data_value=" + '&report_type=' + report_type + "&isHistory=" + isHistoryDoc;
                }
            }
            $.get(reqUrl).done(function (res) {
                $.loading('close');
                if (res['rslt'] === "0" || res['rslt'] === 0) {
                    var data = res.data || [];

                    // 获取表格表头数据
                    that.getTableHeader().then(function (res) {
                        var headerData = res.data || [];
                        // 表头获取
                        // if (res.data && res.data.data_item) {
                        //     that.Data.data('list_table_header', res.data.data_item);
                        //     that.Data.data('tableRecordId', res.data.recordId);
                        // } else {
                        //     that.Data.data('list_table_header', that.inpatient_info);
                        // }

                        //当初次进入模板时 无创建时间和创建护士时 增加这两项数据
                        if (that.nurse && that.nurse.createNurse2) {
                            //that.nurse.createNurse2.val(that.nurse.name);
                            that.nurse.createNurse2.attr("data-value", that.nurse.id).attr("data-text", that.nurse.name).val(that.nurse.name);
                            //this.nurse.createNurse2

                            //this.nurse.createNurse.attr("data-value",this.nurse.id).attr("data-text",this.nurse.name).val(this.nurse.name);
                            // $(".inputItem[itemname=CREATE_TIME]").find("input:eq(0)").datetimebox({
                            //  value:this.tempConfig.now_time
                            // });
                            // $(".inputItem[itemname=APPROVE_TIME]").find("input:eq(0)").datetimebox({
                            //  disabled:true
                            // });
                        }
                        that.parseData(data, headerData);
                    });


                }
            }).fail(function (err) {
                //alert(err);
            });
            /*try {
             $.ajax({
             type: "GET",
             url: "./loadData?inpatient_no=" + pId + "&template_id=" + tId + "&data_value=" + '&report_type=' + report_type+ "&isHistory=" + isHistoryDoc,
             //url:"resources/js/jsonData/norton/data.json",
             success: function (msg) {
             if (msg.rslt === "0" || msg.rslt === 0) {
             var data = msg.data || [];

             //当初次进入模板时 无创建时间和创建护士时 增加这两项数据
             if (that.nurse && that.nurse.createNurse2) {
             //that.nurse.createNurse2.val(that.nurse.name);
             that.nurse.createNurse2.attr("data-value", that.nurse.id).attr("data-text", that.nurse.name).val(that.nurse.name);
             //this.nurse.createNurse2

             //this.nurse.createNurse.attr("data-value",this.nurse.id).attr("data-text",this.nurse.name).val(this.nurse.name);
             // $(".inputItem[itemname=CREATE_TIME]").find("input:eq(0)").datetimebox({
             //     value:this.tempConfig.now_time
             // });
             // $(".inputItem[itemname=APPROVE_TIME]").find("input:eq(0)").datetimebox({
             //     disabled:true
             // });
             }
             that.parseData(data);
             }
             },
             async: false
             });
             } catch (e) {
             console.log("error: " + e);
             }*/
            break;

    }
    switch (params.type) {
        case "day":
            var url = "";
            if (report_type === null) {
                url = "./loadData?inpatient_no=" + pId + "&template_id=" + tId + "&data_value=" + params.day + "&isHistory=" + isHistoryDoc + '&isNurseRecord=' + isNurseDoc;
            } else {
                url = "./loadData?inpatient_no=" + pId + "&template_id=" + tId + "&data_value=" + params.day + "&report_type=" + report_type + "&isHistory=" + isHistoryDoc + '&isNurseRecord=' + isNurseDoc;
            }

            //try {
            $.ajax({
                type: "GET",
                url: url,
                success: function (msg) {
                    if (msg.rslt === "0" || msg.rslt === 0) {
                        var data = msg.data || [];

                        if (data.list[0].approve_status == 'N') {
                          $('#verify').css('display', 'inline-block');
                        }

                        // 获取表格表头数据
                        that.getTableHeader().then(function (res) {
                            var headerData = res.data || [];
                            that.parseData(data, headerData);

                        });
                    }
                }
                //async: false
            });
            //} catch (e) {
            //    console.log("error: " + e);
            //}
            break;
        case "time":
            try {
                $.ajax({
                    type: "GET",
                    url: "./loadData?inpatient_no=" + pId + "&record_id=" + rId + "&template_id=" + tId + "&data_value=" + params.day + " " + params.time + "&isHistory=" + isHistoryDoc + '&isNurseRecord=' + isNurseDoc,
                    success: function (msg) {
                        if (msg.rslt === "0" || msg.rslt === 0) {
                            var data = msg.data || [];
                            // 获取表格表头数据
                            that.getTableHeader().then(function (res) {
                                var headerData = res.data || [];
                                //that.Data.data('list_table_header', res.data[0].data_item || );
                                that.parseData(data, headerData);

                            });
                            var time2 = new Date().getTime();
                            console.log("加载数据时间：" + (time2 - time1) + "ms");
                        }
                    },
                    async: false
                });
            } catch (e) {
                console.log("error: " + e);
            }
            break;
    }
}

/*
 解析数据
 @params {obj} 数据
 @params {obj} 表头数据
 */
doc.parseData = function (data, headerData) {
    var that = this;
    var listData = data.list;
    var statisticData = data.docStatistic || [];

    //数据为空退出
    if (doc.loadDataParams.template_id.indexOf('print') >= 0) {
        this.isForPrint = true;
        $("#save, #addNewLine, #highCheck, #masterCheck").hide();
        $('#set-start-btn').show();

        // 弹出设置打印起始页码
        $('#set-page-start').dialog({
            title: '设置打印起始页码',
            width: 400,
            height: 200,
            modal: true,
            closed: true,
            buttons: [{
                text: '保存',
                handler: function () {
                    if ($('#page-start-num').validatebox("isValid")) {
                        that.pageStart = Number($('#page-start-num').val());
                        $("#set-page-start").dialog("close");
                    }
                }
            }, {
                text: '恢复并关闭',
                handler: function () {
                    $('#page-start-num').val(1);
                    $("#set-page-start").dialog("close");
                    that.pageStart = 1;
                }
            }]
        });

        $('#page-start-num').validatebox({
            validType: 'minNum[0]',
            tipPosition: 'right',
            required: true
        })
    }

    if (data.length == 0) {
        this.initEvent();

        // 解析表头数据
        this.parseHeaderData(headerData);
        this.fillHeader(null);


        return;
    }

    // var ListLen = data[0].data_list.length;
    this.Data.data('recordId', listData[0].recordId);
    this.Data.data('createTime', listData[0].createTime);
    this.Data.data('modify_time', listData[0].modify_time);
    this.Data.data('template_id', listData[0].template_id);
    this.Data.data('inpatient_no', listData[0].inpatient_no);
    this.Data.data('list_table_header', {});
    that.Data.data('tableRecordId', headerData && headerData.recordId || null);

    // 2016-6-13 出入量统计
    this.transformStatistic(statisticData);

    // 前一天入量出量数据
    //that.prevDayData = !data[0]['count_list'] ? [] : data[0]['count_list'];
    // 保存原始数据
    that.prevDataCache = !listData[0]['count_list'] ? [] : listData[0]['count_list'];
    if (!that.prevDataCache.outList) {
        that.prevDataCache.outList = [];
    }
    that.prevDayData['outList'] = [];
    //that.templateId =
    this.tablePrintType = listData[0].print_show;

    var i = 0;
    //var data_item = $.extend(true, [], this.Data.data('list_table_header')) || [];
    var data_item = headerData && headerData.data_item || listData[0].data_item || [];
    // var data_item = data[0].data_item || [];
    // var data_item_obj = this.Data.data('data_item');
    var data_item_obj = headerData ? this.Data.data('list_table_header') : this.Data.data('data_item');

    this.diagnose = {
        id: data_item[7] ? data_item[7]['template_item_id'] : '',
        value: data_item[7] ? data_item[7]['record_value'] : ''
    };

    //设置断点时间
    breakTime = this.breakTime = listData[0]['count_time'];
    this.breakTimeAfternoon = listData[0]['count_time_afternoon'];

    if (this.isTableType) {
        this.data_item_list = listData[0].data_item_list;
        this.lastTablePrintHeader = data_item;

        //保存count_list
        if (listData[0]['count_list']) {
            var count_list = {},
                prop;

            for (prop in listData[0]['count_list']) {
                count_list[prop] = listData[0]['count_list'][prop];
            }

            if (listData[0]['count_list']['out_list']) {
                $.each(listData[0]['count_list']['out_list'], function (i, v) {
                    that.preOutName.push(v['id']);
                    that.preOutNameCache.push(v['id']);
                    that.prevDayData['outList'].push([
                        v['id'],
                        v['name'],
                        v['value']
                    ]);
                    that.prevDataCache['outList'].push([
                        v['id'],
                        v['name'],
                        v['value']
                    ])
                });
            }

            /*$.each(data[0]['count_list'], function (i, n) {
             count_list[n.data_key] = n.data_value;
             });*/
            that.Data.data('count_list', count_list);
        }

    } else {
        //非表格类型增加审签判断
        this.APPROVE = {
            "create_person": listData[0].create_person,
            "approve_status": listData[0].approve_status,
            "record_id": listData[0].recordId,
            "now_time": listData[0].now_time
        }
    }
    for (i; i < data_item.length; i++) {
        data_item_obj[data_item[i].template_item_id] = {
            "record_value": data_item[i].record_value || '',
            "record_item_id": data_item[i].record_item_id,
            "record_id": data_item[i].record_id
        }
        if (data_item[i].list_item) {
            data_item_obj[data_item[i].template_item_id]['list_item'] = data_item[i].list_item;
        }
    }
    var index = -1;
    var data_list = listData[0].data_list || [];
    var data_list_arr = this.Data.data('data_list');
    var list_detail = [];
    var tableRow = [],
        create_personArr, create_person;
    for (i = 0; i < data_list.length; i++) {
        data_list_arr.push({
            "record_id": data_list[i].record_id,
            "data_index": data_list[i].data_index
        });
        var list_detail = data_list[i].list_detail;
        for (var li = 0; li < list_detail.length; li++) {
            data_list_arr[data_list_arr.length - 1][list_detail[li].data_key] = {
                "data_value": list_detail[li].data_value,
                "view_value": list_detail[li].view_value || ''
            };
        }
        if (data_list[i].create_person && data_list[i].approve_status == "N") {
            create_person = data_list[i].create_person.split("-")[0];
            if (!this.detail.row) {
                this.detail.row = {};
            }
            this.detail.row[i] = {
                "approve_status": "N",
                "create_person": create_person,
                "record_id": data_list[i].record_id
            };
        }
    }

    //新增动态表头读取过程
    //在获取数据的时候动态加载
    var dyna_list = listData[0].dyna_list || [];
    this.Data.data('dyna_list', dyna_list);
    // 设置表头
    var dyna_elem = $('#editBox').find('div[itemname*="TITLE"]');

    $.each(dyna_list, function (idx, el) {
        var dyna_element = $('#editBox').find('select[data-name="' + el['data_key'] + '"]');
        var dyna_option = dyna_element.find('option[value="' + el['data_value'] + '"]');
        var text = dyna_option.text();
        if (typeof listData[0]['data_list_print'] !== 'undefined') {
            if (text === '') {
                $('#editBox').find('select').eq(idx).remove();
            }

            dyna_element.parent().data('value', el['data_value']).html(text);
        } else {
            if (text === '') return true;
            dyna_option.attr('selected', 'selected');

        }
        //dyna_element.parent().data('value', el['data_value']).html(text);
    });

    /*var dynaListItems = $(".dynaTableHeader");
     var dynaTableCell = this.detail.changeableCell;
     for (i = 0; i < dyna_list.length; i++) {

     var dynaCheckbox = dynaTableCell[i].params.checkbox;
     var checkboxItem = this.getCheckbox(dynaCheckbox, dyna_list[i].data_value);
     var data_type = checkboxItem ? checkboxItem.data_type : checkboxItem;
     if (!data_type) {
     continue;
     }
     $(dynaListItems[i]).attr("data-value", dyna_list[i].data_value).attr("data-text", checkboxItem.checkbox_name).children("div").width($(dynaListItems[i]).width()).html("<div>" + checkboxItem.checkbox_name + "</div>");
     var cells = this.detail.cell;
     var dynaDetailCellName = ["DATA_ONE", "DATA_TWO", "DATA_THREE", "DATA_FOUR", "DATA_FIVE"][i];
     for (var j = 0; j < cells.length; j++) {
     if (cells[j].params.item_name == dynaDetailCellName) {
     $.ajax({
     type: "get",
     async: false,
     url: "./getDynaItemData",
     data: "checkbox_code=" + dyna_list[i].data_value + "&data_type=" + data_type,
     success: function (d) {
     cells[j].params.data_type = data_type;
     var cellTemp = that.getDetailCell(cells[j].params);
     cells[j].cellTemp = cellTemp;
     if (data_type == "SEL") {
     cells[j].params.checkbox = d.data;
     }
     else if (data_type == "OPT") {
     cells[j].params.options = d.data;
     }
     }
     });
     }
     }
     }*/

    if (window.parent.treeNode['report_type'] === '1' && window.parent.treeNode['type'] === '2') {
        $('#print, #smartPrint, #showPrintTool, #editPrintRow').hide();
    }

    if (typeof listData[0]['data_list_print'] !== 'undefined' || listData[0]['data_list_print']) {
        this.Data.data('data_print', listData[0]['data_list_print']);
        this.isForPrint = true;
        this.onlyForPrint();
        $("#save, #addNewLine, #highCheck, #masterCheck").hide();
        $('#set-start-btn').show();

        // 弹出设置打印起始页码
        $('#set-page-start').dialog({
            title: '设置打印起始页码',
            width: 400,
            height: 200,
            modal: true,
            closed: true,
            buttons: [{
                text: '保存',
                handler: function () {
                    if ($('#page-start-num').validatebox("isValid")) {
                        that.pageStart = Number($('#page-start-num').val());
                        $("#set-page-start").dialog("close");
                    }
                }
            }, {
                text: '恢复并关闭',
                handler: function () {
                    $('#page-start-num').val(1);
                    $("#set-page-start").dialog("close");
                    that.pageStart = 1;
                }
            }]
        });

        $('#page-start-num').validatebox({
            validType: 'minNum[0]',
            tipPosition: 'right',
            required: true
        })
    } else {
        this.dataToView(null, false);
    }

    $.loading('close');

    that.isLoading = false;

    console.log(this.Data.data());
    console.log(doc.detail);
    if (this.loadDataParams.type == '2' && this.loadDataParams.report_type == '1') {
        this.isListTotal = true;
        var $detail = $('[name="detail"]'),
            dayList = window.parent.dayList,
            iOfDay,
            chunk = 3,
            newDayList = [],
            len;

        //dayList.shift();

        // 分割数组
        for (iOfDay = 0, len = dayList.length; iOfDay < len; iOfDay += chunk) {
            newDayList.push(dayList.slice(iOfDay, iOfDay + chunk));
        }
        newDayList.pop();
        $detail.off('scroll');
        if (data_list_arr.length >= 20) {
            $detail.on('scroll', function () {
                if (!newDayList[doc.loadCount]) return;
                if (that.isLoading) return;
                var $this = $(this),
                    sTop = $this.scrollTop(),
                    detailHeight = $detail.height(),
                    scrollHeight = $this[0].scrollHeight,
                    dayItemLen = newDayList[doc.loadCount].length;

                log(sTop, scrollHeight);

                if (scrollHeight < sTop + 10 + detailHeight) {
                    debugger;
                    doc.loadData(doc.loadDataParams, newDayList[newDayList.length - 1 - doc.loadCount][dayItemLen - 1], newDayList[newDayList.length - 1 - doc.loadCount][0]);
                    doc.loadCount += 1;
                    doc.prevIndex += data_list.length;
                }
            });
        }


        if (data_list_arr.length < 20) {
            setTimeout(function () {
                if (newDayList.length === 0) return;
                if (!newDayList[doc.loadCount]) return;
                var dayItemLen = newDayList[doc.loadCount].length;

                doc.loadData(doc.loadDataParams, newDayList[newDayList.length - doc.loadCount - 1][dayItemLen - 1], newDayList[newDayList.length - doc.loadCount - 1][0]);
                doc.loadCount += 1;
                doc.prevIndex += data_list.length;
                $detail.off('scroll');
            }, 100)
        }
    }


    // 如果是历史文书，则无法编辑，添加遮罩层
    if (that.loadDataParams.isHistoryDoc === 'true') {
        $('#print').siblings().remove();
        if (that.loadDataParams.show_type === 'fixed') {
            $('#editor1').append('<div class="disable-edit"></div>');
        }
    }

    if (that.isTableType) {
        $('.band[name="detail"]').addClass('is-list');
    }
};

doc.parseHeaderData = function (data) {
    this.Data.data('list_table_header', {});
    this.Data.data('tableRecordId', data && data.recordId || null);

    var data_item = data && data.data_item || [];
    var data_item_obj = data ? this.Data.data('list_table_header') : this.Data.data('data_item');
    var i = 0;

    for (i; i < data_item.length; i++) {
        data_item_obj[data_item[i].template_item_id] = {
            "record_value": data_item[i].record_value || '',
            "record_item_id": data_item[i].record_item_id,
            "record_id": data_item[i].record_id
        }
        if (data_item[i].list_item) {
            data_item_obj[data_item[i].template_item_id]['list_item'] = data_item[i].list_item;
        }
    }
};

doc.approve = function (data, callback) {
  try {
      $.messager.confirm("提示", "确认审签吗？", function (r) {
          if (r) {
              $.post("./approve", {
                  "json": JSON.stringify(data)
              }, function (msg) {
                  console.log(msg);
                  if (msg.rslt == "0") {
                    if (callback) callback();

                      $.messager.alert("提示", "审核成功！");
                      $('#verify').hide();
                  } else {
                    $.messager.alert("警告", msg.msg);
                  }
              }).fail(function () {
                  $.messager.alert("警告", '服务器错误！');
              });
          } else {
              console.log("取消审签！");
              return;
          }
      });

  } catch (e) {
      console.log(e);
  }
};

doc.fillHeader = function (containter, isToPrint) {
    var that = this;
    //非表格形式的数据获取
    var inpatientInfo = doc.inpatient_info;
    var dataItem = this.isTableType ? this.Data.data('list_table_header') : this.Data.data('data_item');
    var dataItem_temp = this.Data.data('data_item');
    var tableHeader_temp = this.Data.data('list_table_header');

    if ($.isEmptyObject(dataItem)) {
        dataItem = this.inpatient_info;
    }

    var inputItem = null,
        cInputItem = null,
        type = null,
        val = null,
        list_item = null,
        inputItems = $("body");

    if (containter) {
        inputItems = containter;
    }


    for (x in dataItem) {
        inputItem = inputItems.find("div.inputItem[itemname='" + x + "']");
        type = inputItem.attr('data-type');
        val = dataItem[x].record_value;
        if (val === 'gary' || val === 'empty') {
            val = '';
        }
        list_item = dataItem[x].list_item ? dataItem[x].list_item : dataItem[x].list_item = [];
        if (type === "SIGNATURE") {
            var singleApproveBox;
            var signVal = val ? val.split("-") : ["", ""];
            cInputItem = inputItem.children();
            cInputItem.attr("data-value", signVal[0]).attr("data-text", signVal[1]).val(signVal[1]);
            if (isToPrint) {
                cInputItem.css({
                    "height": "18px"
                });
            }
            if ("APPROVE_PERSON" === x && this.APPROVE && this.APPROVE.approve_status === "N") {
                //判断是否是自己查看 不是自己就创建审签按钮
                var createId = this.APPROVE.create_person.split("-")[0];
                if (this.nurse.id != createId) {
                    singleApproveBox = $("<div class='single-approve-box' id='saBox'>审签</div>");
                    var Approvedata = {
                        "approve_person": this.nurse.id + "-" + this.nurse.name,
                        "approve_time": that.APPROVE.now_time,
                        "list": [{
                            "record_id": this.APPROVE.record_id
                        }]
                    };
                    var approveCInputItem = cInputItem;
                    singleApproveBox.bind("click", function () {
                        try {
                            $.messager.confirm("提示", "确认审签吗？", function (r) {
                                if (r) {
                                    $.post("./approve", {
                                        "json": JSON.stringify(Approvedata)
                                    }, function (msg) {
                                        console.log(msg);
                                        if (msg.rslt == "0") {
                                            $.messager.alert("提示", "审核成功！", 'info', function () {
                                                singleApproveBox.hide(200, function () {
                                                    approveCInputItem.val(that.nurse.name);
                                                    $(".inputItem[itemname=APPROVE_TIME]").find("input:eq(0)").datetimebox({
                                                        value: that.APPROVE.now_time
                                                    });
                                                });
                                                window.location.reload();
                                            });
                                        }
                                    });
                                } else {
                                    console.log("取消审签！");
                                    return;
                                }
                            });

                        } catch (e) {
                            console.log(e);
                        }
                    });
                    inputItem.append(singleApproveBox);
                }
            } else {
                cInputItem.attr("data-value", signVal[0]).attr("data-text", signVal[1]).val(signVal[1]);

                if (val !== '') {
                    cInputItem.eq(1).hide();
                    cInputItem.eq(0).height('100%');
                }

                //singleApproveBox = $('<a href="javascript:;" class="btn btn-sm signature-btn">签名</a>');
                /*if (!val) {
                 inputItem.append(singleApproveBox);
                 cInputItem.css('height', '0');

                 singleApproveBox.on('click', function (e) {
                 var $this = $(this);
                 $.messager.confirm("提示", "确认签名吗？", function (r) {
                 if (r) {
                 $this.hide();
                 $this.prev()
                 .val(doc.nurse.name)
                 .attr('data-value', doc.nurse.id)
                 .attr('data-text', doc.nurse.name)
                 .attr('value', doc.nurse.name)
                 .height('100%');
                 }
                 else {
                 console.log("取消审签！");
                 return;
                 }
                 });
                 });
                 }*/

            }
        }
        if (type == 'STR' || type == 'FIELD' || type == 'NUM' || type == 'BP' || type == 'STR' || type == 'MONOIDAL_SCORE') {
            if (x === "ALLERGY_SHOW" && "ALLERGY_SHOW_02" === val) {
                cInputItem = inputItem.children();
                cInputItem.val(val);
                cInputItem.addClass("red");
            }
            // 床号不正确修复
            cInputItem = inputItem.children();
            if (x === 'PTNT_BED') {
                var valTxt = val;
                var newVal = $.grep(that.inpatient_info, function (infoItem) {
                    if (infoItem.key === 'PTNT_BED') {
                        return true;
                    }
                })[0].value;

                if (val.indexOf('→') > -1) {
                    val = val.split('→')[1];
                }

                // 判断是否转床
                if (val != newVal) {
                    valTxt = val + '→' + newVal;
                }

                cInputItem.val(valTxt);
            } else {
                cInputItem = inputItem.children();
                cInputItem.val(val);
            }

        }
        if (type == 'OPT' && inputItem.length === 1) {
            cInputItem = inputItem.eq(0).children();
            //cInputItem.trigger('click');

            if (cInputItem[0].tagName === 'SPAN') {
                cInputItem = cInputItem.children();
            }

            inputItem.data('is-changed', true);
            // TODO 动态表头的下拉可填
            var options = cInputItem.data('options');

            if (!options) {
                if (cInputItem.attr('data-options')) {
                    options = JSON.parse(cInputItem.attr('data-options'));
                }
            }

            if (options) {
                //var _index = 0;
                var option = $.grep(options, function (n, i) {
                    if (n.code == val) {
                        _index = i;
                        return true;
                    }
                })[0];
            }


            //var comboMenuItem = $('#comboMenu').find('.menu-item').eq(_index);

            //comboMenuItem.trigger('click');


            //$('#comboMenu').find()

            //cInputItem.hide();

            /*//inputItem.append('<input/>' + option.value + '</input>');
             //cInputItem.replaceWith('<span>' + option.value + '</span>');
             */
            cInputItem
                .attr('data-value', val)
                .val(option ? option.value : val)
                .attr('data-text', option ? option.value : val);

            if (isToPrint) {
                cInputItem.val(option ? option.value : val)
            }
        }
        if (type == 'SYS_TIME') {
            cInputItem = inputItem.find("input:eq(0)");
            if (isToPrint) {
                cInputItem = $("<input type='text'>");
                inputItem.html("").append(cInputItem);
                if (val) {
                    cInputItem.val(val);
                }
                cInputItem.css({
                    "border-bottom": "none",
                    "height": "18px"
                });
            } else {
                cInputItem.datetimebox({
                    value: val
                });

                cInputItem.parent().find('.combo-text').val(val.replace('00:00', ''));
            }
        }
        if (type == 'DATE') {
            cInputItem = inputItem.find("input:eq(0)");
            if (isToPrint) {
                cInputItem = $("<input type='text'>");
                inputItem.html("").append(cInputItem);
                cInputItem.val(val);
                cInputItem.css({
                    "border-bottom": "none",
                    "height": "18px"
                });
            } else {
                cInputItem.datebox({
                    value: val
                });

                cInputItem.parent().find('.combo-text').val(val.replace('00:00', ''));
            }
        }

        if (type == 'SWT') {
            cInputItem = inputItem.find("input:eq(0)");

            if (isToPrint) {
                if (val == "Y") {
                    cInputItem.parent().html('<div>√</div>');
                } else {
                    cInputItem.parent().html("<div></div>");
                }
            } else {
                if (val == "Y") {
                    cInputItem.attr('checked', true);
                }
            }
        }

        if (type == "MOPT") {

            if (isToPrint) {
                cInputItem = $("<input>");
                inputItem.html(cInputItem);
                cInputItem.css({
                    "width": "100%",
                    "height": (inputItem.height() - 1) + "px",
                    "border": "none",
                    "border-bottom": "1px solid #000"
                });
                if (val.length == 0) {
                    continue;
                }
                var opts = this.unDetail[x],
                    valueStr = "",
                    valArr = val.split(",");
                $.each(valArr, function (index, n) {
                    $.each(opts, function (idx, m) {
                        if (m.code === n) {
                            valueStr += m.value + ",";
                        }
                    });
                });
                valueStr = valueStr.substring(0, valueStr.length - 1);
                cInputItem.val(valueStr);
            } else {
                cInputItem = inputItem.find("input[data-name='" + x + "']");
                if (val.length > 0) {
                    cInputItem.combobox("setValues", val.split(","));
                }
            }

        }

        if (type == 'SEL' || type == 'MSEL') {

            //var checkVal=this.parseCheckData(val);
            var cCheck = null,
                cInput = null,
                list_son = null;
            for (var i = 0; i < list_item.length; i++) {

                cInputItem = inputItem.find("div > span > label > input[value='" + list_item[i].template_item_id + "']");
                cInputItem.attr('checked', 'checked');
                if (type == 'SEL') {
                    var checkindex = cInputItem.parent().parent().data('index') /*.index()*/;
                    inputItem.children().attr("checkindex", checkindex);
                }
                list_son = list_item[i].list_item;
                cCheck = cInputItem.parent().siblings(".childCheck");
                //子节点大于0让
                if (cCheck.find("input").length > 0) {
                    cCheck.find("input").each(function () {
                        $(this).removeAttr('disabled');
                    });
                }
                cText = cInputItem.parent().siblings(".childText");
                for (var ls = 0; ls < list_son.length; ls++) {
                    //相等时有子选择项
                    if (list_son[ls].record_value == list_son[ls].template_item_id) {
                        cCheck.find("input[value='" + list_son[ls].record_value + "']").attr("checked", "checked");
                    } else {
                        cText.find("input[name='" + list_son[ls].template_item_id + "']").removeAttr("disabled").val(list_son[ls].record_value);
                    }
                }
            }
        }
    }
};

/*
 转换统计数据格式
 */
doc.transformStatistic = function (statisticData, isUpdate) {
    var that = this;
    var tdLen = 0;
    var i = 0;

    function setTrStr(statisticItem) {
        return ['<tr class="ignore">',
            '<td colspan="' + tdLen + '">',
            statisticItem.staticValue,
            '<span class="fr" style="background: #FFE48D;">',
            statisticItem.staticTime,
            '(',
            statisticItem.hourDiff || '8h',
            ')',
            '<span>',
            '</td>',
            '</tr>'
        ].join(' ');
    }

    if (!statisticData) return;

    if (this.bandsConfig) {
        tdLen = this.bandsConfig.find(function (bandItem) {
            return bandItem.bandName === 'detail';
        }).items.length;
    }

    if (isUpdate) {
        for (; i < that.statistic.length; i += 1) {
            if (new Date(that.statistic[i].date).getTime() == new Date(statisticData.staticDate).getTime()) {
                if (new Date('1990-01-01 ' + statisticData.docRecordStatistics[0].staticTime).getTime() == new Date('1990-01-01 07:00').getTime()) {
                    that.statistic[i].morning = setTrStr(statisticData.docRecordStatistics[0]);
                } else {
                    that.statistic[i].afternoon = setTrStr(statisticData.docRecordStatistics[0]);
                    ;
                }

                break;
            }
        }
        return;
    }

    that.statistic = that.statistic.concat(statisticData.map(function (sItem) {
        var obj = {
            date: sItem.staticDate
        }

        if (sItem.docRecordStatistics) {
            sItem.docRecordStatistics.forEach(function (sigleItem) {
                if (new Date('1990-01-01 ' + sigleItem.staticTime).getTime() == new Date('1990-01-01 07:00').getTime()) {
                    obj.morning = setTrStr(sigleItem);
                } else {
                    obj.afternoon = setTrStr(sigleItem);
                }
            })
        }

        return obj;
    }));
};

/*
 缓存数据展示到页面上
 containter 目标容器
 isToPrint  是否是面向打印
 */

doc.dataToView = function (containter, isToPrint, isDel) {
    var that = this;

    that.preOutName = $.extend(true, [], that.preOutNameCache);
    that.prevDayData = $.extend(true, {}, that.prevDataCache);
    //debugger;

    //非表格形式的数据获取
    that.fillHeader(containter, isToPrint);
    // var inpatientInfo = doc.inpatient_info;
    // var dataItem = this.isTableType ? this.Data.data('list_table_header') : this.Data.data('data_item');
    // var dataItem_temp = this.Data.data('data_item');
    // var tableHeader_temp = this.Data.data('list_table_header');
    //
    // if ($.isEmptyObject(dataItem)) {
    //     dataItem = this.inpatient_info;
    // }
    //
    // var inputItem = null,
    //     cInputItem = null,
    //     type = null,
    //     val = null,
    //     list_item = null,
    //     inputItems = $("body");
    //
    // if (containter) {
    //     inputItems = containter;
    // }
    //
    // for (x in dataItem) {
    //     inputItem = inputItems.find("div.inputItem[itemname='" + x + "']");
    //     type = inputItem.attr('data-type');
    //     val = dataItem[x].record_value;
    //     list_item = dataItem[x].list_item ? dataItem[x].list_item : dataItem[x].list_item = [];
    //     if (type === "SIGNATURE") {
    //         var singleApproveBox;
    //         var signVal = val ? val.split("-") : ["", ""];
    //         cInputItem = inputItem.children();
    //         cInputItem.attr("data-value", signVal[0]).attr("data-text", signVal[1]).val(signVal[1]);
    //         if (isToPrint) {
    //             cInputItem.css({
    //                 "height": "18px"
    //             });
    //         }
    //         if ("APPROVE_PERSON" === x && this.APPROVE && this.APPROVE.approve_status === "N") {
    //             //判断是否是自己查看 不是自己就创建审签按钮
    //             var createId = this.APPROVE.create_person.split("-")[0];
    //             if (this.nurse.id != createId) {
    //                 singleApproveBox = $("<div class='single-approve-box' id='saBox'>审签</div>");
    //                 var Approvedata = {
    //                     "approve_person": this.nurse.id + "-" + this.nurse.name,
    //                     "approve_time": that.APPROVE.now_time,
    //                     "list": [{
    //                         "record_id": this.APPROVE.record_id
    //                     }]
    //                 };
    //                 var approveCInputItem = cInputItem;
    //                 singleApproveBox.bind("click", function() {
    //                     try {
    //                         $.messager.confirm("提示", "确认审签吗？", function(r) {
    //                             if (r) {
    //                                 $.post("./approve", {
    //                                     "json": JSON.stringify(Approvedata)
    //                                 }, function(msg) {
    //                                     console.log(msg);
    //                                     if (msg.rslt == "0") {
    //                                         $.messager.alert("提示", "审核成功！", 'info', function() {
    //                                             singleApproveBox.hide(200, function() {
    //                                                 approveCInputItem.val(that.nurse.name);
    //                                                 $(".inputItem[itemname=APPROVE_TIME]").find("input:eq(0)").datetimebox({
    //                                                     value: that.APPROVE.now_time
    //                                                 });
    //                                             });
    //                                             window.location.reload();
    //                                         });
    //                                     }
    //                                 });
    //                             } else {
    //                                 console.log("取消审签！");
    //                                 return;
    //                             }
    //                         });
    //
    //                     } catch (e) {
    //                         console.log(e);
    //                     }
    //                 });
    //                 inputItem.append(singleApproveBox);
    //             }
    //         } else {
    //             cInputItem.attr("data-value", signVal[0]).attr("data-text", signVal[1]).val(signVal[1]);
    //
    //             if (val !== '') {
    //                 cInputItem.eq(1).hide();
    //                 cInputItem.eq(0).height('100%');
    //             }
    //
    //             //singleApproveBox = $('<a href="javascript:;" class="btn btn-sm signature-btn">签名</a>');
    //             /*if (!val) {
    //              inputItem.append(singleApproveBox);
    //              cInputItem.css('height', '0');
    //
    //              singleApproveBox.on('click', function (e) {
    //              var $this = $(this);
    //              $.messager.confirm("提示", "确认签名吗？", function (r) {
    //              if (r) {
    //              $this.hide();
    //              $this.prev()
    //              .val(doc.nurse.name)
    //              .attr('data-value', doc.nurse.id)
    //              .attr('data-text', doc.nurse.name)
    //              .attr('value', doc.nurse.name)
    //              .height('100%');
    //              }
    //              else {
    //              console.log("取消审签！");
    //              return;
    //              }
    //              });
    //              });
    //              }*/
    //
    //         }
    //     }
    //     if (type == 'STR' || type == 'NUM' || type == 'BP' || type == 'STR' || type == 'MONOIDAL_SCORE') {
    //         if (x === "ALLERGY_SHOW" && "ALLERGY_SHOW_02" === val) {
    //             cInputItem = inputItem.children();
    //             cInputItem.val(val);
    //             cInputItem.addClass("red");
    //         }
    //         // 床号不正确修复
    //         cInputItem = inputItem.children();
    //         if (x === 'PTNT_BED') {
    //             var valTxt = val;
    //             var newVal = $.grep(that.inpatient_info, function(infoItem) {
    //                 if (infoItem.key === 'PTNT_BED') {
    //                     return true;
    //                 }
    //             })[0].value;
    //
    //             // 判断是否转床
    //             if (val != newVal && $.isEmptyObject(dataItem_temp) && $.isEmptyObject(tableHeader_temp)) {
    //                 valTxt = val + '→' + newVal;
    //             }
    //
    //             cInputItem.val(valTxt);
    //         } else {
    //             cInputItem = inputItem.children();
    //             cInputItem.val(val);
    //         }
    //
    //     }
    //     if (type == 'OPT' && inputItem.length === 1) {
    //         cInputItem = inputItem.eq(0).children();
    //         //cInputItem.trigger('click');
    //
    //         if (cInputItem[0].tagName === 'SPAN') {
    //             cInputItem = cInputItem.children();
    //         }
    //
    //         inputItem.data('is-changed', true);
    //         // TODO 动态表头的下拉可填
    //         var options = cInputItem.data('options');
    //
    //         if (!options) {
    //             if (cInputItem.attr('data-options')) {
    //                 options = JSON.parse(cInputItem.attr('data-options'));
    //             }
    //         }
    //
    //         if (options) {
    //             //var _index = 0;
    //             var option = $.grep(options, function(n, i) {
    //                 if (n.code == val) {
    //                     _index = i;
    //                     return true;
    //                 }
    //             })[0];
    //         }
    //
    //
    //         //var comboMenuItem = $('#comboMenu').find('.menu-item').eq(_index);
    //
    //         //comboMenuItem.trigger('click');
    //
    //
    //         //$('#comboMenu').find()
    //
    //         //cInputItem.hide();
    //
    //         /*//inputItem.append('<input/>' + option.value + '</input>');
    //          //cInputItem.replaceWith('<span>' + option.value + '</span>');
    //          */
    //         cInputItem
    //             .attr('data-value', val)
    //             .val(option ? option.value : val)
    //             .attr('data-text', option ? option.value : val);
    //
    //         if (isToPrint) {
    //             cInputItem.val(option ? option.value : val)
    //         }
    //     }
    //     if (type == 'SYS_TIME') {
    //         cInputItem = inputItem.find("input:eq(0)");
    //         if (isToPrint) {
    //             cInputItem = $("<input type='text'>");
    //             inputItem.html("").append(cInputItem);
    //             if (val) {
    //                 cInputItem.val(new Date(val).format('yyyy-MM-dd hh:mm'));
    //             }
    //             cInputItem.css({
    //                 "border-bottom": "none",
    //                 "height": "18px"
    //             });
    //         } else {
    //             cInputItem.datetimebox({
    //                 value: val
    //             });
    //         }
    //     }
    //     if (type == 'DATE') {
    //         cInputItem = inputItem.find("input:eq(0)");
    //         if (isToPrint) {
    //             cInputItem = $("<input type='text'>");
    //             inputItem.html("").append(cInputItem);
    //             cInputItem.val(val);
    //             cInputItem.css({
    //                 "border-bottom": "none",
    //                 "height": "18px"
    //             });
    //         } else {
    //             cInputItem.datebox({
    //                 value: val
    //             });
    //         }
    //     }
    //
    //     if (type == 'SWT') {
    //         cInputItem = inputItem.find("input:eq(0)");
    //
    //         if (isToPrint) {
    //             if (val == "Y") {
    //                 cInputItem.parent().html('<div>√</div>');
    //             } else {
    //                 cInputItem.parent().html("<div></div>");
    //             }
    //         } else {
    //             if (val == "Y") {
    //                 cInputItem.attr('checked', true);
    //             }
    //         }
    //     }
    //
    //     if (type == "MOPT") {
    //
    //         if (isToPrint) {
    //             cInputItem = $("<input>");
    //             inputItem.html(cInputItem);
    //             cInputItem.css({
    //                 "width": "100%",
    //                 "height": (inputItem.height() - 1) + "px",
    //                 "border": "none",
    //                 "border-bottom": "1px solid #000"
    //             });
    //             if (val.length == 0) {
    //                 continue;
    //             }
    //             var opts = this.unDetail[x],
    //                 valueStr = "",
    //                 valArr = val.split(",");
    //             $.each(valArr, function(index, n) {
    //                 $.each(opts, function(idx, m) {
    //                     if (m.code === n) {
    //                         valueStr += m.value + ",";
    //                     }
    //                 });
    //             });
    //             valueStr = valueStr.substring(0, valueStr.length - 1);
    //             cInputItem.val(valueStr);
    //         } else {
    //             cInputItem = inputItem.find("input[data-name='" + x + "']");
    //             if (val.length > 0) {
    //                 cInputItem.combobox("setValues", val.split(","));
    //             }
    //         }
    //
    //     }
    //
    //     if (type == 'SEL' || type == 'MSEL') {
    //
    //         //var checkVal=this.parseCheckData(val);
    //         var cCheck = null,
    //             cInput = null,
    //             list_son = null;
    //         for (var i = 0; i < list_item.length; i++) {
    //
    //             cInputItem = inputItem.find("div > span > label > input[value='" + list_item[i].template_item_id + "']");
    //             cInputItem.attr('checked', 'checked');
    //             if (type == 'SEL') {
    //                 var checkindex = cInputItem.parent().parent().data('index') /*.index()*/ ;
    //                 inputItem.children().attr("checkindex", checkindex);
    //             }
    //             list_son = list_item[i].list_item;
    //             cCheck = cInputItem.parent().siblings(".childCheck");
    //             //子节点大于0让
    //             if (cCheck.find("input").length > 0) {
    //                 cCheck.find("input").each(function() {
    //                     $(this).removeAttr('disabled');
    //                 });
    //             }
    //             cText = cInputItem.parent().siblings(".childText");
    //             for (var ls = 0; ls < list_son.length; ls++) {
    //                 //相等时有子选择项
    //                 if (list_son[ls].record_value == list_son[ls].template_item_id) {
    //                     cCheck.find("input[value='" + list_son[ls].record_value + "']").attr("checked", "checked");
    //                 } else {
    //                     cText.find("input[name='" + list_son[ls].template_item_id + "']").removeAttr("disabled").val(list_son[ls].record_value);
    //                 }
    //             }
    //         }
    //     }
    // }

    if (isToPrint) {
        return;
    }

    //表格形式的数据获取
    var dataList = this.Data.data('data_list');
    //var table = $("#dataTableList")
    var table = $("#dataTable"),
        tr = null,
        td = null;
    //table.show().css("visibility","hidden");
    if (!this.isListTotal || isDel) {
        table.find("tbody").html("");
    }
    this.page.count = 0; //
    this.page.breakIndex = [];

    //渲染所有的数据 该数据只用于计算分页 并隐藏
    //每加载一行就计算一次高度
    var tableHeight = 0;
    var detailHeight = this.detailHeight;
    var remainHeight = detailHeight - tableHeight;
    var currenIndex = 1;
    var _index = this.isListTotal ? doc.prevIndex : 0;
    if (isDel) {
        _index = 0;
    }
    //if (table.length > 0) {
    // 总护理记录单保存时不新增行
    if (!that.isSaving || isDel) {
        for (var i = _index; i < dataList.length; i++) {
            this.addNewLine(table, {
                "edit": false,
                "auto": true
            });

            /*if (dataList[i]['record_id'] !== '0') {
             };*/
            //tr = table.find("tr:eq(" + i + ")");
            var cpId = dataList[i].CREATE_PERSON && dataList[i].CREATE_PERSON.data_value.split("-")[0];
            tr = table.find("tr[data-index='" + i + "']");
            var deleteRow = {
                text: "删除该行",
                func: function (e, item) {
                    var nid = $(this).attr('nurseId');
                    if (nid !== doc.nurse.id) {
                        $.messager.alert('提示', '抱歉，您没有权限删除这条数据！');
                        return;
                    }

                    var index = parseInt($(this).attr("data-index"));
                    var rId = doc.Data.data("data_list")[index].record_id;
                    $.messager.confirm("删除提示", "您确定要删除该条数据吗?", function (r) {
                        if (r) {
                            /*$.post("./delete_data", {
                             record_id: rId,
                             isNurseRecord: that.isNurseRecord,
                             patId: that.loadDataParams.patientId,
                             delete_person: that.nurse.id + '-' + that.nurse.name
                             }, function(msg) {*/
                            $.post("./delete_data?record_id=" + rId + '&isNurseRecord=' + that.isNurseDoc + '&patId=' + that.loadDataParams.patientId + '&delete_person=' + encodeURI(encodeURI(that.nurse.id + '-' + that.nurse.name)), {}, function (msg) {
                                if (msg.rslt === "0") {
                                    doc.Data.data("data_list").splice(index, 1);
                                    that.dataToView(null, false, true);
                                    window.parent.window.loadMenu();
                                } else {
                                    $.messager.alert("提示", "删除操作失败，请重试！");
                                }
                            });
                        }
                    });
                }
            }
            var rightBtnMenuData = [
                []
            ];
            //if (cpId == doc.nurse.id) {
            rightBtnMenuData[0].push(deleteRow);
            //}
            tr.smartMenu(rightBtnMenuData /*.concat(doc.contextMenuData)*/, {
                name: "row"
            });

            tr.attr('nurseId', cpId);

            /*$('input[type="text"]').smartMenu(doc.contextMenuData, {
             name: 'clipboard'
             });*/

            for (x in dataList[i]) {
                this.dataToCellView(tr, x, dataList, i);
            }
            /*
             加载一行计算一次高度
             当高度超过detail高度的时候对parser.page进行处理增加count、pageindex等
             处理完分页后再进行一次分页后的渲染（或节点复制）
             */
            remainHeight = remainHeight - tr.height();
            if (remainHeight <= 0) {
                currenIndex += 1;
                this.page.count += 1;
                this.page.breakIndex.push(i);
                detailHeight = this.detailHeight;
                remainHeight = detailHeight - tr.height();
            }
        }
    }

    //}

    if (dataList.length > 0) {

        //设置分页点
        this.page.count += 1;
        this.page.breakIndex.push(dataList.length);

        //当页面大于1的时候设置表格最后一个cell的样式为腾出滚动条的样式
        /*if (this.page.count >= 2) {

         table.find("tr[class!='ignore'] td:last-child").each(function () {
         var width = $(this).width() - 5;
         $(this).width(width);
         $(this).children().width(width);
         });
         }*/
    }
    // var detailRow    = this.detail.row;
    // //创建审签盒子
    // if(detailRow){
    //  var deleteBox = $("<div class='delete-box' id='deleteBox'></div>");
    //  table.parent().append(deleteBox);
    // }


    // /*
    //  渲染可展示的view 只显示一页
    //  调用分页渲染方法渲染分页
    // */
    // table.hide();
    // var table = $("#dataTable");
    // $("#pagination").remove();
    // table.find("tbody").html("");
    // var detailRow    = this.detail.row;
    // //创建审签盒子
    // if(detailRow){
    //  var approveBox = $("<div class='approve-box' id='approveBox'></div>");
    //  table.parent().append(approveBox);
    // }
    // //获取第一个分页索引 若不存在分页则获取 datalist.length
    // var pageOneIndex = this.page.breakIndex.length >= 0 ? this.page.breakIndex[0] : dataList.length;
    // this.page.currenPageIndex = 1;
    // for(var i=0;i<pageOneIndex;i++){
    //  this.addNewLine(table,{"edit":false,"auto":true});
    //  tr = table.find("tr:eq("+i+")");
    //  tr.attr("data-index",i);
    //  for(x in dataList[i]){
    //      this.dataToCellView(tr,x,dataList[i]);
    //  }
    //  if(detailRow && detailRow[i] && detailRow[i].create_person != this.nurse.id &&  detailRow[i].approve_status === "N"){
    //      this.addApproveBtn(approveBox,tr,i);
    //  }
    // };
    // if(dataList.length>0){
    //  // 调用分页
    //  this.showPagination($("#editor1"),this.page,table);
    // }

    this.initEvent();

    if (this.isTableType) {
        var trs = $('#dataTable').find('tr').toArray(),
            newTrs = trs.map(function (current, index) {
                var tds = $(current).find('td').toArray(),
                    newTds = tds.map(function (curr, idx) {
                        if (idx === 0) {
                            return '<td style="width: 21px;"> <input data-index="' + index + '" id="row' + index + '" type="checkbox" checked="checked"/> </td>' + curr.outerHTML;
                        } else {
                            return curr.outerHTML;
                        }
                    });
                return ['<tr>', newTds.join(''), '</tr>'].join('');
            }),
            newTable = ['<tbody>', newTrs.join(''), '</tbody>'].join('');

        $('#print-row__table').html(newTable);
        $('#check-all')[0].checked = true;
    }
}

/*
 新增审签按钮
 */
doc.addApproveBtn = function (target, tr, index) {
    var approveBtn = $("<div class='approve-btn' id='approveBtn' data-index='" + index + "'>审签</div>");
    approveBtn.css({
        "top": tr.position().top + tr.height() / 2
    })
    target.append(approveBtn);
}

/*
 显示分页
 */
doc.showPagination = function (containt, page, table) {
    var that = this;

    var dataList = this.Data.data('data_list'),
        currenPageIndex = this.page.currenPageIndex,
        currenIndex = 0,
        breakIndex = this.page.breakIndex,
        count = this.page.count,
        start = 0,
        end = 0,
        rowIndex = 0,
        i,
        pagination = $("<div id='pagination' class='pagination'></div>");

    var prev = $("<div>上一页</div>");
    var mid = $("<div class='p-mid'></div>");
    var next = $("<div>下一页</div>");
    pagination.append(prev).append(mid).append(next);

    if (currenIndex == 0) {
        prev.addClass("disabled");
    }
    if (count == 1) {
        prev.addClass("disabled");
        next.addClass("disabled");
    }
    var select = $("<select></select>");
    for (i = 1; i <= count; i++) {
        select.append("<option value='" + i + "'>" + i + "/" + count + "</option>");
    }
    mid.append(select);
    select.unbind().bind("change", function () {

        $("#approveBox").html("");

        if (that.editInfo.saveStatus) {
            $.messager.alert("提示", "请先保存编辑中的数据！");
            return;
        }
        currenIndex = 0;
        table.find("tbody").html("");
        var detailRow = that.detail.row;
        var val = parseInt($(this).val()); //当前选择页
        if (val == 1) {
            start = 0;
            end = breakIndex[val - 1];
            prev.addClass("disabled");
            next.removeClass("disabled");
        } else {
            start = breakIndex[val - 2];
            end = breakIndex[val - 1];
            prev.removeClass("disabled");
            if (val == count) {
                next.addClass("disabled");
            } else {
                next.removeClass("disabled");
            }
        }
        // TODO 该方法可以提取出来
        for (start; start < end; start++) {
            that.addNewLine(table, {
                "edit": false,
                "auto": true
            });
            tr = table.find("tr[data-index=" + currenIndex + "]");
            //tr = table.find("tr:eq(" + currenIndex + ")");
            log(tr);
            tr.attr("data-index", start);
            for (x in dataList[start]) {
                that.dataToCellView(tr, x, dataList[start]);
            }
            if (detailRow && detailRow[start] && detailRow[start].create_person != that.nurse.id && detailRow[start].approve_status == "N") {
                var apBox = $("#approveBox");
                if (apBox) {
                    that.addApproveBtn($("#approveBox"), tr, start);
                }
            }
            currenIndex += 1;
        }
        currenPageIndex = val;
        that.page.currenPageIndex = val;
        //$("#pageIndex").html("第 "+val+" 页");
    });

    next.unbind().bind('click', function () {
        if ($(this).hasClass("disabled")) {
            return;
        }
        //换页时判断是否有进入编辑状态的行 若是则保存
        if (that.editInfo.saveStatus) {
            $.messager.alert("提示", "请先保存编辑中的数据！");
            return;
        }

        currenPageIndex += 1;
        currenIndex = 0;
        prev.removeClass("disabled");

        select.val(currenPageIndex);
        select.trigger("change");
    });
    prev.unbind().bind('click', function () {
        if ($(this).hasClass("disabled")) {
            return;
        }
        if (that.editInfo.saveStatus) {
            $.messager.alert("提示", "请先保存编辑中的数据！");
            return;
        }

        currenPageIndex -= 1;
        currenIndex = 0;
        table.find("tbody").html("");
        if (currenPageIndex == 1) {
            $(this).addClass("disabled");
        }
        next.removeClass("disabled");
        select.val(currenPageIndex);
        select.trigger("change");
    });
    containt.append(pagination);
}

/*
 新增一行
 @table  {dom} 新增的table
 @params {obj} .edit是否让第一个录入框进入编辑模式 .auto是否是自动增加(查询数据时)
 */
doc.addNewLine = function (table, params) {
    var that = this;
    //this.editInfo.editIndex = null;
    if (!this.detail.rowWrap) {
        this.detail.tbody = table.find("tbody"); //$(".band[name='detail'] table tbody");
    }

    var tbody = table.find("tbody");
    var tr = $("<tr></tr>");
    var tw = tbody.width();

    // 行索引
    var dataIndex, pageIndex;
    if (tbody.children().length == 0) {
        dataIndex = 0;
        pageIndex = 0;
    } else {
        //if (tr.hasClass('ignore')) continue;
        dataIndex = parseInt(tbody.find("tr[class!='ignore']").last().attr("data-index")) + 1;
        pageIndex = parseInt(tbody.find("tr[class!='ignore']").last().data('index')) + 1;
    }
    //log('FUCK', dataIndex);
    tr.attr("data-index", dataIndex);
    tr.attr("page-index", pageIndex);
    tbody.append(tr);
    if (this.detail.cell) {
        $.each(this.detail.cell, function (i, n) {
            var td = $("<td></td>");
            td.attr('data-type', n.params.data_type);
            td.attr('itemname', n.params.item_name);
            td.css({
                "width": (n.params.width) + "px",
            "fontSize": n.params.size + 'px'
                //"text-align": n.params.align && n.params.align.toLowerCase()
            });
            if (n.params.item_name.indexOf('remark') >= 0) {
                td.css({
                    "text-align": 'left'
                });
            }
            // if(i === that.detail.cell.length-1){
            //  td.css("width",(n.params.width-3)+"px");
            //  tr.append(td);
            //  td.append("<div style='width:"+(n.params.width-2)+"px;'></div>");
            // }
            // else{
            //td.css("width", (n.params.width - 1) + "px");
            tr.append(td);
            td.append("<div style='width:" + (n.params.width - 1) + "px;'></div>");
            //}

            //td.children().css('display','block').css('display','inline-block');
            tw -= n.params.width;
            //对SIGNATURE单独操作
            //新增时不存在审签标志
            if (n.params.item_name === "CREATE_PERSON" && !params.auto) {
                td.attr('data-value', that.nurse.id);
                td.attr('data-text', that.nurse.name);
                td.children().append("<div>" + that.nurse.name + "</div>");
            }
        });
    }


    if (tw > 0) {
        tr.append("<td style='width:auto;'></td>");
    }

    //进入编辑状态
    if (params.edit) {
        that.editInfo.trIndex = dataIndex;
        that.editInfo.pageIndex = pageIndex;
        that.editInfo.saveStatus = true;
        this.enterEditStatus(tr.find("td:eq(0)"), this.detail.cell[0], true, dataIndex, pageIndex);
    }

    //$(".band[name='detail']").scrollTop(table.height());
    $(document).on('keyup', '[data-name="outTake"],[data-name="inTake"]', function (e) {
        $(this).val(that.replaceText($(this).val()));
    });

    $(document).on('keyup', 'input.timespinner-f', function (e) {
        if (e.which === 8 || e.which === 46) return;
        var $val = $(this).val(),
            len = $val.length;

        if (len === 2) {
            $(this).val($val.replace(/^\d{2}/g, '$&' + ':'));
        }

    });
},

    // 获取编辑状态
    doc.getEditStatus = function () {

        // 是否可以保存，其他状态
        var save = false,
            edit = false,
            other = false;
        if (this.editInfo.target === null) {
            other = true;
            edit = true;
        }
        if (this.editInfo.saveStatus) {
            save = true;
        }
        return {
            "save": save,
            "other": other,
            "edit": edit
        }
    }

doc.setEditStatus = function (cell, target) {

    if (!cell && !target) {
        this.editInfo.tdIndex = null;
        this.editInfo.trIndex = null;
        this.editInfo.target = null;
        return;
    }
    //更新编辑状态、节点、位置
    target.parent().addClass("edit");
    this.editInfo.tdIndex = target.index();
    this.editInfo.trIndex = target.parent().data('index') /*.index()*/;
    this.editInfo.target = cell;
}


/*
 展示编辑框
 */
doc.showEditWidget = function (target, cell, isAdd, data_type) {
    var that = this;

    var cellNode = $(cell.cellTemp),
        div = $("<div></div>"),
        oValue = target.attr('data-value'),
        oText = target.attr('data-text');
    div.css({
        "width": (cell.params.width - 1) + "px"
    });

    div.append(cellNode);
    target.html(div);

    this.editInfo.target = cellNode;

    if (cell.params.data_type == 'DATE') {
        oValue = oValue ? oValue : new Date().format('yyyy-MM-dd');
        cellNode.datebox({
            width: cellNode.width(),
            value: oValue,
            required: (cell.params.required ? true : false),
            invalidMessage: "",
            missingMessage: "请填入日期",
            showSeconds: false,
            formatter: function (date) {
                return new Date(date).format('yyyy-MM-dd');
            }
        });

        // 增加模拟keypress事件 用于讲时间与日期两项创建一行时默认填写
        if (isAdd) {
            this.fireEventStack.push({
                target: $(document),
                event: "keypress"
            });
        }
    } else if (cell.params.data_type == 'TIME') {
        if (!oValue) {
            //oValue = '';
            oValue = new Date().format('hh:mm');
        }
        cellNode.timespinner({
            width: cellNode.width(),
            required: (cell.params.required ? true : false),
            invalidMessage: "",
            missingMessage: "请填入时间",
            value: oValue,
            showSeconds: false
        });
        if (isAdd) {
            this.fireEventStack.push({
                target: $(document),
                event: "keypress"
            });
        }
    } else if (cell.params.data_type == 'NUM') {

        if (["inTake", "outTake"].indexOf(cell.params.item_name) >= 0) {
            if (oValue) {
                cellNode.val(oValue);
            }
            cellNode.val(cellNode.val());
            // cellNode.attr("readonly","readonly");
            // cellNode.bind("click",function(e){
            //  console.log(e);
            //  //var panel = $("#calTotalInput");
            //  cellNode.tooltip({
            //      deltaY:-5,
            //      position: 'bottom',
            //      content: '"<input type="text" style="width:50px;"/>"',
            //      onShow: function(){
            //          //$(this).tooltip('tip').css({backgroundColor: '#666',borderColor: '#666'});
            //      }
            //  });
            // });
        } else {
            if (oValue) {
                cellNode.val(oValue);
            }
            cellNode.validatebox({
                required: (cell.params.required ? true : false),
                validType: 'number[' + cell.params.min_value + "," + cell.params.max_value + "," + cell.params.precision + "]",
                required: cell.params.required
            });
        }

        //cellNode.focus();
    } else if (cell.params.data_type == 'BP') {
        if (oValue) {
            cellNode.val(oValue);
        }
        cellNode.validatebox({
            required: (cell.params.required ? true : false),
            validType: 'bp[' + cell.params.min_value + "," + cell.params.max_value + ",'" + cell.params.item_name + "']",
            required: cell.params.required
        });
        //cellNode.focus();
    } else if (cell.params.data_type == 'STR') {
        cellNode.val(oValue);
        cellNode.validatebox({
            required: (cell.params.required ? true : false),
        });
        //cellNode.focus();

        // 病情观察特殊处理: 输入框太小,弹窗输入.
        if (cell.params.item_name.indexOf('remark') > -1) {
            cellNode.attr('readOnly', 'readOnly');
            var $dialogSetIllness = $('#set-illness-state');
            var content = $('#state-content');

            content.val(oValue);

            $dialogSetIllness.dialog({
                title: '请填写病情观察',
                width: 400,
                height: 340,
                closed: true,
                modal: true,
                buttons: [{
                    text: '保存',
                    handler: function () {

                        $dialogSetIllness.dialog("close");
                        cellNode
                            .val(content.val())
                            .attr('value', content.val());
                    }
                }, {
                    text: '关闭',
                    handler: function () {
                        $dialogSetIllness.dialog("close");
                    }
                }]
            });

            cellNode.on('click', function (e) {
                $dialogSetIllness.dialog('open');
            })
        }
    } /*else if (cell.params.data_type == 'Field') {
        children = $("<textarea placeholder='此处填写' class='doc-textarea' style='width: " + params.width + "px; height: " + params.height + "px' data-name='" + params.item_name + "'></textarea>");

    }*/ else if (cell.params.data_type == 'OPT' || cell.params.data_type == 'MOPT') {

        var options = cell.params.options,
            sValue = "";

        if (oValue && oValue !== "") {
            for (var idx = 0; idx < options.length; idx++) {
                if (oValue === options[idx].code) {
                    sValue = options[idx].value;
                }
            }

            if (sValue == '') {
                sValue = oValue;
            }
        }

        cellNode.val(sValue).css("text-align", "center");
        cellNode.validatebox({
            required: (cell.params.required ? true : false),
        });

        cellNode.data('options', options);
        cellNode.off('keyup', '**').on('keyup', doc.optKeyPress);

        cellNode.bind("click", function (e) {

            that.editInfo.target = $(this);

            var target = $(e.target),
                menu = null,
                i = 0,
                opStr = '',
                flag = '';
            cellNode.attr("size", 1);
            $("#comboMenu").html("");
            $("#comboMenu").menu("appendItem", {
                // 修复下拉为空时,值未修改的情况 指定为空的id为empty
                text: "",
                id: "empty",
                name: ""
            });
            for (var i = 0; i < options.length; i++) {
                flag = '';
                //if(options[i].code==oValue){ cellNode.val(options[i].value); }
                opStr += '<option ' + flag + ' value="' + options[i].code + '">' + options[i].value + '</option>';
                menu = {
                    text: options[i].value,
                    id: options[i].code,
                    name: options[i].value
                }
                $("#comboMenu").menu("appendItem", menu);
            }
            //opStr += '<option '+flag+' value="">333333</option>';
            $("#comboMenu").menu({
                onClick: doc.selWidgetMenuEvent,
                onHide: doc.selWidgetClearMenu
            });
            $("#comboMenu").menu('show', {
                left: target.offset().left,
                top: target.offset().top + target.height()
            });
        });
    }
    // 表格里的审核 签名按钮
    else if (cell.params.item_name == 'APPROVE_PERSON') {
        var $input = $('<input type="text" data-name="' + cell.params.item_name + '" item-name="' + cell.params.item_name + '">');
        singleApproveBox = $('<a href="javascript:;" class="_btn _btn-tn signature-btn">签名</a>');
        //if (!val) {
        $input.css({
            'display': 'none',
            'text-align': 'center'
        });
        this.editInfo.target = $input;
        cellNode = $input;
        target.children().append($input);
        target.children().append(singleApproveBox);
        //cellNode.css('height', '0');

        singleApproveBox.on('click', function (e) {
            var $this = $(this);
            $.messager.confirm("提示", "确认签名吗？", function (r) {
                if (r) {
                    $this.hide();
                    $this.prev()
                        .val(doc.nurse.name)
                        .attr('data-value', doc.nurse.id)
                        .attr('data-text', doc.nurse.name)
                        .attr('value', doc.nurse.name)
                        .show();
                } else {
                    console.log("取消审签！");
                    return;
                }
            });
        });
        //}
    } else if (cell.params.data_type == 'SWT') {
        if (target.attr("data-value") == '1') {
            cellNode.attr('checked', true);
        }
    } else if (cell.params.data_type == "DYNA_TITLE") {
        var checkbox = cell.params.checkbox,
            cbStr = '',
            flag = '';

        for (var i = 0; i < checkbox.length; i++) {
            cbStr += '<span><label><input name="' + cell.params.itemValue + '" type="checkbox" value="' + checkbox[i].checkbox_code + '" data-text="' + checkbox[i].checkbox_name + '" data-type="' + checkbox[i].data_type + '"/>' + checkbox[i].checkbox_name + '</label></span>'
        }
        cbStr += '<div><button id="cfmCb">确定</button></div>'; //<button id="cancelCb">取消</button>
        cellNode.tooltip({
            content: $('<div class="tab-checkbox">' + cbStr + '</div>'),
            showEvent: 'click',
            hideEvent: null,
            onShow: function () {
                var t = $(this);
                var span = t.tooltip('tip').find(".tab-checkbox");
                var input = t.tooltip('tip').find(".tab-checkbox span input");
                if (cell.params.data_type == "DYNA_TITLE" && !span.attr("data-type")) {
                    span.attr("data-type", "SEL");
                    input.each(function () {
                        //设置单选
                        $(this).bind("click", function () {
                            var that = $(this);
                            var currClickIndex = that.parent().parent().data('index') /*index()*/;
                            if (parseInt(span.attr("data-index")) == that.parent().parent().data('index') /*index()*/) {
                                $(this).removeAttr("checked");
                                span.attr("data-index", null);
                                return;
                            }
                            input.each(function () {
                                $(this).removeAttr("checked");
                                if (currClickIndex == $(this).parent().parent().data('index')) {
                                    $(this).attr("checked", "checked");
                                    span.attr("data-index", that.parent().parent().data('index'));
                                }
                            });
                        });
                    })
                }
                var value = target.attr('data-value'),
                    text = target.attr('data-text');
                if (value) {
                    t.tooltip('tip').find(".tab-checkbox span input").each(function (i) {
                        if (value.indexOf($(this).val()) >= 0) {
                            $(this).attr('checked', true);
                            span.attr("data-index", i);
                        }
                    });
                }
                t.tooltip('tip').find(".tab-checkbox button#cfmCb").unbind().bind('click', function () {
                    //确定操作  更新响应的数据源框，并清空数据
                    var thIndex = that.editInfo.thIndex;
                    that.editInfo.thIndex = null;
                    var checkItem = t.tooltip('tip').find(".tab-checkbox span input:checked");
                    var checkType = checkItem.attr("data-type");
                    var dynaIndex = cell.params.dynaIndex;
                    var dynaDetailCellName = ["DATA_ONE", "DATA_TWO", "DATA_THREE", "DATA_FOUR", "DATA_FIVE"][dynaIndex];
                    //if(that.endEdit().edit){
                    that.endEditWidget();
                    if (!that.editInfo.saveStatus) {
                        that.editInfo.target = null;
                    }
                    that.editInfo.tdIndex = null;
                    that.editInfo.thIndex = null;
                    $.get('./getDynaItemData?checkbox_code=' + checkItem.val() + "&data_type=" + checkType, function (d) {
                        var cells = that.detail.cell;
                        $.each(cells, function (i, n) {
                            if (n.params.item_name == dynaDetailCellName) {
                                n.params.data_type = checkType;
                                var cellTemp = that.getDetailCell(n.params);
                                n.cellTemp = cellTemp;
                                if (checkType == "SEL") {
                                    n.params.checkbox = d.data;
                                } else if (checkType == "OPT") {
                                    n.params.options = d.data;
                                }
                            }
                        });

                    });
                    //}
                    return false;
                });
                t.tooltip('tip').find(".tab-checkbox button#cancelCb").unbind().bind('click', function () {
                    //取消操作恢复原来状态；
                    var thIndex = that.isEdit.thIndex;
                    if (parseInt(thIndex) >= 0) {
                        var currTh = $(".dynaTableHeader:eq(" + thIndex + ")");
                        var oldChoose = currTh.attr("data-index");
                        span.attr("data-index", oldChoose);
                        input.each(function () {
                            $(this).removeAttr("checked");
                        });
                        t.tooltip('tip').find(".tab-checkbox span input:eq(" + oldChoose + ")").attr("checked", "checked")
                        that.isEdit.thIndex = null;
                    }
                    //that.endEdit().state;
                    t.tooltip('hide');
                    t.tooltip('destroy');
                });
                t.tooltip('tip').unbind().bind('mouseenter', function () {
                    return true;
                }).bind('mouseleave', function () {
                    return true;
                });
            }
        });
        cellNode.tooltip("show");
    }
    //总分控件
    else if (cell.params.data_type === "SCORE_TOTAL_ROW") {
        cellNode.val(oValue).css("text-align", "center");
        // if(params.if_rel === "Y"){
        //  cellNode.attr("readonly","readonly");
        // }
    }
    //算分控件
    else if (cell.params.data_type === "MONOIDAL_SCORE") {
        var options = cell.params.options,
            sValue = "";

        if (oValue && oValue !== "") {
            for (var idx = 0; idx < options.length; idx++) {
                if (oValue === options[idx].code) {
                    sValue = options[idx].value;
                }
            }
        }

        cellNode.val(sValue).attr("readonly", true).css("text-align", "center");
        cellNode.validatebox({
            required: (cell.params.required ? true : false),
        });


        cellNode.unbind().bind("click", function (e) {

            that.editInfo.target = $(this);

            var target = $(e.target),
                menu = null,
                i = 0,
                opStr = '',
                flag = '';
            cellNode.attr("size", 1);
            $("#comboMenu").html("");
            for (var i = 0; i < options.length; i++) {
                flag = '';
                //if(options[i].code==oValue){ cellNode.val(options[i].value); }
                opStr += '<option ' + flag + ' value="' + options[i].code + '">' + options[i].value + '</option>';
                menu = {
                    text: options[i].value,
                    id: options[i].code,
                    name: options[i].value
                }
                $("#comboMenu").menu("appendItem", menu);
            }
            $("#comboMenu").menu({
                onClick: doc.selWidgetMenuEvent,
                onHide: doc.selWidgetClearMenu
            });
            $("#comboMenu").menu('show', {
                left: target.offset().left,
                top: target.offset().top + target.height()
            });
        });
    } else if (cell.params.data_type == 'MSEL') {

        var checkbox = cell.params.checkbox,
            mSelData = [];
        var selTextObj = that.SelTextObj;
        for (var idx = 0; idx < checkbox.length; idx++) {
            mSelData.push({
                "id": checkbox[idx].checkbox_code,
                "text": selTextObj[checkbox[idx].show_type][checkbox[idx].checkbox_ord - 1] + checkbox[idx].checkbox_name,
                "showText": selTextObj[checkbox[idx].show_type][checkbox[idx].checkbox_ord - 1]
            });
        }

        if (oValue && oValue !== "") {
            // var arrValue = oValue.split(",");
            // if(oValue === checkbox[idx].checkbox_code){
            //  sValue = that.SelTextObj[checkbox[idx].show_type][checkbox[idx].checkbox_ord-1]
            // }
        }
        cellNode.combobox({
            data: mSelData,
            multiple: true,
            valueField: 'id',
            textField: 'text',
            onSelect: function (record) {
                var text = $(this).combobox("getText"),
                    newText = "";

                $.each(text.split(","), function (i, n) {
                    newText += n.substring(0, 1) + ",";
                })
                var newText = newText.substring(0, newText.length - 1);
                $(this).combobox("setText", newText);
            },
            onUnselect: function () {
                var text = $(this).combobox("getText"),
                    newText = "";

                $.each(text.split(","), function (i, n) {
                    newText += n.substring(0, 1) + ",";
                })
                var newText = newText.substring(0, newText.length - 1);
                $(this).combobox("setText", newText);
            }
        });
        if (oValue && oValue !== "") {
            cellNode.combobox("setValues", oValue.split(","));
            var text = cellNode.combobox("getText"),
                newText = "";
            $.each(text.split(","), function (i, n) {
                newText += n.substring(0, 1) + ",";
            })
            var newText = newText.substring(0, newText.length - 1);
            cellNode.combobox("setText", newText);
        }

    } else if (cell.params.data_type == 'SEL') {

        var checkbox = cell.params.checkbox,
            sValue = "";

        if (oValue && oValue !== "") {
            for (var idx = 0; idx < checkbox.length; idx++) {
                if (oValue === checkbox[idx].checkbox_code) {
                    sValue = that.SelTextObj[checkbox[idx].show_type][checkbox[idx].checkbox_ord - 1]
                }
            }
        }

        cellNode.val(sValue).attr("readonly", true).css("text-align", "center");
        cellNode.validatebox({
            required: (cell.params.required ? true : false),
        });

        sValue = null;

        cellNode.bind("click", function (e) {

            that.editInfo.target = $(this);
            $("#comboMenu").html("");
            var target = $(e.target),
                menu = null,
                i = 0,
                show_type = "";

            //新增一条空数据
            $("#comboMenu").menu("appendItem", {
                text: "",
                id: "",
                name: ""
            });

            for (; i < checkbox.length; i++) {
                show_type = checkbox[i].show_type ? checkbox[i].show_type : "ARAB";

                menu = {
                    text: that.SelTextObj[show_type][checkbox[i].checkbox_ord - 1] + checkbox[i].checkbox_name,
                    id: checkbox[i].checkbox_code,
                    name: that.SelTextObj[show_type][checkbox[i].checkbox_ord - 1]
                }
                $("#comboMenu").menu("appendItem", menu);
            }
            $("#comboMenu").menu({
                onClick: doc.selWidgetMenuEvent,
                onHide: doc.selWidgetClearMenu
            });
            $("#comboMenu").menu('show', {
                left: target.offset().left,
                top: target.offset().top + target.height()
            });
        });
    } else {
        cellNode.val(oValue);
        //cellNode.focus();
    }
    if (data_type != "DYNA_TITLE") {
        this.setEditStatus(cellNode, target);
        if (!this.editInfo.editIndex) {
            // TODO index bug 修复
            /*if (target.parent().hasClass('ignore')) {
             this.editInfo.editIndex = parseInt(target.parent().prev().attr("data-index"));
             } else {*/
            this.editInfo.editIndex = parseInt(target.parent().attr("data-index"));
            //}
        }
        return cellNode;
    } else {
        //this.editInfo.target = cellNode;
        return cellNode;
    }
}

/*
 使 某块/某行 进入编辑状态
 @target   {obj} 目标节点(行编辑时无效)
 @cell     {obj} 单个列配置信息
 @isAdd    {bon} 是否是新增
 @rowindex {num} 行索引
 */
doc.enterEditStatus = function (target, cell, isAdd, rowIndex) {
    var that = this;

    // 处于编辑状态时，离开窗口提示
    window.parent.isUnsaved = true;

    if (rowIndex != null && rowIndex >= 0) {
        var tds = $("#dataTable").find("tr[data-index=" + rowIndex + "] td"),
            cellNode = undefined;

        tds.each(function (i) {

            //遇到不需要渲染的类型跳过渲染操作
            if (that.filterPassWidget.indexOf($(this).attr("data-type")) >= 0) {
                that.editInfo.targetList.push(undefined);
                return;
            }

            //返回被创建的控件 缓存到targetList中endedit时清空;
            cellNode = that.showEditWidget($(this), that.detail.cell[i], isAdd, $(this).attr("data-type"));
            that.editInfo.targetList.push(cellNode);
        });

    } else {
        this.showEditWidget(target, cell, isAdd, data_type);
    }

    // TODO 动态表头信息
    //动态表头的保存状态修改
    /*if (!this.editInfo.saveStatus && data_type !== "DYNA_TITLE") {
     this.editInfo.saveStatus = true;
     }*/

    this.fireEvent();
    //}
}

/*
 行数据校验(只做校验)
 @return{
 false;通过 || cellIndex; num类型失败列
 }
 */
doc.rowWidgetDataValid = function (target) {

    if (target === null || !target) {
        return {
            status: true,
            value: value,
            text: text
        };
    }

    var value = target.val(),
        text = '',
        className = target.attr('class'),
        dataType = target.parent().parent().attr("data-type");

    //当是 datebox 的时候无法去调用isValid
    //此处只处理能够使用validatebox来判断是否检验成功
    //失败返回false
    if (className && className.indexOf('validatebox-text') >= 0 && !target.validatebox('isValid')) {
        target.focus();
        return {
            status: false
        };
    }

    if (className && className.indexOf('swt') >= 0) {
        value = 0;
        text = '';
        if (target.is(":checked")) {
            value = 1;
            text = '√';
        }
    } else if (className && className.indexOf("combobox-f") >= 0) {
        value = target.combobox("getValues");
        text = target.combobox("getText");
    } else if (className && className.indexOf('timespinner') >= 0) {
        //timespinner多了一层结构，需单独处理。
        value = target.timespinner('getValue');
        text = value;

        return {
            status: true,
            value: value,
            text: text
        };
    } else if (className && className.indexOf('validatebox-text') >= 0) {
        value = target.val();
        text = value;
        if (dataType === "OPT" || dataType === "SEL" || dataType === "MONOIDAL_SCORE") {
            value = target.attr("data-value");
        }
    } else if (className && className.indexOf('datebox') >= 0) {
        value = target.datebox('getValue');
        text = value;
        if (target.attr("data-required")) {
            if (!value) {
                target.focus();
                return {
                    status: false
                };
            }
        }
    } else if (className && className.indexOf('tooltip') >= 0) {
        var checkedbox = target.tooltip('tip').find(".tab-checkbox span input");
        var num = 0;
        checkedbox.each(function (n) {
            if ($(this).is(":checked")) {
                value += ',' + $(this).val();
                text += ',' + $(this).attr('data-text');
                if (num == 0) {
                    value = value.substring(1, value.length);
                    text = text.substring(1, text.length)
                }
                num++;
            }
        });
        target.tooltip('tip').find(".tab-checkbox button#cfmCb").unbind('click');
        target.tooltip('tip').find(".tab-checkbox button#cancelCb").unbind('click');
        target.tooltip('tip').unbind();
        target.tooltip('hide');
        target.tooltip('destroy');
    } else if (!className && $(target).is('input')) {
        value = $(target).val();
        text = value;
    } else if (!className && $(target).is('select')) {
        value = $(target).val();
        text = $(target).children(":checked").html();
    }
    return {
        status: true,
        value: value,
        text: text
    };
}


/*
 *  结束编辑状态
 *
 *  ture:可进行下一步操作
 *  false:行保存失败
 *  @return {
 *      boolean
 *  }
 */
doc.endEditWidget = function () {
    var that = this;

    window.parent.isUnsaved = false;

    //var target = this.editInfo.target;
    var trIndex = this.editInfo.trIndex,
        i = 0,
        status = true,
        valArr = [];
    //if(trIndex === null){ return true;}

    //循环检查每个控件
    var targets = this.editInfo.targetList;
    for (; i < targets.length; i++) {
        if (!targets[i]) {
            continue;
        }
        status = this.rowWidgetDataValid(targets[i]);
        if (status.status) {
            valArr.push({
                value: status.value,
                text: status.text
            });
        } else {
            return false;
        }
    }

    //一个将编辑框数据转换到td上的方式;
    var pTd = null;
    for (i = 0; i < targets.length; i++) {
        if (!targets[i]) {
            continue;
        }
        if (targets[i].length > 0) {
            pTd = targets[i].parent().parent()[0].tagName === "TD" ? targets[i].parent().parent() : targets[i].parent().parent().parent();
            pTd.attr({
                "data-value": valArr[i].value,
                "data-text": valArr[i].text
            });
            pTd.children().html("<div>" + valArr[i].text + "</div>");
        }

    }

    return true;
}

/*
 保存数据到本地
 */
doc.saveToData = function (params, fn) {
    var that = this;

    var inputItem = $(".inputItem");

    if (that.isTableType) {
        inputItem = $('[name="page-header"], [name="page-footer"]').find('.inputItem');
    }

    var recordStr;

    //是否可保存状态 默认true
    var saveStatus = true,
    //必填写并未填项
        sUnFilled = "",
    //总数
        nUnfilledCount = 0;
    var data_item = that.isTableType ? that.Data.data("list_table_header") || {} : that.Data.data("data_item");

    //获取所有非表格性质的录入框内容
    inputItem.each(function (i, n) {
        var type = $(this).attr('data-type');
        var itemName = $(this).attr("itemname");

        //var data_item = that.Data.data("data_item");
        //非表格形式数据获取
        if ($(this).parent().parent().attr('id') !== 'printToolHeader') {
            var data = that.getDataFromInput($(this), type);
        }
        if (data) {
            if (that.isTableType) {
                if ((data_item[data.key] && data_item[data.key].record_value != data.val) ||
                    (typeof data_item[data.key] === 'undefined' && data.val !== '')) {
                    that.isHeaderChanged = true;
                }
            }

            if (data.val == '' && data_item[data.key] && data.list_item && data.list_item.length == 0) {
                data_item[data.key].record_value = '';
                data_item[data.key].list_item = [];
            }
            //if(data.val ||    data.list_item && data.list_item.length>0) ){
            //判断是否存在必填列表中
            if (that.requiredList.indexOf(itemName) >= 0) {
                if ((data.val === "" || !data.val) && data.list_item.length === 0) {
                    $(this).addClass("required-err").prev().addClass("required-text");
                    sUnFilled = sUnFilled + that.itemList[itemName].config.metadata_simple_name + " , ";
                    nUnfilledCount += 1;
                    //改变保存状态;
                    saveStatus = false;
                } else {
                    $(this).removeClass("required-err").prev().removeClass("required-text");
                }
            }
            if (true) {

                if (['page-header', 'page-footer'].indexOf($(n).parent().attr('name')) < 0 && that.isTableType) {
                    return true;
                }
                if (!data_item[data.key]) {
                    data_item[data.key] = {
                        "record_value": data.val,
                        "record_item_id": data.val,
                        "record_id": null
                    };
                    if (data.list_item && data.list_item.length > 0) {
                        data_item[data.key]['list_item'] = data.list_item;
                    }
                } else {
                    //data_item[data.key].record_id = doc.fixedRecordId || null;

                    data_item[data.key].record_value = data.val;
                    if (data.list_item && data.list_item.length > 0) {
                        data_item[data.key]['list_item'] = data.list_item;
                    }
                }
            }
        }
    });

    // 只保存表头:
    if (that.isTableType && params.justTableHeader) {
        if (!that.isHeaderChanged) {
            return;
        }
        // var isTableHeaderChanged = this.checkHeaderIsChanged(inputItem);
        // log(that.isHeaderChanged);
        data_item['CREATE_PERSON'] = {
            "record_id": null,
            "record_item_id": that.nurse.id + '-' + that.nurse.name,
            "record_value": that.nurse.id + '-' + that.nurse.name
        };
        that.Data.data("list_table_header", data_item);
        recordStr = this.localDataToServerData();
        console.log(recordStr);
        //var result = this.onlySaveHeader([]);

        //recordStr.data_item = that.Data.data("list_table_header");
        //recordStr.data_list = result.dataList;
        recordStr.recordId = that.Data.data('tableRecordId') || null;

        if (!recordStr.recordId) {
            recordStr.createTime = new Date().format('yyyy-MM-dd hh:mm:ss');
        } else {
            recordStr.modify_time = new Date().format('yyyy-MM-dd hh:mm:ss');
        }

        recordStr.data_list = [];

        recordStr.isHeader = 1;
        //if (result.isChanged) {
        this.saveToServer(recordStr, params, fn);
        //}
        return;
    }

    //表格形式的数据获取
    var data_list = this.Data.data("data_list");
    var tableList = $("#dataTable");
    var trs = tableList.find("tr[class!=ignore]");
    trs.each(function (rowIndex) {
        log($(this));
        //if ($(this).hasClass('ignore')) return;
        var dataListIndex = parseInt($(this).attr("data-index"));
        var tds = $(this).find('td');
        tds.each(function (cellIndex) {
            var val = that.getTableListData($(this), rowIndex, cellIndex);
            //if(val != undefined){
            if (!data_list[dataListIndex]) {
                data_list.push({
                    "record_id": null,
                    "data_index": null
                });
            }
            that.setTableListData(val, dataListIndex, cellIndex);
            //}
        });
    });
    /*
     动态表头数据
     */
    var dynaList = [];
    var dynaListItems = $(".panel-body-noheader select");
    var list_detail = [];
    dynaListItems.each(function (i, v) {
        var dataKey = $(v).data('name');
        list_detail.push({
            "data_key": dataKey,
            "data_value": $(v).val() || "null"
        });
    });
    dynaList = list_detail.length > 0 ? list_detail : [];
    this.Data.data("dyna_list", dynaList);
    var editInfo = this.editInfo,
        editIndex = editInfo.trIndex,
        pageIndex = editInfo.pageindex;
    if (!saveStatus) {
        sUnFilled = sUnFilled.substring(0, sUnFilled.length - 2);
        $.messager.confirm("提示", "有 <font style='color:#9c0a09;font-size:14px;'>" + nUnfilledCount + "</font> 必填数据未填入:<br><font style='color:#9c0a09;font-size:14px;'>" + sUnFilled + "</font><br>点击<font style='color:#9c0a09;font-size:14px;'>确定</font>保存<br>点击<font style='color:#9c0a09;font-size:14px;'>取消</font>继续编辑", function (r) {
            if (r) {
                //继续保存
                that.editInfo = {
                    targetList: [],
                    trIndex: null,
                    pageindex: pageIndex,
                    target: null,
                    saveStatus: false,
                    editIndex: editIndex
                }

                //将本地数据转换成保存到服务器的格式
                var recordStr = that.localDataToServerData();
                console.log(recordStr);
                that.saveToServer(recordStr, params, fn);
            } else {
                //继续编辑
                $("#save").unbind().bind('click', function () {
          if (!that.permission) {
            $.messager.alert("提示", "当前文书无法修改");
            return;
          }
                    that.saveData();
                });
                that.editInfo.targetList = [];
                that.enterEditStatus(null, that.detail.cell[0], true, editIndex, editIndex);
            }
        });
        return;
    }

    this.editInfo = {
        targetList: [],
        trIndex: null,
        pageindex: pageIndex,
        target: null,
        saveStatus: false,
        editIndex: editIndex
    };

    //将本地数据转换成保存到服务器的格式
    recordStr = this.localDataToServerData();
    if (that.isTableType && !params.justTableHeader) {
        recordStr.data_item = [];
    }
    console.log(recordStr);
    this.saveToServer(recordStr, params, fn);
};

doc.checkHeaderIsChanged = function (inputs) {
    var that = doc;
    var isTableHeaderChanged = false;
    var changeCount = 0;
    var tableHeaderData = jQuery.extend(true, {}, that.Data.data('list_table_header'));

    // 如果表头数据未更改,则不传给后端
    if (!that.Data.data('tableRecordId')) {
        return true;
    }

    inputs.each(function (i, item) {
        var type = $(this).attr('data-type');
        var itemName = $(this).attr("itemname");

        // if (tableHeaderData[i]) {
        //非表格形式数据获取
        if ($(this).parent().parent().attr('id') !== 'printToolHeader') {
            var data = that.getDataFromInput($(this), type);
        }
        if (data) {
            if (data.key in tableHeaderData) {
                if (tableHeaderData[data.key].record_value != data.val) {
                    changeCount += 1;
                    isTableHeaderChanged = true;
                }
            } else {
                isTableHeaderChanged = true;
                return false;
            }
        }

        // } else {
        // return true;
        // }
    });

    return isTableHeaderChanged;
};

doc.getTableHeaderData = function (inputs) {
    var that = doc;
    var dataHeaderArr = [{
        data_key: 'CREATE_PERSON',
        data_value: that.nurse.id + '-' + that.nurse.name
    }, {
        data_key: 'DATE_LIST',
        data_value: new Date().format('yyyy-MM-dd')
    }, {
        data_key: 'TIME_LIST',
        data_value: new Date().format('hh:mm')
    }];
    var tableHeaderData = that.Data.data('list_table_header');

    inputs.each(function (i, n) {
        var type = $(this).attr('data-type');
        var itemName = $(this).attr("itemname");
        //非表格形式数据获取
        if ($(this).parent().parent().attr('id') !== 'printToolHeader') {
            var data = that.getDataFromInput($(this), type);
        }
        if (data) {
            if (data.val == '' && tableHeaderData[data.key] && data.list_item && data.list_item.length == 0) {
                tableHeaderData[data.key].record_value = '';
                tableHeaderData[data.key].list_item = [];
            }

            if ($(this).parent().attr('name') == 'page-header') {
                dataHeaderArr.push({
                    data_key: data.key,
                    data_value: data.val
                });
            }

            if (!tableHeaderData[data.key]) {
                tableHeaderData[data.key] = {
                    "record_value": data.val,
                    "record_item_id": data.val,
                    "record_id": null
                };
                if (data.list_item && data.list_item.length > 0) {
                    tableHeaderData[data.key]['list_item'] = data.list_item;
                }
            } else {
                //data_item[data.key].record_id = doc.fixedRecordId || null;
                tableHeaderData[data.key].record_value = data.val;
                if (data.list_item && data.list_item.length > 0) {
                    tableHeaderData[data.key]['list_item'] = data.list_item;
                }
            }
        }
    });

    return dataHeaderArr;
};

doc.getTableHeader = function () {
    return $.get(ay.contextPath + '/nur/doc/getDocHeaderData', {
        patID: doc.loadDataParams.patientId,
        templateID: doc.loadDataParams.template_id.replace('print_', ''),
        isPrint: doc.loadDataParams.template_id.indexOf('print') >= 0
    }).done(function (res) {
        if (res.rslt == 0) {
            return res.data
        } else {
            return null;
        }
    })
};

// 只保存表头
doc.onlySaveHeader = function (dataList) {
    var $tableHeader = $('[name="page-header"]');
    var $inputs = $(".inputItem");
    var isTableHeaderChanged = this.checkHeaderIsChanged($inputs);

    //if (isTableHeaderChanged) {
    var headerData = doc.getTableHeaderData($inputs);

    dataList.push({
        data_index: null,
        record_id: doc.Data.data('tableRecordId') || null,
        list_detail: []
    });
    //}

    return {
        dataList: dataList,
        isChanged: true
    };
};

// 保存至服务器
doc.saveToServer = function (recordStr, params, fn) {
    var that = this;
    var rId = recordStr.recordId,
        update = false;
    if (rId) {
        update = true;
    }
    try {
        $.post("./saveData", {
            "recordStr": JSON.stringify(recordStr),
            "isNurseRecord": that.isNurseDoc
        }, function (d) {

            if (d.rslt == "0") {
                var rId = d.msg; // 保存存储后生成的ID

                // 更新出入量统计数据
                // 如果保存的是表头数据，返回的结果没有统计数据
                if (d.data && d.data.docStatistic) {
                    that.transformStatistic(d.data.docStatistic, true);
                }

                if (params.justTableHeader) {
                    doc.Data.data('tableRecordId', rId);
                    $.messager.show({
                        title: '提示',
                        msg: '保存成功!',
                        timeout: 2000,
                        showType: 'fade',
                        style: {
                            right: '',
                            top: document.body.scrollTop + document.documentElement.scrollTop + 30,
                            bottom: ''
                        }
                    });
                    return;
                }
                if (that.loadDataParams.report_type === '1') {
                    $.messager.show({
                        title: '提示',
                        msg: '保存成功!',
                        timeout: 2000,
                        showType: 'fade',
                        style: {
                            right: '',
                            top: document.body.scrollTop + document.documentElement.scrollTop + 30,
                            bottom: ''
                        }
                    });
                } else {
                    $.messager.alert('提示信息', '保存成功!', 'info', function () {
                        var node = window.parent.treeNode;
                        var url = window.parent.accessUrl;

                        //log($(window.parent.document).find('#mainMenu').tree('options'));
                        //$(window.parent.document).find('#mainMenu').tree('select', node.target);
                        if (!!node && (node['type'] === 'day' || node['type'] === '2')) {
                            setTimeout(function () {
                                window.location.reload();
                            }, 100);
                        }

                        //window.location.reload();
                    });
                }

                $("#save").unbind().bind('click', function () {
          if (!that.permission) {
            $.messager.alert("提示", "当前文书无法修改");
            return;
          }
                    that.saveData();
                });

                /*setTimeout(function () {
                 var node = window.parent.treeNode;
                 $('#mainMenu').tree('expandTo', node.target).tree('select', node.target);
                 }, 1500);*/

                //重新渲染view
                debugger;

                that.dataToView(null, false);

                if (fn) {
                    fn.call(doc);
                }

                // var select = $("#pagination").find(".p-mid select");
                // select.val(that.page.count);
                // select.trigger("change");

                // 动态更新树
                if (update) {
                    //更新表格日期时间
                    // window.location.reload();
                    //that.loadData(that.url);
                    return;
                }
                if (that.isTableType) {
                    //  var tableInfo = that.url;
                    //  var treeNode = {
                    //      id:tableInfo.id,
                    //      pId:tableInfo.patientId,
                    //      rId:null,
                    //      tId:tableInfo.template_id,
                    //      text:tableInfo.day,
                    //      type:"day",
                    //      children:{
                    //          rId:rId,
                    //          text:tableInfo.time,
                    //      }
                    //  };
                    //  if("recordId" in that.url){ that.url.recordId = rId; }

                    //更新添加行的recordId
                    var data_list = that.Data.data("data_list");
                    if (data_list[data_list.length - 1]) {
                        data_list[data_list.length - 1].record_id = rId;
                    }
                    //更新数据行的rID
                    that.setDataItemList(recordStr, rId);

                    //  //window.parent.window.updateTreeTableInfo(treeNode);
                    window.parent.window.loadMenu();
                    //  //that.loadData(that.url);
                } else {
                    // if("recordId" in that.url){ that.url.recordId = rId;}
                    // window.parent.window.updateTreeInfo(rId);
                    // that.Data.data("recordId",rId);
                    // var url = window.location.href;
                    // var index = url.indexOf("recordId=")+9;
                    // var _url = url.substring(0,index)+rId+url.substring(index,url.length);
                    // that.reloadUrl.page = _url;
                    doc.fixedRecordId = rId;
                    that.Data.data('recordId', rId);
                    //更新数据行的rID
                    //that.setDataItemList(recordStr, rId);
                    window.parent.window.loadMenu();
                }
            } else {
                if (!params.justTableHeader) {
                    $.messager.alert('提示', d.msg || '保存失败, 请刷新后重试');
                }
            }
        });
    } catch (e) {
        console.log(e);
        $("#save").unbind().bind('click', function () {
      if (!that.permission) {
        $.messager.alert("提示", "当前文书无法修改");
        return;
      }
            that.saveData();
        });
    }
}

doc.saveData = function (fn) {
    var tableList = $("#dataTable"),
        trs = tableList.find("tr[class!='ignore']"),
        editInfo = this.editInfo,
        that = this;
    var $tableHeader = $('[name="page-header"]');
    var $inputs = $tableHeader.find('.inputItem');

    if (editInfo.saveStatus || !this.isTableType) {
        var endEditStatus = this.endEditWidget();

        //控件校验失败
        if (!endEditStatus) {
            $.messager.alert('提示', '请检查数据是否填写完整!');
            return;
        }

        if (trs.length === 1 && this.soreGrade) {
            var td = $(trs[0]).find("td[itemname='PRESSURE_SORE']"),
                input = $("input[data-name='PRESSURE_SORE_GRADE']");
            input.val(td.attr("data-value"));

        }
        // $("#save").unbind("click");
        if (this.isListTotal) {
            this.isSaving = true;
        }
        this.saveToData({
            "active": true
        }, fn);
        if (this.isTableType) {
            this.saveToData({
                "active": true,
                'justTableHeader': true
            }, fn);
        }

        //this.onlySaveHeader(this.Data.data('data_list'));
    } else {
        if ($inputs.length === 0) {
            $.messager.alert("提示", "请新增一条数据，或者修改一条数据!");
        } else {
            if (this.isTableType) {
                //this.onlySaveHeader(this.Data.data('data_list'));
                this.saveToData({
                    "active": true,
                    'justTableHeader': true
                }, fn);
            }
        }
    }
};

/*
 加载审签事件
 */
doc.approveRecord = function (btn) {
    var that = this;

    var recordIndex = parseInt(btn.attr("data-index")),
        oldId = this.detail.row[recordIndex].create_person,
        rId = this.detail.row[recordIndex].record_id,
        tr = $("#dataTable tr[data-index='" + recordIndex + "']"),
        id = this.nurse.id,
        name = this.nurse.name,
        data = {
            "approve_person": id + "-" + name,
            "list": [{
                "record_id": rId
            }]
        }
    try {
        $.post("./approve", {
            "json": JSON.stringify(data)
        }, function (msg) {
            console.log(msg)
            if (true) {
                btn.unbind("click");
                td = tr.find("td[itemname='APPROVE_PERSON']");
                td.attr("data-value", id);
                td.attr("data-text", name);
                td.children().children().html(name);
                that.detail.row[recordIndex].approve_status = "Y";
                btn.animate({
                    left: "-30px"
                }, 200, function () {
                    $(this).remove();
                });
            }
        });
    } catch (e) {
        console.log(e);
    }
};

/*
 * 表头数据转换
 *
 * */
doc.listToObj = function (list) {
    var obj = {};

    list.forEach(function (item) {
        obj[item.template_item_id] = {
            record_id: item.record_id || '',
            record_item_id: item.record_item_id || '',
            record_value: item.record_value || ''
        }

        if (item.list_item) {
            obj[item.template_item_id].list_item = item.list_item;
        }
    });

    return obj;
};

/*
 *  传入目标节点,类型
 *  返回{id:data}
 */
doc.getDataFromInput = function (target, type) {
    var that = this;
    if (!type) {
        return null;
    }
    var key = target.attr('itemName'),
        val = '',
        list_item = [],
        list_son = [],
        input = null,
        cSpan = null;

    if ('STR' === type || 'NUM' === type || 'BP' === type || 'FIELD' === type) {
        input = target.children();
        val = input.val();
    } else if ("CREATE_PERSON" === key || "APPROVE_PERSON" === key || "CREATE_PERSON2" === key) {
        input = target.children();
        val = "";

        if (/*input.attr("data-value") !== that.nurse.id && */ input.attr("data-value") && input.attr("data-text")) {
            val = input.attr("data-value") + "-" + input.attr("data-text");
        }
        /*else {
         val = $(window.parent.document).find('#nurseId').val() + "-" + $(window.parent.document).find('#nurseName').text();
         }*/
    } else if ('SYS_TIME' === type) {
        input = target.children();
        if (key === 'CREATE_TIME') {
            val = input.datetimebox('getValue') === '' ? new Date().format('yyyy-MM-dd hh:mm') : input.datetimebox('getValue');
        } else {
            val = input.datetimebox('getValue');
        }
    } else if ("DATE" === type) {
        input = target.children();
        val = input.datebox('getValue');
    } else if ('SEL' === type) {
        //单选
        input = target.find("div > span > label > input:checked");
        if (input.length == 0) {
            return {
                "key": key,
                "val": val,
                "list_item": []
            }
        }
        val = input.val();
        cSpan = input.parent().parent().children("span");
        var cbc = this.getCheckBoxChild(cSpan);
        for (x in cbc) {
            if (cbc[x] instanceof Array && cbc[x].length >= 0) {
                for (var i = 0; i < cbc[x].length; i++) {
                    list_son.push({
                        "template_item_id": x,
                        "record_value": cbc[x][i]
                    });
                }
            } else {
                list_son.push({
                    "template_item_id": x,
                    "record_value": cbc[x]
                });
            }
        }
        //if(list_son.length > 0){
        list_item.push({
            "template_item_id": input.val(),
            "record_value": input.val(),
            "list_item": list_son
        });
    } else if ('MSEL' === type) {
        var that = this;
        //多选
        //数据分隔符采用[\,,\#,]
        input = target.find("div > span > label > input:checked");
        if (input.length == 0) {
            return {
                "key": key,
                "val": val,
                "list_item": []
            }
        }

        input.each(function () {
            val += $(this).val() + ",";
            cSpan = $(this).parent().parent().children("span");
            var cbc = that.getCheckBoxChild(cSpan);
            for (x in cbc) {
                if (cbc[x] instanceof Array && cbc[x].length >= 0) {
                    for (var i = 0; i < cbc[x].length; i++) {
                        list_son.push({
                            "template_item_id": x,
                            "record_value": cbc[x][i]
                        });
                    }
                } else {
                    list_son.push({
                        "template_item_id": x,
                        "record_value": cbc[x]
                    });
                }
            }
            list_item.push({
                "template_item_id": $(this).val(),
                "record_value": $(this).val(),
                "list_item": list_son
            });
        });
        val = val.substring(0, val.length - 1);
    } else if ('OPT' === type) {
        input = target.children();
        if (input[0].tagName === 'SPAN') {
            val = input.data('value');
        }
        if (input.children().length > 0 && input.children()[0].tagName === 'SELECT') {
            val = input.children().val();
        }
        if (target.data('is-changed') || input.data('options')) {
            val = input.attr('data-value');
        } else {
            val = input.val();
        }
        //if (input)
    } else if ("MOPT" === type) {
        input = target.find("input[data-name='" + key + "']");
        console.log(input);
        val = input.combobox("getValues").toString();
    }
    /* else if (type === 'DATA_ONE' || type === 'DATA_TWO' || type === 'DATA_THREE') {
     log('*******', type);
     }*/
    else if (type === 'SWT') {
        input = target.find("input[data-name='" + key + "']");
        val = input[0].checked ? 'Y' : 'N';
    } else if (type === 'SIGNATURE') {
        if (target.children('input').val() !== '') {
            val = target.children('input').attr('data-value') + '-' + target.children('input').val();
        }
    }
    return {
        "key": key,
        "val": val,
        "list_item": list_item
    }
}

/*
 *  获取表格列表录入数据
 */
doc.getTableListData = function (target, rIndex, cIndex) {
    var type = target.attr("data-type");
    var value = target.attr("data-value") || "";
    var itemName = target.attr("itemname");
    if (itemName === "DATE_LIST") {
        this.url.day = value;
    }
    if (itemName === "TIME_LIST") {
        this.url.time = value;
    }
    if (itemName === "CREATE_PERSON" || itemName === "APPROVE_PERSON") {
        var text = target.attr("data-text") || "";
        if (!text && !value) {
            value = ""
        } else {
            value += "-" + text;
        }

    }
    if (type == "SWT") {
        if (value == 1) {
            value = "Y";
        } else {
            value = "N";
        }
    }
    return value;
}

/*
 *  将table数据存入data_list
 *
 */
doc.setTableListData = function (val, rIndex, cIndex) {
    var data_list = this.Data.data("data_list");
    var cellObj = data_list[rIndex];
    var cell = this.detail.cell[cIndex];
    log(val, rIndex, cIndex);
    var itemName = cell.params.item_name;
    if (!cellObj[itemName]) {
        cellObj[itemName] = {
            "data_value": (val ? val : "")
        };
    } else {
        cellObj[itemName].data_value = (val ? val : "");
    }
}

doc.localDataToServerData = function () {
    var data_item = this.isTableType ? this.Data.data('list_table_header') : this.Data.data('data_item');
    var data_list = this.Data.data('data_list');
    var dyna_list = this.Data.data("dyna_list");

    if (!('CREATE_PERSON' in data_item)) {
        data_item['CREATE_PERSON'] = {
            record_id: null,
            record_item_id: this.nurse.id + '-' + this.nurse.name,
            record_value: this.nurse.id + '-' + this.nurse.name
        };
    }

    var recordStr = {};
    if (!this.Data.data('recordId')) {
        recordStr = {
            "recordId": null,
            "createTime": null,
            "modify_time": null,
            "inpatient_no": this.Data.data("inpatient_no"),
            "template_id": this.Data.data('template_id'),
            "data_list": [],
            "data_item": []
        };
    } else {
        recordStr = {
            "recordId": this.Data.data('recordId'),
            "createTime": this.Data.data('createTime'),
            "modify_time": this.Data.data('modify_time'),
            "inpatient_no": this.Data.data('inpatient_no'),
            "template_id": this.Data.data('template_id'),
            "data_list": [],
            "data_item": []
        };
    }
    for (x in data_item) {
        recordStr.data_item.push({
            "template_item_id": x,
            "record_value": data_item[x].record_value,
            "record_item_id": data_item[x].record_item_id,
            "record_id": null
        });
        if (data_item[x].list_item) {
            recordStr.data_item[recordStr.data_item.length - 1]["list_item"] = data_item[x].list_item;
        }
    }
    var list_detail = [];
    var index = this.editInfo.editIndex;
    for (var i = 0; i < data_list.length; i++) {
        if (index >= 0 && index != i) continue;
        list_detail = [];
        for (cell in data_list[i]) {
            if (cell != "record_id" && cell != "data_index") {
                list_detail.push({
                    "data_key": cell,
                    "data_value": data_list[i][cell].data_value
                });
            }
        }
        recordStr.data_list.push({
            "record_id": data_list[i].record_id,
            "data_index": null,
            "list_detail": list_detail
        });

    }
    //暂时只能一条一条更新
    //外部recordId存放的最新一条ID
    //将内部record_id提取到外部
    if (recordStr.data_list.length > 0) {
        recordStr.recordId = recordStr.data_list[0].record_id;
    }

    recordStr.dyna_list = dyna_list;
    return recordStr;
}

//设置编辑的行索引，用以判断是否修改的不是当前行
doc.setEditIndex = function (target) {

    // 如果是动态表头或签名列则不进行编辑索引操作;
    var filterPass = ["dynaTh"];
    if (filterPass.indexOf(target.attr("data-type")) >= 0) {
        return true;
    }

    // 设置是否可保存标识
    this.isEdit.saveState = true;

    var tr = target.parent();
    var index = parseInt(tr.attr("data-index"));
    if (typeof this.editIndex == 'number' && this.editIndex != index) {

        // 不是同一行保存
        this.isEdit = {
            // state:false, //是否可被编辑
            target: null, //被编辑的目标
            editStatus: false,
            saveState: false, // 是否可被保存 默认false 进入任何一个控件后保存状态都变成true
            index: {
                tr: null,
                td: null
            },
            thIndex: null //用来处理动态表头的
        }
        this.isEdit.editStatus = false;
        this.saveToData({
            "active": false
        });
        $("#dataTable").find("tr").removeClass("edit");
        tr.addClass("edit");
        this.editIndex = index;
    } else if (index === this.editIndex) {
        //操作的是同一行，不处理退出
    } else {
        $("#dataTable").find("tr").removeClass("edit");
        tr.addClass("edit");
        this.editIndex = index;
    }
    return false;
};

/*
 解决中文标点符号换行问题
 */
doc.replaceChineseChar = function (str) {
    if (typeof str === 'string') {
        return str.replace(/([，。、！：；？])/g, '$1 ');
    } else {
        return str;
        console.error('str：必须是字符串!');
    }
};

/*
 数据到cell
 */
doc.dataToCellView = function (tr, x, dl, i) {
    var that = this;
    var td = tr.find("td[itemname='" + x + "']"),
        type = td.attr('data-type'),
        itemName = td.attr("itemname"),
        reqType = this.url.type, //请求类型
        index = i,
        tdLen = tr.children('td').length;
    if (['data_index', 'record_id'].indexOf(x) >= 0) {
        return;
    }
    //增加了对 null，空 数据的处理
    if (!dl[i][x].data_value && ["inTake", "outTake"].indexOf(itemName) < 0) {
        td.attr({
            "data-value": "",
            "data-text": ""
        });
        td.children().html("<div></div>");
        return;
    }
    //
    if (type === 'DATE' && !('inTake' in dl[i]) && !('outTake' in dl[i])) {
      that.setInAndOutStastics(tr, x, dl, i);
    }
    if (type === 'SIGNATURE') {

        if (dl[i][x].data_value && dl[i][x].data_value != "-") {
            td.attr({
                "data-value": dl[i][x].data_value.split("-")[0],
                "data-text": dl[i][x].data_value.split("-")[1] || dl[i][x].data_value.split("-")[0]
            });
            td.children().html("<div>" + (dl[i][x].data_value.split("-")[1] || dl[i][x].data_value.split("-")[0]) + "</div>");
        }

    } else if (type == 'SWT') {
        if (dl[i][x].data_value == "Y") {
            td.attr({
                "data-value": 1
            });
            td.children().html("<div>√</div>");
        } else {
            td.attr({
                "data-value": 0
            });
            td.children().html("<div></div>");
        }
    } else if (type == 'SEL' || type == 'MSEL') {
        var selIndex = '';
        var checkbox = this.detail.cell[td.index()].params.checkbox;
        var data_value = dl[i][x].data_value;
        var show_type = "";
        $.each(data_value.split(","), function (ci, n) {
            for (var ci = 0; ci < checkbox.length; ci++) {
                if (checkbox[ci].checkbox_code == n) {
                    show_type = checkbox[ci].show_type ? checkbox[ci].show_type : "ARAB";
                    selIndex += (that.SelTextObj[show_type][ci]) + ",";
                }
            }
        });
        if (selIndex.length > 0) {
            selIndex = selIndex.substring(0, selIndex.length - 1);
        }
        td.attr({
            "data-value": dl[i][x].data_value,
            "data-text": selIndex
        });
        td.children().html("<div>" + that.replaceChineseChar(selIndex) + "</div>");
    } else if (type == 'OPT' || type === 'MONOIDAL_SCORE') {
        var optStr = this.getValFormOptions(dl[i][x].data_value, this.detail.cell[td.index()].params.options) || '';
        var dIndex = optStr && optStr.indexOf('、');

        // 存在[、]截取顿号前面的字符
        if (optStr && dIndex > -1) {
            optStr = optStr.substring(0, dIndex);
        }

        td.attr({
            "data-value": dl[i][x].data_value,
            "data-text": optStr
        });
        td.children().html("<div>" + that.replaceChineseChar(optStr) + "</div>");
    } else if (type == "SCORE_TOTAL_ROW") {
        var count_list = this.Data.data("count_list"),
            currValue = (dl[i][x].data_value ? Number(dl[i][x].data_value) : ''),
            view_value = dl[i][x].view_value && currValue != dl[i][x].view_value.split('/')[0] ? currValue : dl[i][x].view_value || currValue,
            totalValue = typeof view_value == 'string' ? view_value.split('/')[1] : view_value || 0;

        //debugger;

        if (["inTake", "outTake"].indexOf(itemName) >= 0) {
            that.setInAndOutStastics(tr, x, dl, i);
        } else {
            td.attr({
                "data-value": dl[i][x].data_value,
                "data-text": dl[i][x].data_value
            });
            td.children().html("<div>" + view_value + "</div>");
        }
    } else if (type == "NUM") {
        var currValue = 0,
            prevValue = 0,
            totalValue = 0;
        if (["inTake", "outTake"].indexOf(itemName) >= 0) {
            //第一行的特殊处理
            if (index === 0) {
                // if (reqType === "day") {
                td.attr({
                    "data-value": dl[i][x].data_value,
                    "data-text": dl[i][x].data_value,
                    "data-totalValue": beforeData
                });
                td.children().html("<div>" + dl[i][x].data_value + ((beforeData === 0 || beforeData === null) ? "" : "/" + beforeData) + "</div>");
                // } else {
                //     td.attr({
                //         "data-value": dl[i][x].data_value,
                //         "data-text": dl[i][x].data_value,
                //         "data-totalValue": 0
                //     });
                //     td.children().html("<div>" + dl[i][x].data_value + "</div>");
                // }
            } else {
                //判断当前日前是否与前一天日期相同
                log(dl);
                var section = doc.compareSection(dl[i - 1].DATE_LIST.data_value, dl[i - 1].TIME_LIST.data_value, dl[i].DATE_LIST.data_value, dl[i].TIME_LIST.data_value, breakTime);
                currValue = (!dl[i][x].data_value || dl[i][x].data_value == null) ? 0 : Number(dl[i][x].data_value);
                if (section === "add") {
                    //为第二条时累加前一行录入值
                    if (index == 1) {
                        prevValue = !dl[i - 1][x] ? 0 : (!dl[i - 1][x].data_value ? 0 : Number(dl[i - 1][x].data_value));
                        //prevValue = (!dl[i-1][x] || !dl[i-1][x].data_value) || 0;
                        //prevValue = prevValue === 0 ? 0 : parseInt(prevValue.data_value) ;
                        //prevValue = (!dl[i-1][x].data_value || ( !dl[i-1][x] || dl[i-1][x].data_value==null) ) ? 0 :  parseInt(dl[i-1][x].data_value);
                        /*if (x === 'outTake') {
                         for (var j = i - 1; j > 0; j -= 1) {
                         preItemName = !dl[j][outNameKey] ? '' : dl[j][outNameKey]['data_value'];
                         if (preItemName === '' || currItemName === '') continue;
                         var isSame = doc.compareSection(dl[j]['DATE_LIST'].data_value, dl[j]['TIME_LIST'].data_value, dl[i]['DATE_LIST'].data_value, dl[i]['TIME_LIST'].data_value, breakTime);
                         // 类目相同 且 在同一个时间区间时，出量分类统计
                         if (preItemName === currItemName && x === 'outTake' && isSame === 'add') {
                         totalValue += Number(dl[j][x]['data_value']);
                         }
                         }
                         totalValue += currValue;
                         } else {
                         totalValue = currValue + prevValue;
                         }*/
                        totalValue = currValue + prevValue;
                        td.attr({
                            "data-value": currValue,
                            "data-text": currValue,
                            "data-totalValue": totalValue
                        });

                        if (currValue === 0) {
                            td.children().html("<div></div>");
                        } else {
                            td.children().html("<div>" + currValue + "/" + totalValue + "</div>");
                        }
                    }
                    //大第二条时累加前一行累加值
                    else {
                        var _td = tr.prev().find("td[itemname='" + itemName + "']");
                        prevValue = !_td.attr("data-totalValue") ? 0 : Number(_td.attr("data-totalValue"));
                        totalValue = currValue + prevValue;
                        td.attr({
                            "data-value": currValue,
                            "data-text": currValue,
                            "data-totalValue": totalValue
                        });
                        if (currValue === 0) {
                            td.children().html("<div></div>");
                        } else {
                            td.children().html("<div>" + currValue + "/" + totalValue + "</div>");
                        }
                    }
                }
                if (section === "break") {
                    td.attr({
                        "data-value": dl[i][x].data_value,
                        "data-text": dl[i][x].data_value,
                        "data-totalValue": dl[i][x].data_value
                    });
                    td.children().html("<div>" + dl[i][x].data_value + "</div>");
                }

            }
        } else {
            td.attr({
                "data-value": dl[i][x].data_value,
                "data-text": dl[i][x].data_value
            });
            td.children().html("<div>" + dl[i][x].data_value + "</div>");
        }

    } else {
        td.attr({
            "data-value": dl[i][x].data_value,
            "data-text": dl[i][x].data_value
        });
        td.children().html("<div>" + dl[i][x].data_value + "</div>");
        if ("DATE" == type) {
            //std.css("position","relative");

        }
    }
};

doc.setInAndOutStastics = function (tr, x, dl, i) {
  var that = this;
  var td = tr.find("td[itemname='" + x + "']"),
      type = td.attr('data-type'),
      itemName = td.attr("itemname"),
      reqType = this.url.type, //请求类型
      index = i,
      tdLen = tr.children('td').length;
  var preItemName,
      oldValue = [],
      currItemName,
      prevValue = 0,
      totalValue = 0,
      count_list = this.Data.data("count_list"),
      currValue = (dl[i][x].data_value ? Number(dl[i][x].data_value) : ''),
      view_value = dl[i][x].view_value && currValue != dl[i][x].view_value.split('/')[0] ? currValue : dl[i][x].view_value || currValue,
      totalValue = typeof view_value == 'string' ? view_value.split('/')[1] : view_value || 0;

  //debugger;

      that.outName = [];
      that.outTotal = [];
      that.inTotal = 0;
      //第一行的特殊处理
      log(that.prevDayData);
      //debugger;
      var inTime = that.inpatient_info.find(function (item) {
          return item.key === 'BE_HOSPITALIZED_TIME';
      });

    // 有些文书没有时间  囧rz
    if (!dl[i].TIME_LIST) {
        td.attr({
            "data-value": dl[i][x].data_value,
            "data-text": dl[i][x].data_value
        });
        // td.children().html("<div>" + currValue + "</div>");
        td.children().html("<div>" + view_value + "</div>");

        return;
    }

      var currentDay = new Date(dl[i].DATE_LIST.data_value + ' ' + dl[i].TIME_LIST.data_value).getTime(),
          nextDay = !dl[i + 1] ? 0 : new Date(dl[i + 1].DATE_LIST.data_value + ' ' + dl[i + 1].TIME_LIST.data_value).getTime(),
          prevDay = !dl[i - 1] ? 0 : new Date(dl[i - 1].DATE_LIST.data_value + ' ' + dl[i - 1].TIME_LIST.data_value).getTime(),
          breakDay1 = new Date(dl[i].DATE_LIST.data_value + ' ' + that.breakTime).getTime(),
          breakDay2 = new Date(dl[i].DATE_LIST.data_value + ' ' + that.breakTimeAfternoon).getTime(),
          preInTotal = !that.prevDayData['value'] || that.prevDayData['value'] === '' ? 0 : that.prevDayData['value'],
          inHospTime = inTime && inTime.value || 0,
          eIndex,
          str = '',
          outNameKey = that.outInColumn.out,
          inNameKey = that.outInColumn.input;
      //preOutTotal = that.prevDayData.length === 0 ? 0 : that.prevDayData[1]['data_value'];
      preOutTotal = 0,
          currentDateStatistic = that.statistic.find(function (item) {
              return item.date == dl[i].DATE_LIST.data_value;
          });

      // 获取出量的code


      if (that.prevDayData['value'] === '') {
          that.prevDayData['value'] = 0;
      }

      if (index === 0) {
          log(dl[i + 1]);
          currItemName = (typeof dl[i][outNameKey] === "undefined" || dl[i][x].data_value === null || dl[i][x].data_value === "") ? "" : dl[i][outNameKey].data_value;
          // if (reqType === "day") {
          var _total = 0;
          count_list = count_list || {};
          count_list[itemName] = count_list[itemName] || "";
          if (!tr.prev().hasClass('ignore')) {
              str = '';
              // 第一条时间等于7点并且存在下一条数据
              // ---------
              // 6:00 or 7:00
              // ---------
              // ...
              if (currentDay <= breakDay1) {

                  //计算出量, 并更新前一天出量数据 prevDayDate['outList']
                  if (x === 'outTake') {
                      // 第一条数据也是最后一条时, 插入下午换班数据.
                      if (nextDay === 0) {
                          if (currentDateStatistic && currentDateStatistic.morning) {
                              $(currentDateStatistic.morning).insertAfter(tr);
                          }
                      }

                      // 下一条记录时间大于7点, 避免7点多条数据时插入多条总量
                      if (nextDay > breakDay1) {
                          if (currentDateStatistic && currentDateStatistic.morning) {
                              $(currentDateStatistic.morning).insertAfter(tr);
                          }
                      }
                  } else {
                      // 计算入量,并更新前一天入量数据
                      if (!dl[i]['outTake']) {
                          // 第一条数据也是最后一条时, 插入下午换班数据.
                          if (nextDay === 0) {
                              if (currentDateStatistic && currentDateStatistic.morning) {
                                  $(currentDateStatistic.morning).insertAfter(tr);
                              }
                          }

                          // 下一条记录时间大于7点, 避免7点多条数据时插入多条总量
                          if (nextDay > breakDay1) {
                              if (currentDateStatistic && currentDateStatistic.morning) {
                                  $(currentDateStatistic.morning).insertAfter(tr);
                              }

                          }
                      }
                  }
              }
              //计算出量, 并更新前一天出量数据 prevDayDate['outList']
              if (currentDay > breakDay1) {

                  if (x === 'outTake') {
                      // 第一条数据也是最后一条时, 插入下午换班数据.
                      if (nextDay === 0) {
                          // 第一条记录时间在下午3点之前, 并且是最后一条时.
                          // 总出量是当前记录的出量
                          var currInValue = tr.find('[itemname="inTake"]').data('totalvalue'),
                              currItemText = td.prev().data('text');
                          if (currentDateStatistic && currentDateStatistic.morning) {
                              $(currentDateStatistic.morning).insertBefore(tr);
                          }
                          if (currentDay < breakDay2) {
                              if (currentDateStatistic && currentDateStatistic.afternoon) {
                                  $(currentDateStatistic.afternoon).insertAfter(tr);
                              }
                          }
                      }

                      // 下一条记录时间大于7点, 避免7点多条数据时插入多条总量
                      // if (preInTotal !== 0 || str !== '') {
                      if (nextDay > breakDay1) {
                          if (currentDateStatistic && currentDateStatistic.morning) {
                              $(currentDateStatistic.morning).insertBefore(tr);
                          }
                      }
                      // }
                  } else {
                      // 计算入量,并更新前一天入量数据
                      //that.prevDayData['value'] = currValue + Number(preInTotal);
                      _total = currValue;
                      if (!dl[i]['outTake']) {
                          if (currentDateStatistic && currentDateStatistic.morning) {
                              $(currentDateStatistic.morning).insertBefore(tr);
                          }
                          // 第一条数据也是最后一条时, 插入下午换班数据.
                          if (currentDay <= breakDay2 && nextDay === 0) {
                              if (currentDateStatistic && currentDateStatistic.morning) {
                                  $(currentDateStatistic.morning).insertAfter(tr);
                              }
                          }
                      }
                  }
              }
              //}
              // }


              td.children().html("<div>" + view_value + "</div>");

          } else {
              if (currValue === "") {
                  td.children().html("<div></div>");
              } else {
                  td.children().html("<div>" + currValue + "</div>");
              }
          }

          td.attr({
              "data-name": currItemName,
              "data-value": dl[i][x].data_value,
              "data-text": td.prev().data('text'),
              "data-totalValue": totalValue
          });

          // 如果第一条数据没有intake
          if (!dl[i]['inTake']) {
              td.siblings('[itemname="inTake"]').attr({
                  "data-totalValue": Number(that.prevDayData['value'])
              })
          }

      } else {
          // TODO 第二条记录开始
          preItemName = (typeof dl[i - 1][outNameKey] === "undefined" || dl[i - 1]['outTake'].data_value === null || dl[i - 1]['outTake'].data_value === "undefined") ? "" : dl[i - 1][outNameKey].data_value;
          currItemName = (typeof dl[i][outNameKey] === "undefined" || dl[i][x].data_value === null || dl[i][x].data_value === "") ? "" : dl[i][outNameKey].data_value;

          var section = doc.compareSection(dl[i - 1].DATE_LIST.data_value, dl[i - 1].TIME_LIST.data_value, dl[i].DATE_LIST.data_value, dl[i].TIME_LIST.data_value, breakTime);
          currValue = (dl[i][x].data_value === "" || dl[i][x].data_value === null) ? "" : Number(dl[i][x].data_value);
          var addValue = currValue || 0;
          var valueText = '';
          var _td = tr.prev().find("td[itemname='" + itemName + "']");

          /*var _td = tr.prev().find("td[itemname='" + itemName + "']"),
           trPrev;

           for (var ind = tr.data('index'); ind > 0; i -= 1) {
           trPrev = $("#dataTable").find('tr[data-index="' + ind + '"]');
           if (Number(trPrev.data('totalvalue')) !== 0) {
           prevValue = trPrev.find("td[itemname='" + itemName + "']");
           }
           }*/

          prevValue = !_td.attr("data-totalValue") ? 0 : Number(_td.attr("data-totalValue"));
          var prevInValue = tr.prev().hasClass('ignore') ? tr.prev().prev().find('td[itemname="inTake"]').data('totalvalue') : tr.prev().find('td[itemname="inTake"]').data('totalvalue');
          if (prevInValue === '') {
              prevInValue = 0;
          }
          prevInValue = prevInValue || 0;
          // 统计总出量和分类总入量
          var outTotal = 0,
              inTotal = 0;
          //判断当前日前是否与前一天日期相同

          if (section === "add" || section === 'yesterday') {
              //为第二条时累加前一行录入值

              //判断当前行是否需要加粗
              // var _bold = "";
              // if (dl[i].TIME_LIST.data_value === breakTime) {
              //     _bold = "b";
              // }

              //大于第二条时累加前一行累加值 并且类目相同时
              //if ( preItemName === currItemName ) {
              // if (x === 'outTake') {
              //
              //     for (var j = i - 1; j >= 0; j -= 1) {
              //         preItemName = !dl[j][outNameKey] ? '' : dl[j][outNameKey]['data_value'];
              //         if (preItemName === '' || currItemName === '') continue;
              //         var isSame = doc.compareSection(dl[j]['DATE_LIST'].data_value, dl[j]['TIME_LIST'].data_value, dl[i]['DATE_LIST'].data_value, dl[i]['TIME_LIST'].data_value, breakTime);
              //         if (isSame === 'tooLarge') break;
              //         // 类目相同 且 在同一个时间区间时，出量分类统计
              //         if (preItemName === currItemName && x === 'outTake' && isSame === 'add') {
              //             totalValue += Number(dl[j][x]['data_value']);
              //         }
              //     }
              //     totalValue += addValue;
              //
              //     // 如果beakTime以前有多条
              //     if (reqType === 'day' && currentDay <= breakDay1 && currItemName !== '') {
              //         var eIndex = $.inArray(currItemName, that.preOutName);
              //         if (eIndex == -1) {
              //             that.preOutName.push(currItemName);
              //             that.prevDayData['outList'].push([
              //                 currItemName,
              //                 td.prev().data('text'),
              //                 currValue
              //             ]);
              //         } else {
              //             totalValue = that.prevDayData['outList'][eIndex][2] = currValue + Number(that.prevDayData['outList'][eIndex][2]);
              //         }
              //     }
              // } else {
              //     totalValue = addValue + Number(prevInValue);
              // }

              td.attr({
                  "data-name": currItemName,
                  "data-value": currValue,
                  "data-text": td.prev().data('text'),
                  "data-totalValue": totalValue
              });
              if (!dl[i]['inTake']) {
                  td.siblings('[itemname="inTake"]').attr({
                      "data-totalValue": Number(prevInValue)
                  })
              }
              // if (currValue === "") {
              //     td.children().html("<div></div>");
              // }
              // else {
              //     td.children().html("<div class='sada" + _bold + "'>" + currValue + "/" + totalValue + "</div>");
              // }

              td.children().html("<div>" + view_value + "</div>");

              // if (reqType === 'day') {


              // 如果当前记录时间小于7点, 没有下一条记录
              // 出现7点有多条记录时,插入.
              // ---------
              // 0:00 ~ 7:00
              // ---------
              // 0:00 ~ 7:00  <----
              // ---------
              if (currentDay <= breakDay1 && nextDay === 0) {
                  if (x === 'outTake') {
                      if (currentDateStatistic && currentDateStatistic.morning) {
                          $(currentDateStatistic.morning).insertAfter(tr);
                      }

                  }
                  if (!dl[i]['outTake']) {
                      if (currentDateStatistic && currentDateStatistic.morning) {
                          $(currentDateStatistic.morning).insertAfter(tr);
                      }
                  }
              }
              // 如果当前记录时间大于7点, 前一条记录时间小于等于7点
              // 出现7点有多条记录时,插入.
              // ---------
              // 0:00 ~ 7:00
              // ---------
              // 0:00 ~ 7:00
              // ---------
              // 7:01 +   <----
              if (currentDay > breakDay1 && prevDay <= breakDay1 && i > 1) {
                  if (x === 'outTake') {
                      if (currentDateStatistic && currentDateStatistic.morning) {
                          $(currentDateStatistic.morning).insertBefore(tr);
                      }
                  }
              }


              // 结束时间在下午breakTimeAfternoon之后
              // ---------
              // 7:01 ~ 15:00
              // ---------
              // 7:01 ~ 15:00
              // ---------
              // 15:01 +   <----
              if (currentDay > breakDay2 && prevDay <= breakDay2 && x === 'inTake') {
                  if (currentDateStatistic && currentDateStatistic.afternoon) {
                      $(currentDateStatistic.afternoon).insertBefore(tr);
                  }
              }

              // 结束时间从下午breakTimeAfternoon之前开始
              // ---------
              // 7:01 ~ 15:00
              // ---------
              // 7:01 ~ 15:00     <----
              // ---------
              // TODO inTake 修改 建议增加outTake为空的key
              if (reqType === 'day') {
                  if (currentDay <= breakDay2 && nextDay === 0 && currentDay > breakDay1 /* && x === 'outTake'*/) {
                      // calculateTotal();
                      var currInValue = !tr.find('[itemname="inTake"]').data('totalvalue') || tr.find('[itemname="inTake"]').data('totalvalue') === '' ? 0 : tr.find('[itemname="inTake"]').data('totalvalue');
                      if (!dl[i]['outTake']) {
                          if (currentDateStatistic && currentDateStatistic.afternoon) {
                              $(currentDateStatistic.afternoon).insertAfter(tr);
                          }
                      }

                      if (x === 'outTake') {
                          if (currentDateStatistic && currentDateStatistic.afternoon) {
                              $(currentDateStatistic.afternoon).insertAfter(tr);
                          }
                      }
                  }
              } else {
                  var nextMidnight = new Date(dl[i].DATE_LIST.data_value + ' 00:00').getTime() + 3600 * 24 * 1000;
                  if (currentDay <= breakDay2 && currentDay > breakDay1 && nextDay >= nextMidnight /* && x === 'outTake'*/) {
                      // calculateTotal();
                      var currInValue = !tr.find('[itemname="inTake"]').data('totalvalue') || tr.find('[itemname="inTake"]').data('totalvalue') === '' ? 0 : tr.find('[itemname="inTake"]').data('totalvalue');
                      // if (str !== '' || that.inTotal !== 0) {
                      if (!dl[i]['outTake']) {
                          if (currentDateStatistic && currentDateStatistic.afternoon) {
                              $(currentDateStatistic.afternoon).insertAfter(tr);
                          }

                      }

                      if (x === 'outTake') {
                          if (currentDateStatistic && currentDateStatistic.afternoon) {
                              $(currentDateStatistic.afternoon).insertAfter(tr);
                          }
                      }
                      // }
                  }
              }
              // }
          } else if (section === "break") {

              //查找前一条是否是07:00是则加粗
              // if (dl[i - 1].TIME_LIST.data_value === breakTime) {
              //     var prevTr = tr.prev(); //前一行
              //     var __td = prevTr.find("td[itemname='" + itemName + "']"); //前一行当前列td
              //     __td.children().children().addClass("b");
              // }

              currValue = (dl[i][x].data_value === "" || dl[i][x].data_value === null) ? "" : Number(dl[i][x].data_value);
              td.attr({
                  "data-name": currItemName,
                  "data-value": currValue,
                  "data-text": td.prev().data('text'),
                  "data-totalValue": currValue
              });
              valueText = !currValue && currValue != 0 ? '' : currValue + "/" + currValue;
              // td.children().html("<div>" + valueText + "</div>");

              td.children().html("<div>" + view_value + "</div>");

              //if (reqType === 'day') {
              // 如果当前记录时间大于7点, 前一条记录时间小于等于7点
              // 出现7点有多条记录时,插入.
              // ---------
              // 0:00 ~ 7:00
              // ---------
              // 0:00 ~ 7:00
              // ---------
              // 7:01 +   <----

              /*if (reqType !== 'day') {
               if (currentDay > breakDay1 && prevDay <= breakDay1 && i > 1) {
               if (x === 'outTake') {
               if (currentDateStatistic && currentDateStatistic.morning) {
               $(currentDateStatistic.morning).insertBefore(tr);
               }

               if (nextDay > breakDay2) {
               if (currentDateStatistic && currentDateStatistic.afternoon) {
               $(currentDateStatistic.afternoon).insertAfter(tr);
               }
               }
               // }
               }

               if (!dl[i]['outTake']) {
               if (currentDateStatistic && currentDateStatistic.morning) {
               $(currentDateStatistic.morning).insertBefore(tr);
               }

               if (nextDay > breakDay2) {
               if (currentDateStatistic && currentDateStatistic.afternoon) {
               $(currentDateStatistic.afternoon).insertAfter(tr);
               }
               }
               // }
               }
               }
               } else {*/
              if (currentDay > breakDay1 && prevDay <= breakDay1 && i > 1) {
                  if (x === 'outTake') {
                      if (currentDateStatistic && currentDateStatistic.morning) {
                          $(currentDateStatistic.morning).insertBefore(tr);
                      }

                  }
                  if (!dl[i]['outTake']) {
                      if (currentDateStatistic && currentDateStatistic.morning) {
                          $(currentDateStatistic.morning).insertBefore(tr);
                      }
                  }
              }
              //}

              // 结束时间在下午breakTimeAfternoon之后
              // ---------
              // 7:01 ~ 15:00
              // ---------
              // 7:01 ~ 15:00
              // ---------
              // 15:01 +   <----
              // TODO inTake 修改 建议增加outTake为空的key
              var currInValue = tr.find('[itemname="inTake"]').data('totalvalue');

              if (currentDay > breakDay2 && prevDay <= breakDay2 && prevDay > breakDay1 /*&& x === 'inTake'*/) {
                  if (!dl[i]['outTake']) {
                      if (currentDateStatistic && currentDateStatistic.afternoon) {
                          $(currentDateStatistic.afternoon).insertBefore(tr);
                      }
                  }

                  if (x === 'outTake') {
                      if (currentDateStatistic && currentDateStatistic.afternoon) {
                          $(currentDateStatistic.afternoon).insertBefore(tr);
                      }
                  }
              }

              // 结束时间从下午breakTimeAfternoon之前开始
              // ---------
              // 7:01 ~ 15:00
              // ---------
              // 7:01 ~ 15:00     <----
              // ---------
              // TODO inTake 修改 建议增加outTake为空的key
              if (currentDay <= breakDay2 && nextDay === 0 && currentDay > breakDay1) {
                  currInValue = tr.find('[itemname="inTake"]').data('totalvalue') === '' ? 0 : tr.find('[itemname="inTake"]').data('totalvalue');
                  if (!dl[i]['outTake'] || x === 'outTake') {
                      if (currentDateStatistic && currentDateStatistic.afternoon) {
                          $(currentDateStatistic.afternoon).insertAfter(tr);
                      }
                  }
              }
              //}
          } else {
              td.attr({
                  "data-value": dl[i][x].data_value,
                  "data-text": dl[i][x].data_value
              });
              // td.children().html("<div>" + currValue + "</div>");
              td.children().html("<div>" + view_value + "</div>");
          }
      }
};

/*
 初始化事件
 */
doc.initEvent = function () {
    this.initDocToolEvent();
}
/*
 初始化工具栏
 */
doc.initDocToolEvent = function () {
    var that = this;
    // $("#docStatusBtn").unbind().bind("click",function(){
    //  var docTool = $("#docTool");
    //  var left = "-30px";
    //  var right  = parseInt(docTool.css("right")) < 0 ? 0 : "-152px";
    //  var arrowText = "<";
    //  if(right===0){
    //      left = 0;
    //      arrowText = ">"
    //  }
    //  $(this).animate({
    //      "left":left
    //  },"fast",function(){
    //      $(this).html(arrowText);
    //      $("#docTool").animate({
    //          "right":right
    //      },"fast");
    //  });
    // });

    // 护理记录单添加智能打印  2016-07-04 by gary
    if (that.loadDataParams.report_type !== '1' && that.loadDataParams.template_id.indexOf('print') == -1) {
        $('#smartPrint').hide();
    }

    $("#print").unbind().click(function () {

        //that.getPrintNum();

        //return;

        if (that.isTableType) {

            // 当进入打印时 未保存的数据将会被清空  （或改成直接保存）
            if (that.isEditing) {
                that.isEditing = false;
                that.endEdit();
                $("#dataTable tbody tr:last-child").remove();
            }

            if (that.isForPrint) {
                that.splitPage();
            } else {
                that.printTable();
            }
        } else {
            //that.saveData();
            that.printPage();
        }
    });
    $("#addNewLine").unbind().bind('click', function (e) {
        if (!that.permission) {
          $.messager.alert("提示", "当前文书无法修改");
          return;
        }
        // 判断是否是键盘触发 键盘触发时X,Y为0;
        if (e.pageX <= 0) {
            return;
        }
        $(this).trigger("focusout");

        //设置是否正在编辑状态默认为false;为true则提示先保存并跳出
        if (that.editInfo.target != null) {
            $.messager.alert("提示", "请先<b style='color:#f00;'>保存</b>录入的数据");
            return;
        }

        // 当保存完成后将会设置为false
        // that.isEdit.editStatus = true;

        var table = $(".editor:last-child").find("#dataTable");
        if (that.page.currenPageIndex != that.page.count) {
            var select = $("#pagination").find(".p-mid select");
            select.val(that.page.count);
            select.trigger("change");
            that.addNewLine(table, {
                "edit": true,
                "auto": false
            });
        } else {
            that.addNewLine(table, {
                "edit": true,
                "auto": false
            });
        }

        //收起工具栏
        $("#docStatusBtn").trigger("click");

    });
    $("#showPrintTool").unbind().bind("click", function () {
        $("#printTool").show().dialog({
            title: '打印项控制',
            width: 973,
            height: 350,
            closed: false,
            modal: true,
            buttons: [{
                text: '保存',
                handler: function () {
                    $("#printTool").dialog("close");
                }
            }, {
                text: '关闭',
                handler: function () {
                    $("#printTool").dialog("close");
                }
            }]

        });
    });

    // 全选
    $('#check-all').off().on('click', function (e) {
        var checkBoxs = $('#print-row__table').find('input[type="checkbox"]'),
            thisIsCheck = this.checked;
        $.each(checkBoxs, function (i, v) {
            v.checked = thisIsCheck;
        });
    });

    // 编辑打印行
    $("#editPrintRow").unbind().bind("click", function () {

        $("#print-row-wrapper").show().dialog({
            title: '选择打印行',
            width: $('#editBox').parent().width() - 100,
            height: $('#editBox').parent().height() - 180,
            closed: false,
            modal: true,
            buttons: [{
                text: '保存',
                handler: function () {
                    $("#print-row-wrapper").dialog("close");
                    var checkedInputs = $('#print-row__table input:not(:checked)');
                    that.printIndex = [];
                    // 缓存已选择行索引
                    $.each(checkedInputs, function (i, v) {
                        var index = $(v).data('index');
                        that.printIndex.push(index);
                    });

                    log(that.printIndex);
                }
            }, {
                text: '取消',
                handler: function () {
                    $("#print-row-wrapper").dialog("close");
                }
            }]

        });
    });

    // 设置起始页码
    $('#set-start-btn').off().on('click', function (e) {
        $("#set-page-start").dialog("open");
    });

    $('#smart-print-dialog').dialog({
        title: '智能打印设置',
        width: 400,
        height: 300,
        modal: true,
        closed: true,
        buttons: [{
            text: '打印',
            handler: doc.getPrintData
        }, {
            text: '取消',
            handler: function () {
                $('#smart-print-dialog').dialog("close");
            }
        }]
    });

    // 护理记录单审核
    $('#verify').off().on('click', function () {
      var approveData = {
        approve_person: doc.nurse.id + "-" + doc.nurse.name,
        approve_time: new Date().format('yyyy-MM-dd hh:mm:ss'),
        list: doc.Data.data('data_list').map(function (listItem) {
          return {
            record_id: listItem.record_id
          }
        })
      }
      doc.approve(approveData);
    });

    $('#printStartDate, #printEndDate').datebox({
        onChange: function (newValue, oldValue) {
            if (newValue !== '') {
                $('#smart-start-num, #smart-end-num').val('');
            }
        }
    });

    $('#smart-start-num, #smart-end-num').on('keyup', function () {
        if ($(this).val() !== '') {
            $('#printStartDate, #printEndDate').datebox('setValue', '');
        }
    });

    $('#smartPrint').off().on('click', function (e) {
        $("#smart-print-dialog").dialog("open");

    });

    $(document).unbind().bind('keypress', function (e) {

        var index = that.editInfo.tdIndex;
        if (index === null || typeof index === 'undefined') {
            return;
        }
        var params = that.detail.cell[index].params,
            item_name = params.item_name,
            data_type = params.data_type;
        // 判断是否是trigger触发
        if (e.isTrigger) {
            // 若为日期或者时间则设置keyCode 让下面的判断生效
            if (item_name === "DATE_LIST" || item_name === "TIME_LIST") {
                e.keyCode = 13;
            }
        }
        if (e.keyCode === 13) {

            var tr = that.editInfo.trIndex,
                td = that.editInfo.tdIndex;
            if (that.editInfo.target != null) {
                // if(data_type === "SIGNATURE"){
                //  that.endEditWidget();
                // }
                if (parseInt(tr) >= 0 && parseInt(td) >= 0 && parseInt(td) <= $("#dataTable").find("tr:eq(" + tr + ")").find("td").length - 1) {
                    that.enterEditStatus($("#dataTable").find("tr:eq(" + tr + ")").find("td:eq(" + (td + 1) + ")"), that.detail.cell[td + 1]);
                }
            }
        }
    });
    $("#save").unbind().bind('click', function () {
    if (!that.permission) {
      $.messager.alert("提示", "当前文书无法修改");
      return;
    }
        that.saveData();
    });
    $("#approveBox").delegate(".approve-btn", "click", function () {
        that.approveRecord($(this));
    });
};

doc.getPrintData = function () {
    var $btn = $(this);
    var endPage = $('#smart-end-num').val();
    var startPage = $('#smart-start-num').val();
    var numType = $('[name="printType"]:checked').val();
    var startDate = $('#printStartDate').combo('getValue');
    var endDate = $('#printEndDate').combo('getValue');

    var params = {
        patID: doc.loadDataParams.patientId,
        templateID: doc.loadDataParams.template_id,
        isHistoryDoc: doc.loadDataParams.isHistoryDoc,
        numType: numType,
        startDate: startDate,
        endDate: endDate,
        startPage: startPage,
        endPage: endPage
    };

    if ($btn.attr('disabled')) {
        $.messager.alert('提示', '正在加载数据，请不要重复点击打印！');
        return;
    }

    $btn.attr('disabled', 'disabled');

    if (parseInt(endPage) < parseInt(startPage)) {
        $.messager.alert('提示', '结束页码必须大于或等于起始页码!');
    }

    $.get(ay.contextPath + '/nur/doc/loadPrintData', params).done(function (response) {
        $btn.removeAttr('disabled');
        if (response.rslt == '0') {
            doc.splitPageFromData(response.data.printData);
        }
    }).fail(function (err) {
        $btn.removeAttr('disabled');
    });
};

doc.getPrintNum = function (templateId) {
    var printNumPromise = $.get(ay.contextPath + '/nur/doc/getReportPrintNum', {
        patID: doc.loadDataParams.patientId,
        templateID: templateId
    }).done(function (res) {
        if (res.rslt === '0') {

        }
    }).fail(function (err) {

    });

    return printNumPromise;
};

doc.setPrintNum = function (num) {
    var printNumPromise = $.post(ay.contextPath + '/nur/doc/setReportPrintNum', {
        patID: doc.loadDataParams.patientId,
        templateID: doc.loadDataParams.template_id.replace('print_', ''),
        printNum: num,
        createPerson: this.nurse.id + "-" + this.nurse.name
    }).done(function (res) {
        if (res.rslt !== '0') {

        }
    }).fail(function (err) {

    })
};

doc.splitPageFromData = function (data) {
    var tableHeaderCopy = $('.band[name="cloumn-header"]').clone();
    var bandHeaderCopy = $('.band[name="page-header"]').clone();
    tableHeaderCopy.css({
        'top': 0
    });
    var printWindow = $('#printFrame')[0].contentWindow,
        printBody = $(printWindow.document.body),
        bandTrs = $('#dataTable tbody').find('tr').toArray(),
        bandTrsLen = bandTrs.length,
        bandHeader = bandHeaderCopy[0].outerHTML.replace('headFixed1', ''),
        tableHeader = tableHeaderCopy[0].outerHTML.replace('headFixed2', ''),
        printPageArr = [],
        singlePageArr = ['<div class="band band-print" name="detail"><table class="page-dataTable"><tbody>'],
        pageHeight = 0,
        that = this,
        pageStart = that.pageStart,
        tableHeight = 670 - $('.band[name="page-header"]').eq(0).outerHeight() - $('.band[name="cloumn-header"]').eq(0).outerHeight() - 24;


    if (!data) return;

    $.each(data, function (i, v) {
        var vLen = v.length;
        var trHeight = v[vLen - 2],
            pageNum = v[vLen - 1],
            trHtml = that.generateTr(v).join('').replace(/(true)/g, ''),
            singlePageStr;

        if (!printPageArr[pageNum]) {
            printPageArr[pageNum] = [];
        }

        printPageArr[pageNum].push(trHtml);
    });

    // 给每页添加头部
    printPageArr = printPageArr.map(function (printItem, i) {
        return [
            '<div class="page-count">',
            bandHeader,
            tableHeader,
            '<div class="band band-print" name="detail"><table class="page-dataTable"><tbody>',
            printItem.join(''),
            '</tbody></table></div>',
            '<div class="page-num">第 ' + Number(i + 1) + ' 页</div>' + '</div>'].join('');
    });

    printBody.addClass("printTable");
    printWindow.document.head.innerHTML =
        '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/normalize.css">' +
        '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/docMain.css">' +
        '<style>@media print {@page {size: landscape;margin-left:20mm;#editor1{page-break-before:always;}}}</style>';

    printBody.html(printPageArr.join(''));

    //增加列数据过滤方法，控制不需要的列在打印时不显示
    var filterArr = that.filterRowData();
    var _table = printBody.find(".page-count .page-dataTable");
    for (var fdx = 0; fdx < filterArr.length; fdx++) {
        if (filterArr[fdx] === 0) {
            _table.each(function () {
                $(this).find("tr").each(function () {
                    $(this).find("td:eq(" + fdx + ")").html("");
                });
            })
        }
    }

    setTimeout(function () {
        printWindow.print();
    }, 100);
};

doc.splitPage = function () {
    var tableHeaderCopy = $('.band[name="cloumn-header"]').clone();
    var bandHeaderCopy = $('.band[name="page-header"]').clone();
    tableHeaderCopy.css({
        'top': 0
    });
    var printWindow = $('#printFrame')[0].contentWindow,
        printBody = $(printWindow.document.body),
        bandTrs = $('#dataTable tbody').find('tr').toArray(),
        bandTrsLen = bandTrs.length,
        bandHeader = bandHeaderCopy[0].outerHTML.replace('headFixed1', ''),
        tableHeader = tableHeaderCopy[0].outerHTML.replace('headFixed2', ''),
        printPageArr = [],
        singlePageArr = ['<div class="band band-print" name="detail"><table class="page-dataTable"><tbody>'],
        pageHeight = 0,
        bedArr = [],
        deptArr = [],
        sinBed = [],
        sinDept = [],
        printSelect,
        that = this,
        pageStart = that.pageStart,
        tableHeight = 670 - $('.band[name="page-header"]').eq(0).outerHeight() - $('.band[name="cloumn-header"]').eq(0).outerHeight() - 15;

    printBody.html('');

    function transformBed() {
        // 显示转床号
        var headBed = $(bandHeader).find('[data-name="PTNT_BED"]').val(),
            headDept = $(bandHeader).find('[data-name="DE08_10_054_00"]').val(),
            newBedArr = [],
            newDeptArr = [];

        bedArr.push(sinBed);
        deptArr.push(sinDept);

        $.each(sinBed, function (i, c) {
            if ($.inArray(c, newBedArr) < 0 && c !== '') {
                newBedArr.push(c);
            }
        });

        $.each(sinDept, function (i, c) {
            if ($.inArray(c, newDeptArr) < 0 && c !== '') {
                newDeptArr.push(c);
            }
        });

        if (newBedArr.length > 0) {
            //$(bandHeader).find('[data-name="PTNT_BED"]').attr('value', newBedArr.join('→')).val(newBedArr.join('→'));
            bandHeader = bandHeader.replace('value="' + headBed + '"', 'value="' + newBedArr.join('→') + '"');
        }

        if (newDeptArr.length > 0) {
            bandHeader = bandHeader.replace('value="' + headDept + '"', 'value="' + newDeptArr.join('→') + '"');
        }
    }

    if (typeof that.printIndex !== 'undefined' && that.printIndex.length === bandTrsLen) {
        $.messager.alert('提示', '您没有选择打印行, 没有数据需要打印!');
        return;
    }

    $.each(bandTrs, function (i, v) {
        if (typeof that.printIndex !== 'undefined' && $.inArray(i, that.printIndex) !== -1) {
            if (i === bandTrsLen - 1) {
                singlePageArr.push('</tbody></table></div>');
                singlePageStr = singlePageArr.join('');
                printPageArr.push('<div class="page-count">' + bandHeader + tableHeader + singlePageStr + '<div class="page-num">第 ' + Number(printPageArr.length + pageStart) + ' 页</div>' + '</div>');
                pageHeight = 0;
                singlePageStr = '';
                singlePageArr = ['<div class="band band-print" name="detail"><table class="page-dataTable"><tbody>'];
            }
            return true;
        }

        var trHeight = $(v).outerHeight(),
            trHtml = v.outerHTML,
            singlePageStr;

        pageHeight += trHeight;

        if (pageHeight <= tableHeight) {
            singlePageArr.push(trHtml);

            // 缓存病区和床号
            sinBed.push(($(v).data('code') === null ? '' : $(v).data('code').toString()));
            sinDept.push(($(v).data('dept') === null ? '' : $(v).data('dept').toString()));

            if (i === bandTrsLen - 1) {

                transformBed();

                bedArr.push(sinBed);
                deptArr.push(sinDept);
                singlePageArr.push('</tbody></table></div>');
                singlePageStr = singlePageArr.join('');
                printPageArr.push('<div class="page-count">' + bandHeader + tableHeader + singlePageStr + '<div class="page-num">第 ' + Number(printPageArr.length + pageStart) + ' 页</div>' + '</div>');
                pageHeight = 0;
                singlePageStr = '';

                sinBed = [];
                sinDept = [];

                singlePageArr = ['<div class="band band-print" name="detail"><table class="page-dataTable"><tbody>'];
            }
        } else {

            transformBed();
            sinBed = [];
            sinDept = [];
            sinBed.push(($(v).data('code') === null ? '' : $(v).data('code').toString()));
            sinDept.push(($(v).data('dept') === null ? '' : $(v).data('dept').toString()));

            singlePageArr.push('</tbody></table></div>');
            singlePageStr = singlePageArr.join('');
            printPageArr.push('<div class="page-count">' + bandHeader + tableHeader + singlePageStr + '<div class="page-num">第 ' + Number(printPageArr.length + pageStart) + ' 页</div>' + '</div>');
            pageHeight = trHeight;
            singlePageStr = '';
            singlePageArr = ['<div class="band band-print" name="detail"><table class="page-dataTable"><tbody>'];
            singlePageArr.push(trHtml);

            if (i === bandTrsLen - 1) {

                transformBed();

                bedArr.push(sinBed);
                deptArr.push(sinDept);
                singlePageArr.push('</tbody></table></div>');
                singlePageStr = singlePageArr.join('');
                printPageArr.push('<div class="page-count">' + bandHeader + tableHeader + singlePageStr + '<div class="page-num">第 ' + Number(printPageArr.length + pageStart) + ' 页</div>' + '</div>');
                pageHeight = 0;
                singlePageStr = '';
                singlePageArr = ['<div class="band band-print" name="detail"><table class="page-dataTable"><tbody>'];

                sinBed = [];
                sinDept = [];
            }
        }
    });
    log(bedArr);
    // 缓存页码
    //sessionStorage.setItem('docPageNum', printPageArr.length + pageStart);
    debugger;
    that.setPrintNum(printPageArr.length + pageStart);
    printBody.addClass("printTable");
    printWindow.document.head.innerHTML =
        '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/normalize.css">' +
        '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/docMain.css">' +
        '<style>@media print {@page {size: landscape;margin-left:20mm;#editor1{page-break-before:always;}}}</style>';
    /*$.each(printPageArr, function (i, v) {
     //printBody.append(v);
     $(v).appendTo(printBody);
     });*/

    printBody.html(printPageArr.join(''));

    //增加列数据过滤方法，控制不需要的列在打印时不显示
    var filterArr = that.filterRowData();
    var _table = printBody.find(".page-count .page-dataTable");
    for (var fdx = 0; fdx < filterArr.length; fdx++) {
        if (filterArr[fdx] === 0) {
            _table.each(function () {
                $(this).find("tr").each(function () {
                    $(this).find("td:eq(" + fdx + ")").html("");
                });
            })
        }
    }

    setTimeout(function () {
        printWindow.print();
    }, 100);
};

/*
 初始化模板
 params {obj} url参数
 */
doc.initTemplate = function (url) {
    var tId = url.template_id,
        recordId = url.recordId,
        pId = url.patientId,
        type = url.type,
        params = null,
        reqUrl,
        show_type = url['show_type'];

    var paramObj = {
        inpatient_no: pId,
        template_id: tId,
        deptCode: ay.getLocalData('deptCode'),
        isHistoryDoc: url.isHistoryDoc,
        userCode: doc.nurse.id
    };

    //默认初始为表格类型
    this.isTableType = true;

    //非表格
    if (show_type === "fixed") {
        this.isTableType = false;
        params = {
            type: 1,
            rId: recordId,
            tId: tId,
            pId: pId
        };
    } else if (show_type === "list") {
        params = {
            type: 2,
            tId: tId,
            pId: pId
        };
    } else if (type === "day") {
        var day = url.day;
        params = {
            type: "day",
            tId: tId,
            day: day,
            pId: pId,
            report_type: url.report_type
        };
    } else if (type === "time") {
        var time = url.time;
        var day = url.day;
        params = {
            type: "time",
            day: day,
            time: time,
            tId: tId,
            rId: recordId,
            pId: pId
        };
    }

    // 专门打印接口
    if (tId.indexOf('print') >= 0) {
        paramObj.isprint = 'Y';
        reqUrl = "./list?isprint=Y&inpatient_no=" + pId + "&template_id=" + tId + '&deptCode=' + ay.getLocalData('deptCode') + '&isHistoryDoc=' + doc.loadDataParams.isHistoryDoc;
    } else {
        reqUrl = "./list?inpatient_no=" + pId + "&template_id=" + tId;
    }
    this.getTempConfig('./list', this.parseTemplate, paramObj);
};

doc.generateTbody = function (dataArr) {
    var that = this;

    return dataArr.map(function (c, i, a) {
        return that.generateTr(c).join('');
    }).join('');
};

doc.generateTr = function (tdDataArr) {
    var that = this;
    var hasDynaHeader = that.Data.data('dyna_list').length !== 0,
        tableCell = that.detail.cell,
        hasWidth = tableCell.length !== 0;
    var arr;

    var widths = hasWidth ?
        tableCell.map(function (c, i, a) {
            return Number(c['params']['width']);
        }) : [69, 53, 28, 27, 27, 47, 27, 27, 33, 69, 39, 44, 39, 25, 31, 29, 34, 35, 31, 40, 29, 29, 29, 53, 51];

    if (!tdDataArr) return;

    arr = tdDataArr.map(function (currentValue, index, array) {
        var current_value = currentValue == 'gary' || currentValue == 'empty' ? '' : currentValue;

        if (index > widths.length - 1 || (tdDataArr.length === 3 && index > 0)) return true;
        if (index === 0) {
            if (tdDataArr.length === 3 || tdDataArr.length === 4) {
                return ['<tr class="ignore" data-code="' + tdDataArr[tdDataArr.length - 2] + '" data-dept="' + tdDataArr[tdDataArr.length - 1] + '"><td colspan="' + widths.length + '">',
                    current_value,
                    '</td></tr>'
                ].join('');
            } else if (tdDataArr.length > tableCell.length) {
                return ['<tr data-code="' + tdDataArr[tdDataArr.length - 2] + '" data-dept="' + tdDataArr[tdDataArr.length - 1] + '"><td style="width: ' + widths[index] + 'px;">',
                    '<div style="width: ' + widths[index] + 'px;">',
                    that.replaceChineseChar(current_value),
                    '</div>',
                    '</td>'
                ].join('');
            } else {
                return ['<tr><td style="width: ' + widths[index] + 'px;">',
                    '<div style="width: ' + widths[index] + 'px;">',
                    that.replaceChineseChar(current_value),
                    '</div>',
                    '</td>'
                ].join('');
            }
        }

        if (tdDataArr.length > 5) {
            if (index === widths.length - 1) {
                return ['<td style="width: ' + widths[index] + 'px;">',
                    '<div style="width: ' + widths[index] + 'px;">',
                    that.replaceChineseChar(current_value),
                    '</div>',
                    '</td></tr>'
                ].join('');
            }
        } else {
            return '';
        }

        return ['<td style="width: ' + widths[index] + 'px;">',
            '<div style="width: ' + widths[index] + 'px;">',
            that.replaceChineseChar(current_value),
            '</div>',
            '</td>'
        ].join('');
    });

    return arr;
};

doc.onlyForPrint = function () {
    var that = this,
        printArr = that.Data.data('data_print'),
        htmlStr = that.generateTbody(printArr);

    //log(htmlStr);

    $('#dataTable tbody').html(htmlStr);
    $('[data-name="' + that.diagnose['id'] + '"]').val(that.diagnose['value']).attr('value', that.diagnose['value']);

    // 渲染选择打印行
    var trs = $('#dataTable').find('tr').toArray(),
        newTrs = trs.map(function (current, index) {
            var tds = $(current).find('td').toArray(),
                newTds = tds.map(function (curr, idx) {
                    if (idx === 0) {
                        return '<td style="width: 21px;"> <input data-index="' + index + '" id="row' + index + '" type="checkbox" checked="checked"/> </td>' + curr.outerHTML;
                    } else {
                        return curr.outerHTML;
                    }
                });
            return ['<tr>', newTds.join(''), '</tr>'].join('');
        }),
        newTable = ['<tbody>', newTrs.join(''), '</tbody>'].join('');

    $('#print-row__table').html(newTable);
    $('#print-row__table td').each(function (i, tdItem) {
        var contentDiv = $(tdItem).children('div');

        if (contentDiv.length > 0 && contentDiv.text() === '') {
            contentDiv.width(4);
            $(tdItem).width(4);
        }
    });
    $('#check-all')[0].checked = true;
};

/*
 解析模板
 @params {obj} 模板配置数据
 */
doc.parseTemplate = function (config) {

    if (!this.getTempConfigStatus) {
        console.log("获取模板配置数据错误！");
        return;
    }

    //非表格类型，隐藏新增打印按钮
    if (!this.isTableType) {
        $("#addNewLine").remove();
        $("#showPrintTool").remove();
    }

    //保存患者信息和反写数据
    this.inpatient_info = config.inpatient_info
    this.ref_list = config.ref_list

    this.parseBandArea(config);

    // 模板加载完成后 使某些未生效的easyui控件生效
    $.parser.parse();
    this.loadTempStatus = true;
}

/*
 解析非表格类型模板
 @params {obj} 模板配置数据
 */
doc.parseUnTableTemp = function (config) {

}

/*
 解析表格类型模板
 @params {obj} 模板配置数据
 */
doc.parseTableTemp = function (config) {

}

/*
 解析band区域
 */
doc.parseBandArea = function (config) {
    var that = this;

    // 创建容器
    this.containter = this.createContainter(config);
    //this.fixDetailHeight = false;
    var containter = this.containter;

    //解析band一个band是一个区域（例如header,detail）
    var bands = config.bands;
    this.bandsConfig = bands;
    this.bands = [];
    this.outInColumn = {
        out: null,
        input: null
    };

    // 每个区域的标识 不为undefined则表示存在
    var pageTitleIndex, pageHeaderIndex, cloumnHeaderIndex, detailIndex, pageFooterIndex;

    for (i = 0; i < bands.length; i++) {
        this.bands[i] = $("<div class='band'></div>");
        containter.append(this.bands[i]);
        this.bands[i].attr('name', bands[i].bandName);
        this.bands[i].css({
            "height": bands[i].height + "px"
        });
        var items = bands[i].items;
        if (bands[i].bandName == 'title') {
            pageTitleIndex = i;
        } else if (bands[i].bandName == 'page-header') {
            pageHeaderIndex = i;
        } else if (bands[i].bandName == 'cloumn-header') {
            cloumnHeaderIndex = i;
            this.bands[i].css({
                "border-right": "1px solid #000"
            });
        } else if (bands[i].bandName == 'page-footer') {
            pageFooterIndex = i;
        } else if (bands[i].bandName == 'detail') {
            detailIndex = i;
            if (this.isTableType) {
                this.bands[i].css({
                    "height": "auto",
                    "border-left": "1px solid",
                    "border-right": "1px solid",
                    "border-collapse": " collapse",
                    "border-bottom": "1px solid #000",
                    "overflow": "hidden",
                    "overflow-y": "auto"
                });

                //修改detail容器宽度 适用于增加右表格线
                this.bands[i].width(this.bands[i].width());

                // 每个科室的出入量名是变化的,在这里取得出入量名字

                bands[i].items.forEach(function (tdDataItem, tdIndex) {
                    if (tdDataItem['item_name'].indexOf('out_name') > -1) {
                        that.outInColumn.out = tdDataItem['item_name'];
                    }

                    if (tdDataItem['item_name'].indexOf('input_name') > -1) {
                        that.outInColumn.input = tdDataItem['item_name'];
                    }
                });
            }
        } else {
            console.log("出现了其他情况！");
            //其他情况暂不做处理
            //this.parseItems(items,this.bands[i]);
        }
    }

    if (parseInt(cloumnHeaderIndex) >= 0) {
        var items = bands[cloumnHeaderIndex].items;
        this.parseTableHeader(items, this.bands[cloumnHeaderIndex], function () {
            //增加表格头渲染完成后的回调
            //处理第一个表格头元素内包含line元素时需处理多余的上边框线
            var line = $(".band[name='cloumn-header'] > div[data-type='line']");
            if (line.length > 0) {
                var lt3sText = $(".band[name='cloumn-header'] > div:lt(3)[data-type!='line']");
                lt3sText.each(function () {
                    if ($(this).css('top') != 0) {
                        $(this).css('border-top', 'none');
                    }
                    if (line.attr('data-dir') && $(this).css('text-align') == 'right') {
                        $(this).css('border-top', 'none');
                    }
                });
            }
        });
    }
    if (parseInt(pageTitleIndex) >= 0) {
        var items = bands[pageTitleIndex].items;
        this.parseItems(items, this.bands[pageTitleIndex]);
    }
    if (parseInt(pageHeaderIndex) >= 0) {
        var items = bands[pageHeaderIndex].items;
        this.parseItems(items, this.bands[pageHeaderIndex]);
        if (this.isTableType) {
            var hHtml = $("div[name='cloumn-header']")[0].outerHTML;
            $("#printToolHeader").html(hHtml);
        }
    }
    if (parseInt(pageFooterIndex) >= 0) {
        var items = bands[pageFooterIndex].items;
        this.parseItems(items, this.bands[pageFooterIndex]);
    }
    if (parseInt(detailIndex) >= 0) {
        var items = bands[detailIndex].items;
        if (this.isTableType) {
            //表格类型时计算detail高度
            //默认为655 加上15像素的分页高度  总计670px
            var editorHight = this.orientation === 'landscape' ? 655 : 985;
            var allband = $(".editor .band");
            //缓存pagefooter Y轴的值
            var footerY = 0;
            allband.each(function () {

                //band不为detail时计算剩余detail高度
                if ($(this).attr("name") !== "detail") {
                    editorHight = editorHight - $(this).height();
                }

                //计算footer距离顶部的y值
                if ($(this).attr("name") !== "page-footer" && $(this).attr("name") !== "detail") {
                    footerY = footerY + $(this).height();
                }
            });
            // editorHight += 125;
            // editorHight -= 15;
            $(".band[name='detail']").css("max-height", editorHight + "px");

            footerY += editorHight; //计算完成并未使用

            if (parseInt(pageFooterIndex) >= 0) {

            }
            this.fixDetailHeight = true;
            this.footerTop = this.orientation === 'landscape' ? footerY : footerY + 30;
            this.detailHeight = editorHight;
            this.bands[detailIndex].attr("data-height", this.detailHeight);
        }

        //判断是否是表格类型  是则解析detail  不是则当成非表格类型解析items
        if (this.isTableType) {
            this.parseDetail(items, this.bands[detailIndex], function () {
            });

            //读取第一行数据放入打印控制表格内
            this.setPrintToolRow(items);

        } else {
            this.parseItems(items, this.bands[detailIndex]);
        }
    }

    //填充第一次数据
    if (this.isTableType) {
        // 表格类型模板 将获取第一次的信息数据填充到模板中
        var inpatient_info = this.Data.data('list_table_header') || config.inpatient_info;
        var ref_list = config.ref_list;
        //for(var i=0;i<inpatient_info.length;i++){
        //$(".inputItem input[data-name='"+inpatient_info[i].key+"']").val(inpatient_info[i].value);
        //this.setInverseData(inpatient_info[i].key,inpatient_info[i].value);
        this.setInverseData(inpatient_info);
        if (ref_list) {
            this.setInverseData(ref_list, "record_");
        }
        //this.dataToView(null, false);
        //}
  }
  if (!this.permission) this.containter.find('input').attr('disabled', 'disabled');
}

/*
 填充数据
 @params {str} key
 @params {str} value
 */
doc.setInverseData = function (data, prev) {

    if (!data || data === null) {
        return;
    }

    var inputItem = null,
        prevText = prev ? prev : "",
        type = "";

    for (var i = 0; i < data.length; i++) {

        inputItem = $("div[itemname='" + data[i][prevText + "key"] + "']");
        type = inputItem.attr("data-type");
        //无此节点 退出
        if (!inputItem || inputItem.length === 0) {
            continue;
        }

        var children = inputItem.children(),
            tagName = children ? (children[0] ? children[0].tagName : "") : "";

        if (tagName === "DIV") {

        } else if (tagName === "SELECT") {
            children.val(data[i][prevText + "value"]);
        } else if (tagName === "INPUT") {
            if (type === "SYS_TIME") {
                children.datetimebox({
                    value: data[i][prevText + "value"]
                });

                inputItem.find('.combo-text').attr('value', data[i][prevText + "value"]);
            } else {
                if (type == 'OPT' && inputItem.length === 1) {
                    cInputItem = inputItem.eq(0).children();
                    //cInputItem.trigger('click');

                    if (cInputItem[0].tagName === 'SPAN') {
                        cInputItem = cInputItem.children();
                    }

                    inputItem.data('is-changed', true);
                    // TODO 动态表头的下拉可填
                    var options = cInputItem.data('options');

                    if (!options) {
                        if (cInputItem.attr('data-options')) {
                            options = JSON.parse(cInputItem.attr('data-options'));
                        }
                    }

                    if (options) {
                        var option = $.grep(options, function (n, i1) {
                            if (data[i]) {
                                if (n.code == data[i][prevText + "value"]) {
                                    //_index = i1;
                                    return true;
                                }
                            }

                        })[0];
                    }


                } else {
                    children.val(data[i][prevText + "value"]).attr('value', data[i][prevText + "value"]);
                }
            }
        } else {

        }
    }


}

/*
 解析每一个项目
 */
doc.parseItems = function (items, containter, offsetTop) {
    var that = this;
    var tempNode = null;
    var offsetT = offsetTop ? offsetTop : 0;

    //增加缓存itemList节点
    this.itemList = this.itemList || {};

    $.each(items, function (i, n) {
        tempNode = that.assembleNode(n, offsetT, i);
        log(i, n, tempNode);
        that.itemList[n.item_name] = {
            config: n,
            target: tempNode
        };
        if (tempNode === "break") {
            //创建新的page
            var newPageContainter = that.createContainter(that.tempConfig);
            //将page header中的部分提取到新页面中;
            var newPageDetail = that.initNewPage(newPageContainter);
            //解析新页面
            that.parseItemForBreak(i, items, newPageDetail);
            return false;
        } else {
            containter.append(tempNode);
        }
    });
}


/*
 组装节点 传入配置信息返回相应节点
 @params {obj} 配置信息
 @params {num} 距离顶部高度
 @return {$dom} 返回组装好的节点
 */
doc.assembleNode = function (params, offsetT, index) {
    var that = this,
        tempNode,
        node = null,
        required = false;
    if (params.type == 'break') {
        return "break";
    }
    if ((params['size'] === '24' || params['size'] === '20') && params['align'] === 'CENTER') {
        tempNode = $("<div class='noBlock' itemName='" + params.item_name + "'></div>");
    } else {
        tempNode = $("<div class='block' itemName='" + params.item_name + "'></div>");
    }
    tempNode.css({
        "height": params.height + "px",
        "line-height": params.height + "px",
        "left": (params.posX) + "px",
        "top": (params.posY - offsetT) + "px",
        "fontSize": params.size + "px",
        "fontWeight": params.isbold,
        "textAlign": params.align
    });

    if (params.margin == 'MIDDLE') {
        tempNode.css('lineHeight', params.height + "px");
    }

    if (params.type == 'line') {
        tempNode.attr('data-type', 'line');
        tempNode.css({
            "width": params.width + "px",
            "backgroundColor": '#000'
        });
    }

    if (params.type == 'Static') {
        tempNode.attr('data-type', 'sText');
        tempNode.html(params.item_name);
        tempNode.css({
            "width": params.width + "px"
        });
    }
    if (params.type == 'Param') {

        //默认值
        var default_value = params.default_value || undefined;
        var children = null;

        this.requiredList = this.requiredList || [];
        if (params.required === true) {
            this.requiredList.push(params.item_name);
        }
        //标识类型
        tempNode.attr('data-type', params.data_type);
        //录入控件的标识
        tempNode.addClass('inputItem');
        tempNode.css({
            "width": params.width + "px"
            //"text-align": params.
        });
        if (params.data_type === "SIGNATURE") {
            tempNode.attr("data-type", "SIGNATURE");
            children = $("<input type='text' disabled='disabled' style='border-bottom:none;' data-name='" + params.item_name + "'>");
            tempNode.append(children);
            if (params.item_name === "APPROVE_PERSON") {
                this.nurse.approveNurse = children;
            } else if (params.item_name === "CREATE_PERSON") {
                this.nurse.createNurse = children
            } else if (params.item_name === "CREATE_PERSON2") {
                this.nurse.createNurse2 = children
                children.css({
                    "border-bottom": "1px solid #000"
                }).removeAttr("disabled");
            } else {
                children = $('<a href="javascript:;" class="_btn _btn-tn signature-btn">签名</a>');
                tempNode.append(children);
                //var cInputItem = $("<input type='text' disabled='disabled' style='border-bottom:none;' data-name='" + params.item_name + "'>");
                //tempNode.append(cInputItem);
                tempNode.find('input').css('height', '0');

                children.on('click', function (e) {
                    var $this = $(this);
                    $.messager.confirm("提示", "确认签名吗？", function (r) {
                        if (r) {
                            $this.hide();
                            $this.prev()
                                .val(doc.nurse.name)
                                .attr('data-value', doc.nurse.id)
                                .attr('data-text', doc.nurse.name)
                                .attr('value', doc.nurse.name)
                                .height('100%');
                        } else {
                            console.log("取消审签！");
                            return;
                        }
                    });
                });
            }
        }
        if (params.data_type === 'STR' || params.data_type === "SCORE_TOTAL_ROW") {
            tempNode.attr("data-type", "STR");
            children = $("<input type='text' data-name='" + params.item_name + "'>");
            tempNode.append(children);
            if (params.if_underline && params.if_underline === "N") {
                children.css({
                    "border-bottom": "none"
                });
            }
            if (params.readonly_flag === "Y") {
                children.attr("disabled", "disabled").css({
                    "border-bottom": "none",
                    "text-indent": 0,
                    "font-size": "12px",
                    "line-height": "18px"
                });
            }
        }
        if (params.data_type === "DATE") {
            children = $("<input type='text' class='easyui-datebox' data-options=\"showSeconds:false,formatter:function(date){return new Date(date).format('yyyy-MM-dd');}\" data-name='" + params.item_name + "' style='height:18px;width:" + params.width + "px;'>");
            tempNode.html(children);
        }
        if (params.data_type == 'SYS_TIME') {
            children = $("<input type='text' class='easyui-datetimebox' data-options=\"showSeconds:false,formatter:function(date){/*var y = date.getFullYear();var m = date.getMonth()+1;var d = date.getDate();var h = date.getHours();var M = date.getMinutes();*/return new Date(date).format('yyyy-MM-dd hh:mm');}\" data-name='" + params.item_name + "' style='height:18px;width:" + params.width + "px;'>");
            tempNode.html(children);
        }
        if (params.data_type == 'BP') {
            children = $("<input type='text' data-name='" + params.item_name + "' >");
            tempNode.append(children);
            children.validatebox({
                validType: 'bp[' + params.min_value + "," + params.max_value + ",'" + params.item_name + "']",
                required: params.required
            });
        }
        if (params.data_type == 'NUM') {
            children = $("<input type='text' data-name='" + params.item_name + "' >");
            tempNode.append(children);
            children.validatebox({
                validType: 'number[' + params.min_value + "," + params.max_value + "," + params.precision + "]"
            });
            if (params.readonly_flag === "Y") {
                children.attr("disabled", "disabled").css({
                    "text-indent": 0
                });
            }
            //设置一个反写总分标识
            if ("PRESSURE_SORE_GRADE" === params.item_name) {
                this.soreGrade = true;
            }
        }
        if (params.data_type == 'SEL') {
            tempNode.css({
                'lineHeight': "18px"
            });
            var children = $("<div data-name=" + params.item_name + "></div>");
            var c_son_str = '',
                c_text_str = '';
            tempNode.append(children);
            if (params.checkbox.length > 0) {
                var checkbox = params.checkbox,
                    span = null,
                    hasChildNode = false;
                for (var cdx = 0; cdx < checkbox.length; cdx++) {

                    span = $("<span></span>");
                    var c_son_span = null,
                        c_text_span = null,
                        hasChildNode = false;

                    span.append("<label><input type='checkbox' id='" + ("" + params.item_name + checkbox[cdx].checkbox_code) + "' name='" + params.item_name + "' value='" + checkbox[cdx].checkbox_code + "'>" + checkbox[cdx].checkbox_name + "</label>");
                    if (checkbox[cdx].checkbox_son.length > 0) {
                        var c_son = checkbox[cdx].checkbox_son,
                            c_son_text = '';
                        c_son_span = $("<span class='childCheck'></span>");
                        for (var csdx = 0; csdx < c_son.length; csdx++) {
                            c_son_text += "<label><input type='checkbox' disabled='disabled' name='" + checkbox[cdx].checkbox_code + "' value='" + c_son[csdx].checkbox_code_son + "'><span>" + c_son[csdx].checkbox_name_son + "</span></label>";
                        }
                        c_son_span.html("(" + c_son_text + ")");
                        span.append(c_son_span);
                        hasChildNode = true;
                    }
                    if (checkbox[cdx].checkbox_text.length > 0) {
                        var c_text = checkbox[cdx].checkbox_text,
                            c_text_text = '';
                        c_text_span = $("<span class='childText'></span>");
                        for (var csdx = 0; csdx < c_text.length; csdx++) {
                            c_text_text += "<label><input style='width:" + (c_text[csdx].width ? c_text[csdx].width : 40) + "px;' type='text' disabled='disabled' name='" + c_text[csdx].checkbox_text_code + "'>" + (c_text[csdx].unit_text ? c_text[csdx].unit_text : '') + "</label>";
                        }
                        c_text_span.html(c_text_text);
                        span.append(c_text_span);
                        hasChildNode = true;
                    }
                    children.append(span);
                    if (hasChildNode) {
                        span.attr('data-hasChild', 'true');
                    }
                }
            }
            var cCheckbox = children.children().children('label').children();
            children.attr("checkIndex", "null");
            cCheckbox.bind('click', function () {
                var thisId = $(this).attr('id');

                cCheckbox.each(function () {
                    if ($(this).attr('id') !== thisId) {
                        $(this).removeAttr('checked');
                    }

                    if ($(this).parent().parent().attr('data-haschild')) {

                        //修复当子节点包含 多/单选控件和文本录入控件 结束控件编辑状态的错误
                        $(this).parent().parent().children("span").find("input").attr('disabled', 'disabled').removeAttr('checked');
                    }
                });
                //$(this).attr("checked", "checked");
                var index = $(this).parent().parent().data('index') /*.index()*/;
                if (parseInt(children.attr("checkIndex")) == index) {
                    $(this).removeAttr("checked");
                    children.attr("checkIndex", null);
                    return;
                }
                //if(!checkIndex) checkIndex = index;
                children.attr("checkIndex", index);

                if ($(this).parent().parent().attr('data-haschild')) {
                    if ($(this).attr('checked') == 'checked') {

                        //修复当子节点包含 多/单选控件和文本录入控件 打开控件编辑状态的错误
                        $(this).parent().parent().children("span").find("input").removeAttr('disabled');
                    }
                }
            });
        }
        if (params.data_type == 'MSEL') {
            tempNode.css({
                'lineHeight': "18px"
            });
            var children = $("<div data-name=" + params.item_name + "></div>");
            var c_son_str = '',
                c_text_str = '';
            tempNode.append(children);
            if (params.checkbox.length > 0) {

                var checkbox = params.checkbox,
                    span = null,
                    hasChildNode = false;

                for (var cdx = 0; cdx < checkbox.length; cdx++) {

                    span = $("<span></span>");
                    var c_son_span = null,
                        c_text_span = null;

                    span.append("<label><input type='checkbox' name='" + params.item_name + "' value='" + checkbox[cdx].checkbox_code + "'>" + checkbox[cdx].checkbox_name + "</label>");
                    if (checkbox[cdx].checkbox_son.length > 0) {
                        var c_son = checkbox[cdx].checkbox_son,
                            c_son_text = '';
                        c_son_span = $("<span class='childCheck'></span>");
                        for (var csdx = 0; csdx < c_son.length; csdx++) {
                            c_son_text += "<label><input type='checkbox' disabled='disabled' name='" + checkbox[cdx].checkbox_code + "' value='" + c_son[csdx].checkbox_code_son + "'><span>" + c_son[csdx].checkbox_name_son + "</span></label>";
                        }
                        c_son_span.html("(" + c_son_text + ")");
                        span.append(c_son_span);
                        hasChildNode = true;
                    }
                    if (checkbox[cdx].checkbox_text.length > 0) {
                        var c_text = checkbox[cdx].checkbox_text,
                            c_text_text = '';
                        c_text_span = $("<span class='childText'></span>");
                        for (var csdx = 0; csdx < c_text.length; csdx++) {
                            c_text_text += "<label>" + (c_text[csdx].text_name ? c_text[csdx].text_name : '') + "<input type='text' disabled='disabled'  name='" + c_text[csdx].checkbox_text_code + "' style='width:" + c_text[csdx].width + "px;'>" + (c_text[csdx].text_unit ? c_text[csdx].text_unit : '') + "</label>";
                        }
                        c_text_span.html(c_text_text + "");
                        span.append(c_text_span);
                        hasChildNode = true;
                    }
                    children.append(span);
                    if (hasChildNode) {
                        span.attr('data-hasChild', 'true');
                    }
                }
            }

            var cCheckbox = children.children().children('label').children();
            children.attr("checkIndex", "null");
            cCheckbox.bind('click', function () {
                if ($(this).parent().parent().attr('data-haschild')) {
                    if ($(this).attr('checked') == 'checked') {
                        //修复当子节点包含 多/单选控件和文本录入控件 打开控件编辑状态的错误
                        $(this).parent().parent().children("span").find("input").removeAttr('checked').removeAttr('disabled');
                    } else {
                        $(this).parent().parent().children("span").find("input").removeAttr('checked').attr('disabled', 'disabled');
                    }
                }
            });
            cCheckbox.delegate("input", "click", function () {
                if ($(this).attr("checked") === "checked") {
                    $(this).removeAttr("checked");
                }
            });
        }

        if (params.data_type == "MOPT") {
            this.unDetail[params.item_name] = params.options;
            children = $("<input type='text' data-name='" + params.item_name + "'>");
            tempNode.append(children);
            var options = params.options,
                moptArr = [];

            for (var i = 0; i < options.length; i++) {
                moptArr.push({});
                moptArr[i].code = options[i].code;
                moptArr[i].option_ord = options[i].option_ord;
                moptArr[i].children = options[i].children;
                moptArr[i].value = options[i].value;
            }

            var eType = params.event_type,
                targetName = params.target;

            children.combobox({
                data: (params.if_show === "N" ? [] : moptArr),
                valueField: 'code',
                textField: 'value',
                editable: false,
                multiple: true,
                readonly: (params.if_show === "N" ? true : false),
                width: params.width,
                height: params.height,
                onChange: function (newValue, oldValue) {
                    var data = $(this).combobox("options").data;
                    currValue = newValue,
                        selectedChild = "";
                    if (currValue.length == 0) {
                        var targetNode = $("input[data-name='" + targetName + "']");
                        //targetNode
                    }
                    if (eType === "CFS") {
                        for (var idx = 0; idx < currValue.length; idx++) {
                            $.each(data, function (i, n) {
                                if (n.code === currValue[idx]) {
                                    selectedChild += n.children + ",";
                                }
                            });
                        }
                        if (selectedChild.length > 0) {
                            selectedChild = selectedChild.substring(0, selectedChild.length - 1);
                        }
                        var targetNode = $("input[data-name='" + targetName + "']"),
                            targetValue = targetNode.data("opts"),
                            selectedChildArr = selectedChild.split(","),
                            childOpts = [];
                        for (idx = 0; idx < selectedChildArr.length; idx++) {
                            $.each(targetValue, function (i, n) {
                                if (n.code === selectedChildArr[idx]) {
                                    childOpts.push({
                                        code: n.code,
                                        value: n.value
                                    });
                                }
                            });
                        }
                        targetNode.combobox("clear");
                        targetNode.combobox({
                            data: childOpts,
                            readonly: false
                        });
                        //更新该项缓存数据
                        that.unDetail[targetName] = childOpts;
                    }
                    if (eType === "CFV") {
                        var targetNode = $("input[data-name='" + targetName + "']"),
                            targetValue = targetNode.data("opts"),
                            selectedChildArr = selectedChild.split(","),
                            childOpts = [];
                        for (var idx = 0; idx < currValue.length; idx++) {
                            $.each(data, function (i, n) {
                                if (n.code === currValue[idx]) {
                                    childOpts.push({
                                        code: n.code,
                                        value: n.value
                                    });
                                }
                            });
                        }
                        targetNode.combobox("clear");
                        targetNode.combobox({
                            data: childOpts,
                            readonly: false
                        });
                        //更新该项缓存数据
                        that.unDetail[targetName] = childOpts;
                    }
                }
            });

            if (params.if_show === "N") {
                children.data("opts", options);
            }

            // if(oValue && oValue !== ""){
            //  for(var idx=0;idx<options.length;idx++){
            //      if(oValue === options[idx].code){
            //          sValue = options[idx].value;
            //      }
            //  }
            // }

            // cellNode.val(sValue).attr("readonly",true).css("text-align","center");
            // cellNode.validatebox({
            //  required:(cell.params.required ? true : false),
            // });
            // children.attr("readonly",true).css("text-align","center");
            // children.validatebox({
            //  required:(params.required ? true : false),
            // });
            // var options = params.options;
            // children.unbind().bind("click",function(e){
            //  that.optTarget = $(this);
            //  var target = $(e.target),menu =null,i=0,opStr= '',flag= '';
            //  //cellNode.attr("size",1);
            //  $("#comboMenu").html("");
            //  for(var i=0;i<options.length;i++){
            //      flag = '';
            //      //if(options[i].code==oValue){ cellNode.val(options[i].value); }
            //      opStr += '<option '+flag+' value="'+options[i].code+'">'+options[i].value+'</option>';
            //      menu={
            //          text: options[i].value,
            //          id  : options[i].code,
            //          name: options[i].value
            //      }
            //      $("#comboMenu").menu("appendItem",menu);
            //  }
            //  $("#comboMenu").menu({
            //      onClick:doc.correlativeMenuEvent
            //  });
            //  $("#comboMenu").menu('show',{
            //      left:target.offset().left,
            //      top:target.offset().top+target.height()
            //  });
            // });
        }

        if (params.data_type == 'OPT') {


            if (params.options.length > 0) {

                var options = params.options,
                    sValue = "";

                if (params.inputType === 'DYNA_TITLE') {
                    children = $("<select data-name=" + params.item_name + "></select>");
                    tempNode.append(children);

                    children.append("<option value=''></option>");
                    for (var odx = 0; odx < options.length; odx++) {
                        children.append("<option value='" + options[odx].code + "'>" + options[odx].value + "</option>")
                    }
                } else {
                    children = $("<input data-name=" + params.item_name + ">");
                    tempNode.append(children);

                    children.val(sValue).css({
                        'width': '100%',
                        'border': 0,
                        'border-bottom': '1px #333 solid'
                    });

                    children.off('keyup', '**').on('keyup', doc.optKeyPress);
                    //children.off('change', '**').on('change', doc.optChange);

                    children.validatebox({
                        required: params.required
                    });

                    //$(document).off('click', '[data-name=' + params.item_name + ']', '**').on('click',)

                    children.data('options', params.options).attr('data-options', JSON.stringify(params.options));

                    children.bind("click", function (e) {
                        doc.showOPTMenu(e, this, options);
                    });
                }

                //


                //if (oValue && oValue !== "") {
                //    for (var idx = 0; idx < options.length; idx++) {
                //        if (oValue === options[idx].code) {
                //            sValue = options[idx].value;
                //        }
                //    }
                //}


                //


            }


            /*
             * // 动态表头
             if (params.item_name.indexOf('TITLE')) {

             }
             * */

            if (params.item_name === "ALLERGY_SHOW") {
                children.bind("change", function () {
                    if ($(this).val() === "ALLERGY_SHOW_02") {
                        $(this).addClass("red");
                    } else {
                        $(this).removeClass("red");
                    }
                });
            }
        }

        if (params.data_type == 'SWT') {
            children = $('<input type="checkbox" class="swt" data-type="SWT" type="checkbox" data-name="' + params.item_name + '" >');
            tempNode.append(children);
        }

        if (params.if_underline && params.if_underline === "N") {
            children.css({
                "border-bottom": "none",
                "height": "18px"
            });
        }


    }

    // 文本域
    if (params.type == 'Field') {
        tempNode.addClass('inputItem');
        tempNode.attr("data-type", "FIELD");
        children = $("<textarea placeholder='此处填写' class='doc-textarea' style='width: " + params.width + "px; height: " + params.height + "px' data-name='" + params.item_name + "'></textarea>");
        tempNode.append(children);
        if (params.if_underline && params.if_underline === "N") {
            children.css({
                "border-bottom": "none"
            });
        }
        if (params.readonly_flag === "Y") {
            children.attr("disabled", "disabled").css({
                "border-bottom": "none",
                "text-indent": 0,
                "font-size": "12px",
                "line-height": "18px"
            });
        }
    }

    // 显示图片
    if (params.type == 'image') {
        children = $('<img style="height: 100%;" data-type="image" src="' + ay.contextPath + '/' + params.image_url + '" data-name="' + params.item_name + '" >');
        tempNode.append(children);
    }

    if (default_value) {

        //TODO 暂时不处理其他类型
        /*if(params.data_type === "SEL" || params.data_type === "SEL"){
         $("input[value='"+default_value+"']").trigger("click");
         //children.find("input[value='"+default_value+"']").attr("checked","checked");
         this.fireEventStack.push({
         target:$("input[value='"+default_value+"']").selector,
         event:"click"
         });
         return tempNode;
         }*/
        children.val(default_value);
    }
    return tempNode;
};

doc.showOPTMenu = function (event, targetObj, optionsObj) {
    var that = doc;
    var options = optionsObj;

    that.editInfo.target = $(targetObj);
    var windowHeight = $(window).height();

    var target = event ? $(event.target) : $(targetObj),
        menu = null,
        i = 0,
        opStr = '',
        flag = '';
    //cellNode.attr("size", 1);
    var $comboMenu = $("#comboMenu");
    $comboMenu.html("");
    $comboMenu.menu("appendItem", {
        // 修复下拉为空时,值未修改的情况 指定为空的id为empty
        text: "",
        id: "empty",
        name: ""
    });
    for (var i = 0; i < options.length; i++) {
        flag = '';
        //if(options[i].code==oValue){ cellNode.val(options[i].value); }
        opStr += '<option ' + flag + ' value="' + options[i].code + '">' + options[i].value + '</option>';
        menu = {
            text: options[i].value,
            id: options[i].code,
            name: options[i].value
        };
        $("#comboMenu").menu("appendItem", menu);
    }
    //opStr += '<option '+flag+' value="">333333</option>';
    $comboMenu.menu({
        onClick: doc.selWidgetMenuEvent,
        onHide: doc.selWidgetClearMenu
    });
    $comboMenu.menu('show', {
        left: target.offset().left,
        top: (target.offset().top + target.height() <= 0 ? 0 : target.offset().top + target.height())
    });

    if ($comboMenu.offset().top < 0) {
        $comboMenu.css({
            'top': 0,
            'height': windowHeight - 60 + 'px',
            'overflow-y': 'scroll'
        });

        $(".menu-shadow").css({
            'top': 0,
            'height': windowHeight - 60 + 'px'
        })
    }
};

//解析表格头
doc.parseTableHeader = function (items, containter, fn) {
    var that = this;
    var tempNode = null;
    $.each(items, function (i, n) {
        if (n.type != 'line') {

            //增加自定义表头
            if (n.data_type == "DYNA_TITLE") {
                var cellTemp = that.getDetailCell(n);
                var index = ["TITLE_ONE", "TITLE_TWO", "TITLE_THREE", "TITLE_FOUR", "TITLE_FIVE"].indexOf(n.item_name);
                n.dynaIndex = index;
                if (!that.detail.changeableCell) {
                    that.detail.changeableCell = [];
                }
                that.detail.changeableCell.push({});
                that.detail.changeableCell[index].cellTemp = cellTemp;
                that.detail.changeableCell[index].params = n;
                var dynaTh = $("<div class='dynaTableHeader' data-type='dynaTh' style='position:absolute;left:" + n.posX + "px;top:" + n.posY + "px;width:" + n.width + "px;height:" + n.height + "px;'><div></div></div>");
                dynaTh.attr("data-index", index);
                containter.append(dynaTh);
                dynaTh.unbind().bind('click', function () {
                    var thIndex = parseInt($(this).attr("data-index"));
                    var currIndex = that.editInfo.thIndex;
                    if (thIndex == currIndex) {
                        console.log('同一个表头节点---return');
                        return;
                    }
                    that.editInfo.tdIndex = null;
                    that.editInfo.thIndex = thIndex;
                    that.enterEditStatus($(this), that.detail.changeableCell[thIndex]);
                });
            } else {
                tempNode = that.assembleNode(n, 0);
                tempNode.css({
                    "line-height": "inherit"
                });
                tempNode.html("<span class='headrText'>" + tempNode.html() + "</span>")
                tempNode.addClass("hBefore");
                containter.append(tempNode);
            }

        } else {
            tempNode = that.assembleNode(n, 0);
            tempNode.css({
                "backgroundColor": "inherit"
            }).append('<svg  width="100%" height="100%" version="1.1"></svg>');
            containter.append(tempNode);
            var line = '';
            if (n.direction) {
                tempNode.attr('data-dir', n.direction);
                line = '<line x1="0" y1="0" x2="' + n.width + '" y2="' + n.height + '" style="stroke:rgb(0,0,0);stroke-width:1"/>';
            } else {
                line = '<line x1="0" y1="' + n.height + '" x2="' + n.width + '" y2="0" style="stroke:rgb(0,0,0);stroke-width:1"/>';
            }
            tempNode.children().html(line);
        }
    });
    return fn();
}

doc.getDetailCell = function (item) {
    var cellTemp = '';
    switch (item.data_type) {
        case 'DATE':
        case 'TIME':
        case 'SYS_TIME':
            cellTemp = '<input type="text" data-required="' + item.required + '" style="width:' + (item.width - 1) + 'px;height:' + item.height + 'px;"/>'
            break;
        case 'BP':
        case 'NUM':
        case 'STR':
            cellTemp = '<input type="text" data-required="' + item.required + '" data-name="' + item.item_name + '"  style="width:' + (item.width - 1) + 'px;height:18px;"/>';
            break;
        // case 'OPT':
        //  cellTemp = '<select data-required="'+item.required+'" style="width:'+(item.width-1)+'px;height:'+(item.height-1)+'px;"></select>';
        //  break;
        case 'DYNA_TITLE':
            //case 'MSEL':
            //case 'SEL':
            cellTemp = '<div data-required="' + item.required + '" style="width:' + (item.width - 1) + 'px;height:' + (item.height - 1) + 'px;"></div>';
            break;
        case 'SWT':
            cellTemp = '<input data-required="' + item.required + '" class="swt" data-type="SWT" type="checkbox" />'
            break;
        case 'SIGNATURE':
            cellTemp = '';
            break;
        default:
            cellTemp = '<input type="text" data-required="' + item.required + '" data-name="' + item.item_name + '"  style="width:' + (item.width - 1) + 'px;height:' + (item.height - 1) + 'px;"/>';
    }
    return cellTemp;
}

doc.replaceText = function (value) {
    return value.replace(/[^\-\.0-9]/g, '');
};

doc.parseDetail = function (items, containter, fn) {
  function edit() {
    var node = $(this);

    if (that.loadDataParams.isHistoryDoc === 'true' || that.loadDataParams.template_id.indexOf('print') >= 0) {
      return;
    }

    // 获取当前点击行索引和前一次编辑索引 判断是否换行
    var trIndex = parseInt($(this).attr("page-index")), //当前行索引
      ctrIndex = that.editInfo.trIndex, //当前操作行索引,首次进入为NULL
      //tdIndex = that.editInfo.tdIndex,
      filterPass = ["SIGNATURE"]; // 过滤无需展示编辑框的控件

    //var data_type  = that.detail.cell[tdIndex].params.data_type;

    //如果是DYNA_TITLE动态表头直接进入enterEditStatus不触发任何行列更新
    // TODO 增加动态表头
    /*if (data_type === "DYNA_TITLE") {
     that.enterEditStatus($(this), that.detail.cell[tdIndex]);
     that.editInfo.target = null;
     //that.editInfo.tdIndex = null;
     return;
     }*/

    // 同一行不处理;
    if (ctrIndex === trIndex) {
      return;
    }

    // 首次进入或保存后ctrIndex为null 赋值ctrIndex;
    if (ctrIndex === null) {
      ctrIndex = trIndex;
      that.editInfo.trIndex = ctrIndex;
      console.log(trIndex);
    }
    //that.editInfo.thIndex = null;

    // 不在同一行的数据 提示保存后才能继续操作
    if (trIndex != ctrIndex) {
      $.messager.confirm("保存提示", "当前编辑行尚未保存点击“确定”保存，点击“取消”继续编辑该行。", function(r) {
        that.nextIndex = trIndex;
        if (r) {
          //保存切换到下一个编辑行
          that.saveData(that.enterEditByIndex);
        } else {
          //不保存切换到下一行

        }
      });
      return;
    }

    //更改编辑状态
    that.editInfo.saveStatus = true;

    //查房按钮

    var highCheck = $('#highCheck'),
      masterCheck = $('#masterCheck'),
      tdDate = $(node.children('td')[0]).data('value'),
      tdTime = $(node.children('td')[1]).data('value'),
      parentDocment = $(window.parent.document),
      patientId = parentDocment.find('#patientId').val(),
      userName = parentDocment.find('#nurseId').val(),
      userId = parentDocment.find('#nurseName').text();

    log(window);

    $('#masterCheck, #highCheck').off('click').on('click', function(e) {
      var checkType = $(this).data('type');
      log('点击');
      $.post(ay.contextPath + '/nur/doc/nurseRounds.do', {
        type: checkType,
        inpatient_no: patientId,
        create_person: userName + "-" + userId,
        date_list: tdDate,
        time_list: tdTime,
        template_id: that.loadDataParams.template_id
      }).done(function(res) {
        var currentItem = parentDocment.find('#' + parentDocment.find('.tree-node-selected').attr('id'));
        log(res);
        if (res['rslt'] === '0') {
          // 刷新左侧菜单
          function loadMenu() {
            window.parent.jsPlugin.doPlugin('patientMenu', {
              'patientId': window.parent.patient.patientId,
              'hospitalNo': window.parent.patient.hospitalNo,
              'departmentId': window.parent.patient.departmentId
            });
          }

          /*window.parent.jsPlugin.loadPlugin(ay.contextPath + '/resources/js/main/patientMenu.js', function () {
           loadMenu();
           });
           console.log(parentDocment.find('.tree-node-selected').attr('id'));

           //that.saveData();
           currentItem.trigger('click');
           // 第二次无法选中
           currentItem.addClass('tree-node-selected');
           //that.dataToView(null, false);

           */
          $.messager.alert("提示", "查房成功！", 'success', function() {
            window.location.reload();
          });


        } else {
            $.messager.alert("提示", res.msg || '查房失败, 请重试!');
        }
      }).fail(function(err) {
        $.messager.alert("警告", "服务器错误！");
      });
    });

    $(document).on('keyup', '[data-name="outTake"],[data-name="inTake"]', function(e) {
      $(this).val(that.replaceText($(this).val()));
    });

    $(document).on('keyup', 'input.timespinner-f', function(e) {
      if (e.which === 8 || e.which === 46) return;
      var $val = $(this).val(),
        len = $val.length;

      if (len === 2) {
        $(this).val($val.replace(/^\d{2}/g, '$&' + ':'));
      }

    });

    that.enterEditStatus(null, that.detail.cell[0], true, ctrIndex);
  }
  var that = this;

  // 创建显示表格 用来显示一页内容
  var table = $("<table id='dataTable'><tbody></tbody></table>");

  // 创建所有信息缓存表格 主要用来加载所有的行
  var tableList = $("<table id='dataTableList'><tbody></tbody></table>");
  containter.append(table);
  containter.append(tableList);

  // 点击tr触发开始编辑状态
  if (this.permission) table.delegate('tr[class!="ignore"]', 'dblclick', edit);

    if (items.length == 0) {
        return;
    }

    //新增detail td数据缓存  cellTemp:dom模板  params:配置信息
    this.detail.cell = [];
    $.each(items, function (i, n) {
        that.detail.cell.push({});
        var cellTemp = '';
        cellTemp = that.getDetailCell(n);
        that.detail.cell[i].cellTemp = cellTemp;
        that.detail.cell[i].params = n;
    });
}

/*
 创建新的容器
 */
doc.createContainter = function (config) {
    this.pageIndex = this.pageIndex + 1;
    if (this.pageIndex > 1) {
        $("#editBox").append("<div style='page-break-after:always;'></div>")
    }
    var containter = $("<div class='editor' id='editor" + this.pageIndex + "'></div>");
    $("#editBox").append(containter);

    //生成显示纸张 根据是否为表格类型生成不同规格容器
    if (!this.isTableType) {
        containter.css({
            "width": "720px",
            "height": "1000px",
            "position": "relative",
            "margin": "10px auto 10px"
        });
    } else {
        if (config.orientation.toLowerCase() === 'portrait') {
            containter.css({
                "width": (config.width - 40) + "px",
                "min-height": (config.height + 30) + "px",
                "position": "relative",
                "margin": "10px auto"
            });
        } else {
            containter.css({
                "width": (config.width - 40) + "px",
                "min-height": (config.height - 30) + "px",
                "position": "relative",
                "margin": "10px auto"
            });
        }
    }
    return containter;
}
/*
 *  初始化新非表格页面
 */
doc.initNewPage = function (containter) {
    var containterOne = $("#editor" + "1");
    containter.append(containterOne.find(".band[name='page-header']").clone());
    var detail = $("<div class='band' name='detail'>");
    containter.append(detail);
    detail.height((970 - containter.children().height()));
    return detail;
}
/*
 * i 上次解析的位置,items 解析的组件,containter band容器
 */
doc.parseItemForBreak = function (i, items, containter) {
    var nextPageItems = [];
    var offset = items[i].posY;
    $.each(items, function (index, n) {
        if (index <= i) {
            return true;
        }
        nextPageItems.push(n);
    });
    console.log(nextPageItems);
    this.parseItems(nextPageItems, containter, offset);
}
/*
 重置
 */
doc.reset = function () {

}

doc.getTempConfig = function (url, callback, params) {
    var that = this;

    try {
        $.ajax({
            type: "GET",
            url: url,
            data: params,
            success: function (msg) {
                if (msg.rslt === "0" || msg.rslt === 0) {

                    //缓存文书模板数据，修改获取文书模板状态
          that.tempConfig = msg;
          that.permission = msg.data.permission;
          // 文书打印方向:水平Landscape/垂直Portrait
                    //
                    that.getTempConfigStatus = true;
          try {
            that.orientation = msg.data.template[0]['orientation'].toLowerCase();

                    } catch (err) {
                        console.log(err);
                        that.orientation = 'portrait';
                        //callback.call(doc, {});

          }
          callback.call(doc, msg.data.template[0]);

                }
            }
        });
    } catch (e) {
        console.log("error: " + e);
    }
}

/*
 加载缓存系统
 */
doc.initDataSysTem = function () {

    this.Data = $("#dataElm");

    // 表格类型list缓存数组
    this.Data.data("data_list", []);

    // 非表格类型缓存对象
    this.Data.data("data_item", {});

    // 动态表头缓存数组
    this.Data.data("dyna_list", []);

}

/*
 设置基础数据
 url-hash值 和 护士信息等
 */
doc.setBaseData = function () {

    //获取url “？”后的值 返回对象
    this.url = ay.hashToObj(window.location.href);
    // this.url = {
    //  day: "2015-08-22",
    //  id: "record",
    //  patientId: "ZA4635737_1",
    //  recordId: "",
    //  template_id: "1439800874536",
    //  type: "2"
    // }

    // 护士信息
    this.nurse = {
        id: $(window.parent.document).find("#nurseId").val(),
        name: $(window.parent.document).find("#nurseName").text()
    }
    this.Data.data("inpatient_no", this.url.patientId);
    this.Data.data("template_id", this.url.template_id);
}

/*
 辅助方法
 */
doc.getValFormOptions = function (key, options) {
    var op = options;
    for (var i = 0; i < op.length; i++) {
        // 如果key为gary or empty,则为空值
        if (key === 'gary' || key === 'empty') return '';
        if (key == op[i].code) {
            return op[i].value;
        }
    }
    return key;
};
/*
 * OPT类型可输入
 * */

doc.optKeyPress = function (event) {
    var $this = $(this),
        _value = $this.val();
    var options = $this.data('options');

    doc.showOPTMenu(event, this, options);

    log('sss', _value);

    $this.parents('td')
        .data('value', _value)
        .attr('data-value', _value)
        .data('text', _value)
        .attr('data-text', _value);
    $this.attr('data-value', _value);

    var currentOpts = options.filter(function (optItem) {
            return optItem.value.indexOf(_value.trim()) >= 0;
        }) || [];

    if (currentOpts.length === options.length) {
        $('#comboMenu').find('.menu-item').removeClass('highlight-option option-hide');
    }

    if (_value == '') return;

    options.forEach(function (item) {
        var foundItem = $('#comboMenu').menu('findItem', item.value);

        if (foundItem) {
            if (item.value.indexOf(_value.trim()) >= 0) {
                if (!$(foundItem.target).hasClass('highlight-option')) {
                    $(foundItem.target).addClass('highlight-option');
                }
                $(foundItem.target).removeClass('option-hide');

            } else {
                $(foundItem.target).addClass('option-hide')
            }
        }
    });

    $('#comboMenu').height('auto');
    $('.menu-shadow').height('auto');

};

doc.getCheckbox = function (checkboxs, val) {
    var checkbox = null;
    for (var i = 0; i < checkboxs.length; i++) {
        if (checkboxs[i].checkbox_code == val) {
            checkbox = checkboxs[i];
            return checkbox;
        }
    }
    return checkbox;
}

doc.printTable = function () {

    var w = $('#printFrame')[0].contentWindow;
    var html = $("#editBox").html();
    var that = doc;
    // w.document.head.innerHTML = "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=GBK\">"+
    //      "<meta http-equiv=\"pragma\" content=\"no-cache\">"+
    //      "<meta http-equiv=\"cache-control\" content=\"no-cache\">"+
    //      "<meta http-equiv=\"expires\" content=\"0\">   "+
    //      "<title> </title> "+
    //      "<link rel=\"shortcut icon\" href=\"./resources/img/favicon.ico\"> "+
    //      "<link id=\"themeStyles\" rel=\"stylesheet\" href=\"./resources/js/easyui/themes/anyi/easyui.css\">"+
    //      "<link rel=\"stylesheet\" type=\"text/css\" href=\"./resources/js/easyui/themes/icon.css\">"+
    //      "<link rel=\"stylesheet\" type=\"text/css\" href=\"./resources/css/public.css\">"+
    //      "<script language=\"javascript\">"+
    //      "var ay = ay || {};"+
    //      "ay.contextPath = \"..\";"+
    //      "</script><style type=\"text/css\" adt=\"123\"></style>"+
    //      "<script type=\"text/javascript\" src=\"./resources/js/jquery-1.8.3.min.js\"></script> "+
    //      "<script type=\"text/javascript\" src=\"./resources/js/easyui/jquery.easyui.min.js\"></script>"+
    //      "<script type=\"text/javascript\" src=\"./resources/js/easyuiUtils.js\"></script> "+
    //      "<script type=\"text/javascript\" src=\"./resources/js/easyui/locale/easyui-lang-zh_CN.js\"></script>"+
    //      "<script type=\"text/javascript\" src=\"./resources/js/common.js\" charset=\"utf-8\"></script>"+
    //      "<!-- import data picker control. -->"+
    //      "<script type=\"text/javascript\" src=\"./resources/js/DatePicker/WdatePicker.js\" charset=\"utf-8\"></script><link href=\"./resources/js/DatePicker/skin/WdatePicker.css\" rel=\"stylesheet\" type=\"text/css\">"+
    //      "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\">"+
    //      '<link rel="stylesheet" type="text/css" href="./resources/css/normalize.css"><link rel="stylesheet" type="text/css" href="./resources/css/docMain.css">';
    w.document.head.innerHTML = '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/normalize.css"><link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/docMain.css">'
    w.document.body.innerHTML = html;
    var printWin = $(w.document.body);
    printWin.addClass("printTable"); //.addClass("fixBorderLine");
    printWin.find(".editor").css({
        "padding": "0",
        "margin": "0",
        "margin-bottom": "5px"
    });
    /*=======================修改打印时checkbox样式===========================*/
    printWin.find(".block.inputItem input[type='checkbox']").each(function () {
        $(this).after("<label for='" + $(this).attr('name') + "'></label>");
    });
    printWin.find(".block.inputItem[data-type='DAT_TIM']").each(function () {
        $(this).html("");
        $(this).append("<input type='text'>");
    });

    /*=======================页面偏移===========================*/
    if (doc.isTableType) {
        if (that.orientation === 'portrait') {
      $(w.document.head).append("<style>@media print {@page {size: portrait;#editor1{page-break-before:always;}}}body {text-align: center; } body > div {margin: 0 auto!important; text-align: left; }</style>");
    } else {
      $(w.document.head).append("<style>@media print {@page {size: landscape;#editor1{page-break-before:always;}}}body {text-align: center; } body > div {margin: 0 auto!important; text-align: left; }</style>");
    }
    }
    //printWin.width( printWin.find(".editor:eq(0)").width() +15 );


    //$("#printFrame").show();
    printWin.find("table").css("font-size", "12px");
    //删除分页
    // printWin.find(".pagination").remove();
    //清空页面内容
    /*=======================无需清空===========================*/
    //printWin.find("#dataTable").find("tbody").html("");
    /*=======================更换id===========================*/
    var dataTableList = printWin.find("#dataTable").detach(),
        tr = dataTableList.find("tr[class!='ignore']"),
    //trIgnore
        editor,
        newPage = null;

    var printSelect = printWin.find('select');

    printSelect.each(function (i, item) {
        var sIndex = item.selectedIndex,
            sText = $(item).find('option').eq(sIndex).text();

        $(item).parent().html(sText);
    });

    editor = printWin.html();

    //若表内数据为空，打印空表格
    if (tr.length === 0) {
        //设置pagefooter固定到底部
        printWin.find(".band[name='page-footer']").css({
            "position": "absolute",
            "top": this.footerTop + "px"
        });
        //直接在此打印，直接退出;
        setTimeout(function () {
            //printWin.removeClass("fixBorderLine");
            w.print();
        }, 1000);
        return;
    } else {
        //清空页面内容，准备重新绘制表格
        printWin.html("");
    }

    // 在此处进行打印分页
    var count = this.page.count || 0,
        breakIndex = this.page.breakIndex,
        i, start, end;
    var tablePrintHeaderItems = [],
        tablePrintHeaderOld = null,
        tablePrintHeaderIndex = null,
        currentIndex = -1,
        trStr = '',
        currentItem = null,
        tablePrintHeaderState = 0,
        data_list = this.Data.data("data_list"),
        data_item_list = this.data_item_list || [];

    for (i = 0; i < count; i++) {

        if (i == 0) {
            start = 0;
            end = breakIndex[i];
        } else {
            start = breakIndex[i - 1];
            end = breakIndex[i];
        }
        //创建页面
        printWin.append(editor);

        /*
         所有对表格的操作都存储在了本地变量中
         打印前首先对dataTableList中的内容进行再次渲染保证数据正确并且分页正确
         */
        this.dataToPrintView(printWin.children().eq(i));


        for (start; start < end; start++) {

            // 插入该页中的行
            if (tablePrintHeaderState === 0) {
                tablePrintHeaderState = -1;

                // TODO: 打印表头为啥要用旧数据?
                /*currentIndex = tr.eq(start).attr("data-index");

                 currentItem = this.getPageFirstItem(data_list[currentIndex].record_id, this.data_item_list);

                 tablePrintHeaderOld = this.getTablePrintHeader(this.tablePrintType, i, tablePrintHeaderOld, currentItem);

                 tablePrintHeaderItems.push(tablePrintHeaderOld);

                 this.setTablePrintHeader(printWin.children().eq(i), tablePrintHeaderOld);*/

            }

            if (start === 0) {
                if (tr.eq(start).prev().hasClass('ignore')) {
                    trStr = tr.eq(start).prev()[0].outerHTML + tr.eq(start)[0].outerHTML;
                } else if (tr.eq(start).next().hasClass('ignore')) {
                    trStr = tr.eq(start)[0].outerHTML + tr.eq(start).next()[0].outerHTML;
                } else {
                    trStr = tr.eq(start)[0].outerHTML;
                }
            } else {
                if (tr.eq(start).next().hasClass('ignore')) {
                    trStr = tr.eq(start)[0].outerHTML + tr.eq(start).next()[0].outerHTML;
                } else {
                    trStr = tr.eq(start)[0].outerHTML;
                }
            }
            /*=======================更换ID===========================*/
            printWin.children(":last-child").find("#dataTableList tbody").append(trStr);
        }

        //打印中的页码

        newPage = $("<div>第 " + (i + 1) + " 页</div>");

        printWin.children(":last-child").append(newPage);
        newPage.css({
            "position": "absolute",
            "bottom": "0px",
            "width": "100%",
            "line-height": "12px",
            "text-align": "center"
        });

        // 重置tablePrintHeaderIndex为0 重新获取第一条的data_item数据 第一条只是相对于新增的editor来判断
        tablePrintHeaderState = 0;
    }
    //设置pagefooter固定到底部
    printWin.find(".band[name='page-footer']").css({
        "position": "absolute",
        "top": this.footerTop + "px"
    });
    /*=======================修复因产生滚动条而减少的宽度造成的bug===========================*/
    /*if (this.page.count >= 2) {
     printWin.find("table tr td:last-child").each(function () {
     var width = $(this).children().width() + 5;
     $(this).width(width);
     $(this).children().width(width);
     });
     }*/

    //增加列数据过滤方法，控制不需要的列在打印时不显示
    var filterArr = this.filterRowData();
    var _table = typeof that.printIndex !== 'undefined' ? printWin.find(".page-count .page-dataTable") : printWin.find(".editor #dataTableList");
    for (var fdx = 0; fdx < filterArr.length; fdx++) {
        if (filterArr[fdx] === 0) {
            _table.each(function () {
                $(this).find("tr").each(function () {
                    $(this).find("td:eq(" + fdx + ") div div").html("");
                });
            })
        }
    }

    // 行过滤, 只打印已选择行
    if (typeof that.printIndex !== 'undefined') {
        var trLen = printWin.find('tr').length,
            unLen = that.printIndex.length;
        $.each(that.printIndex, function (i, v) {
            /*printWin.find('tr').eq(v).css({
             'background': '#f60'
             });*/
            printWin.find('tr').eq(v).hide();
        });

        if (trLen === unLen) {
            $.messager.alert('提示', '您没有选择打印行, 没有数据需要打印!');
            return;
        }
    }

    // 去除无数据的div

    var editWrapper = printWin.find('.editor');

    editWrapper.each(function (i, v) {
        if ($(v).find('.band[name="detail"] table tbody').children().length === 0) {
            editWrapper.remove();
        }
    })

    setTimeout(function () {
        //printWin.removeClass("fixBorderLine");
        w.print();
    }, 1000);
}

doc.filterRowData = function () {
    var filterArr = [];
    var checkbox = $("#printToolTable td input");
    checkbox.each(function (i, n) {
        if ($(this).is(':checked')) {
            filterArr.push(1);
        } else {
            filterArr.push(0);
        }
    });
    return filterArr;
}

doc.dataToPrintView = function (containter) {
    this.dataToView(containter, true);
}

/*
 返回该rid对应item
 @rId  {str} record_id
 @itemList {arr} data_item_list
 @return item
 */
doc.getPageFirstItem = function (rId, itemList) {
    var i;
    for (i = 0; i < itemList.length; i++) {
        if (itemList[i].record_id === rId) {
            return itemList[i].list_item;
        }
    }
    return null;
}

/*

 */
doc.getCheckBoxChild = function (target) {
    var cInputCheck = null,
        cInputText = null,
        childVal = {};
    cCheck = '', cText = '';
    if (target) {
        cInputCheck = target.find("label > input[type='checkbox']:checked");
        cInputText = target.find("label > input[type='text']");
    }
    if (cInputCheck.length > 0) {
        cInputCheck.each(function () {
            if (!childVal[$(this).val()]) {
                childVal[$(this).val()] = [];
            }
            //cCheck += $(this).val()+"*";
            childVal[$(this).val()].push($(this).val());
        });
        if (cCheck.length > 0) {
            //cCheck = cCheck.substring(0,cCheck.length-1);
        }
    }
    if (cInputText.length > 0) {
        cInputText.each(function () {
            //cText += ($(this).val() ? $(this).val() : null) + "*";
            childVal[$(this).attr('name')] = $(this).val();
        });
        //cText = cText.substring(0,cText.length-1);
    }
    return childVal
}

/*
 获取表格打印头部
 @printType {str} 表头打印的方式 old:显示每一页第一条 new:显示最新的data_item内的 oldtonew:显示第一页第一条到最新的 change:每页第一条与前一页第一条的比较
 @pageIndex {num} 当前页索引 0。。1。。2。。
 @oldItem   {arr} 缓存的Items数据
 @item      {obj} 当前页第一条表头数据
 @return {obj} 处理过后的items可直接赋值到响应的控件上
 */
doc.getTablePrintHeader = function (printType, pageIndex, oldItem, item) {
    var rItems = null;

    if ("OLD" === printType) {
        return oldItem || item;
    } else if ("NEW" === printType) {
        var newItem = this.lastTablePrintHeader;
        return oldItem || newItem;
    }
    // 将第一条与最新的信息进行比较
    else if ("OLDTONEW" === printType) {

        var oItem = oldItem;
        // 是否是第一页 第一页第一条为最old的头部信息
        // data_item内的为最新
        if (oldItem) {
            return oldItem;
        } else {
            oItem = item
            var newItem = this.lastTablePrintHeader,
                checkArr = ["DE08_10_054_00", "PTNT_BED", "PTNT_DIAGNOSE"];
            for (var i = 0; i < oItem.length; i++) {
                if (checkArr.indexOf(oItem[i].template_item_id) >= 0) {
                    if (oItem[i].record_value != newItem[i].record_value) {
                        newItem[i].record_value = (oItem[i].record_value == "" ? "无" : oItem[i].record_value) + " → " + (newItem[i].record_value == "" ? "无" : newItem[i].record_value);
                    }
                }
            }
            return newItem;
        }
    }
    //若为第一页则显示第一页信息， 非第一页则与前面页信息比较不同则进行处理 x -> y
    else if ("CHANGE" === printType) {

        if (pageIndex === 0) {
            return item;
        }
        var oItem = oldItem,
            newItem = item,
            checkArr = ["DE08_10_054_00", "PTNT_BED", "PTNT_DIAGNOSE"];
        for (var i = 0; i < oItem.length; i++) {
            if (checkArr.indexOf(oItem[i].template_item_id) >= 0) {
                if (oItem[i].record_value != newItem[i].record_value) {
                    var splitArr = oItem[i].record_value.split("→");
                    if (splitArr.length == 1) {
                        newItem[i].record_value = (splitArr == "" ? "无" : oItem[i].record_value) + " → " + (newItem[i].record_value == "" ? "无" : newItem[i].record_value);
                    } else {
                        newItem[i].record_value = splitArr[1] + " → " + (newItem[i].record_value == "" ? "无" : newItem[i].record_value);
                    }

                }
            }
        }
        return newItem;
    } else {

    }
    //return items;
}

/*
 设置打印表头
 @target {dom} dom对象目标页面对象
 @items  {obj} 将要显示的数据
 */
doc.setTablePrintHeader = function (target, item) {
    for (var i = 0; i < item.length; i++) {
        if ("CREATE_PERSON2" === item[i].template_item_id) {
            continue;
        }
        target.find("input[data-name='" + item[i].template_item_id + "']").val(item[i].record_value);
    }
}

/*
 打印非表格类型
 */
doc.printPage = function () {
    var win = $('#printFrame')[0].contentWindow;
    var html = $("#editBox").html();
    //html = html.replace(/zoom\:\s[0-9]*\.?[0-9]*/,'zoom:1');
    this.printHtml(html, win);
}


doc.printHtml = function (html, w) {


    w.document.head.innerHTML = '<link rel="stylesheet" type="text/css" href="' + ay.contextPath + '/resources/css/docMain.css">'
    w.document.body.innerHTML = html;
    var printWin = $(w.document.body),
        pageNum;

    printWin.find('div')
        .removeClass('required-text')
        .removeClass('required-err');

    printWin.find('.signature-btn').remove();

    printWin.addClass('print');
    log(printWin.find('[itemname="出院记录"]'));
    debugger;
    //pageNum = printWin.find('[itemname="出院记录"]').length !== 0 ? sessionStorage.getItem('docPageNum') : 1;
    // 出院记录单特殊处理
    if (printWin.find('[itemname="出院记录"]').length !== 0) {
        var $prevPageDialog = $('#prev-page-dialog');

        $prevPageDialog.dialog({
            title: '护理记录单页码',
            modal: true,
            closed: true,
            buttons: [{
                text: '确定',
                handler: function () {
                    var $num = $('#prev-page-num');

                    $num.validatebox({
                        validType: 'minNum[0]',
                        tipPosition: 'right',
                        required: true
                    })

                    if ($num.validatebox("isValid")) {
                        doc.printAction(Number($('#prev-page-num').val()) + 1);
                        $prevPageDialog.dialog("close");
                    }
                }
            }, {
                text: '取消打印',
                handler: function () {
                    $prevPageDialog.dialog('close');
                    //return;
                }
            }]
        });

        doc.getPrintNum(window.parent.fromTemplate[0]).then(function (res) {
            if (res.rslt == '0') {

                $prevPageDialog.dialog('open');
                $('#prev-page-num').val(Math.abs(res.data - 1));
                pageNum = res.data + 1;
            }
        });
    } else {
        pageNum = 1;
        doc.printAction(pageNum);
    }
};

doc.printAction = function (pageCount) {
    var pageNum = pageCount,
        w = $('#printFrame')[0].contentWindow,
        printWin = $(w.document.body);

    printWin.find('.editor').each(function (i) {
        $(this).css({
            "font-weight": 100,
            "margin": 0,
            "marinBottom": "10px"
        });
        $(this).append("<div class='page-index'>第 " + ((i == 0 ? '' : i) + (pageNum || 2)) + " 页</div>");
    });
    //修改打印时checkbox样式
    printWin.find(".block.inputItem input[type='checkbox']").each(function () {
        $(this).after("<label for='" + $(this).attr('name') + "'></label>");
    });

    printWin.find(".block.inputItem[data-type='DAT_TIM']").each(function () {
        $(this).html("");
        $(this).append("<input type='text'>");
    });
    //$.parser.parse();
    doc.dataToPrintView(printWin);
    $(printWin).find('.single-approve-box').hide();
    setTimeout(function () {
        w.print()
    }, 500);
}


/*
 menu点击事件
 */
doc.selWidgetMenuEvent = function (item) {
    var dIndex = item.name && item.name.indexOf('、');
    console.log(item);
    console.log(doc.editInfo.target);
    var target = doc.editInfo.target;

    // 存在顿号[、]则取顿号前面的字符
    if (dIndex > -1) {
        item.name = item.name.substring(0, dIndex);
    }

    target.val(item.name).attr("data-value", item.id).validatebox("isValid");

    //判断是否计算分数
    var dataType = target.parents("td").attr("data-type");
    if (dataType === "MONOIDAL_SCORE") {
        var totalCell = target.parents("tr").find("td[data-type='SCORE_TOTAL_ROW'] div input"),
            scores = target.parents("tr").find("td[data-type='MONOIDAL_SCORE'] div input"),
            totalNum = 0;
        scores.each(function () {
            if ($(this).val()) {
                totalNum += parseInt($(this).val());
            }
        });
        totalCell.val(totalNum);
    }
}

/*
 联动下拉事件
 */
doc.correlativeMenuEvent = function (item) {
    var target = doc.optTarget;
    target.val(item.name).attr("data-value", item.id).validatebox("isValid");
    return false;
}

/*
 menu点击事件
 */
doc.selWidgetClearMenu = function (item) {
    $(this).html("");
}

/*
 表格类型文书保存记录后的 dataItemList 记录增加；解决首次录入记录无法打印问题
 */
doc.setDataItemList = function (recordStr, rId) {
    var dataItemList = this.data_item_list;
    var dataItem = recordStr.data_item;
    $.each(dataItem, function (i, n) {
        n.record_id = rId;
    });
    console.log(dataItem);
    dataItemList.push({
        "list_item": dataItem,
        "record_id": rId,
        "approve_status": "N"
    });
}

/*
 *  返回时间区间的判断
 *  d1,t1 当前日期时间; d2,t2 前一天日期时间; t3分隔时间点;
 */
doc.compareSection = function (d1, t1, d2, t2, t3) {
    var dateTime1 = new Date(d1 + " " + t1).getTime(),
        dateTime2 = new Date(d2 + " " + t2).getTime(),
        dateTime3 = new Date(d1 + " " + t3).getTime(),
        dateTime4 = new Date(d2 + " " + t3).getTime();

    // var t1h = parseInt(t1.split(":")[0],10),
    //  t1m = parseInt(t1.split(":")[1],10),
    //  t3h = parseInt(t3.split(":")[0],10),
    //  t3m = parseInt(t3.split(":")[1],10);

    if (dateTime1 + 3600 * 24 * 1000 < dateTime4) {
        return "tooLarge";
    }

    if (dateTime1 > dateTime3) {
        //加一天
        dateTime3 += 3600 * 24 * 1000;
    }

    /*if (dateTime2 > dateTime4 && dateTime1 < dateTime4) {
     //log(dateTime1, dateTime2, dateTime4);
     return "casual";
     }*/

    /*if (dateTime2 < dateTime3) {
     return 'yesterday';
     }*/

    if (dateTime2 >= dateTime1 && dateTime2 <= dateTime3) {
        //累加状态
        return "add";
    }
    if (dateTime2 > dateTime3) {
        return "break";
    }


}

/*doc.comparePrevSection = function (dateCurrent, timeCurrent, prevDate, prevTime, breakTime) {
 var currDateTime = new Date(dateCurrent + ' ' + timeCurrent).getTime(),
 prevDateTime = new Date(prevDate + ' ' + prevTime).getTime(),
 breakDateTime = new Date(dateCurrent + ' ' + breakTime);

 if (currDateTime > breakDateTime) {
 return 'noMorning';
 }

 if (currDateTime <= breakDateTime) {
 return 'morning'
 }

 };*/

doc.setPrintToolRow = function (items) {
    console.log(items);
    var tr = $("<tr>"),
        td = null,
        cb = null,
        length = items.length;

    $.each(items, function (i, n) {

        td = $("<td>");
        cb = $("<input type='checkbox' checked='checked'>");
        cb.width(20);
        cb.height(20);
        td.attr("data-type", n.data_type)
            .attr("itemname", n.item_name)
            .css({
                "width": n.width - 1,
                "height": "30px",
                "borderLeft": "1px solid #000",
        "textAlign": "center",
        "fontSize": n.size + 'px'
            })
            .append(cb);
        if (i === length) {
            td.width(td.width() - 1);
            td.css("border-right", "1px solid #000");
        }
        tr.append(td);
    });
    $("#printToolTable").append(tr);
}

doc.enterEditByIndex = function () {
    $("#dataTable").find("tr:eq(" + this.nextIndex + ")").trigger("click");
};

$(function () {
    doc.init();

    var reportType = window.parent.treeNode['report_type'],
        _type = window.parent.treeNode['type'];

    $('#print-row__table').on('click', 'input[type="checkbox"]', function (e) {
        var len = $('#print-row__table input[type="checkbox"]').length;
        var checkedLen = $('#print-row__table input:checked').length;

        log(len, checkedLen);
        $('#check-all')[0].checked = checkedLen === len;
    });

    log(window);

    if ((reportType === '2' || reportType === '1') && (_type === '2' || _type === 'day')) {
        $('#wrapper .layout-body').scroll(function () {
            var $this = $(this),
                sTop = $this.scrollTop(),
                pageHeadr = $('[name="page-header"]'),
                bandHeader = $('[name="cloumn-header"]'),
                bHeight = bandHeader.height(),
                detailWrapper = $('[name="detail"]'),
                pHeight = pageHeadr.height();

            if (sTop >= 10) {
                bandHeader.addClass('headFixed2');
                pageHeadr.addClass('headFixed1');
                detailWrapper.parent().css({
                    'padding-top': Number(bHeight + pHeight) + 'px',
                    'height': Number(670 - bHeight - pHeight) + 'px',
                    'min-height': '0'
                });
                $('.headFixed2').css({
                    top: pHeight + 'px'
                });
            } else {
                bandHeader.removeClass('headFixed2');
                pageHeadr.removeClass('headFixed1');
                detailWrapper.parent().css({
                    'padding-top': 0,
                    'height': 'auto',
                    'min-height': '670px'
                });

                bandHeader.css({
                    top: '0px'
                });
            }

        });

    }

});

$(function() {
    $(document.body).keydown(function(e) {
        if (e.keyCode === 37 || e.keyCode === 39) {
            e.preventDefault();
            var target = $('#editBox')[0];
            if (target) {
              if (e.keyCode === 37) {
                target.scrollLeft = 0;
              } else if (e.keyCode === 39) {
                target.scrollLeft = $(target).width();
              }
            }
          }
    });
})
