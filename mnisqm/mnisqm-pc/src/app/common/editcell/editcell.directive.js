(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('editCellDir', editCellDir)
    .directive('editTextDir', editTextDir)
    .directive('editInputDir', editInputDir);
  /** @ngInject */
  function editCellDir($rootScope) {
    return {
      restrict: 'C',
      link: function (scope, elem, attrs) {
        elem.click(function() {
          elem.find('.edit-text-dir').hide();
          elem.find('.edit-input-dir').show();
          elem.find('.edit-input-dir').find('.select2-choices').click();
          $rootScope.$broadcast('hide', elem);
        });
        scope.$on('hide', function(e, target) {
          if(elem === target) return;
          elem.find('.edit-text-dir').show();
          elem.find('.edit-input-dir').hide();
        })
      }
    };
  }

  function editTextDir() {
    return {
      restrict: 'C',
      link: function (scope, elem, attrs) {
        
      }
    };
  }

  function editInputDir() {
    return {
      restrict: 'C',
      link: function (scope, elem, attrs) {
        
      }
    };
  }
})();
