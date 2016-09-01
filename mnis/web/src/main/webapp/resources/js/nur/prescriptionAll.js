$(function () {

    //设置content区域高
    $('.content').height($(window).height() - $('.top-tools').height());
    var recordDay = new Date().format("yyyy-MM-dd");
    $("#date").val(recordDay);

    Order.init();
});

window.Order = {
    DEFAULT_TYPE: "CZ",
    CURRENT_TYPE: null,
    CURRENT_PATIENT: null
}

Order.init = function () {

    var dateInitEnd = new Date(new Date().getTime()).format('yyyy-MM-dd'),
        dateInitStart = new Date(new Date().getTime()).format('yyyy-MM-dd');
    $('#startDate').val(dateInitStart);
    $('#endDate').val(dateInitEnd);

    this.initPageEvent();
    this.initViewTable(this.DEFAULT_TYPE);
    this.getPatOrderDetail(null,false);
}

Order.initPageEvent = function () {
    var that = this;
    $("#topBtnItems").delegate(".btn-item", "click", function () {
        var btnItems = $("#topBtnItems .btn-item");
        var target = $(this),
            type = target.attr("data-type");

        if (that.CURRENT_TYPE === type) {
            return;
        }
        else {
            btnItems.each(function () {
                $(this).removeClass("checked");
            });
            target.addClass("checked");
            // 装载datagrid
            that.initViewTable(type);
            // 触发查询事件
            that.getPatOrderDetail(null,false);
        }
    });

    $('#orderExecTypeCode').on('change', function () {
        that.getPatOrderDetail(null,true);
    });

    $("#searchOrderBtn").bind("click", function () {
        that.getPatOrderDetail(null,true);
        //Order.getPatOrderDetail.call(that);
    });
}

Order.initViewTable = function (type) {

    // 清空表格数据
    if (this.CURRENT_TYPE !== null) {
        $('#viewTable').datagrid('loadData', {total: 0, rows: []});
    }

    this.CURRENT_TYPE = type;

    switch (type) {
        case "CZ":
            this.initCzViewTable();
            break;
        case "LZ":
            this.initLzViewTable();
            break;
        default:
            break;
    }
}

Order.getPatOrderDetail = function (pList,isSelectPat) {
    var that = this;

    // 获取患者 没有传入患者就去父页面找 父页面没有就 跳出;

    var patient = null;
    if (pList) {
        patient = pList;
    }
    else {
        var url = location.href;
        patient = window.parent.peopleList ? window.parent.peopleList.toString() : url.substring(url.indexOf('=') + 1, url.length);
        if (!patient) {
        	if(isSelectPat)
        		$.messager.alert("提示", "没有选择患者!");
            $('#viewTable').datagrid('loadData', {
                total: 0,
                rows: []
            });
            return;
        }
    }

    // 获取类型
    var type = this.CURRENT_TYPE || this.DEFAULT_TYPE;

    //时间段 临嘱去时间框内的时间，长嘱取当天
    //if ("LZ" === type) {
    //    var date = new Date($("#startDate").val() + " 00:00:00");
    //}
    //else {
    //    var date = new Date(new Date().format("yyyy-MM-dd") + " 00:00:00");
    //}
    var startDate = new Date($("#startDate").val() + " 00:00:00").format("yyyy-MM-dd hh:mm:ss");
    var endDate = new Date(new Date($("#endDate").val()).getTime() + 3600 * 24 * 1000).format("yyyy-MM-dd hh:mm:ss");
    var typeCode = $("#orderExecTypeCode").val();
    try {
        // $.get(ay.contextPath+"/nur/patOrderDetail/getOrderBaseGroupList.do?patientId="+patient+"&orderTypeCode="+type+"&startDate=2015-09-24 00:00:00&endDate=2015-09-25 00:00:00",function(data){
        // 	that.parseOrder(data,type);
        // });
        $.get(ay.contextPath + "/nur/patOrderDetail/getOriginalOrderList.do?patientId=" + patient + "&orderTypeCode=" + type + "&orderExecTypeCode=" + typeCode + "&startDate=" + startDate + "&endDate=" + endDate, function (data) {
            console.log(data);
            that.parseOrder(data, type);
        });
    } catch (e) {
        console.log("error:" + e);
    }

}

