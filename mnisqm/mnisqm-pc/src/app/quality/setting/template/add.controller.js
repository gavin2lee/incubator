(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityAddFormController', QualityAddFormController);

    function QualityAddFormController($uibModal, $rootScope, $state, $stateParams, formService) {
      var vm = this, cache = {}, cates = [], subcates = [], id = $stateParams.id, fromId = $stateParams.from;
      vm.open = open;
      vm.save = save;
      vm.del = del;
      vm.tableLayout = tableLayout;
      vm.lst = [];
      vm.load = {
        func: load,
        init: true
      };

      function load() {
        if(!id) {
          return formService.copyModel(fromId).then(function(rsp) {
            vm.formData = rsp.data.data;
            id = vm.formData.seqId;
            vm.types = _.compact(vm.formData.qualityFormList);
            vm.data = handleList(vm.formData.qualityFormDetailList);
            vm.lst = vm.data.raw;
            vm.dirtyLst = vm.data.dirty;       
            refreshTable();
          });
        }else {
          return formService.getForm({
            seqId: id
          }).then(function(rsp) {
            vm.formData = rsp.data.data[0];
            vm.types = _.compact(vm.formData.qualityFormList);
            vm.data = handleList(vm.formData.qualityFormDetailList);
            vm.lst = vm.data.raw;
            vm.dirtyLst = vm.data.dirty;
            refreshTable();
          });
        }
      }

      function save() {
        vm.formData.qualityFormDetailList = vm.lst;
        formService.saveForm(_.pick(vm.formData, 'seqId', 'modelName', 'isUsed')).then(function(rsp) {
          vm.load.func();
        });
      }

      function del(id) {
        formService.delOption(id).then(function() {
          vm.load.func();
        })
      }

      function open(data, i) {
        var modalInstance = $uibModal.open({
          templateUrl: 'app/quality/setting/template/add.modal.html',
          controller: 'QualityAddFormModalCtrl',
          controllerAs: 'vm',
          backdrop: 'static',
          animation: true,
          size: 'md',
          resolve: {
            id: function() {
              return {
                data: angular.copy(data),
                id: vm.formData.seqId,
                types: angular.copy(vm.types)
              }
            }
          }
        });

        modalInstance.result.then(function(option) {
          vm.load.func();
        });
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

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityAddFormModalCtrl', QualityAddFormModalCtrl);

    function QualityAddFormModalCtrl($uibModalInstance, formService, id) {
      var vm = this;
      vm.model = {
        seqId: id.id,
        qualityFormList: [],
        qualityFormDetailList: []
      };
      vm.types = id.types || [];
      _.each(vm.types, function(v) {
        v.parentType || (v.parentType = '');
      });
      vm.parent = _.filter(vm.types, function(v) {
        return !v.parentType;
      });
      if(id.data) {
        vm.formData = id.data;
        vm.cateText = vm.formData.cate;
        vm.subCateText = vm.formData.subcate;
        var type = _.findWhere(vm.types, {
          formCode: vm.formData.formCode
        });
        vm.cParent = _.findWhere(vm.parent, {
          formName: vm.cateText
        });
        vm.son = _.filter(vm.types, function(v) {
          return v.parentType === vm.cParent.formCode;
        });
        vm.cSon = _.findWhere(vm.son, {
          formName: vm.subCateText
        });
      }else {
        vm.formData = {};
      }
      
      vm.cancel = cancel;
      vm.ok = ok;
      vm.pChange = pChange;
      vm.sChange = sChange;

      function cancel() {
        $uibModalInstance.dismiss();
      }

      function pChange() {
        vm.cParent = _.findWhere(vm.parent, {
          formName: vm.cateText
        });
        if(vm.cParent) {
          vm.son = _.filter(vm.types, function(v) {
            return v.parentType === vm.cParent.formCode;
          });
        }else {
          vm.son = [];
        }
        vm.cSon = null;
      }

      function sChange() {
        vm.cSon = _.findWhere(vm.son, {
          formName: vm.subCateText
        });
      }

      function ok() {
        if(!vm.cParent) {
          vm.model.qualityFormList.push({
            formName: vm.cateText
          });
          vm.model.qualityFormList.push({
            formName: vm.subCateText
          });
          vm.formData.formCode = null;
        }else if(!vm.cSon) {
          vm.model.qualityFormList.push({
            formName: vm.subCateText,
            parentType: vm.cParent.formCode
          });
          vm.formData.formCode = null;
        }else {
          vm.formData.formCode = vm.cSon.formCode;
        }
        
        vm.model.qualityFormDetailList.push(vm.formData);
        if(!vm.model.qualityFormList.length) {
          delete vm.model.qualityFormList;
        }
        formService.saveForm(vm.model).then(function() {
          $uibModalInstance.close();
        })
      }
    }

})();