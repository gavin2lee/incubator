(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('colorPicker', colorPicker);

  /** @ngInject */
  function colorPicker($timeout) {
    var directive = {
      restrict: 'E',
      require: 'ngModel',
      scope: {},
      replace: true,
      templateUrl: 'app/components/colorPicker/colorPicker.html',
      link: function(scope, element, attrs, ngModel) {
        $timeout(function() {
          scope.current = ngModel.$viewValue;
        })
        scope.colorArr = ['white', '#CC6666', '#FFFF66', '#00CCff', '#00FF66', '#CC99FF', '#CC9933', '#CCCCFF'];
        scope.choose = function(color) {
          ngModel.$setViewValue(color);
          element.find('.color-picker-selector').hide();
          scope.current = color;
        }
        scope.toggle = function() {
          if(!attrs.handle) return;
          element.find('.color-picker-selector').toggle();
        }
      }
    };

    return directive;
  }

})();
