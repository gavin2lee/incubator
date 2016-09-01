(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('SurveyInfoController', surveyInfoController);

  function surveyInfoController($rootScope, $scope, $q, $state, $stateParams, security, moment, levelService, modalService, toastr, fixService) {
    var vm = this;

    vm.reportDetailList = {};
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

    // TODO 调查显示数据，不显示code
    function getReportDetail() {
      return fixService.getReportDetail({
        reportCode: $stateParams.reportCode
      }).then(function (response) {
        response.data.data.lst.forEach(function(item) {
          vm.reportDetailList[item.itemCode] = {
            value: item.itemValue,
            seqId: item.seqId
          };
        });
      });
    }
  }
})();
