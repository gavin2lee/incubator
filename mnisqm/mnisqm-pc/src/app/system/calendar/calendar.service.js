(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('calendarService', calendarServiceFunc);

  /** @ngInject */
  function calendarServiceFunc($window, apiHost, $http) {
    var reqs = apiHost.system,
      service = {
        getDate: Getter('calendar'),
        updateDate: Setter('calendar', 'update'),
        createDate: createDate()
      };

    return service;

    function Getter(type) {
      return function(data) {
        var req = reqs[type].getter;
        return $http[req.method](req.url, {
          params: data
        });
      }
    }

    function createDate() {
      return function(data) {
        var req = reqs.calendar.setter;
        return $http.get(req.url, {
          params: data
        });
      }
    }

    function Setter(type, action) {
      return function(data) {
        var req = reqs[type][action] || reqs[type].setter;
        return $http[req.method](req.url, data);
      }
    }

  }
})();
