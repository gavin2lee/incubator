/**
 * Created by lin on 16/3/15.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .config(function(w5cValidatorProvider) {
      w5cValidatorProvider.setRules({
        loginName: {
          required: "用户名不能为空",
          pattern: "用户名必须输入字母、数字、下划线"
        },
        password: {
          required: "密码不能为空",
          minlength: "密码长度不能小于{minlength}",
          maxlength: "密码长度不能大于{maxlength}"
        }
      })
    })
    .controller('ChangeUserController', changeUserControllerFunc);

  function changeUserControllerFunc($uibModalInstance, $location, security, $log, moment, $window, $timeout, toastr) {
    var vm = this;

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

    vm.loginBtnStatus = {
      title: '切换',
      state: ''
    };

    vm.changeUser = function(data) {
      var loginedUser = security.getUserinfo().user;
      $log.info(data);

      vm.loginBtnStatus.title = '正在切换...';
      vm.loginBtnStatus.state = 'disabled';

      // 检查切换用户是否为当前登录用户
      if (data.loginName === loginedUser.loginName) {
        toastr.info('您切换的用户已是当前登录用户，无需切换～', '提示');
        resetState();

        return;
      }

      security.login(data).then(function(result) {
        var user_info;

        resetState();

        if (result && result.data['code'] == '-1') {
          return;
        }
        //debugger;
        user_info = {
          user: result.data.data.user,
          nav: result.data.data.funs,
          role: result.data.data.role,
          tab: {}
        };

        security.setUserinfo(user_info);
        security.setCacheUser(user_info);

        $uibModalInstance.dismiss('cancel');
        $timeout(function() {
          $location.path('/home');
          $window.location.reload();
        }, 0)
      }, resetState);
    };

    function cancel() {
      $uibModalInstance.dismiss('cancel');
    }

    function resetState() {
      vm.loginBtnStatus.title = '切换';
      vm.loginBtnStatus.state = '';
    }
  }
})();
