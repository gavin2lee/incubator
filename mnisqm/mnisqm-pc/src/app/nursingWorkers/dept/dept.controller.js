(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DeptController', DeptController);

  function DeptController($rootScope, $uibModal, deptMgtService) {
    var vm = this,
      activeDeptCode;
    load();

    vm.loadDetail = loadDetail;
    vm.open = function(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/nursingWorkers/dept/dept.modal.html',
        controller: 'DeptModalController',
        backdrop: 'static',
        controllerAs: 'vm',
        animation: true,
        size: 'sm',
        resolve: {
          id: function() {
            return angular.copy(data);
          }
        }
      });

      modalInstance.result.then(function() {
        load();
      });
    }

    vm.del = function(id) {
      deptMgtService.deleteDept(id)
        .then(function() {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '操作成功'
          });
          load();
        });
    }

    vm.save = save;

    function load() {
      deptMgtService.getAllDept()
        .then(function(rsp) {
          vm.deptData = rsp.data.data;
          vm.getDeptName = _.memoize(function(wardCode) {
            var name;
            _.some(vm.deptData, function(item) {
              if (item.deptCode == wardCode) {
                name = item.deptName;
                return true;
              }
            });
            return name;
          });
        });
    }

    function loadDetail(deptCode) {
      vm.detail = [];
      activeDeptCode = deptCode;
      deptMgtService.getUserList(deptCode).then(function(data) {
        vm.detail = data;
      });
    }

    function save(item) {
      deptMgtService.saveUserDept(_.pick(item, 'userCode', 'deptCode'))
        .then(function(rsp) {
          loadDetail(activeDeptCode);
        });
    }

  }
})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DeptModalController', DeptModalController);

  function DeptModalController($rootScope, $uibModalInstance, deptMgtService, id) {
    var vm = this;
    vm.cancel = cancel;
    vm.ok = ok;
    vm.detail = id ? id : {};

    deptMgtService.getAllDept()
      .then(function(rsp) {
        vm.deptData = rsp.data.data;
      });

    function cancel() {
      $uibModalInstance.dismiss();
    }

    function ok() {
      var data = _.pick(vm.detail, 'deptCode', 'deptName', 'fatherDept', 'seqId');
      deptMgtService.saveDept(data)
        .then(function(rsp) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '操作成功'
          });
          $uibModalInstance.close();
        });
    }

  }
})();
