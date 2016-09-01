(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('deptMgtService', deptMgtService);

  /** @ngInject */
  function deptMgtService($http, apiHost) {
    var reqs = apiHost.deptManager,
      service = {
        getAllDept: getAllDept,
        saveDept: saveDept,
        deleteDept: deleteDept,
        saveUserDept: saveUserDept,
        deleteUserDept: deleteUserDept,
        getUserDept: getUserDept,
        getUserList: getUserList
      };

    return service;

    function getAllDept() {
      var req = reqs.dept.getter;
      return $http[req.method](req.url);
    }

    function saveDept(data) {
      var req = reqs.dept.setter;
      return $http[req.method](req.url, data);
    }

    function deleteDept(seqId) {
      var req = reqs.dept.deleter;
      return $http[req.method](req.url, {
        params: {
          seqId: seqId
        }
      });
    }

    function saveUserDept(data) {
      var req = reqs.userDept.setter;
      return $http[req.method](req.url, data);
    }

    function deleteUserDept(seqId) {
      var req = reqs.userDept.deleter;
      return $http[req.method](req.url, {
        params: {
          seqId: seqId
        }
      });
    }

    function getUserDept(deptCode) {
      return $http.get(apiHost.user.getDept, {
        params: {
          deptCode: deptCode
        }
      });
    }

    function getUserList(deptCode) {
      return $http.get(apiHost.files.getUserList, {
          params: {
            deptCode: deptCode
          }
        })
        .then(getUserListComplete);

      function getUserListComplete(response) {
        if (response.data.code === '0') {
          service.userList = response.data.data.lst;
          return response.data.data.lst;
        } else {
          $log.error('获取用户数据失败！\n' + response.data.msg);
        }
      }
    }

  }
})();
