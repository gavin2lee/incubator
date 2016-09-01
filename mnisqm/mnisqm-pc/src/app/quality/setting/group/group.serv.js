(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('qualityGroupService', qualityGroupService);

  /** @ngInject */
  function qualityGroupService($http, apiHost) {
    var reqs = apiHost.qualityManager.group,
    service = {
      getGroup: getGroup,
      saveGroup: saveGroup,
      deleteGroup: deleteGroup
    };

    return service;

    function deleteGroup(id) {
      var req = reqs.deleter;
      return $http[req.method](req.url, {
          params: {
            teamCode: id
          }
        });
    }

    function saveGroup(data) {
      var req = reqs.setter;
      return $http[req.method](req.url, data);
    }

    function getGroup() {
      var req = reqs.getter;
      return $http[req.method](req.url);
    }
  }
})();
