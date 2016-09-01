/**
 * Created by gary on 16/8/25.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('BDErrorRateController', bdErrorRateController);

  function bdErrorRateController($rootScope, $scope, $q, $state, $stateParams, $echarts, security, moment, chartsService, modalService, deptService) {
    var vm = this;
    var deptCache = deptService.getCacheDeptList();
    var defer = $q.defer();
    var mockData = [{
      value: _.random(10),
      name: '22'
    }, {
      value: _.random(10),
      name: '2'
    }, {
      value: _.random(10),
      name: '224'
    }, {
      value: _.random(10),
      name: '212'
    }, {
      value: _.random(10),
      name: '252'
    }];

    if (!deptCache) {
      defer.promise = deptService.getDeptData();
    } else {
      defer.resolve(deptCache);
    }

    // 表格 数据
    vm.table_1_list = [{
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }, {
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }, {
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }, {
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }, {
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }, {
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }, {
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }, {
      name: '蓝色',
      age: '事故',
      sex: '31',
      job: '31%',
      addr: '广东'
    }]

    vm.stastics = {
      err: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:3',
        config: chartsService.pieInit('')
      },
      dept: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:1',
        config: {
          color: ['#3398DB'],
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [{
            type: 'category',
            data: ['门诊科', '外科', '内科', '妇产科', '儿科', '五官科', '肝病二区', '门诊科', '外科', '内科', '妇产科', '儿科', '五官科', '肝病二区'],
            axisTick: {
              alignWithLabel: true
            }
          }],
          yAxis: [{
            type: 'value'
          }],
          series: [{
            name: '直接访问',
            type: 'bar',
            barWidth: '60%',
            data: [10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220]
          }]
        }
      },
      turns: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:1',
        config: {
          color: ['#3398DB'],
          tooltip: {
            trigger: 'axis',
            axisPointer: { // 坐标轴指示器，坐标轴触发有效
              type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: [{
            type: 'category',
            data: ['门诊科', '外科', '内科', '妇产科', '儿科', '五官科', '肝病二区', '门诊科', '外科', '内科', '妇产科', '儿科', '五官科', '肝病二区'],
            axisTick: {
              alignWithLabel: true
            }
          }],
          yAxis: [{
            type: 'value'
          }],
          series: [{
            name: '直接访问',
            type: 'bar',
            barWidth: '60%',
            data: [10, 52, 200, 334, 390, 330, 220, 10, 52, 200, 334, 390, 330, 220]
          }]
        }
      }
    }

    //  vm.stastics.err.config.legend.data = _.pluck(mockData, '');
    vm.stastics.err.config.legend.show = false;
    vm.stastics.err.config.series[0].data = mockData;

    defer.promise.then(function(res) {
      vm.deptArr = transformDeptData(res);
        debugger
    })

    function transformDeptData(data) {
      return data.belongedDepts.map(function (deptItem) {
        return deptItem.deptName;
      });
    }
  }
})();
