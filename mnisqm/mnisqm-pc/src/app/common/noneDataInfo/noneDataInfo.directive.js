(function() {
  'use strict';

  angular
    .module('nqms')
    .directive('noneDataInfo', noneDataInfoFunc);

  /** @ngInject */
  /**
   * 用于数据为空时，显示提示
   * @return {object} 指令对象
   */
  function noneDataInfoFunc($http) {
    return {
      restrict: 'E',
      scope: {
        list: '='
      },
      replace: true,
      templateUrl: 'app/common/noneDataInfo/noneDataInfo.tpl.html',
      link: function (scope, elem) {
        scope.isLoading = function() {
          return $http.pendingRequests.length > 0;
        };

        scope.$watch(scope.isLoading, function(v) {
          if (!v && (!scope.list || scope.list.length === 0)) {
            elem[0].setAttribute('style', 'display: block');
          } else {
              elem[0].setAttribute('style', 'display: none');
          }
        });
      }
    };
  }
})();
