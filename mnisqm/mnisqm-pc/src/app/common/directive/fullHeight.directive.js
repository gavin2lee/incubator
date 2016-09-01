(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('fullHeight', fullHeight);

  /** @ngInject */
  function fullHeight() {
    var directive = {
      restrict: 'A',
      link: function(scope, element, attrs) {
        var list_content = element.find('.list-content');

        element.css({
          'height': innerHeight - element.offset().top - 10
        });
        // 档案人员管理 科室人员列表
        if (list_content.length > 0) {
          element.find('.list-content').css({
            'height': innerHeight - element.offset().top - element.find('.list-content').position().top - 36
          });
        }
      }
    };

    return directive;
  }

})();
