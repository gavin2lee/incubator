(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('hrSearchService', hrSearchService);

    function hrSearchService($http, apiHost) {
      var service = {},
        reqs = apiHost.files;
      service.getUserList = getUserList;
      return service;

      function getUserList(params) {
        return $http.get(reqs.getUserList, {
          params: params
        });
      }
    }
})();
