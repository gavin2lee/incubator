var globalDeptCode = $('#globalDeptCode').val();

$(function () {

    try {
        // var mData  = [
        // 	{"btAddress":"00-E0-4C-68-18-EF","dataLength":0,"ackCode":0,"ackType":0,"state":1,"index":0,"data":55,"startTime":"2015-08-10 07:00:00","currentTime":"2015-08-10 07:06:41","patName":"test1","bedCode":"01","maxData":250,"orderCount":"1/4"},
        // 	{"btAddress":"00-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":2,"index":0,"data":0,"startTime":"2015-08-10 07:00:00","currentTime":"2015-08-10 07:06:41","patName":"test2","bedCode":"02","maxData":250,"orderCount":"1/2"},
        // 	{"btAddress":"00-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-10 07:00:00","currentTime":"2015-08-10 07:06:41","patName":"test3","bedCode":"03","maxData":250,"orderCount":"1/2"},
        // 	{"btAddress":"00-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":60,"startTime":"2015-08-10 07:00:00","currentTime":"2015-08-10 07:06:41","patName":"test4","bedCode":"04","maxData":250,"orderCount":"1/2"},
        // 	{"btAddress":"00-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":5,"index":0,"data":18,"startTime":"2015-08-10 07:00:00","currentTime":"2015-08-10 07:06:41","patName":"test4","bedCode":"05","maxData":250,"orderCount":"1/2"}
        // ];

        //	var msg = {"data":[{"btAddress":"00-E0-4C-68-18-EF","dataLength":0,"ackCode":0,"ackType":0,"state":1,"index":0,"data":101,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"周际仁","bedCode":"05","maxData":250,"orderCount":"1/2"},{"btAddress":"00-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":1,"index":0,"data":101,"startTime":"2015-08-14 13:00:00","currentTime":"2015-08-14 14:35:57","patName":"肖剑锋","bedCode":"06","maxData":500,"orderCount":"1/2"},{"btAddress":"00-E0-3D-45-19-3E","dataLength":0,"ackCode":0,"ackType":0,"state":2,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"黄诗俊","bedCode":"07","maxData":250,"orderCount":"1/3"},{"btAddress":"00-E0-3D-45-19-3A","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"汤燕清","bedCode":"08","maxData":250,"orderCount":""},{"btAddress":"00-E0-3D-45-19-3B","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"吕桂英","bedCode":"09","maxData":0,"orderCount":""},{"btAddress":"00-E0-3D-45-19-3C","dataLength":0,"ackCode":0,"ackType":0,"state":2,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"杜志初","bedCode":"10","maxData":250,"orderCount":"1/2"},{"btAddress":"10-E0-4C-68-18-EF","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"陈如意","bedCode":"11","maxData":0,"orderCount":"1/3"},{"btAddress":"20-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":2,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"符乃朋","bedCode":"13","maxData":250,"orderCount":"1/2"},{"btAddress":"30-E0-3D-45-19-3E","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"赵崇悦","bedCode":"15","maxData":250,"orderCount":""},{"btAddress":"40-E0-3D-45-19-3A","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"崔丽霞","bedCode":"16","maxData":0,"orderCount":""},{"btAddress":"50-E0-3D-45-19-3B","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"魏琴方","bedCode":"17","maxData":150,"orderCount":"1/5"},{"btAddress":"60-E0-3D-45-19-3C","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"谢宪森","bedCode":"19","maxData":200,"orderCount":""},{"btAddress":"10-E0-4C-68-18-AF","dataLength":0,"ackCode":0,"ackType":0,"state":5,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"刘海文","bedCode":"20","maxData":250,"orderCount":"1/2"},{"btAddress":"20-E0-3D-45-19-BF","dataLength":0,"ackCode":0,"ackType":0,"state":5,"index":0,"data":30,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"晏夏新","bedCode":"21","maxData":250,"orderCount":"1/4"},{"btAddress":"30-E0-3D-45-19-CE","dataLength":0,"ackCode":0,"ackType":0,"state":5,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"陈森","bedCode":"22","maxData":500,"orderCount":"1/3"},{"btAddress":"30-E0-3D-45-19-EE","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"刘波","bedCode":"23","maxData":0,"orderCount":""},{"btAddress":"30-E0-3D-45-19-FE","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"王文杰","bedCode":"24","maxData":0,"orderCount":""},{"btAddress":"30-E0-3D-45-29-FE","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"晏琪","bedCode":"25","maxData":0,"orderCount":""}]}
        $.post(ay.contextPath + '/nur/infusionmanager/getInfusionManagerInfos.do?deptCode=' + globalDeptCode, {},
            function (data) {
                Monitor.init(data.data);
                Monitor.updateMonitor(data.data);
            });
//		$.get("../infusiontimer/list.do",function(msg){
//		 	Monitor.init(msg.data);
//		});
    } catch (e) {
        console.log("error:" + e);
    }
});

