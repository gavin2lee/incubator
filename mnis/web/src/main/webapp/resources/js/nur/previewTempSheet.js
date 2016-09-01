//全局配置
var log = console.log.bind(console);
var allConfig = {
    "title": {
        "name": "深圳市联新医院",
        "top": 25
    },
    "titleType": {
        "name": "体温单",
        "top": 55
    },
    "headInfo": {
        "top": 85,
        "left": 22
    },
    "tConfig": {
        "maxHeight": 970,
        "maxWidth": 735,
        "xOffset": 0.5,
        "yOffset": 0.5,
        "lineColor": "#000",
        "lineUnit": 0,
        "rowUnit": 0,
        "aboveGrid": 5,
        "underGrid": 0,
        "underGridObj": {//底部grid配置对象格式
            "length": 0,
            "str": ""
        },
        "grid": 40,
        "customRowNum": 0,
        "lineSingleNum": 6,
        "lineNum": 8,
        "rowNum": 0,
        "isHasPain": false,
        "painRowNum": 0
    },
    "LODOP": null,
    "temp": 0,
    "strTaskName": 0,
    "tempIndex": null,
    "tableNum": 0
};

var oTemperature = new Map(),
    oCooledTemp = new Map(),
    oPulse = new Map(),
    oHeartRate = new Map(),
    oPain = new Map(),
    oPPD = new Map();

// 日期遮罩
var maskDateArr = [];

/*
 *	itemsStr用于排序  tempsheetItems字段
 *	遍历整个对象最大3层
 */
function getUnderGridObj(itemsStr, items) {
    var oItems = $.parseJSON(items),
        aItems = itemsStr.split(','),
        oUnderGridItems = {},
        len = 0,
        underGridLen = 0,
        str = "";

    var aTempItems1 = null,
        aTempItems2 = null,
        pIndex = 0,
        index = 0,
        nTempLen1 = 0,
        nTempLen2 = 0;

    for (i = 0; i < aItems.length; i++) {
        /*if (aItems[i] == 'pain') {
         continue;
         }*/
        var m = n = 0;
        //如果存在就继续下一个循环
        if (oUnderGridItems[aItems[i]]) {
            continue;
        }
        //index = i == 0 ? 0 : index+=
        //第一层无子节点
        if (oItems[aItems[i]] == 1) {
            len++;
            str += aItems[i] + ",";
            oUnderGridItems[aItems[i]] = {
                "id": len,
                "layer": 1,
                "length": 1,
                "index": index,
                "cLength": 0
            }
            index++;
        }
        //值大于1的表示该项有大于一个字内容 但不为子项。
        else if (oItems[aItems[i]] > 1) {
            len++;
            str += aItems[i] + ",";
            oUnderGridItems[aItems[i]] = {
                "id": len,
                "layer": 1,
                "length": oItems[aItems[i]],
                "colspan": oItems[aItems[i]],
                "index": index,
                "cLength": 0
            }
            index += oItems[aItems[i]];
        }
        else {
            //第一层有子节点
            //增加第一层数据
            len++;
            pIndex = len;

            oUnderGridItems[aItems[i]] = {
                "id": len,
                "layer": 1,
                "length": 0,
                "cLength": 0
            }
            str += aItems[i] + ",";

            console.log(oItems, aItems);

            aTempItems2 = oItems[aItems[i]].split(',');
            nTempLen2 = aTempItems2.length;
            for (n = 0; n < nTempLen2; n++) {

                //第二层没子节点 增加进对象内
                if (oItems[aTempItems2[n]] == 1) {
                    len++;
                    str += aTempItems2[n] + ",";
                    oUnderGridItems[aTempItems2[n]] = {
                        "id": len,
                        "layer": 2,
                        "parent": pIndex,
                        "index": index,
                        "length": 1
                    }
                    oUnderGridItems[aItems[i]].cLength += 1;
                    oUnderGridItems[aItems[i]].length += 1;
                    index++;
                }
                else if (oItems[aTempItems2[n]] > 1) {
                    len++;
                    str += aTempItems2[n] + ",";
                    oUnderGridItems[aTempItems2[n]] = {
                        "id": len,
                        "layer": 2,
                        "parent": pIndex,
                        "length": oItems[aTempItems2[n]],
                        "colspan": oItems[aTempItems2[n]],
                        "index": index,
                        "cLength": 0
                    }
                    index += oItems[aTempItems2[n]];
                    oUnderGridItems[aItems[i]].cLength += 1;
                    oUnderGridItems[aItems[i]].length += oItems[aTempItems2[n]];
                }

                //好像没有了第三层
                //else{
                //第二层有子节点
                /*len++;
                 str +=aTempItems2[n]+",";
                 oUnderGridItems[aTempItems2[n]] ={
                 "id":len,
                 "layer":2,
                 "length":0,
                 "cLength":0,
                 "parent":pIndex
                 }
                 pIndex = len;
                 aTempItems3 = oItems[aTempItems2[n]].split(',');
                 nTempLen3 = aTempItems3.length;
                 //第三层
                 for(m=0;m<nTempLen3;m++){
                 len++;
                 str +=aTempItems3[m]+",";
                 oUnderGridItems[aTempItems3[m]] ={
                 "id":len,
                 "layer":3,
                 "parent":pIndex,
                 "cLength":0,
                 "length":1
                 }

                 }
                 oUnderGridItems[aTempItems2[n]].length=m;
                 oUnderGridItems[aTempItems2[n]].cLength=m;
                 oUnderGridItems[aItems[i]].cLength+=1;
                 oUnderGridItems[aItems[i]].length +=m;		*/
                //}
            }
        }
        underGridLen += oUnderGridItems[aItems[i]].length;
    }
    allConfig.tConfig.underGridObj.length = len;
    allConfig.tConfig.underGridObj.str = str.substring(0, str.length - 1);
    allConfig.tConfig.underGridObj.o = oUnderGridItems;
    allConfig.tConfig.underGrid = underGridLen;

    return oUnderGridItems;
}

function findNotInTest1(t1, t2) {
    var ot = $.parseJSON(t2), str = '', target = '';
    for (var x in ot) {
        if (ot[x].length > 3) {
            str = x;
            target = ot[x].split(',')[0];
        }
    }
    if (str.length != 0 || target.length != 0) {
        t1 = t1.substring(0, t1.indexOf(target)) + str + ',' + t1.substring(t1.indexOf(target), t1.length);
    }
    return t1;
}

