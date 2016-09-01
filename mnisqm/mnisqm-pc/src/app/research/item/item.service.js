(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('researchItemService', researchItemServiceFunc);

  /** @ngInject */
  function researchItemServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getResearchItems: getResearchItems,
      saveResearchItem: saveResearchItem,
      deleteResearchItem: deleteResearchItem
    };

    return service;

    /**
     * [del 删除床位分组]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteResearchItem(seqId) {
      return $http.post(apiHost.research.deleteResearchItem, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存床位分组]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveResearchItem(dataObj) {
      return $http.post(apiHost.research.saveResearchItem, dataObj)
        .then(complete);
    }

    /**
     * [get 床位分组列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getResearchItems() {
      var param = {};

      param.deptCode = security.getUserinfo().role.deptCode;

      return $http.get(apiHost.research.getResearchItems, {
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
