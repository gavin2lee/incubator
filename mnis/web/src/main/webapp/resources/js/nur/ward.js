$(function(){
	var recordDate = new Date().format("yyyy-MM-dd");
	$("#startDate").val(recordDate);
	$("#endDate").val(recordDate);
	$('.content').height($(window).height() - $('.top-tools').height());
	$("#info-tab").datagrid({
		fitColumns:true,
		fit:true,
		columns:[[
			{field:'patrolDate',width:150,align:'center',title:'巡视时间'},
			{field:'nurseName',width:150,align:'center',title:'巡视护士'}
		]]
	});
	search();
});

function search(){
	var url = location.href;
	var patientId = url.substring(url.indexOf('=')+1,url.length);
	var time = $("#startDate").val();
	$.post(ay.contextPath+'/nur/documents/ward?patientId='+patientId+'&queryTime='+time,function(data){
		console.log(data);
		var cacheData = data.data.list;
		$("#info-tab").datagrid({
			data:cacheData
		});
	});
	
}
