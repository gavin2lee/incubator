(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('paginationSt', paginationFunc);

  /** @ngInject */
  function paginationFunc() {
    var directive = {
      restrict: 'E',
      templateUrl: 'app/components/pagination/pagination.tpl.html',
      replace: true
    };

    return directive;
  }

})();
