/**
 * Created by lin on 15/9/23.
 */
var log = console.log.bind(console),
    settingDeviceUrl = ay.contextPath + '/nur/infusionmanager/saveInfusionDeviceSetInfo/',
    getDeviceSettingInfoUrl = ay.contextPath + '/nur/infusionmanager/getInfusionDeviceSetInfoByMacaddress',
    getInfusionDetailInfoUrl = ay.contextPath + '/nur/infusionmanager/getDeviceInfusionInfos',
    getOrderActionInfoUrl = ay.contextPath + '/nur/infusionmanager/getOrderActionInfo',
//nur/infusion/saveInfusionMonitorRecord.do?infusionMonitorRecord=****
//http://localhost:8919/mnis/nur/infusionmanager/getPerBagDetailLst?infusionBagId=20150916001
//http://localhost:8919/mnis//nur/infusionmanager/getOrderActionInfo?orderId=I201509091225258528
    saveRecodeInfoUrl = ay.contextPath + '/nur/infusion/saveInfusionMonitorRecord.do';

function getPatrolInfo(callback) {

}

function saveRecodeInfo(url, callback) {
    var formData = ay.serializeObject($('#addPatrolForm'));
    //formData['abnormal'] === 'on' ? formData['abnormal'] = '1' : formData['abnormal'] = '0';

    if (typeof formData['anomalyDisposal'] === 'undefined') {
        formData['anomalyDisposal'] = '';
    }

    formData['residue'] = parseInt(formData['residue']);

    formData['abnormal'] = $('#infusion-normal')[0].checked;

    formData['recordNurseName'] = $('#user').text();
    formData['recordNurseId'] = $('#nurseId').val();
    formData['orderExecId'] = $('#orderId').val();
    log(formData);
    //log(formData['abnormal'], formData['anomalyDisposal']);

    var isValid = $('#addPatrolForm').form('validate');

    if (isValid) {
        $.post(url, {
            infusionMonitorRecord: JSON.stringify(formData)
        }).done(function (res) {
            log(res);
            if (res['rslt'] === '0') {
                $('#addPatrol').dialog('close');
                alert('成功录入！');
            } else {
                alert('错误' + res['rslt'] + res['msg']);
            }
        }).fail(function (err) {
            alert('服务器错误!');
        });
    }
}

function getInfusionDetail(callback) {
    var macAddr = $('#devMacAddr').val(),
        orderId = $('#orderId').val();
    $.getJSON(getInfusionDetailInfoUrl, {
        macAddress: macAddr
    }).done(function (res) {
        log(res);
        var data = res['data'],
            dropSpeed = data['drop_speed'],
            restCapacity = data['rest_capacity'];

        log(dropSpeed, restCapacity);
        $('[name="deliverSpeed"]').val(dropSpeed);
        $('[name="residue"]').val(restCapacity);
    });

    $.getJSON(getOrderActionInfoUrl, {
        orderId: orderId
    }).done(function (res) {
        log(res);
        var data = res['data'],
            str = '',
            orderExec = data['orderExecLog'],
            orderLiquo = data['orderLiquorItem'],
            monitorList = data['monitorRecords'];

        log(typeof orderLiquo);

        if (typeof orderLiquo !== 'undefined') {
            str += '<li class="fl">' +
                '                                    备药护士：' +
                '                                    <span>' + orderLiquo['prepareNurseName'] + '</span>' +
                '                                    <span>' + orderLiquo['prepareTime'] + '</span>' +
                '                                </li>' +
                '                                <li class="fl">' +
                '                                <li class="fl">' +
                '                                    审核护士：' +
                '                                    <span>' + orderLiquo['verifyNurseName'] + '</span>' +
                '                                    <span>' + orderLiquo['verifyTime'] + '</span>' +
                '                                </li>' +
                '                                <li class="fl">' +
                '                                    配液护士：' +
                '                                    <span>' + orderLiquo['execNurseName'] + '</span>' +
                '                                    <span>' + orderLiquo['execTime'] + '</span>' +
                '                                </li>';
        }

        if (typeof orderExec !== 'undefined') {
            str +=
                '                                <li class="fl">' +
                '                                    执行护士：' +
                '                                    <span>' + orderExec['execNurseName'] + '</span>' +
                '                                    <span>' + orderExec['execDate'] + '</span>' +
                '                                </li>';
        }


        log(monitorList);
        if (typeof monitorList !== 'undefined') {
            str += '                                <li class="fl alone">' +
                '                                    巡视护士：' +
                '                                    <div class="fr" style="width: 89%;">';
            $.each(monitorList, function (i, ele) {
                str += '<div>' + ele['recordNurseName'] + '&nbsp;&nbsp;' + ele['recordDate'] + '</div>';
            });

            str += '                                    </div>' +
                '                                </li>';
        }

        $('#patrolWrapper').html(str);
        log(data);
    });
}

