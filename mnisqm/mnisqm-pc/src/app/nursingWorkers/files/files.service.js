(function() {
  'use strict';

  angular
    .module('nqms')
    .config(function(toastrConfig) {
      toastrConfig.tapToDismiss = true;
      toastrConfig.timeOut = 0;
    })
    .factory('filesService', filesServiceFunc);

  /** @ngInject */
  function filesServiceFunc($window, apiHost, $http, $log) {
    var service = {
      selectedUser    : {},
      isNew           : true,
      selectedDeptCode: '',
      userList        : [],
      getUserInfo     : getUserInfo,
      getUserList     : getUserList,
      setSelectUser   : setSelectUser,
      add             : add,
      deleteUser      : deleteUser
    };

    return service;

    function getUserInfo(userCode, hisCode) {

      return $http.get(apiHost.files.getUserDetailInfo, {
          params: {
            userCode: userCode,
            hisCode: hisCode
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

    function setSelectUser(userObj) {
      service.selectedUser = userObj;

      return userObj;
    }

    function add(userData) {
      return $http.post(apiHost.files.save, userData)
        .then(complete)
    }

    function deleteUser(dataType, delSeqId) {
      return $http.post(apiHost.files.delete, {
          dataType: dataType,
          delSeqId: delSeqId
        })
        .then(complete);
    }

    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }
  }
})();
