var _data = {"total":1,"rows":[
	{
		"id":1,
		"getTime":"2014-08-15",
		"bedId":1,
		"pName":"金秀芝秀",
		"hospitalNumber":"000001",
		"checkItem":"血液生化",
		"result":"ALT 1200U/L",
		"sendPeople":"李小里",
		"sendTime":"2014-08-15"
	}
]};

$(function(){

	//设置content区域高
	$('.content').height( $(window).height() - $('.top-tools').height() );
	$("#info-tab").datagrid({
		data : _data,
		fit : true,
		fitColumns:true,
		singleSelect : true,
		columns : [ [ 
		{
			field : 'getTime',
			title : '收到时间',
			width : 100
		}, {
			field : 'bedId',
			title : '床号',
			width : 60
		}, {
			field : 'pName',
			title : '姓名',
			width : 100
		}, {
			field : 'hospitalNumber',
			title : '住院号',
			width : 100
		}, {
			field : 'checkItem',
			title : '检验检查项目',
			width : 100
		},{
			field : 'result',
			title : '结果',
			width : 200
		},{
			field : 'sendPeople',
			title : '发送人',
			width : 100
		},{
			field : 'sendTime',
			title : '发送时间',
			width : 100
		}
		 ] ]
	});

});