(function() {
  'use strict';
  angular
    .module('nqms')
    .controller('MalCaseUpEsimateController', esimateControllerFun);

  function esimateControllerFun($rootScope, $scope, $log, $q, $stateParams, security, levelService, modalService, toastr, fixService, deptService, handleService, filesService) {
    var vm = this;
    var formData = {};
    var metaData = {};

    vm.formData = formData;
    vm.metaData = metaData;
    vm.saveReportDetail = saveReportDetail;

    vm.rootScope = $rootScope;
    vm.scope = $scope;
    vm.open = open;
    vm.del = del;
    vm.load = {
      func: initData,
      init: true
    }

    // initData();

    // 获取分级数据
    function initData() {
      return initModalData($stateParams.reportCode);
    }

    function open_it() {
      // debugger;
      modalService.open({
        templateUrl: 'app/malCaseUp/handle/handle.modal.html',
        size: 'lg',
        appendTo: angular.element('.handle-wrapper'),
        data: {
          formData: formData,
          metaData: metaData
        },
        methodsObj: {
          saveReportDetail: saveReportDetail,
          // TODO 添加措施的方法
          saveListItem: function(listName, itemData) {
            var m_this = this;
            var list_name = m_this.upFirstLetter(listName);
            var data_temp = {};

            list_name = list_name === 'Estimate' ? 'Measure' : list_name;

            data_temp.userCode = security.getUserinfo().user.userCode;

            switch (listName) {
              case 'requirement':
                angular.extend(data_temp, {
                  reportCode: m_this.formData.reportCode,
                  seqId: m_this.formData[listName].seqId || null,
                  reqCode: m_this.formData[listName] && m_this.formData[listName].reqCode || null,
                  reqContent: m_this.formData[listName].reqContent,
                  remark: ''
                });
                break;
              case 'measure':
                if (!m_this.formData[listName].meaTime) {
                  data_temp.meaTime = moment().format('YYYY-MM-DD HH:mm:ss');
                }
                angular.extend(data_temp, {
                  seqId: m_this.formData[listName].seqId || null,
                  reportCode: m_this.formData.reportCode,
                  meaCode: m_this.formData[listName] && m_this.formData[listName].meaCode || null,
                  meaContent: m_this.formData[listName].meaContent,
                  headUserCode: m_this.formData[listName].headUserCode
                    // headUserName: m_this.formData[listName].headUserName
                });
                break;
              case 'estimate':
                angular.extend(data_temp, {
                  seqId: m_this.formData.measure.seqId || null,
                  reportCode: m_this.formData.reportCode,
                  meaCode: m_this.formData.measure && m_this.formData.measure.meaCode || null,
                  headUserCode: m_this.formData.measure.headUserCode,
                  meaContent: m_this.formData.measure.meaContent,
                  // headUserName: m_this.formData[listName].headUserName,
                  resStatus: '',
                  resDescribe: m_this.formData.measure.resDescribe
                });
                break;
              default:
                break;
            }

            if (!m_this.formData[listName + 'List']) {
              m_this.formData[listName + 'List'] = [];
            }

            // return;

            // 保存到服务器
            handleService['save' + list_name](data_temp).then(function(response) {
              if (response) {
                toastr.success('保存成功', '提示');
                // m_this.formData[listName].push(data_temp);
                m_this.rootScope.$broadcast('refresh' + list_name, m_this.formData.reportCode, m_this);
                m_this.formData[listName] = {};
              }
            });
          },
          removeListItem: function(params) {
            var m_this = this;
            var list_name = m_this.upFirstLetter(params.listName);
            var _params;

            m_this.formData[params.listName + 'List'].splice(params.index, 1);

            switch (params.listName) {
              case 'measure':
                _params = {
                  reportCode: m_this.reportCode,
                  meaCode: params.meaCode
                };
                break;
              case 'requirement':
                _params = {
                  seqId: params.seqId
                };
                break;
              case 'estimate':
                return;
              default:
                break;
            }

            handleService['delete' + list_name](_params).then(function(response) {
              if (response) {
                toastr.success('已删除', '提示');
              }
            });
          },
          fillItem: function(itemName, item) {
            var m_this = this;

            m_this.formData[itemName] = angular.extend({}, item);
          },
          resetForm: function(itemName) {
            this.formData[itemName] = {};
          },
          upFirstLetter: function(str) {
            return str.replace(/^\w{1}/, function(match) {
              return match.toUpperCase();
            });
          }
        }
      });
    }

    function initModalData(reportCode) {
      // var reportCode = row.reportCode;
      var deptCode = security.getUserinfo().role.deptCode;

      formData.reportCode = reportCode;

      // 获取当前选择科室的员工列表

      var getUserList = filesService.getUserList(deptCode);

      var getEventRequirement = handleService.getEventRequirement({
        reportCode: reportCode
      });
      var getEventMeasures = handleService.getEventMeasures({
        reportCode: reportCode
      });
      var getReportDetails = handleService.getReportDetail({
        reportCode: reportCode
      });
      // var getEventRequirement = handleService.getEventRequirement({reportCode: reportCode});
      var _promise = $q.all([getEventRequirement, getEventMeasures, getReportDetails, getUserList]).then(function(response) {
        var requirements = response[0].data.data.lst;
        var measures = response[1].data.data.lst;
        var reportDetails = {};
        var userList = response[3];

        // debugger

        response[2].data.data.lst.forEach(function(item) {
          reportDetails[item.itemCode] = {
            value: item.itemValue,
            seqId: item.seqId
          };
        });

        // 初始化弹层内的表单数据
        // metaData.malCaseInfo = row;
        vm.metaData.userList = userList;
        vm.formData.requirementList = requirements;
        vm.formData.measureList = measures;
        vm.formData.reportDetailList = reportDetails;
        // return response;
      });

      return _promise;
    }

    function removeFlow($index) {
      vm.formData.flowList.splice($index, 1);
    }

    function saveReportDetail() {
      var m_this = this;
      var data = angular.extend({}, m_this.formData.reportDetailList);
      var data_arr = [];
      var prop;
      debugger;
      // 将对象转成数组
      for (prop in data) {
        if (data.hasOwnProperty(prop)) {
          data_arr.push({
            reportCode: m_this.formData.reportCode,
            itemCode: prop,
            seqId: data[prop].seqId || null,
            itemValue: data[prop].value
          });
        }
      }


      // return;
      handleService.saveReportDetail(data_arr).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          vm.scope.$broadcast('resfreshReportDetail', m_this.formData.reportCode, m_this);
          modalService.close('success');
        }
      });
    }

    function saveMeasure() {
      // debugger;
      var m_this = this;
      var data = angular.extend({}, m_this.formData.measures);
      // return;
      handleService.saveMeasure(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          modalService.close('success');
        }
      });
    }

    function saveRequirement() {
      var m_this = this;
      var data = angular.extend({}, m_this.formData.requirements);
      // return;
      handleService.saveRequirement(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          modalService.close('success');
        }
      });
    }

    function del(seqId, reportCode) {
      levelService.del(seqId, reportCode).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          vm.load.func();
          modalService.close('success');
        }
      });
    }

    function getEventRequirement(event, reportCode, context) {
      handleService.getEventRequirement({
        reportCode: reportCode
      }).then(function(response) {
        if (response) {
          context.formData.requirementList = response.data.data.lst;
          // vm.rootScope.$digest();
        }
      });
    }

    function getEventMeasures(event, reportCode, context) {
      handleService.getEventMeasures({
        reportCode: reportCode
      }).then(function(response) {
        if (response) {
          context.formData.measureList = response.data.data.lst;
          // vm.rootScope.$digest();
          // debugger;
        }
      });
    }

    function getReportDetail(event, reportCode, context) {
      handleService.getReportDetail({
        reportCode: reportCode
      }).then(function(response) {
        if (response) {
          context.formData.reportDetailList = response.data.data.lst;
          // vm.rootScope.$digest();
          // debugger;
        }
      });
    }

    // 数据刷新
    vm.scope.$on('refreshMeasure', getEventMeasures);
    vm.scope.$on('refreshRequirement', getEventRequirement);
    vm.scope.$on('refreshReportDetail', getReportDetail);
  }
})();
