(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('workManualService', workManualServiceFunc);

  /** @ngInject */
  function workManualServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      get: get,
      save: save,
      delete: del
    };

    return service;

    /**
     * [del 删除工作条目]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function del(seqId) {
      return $http.post(apiHost.manual.workManual.delete, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存床位分组]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function save(dataObj) {
      return $http.post(apiHost.manual.workManual.save, dataObj)
        .then(complete);
    }

    /**
     * [get 床位分组列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function get() {
      var param = {};

      param.deptCode = security.getUserinfo().role.deptCode;

      return $http.get(apiHost.manual.workManual.get, {
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