function DP(data, config, cb) {
    //console.log(data);
    //页码  // Math.ceil(data.data.bodySignDailyRecordList[0].daysInHospital/7);
    var currDate = new Date(currentDay).getTime();
    var admitDate = new Date(new Date(data.data.patientInfo.inDate).format("yyyy-MM-dd")).getTime();
    if (currDate < admitDate) {
        $("#nurMainFrame").contents().find('#startDate').val(data.data.patientInfo.inDate);
        $("#nurMainFrame").contents().find('#showTempSheetBtn').trigger('click');
        return;
    }
    var tempIndex = (currDate - admitDate) / 86400000;

    allConfig.tempIndex = tempIndex % 7 == 0 ? Math.ceil(tempIndex / 7) + 1 : Math.ceil(tempIndex / 7);


    $("#currentWeek").val(allConfig.tempIndex);
    $("#recordDay").val(currentDay);


    //三级配置文件 优先顺序 user->dept->system;
    var currConfig = config.user ? config.user : config.dept ? config.dept : config.system;
    //设置事件显示分隔符
    var bodTempEventSeparator = currConfig.bodyTempEventSeparator ? currConfig.bodyTempEventSeparator : "–-";
    //根据配置重设allConfig
    var temperatureChart = $.parseJSON(currConfig.temperatureChart),
        itemsArray = temperatureChart.tempsheetItems.split(','),
        timeArr = temperatureChart.times;

    allConfig.title.name = currConfig.hospitalName || allConfig.title.name;

    log('config⬇️');
    log(itemsArray.indexOf('pain'));

    //初始化操作
    allConfig.tConfig.isHasPain = itemsArray.indexOf('pain') !== -1;
    allConfig.tConfig.customRowNum = itemsArray.length;

    itemsArray.indexOf('pain') != -1 ? allConfig.tConfig.customRowNum -= 3 : allConfig.tConfig.customRowNum -= 2;
    allConfig.tConfig.constantRowNum = 2;

    var test1 = temperatureChart.tempsheetItems;//"breath,bloodPress,totalInput,output,stool,urine,other1,weight,skinTest,other2";
    var test2 = currConfig.temperatureChartLabel;//"{\"breath\":1,\"bloodPress\":1,\"totalInput\":1,\"output\":\"stool,urine,other1\",\"stool\":1,\"urine\":1,\"other1\":2,\"weight\":1,\"skinTest\":1,\"other2\":2}"
    //var test1 = "breath,bloodPress,totalInput,output,stool,urine,other1,hh,weight,skinTest,other2";
    //var test2 = "{\"breath\":1,\"bloodPress\":1,\"totalInput\":1,\"output\":\"stool,urine,other1\",\"stool\":2,\"urine\":1,\"other1\":2,\"hh\":\"weight,skinTest\",\"weight\":1,\"skinTest\":1,\"other2\":2}"
    //设置underGridItem
    //做一个配置表的处理
    //带有子项的 项目 在test1中不存在，需通过test2查询到 往test1中添加
    log(test1, test2);

    test1 = findNotInTest1(test1, test2);
    log(test1, test2);
    var oUnderGridItems = getUnderGridObj(test1, test2);

    if (itemsArray.indexOf('pain') != -1) {
        allConfig.tConfig.isHasPain = true;
        allConfig.tConfig.painRowNum = 1;  // 7
    }

    var patientInfo = data.data.patientInfo;
    var showPanel = d3.select("#showPanel");

    //设置体温单适宽显示

    d3.select('.drawTempBox').html('').remove();
    var wrapper = showPanel.append('div').attr('id', 'drawTempBox' + allConfig.tempIndex).attr('class', 'drawTempBox').style('margin', '0 auto').style('width', allConfig.tConfig.maxWidth + 'px');
    wrapper.style('zoom', function () {
        var zm = 1;
        zm = parseInt(showPanel.style('width')) > allConfig.tConfig.maxWidth + 20 ? 0.5 + parseInt(showPanel.style('width')) / (allConfig.tConfig.maxWidth + 20) / 2 : 1;
        return zm;
    });
    wrapper.append('div')
        .style('font-size', '24px')
        .style('text-align', 'center')
        .html(currConfig.hospitalName);
    wrapper.append('div')
        .style('text-align', 'center')
        .style('padding', '3px 0 0')
        .style('font-size', '17px')
        .html('体温单');
    wrapper.append('div')
        .style('width', '100%')
        .style('margin', '5px auto 0')
        .style('border-bottom', '1px solid #000');

    var gender = patientInfo.gender == 'M' ? '男' : '女';
    var age = patientInfo.age ? patientInfo.age : '';
    var inDate = patientInfo.inDate ? patientInfo.inDate.substring(0, 10) : '';
    var headStrHtml = '<table style="width:97%;margin:0 auto;font-size:12px;" cellspacing="0px"><tbody><tr><td width="13.6%">姓名:<span class="info" style="border-bottom:1px solid #000;display: inline-block;">' + patientInfo.name + '</span></td><td width="10.89%">年龄:<span class="info" style="border-bottom:1px solid #000;display: inline-block;">' + age + '</span></td><td width="7.9%">性别:<span class="info" style="border-bottom:1px solid #000;display: inline-block;">' + gender + '</span></td><td width="18.776%">入院日期:<span class="info" style="border-bottom:1px solid #000;display: inline-block;">' + inDate + '</span></td><td>病区:<span class="info" style="border-bottom:1px solid #000;display: inline-block;">' + patientInfo.deptName + '</span></td><td>床号:<span class="info" style="border-bottom:1px solid #000;display: inline-block;">' + patientInfo.bedCode + '</span></td><td>住院号:<span class="info" style="border-bottom:1px solid #000;display: inline-block;">' + patientInfo.inHospNo + '</span></td></tr></tbody></table>';
    wrapper.append('div')
        .style('font-size', '12px')
        .style('margin-top', '5px')
        .html(headStrHtml);

    //增加绘制面板
    var svg = wrapper.append('svg');
    svg.attr('id', 'board')
        .style('width', '100%')
        .style('height', allConfig.tConfig.maxHeight + 'px')
        .style('margin', '10px 0 0');

    //为了规避svg中绘制线条时，由于它绘制的原理产生的BUG，
    //手动将宽高数值设置为整数，并且默认0.5为初始值
    //var gridNum = allConfig.tConfig.grid + allConfig.tConfig.painRowNum,
    var gridNum = allConfig.tConfig.grid,
        svgWidth = parseInt(svg.style('width')),
        svgHeight = parseInt(svg.style('height'));
    allConfig.tConfig.rowNum = allConfig.tConfig.aboveGrid + gridNum + allConfig.tConfig.underGrid * 2;
    allConfig.tConfig.lineUnit = Math.floor(svgWidth / allConfig.tConfig.lineNum / 6);
    allConfig.tConfig.rowUnit = Math.floor(svgHeight / allConfig.tConfig.rowNum);

    var t = {
        "w": allConfig.tConfig.lineUnit * allConfig.tConfig.lineNum * 6,
        "lu": allConfig.tConfig.lineUnit,
        "ln": allConfig.tConfig.lineNum,
        "ru": allConfig.tConfig.rowUnit,
        "rn": allConfig.tConfig.rowNum
    };

    //设置svg居中
    svg.style('margin-left', (allConfig.tConfig.maxWidth - t.w) / 2 + 'px');

    var lData = [];
    //竖线
    for (var i = 0; i <= t.ln; i++) {
        lData.push([[i * t.lu * 6, 0], [i * t.lu * 6, t.ru * t.rn]]);
    }
    //前三个
    for (i = 0; i <= 3; i++) {
        lData.push([[0, i * t.ru], [t.lu * 6 * t.ln, i * t.ru]]);
    }
    //特殊的时间块
    lData.push([[t.lu * 6, 4 * t.ru], [t.w, 4 * t.ru]]);
    lData.push([[0, 5 * t.ru], [t.w, 5 * t.ru]]);

    //前两个特殊竖线
    for (i = 0; i <= 13; i++) {
        lData.push([[t.lu * 6 + i * t.lu * 6 * 0.5, 3 * t.ru], [t.lu * 6 + i * t.lu * 6 * 0.5, 4 * t.ru]]);
    }
    for (i = 0; i < 43; i++) {
        lData.push([[t.lu * 6 + i * t.lu, 4 * t.ru], [t.lu * 6 + i * t.lu, (5 + gridNum) * t.ru]]);
    }
    //左侧中坐标轴线
    lData.push([[Math.ceil(t.lu * 4), t.ru * 5], [Math.ceil(t.lu * 4), t.ru * (gridNum + 5)]]);

    //网格横线
    for (i = 0; i < gridNum; i++) {
        lData.push([[6 * t.lu, (i + 5) * t.ru], [t.w, (i + 5) * t.ru]]);
    }

    //疼痛分隔线
    /*if (allConfig.tConfig.isHasPain) {
     lData.push([[0, (gridNum - 2) * t.ru - 0.5], [8 * 6 * t.lu, (gridNum - 2) * t.ru - 0.5], null, 1.5])
     }*/

    //网格后的行
    var underGridItemLen = 0,
        underGridItemStr = allConfig.tConfig.underGridObj.str.split(','),
        ugiTemp = 0,//缓存上一个节点
        ugic = 0,//子节点长度
        aExcludeArr = ['breath', 'bloodPress', 'skinTest', 'oxygenSaturation', 'pain', 'PPD'],
        aUnderGridOne = [],
        aUnderGridTwo = [];
    for (i = 0; i < underGridItemStr.length; i++) {
        var ugi = null;
        if (oUnderGridItems[underGridItemStr[i]].layer == 1) {
            ugi = oUnderGridItems[underGridItemStr[i]], ugic = 0;
            lData.push([[0, (5 + gridNum + underGridItemLen) * t.ru], [t.w, (underGridItemLen + 5 + gridNum) * t.ru]]);
            if (ugi.length == 1 && aExcludeArr.indexOf(underGridItemStr[i]) < 0) {
                aUnderGridOne.push(underGridItemStr[i]);
            }
            if (ugi.colspan > 1) {
                //lData.push([ [2*t.lu,(5+gridNum+underGridItemLen)*t.ru],[2*t.lu,(5+gridNum+underGridItemLen+ugi.length*2)*t.ru] ]);
                for (var cols = 0; cols < ugi.colspan; cols++) {
                    lData.push([[6 * t.lu, (5 + gridNum + underGridItemLen + 2 + cols * 2) * t.ru], [t.w, (5 + gridNum + underGridItemLen + 2 + cols * 2) * t.ru]]);
                }
                if (aExcludeArr.indexOf(underGridItemStr[i]) < 0) {
                    aUnderGridTwo.push(underGridItemStr[i]);
                }
            }
            else if (ugi.length > 1) {
                lData.push([[2 * t.lu, (5 + gridNum + underGridItemLen) * t.ru], [2 * t.lu, (5 + gridNum + underGridItemLen + ugi.length * 2) * t.ru]]);

            }
            underGridItemLen += ugi.length * 2;
            ugiTemp = ugi.length * 2;
        }
        //第二层行
        else if (oUnderGridItems[underGridItemStr[i]].layer == 2) {
            ugi = oUnderGridItems[underGridItemStr[i]];
            //第二层的间隔
            lData.push([[2 * t.lu, (5 + gridNum + underGridItemLen - ugiTemp + ugic) * t.ru], [6 * t.lu, (5 + gridNum + underGridItemLen - ugiTemp + ugic) * t.ru]]);
            //第二带层子节点
            if (ugi.colspan > 1) {
                for (var cols = 0; cols < ugi.colspan; cols++) {
                    lData.push([[6 * t.lu, (5 + gridNum + underGridItemLen - ugiTemp + 2 + ugic) * t.ru], [t.w, (5 + gridNum + underGridItemLen - ugiTemp + 2 + ugic) * t.ru]]);
                    ugic += 2;
                }
                if (aExcludeArr.indexOf(underGridItemStr[i]) < 0) {
                    aUnderGridTwo.push(underGridItemStr[i]);
                }
            }
            else {
                if (aExcludeArr.indexOf(underGridItemStr[i]) < 0) {
                    aUnderGridOne.push(underGridItemStr[i]);
                }
                lData.push([[2 * t.lu, (5 + gridNum + underGridItemLen - ugiTemp + ugi.length * 2 + ugic) * t.ru], [t.w, (5 + gridNum + underGridItemLen - ugiTemp + ugi.length * 2 + ugic) * t.ru]]);
                ugic += ugi.length * 2;
            }

        }
    }

    lData.push([[0, (5 + gridNum + underGridItemLen) * t.ru], [t.w, (5 + gridNum + underGridItemLen) * t.ru]]);

    //带颜色的线段
    //红色竖线
    for (i = 2; i < 8; i++) {
        lData.push([[i * 6 * t.lu, 0], [i * 6 * t.lu, (gridNum + 5) * t.ru], '#f00']);
    }
    //蓝色横线
    for (i = 1; i < 8; i++) {
        if (i == 5) {
            lData.push([[6 * t.lu, (i * 5 + 5) * t.ru - 0.5], [8 * 6 * t.lu, (i * 5 + 5) * t.ru - 0.5], '#f00', 1.5]);
        }
        else {
            lData.push([[6 * t.lu, (i * 5 + 5) * t.ru - 0.5], [8 * 6 * t.lu, (i * 5 + 5) * t.ru - 0.5], '#00f', 1.5]);
        }
    }
    var breathTimes = timeArr.breath;
    for (i = 0; i < 7; i++) {
        for (tdx = 0; tdx < breathTimes; tdx++) {
            lData.push([[(6 + i * 6 + tdx) * t.lu, (5 + gridNum) * t.ru], [(6 + i * 6 + tdx) * t.lu, (5 + gridNum + 2) * t.ru], '#000', 1]);
        }
    }

    var painTimes = timeArr.pain;
    var painUnit = 6 / painTimes;
    for (i = 0; i < 7; i++) {
        for (tdx = 0; tdx < painTimes; tdx++) {
            lData.push([[(6 + i * 6 + painUnit * tdx) * t.lu, (5 + gridNum + 2) * t.ru], [(6 + i * 6 + painUnit * tdx) * t.lu, (5 + gridNum + 4) * t.ru], '#000', 1]);
        }
    }

    var oxygenTimes = timeArr.oxygenSaturation;
    var oxUnit = 6 / oxygenTimes;
    for (i = 0; i < 7; i++) {
        for (tdx = 0; tdx < oxygenTimes; tdx++) {
            lData.push([[(6 + i * 6 + oxUnit * tdx) * t.lu, (5 + gridNum + 2) * t.ru], [(6 + i * 6 + oxUnit * tdx) * t.lu, (5 + gridNum + 8) * t.ru], '#000', 1]);
        }
    }


    var bloodpressTimes = timeArr.bloodPress,
        bpUnit = 6 / bloodpressTimes;
    for (i = 0; i < 7; i++) {
        for (tdx = 0; tdx < bloodpressTimes; tdx++) {
            lData.push([[(6 + i * 6 + bpUnit * tdx) * t.lu, (5 + gridNum + 2) * t.ru], [(6 + i * 6 + bpUnit * tdx) * t.lu, (5 + gridNum + 6) * t.ru], '#000', 1]);
        }
    }

    drawLine(lData, svg, t);

    /* 表格绘制完成 */

    /* 填充部分固定数据 */
    var tData = [];

    tData.push([[0, 1], [0, 1], 12, '日期'], [[0, 1], [1, 1], 12, '住院日数'], [[0 + 0.1, 1], [2, 1], 12, '手术或产后日数'], [[0, 1], [3, 2], 12, '时间']);
    for (i = 0; i < 14; i++) {
        if (i % 2 == 0) {
            tData.push([[1 + i / 2, 0.5], [3, 1], 12, '上午']);
        }
        else {
            tData.push([[1 + i / 2, 0.5], [3, 1], 12, '下午']);
        }
    }
    var timeTitle = temperatureChart.timeTitle.split(',');
    for (i = 0; i < timeTitle.length; i++) {
        for (var n = 1; n < 8; n++) {
            if (i == 0 || i == (timeTitle.length - 1) || i == (timeTitle.length - 2)) {
                tData.push([[n + i / 6, 1 / 6], [4, 1], 11, timeTitle[i], '#f00']);
            } else {
                tData.push([[n + i / 6, 1 / 6], [4, 1], 11, timeTitle[i]]);
            }
        }
    }
    //左侧坐标轴
    tData.push([[1.5 / 6, 2.2 / 6], [5, 1.5], 12, '脉搏', '#f00']);
    tData.push([[1.5 / 6, 2.5 / 6], [6, 1.5], 12, '(次/分)', '#f00']);
    tData.push([[4 / 6, 2 / 6], [5, 1.5], 12, '体温', '#f00']);
    tData.push([[4.2 / 6, 2 / 6], [6, 1.5], 12, '(℃)', '#f00']);
    //坐标轴数字
    for (i = 0; i < 7; i++) {
        tData.push([[2.5 / 6, 1 / 6], [9 + i * 5 + 0.5, 1], 12, 160 - i * 20 + '', '#f00']);
        tData.push([[4 / 6, 1.5 / 6], [9 + i * 5 + 0.5, 1], 12, (41 - i) + '°', '#000']);
    }
    //几个坐标点的实例
    var tempNum = allConfig.tConfig.grid + 5;
    tData.push([[0, 2 / 6], [tempNum - 8, 1.5], 12, '口表', null]);
    tData.push([[0, 2 / 6], [tempNum - 6.5, 1.5], 12, '腋表', null]);
    tData.push([[0, 2 / 6], [tempNum - 5, 1.5], 12, '肛表', null]);
    tData.push([[0, 2 / 6], [tempNum - 3.5, 1.5], 12, '脉搏', null]);
    tData.push([[0, 2 / 6], [tempNum - 2, 1.5], 12, '心率', null]);

    /*if (allConfig.tConfig.isHasPain) {
     for (i = 0; i < 6; i++) {
     tData.push([[4.5 / 6, 1.5 / 6], [tempNum + 1 + i, 1], 12, 10 - i * 2 + '', null])
     }
     }*/

    //图表点的信息
    var iconInfo = {
        "kw": {
            'name': '口表',
            'style': 1,
            'radius': 5,
            'bgcolor': "#00f",
            "borderColor": "#00f"
        },
        "moren": {
            'name': '腋表',
            'radius': 5,
            'bgcolor': "#00f",
            "borderColor": "#00f",
            "borderWidth": 1
        },
        "yw": {
            'name': '腋表',
            'radius': 5,
            'bgcolor': "#00f",
            "borderColor": "#00f",
            "borderWidth": 1
        },
        "gw": {
            'name': '肛表',
            'style': 2,
            'radius': 5,
            'bgcolor': "#00f",
            "borderColor": "#00f"
        },
        "pulse": {
            'name': '脉搏',
            'style': 1,
            'radius': 5,
            'borderWidth': 1,
            'bgcolor': "#f00",
            "borderColor": "#f00"
        },
        "heartRate": {
            'name': '心率',
            'style': 2,
            'radius': 5,
            'borderWidth': 1,
            'bgcolor': "#f00",
            "borderColor": "#f00"
        }/*,
         "pain": {
         'name': '疼痛',
         'radius': 10,
         'borderWidth': 1,
         'bgcolor': "#000",
         "borderColor": "#000"
         }*/
    };

    var iData = [[], [], [], []];

    svg.append('circle').attr('cx', 2.2 * t.lu + 3).attr('cy', (tempNum - 7) * t.ru - 3).attr('r', 5).attr('fill', 'blue');
    lData.push([[2.2 * t.lu - 2, (tempNum - 5.5) * t.ru + 2], [2.2 * t.lu + 6, (tempNum - 5.5) * t.ru - 7], iconInfo.yw.borderColor, iconInfo.yw.borderWidth]);
    lData.push([[2.2 * t.lu - 2, (tempNum - 5.5) * t.ru - 7], [2.2 * t.lu + 6, (tempNum - 5.5) * t.ru + 2], iconInfo.yw.borderColor, iconInfo.yw.borderWidth]);
    svg.append('circle').attr('cx', 2.2 * t.lu + 3).attr('cy', (tempNum - 4) * t.ru - 3).attr('r', 5).attr('fill', 'none').attr('stroke', 'blue').attr('stroke-width', '1');
    svg.append('circle').attr('cx', 2.2 * t.lu + 3).attr('cy', (tempNum - 2.5) * t.ru - 3).attr('r', 5).attr('fill', 'red');
    svg.append('circle').attr('cx', 2.2 * t.lu + 3).attr('cy', (tempNum - 1) * t.ru - 3).attr('r', 5).attr('fill', '#fff').attr('stroke', 'red').attr('stroke-width', '1');


    //两次运用selectAll时会出现问题
    drawTextAlignCenter(tData, svg, t);

    //oUnderGridItems
    //填入grid下方动态内容
    var aUgiTitle = [], oName = temperatureChart.names, tempLength = 0, breathIndex = 0;
    for (i = 0; i < underGridItemStr.length; i++) {
        var oUgi = oUnderGridItems[underGridItemStr[i]];
        if (oUgi.layer == 1) {
            if (oUgi.colspan > 1) {
                //第一行 带有隐藏字分类但不显示
                tData.push([0.5, [5 + gridNum + tempLength * 2, oUgi.length * 2], 12, oName[underGridItemStr[i]], null]);
            }
            else if (oUgi.length > 1) {
                //第一行带有可显示的子分类
                tData.push([1, [5 + gridNum + tempLength * 2, oUgi.length * 2], 12, oName[underGridItemStr[i]], null, 'tb-rl']);
                tempLength -= oUgi.length;
            }
            else {
                //第一层 无子分类
                tData.push([0.5, [5 + gridNum + tempLength * 2, oUgi.length * 2], 12, oName[underGridItemStr[i]], null]);
            }
        }
        else {
            if (oUgi.colspan > 1) {
                //第二行有隐藏子分类
                tData.push([0.5 + 2, [5 + gridNum + tempLength * 2, oUgi.length * 2], 12, oName[underGridItemStr[i]], null]);
            }
            else {
                //第二行无子分类
                tData.push([0.5 + 2, [5 + gridNum + tempLength * 2, oUgi.length * 2], 12, oName[underGridItemStr[i]], null]);
            }
        }
        tempLength += oUgi.length;
    }
    drawTextCommon(tData, svg, t);

    //alert(allConfig.tConfig.isHasPain);

    /*if (allConfig.tConfig.isHasPain) {
     svg.append('text')
     .attr('x', (6 * t.lu - 24) / 2)
     .attr('y', (tempNum + 0.8) * t.ru)
     .attr('font-size', '14px')
     .attr('writing-mode', 'tb-rl')
     .html('疼痛强度');
     }*/

    /* 固定数据填充结束 */

    /* 绘制数据 */
    log(tUnderGridOneData);
    var bsdrl = data.data.bodySignDailyRecordList,
        rLen = bsdrl.length,
        top3Data = [],
        oBsdrl = null,
        oBsrl = null,
        oEvent = null,
        tEventData = [],
        oSkinTest = null,
        tSkinTestTempData = [],
        tSkinTestData = [],
        bSkinTest = true,
        oBsil = null,
        tUnderGridOneData = [],
        tUnderGridTwoData = [],
        tUnderGridBreathData = [],
        tUnderGridBpData = [],
    // 增加血氧和疼痛在体温单显示  2016-07-07  by gary
        tUnderGridOSData = [],
        tUnderGridPainData = [],
        tUnderGridPPDData = [],
    ////////
        tUnderGridSkinTestData = [],

    // 缓存同一时间的事件
        eventCodeArr = [],
        eventNameArr = [],
        eventCindexArr = [],
        eventIndexArr = [],

        oGridOtherData = new Map(),//表格内非描点数据 绘制在35-34度之间超过省略，
        aGridOtherDataTemp = null,


        itemCode = null,
        mnc = null,//measureNoteCode
        oItem = null,
        showName,
        timeRage = 4 * 3600 * 1000,
    //temperatureTimeArr = [],
        heartRateTimeArr = [],
        pulseTimeArr = [];

    var hasUnderPPD = false;
    var hasUnderBreath = false;
    var hasUnderCooled = false;

    /*
     * 体温单 心率和脉搏连线规则:
     * 1. 心率和脉搏其实是一个东西, 只是测量的部位不同;
     * 2. 心率和心率之间如果有脉搏的数据, 则两个心率之间不相连;
     * 3. 同理, 脉搏和脉搏之间有心率数据, 则两个脉搏之间不相连.
     * 注: 体温无论怎样都要相连.
     * */

    log(tUnderGridOneData);
    tData.push([]);

    log(bsdrl);
    maskDateArr = [];
    /*
     前一周的数据未清空
     */

    oTemperature.clear();
    oHeartRate.clear();
    oPulse.clear();
    oCooledTemp.clear();
    oPPD.clear();


    for (i = 0; i <= rLen; i++) {
        oBsdrl = bsdrl[i];

        if (tSkinTestTempData.length == 1) {
            tData.push([[tSkinTestTempData[0][0] + 1, 1, 15], [tSkinTestTempData[0][1] * 2 + gridNum + 5, 2], 12, tSkinTestTempData[0][2], tSkinTestTempData[0][3]]);
        }
        else if (tSkinTestTempData.length == 2) {
            tData.push([[tSkinTestTempData[0][0] + 1, 1, 15], [tSkinTestTempData[0][1] * 2 + gridNum + 5, 1], 12, tSkinTestTempData[0][2], tSkinTestTempData[0][3]]);
            tData.push([[tSkinTestTempData[1][0] + 1, 1, 15], [tSkinTestTempData[1][1] * 2 + gridNum + 6, 1], 12, tSkinTestTempData[1][2], tSkinTestTempData[1][3]]);
        }
        //日期，住院日数，手术后日数

        if (!oBsdrl) {
            continue;
        }

        maskDateArr.push(oBsdrl.recordDate);

        tData.push([[i + 1, 1], [0, 1], 12, oBsdrl.recordDate]);
        tData.push([[i + 1, 1], [1, 1], 12, oBsdrl.daysInHospital]);
        tData.push([[i + 1, 1], [2, 1], 12, oBsdrl.daysAfterSurgery, '#f00']);

        oBsrl = oBsdrl.bodySignRecordList;

        //重置皮试状态
        bSkinTest = true;
        tSkinTestData = [];
        tSkinTestTempData = [];
        log(oBsrl);
        var ppdRecordDate;
        hasUnderPPD = oBsrl[0] && oBsrl[0].bodySignItemList && oBsrl[0].bodySignItemList.find(function (oBsilItem) {
                ppdRecordDate = new Date(oBsilItem.recordDate).getTime();

                return oBsilItem.bodySignDict.itemCode == 'PPD';
            });
        for (n = 0; n < oBsrl.length; n++) {

            tempLength = 0;

            //event
            oEvent = oBsrl[n].event;
            if (oEvent) {
                //TODO 同一时间点有多个事件
                var existedIndex = $.inArray(oEvent.index, eventIndexArr);
                if (existedIndex < 0) {
                    eventIndexArr.push(oEvent.index);
                    eventCindexArr.push(n);
                } else {

                    if ($.inArray(oBsrl[eventCindexArr[existedIndex]].event.eventCode, eventCodeArr) < 0) {
                        eventCodeArr.push(oBsrl[eventCindexArr[existedIndex]].event.eventCode);
                        eventNameArr.push(oBsrl[eventCindexArr[existedIndex]].event.eventName);
                    } else {
                        if ($.inArray(oEvent.eventCode, eventCodeArr) < 0) {
                            eventCodeArr.push(oEvent.eventCode);
                            eventNameArr.push(oEvent.eventName);
                        }
                    }
                }

                var eStr = oEvent.eventCode == 'ss' ? oEvent.eventName : oEvent.eventName + bodTempEventSeparator + oEvent.chineseEventDate;
                if (!oEvent.eventName) eStr = '';
                var eStrArr = eStr.split('');

                for (var eventI = 0, eventStrLen = eStrArr.length; eventI < eventStrLen; eventI += 1) {
                    tEventData.push([(oEvent.index + 0.5) / 6 + 1, 5 + eventI, 10, eStrArr[eventI], '#f00', 'tb-rl']);
                }
                //tEventData.push([(oEvent.index + 0.5) / 6 + 1, 5, 10, eStr, '#f00', 'tb-rl']);
            }
            oSkinTest = oBsrl[n].skinTestInfo;
            if (oSkinTest && bSkinTest) {
                console.log(oSkinTest, tSkinTestData);
                if (oSkinTest.testResult === 'p') {
                    // 过敏药物加标识(+)
                    showName = oSkinTest.drugName + '(+)';
                    tSkinTestTempData.push([i, oUnderGridItems['skinTest'].index, showName, '#f00']);
                    //tSkinTestData.push([ [i+1,1,10],[oUnderGridItems['skinTest'].index*2+gridNum+5+tSkinTestData.length,1],12,oSkinTest.drugName,'#f00' ]);
                }
                else {
                    showName = oSkinTest.drugName + '(-)';
                    tSkinTestTempData.push([i, oUnderGridItems['skinTest'].index, showName, '#00f']);
                    //tSkinTestData.push([ [i+1,1,10],[oUnderGridItems['skinTest'].index*2+gridNum+5+tSkinTestData.length,1],12,oSkinTest.drugName,'#0f0' ]);
                }
                if (tSkinTestTempData.length == 2) {
                    bSkinTest = false;
                    //tData = tData.concat(tSkinTestData);
                }

            }

            //bodySignItemList
            oBsil = oBsrl[n].bodySignItemList;
            if (oBsil) {

                var hideCount = 0; // 外出的次数
                var bloodCount = 0; // 血压外出／请假次数
                var breathCount = 0;
                var dateTimeCurrent;

                hasUnderBreath = oBsil.find(function (oBsilItem) {
                    return oBsilItem.bodySignDict.itemCode == 'breath';
                });
                hasUnderCooled = oBsil.find(function (oBsilItem) {
                    return oBsilItem.bodySignDict.itemCode == 'coolway';
                });

                for (m = 0; m < oBsil.length; m++) {
                    if (ppdRecordDate + 3 * 3600 * 1000 < new Date(oBsil[m].recordDate).getTime()) {
                        hasUnderPPD = false;
                    }
                    itemCode = oBsil[m].bodySignDict.itemCode;
                    oItem = oUnderGridItems[itemCode];
                    log('真实数据1：', tUnderGridOneData);
                    mnc = oBsil[m].measureNoteCode;

                    log(itemCode + ': ' + oBsil[m].index);

                    if (!oBsil[m].show || oItem && oItem.length > 1 && !oItem.colspan) {
                        //设置不继续条件 当存在子节点并且该节点没有colspan属性时跳过
                        continue;
                    }

                    if (itemCode == 'breath') {

                        if (mnc == 'cbc') {
                            // hideCount += 1;
                            tUnderGridBreathData.push([
                                [(oBsil[m].index) / 6 + 1, 1 / 6],
                                [5 + gridNum, 1],
                                10,
                                '不',
                                '#00f'
                            ]);
                            tUnderGridBreathData.push([
                                [(oBsil[m].index) / 6 + 1, 1 / 6],
                                [6 + gridNum, 1],
                                10,
                                '出',
                                '#00f'
                            ]);
                        }

                        if (mnc == 'tfzhx') {
                            tUnderGridBreathData.push([
                                [(oBsil[m].index) / 6 + 1, 1 / 6],
                                [5 + breathIndex + gridNum, 1],
                                10,
                                Number.isNaN(parseInt(oBsil[m].itemValue)) ? '' : oBsil[m].itemValue,
                                '#00f'
                            ]);
                            // hideCount += 1;
                            // tUnderGridBreathData.push([
                            //     [(oBsil[m].index) / 6 + 1, 1 / 6],
                            //     [gridNum + 5, 1],
                            //     10,
                            //     '停',
                            //     '#00f'
                            // ]);
                            // tUnderGridBreathData.push([
                            //     [(oBsil[m].index) / 6 + 1, 1 / 6],
                            //     [gridNum + 6, 1],
                            //     10,
                            //     '辅',
                            //     '#00f'
                            // ]);
                            if (!hasUnderPPD) {
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [gridNum, 1], 10, '停', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [1 + gridNum, 1], 10, '辅', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [2 + gridNum, 1], 10, '助', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [3 + gridNum, 1], 10, '呼', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [4 + gridNum, 1], 10, '吸', '#00f']);
                            }
                        }

                        if (mnc == 'fzhx') {
                            tUnderGridBreathData.push([
                                [(oBsil[m].index) / 6 + 1, 1 / 6],
                                [5 + breathIndex + gridNum, 1],
                                10,
                                Number.isNaN(parseInt(oBsil[m].itemValue)) ? '' : oBsil[m].itemValue,
                                '#00f'
                            ]);
                            // hideCount += 1;
                            // tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [5 + gridNum, 1], 10, '辅', '#00f']);
                            // tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [6 + gridNum, 1], 10, '助', '#00f']);
                            if (!hasUnderPPD) {
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [gridNum, 1], 10, '辅', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [1 + gridNum, 1], 10, '助', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [2 + gridNum, 1], 10, '呼', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [3 + gridNum, 1], 10, '吸', '#00f']);
                            }
                        }
                        // 体温单修改成统一设置外出／请假后，对非数字的值也要处理；
                        if (['out', 'qj', 'jc'].indexOf(mnc) >= 0 || Number.isNaN(parseInt(oBsil[m].itemValue))) {
                            // if ()
                            breathCount += 1;
                            tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [5 + gridNum, 1], 10, (breathCount > 0 ? '' : oBsil[m].itemValue[0]), '#00f']);
                            tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [6 + gridNum, 1], 10, (breathCount > 0 ? '' : oBsil[m].itemValue[1]), '#00f']);
                        }
                        if (mnc == 'hxj') {
                            tUnderGridBreathData.push([[(oBsil[m].index - 0.18) / 6 + 1, 1 / 6], [5 + gridNum + 0.05, 1], 13, '®', '#00f']);
                            tUnderGridBreathData.push([
                                [(oBsil[m].index) / 6 + 1, 1 / 6],
                                [6 + gridNum, 1],
                                10,
                                oBsil[m].itemValue,
                                '#00f'
                            ]);
                            if (!hasUnderPPD) {
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [gridNum, 1], 10, '呼', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [1 + gridNum, 1], 10, '吸', '#00f']);
                                tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [2 + gridNum, 1], 10, '机', '#00f']);
                            }
                        }
                        if ((mnc == 'moren' || typeof mnc === 'undefined') && !Number.isNaN(parseInt(oBsil[m].itemValue))) {
                            tUnderGridBreathData.push([
                                [(oBsil[m].index) / 6 + 1, 1 / 6],
                                [5 + breathIndex + gridNum, 1],
                                10,
                                oBsil[m].itemValue,
                                '#00f'
                            ]);
                        }
                        if (mnc == 'ts') {
                            tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [5 + gridNum, 1], 10, '特', '#00f']);
                            tUnderGridBreathData.push([[(oBsil[m].index) / 6 + 1, 1 / 6], [6 + gridNum, 1], 10, '殊', '#00f']);
                        }
                        breathIndex = breathIndex == 0 ? 1 : 0;
                    }
                    // pain
                    if (itemCode == 'pain') {

                        tUnderGridPainData.push([
                            [(oBsil[m].index) / painTimes + 1, 1 / 6],
                            [5 + gridNum + 2.5, 1],
                            10,
                            oBsil[m].itemValue,
                            '#00f'
                        ]);
                    }
                    //oxygenSaturation
                    if (itemCode == 'oxygenSaturation') {
                        //bloodpressTimes
                        var oxygenYs = gridNum + 11;

                        if (oxygenTimes == 2) {
                            tUnderGridOSData.push([
                                [oBsil[m].index / oxygenTimes + 1, 1 / oxygenTimes, 7, 0],
                                [oxygenYs, oItem.length * 2],
                                12,
                                oBsil[m].itemValue,
                                '#00f'
                            ]);
                        }
                        else {
                            tUnderGridOSData.push([
                                [oBsil[m].index / oxygenTimes + 1, 1 / oxygenTimes - 1 / 12, 3, -2],
                                [oxygenYs, 1],
                                12,
                                oBsil[m].itemValue,
                                '#00f'
                            ]);
                            tUnderGridOSData.push([[oBsil[m].index / oxygenTimes + 1 + 1 / 7, 1 / oxygenTimes - 1 / 12, 3, -2], [oxygenYs, 1], 12, oBsil[m].itemValue.split('/')[1], '#00f']);
                            lData.push([
                                [
                                    (oBsil[m].index / oxygenTimes + 1 + 1 / 6) * 6 * t.lu - 1 / 2 * t.lu,
                                    (8 + gridNum) * t.ru + 1 / 3 * t.ru],
                                [
                                    (oBsil[m].index / oxygenTimes + 1 + 1 / 6) * 6 * t.lu + 1 / 2 * t.lu,
                                    (8 + gridNum) * t.ru - 1 / 3 * t.ru
                                ]
                            ]);
                        }
                    }
                    if (itemCode == 'bloodPress') {
                        var bloodPressYsUp = gridNum + 9;
                        var boolPressYs = bloodPressYsUp + 1;
                        //bloodpressTimes
                        if (
                            (mnc == 'sz' && !Number.isNaN(parseInt(oBsil[m].itemValue))) ||
                            (mnc == 'moren' && !Number.isNaN(parseInt(oBsil[m].itemValue)))
                        ) {
                            if (bloodpressTimes == 2) {
                                tUnderGridBpData.push([
                                    [oBsil[m].index / bloodpressTimes + 1, 1 / bloodpressTimes, 7, 0],
                                    [bloodPressYsUp, oItem.length * 2],
                                    12,
                                    oBsil[m].itemValue,
                                    '#00f'
                                ]);
                            }
                            else {
                                tUnderGridBpData.push([
                                    [oBsil[m].index / bloodpressTimes + 1, 1 / bloodpressTimes - 1 / 12, 3, -2],
                                    [bloodPressYsUp, 1],
                                    12,
                                    oBsil[m].itemValue.split('/')[0],
                                    '#00f'
                                ]);
                                tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1 + 1 / 7, 1 / bloodpressTimes - 1 / 12, 3, -2], [boolPressYs, 1], 12, oBsil[m].itemValue.split('/')[1], '#00f']);
                                lData.push([[(oBsil[m].index / bloodpressTimes + 1 + 1 / 6) * 6 * t.lu - 1 / 2 * t.lu, (boolPressYs) * t.ru + 1 / 3 * t.ru], [(oBsil[m].index / bloodpressTimes + 1 + 1 / 6) * 6 * t.lu + 1 / 2 * t.lu, (boolPressYs) * t.ru - 1 / 3 * t.ru]]);
                            }
                        }
                        if (mnc == 'xz' && !Number.isNaN(parseInt(oBsil[m].itemValue))) {
                            // lData.push([[(6 + i * 6 + bpUnit * tdx) * t.lu, (5 + gridNum + 2) * t.ru], [(6 + i * 6 + bpUnit * tdx) * t.lu, (5 + gridNum + 6) * t.ru], '#000', 1]);
                            if (bloodpressTimes == 2) {
                                tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1, 1 / bloodpressTimes, 7, 0], [bloodPressYsUp, 1], 12, '下肢', '#00f']);
                                tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1, 1 / bloodpressTimes, 7, 0], [boolPressYs, 1], 12, oBsil[m].itemValue, '#00f']);
                            }
                            else {
                                tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1 - 1 / 24, 1 / bloodpressTimes - 1 / 12, 3, -2], [bloodPressYsUp, 2], 12, '下', '#00f']);
                                tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1 + 1 / 7, 1 / bloodpressTimes - 1 / 12, 3, -2], [bloodPressYsUp, 1], 12, oBsil[m].itemValue.split('/')[0], '#00f']);
                                tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1 + 1 / 7, 1 / bloodpressTimes - 1 / 12, 3, -2], [boolPressYs, 1], 12, oBsil[m].itemValue.split('/')[1], '#00f']);
                                lData.push([[(oBsil[m].index / bloodpressTimes + 1 + 1 / 6) * 6 * t.lu - 1 / 5 * t.lu, (8 + gridNum) * t.ru + 1 / 4 * t.ru], [(oBsil[m].index / bloodpressTimes + 1 + 1 / 6) * 6 * t.lu + t.lu, (boolPressYs) * t.ru - 1 / 8 * t.ru]]);
                                //add / line

                            }
                        }
                        if (bloodpressTimes == 3 && ['cbc'].indexOf(mnc) >= 0) {
                            tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1, 1 / bloodpressTimes, Math.ceil(11 / bloodpressTimes)], [bloodPressYsUp, oItem.length * 2], 10, '不出', '#00f']);
                            continue;
                        }

                        if (['cbc', 'ts', 'kn', 'jc'].indexOf(mnc) >= 0) {

                            tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1, 1 / bloodpressTimes, Math.ceil(11 / bloodpressTimes), -1], [bloodPressYsUp, oItem.length * 2], 10, oBsil[m].itemValue, '#00f']);
                        }

                        if (['out', 'qj'].indexOf(mnc) >= 0 || Number.isNaN(parseInt(oBsil[m].itemValue))) {
                            bloodCount += 1;

                            tUnderGridBpData.push([[oBsil[m].index / bloodpressTimes + 1, 1 / bloodpressTimes, Math.ceil(11 / bloodpressTimes), -1], [bloodPressYsUp, oItem.length * 2], 10, (bloodCount > 0 ? '' : oBsil[m].itemValue), '#00f']);
                        }
                    }
                    if ('temperature' == itemCode) {
                        dateTimeCurrent = new Date(oBsil[m].recordDate).getTime();
                        var tempText = mnc == 'yw' || mnc == 'moren' ? 'yw' : mnc;
                        //aGridOtherData
                        if (oBsil[m].itemValue <= 34) {
                            continue;
                        }
                        if (['bs', 'out', 'qj', 'jc', 'ts'].indexOf(mnc) >= 0 || Number.isNaN(parseInt(oBsil[m].itemValue))) {
                            if (['out', 'qj', 'jc'].indexOf(mnc) >= 0) {
                                hideCount += 1;
                            }
                            aGridOtherDataTemp = oGridOtherData.get(oBsil[m].index);
                            if (aGridOtherDataTemp) {
                                aGridOtherDataTemp.push([(hideCount > 1 ? '' : oBsil[m].itemValue), '#00f']);
                            }
                            else {
                                oGridOtherData.put(oBsil[m].index, [[(hideCount > 1 ? '' : oBsil[m].itemValue), '#00f']]);
                            }
                            // 断点 没有实际度数不连线
                            oTemperature.put(oBsil[m].index, [0, 'break']);
                            continue;
                        } else {

                            if ('yw' == mnc || 'moren' == mnc) {
                                addXIcon([oBsil[m].index + 6.5, (42 - oBsil[m].itemValue) / 0.2 + 5], lData, t, iconInfo, 'yw');
                            } else {
                                iData.push([oBsil[m].index + 6.5, (42 - oBsil[m].itemValue) / 0.2 + 5, iconInfo[tempText].radius, iconInfo[tempText].style, iconInfo[tempText].borderColor, tempText]);
                            }
                        }

                        oTemperature.put(oBsil[m].index, [oBsil[m].itemValue, tempText]);
                    }
                    if ('cooledTemperature' == itemCode) {
                        oCooledTemp.put(oBsil[m].index, [oBsil[m].itemValue, 'cooled']);
                    }

                    if ('coolway' == itemCode) {
                        // 有降温方式的将降温方式文字写在35度下  2016-07-07  by gary
                        if (!hasUnderBreath && !hasUnderPPD) {
                            aGridOtherDataTemp = oGridOtherData.get(oBsil[m].index);
                            if (aGridOtherDataTemp) {
                                aGridOtherDataTemp.push([oBsil[m].itemValue, '#00f']);
                            } else {
                                oGridOtherData.put(oBsil[m].index, [[oBsil[m].itemValue, '#00f']]);
                            }
                        }
                    }
                    if ('pulse' == itemCode) {
                        dateTimeCurrent = new Date(oBsil[m].recordDate).getTime();
                        //debugger;
                        if (typeof mnc !== 'undefined' && (['cbc', 'out', 'qj', 'jc', 'ts'].indexOf(mnc) >= 0 || Number.isNaN(parseInt(oBsil[m].itemValue)) )) {
                            if (['out', 'qj', 'jc'].indexOf(mnc) >= 0 || Number.isNaN(parseInt(oBsil[m].itemValue))) {
                                hideCount += 1;
                            }
                            aGridOtherDataTemp = oGridOtherData.get(oBsil[m].index);
                            if (aGridOtherDataTemp) {
                                aGridOtherDataTemp.push([(hideCount > 1 ? '' : oBsil[m].itemValue), '#00f']);
                            } else {
                                oGridOtherData.put(oBsil[m].index, [[(hideCount > 1 ? '' : oBsil[m].itemValue), '#00f']]);
                            }

                            // 断点 没有实际数字不连线
                            oPulse.put(oBsil[m].index, [0, 'break']);
                        } else {
                            if (oBsil[m].itemValue <= 20) {
                                continue;
                            }
                            // 2016-05-25 有时间间隔不相连／之间有心率不相连
                            if (pulseTimeArr.length > 0) {
                                var dateTimePrev = new Date(pulseTimeArr[pulseTimeArr.length - 1][1]).getTime();
                                var existHeart;

                                if (dateTimeCurrent - dateTimePrev > timeRage) {
                                    existHeart = heartRateTimeArr.filter(function (item) {
                                        var _time = new Date(item[1]).getTime();

                                        if (_time > dateTimePrev && _time < dateTimeCurrent) {
                                            return true;
                                        }
                                    });
                                    if (existHeart.length > 0) {
                                        // 为使一个点与下一点不相连，在两点之间插一个无用的点，用来打断连接。
                                        oPulse.put(Number(pulseTimeArr[pulseTimeArr.length - 1][0]) + 1, [0, 'break']);
                                    }
                                } else {
                                    log('错误？', oBsil[m].itemValue);
                                    // iData.push([oBsil[m].index + 6.5, (180 - Number(oBsil[m].itemValue)) / 4 + 5, iconInfo.pulse.radius, iconInfo.pulse.style, iconInfo.pulse.bgcolor, 'pulse']);
                                }
                                //iData.push([oBsil[m].index + 6.5, (180 - Number(oBsil[m].itemValue)) / 4 + 5, iconInfo.pulse.radius, iconInfo.pulse.style, iconInfo.pulse.bgcolor, 'pulse']);
                            } else {
                                log('错误？', oBsil[m].itemValue);
                            }
                            oPulse.put(oBsil[m].index, [oBsil[m].itemValue, 'pulse']);
                            iData.push([oBsil[m].index + 6.5, (180 - Number(oBsil[m].itemValue)) / 4 + 5, iconInfo.pulse.radius, iconInfo.pulse.style, iconInfo.pulse.bgcolor, 'pulse']);
                        }
                        pulseTimeArr.push([oBsil[m].index, oBsil[m].recordDate]);
                    }

                    // PPD 显示在35度以下   2016-07-11  by gary
                    if (itemCode === 'PPD') {
                        /*
                        *
                        * */
                        var hasPlus = oBsil[m].itemValue.indexOf('+') >= 0;
                        var itemValueTemp = ['P', 'P', 'D'].concat(oBsil[m].itemValue.split(''));

                        itemValueTemp.forEach(function (ppdItem, ppdIndex) {
                         tUnderGridPPDData.push([
                             [(oBsil[m].index) / 6 + 1, 1 / 6],
                             [(function () {

                                 if (itemValueTemp.length > 5 && ppdIndex > 0) {
                                     return gridNum + ppdIndex * 0.8 - 0.5;
                                 }

                                 return ppdIndex + gridNum - 0.5;

                             })(), 1],
                             10,
                             ppdItem,
                             (hasPlus ? '#f00' : '#00f'),
                             null,
                             null,
                             null,
                             '90'
                         ]);
                         });
                        /*aGridOtherDataTemp = oGridOtherData.get(oBsil[m].index);
                        if (aGridOtherDataTemp) {
                            aGridOtherDataTemp.push(['PPD' + oBsil[m].itemValue, '#00f']);
                        } else {
                            oGridOtherData.put(oBsil[m].index, [['PPD' + oBsil[m].itemValue, '#00f']]);
                        }*/
                        //aGridOtherDataTemp.put(oBsil[m].index, [oBsil[m].itemValue, '#00f', 'ppd']);
                    }

                    if ('heartRate' == itemCode) {
                        dateTimeCurrent = new Date(oBsil[m].recordDate).getTime();

                        if (['cbc', 'out', 'qj', 'jc', 'ts'].indexOf(mnc) >= 0 || Number.isNaN(parseInt(oBsil[m].itemValue))) {
                            if (['out', 'qj', 'jc'].indexOf(mnc) >= 0 || Number.isNaN(parseInt(oBsil[m].itemValue))) {
                                hideCount += 1;
                            }
                            aGridOtherDataTemp = oGridOtherData.get(oBsil[m].index);
                            if (aGridOtherDataTemp) {
                                aGridOtherDataTemp.push([(hideCount > 1 ? '' : oBsil[m].itemValue), '#00f']);
                            }
                            else {
                                oGridOtherData.put(oBsil[m].index, [[(hideCount > 1 ? '' : oBsil[m].itemValue), '#00f']]);
                            }

                            // 断点 没有实际数字不连线
                            oHeartRate.put(oBsil[m].index, [0, 'break']);
                        } else {
                            if (oBsil[m].itemValue <= 20) {
                                continue;
                            }

                            // 2016-05-25 有时间间隔不相连／之间有脉搏不相连
                            if (heartRateTimeArr.length > 0) {
                                var dateTimePrev = new Date(heartRateTimeArr[heartRateTimeArr.length - 1][1]).getTime();
                                var existPulse;

                                if (dateTimeCurrent - dateTimePrev > timeRage) {

                                    existPulse = pulseTimeArr.filter(function (item) {
                                        var _time = new Date(item[1]).getTime();

                                        if (_time > dateTimePrev && _time < dateTimeCurrent) {
                                            return true;
                                        }
                                    });
                                    if (existPulse.length > 0) {
                                        // 为使一个点与下一点不相连，在两点之间插一个无用的点，用来打断连接。
                                        oHeartRate.put(Number(heartRateTimeArr[heartRateTimeArr.length - 1][0]) + 1, [0, 'break']);
                                        //oHeartRate.put(oBsil[m].index, [oBsil[m].itemValue, 'heartRate']);
                                    }
                                }
                                //iData.push([oBsil[m].index + 6.5, (180 - Number(oBsil[m].itemValue)) / 4 + 5, iconInfo.heartRate.radius, iconInfo.heartRate.style, iconInfo.heartRate.borderColor, 'heartRate']);
                            } else {

                                log('错误？', oBsil[m].itemValue);
                            }
                            oHeartRate.put(oBsil[m].index, [oBsil[m].itemValue, 'heartRate']);
                            iData.push([oBsil[m].index + 6.5, (180 - Number(oBsil[m].itemValue)) / 4 + 5, iconInfo.heartRate.radius, iconInfo.heartRate.style, iconInfo.heartRate.borderColor, 'heartRate']);
                            // oHeartRate.put(oBsil[m].index, [oBsil[m].itemValue, 'heartRate']);
                            // iData.push([oBsil[m].index + 6.5, (180 - oBsil[m].itemValue) / 4 + 5, iconInfo.heartRate.radius, iconInfo.heartRate.style, iconInfo.heartRate.borderColor, 'heartRate']);
                        }

                        heartRateTimeArr.push([oBsil[m].index, oBsil[m].recordDate]);
                    }
                    /*if (itemCode === 'OtherOne' || itemCode === 'OtherTwo') {
                     tUnderGridOneData.push([[oBsil[m].index + 1, 1, 11], [oItem.index * 2 + gridNum + 5, oItem.length * 2], 12, oBsil[m]['measureNoteName'] + ':' + oBsil[m].itemValue, '#00f']);
                     }*/


                    /*if (aUnderGridTwo.indexOf(itemCode) >= 0) {
                     //tUnderGridOneData.push([ [oBsil[m].index+1,1,11],[oItem.index*2+gridNum+5,oItem.length*2],12,oBsil[m].itemValue ]);
                     }*/

                    if (aUnderGridOne.indexOf(itemCode) >= 0) {
                        var sValue = '';
                        var unitDataArr = ['OtherTwo', 'OtherOne', 'otherOutput'];

                        if (itemCode === 'OtherOne' ||
                            itemCode === 'OtherTwo' ||
                            itemCode === 'otherOutput' ||
                            itemCode === 'urine'
                        ) {
                            if (oBsil[m]['bodySignDict'] &&
                                (unitDataArr.indexOf(oBsil[m]['bodySignDict']['itemCode']) >= 0)) {
                                var isNoUnit = noUnitArr.indexOf(oBsil[m]['measureNoteCode']) >= 0;

                                sValue = oBsil[m].itemValue + (oBsil[m].unit && oBsil[m].unit !== '' && !oBsil[m].itemValue.match(/(\d+)(ml|cm|kg)/g) && !isNoUnit ? oBsil[m].unit : '');
                            } else {
                                if (itemCode === 'urine' && oBsil[m].measureNoteCode == 'dn') {
                                    sValue = oBsil[m].itemValue + '/c';
                                } else {
                                    sValue = oBsil[m].itemValue + (oBsil[m].ryHourDiff ? oBsil[m].ryHourDiff : '');
                                }
                            }
                            tUnderGridOneData.push([
                                [oBsil[m].index + 1, 1, 11],
                                [oItem.index * 2 + gridNum + 5, oItem.length * 2],
                                12,
                                (function () {
                                    var labelArr = ['默认', '特殊值', '特殊治疗', '失禁'];
                                    var codeArr = ['moren', 'ts', 'tszl', 'tszlone', 'tszltwo', 'sj', 'dn'];

                                    if (!oBsil[m].measureNoteCode ||
                                        (oBsil[m].measureNoteCode && codeArr.indexOf(oBsil[m].measureNoteCode) >= 0)) {
                                        return '';
                                    } else {
                                        return oBsil[m].measureNoteName + ':'
                                    }
                                    //(oBsil[m]['measureNoteName'] == '默认' || oBsil[m]['measureNoteName'] == '特殊值' || oBsil[m]['measureNoteName'] == '特殊治疗' || oBsil[m]['measureNoteName'] == '失禁' ? '' : oBsil[m]['measureNoteName'] + ':'),
                                })(),
                                '#00f',
                                sValue
                            ]);
                        } else {
                            sValue = oBsil[m].itemValue + (oBsil[m].ryHourDiff ? oBsil[m].ryHourDiff : '');
                            tUnderGridOneData.push([[oBsil[m].index + 1, 1, 11], [oItem.index * 2 + gridNum + 5, oItem.length * 2], 12, sValue, '#00f']);
                        }
                    }

                    //tempLength += oItem.length;
                }
            }

        }
        //绘制表格 非坐标点数据
        /*
         ay.getInterceptedStr(d[3],d[0][2]);
         ay.getStrActualLen
         */
        /*var aGridOtherDataTemp = oGridOtherData.keys(),oGodt=null;
         for(var idx=0;idx<aGridOtherDataTemp.length;idx++){
         oGodt = oGridOtherData.get(aGridOtherDataTemp[idx]);

         }*/
    }

    //log('真实数据：' + tUnderGridOneData[1][3], tUnderGridOneData);


    //绘制非坐标点数据
    var aGridOtherDataTemp = oGridOtherData.keys(), aGridOther, goIndex = 0;
    for (i = 0; i < aGridOtherDataTemp.length; i++) {
        var godtLen = 0,
            yPoint;
        aGridOther = oGridOtherData.get(aGridOtherDataTemp[i]);
        for (n = 0; n < aGridOther.length; n++) {
            godtLen += aGridOther[n][0].length;
        }
        log(godtLen);

        yPoint = godtLen > 4 ? [allConfig.tConfig.aboveGrid + allConfig.tConfig.grid + goIndex - 5, 1] :
            [allConfig.tConfig.aboveGrid + allConfig.tConfig.grid + goIndex - 6, 1];

        if (godtLen > 5) {
            var arrTemp;

            for (n = 0; n < aGridOther.length; n++) {
                //[x,xs,len,letter-spacing],[y,ys],font-size,string,color]
                var ppdCount = 0;
                for (m = 0; m < aGridOther[n][0].length; m++) {

                    if (goIndex >= 5) {
                        continue;
                    }

                    arrTemp = [
                        [aGridOtherDataTemp[i] / 6 + 1, 1 / 6],
                        [allConfig.tConfig.aboveGrid + allConfig.tConfig.grid + goIndex - 5, 1],
                        12,
                        aGridOther[n][0][m],
                        aGridOther[n][1]
                    ];

                    // PPD皮试不用一个字一个格子, 单独处理
                    if (aGridOther[n][0].indexOf('PPD') >= 0) {
                        if (ppdCount === 0) {
                            goIndex = 0;
                            arrTemp[1] = [allConfig.tConfig.aboveGrid + allConfig.tConfig.grid + goIndex - 5, 1];
                        }
                        ppdCount += 1;
                        arrTemp[1][0] = arrTemp[1][0] - 0.5;
                        arrTemp[0] = [aGridOtherDataTemp[i] + 1, 1];
                        arrTemp[7] = 'PPD';
                        // 囧rz PPD三个字旋转90度显示, 我也是醉了
                        arrTemp[8] = '90';
                        tData.push(arrTemp);
                        goIndex += 0.8;
                    } else {
                        tData.push(arrTemp);
                        goIndex++;
                    }
                }
            }
        }
        else {
            goIndex = 3 - godtLen;
            // 四个字顶格，5个刚好
            yPoint = godtLen > 3 ? godtLen : 6;
            for (n = 0; n < aGridOther.length; n++) {
                ppdCount = 0;

                for (m = 0; m < aGridOther[n][0].length; m++) {
                    if (goIndex >= 5) {
                        continue;
                    }
                    arrTemp = [[aGridOtherDataTemp[i] / 6 + 1, 1 / 6], [allConfig.tConfig.grid + m, 1], 12, aGridOther[n][0][m], aGridOther[n][1]];
                    //allConfig.tConfig.grid + goIndex - yPoint?
                    // PPD皮试不用一个字一个格子, 单独处理
                    if (aGridOther[n][0].indexOf('PPD') >= 0) {
                        if (ppdCount === 0) {
                            goIndex = 0;
                        }
                        ppdCount += 1;
                        arrTemp[1][0] = arrTemp[1][0] - 0.5;
                        arrTemp[0] = [aGridOtherDataTemp[i] + 1, 1];
                        arrTemp[7] = 'PPD';
                        // 囧rz PPD三个字旋转90度显示, 我也是醉了
                        arrTemp[8] = '90';
                        tData.push(arrTemp);
                        goIndex += 0.8;
                    } else {
                        tData.push(arrTemp);
                        goIndex++;
                    }
                }
            }
        }
        goIndex = 0;
    }

    //心率与脉搏不一致
    var polygon = [], aHeartRateLine = [], aPulseLine = [],
        aHeartRate = oHeartRate.keys(),
        aPulse = oPulse.keys();


    for (i = 0; i < aHeartRate.length; i++) {
        if (i == 0) {
            polygon.push(aHeartRate[i]);
            continue;
        }

        if (i == aHeartRate.length - 1) {
            if (Math.ceil((aHeartRate[i] + 1) / 6) - Math.ceil((aHeartRate[i - 1] + 1) / 6) <= 1) {
                polygon.push(aHeartRate[i]);
            }
            aHeartRateLine.push(polygon);
            polygon = [];
        }
        else if (Math.ceil((aHeartRate[i] + 1) / 6) - Math.ceil((aHeartRate[i - 1] + 1) / 6) <= 1) {
            polygon.push(aHeartRate[i]);
        }
        else {
            if (polygon.length > 1)aHeartRateLine.push(polygon);
            polygon = [aHeartRate[i]];
        }
    }
    for (i = 0; i < aPulse.length; i++) {
        if (i == 0) {
            polygon.push(aPulse[i]);
            continue;
        }

        if (i == aPulse.length - 1) {
            if (Math.ceil((aPulse[i] + 1) / 6) - Math.ceil((aPulse[i - 1] + 1) / 6) <= 1) {
                polygon.push(aPulse[i]);
            }
            aPulseLine.push(polygon);
            polygon = [];
        }
        else if (Math.ceil((aPulse[i] + 1) / 6) - Math.ceil((aPulse[i - 1] + 1) / 6) <= 1) {
            polygon.push(aPulse[i]);
        }
        else {
            if (polygon.length > 1) {
                aPulseLine.push(polygon);
            }
            polygon = [aPulse[i]];
        }
    }

    for (i = 0; i < aHeartRateLine.length; i++) {
        for (j = 0; j < aPulseLine.length; j++) {
            polygon.push(getSameRange(aHeartRateLine[i], aPulseLine[j], t));
        }
    }

    // 脉搏与心率部分的阴影
    /*svg.selectAll('polygon')
     .data(polygon)
     .enter().append('polygon')
     .attr('points', function (d) {
     if (d.length == 0) {
     return '';
     }
     var str = '';
     for (var i = 0; i < d.length; i++) {
     str += d[i][0] + ',' + d[i][1] + ' '
     }
     return str;
     })
     .attr('opacity', 0.5)
     .attr('fill', 'red')
     .attr('stroke-width', 'none');*/


    //连线处理
    var aTemperature = oTemperature.keys();
    var v1, v2;
    for (i = 0; i < aTemperature.length; i++) {
        if (i == 0 || aTemperature.length == 1) {
            continue;
        }
        v1 = oTemperature.get(aTemperature[i - 1]);
        v2 = oTemperature.get(aTemperature[i]);

        if (v1[1] === 'break' || v2[1] === 'break') {
            continue;
        }

        if (parseInt((aTemperature[i] + 6) / 6) - parseInt((aTemperature[i - 1] + 6) / 6) <= 1) {
            lData.push([[(aTemperature[i - 1] + 6.5) * t.lu, ((42 - v1[0]) / 0.2 + 5) * t.ru], [(aTemperature[i] + 6.5) * t.lu, ((42 - v2[0]) / 0.2 + 5) * t.ru], '#00f', 1]);
        }
    }
    for (i = 0; i < aHeartRate.length; i++) {
        if (i == 0 || aHeartRate.length == 1) {
            continue;
        }
        v1 = oHeartRate.get(aHeartRate[i - 1]);
        v2 = oHeartRate.get(aHeartRate[i]);

        if (v1[1] === 'break' || v2[1] === 'break') {
            continue;
        }

        if (parseInt((aHeartRate[i] + 6) / 6) - parseInt((aHeartRate[i - 1] + 6) / 6) <= 1) {
            lData.push([[(aHeartRate[i - 1] + 6.5) * t.lu, ((180 - v1[0]) / 4 + 5) * t.ru], [(aHeartRate[i] + 6.5) * t.lu, ((180 - v2[0]) / 4 + 5) * t.ru], '#f00', 1]);
        }
    }
    for (i = 0; i < aPulse.length; i++) {
        if (i == 0 || aPulse.length == 1) {
            continue;
        }
        v1 = oPulse.get(aPulse[i - 1]);
        v2 = oPulse.get(aPulse[i]);

        if (v1[1] === 'break' || v2[1] === 'break') {
            continue;
        }

        if (parseInt((aPulse[i] + 6) / 6) - parseInt((aPulse[i - 1] + 6) / 6) <= 1) {
            lData.push([[(aPulse[i - 1] + 6.5) * t.lu, ((180 - v1[0]) / 4 + 5) * t.ru], [(aPulse[i] + 6.5) * t.lu, ((180 - v2[0]) / 4 + 5) * t.ru], '#f00', 1]);
        }
    }
    var aCooled = oCooledTemp.keys();
    var lDotted = [];
    for (i = 0; i < aCooled.length; i++) {
        v1 = oTemperature.get(aCooled[i]);
        v2 = oCooledTemp.get(aCooled[i]);
        if (v1 && v2[0] !== '' && v2[0] !== '0') {
            iData.push([aCooled[i] + 6.5, (42 - v2[0]) / 0.2 + 5, 5, 2, '#f00']);
            lDotted.push([[(aCooled[i] + 6.5) * t.lu, ((42 - v1[0]) / 0.2 + 5) * t.ru], [(aCooled[i] + 6.5) * t.lu, ((42 - v2[0]) / 0.2 + 5) * t.ru], '#f00', 1]);
        }
    }

    log(tUnderGridOneData);


    drawTextAlignCenter(tData, svg, t);
    //体温单事件数据
    tData = tData.concat(tEventData);
    drawVerticalText(tData, svg, t);
    //绘制皮试数据
    //drawTextAlignCenter(tData,svg,t);
    //undergridOne 网格下无子节点的节点数据和无子节点的二级节点
    tData = tData.concat(tUnderGridOneData);
    drawTextAlignCenter(tData, svg, t);
    //绘制呼吸数据
    tData = tData.concat(tUnderGridBreathData);
    drawTextAlignCenter(tData, svg, t);
    // 疼头痛数据
    tData = tData.concat(tUnderGridPainData);
    drawTextAlignCenter(tData, svg, t);
    //绘制血压数据
    tData = tData.concat(tUnderGridBpData);
    drawTextAlignCenter(tData, svg, t);

    tData = tData.concat(tUnderGridPPDData);
    drawTextAlignCenter(tData, svg, t);

    tData = tData.concat(tUnderGridOSData);
    drawTextAlignCenter(tData, svg, t);

    drawLine(lData, svg, t);
    lData = lData.concat(lDotted);
    drawDottedLine(lData, svg, t);
    drawIcon(iData, svg, t);

    log(tData);

    //表格内的特殊事件
    //drawGridOther(oGridOtherDatasvg,t);
    /* 绘制数据结束 */

    wrapper.append('div')
        .style('font-size', '12px')
        .style('text-align', 'center')
        .html('第&nbsp;&nbsp;' + allConfig.tempIndex + '&nbsp;&nbsp;周');

    oTemperature.clear();
    oHeartRate.clear();
    oPulse.clear();

    // 添加遮罩 双击后对应的日期
    var $maskDate = /*$('#showPanel').next()*/$('<div class="mask-date-wrapper"></div>'),
        dateMaskHtmlArr = [],
        $date = new Date(maskDateArr[0]),
        $year = $date.getFullYear(),
        $month = $date.getMonth() + 1;


    maskDateArr.forEach(function (dateItem, i) {
        var _date = dateItem;

        if (i > 0) {
            if (dateItem.length === 5) {
                _date = new Date($year + '-' + dateItem).format('yyyy-MM-dd');

                $month = dateItem.substr(0, 2);
            } else {
                _date = new Date($year + '-' + $month + '-' + dateItem).format('yyyy-MM-dd');
            }
        }
        dateMaskHtmlArr.push('<div class="fl" style="width: 90px;height: 100%;" data-date="' + _date + '"></div>');
    });


    $maskDate.html(dateMaskHtmlArr.join(''));
    $('.drawTempBox').append($maskDate);

}

