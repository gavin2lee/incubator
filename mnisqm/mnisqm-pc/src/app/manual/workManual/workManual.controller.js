(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('ManualWorkController', workControllerFunc);

  /** @ngInject */
  function workControllerFunc($scope, $log, NgTableParams, $filter, bedService, modalService, toastr, security) {
    var vm = this;

    vm.saveGroup = saveGroup;
    vm.edit = edit;
    vm.deleteGroup = deleteGroup;
    vm.formData = {};
    vm.add = add;
    vm.deptCode = security.getUserinfo().role.deptCode;

    initData();

    function initData() {
      bedService.getGroupList().then(function(response) {
        if (response) {
          vm.lst = response.data.data.lst;
          vm.tableParams = new NgTableParams({}, {
            dataset: vm.lst
          });
        }
      });
    }

    function edit(row) {
      vm.formData = angular.extend({}, row);
      vm.selectedLevel = row;

      if (row) {
        if (row.beds) {
          vm.formData.bedList = row.beds.split(';');
        }
      }
    }

    function add() {
      vm.formData = {};
    }

    /************
     ****编辑框****
     ************/
    vm.removeFlow = removeFlow;
    vm.addFlow = addFlow;

    function addFlow() {
      if (vm.flowText === '') {
        return;
      }

      vm.formData.bedList = vm.formData.bedList || [];

      vm.formData.bedList.push(vm.flowText);

      // 置空 ng-model里的值
      vm.flowText = '';
    }

    function removeFlow($index) {
      vm.formData.bedList.splice($index, 1);
    }

    function saveGroup() {
      // debugger;
      var data = angular.extend({}, vm.formData);

      data.beds = data.bedList ? data.bedList.join(';') : '';
      data.deptCode = vm.deptCode;
      // return;
      bedService.saveGroup(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          initData();
          vm.formData = {};
          modalService.close('success');
        }
      });
    }

    function deleteGroup(seqId) {
      bedService.deleteGroup(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          initData();
          //如果删除选中行，那么编辑数据清空
          if (seqId == vm.formData.seqId) {
            vm.formData = {};
          }
          modalService.close('success');
        }
      });
    }
  }



})();
