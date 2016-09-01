(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('DepartmentManagerTurnsController', turnsControllerFunc);

  /** @ngInject */
  function turnsControllerFunc(turnsService,classesService, $log, $q, toastr, security, deptService) {
    var vm = this, date = new Date(), cache = {};
    date.setDate(date.getDate() - date.getDay() + 1);
    vm.saveTurns = saveTurns;
    vm.nextWeek = nextWeek;
    vm.backWeek = backWeek;
    vm.newTurns = newTurns;
    vm.copyWeek = copyWeek;
    vm.load = {
      func: initData,
      init: true
    };
    vm.treeData = [];
    vm.treeOption = {
      allowDeselect: false,
      dirSelectable: false
    };
    vm.exp = /\d{4}-/;
    deptService.getDeptData().then(function(data) {
      vm.deptData = handleData(data);
    });
    

      // initData();//数据初始化
    
    vm.select = select;
    vm.tableLayout = tableLayout;
    vm.compare = compare;

    (function() {
      vm.treeData.push(initCalendar(date.getFullYear() - 1));
      vm.treeData.push(initCalendar(date.getFullYear()));
      vm.treeData.push(initCalendar(date.getFullYear() + 1));
      handlerTree();
    })()

    vm.params = {
      year: vm.selectedNode.year,
      weeks: vm.selectedNode.index,
      deptCode : security.getUserinfo().role.deptCode
    }

    function handlerTree() {
      var year = _.find(vm.treeData, function(v) {
        return v.year == date.getFullYear();
      });
      var month = year.children[date.getMonth()];
      var day = _.find(month.children, function(v) {
        return (v.start < date) && (v.end > date);
      });
      vm.expandedNodes = [year, month];
      vm.selectedNode = day;
      // vm.expandedNodes = [vm.treeData[1], vm.treeData[1].children[date.getMonth()]];
      // vm.selectedNode = _.find(vm.treeData[1].children[date.getMonth()].children, function(v) {
      //   return (v.start < date) && (v.end > date);
      // });
    }

    function select(node) {
      if(node.index) {
        vm.params.year = node.year;
        vm.params.weeks = node.index;
        vm.load.func();
      }
    }

      function initData(){
        var q1 = classesService.getClassList({
          deptCode: vm.params.deptCode
        }).then(function(response){
            if(response){
                vm.classList = response.data.data.lst;
                vm.getClassName = _.memoize(function(classCode) {
                  var name;
                  _.some(vm.classList, function(item) {
                    if (item.classCode == classCode) {
                      name = item.className;
                      return true;
                    }
                  });
                  return name;
                });
                vm.getClassColor = _.memoize(function(classCode) {
                  var color;
                  _.some(vm.classList, function(item) {
                    if (item.classCode == classCode) {
                      color = item.color;
                      return true;
                    }
                  });
                  return color;
                });
            }
        });

        var q2 = turnsService.getSchedule(vm.params).then(function(response) {
            if (response) {
                vm.data = response.data.data;
                var data = handleList(vm.data.dtls);
                vm.lst = data.raw;
                vm.weeks = vm.data.weeks;
                vm.dirtyLst = data.dirty;
                cache = {};
            }
        });
        return $q.all([q1,q2]);
      }

    function saveTurns() {
      var copy = angular.copy(vm.lst);
      //将临时变量weekNclass作为输入生成新的weekN
      _.forEach(copy, function(v) {
        for(var i = 1; i <= 7; i++) {
          v['week' + i] = v['week' + i].slice(0, v['week' + i + 'class'].length);
          _.forEach(v['week' + i + 'class'], function(c, index) {
            if(v['week' + i][index]) {
              v['week' + i][index].classCode = c;
            }else {
              v['week' + i][index] = {
                classCode: c
              }
            }
          });
          delete v['week' + i + 'class'];
          delete v['week' + i + 'meta'];
        }
      });
      vm.data.dtls = copy;
      turnsService.saveSchedule(vm.data).then(function(response) {
        if (response && response.data.code == 0) {
          toastr.success('保存成功！', '提示');
            vm.load.func(vm.data.weeks);
        }
      });
    }
        //下一个星期
      function nextWeek(){
              vm.params.weeks = vm.params.weeks + 1;
              date.setDate(date.getDate() + 7);
              handlerTree();
              vm.load.func();
      }
      //上一个星期
      function backWeek(){
              vm.params.weeks = vm.params.weeks - 1;
              date.setDate(date.getDate() - 7);
              handlerTree();
              vm.load.func();
      }
      //新增排班-针对本周
      function newTurns(){
          turnsService.newSchedule(vm.data.weeks).then(function(response) {
              if (response && response.data.code == 0) {
                  vm.data = response.data.data;
                  var data = handleList(vm.data.dtls);
                  vm.lst = data.raw;
                  // vm.weeks = vm.data.weeks;
                  vm.dirtyLst = data.dirty;
                  cache = {};
                  // vm.lst = vm.data.dtls;
              }
          });
      }

      function copyWeek(){
          turnsService.copySchedule(vm.params).then(function(response){
                if (response && response.data.code == 0) {
                    toastr.success('复制成功！', '提示');
                    vm.data = response.data.data;
                    var data = handleList(vm.data.dtls);
                    vm.lst = data.raw;
                    // vm.weeks = vm.data.weeks;
                    vm.dirtyLst = data.dirty;
                    cache = {};
                    // vm.lst = vm.data.dtls;
                }
            });
      }

      function initCalendar(year) {
        var date = new Date(year, 0, 1),
          result = {},
          count = 0;
        // date.setDate(date.getDate() - date.getDay() + 1);
        result.text = year + '年';
        result.year = year;
        result.children = [];
        while(date.getFullYear() <= year) {
          var week = createWeek(),
            date = week.end,
            month = week.start.getFullYear() < year ? week.end.getMonth() : week.start.getMonth();
          if(!result.children[month]) {
            result.children[month] = {
              children: [],
              text: (month + 1) + '月',
              year: year
            };
          };
          result.children[month].children.push(week);
        }
        return result;
        function createWeek() {
          var start = new Date(date.getFullYear(), date.getMonth(), date.getDate() - date.getDay() + 1),
              end = new Date(start.getFullYear(), start.getMonth(), start.getDate() + 6);
          count++;
          return {
            start: start,
            end: end,
            text: '第' + count + '周' + '（' + start.getDate() + '~' + end.getDate() + '）',
            index: count,
            year: year
          }
        }
      }

      function handleData(data) {
        var result = [];
        result = result.concat([_.pick(data, 'deptCode', 'deptName')]);
        _.each(data.belongedDepts, function(v) {
          result = result.concat(handleData(v));
        });
        return result;
      }

      function handleList(data) {
        //提取weekN中的classCode为数组供select作为model
        _.forEach(data, function(v) {
          for(var i = 1; i <= 7; i++) {
            v['week' + i + 'class'] = _.compact(_.pluck(v['week' + i], 'classCode'));
            v['week' + i + 'meta'] = angular.copy(v['week' + i + 'class']);
          }
        });
        //先使用姓名排序
        data = _.sortBy(data, function(v, i) {
          return v.userName;
        });
        //设置默认值
        _.forEach(data, function(v) {
          v.groupName = v.groupName || '未分组';
          v.clinical = v.clinical || '未分层';
        });
        //遍历所有元素groupName属性
        var groups = _.uniq(_.pluck(data, 'groupName'));
        //遍历所有元素的clinical属性
        var lays = _.uniq(_.pluck(data, 'clinical'));
        //使用groupName分组
        var grouped = _.groupBy(data, function(v) {return v.groupName});
        var raw = [];
        _.forEach(grouped, function(v, i) {
          //使用clinical分组
          var layed = _.groupBy(v, function(v) {
            return v.clinical;
          });
          //获取每个分组的大小
          var length = grouped[i].length;
          grouped[i] = layed;
          grouped[i].length = length;
        });
        //未分组放在最后
        groups = _.sortBy(groups, function(v, i) {
          var sort = (v === '未分组') ? 999: i;
          return sort;
        });
        //未分层放在最后
        lays = _.sortBy(lays, function(v, i) {
          var sort = (v === '未分层') ? 999: i;
          return sort;
        });
        //获取经过姓名排序、分组排序后最终的list
        _.forEach(groups, function(v1) {
          _.forEach(lays, function(v2) {
            raw = raw.concat(grouped[v1][v2])
          });
        });
        raw = _.compact(raw);
        //返回raw，供view层调用，返回dirty供计算rowspan
        return {
          raw: raw,
          dirty: grouped
        };
      }
      function tableLayout(id, group, lay) {
        var result;
        if(cache[group + lay]) {
          result = cache[group + lay][id] || 0;
        }else if(lay){
          result = vm.dirtyLst[group][lay].length;
          cache[group + lay] = {};
          cache[group + lay][id] = result;
        }else {
          result = vm.dirtyLst[group].length;
          cache[group + lay] = {};
          cache[group + lay][id] = result;
        }
        return result;
      }
      function compare(data, index) {
        return _.isEqual(data['week' + index + 'class'], data['week' + index + 'meta']);
      }
  }

})();
