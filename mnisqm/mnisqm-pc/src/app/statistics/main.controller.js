(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('StatisticsController', StatisticsController);

  function StatisticsController(deptService) {
    var vm = this;

    deptService.getDeptData().then(function(data) {
      vm.deptData = data;
      vm.getDeptName = _.memoize(function(wardCode) {
        var name;
        _.some(data.belongedDepts, function(item) {
          if (item.deptCode == wardCode) {
            name = item.deptName;
            return true;
          }
        });
        name = name.replace('护士站', '');
        return name;
      });
    });

    vm.nurseCount = {
      total: 778,
      increase: 109,
      leave: 65
    }

    vm.dutyRoster = [{
      deptCode: '0211',
      hours: 3376
    }, {
      deptCode: '5063',
      hours: 3265
    }, {
      deptCode: '5043',
      hours: 2998
    }, {
      deptCode: '5042',
      hours: 2776
    }, {
      deptCode: '2032',
      hours: 2552
    }];

    vm.malCase = {
      type: '压疮',
      deptCode: '5043'
    };

    vm.research = {
      proj: 78,
      paper: 691
    };

    vm.quality = {
      worst: [
        '书写标准', '签名正规', '人数相符', '运用医学术语', '特殊用药'
      ],
      bestDeptCode: '5042',
      worstDeptCode: '0211'
    };

    vm.bedNursing = {
      liqDis: 10018,
      doctor: 9780,
      doc: 20012,
      patrol: 5012
    };

    vm.exam = {
      worst: [
        '学习能力', '抗压能力', '礼貌用语', '专业技能', '工作态度'
      ],
      bestDeptCode: '2032',
      worstDeptCode: '5063'
    };

    vm.sat = {
      patient: ['医疗费用', '医院环境', '医护态度'],
      nurse: ['工资待遇', '排班制度', '工作环境']
    };
  }

})();
