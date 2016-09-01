(function() {
  'use strict';

  angular
    .module('nqms')
    // 响应拦截器
    .factory('myHttpInterceptor', interceptorFunc);

  /** @ngInject */
  function interceptorFunc($rootScope, $log, $q) {
    return {
      // 请求成功
      request: function(config) {
        // $log.info('请求结果：' + config.url, config);
        // 'request': function(config) {
        config.timeout = 10000;
        return config || $q.when(config);
      },

      // 请求失败
      requestError: function(rejection) {
        $log.info('请求失败：' + rejection.statusText, rejection);
        $rootScope.$broadcast('toast', {
          type: 'error',
          content: '请检查网络连接或联系服务器管理员！',
          title: '请求错误'
        });
        return $q.reject(rejection);
      },

      // 响应成功
      response: function(response) {
        // $log.info('响应结果：' + response.config.url, response);
        if (response.data.code == -2) {
          // authfail
          $rootScope.$broadcast('logout');
        }
        if (response.data.code == -1) {
          $rootScope.$broadcast('toast', {
            type: 'error',
            content: response.data.msg
          });
        }
        if (response.data.code && response.data.code != 0) {
          return $q.reject('code error');
        }
        return response || $q.when(response);
      },

      // 响应失败
      responseError: function(rejection) {
        $log.info('响应失败：' + rejection.statusText, rejection);
        $rootScope.$broadcast('toast', {
          type: 'error',
          content: '错误码：' + rejection.status + '<br> URL：' + (rejection.config && rejection.config.url || '') + '\n ' + (rejection.message || '') + '<br> 请检查网络连接或联系服务器管理员！',
          title: '没有响应'
        });
        return $q.reject(rejection);
      }
    };
  }
})();
