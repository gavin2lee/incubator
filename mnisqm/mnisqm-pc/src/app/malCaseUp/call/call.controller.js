(function() {
  'use strict';

  angular
    .module('nqms')
    .filter('unique', function() {
      return function(arr, field) {
        if (typeof arr === 'undefined') {
          return;
        }
        var o = {},
          i, l = arr.length,
          r = [];
        for (i = 0; i < l; i += 1) {
          o[arr[i][field]] = arr[i];
        }
        for (i in o) {
          r.push(o[i]);
        }
        return r;
      };
    })
    .controller('MalCaseUpCallController', callControllerFun);
  /** @ngInject */
  function callControllerFun($rootScope, $scope, $q, $state, security, moment, levelService, modalService, toastr, fixService, callService, deptService, malCaseService) {
    var vm = this;
    var formData = {};
    var metaData = {};

    vm.state = $state;
    vm.openConfirm = openConfirm;
    vm.loadDetail = loadDetail;
    vm.setCurrent = setCurrent;
    vm.save = save;
    vm.open = open;
    vm.del = del;
    vm.add = add;
    vm.load = {
      func: initData,
      init: true
    }

    // mock数据
    metaData.eventTypeList = [{
      code: 1,
      name: '运送中病情变化'
    }, {
      code: 2,
      name: '误吸'
    }, {
      code: 3,
      name: '院内压疮'
    }, {
      code: 4,
      name: '坠床'
    }, {
      code: 5,
      name: '跌倒'
    }];

    initData();

    // 获取不良事件数据
    function initData() {

      var q1 = fixService.getEventList().then(function(response) {
        if (response) {
          vm.list = response.data.data.lst.map(function(item) {
            item.eventTime = new Date(item.eventTime).getTime();

            return item;
          });
        }
      });
      var q2 = malCaseService.getEventType().then(function(response) {
        if (response) {
          metaData.eventTypeList = response.data.data.lst;
        }
      });
      return $q.all([q1, q2]);
    }

    // TODO 编辑时日期格式处理  －>日期和时间已分开
    function open(row) {
      // 处理流程分解
      formData = angular.extend({}, row);
      vm.selectedLevel = row;

      metaData.deptList = deptService.deptList && deptService.deptList.belongedDepts;

      // 设置弹层的科室数据
      // TODO 对科室数据请求缓存
      initLevelData().then(function() {
        if (!metaData.deptList) {
          deptService.getDeptData().then(function(response) {
            var response_data = response;

            if (response_data) {
              metaData.deptList = response_data.belongedDepts;

              if (metaData.deptList.length === 0) {
                delete response_data.belongedDepts;
                metaData.deptList = [response_data];
              }

              open_it();
            }
          });
        } else {
          open_it();
        }
      });

      // debugger;
      // 编辑时把时间转换成原始格式
      if (row) {
        formData.eventDate = new Date(row.eventTime);
        formData.eventTime = new Date(row.eventTime);
        // 事件等级
        formData.level = {
          damageLevel: row.damageLevel
        };
        // 科室转换
        formData.dept = {
          deptCode: row.deptCode,
          deptName: row.deptName
        };

        // 事件类型转换
        formData.eventType = {
          eventTypeCode: row.eventTypeCode,
          eventTypeName: row.eventTypeName
        };
      }


      // 设置上报人为当前用户
      formData.userCode = security.getUserinfo().user.userCode;
      formData.userCode = security.getUserinfo().user.userName;

      function open_it() {
        modalService.open({
          templateUrl: 'app/malCaseUp/call/call.modal.html',
          size: 'lg',
          ok: save,
          data: {
            formData: formData,
            metaData: metaData
          },
          methodsObj: {
            eventTimeCalender: function() {
              var m_this = this;

              m_this.eventTimeOpen = true;
            },

            setUser: function($index) {
              var m_this = this;

              m_this.formData.flowList.splice($index, 1);
            }
          }
        });
      }
    }

    function initLevelData() {
      return levelService.get().then(function(response) {
        if (response) {
          metaData.levelList = response.data.data.lst;
        }
      });
    }

    function setCurrent(row) {
      vm.currentRow = row;
    }

    function loadDetail(row, type) {
      modalService.open({
        templateUrl: (function () {
          if (type === 'caseInfo') {
            return 'app/malCaseUp/call/caseInfo.modal.html'
          }

          return 'app/malCaseUp/call/surveyInfo.modal.html'
        })(),
        size: 'lg',
        data: {
          metaData: {
            row: row
          }
        },
        initFn: (function() {
          if (type === 'caseInfo') {
            return function() {
              var _modal = this;

              getReport(row).then(function(response) {
                if (response) {
                  _modal.metaData.malCaseInfo = response.data.data;
                }
              })
            }
          } else {
            return function() {
              var _modal = this;

              getReportDetail(row).then(function(response) {
                if (response) {
                  response.data.data.lst.forEach(function(item) {
                    if (!_modal.reportDetailList) {
                      _modal.reportDetailList = {};
                    }
                    _modal.reportDetailList[item.itemCode] = {
                      value: item.itemValue,
                      seqId: item.seqId
                    };
                  });
                }
              })
            }
          }


        })()
      });
    }

    function getReport(row) {
      return callService.getDetail(row.reportCode);
    }

    function getReportDetail(row) {
      return fixService.getReportDetail({
        reportCode: row.reportCode
      })
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

    function save(dataObj) {
      // debugger;
      var data = angular.extend({}, dataObj);
      var date_temp;
      var time_temp;
      // 科室
      data.deptName = data.dept.deptName;
      // 事件发生时间
      date_temp = moment(data.eventDate).format('YYYY-MM-DD');
      time_temp = moment(data.eventTime).format('HH:mm:ss');
      delete data.eventDate;
      delete data.dept;

      // 事件类型名称和code
      data.eventTypeCode = angular.fromJson(data.eventType).eventTypeCode;
      data.eventTypeName = angular.fromJson(data.eventType).eventTypeName;
      delete data.eventType;
      delete data.level;

      data.eventTime = moment(date_temp + ' ' + time_temp).format('YYYY-MM-DD HH:mm:ss');
      // debugger;
      // return;
      callService.save(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          vm.load.func();
          vm.formData = {};
          modalService.close('success');
        }
      });
    }

    function del(row) {
      callService.del(row.seqId, row.reportCode).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          vm.load.func();
          modalService.close('success');
        }
      });
    }

    // 上报
    function doReport(row) {
      var option = this.metaData.option;

      callService.doReport(row.reportCode, vm.opType).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
              type: 'success',
              content: '事件已成功' + option.title + '！'
            })
            // debugger
          modalService.close();
          vm.load.func();
        }
      })
    }

    function openConfirm(row, option) {
      vm.opType = option.value;

      modalService.open({
        templateUrl: 'app/common/confirm/confirm.tpl.html',
        size: 'sm',
        ok: option.type === 'report' ? doReport : del,
        data: {
          metaData: {
            title: (function() {
              if (option.type === 'report') {
                return '确定' + (option.title || '处理') + '该事件吗？';
              } else {
                return '确定删除这条调查表吗？';
              }
            })(),
            option: option
          },
          formData: row
        }
      })
    }
  }
})();
