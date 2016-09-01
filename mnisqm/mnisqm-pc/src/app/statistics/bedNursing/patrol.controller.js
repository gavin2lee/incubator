(function() {
  "use strict";

  angular.module('nqms')
    .controller('BedNursingPatrolStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
      var vm = this;
      vm.filter = {
        date: new Date()
      };
      var names = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
      var level = ['特级', '一级', '二级', '三级'];
      vm.charts = [];

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

      level.forEach(function(v) {
        vm.charts.push({
          identity: $echarts.generateInstanceIdentity(),
          dimension: '16:9',
          config: chartsService.pieInit(v + '合格率')
        })
      });
      var config = chartsService.lineInit('年合格率一览');
      config.tooltip.formatter = function(v) {
        var line1 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + v[0].color + '"></span>' + vm.filter.date.getFullYear() + ' : ' + v[0].data,
          line2 = '<span style="display:inline-block;margin-right:5px;border-radius:10px;width:9px;height:9px;background-color:' + v[1].color + '"></span>' + (vm.filter.date.getFullYear() - 1) + ' : ' + v[1].data;
        return v[0].name + '<br>' + line1 + '<br>' + line2;
      }
      vm.chart = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: config
      }

      function load() {
        render();
      }

      function render() {
        vm.charts.forEach(function(v) {
          var data = random('pie');
          var config = v.config;
          config.legend.data = _.pluck(data, 'name');
          config.series[0].data = data;
          config.title.text = config.title.text.slice(0, 5) + ' ' + _.random(50, 100) + '%';
          $echarts.updateEchartsInstance(v.identity, config);
        });
        var data = random('line');
        var config = vm.chart.config;
        config.xAxis[0].data = _.keys(data);
        config.series = [{
          type: 'line',
          data: _.pluck(_.values(data), 0)
        }, {
          type: 'line',
          data: _.pluck(_.values(data), 1)
        }];
        $echarts.updateEchartsInstance(vm.chart.identity, config);
      }

      function random(type) {
        var data = [];
        if (type === 'pie') {
          data.push({
            name: '体征巡视',
            value: _.random(0, 500)
          });
          data.push({
            name: '输液巡视',
            value: _.random(0, 500)
          });
          data.push({
            name: '病房巡视',
            value: _.random(0, 500)
          });
          data.push({
            name: '护理治疗巡视',
            value: _.random(0, 500)
          });
          data.push({
            name: '遗漏巡视',
            value: _.random(0, 500)
          });
        } else {
          names.forEach(function(v) {
            data[v] = [_.random(0, 100) / 100, _.random(0, 100) / 100];
          })
        }
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
