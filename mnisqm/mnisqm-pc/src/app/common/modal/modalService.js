// getUserSysRoles
(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('modalService', modalServiceFunc);

  function modalServiceFunc($uibModal, $uibModalStack) {
    return {
      open: function(options) {
        $uibModal.open({
          templateUrl: options.templateUrl,
          size: options.size,
          backdrop: 'static',
          appendTo: options.appendTo,
          animation: options.animation || true,
          controller: 'ModalController',
          controllerAs: 'vm',
          resolve: {
            initFn: function() {
              return options.initFn;
            },
            ok: function() {
              return options.ok;
            },
            data: function() {
              if (options.data) {
                return options.data;
              }
            },
            methodsObj: function () {
              if (options.methodsObj) {
                return options.methodsObj;
              }
            }
          }
        });
      },
      close: function(reason) {
        $uibModalStack.dismissAll(reason);
      }
    };
  }
})();
