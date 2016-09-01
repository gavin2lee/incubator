(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('CaseInfoController', caseInfoController);

  function caseInfoController($rootScope, $scope, $q, $state, $stateParams, security, moment, levelService, modalService, toastr, fixService, callService) {
    var vm = this;

    vm.formData = {};
    vm.metaData = {};
    vm.rootScope = $rootScope;
    vm.scope = $scope;
    vm.load = {
      func: initData,
      init: true
    };

    initData();

    function initData() {
      return getReportDetail()
    }

    function getReportDetail() {
      return callService.getDetail($stateParams.reportCode).then(function (response) {
        if (response) {
          vm.metaData.malCaseInfo = response.data.data;
        }
      });
    }
  }
})();
