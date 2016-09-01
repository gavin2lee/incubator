(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('paperService', paperServiceFunc);

  /** @ngInject */
  function paperServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getResearchPapers: getResearchPapers,
      saveResearchPaper: saveResearchPaper,
      deleteResearchPaper: deleteResearchPaper
    };

    return service;

    /**
     * [del 删除床位分组]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteResearchPaper(seqId) {
      return $http.post(apiHost.research.deleteResearchPaper, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存床位分组]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveResearchPaper(dataObj) {
      return $http.post(apiHost.research.saveResearchPaper, dataObj)
        .then(complete);
    }

    /**
     * [get 床位分组列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getResearchPapers() {
      var param = {};

      param.deptCode = security.getUserinfo().role.deptCode;

      return $http.get(apiHost.research.getResearchPapers, {
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
