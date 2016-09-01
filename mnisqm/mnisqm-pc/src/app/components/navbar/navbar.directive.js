(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('acmeNavbar', acmeNavbar);

  /** @ngInject */
  function acmeNavbar() {
    var directive = {
      restrict: 'E',
      templateUrl: 'app/components/navbar/navbar.html',
      scope: {},
      replace: true,
      controller: NavbarController,
      controllerAs: 'vm',
      bindToController: true
    };

    return directive;

    /** @ngInject */
    function NavbarController($rootScope, $scope, $log, security) {
      var vm = this;
      var userInfo = security.getUserinfo();

      vm.minMode = false;
      vm.isSubNavShow = false;
      vm.isActive = $scope.$parent.isActive;
      vm.currentIndex = security.getUserinfo().currentIndex;
      vm.lastIndex = vm.currentIndex;
      vm.navData = userInfo.nav;
      // vm.navData = [{
      //   "child": [{
      //     "funName": "护理人员档案管理",
      //     "icoName": null,
      //     "funCode": "FUN0000022",
      //     "funUrl": "files"
      //   }, {
      //     "funName": "实习生护士管理",
      //     "icoName": null,
      //     "funCode": "FUN0000023",
      //     "funUrl": "internship"
      //   }, {
      //     "funName": "研究生护士管理",
      //     "icoName": null,
      //     "funCode": "FUN0000024",
      //     "funUrl": "graduate"
      //   }, {
      //     "funName": "专科护士管理",
      //     "icoName": null,
      //     "funCode": "FUN0000025",
      //     "funUrl": "technical"
      //   }, {
      //     "funName": "进修代陪护士管理",
      //     "icoName": null,
      //     "funCode": "FUN0000026",
      //     "funUrl": "ulteriorly"
      //   }, {
      //     "funName": "人员调动管理",
      //     "icoName": null,
      //     "funCode": "FUN0000027",
      //     "funUrl": "attemper"
      //   }, {
      //     "funName": "查询统计管理",
      //     "icoName": null,
      //     "funCode": "FUN0000028",
      //     "funUrl": "statistics"
      //   }],
      //   "funName": "护理人员管理",
      //   "icoName": "users",
      //   "funCode": "FUN0000021",
      //   "funUrl": "nursingWorkers"
      // }, {
      //   "child": [{
      //     "funName": "床位分组管理",
      //     "icoName": null,
      //     "funCode": "FUN0000042",
      //     "funUrl": "bed"
      //   }, {
      //     "funName": "病区护理人员管理",
      //     "icoName": null,
      //     "funCode": "FUN0000043",
      //     "funUrl": "endemicArea"
      //   }, {
      //     "funName": "班次管理管理",
      //     "icoName": null,
      //     "funCode": "FUN0000044",
      //     "funUrl": "classes"
      //   }, {
      //     "funName": "排班规则管理",
      //     "icoName": null,
      //     "funCode": "FUN0000045",
      //     "funUrl": "turnsRule"
      //   }, {
      //     "funName": "请假管理",
      //     "icoName": null,
      //     "funCode": "FUN0000046",
      //     "funUrl": "leave"
      //   }, {
      //     "funName": "排班管理",
      //     "icoName": null,
      //     "funCode": "FUN0000047",
      //     "funUrl": "turns"
      //   }, {
      //     "funName": "调班管理",
      //     "icoName": null,
      //     "funCode": "FUN0000048",
      //     "funUrl": "attemper"
      //   }, {
      //     "funName": "统计上报",
      //     "icoName": null,
      //     "funCode": "FUN0000049",
      //     "funUrl": "departmentStatistics"
      //   }],
      //   "funName": "科室排班管理",
      //   "icoName": "calendar-check-o",
      //   "funCode": "FUN0000041",
      //   "funUrl": "departmentManager"
      // }, {
      //   "child": [{
      //     "funName": "不良事件分级标准",
      //     "icoName": null,
      //     "funCode": "FUN0000067",
      //     "funUrl": "level"
      //   }, {
      //     "funName": "上报类型管理",
      //     "icoName": null,
      //     "funCode": "FUN0000062",
      //     "funUrl": "type"
      //   }, {
      //     "funName": "不良事件上报",
      //     "icoName": null,
      //     "funCode": "FUN0000063",
      //     "funUrl": "call"
      //   }, {
      //     "funName": "不良事件处理",
      //     "icoName": null,
      //     "funCode": "FUN0000064",
      //     "funUrl": "handle"
      //   }, {
      //     "funName": "不良事件整改",
      //     "icoName": null,
      //     "funCode": "FUN0000065",
      //     "funUrl": "handle.requirement"
      //   }, {
      //     "funName": "不良事件效果评估",
      //     "icoName": null,
      //     "funCode": "FUN0000068",
      //     "funUrl": "handle.estimate"
      //   }, {
      //     "funName": "统计报表",
      //     "icoName": null,
      //     "funCode": "FUN0000066",
      //     "funUrl": "statistics"
      //   }],
      //   "funName": "不良事件上报管理",
      //   "icoName": "ambulance",
      //   "funCode": "FUN0000061",
      //   "funUrl": "malCaseUp"
      // }, {
      //   "child": [{
      //     "funName": "护士长工作条目设置",
      //     "icoName": null,
      //     "funCode": "FUN0000082",
      //     "funUrl": "manualItem"
      //   }, {
      //     "funName": "护士长工作手册",
      //     "icoName": null,
      //     "funCode": "FUN0000083",
      //     "funUrl": "workManual"
      //   }],
      //   "funName": "护士长手册",
      //   "icoName": "ambulance",
      //   "funCode": "FUN0000081",
      //   "funUrl": "manual"
      // }, {
      //   "child": [{
      //     "funName": "科研项目管理",
      //     "icoName": null,
      //     "funCode": "FUN0000102",
      //     "funUrl": "researchItem"
      //   }, {
      //     "funName": "科研论文管理",
      //     "icoName": null,
      //     "funCode": "FUN0000103",
      //     "funUrl": "researchPapers"
      //   }],
      //   "funName": "科研管理",
      //   "icoName": "ambulance",
      //   "funCode": "FUN0000101",
      //   "funUrl": "research"
      // }, {
      //   "child": [{
      //     "funName": "质量检查表单管理",
      //     "icoName": null,
      //     "funCode": "FUN0000122",
      //     "funUrl": "qualityForm"
      //   }, {
      //     "funName": "任务管理",
      //     "icoName": null,
      //     "funCode": "FUN0000123",
      //     "funUrl": "qualityTask"
      //   }, {
      //     "funName": "质量检查",
      //     "icoName": null,
      //     "funCode": "FUN0000124",
      //     "funUrl": "qualityCheck"
      //   }, {
      //     "funName": "质量检查问题处理",
      //     "icoName": null,
      //     "funCode": "FUN0000125",
      //     "funUrl": "qualityDeal"
      //   }],
      //   "funName": "护理质控",
      //   "icoName": "ambulance",
      //   "funCode": "FUN0000121",
      //   "funUrl": "quality"
      // }, {
      //   "child": [{
      //     "funName": "角色管理",
      //     "icoName": null,
      //     "funCode": "FUN0000002",
      //     "funUrl": "roles"
      //   }, {
      //     "funName": "用户管理",
      //     "icoName": null,
      //     "funCode": "FUN0000003",
      //     "funUrl": "users"
      //   }, {
      //     "funName": "日期管理",
      //     "icoName": null,
      //     "funCode": "FUN0000004",
      //     "funUrl": "systemCalendar"
      //   }],
      //   "funName": "系统模块",
      //   "icoName": "cogs",
      //   "funCode": "FUN0000001",
      //   "funUrl": "system"
      // }];

      // 第一次进入系统, 一级菜单显示, 子菜单不显示.
      if (!vm.currentIndex) {
        vm.minMode = false;
        vm.isSubNavShow = false;
      }

      vm.toggleNavView = toggleNavView;
      vm.toggleSubNavView = toggleSubNavView;
      vm.showSubNav = showSubNav;
      vm.setActiveIndex = setActiveIndex;

      $rootScope.$broadcast('toggleNavView', vm.minMode);
      $rootScope.$broadcast('toggleSubNavView', vm.isSubNavShow);

      function toggleNavView() {
        vm.minMode = !vm.minMode;

        $rootScope.$broadcast('toggleNavView', vm.minMode);
      }

      function showSubNav(index) {
        vm.currentIndex = index;

        if (vm.navData[index].child && vm.navData[index].child.length > 0) {
          // vm.isSubNavShow = true;
          // $rootScope.$broadcast('toggleSubNavView', vm.isSubNavShow);
        }
      }

      function toggleSubNavView() {
        vm.isSubNavShow = !vm.isSubNavShow;
        $rootScope.$broadcast('toggleSubNavView', vm.isSubNavShow);
      }

      function setActiveIndex() {
        userInfo.currentIndex = vm.currentIndex;

        vm.lastIndex = vm.currentIndex;

        security.setCacheUser(userInfo);
      }

      // "vm.creation" is avaible by directive option "bindToController: true"
    }
  }

})();
