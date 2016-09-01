/**
 * Created by lin on 16/3/15.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('UsersController', UsersControllerFunc);

  /** @ngInject */
  function UsersControllerFunc($scope, $log, $uibModal, deptService, toastr, usersService, filesService, modalService, security) {
    var vm = this;
    var dataForUserModal = {
      formData: {},
      metaData: {}
    };
    var dataForRoleModal = {
      formData: {},
      metaData: {}
    };

    deptService.getDeptData().then(function(data) {
      vm.deptData = data;
    });

    //删除用户数据
    vm.del = del;
    //添加用户
    vm.open = open;

    // 删除用户角色
    vm.delUserRole = delUserRole;

    // 添加角色
    vm.openRole = openRole;


    // 点击用户显示该用户的角色信息
    vm.showRole = showRole;

    vm.selectedDept = null;
    // 点击加载科室人员
    vm.load = load;

    function load(deptCode, deptName) {
      vm.selectedDept = deptCode;
      vm.selectedUser = false;

      dataForUserModal.formData = {
        deptCode: deptCode,
        deptName: deptName
      };

      usersService.getUserList(deptCode).then(function(response) {
        if (response.data.code === '0') {
          vm.list = response.data.data;
        }
      });

      // 获取当前选择科室的员工列表
      filesService.getUserList(deptCode).then(function(response) {
        if (response) {
          dataForUserModal.metaData.userList = response;

          $log.info(response);
        }
      });
    }

    function open(user) {
      // 编辑用户数据时加载当前用户的数据
      if (user) {
        dataForUserModal.formData = angular.extend({}, dataForUserModal.formData, user);
      } else {
        // 删除已存在的表单数据
        for (var i in dataForUserModal.formData) {
          if (dataForUserModal.formData.hasOwnProperty(i)) {
            if (i !== 'deptCode' && i !== 'deptName') {
              delete dataForUserModal.formData[i];
            }
          }
        }
      }
      modalService.open({
        templateUrl: 'app/system/users/addUser.tpl.html',
        ok: save,
        size: 'md',
        data: dataForUserModal
      });
    }

    function openRole(role) {
      // var template_url = 'app/system/roles/addRole.tpl.html';
      var template_url = 'app/system/users/addExistRole.tpl.html';

      // if (role) {
      //   dataForRoleModal.formData = angular.extend({}, dataForRoleModal.formData, role);
      //   modalService.open({
      //     templateUrl: template_url,
      //     ok: save,
      //     size: 'md',
      //     data: dataForRoleModal
      //   });
      // } else {
        template_url = 'app/system/users/addExistRole.tpl.html';

        // 获得所有角色列表
        // 获得当前用户的角色
        security.getUserRoleList({
            userId: security.getCacheUser().user.seqId
        }).then(function(response) {
          dataForRoleModal.metaData.roleList = response.data.data;
          dataForRoleModal.metaData.currentUser = role;
          dataForRoleModal.metaData.deptList = angular.extend([], vm.deptData.belongedDepts);
          // push顶层科室
          dataForRoleModal.metaData.deptList.unshift({
            deptCode: vm.deptData.deptCode,
            deptName: vm.deptData.deptName
          });
          dataForRoleModal.metaData.deptList = angular.extend({}, [dataForUserModal.formData]);
          dataForRoleModal.metaData.hadRoleList = vm.userRoleList.map(function(r) {
            return r.roleCode;
          });

          modalService.open({
            templateUrl: template_url,
            ok: saveRole,
            size: 'md',
            data: dataForRoleModal
          });
        });

      // }
    }

    function del(seqId) {
      usersService.deleteUser(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('删除成功！', '提示');

          load(dataForUserModal.formData.deptCode, dataForUserModal.formData.deptName);
          modalService.close('success');
        }
      });
    }

    function save(formData) {
      usersService.save(formData).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已保存！', '提示');
          // 刷新数据
          load(dataForUserModal.formData.deptCode, dataForUserModal.formData.deptName);
          modalService.close('success');
        }
      });
    }

    function saveRole(formData) {
      usersService.saveUserRole(formData).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('添加成功！', '提示');

          showRole(formData.seqId);
          modalService.close('success');
        }
      });
    }

    function showRole(seqId) {
      // vm.selectedUser = user;

      security.getUserRoleList({
        userId: seqId
      }).then(function(response) {
        vm.userRoleList = response.data.data;
      });
    }

    function delUserRole(seqId) {
      usersService.deleteUserRole(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('删除成功！', '提示');

          showRole(vm.selectedUser);
          modalService.close('success');
        }
      });
    }
  }

})();
