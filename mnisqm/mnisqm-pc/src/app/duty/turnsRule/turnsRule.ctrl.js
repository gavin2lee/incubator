(function (){
    'use strict';

    angular
        .module('nqms')
        .controller('DepartmentManagerTurnsRuleController',TurnsRuleControlleFun);

    function TurnsRuleControlleFun(turnsRuleService, modalService, toastr,classesService){
        var vm = this;
        var formData = {};
        var metaData = {};

        vm.save = save;
        vm.add = add;
        vm.del = del;
        vm.edit = edit;
        vm.load = {
          func: initData,
          init: true
        }

        // initData();

        classesService.getClassList().then(function(response){
            if(response){
                vm.classList = response.data.data.lst;
                metaData.classList = vm.classList;
            }
        });

        // 获取不良事件数据
        function initData() {
            return turnsRuleService.getRuleList().then(function(response) {
                if (response) {
                    vm.list = response.data.data.lst;
                }
            });
        }
        //编辑
        function edit(row){
            formData = row;
            open();
        }
        //新增
        function add(){
            formData = {};
            open();
        }

        function open() {
            modalService.open({
                templateUrl: 'app/departmentManager/turnsRule/turnsRule.model.html',
                size: 'middle',
                ok: save,
                data: {
                    formData: formData,
                    metaData : metaData
                }
            });
        }


        function save() {
            turnsRuleService.saveRule(formData).then(function (response){
                if(response && response.data.code =='0'){
                    toastr.success('保存成功！','提示');
                    initData();
                    modalService.close('success');
                }
            });
        }

        function del(seqId) {
            turnsRuleService.deleteRule(seqId).then(function (response) {
                if (response && response.data.code == '0') {
                    toastr.success('已删除！', '提示');
                    initData();
                }
            });
        }
    }
})();
