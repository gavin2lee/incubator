(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DepartmentManagerEndemicAreaController', endemicAreaControllerFunc);

  /** @ngInject */
  function endemicAreaControllerFunc($scope, $q,areaService,toastr,bedService, security) {
      var vm = this, deptCode = security.getUserinfo().role.deptCode;
      vm.saveArea = saveArea;

      vm.load = {
        func: initData,
        init: true
      };

      $scope.$on('groupreload', function() {
        vm.load.func()
      });

      $scope.$on('deptchange', function(e, code) {
        deptCode = code;
        vm.load.func()
      });
      // initData();

      function initData(){
          //查询病区护理人员
          var q1 = areaService.getUserNurses({
            deptCode: deptCode
          }).then(function(response){
              if(response){
                  vm.list = response.data.data.lst;
              }
          });
          //查询科室所有分组
          var q2 = bedService.getGroupList({
            deptCode: deptCode
          }).then(function (response){
              if(response && response.data.code == 0){
                  vm.groupLst = response.data.data.lst;
              }
          });
          return $q.all([q1, q2]);
      }


      function saveArea(){
          areaService.batchSaveUserNurse(vm.list).then(function (response){
              if(response && response.data.code == 0){
                  toastr.success('保存成功！', '提示');
                  vm.load.func();
              }
          });
      }

  }

})();