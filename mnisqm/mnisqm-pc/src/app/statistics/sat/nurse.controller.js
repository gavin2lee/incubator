(function() {
  "use strict";

  angular.module('nqms')
    .controller('NurseSatStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
      var vm = this;
      vm.filter = {
        date: new Date()
      };
      vm.lst = [{
        key: "工作环境"
      }, {
        key: "工作氛围"
      }, {
        key: "个人发展"
      }, {
        key: "工资水平"
      }];

      loadDept();

      $scope.$watch('vm.filter.date', function(v) {
        load();
      });

      $scope.$watch('vm.filter.type', function(v) {
        load();
      });

      $scope.$watch('vm.filter.deptCode', function(v) {
        load();
      });

      vm.dateOptions = {
        datepickerMode: 'month',
        minMode: 'month'
      };

      vm.sat = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('护士满意度')
      };

      function load() {
        render();
      }

      function render() {
        var sat = random('sat');
        var satConfig = vm.sat.config;
        satConfig.legend.data = _.pluck(sat, 'name');
        satConfig.series[0].data = sat;
        $echarts.updateEchartsInstance(vm.sat.identity, satConfig);
      }

      function random(type) {
        vm.lst.forEach(function(v) {
          v.value = _.random(5, 10)
        });
        var data = [];
        data.push({
          name: '满意',
          value: _.random(2000, 3000)
        });
        data.push({
          name: '良好',
          value: _.random(1000, 2000)
        });
        data.push({
          name: '不满意',
          value: _.random(0, 1000)
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

    });

})();
