(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('QualityDealController', dealControllerFunc);

  /** @ngInject */
  function dealControllerFunc($scope, $log, NgTableParams, $filter, dealService, modalService, toastr, security, deptService) {
    var vm = this;
    var dataForDealModal = {
      formData: {},
      metaData: {}
    };

    vm.options = {
      month: {
        datepickerMode: 'month',
        minMode: 'month'
      }
    };

    var userCode = security.getUserinfo().user.userCode
    var get_result_code;

    vm.add = add;
    vm.saveEdit = saveEdit;
    vm.save = save;
    vm.edit = edit;
    vm.del = del;
    vm.qualityFormList = qualityFormList;
    vm.deptCode = security.getUserinfo().role.deptCode;
    vm.checkDetail = checkDetail;

    vm.load = {
      func: initData,
      init: true
    };

    vm.queryParams = {
      type: '',
      month: new Date(),
      deptCode: security.getUserinfo().role.deptCode
    };

    get_myResultCode().then(initData);

    deptService.getDeptData().then(function(data) {
      vm.deptData = handleData(data);
    });

    function handleData(data) {
      var result = [];
      result = result.concat([_.pick(data, 'deptCode', 'deptName')]);
      _.each(data.belongedDepts, function(v) {
        result = result.concat(handleData(v));
      });
      return result;
    }

    function get_myResultCode() {
      return dealService.getResultCode(vm.queryParams).then(function(response) {
        if (response) {
          vm.list = response.data.data;

          return vm.list[0].resultCode;
        }
      });
    }

    // initData();

    function initData(resCode) {
      dealService.getDealList(resCode).then(function(response) {
        if (response) {
          vm.lst = response.data.data;
          vm.tableParams = new NgTableParams({}, {
            dataset: vm.lst
          });
        }
      });
    }

    //## 弹出框-检查表单
    function qualityFormList() {
      dealService.getAllQualityForm().then(function(response) {
        if (response) {
          vm.formList = response.data.data;
          vm.tableParams = new NgTableParams({}, {
            dataset: vm.formList
          });

          dataForDealModal.metaData.qualityFormList = angular.extend({}, vm.formList);
        }
      });
    }

    // dataForLeaveModal.formData.deptCode = security.getUserinfo().role.deptCode;

    function add() {
      dataForDealModal = {
        formData: {},
        metaData: {}
      };

      qualityFormList();

      modalService.open({
        templateUrl: 'app/quality/deal/addDeal.tpl.html',
        size: 'lg',
        ok: save,
        data: dataForDealModal,
        methodsObj: {
          qualityFormList: qualityFormList,
          checkDetail: checkDetail
        }
      });
    }

    //## 弹出框-查看检测详情
    function checkDetail() {
      var m_this = this;
      var s_date,
        s_deptCode,
        s_formName,
        init_date;

      dataForDealModal.metaData = {};

      if (!m_this.formData.checkDetail) return;

      if (!m_this.formData.checkDetail.taskDate || !m_this.formData.checkDetail.deptCode || !m_this.formData.checkDetail.selectForm.formName) return;

      init_date = m_this.formData.checkDetail.taskDate;

      s_date = moment(init_date).format('YYYY-MM-DD');
      s_deptCode = m_this.formData.checkDetail.deptCode;
      s_formName = m_this.formData.checkDetail.selectForm.formName;

      dealService.getChekcDetail(s_date, s_deptCode, s_formName).then(function(response) {
        if (response) {
          vm.get_lst = response.data.data;
          vm.tableParams = new NgTableParams({}, {
            dataset: vm.get_lst
          });

          dataForDealModal.metaData.checkDetailList = angular.extend({}, vm.get_lst);

          get_result_code = dataForDealModal.metaData.checkDetailList[0].resultCode;
        }
      });

      modalService.open({
        templateUrl: 'app/quality/deal/checkDetail.tpl.html',
        size: 'md',
        data: dataForDealModal,
        methodsObj: {
          qualityFormList: qualityFormList,
          checkDetail: checkDetail
        }
      });

    }

    //## 编辑
    function edit(row) {
      dataForDealModal.formData = angular.extend({}, row);
      vm.selectedLevel = row;

      if (row) {
        row.deptCode ? dataForDealModal.formData.deptList = row.deptCode.split(',') : '';

        row.joinUserName ? dataForDealModal.formData.memberList = row.joinUserName.split(',') : '';
      }

      modalService.open({
        templateUrl: 'app/quality/deal/addDeal.tpl.html',
        size: 'lg',
        ok: saveEdit,
        data: dataForDealModal,
        methodsObj: {
          qualityFormList: qualityFormList,
          checkDetail: checkDetail
        }
      });
    }

    //## 保存修改
    function saveEdit() {
      var data = angular.extend({}, dataForDealModal.formData);
      var init_taskDate = data.taskDate;

      data.deptCode = data.deptList ? data.deptList.join(',') : '';
      data.joinUserName = data.memberList ? data.memberList.join(',') : '';

      data.formCode = data.formCode.formCode;

      data.taskDate = moment(init_taskDate).format('YYYY-MM-DD');

      dealService.saveDeal(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          initData();
          vm.formData = {};
          modalService.close('success');
        }
      });
    }

    //## 保存
    function save() {
      var data = angular.extend({}, dataForDealModal.formData);

      if (!data.issueContent && !data.reasonAnalysis && !data.resDescribe && !data.resPlan && !data.evaluation) return;

      data.formName = data.checkDetail.selectForm.formName;

      data.resultCode = get_result_code;

      data.issueTime = moment(new Date()).format('YYYY-MM-DD hh:mm:ss');
      data.reasonTime = moment(new Date()).format('YYYY-MM-DD hh:mm:ss');
      data.resDTime = moment(new Date()).format('YYYY-MM-DD hh:mm:ss');
      data.resPTime = moment(new Date()).format('YYYY-MM-DD hh:mm:ss');
      data.evaluationTime = moment(new Date()).format('YYYY-MM-DD hh:mm:ss');

      data.issueUserCode = userCode;
      data.reasonUserCode = userCode;
      data.resDUserCode = userCode;
      data.resPUserCode = userCode;
      data.evaluationUserCode = userCode;

      dealService.saveDeal(data).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('保存成功！', '提示');
          initData();
          vm.formData = {};
          modalService.close('success');
        }
      });
    }

    //## 删除
    function del(seqId) {
      dealService.deleteDeal(seqId).then(function(response) {
        if (response && response.data.code === '0') {
          toastr.success('已删除！', '提示');
          initData();
          //如果删除选中行，那么编辑数据清空
          if (seqId == vm.formData.seqId) {
            vm.dataForDealModal.formData = {};
          }
          modalService.close('success');
        }
      });
    }
  }



})();
