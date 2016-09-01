/**
 * Created by lin on 15/9/23.
 */
var log = console.log.bind(console);
var addDeviceInfo = ay.contextPath + '/nur/infusionmanager/getInfusionDepts/',
    chartUrl = ay.contextPath + '/nur/infusionmanager/getInfusionWorkloadInfo',  // getInfusionWorkloadInfo?deptCode=5042&startDate=2015-09-09&endDate=2015-09-11
    $selection = $('#departmentList');

function setChart(deptCode, sDate, eDate) {
    $.getJSON(chartUrl, {
        deptCode: deptCode || '',
        startDate: sDate || '',
        endDate: eDate || ''
    }).done(function (res) {
        log(res);
        var xTitle = [],
            operPercent = [],
            operTimes = [],
            operValues = [],
            data = res['data'],
            panTimeData = [],
            panPercentData = [];

        $.each(data['list'], function (i, v) {
            log(v);
            xTitle.push(v['workTypeName']);
            operPercent.push((v['operTimes'] / v['operValues']).toFixed(3) * 100);
            operTimes.push(v['operTimes']);
            operValues.push(v['operValues']);
            panTimeData.push([v['workTypeName'], v['operTimes']]);
            panPercentData.push([v['workTypeName'], operPercent[i]]);
        });

        log(xTitle, operPercent, operTimes, operValues);

        $('#workloadData').highcharts({
            chart: {
                type: 'column',
                zoomType: 'xy'
            },
            title: {
                text: '工作量统计图表',
                x: -20 //center
            },
            plotOptions: {
                column: {
                    grouping: false,
                    shadow: false,
                    borderWidth: 0
                }
            },
            xAxis: {
                categories: xTitle
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
                //valueSuffix: '滴／分'
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
                name: '次数',
                data: operTimes,
                tooltip: {
                    valueSuffix: '次'
                },
                pointPadding: 0.3,
                pointPlacement: -0.2
            }, {
                name: '分数',
                yAxis: 1,
                data: operPercent,
                tooltip: {
                    valueSuffix: '％'
                },
                pointPadding: 0.3,
                pointPlacement: 0.1
            }]
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

$(function () {

    var recordDay = new Date().format("yyyy-MM-dd"),
        year = new Date().getFullYear(),
        lastmonth = new Date().getMonth() - 1,
        yesterday = new Date().getDay();

    $("#startDate").val(new Date(year, lastmonth, yesterday).format("yyyy-MM-dd"));
    $("#endDate").val(recordDay);

    setChart();

    $.get(addDeviceInfo).done(function (data) {
        log(data);
        var list = data['data']['list'],
            str = '<option selected="selected" value="">--全部--</option>',
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

});