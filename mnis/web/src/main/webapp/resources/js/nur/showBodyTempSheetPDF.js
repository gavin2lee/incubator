var editIndex = undefined,
	fields    = '',
	cacheData = null,
	masterRecordId = null,
	cooledStatus = [],
	isChecked = false,
	currentDate = null;
var LODOP;


$(function(){
	var recordDay = new Date().format("yyyy-MM-dd");
	$("#startDate").val(recordDay);
	if(!currentDate){ currentDate = recordDay; }
	if(document.body.childNodes[0].nodeType == 3){
		document.body.removeChild(document.body.childNodes[0]);	
	}
	$('.content').height( $(window).height() - $('.top-tools').height() );
	$('#LODOP_OB').height( $(window).height() - $('.top-tools').height() );
	$('#LODOP_EM').height( $(window).height() - $('.top-tools').height() );
});

Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}


//查看体温单
function showTempSheet(){
	var startDate = $("#startDate").val();
	$("#prevBtn").removeAttr('disabled');
	$("#nextBtn").removeAttr('disabled');
	$.get(ay.contextPath+'/nur/system/getConfig.do',function(config){
		var params = {}
        var temperatureChart = $.parseJSON(config.data.system.temperatureChart);
        params.temperatureChart = temperatureChart;
		$.post(ay.contextPath+'/nur/bodySign/getBodyTempSheet?id='+parent.peopleList+'&date='+startDate,{},function(data){
				//绘制数据
				if(!data.data){ 
					$("#showTempSheet").dialog('close');
					return ;
				}
				$("#showTempSheet").dialog('open');
				$("#LODOP_OB").css('visibility','visible');
				if(!LODOP){
					LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
				}
				params.currentWeek =  $("#currentWeek");
				DP(data,params,null);
		});
	});
}
function prevWeekSheetPdf(){
	var currWeek = $("#currentWeek").val();
	currWeek -= 1;
	$("#nextBtn").removeAttr('disabled');
	if(currWeek <= 1){
		$("#prevBtn").attr('disabled','disabled');
	}
	$("#currentWeek").val(currWeek);
	var prevWeek = new Date($("#startDate").val());
	prevWeekTime = prevWeek.getTime();
	prevWeekTime -= 86400000*7;
	prevWeek.setTime(prevWeekTime);
	prevWeek = prevWeek.format("yyyy-MM-dd");
	$("#startDate").val(prevWeek);
	//$("#LODOP_OB").css('visibility','hidden');
	showTempSheet();
}
function nextWeekSheetPdf(){
	var currWeek = parseInt($("#currentWeek").val());
	if(currWeek == 1){ 
		$("#prevBtn").removeAttr('disabled');
	}
	currWeek += 1;
	var currWeekDate = new Date($("#startDate").val()).getTime();
	if(currWeekDate+86400000*7 > new Date(currentDate).getTime()){ 
		currWeekDate = new Date(currentDate).getTime();
		$("#nextBtn").attr('disabled','disabled'); 
	}
	else{
		currWeekDate += 86400000*7;
		$("#currentWeek").val(currWeek);
		var tempCurrWeekDate = new Date();
		tempCurrWeekDate.setTime(currWeekDate);
		currWeekDate = tempCurrWeekDate.format("yyyy-MM-dd");
		$("#startDate").val(currWeekDate);
		showTempSheet();
	}	
}


Date.prototype.format = function(format) {
	var o = {
		"M+" : this.getMonth() + 1, // month
		"d+" : this.getDate(), // day
		"h+" : this.getHours(), // hour
		"m+" : this.getMinutes(), // minute
		"s+" : this.getSeconds(), // second
		"q+" : Math.floor((this.getMonth() + 3) / 3), // quarter
		"S" : this.getMilliseconds()
	// millisecond
	};
	if (/(y+)/.test(format))
		format = format.replace(RegExp.$1, (this.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(format))
			format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k]
					: ("00" + o[k]).substr(("" + o[k]).length));
	return format;
}

