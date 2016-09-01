(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('usersService', usersServiceFunc);

  /** @ngInject */
  function usersServiceFunc($window, apiHost, $http, $log, $q, toastr) {
    var service = {
      getUserList: getUserList,
      deleteUser: deleteUser,
      save: save,
      saveUserRole: saveUserRole,
      deleteUserRole: deleteUserRole
    };

    return service;

    function getUserList(deptCode) {

      return $http.get(apiHost.users.getUserList, {
          params: {
            deptCode: deptCode
          }
        })
        .then(complete)
        .catch(failed);

      function failed(error) {
        $log.error('获取用户数据失败！\n' + angular.toJson(error.data, true));
      }
    }

    function deleteUser(seqId) {

      return $http.post(apiHost.users.deleteUser, {
          seqId: seqId
        })
        .then(complete);
    }

    function save(userObj) {
      return $http.post(apiHost.users.saveUser, userObj)
        .then(complete);
    }

    function saveUserRole(userRoleObj) {
      return $http.post(apiHost.users.saveUserRole, userRoleObj)
        .then(complete);
    }

    function deleteUserRole(seqId) {
      return $http.post(apiHost.users.deleteUserRole, seqId)
        .then(complete);
    }

    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }
  }
})();
