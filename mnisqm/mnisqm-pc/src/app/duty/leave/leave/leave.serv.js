(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('leaveService', leaveServiceFunc);

  /** @ngInject */
  function leaveServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getLeaveList: getLeaveList,
      saveScheduleLeave: saveScheduleLeave,
      deleteScheduleLeave: deleteScheduleLeave,
      apprvScheduleLeave: apprvScheduleLeave
    };

    return service;

    /**
     * [del 审批请假信息]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function apprvScheduleLeave(dataObj) {
      return $http.post(apiHost.schedule.apprvScheduleLeave, dataObj)
        .then(complete);
    }

    /**
     * [del 删除请假信息]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteScheduleLeave(seqId) {
      return $http.post(apiHost.schedule.deleteScheduleLeave, {
          seqId: seqId
        })
        .then(complete);
    }

    /**
     * [save 保存请假信息]
     * @param  {[object]} dataObj
     * @return {[object]}         [promise]
     */
    function saveScheduleLeave(dataObj) {
      return $http.post(apiHost.schedule.saveScheduleLeave, dataObj)
        .then(complete);
    }

    /**
     * [get 查询请假信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getLeaveList(params) {

      return $http.get(apiHost.schedule.getLeaveList, {
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
