(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DepartmentManagerLeaveController', leaveControllerFunc);

  /** @ngInject */
  function leaveControllerFunc($uibModal, leaveService, $scope, moment, toastr, modalService, security) {
    var vm = this, deptCode = security.getUserinfo().role.deptCode;
    var formData = {};

    vm.open = open;
    vm.del = del;
    vm.review = review;
    vm.upload = upload;
    vm.load = {
      func: initData,
      init: true
    };

    $scope.$on('deptchange', function(e, code) {
      deptCode = code;
      vm.load.func()
    });


    //## 获取数据
    // initData();

    function initData() {
      return leaveService.getLeaveList({
        deptCode: deptCode
      }).then(function(response) {
        if (response) {
          vm.list = response.data.data.lst;
          for (var i = 0; i < vm.list.length; i++) {
            vm.list[i].startTime = moment(new Date(vm.list[i].startTime)).format('YYYY-MM-DD');
            vm.list[i].endTime = moment(new Date(vm.list[i].endTime)).format('YYYY-MM-DD');
          }
        }
      });
    }

    formData.deptCode = security.getUserinfo().role.deptCode;
    formData.deptName = security.getUserinfo().role.deptName;
    formData.userCode = security.getUserinfo().user.userCode;

    function open(data) {
      var modalInstance = $uibModal.open({
        templateUrl: 'app/duty/leave/leave/leave.modal.html',
        controller: 'LeaveModalCtrl',
        controllerAs: 'vm',
        backdrop: 'static',
        animation: true,
        size: 'lg',
        resolve: {
          id: function() {
            return angular.copy(data);
          }
        }
      });

      modalInstance.result.finally(function() {
        vm.load.func();
      });
    }

    // 获取日期
    function dater() {
      formData.startDate = new Date(formData.startTime);
      formData.endDate = new Date(formData.endTime);
    }

    function fen() {
      formData.startTime = moment(formData.startTime).format('YYYY-MM-DD HH:mm:ss');
      formData.endTime = moment(formData.endTime).format('YYYY-MM-DD HH:mm:ss');
    }

    //## 删除
    function del(seqId) {
      leaveService.deleteScheduleLeave(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          vm.load.func();
          modalService.close('success');
        }
      });
    }

    //## 审核
    function review(row) {
      formData = angular.extend({}, row);

      dater();
      fen();

      modalService.open({
        templateUrl: 'app/duty/leave/leave/review.tpl.html',
        size: 'md',
        ok: saveReview,
        data: {
          formData: formData
        }
      });
    }

    function saveReview() {
      leaveService.apprvScheduleLeave(formData).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          vm.load.func();
          formData = {};
          modalService.close('success');
        }
      });
    }

    function upload(row) {
      formData = angular.extend({}, row);
      formData.approveStatus = '02';
      fen();
      leaveService.apprvScheduleLeave(formData).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          vm.load.func();
        }
      });
    }

  }

})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('LeaveModalCtrl', LeaveModalCtrl);

    function LeaveModalCtrl($rootScope, $uibModalInstance, leaveService, security, id) {
      var vm = this;
      vm.formData = id || {};
      vm.cancel = cancel;
      vm.ok = ok;
      init();
      vm.startOptions = {
        maxDate: vm.formData.endDate,
        showWeeks: false
      };
      vm.endOptions = {
        minDate: vm.formData.startDate,
        showWeeks: false
      };
      vm.startChange = startChange;
      vm.endChange = endChange;
      function cancel() {
        $uibModalInstance.dismiss();
      }

      function init() {
        var today = new Date();
        today = today - (today % (3600 * 1000 * 24) );
        vm.formData.startDate = vm.formData.startTime ? new Date(vm.formData.startTime) : undefined;
        vm.formData.endDate = vm.formData.endTime ? new Date(vm.formData.endTime) : undefined;
        countDays();
      }

      function startChange() {
        if(vm.formData.startDate) {
          vm.endOptions.minDate = vm.formData.startDate;
        }
        if(vm.formData.startDate && vm.formData.endDate) {
          countDays();
        }
      }

      function endChange() {
        if(vm.formData.endDate) {
          vm.startOptions.maxDate = vm.formData.endDate;
        }
        if(vm.formData.startDate && vm.formData.endDate) {
          countDays();
        }
      }

      function countDays() {
        vm.formData.days = (vm.formData.endDate - vm.formData.startDate) / (3600 * 1000 * 24) + 1;
      }

      
      //## 保存
      function ok() {
        // 初始获取页面的时间
        var init_startDate = vm.formData.startDate,
          init_startTime = vm.formData.startTime,
          init_endDate = vm.formData.endDate,
          init_endTime = vm.formData.endTime;

        // 处理时间格式
        function timer(d, t) {
          var date, time, result;
          date = moment(d).format('YYYY-MM-DD');
          time = moment(t).format('HH:mm:ss');
          result = moment(date + ' ' + time).format('YYYY-MM-DD HH:mm:ss')
          return result;
        }

        // 最终提交表单的时间
        vm.formData.startTime = timer(init_startDate, init_startTime);
        vm.formData.endTime = timer(init_endDate, init_endTime);

        vm.formData.deptCode = security.getUserinfo().role.deptCode;
        vm.formData.deptNme = security.getUserinfo().role.deptNme;
        leaveService.saveScheduleLeave(vm.formData).then(function(response) {
          $rootScope.$broadcast('toast');
          $uibModalInstance.close('success');
        });
      }
    }

})();