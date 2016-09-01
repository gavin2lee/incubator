(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('commonPicker', commonPicker);

  /** @ngInject */
  function commonPicker($filter) {
    var dateFilter = $filter('date');
    var directive = {
      restrict: 'A',
      require: 'ngModel',
      link: function(scope, element, attrs, ngModel) {
        ngModel.$formatters.shift();
        ngModel.$formatters.shift();
        ngModel.$formatters.unshift(function(v) {
          var result;
          if (attrs.commonPicker == 'month') {
            result = dateFilter(v, 'yyyy-MM');
          } else {
            result = dateFilter(v, 'yyyy');
          }
          return result;
        });
        ngModel.$parsers.push(function(v) {
          var result;
          if (attrs.commonPicker == 'month') {
            result = dateFilter(v, 'yyyy-MM');
          } else {
            result = dateFilter(v, 'yyyy');
          }
          element.val(result);
          return v;
        })
      }
    };

    return directive;
  }

})();
