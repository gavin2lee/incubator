/**
 * Created by lin on 15/9/23.
 */
var log = console.log.bind(console);
var addDeviceInfo = ay.contextPath + '/nur/infusionmanager/getInfusionDepts/',
    chartByDayUrl = ay.contextPath + '/nur/infusionmanager/statInfusionByDay',
    chartByDeptUrl = ay.contextPath + '/nur/infusionmanager/statInfusionEveryDeptByDays',
    $selection = $('#departmentList');

/*
* 按天对指定科室统计输液量
 http://localhost:8919/mnis//nur/infusionmanager/statInfusionByDay?deptCode=5042&startDate=2015-09-09&endDate=2015-09-11


 按每个科室统计某个时间段内的输液量
 http://localhost:8919/mnis//nur/infusionmanager/statInfusionEveryDeptByDays?startDate=2015-09-09&endDate=2015-09-11
 */

function setChart(deptCode, sDate, eDate) {
    $.getJSON(chartByDayUrl, {
        deptCode: deptCode || '',
        startDate: sDate || '',
        endDate: eDate || ''
    }).done(function (res) {
        log(res);
        var xTitle = [],
            bagCount = [],
            bagVol = [],
            bagDate = [],
            data = res['data'],
            panTimeData = [],
            panPercentData = [];

        $.each(data['list'], function (i, v) {
            var timeTemp = new Date(v['natural_stat_date']);

            log(timeTemp.getDay());

            bagDate.push(Number(timeTemp.getMonth() + 1) + '-' + Number(timeTemp.getUTCDate() + 1));
            bagCount.push(v['infusion_bag_num']);
            bagVol.push(v['infusion_ml_num']);
        });


        $('#workloadData').highcharts({
            chart: {
                type: 'areaspline',
                zoomType: 'xy'
            },
            title: {
                text: '输液统计表'
            },
            legend: {
                layout: 'vertical',
                align: 'top',
                verticalAlign: 'top',
                x: 660,
                y: 6,
                floating: true,
                borderWidth: 1,
                backgroundColor: (Highcharts.theme && Highcharts.theme.legendBackgroundColor) || '#FFFFFF'
            },
            xAxis: {
                categories: bagDate/*,
                plotBands: [{ // visualize the weekend
                    from: 4.5,
                    to: 6.5,
                    color: 'rgba(68, 170, 213, 0.1)'
                }]*/
            },
            yAxis: [
                {
                    title: {
                        text: '次数'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }],
                    floor: 0
                    //opposite: true
                },
                {
                    title: {
                        text: '分数'
                    },
                    plotLines: [{
                        value: 0,
                        width: 1,
                        color: '#808080'
                    }],
                    floor: 0,
                    opposite: true
                }],
            colors: ['#3084c3', '#17d9b8'],
            tooltip: {
                shared: true,
                valueSuffix: ' units'
            },
            credits: {
                enabled: false
            },
            plotOptions: {
                areaspline: {
                    fillOpacity: 0.0
                }
            },
            series: [{
                name: '袋',
                data: bagCount,
                tooltip: {
                    valueSuffix: '袋'
                }
            }, {
                name: '容量',
                data: bagVol,
                yAxis: 1,
                tooltip: {
                    valueSuffix: 'ml'
                }
            }]
            /*series: [{
                name: 'John',
                data: [3, 4, 3, 5, 4, 10, 12]
            }, {
                name: 'Jane',
                data: [1, 3, 4, 3, 3, 5, 4]
            }]*/
        });


        Highcharts.getOptions().plotOptions.pie.colors = (function () {
            var colors = [],
                base = Highcharts.getOptions().colors[0],
                i;

            for (i = 0; i < 10; i += 1) {
                // Start out with a darkened base color (negative brighten), and end
                // up with a much brighter color
                colors.push(Highcharts.Color(base).brighten((i - 3) / 7).get());
            }
            return colors;
        }());
        $('#workloadPercent').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                width: 400,
                height: 200
            },
            title: {
                text: '分数'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            },
            series: [{
                /*['皮试', '备药', '配液', '穿刺', '接瓶', '巡视',
                 '拔针', '报警处理']*/
                type: 'pie',
                name: '分数',
                data: panPercentData
            }]
        });

        $('#workloadCount').highcharts({
            chart: {
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                width: 400,
                height: 200
            },
            title: {
                text: '次数'
            },
            tooltip: {
                pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                        style: {
                            color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                        }
                    }
                }
            },
            series: [{
                /*['皮试', '备药', '配液', '穿刺', '接瓶', '巡视',
                 '拔针', '报警处理']*/
                type: 'pie',
                name: '次数',
                data: panTimeData
            }]
        });
    });
}

