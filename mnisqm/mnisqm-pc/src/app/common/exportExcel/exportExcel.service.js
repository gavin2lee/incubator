(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('exportExcelService', exportExcelService);

  /** @ngInject */
  function exportExcelService($http, $log, $q, toastr, security) {
    var service = {
      downloadExcel: downloadExcel
    };

    return service;

    /**
     * 导出excel表格
     * @param  {string} url  下载url
     * @param  {object} data 表格数据对象
     * @return {promise}      promise对象
     */
    function downloadExcel(url, data) {
      return $http({
        url: url,
        method: 'POST',
        data: data,
      })
      .then(complete);
    }

    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }
  }
})();
