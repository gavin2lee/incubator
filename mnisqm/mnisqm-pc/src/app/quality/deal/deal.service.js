(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('dealService', dealServiceFunc);

  /** @ngInject */
  function dealServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getDealList: getDealList,
      saveDeal: saveDeal,
      deleteDeal: deleteDeal,
      getChekcDetail: getChekcDetail,
      getAllQualityForm: getAllQualityForm,
      getResultCode: getResultCode
    };

    return service;

    /**
     * [del 删除质量检查问题处理]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteDeal(seqId) {
      var param = {};
      param.seqId = seqId;
      return $http.get(apiHost.qualityManager.deleteQualityDeal, {
          params: param
        })
        .then(complete);
    }

    /**
     * [save 保存质量检查问题处理]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveDeal(dataObj) {
      return $http.post(apiHost.qualityManager.saveQualityDeal, dataObj)
        .then(complete);
    }

    /**
     * [get 质量检查问题处理列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getDealList(resultCode) {
      var param = {};

      // param.deptCode = security.getUserinfo().role.deptCode;
      resultCode ? param.resultCode = resultCode : '';

      return $http.get(apiHost.qualityManager.getQualityDealList, {
          params: param
        })
        .then(complete);
    }

    /**
     * [get 获取resultCode]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getResultCode(param) {
      // var param = {};

      // // param.deptCode = security.getUserinfo().role.deptCode;
      // date ? param.date = date : '';
      // deptCode ? param.deptCode = deptCode : '';
      // type ? param.type = type : '';

      return $http.get(apiHost.qualityManager.getQualityResultCode, {
          params: param
        })
        .then(complete);
    }


    /**
     * [get 查看检测情况]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getChekcDetail(date, deptCode, formName) {
      var param = {};

      // param.deptCode = security.getUserinfo().role.deptCode;
      date ? param.date = date : '';
      deptCode ? param.deptCode = deptCode : '';
      formName ? param.formName = formName : '';

      return $http.get(apiHost.qualityManager.getQualityCheckDetail, {
          params: param
        })
        .then(complete);
    }

    /**
     * [get 检查表单选择项]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getAllQualityForm() {
      var param = {};

      return $http.get(apiHost.qualityManager.getAllQualityForm, {
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
