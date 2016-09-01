(function() {
  "use strict";

  angular.module('nqms')
    .controller('BedNursingDoctorStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
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
            text: '医嘱统计'
          },
          tooltip: {
            trigger: 'axis'
          },
          legend: {
            top: '30',
            data: ['成功核对', '近似出错', '执行总量']
          },
          xAxis: [{
            type: 'category',
            data: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月']
          }],
          yAxis: [{
            type: 'value'
          }],
          series: [{
            name: '成功核对',
            type: 'bar',
            data: []
          }, {
            name: '近似出错',
            type: 'bar',
            data: []
          }, {
            name: '执行总量',
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
        $echarts.updateEchartsInstance(vm.chart.identity, config);
      }

      function random() {
        var names = vm.chart.config.xAxis[0].data;
        var data = {};
        names.forEach(function(v) {
          data[v] = [_.random(500, 2000), _.random(0, 500)];
          data[v][2] = data[v][0] + data[v][1];
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
