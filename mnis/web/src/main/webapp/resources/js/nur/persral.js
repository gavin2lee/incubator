$(function(){
	var recordDate = new Date().format("yyyy-MM-dd");
	$("#startDate").val(recordDate);
	$("#endDate").val(recordDate);
	$('.content').height($(window).height() - $('.top-tools').height());
	$("#info-tab").datagrid({
		fitColumns:true,
		fit:true,
		columns:[[
			{field:'drugItems',width:150,align:'center',title:'医嘱内容'},
			{field:'dosage',width:60,align:'center',title:'剂量'},
			{field:'usage',width:60,align:'center',title:'用法'},
			{field:'freq',width:60,align:'center',title:'频次'},
			{field:'createTime',width:150,align:'center',title:'开嘱时间'},
			{field:'createName',width:150,align:'center',title:'开嘱医生'},
			{field:'execTime',width:150,align:'center',title:'执行时间'},
			{field:'execName',width:150,align:'center',title:'执行护士'}
		]]
	});
	search();
});

function search(){
	var url = location.href;
	var patientId = url.substring(url.indexOf('=')+1,url.length);
	var time = $("#startDate").val();
	$.post(ay.contextPath+'/nur/documents/persral?patientId='+patientId+'&queryTime='+time,function(data){
		console.log(data);
		var cacheData = data.data.list;
		var drugItems = null;
		var str    = '',
		    dosage = '';
		for(var i=0;i<cacheData.length;i++){
			var usage  = null,
		    	freq   = null;
		    drugItems = '',
		    dosage    = '';
			items = cacheData[i].drugItems;
			for(var j=0;j<items.length;j++){
				if(j!=0){ 
					drugItems    +="<br>";
					dosage +="<br>";
				}
				drugItems    += items[j].drugName;
				dosage += items[j].drugDosage+' '+items[j].drugUnit;
				if(!usage){ usage = items[j].drugUsage; }
				if(!freq){ freq   = items[j].drugFreq; }
			}
			cacheData[i].drugItems = drugItems;
			cacheData[i].dosage    = dosage;
			cacheData[i].usage     = usage;
			cacheData[i].freq      = freq;
		}
		console.log(cacheData);
		$("#info-tab").datagrid({
			data:cacheData
		});
	});
	
}