(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('bedService', bedServiceFunc);

  /** @ngInject */
  function bedServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getGroupList: getGroupList,
      saveGroup: saveGroup,
      deleteGroup: deleteGroup
    };

    return service;

    /**
     * [del 删除床位分组]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteGroup(seqId) {
      return $http.post(apiHost.schedule.deleteGroup, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存床位分组]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveGroup(dataObj) {
      return $http.post(apiHost.schedule.saveGroup, dataObj)
        .then(complete);
    }

    /**
     * [get 床位分组列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getGroupList(params) {

      return $http.get(apiHost.schedule.getGroupList, {
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
