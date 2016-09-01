/**
 * Created by lin on 16/3/15.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .config(function(w5cValidatorProvider) {
      w5cValidatorProvider.config({
        showError: false,
        removeError: true
      });
      w5cValidatorProvider.setRules({
        userCode: {
          number: '必须输入数字'
        },
        userName: {
          required: "姓名不能为空"
        },
        duty: {
          required: "职务不能为空"
        },
        email: {
          required: "邮箱不能为空"
        },
        phone: {
          required: "电话不能为空"
        },
        positionName: {
          required: '必填'
        },
        reviewUnit: {
          required: '必填'
        }
      });
    })
    .controller('fileModalController', fileModalControllerFunc);

  function fileModalControllerFunc($rootScope, $scope, $uibModalInstance, filesService, $log, moment, toastr, $window, $controller) {
    var vm = this;

    vm.rootScope = $rootScope;
    vm.scope = $scope;
    vm.deptName =  filesService.selectedDeptName;

    vm.cancel = cancel;
    vm.datepickerOption = {
      dateDisabled: 'disabled',
      formatYear: 'yyyy',
      formatMonth: 'MM',
      formatDay: 'dd',
      initDate: new Date(),
      maxDate: new Date(2020, 5, 22),
      minDate: moment(),
      startingDay: 1
    };
    vm.validationOption = {
      blurTrig: true
    };

    vm.accessible = !filesService.isNew;

    // 保存用户
    vm.save = save;
    // 添加项目
    vm.count = filesService.isNew ? {} : angular.extend({}, filesService.selectedUser);
    // 信息为空时，添加项目时的初始化数据
    var itemInitData = {
      comUserPosition: {
        positionName: '',
        reviewUnit: '',
        startDate: '',
        endDate: ''
      },
      comUserNursing: {
        reviewUnit: '',
        unitLevel: '',
        deptName: '',
        position: '',
        startDate: '',
        endDate: ''
      },
      comUserEducation: {
        educationType: '',
        leaningTime: '',
        courseTopics: '',
        courseContent: '',
        startDate: '',
        endDate: ''
      },
      comUserTraining: {
        learnContent: '',
        professional: '',
        diploma: '',
        speaker: '',
        creditType: '',
        credit: '',
        funding: '',
        describe: '',
        startDate: '',
        endDate: ''
      },
      comUserLearning: {
        school: '',
        professional: '',
        diploma: '',
        diplomaNo: '',
        degree: '',
        degreeNo: '',
        describe: '',
        startDate: '',
        endDate: ''
      },
      comUserFamily: {
        relation: '',
        name: '',
        phone: '',
        email: '',
        remark: ''
      }
    };
    vm.addItem = function(type) {
      vm.count[type].push(angular.extend({}, itemInitData[type]));
    };
    // 删除职称
    vm.removeItem = function(type, index, seqId) {
      if (seqId) {
        filesService.deleteUser(type, seqId).then(function(response) {
          if (response) {
            vm.count[type].splice(index, 1);
          }
        });
      } else {
        vm.count[type].splice(index, 1);
      }
    };

    vm.openCalender = function(item, obj) {
      if (obj) {
        obj[item] = true;
      } else {
        vm[item] = true;
      }
    };

    function save(type) {
      var data = {};

      data[type] = vm.count[type];

      if (type == 'comUser') {
        //日期格式转换
        if (data[type].birthday) data[type].birthday = moment(data[type].birthday).format('YYYY-MM-DD');
        if (data[type].practiceApproveDate) data[type].practiceApproveDate = moment(data[type].practiceApproveDate).format('YYYY-MM-DD');
        if (data[type].inductionDate) data[type].inductionDate = moment(data[type].inductionDate).format('YYYY-MM-DD');
        if (data[type].graduationDate) data[type].graduationDate = moment(data[type].graduationDate).format('YYYY-MM-DD');
        if (data[type].practiceValidDate) data[type].practiceValidDate = moment(data[type].practiceValidDate).format('YYYY-MM-DD');
        // 添加新用户需要添加当前的deptCode
        vm.count[type].deptCode = filesService.selectedDeptCode;
      } else {
        if (type !== 'comUserFamily') {
          data[type].startDate = moment(data[type].startDate).format('YYYY-MM-DD');
          data[type].endDate = moment(data[type].endDate).format('YYYY-MM-DD');
        }
        // 给除基本信息外的每个项添加userCode
        data[type].map(function(item, i) {
          item.userCode = filesService.selectedUser.comUser.userCode;

          return item;
        });
      }

      data.dataType = type;
      filesService.add(data).then(function(response) {
        if (response) {
          toastr.success('保存成功！', '提示');

          filesService.getUserInfo(vm.count.comUser.userCode, vm.count.comUser.hisCode).then(function(data) {
            vm.accessible = true;
            // 刷新
            vm.rootScope.$broadcast('refreshUserList', filesService.selectedDeptCode);
            vm.rootScope.$broadcast('refreshUserDetail', data);
          });
        }
      });
    }

    function cancel() {
      $uibModalInstance.dismiss('cancel');
    }

    vm.scope.$on('resetCount', function(event, data) {
      vm.count = data;
    });
  }
})();
