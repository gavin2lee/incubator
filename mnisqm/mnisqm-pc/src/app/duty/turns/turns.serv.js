(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('turnsService', turnsServiceFunc);

  /** @ngInject */
  function turnsServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      saveSchedule: saveSchedule,
      newSchedule: newSchedule,
      getSchedule: getSchedule,
      copySchedule: copySchedule
    };

    return service;

    /**
     * [del 删除不良事件分级]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */

    function getSchedule(params) {
      return $http.get(apiHost.schedule.getSchedule, {
          params: params
        })
        .then(complete);
    }

    /**
     * [save 保存不良事件级别]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveSchedule(dataObj) {
      dataObj.deptCode = security.getUserinfo().role.deptCode;
      dataObj.deptName = security.getUserinfo().role.deptName;
      return $http.post(apiHost.schedule.saveSchedule, dataObj)
        .then(complete);
    }

    /**
     * [get 获取不良事件级别]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function newSchedule(week) {
      var param = {};

      param.deptCode = security.getUserinfo().role.deptCode;
      param.week = week;

      return $http.get(apiHost.schedule.newSchedule, {
          params: param
        })
        .then(complete);
    }

    /**
     * [get 周排班复制]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function copySchedule(params) {
      var param = {};

      param.deptCode = params.deptCode;
      param.outWeek = params.weeks;
      param.srcWeek = params.weeks - 1;

      return $http.get(apiHost.schedule.copySchedule, {
          params: param
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
