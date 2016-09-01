(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('manualItemService', manualItemServiceFunc);

  /** @ngInject */
  function manualItemServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getItems: getItems,
      saveItem: saveItem,
      deleteItem: deleteItem
    };

    return service;

    /**
     * [del 删除工作项目]
     * @param  {[string]} itemCode [无]
     * @return {[object]}       [promise]
     */
    function deleteItem(itemCode) {
      return $http.post(apiHost.manual.item.delete, {
          itemCode: itemCode
        })
        .then(complete);
    }

    /**
     * [save 保存工作条目]
     * @param  {[object]} dataObj [seqId: number; itemCode: string; itemName: string; deptName: string]
     * @return {[object]}         [promise]
     */
    function saveItem(dataObj) {
      return $http.post(apiHost.manual.item.save, dataObj)
        .then(complete);
    }

    /**
     * [getItems 获取工作条目]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getItems(deptCode) {
      var param = {};

      param.deptCode = deptCode || security.getUserinfo().role.deptCode;

      return $http.get(apiHost.manual.item.get, {
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
