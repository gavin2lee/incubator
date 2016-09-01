(function() {
  'use strict';
  angular
    .module('nqms')
    .factory('malCaseService', malCaseService);

  function malCaseService(apiHost, $http) {
    var service = {
      apiHost: apiHost,
      getEventType: getEventType,
      deleteType: deleteType
    };

    return service;
    //查询类型数据
    function getEventType() {
      return $http.get(apiHost.events.getEventTypeList)
        .then(complete);
    }

    function deleteType(seqId) {
      return $http.post(apiHost.events.deleteEventType, {
        seqId: seqId
      }).then(complete);
    }

    //执行成功
    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }
  }


})();
