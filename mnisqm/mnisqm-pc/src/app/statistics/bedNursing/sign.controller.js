(function() {
  "use strict";

  angular.module('nqms')
    .controller('BedNursingSignStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
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

      var config = chartsService.lineInit('生命体征', ['体征采集总量']);
      vm.sign = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: config
      };

      function load() {
        render();
      }

      function render() {
        var data = random();
        var config = vm.sign.config;
        config.xAxis[0].data = _.keys(data);
        config.series[0].data = _.pluck(_.values(data), 0);
        $echarts.updateEchartsInstance(vm.sign.identity, config);
      }

      function random() {
        var names = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
        var data = {};
        names.forEach(function(v) {
          data[v] = [_.random(500, 2000)];
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
