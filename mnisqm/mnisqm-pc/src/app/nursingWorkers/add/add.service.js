(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('hrAddService', hrAddService);

    function hrAddService($http, apiHost) {
      var service = {},
        reqs = apiHost.files;

      service.saveUser = saveUser;
      service.setUserHeader = setUserHeader;

      return service;

      function saveUser(data) {
        return $http.post(reqs.save, {
          comUser: data,
          dataType: 'comUser'
        }).then(complete);
      }

      function setUserHeader(formData) {
        return $http[apiHost.user.setUserHeader.method](apiHost.user.setUserHeader.url, formData).then(complete)
      }

      function complete(response) {
        if (response.data.code === '0') {
          return response;
        }
      }
    }
})();
