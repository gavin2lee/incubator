(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('timestampToString', timestampToString);

  /** @ngInject */
  function timestampToString($filter) {
    var directive = {
      restrict: 'A',
      require: 'ngModel',
      link: function(scope, element, attrs, ngModel) {
        ngModel.$formatters.shift();
        ngModel.$formatters.shift();
        ngModel.$parsers.shift();
      }
    };

    return directive;
  }

})();
