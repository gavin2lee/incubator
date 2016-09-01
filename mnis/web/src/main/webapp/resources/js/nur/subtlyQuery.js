/**
 * Created by lin on 15/9/23.
 */
var log = console.log.bind(console);
$(function () {

    /*var addDeviceInfo = ay.contextPath + '/nur/infusionmanager/getInfusionDepts/',
        $selection = $('#departmentList');
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

    });*/

    /*
    * <div id="medicine-use-scale" class="subtly-item fl">

     </div>
     <div id="unusual-scale" class="subtly-item fl">

     </div>
     <div id="unusual-use-scale" class="subtly-item fl">

     </div>
     <div id="user-age-scale" class="subtly-item fl">

     </div>
     <div id="peruse-vol-scale" class="subtly-item fl">

     </div>
     <div id="use-time" class="subtly-item fl">

     </div>
     <div id="use-speed" class="subtly-item fl">

     </div>
     <div id="use-total" class="subtly-item fl">

     </div>*/

    $('#medicine-use-scale').highcharts({
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
            categories: ['皮试', '备药', '配液', '穿刺', '接瓶', '巡视',
                '拔针', '报警处理']
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
            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1],
            tooltip: {
                valueSuffix: '次'
            },
            pointPadding: 0.3,
            pointPlacement: -0.2
        }, {
            name: '分数',
            yAxis: 1,
            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5],
            tooltip: {
                valueSuffix: '％'
            },
            pointPadding: 0.4,
            pointPlacement: -0.2
        }]
    });

    $('#unusual-scale').highcharts({
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
            categories: ['皮试', '备药', '配液', '穿刺', '接瓶', '巡视',
                '拔针', '报警处理']
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
            data: [-0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1],
            tooltip: {
                valueSuffix: '次'
            },
            pointPadding: 0.3,
            pointPlacement: -0.2
        }, {
            name: '分数',
            yAxis: 1,
            data: [7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5],
            tooltip: {
                valueSuffix: '％'
            },
            pointPadding: 0.4,
            pointPlacement: -0.2
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
            data: [
                ['皮试',   45.0],
                ['备药',       26.8],
                {
                    name: '拔针',
                    y: 12.8,
                    sliced: true,
                    selected: true
                },
                ['配液',    8.5],
                ['穿刺',     6.2],
                ['巡视',   0.5],
                ['报警处理',   0.5]
            ]
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
            data: [
                ['皮试',   45.0],
                ['备药',       26.8],
                {
                    name: '拔针',
                    y: 12.8,
                    sliced: true,
                    selected: true
                },
                ['配液',    8.5],
                ['穿刺',     6.2],
                ['巡视',   0.5],
                ['报警处理',   0.5]
            ]
        }]
    });

});