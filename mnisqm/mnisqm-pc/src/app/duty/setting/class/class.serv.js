(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('classesService', classesServiceFunc);

  /** @ngInject */
  function classesServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getClassList: getClassList,
      saveclass: saveClass,
      deleteClass: deleteClass
    };

    return service;

    /**
     * [del 删除班次信息]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteClass(seqId) {
      return $http.post(apiHost.schedule.deleteClass, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存班次信息]
     * @param  {[object]} dataObj
     * @return {[object]}         [promise]
     */
    function saveClass(dataObj) {
      return $http.post(apiHost.schedule.saveClass, dataObj)
        .then(complete);
    }

    /**
     * [get 查询科室班次信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getClassList(params) {
      return $http.get(apiHost.schedule.getClassList, {
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
