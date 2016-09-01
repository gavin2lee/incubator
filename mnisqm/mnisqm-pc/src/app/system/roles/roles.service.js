(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('rolesService', rolesServiceFunc);

  /** @ngInject */
  function rolesServiceFunc($window, apiHost, $http, $log, $q, toastr) {
    var service = {
      getHaveFunction: getHaveFunction,
      getNotHaveFunction: getNotHaveFunction,
      save: save,
      edit: edit,
      del: del
    };

    return service;

    function getHaveFunction(roleCode) {

      return $http.get(apiHost.roles.getHaveFunction, {
        params: {
          roleCode: roleCode
        }
      })
        .then(complete)
        .catch(failed);

      function complete(response) {
        if (response.data.code === '0') {
          return response;
        } else {
          $log.error('获取用户数据失败！\n' + response.data.msg);
        }
      }

      function failed(error) {
        $log.error('获取用户数据失败！\n' + angular.toJson(error.data, true));
      }
    }

    function getNotHaveFunction(roleCode) {

      return $http.get(apiHost.roles.getNotHaveFunction, {
        params: {
          roleCode: roleCode
        }
      })
        .then(getUserListComplete)
        .catch(getUserListFailed);

      function getUserListComplete(response) {
        if (response.data.code === '0') {
          return response;
        } else {
          $log.error('获取用户数据失败！\n' + response.data.msg);
        }
      }

      function getUserListFailed(error) {
        $log.error('获取用户数据失败！\n' + angular.toJson(error.data, true));
      }
    }

    function edit(roleData) {
      return $http.post(apiHost.roles.updateRole, roleData)
      .then(function (response) {
        if (response.data.code === '0') {
          return response;
        }
        return null;
      });
    }

    function save(roleData) {
      return $http.post(apiHost.roles.saveRole, roleData)
      .then(function (response) {
        if (response.data.code === '0') {
          return response;
        }
        return null;
      })
      .catch(function (error) {

      });
    }

    function del(seqId) {
      return $http.post(apiHost.roles.deleteRole, seqId)
      .then(function (response) {
        if (response.data.code === '0') {
          return response;
        }

        return null;
      });
    }
  }
})();
