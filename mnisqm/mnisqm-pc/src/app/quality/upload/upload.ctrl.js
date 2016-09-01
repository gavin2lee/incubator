(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityTaskUploadCtrl', QualityTaskUploadCtrl);

    function QualityTaskUploadCtrl($uibModal, comService, formService) {
      var vm = this;

      comService.getDeptHelper(true).then(function(data) {
        vm.dept = data;
      });

      formService.getForm().then(function(rsp) {
        vm.models = rsp.data.data;
        vm.getModelName = function(id) {
          var target = _.findWhere(vm.models, {
            seqId: +id
          });
          return target ? target.modelName : id;
        }
      });

      vm.list = [
        {
          "seqId": 36,
          "taskCode": "16082433648",
          "taskDate": "2016-08-01",
          "formCode": "159",
          "handUserCode": "000073",
          "joinUserName": null,
          "deptCode": "0111",
          "status": "01",
          "createTime": null,
          "updateTime": null,
          "createPerson": null,
          "updatePerson": null,
          "formName": null,
          "formType": null,
          "deptNames": null,
          "isDone": null,
          "planId": 33,
          "remark": "12313123"
        }
      ];

      vm.open = open;

      function open(data) {
        var modalInstance = $uibModal.open({
          templateUrl: 'app/quality/upload/upload.modal.html',
          controller: 'QualityTaskUploadModalCtrl',
          controllerAs: 'vm',
          backdrop: 'static',
          animation: true,
          size: 'lg',
          resolve: {
            id: function() {
              return angular.copy(data)
            }
          }
        });

        modalInstance.result.finally(function() {
        });
      }

    }

})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityTaskUploadModalCtrl', QualityTaskUploadModalCtrl);

    function QualityTaskUploadModalCtrl($uibModalInstance, taskUploadService, formService, id) {
      var vm = this, cache = {}, cates = [], subcates = [];
      vm.cancel = cancel;
      vm.ok = ok;
      vm.tableLayout = tableLayout;
      vm.task = id;

      formService.getForm({
        seqId: vm.task.formCode
      }).then(function(rsp) {
        vm.model = rsp.data.data[0];
        vm.types = _.compact(vm.model.qualityFormList);
        vm.data = handleList(vm.model.qualityFormDetailList);
        vm.lst = vm.data.raw;
        vm.dirtyLst = vm.data.dirty;
        vm.formData = {
          deptCode: vm.task.deptCode,
          taskCode: vm.task.seqId,
          modelId: vm.task.formCode,
          startDate: vm.task.taskDate
        };
        refreshTable();
      });

      function cancel() {
        $uibModalInstance.dismiss();
      }

      function ok() {
        var list = vm.lst.map(function(v, i) {
          return {
            detailId: v.seqId,
            points: v.points,
            stantard: v.stantard,
            count: v.count,
            cause: v.cause
          };
        });
        vm.formData.qualityResultDetailList = list;
        taskUploadService.setResult(vm.formData).then(function() {
          
        })
      }

      function handleList(data) {
        data = _.sortBy(data, function(v, i) {
          return v.name;
        });
        _.each(data, function(v, i) {
          var type = _.findWhere(vm.types, {
            formCode: v.formCode
          });
          v.cate = _.findWhere(vm.types, {
            formCode: type.parentType
          }).formName;
          v.subcate = type.formName;
        });
        cates = _.uniq(_.pluck(data, 'cate'));
        subcates = _.uniq(_.pluck(data, 'subcate'));
        var grouped = _.groupBy(data, function(v) {return v.cate});
        var raw = [];
        _.forEach(grouped, function(v, i) {
          var layed = _.groupBy(v, function(v) {
            return v.subcate;
          });
          var length = grouped[i].length;
          grouped[i] = layed;
          grouped[i].length = length;
        });
        _.forEach(cates, function(v1) {
          _.forEach(subcates, function(v2) {
            raw = raw.concat(grouped[v1][v2])
          });
        });
        raw = _.compact(raw);
        return {
          raw: raw,
          dirty: grouped
        };
      }
      function tableLayout(id, cate, subcate) {
        var result;
        subcate = subcate || '';
        if(cache[cate + subcate]) {
          result = cache[cate + subcate][id] || 0;
        }else if(subcate){
          result = vm.dirtyLst[cate][subcate].length;
          cache[cate + subcate] = {};
          cache[cate + subcate][id] = result;
        }else {
          result = vm.dirtyLst[cate].length;
          cache[cate] = {};
          cache[cate][id] = result;
        }
        return result;
      }

      function refreshTable() {
        var data = handleList(vm.lst);
        vm.totalScore = _.reduce(vm.lst, function(memo, v) {
          return memo + v.points;
        }, 0);
        vm.dirtyLst = data.dirty;
        vm.lst = data.raw;
        cache = {};
        window.cache = cache;
        window.dirtyLst = vm.dirtyList;
        window.lst = vm.lst;
        window.tableLayout = tableLayout;
      }
    }

})();