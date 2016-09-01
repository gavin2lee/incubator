(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('taskUploadService', taskUploadService);

    function taskUploadService($http, apiHost) {
      var service = {},
          reqs = apiHost.qualityManager.result;

      service.setResult = setResult;
      return service;
      function setResult(data) {
        var req = reqs.setter;
        $http[req.method](req.url, data);
      }

    }

})();
