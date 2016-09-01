(function() {
  "use strict";

  angular.module('nqms')
    .controller('BedNursingDocStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
      var vm = this;
      vm.filter = {
        date: new Date()
      };
      var names = ['体温单', '护理记录单', '压疮报告单', '跌倒评估单', '疼痛评估单', '自理能力评估单'];
      vm.charts = [];

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

      names.forEach(function(v) {
        vm.charts.push({
          identity: $echarts.generateInstanceIdentity(),
          dimension: '4:3',
          config: chartsService.pieInit(v + '合格率')
        })
      });

      function load() {
        render();
      }

      function render() {
        vm.charts.forEach(function(v, i) {
          var data = random();
          var config = v.config;
          config.series[0].data = data;
          config.title.text = config.title.text.slice(0, names[i].length + 3) + ' ' + _.random(50, 100) + '%';
          $echarts.updateEchartsInstance(v.identity, config);
        })
      }

      function random() {
        vm.baseData = {
          miss: _.random(3, 12) + '%',
          qualified: _.random(70, 98) + '%'
        }
        var data = [];
        data.push({
          name: '遗漏',
          value: _.random(0, 500)
        });
        data.push({
          name: '修改',
          value: _.random(0, 500)
        });
        data.push({
          name: '新增',
          value: _.random(2000, 3000)
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