/*
 解析医嘱
 @params data{json} 医嘱数据
 @params type{str } 类型
 */
Order.parseOrder = function (data, type) {
    console.log(data);
    switch (type) {
        case "CZ":
            this.parseCzOrder(data);
            break;
        case "LZ":
            this.parseLzOrder(data);
            break;
        default:
            break;
    }

}

Order.initCzViewTable = function (cb) {
    // 加载长嘱datagrid;
    var columns = [[
        {
            field: 'orderStatusCode',
            title: '',
            formatter: function (value, row) {
                var str = '';
                if (row.emergent) {
                    str += '<span class="adviceState bgcred">急</span>';
                }

                if (row.orderStatusCode == 'N') {
                    str += '<span class="adviceState bgcgreen">新</span>';
                } else if (row.orderStatusCode == 'S') {
                    str += '<span class="adviceState bgcgray">停</span>';
                } else {

                }
                return str;
            }
        }, {
            field: 'createDate',
            title: '开立时间',
            align: "center",
            width: 80,
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        }, {
            field: 'orderItems',
            title: '医嘱内容',
            width: 250,
            align: "center",
            formatter: function (value, row) {
                var str = "";
                for (var i = 0; i < value.length; i++) {
                    str += "<p>" + value[i].orderName + "</p>";
                }
                return str;
            }
        }, {
            field: 'dosage',
            title: '剂量',
            width: 60,
            align: "center",
            formatter: function (value, row) {
                var str = "";
                for (var i = 0; i < value.length; i++) {
                    str += "<p>" + value[i].dosage + value[i].dosageUnit + "</p>";
                }
                return str;
            }
        },
        {
            field: 'usageName',
            title: '用法',
            width: 60
        }, /*{
         field:'planExecTime',
         title:'计划时间',
         width:120,
         align:"center",
         formatter:function(value,row){
         return value;
         }
         },*/{
            field: 'deliverFreq',
            title: '频率',
            width: 60,
            align: "center",
            formatter: function (value, row) {
                return value;
            }
        }, {
            field: 'createDoctorName',
            title: '开立医生',
            width: 60,
            align: "center"
        },
        {
            field: 'process',
            title: '进度',
            width: 60,
            align: "center",
            formatter: function (value, row) {
                return value;
            }
        },
        {
            field: 'beginDate',
            title: '开始时间',
            width: 80,
            align: "center",
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        }, {
            field: 'endDate',
            title: '结束时间',
            width: 80,
            align: "center",
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        }/*,{
         field:'createCheckNurseName',
         title:'核对护士',
         width:60,
         align:"center"
         }*/, {
            field: 'stopDate',
            title: '停止时间',
            width: 80,
            align: "center",
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        }, {
            field: 'stopDoctorName',
            title: '停止医生',
            width: 60,
            align: "center"
        }/*,{
         field:'endCheckNurseName',
         title:'核对护士',
         width:60,
         align:"center"
         }*//*,{
         field:'execNurse',
         title:'执行签名',
         width:120,
         align:"center",
         formatter:function(value,row){
         var arr = value.split(",");
         if(arr[0]==="undefined"){
         return "";
         }
         return new Date(arr[0]).format("MM-dd hh:mm") +"&nbsp;&nbsp;"+arr[1];
         }
         },*/
    ]];
    $("#viewTable").datagrid({
        fit: true,
        fitColumns: true,
        // pagination:true,
        // pageSize:20,
        remoteSort: false,
        singleSelect: true,
        //sortName: 'beginDate, usageName',
        //sortOrder: 'desc',
        //multiSort:true,
        rownumbers: true,
        columns: columns,
        view: detailview,
        showGroup: true,
        rowStyler: function (index, row) {
            var proc = row['process'],
                idx = proc.indexOf('/');
            console.log(proc, idx);
            //console.log(index, row);
            if (Number(proc.substring(0, idx)) > 0) {
                return 'background-color:#ffe48d;color:#333;font-weight:bold;';
            }
        },
        detailFormatter: function (index, row) {
            return '<div style="padding:2px"><table class="ddv"></table></div>';
        }, 
        onExpandRow: function (index, row) {
            console.log(row);
            var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
            var childTableData = [];
            var orderExecList = row.orderExecList;
            for (var i = 0; i < orderExecList.length; i++) {
                if (orderExecList[i].orderExecId && "" != orderExecList[i].orderExecId) {
                    childTableData.push(orderExecList[i]);
                }
            }
            if (childTableData.length == 0) {
                return;
            }
            ddv.datagrid({
                data: childTableData,
                fitColumns: true,
                singleSelect: true,
                rownumbers: true,
                loadMsg: '',
                height: 'auto',
                rowStyler: function (index, row) {
                    console.log(index, row);
                    if (row.execNurseName !== '') {
                        return 'background-color:#ffe48d;color:#333;font-weight:bold;';
                    }
                },
                columns: [[
                    {field: 'orderExecId', title: '执行编码', width: 100, align: "center"},
                    {field: 'planDate', title: '计划执行时间', width: 200, align: "center"},
                    {field: 'execDate', title: '执行时间', width: 200, align: "center"},
                    {
                        field: 'execNurseName',
                        title: '执行人',
                        width: 200,
                        align: "center"
                    }
                ]],
                onResize: function () {
                    $('#viewTable').datagrid('fixDetailRowHeight', index);
                },
                onLoadSuccess: function (data) {
                    setTimeout(function () {
                        $('#viewTable').datagrid('fixDetailRowHeight', index);
                       /* $(this).find('span.datagrid-row-expander').trigger('click'); */
                    }, 0);
                }
            });
            $('#viewTable').datagrid('fixDetailRowHeight', index);
        }
    });

    //return cb();
}

