(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('ResearchPapersController', papersControllerFunc);

  /** @ngInject */
  function papersControllerFunc($scope, $log, $filter, paperService, modalService, toastr, security) {
    var vm = this;

    vm.savePaper = savePaper;
    vm.edit = edit;
    vm.deletePapers = deletePapers;
    vm.formData = {};
    vm.add = add;
    vm.deptCode = security.getUserinfo().role.deptCode;

    initData();

    function initData() {
      paperService.getResearchPapers().then(function(response) {
        if (response) {
          vm.list = response.data.data.lst;
        }
      });
    }

    //编辑
    function edit(row) {
      vm.formData = row;
      openEdit();
    }

    //添加
    function add() {
      vm.formData = {};
      openEdit();
    }

    //保存论文
    function savePaper() {
      paperService.saveResearchPaper(vm.formData).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          initData();
          modalService.close('success');
        }
      });
    }

    //删除论文
    function deletePapers(seqId) {
      paperService.deleteResearchPaper(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          initData();
          modalService.close('success');
        }
      });
    }

    //弹出框
    function openEdit() {
      vm.formData.deptCode = vm.deptCode;
      modalService.open({
        templateUrl: 'app/research/papers/papers.model.html',
        size: 'md',
        ok: savePaper,
        data: {
          formData: vm.formData
        }
      });
    }
  }
})();
