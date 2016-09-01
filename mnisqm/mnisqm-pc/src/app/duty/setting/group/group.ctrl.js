(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DepartmentManagerBedController', bedControllerFunc);

  /** @ngInject */
  function bedControllerFunc($rootScope, $scope, $uibModal, bedService, modalService, toastr, security) {
    var vm = this, deptCode = security.getUserinfo().role.deptCode;

    vm.list = [];
    vm.saveGroup = saveGroup;
    vm.edit = edit;
    vm.deleteGroup = deleteGroup;
    vm.formData = {};
    vm.add = add;
    vm.deptCode = security.getUserinfo().role.deptCode;
    vm.open = open;

    $scope.$on('deptchange', function(e, code) {
      deptCode = code;
      vm.load.func()
    });
    // vm.addBed = addBed;

    // initData();

    function initData() {
      return bedService.getGroupList({
        deptCode: deptCode
      }).then(function(response) {
        if (response) {
          vm.list = response.data.data.lst;
          $rootScope.$broadcast('groupreload');
        }
      });
    }

    function edit(row) {
      vm.formData = angular.extend({}, row);
      vm.selectedLevel = row;

      if (row) {
        if (row.beds) {
          vm.formData.bedList = row.beds.split(';');
        }
      }
    }

    function add() {
      vm.formData = {};
    }

    function open(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/duty/setting/group/group.modal.html',
        controller: 'AddClassCtrl',
        controllerAs: 'vm',
        backdrop: 'static',
        animation: true,
        size: 'sm',
        resolve: {
          id: function() {
            return {
              data: angular.copy(data),
              dept: deptCode
            }
          }
        }
      });

      modalInstance.result.finally(function() {
        vm.load.func();
      });
    }

    /************
     ****编辑框****
     ************/
    vm.removeFlow = removeFlow;
    vm.addFlow = addFlow;
    vm.load = {
      func: initData,
      init: true
    }

    function addFlow() {
      if (vm.flowText === '') {
        return;
      }

      vm.formData.bedList = vm.formData.bedList || [];

      vm.formData.bedList.push(vm.flowText);

      // 置空 ng-model里的值
      vm.flowText = '';
    }

    function removeFlow($index) {
      vm.formData.bedList.splice($index, 1);
    }

    function saveGroup() {
      // debugger;
      var data = angular.extend({}, vm.formData);

      data.beds = data.bedList ? data.bedList.join(';') : '';
      data.deptCode = vm.deptCode;
      // return;
      bedService.saveGroup(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          vm.load.func();
          vm.formData = {};
          modalService.close('success');
        }
      });
    }

    function deleteGroup(seqId) {
      bedService.deleteGroup(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          vm.load.func();
          //如果删除选中行，那么编辑数据清空
          if (seqId == vm.formData.seqId) {
            vm.formData = {};
          }
          modalService.close('success');
        }
      });
    }
  }
})();



(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('AddClassCtrl', AddClassCtrl);

    function AddClassCtrl($rootScope, $scope, $uibModalInstance, bedService, security, id) {
      var vm = this, deptCode = id.dept, data = id.data;
      if(data && data.beds) {
        data.bedList = data.beds.split(';');
      }
      vm.formData = data || {};

      vm.addFlow = addFlow;
      vm.removeFlow = removeFlow;
      vm.cancel = cancel;
      vm.ok = ok;

      function addFlow() {
        if (vm.flowText === '') {
          return;
        }

        vm.formData.bedList = vm.formData.bedList || [];

        vm.formData.bedList.push(vm.flowText);

        // 置空 ng-model里的值
        vm.flowText = '';
      }

      function cancel() {
        $uibModalInstance.dismiss();
      }

      function ok() {
        var data = angular.extend({}, vm.formData);

        data.beds = data.bedList ? data.bedList.join(';') : '';
        data.deptCode = deptCode;
        // return;
        bedService.saveGroup(data).then(function(response) {
          if (response && response.data.code === '0') {
            $rootScope.$broadcast('toast', {
              type: 'success',
              content: '保存成功'
            });
            // toastr.success('保存成功！', '提示');
            $uibModalInstance.close('success');
          }
        });
      }

      function removeFlow($index) {
        vm.formData.bedList.splice($index, 1);
      }
    }
})();