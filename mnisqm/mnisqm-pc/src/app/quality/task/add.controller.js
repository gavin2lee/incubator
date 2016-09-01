(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityTaskAddController', QualityTaskAddController);

    function QualityTaskAddController($uibModal, $rootScope, $stateParams, qualityGroupService, formService, taskService, comService) {
      var vm = this;
      vm.userFilter = '';
      vm.del = del;
      vm.open = open;
      vm.load = {
        func: load,
        init: true
      };

      comService.getHelper(true).then(function(data) {
        vm.helper = data;
      });

      formService.getForm().then(function(rsp) {
        vm.models = rsp.data.data;
      });

      function load() {
        return taskService.getPlan({
          seqId: +$stateParams.id
        }).then(function(rsp) {
          vm.detail = rsp.data.data[0];
          if(vm.groups) {
            vm.currentTeam = _.findWhere(vm.groups, {
              teamName: vm.detail.teamName
            });
          }else {
            qualityGroupService.getGroup().then(function(rsp) {
              vm.groups = rsp.data.data;
              vm.currentTeam = _.findWhere(vm.groups, {
                teamName: vm.detail.teamName
              });
            });
          }
        })
      }

      function del(id) {
        taskService.deleteTask(id).then(function() {
          vm.load.func();
        });
      }

      function open(data, i) {
        var modalInstance = $uibModal.open({
          templateUrl: 'app/quality/task/add.modal.html',
          controller: 'QualityTaskAddModalController',
          controllerAs: 'vm',
          backdrop: 'static',
          animation: true,
          size: 'md',
          resolve: {
            id: function() {
              return {
                data: angular.copy(data),
                userList: vm.currentTeam.userList,
                plan: vm.detail.seqId,
                model: vm.detail.modelId
              }
            }
          }
        });

        modalInstance.result.then(function(task) {
          vm.load.func();
        });
      }
    }

})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityTaskAddModalController', QualityTaskAddModalController);

    function QualityTaskAddModalController($uibModalInstance, id, comService, taskService) {
      var vm = this;
      vm.cancel = cancel;
      vm.ok = ok;
      vm.formData = id.data || {};
      vm.userList = id.userList;
      vm.formData.planId || (vm.formData.planId = id.plan);
      vm.formData.formCode || (vm.formData.formCode = id.model);

      comService.getHelper().then(function(data) {
        vm.depts = data.dept.depts;
        vm.user = data.user;
      })

      function cancel() {
        $uibModalInstance.dismiss();
      }

      function ok() {
        taskService.saveTask(vm.formData).then(function() {
          $uibModalInstance.close();
        });
      }
    }

})();