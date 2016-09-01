(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('levelService', levelServiceFunc);

  /** @ngInject */
  function levelServiceFunc($window, apiHost, $http, $log, $q, toastr) {
    var service = {
      save: save,
      get: get,
      del: del
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

    /**
     * [save 保存不良事件级别]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function save(dataObj) {
      return $http.post(apiHost.events.saveEventLevel, dataObj)
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
  }
})();
