(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DepartmentManagerAttemperController', attemperControllerFunc);

  /** @ngInject */
  function attemperControllerFunc($scope, $q, attemperService, $uibModal, $filter, toastr,classesService, deptService, security){
      var vm = this, deptCode = security.getUserinfo().role.deptCode;
      var formData = {};

      vm.save = save;
      vm.add = add;
      vm.del = del;
      vm.edit = edit;
      vm.load = {
        func: initData,
        init: true
      };
      vm.handler = handler;
      vm.filter = filter;
      vm.upload = upload;

      $scope.$on('deptchange', function(e, code) {
        deptCode = code;
        vm.load.func()
      });


      deptService.getDeptData().then(function(data) {
        vm.users = handleData(data);
        vm.getUserName = _.memoize(function(userCode) {
          var name;
          _.some(vm.users, function(item) {
            if (item.userCode == userCode) {
              name = item.userName;
              return true;
            }
          });
          return name;
        });
      });

      // initData();

      // 获取不良事件数据
      function initData() {
          var q1 = attemperService.getChangeClass({
            deptCode: deptCode
          }).then(function(response) {
              if (response) {
                  vm.list = response.data.data.lst;
              }
          });

          var q2 = classesService.getClassList({
            deptCode: deptCode
          }).then(function(response){
              if(response){
                  vm.classes = response.data.data.lst;
                  vm.getClassName = _.memoize(function(classCode) {
                    var name;
                    _.some(vm.classes, function(item) {
                      if (item.classCode == classCode) {
                        name = item.className;
                        return true;
                      }
                    });
                    return name;
                  });
              }
          });
          return $q.all([q1, q2]);
      }
      //编辑
      function edit(row){
          open(row);
      }
      //新增
      function add(){
          formData = {};
          open();
      }

      function open(row) {
        var modalInstance = $uibModal.open({
          templateUrl: 'app/duty/leave/attemper/attemper.model.html',
          controller: 'AttemperModalCtrl',
          controllerAs: 'vm',
          backdrop: 'static',
          animation: true,
          size: 'lg',
          resolve: {
            id: function() {
              return row;
            }
          }
        });

        modalInstance.result.finally(function() {
          vm.load.func();
        });
          // modalService.open({
          //     templateUrl: 'app/departmentManager/attemper/attemper.model.html',
          //     size: 'lg',
          //     ok: save,
          //     data: {
          //         formData: formData,
          //         classes : vm.classes
          //     },
          //     methodsObj: {
          //     }
          // });
      }

      function handler(data) {
        var modalInstance = $uibModal.open({
          templateUrl: 'app/duty/leave/attemper/review.tpl.html',
          controller: 'AttemperHandlerCtrl',
          controllerAs: 'vm',
          backdrop: 'static',
          animation: true,
          size: 'sm',
          resolve: {
            id: function() {
              return angular.copy(data);
            }
          }
        });

        modalInstance.result.finally(function() {
          vm.load.func();
        });
      }


      function save() {
          attemperService.saveChangeClass(formData).then(function (response){
              if(response && response.data.code =='0'){
                  toastr.success('保存成功！','提示');
                  vm.load.func();
                  modalService.close('success');
              }
          });
      }

      function del(seqId) {
          attemperService.deleteChangeClass(seqId).then(function (response) {
              if (response && response.data.code == '0') {
                  toastr.success('已删除！', '提示');
                  vm.load.func();
              }
          });
      }

      function handleData(data) {
        var result = [];
        result = result.concat(data.user);
        _.each(data.belongedDepts, function(v, i) {
          result = result.concat(handleData(v));
        })
        result = _.compact(_.flatten(result));
        return result;
      }

      function upload(row) {
        row.createTime = $filter('date')(row.createTime, 'yyyy-MM-dd HH:mm:ss');
        row.updateTime = $filter('date')(row.updateTime, 'yyyy-MM-dd HH:mm:ss');
        row.status = '02';
        attemperService.apprvChangeClass(row).then(function (response){
          vm.load.func();
          toastr.success('处理成功！','提示');
        });
      }

      function filter(item) {
        return item.status === '01' || item.status === '02' || item.status === '03';
      }
  }

})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('AttemperModalCtrl', AttemperModalCtrl);

    function AttemperModalCtrl($rootScope, $scope, $filter, $uibModalInstance, attemperService, classesService, security, toastr, deptService, id, turnsService) {
      var vm = this, deptCode = security.getUserinfo().role.deptCode;
      vm.userCode = security.getUserinfo().user.userCode;
      classesService.getClassList({
        deptCode: deptCode
      }).then(function(response){
          if(response){
              vm.classes = response.data.data.lst;
              vm.getClassName = _.memoize(function(classCode) {
                var name;
                _.some(vm.classes, function(item) {
                  if (item.classCode == classCode) {
                    name = item.className;
                    return true;
                  }
                });
                return name;
              });
              vm.getClassColor = _.memoize(function(classCode) {
                var color;
                _.some(vm.classes, function(item) {
                  if (item.classCode == classCode) {
                    color = item.color;
                    return true;
                  }
                });
                return color;
              });
          }
      });

      deptService.getDeptData(deptCode).then(function(data) {
        vm.users = data.user;
      });

      vm.cancel = cancel;
      vm.ok = ok;
      vm.formData = id || {
        applyUserCode : security.getUserinfo().user.userCode,
        status : '01'
      };
      vm.apply = {
        list: null,
        params: {
          deptCode : deptCode,
          weeks: vm.formData.applyClassDate ? countWeek(new Date(vm.formData.applyClassDate)): countWeek()
        }
      };
      vm.change = {
        list: null,
        params: {
          deptCode : deptCode,
          weeks: vm.formData.applyClassDate ? countWeek(new Date(vm.formData.changeClassDate)): countWeek()
        }
      };
      vm.changeUser = changeUser;
      vm.chooseClass = chooseClass;
      vm.nextWeek = nextWeek;
      vm.prevWeek = prevWeek;
      vm.activeCell = activeCell;
      getApplySchedule();
      getChangeSchedule();

      function cancel() {
        $uibModalInstance.dismiss();
      }

      function ok() {
        vm.formData.createTime && (vm.formData.createTime = $filter('date')(vm.formData.createTime, 'yyyy-MM-dd HH:mm:ss'));
        vm.formData.updateTime && (vm.formData.updateTime = $filter('date')(vm.formData.updateTime, 'yyyy-MM-dd HH:mm:ss'));
        vm.formData.applyClass = vm.applyObj.classCode;
        vm.formData.applyClassDate = vm.applyObj.recordDate;
        vm.formData.changeClass = vm.changeObj.classCode;
        vm.formData.changeClassDate = vm.changeObj.recordDate;
        attemperService.saveChangeClass(vm.formData).then(function (response){
          toastr.success('保存成功！','提示');
          $uibModalInstance.close('success');
        });
      }

      function getApplySchedule() {
         turnsService.getSchedule(vm.apply.params).then(function(response) {
            if (response) {
              vm.apply.data = response.data.data;
              vm.apply.list = handleData(response.data.data.dtls);
              vm.apply.params.weeks = response.data.data.weeks;
                // vm.data = response.data.data;
                // vm.lst = vm.data.dtls;
                // vm.weeks = vm.data.weeks;
                // vm.dirtyLst = data.dirty;
                // cache = {};
            }
        });
      }

      function getChangeSchedule() {
         turnsService.getSchedule(vm.change.params).then(function(response) {
            if (response) {
              vm.change.data = response.data.data;
              vm.change.list = handleData(response.data.data.dtls);
              vm.change.params.weeks = response.data.data.weeks;
                // vm.data = response.data.data;
                // vm.lst = vm.data.dtls;
                // vm.weeks = vm.data.weeks;
                // vm.dirtyLst = data.dirty;
                // cache = {};
            }
        });
      }

      function handleData(data) {
        _.forEach(data, function(v) {
          for(var i = 1; i <= 7; i++) {
            v['week' + i + 'class'] = _.compact(_.pluck(v['week' + i], 'classCode'));
            v['week' + i + 'meta'] = angular.copy(v['week' + i + 'class']);
          }
        });
        return data;
      }

      function changeUser() {
        getChangeSchedule();
      }

      function chooseClass(item, type) {
        if(!item.classCode) return;
        vm[type + 'Obj'] = item;
        // if(type === 'apply') {
        //   vm.applyObj = row;
        //   // vm.formData.applyClass = row.classCode;
        //   // vm.formData.applyClassDate = row.recordDate;
        // }else {
        //   vm.changeObj = row;
        //   // vm.formData.changeClass = row.classCode;
        //   // vm.formData.changeClassDate = row.recordDate;
        // }
      }

      function activeCell(item, type) {
        return vm[type + 'Obj'] === item;
        // var result;
        // _.some(vm[type].data, function(v, i) {
        //   if(i.indexOf('week') === 0) {
        //     if(v && v.indexOf(vm.formData[type + 'ClassDate']) === 0) {
        //       result = i.charAt(4);
        //       return true;
        //     }
        //   }
        // });
        // return result;
      }

      function nextWeek(type) {
        if(type === 'apply') {
          vm.apply.params.weeks++;
          getApplySchedule();
        }else {
          vm.change.params.weeks++;
          getChangeSchedule();
        }
      }

      function prevWeek(type) {
        if(type === 'apply') {
          vm.apply.params.weeks--;
          getApplySchedule();
        }else {
          vm.change.params.weeks--;
          getChangeSchedule();
        }
      }

      function countWeek(date) {
        if(!date) date = new Date();
        var year = date.getFullYear();
        var start = new Date(year, 0, 1);
        var offset = (7 - start.getDay() + 1);
        var day = Math.floor((date.getTime() - start.getTime()) / (1000 * 3600 * 24)) + 1;
        return Math.ceil((day - offset) / 7) + 1;
      }

    }
})();

(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('AttemperHandlerCtrl', AttemperHandlerCtrl);

    function AttemperHandlerCtrl($rootScope, $scope, $filter, $uibModalInstance, attemperService, toastr, id) {
      var vm = this;
      vm.formData = id;
      vm.ok = ok;
      vm.cancel = cancel;

      function ok() {
        vm.formData.createTime = $filter('date')(vm.formData.createTime, 'yyyy-MM-dd HH:mm:ss');
        vm.formData.updateTime = $filter('date')(vm.formData.updateTime, 'yyyy-MM-dd HH:mm:ss');
        attemperService.apprvChangeClass(vm.formData).then(function (response){
          toastr.success('处理成功！','提示');
          $uibModalInstance.close('success');
        });
      }

      function cancel() {
        $uibModalInstance.dismiss();
      }
    }

})();