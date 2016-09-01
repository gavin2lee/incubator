(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('formService', formServiceFunc);

  /** @ngInject */
  function formServiceFunc($http, apiHost) {
    var service = {
      getForm: getForm,
      saveForm: saveForm,
      deleteForm: deleteForm,
      delOption: delOption,
      copyModel: copyModel
    }, reqs = apiHost.qualityManager.model;

    return service;

    function deleteForm(id) {
      var req = reqs.deleter;
      return $http[req.method](req.url, {
          params: {
            seqId: id
          }
        });
    }

    function saveForm(dataObj) {
      var req = reqs.setter;
      return $http[req.method](req.url, dataObj);
    }

    function getForm(param) {
      var req = reqs.getter;
      return $http[req.method](req.url, {
        params: param
      });
    }

    function delOption(id) {
      var req = reqs.delOption;
      return $http[req.method](req.url, {
        params: {
          seqId: id
        }
      });
    }

    function copyModel(id) {
      var req = reqs.copyModel;
      return $http[req.method](req.url, {
        params: {
          modelId: id
        }
      })
    }

  }
})();
