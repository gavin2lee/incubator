/**
 * Created by lin on 16/3/13.
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
      });
    })
    .controller('LoginController', loginController);

  /** @ngInject */
  function loginController($rootScope, $log, $state, security) {
    var vm = this;

    vm.reset = reset;
    vm.loginBtnStatus = {
      title: '登录',
      state: ''
    };

    vm.login = function(data) {
      vm.loginBtnStatus.title = '正在登录...';
      vm.loginBtnStatus.state = 'disabled';

      security.login(data)
        .then(function(result) {
          //debugger;
          var user_info = {
            user: result.data.data.user,
            nav: result.data.data.funs,
            role: result.data.data.role,
            tab: {}
          };

          security.setUserinfo(user_info);
          security.setCacheUser(user_info);

          // 登录成功后跳转至首页
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '登录成功！'
          });
          $state.go('home');
          $rootScope.$broadcast('init');
        })
        .finally(setLoginBtnstate);
    };

    vm.validationOption = {
      blurTrig: true
    };

    function setLoginBtnstate() {
      vm.loginBtnStatus.title = '登录';
      vm.loginBtnStatus.state = '';
    }

    function reset() {
      vm.formData = {};
    }
  }
})();
