(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('HrQuitController', HrQuitController);

    function HrQuitController($rootScope, hrQuitService, deptService) {
      var vm = this;

      vm.search = search;
      vm.quit = quit;

      deptService.getDeptData().then(function(data) {
        vm.deptData = handlerData(data);
        vm.getDeptName = _.memoize(function(wardCode) {
          var name;
          _.some(vm.deptData, function(item) {
            if (item.deptCode == wardCode) {
              name = item.deptName;
              return true;
            }
          });
          return name;
        });
      });

      function search() {
        return hrQuitService.getUserList(vm.params).then(function(rsp) {
          vm.userData = rsp.data.data.lst;
        });
      }

      function handlerData(data) {
        var result = [];
        result = result.concat([_.pick(data, 'deptCode', 'deptName')]);
        _.each(data.belongedDepts, function(v) {
          result = result.concat(handlerData(v));
        });
        return result;
      }

      function quit(userCode) {
        hrQuitService.quit(userCode).then(function(rsp) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '成功登记离职'
          })
        });
      }
    }
})();