var dateToggle = {
    keyWord: 'week',
    dateNow: (function () {
        return new Date();
    })(),

    dateStart: null,
    dateEnd: null,

    init: function () {
        this.setWeek();
    },

    setYear: function (m) {
        this.keyWord = 'year';
        var _time = this.dateNow,
            timeStart = new Date(),
            timeEnd = new Date();

        timeStart.setMonth(0);
        timeEnd.setMonth(0);
        timeStart.setDate(1);
        timeEnd.setDate(1);
        timeEnd.setYear(_time.getUTCFullYear() + 1);
        timeEnd.setDate(timeEnd.getDate() - 1);

        this.setValue(timeStart, timeEnd);
    },

    setSeason: function (m) {
        this.keyWord = 'season';
        var _time = this.dateNow,
            timeStart = new Date(),
            timeEnd = new Date(),
            currentMonth = timeStart.getMonth();

        if (0 <= currentMonth <= 2) {
            timeStart.setMonth(0);
            timeStart.setDate(1);
            timeEnd.setDate(1);
            timeEnd.setMonth(3);
            timeEnd.setDate(timeEnd.getDate() - 1);
        }
        if (3 <= currentMonth <= 5) {
            timeStart.setMonth(3);
            timeStart.setDate(1);
            timeEnd.setDate(1);
            timeEnd.setMonth(6);
            timeEnd.setDate(timeEnd.getDate() - 1);
        }
        if (6 <= currentMonth <= 8) {
            timeStart.setMonth(6);
            timeStart.setDate(1);
            timeEnd.setDate(1);
            timeEnd.setMonth(9);
            timeEnd.setDate(timeEnd.getDate() - 1);
        }
        if (9 <= currentMonth <= 11) {
            timeStart.setMonth(9);
            timeStart.setDate(1);
            timeEnd.setDate(1);
            timeEnd.setMonth(0);
            timeEnd.setYear(timeEnd.getUTCFullYear() + 1);
            timeEnd.setDate(timeEnd.getDate() - 1);
        }

        this.setValue(timeStart, timeEnd);
    },

    setMonth: function (m) {
        this.keyWord = 'month';
        var _time = this.dateNow,
            timeStart = new Date(),
            timeEnd = new Date();

        if (!this.dateStart) {

        }

        timeStart.setDate(1);
        timeEnd.setDate(1);

        timeEnd.setMonth(_time.getMonth() + 1);
        timeEnd.setDate(timeEnd.getDate() - 1);
        this.setValue(timeStart, timeEnd);
    },

    setWeek: function (m) {
        this.keyWord = 'week';
        log('\n');
        var _time = this.dateNow,
            timeStart = this.dateStart || new Date(),
            timeEnd = this.dateEnd || new Date(),
            dateStart = timeStart.getDate() - timeStart.getDay(),
            dateEnd = dateStart + 6;

        /*if (timeEnd.getMonth() !== timeStart.getMonth()) {
            dateEnd = dateEnd - dateStart + 6;
        }*/

        //this.dateStart = this.dateStart || new Date().setDate(dateStart);
        log(dateStart + '：' + dateEnd);
        log(timeStart.format('yyyy-MM-dd') + '：' + timeEnd.format('yyyy-MM-dd'));

        switch (m) {
            case 'add':
                dateStart = dateEnd + 1;
                if (timeEnd.getMonth() !== timeStart.getMonth()) {
                    timeStart.setDate(dateStart);
                    timeEnd.setDate(timeStart.getDate() + 6);
                } else {
                    dateEnd = dateStart + 6;
                    timeStart.setDate(dateStart);
                    timeEnd.setDate(dateEnd);
                }

                break;

            case 'sub':
                dateEnd = dateStart - 1;
                dateStart = dateEnd - 6;
                if (timeEnd.getMonth() !== timeStart.getMonth()) {
                    timeStart.setDate(dateStart);
                    if (timeEnd.getFullYear() !== timeStart.getFullYear()) {
                        timeEnd.setFullYear(timeStart.getFullYear());
                    }
                    timeEnd.setMonth(timeStart.getMonth());
                    timeEnd.setDate(dateEnd);
                } else {
                    timeStart.setDate(dateStart);
                    timeEnd.setDate(dateEnd);
                }
                break;

            default :
                timeStart.setDate(dateStart);
                timeEnd.setDate(dateEnd);
                break;
        }

        log('------------------');

/*        timeStart.setDate(dateStart);
        timeEnd.setDate(dateEnd);*/
        this.setValue(timeStart, timeEnd);
        log(dateStart + '：' + dateEnd);
        log(timeStart.format('yyyy-MM-dd') + '：' + timeEnd.format('yyyy-MM-dd'));

    },

    setValue: function (timeStart, timeEnd) {
        var _timeWrapper = $('.time-label'),
            str = (this.keyWord === 'year' && timeStart.format("yyyy/MM") + ' - ' + timeEnd.format("yyyy/MM")) ||
                timeStart.format("yyyy/MM/dd") + ' - ' + timeEnd.format("yyyy/MM/dd");
        _timeWrapper.html(str);
        this.dateStart = timeStart/*.format('yyyy-MM-dd')*/;
        this.dateEnd = timeEnd/*.format('yyyy-MM-dd')*/;

        //log(this.dateStart, this.dateEnd);
    },

    dateFlow: function (method) {
        var word = this.keyWord;

        switch (word) {
            case 'week':
                this.setWeek(method);
                break;

            case 'month':
                this.setMonth(method);
                break;

            case 'season':
                this.setSeason(method);
                break;

            case 'year':
                this.setYear(method);
                break;

            default :
                return;
        }
    }
};

