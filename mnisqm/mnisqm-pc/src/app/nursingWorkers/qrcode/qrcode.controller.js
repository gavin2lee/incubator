(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('HrQrcodeController', HrQrcodeController);

    function HrQrcodeController($rootScope, hrQrcodeService, deptService) {
      var vm = this;

      vm.search = search;

      deptService.getDeptData().then(function(data) {
        vm.deptData = handlerData(data);
      });

      function search() {
        hrQrcodeService.getUserList(vm.params).then(function(rsp) {
          vm.userData = rsp.data.data.lst;
          console.log(vm.userData);
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
