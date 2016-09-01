(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('techFilesJCIService', techFilesJCIService);

  /** @ngInject */
  function techFilesJCIService($window, $http, apiHost) {
    var reqs = apiHost.techFiles,
      service = {
        getNurseList: getNurseList,
        updateNurseInfo: updateNurseInfo,
        getPosStatement: Getter('posStatement'),
        savePosStatement: Setter('posStatement'),
        deletePosStatement: Deleter('posStatement'),
        getPreJobTrain: Getter('preJobTrain'),
        savePreJobTrain: Setter('preJobTrain'),
        deletePreJobTrain: Deleter('preJobTrain'),
        getAssessment: Getter('assessment'),
        saveAssessment: Setter('assessment'),
        deleteAssessment: Deleter('assessment'),
        getConEducation: Getter('conEducation'),
        saveConEducation: Setter('conEducation'),
        deleteConEducation: Deleter('conEducation'),
        getCprAcls: Getter('cprAcls'),
        saveCprAcls: Setter('cprAcls'),
        deleteCprAcls: Deleter('cprAcls'),
        getEduRecord: Getter('eduRecord'),
        saveEduRecord: Setter('eduRecord'),
        deleteEduRecord: Deleter('eduRecord')
      };

    return service;

    function getNurseList(deptCode) {
      var req = reqs.getNurseList;
      return $http[req.method](req.url, {
        params: {
          deptCode: deptCode
        }
      });
    }

    function updateNurseInfo(data) {
      var req = reqs.updateNurseInfo;
      return $http[req.method](req.url, {
        comUser: data,
        dataType: 'comUser'
      });
    }

    function Getter(type) {
      return function(id) {
        var req = reqs[type].getter;
        return $http[req.method](req.url, {
          userCode: id
        });
      }
    }

    function Setter(type) {
      return function(data) {
        var req = reqs[type].setter;
        return $http[req.method](req.url, data);
      }
    }

    function Deleter(type) {
      return function(id) {
        var req = reqs[type].deleter;
        return $http[req.method](req.url, {
          params: {
            seqId: id
          }
        });
      }
    }

  }
})();
