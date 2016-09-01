$(function () {
    var recordDate = new Date().format("yyyy-MM-dd");
    $("#startDate").val(recordDate);
    $("#endDate").val(recordDate);
    $('.content').height($(window).height() - $('.top-tools').height());
    $("#info-tab").datagrid({
        nowrap: false,
        fit: true,
        fitColumns: true,
        columns: [[
            /*{field:'recordDate',width:100,align:'center',title:'执行时间',formatter:function(value,row){
             if(value){
             return new Date(value).format('hh:mm');
             }

             }},*/
            {field: 'items', width: 350, align: 'center', title: '医嘱内容'},
            {field: 'du', width: 100, align: 'right', title: '剂量'},
            {field: 'usageName', width: 80, align: 'center', title: '用法'},
            {field: 'deliverFreq', width: 80, align: 'center', title: '频次'},

            {
                field: 'deliverSpeed', width: 80, align: 'center', title: '滴速', formatter: function (value, row) {
                if (typeof value == 'number') {
                    return value + ' 滴/分';
                }
                return '';
            }
            },
            {
                field: 'residue', width: 60, align: 'center', title: '剩余液量', formatter: function (value, row) {
                if (typeof value == 'number') {
                    return value + 'ml';
                }
                return '';
            }
            },
            {
                field: 'abnormal', width: 150, align: 'center', title: '输液异常', formatter: function (value, row) {
                if (!value) {
                    return '';
                }
                return row.anomalyMsg;
            }
            },
            {
                field: 'status', width: 100, align: 'center', title: '状态', formatter: function (value, row) {
                var str = '';
                switch (value) {
                    case 'N':
                        str = '刚执行';
                        break;
                    case 'I':
                        str = '正巡视';
                        break;
                    case 'P':
                        str = '已经暂停';
                        break;
                    case 'S':
                        str = '终止';
                        break;
                    case 'F':
                        str = '完成';
                        break;
                    default:
                        break;
                }
                return str;
            }
            },
            {field: 'recordNurseName', width: 80, align: 'center', title: '巡视护士'},
            {
                field: 'recordDate', width: 150, align: 'center', title: '巡视时间', formatter: function (value, row) {
                return new Date(value).format('yyyy-MM-dd hh:mm');
            }
            }
        ]]
    });

    search();
});

function search() {
    var url = location.href;
    var patientId = url.substring(url.indexOf('=') + 1, url.length);
    var time = $("#startDate").val();
    $.post(ay.contextPath + '/nur/documents/infusion?patientId=' + patientId + '&queryTime=' + time, function (data) {
        console.log(data);
        var cacheData = data.data.list;
        var infusionData = [];
        var drugItems = null;
        var k = 0;
        for (var i = 0; i < cacheData.length; i++) {
            items = cacheData[i].items;
            var str = '';
            var du = '';
            for (var j = 0; j < items.length; j++) {
                if (j != 0) {
                    str += "<br>";
                    du += "<br>";
                }
                str += items[j].orderName;
                cacheData[i].dosage = items[j].dosage;
                du += cacheData[i].dosage + items[j].dosageUnit;
            }
            /*cacheData[i].recordDate = cacheData[i].records[0].recordDate;
             cacheData[i].deliverSpeed = cacheData[i].records[0].deliverSpeed;
             cacheData[i].abnormal = cacheData[i].records[0].abnormal;
             cacheData[i].anomalyMsg = cacheData[i].records[0].anomalyMsg;
             cacheData[i].recordNurseName = cacheData[i].records[0].recordNurseName;
             cacheData[i].residue = cacheData[i].records[0].residue;
             cacheData[i].status = cacheData[i].records[0].status;
             cacheData[i].recordNurseName = cacheData[i].records[0].recordNurseName;
             */
            records = cacheData[i].records;
            for (var j = 0; j < records.length; j++) {
                var curCacheData = {};

                curCacheData.du = du;
                curCacheData.items = str;
                curCacheData.dosage = cacheData[i].dosage;
                curCacheData.usageName = cacheData[i].usageName;
                curCacheData.deliverFreq = cacheData[i].deliverFreq;
                curCacheData.recordDate = records[j].recordDate;
                curCacheData.deliverSpeed = records[j].deliverSpeed;
                curCacheData.abnormal = records[j].abnormal;
                curCacheData.anomalyMsg = records[j].anomalyMsg;
                curCacheData.recordNurseName = records[j].recordNurseName;
                curCacheData.residue = records[j].residue;
                curCacheData.status = records[j].status;
                curCacheData.recordNurseName = records[j].recordNurseName;

                infusionData.push(curCacheData);
            }
        }

        console.log(infusionData);
        $("#info-tab").datagrid({
            data: infusionData
        });
    });

}