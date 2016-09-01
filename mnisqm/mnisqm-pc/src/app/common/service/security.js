// getUserSysRoles
(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('security', securityFunc);

  function securityFunc($http, $log, $window, $location, $timeout, $state, apiHost) {
    var userinfo,
      service = {
        getUserinfo: getUserinfo,
        setUserinfo: setUserinfo,
        clearUserinfo: clearUserinfo,
        getCacheUser: getCacheUser,
        setCacheUser: setCacheUser,
        clearCacheUser: clearCacheUser,
        login: login,
        getUserRoleList: getUserRoleList,
        getNavigation: getNavigation,
        addToBreadcrumb: addToBreadcrumb,
        removeFromBreadcrumb: removeFromBreadcrumb,
        minMode: false // 左侧菜单迷你和展开模式
      };

    return service;

    function getUserinfo() {
      return userinfo;
    }

    function setUserinfo(user) {
      userinfo = user;
    }

    function clearUserinfo() {
      userinfo = null;
    }

    function getCacheUser() {
      return angular.fromJson($window.sessionStorage.getItem('userinfo'));
    }

    function setCacheUser(user) {
      $window.sessionStorage.setItem('userinfo', angular.toJson(user));
    }

    function clearCacheUser() {
      $window.sessionStorage.removeItem('userinfo');
    }

    // 登录
    function login(formData) {
      return $http.post(apiHost.system.login, formData);
    }

    // 获取当前用户的所有角色
    function getUserRoleList(params) {
      return $http.get(apiHost.system.getRoleList, {
        params: params
      });
    }

    // 根据用户角色获取导航菜单
    function getNavigation(roleObj) {
      return $http.get(apiHost.system.getNavigation, {
        params: {
          roleCode: roleObj.roleCode,
          userRoleId: roleObj.seqId
        }
      });
    }

    function addToBreadcrumb(key, value) {
      userinfo.tab[key] = value;
      setCacheUser(userinfo);
    }

    function removeFromBreadcrumb(key) {
      delete userinfo.tab[key];
      setCacheUser(userinfo);
    }
  }
})();