/*
 获取比较线段的重叠区域 h:heartRate[]  p:pulse[]
 */
function getSameRange(h, p, t) {

    var s = 0, e = 0, arr = [];
    if (h[1] <= p[0] || h[0] >= p[p.length - 1]) {
        return [];
    }

    if (h[0] <= p[0]) {
        s = p[0];
    }
    else {
        s = h[0];
    }
    if (h[h.length - 1] >= p[p.length - 1]) {
        e = p[p.length - 1];
    }
    else {
        e = h[h.length - 1];
    }

    for (var i = 0; i < h.length; i++) {
        if (h[i] >= s && h[i] <= e) {
            arr.push([(h[i] + 6.5) * t.lu, ((180 - oHeartRate.get(h[i])[0]) / 4 + 5) * t.ru])
        }
    }
    for (i = p.length - 1; i >= 0; i--) {
        if (p[i] <= e && p[i] >= s) {
            arr.push([(p[i] + 6.5) * t.lu, ((180 - oPulse.get(p[i])[0]) / 4 + 5) * t.ru])
        }
    }
    if (arr.length == 2) {
        arr = [];
    }
    return arr;
}

/*
 表格内的特殊事件 非绘制结果
 */
function drawGridOther(params, svg, t) {
    var index = 45, keys = [], v = [], len = 5;
    keys = params.keys();
    for (vai = 0; i < keys.length; i++) {
        index = 0;
        v = params.get(keys[i]);
        for (var n = 0; n < v.length; n++) {
            if (len >= 0) {
                for (var m = 0; m < v[n][0].length; m++) {

                }
            }
            index = index - (v[n][0].length > 5 ? 5 : v[n][0].length);
        }
    }
}


