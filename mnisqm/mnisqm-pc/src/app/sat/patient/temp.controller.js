(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('SatPatientTempController', SatPatientTempController);

  /** @ngInject */
  function SatPatientTempController($rootScope, $scope, $uibModal, $filter, NgTableParams, satPatientService) {
    var vm = this;

    vm.rootScope = $rootScope;
    vm.scope = $scope;

    //删除
    vm.del = del;
    //添加
    vm.open = open;
    // 加载列表
    vm.load = load;

    load();

    function del(id) {
      satPatientService.deleteSatTemp(id).then(function(response) {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '删除成功'
        });
        load();
      });
    }

    function open(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/sat/patient/temp.modal.html',
        controller: 'SatPatientTempModalController',
        controllerAs: 'vm',
        backdrop: 'static',
        animation: true,
        size: 'lg',
        resolve: {
          id: function() {
            return data;
          }
        }
      });

      modalInstance.result.finally(function() {
        load();
      });
    }

    function load() {
      satPatientService.getSatTemp().then(function(rsp) {
        vm.lst = rsp.data.data;
        vm.lst.forEach(function(v) {
          v.createTime = $filter('date')(v.createTime, 'yyyy-MM-dd HH:mm:ss');
          v.updateTime = $filter('date')(v.updateTime, 'yyyy-MM-dd HH:mm:ss');
          v.satTempDetailList.forEach(function(v2) {
            v2.createTime = $filter('date')(v2.createTime, 'yyyy-MM-dd HH:mm:ss');
            v2.updateTime = $filter('date')(v2.updateTime, 'yyyy-MM-dd HH:mm:ss');
            v2.satOptionList.forEach(function(v3) {
              v3.createTime = $filter('date')(v3.createTime, 'yyyy-MM-dd HH:mm:ss');
              v3.updateTime = $filter('date')(v3.updateTime, 'yyyy-MM-dd HH:mm:ss');
            })
          });
        })
      });
    }

  }
})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('SatPatientTempModalController', SatPatientTempModalController);

  /** @ngInject */
  function SatPatientTempModalController($rootScope, $scope, $uibModalInstance, NgTableParams, satPatientService, id) {
    var vm = this,
      handler;

    if (id) {
      vm.formData = id;
      handler = satPatientService.updateSatTemp;
    } else {
      vm.formData = {
        satTempDetailList: [],
        userType: '2'
      };
      handler = satPatientService.saveSatTemp;
    }

    vm.rootScope = $rootScope;
    vm.scope = $scope;

    //删除
    vm.del = del;
    //添加
    vm.add = add;

    vm.ok = ok;
    vm.cancel = cancel;

    function del(i) {
      vm.formData.satTempDetailList.splice(i, 1);
    }

    function add() {
      vm.formData.satTempDetailList.push({
        satOptionList: [{
          optionContent: '满意'
        }, {
          optionContent: '基本满意'
        }, {
          optionContent: '不满意'
        }]
      });
    }

    function ok() {
      handler(vm.formData).then(function() {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '保存成功'
        });
        $uibModalInstance.close();
      });
    }

    function cancel() {
      $uibModalInstance.dismiss();
    }

  }
})();
