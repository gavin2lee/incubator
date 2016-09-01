(function(){
    'use strict';

    angular.module('nqms').controller('MalCaseUpElementController', function ($uibModal, $log) {
        var vm = this;
        vm.lst = [{elementCode:'1',elementName:'压疮大小',viewName : '压疮大小',groupName : '压疮信息',
            elementType:'文本框',width:'3/12',remark:'备注',required:'是'},
            {elementCode:'2',elementName:'发生时间',viewName : '发生时间',groupName : '压疮信息',
                elementType:'文本框',width:'3/12',remark:'备注',required:'是'},
            {elementCode:'3',elementName:'性别',viewName : '性别',groupName : '患者信息',
                elementType:'单选框',width:'3/12',remark:'备注',required:'是'},
            {elementCode:'4',elementName:'医疗类型',viewName : '医疗类型',groupName : '患者信息',
                elementType:'下拉框',width:'3/12',remark:'备注',required:'是'},
            {elementCode:'5',elementName:'年龄',viewName : '年龄',groupName : '患者信息',
                elementType:'文本框',width:'3/12',remark:'备注',required:'是'}];
    })
})();