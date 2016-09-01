(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('SurveyController', SurveyController);
  /** @ngInject */
  function SurveyController($rootScope, $scope, $q, $state, $stateParams, $timeout, security, moment, levelService, modalService, toastr, fixService, callService, deptService, handleService) {
    var vm = this;

    vm.formData = {};
    vm.metaData = {};
    vm.formData.reportDetailList = {};
    vm.rootScope = $rootScope;
    vm.scope = $scope;
    vm.saveReportDetail = saveReportDetail;
    vm.load = {
      func: initData,
      init: true
    };

    // initData();

    // 获取分级数据
    function initData() {
      return getReportDetail()
    }

    function saveReportDetail() {
      var data = angular.merge({}, vm.formData.reportDetailList);
      var data_arr = [];
      var prop;
      debugger;
      // 将对象转成数组
      for (prop in data) {
        if (data.hasOwnProperty(prop)) {
          data_arr.push({
            reportCode: $stateParams.reportCode,
            itemCode: prop,
            seqId: data[prop].seqId || null,
            itemValue: data[prop].value
          });
        }
      }
      // return;
      handleService.saveReportDetail(data_arr).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          modalService.close('success');

          $timeout(function() {
            $state.go('home.call', null, {
              reload: true
            });
          }, 500)
        }
      });
    }

    function getReportDetail() {
      return handleService.getReportDetail({
        reportCode: $stateParams.reportCode
      }).then(function(response) {
        response.data.data.lst.forEach(function(item) {
          vm.formData.reportDetailList[item.itemCode] = {
            value: item.itemValue,
            seqId: item.seqId
          };
        });
      });
    }
  }
})();
