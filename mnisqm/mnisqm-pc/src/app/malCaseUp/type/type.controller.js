(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('MalCaseUpTypeController', typeControllerFun);

  function typeControllerFun($scope, $log, $filter,typeService) {
    var vm = this;
    //数据初始化
    // initData();
    vm.load = {
      func: initData,
      init: true
    }

    function initData(){
        return typeService.getEventType().then(function (response){
            if(response){
                vm.list = response.data.data.lst;
            }
        });
    }

  }
})();
