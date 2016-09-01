
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DeptTreeCtrl', DeptTreeCtrl);

    function DeptTreeCtrl($scope, deptService) {
      var vm = this;
      vm.select = select;

      vm.treeOption = {
        allowDeselect: false
      };

      deptService.getDeptData().then(function(data) {
        vm.deptData = data;
        vm.treeData = [handleData(data)];
        vm.expandedNodes = [vm.treeData[0]];
        vm.selectedNode = vm.treeData[0];
        $scope.$watch('vm.filterText', function(v) {
          if (v) {
            vm.expandedNodes = [].concat(vm.treeData[0]).concat(vm.treeData[0].children);
          } else {
            vm.expandedNodes = [vm.treeData[0]];
          }
        })
      });

      function handleData(data) {
        var result = {};
        result.deptCode = data.deptCode;
        result.deptName = data.deptName;
        _.each(data.belongedDepts, function(v, i) {
          data.belongedDepts[i] = handleData(v);
        })

        // 给每个用户加上deptCode
        if (Array.isArray(data.user)) {
          data.user.forEach(function (item) {
            item.deptCode = data.deptCode;
            item.deptName = data.deptName;
          })
        }

        result.children = _.compact([].concat(data.belongedDepts));
        return result;
      }

      function select(node) {
        $scope.$broadcast('deptchange', node.deptCode);
      }
    }
})();