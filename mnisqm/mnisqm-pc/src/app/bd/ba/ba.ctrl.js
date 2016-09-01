(function() {
  'use strict';

  angular
    .module('nqms')
    .controller('BDBusAnalyserController', BDBusAnalyserController);

    function BDBusAnalyserController($echarts, $state, chartsService, bDBusAnalyserService) {
      var vm = this, pieConfig = chartsService.pieInit();
      
      delete pieConfig.legend;
      pieConfig.series[0].label.normal.show = false;
      pieConfig.series[0].radius = '80%';
      
      vm.options = {
        month: {
          datepickerMode: 'month',
          minMode: 'month'
        }
      };
      vm.queryParams = {
        month: new Date()
      };
      vm.pie = {
        identity: $echarts.generateInstanceIdentity(),
        dimension: '16:9',
        config: pieConfig
      };
      vm.go = go;
      vm.load = load;

      load();
      
      function load() {
        bDBusAnalyserService.getBABrief().then(function(rsp) {
          var data = rsp.data.data;
          var pieData = [
            { value: data.statis.hj, name: '护理记录' },
            { value: data.statis.pgd, name: '护理评估' },
            { value: data.statis.tys, name: '知情同意书' }
          ];
          pieConfig.series[0].data = pieData;
          $echarts.updateEchartsInstance(vm.pie.identity, pieConfig);
          vm.baseData = {
            'bh': data.statis.model,
            'er': (data.statis.errOrderNum / data.statis.orderNum * 100).toFixed(2),
            'ws': data.statis.docNum
          }
          vm.bhData = data.statis.lst;
          vm.wsData = data.statis.lst;
        });
      }

      function go(type) {
        $state.go('home.' + type);
      }
    }

})();