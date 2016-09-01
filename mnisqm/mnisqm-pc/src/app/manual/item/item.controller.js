(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('ManualItemController', itemControllerFunc);

  /** @ngInject */
  function itemControllerFunc($scope, $log, NgTableParams, $filter, manualItemService, toastr, security, deptService) {
    var vm = this;

    vm.save = save;
    vm.edit = edit;
    vm.deleteItem = deleteItem;
    vm.formData = {};
    vm.add = add;
    vm.deptCode = security.getUserinfo().role.deptCode;
    vm.deptList = deptService.deptList;

    if (vm.deptList.length === 0) {
      deptService.getDeptData().then(function(data) {
        // debugger;
        vm.deptList = data.belongedDepts;
        vm.deptList.unshift({
          deptCode: data.deptCode,
          deptName: data.deptName
        })
      })
    }

    initData();

    function initData() {
      manualItemService.getItems().then(function(response) {
        // debugger;
        if (response) {
          vm.lst = response.data.data;
          vm.tableParams = new NgTableParams({}, {
            dataset: vm.lst
          });
        }
      });
    }

    function edit(row) {
      vm.formData = angular.extend({}, row);
      vm.selectedItem = row;
      vm.formData.dept = {
        deptCode: vm.formData.deptCode,
        deptName: vm.formData.deptName
      }
    }

    function add() {
      vm.formData = {};
    }

    function save() {
      // debugger;
      var data = angular.extend({}, vm.formData);
      data.deptCode = data.dept.deptCode;
      data.deptName = data.dept.deptName;

      delete data.dept;
      // return;
      manualItemService.saveItem(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          initData();
          vm.formData = {};
        }
      });
    }

    function deleteItem(seqId) {
      manualItemService.deleteItem(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          initData();
          //如果删除选中行，那么编辑数据清空
          if (seqId == vm.formData.seqId) {
            vm.formData = {};
          }
        }
      });
    }
  }



})();
