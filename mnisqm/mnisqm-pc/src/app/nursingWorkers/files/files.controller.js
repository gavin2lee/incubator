/**
 * Created by lin on 16/3/15.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('NursingWorkersFileController', filesControllerFunc);

  /** @ngInject */
  function filesControllerFunc($rootScope, $scope, $log, $uibModal, deptService, NgTableParams, filesService, toastr, modalService, FileUploader) {
    var vm = this;
    var count = 0;

    var dateArr = [
      'birthday',
      'inductionDate',
      'practiceApproveDate',
      'practiceValidDate',
      'graduationDate',
      'startDate',
      'endDate'
    ];

    vm.rootScope = $rootScope;
    vm.scope = $scope;
    vm.uploader = new FileUploader();

    vm.isOpen = true;

    deptService.getDeptData().then(function(data) {
      $log.info('科室数据', data);
      vm.deptData = data;
    });

    $log.info(count);

    //删除用户数据
    vm.del = del;
    //添加用户
    vm.open = open;

    // 加载用户列表
    vm.load = load;

    //加载用户详细信息
    vm.loadDetail = loadDetail;

    // 上传文件窗口
    vm.openUpload = openUpload;

    // vm.detail = filesService.userList;
    // vm.tableParams = filesService.tableParams;


    function load(deptCode,deptName) {
      vm.selectedDept = deptCode;
      vm.selectedUserCode = false;
      vm.expanded = false;
      vm.expanedIndex = null;

      filesService.selectedDeptCode = deptCode;
      filesService.selectedDeptName = deptName;

      filesService.getUserList(deptCode).then(function(data) {
        $log.info('用户数据', data);
        // vm.detail = filesService.userList;
        vm.userDataList = data;
      });
    }

    function open(userCode) {
      filesService.isNew = !userCode;

      if (userCode) {
        loadDetail(userCode, openDialog);
      } else {
        openDialog();
      }

      function openDialog() {
        var modalInstance = $uibModal.open({
          templateUrl: 'app/nursingWorkers/files/modal.tpl.html',
          controller: 'fileModalController',
          backdrop: 'static',
          controllerAs: 'vm',
          animation: true,
          size: 'xlg'
        });
      }
    }

    function openUpload() {
      modalService.open({
        templateUrl: 'app/nursingWorkers/files/uploadFile.modal.html',
        size: 'md',
        data: {
          formData: {
            uploader: new FileUploader({
              url: 'mnisqm/system/importSysUserExcel'
            })
          }
        }
      });
    }

    function del(row) {
      filesService.deleteUser('comUser', row.seqId).then(function(response) {
        if (response) {
          toastr.success('删除成功', '提示');

          // 刷新
          vm.scope.$emit('refreshUserList', vm.selectedDept);
        }
      });
    }

    function loadDetail(userCode, callback) {
      vm.selectedUserCode = userCode;
      filesService.getUserInfo(userCode, '').then(function(response) {
        viewDetail(response.data.data, callback);
      });
    }

    //明细数据显示
    function viewDetail(data, callback) {
      vm.detail = angular.extend({}, data);

      // 转换日期
      var dataTemp = data;
      var prop;

      for (prop in dataTemp) {
        if (dataTemp.hasOwnProperty(prop)) {
          dateArr.forEach(function(dateItem) {
            if (dataTemp[prop][dateItem]) {
              dataTemp[prop][dateItem] = new Date(dataTemp[prop][dateItem]);
            }

            if (dataTemp[prop].length > 0) {
              dataTemp[prop].map(function(tempItem) {
                if (tempItem[dateItem]) {
                  tempItem[dateItem] = new Date(tempItem[dateItem]);
                }

                return tempItem;
              });
            }
          });
        }
      }

      filesService.selectedUser = dataTemp;
      vm.rootScope.$broadcast('resetCount', filesService.selectedUser);

      if (callback) callback();
    }


    // 刷新用户列表
    vm.scope.$on('refreshUserList', function(event, deptCode) {
      load(deptCode);
    });

    // 刷新用户信息详情
    vm.scope.$on('refreshUserDetail', function(event, data) {
      vm.selectedUserCode = data.comUser.userCode; //选中行的员工编号
      viewDetail(data);
    });
  }

})();
