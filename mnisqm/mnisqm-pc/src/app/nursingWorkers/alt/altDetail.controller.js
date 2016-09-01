(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('AltDetailCtrl', AltDetailCtrl);

  /** @ngInject */
  function AltDetailCtrl($rootScope, $scope, $state, $stateParams, deptService, baseInfoService, moment) {
    var vm = this,
      currentNode, userinfo = {};
    vm.originalDetail = null;

    vm.scope = $scope;
    vm.selectedNode = {};
    vm.deptList = deptService.getCacheDeptList();

    // 把护理部放到最顶部
    vm.saveChange = saveChange;
    vm.saveUserDept = saveUserDept;

    init();

    function transfromDeptList() {
      try {
        vm.deptList.belongedDepts.unshift({
          deptCode: vm.deptList.deptCode,
          deptName: vm.deptList.deptName
        });
        vm.deptList = vm.deptList.belongedDepts;
      } catch (err) {}
    }

    function init() {
      if (!vm.deptList) {
        deptService.getDeptData().then(function(response) {
          vm.deptList = response;
          transfromDeptList();
        })
      } else {
        transfromDeptList();
      }
      getChanges($stateParams.userCode);
      getData($stateParams.userCode)
    }

    function getChanges(id) {
      baseInfoService.getComUserChange(id).then(function(res) {
        vm.clinicalList = res.data.data.filter(function(dataItem) {
          return parseInt(dataItem.changeType) === 1;
        });
        vm.teachingList = res.data.data.filter(function(dataItem) {
          return parseInt(dataItem.changeType) === 2;
        });

        vm.splitedDeptList = res.data.data.filter(function(dataItem) {
          return parseInt(dataItem.changeType) === 3;
        });
      });
    }

    function saveChange(type) {
      var foreName;
      var rearName;

      switch (type) {
        case 1:
          foreName = vm.originalDetail.comUser.clinical;
          rearName = vm.detail.comUser.clinical;
          break;

        case 2:
          foreName = vm.originalDetail.comUser.teachLevel;
          rearName = vm.detail.comUser.teachLevel;
          break;
          // 科室更改有单独接口
        case 3:
          foreName = vm.originalDetail.comUser.deptCode;
          rearName = vm.detail.comUser.deptCode;
          break;
      }

      baseInfoService.saveChange({
        userCode: $stateParams.userCode,
        changeType: type,
        foreName: foreName,
        rearName: rearName,
        changeTime: moment().format('YYYY-MM-DD HH:mm:ss')
      }).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: response.data.msg || '保存成功！'
          });
        }
      })
    }

    function saveUserDept() {
      baseInfoService.saveUserDept(vm.detail.comUser.userCode, vm.detail.comUser.deptCode).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '保存成功！'
          });

          vm.load.func();
        }
      })
    }

    vm.openCalender = function(item, obj) {
      if (obj) {
        obj[item] = true;
      } else {
        vm[item] = true;
      }
    };
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
    vm.addItem = function(type) {
      vm.detail[type].push(angular.extend({}, itemInitData[type]));
    };

    vm.removeItem = function(type, index) {
      vm.detail[type].splice(index, 1);
    };
    vm.save = save;
    vm.load = {
      func: getData,
      init: true
    }

    function getData(userCode) {
      var _userCode = userCode || $stateParams.userCode;

      return baseInfoService.getUserInfo(_userCode, '').then(function(response) {
        vm.originalDetail = angular.merge({}, response.data.data);
        vm.detail = response.data.data;
        // 设置开始时间的初始化时间
        vm.detail.comUser.positionStartDate = moment().format('YYYY-MM-DD');
        vm.detail.comUser.dutyStartDate = moment().format('YYYY-MM-DD');
        vm.scope.userName = vm.originalDetail.comUser.userName;

        vm.originalDetail.comUser.deptCode = $stateParams.deptCode;
        vm.originalDetail.comUser.deptName = $stateParams.deptName;

        $rootScope.$broadcast('loaded', 'content');
      });
    }

    function save(type, name) {
      // TODO 保存职称变更和职务变更不清晰, 还需要再确认.
      var data = {};

      data[type] = {
        userCode: vm.detail.comUser.userCode
      };

      if (type == 'comUser') {
        data[type].deptCode = userinfo.dept;
      }

      switch (type) {
        case 'comUserPosition':
          data[type].reviewUnit = vm.detail.comUser.reviewUnit;
          data[type].startDate = vm.detail.comUser.positionStartDate;
          break;

        case 'comUserNursing':
          data[type].startDate = vm.detail.comUser.dutyStartDate;
          break;
        default:
          break;
      }

      data[type][name] = vm.detail.comUser[name];
      data.dataType = type;
      baseInfoService.add(data).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '保存成功！'
          });

          vm.load.func();
        }
      });
    }

  }


})();
