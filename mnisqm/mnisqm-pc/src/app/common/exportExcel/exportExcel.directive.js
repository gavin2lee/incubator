(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('exportExcel', exportFunc);

  /** @ngInject */
  /**
   * 导出Excel
   * @return {object}
   */
  function exportFunc($http) {
    return {
      restrict: 'E',
      scope: {},
      controller: 'ExportExcelController',
      controllerAs: 'vm',
      replace: true,
      bindToController: {
        exslSrc: '=',
        classList: '='
      },
      templateUrl: 'app/common/exportExcel/exportExcel.tpl.html'
    };
  }
})();
