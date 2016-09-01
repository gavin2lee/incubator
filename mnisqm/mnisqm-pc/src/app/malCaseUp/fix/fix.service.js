(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('fixService', fixServiceFunc);

  /** @ngInject */
  function fixServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      save: save,
      getEventList: getEventList,
      del: del,
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
    };

    return service;

    /**
     * [del 删除不良事件分级]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function del(seqId) {
      return $http.post(apiHost.events.deleteEventLevel, {
          seqId: seqId
        })
        .then(complete);
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

    /**
     * [save 保存不良事件级别]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function save(dataObj) {
      return $http.post(apiHost.events.saveEventLevel, dataObj)
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
    function getEventList(params) {
      var param = params || {};

      // param.userCode = security.getUserinfo().user.userCode;
      param.deptCode = security.getUserinfo().role.deptCode;

      return $http.get(apiHost.events.getEventList, {
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
