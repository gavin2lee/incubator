(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('TechFileController', techFileController);

  /** @ngInject */
  function techFileController($rootScope, $scope, $uibModal, deptService, NgTableParams, techFilesService) {
    var vm = this;

    vm.rootScope = $rootScope;
    vm.scope = $scope;

    //删除用户数据
    vm.del = del;
    //添加用户
    vm.open = open;
    // 加载用户列表
    vm.load = load;
    //加载用户详细信息
    vm.loadDetail = loadDetail;
    vm.detail = {};
    vm.genders = ['女', '男'];

    deptService.getDeptData().then(function(data) {
      vm.deptData = data;
      vm.getDeptName = _.memoize(function(wardCode) {
        var name;
        _.some(data.belongedDepts, function(item) {
          if (item.deptCode == wardCode) {
            name = item.deptName;
            return true;
          }
        });
        return name;
      });
    });


    function del(userCode) {
      techFilesService.delNurseJCI(userCode).then(function(response) {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '删除成功'
        });
        load();
      });
    }

    function open(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/techFiles/techFiles/modal.tpl.html',
        controller: 'TechModalController',
        backdrop: 'static',
        controllerAs: 'vm',
        animation: true,
        size: 'xlg',
        resolve: {
          id: function() {
            return angular.extend({
              deptCode: vm.selectedDept
            }, data);
          }
        }
      });

      modalInstance.result.finally(function() {
        load();
      });
    }

    function load(deptCode) {
      vm.expanedIndex = null;
      vm.expanded = false;
      if (deptCode) {
        vm.selectedDept = deptCode;
      } else {
        deptCode = vm.selectedDept;
      }
      techFilesService.getNurseList(deptCode).then(function(rsp) {
        vm.data = rsp.data.data.lst;
      });
    }

    function loadDetail(userCode) {
      techFilesService.getNurseHirReg(userCode).then(function(rsp) {
        vm.detail.nurseHirReg = rsp.data.data;
      });
      techFilesService.getNurseExp(userCode).then(function(rsp) {
        vm.detail.nurseExp = rsp.data.data;
      });
      techFilesService.getAcademic(userCode).then(function(rsp) {
        vm.detail.academic = rsp.data.data;
      });
      techFilesService.getAwardPunish(userCode).then(function(rsp) {
        vm.detail.awardPunish = rsp.data.data;
      });
      techFilesService.getPerAssessment(userCode).then(function(rsp) {
        vm.detail.perAssessment = rsp.data.data;
      });
    }

  }
})();
