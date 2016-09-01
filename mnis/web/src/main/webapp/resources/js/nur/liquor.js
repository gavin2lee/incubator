

$(function(){
	var recordDate = new Date().format("yyyy-MM-dd");
	$("#startDate").val(recordDate);
	$("#endDate").val(recordDate);
	$('.content').height($(window).height() - $('.top-tools').height());
	$("#info-tab").datagrid({
		fitColumns:true,
		fit:true,
		columns:[[
			{field:'drugItems',align:'center',width:300,title:'医嘱内容'},
			{field:'prepareTime',align:'center',width:130,title:'备药时间'},
			{field:'prepareName',align:'center',width:80,title:'备药护士'},
			{field:'liquorTime',align:'center',width:130,title:'配液时间'},
			{field:'liquorName',align:'center',width:80,title:'配液护士'},
			{field:'execTime',align:'center',width:130,title:'执行时间'},
			{field:'execName',align:'center',width:80,title:'执行护士'}
		]]
	});	
	search();
});

function search(){
	var url = location.href;
	var patientId = url.substring(url.indexOf('=')+1,url.length);
	var time = $("#startDate").val();
	$.post(ay.contextPath+'/nur/documents/liquor?patientId='+patientId+'&queryTime='+time,function(data){
		var cacheData = data.data.list;
		var drugItems = null;
		for(var i=0;i<cacheData.length;i++){
			drugItems = cacheData[i].drugItems;
			var str   = '';
			var unit  = '';
			for(var j=0;j<drugItems.length;j++){
				if(j!=0){ str +="<br>"; }
				str   += drugItems[j].drugName;
				unit  += drugItems[j].drugUnit;
			}
			cacheData[i].drugItems = str;
			cacheData[i].drugUnit  = unit;
		}
		console.log(cacheData);
		$("#info-tab").datagrid({
			data:cacheData
		});
	});
	
}