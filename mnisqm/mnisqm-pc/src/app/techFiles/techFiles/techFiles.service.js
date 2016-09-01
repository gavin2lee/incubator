(function() {
  'use strict';

  angular
    .module('nqms')
    .factory('techFilesService', techFilesService);

  /** @ngInject */
  function techFilesService($window, $http, apiHost) {
    var reqs = apiHost.techFiles,
      service = {
        getNurseList: getNurseList,
        updateNurseInfo: updateNurseInfo,
        getNurseHirReg: Getter('nurseHirReg'),
        saveNurseHirReg: Setter('nurseHirReg'),
        deleteNurseHirReg: Deleter('nurseHirReg'),
        getNurseExp: Getter('nurseExp'),
        saveNurseExp: Setter('nurseExp'),
        deleteNurseExp: Deleter('nurseExp'),
        getAcademic: Getter('academic'),
        saveAcademic: Setter('academic'),
        deleteAcademic: Deleter('academic'),
        getAwardPunish: Getter('awardPunish'),
        saveAwardPunish: Setter('awardPunish'),
        deleteAwardPunish: Deleter('awardPunish'),
        getPerAssessment: Getter('perAssessment'),
        savePerAssessment: Setter('perAssessment'),
        deletePerAssessment: Deleter('perAssessment')
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
