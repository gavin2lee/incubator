/**
 * Created by lin on 16/3/15.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('ChangeRoleController', changeRoleControllerFunc);

  function changeRoleControllerFunc($uibModalInstance, $log, $state, $timeout, moment, $window, toastr, $scope, security) {
    var vm = this;
    var currentRole = security.getUserinfo().role;
    var userId = security.getUserinfo().user.seqId;

    vm.currentRole = security.getUserinfo().role;

    debugger

    vm.cancel = cancel;
    vm.formData = {};
    vm.datepickerOption = {
      dateDisabled: 'disabled',
      formatYear: 'yyyy',
      formatMonth: 'MM',
      formatDay: 'dd',
      initDate: new Date(),
      maxDate: new Date(2020, 5, 22),
      minDate: moment(),
      startingDay: 1
    };

    vm.validationOption = {
      blurTrig: true
    };

    vm.roleBtnStatus = {
      title: '切换',
      state: ''
    };

    security.getUserRoleList({
      userId: userId
    }).then(function(response) {
      vm.userRoleList = response.data.data;
    });

    vm.selectedRole = currentRole;

    vm.showRole = function(roleObj) {
      $log.info(roleObj);
      vm.changedRole = roleObj;
    };

    vm.changeRole = function() {
      currentRole = security.getUserinfo().role;

      if (!vm.changedRole) return;

      vm.roleBtnStatus.title = '正在切换...';
      vm.roleBtnStatus.state = 'disabled';

      // 检查切换角色是否为当前角色
      if (vm.changedRole.seqId === currentRole.seqId) {
        toastr.info('您切换的角色已是当前登录的角色，无需切换～', '提示');

        resetState();
        return;
      }

      security.getNavigation(vm.changedRole).then(function(response) {
        var userinfo = security.getUserinfo();
        userinfo.nav = response.data.data;
        userinfo.role = vm.changedRole;
        userinfo.tab = {};
        security.setCacheUser(userinfo);
        toastr.success('正在切换角色...', '恭喜');
        $state.go('home');
        $timeout(function() {
          $window.location.reload();
        }, 2000);
      }).finally(resetState);

    }

    function cancel() {
      $uibModalInstance.dismiss('cancel');
    }

    function resetState() {
      vm.roleBtnStatus.title = '切换';
      vm.roleBtnStatus.state = '';
    }
  }
})();
