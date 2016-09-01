/**
 * Created by gary on 16/7/25.
 */
/**
 * Created by gary on 16/5/16.
 */
var inAndOut = {
  fields: {
    operation: [{
    	field: 'deptName',
    	title: '科室'
    }, {
      field: 'operationName',
      title: '手术名称'
    }, {
    	field: 'operationTime',
    	title: '手术时间'
    }, {
    	field: 'bedNo',
    	title: '床号'
    }, {
    	field: 'patientName',
    	title: '姓名'
    }],
    normal: [{
    	field: 'eventName',
    	title: '外出类型'
    }, {
    	field: 'record_date',
    	title: '时间'
    }, {
    	field: 'bedNo',
    	title: '床号'
    }, {
    	field: 'patientName',
    	title: '姓名'
    }, {
    	field: 'phone',
    	title: '电话'
    }, {
    	field: 'recorderName',
    	title: '交接人'
    }, {
    	field: 'remark',
    	title: '备注'
    }]
  },
  data: [],

  init: function() {
    var that = this;
    var $startDate = $('#startDate');
    var $endDate = $('#endDate');

    $startDate.datebox('setValue', new Date('2015-01-01').format('yyyy-MM-dd'));
    $endDate.datebox('setValue', new Date().format('yyyy-MM-dd'));

    that.getData().then(that.createTable);


    $('#refresh').on('click', function () {
      that.getData().then(that.createTable);
    });

  },

  getData: function() {
    // + 3600 * 24 * 1000
    var that = inAndOut;
    var startDate = new Date($('#startDate').datebox('getValue') + ' 00:00:01').format('yyyy-MM-dd hh:mm:ss');
    var endDate = new Date(new Date($('#endDate').datebox('getValue') + ' 23:59:59').getTime()).format('yyyy-MM-dd hh:mm:ss');
    var nurseCode = $('#nurseId').val();
    var eventCode = $('[name="action"]:checked').val();
    var param = {
      method: eventCode,
      beginTime: startDate,
      endTime: endDate,
    };

    if (eventCode === '') {
      delete param.method;
    }

    $.loading();
    that.eventCode = eventCode;

    return $.get(ay.contextPath + '/nur/patientManage/queryManageInfo', param).done(function(res) {
      $.loading('close');
      if (res.rslt === '0') {
        that.data = res.data || [];
        return that.data;
      } else {
        $.messager.alert(res.msg);
        return null;
      }

    }).fail(function(err) {
      $.loading('close');
      $.messager.alert('警告', '服务器错误！');
    });
  },
  pagerFilter: function (data) {
    if (typeof data.length == 'number' && typeof data.splice == 'function') { // is array
      data = {
        total: data.length,
        rows: data
      }
    }
    var dg = $(this);
    var opts = dg.datagrid('options');
    var pager = dg.datagrid('getPager');
    pager.pagination({
      onSelectPage: function(pageNum, pageSize) {
        opts.pageNumber = pageNum;
        opts.pageSize = pageSize;
        pager.pagination('refresh', {
          pageNumber: pageNum,
          pageSize: pageSize
        });
        dg.datagrid('loadData', data);
      }
    });
    if (!data.originalRows) {
      data.originalRows = (data.rows);
    }
    var start = (opts.pageNumber - 1) * parseInt(opts.pageSize);
    var end = start + parseInt(opts.pageSize);
    data.rows = (data.originalRows.slice(start, end));
    return data;
  },
  createTable: function() {
    var that = inAndOut;
    var table = $('#work-table');
    var data = inAndOut.data;
    var fields;

    switch (that.eventCode) {
      case 'ss':
        fields = that.fields['operation'];
        break;

      default:
      fields = that.fields['normal'];
    }

    table.datagrid({
      rownumbers: true,
      columns: [fields],
      data: data,
      singleSelect: true,
      onSelect: inAndOut.loadDetail,
      loadFilter: inAndOut.pagerFilter,
      pagination: true,
      pageSize: 20
    })
  },
  // TODO: 当前只有手术详情信息，后续添加出入院、外出、检查等信息展示
  loadDetail: function (index, row) {
    var that = inAndOut;
    var htmlArr;

  	console.log(arguments);
    switch (that.eventCode) {
      case 'ss':

      htmlArr = ['<li>',
'               <dl class="out-dl">',
'                 <dt>基本信息</dt>',
'                 <dd>',
'                   床号：<span  class="basic-info">', row.bedNo, '床</span>  姓名：<span  class="basic-info">', row.patientName, '</span>   科室：<span  class="basic-info">', row.deptName, '</span>',
'                 </dd>',
'                 <dd>',
'                   性别：<span>', (row.sex == 'F' ? '女' : '男'),'</span>  年龄：<span>', row.age, '</span>   住院号：<span>', row.patId,'</span>',
'                 </dd>',
'                 <dd>',
'                   手术时间：<span>', row.operationTime, '</span>',
'                 </dd>',
'                 <dd>',
'                   手术名称：<span class="basic-info">', row.operationName, '</span>',
'                 </dd>',
'               </dl>',
'             </li>',
'             <li>',
'               <dl class="out-dl operation-records">',
'                 <dt>手术记录</dt>',
(function () {
  if (row.patOperationStatusList) {
    return row.patOperationStatusList.map(function (statusItem) {
      return ['<dd>',
          statusItem.createTime,
          '，【',statusItem.statusName,'】',
          '，送回人员：',
          statusItem.createPerson,
          '，联系方式：',
          statusItem.phone,
      '</dd>'].join('');
    }).join('');
  }

  return '<dd>无</dd>';
})(),
'               </dl>',
'             </li>',
'             <li>',
'               <dl class="out-dl">',
'                 <dt>手术信息</dt>',
'                 <dd>',
'                   手术室：<strong class="basic-info">', row.operationNo,'</strong>',
'                 </dd>',
'                 <dd>',
'                   术前诊断：',
(row.preOperationDiagnosis || '')
,
'                 </dd>',
'                 <dd>',
'                   术后诊断：',
(row.postOperationDiagnosis || ''),
'                 </dd>',
'                 <dd>',
'                   主刀医生：<span>', (row.surgeon || ''), '</span>   麻醉医生：<span>', (row.anesthesiaDoctor || ''), '</span> 一助：<span>', (row.operationOne || ''), '</span>二助：<span>', (row.operationTwo || ''), '</span>',
'                 </dd>',
'                 <dd>',
'                   巡回护士：',
(row.circulatingNurse || '')
,
'                 </dd>',
'                 <dd>',
'                   器械护士：',
(row.instrumentNurse || ''),
'                 </dd>',
'                 <dd>',
'                   洗手护士：',
(row.scrubNurse || '')
,
'                 </dd>',
'               </dl>',
'             </li>'].join('');

        break;
      default:

      htmlArr = '<div style="padding: 0 10px 10px 10px; text-align: center;">暂无信息</div>';

    }

    $('.out-list').html(htmlArr);

  }
};

$(function() {
  inAndOut.init();
  $('.show-style').find('input[type=radio]').rdobox();
});
