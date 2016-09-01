(function (){
    'use strict';

    angular
        .module('nqms')
        .controller('ModalController',ModalControllerFunc);

    function ModalControllerFunc($rootScope, $scope, $uibModalInstance, ok, data, methodsObj, initFn){
      var vm = this;

      vm.rootScope = $rootScope;
      vm.scope = $scope;

      // 预设的表单数据
      vm.formData = data && data.formData;
      // 预设的原始数据
      vm.metaData = data && data.metaData;

      // 在弹层中调用的事件栈
      if (angular.isObject(methodsObj)) {
        for (var prop in methodsObj) {
          // if (typeof methodsObj[prop] === 'function') {
            vm[prop] = methodsObj[prop];
          // }
        }
      } else {
        if (methodsObj) {
          $log.error('methodsObj必须是对象!');
        }
      }

      initFn && initFn.bind(vm)(vm);

      vm.ok = function(arg) {
        var _promise = ok.call(vm ,vm.formData, arg);

        if (typeof _promise.then === 'function' ) {
          _promise.then(function (response) {
            debugger;
            if (response.data.code === '0') {
              $uibModalInstance.dismiss('cancel');
            }
          })
        }
      };

      vm.cancel = function() {
        $uibModalInstance.dismiss('cancel');
      };
    }
})();
