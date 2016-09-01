(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('gridExpand', gridExpandFunc)
    .directive('gridExpandTrigger', gridExpandTriggerFunc);

  /** @ngInject */
  /**
   * 表格展开更多信息
   * @return {object}
   */
  function gridExpandFunc() {
    return {
      restrict: 'A',
      templateUrl: function(element, attrs) {
        return attrs.tplSrc || ''
      },
      link: function(scope, element) {
        element.addClass('tr-expand');
      }
    }
  }

  /** @ngInject */
  /**
   * 展开表格开关
   * @return {object} 指令对象
   */
  function gridExpandTriggerFunc() {
    return {
      restrict: 'A',
      template: '<i class="fa" ng-class="{\'fa-caret-right\': vm.expanedIndex !== $index, \'fa-caret-down\': vm.expanedIndex == $index}">',
      link: function(scope, element, attrs) {
        var td_parent = element.parent()[0];
        var method = attrs.gridExpandMethod;
        var ctrl = scope.$parent.vm;

        ctrl.expanded = false;
        element.addClass('td-expand print-remove');

        element.unbind().bind('click', function() {
          var tdLen = td_parent.querySelectorAll('td').length;

          if ((!ctrl.expanded || ctrl.expanedIndex !== scope.$index) && method && method !== '') {
            ctrl[method](scope.row[attrs.gridP]);
            ctrl.expanedIndex = scope.$index;
            ctrl.expanded = true;
          } else {
            ctrl.expanedIndex = null;
            ctrl.expanded = false;
          }

          scope.tdLen = tdLen;
        });
      }
    }
  }

})();