$(function () {
    var infusionToggle = {
        len: 0,
        describe: null,
        height: 0,
        leave: false,
        $more: null,
        fHeight: 0,
        fix: false,
        pre: 0,
        oldHeight: 0,
        slide: $('.slide-container'),
        wrapperHeight: 183,

        hoverHandler: function (context) {
            var $this = $(context),
                $describe = $this.children('.infusion-describe'),
                $li = $describe.find('li'),
                $liLen = $li.length,
                height = $li.height(),
                $more = $describe.find('.more');

            this.len = $liLen;
            this.describe = $describe;
            this.height = height;
            this.$more = $more;
            this.fHeight = 0;

            for (var i = 0; i < $liLen; i += 1) {
                this.fHeight += $($li[i]).height();
            }

            if (this.fHeight < 56) this.fHeight = 56;

            //log(height, this.fHeight);

            if ($(context).hasClass('on')) {
                return;
            }

            if (this.leave) {
                this.boxContract();
                return;
            }

            this.boxExpand();
        },

        clickHandler: function (context) {
            var $this = $(context),
                $preDescribe = $('.infusion-describe').eq(this.pre);

            log(this.pre, $this.index());

            if ($this.index() === this.pre) {
                return;
            }

            if ($this.hasClass('unreached')) {
                return;
            }

            $this.addClass('on').siblings().removeClass('on');
            !!this.$more && this.$more.hide();
            !!this.describe && this.describe.height(this.fHeight);

            this.pre = $this.index();
            this.oldHeight = $preDescribe.height();
            this.boxContract($preDescribe, true);
        },

        boxExpand: function () {
            if (this.len > 2) {
                var that = this,
                    $sHeight = this.wrapperHeight;

                that.slide.animate({
                    height: $sHeight + this.fHeight - 56 + 'px'
                }, 200);

                that.describe.animate({
                    height: this.fHeight + 'px'
                }, 200);

                that.$more.hide();
            }
        },

        boxContract: function (box, fix) {
            var that = this,
                $wrapper = box || that.describe,
                $more = box && box.children('.more') || that.$more,
                $sHeight = this.wrapperHeight,
                num;
            if (this.len > 2) {
                log(box);
                $wrapper.animate({
                    height: '56px'
                }, 200, function () {
                    $more.show();
                });

                if (fix) {
                    return;
                }
                this.slide.animate({
                    height: $sHeight + 'px'
                }, 200);
            }

            if (fix) {
                // 鼠标悬浮的盒子高度大于当前选中盒子高度时候，合上盒子的最终高度不是原始高度    --bugFixed
                num = that.fHeight > that.oldHeight && $sHeight + this.fHeight - 56 || $sHeight;

                $wrapper.animate({
                    height: '56px'
                }, 200, function () {
                    $more.show();
                });
                this.slide.animate({
                    height: num + 'px'
                }, 200);
            }
        }
    };

    var slider = {
        wrapper: $('.slide-wrapper'),
        nextBtn: $('.i-d-next'),
        prevBtn: $('.i-d-prev'),
        container: $('.slide-container'),
        moveHandler: function () {
            alert();
        }
    };

    slider.prevBtn.on('click', slider.moveHandler);
    slider.nextBtn.on('click', slider.moveHandler);

    function drawTable(context) {
        $.get(ay.contextPath + '/nur/infusionmanager/getPerBagDetailLst', {
            infusionBagId: $(context).data('bagid')
        }).done(function (data) {
            log(data['data'], !data['data']);
            if (!data['data']) return;
            var resList = data['data']['list'],
                categories = [],
                speedCount = [],
                restCount = [],
                lastRest;

            //log(resList);

            $.each(resList, function (i, val) {
                var dropSpeed = val['drop_speed'],
                    rest = val['rest_capacity'],
                    timeItem = val['sent_time'];
                categories.push(timeItem);
                speedCount.push(dropSpeed);
                restCount.push(rest);
            });

            lastRest = restCount[restCount.length - 1];
            // 绘制图表
            $('#i-d-wrapper').highcharts({
                chart: {
                    zoomType: 'xy'
                },
                title: {
                    text: '剩余' + (lastRest || 0) + 'ml',
                    x: -20 //center
                },
                xAxis: {
                    categories: categories,
                    minTickInterval: 4
                },
                yAxis: [
                    {
                        title: {
                            text: '单位：滴／分'
                        },
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: '#808080'
                        }],
                        ceiling: 100,
                        floor: 0
                    },
                    {
                        title: {
                            text: '单位：ml'
                        },
                        plotLines: [{
                            value: 0,
                            width: 1,
                            color: '#808080'
                        }],
                        ceiling: 500,
                        floor: 0,
                        opposite: true
                    }],
                colors: ['#3084c3', '#17d9b8'],
                tooltip: {
                    shared: true
                },
                legend: {
                    layout: 'vertical',
                    align: 'top',
                    x: 1100,
                    verticalAlign: 'top',
                    //y: 100,
                    floating: true,
                    borderWidth: 0
                },
                series: [{
                    name: '速度',
                    data: speedCount,
                    tooltip: {
                        valueSuffix: '滴/分'
                    }
                }, {
                    name: '剩余量',
                    yAxis: 1,
                    data: restCount,
                    tooltip: {
                        valueSuffix: 'ml'
                    }
                }]
            });
        });
    }

    var infusionItem = $('.i-d-list > li'),
        iLen = infusionItem.length;

    for (var i = 0; i < iLen; i += 1) {
        if ($(infusionItem[i]).hasClass('transfusing')) {
            infusionToggle.pre = i;
            drawTable(infusionItem[i]);
            infusionToggle.hoverHandler(infusionItem[i]);
            infusionToggle.boxExpand();
        }
    }

    $('#addPatrol').dialog({
        title: '巡视录入',
        width: 600,
        height: 400,
        modal: true,
        closed: true,
        buttons: [{
            text: '保存',
            handler: function () {
                saveRecodeInfo(saveRecodeInfoUrl, function () {

                });
            }
        }, {
            text: '取消',
            handler: function () {
                $('#addPatrol').dialog('close');
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

                log(isValid);

                if (isValid) {
                    $.post(settingDeviceUrl, {
                        itemDeviceSet: JSON.stringify(formData)
                    }).done(function (data) {
                        log(data);
                        if (data['rslt'] === '0') {
                            $('#deviceSetting').dialog('close');
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

    // 解绑警告
    $('#unbindConfirm').dialog({
        title: '警告',
        width: 400,
        height: 120,
        modal: true,
        closed: true,
        buttons: [{
            text: '确认',
            handler: function () {

                $.get(ay.contextPath + '/nur/infusionmanager/setMathDevicePatient/', {
                    doType: 0,
                    mathType: 'DB',
                    macAddress: $('#devMacAddr').val()
                }).done(function (data) {
                    log(data);

                    if (data['rslt'] === '0') {
                        $('#dev-off').remove();
                        $('#unbindConfirm').dialog('close');
                        alert(data['msg']);
                    }
                });

                /*$.post(delDeviceUrl, {
                 macAddress: $('#confirm-dev-name').data('macaddr')
                 }).done(function (data) {
                 log(data);
                 getData(getDeviceUrl);
                 $('#delConfirm').dialog('close');
                 alert(data['msg']);
                 }).fail(function (err) {
                 alert('服务器错误!');
                 });*/
            }
        }, {
            text: '取消',
            handler: function () {
                $('#unbindConfirm').dialog('close');
            }
        }]
    });

    $('.i-d-list').on('click', 'li.fl', function (e) {
        infusionToggle.fix = true;
        infusionToggle.clickHandler(this);
        drawTable(this);
    });

    $('.i-d-list').on('mouseenter', 'li.fl', function (e) {
        infusionToggle.leave = false;
        infusionToggle.hoverHandler(this);
    });

    $('.i-d-list').on('mouseleave', 'li.fl', function (e) {
        infusionToggle.leave = true;
        infusionToggle.hoverHandler(this);
    });

    $('#searchBtn').on('click', function (e) {
        var bedCode = $('#searchKey').val(),
            url = ay.contextPath + '/nur/infusionmanager/infusionPatientDetailByBedcode';
        log(bedCode);
        $.get(url, {
            deptCode: $('#deptCode').val(),
            bedCode: bedCode
        }).done(function (res) {
            if (res['rslt'] === '0') {
                var patId = res['data']['patId'];
                log(patId);
                window.location.href = ay.contextPath + '/nur/infusionmanager/infusionDetail.do?id=' + patId;
            } else {
                alert(res['msg']);
            }
        }).fail(function (err) {
            alert('服务器错误！');
        });
    });

    $('#addPatrolBtn').on('click', function (e) {
        $('#addPatrol').dialog('open');
        getInfusionDetail();
    });

    $('#refresh').on('click', function (e) {
        getInfusionDetail();
    });

    $('#dev-off').on('click', function (e) {

        $('#unbindConfirm').dialog('open');

    });

    $('#dev-set').on('click', function (e) {
        $.get(getDeviceSettingInfoUrl, {
            macAddress: $('#devMacAddr').val()
        }).done(function (data) {
            var res = data['data'],
                alarmSwitch = res['alarmSwitch'] !== '0' ? 'checked' : false,
                restTime = res['restTimeAlarm'],
                restVol = res['restMlAlerm'],
                speedFloor = res['dropSpeedFloor'],
                speedUpper = res['dropSpeedUpper'],
                idc = res['idc'],
                idcOptions = $('[name="idc"]').find('option'),
                timeOptions = $('[name="restTimeAlarm"]').find('option');
            log(alarmSwitch, restTime, restVol, speedFloor, speedUpper);

            $('#isAlarm').attr('checked', alarmSwitch);
            $('[name="restMlAlerm"]').val(restVol);
            $('[name="restTimeAlarm"]').val(restTime);
            $('[name="dropSpeedFloor"]').val(speedFloor);
            $('[name="dropSpeedUpper"]').val(speedUpper);
            $.each(idcOptions, function (i, ele) {
                var eValue = $(ele).val();
                log(typeof eValue, idc);

                if (Number(eValue) === idc) {
                    idcOptions.eq(i).attr('selected', true);
                }
            });
            $.each(timeOptions, function (i, ele) {
                var eValue = $(ele).val();
                log(typeof eValue, idc);

                if (Number(eValue) === restTime) {
                    timeOptions.eq(i).attr('selected', true);
                }
            });

        });
        $('#deviceSetting').dialog('open');
    });

    $('[name="abnormal"]').on('click', function (e) {
        var $this = $(this),
            $id = $this.attr('id'),
            isChecked = $id === 'infusion-unusual';

        log(isChecked);

        if (isChecked) {
            $('.visible-toggle').show();
        } else {
            $('.visible-toggle').hide();
            $('[name="anomalyMsg"]')[0].selectedIndex = 0;
            var len = $('[name="anomalyDisposal"]').length;
            for (var i = 0; i < len; i += 1) {
                $('[name="anomalyDisposal"]')[i].checked = false;
            }
        }
    });

    $('.handler-toggle').on('click', function (e) {
        var $this = $(this);
        $this.siblings('[type="checkbox"]').attr('checked', false);
    })
});