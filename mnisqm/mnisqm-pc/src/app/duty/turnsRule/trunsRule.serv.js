(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('turnsRuleService', turnsRuleService);

  /** @ngInject */
  function turnsRuleService($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getRuleList: getRuleList,
      saveRule: saveRule,
      deleteRule: deleteRule
    };

    return service;


    /**
     * [del 删除排班规则]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteRule(seqId) {
      return $http.post(apiHost.schedule.deleteRule, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存排班规则]
     * @param  {[object]} dataObj
     * @return {[object]}         [promise]
     */
    function saveRule(dataObj) {
      dataObj.deptCode = security.getUserinfo().role.deptCode;
      return $http.post(apiHost.schedule.saveRule, dataObj)
        .then(complete);
    }

    /**
     * [get 查询排班规则]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getRuleList() {
      var param = {};

      param.deptCode = security.getUserinfo().role.deptCode;

      return $http.get(apiHost.schedule.getRuleList, {
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
