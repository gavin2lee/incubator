(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('bDBusAnalyserService', bDBusAnalyserService);

    function bDBusAnalyserService($http, apiHost) {
      var service = {},
          reqs = apiHost.businessAnalyser;
      service.getBABrief = getBABrief;
      return service;
      function getBABrief() {
        var req = reqs.ba.getter;
        return $http[req.method](req.url);
      }
    }

})();