/*
 绘制grid内竖直事件
 params Array[x,y,font-size,string,color,writing-mode]
 */
function drawVerticalText(params, svg, t) {
    svg.selectAll('text')
        .data(params)
        .enter().append('text')
        .attr('x', function (d) {
            var x;
            x = d[0] * 6 * t.lu;
            return x;
        })
        .attr('y', function (d) {
            return d[1] * t.ru;
        })
        .attr('font-size', function (d) {
            if (d[2]) {
                return d[2];
            }
            return 12;
        })
        .attr('fill', function (d) {
            if (d[4]) {
                return d[4];
            }
            return '#000';
        })
        .attr('writing-mode', function (d) {
            if (d[5]) {
                return d[5];
            }
            return 'lr-tb';
        })
        .html(function (d) {
            return d[3]
        })
}

/*
 普通绘制文本
 params typeof Array[ x,[y,cols],font-size,string,color,writing-mode]
 */
function drawTextCommon(params, svg, t) {

    svg.selectAll('text')
        .data(params)
        .enter().append('text')
        .attr('x', function (d) {
            var x;
            console.log('gary:' + d);
            if (d[0]) {
                x = d[0] * t.lu;
                return x;
            }
        })
        .attr('y', function (d) {
            var fs = 12, len = d[3].length, h = fs;
            if (d[2]) {
                fs = d[2];
            }
            if (d[5]) {
                h = len * (d[2]);
                fs = h > fs ? -h : -fs;
            }
            return (d[1][0] + d[1][1] / 2) * t.ru + fs / 2;
        })
        .attr('font-size', function (d) {
            if (d[2]) {
                return d[2];
            }
            return 12;
        })
        .attr('fill', function (d) {
            console.log('vivi:' + d[4]);
            if (d[4]) {
                return d[4];
            }
            return '#000';
        })
        .attr('writing-mode', function (d) {
            if (d[5]) {
                return d[5];
            }
            return 'lr-tb';
        })
        .html(function (d) {
            return d[3]
        })
    /*svg.append('text')
     .attr('x',function(){
     if(params[0]){
     return params[0]*t.lu;
     }
     })
     .attr('y',function(){
     var fs = 12,len = params[3].length,h=fs;
     if(params[2]){
     fs = params[2];
     }
     if(params[5]){
     h = len*(params[2]);
     fs = h > fs ? -h : -fs;
     }
     return (params[1][0]+params[1][1]/2)*t.ru + fs/2;
     })
     .attr('font-size',function(){
     if(params[2]){
     return params[2];
     }
     return 12;
     })
     .attr('fill',function(){
     if(params[4]){
     return params[4];
     }
     return '#000';
     })
     .attr('writing-mode',function(){
     if(params[5]){
     return 'tb-rl';
     }
     return 'lr-tb';
     })
     .html(function(){
     return params[3]
     })*/
}

