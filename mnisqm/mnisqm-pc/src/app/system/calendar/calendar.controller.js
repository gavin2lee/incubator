/**
 * Created by gary on 16/4/1.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('CalendarController', calendarControllerFunc);

  /** @ngInject */
  function calendarControllerFunc($rootScope, $scope, $log, $q, $uibModal, calendarService, modalService, moment) {
    var vm = this;

    vm.open = open;
    vm.openSetting = openSetting;
    vm.loadParams = {
      year: new Date().getFullYear(),
      month: new Date().getMonth() + 1
    };

    vm.nextMonth = nextMonth;
    vm.previousMonth = previousMonth;
    vm.resetCalendar = resetCalendar;
    vm.isEmpty = false;
    vm.currentDate = moment().format('YYYY-MM-DD');

    init();

    // 初始化查询当月日期
    function init() {
      calendarService.getDate(vm.loadParams).then(function(response) {
        if (response && response.data.code === '0') {
          var count = 0;

          if (response.data.data.length === 0) {
            vm.isEmpty = true;

            $rootScope.$broadcast('toast', {
              type: 'warning',
              content: '系统没有' + vm.loadParams.year + '年的日历，如需使用请创建。'
            });

            return;
          }

          vm.isEmpty = false;
          vm.dataList = transformCalendar(response.data.data);

          // 计算空格数
          for (var prop in vm.dataList) {
            if (vm.dataList.hasOwnProperty(prop)) {
              if (count === 0) {
                count = vm.dataList[prop].length;
                break;
              }
              ++count;
            }
          }
          vm.placeholderArr = new Array(7 - count);

          $log.info(vm.dataList);
        }
      });
    }

    function resetCalendar() {
      vm.loadParams = {
        year: new Date().getFullYear(),
        month: new Date().getMonth() + 1
      };

      init();
    }

    function nextMonth() {
      vm.loadParams.month++;

      if (vm.loadParams.month > 12) {
        vm.loadParams.month = 1;

          vm.loadParams.year ++;
      }

      init();
    }

    function previousMonth() {
      vm.loadParams.month--;

      if (vm.loadParams.month < 1) {
        vm.loadParams.month = 12;
        vm.loadParams.year --;
      }

      init();
    }

    function transformCalendar(data) {
      var i;
      var data_month_temp = {};
      // var data_week_temp = [];
      var week_text = [
        ['monday', '星期一'],
        ['tuesday', '星期二'],
        ['wednesday', '星期三'],
        ['thursday', '星期四'],
        ['friday', '星期五'],
        ['saturday', '星期六'],
        ['sunday', '星期天']
      ];
      // var month_cont = 0;
      var week_count = 0;
      var obj_temp;

      vm.weeks = week_text;

      if (!data || !Array.isArray(data)) {
        return;
      }

      // 分解数据
      for (i = 0; i < data.length; i += 1) {
        // month_cont = Number(data[i].month) - 1;
        week_count = Number(data[i].weekDay) - 1;

        obj_temp = mergeObj(data[i], {
          weekText: week_text[week_count][1],
          day: new Date(data[i].date).getUTCDate(),
          isHoliday: data[i].isHoliday ? parseInt(data[i].isHoliday) : 0,
          isWeekend: data[i].isWeekend ? parseInt(data[i].isWeekend) : 0
        });

        if (!data_month_temp['week_' + obj_temp.weeks]) {
          data_month_temp['week_' + obj_temp.weeks] = [];
        }

        data_month_temp['week_' + obj_temp.weeks].push(obj_temp);

        // if (!data_month_temp[month_cont]) {
        //   data_month_temp[month_cont] = {};
        // }
        //
        // if (!data_month_temp[month_cont]['week_' + obj_temp.weeks]) {
        //   data_month_temp[month_cont]['week_' + obj_temp.weeks] = [];
        // }
        //
        // data_month_temp[month_cont]['week_' + obj_temp.weeks].push(obj_temp);
      }

      return data_month_temp;
    }

    function mergeObj(obj1, obj2) {
      return angular.merge({}, obj1, obj2);
    }

    function openSetting (day) {
      modalService.open({
        templateUrl: 'app/system/calendar/calendarSetting.modal.html',
        size: 'sm',
        ok: saveSetting,
        data: {
          formData: angular.extend({}, day)
        }
      });
    }

    function open() {
      modalService.open({
        templateUrl: 'app/system/calendar/calendarCreate.modal.html',
        size: 'sm',
        ok: save
      });
    }

    function save(formData) {
      $log.info(formData)
      calendarService.createDate(formData).then(function(response) {
        if (response.data.code === '0') {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '创建成功'
          });

          modalService.close('success');
        }
      })
    }

    function saveSetting (formData) {
      delete formData.day;
      delete formData.weekText;
      formData.createTime = moment(formData.createTime).format('YYYY-MM-DD hh:mm:ss');

      calendarService.updateDate(formData).then(function(response) {
        if (response.data.code === '0') {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '修改成功'
          });
          init();

          modalService.close('success');
        }
      })
    }

  }
})();
