(function() {
  "use strict";

  angular.module('nqms')
    .controller('BedNursingLiqDisStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
      var vm = this;
      vm.filter = {
        date: new Date()
      };

      loadDept();

      $scope.$watch('vm.filter.date', function(v) {
        load();
      });

      $scope.$watch('vm.filter.deptCode', function(v) {
        load();
      });

      vm.dateOptions = {
        datepickerMode: 'year',
        minMode: 'year'
      };

      vm.chart = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: {
          title: {
            x: 'center',
            text: '配液统计'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            top: '30',
            data: ['备药', '审核', '配液', '配液总量']
          },
          xAxis: [{
            type: 'category',
            data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
          }],
          yAxis: [{
            type: 'value'
          }],
          series: [{
            name: '备药',
            type: 'bar',
            data: []
          }, {
            name: '审核',
            type: 'bar',
            data: []
          }, {
            name: '配液',
            type: 'bar',
            data: []
          }, {
            name: '配液总量',
            type: 'line',
            data: []
          }]
        }
      };

      function load() {
        render();
      }

      function render() {
        var data = random();
        var config = vm.chart.config;
        config.series[0].data = _.pluck(_.values(data), 0);
        config.series[1].data = _.pluck(_.values(data), 1);
        config.series[2].data = _.pluck(_.values(data), 2);
        config.series[3].data = _.pluck(_.values(data), 3);
        $echarts.updateEchartsInstance(vm.chart.identity, config);
      }

      function random() {
        var names = vm.chart.config.xAxis[0].data;
        var data = {};
        names.forEach(function(v) {
          data[v] = [_.random(500, 2000), _.random(500, 2000), _.random(500, 2000)];
          data[v][3] = data[v][0] + data[v][1] + data[v][2];
        });
        return data;
      }

      function loadDept() {
        return deptService.getDeptData().then(function(data) {
          vm.deptData = data;
          vm.getDeptName = _.memoize(function(wardCode) {
            var name;
            _.some(data.belongedDepts, function(item) {
              if (item.deptCode == wardCode) {
                name = item.deptName;
                return true;
              }
            });
            name = name.replace('护士站', '');
            return name;
          });
        });
      }
    })
})();
