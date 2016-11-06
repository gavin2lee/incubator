function viewLog(id){
	showLogInDialog(WEB_APP_PATH+"/log/build.do",{buildId:id});
}
function showLogInDialog(url,data){
	var con = "<div style='padding:10px;line-height:15px;word-wrap: break-word;'><pre id='dialogLog'></pre>" +
			'<div id="spinner"><img src="'+WEB_APP_PATH+'/assets/images/spinner.gif" alt="..."></div></div>';
	
	var closeCallback = function(){
		if(typeof __logSetTimeoutId !== 'undefined' && __logSetTimeoutId){
			clearTimeout(__logSetTimeoutId);
		}
		if(typeof loadItems === 'function'){
			//loadItems(pageNo,pageSize);
		}
	}
	var opt = {
			title:'日志',
		    type: 1,
		    area: [$(window).width()*0.8+'px', $(window).height()*0.8+'px'],
		    fix: false, //不固定
		    maxmin: false,
		    btn:['关闭'],
		    content: con,
		    btn1:function(){ 
		    	closeCallback();
			},
			cancel : function(index){
				closeCallback();
			}
		};
	var layerObj = top.common.dialog(opt);
	
	var loadLog = function(){
		$.get(url,data,function(res){
			var json = res.data;
			var log = json.log;
			if(!log && json.end && !$("#dialogLog").html())	log="没有日志信息。";
			if(log){
				$("#dialogLog").append("<pre>"+log+"</pre>");
				var container=$("#dialogLog").parent().parent();
				container.scrollTop(container[0].scrollHeight);
			}
			if(json.end === false){
				data.startLine = json.lineNumber;
				__logSetTimeoutId = setTimeout(loadLog,1000);
			}else{
				$("#spinner").hide();
			}
		});
	};
	loadLog();
}
 //增加端口
 function addPort(port){
 	if(!port){
 		port = {
     			portNo : '',
     			portType : '',
     			portKey : '',
     			portDesc : ''
     		};
 	}
 	$("#ports").append('<tr>										   '+
			    '	<td style="width:100px;"><select style="width:80px;" class="u-ipt" value="'+port.portType+'"><option>HTTP</option><option>TCP</option><option>UDP</option></select></td>				      '+
				'	<td style="width:100px;"><input type="text" style="width:80px;" class="u-ipt" value="'+port.portNo+'"/></td>				      '+
				'	<td><input type="text" name="portKey" style="width:260px;" class="u-ipt2" value="'+(port.portKey || '')+'"/></td>				     '+
				'	<td><input type="text" style="width:260px;" class="u-ipt2" value="'+port.portDesc+'"/></td>				     '+
				'	<td><a class="btn btn-default btn-yellow btn-close btn-sm btn-del" href="javascript:void(0);">    '+
				'	<em class="f14"> × </em></a> </td>					      '+
				'</tr>										  ');
 	$("#ports select:last").val(port.portType);
 }
 function getPorts(){
	//获取端口信息
 	var ports = [];
 	var keys = {},duplicate=false;
 	$("#ports tr:gt(0)").each(function(){
 		var inputs = $(this).find("input");
 		ports.push({
 			portNo : inputs[0].value,
 			portType : $(this).find("select").val(),
 			portKey : inputs[1].value,
 			portDesc : inputs[2].value
 		})
 		if(keys[inputs[1].value]===1){
 			duplicate=true;
 			return false;
 		}else{
 			keys[inputs[1].value]=1;
 		}
 	})
 	if(duplicate==true){
 		common.alert("端口关键字不能重复！",2);
 		throw "端口关键字不能重复！";
 	}
// 	return JSON.stringify(ports);
 	return ports;
 }
 function initFileUpload(fileData,id){
	initFile($("#buildFileP"),fileData,['.zip,.tar.gz'],"上传文件只支持ZIP格式",true);
	if(!id)	$("#buildFileP a.edit").click();
	initImage($("#appLogo"),$("#appLogo").attr("path"));
 }
 $(function(){
	 $(".tab-handler").click(function(){
 		if($(this).attr("index")==1){
 			$("#tab-first").show();
 			$("#tab-second").hide();
 		}else if($(this).attr("index")==2){
 			if($("#createForm").length>0 && !$("#createForm").valid())	return false;
 			$("#tab-first").hide();
 			$("#tab-second").show();
 		}
 		$(".tab-handler").removeClass("active");
 		$(this).addClass("active");
 	})
 	
 	$(".btn-add").on('click',function(){
		addPort();
	});
	$(".btn-add-res").on('click',function(){
		addDependencyResource();
	});
	$(".btn-del").live('click',function(){
		$(this).parent().parent().remove();
	});
	$("#dependencyResources .btn-del").live('click',function(){
		$("#dependencyResources tr:gt(0)").each(function(i,v){
			$(this).find("td:first").html(i+1);
		});
	});
	//------------以上为加减号事件绑定
 })