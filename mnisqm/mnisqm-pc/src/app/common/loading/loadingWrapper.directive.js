(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('loadingWrapper', loadingFunc);

  /** @ngInject */
  /**
   * [loadingFunc 全局加载动画]
   * @return {[object]} [指令对象]
   */
  function loadingFunc($timeout) {
    return {
      restrict: 'A',
      // templateUrl: 'app/common/loading/loadingWrapper.tpl.html',
      template: '<div ng-transclude></div><loading type="{{type}}"></loading>',
      scope: {
        type: '@loadingType',
        options: '=loadingOptions',
        init: '@loadingInit'
      },
      link: loadingLinkFunc,
      transclude: true
    };

    function loadingLinkFunc(scope, elem, attrs) {
      var options = scope.options;
      var rawFunc = scope.options.func;

      if (!elem.css('position') || elem.css('position') === 'static') {
        elem.css('position', 'relative');
      }

      options.func = function() {
        $timeout(function() {
          scope.$broadcast('loading', scope.type);
        });
        rawFunc().then(function() {
          $timeout(function() {
            scope.$broadcast('loaded', scope.type);
          });
        });
      }
      
      if (options.init) {
        options.func(options.params);
      }
    }
  }
})();