/*
 绘制居中文本
 params typeof Array[ [x,xs,len,letter-spacing],[y,ys],font-size,string,color]
 */
function drawTextAlignCenter(params, svg, t) {
    svg.selectAll('text')
        .data(params)
        .enter().append('text')
        .attr('x', function (d, i) {
            var data = d[3] === '' ? d[5] : d[3];

            if (typeof d[0] === 'undefined') return;
            //console.log('%c' + typeof d[0] + '|' + i, 'background: #222; color: #bada55');
            var len = 0,
                x = 0;
            var firstX = d[0][0] * 6 * t.lu;

            if (data) {
                len = ay.getStrActualLen(data) * t.lu / 2;
                if (d[0][2]) {

                    // 不截取长度
                    /*if (data == d[3]) {
                     if (d[5]) {
                     //len = (ay.getStrActualLen(data) + ay.getStrActualLen(d[5])) * t.lu / 2;
                     }
                     data = d[3] = ay.getInterceptedStr(data, d[0][2]);
                     } else if (data == d[5]) {
                     data = d[5] = ay.getInterceptedStr(data, d[0][2]);
                     }*/
                    //len = ay.getStrActualLen(data) * d[2] / 2;

                    //if (d[5] && !d[3]) {
                    len = ay.getStrActualLen(data) * d[2] / 2;
                    //}

                    if (d[5] && d[3]) {
                        len = ay.getStrActualLen(d[3] + d[5]) * d[2] / 2;
                    }

                    // 计算有误差 减去20
                    //len = len - 20;

                    if (len >= d[0][1] * 6 * t.lu) {
                        x = firstX;
                        return x;
                    }

                    // 日期栏宽度(一列) + (一列宽度 - 实际字符宽度)[空隙] / 2
                    // 详细: 当前数据在第几列索引 * 6单元列 * 单元宽度
                    x = firstX + (d[0][1] * 6 * t.lu - len) / 2;
                    //console.log(d[0], data, len, x, t.lu);

                    console.log('%c' + x + '|' + i + '|' + d[3], 'background: #222; color: #f60');
                    return x;
                }
            }
            // else if (d[3] === '') {
            //     x = d[0][0] * 6 * t.lu + (d[0][1] * 6 * t.lu - len) / 2 + 1 - 16;
            //     return x;
            // }

            x = d[0][0] * 6 * t.lu + (d[0][1] * 6 * t.lu - len) / 2 + 1;

            if (d[8]) {
                //x ;
                if (/(?=[^+])(?=[^-])(?=\W)/g.test(d[3])) {
                    x += 5;
                }
            }

            return x;
        })
        .attr('y', function (d, i) {
            var y;

            if (typeof d[0] === 'undefined') return;
            y = d[1][0] * t.ru + (d[1][1] * t.ru + d[2]) / 2 - 1;

            return y;
        })
        .attr('letter-spacing', function (d) {
            if (typeof d[0] === 'undefined') return;
            if (d[0][3]) {
                return d[0][3];
            }
            return 0;
        })
        .attr('font-size', function (d) {
            if (typeof d[0] === 'undefined') return;
            /*if (d[2]) {
             return d[2];
             }*/
            return 10;
        })
        .attr('fill', function (d, i) {
            if (typeof d[0] === 'undefined') return;
            if (d[4]) {
                return d[4];
            }
            else {
                return '#333';
            }
        })
        .attr('rotate', function (d) {
            if (d[8]) {
                return d[8];
            }

            return;
        })
        .html(function (d, i) {
            if (typeof d[0] === 'undefined') return;
            // console.log('%c' + d + '|' + i, 'background: #222; color: #bada55');
            if (d[5]) {
                return d[3] + d[5];
            }
            return d[3]
        })
}
/*
 [ [x1,y1],[x2,y2],color,width]
 */
