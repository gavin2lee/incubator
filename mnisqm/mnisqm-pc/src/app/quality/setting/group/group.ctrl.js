(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityGroupCtrl', QualityGroupCtrl);

  /** @ngInject */
  function QualityGroupCtrl($rootScope, $uibModal, qualityGroupService, comService) {
    var vm = this;
    vm.open = open;
    vm.del = del;

    vm.load = {
      func: initData,
      init: true
    };

    comService.getUserHelper(true).then(function(data) {
      vm.userHelper = data;
    });

    function initData() {
      return qualityGroupService.getGroup().then(function(rsp) {
        vm.list = rsp.data.data;
      });
    }

    function open(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/quality/setting/group/group.modal.html',
        controller: 'QualityAddGroupCtrl',
        controllerAs: 'vm',
        backdrop: 'static',
        animation: true,
        size: 'lg',
        resolve: {
          id: function() {
            return angular.copy(data);
          }
        }
      });

      modalInstance.result.finally(function() {
        vm.load.func();
      });
    }

    function del(id) {
      qualityGroupService.deleteGroup(id).then(function() {
        $rootScope.$broadcast('toast');
        vm.load.func();
      })
    }

  }
})();



(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityAddGroupCtrl', QualityAddGroupCtrl);

    function QualityAddGroupCtrl($rootScope, $uibModalInstance, security, comService, qualityGroupService, id) {
      var vm = this;
      vm.cancel = cancel;
      vm.ok = ok;
      vm.choose = choose;
      vm.remove = remove;
      vm.formData = id || {};

      comService.getUserHelper().then(function(data) {
        vm.grouped = _.groupBy(data.users, function(v) {
          return _.contains(vm.formData.userList, v.userCode) ? 'yes': 'no';
        });
        vm.grouped.yes || (vm.grouped.yes = []);
        vm.grouped.no || (vm.grouped.no = []);
      })

      function cancel() {
        $uibModalInstance.dismiss();
      }

      function ok() {
        vm.formData.userList = _.pluck(vm.grouped.yes, 'userCode');
        qualityGroupService.saveGroup(vm.formData).then(function() {
          $rootScope.$broadcast('toast');
          $uibModalInstance.close();
        });
      }

      function choose(v) {
        var i = vm.grouped.no.indexOf(v);
        vm.grouped.yes.push(vm.grouped.no.splice(i, 1)[0]);
      }

      function remove(v) {
        var i = vm.grouped.yes.indexOf(v);
        vm.grouped.no.push(vm.grouped.yes.splice(i, 1)[0]);
        if(vm.formData.teamLeader && v.userCode === vm.formData.teamLeader) {
          vm.formData.teamLeader = '';
        }
      }
    }
})();