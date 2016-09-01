(function() {
  "use strict";

  angular.module('nqms')
    .controller('MalCaseStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
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
      var types = ['跌倒', '压疮', '误床', '坠床'];
      var config = chartsService.lineInit('不良事件', types);
      var prevData = null;
      config.tooltip.trigger = 'item';
      config.tooltip.formatter = function(v) {
        console.log(v);
        var line1 = '',
          line2 = '';
        line1 = vm.filter.date.getFullYear() + ' : ' + v.data;
        line2 = (vm.filter.date.getFullYear() - 1) + ' : ' + prevData[v.name][types.indexOf(v.seriesName)];
        return v.seriesName + ' - ' + v.name + '<br>' + line1 + '<br>' + line2;
      };
      vm.mal = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: config
      };


      function load() {
        render();
      }

      function render() {
        var datas = random();
        prevData = datas[1];
        var config = vm.mal.config;
        config.xAxis[0].data = _.keys(datas[0]);
        types.forEach(function(v, i) {
          config.series[i].data = _.pluck(_.values(datas[0]), i);
        })
        $echarts.updateEchartsInstance(vm.mal.identity, config);
      }

      function random(type) {
        var names = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];
        var data = {},
          data2 = {};
        names.forEach(function(v) {
          data[v] = [_.random(10, 30), _.random(10, 30), _.random(10, 30), _.random(10, 30)];
        });
        names.forEach(function(v) {
          data2[v] = [_.random(10, 30), _.random(10, 30), _.random(10, 30), _.random(10, 30)];
        });
        return [data, data2];
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
