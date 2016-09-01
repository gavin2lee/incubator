(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('acmeHeader', cusHeaderFunc);

  /** @ngInject */
  function cusHeaderFunc() {
    var directive = {
      restrict: 'E',
      templateUrl: 'app/components/header/header.html',
      scope: {},
      replace: true,
      controller: headerController,
      controllerAs: 'hd',
      bindToController: true
    };

    return directive;

    /** @ngInject */
    function headerController($rootScope, $interval, $uibModal, moment, $log, security, $state, $timeout, $window) {
      var hd = this;
      var userId;

      hd.openUserDialog = openUserDialog;
      hd.openRoleDialog = openRoleDialog;
      hd.logout = logout;
      hd.changeRole = changeRole;

      $interval(function() {
        hd.time_now = moment().format('YYYY年MM月DD日  HH:mm:ss');
      }, 1000);
      try {
        userId = security.getUserinfo().user.seqId;
        hd.currentRole = security.getUserinfo().role;
        hd.user = security.getUserinfo().user;
      } catch (e) {
        $rootScope.$broadcast('toast', {
          type: 'error',
          content: '您还未登录！'
        })
        $state.go('login');
      }

      function openUserDialog() {
        var userModalInstance = $uibModal.open({
          templateUrl: 'app/components/header/changeUser.tpl.html',
          controller: 'ChangeUserController',
          controllerAs: 'vm',
          backdrop: 'static',
          animation: false,
          size: 'sm'
        });
      }

      function openRoleDialog() {
        var roleModalInstance = $uibModal.open({
          templateUrl: 'app/components/header/changeRole.tpl.html',
          controller: 'ChangeRoleController',
          controllerAs: 'vm',
          backdrop: 'static',
          animation: false,
          size: 'md'
        });
      }

      function changeRole(role) {

        if (hd.currentRole.roleCode == role.roleCode) {
          $rootScope.$broadcast('toast', {
            type: 'warning',
            content: '已是当前角色！'
          });

          return;
        }

        security.getNavigation(role).then(function(response) {
          var userinfo = security.getUserinfo();

          userinfo.nav = response.data.data;
          userinfo.role = role;
          security.setCacheUser(userinfo);
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '正在切换...'
          });

          $timeout(function() {
            $state.go('home');
            $window.location.reload();
          }, 1000);
        })
      }

      security.getUserRoleList({
        userId: userId
      }).then(function(response) {
        hd.userRoleList = response.data.data;
      });

      function logout() {
        $rootScope.$broadcast('logout');
      }

      $log.info(hd.user);
      // "vm.creation" is avaible by directive option "bindToController: true"
    }
  }

})();
