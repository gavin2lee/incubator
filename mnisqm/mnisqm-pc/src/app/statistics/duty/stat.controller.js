(function() {
  "use strict";

  angular.module('nqms')
    .controller('DutyStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
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
        datepickerMode: 'month',
        minMode: 'month'
      };

      var config = chartsService.horBarInit(['工作时间', '请假时间', '夜班时间']);
      vm.duty = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: config
      };

      function load() {
        render();
      }

      function render() {
        var config = vm.duty.config;
        var data = random();
        config.yAxis[0].data = _.keys(data);
        config.series[0].data = _.pluck(_.values(data), 0);
        config.series[1].data = _.pluck(_.values(data), 1);
        config.series[2].data = _.pluck(_.values(data), 2);
        $echarts.updateEchartsInstance(vm.duty.identity, config);
      }

      function random() {
        vm.baseData = {
          total: _.random(400, 500),
          leave: _.random(20, 100),
          shift: _.random(5, 30),
          refuse: _.random(5, 30)
        }
        var names = ['何诗华', '刘铭杨', '李向宾', '徐一帆', '赵圳', '夏琼', '周荔'];
        var data = {};
        names.forEach(function(v) {
          data[v] = [_.random(100, 200), _.random(0, 60), _.random(0, 100)];
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
