(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('callService', callServiceFunc);

  /** @ngInject */
  function callServiceFunc($window, apiHost, $http, $log, $q, toastr) {
    var service = {
      save: save,
      del: del,
      getDetail: getDetail,
      doReport: doReport
    };

    return service;

    /**
     * [del 删除不良事件分级]
     * @param  {[string]} seqId [必填]
     * @param  {[string]} reportCode [必填]
     * @return {[object]}       [promise]
     */
    function del(seqId, reportCode) {
      return $http.post(apiHost.events.deleteEventReport, {
          seqId: seqId,
          reportCode: reportCode
        })
        .then(complete);
    }

    function getDetail(reportCode) {
      return $http.get(apiHost.events.getEventReport, {
        params: {
          reportCode: reportCode
        }
      }).then(complete)
    }

    /**
     * [save 保存不良事件级别]
     * @param  {[object]} dataObj
     * @return {[object]}         [promise]
     */
    function save(dataObj) {
      return $http.post(apiHost.events.saveEventReport, dataObj)
        .then(complete);
    }

    /**
     * 上报事件
     * @param  {string} reportCode 事件code
     * @param  {string} opType     操作类型
     * @return {object}            promise对象
     */
    function doReport(reportCode, opType) {
      return $http.post(apiHost.events.handleEventReport, {
          reportCode: reportCode,
          opType: opType
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