function drawDottedLine(params, svg, t) {
    svg.selectAll('line')
        .data(params)
        .enter().append('line')
        .attr('x1', function (d) {
            return d[0][0] + 0.5;
        })
        .attr('y1', function (d) {
            return d[0][1] + 0.5;
        })
        .attr('x2', function (d) {
            return d[1][0] + 0.5;
        })
        .attr('y2', function (d) {
            return d[1][1] + 0.5;
        })
        .attr('stroke-dasharray', '2,6')
        .attr('stroke', function (d) {
            if (d[2]) {
                return d[2];
            }
            return '#000';
        })
        .attr('stroke-width', function (d) {
            if (d[3]) {
                return d[3];
            }
            return 1;
        });
}
/*
 [ [x1,y1],[x2,y2],color,width]
 */
function drawLine(params, svg, t) {
    svg.selectAll('line')
        .data(params)
        .enter().append('line')
        .attr('x1', function (d) {
            return d[0][0] + 0.5;
        })
        .attr('y1', function (d) {
            return d[0][1] + 0.5;
        })
        .attr('x2', function (d) {
            return d[1][0] + 0.5;
        })
        .attr('y2', function (d) {
            return d[1][1] + 0.5;
        })
        .attr('stroke', function (d) {
            if (d[2]) {
                return d[2];
            }
            return '#000';
        })
        .attr('stroke-width', function (d) {
            if (d[3]) {
                return d[3];
            }
            return 1;
        });
}

