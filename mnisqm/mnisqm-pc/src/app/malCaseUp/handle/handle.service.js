(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('handleService', handleServiceFunc);

  /** @ngInject */
  function handleServiceFunc($window, apiHost, $http, $log, $q, toastr) {
    var service = {
      saveReportDetail: saveReportDetail,
      saveMeasure: saveMeasure,
      saveRequirement: saveRequirement,
      saveEstimate: saveEstimate,
      deleteMeasure: deleteMeasure,
      deleteRequirement: deleteRequirement,
      deleteEstimate: deleteEstimate,
      getReportDetail: getReportDetail,
      getEventMeasures: getEventMeasures,
      getEventRequirement: getEventRequirement,
      del: del
    };

    return service;

    /**
     * del 删除不良事件分级
     * @param  {string} seqId 必填
     * @param  {string} reportCode 必填
     * @return {object}       promise
     */
    function del(seqId, reportCode) {
      return $http.post(apiHost.events.deleteEventReport, {
          seqId: seqId,
          reportCode: reportCode
        })
        .then(complete);
    }

    function getReportDetail(params) {
      return $http.get(apiHost.events.getReportDetail, {
        params: params
      })
        .then(complete);
    }

    function getEventMeasures(params) {
      return $http.get(apiHost.events.getEventMeasures, {
        params: params
      })
        .then(complete);
    }

    function getEventRequirement(params) {
      return $http.get(apiHost.events.getEventRequirement, {
        params: params
      })
        .then(complete);
    }

    /**
     * [save 保存不良事件级别]
     * @param  {[object]} dataObj
     * @return {[object]}         [promise]
     */
    function saveReportDetail(dataObj) {
      return $http.post(apiHost.events.saveReportDetail, dataObj)
        .then(complete);
    }

    function saveMeasure(dataObj) {
      return $http.post(apiHost.events.saveEventMeasures, dataObj)
        .then(complete);
    }

    function saveRequirement(dataObj) {
      return $http.post(apiHost.events.saveEventRequirement, dataObj)
        .then(complete);
    }

    // TODO 整改效果评估还没给接口
    function saveEstimate(dataObj) {
      return $http.post(apiHost.events.saveReportDetail, dataObj)
        .then(complete);
    }

    /**
     * [get 获取不良事件级别]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function get(params) {
      return $http.get(apiHost.events.getEventLevels, {
        params: params
      })
        .then(complete);
    }

    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }


    function deleteRequirement(params) {
      return $http.post(apiHost.events.deleteEventRequirement, params)
        .then(complete);
    }

    function deleteMeasure(params) {
      return $http.post(apiHost.events.deleteEventMeasures, params)
        .then(complete);
    }

    function deleteEstimate(params) {
      return $http.post(apiHost.events.deleteEventReport, params)
        .then(complete);
    }

  }
})();