var Monitor = {
    /*
     1:输液中 、2:输液结束、3:输液暂停、4:连接断开、5:输液报警
     */
    userList: [],
    domList: [],
    ST: null
}

Monitor.init = function (data) {
    var mData = this.transformData(data);
    this.initMonitor(mData);
    Monitor.initEvent();
    this.setDynaAction(4000);
}

Monitor.initEvent = function () {
    var monitorItems = $(".monitor-item");
    $("#searchKey").bind("change", function () {
        $("#searchBtn").trigger("click");
    });
    $("#searchBtn").bind("click", function () {
        var key = $("#searchKey").val();
        monitorItems.each(function () {
            if (key === "") {
                $(this).fadeIn();
                return;
            }
            if (/^[0-9]+$/.test(key) && parseInt($(this).attr("data-bed")) === parseInt(key)) {
                $(this).fadeIn(200);
            }
            else {
                $(this).fadeOut(200);
            }
        });
    });
}

Monitor.setDynaAction = function (t) {
    var that = this;

    var update = function () {
        try {

            //		var msg = {"data":[{"btAddress":"00-E0-4C-68-18-EF","dataLength":0,"ackCode":0,"ackType":0,"state":1,"index":0,"data":91,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"周际仁","bedCode":"05","maxData":250,"orderCount":"1/2"},{"btAddress":"00-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":1,"index":0,"data":101,"startTime":"2015-08-14 13:00:00","currentTime":"2015-08-14 14:35:57","patName":"肖剑锋","bedCode":"06","maxData":500,"orderCount":"1/2"},{"btAddress":"00-E0-3D-45-19-3E","dataLength":0,"ackCode":0,"ackType":0,"state":2,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"黄诗俊","bedCode":"07","maxData":250,"orderCount":"1/3"},{"btAddress":"00-E0-3D-45-19-3A","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:40:57","patName":"汤燕清","bedCode":"08","maxData":250,"orderCount":""},{"btAddress":"00-E0-3D-45-19-3B","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"吕桂英","bedCode":"09","maxData":0,"orderCount":""},{"btAddress":"00-E0-3D-45-19-3C","dataLength":0,"ackCode":0,"ackType":0,"state":2,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"杜志初","bedCode":"10","maxData":250,"orderCount":"1/2"},{"btAddress":"10-E0-4C-68-18-EF","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"陈如意","bedCode":"11","maxData":0,"orderCount":"1/3"},{"btAddress":"20-E0-3D-45-19-3F","dataLength":0,"ackCode":0,"ackType":0,"state":2,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"符乃朋","bedCode":"13","maxData":250,"orderCount":"1/2"},{"btAddress":"30-E0-3D-45-19-3E","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"赵崇悦","bedCode":"15","maxData":250,"orderCount":""},{"btAddress":"40-E0-3D-45-19-3A","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"崔丽霞","bedCode":"16","maxData":0,"orderCount":""},{"btAddress":"50-E0-3D-45-19-3B","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"魏琴方","bedCode":"17","maxData":150,"orderCount":"1/5"},{"btAddress":"60-E0-3D-45-19-3C","dataLength":0,"ackCode":0,"ackType":0,"state":3,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"谢宪森","bedCode":"19","maxData":200,"orderCount":""},{"btAddress":"10-E0-4C-68-18-AF","dataLength":0,"ackCode":0,"ackType":0,"state":5,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"刘海文","bedCode":"20","maxData":250,"orderCount":"1/2"},{"btAddress":"20-E0-3D-45-19-BF","dataLength":0,"ackCode":0,"ackType":0,"state":5,"index":0,"data":30,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"晏夏新","bedCode":"21","maxData":250,"orderCount":"1/4"},{"btAddress":"30-E0-3D-45-19-CE","dataLength":0,"ackCode":0,"ackType":0,"state":5,"index":0,"data":50,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"陈森","bedCode":"22","maxData":500,"orderCount":"1/3"},{"btAddress":"30-E0-3D-45-19-EE","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"刘波","bedCode":"23","maxData":0,"orderCount":""},{"btAddress":"30-E0-3D-45-19-FE","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"王文杰","bedCode":"24","maxData":0,"orderCount":""},{"btAddress":"30-E0-3D-45-29-FE","dataLength":0,"ackCode":0,"ackType":0,"state":4,"index":0,"data":0,"startTime":"2015-08-14 14:00:00","currentTime":"2015-08-14 14:35:57","patName":"晏琪","bedCode":"25","maxData":0,"orderCount":""}]}

            $.post(ay.contextPath + '/nur/infusionmanager/getInfusionManagerInfos.do?deptCode=' + globalDeptCode, {},
                function (data) {
                    Monitor.updateMonitor(data.data);
                    that.ST = setTimeout(update, t);
                });
//			$.get("../infusiontimer/list.do",function(msg){
//				Monitor.updateMonitor(msg.data);
//				that.ST = setTimeout(update,t);
//			});	
        } catch (e) {
            console.log("error" + e);
            that.ST = setTimeout(update, t);
        }
    }
    setTimeout(function () {
        update();
    }, 2000);
};

