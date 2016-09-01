(function (){
    'use strict';

    angular
        .module('nqms')
        .controller('MalCaseUpLevelController',levelControllerFun);

    function levelControllerFun($scope, $log, levelService, modalService, toastr){
        var vm = this;

        vm.save = save;
        vm.edit = edit;
        vm.del = del;
        vm.formData = {};
        vm.add = add;
        vm.load = {
          func: initData,
          init: true
        }

        // initData();

        // 获取分级数据
        function initData() {
          return levelService.get().then(function(response) {
            if (response) {
              vm.list = response.data.data.lst;
            }
          });
        }

        function edit(row) {
          // 处理流程分解
          vm.formData = angular.extend({}, row);
          vm.selectedLevel = row;

          if (row) {
            if (row.flow) {
              vm.formData.flowList = row.flow.split('|');
            }
          }
        }

        function add() {
          vm.formData = {};
        }

        /************
        ****编辑框****
        ************/
        vm.removeFlow = removeFlow;
        vm.addFlow = addFlow;

        function addFlow() {
          if (vm.flowText === '') {
            return;
          }

          vm.formData.flowList = vm.formData.flowList || [];

          vm.formData.flowList.push(vm.flowText);

          // 置空 ng-model里的值
          vm.flowText = '';
        }

        function removeFlow($index) {
          vm.formData.flowList.splice($index, 1);
        }

        function save() {
          // debugger;
          var data = angular.extend({}, vm.formData);

          data.flow = data.flowList ? data.flowList.join('|') : '';
          // return;
          levelService.save(data).then(function(response) {
            if (response && response.data.code === '0') {
              toastr.success('保存成功！', '提示');
              vm.load.func();
              vm.formData = {};
              modalService.close('success');
            }
          });
        }

        function del(seqId) {
          levelService.del(seqId).then(function (response) {
            if (response && response.data.code === '0') {
              toastr.success('已删除！', '提示');
              vm.load.func();
              //如果删除选中行，那么编辑数据清空
              if(seqId == vm.formData.seqId){
                  vm.formData = {};
              }
              modalService.close('success');
            }
          });
        }
    }
})();
