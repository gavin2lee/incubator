(function() {
  'use strict';
  angular
    .module('nqms')
    .controller('MalCaseUpDisposeController', DisposeControllerFun);

  function DisposeControllerFun($rootScope, $scope, $log, $q, $stateParams, security, levelService, modalService, toastr, fixService, deptService, handleService) {
    var vm = this;

    vm.formData = {};
    vm.metaData = {};
    vm.saveReportDetail = saveReportDetail;
    vm.rootScope = $rootScope;
    vm.scope = $scope;
    vm.load = {
      func: initData,
      init: true
    }

    // initData();

    // 获取分级数据
    function initData() {
      return initModalData($stateParams.reportCode);
    }

    function initModalData(reportCode) {
      var getReportDetails = handleService.getReportDetail({
        reportCode: reportCode
      });
      var _promise = $q.all([getReportDetails]).then(function(response) {
        var reportDetails = {};
        response[0].data.data.lst.forEach(function(item) {
          reportDetails[item.itemCode] = {
            value: item.itemValue,
            seqId: item.seqId
          };
        });

        vm.formData.reportCode = reportCode;
        vm.formData.reportDetailList = reportDetails;
      });

      return _promise;
    }

    function saveReportDetail() {
      var data = angular.extend({}, vm.formData.reportDetailList);
      var data_arr = [];
      var prop;

      // 将对象转成数组
      for (prop in data) {
        if (data.hasOwnProperty(prop)) {
          data_arr.push({
            reportCode: vm.formData.reportCode,
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
          vm.scope.$broadcast('resfreshReportDetail', vm.formData.reportCode, vm);
          modalService.close('success');
        }
      });
    }

    function getReportDetail(event, reportCode, context) {
      handleService.getReportDetail({
        reportCode: reportCode
      }).then(function(response) {
        if (response) {
          context.formData.reportDetailList = response.data.data.lst;
        }
      });
    }

    // 数据刷新
    vm.scope.$on('refreshReportDetail', getReportDetail);
  }
})();
