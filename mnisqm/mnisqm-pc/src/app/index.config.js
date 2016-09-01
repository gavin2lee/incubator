(function () {
  'use strict';

  angular
    .module('nqms')
    .config(config);

  /** @ngInject */
  function config($logProvider, toastrConfig, w5cValidatorProvider, $httpProvider, stConfig) {
    // Enable log
    $logProvider.debugEnabled(true);

    // Set options third-party lib
    toastrConfig.autoDismiss = true;
    toastrConfig.allowHtml = true;
    toastrConfig.timeOut = 3000;
    // toastrConfig.closeButton = true;
    toastrConfig.positionClass = 'toast-top-center';
    // toastrConfig.preventDuplicates = true;
    toastrConfig.progressBar = false;
    toastrConfig.preventOpenDuplicates = true;
    toastrConfig.iconClasses = {
      error: 'toast-error',
      info: 'toast-info',
      success: 'toast-success',
      warning: 'toast-warning'
    };

    // 验证 全局配置
    w5cValidatorProvider.config({
      blurTrig: true,
      showError: true,
      removeError: true
    });

    w5cValidatorProvider.setRules({
      calendarYear: {
        number: '必须输入数字'
      },
      leaningTime: {
        required: "必填项",
        number: "请输入数字"
      }
    });

    /**
     * 分页配置
     */
     stConfig.pagination.template = 'app/components/pagination/pagination.tpl.html';

    // 拦截器配置
    // Add the interceptor to the $httpProvider.
    $httpProvider.interceptors.push('myHttpInterceptor');
  }

})();
