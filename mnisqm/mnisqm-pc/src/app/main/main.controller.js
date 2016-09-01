(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('MainController', MainController);

  /** @ngInject */
  function MainController($log, $rootScope, $scope, $uibModal, $location, $window, security, $state) {
    var vm = this;
    var isLogined;

    vm.$scope = $scope;
    vm.minMode = security.minMode;

    /*
    高亮显示标签页
     */
    $scope.isActive = vm.isActive = function(path) {
      var pathLen = 0;
      var locationPath;

      if (path === '') return false;

      pathLen = path.length;
      locationPath = $location.path().substr(-pathLen, pathLen);

      return path === locationPath;
    };

    /*
    监听菜单模式
     */
    vm.$scope.$on('toggleNavView', function(event, isMini) {

      vm.minMode = isMini;
    });

    vm.$scope.$on('toggleSubNavView', function(event, isShow) {

      vm.isSubNavShow = isShow;
    });

    /*
    当路由发生改变时，判别是否已登录，若非登录则跳转至登录页。
     */
    $rootScope.$on('$stateChangeStart',
      function(event, toState, toParams, fromState, fromParams, options) {
        isLogined = security.getUserinfo('isLogined');
        if (toState.name === 'login') {
          if (isLogined) {
            event.preventDefault();
          }
        }
      });
  }
})();
