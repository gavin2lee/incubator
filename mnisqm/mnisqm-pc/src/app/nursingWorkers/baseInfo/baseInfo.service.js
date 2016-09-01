(function() {
  'use strict';

  angular
    .module('nqms')
    .config(function(toastrConfig) {
      toastrConfig.tapToDismiss = true;
      toastrConfig.timeOut = 0;
    })
    .factory('baseInfoService', baseInfoService);

  /** @ngInject */
  function baseInfoService($rootScope, $window, apiHost, $http, $log) {
    var service = {
      selectedUser: {},
      isNew: true,
      selectedDeptCode: '',
      userList: [],
      getUserInfo: getUserInfo,
      getUserList: getUserList,
      getComUserChange: getComUserChange,
      getDimission: getDimission,
      setSelectUser: setSelectUser,
      getUserStatistics: getUserStatistics,
      add: add,
      deleteUser: deleteUser,
      saveUser: saveUser,
      saveUserDept: saveUserDept,
      saveChange: saveChange,
      quit: quit,
      retire: retire
    };
    var reqs = apiHost.files;

    return service;

    function saveUser(data) {
      return $http.post(reqs.save, {
        comUser: data,
        dataType: 'comUser'
      });
    }

    function getUserInfo(userCode, hisCode) {

      return $http.get(apiHost.files.getUserDetailInfo, {
          params: {
            userCode: userCode,
            hisCode: hisCode
          }
        })
        .then(complete);
    }

    function getDimission(deptCode) {
      return $http.get(apiHost.files.getDimissionUserList, {
          params: {
            deptCode: deptCode
          }
        })
        .then(complete);
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

    // 获取科室的统计信息
    function getUserStatistics(deptCode) {
      return $http.get(apiHost.files.getUserStatistics, {
          params: {
            deptCode: deptCode
          }
        })
        .then(complete);
    }

    function setSelectUser(userObj) {
      service.selectedUser = userObj;

      return userObj;
    }

    function add(userData) {
      return $http.post(apiHost.files.save, userData)
        .then(complete)
    }

    function saveUserDept(userCode, deptCode) {
      return $http.post(apiHost.files.saveUserDept, {
        userCode: userCode,
        deptCode: deptCode
      }).then(complete)
    }

    // 离职
    function quit(id) {
      var req = apiHost.user.quit;
      return $http[req.method](req.url, {
        userType: $rootScope.getDicCode('nature', '离职'),
        userCode: id
      });
    }

    // 退休
    function retire(id) {
      var req = apiHost.user.quit;
      return $http[req.method](req.url, {
        userType: $rootScope.getDicCode('nature', '退休'),
        userCode: id
      });
    }

    // 获取人员变更信息
    function getComUserChange (userCode) {
      return $http[apiHost.user.getComUserChange.method](apiHost.user.getComUserChange.url, {
        params: {
          userCode: userCode
        }
      })
      .then(complete)
    }

    function deleteUser(dataType, delSeqId) {
      return $http.post(apiHost.files.delete, {
          dataType: dataType,
          delSeqId: delSeqId
        })
        .then(complete);
    }

    // 保存变更
    function saveChange(data) {
      return $http.post(apiHost.files.saveChange, data)
        .then(complete)
    }

    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }
  }
})();
