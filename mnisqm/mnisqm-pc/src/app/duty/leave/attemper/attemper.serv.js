(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('attemperService', attemperService);

  /** @ngInject */
  function attemperService($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getChangeClass: getChangeClass,
      saveChangeClass: saveChangeClass,
      deleteChangeClass: deleteChangeClass,
      apprvChangeClass: apprvChangeClass
    };

    return service;

    /**
     * [del 删除床位分组]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteChangeClass(seqId) {
      return $http.post(apiHost.schedule.deleteChangeClass, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存调班申请]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveChangeClass(dataObj) {
      return $http.post(apiHost.schedule.saveChangeClass, dataObj)
        .then(complete);
    }

    /**
     * [save 保存调班申请]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function apprvChangeClass(dataObj) {
      return $http.post(apiHost.schedule.apprvChangeClass, dataObj)
        .then(complete);
    }

    /**
     * [get 查询调班申请]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getChangeClass(params) {

      return $http.get(apiHost.schedule.getChangeClass, {
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