Order.initLzViewTable = function (cb) {
    var columns = [[
        {
            field: 'createDate',
            title: '开立时间',
            align: "center",
            width: 60,
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        },
        {
            field: 'orderItems',
            title: '医嘱内容',
            width: 200,
            align: "center",
            formatter: function (value, row) {
                var str = "";
                for (var i = 0; i < value.length; i++) {
                    str += "<p>" + value[i].orderName + "</p>";
                }
                return str;
            }
        },
        {
            field: 'dosage',
            title: '剂量',
            width: 50,
            align: "center",
            formatter: function (value, row) {
                var str = "";
                for (var i = 0; i < value.length; i++) {
                    str += "<p>" + value[i].dosage + value[i].dosageUnit + "</p>";
                }
                return str;
            }
        }, {
            field: 'usageName',
            title: '用法',
            width: 50
        }, /*{
         field:'planExecTime',
         title:'计划时间',
         width:120,
         align:"center",
         formatter:function(value,row){
         return value;
         }
         },*/{
            field: 'createDoctorName',
            title: '开立医生',
            width: 60,
            align: "center"
        },
        {
            field: 'process',
            title: '进度',
            width: 60,
            align: "center",
            formatter: function (value, row) {
                return value;
            }
        },
        {
            field: 'beginDate',
            title: '开始时间',
            width: 60,
            align: "center",
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        }, {
            field: 'endDate',
            title: '结束时间',
            width: 60,
            align: "center",
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        }/*,{
         field:'createCheckNurseName',
         title:'核对护士',
         width:60,
         align:"center"
         }*/, {
            field: 'stopDate',
            title: '停止时间',
            width: 60,
            align: "center",
            formatter: function (value, row) {
                console.log(value, row);
                return value.replace(/\s/g, '<br/>');
            }
        }, {
            field: 'stopDoctorName',
            title: '停止医生',
            width: 80,
            align: "center"
        }
    ]];

    //$("#viewTable").hide();
    $("#viewTable").datagrid({
        fit: true,
        fitColumns: true,
        // pagination:true,
        // pageSize:20,
        remoteSort: false,
        singleSelect: true,
        //sortName: 'beginDate, usageName',
        //sortOrder: 'desc',
        //multiSort:true,
        rownumbers: true,
        columns: columns,
        view: detailview,
        showGroup: true,
        rowStyler: function (index, row) {
            var proc = row['process'],
                idx = proc.indexOf('/');
            console.log(proc, idx);
            //console.log(index, row);
            if (Number(proc.substring(0, idx)) > 0) {
                return 'background-color:#ffe48d;color:#333;font-weight:bold;';
            }
        },
        detailFormatter: function (index, row) {
            return '<div style="padding:2px"><table class="ddv"></table></div>';
        },
        onExpandRow: function (index, row) {
            console.log(row);
            var ddv = $(this).datagrid('getRowDetail', index).find('table.ddv');
            var childTableData = [];
            var orderExecList = row.orderExecList;
            for (var i = 0; i < orderExecList.length; i++) {
                if (orderExecList[i].orderExecId && "" != orderExecList[i].orderExecId) {
                    childTableData.push(orderExecList[i]);
                }
            }
            if (childTableData.length == 0) {
                return;
            }
            ddv.datagrid({
                data: childTableData,
                fitColumns: true,
                singleSelect: true,
                rownumbers: true,
                loadMsg: '',
                height: 'auto',
                rowStyler: function (index, row) {
                    console.log(index, row);
                    if (row.execNurseName !== '') {
                        return 'background-color:#ffe48d;color:#333;font-weight:bold;';
                    }
                },
                columns: [[
                    {field: 'orderExecId', title: '执行编码', width: 100, align: "center"},
                    {field: 'planDate', title: '计划执行时间', width: 200, align: "center"},
                    {field: 'execDate', title: '执行时间', width: 200, align: "center"},
                    {
                        field: 'execNurseName',
                        title: '执行人',
                        width: 200,
                        align: "center"
                    }
                ]],
                onResize: function () {
                    $('#viewTable').datagrid('fixDetailRowHeight', index);
                },
                onLoadSuccess: function () {
                    setTimeout(function () {
                        $('#viewTable').datagrid('fixDetailRowHeight', index);
                    }, 0);
                }
            });
            $('#viewTable').datagrid('fixDetailRowHeight', index);
        }
    });
    /*$("#viewTable").datagrid({
        fit: true,
        fitColumns: true,
        // pagination:true,
        // pageSize:20,
        remoteSort: false,
        singleSelect: true,
        sortName: "beginDate",
        sortOrder: 'desc',
        rownumbers: true,
        columns: columns,
        view: $.fn.datagrid.defaults.view
    });*/
    //return cb();
}