Monitor.updateMonitor = function (data) {
    var userList = this.userList,
        domList = this.domList,
        params = [],
        tranLow = "", status = "", maxHeight = 83,
        i = 0, len = data.length,
        that = this;
    var inmDeviceInfo,
        inmPerbagDetailInfo,
        inmPerbagInfo,
        isAlarm,
        inmDeviceSetInfo,
        defaultSetInfoUrl = ay.contextPath + '/nur/infusionmanager/getInfusionDeviceSetInfoByMacaddress?macAddress=DEFAULT',
    // 报警项目
        alarmRestMlAlermValue,
        alarmDropSpeedFloorValue,
        alarmDropSpeedUpperValue,
        alarmRestTimeAlarmValue,
        currentDropSpeed,
        currentRest_capacity,
        currentInm_percent,
        currentRest_time,
        infusionStatus,
        alarmIdc;

    $.getJSON(defaultSetInfoUrl).done(function (res) {
        var defaultSetting = res['data'];

        for (; i < len; i++) {
            status = "";
            inmDeviceSetInfo = data[i]['infusionDeviceSetInfo'];
            inmDeviceInfo = data[i]['infusionDeviceInfo'];
            inmPerbagDetailInfo = data[i]['inmPerBagDetailInfo'];
            inmPerbagInfo = data[i]['inmPerBagInfo'];
            isAlarm = inmDeviceSetInfo['alarmSwitch'];
            infusionStatus = inmPerbagInfo['infusion_status'];

            var $item = domList[i],
                bottle = $item.find(".infusion-bottle");

            // 获取报警的临界值
            alarmRestMlAlermValue = inmDeviceSetInfo['restMlAlerm'];
            alarmDropSpeedFloorValue = inmDeviceSetInfo['dropSpeedFloor'];
            alarmDropSpeedUpperValue = inmDeviceSetInfo['dropSpeedUpper'];
            alarmRestTimeAlarmValue = inmDeviceSetInfo['restTimeAlarm'];
            alarmIdc = inmDeviceSetInfo['idc'];

            // 获取当前值
            currentDropSpeed = inmPerbagDetailInfo['drop_speed'];
            currentRest_capacity = inmPerbagDetailInfo['rest_capacity'];
            currentInm_percent = inmPerbagDetailInfo['inm_percent'];
            currentRest_time = inmPerbagDetailInfo['rest_time'];

            // 如果没有没有配置信息，则使用默认配置
            if (alarmRestMlAlermValue === 0 &&
                alarmDropSpeedFloorValue === 0 &&
                alarmDropSpeedUpperValue === 0 &&
                alarmRestTimeAlarmValue === 0 &&
                alarmIdc === 0
            ) {
                alarmRestMlAlermValue = defaultSetting['restMlAlerm'];
                alarmDropSpeedFloorValue = defaultSetting['dropSpeedFloor'];
                alarmDropSpeedUpperValue = defaultSetting['dropSpeedUpper'];
                alarmRestTimeAlarmValue = defaultSetting['restTimeAlarm'];
                alarmIdc = defaultSetting['idc'],
                isAlarm = 1;
            }

            log('床号：' + data[i]['bedCode'] + '，状态：' + infusionStatus, '是否报警：' + isAlarm);
            // 设置报警情况
            if (infusionStatus === 'I' || infusionStatus === 'P' || infusionStatus === 'F') {

                // 设置可点击
                $item.addClass('clickAble');

                //
                if (isAlarm === 1 && currentRest_capacity <= alarmRestMlAlermValue ||
                    currentDropSpeed > alarmDropSpeedUpperValue ||
                    currentDropSpeed < alarmDropSpeedFloorValue ||
                    currentRest_time <= alarmRestTimeAlarmValue
                ) {
                    //alert();

                    if (!bottle.hasClass('alarm')) {
                        bottle.addClass('alarm');
                    }

                    if (currentRest_capacity < alarmRestMlAlermValue) {
                        bottle.find('.liquid').css({
                            color: 'red'
                        });
                    }

                    if (currentRest_time < alarmRestTimeAlarmValue) {
                        bottle.find('.time').css({
                            color: 'red'
                        });
                    }
                }

            } else {
                bottle.removeClass('alarm');
            }

            //连接断开
            if (inmDeviceInfo.deviceLnk === "0") {
                status = "outline";
                $item.find("#infusionBottle").attr("class", "infusion-bottle outline");
                userList[i].set("percent", "");
                userList[i].set("name", "");
                userList[i].set("index", "");
                continue;
            }

            params = that.getCalValue(inmPerbagDetailInfo);

            if (inmPerbagInfo.infusion_status === "P") {
                status = "pause";
                if (params.tranStatus >= 8) {
                    if (params.percent <= 20) {
                        tranLow = "low-2";
                    }
                    else {
                        tranLow = "";
                    }
                }
                else {
                    tranLow = "low-1";
                }

                if (params.timeRemain === 0 && inmPerbagInfo.rest_capacity > 0) {
                    params.timeRemain = "<1"
                }
                if (params.percent === 0 && inmPerbagInfo.rest_capacity > 0) {
                    params.timeRemain = "<1"
                }

                if (params.percent === 0 || inmPerbagDetailInfo.infusion_status === "F"
                    || inmPerbagDetailInfo.infusion_status === "E") {
                    status = "empty";
                    tranLow = "";
                    params.tranStatus = "";
                }
                $item.find("#infusionBottle").attr("class", "infusion-bottle " + status);
                $item.find("#tranStatus").height(params.tranStatus).attr("class", "tranStatus " + tranLow);
                userList[i].set("liquid", currentRest_capacity);
                userList[i].set("name", inmDeviceInfo.patName);
                userList[i].set("percent", params.percent + "%");
                userList[i].set("bed", inmDeviceInfo.bedCode + "床");
                userList[i].set("index", (inmPerbagInfo.order_exec_count) + '/' + (inmPerbagInfo.order_count));
                continue;
            }
            //当液量高度小于8像素 需修改tranStatus的宽度保持正确的显示
            if (params.tranStatus >= 8) {
                if (params.percent <= 20) {
                    tranLow = "low-2";
                }
                else {
                    tranLow = "";
                }
            }
            else {
                tranLow = "low-1";
            }

            if (params.timeRemain === 0 && inmPerbagInfo.rest_capacity > 0) {
                params.timeRemain = "<1"
            }
            if (params.percent === 0 && inmPerbagInfo.rest_capacity > 0) {
                params.timeRemain = "<1"
            }

            if (params.percent === 0 || inmPerbagDetailInfo.infusion_status === "F" || inmPerbagDetailInfo.infusion_status === "E") {
                status = "empty";
                tranLow = "";
                params.tranStatus = "";
            }


            domList[i].find("#infusionBottle").attr("class", "infusion-bottle " + status);
            domList[i].find("#tranStatus").height(params.tranStatus).attr("class", "tranStatus " + tranLow);
            userList[i].set("liquid", /*inmPerbagInfo.rest_capacity*/parseInt(inmPerbagDetailInfo['rest_capacity']));
            userList[i].set("time", params.timeRemain + '分');

            userList[i].set("name", inmDeviceInfo.patName);
            userList[i].set("bed", inmDeviceInfo.bedCode + "床");
            userList[i].set("index", (inmPerbagInfo.order_exec_count) + '/' + (inmPerbagInfo.order_count));
        }

    }).fail(function (err) {
    });


};

