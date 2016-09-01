(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('TechFileJCIController', techFileJCIController);

  /** @ngInject */
  function techFileJCIController($rootScope, $scope, $uibModal, deptService, NgTableParams, techFilesJCIService) {
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
      techFilesJCIService.delNurseJCI(userCode).then(function(response) {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '删除成功'
        });
        load();
      });
    }

    function open(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/techFiles/jci/modal.tpl.html',
        controller: 'JCIModalController',
        controllerAs: 'vm',
        backdrop: 'static',
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
      techFilesJCIService.getNurseList(deptCode).then(function(rsp) {
        vm.data = rsp.data.data.lst;
      });
    }

    function loadDetail(userCode) {
      techFilesJCIService.getPosStatement(userCode).then(function(rsp) {
        vm.detail.posStatement = rsp.data.data;
      });
      techFilesJCIService.getPreJobTrain(userCode).then(function(rsp) {
        vm.detail.preJobTrain = rsp.data.data;
      });
      techFilesJCIService.getAssessment(userCode).then(function(rsp) {
        vm.detail.assessment = rsp.data.data;
      });
      techFilesJCIService.getConEducation(userCode).then(function(rsp) {
        vm.detail.conEducation = rsp.data.data;
      });
      techFilesJCIService.getCprAcls(userCode).then(function(rsp) {
        vm.detail.cprAcls = rsp.data.data;
      });
      techFilesJCIService.getEduRecord(userCode).then(function(rsp) {
        vm.detail.eduRecord = rsp.data.data;
      });
    }

  }
})();
