/**
 * Created by lin on 16/3/15.
 */
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('BaseInfoCtrl', BaseInfoCtrl);

  /** @ngInject */
  function BaseInfoCtrl($rootScope, $scope, $state, deptService, baseInfoService, modalService, FileUploader, security, $stateParams) {
    var vm = this,
      currentNode;

    vm.scope = $scope;
    vm.openUpload = openUpload;
    vm.openAdd = openAdd;
    vm.switchType = switchType;
    vm.stateParam = $stateParams;
    vm.isIncumbency = true;
    vm.treeOption = {
      allowDeselect: false
    };

    vm.isTopDept = security.getUserinfo().role.isTopDept;

    try {
      vm.selectedNode = angular.fromJson(sessionStorage.getItem('selectedNode')) || {}
    } catch (err) {
      vm.selectedNode = {}
    }

    init();

    vm.select = select;

    function init(userInfo, updateTree) {

      var deptCache = deptService.getCacheDeptList();

      // 人事档案增加全院视角
      if (deptCache && !updateTree) {
        transformTree(deptCache, userInfo)
      } else {
        deptService.getDeptData().then(function(data) {
          transformTree(data, userInfo);
        });
      }
      // 获取离职员工列表
      baseInfoService.getDimission(security.getUserinfo().role.deptCode).then(function(response) {
        vm.dimissionData = response.data.data.lst;
      });

      // 新增用户时跳转至详情页
      if (userInfo) {
        $state.go('home.baseInfo.detail', {
          hisCode: userInfo.hisCode,
          deptN: '',
          deptCode: ''
        });
        vm.selectedNode = {}
      } else {
        // 刷新后选中状态不消失
        if (vm.selectedNode.deptName) {
          if ($state.current.controller !== 'BaseInfoDetailCtrl') {
            $state.go('home.baseInfo.stat', {
              id: vm.selectedNode.deptCode
            })
          }
        } else {
          if (vm.selectedNode.hisCode) {
            $state.go('home.baseInfo.detail', {
              hisCode: vm.selectedNode.hisCode,
              deptN: '',
              deptCode: ''
            })
          } else if (vm.selectedNode.userCode) {
            $state.go('home.baseInfo.detail', {
              id: vm.selectedNode.userCode,
              deptN: '',
              deptCode: ''
            })
          } else {
            $state.go('home.baseInfo.stat', {
              id: security.getUserinfo().role.deptCode
            })
          }
        }
      }
    }

    function transformTree(data, userInfo) {
      // 人事档案增加全院视角
      var data_temp = angular.toJson({
        deptCode: data.deptCode,
        deptName: data.deptName,
        user: data.user
      });
      vm.deptData = data;
      vm.deptData.belongedDepts.unshift(angular.fromJson(data_temp));
      vm.deptData.deptCode = '';
      vm.deptData.deptName = '全院';
      delete vm.deptData.user;
      vm.treeData = [handleData(vm.deptData)];

      if (Object.keys(vm.selectedNode).length === 0) {
        vm.expandedNodes = [vm.treeData[0]];
        vm.selectedNode = vm.treeData[0];
      }

      vm.scope.$watch('vm.filterText', function(v) {
        if (v) {
          vm.expandedNodes = [].concat(vm.treeData[0]).concat(vm.treeData[0].children);
        } else {
          vm.expandedNodes = [vm.treeData[0]];
        }
      });
    }


    function select(node) {
      if (vm.selectedNode === node) return;
      vm.selectedNode = node;
      $rootScope.$broadcast('loading', 'tree');
      if (node.deptName) {
        $state.go('home.baseInfo.stat', {
          id: node.deptCode
        });
      } else {
        $state.go('home.baseInfo.detail', {
          id: node.userCode,
          hisCode: node.hisCode,
          dept: node.deptCode,
          deptN: node.deptN
        });
      }
      sessionStorage.setItem('selectedNode', angular.toJson(node));
      $rootScope.$broadcast('loaded', 'tree');
    }

    function handleData(data, innerIndex) {
      var result = {};
      result.deptCode = data.deptCode;
      result.deptName = data.deptName;
      _.each(data.belongedDepts, function(v, i) {
        data.belongedDepts[i] = handleData(v, i);
      })

      // 给每个用户加上deptCode
      if (Array.isArray(data.user)) {
        data.user.forEach(function(item) {
          item.deptCode = data.deptCode;
          item.deptN = data.deptName;
        })
      }

      result.children = _.compact([].concat(data.user, data.belongedDepts));
      return result;
    }

    function openUpload() {
      modalService.open({
        templateUrl: 'app/nursingWorkers/baseInfo/uploadFile.modal.html',
        size: 'md',
        data: {
          formData: {
            uploader: new FileUploader({
              url: 'mnisqm/system/importSysUserExcel'
            })
          }
        }
      });
    }

    function openAdd() {
      modalService.open({
        templateUrl: 'app/nursingWorkers/baseInfo/add.modal.html',
        size: 'md',
        data: {
          formData: {
            userType: '03'
          }
        },
        ok: function(data) {
          save(data)
            // init(data);
        }
      });
    }

    function save(formData) {
      var data = {};
      data.dataType = 'comUser';
      data.comUser = formData;

      baseInfoService.add(data).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '保存成功！'
          });

          modalService.close('success');

          init(data.comUser, true);
        }
      });
    }

    function switchType() {
      vm.isIncumbency = !vm.isIncumbency;
    }

    // 刷新树形菜单
    $scope.$on('refreshTree', init)

  }

})();
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('BaseInfoStatCtrl', BaseInfoStatCtrl);

  /** @ngInject */
  function BaseInfoStatCtrl($rootScope, $scope, $echarts, $state, $stateParams, chartsService, baseInfoService, deptService, $q) {
    $rootScope.$broadcast('loaded', 'tree');
    var vm = this;

    vm.total = 0;
    vm.activeCount = 0;
    vm.inviteCount = 0;
    vm.scope = $scope;
    vm.goDetail = goDetal;
    vm.stastics = {
      duty: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:3',
        config: chartsService.pieInit('职务')
      },
      technicalPost: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:3',
        config: chartsService.pieInit('职称')
      },
      teachLevel: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:3',
        config: chartsService.pieInit('带教')
      },
      clinical: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:3',
        config: chartsService.pieInit('临床层级')
      },
      education: {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '4:3',
        config: chartsService.pieInit('学历')
      }
    }

    load();
    // getUserCount()

    function load() {

      // 获取科室人员统计信息
      baseInfoService.getUserStatistics($stateParams.id).then(function(response) {
        var data = response.data.data;
        var prop;

        // 转换数据为图表需要的格式
        for (prop in data) {
          if (data.hasOwnProperty(prop) && data[prop]) {
            data[prop] = data[prop].map(function(dataItem) {
              return {
                value: dataItem.sitcValue,
                name: dataItem.sticType
              };
            });
          }
        }

        for (prop in vm.stastics) {
          if (vm.stastics.hasOwnProperty(prop) && vm.stastics[prop]) {
            vm.stastics[prop].config.legend.data = _.pluck(data[prop], 'name');
            vm.stastics[prop].config.series[0].data = data[prop];
            // vm.stastics[prop].config.series[0].label.normal.position = 'center';
            vm.stastics[prop].config.series[0].label.normal.show = false;
            vm.stastics[prop].config.series[0].label.emphasis = {
              show: false
            };
            vm.stastics[prop].config.series[0].labelLine = {
              normal: {
                show: false
              },
              emphasis: {
                show: false
              }
            };
            // vm.stastics[prop].config.series[0].labelLine.normal.show = false;
            // vm.stastics[prop].config.legend.show = false;

            $echarts.updateEchartsInstance(vm.stastics[prop].identity, vm.stastics[prop].config);
          }
        }
      })
    }

    // 计算人员数
    // function getUserCount() {
    //   var deptList = deptService.getCacheDeptList();
    //   var users = [];
    //   var defferd = $q.defer();
    //
    //   vm.total = 0;
    //   vm.activeCount = 0;
    //   vm.dimissonCount = 0;
    //
    //   if (!deptList) {
    //     deptService.getDeptData().then(function (response) {
    //       defferd.resolve(handle(response));
    //     })
    //   } else {
    //     defferd.resolve(handle(deptList));
    //   }
    //
    //   function handle(obj) {
    //     try {
    //       users = users.concat(obj.user);
    //       vm.total += obj.user.length;
    //     } catch (err) {
    //       vm.total += 0;
    //     }
    //
    //     if (obj.belongedDepts) {
    //       obj.belongedDepts.forEach(function (item) {
    //         handle(item);
    //       })
    //     }
    //   }
    //
    //   // 在父的controller中得到了离职员工列表，在那里触发这个事件
    //   vm.scope.$on('getDimissonCount', function(e, dimissonData) {
    //     // 等待计算出总人数后，再计算在编和聘用人数
    //     defferd.promise.then(function () {
    //       vm.activeCount = vm.total - dimissonData.length;
    //       vm.dimissonCount = dimissonData.length;
    //
    //     })
    //   })
    // }

    // 双击进入员工详情数据
    function goDetal(row) {
      var selectedNode = {
        deptCode: row.deptCode,
        deptN: $scope.$parent.vm.selectedNode.deptName,
        userCode: row.userCode,
        userName: row.userName
      };

      $state.go('home.baseInfo.detail', {
        id: row.userCode,
        deptN: $scope.$parent.vm.selectedNode.deptName,
        dept: row.deptCode
      });

      sessionStorage.setItem('selectedNode', angular.toJson(selectedNode))

      $scope.$parent.vm.selectedNode = selectedNode
    }

    baseInfoService.getUserList($stateParams.id).then(function(data) {
      // $log.info('用户数据', data);
      // vm.detail = filesService.userList;
      vm.userDataList = data;
      vm.deptTotal = data.length;

      vm.activeCount = data.filter(function(item) {
        return item.userType === '03';
      }).length;

      vm.inviteCount = data.filter(function(item) {
        return item.userType === '05';
      }).length;


    });

  }

})();
(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('BaseInfoDetailCtrl', BaseInfoDetailCtrl);

  /** @ngInject */
  function BaseInfoDetailCtrl($rootScope, $scope, $state, $stateParams, baseInfoService, modalService, security, moment) {
    $rootScope.$broadcast('loaded', 'tree');

    var vm = this;

    vm.isTopDept = $scope.$parent.vm.isTopDept;

    vm.scope = $scope;
    vm.openInfo = openInfo;
    vm.stateParam = $stateParams;
    vm.quit = quit;
    vm.openConfirm = openConfirm;
    vm.cancelEdit = cancelEdit;

    vm.marrige = [{
      code: '01',
      name: '已婚'
    }, {
      code: '02',
      name: '未婚'
    }, {
      code: '03',
      name: '离异'
    }, {
      code: '04',
      name: '丧偶'
    }];
    var itemInitData = {
      comUserPosition: {
        positionName: '',
        reviewUnit: '',
        startDate: '',
        endDate: ''
      },
      comUserNursing: {
        reviewUnit: '',
        unitLevel: '',
        deptName: '',
        position: '',
        startDate: '',
        endDate: ''
      },
      comUserEducation: {
        educationType: '',
        leaningTime: '',
        courseTopics: '',
        courseContent: '',
        startDate: '',
        endDate: ''
      },
      comUserTraining: {
        learnContent: '',
        professional: '',
        diploma: '',
        speaker: '',
        creditType: '',
        credit: '',
        funding: '',
        describe: '',
        startDate: '',
        endDate: ''
      },
      comUserLearning: {
        school: '',
        professional: '',
        diploma: '',
        diplomaNo: '',
        degree: '',
        degreeNo: '',
        describe: '',
        startDate: '',
        endDate: ''
      },
      comUserFamily: {
        relation: '',
        name: '',
        phone: '',
        email: '',
        remark: ''
      }
    };

    // load();

    vm.openCalender = function(item, obj) {
      if (obj) {
        obj[item] = true;
      } else {
        vm[item] = true;
      }
    };
    vm.datepickerOption = {
      dateDisabled: 'disabled',
      formatYear: 'yyyy',
      formatMonth: 'MM',
      formatDay: 'dd',
      initDate: new Date(),
      maxDate: new Date(2020, 5, 22),
      minDate: moment(),
      startingDay: 1
    };
    vm.addItem = function(type) {
      vm[type + '_len'] = vm.detail[type].length;
      vm.detail[type].push(angular.extend({}, itemInitData[type]));
    };

    vm.removeItem = function(type, index) {
      vm.detail[type].splice(index, 1);
    };
    vm.save = save;
    vm.load = {
      func: getData,
      init: true
    }

    function getData() {
      var _userCode = $stateParams.id;
      var _hisCode = $stateParams.hisCode;

      if (_hisCode) {
        _userCode = '';
      }

      return baseInfoService.getUserInfo(_userCode, _hisCode).then(function(response) {
        vm.detail = response.data.data;

        vm.originalDetail = angular.merge({}, vm.detail);
      });
    }

    function openConfirm(type) {
      modalService.open({
        templateUrl: 'app/common/confirm/confirm.tpl.html',
        size: 'sm',
        ok: (type === 4 ? quit : retire),
        data: {
          metaData: {
            title: (function() {
              switch (type) {
                case 4:
                  return '确定登记为离职吗？';

                case 6:
                  return '确定登记为退休吗？';
              }
            })()
          },
          formData: vm.detail.comUser.userCode
        }
      })
    }

    function openInfo(name, title, type) {
      modalService.open({
        templateUrl: 'app/nursingWorkers/baseInfo/editInfoTime.modal.html',
        size: 'md',
        data: {
          metaData: {
            title: title,
            name: name,
            list: vm.detail[type]
          },
          formData: vm.detail.comUser
        },
        initFn: function(ctrl) {
          // 带教／临床层级等获取人员变更信息
          if (['technicalPost', 'duty'].indexOf(ctrl.metaData.name) < 0) {
            baseInfoService.getComUserChange($stateParams.id).then(function(res) {
              if (ctrl.metaData.name === 'clinical') {
                ctrl.metaData.list = res.data.data.filter(function(dataItem) {
                  return parseInt(dataItem.changeType) === 1;
                });
              } else {
                ctrl.metaData.list = res.data.data.filter(function(dataItem) {
                  return parseInt(dataItem.changeType) === 2;
                });
              }
            })
          }
        },
        ok: save,
        methodsObj: {
          openCalender: function(item, obj) {
            if (obj) {
              obj[item] = true;
            } else {
              this[item] = true;
            }
          },
          saveChanges: saveChange
        }
      });
    }

    function saveChange(type) {
      var modal_vm = this;
      var foreName;
      var rearName;

      switch (type) {
        case 1:
          foreName = vm.originalDetail.comUser.clinical;
          rearName = modal_vm.formData.clinical;
          break;

        case 2:
          foreName = vm.originalDetail.comUser.teachLevel;
          rearName = modal_vm.formData.teachLevel;
          break;
      }

      baseInfoService.saveChange({
        userCode: $stateParams.id,
        changeType: type,
        foreName: foreName,
        rearName: rearName,
        changeTime: moment().format('YYYY-MM-DD HH:mm:ss')
      }).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '保存成功！'
          });

          modalService.close();
        }
      })
    }


    // 离职登记
    function quit(userCode) {
      baseInfoService.quit(userCode).then(function(rsp) {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '成功登记离职'
        });

        refreshMenu();
        modalService.close();

        // TODO 编辑权限控制
      });
    }

    // 退休登记
    function retire(userCode) {
      baseInfoService.retire(userCode).then(function(rsp) {
        $rootScope.$broadcast('toast', {
          type: 'success',
          content: '已登记为退休！'
        });

        refreshMenu();
        modalService.close();

        // TODO 编辑权限控制
      });
    }

    // 保存
    function save(type, typeFromModal) {
      var data = {};

      data.dataType = typeFromModal || type;

      data[type] = vm.detail[type];
      if (type == 'comUser') {
        data[type].deptCode = $stateParams.dept;
      } else if (typeof type === 'string') {
        data[type].forEach(function(item) {
          item.userCode = vm.detail.comUser.userCode;

          delete item.endDateIsOpen;
          delete item.startDateIsOpen;
        });

        vm[type + '_len'] = vm.detail[type].length;
      }
      // debugger;
      // 修改职务、职称时，另外处理
      if (typeof type === 'object') {
        type.deptCode = $stateParams.dept;
        data = {};
        data[typeFromModal] = {
          userCode: type.userCode
        };
        data.dataType = typeFromModal;
        // data[typeFromModal] = type;

        switch (typeFromModal) {
          case 'comUserPosition':
            data[typeFromModal].technicalPost = type.technicalPost;
            data[typeFromModal].reviewUnit = type.reviewUnit;
            data[typeFromModal].startDate = type.positionStartDate;
            break;

          case 'comUserNursing':
            data[typeFromModal].duty = type.duty;
            data[typeFromModal].startDate = type.dutyStartDate;
            break;
          default:
            break;
        }
      }

      baseInfoService.add(data).then(function(response) {
        if (response) {
          $rootScope.$broadcast('toast', {
            type: 'success',
            content: '保存成功！'
          });

          modalService.close('success');

          vm.load.func();
        }
      });
    }

    // 取消编辑后，将数据恢复开始编辑时的状态
    function cancelEdit(type) {
      if (typeof vm[type + '_len'] == 'undefined') {
        vm[type + '_len'] = vm[type + '_len'] || vm.detail[type].length;
      }
      vm.detail[type].splice(vm[type + '_len'], vm.detail[type].length - vm[type + '_len']);
    }

    $scope.$on('save', save);

    function refreshMenu() {
      $rootScope.$broadcast('refreshTree', true);
    }
  }

})();