/*
 增加x  腋温
 array [x1,y1,x2,y2]
 */
function addXIcon(params, lData, t, iconInfo) {
    lData.push([[params[0] * t.lu - 4, params[1] * t.ru + 4], [params[0] * t.lu + 4, params[1] * t.ru - 5], iconInfo.yw.borderColor, iconInfo.yw.borderWidth]);
    lData.push([[params[0] * t.lu - 4, params[1] * t.ru - 5], [params[0] * t.lu + 4, params[1] * t.ru + 4], iconInfo.yw.borderColor, iconInfo.yw.borderWidth]);
}
/*
 array[ x,y,r,style,color,name ]	1：实心，2圈圈
 */
function drawIcon(params, svg, t) {

    svg.selectAll('circle')
        .data(params)
        .enter().append('circle')
        .attr('cx', function (d) {
            return d[0] * t.lu;
        })
        .attr('cy', function (d) {
            log('Y轴：', d);
            return d[1] * t.ru;
        })
        .attr('r', function (d) {

            //var key = d[5] == 'pulse' ? oHeartRate : oPulse;
            if (d[5] == 'pulse' || d[5] == 'heartRate') {
                var temp = oTemperature.get(d[0] - 6.5);

                // 因javascript浮点数字计算精度问题, 浮点结果 * 1000再进行计算   2016-07-11  by gary
                if (temp) {
                    var tempDigit = ((42 * 1000 - temp[0] * 1000) / (2 / 10) + 5 * 1000) / 1000;
                    var pulseDigit = ((180 * 1000 - temp[0] * 1000) / 4 + 5 * 1000) / 1000;
                }


                // 脉搏和体温重合时, 将脉搏的点风格设为2
                if (temp && tempDigit == d[1]) {
                    d[3] = 2;
                    return d[2] + 1.5;
                }
                else if (d[5] == 'heartRate') {
                    var temp = oPulse.get(d[0] - 6.5);
                    if (temp && pulseDigit == d[1]) {
                        d[3] = 2;
                        return d[2] + 1.5;
                    }
                }
                else {
                    return 4;
                    //return d[2];
                }
                /*else if(){

                 }*/
            }
            if (d[2]) {
                return d[2];
            }
            return 5;
        })
        .attr('fill', function (d) {
            if (d[3] == 1) {
                return d[4];
            }
            return 'none';
        })
        .attr('stroke', function (d) {
            if (d[3] == 2) {
                return d[4];
            }
            return 'none';
        })
        .attr('stroke-width', function (d) {
            if (d[3] == 2) {
                return 2;
            }
            return 0;
        })
}
