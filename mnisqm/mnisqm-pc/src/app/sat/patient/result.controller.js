(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('SatPatientResultController', SatPatientResultController);

  /** @ngInject */
  function SatPatientResultController($rootScope, $scope, $uibModal, $filter, NgTableParams, satPatientService, deptService) {
    var vm = this;

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
      satPatientService.deleteSatResult(id).then(function(response) {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '删除成功'
        });
        load();
      });
    }

    function open(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/sat/patient/result.modal.html',
        controller: 'SatPatientResultModalController',
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
      satPatientService.getSatResult(vm.params).then(function(rsp) {
        vm.lst = rsp.data.data;
        vm.lst.forEach(function(v) {
          v.createTime = $filter('date')(v.createTime, 'yyyy-MM-dd HH:mm:ss');
          v.updateTime = $filter('date')(v.updateTime, 'yyyy-MM-dd HH:mm:ss');
          v.satResultDetailList.forEach(function(v2) {
            v2.createTime = $filter('date')(v2.createTime, 'yyyy-MM-dd HH:mm:ss');
            v2.updateTime = $filter('date')(v2.updateTime, 'yyyy-MM-dd HH:mm:ss');
            v2.satOptionList.forEach(function(v3) {
              v3.createTime = $filter('date')(v3.createTime, 'yyyy-MM-dd HH:mm:ss');
              v3.updateTime = $filter('date')(v3.updateTime, 'yyyy-MM-dd HH:mm:ss');
            })
          });
        });
      });
    }

  }
})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('SatPatientResultModalController', SatPatientResultModalController);

  /** @ngInject */
  function SatPatientResultModalController($rootScope, $scope, $uibModalInstance, NgTableParams, deptService, satPatientService, id) {
    var vm = this,
      handler;

    satPatientService.getSatTemp().then(function(rsp) {
      vm.tmps = rsp.data.data;
      vm.getTmpName = _.memoize(function(formCode) {
        var name;
        _.some(vm.tmps, function(item) {
          if (item.formCode == formCode) {
            name = item.formName;
            return true;
          }
        });
        return name;
      });
      vm.getTmpType = _.memoize(function(formCode) {
        var name;
        _.some(vm.tmps, function(item) {
          if (item.formCode == formCode) {
            name = item.formType;
            return true;
          }
        });
        return name;
      });
    });

    deptService.getDeptData().then(function(data) {
      vm.deptData = data;
    });

    if (id) {
      vm.detail = id;
      handler = satPatientService.updateSatResult;
    } else {
      vm.detail = {
        satResultDetailList: [],
        userType: '2'
      };
      vm.detail.satResultDetailList = vm.detail.satTempDetailList;
      handler = satPatientService.saveSatResult;
      vm.isNew = true;
    }

    vm.ok = ok;
    vm.cancel = cancel;
    vm.reload = reload;

    function del(i) {
      vm.formData.satTempDetailList.splice(i, 1);
    }

    function ok() {
      handler(vm.detail).then(function() {
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

    function reload() {
      if (vm.tmpCode) {
        vm.detail.resultName = vm.getTmpName(vm.tmpCode);
        vm.detail.formType = vm.getTmpType(vm.tmpCode);
        satPatientService.getSatTempByCode(vm.tmpCode)
          .then(function(rsp) {
            vm.detail.satResultDetailList = [];
            rsp.data.data.satTempDetailList.forEach(function(v, i) {
              var item = _.pick(v, 'content', 'formCode', 'detailCode'),
                options = [];
              v.satOptionList.forEach(function(v2, i2) {
                var option = _.pick(v2, 'detailCode', 'optionCode', 'optionContent', 'seqId');
                options.push(option);
              });
              item.satOptionList = options;
              vm.detail.satResultDetailList.push(item);
            });
          });
      } else {
        vm.detail.resultName = '';
        vm.detail.formType = '';
        vm.detail.satResultDetailList = [];
      }
    }

  }
})();
