(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('chartsService', chartsService);

  function chartsService($log) {
    var pie = {
        title: {
          x: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: "{b} : {c} ({d}%)"
        },
        legend: {
          orient: 'horizontal',
          bottom: 'bottom',
          data: []
        },
        series: [{
          type: 'pie',
          radius: '55%',
          center: ['50%', '50%'],
          label: {
            normal: {
              show: true,
              formatter: "{b} : {c} ({d}%)"
            }
          },
          itemStyle: {
            emphasis: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          },
          data: []
        }]
      },
      horBar = {
        grid: {
          top: 60,
          right: '2%',
          left: '2%',
          bottom: '2%',
          containLabel: true
        },
        legend: {
          orient: 'horizontal',
          top: 0,
          data: []
        },
        xAxis: [{
          type: 'value'
        }],
        yAxis: [{
          type: 'category',
          axisTick: { show: false },
          data: []
        }],
        serie: {
          type: 'bar',
          label: {
            normal: {
              show: true
            }
          },
          data: []
        },
        series: []
      },
      verBar = {
        grid: {
          top: 60,
          right: '2%',
          left: '2%',
          bottom: '2%',
          containLabel: true
        },
        legend: {
          orient: 'horizontal',
          top: 0,
          data: []
        },
        xAxis: [{
          type: 'category',
          axisTick: { show: false },
          data: []
        }],
        yAxis: [{
          type: 'value'
        }],
        serie: {
          type: 'bar',
          label: {
            normal: {
              show: true
            }
          },
          data: []
        },
        series: []
      },
      line = {
        title: {
          x: 'center'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: [],
          top: '30'
        },
        grid: {
          left: '3%',
          right: '3%',
          top: '80',
          containLabel: true
        },
        xAxis: [{
          type: 'category',
          boundaryGap: false,
          data: []
        }],
        yAxis: [{
          type: 'value'
        }],
        series: [],
        serie: {
          type: 'line',
          data: []
        }
      },
      radar = {
        title: {},
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          x: 'center',
          data: [],
          top: '30'
        },
        grid: {
          left: '3%',
          right: '3%',
          top: '80',
          containLabel: true
        },
        radar: [],
        series: [],
        serie: {
          type: 'radar',
          tooltip: {
            trigger: 'item'
          },
          itemStyle: { normal: { areaStyle: { type: 'default' } } },
          data: []
        }
      },
      config = {
        pie: pie,
        horBar: horBar,
        verBar: verBar,
        line: line,
        radar: radar
      },
      service = {
        pieInit: pieInit,
        horBarInit: horBarInit,
        verBarInit: verBarInit,
        lineInit: lineInit,
        radarInit: radarInit
      };
    return service;

    function pieInit(title) {
      var pie = config.pie,
        raw = angular.copy(pie, {});
      raw.title.text = title;
      return raw;
    }

    function horBarInit(legends) {
      var bar = config.horBar,
        raw = angular.copy(bar, {});
      legends.forEach(function(v) {
        var item = angular.copy(raw.serie, {});
        item.name = v;
        raw.series.push(item);
      });
      raw.legend.data = legends;
      delete raw.serie;
      return raw;
    }

    function verBarInit(legends) {
      var bar = config.verBar,
        raw = angular.copy(bar, {});
      legends.forEach(function(v) {
        var item = angular.copy(raw.serie, {});
        item.name = v;
        raw.series.push(item);
      });
      raw.legend.data = legends;
      delete raw.serie;
      return raw;
    }

    function lineInit(title, legends) {
      var line = config.line,
        raw = angular.copy(line, {});
      if (legends) {
        legends.forEach(function(v) {
          var item = angular.copy(raw.serie, {});
          item.name = v;
          raw.series.push(item);
        });
        raw.legend.data = legends;
      }
      raw.title.text = title;
      delete raw.serie;
      return raw;
    }

    function radarInit(title, legends) {
      var radar = config.radar,
        raw = angular.copy(radar, {});
      raw.legend.data = legends;
      raw.title.text = title;
      return raw;
    }
  }

})();
