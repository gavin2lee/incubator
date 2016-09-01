(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('ExportExcelController', exportExcelController);

  /** @ngInject */
  function exportExcelController($rootScope, $http, exportExcelService) {
    var vm = this;
    var data;

    vm.doExport = doExport;

    /**
     * 从DOM表格中获取表格数据
     * @return {object} 表格数据对象
     */
    function getDataFromDom() {
      var ths = document.querySelectorAll('.table[st-table] .header-export th');
      var tds = document.querySelectorAll('.table[st-table] tbody td');
      var headerArr;
      var bodyArr;

      headerArr = [].map.call(ths, function(th) {
        return th.textContent;
      });

      bodyArr = [].map.call(tds, function(td) {
        return td.textContent;
      });

      return {
        header: headerArr,
        body: bodyArr
      }
    }

    function doExport(url) {
      var data = getDataFromDom();

      exportExcelService.downloadExcel(url, data).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '导出成功！'
          })
        }
      });
    }
  }
})();
