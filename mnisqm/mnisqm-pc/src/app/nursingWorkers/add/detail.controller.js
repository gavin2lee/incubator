(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('AddDetailController', AddDetailController);

    function AddDetailController($rootScope, $scope, $state, $stateParams, $q, deptService, baseInfoService, modalService, hrAddService) {
      var vm = this;

      vm.setNurse = setNurse;
      vm.openConfirm = openConfirm;
      vm.scope = $scope;
      vm.treeOption = {
        allowDeselect: false
      };

      vm.load = {
        func: initData,
        init: true
      };

      function initData() {
        return baseInfoService.getUserList($stateParams.deptCode).then(function(data) {
          vm.userDataList = data;

          // 护士长置顶
          var user_header_index = null;
          var user_header = vm.userDataList.find(function (item, index) {
            if (item.isDeptNurseHeader == '1') {
              user_header_index = index;

              return true;
            }
          });

          if (user_header_index) {
            vm.userDataList.splice(user_header_index, 1);
            vm.userDataList.unshift(user_header);
          }
        });
      }

      // 设置为护士长
      function setNurse(formData) {
        hrAddService.setUserHeader(formData).then(function (response) {
          if (response) {
            $rootScope.$broadcast('toast', {
              type: 'success',
              content: '设置成功！'
            });

            modalService.close();
            vm.load.func();
          }
        })
      }

      function openConfirm(row) {
        modalService.open({
          templateUrl: 'app/common/confirm/confirm.tpl.html',
          size: 'sm',
          ok: setNurse,
          data: {
            metaData: {
              title: '确定设为科护长吗？'
            },
            formData: {
              deptCode: row.deptCode,
              deptNurseHeader: row.userCode
            }
          }
        })
      }
    }
})();
