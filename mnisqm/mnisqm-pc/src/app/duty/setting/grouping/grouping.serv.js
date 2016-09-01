(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('areaService', areaServiceFunc);

  /** @ngInject */
  function areaServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getUserNurses: getUserNurses,
      batchSaveUserNurse: batchSaveUserNurse
    };

    return service;


    /**
     * [save 保存病区护理人员]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function batchSaveUserNurse(dataObj) {
      return $http.post(apiHost.schedule.batchSaveUserNurse, dataObj)
        .then(complete);
    }

    /**
     * [get 查询病区护理人员]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getUserNurses(params) {

      return $http.get(apiHost.schedule.getUserNurses, {
          params: params
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
