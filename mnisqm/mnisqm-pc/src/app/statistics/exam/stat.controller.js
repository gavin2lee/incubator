(function() {
  "use strict";

  angular.module('nqms')
    .controller('ExamStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
      var vm = this;
      vm.filter = {
        date: new Date()
      };

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

      vm.exam = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('考试')
      };

      vm.train = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('培训')
      };

      function load() {
        render();
      }

      function render() {
        var exam = random('exam'),
          train = random('train');
        var examConfig = vm.exam.config;
        examConfig.legend.data = _.pluck(exam, 'name');
        examConfig.series[0].data = exam;
        var trainConfig = vm.train.config;
        trainConfig.legend.data = _.pluck(train, 'name');
        trainConfig.series[0].data = train;
        $echarts.updateEchartsInstance(vm.exam.identity, examConfig);
        $echarts.updateEchartsInstance(vm.train.identity, trainConfig);
      }

      function random(type) {
        var total, data = [];
        if (type === 'exam') {
          data.push({
            name: '优秀',
            value: _.random(100, 200)
          });
          data.push({
            name: '良好',
            value: _.random(200, 300)
          });
          data.push({
            name: '及格',
            value: _.random(100, 200)
          });
          data.push({
            name: '不及格',
            value: _.random(0, 100)
          });
        } else {
          data.push({
            name: '签到',
            value: _.random(400, 500)
          });
          data.push({
            name: '缺勤',
            value: _.random(0, 100)
          });
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

    });

})();
