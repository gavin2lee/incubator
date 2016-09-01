(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('dicService', dicService);

  function dicService($http, apiHost) {
    var service = {
      getSysDic: getSysDic
    };
    return service;

    function getSysDic(type) {
      var req = apiHost.system.getDic;
      return $http[req.method](req.url, [{
        'dicType': type
      }]);
    }
  }
})();
