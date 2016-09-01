/**
 * Created by gary on 16/4/1.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('RolesModalController', rolesModalControllerFunc);

  /** @ngInject */
  function rolesModalControllerFunc($uibModalInstance, $rootScope, rolesService, security, toastr) {
    var vm = this;

    vm.cancel = cancel;
    vm.save = save;

    function save() {
      rolesService.save(vm.role).then(function (response) {
        if (response && response.data.code === '0') {
          toastr.success('添加成功！', '提示');

          $uibModalInstance.dismiss('cancel');

          security.getUserRoleList();
          $rootScope.$apply();
        } else {
          toastr.error('保存失败', '警告');
        }
      });
    }

    function cancel() {
      $uibModalInstance.dismiss('cancel');
    }
  }

})();
