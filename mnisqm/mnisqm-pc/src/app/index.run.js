(function() {
  'use strict';

  angular
    .module('nqms')
    .run(runBlock);

  /** @ngInject */
  function runBlock($rootScope, $state, toastr, security, dicService) {
    $rootScope.$on('toast', function(event, info) {
      info = info || {};
      toastr[info.type || 'success'](info.content || '操作成功', info.title || '');
    });

    $rootScope.$on('logout', function(event) {
      security.clearUserinfo();
      security.clearCacheUser();
      $state.go('login');
    });

    $rootScope.$on('init', function(event) {
      // return;
      dicService.getSysDic().then(function(rsp) {
        $rootScope.dicInfo = rsp.data.data.dicInfo;
        $rootScope.getDicName = _.memoize(function(type, code) {
          var result;
          _.some($rootScope.dicInfo[type], function(item) {
            if (item.dicCode == code) {
              result = item.dicName;
              return true;
            }
          });
          return result || code;
        }, function(type, code) {
          return type + code;
        });
        $rootScope.getDicCode = _.memoize(function(type, name) {
          var result;
          _.some($rootScope.dicInfo[type], function(item) {
            if (item.dicName == name) {
              result = item.dicCode;
              return true;
            }
          });
          return result || code;
        }, function(type, code) {
          return type + code;
        });
      });
    });
  }

})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .run(runBlock);

  /** @ngInject */
  function runBlock($window, $state, $rootScope, security) {
    $rootScope.patientSatTypes = ['住院安全与环境', '护理技术', '尊重病人', '情感支持', '健康教育'];
    $rootScope.nurseSatTypes = ['医院与工作环境'];
    var userinfo = security.getCacheUser();
    if (userinfo) {
      security.setUserinfo(userinfo);
      $rootScope.$broadcast('init');
    } else {
      $state.go('login');
    }
  }

})();
