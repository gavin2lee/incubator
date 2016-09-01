(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('comService', comService);

  function comService($http, $q, security, apiHost) {
    var userList, deptList, service = {},
        deptCode = security.getUserinfo()['role'].deptCode;
    service.getUserHelper = getUserHelper;
    service.getDeptHelper = getDeptHelper;
    service.getHelper = getHelper;
    return service;

    function getUserHelper(refresh) {
      var defer = $q.defer();
      if(userList && !refresh) {
        defer.resolve({
          users: userList,
          getUserName: getUserName,
          getUserCode: getUserCode
        });
      }else {
        $http.get(apiHost.user.getDept, {
          params: {
            deptCode: deptCode
          }
        }).then(function(rsp) {
          var data = rsp.data.data;
          userList = pluckUser(data);
          deptList = pluckDept(data);
          defer.resolve({
            users: userList,
            getUserName: getUserName,
            getUserCode: getUserCode
          });
        });
      }

      return defer.promise;
    }

    function getDeptHelper(refresh) {
      var defer = $q.defer();
      if(deptList && !refresh) {
        defer.resolve({
          depts: deptList,
          getDeptName: getDeptName,
          getDeptCode: getDeptCode
        });
      }else {
        $http.get(apiHost.user.getDept, {
          params: {
            deptCode: deptCode
          }
        }).then(function(rsp) {
          var data = rsp.data.data;
          userList = pluckUser(data);
          deptList = pluckDept(data);
          defer.resolve({
            depts: deptList,
            getDeptName: getDeptName,
            getDeptCode: getDeptCode
          });
        });
      }

      return defer.promise;
    }

    function getHelper(refresh) {
      var defer = $q.defer();
      if(userList && deptList && !refresh) {
        defer.resolve({
          dept: {
            depts: deptList,
            getDeptName: getDeptName,
            getDeptCode: getDeptCode
          },
          user: {
            users: userList,
            getUserName: getUserName,
            getUserCode: getUserCode
          }
        });
      }else {
        $http.get(apiHost.user.getDept, {
          params: {
            deptCode: deptCode
          }
        }).then(function(rsp) {
          var data = rsp.data.data;
          userList = pluckUser(data);
          deptList = pluckDept(data);
          defer.resolve({
            dept: {
              depts: deptList,
              getDeptName: getDeptName,
              getDeptCode: getDeptCode
            },
            user: {
              users: userList,
              getUserName: getUserName,
              getUserCode: getUserCode
            }
          });
        });
      }

      return defer.promise;
    }

    function getUserName(userCode) {
      var target = _.findWhere(userList, {
        userCode: userCode
      });
      return target ? target.userName : '';
    }

    function getUserCode(userName) {
      var target = _.findWhere(userList, {
        userName: userName
      });
      return target ? target.userCode : '';
    }

    function getDeptName(deptCode) {
      var target = _.findWhere(deptList, {
        deptCode: deptCode
      });
      return target ? target.deptName : '';
    }

    function getDeptCode(deptName) {
      var target = _.findWhere(deptList, {
        deptName: deptName
      });
      return target ? target.deptName : '';
    }

    function pluckUser(data) {
      var result = [];
      result = result.concat(data.user);
      _.each(data.belongedDepts, function(v) {
        result = result.concat(pluckUser(v));
      });
      result = _.compact(result);
      return result;
    }

    function pluckDept(data) {
      var result = [];
      result = result.concat([_.pick(data, 'deptCode', 'deptName')]);
      _.each(data.belongedDepts, function(v) {
        result = result.concat(pluckDept(v));
      });
      return result;
    }

    return service;

  }
})();