Monitor.transformData = function (data) {
    var mData = [], i = 0, len = data.length, maxHeight = 83, params = null, tranLow = "", status = "";
    var inmDeviceInfo, inmPerbagDetailInfo, inmPerbagInfo;

    for (; i < len; i++) {
        status = "";
        inmDeviceInfo = data[i].infusionDeviceInfo;
        inmPerbagDetailInfo = data[i].inmPerBagDetailInfo;
        inmPerbagInfo = data[i].inmPerBagInfo;
        //连接断开
        if (inmDeviceInfo.deviceLnk === "0") {
            mData.push({
                mIndex: i,
                btAddress: inmDeviceInfo.macAddress,
                status: "outline",
                liquid: "",
                time: "",
                percent: "",
                tranLow: "",
                tranStatus: "",
                bed: inmDeviceInfo.bedCode,
                // gary
                patId: inmDeviceInfo.patId,
                inhospNo: inmDeviceInfo.inhospNo,
                // gary
                name: "",
                index: "",
                pIcon: "no"
            });
            var user = new Monitor.user(mData[i].mIndex);
            this.userList.push(user);
            continue;
        }
        //终止和拔针
        if (inmPerbagDetailInfo.infusion_status === "F" || inmPerbagDetailInfo.infusion_status === "E") {
            mData.push({
                mIndex: i,
                btAddress: data[i].btAddress,
                status: "empty",
                liquid: 0,
                time: 0 + '',
                percent: 0 + "%",
                tranLow: "",
                tranStatus: "",
                bed: inmDeviceInfo.bedCode,
                name: inmDeviceInfo.patName,
                // gary
                patId: inmDeviceInfo.patId,
                inhospNo: inmDeviceInfo.inhospNo,
                // gary
                index: (inmPerbagInfo.order_exec_count) + '/' + (inmPerbagInfo.order_count),
                pIcon: "no"
            });
            var user = new Monitor.user(mData[i].mIndex);
            this.userList.push(user);
            continue;
        }

        params = this.getCalValue(inmPerbagDetailInfo);

        if (inmPerbagDetailInfo.infusion_status === "P") {
            status = "pause";
        }

        if (params.tranStatus >= 8) {
            if (params.percent <= 20) {
                tranLow = "low-2";
                params.tranStatus = "height:" + params.tranStatus + "px;";
            }
            else {
                params.tranStatus = "height:" + params.tranStatus + "px";
            }
        }
        else {
            tranLow = "low-1";
            params.tranStatus = "height:" + params.tranStatus + "px;";
        }

        if (params.timeRemain === 0 && inmPerbagInfo.rest_capacity > 0) {
            params.timeRemain = "<1"
        }
        if (params.percent === 0 && inmPerbagInfo.rest_capacity > 0) {
            params.percent = "<1"
        }

        if (params.percent === 0) {
            status = "empty";
            tranLow = "";
            params.tranStatus = "";
        }


        mData.push({
            mIndex: i,
            btAddress: inmDeviceInfo.macAddress,
            status: status,
            liquid: inmPerbagInfo.rest_capacity,
            time: params.timeRemain,
            percent: params.percent + "%",
            tranLow: tranLow,
            tranStatus: params.tranStatus,
            bed: inmDeviceInfo.bedCode,
            name: inmDeviceInfo.patName,
            // gary
            patId: inmDeviceInfo.patId,
            inhospNo: inmDeviceInfo.inhospNo,
            // gary
            index: (inmPerbagInfo.order_exec_count) + '/' + (inmPerbagInfo.order_count),
            pIcon: "no"
        });

        var user = new Monitor.user(mData[i].mIndex);
        this.userList.push(user);
    }
    return {data: mData};
}

