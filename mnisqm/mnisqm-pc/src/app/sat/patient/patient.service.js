(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('satPatientService', satPatientService);

  /** @ngInject */
  function satPatientService($window, $http, apiHost) {
    var reqs = apiHost.satisfaction,
      service = {
        getSatTemp: Getter('satTemp'),
        saveSatTemp: Setter('satTemp'),
        updateSatTemp: Updater('satTemp'),
        deleteSatTemp: Deleter('satTemp'),
        getSatResult: Getter('satResult'),
        saveSatResult: Setter('satResult'),
        updateSatResult: Updater('satResult'),
        deleteSatResult: Deleter('satResult'),
        getSatTempByCode: getSatTempByCode
      };

    return service;

    function Getter(type) {
      return function(params) {
        var req = reqs[type].getter;
        return $http[req.method](req.url, {
          params: _.extend({
            userType: 2
          }, params)
        });
      }
    }

    function Setter(type) {
      return function(data) {
        var req = reqs[type].setter;
        return $http[req.method](req.url, data);
      }
    }

    function Deleter(type) {
      return function(id) {
        var req = reqs[type].deleter;
        return $http[req.method](req.url, {
          seqId: id
        });
      }
    }

    function Updater(type) {
      return function(data) {
        var req = reqs[type].updater;
        return $http[req.method](req.url, data);
      }
    }

    function getSatTempByCode(code) {
      var req = reqs.satTemp.single;
      return $http[req.method](req.url, {
        params: {
          formCode: code
        }
      });
    }

  }
})();
