(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('gridCheckAll', gridCheckAllFunc);

  /** @ngInject */
  /**
   * 表格全选
   * @return {object}
   */
  function gridCheckAllFunc() {
    return {
      restrict: 'E',
      template: '<input type="checkbox" ng-model="isAllSelected" />',
      scope: {
        all: '='
      },
      link: function (scope, element) {
        element.parent().addClass('checkbox-td print-remove');

        scope.$watch('isAllSelected', function () {
          if (typeof scope.all === 'undefined') {
            return;
          }
          scope.all.forEach(function (val) {
            val.isSelected = scope.isAllSelected;
          })
        });

        scope.$watch('all', function (newVal, oldVal) {
          if (oldVal) {
            oldVal.forEach(function (val) {
              val.isSelected = false;
            });
          }

          scope.isAllSelected = false;
        });
      }
    }
  }
})();
