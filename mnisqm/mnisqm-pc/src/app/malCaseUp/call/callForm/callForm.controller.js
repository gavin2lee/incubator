(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('CallFormController', CallFormController);
/** @ngInject */
  function CallFormController($scope, $q, $state, $stateParams, $timeout, security, moment, levelService, modalService, toastr, fixService, callService, deptService) {
    var vm = this;
    var level_promise;
    var dept_promise;

    vm.formData = {};
    vm.metaData = {};

    vm.state = $state;
    vm.save = save;
    vm.openPicker = openPicker;
    vm.load = {
      func: initData,
      init: true
    }

    // mock数据
    vm.metaData.eventTypeList = [{
      code: 1,
      name: '运送中病情变化'
    }, {
      code: 2,
      name: '误吸'
    }, {
      code: 3,
      name: '院内压疮'
    }, {
      code: 4,
      name: '坠床'
    }, {
      code: 5,
      name: '跌倒'
    }];

    // initData();

    // 获取不良事件数据
    function initData() {
      var deptCache = deptService.getCacheDeptList();

      deptCache ? (vm.metaData.deptList = deptCache.belongedDepts) :
      dept_promise = deptService.getDeptData().then(function (data) {
        vm.metaData.deptList = data.belongedDepts;

        insertMainDept(data);

        return data.belongedDepts;
      });

      if (deptCache) {
        insertMainDept(deptCache);
        dept_promise = $q.defer();

        dept_promise.resolve(vm.metaData.deptList);
      }

      level_promise = levelService.get().then(function(response) {
        if (response) {
          vm.metaData.levelList = response.data.data.lst;

          return response.data.data.lst;
        }
      });

      if ($stateParams.reportCode !== '' && $stateParams.reportCode) {
        return callService.getDetail($stateParams.reportCode).then(function (response) {
          vm.formData = response.data.data;

          // 设置选中时间等级
          level_promise.then(function (list) {
            vm.selectedLevel = list.find(function (item) {
              return item.damageLevel === vm.formData.damageLevel;
            });
          })

          // 选中科室
          if (typeof dept_promise.then !== 'function') {
            dept_promise.promise.then(function (res) {
              vm.selectedDept = res.find(function (item) {

                return item.deptCode === vm.formData.deptCode;
              });
            })
          } else {
            dept_promise.then(function (res) {
              vm.selectedDept = res.find(function (item) {

                return item.deptCode === vm.formData.deptCode;
              })
            });
          }

          // 时间转换
          vm.formData.eventDate = new Date(vm.formData.eventTime.split(' ')[0]);
          vm.formData.eventTime = new Date(vm.formData.eventTime);
        })
      } else {
        // 新建新的上报时，上报人为当前登录人
        vm.formData.userName = security.getUserinfo().user.userName;
        vm.formData.eventDate = new Date();
        vm.formData.eventTime = new Date();
        return level_promise;
      }
    }

    function insertMainDept(data) {
      vm.metaData.deptList.unshift({
        deptName: data.deptName,
        deptCode: data.deptCode
      });
    }

    function openPicker() {
        vm.eventTimeOpen = !vm.eventTimeOpen;
    }

    function save() {
      // debugger;
      var data = angular.merge({}, vm.formData);
      var date_temp;
      var time_temp;
      // 科室
      data.deptName = vm.selectedDept.deptName;
      // 事件发生时间
      date_temp = moment(data.eventDate).format('YYYY-MM-DD');
      time_temp = moment(data.eventTime).format('HH:mm:ss');
      delete data.eventDate;
      delete data.dept;

      data.eventTime = moment(date_temp + ' ' + time_temp).format('YYYY-MM-DD HH:mm:ss');

      callService.save(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          // vm.load.func();
          modalService.close('success');

          $timeout(function () {
            $state.go('home.call', null, {
              reload: true
            });
          }, 500)
        }
      });
    }
  }
})();
