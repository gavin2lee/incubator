(function() {
  'use strict';
  angular
    .module('nqms')
    .config(function(toastrConfig) {
      toastrConfig.tapToDismiss = true;
      toastrConfig.timeOut = 0;
    })
    .factory('typeService', typeServiceFun);

  function typeServiceFun(apiHost, $http) {
    var service = {
      apiHost: apiHost,
      getEventType: getEventType
    };

    return service;
    //查询类型数据
    function getEventType() {
      return $http.get(apiHost.events.getEventTypeList)
        .then(complete);
    }
    //执行成功
    function complete(response) {
      if (response.data.code === '0') {
        return response;
      }
    }
  }


})();
