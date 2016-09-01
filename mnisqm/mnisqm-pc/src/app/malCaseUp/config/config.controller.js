(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('MalCaseConfigController', malCaseConfigController);
  /** @ngInject */
  function malCaseConfigController($scope, $log, $filter, malCaseService, modalService) {
    var vm = this;
    //数据初始化
    // initData();
    vm.load = {
      func: initData,
      init: true
    }

    vm.openConfirm = openConfirm;
    vm.deleteType = deleteType;

    function initData() {
      return malCaseService.getEventType().then(function(response) {
        if (response) {
          vm.list = response.data.data.lst;
        }
      });
    }

    // 删除事件类型
    function deleteType(seqId) {
      return malCaseService.deleteType(seqId).then(function (response) {
        if (response) {
          return response;
        }
      })
    }

    function openConfirm(seqId) {
      modalService.open({
        templateUrl: 'app/common/confirm/confirm.tpl.html',
        size: 'sm',
        ok: deleteType,
        data: {
          metaData: {
            title: '确定删除该类型吗？'
          },
          formData: seqId
        }
      })
    }

  }
})();
