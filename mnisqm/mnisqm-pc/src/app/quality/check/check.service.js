(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('checkService', checkServiceFunc);

  /** @ngInject */
  function checkServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getCheckList: getCheckList,
      saveEditCheck: saveEditCheck,
      deleteCheck: deleteCheck
    };

    return service;

    /**
     * [del 删除质量检查]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteCheck(seqId) {
      var param = {};
      param.seqId = seqId;
      return $http.get(apiHost.qualityManager.deleteQualityCheck, {
          params: param
        })
        .then(complete);
    }

    /**
     * [save 保存质量检查]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveEditCheck(dataObj) {
      return $http.post(apiHost.qualityManager.editQualityCheck, dataObj)
        .then(complete);
    }

    /**
     * [get 质量检查列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getCheckList(param) {
      // var param = {};

      // param.deptCode = security.getUserinfo().role.deptCode;
      // date ? param.date = date : '';
      // deptCode ? param.deptCode = deptCode : '';
      // formCode ? param.formCode = formCode : '';

      return $http.get(apiHost.qualityManager.getQualityCheckList, {
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