Order.parseCzOrder = function (data) {

    var dataList = data.data.list, i = 0, j = 0, orderItems = [], orderExecList = [];
    var oData = [];
    for (; i < dataList.length; i++) {
        oData.push({});
        oData[i].orderStatusCode = dataList[i].orderGroup.orderStatusCode || "";
        oData[i].beginDate = dataList[i].orderGroup.beginDate || "";
        oData[i].endDate = dataList[i].orderGroup.endDate || "";
        oData[i].stopDate = dataList[i].orderGroup.stopDate || "";
        oData[i].createDoctorName = dataList[i].orderGroup.createDoctorName || "";
        oData[i].createDate = dataList[i].orderGroup.createDate || "";
        oData[i].usageName = dataList[i].orderGroup.usageName || "";
        oData[i].stopDoctorName = dataList[i].orderGroup.stopDoctorName || "";
        orderItems = dataList[i].orderGroup.orderItems;
        oData[i].orderItems = [];
        oData[i].dosage = [];
        for (j = 0; j < orderItems.length; j++) {
            oData[i].orderItems.push({
                orderName: orderItems[j].orderName
            });
            oData[i].dosage.push({
                dosage: (orderItems[j].dosage || ""),
                dosageUnit: (orderItems[j].dosageUnit || "")
            });
        }
        orderExecList = dataList[i].orderExecList;
        oData[i].orderExecList = [];
        var execLen = dataList[i]['orderExecList'].length,
            executedNum = 0;
        for (j = 0; j < orderExecList.length; j++) {
            oData[i].orderExecList.push({
                "orderExecId": (orderExecList[j].orderExecId || ""),
                "planDate": (orderExecList[j].planDate || ""),
                "execDate": (orderExecList[j].execDate || ""),
                "execNurseName": (orderExecList[j].execNurseName || ""),
            });
            if (!!orderExecList[j].execNurseName) {
                executedNum += 1;
            }
        }

        if (dataList[i].orderGroup.orderStatusCode !== 'S' && execLen > 0) {
            oData[i].process = executedNum + '/' + execLen;
        } else {
            oData[i].process = '';
        }

        oData[i].planExecTime = dataList[i].planExecTime;
        oData[i].deliverFreq = dataList[i].orderGroup.deliverFreq || "";
        // oData[i].execNurse = dataList[i].orderExecLog.execDate  +","+ dataList[i].orderExecLog.execNurseName || "";
    }
    console.log(oData);
    $('#viewTable').datagrid('loadData', {
        total: oData.length,
        rows: oData
    });
}

