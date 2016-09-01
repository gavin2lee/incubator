(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('deptService', deptServiceFunc);

  /** @ngInject */
  /**
   * [deptServiceFunc 科室数据]
   * @param  {[Object]} $window [全局window对象]
   * @return {[Object]}         [包涵接口和科室列表数据]
   */
  function deptServiceFunc($window, $http, apiHost, $log, toastr, security) {
    var deptList = null;
    var service = {
      apiHost: apiHost,
      getDeptData: getDeptData,
      getCacheDeptList: getCacheDeptList
    };

    return service;

    function getCacheDeptList() {
      if (deptList) {
        return angular.copy(deptList, []);
      }
      return null;
    }

    function getDeptData(deptCode) {
      return $http.get(apiHost.user.getDept, {
          params: {
            deptCode: deptCode || security.getUserinfo()['role'].deptCode
          }
        })
        .then(getDeptListComplete)
        .catch(getDeptListFailed);

      function getDeptListComplete(response) {
        if (response.data.code === '0') {
          // 不传科室code时，获得全部科室数据
          //if (!security.getUserinfo()['role'].deptCode) {
            deptList = response.data.data;
          //}

          return angular.copy(response.data.data, []);
        } else {
          $log.error('获取科室数据失败！\n' + response.data.msg);
        }
      }

      function getDeptListFailed(error) {
        $log.error('获取科室数据失败！\n' + angular.toJson(error.data, true));
      }
    }
  }
})();
