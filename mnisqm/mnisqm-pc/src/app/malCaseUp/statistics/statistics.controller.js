(function (){
    'use strict';

    angular
        .module('nqms')
        .controller('MalCaseUpStatisticsController',statisticsControllerFun);

    function statisticsControllerFun($scope,$log,NgTableParams,$filter){
        var vm = this;
        vm.lst = [{groupNo:'跌倒',groupName:'不良治疗',bedNo : 'Ⅱ级',remark : '跌倒'},
            {groupNo:'坠床',groupName:'不良治疗',bedNo : 'Ⅱ级',remark : '坠床'},
            {groupNo:'走失',groupName:'不良治疗',bedNo : 'Ⅰ级',remark : '走失'},
            {groupNo:'烫伤',groupName:'不良治疗',bedNo : 'Ⅲ级',remark : '烫伤'},
            {groupNo:'医患争吵',groupName:'医患沟通',bedNo : 'Ⅰ级',remark : '医患争吵'},
            {groupNo:'身体攻击',groupName:'医患沟通',bedNo : 'Ⅳ级',remark : '身体攻击'},
            {groupNo:'咽入异物',groupName:'饮食类',bedNo : 'Ⅱ级',remark : '咽入异物'},
            {groupNo:'食物中毒',groupName:'饮食类',bedNo : 'Ⅳ级',remark : '食物中毒'},
            {groupNo:'仪器故障',groupName:'医疗设备',bedNo : '0级',remark : '仪器故障'},
            {groupNo:'院内压疮',groupName:'皮肤类',bedNo : 'Ⅲ级',remark : '院内压疮'}];

        $log.debug(vm.lst);
        vm.tableParams = new NgTableParams({
            page: 1,            // show first page
            count: 10           // count per page
        }, {
            total: vm.lst.length, // length of data
            getData: function($defer, params) {
                var orderedData = params.sorting() ?
                    $filter('orderBy')(vm.lst, params.orderBy()) :
                    vm.lst;
                orderedData = params.filter() ?
                    $filter('filter')(orderedData, params.filter()) :
                    orderedData;

                params.total(orderedData.length); // set total for recalc pagination
                $defer.resolve($scope.users = orderedData.slice((params.page() - 1) * params.count(), params.page() * params.count()));
            }
        });
    }
})();