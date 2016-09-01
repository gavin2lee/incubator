(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityCheckController', checkControllerFunc);

  /** @ngInject */
  function checkControllerFunc($scope, $log, $filter, checkService, modalService, toastr, security, deptService) {
    var vm = this;
    var dataForCheckModal = {
      formData: {},
      metaData: {}
    };

    vm.options = {
      month: {
        datepickerMode: 'month',
        minMode: 'month'
      }
    };

    vm.add = add;
    vm.save = save;
    vm.edit = edit;
    vm.del = del;
    vm.deptCode = security.getUserinfo().role.deptCode;
    vm.load = {
      func: initData,
      init: true
    };

    vm.queryParams = {
      month: new Date(),
      deptCode: security.getUserinfo().role.deptCode
    };

    deptService.getDeptData().then(function(data) {
      vm.deptData = handleData(data);
    });

    function add() {
      dataForCheckModal = {
        formData: {},
        metaData: {}
      };

      modalService.open({
        templateUrl: 'app/quality/check/editCheck.tpl.html',
        size: 'lg',
        ok: save,
        data: dataForCheckModal,
        methodsObj: {
          deptData: vm.deptData
        }
      });
    }

    function handleData(data) {
      var result = [];
      result = result.concat([_.pick(data, 'deptCode', 'deptName')]);
      _.each(data.belongedDepts, function(v) {
        result = result.concat(handleData(v));
      });
      return result;
    }

    function initData() {
      var params = angular.extend({}, vm.queryParams);
      params.month = moment(params.month).format('YYYY-MM');
      return checkService.getCheckList(params).then(function(response) {
        if (response) {
          vm.list = response.data.data;
        }
      });
    }

    // dataForLeaveModal.formData.deptCode = security.getUserinfo().role.deptCode;

    //## 编辑
    function edit(row) {
      dataForCheckModal.formData = angular.extend({}, row);

      modalService.open({
        templateUrl: 'app/quality/check/editCheck.tpl.html',
        size: 'lg',
        ok: save,
        data: dataForCheckModal,
        methodsObj: {
          deptData: vm.deptData
        }
      });

    }

    //## 保存
    function save() {
      var data,
        array_length,
        parrent_create_Time,
        child_create_Time,
        len,
        parent_count = 0,
        child_count;

      data = angular.extend({}, dataForCheckModal.formData);

      len = data.qualityResultDetailList.length;

      for (var i = 0; i < len; i++) {
        child_count = data.qualityResultDetailList[i].count;
        parent_count = parent_count + Number(child_count);
      }

      data.count = parent_count;

      parrent_create_Time = data.createTime;
      data.createTime = moment(parrent_create_Time).format('YYYY-MM-DD hh:mm:ss');
      parrent_create_Time = data.updateTime;
      data.updateTime = moment(parrent_create_Time).format('YYYY-MM-DD hh:mm:ss');

      if (data.qualityResultDetailList) {
        array_length = data.qualityResultDetailList.length;
        for (var i = 0; i < array_length; i++) {
          child_create_Time = data.qualityResultDetailList[i].createTime;
          data.qualityResultDetailList[i].createTime = moment(child_create_Time).format('YYYY-MM-DD hh:mm:ss');
          data.qualityResultDetailList[i].updateTime = moment(child_create_Time).format('YYYY-MM-DD hh:mm:ss');
        }
      }

      checkService.saveEditCheck(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          initData();
          vm.dataForCheckModal = {};
          modalService.close('success');
        }
      });
    }

    //## 删除
    function del(seqId) {
      checkService.deleteCheck(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          initData();
          modalService.close('success');
        }
      });
    }
  }




})();
