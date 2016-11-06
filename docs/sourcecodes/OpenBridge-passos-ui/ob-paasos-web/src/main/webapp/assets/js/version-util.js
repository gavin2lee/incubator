var FileIssues = {}
//文件issue详情弹出框
FileIssues.sourcesShow = function(object,callback){
	if(!object){
		throw "入参异常，请检查入参是否正确!!";
	}
	var _key = object.key;
	var _uuid = object.uuid;
	if(!_uuid){
		throw "未找到资源uuid值！！";
	}
	if(!_key){
		throw "未找到资源KEY值！！";
	}
	var _title = object.title ? object.title : "文件资源问题详情";
	
	var _data = {
			componentKeys : _key,
			from : 1,
			projectKey : object.projectKey,
			to : 100,
			componentUuids : _uuid,
			uuid : _uuid,
			f : "component,componentId,project,subProject,rule,status,resolution,author,reporter,assignee,debt,line,message,severity,actionPlan,creationDate,updateDate,closeDate,tags,comments,attr,actions,transitions,actionPlanName",
//			additionalFields : "_all",
			s : "FILE_LINE",
			ps : 3000
	}
	var _url= "showSourcesInfo.do?";
	for(var item in _data){
		_url = _url + item + "=" + _data[item] + "&";
	}
	
	var _width = object.width ? object.width : $(window).width() - 50;
	var _height = object.height ? object.height : $(window).height() - 50;
	var _callback = callback ? callback : null;
	common.dialogIframe(_title,_url,_width,_height,_callback);
}

function fillDataToMessageDiv(data){
	var url = "getRulesInfo.do";
	var json = findIssuesInfoByAjax("GET",url,data,"json");
	var jsonObject = JSON.parse(json["resultJson"]);
	var rules = jsonObject["rules"][0];
	$("#name").html("<i class=\"icon-workspace-doc\"></i>" + rules["name"]);
	$("#severityInfo").html("<i class=\"snoarico " + getIconBySeriry(rules["severity"]) + "\"></i>" + traslateToCN(rules["severity"],"severity"));
	$("#sysTags").html(rules["sysTags"].join(","));
	$("#createdAt").html("Available Since " + formatDateInfo(rules["createdAt"]));
	$("#key").html(rules["key"]);
	$("#htmlDesc").html(rules["htmlDesc"]);
	$("#charName").html(rules["defaultDebtChar"] + " > " + rules["defaultDebtSubChar"]);
	$("#debtRemFnOffset").html(rules["debtRemFnOffset"]);
	$("#workspace-viewer").css("display","inline");
}

//根据缺陷类型的不同返回不同的图标
function getSeverityIcoType(item){
	if(item.severity == "MAJOR"){
		return "major_ico";
	}else if(item.severity == "CRITICAL"){
		return "blocker_ico";
	}else if(item.severity == "INFO"){
		return "info_ico";
	}else if(item.severity == "BLOCKER"){
		return "blocker_ico";
	}else if(item.severity == "MINOR"){
		return "minor_ico";
	}
}

//根据状态类型的不同返回不同的图标
function getStatusIcoType(item){
	if(item.status == "OPEN"){
		return "open_ico";
	}else if(item.status == "CLOSED"){
		return "closed_ico";
	}else if(item.status == "RESOLVED"){
		return "resolved_ico";
	}else if(item.status == "REOPENED"){
		return "reopened_ico";
	}else if(item.status == "CONFIRMED"){
		return "confirmed_ico";
	}
}

function formatDateInfo(dateInfo){
	var date = new Date(dateInfo);
	var Y = date.getFullYear() + '年';
	var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '月';
	var D = date.getDate() + '日';
	var h = date.getHours() + '时';
	var m = date.getMinutes() + '分';
	return Y + M + D + h + m;
	
}

function findIssuesInfoByAjax(type, url, data, dataType) {
	var result = "";
	var loadD = common.loading();
	$.ajax({
		async : false,
		type : type,
		/* url: "${WEB_APP_PATH}/app/version/getMetricInfo.do", */
		url : url,
		/* data: {projectKey : projectKey,path:path}, */
		data : data,
		dataType : dataType,
		complete : function(xhr){
			loadD.close();
		},
		success : function(json) {
			result = json;
		}
	});
	return result;
}

function traslateToCN(value , type){
	if("severity" == type){
		if("BLOCKER" == value){
			return "阻塞";
		}else if("MINOR" == value){
			return "一般";
		}else if("CRITICAL" == value){
			return "危险";
		}else if("INFO" == value){
			return "提示";
		}else if("MAJOR" == value){
			return "严重";
		}
	}else if("status" == type){
		if("OPEN" == value){
			return "开启";
		}else if("RESOLVED" == value){
			return "重新解决";
		}else if("REOPENED" == value){
			return "重新开启";
		}else if("CLOSED" == value){
			return "关闭";
		}else if("CONFIRMED" == value){
			return "已确认";
		}
	}else if("status" == type){
		if("OPEN" == value){
			return "开启";
		}else if("RESOLVED" == value){
			return "重新解决";
		}else if("REOPENED" == value){
			return "重新开启";
		}else if("CLOSED" == value){
			return "关闭";
		}else if("CONFIRMED" == value){
			return "已确认";
		}
	}
}

function showFileIssueInfo(dom){
	var data = {
			title : "文件问题详情",
			key : $(dom).attr("key"),
			uuid : $(dom).attr("uuid"),
			projectKey : projectKey,
			width: $(window).width(),
			height : $(window).height() + 100
	};
	FileIssues.sourcesShow(data);
}

function getIconBySeriry(type){
	if("BLOCKER" == type){
		return "blocker_ico";
	}else if("CRITICAL" == type){
		return "critical_ico";
	}else if("MAJOR" == type){
		return "major_ico";
	}else if("MINOR" == type){
		return "minor_ico";
	}else if("INFO" == type){
		return "info_ico";
	}
}

function getPeriodDesc(json){
	if(!json){
		return "已是最新版本";
	}
	var type = json.desc;
	if(type == "days"){
		return "新增变化: 最近"+json.p+"天";
	}else if(type == "previous version"){
		return "新增变化: 从上个版本 " + new Date(json.date).format("yyyy年M月d日 hh时mm分");
	}
}

function showMeasuresDialog(title,metric , projectKey){
	var url = "getMeasuresStructure.do?projectKey=" + projectKey + "&metrics="+metric;
	common.dialogIframe(title,url,1266,600,function(){
	});
};

