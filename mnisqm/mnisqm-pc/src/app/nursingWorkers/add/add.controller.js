(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('HrAddController', HrAddController);

    function HrAddController($rootScope, $scope, $state, $stateParams, $q, deptService, baseInfoService, modalService, hrAddService) {
      var vm = this;
      var currentNode;
      var deptCache = deptService.getCacheDeptList();

      vm.select = select;
      vm.scope = $scope;
      vm.treeOption = {
        allowDeselect: false
      };

      vm.load = {
        func: initData,
        init: true
      };

      vm.load.func();

      function initData() {
        var defferd = $q.defer();

        deptCache ? transformTree(deptCache) :
        defferd.resolve(deptService.getDeptData().then(function (data) {
          transformTree(data);
        }));

        return defferd.promise;
      }

      function transformTree(data) {
        vm.deptData = data;
        vm.treeData = [handleData(data)];
        vm.expandedNodes = [vm.treeData[0]];
        vm.scope.$watch('vm.filterText', function (v) {
          if (v) {
            vm.expandedNodes = [].concat(vm.treeData[0]).concat(vm.treeData[0].children);
          } else {
            vm.expandedNodes = [vm.treeData[0]];
          }
        })
      }

      function select(node) {
        if (currentNode === node) return;
        currentNode = node;
        $rootScope.$broadcast('loading', 'tree');
        if (node.deptCode) {
          $state.go('home.add.detail', {
            deptCode: node.deptCode
          });
        }
        sessionStorage.setItem('selectedNode', angular.toJson(node));
        $rootScope.$broadcast('loaded', 'tree');
        // console.log(node);
      }

      function handleData(data) {
        var result = {};
        result.deptCode = data.deptCode;
        result.deptName = data.deptName;
        _.each(data.belongedDepts, function (v, i) {
          data.belongedDepts[i] = handleData(v);
        })
        result.children = _.compact([].concat(data.belongedDepts));
        return result;
      }
    }
})();
