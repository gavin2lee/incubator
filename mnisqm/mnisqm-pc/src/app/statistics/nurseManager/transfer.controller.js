(function() {
  "use strict";

  angular.module('nqms')
    .controller('NurseManagerStatTransferCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
      var vm = this;
      vm.date = new Date();

      $scope.$watch('vm.date', function(v) {
        load();
      });

      vm.dateOptions = {
        datepickerMode: 'month',
        minMode: 'month'
      };
      var config = chartsService.horBarInit(['转入', '转出']);
      vm.transfer = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '9:16',
        config: config
      };

      function load() {
        if (vm.deptData) {
          render();
        } else {
          loadDept().then(render)
        }
      }

      function render() {
        var config = vm.transfer.config;
        var data = random();
        config.yAxis[0].data = _.keys(data);
        config.series[0].data = _.pluck(_.values(data), 0);
        config.series[1].data = _.pluck(_.values(data), 1);
        $echarts.updateEchartsInstance(vm.transfer.identity, config);
      }

      function random() {
        vm.baseData = {
          total: _.random(400, 500),
          transfer: _.random(20, 100),
          increase: _.random(5, 30),
          leave: _.random(5, 30)
        }
        var deptNames = _.pluck(vm.deptData.belongedDepts, 'deptName');
        var data = {};
        deptNames.forEach(function(v) {
          data[v] = [_.random(0, 10), _.random(0, 10)];
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
