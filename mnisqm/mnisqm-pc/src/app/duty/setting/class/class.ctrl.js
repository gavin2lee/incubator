(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DepartmentManagerClassesController', classesControllerFunc);

  /** @ngInject */
  function classesControllerFunc(classesService, deptService, $scope, $log, $q, $uibModal, toastr, modalService, security, $injector, moment) {
    var vm = this;

    var dataForClassModal = {
      formData: {},
      metaData: {}
    };

    vm.saveclass = saveclass;
    vm.addClasses = open;
    vm.edit = edit;
    vm.del = del;
    vm.load = {
      func: initData,
      init: true
    };

    vm.deptCode = security.getUserinfo().role.deptCode;
    vm.deptName = security.getUserinfo().role.deptName;
    dataForClassModal.formData.deptCode = vm.deptCode;
    dataForClassModal.formData.deptName = vm.deptName;

    $scope.$on('deptchange', function(e, code) {
      vm.deptCode = code;
      vm.load.func()
    });

    //## 获取数据
    // initData();

    function initData() {
      return classesService.getClassList({
        deptCode: vm.deptCode
      }).then(function(response) {
        if (response) {
          vm.list = response.data.data.lst;
        }
      });
    }

    function open() {
      dataForClassModal.formData.deptCode = vm.deptCode;
      dataForClassModal.formData.deptName = vm.deptName;
      dataForClassModal.formData.startTime = '2099 09:00';
      dataForClassModal.formData.endTime = '2099 18:00';
      modalService.open({
        templateUrl: 'app/duty/setting/class/class.modal.html',
        size: 'md',
        ok: saveclass,
        data: dataForClassModal
      });
    }

    //## 编辑
    function edit(row) {
      dataForClassModal.formData = angular.extend({}, row);
      // 转换格式
      function timer(t) {
        var time,
          new_Time,
          final_Time;

        time = '2099-01-01' + ' ' + t;
        new_Time = new Date(time);
        final_Time = moment(new_Time).format('YYYY HH:mm');
        return final_Time;
      }

      dataForClassModal.formData.startTime = timer(row.startTime);
      dataForClassModal.formData.endTime = timer(row.endTime);

      open();
    }

    //## 删除
    function del(seqId) {
      classesService.deleteClass(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          vm.load.func();
          modalService.close('success');
        }
      });
    }

    //## 保存
    function saveclass() {
      // 初始获取页面的时间
      var init_startTime = dataForClassModal.formData.startTime,
        init_endTime = dataForClassModal.formData.endTime,
        new_startTime,
        new_endTime;

      new_startTime = moment(init_startTime).format('HH:mm');
      new_endTime = moment(init_endTime).format('HH:mm');

      // 最终提交表单的时间
      dataForClassModal.formData.startTime = new_startTime;
      dataForClassModal.formData.endTime = new_endTime;

      classesService.saveclass(dataForClassModal.formData).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          vm.load.func();
          dataForClassModal.formData = {};
          modalService.close('success');
        }
      });
    }

  }
})();