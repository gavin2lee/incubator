(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('taskService', taskServiceFunc);

  /** @ngInject */
  function taskServiceFunc($window, apiHost, $http, $log, $q, toastr, security) {
    var service = {
      getTaskList: getTaskList,
      saveTask: saveTask,
      deleteTask: deleteTask,
      editTask: editTask,
      getAllQualityForm: getAllQualityForm,
      getPlan: getPlan,
      setPlan: setPlan,
      delPlan: delPlan
    }, reqs = apiHost.qualityManager.plan;

    return service;

    function getPlan(params) {
      var req = reqs.getter;
      return $http[req.method](req.url, {
        params: params
      });
    }

    function setPlan(data) {
      var req = reqs.setter;
      return $http[req.method](req.url, data);
    }

    function delPlan(id) {
      var req = reqs.deleter;
      return $http[req.method](req.url, {
        params: {
          seqId: id
        }
      });
    }

    /**
     * [del 删除检查任务]
     * @param  {[string]} seqId [无]
     * @return {[object]}       [promise]
     */
    function deleteTask(seqId) {
      var param = {};
      param.seqId = seqId;
      return $http.get(apiHost.qualityManager.deleteQualityTask, {
          params: param
        })
        .then(complete);
    }

    /**
     * [save 保存修改检查任务]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function editTask(dataObj) {
      return $http.post(apiHost.qualityManager.editQualityTask, dataObj)
        .then(complete);
    }

    /**
     * [save 保存检查任务]
     * @param  {[object]} dataObj [seqId: number; damageLevelName: string; flow: string]
     * @return {[object]}         [promise]
     */
    function saveTask(dataObj) {
      return $http.post(apiHost.qualityManager.saveQualityTask, dataObj)
        .then(complete);
    }

    /**
     * [get 检查任务列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getTaskList(param) {
      // var param = {};

      // // param.deptCode = security.getUserinfo().role.deptCode;
      // deptCode ? param.deptCode = deptCode : '';
      // type ? param.type = type : '';
      // date ? param.date = date : '';

      return $http.get(apiHost.qualityManager.getQualityTaskList, {
          params: param
        })
        .then(complete);
    }

    /**
     * [get 检查任务列表信息]
     * @param  {[object]} params [查询参数对象]
     * @return {[object]}        [promise]
     */
    function getAllQualityForm() {
      return $http.get(apiHost.qualityManager.getAllQualityForm, {})
        .then(complete);
    }

    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }
  }
})();
