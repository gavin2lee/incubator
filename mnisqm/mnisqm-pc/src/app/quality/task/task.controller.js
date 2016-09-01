(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityTaskController', taskControllerFunc);

  /** @ngInject */
  function taskControllerFunc($rootScope, $scope, $uibModal, taskService, formService, qualityGroupService) {
    var vm = this;

    vm.del = del;
    vm.open = open;
    vm.load = {
      func: initData,
      init: true
    };

    formService.getForm().then(function(rsp) {
      vm.models = rsp.data.data;
      vm.getModelName = function(id) {
        var target = _.findWhere(vm.models, {
          seqId: id
        });
        return target ? target.modelName : id;
      }
    });

    qualityGroupService.getGroup().then(function(rsp) {
      vm.groups = rsp.data.data;
      vm.getTeamName = function(id) {
        var target = _.findWhere(vm.groups, {
          teamCode: id
        });
        return target ? target.teamName : id;
      }
    });

    function initData() {
      return taskService.getPlan().then(function(response) {
        vm.list = response.data.data;
      });
    }

    function del(id) {
      taskService.delPlan(id).then(function() {
        $rootScope.$broadcast('toast');
        vm.load.func();
      });
    }

    function open() {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/quality/task/task.modal.html',
        controller: 'QualityTaskModalCtrl',
        controllerAs: 'vm',
        backdrop: 'static',
        animation: true,
        size: 'md'
      });

      modalInstance.result.finally(function() {
        vm.load.func();
      });
    }
  }

})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityTaskModalCtrl', QualityTaskModalCtrl);

    function QualityTaskModalCtrl($rootScope, $uibModalInstance, qualityGroupService, formService, taskService) {
      var vm = this;
      vm.ok = save;
      vm.cancel = cancel;

      qualityGroupService.getGroup().then(function(rsp) {
        vm.groups = rsp.data.data;
      });

      formService.getForm().then(function(rsp) {
        vm.models = rsp.data.data;
      });

      function save() {
        taskService.setPlan(vm.formData).then(function() {
          $rootScope.$broadcast('toast');
          $uibModalInstance.close();
        })
      }

      function cancel() {
        $uibModalInstance.dismiss();
      }
    }

})();