Monitor.getCalValue = function (data) {

    var speed = 0, costTime = 0, percent = 0, timeRemain = 0, tranStatus = 0;

//	//剩余比例
//	percent = Math.round(data.data*1.0/data.maxData*100);
//	//花费时间
//	//costTime = (new Date(data.currentTime).getTime() - new Date(data.startTime).getTime())*1.0/1000/60;
//	
//	//速度
//	//speed = percent === 100 ? 0 : (data.maxData - data.data)*1.0/costTime;
//	speed=4.15;
//	//剩余时间
//	timeRemain = speed === 0 ? "-" : Math.round(data.data/speed);
    percent = data.inm_percent;
    timeRemain = data.rest_time;
    //背景色值
    tranStatus = Math.round(83 * percent / 100);

    return {
        timeRemain: timeRemain,
        percent: percent,
        tranStatus: tranStatus
    }
}

Monitor.initMonitor = function (data) {
    var that = this;
    var tpl = $("#monitorItem").html();
    var template = Handlebars.compile(tpl);
    var html = template(data);
    $("#monitorList").append(html);
    $("#monitorList").find("li").each(function () {
        that.domList.push($(this));
    });
}

Monitor.dataBinder = function (object_id) {
    var pubSub = jQuery({});

    var data_attr = "bind-" + object_id,
        message = object_id + ":change";

    jQuery(document).on("change", "[data-" + data_attr + "]", function (evt) {
        var $input = jQuery(this);
        pubSub.trigger(message, [$input.data(data_attr), $input.val()]);
    });

    pubSub.on(message, function (evt, prop_name, new_val) {
        jQuery("[data-" + data_attr + "=" + prop_name + "]").each(function () {
            var $bound = jQuery(this);
            if ($bound.is("input, textarea, select")) {
                $bound.val(new_val);
            } else {
                $bound.html(new_val);
            }
        });
    });
    return pubSub;
}
Monitor.user = function (uid) {
    var binder = new Monitor.dataBinder(uid + ""),

        user = {
            attributes: {},
            set: function (attr_name, val) {
                this.attributes[attr_name] = val;
                binder.trigger(uid + ":change", [attr_name, val, this]);
            },
            get: function (attr_name) {
                return this.attributes[attr_name];
            },
            _binder: binder
        };

    binder.on(uid + ":change", function (evt, attr_name, new_val, initiator) {
        if (initiator !== user) {
            user.set(attr_name, new_val);
        }
    });
    return user;
}
