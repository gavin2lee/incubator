(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('TechModalController', TechModalController);

  /** @ngInject */
  function TechModalController($rootScope, $scope, $uibModalInstance, $filter, NgTableParams, deptService, techFilesService, id) {
    var vm = this,
      nurse = id,
      nurseId = nurse.userCode,
      capitalize = $filter('capitalize'),
      filter = $filter('filter'),
      loaders = {
        getNurseHirReg: function() {
          techFilesService.getNurseHirReg(nurseId).then(function(rsp) {
            vm.detail.nurseHirReg = rsp.data.data;
          });
        },
        getNurseExp: function() {
          techFilesService.getNurseExp(nurseId).then(function(rsp) {
            vm.detail.nurseExp = rsp.data.data;
          });
        },
        getAcademic: function() {
          techFilesService.getAcademic(nurseId).then(function(rsp) {
            vm.detail.academic = rsp.data.data;
          });
        },
        getAwardPunish: function() {
          techFilesService.getAwardPunish(nurseId).then(function(rsp) {
            vm.detail.award = filter(rsp.data.data, { apType: 1 });
            vm.detail.punish = filter(rsp.data.data, { apType: 2 });
          });
        },
        getPerAssessment: function() {
          techFilesService.getPerAssessment(nurseId).then(function(rsp) {
            vm.detail.perAssessment = rsp.data.data;
          });
        }
      },
      initializer = {
        nurseHirReg: {},
        nurseExp: {
          rank: '一般专科护士'
        },
        academic: {},
        award: { apType: 1 },
        punish: { apType: 2 },
        perAssessment: {}
      };

    deptService.getDeptData().then(function(data) {
      vm.deptData = data.belongedDepts;
    });

    vm.detail = {
      info: nurse
    };
    vm.rankArray = ['一般专科护士', '高级专科护士', '临床护理专家'];
    vm.save = save;
    vm.cancel = cancel;
    vm.addItem = addItem;
    vm.saveItem = saveItem;
    vm.removeItem = removeItem;
    loadDetail();

    function loadDetail() {
      loaders.getNurseHirReg();
      loaders.getNurseExp();
      loaders.getAcademic();
      loaders.getAwardPunish();
      loaders.getPerAssessment();
    }

    function save() {
      var data = _.pick(vm.detail.info, 'deptCode', 'hisCode', 'userName', 'birthday', 'gender', 'graduationDate', 'graduationSchool', 'education');
      techFilesService.updateNurseInfo(data).then(function() {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '保存成功'
        });
      });
    }

    function cancel() {
      $uibModalInstance.dismiss();
    }

    function addItem(type) {
      vm.detail[type].push(angular.copy(initializer[type]));
    }

    function saveItem(type, data) {
      var handler = techFilesService['save' + capitalize(type)],
        loader = loaders['get' + capitalize(type)],
        data = angular.extend({}, data);
      purify(data);
      handler(data).then(function() {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '保存成功'
        });
        loader();
      });
    }

    function removeItem(type, seqId, i) {
      if (seqId) {
        var handler = techFilesService['delete' + capitalize(type)],
          loader = loaders['get' + capitalize(type)];
        handler(seqId).then(function() {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '删除成功'
          });
          loader();
        });
      } else {
        vm.detail[type].splice(i, 1);
      }
    }

    function purify(data) {
      for (var i in data) {
        if (i.indexOf('IsOpen') > -1) {
          delete data[i];
        }
      }
      data.userCode = nurse.userCode;
      data.userName = nurse.userName;
      return data;
    }

  }

})();
