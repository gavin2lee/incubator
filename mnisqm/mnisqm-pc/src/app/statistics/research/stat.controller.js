(function() {
  "use strict";

  angular.module('nqms')
    .controller('ResearchStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
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

      vm.proj = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('科研项目')
      };

      vm.paper = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('科研论文')
      };

      function load() {
        render();
      }

      function render() {
        var proj = random('proj'),
          paper = random('paper');
        var projConfig = vm.proj.config;
        projConfig.legend.data = _.pluck(proj.data, 'name');
        projConfig.series[0].data = proj.data;
        var paperConfig = vm.paper.config;
        paperConfig.legend.data = _.pluck(paper.data, 'name');
        paperConfig.series[0].data = paper.data;
        vm.baseData = {
          proj: proj.total,
          paper: paper.total
        };
        $echarts.updateEchartsInstance(vm.proj.identity, projConfig);
        $echarts.updateEchartsInstance(vm.paper.identity, paperConfig);
      }

      function random(type) {
        var total, data = [];
        if (type === 'proj') {
          total = _.random(2, 20);
          data.push({
            name: '成功',
            value: _.random(0, total)
          });
          data.push({
            name: '失败',
            value: total - data[0].value
          });
        } else {
          total = _.random(5, 50);
          data.push({
            name: '国家级',
            value: _.random(0, total)
          });
          data.push({
            name: '省级发表',
            value: total - data[0].value
          });
        }
        return {
          total: total,
          data: data
        }
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
