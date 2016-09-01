(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('BDDeptUseController', BDDeptUseController);

    function BDDeptUseController($echarts, $state, chartsService, deptService) {
      var vm = this;
      var cates = ['核对', '护记', '评估单'];
      vm.catesData = [];
      
      var config = chartsService.verBarInit(cates);
      vm.deptUse = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:6',
        config: config
      };
      vm.options = {
        month: {
          datepickerMode: 'month',
          minMode: 'month'
        }
      };
      vm.queryParams = {
        month: new Date()
      };

      deptService.getDeptData().then(function(data) {
        vm.depts = handleDataDept(data);
        render();
      });

      function render() {
        var config = vm.deptUse.config;
        var data = random();
        cates.forEach(function(v, i) {
          vm.catesData[i] = {
            text: v,
            data: _.pluck(data, i)
          }
        });
        config.xAxis[0].data = _.keys(data);
        config.series[0].data = _.pluck(_.values(data), 0);
        config.series[1].data = _.pluck(_.values(data), 1);
        config.series[2].data = _.pluck(_.values(data), 2);
        $echarts.updateEchartsInstance(vm.deptUse.identity, config);
      }

      function random() {
        vm.names = _.sample(_.pluck(vm.depts, 'deptName'), 8)
        var data = {};
        vm.names.forEach(function(v) {
          data[v] = [_.random(100, 200), _.random(10, 100), _.random(10, 100)];
        });
        return data;
      }

      function handleDataDept(data) {
        var result = [];
        result = result.concat([_.pick(data, 'deptCode', 'deptName')]);
        _.each(data.belongedDepts, function(v) {
          result = result.concat(handleDataDept(v));
        });
        return result;
      }
    }

})();