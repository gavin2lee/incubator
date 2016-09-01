/**
 * Created by gary on 16/5/16.
 */
var workLoad = {
  fields: [],
  childrenTypes: [],
  data: [],

  init: function() {
    var that = this;
    var $startDate = $('#startDate');
    var $endDate = $('#endDate');

    $('#print').on('click', function () {

      ay.printPage(that.generatePrintStr);
    });

    $startDate.datebox('setValue', new Date().format('yyyy-MM-dd'));
    $endDate.datebox('setValue', new Date().format('yyyy-MM-dd'));
    this.getWorkLoadTypes().done(function() {
      that.getWorkLoadInfosByNurse();
    });

    $('#refresh').on('click', that.getWorkLoadInfosByNurse)

  },
  getWorkLoadTypes: function() {
    return $.get(ay.contextPath + '/nur/workload/getWorkLoadTypes.do').done(this.getHeader).fail(function(err) {
      $.messager.alert('info', '数据错误,请重试');
    });
  },
  generatePrintStr: function () {
    var that = workLoad;
    var tHead = ['<table class="print-table">',
                    '<thead>',
                      ' <tr>'];
    var tbody = ['<tbody>'];
    var trs;
    
    that.fields.forEach(function (field) {
      tHead.push(['<th>', field.title, '</th>'].join(''));
    });

    tHead.push('</tr></thead>');

    trs = that.data.map(function (dataItem) {
      var tds = that.fields.map(function (item) {
        return ['<td>', dataItem[item.field], '</td>'].join('');
      }).join('');

      return ['<tr>', tds, '</tr>'].join('');
    }).join('');

    tbody.push(trs + '</tbody></table>');

    return tHead.concat(tbody).join('');
  },
  getHeader: function(data) {
    if (data.rslt === '0') {
      var types = data.data.types.map(function(typeItem) {
        return typeItem.children;
      });
      var that = workLoad;

      that.countObj = {
        rArr: [],
        meta: ['P', 'F']
      };

      that.fields = [];
      that.childrenTypes = [];

      types.forEach(function(item) {
        that.fields = that.fields.concat(item);
        that.childrenTypes = that.childrenTypes.concat(item.type);
      });

      that.fields = that.fields.map(function(item, i) {
        var fieldName = item.type;

        if (that.countObj.meta.indexOf(fieldName) >= 0) {
          fieldName = fieldName + parseInt(Math.random() * 10e5);

          that.countObj.rArr.push({
            name: item.name,
            field: fieldName
          });
        }

        return {
          field: fieldName,
          title: item.name,
          id: item.id,
          width: 1258 / (that.fields.length + 1) - 1,
          sortable: true
        };
      });

      that.fields.unshift({
        field: 'nurNam',
        title: '护士姓名',
        id: 'nurNam',
        width: 1258 / (that.fields.length + 1) - 1
      });

      return data;
    } else {
      $.messager.alert(data.msg);
      return;
    }
  },
  getWorkLoadInfosByNurse: function() {
    var that = workLoad;
    var startDate = $('#startDate').datebox('getValue');
    var endDate = new Date(new Date($('#endDate').datebox('getValue')).getTime() + 3600 * 24 * 1000).format('yyyy-MM-dd');
    var nurseCode = $('#nurseId').val();
    var deptCode = $('#deptId').val();
    var param = {
      deptCode: deptCode,
      types: '',
      startDate: startDate,
      endDate: endDate,
      childrenTypes: ''
    };

    if ($('#show-style__mine')[0].checked) {
      param.nurseCode = nurseCode;
    }

    return $.get(ay.contextPath + '/nur/workload/getWorkLoadInfosByNurseType.do', param).done(function(res) {
      if (res.rslt === '0') {
        that.data = that.transformData(res.data.wlInfs);

        that.createTable();
        return that.data;
      } else {
        $.messager.alert(res.msg);
        return null;
      }

    }).fail(function(err) {

    });
  },
  getWorkLoadInfosByNurseType: function() {

  },
  createTable: function() {
    var table = $('#work-table');
    var data = workLoad.data;

    table.datagrid({
      // rownumbers: true,
      columns: [workLoad.fields],
      data: data,
      singleSelect: true
    })
  },
  transformData: function(data) {
    var temp = [];
    var list = data;

    list.forEach(function(item) {
      var tempObj = {
        nurCod: item.nurCod,
        nurNam: item.nurCod + '-' + item.nurNam
      };

      item.wlDetailInfs.forEach(function(detailItem) {
        var typeName = detailItem.wlTyp;
        var temp;

        if (workLoad.countObj.meta.indexOf(typeName) >= 0) {
          typeName = workLoad.countObj.rArr.find(function (rItem) {
            return rItem.name == detailItem.wlTypNam
          }).field;
        }

        tempObj[typeName] = detailItem.stCount;
      });

      // debugger;

      temp.push(tempObj);
    });

    return temp;
    //for (var prop in data)
  }
};

function getWorkLoadDetail(patientStr) {
  var patientList = patientStr.split(',') || '',
    startDate = $('#startDate').datebox('getValue'),
    endDate = $('#endDate').datebox('getValue'),
    $inputs = $('.filter-wrapper').find('input'),
    $btns = $('.exec-wrapper').find('button');
  //$btns = $('.filter-wrapper').find('button');

  if (patientStr.replace(/[,]/g, '') === '') {
    $inputs.attr('disabled', 'disabled');
    $btns.attr('disabled', 'disabled').addClass('disabled');
    workLoad.queryParam.patIds = '';
    $('.exec-content').html('');
  } else {
    $inputs.removeAttr('disabled');
    $btns.removeAttr('disabled').removeClass('disabled');

    workLoad.queryParam.patIds = patientList.filter(function(curr) {
      if (curr !== '') return true;
    }).join(',');

    //execution.lastData();
  }
}

$(function() {
  workLoad.init();
  $('.show-style').find('input[type=radio]').rdobox();
});
