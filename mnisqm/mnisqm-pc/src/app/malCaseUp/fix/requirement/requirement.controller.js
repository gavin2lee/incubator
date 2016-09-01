(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('RequirementController', requirementController);

  function requirementController($rootScope, $scope, $q, $state, $stateParams, security, moment, levelService, modalService, fixService) {
    var vm = this;

    vm.rootScope = $rootScope;
    vm.formData = {};
    vm.scope = $scope;
    vm.resetForm = resetForm;
    vm.fillItem = fillItem;
    vm.del = del;
    vm.save = save;
    vm.load = {
      func: initData,
      init: true
    };
    initData();

    function initData() {
      return getEventRequirement()
    }

    // 获取整改要求
    function getEventRequirement() {
      fixService.getEventRequirement({
        reportCode: $stateParams.reportCode
      }).then(function(response) {
        vm.requirementList = response.data.data.lst;
      });
    }

    // 保存整改要求
    function save() {
        var data = angular.merge({}, vm.formData);

        angular.extend(data, {
          userCode: security.getUserinfo().user.userCode,
          reportCode: $stateParams.reportCode
        });
        // return;
        fixService.saveRequirement(data).then(function(response) {
          if (response && response.data.code === '0') {
            $rootScope.$broadcast('toast', {
              type: 'success',
              content: '保存成功！'
            });
            modalService.close('success');

            vm.load.func();
          }
        });
    }

    // 清空
    function resetForm() {
      vm.formData = {};
    }

    // 选中
    function fillItem(requirement) {
      vm.formData = angular.merge({}, requirement);
    }

    // 删除
    function del(params) {
      var _params;

          _params = {
            seqId: params.seqId
          };

      fixService['deleteRequirement'](_params).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '已删除！'
          });

          vm.load.func();
        }
      });
    }
  }
})();