$(function () {

    var recordDay = new Date().format("yyyy/MM/dd"),
        year = new Date().getFullYear(),
        lastmonth = new Date().getMonth() - 1,
        yesterday = new Date().getDay();

    $("#startDate").val(new Date(year, lastmonth, yesterday).format("yyyy/MM/dd"));
    $("#endDate").val(recordDay);

    setChart('all', $("#startDate").val(), $("#endDate").val());

    dateToggle.init();

    $.get(addDeviceInfo).done(function (data) {
        log(data);
        var list = data['data']['list'],
            str = '<option selected="selected" value="all">--全部--</option>',
            bedArray = [];

        $.each(list, function (i, val) {
            var bedList = JSON.stringify(val['bedLstFormat']);
            //log(typeof bedList);
            str += '<option data-deptcode=' + val['code'] + ' data-bedlist=' + bedList + ' value=' + val['id'] + '>' + val['name'] + '</option>';
        });

        $selection.html(str);
        //log(str);

    });




    // 查找科室数据
    $('#search').on('click', function (e) {
        var deptCode = $('#departmentList').val(),
            sDate = $('#startDate').val(),
            eDate = $('#endDate').val();
        setChart(deptCode, sDate, eDate);
    });

    $('.sort-list').on('click', 'a', function (e) {
        var $this = $(this),
            _id = $this.attr('id');
        log(_id);

        function toggleClass(context, className) {
            $(context).addClass(className).parent().siblings().children('a').removeClass(className);
        }

        toggleClass(this, 'on');

        switch (_id) {
            case 'by-week':
                dateToggle.setWeek();
                break;

            case 'by-month':
                dateToggle.setMonth();
                break;

            case 'by-season':
                dateToggle.setSeason();
                break;

            case 'by-year':
                dateToggle.setYear();
                break;

            default :
                return false;
        }

    });

    $('#date-sort').on('click', 'a', function (e) {
        var $this = $(this),
            _id = $this.attr('id');

        switch (_id) {
            case 'prev':
                dateToggle.dateFlow('sub');
                break;

            case 'next':
                dateToggle.dateFlow('add');
                break;
        }
    })
});