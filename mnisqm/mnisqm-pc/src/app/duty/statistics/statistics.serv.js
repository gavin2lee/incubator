/**
 * Created by gary on 16/6/30.
 */
(function () {
  'use strict';

  angular
    .module('nqms')
    .config(function (toastrConfig) {
      toastrConfig.tapToDismiss = true;
      toastrConfig.timeOut = 0;
    })
    .factory('deptStatisticsService', statisticsFunc);

  /** @ngInject */
  function statisticsFunc(apiHost, $http) {
    var service = {
      getByLeave: getByLeave,
      getByClass: getByClass,
      getByDays: getByDays
    };

    return service;

    /**
     * 根据请假统计
     * @param params: [deptCode: string; mounth: string]
     * @returns {*}
     */
    function getByLeave(params) {
      return $http.get(apiHost.schedule.statistics.getByLeave, {
          params: params
        })
        .then(complete)
    }

    /**
     * 根据班次统计
     * @param params: [deptCode: string; mounth: string; startDate: string; endDate: string]
     * @returns {*}
     */
    function getByClass(params) {

      return $http.get(apiHost.schedule.statistics.getByClass, {
          params: params
        })
        .then(complete);
    }

    /**
     * 根据日期统计
     * @param params
     * @returns {*}
     */
    function getByDays(params) {
      return $http.get(apiHost.schedule.statistics.getByDays, {
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
