(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('ResearchItemController', itemControllerFunc);

  /** @ngInject */
  function itemControllerFunc($scope, $log, NgTableParams, researchItemService, modalService, toastr, security) {
    var vm = this;
    var formDataForModal = {};
    var metadataForModal = {};

    vm.saveGroup = saveGroup;
    vm.edit = edit;
    vm.deleteGroup = deleteGroup;
    vm.add = add;
    vm.deptCode = security.getUserinfo().role.deptCode;
    vm.open = openEdit;

    initData();

    function initData() {
      researchItemService.getResearchItems().then(function(response) {
        if (response) {
          vm.list = response.data.data.lst;
          vm.tableParams = new NgTableParams({}, {
            dataset: vm.lst
          });
        }
      });
    }

    //编辑
    function edit(row) {
      formDataForModal = row;
      openEdit();
    }

    function add() {
      formDataForModal = {};
      openEdit();
    }

    //保存数据
    function saveGroup() {
      researchItemService.saveResearchItem(formDataForModal).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          initData();
          modalService.close('success');
        }
      });
    }
    //删除数据
    function deleteGroup(seqId) {
      researchItemService.deleteResearchItem(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          initData();
          modalService.close('success');
        }
      });
    }
    //弹出框
    function openEdit() {
      formDataForModal.deptCode = vm.deptCode;
      modalService.open({
        templateUrl: 'app/research/item/item.model.html',
        size: 'md',
        ok: saveGroup,
        data: {
          formData: formDataForModal
        }
      });
    }
  }

})();
