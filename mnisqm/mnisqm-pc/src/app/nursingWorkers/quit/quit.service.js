(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('hrQuitService', hrQuitService);

    function hrQuitService($http, $rootScope, apiHost) {
      var service = {},
        reqs = apiHost.files;
      service.getUserList = getUserList;
      service.quit = quit;
      return service;

      function getUserList(params) {
        return $http.get(reqs.getUserList, {
          params: params
        });
      }

      function quit(id) {
        var req = apiHost.user.quit;
        return $http[req.method](req.url, {
          userType: $rootScope.getDicCode('nature', '离职'),
          userCode: id
        });
      }
    }
})();
