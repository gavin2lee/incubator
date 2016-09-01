(function() {
  "use strict";

  angular.module('nqms')
    .controller('QualityStatCtrl', function($scope, $interval, $timeout, $echarts, chartsService, deptService) {
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
        datepickerMode: 'year',
        minMode: 'year'
      };
      var tables = [{
        name: '临床护理表',
        categories: ['书写标准', '签名正规', '人数相符', '运用医学术语', '特殊用药']
      }, {
        name: '危重护理表',
        categories: ['按时检查', '及时反馈', '上报情况', '特殊情况']
      }];
      var legends = ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'];

      var config = chartsService.radarInit('护理质控', legends);
      config.tooltip.formatter = function(params) {
        var index = config.series[params.seriesIndex].radarIndex || 0;
        var indicators = config.radar[index].indicator;
        var result = params.seriesName + ' ' + params.name;
        indicators.forEach(function(v, i) {
          result += '<br>' + v.text + ' : ' + params.value[i];
        });
        return result;
      }
      tables.forEach(function(v, i) {
        var indicator = [];
        v.categories.forEach(function(v) {
          indicator.push({
            text: v,
            max: 100
          });
        });
        config.radar.push({
          indicator: indicator,
          center: [(25 + 50 * i) + '%', '50%'],
          radius: 100
        });
        config.series.push(_.extend({ radarIndex: i, name: v.name }, angular.copy(config.serie)));
      })
      vm.quality = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: config
      };


      function load() {
        render();
      }

      function render() {
        var data = random();
        var config = vm.quality.config;
        tables.forEach(function(tv, ti) {
          legends.forEach(function(v, i) {
            var value = _.pluck(_.values(data[ti]), i);
            config.series[ti].data[i] = {
              value: value,
              name: v
            };
          });
        });
        console.log(config)
        $echarts.updateEchartsInstance(vm.quality.identity, config);
      }

      function random() {
        var result = [];
        tables.forEach(function(v) {
          var data = {};
          v.categories.forEach(function(v) {
            data[v] = [_.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100), _.random(0, 100)];
          });
          result.push(data);
        });
        return result;
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
