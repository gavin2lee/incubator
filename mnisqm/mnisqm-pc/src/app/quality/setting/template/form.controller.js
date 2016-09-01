(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityFormController', formControllerFunc);

  /** @ngInject */
  function formControllerFunc($state, $rootScope, $scope, formService, security, deptService) {
    var vm = this;

    vm.options = {
      month: {
        datepickerMode: 'month',
        minMode: 'month'
      }
    };

    vm.del = del;
    vm.load = {
      func: initData,
      init: true
    };

    function initData() {
      return formService.getForm().then(function(rsp) {
        vm.list = rsp.data.data;
      });
    }

    function del(seqId) {
      formService.deleteForm(seqId).then(function(response) {
        $rootScope.$broadcast('toast');
        vm.load.func();
      });
    }
  }



})();
