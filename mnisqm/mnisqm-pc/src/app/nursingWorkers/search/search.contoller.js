(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('HrSearchController', HrSearchController);

    function HrSearchController($rootScope, hrSearchService, deptService) {
      var vm = this;

      vm.search = search;

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
        hrSearchService.getUserList(vm.params).then(function(rsp) {
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
    }
})();
