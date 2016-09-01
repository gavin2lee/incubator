(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DepartmentManagerStatisticsController', statisticsControllerFunc);

  /** @ngInject */
  function statisticsControllerFunc($scope, $filter, deptService, deptStatisticsService, moment,classesService, security) {
    var vm = this;

    vm.options = {
      month: {
        datepickerMode: 'month',
        minMode: 'month'
      }
    };

    vm.queryParams = {
      type: 'leave',
      month: new Date(),
      deptCode: security.getUserinfo().role.deptCode
    };

    vm.current = vm.queryParams.type;

    vm.load = {
      func: getData,
      init: true
    };

    deptService.getDeptData().then(function(data) {
      vm.deptData = handleData(data);
    });

    vm.getData = getData;

    // init();

    $scope.$on('deptchange', function(e, code) {
      vm.queryParams.deptCode = code;
      vm.load.func();
    });

    function handleData(data) {
      var result = [];
      result = result.concat([_.pick(data, 'deptCode', 'deptName')]);
      _.each(data.belongedDepts, function(v) {
        result = result.concat(handleData(v));
      });
      return result;
    }

    /**
     * 获取统计信息
     * @param  {string} type 统计的类型
     */
    function getData() {
      vm.current = vm.queryParams.type;
      var type_temp = $filter('capitalize')(vm.queryParams.type);
      var params = angular.extend({}, vm.queryParams);
      if(params.type === 'class') {
        classesService.getClassList({
          deptCode: params.deptCode
        }).then(function(response){
            if (response) {
                vm.classesList = response.data.data.lst;
            }
        });
      }
      
      params.deptCode = params.deptCode;
      params.month = moment(params.month).format('YYYY-MM');

      switch (type_temp) {
        case 'Leave':
          delete params.type;
          break;
        case 'Days':
          delete params.type;
          break;
        default:
          delete params.type;
          break;
      }

      return deptStatisticsService['getBy' + type_temp](params).then(function(response) {
        if (response) {
          vm.statisticsData = response.data.data.lst;
        }
      })
    }
  }

})();
