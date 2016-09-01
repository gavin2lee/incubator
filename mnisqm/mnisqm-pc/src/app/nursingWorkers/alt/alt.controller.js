/**
 * Created by gary on 16/8/19.
 */
(function () {
  'use strict';

  angular
    .module('nqms')
    .controller('AltCtrl', AltCtrl)

  /** @ngInject */

  function AltCtrl($rootScope, $scope, $state, deptService, baseInfoService) {
    var vm = this;
    var currentNode;

    vm.select = select;
    vm.scope = $scope;
    vm.treeOption = {
      allowDeselect: false
    };

    init();

    function init() {
      var deptCache = deptService.getCacheDeptList();

      deptCache ? transformTree(deptCache) :
      deptService.getDeptData().then(function (data) {
        transformTree(data);
      });
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
      if (node.userCode) {
        $state.go('home.alt.detail', {
          userCode: node.userCode,
          deptCode: node.deptCode,
          deptName: node.deptN
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
      result.children = _.compact([].concat(data.user, data.belongedDepts));

      // 给每个用户加上deptCode
      if (Array.isArray(data.user)) {
        data.user.forEach(function(item) {
          item.deptCode = data.deptCode;
          item.deptN = data.deptName;
        })
      }

      return result;
    }
  }

})();
