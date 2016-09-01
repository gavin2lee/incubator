/**
 * Created by gary on 16/4/1.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('RolesController', rolesControllerFunc);

  /** @ngInject */
  function rolesControllerFunc($scope, $log, $q, $uibModal, rolesService, toastr, NgTableParams, modalService, security, $injector) {
    var vm = this;

    security.getUserRoleList().then(function(response) {
      vm.roleData = response.data.data;
    });

    var modalInstance;
    // TODO 首次进入界面表单无数据时，保存和删除按钮不可用状态

    //删除用户数据
    vm.del = del;
    //编辑角色数据
    vm.edit = edit;
    //添加角色
    vm.open = open;

    vm.navExchange = navExchange;

    // 点击加载角色人员
    vm.load = load;

    function load(role) {

      var getNotHave = rolesService.getNotHaveFunction(role.roleCode);
      var getHave = rolesService.getHaveFunction(role.roleCode);

      vm.roleDetail = role;

      $q.all([getNotHave, getHave]).then(function(result) {
        $log.info(result);
        vm.notHaveList = result[0] && result[0].data.code === '0' ? result[0].data.data : [];
        vm.hadList = result[1] && result[1].data.code === '0' ? result[1].data.data : [];
      });
    }

    function open() {
      modalService.open({
        templateUrl: 'app/system/roles/addRole.tpl.html',
        size: 'md',
        ok: save
      });
    }

    /**
     * [del 删除角色]
     * @param  {[type]} row [description]
     * @return {[type]}     [description]
     */
    function del() {
      var delObj = {
        seqId: vm.roleDetail.seqId,
        roleCode: vm.roleDetail.roleCode
      };

      rolesService.del(delObj).then(function(response) {
        if (response) {
          toastr.success('该角色已删除', '提示');
          vm.roleDetail = null;
          refreshData();
        }
      });
    }

    // TODO 如果修改的角色为当前角色，修改后的导航菜单需要重新获取加载。
    /**
     * [edit 修改当前用户的角色信息]
     * @return {[undefined]}
     */
    function edit() {
      var params = angular.copy(vm.roleDetail);
      var funs = [];

      params.funs = angular.copy(vm.hadList);

      // 提取子菜单
      params.funs.forEach(function(fun, i) {
        funs = funs.concat(fun.child);
        fun.child = [];
      });
      params.funs = params.funs.concat(funs);

      delete params.updateTime;
      delete params.createTime;

      $log.info(params);

      rolesService.edit(params).then(function(response) {
        $log.info(response);

        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');

          $log.info(vm.hadList);
        } else {
          toastr.error(response.data.msg, '警告');
        }
      });
    }

    function save(formData) {
      rolesService.save(formData).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('添加成功！', '提示');
          modalService.close('success');
          refreshData();
        } else {
          toastr.error('保存失败', '警告');
        }
      });
    }

    /**
     * navExchange 角色功能的添加与删除
     * @param  {object} parentItem  [父菜单项]
     * @param  {object} item        [子菜单项]
     * @param  {number} parentIndex [父菜单项索引]
     * @param  {number} index       [子菜单项索引]
     * @param  {string} type        [操作类型：添加add；删除remove]
     * @return {undefined}
     */
    function navExchange(parentItem, item, parentIndex, index, type) {
      var new_parentItem;
      var dataList = type === 'add' ? vm.hadList : vm.notHaveList;
      var otherList = type === 'add' ? vm.notHaveList : vm.hadList;

      // 添加到已有功能列表
      dataList.some(function(hadItem, key) {
        if (hadItem.funCode === parentItem.funCode) {
          hadItem.child.push(item);

          return true;
        } else {
          if (key === dataList.length - 1) {
            new_parentItem = angular.copy(parentItem);
            new_parentItem.child = [item];
            dataList.push(new_parentItem);
          }
        }
      });

      // 功能列表为空时，创建第一个元素
      if (dataList.length === 0) {
        new_parentItem = angular.copy(parentItem);
        new_parentItem.child = [item];
        dataList.push(new_parentItem);
      }

      // 从未有功能列表中删除
      parentItem.child.splice(index, 1);

      if (parentItem.child.length === 0) {
        otherList.splice(parentIndex, 1);
      }
    }

    function refreshData() {
      security.getUserRoleList().then(function(response) {
        vm.roleData = response.data.data;
      });
    }
  }

})();