Order.parseLzOrder = function (data) {

    var dataList = data.data.list, i = 0, j = 0, orderItems = [], orderExecList = [];
    var oData = [];
    for (; i < dataList.length; i++) {
        oData.push({});
        oData[i].orderStatusCode = dataList[i].orderGroup.orderStatusCode || "";
        oData[i].beginDate = dataList[i].orderGroup.beginDate || "";
        oData[i].endDate = dataList[i].orderGroup.endDate || "";
        oData[i].stopDate = dataList[i].orderGroup.stopDate || "";
        oData[i].createDate = dataList[i].orderGroup.createDate || "";
        oData[i].usageName = dataList[i].orderGroup.usageName || "";
        oData[i].createDoctorName = dataList[i].orderGroup.createDoctorName || "";
        oData[i].stopDoctorName = dataList[i].orderGroup.stopDoctorName || "";
        orderItems = dataList[i].orderGroup.orderItems;
        oData[i].orderItems = [];

        oData[i].dosage = [];
        orderExecList = dataList[i].orderExecList;
        oData[i].orderExecList = [];
        // 执行单进度
        var execLen = dataList[i]['orderExecList'].length,
            executedNum = 0;
        for (j = 0; j < orderExecList.length; j++) {
            oData[i].orderExecList.push({
                "orderExecId": (orderExecList[j].orderExecId || ""),
                "planDate": (orderExecList[j].planDate || ""),
                "execDate": (orderExecList[j].execDate || ""),
                "execNurseName": (orderExecList[j].execNurseName || ""),
            });
            if (!!orderExecList[j].execNurseName) {
                executedNum += 1;
            }
        }

        for (j = 0; j < orderItems.length; j++) {
            oData[i].orderItems.push({
                orderName: orderItems[j].orderName
            });
            oData[i].dosage.push({
                dosage: (orderItems[j].dosage || ""),
                dosageUnit: (orderItems[j].dosageUnit || "")
            });
        }

        if (dataList[i].orderGroup.orderStatusCode !== 'S' && execLen > 0) {
            oData[i].process = executedNum + '/' + execLen;
        } else {
            oData[i].process = '';
        }

        oData[i].planExecTime = dataList[i].planExecTime;
        oData[i].deliverFreq = dataList[i].orderGroup.deliverFreq || "";
        // oData[i].execNurse = dataList[i].orderExecLog.execDate  +","+ dataList[i].orderExecLog.execNurseName || "";
    }
    console.log(oData);
    $('#viewTable').datagrid('loadData', {total: oData.length, rows: oData});
}

/*
 暂时做一个方法用作调用方法
 */
function getPatOrderDetail(pList,isSelectPat) {
    Order.getPatOrderDetail(pList,isSelectPat);
}



