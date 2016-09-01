(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('JCIModalController', JCIModalController);

  /** @ngInject */
  function JCIModalController($rootScope, $scope, $uibModalInstance, $filter, NgTableParams, deptService, techFilesJCIService, id) {
    var vm = this,
      nurse = id,
      nurseId = nurse.userCode,
      capitalize = $filter('capitalize'),
      loaders = {
        getPosStatement: function() {
          techFilesJCIService.getPosStatement(nurseId).then(function(rsp) {
            vm.detail.posStatement = rsp.data.data;
          });
        },
        getPreJobTrain: function() {
          techFilesJCIService.getPreJobTrain(nurseId).then(function(rsp) {
            vm.detail.preJobTrain = rsp.data.data;
          });
        },
        getAssessment: function() {
          techFilesJCIService.getAssessment(nurseId).then(function(rsp) {
            vm.detail.assessment = rsp.data.data;
          });
        },
        getConEducation: function() {
          techFilesJCIService.getConEducation(nurseId).then(function(rsp) {
            vm.detail.conEducation = rsp.data.data;
          });
        },
        getCprAcls: function() {
          techFilesJCIService.getCprAcls(nurseId).then(function(rsp) {
            vm.detail.cprAcls = rsp.data.data;
          });
        },
        getEduRecord: function() {
          techFilesJCIService.getEduRecord(nurseId).then(function(rsp) {
            vm.detail.eduRecord = rsp.data.data;
          });
        }
      },
      initializer = {
        posStatement: {},
        preJobTrain: {
          rank: '全院',
          assessmentCondition: '合格'
        },
        assessment: {},
        conEducation: {
          rank: '全院'
        },
        cprAcls: {
          assessmentContent: '合格'
        },
        eduRecord: {
          rank: '全院',
          assessmentResult: '合格'
        }
      }

    vm.detail = {
      info: nurse
    };
    vm.rankArray = ['全院', '护理部', '科室'];
    vm.resultArray = ['合格', '不合格'];
    vm.save = save;
    vm.cancel = cancel;
    vm.addItem = addItem;
    vm.saveItem = saveItem;
    vm.removeItem = removeItem;
    loadDetail();

    function loadDetail() {
      loaders.getPosStatement();
      loaders.getPreJobTrain();
      loaders.getAssessment();
      loaders.getConEducation();
      loaders.getCprAcls();
      loaders.getEduRecord();
    }

    function save() {
      var data = _.pick(vm.detail.info, 'seqId', 'deptCode', 'hisCode', 'userName', 'birthday', 'gender', 'graduationDate', 'graduationSchool', 'education');
      techFilesJCIService.updateNurseInfo(data).then(function() {
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
      var handler = techFilesJCIService['save' + capitalize(type)],
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
        var handler = techFilesJCIService['delete' + capitalize(type)],
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
