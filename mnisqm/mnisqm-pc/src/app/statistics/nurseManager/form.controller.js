(function() {
  "use strict";

  angular.module('nqms')
    .controller('NurseManagerStatFormCtrl', function($scope, $interval, $timeout, $echarts, chartsService) {
      var vm = this;
      var levelData = [
        { value: 12, name: '专科护士' },
        { value: 116, name: '高级责任护士' },
        { value: 213, name: '责任护士' },
        { value: 198, name: '助理护士' }
      ];
      var eduData = [
        { value: 167, name: '专科' },
        { value: 207, name: '本科' },
        { value: 129, name: '硕士' },
        { value: 3, name: '博士' }
      ];

      var ageData = [
        { value: 279, name: '21~30' },
        { value: 207, name: '31~40' },
        { value: 79, name: '41~50' },
        { value: 23, name: '其他' }
      ];

      var genderData = [
        { value: 478, name: '女' },
        { value: 13, name: '男' }
      ];

      var politicalData = [
        { value: 367, name: '群众' },
        { value: 163, name: '党员' }
      ];

      vm.level = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('人员层级')
      };
      vm.edu = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('学历')
      };
      vm.age = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('年龄')
      };
      vm.gender = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('性别')
      }
      vm.political = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: chartsService.pieInit('政治面貌')
      }

      load();

      function load() {
        var levelConfig = vm.level.config;
        levelConfig.legend.data = _.pluck(levelData, 'name');
        levelConfig.series[0].data = levelData;
        var eduConfig = vm.edu.config;
        eduConfig.legend.data = _.pluck(eduData, 'name');
        eduConfig.series[0].data = eduData;
        var ageConfig = vm.age.config;
        ageConfig.legend.data = _.pluck(ageData, 'name');
        ageConfig.series[0].data = ageData;
        var genderConfig = vm.gender.config;
        genderConfig.legend.data = _.pluck(genderData, 'name');
        genderConfig.series[0].data = genderData;
        var politicalConfig = vm.political.config;
        politicalConfig.legend.data = _.pluck(politicalData, 'name');
        politicalConfig.series[0].data = politicalData;
      }
    })
})();
