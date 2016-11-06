 
var common = {}; 
common.forward = function(/*string*/_url,/*object*/_obj){
	(!_obj ? document : _obj).location.href = _url;
}

var layerObj = function(_index){
	this.index = _index;
} 
layerObj.prototype.close = function(){
	layer.close(this.index);
}
layerObj.prototype.getIFrame = function(){
	return document.getElementById("layui-layer-iframe"+this.index+"");
} 
common.goto = function(/*string*/_url,/*object*/_obj){
	(!_obj ? document : _obj).location.href = _url;
}
common.forward = function(/*string*/_url,/*object*/_obj){
	(!_obj ? document : _obj).location.href = _url;
}
/**
 * icon 类型说明
 * 0:感叹号,信息
 * 1:正确的勾勾符号，正确
 * 2:错误的叉叉符号，错误
 * 3:询问的问好
 * 4:灰色的锁符号
 * 5:哭脸
 * 6:笑脸
 */
common.alert = function(title,_icon,_cb){
	if(typeof(_icon)==="function"){
		_cb = _icon;
	}
	if(_cb == null){
		_cb = function(){};
	}
	if(_icon == null){
		_icon = 0;
	} 
	return layer.alert(title,{icon:_icon,title: false,btn: [],cancel:_cb});
}
common.tooltip = function(/*提示信息*/_msg,/**/_dom,/**/_param){
	layer.tips(_msg, _dom, _param);
}
common.tips = function(/*提示信息*/_msg,/**/_icon,/**/_cb,/**/_timer){
	if(_timer == null){
		_timer = 2000;
	}
	if(_cb == null){
		_cb = function(){};
	}
	if(_icon == null){
		_icon = 0;
	}
	var x = top.layer.msg(_msg, {icon: _icon,time: _timer,shade:0.3,closeBtn:true},_cb);
	return new layerObj(x);
}
common.confirm = function(title,cb){
	var x = layer.confirm(title, {
		icon:3,
		title: false,
		closeBtn:false,
	    btn: ['确定','取消'],
	    btn1:function(){ 
		    cb(true);
		},
		btn2:function(){ 
		    cb(false);
		}
	});
	return new layerObj(x);
}
common.loading = function(message,cb){
	var x = layer.load(0, {title:message,shade: [0.5,'#fff']});
	return new layerObj(x);
} 
common.ajaxLoading = function(wrapper,text){
	if(typeof text === 'undefined')		text = '正在加载中...';
	$(wrapper).html("<div style='text-align: center;line-height: 30px;font-size: 14px;'>"+text+"</div>");
}
common.dialog = function(options){
	var xId = layer.open(options);
	return new layerObj(xId);
}
common.dialogDom = function(_title,_dom,w,h,buttons,btnCb){
	var opt = {
		title:_title,
	    type: 1,
	    area: [w+'px', h+'px'],
	    fix: false, //不固定
	    maxmin: false,
	    btn:buttons,
	    content: _dom
	};
	$.extend(opt,btnCb);
	return top.common.dialog(opt);
}
common.dialogHtml = function(_title,_html,w,h,buttons,btnCb){
	var opt = {
		title:_title,
	    type: 1,
	    area: [w+'px', h+'px'],
	    fix: false, //不固定
	    maxmin: false,
	    btn:buttons,
	    content: _html
	};
	$.extend(opt,btnCb);
	return top.common.dialog(opt);
}
common.dialogIframe = function(_title,_url,w,h,cb,buttons,btnCb){
	var opt = {
		title:_title,
	    type: 2,
	    area: [w+'px', h+'px'],
	    fix: false, //不固定
	    maxmin: false,
	    btn:buttons,
	    content: _url
	};
	$.extend(opt,btnCb);
	var dialog = top.common.dialog(opt);
	top.document.getElementById("layui-layer-iframe"+dialog.index+"").callback = cb;
	top.document.getElementById("layui-layer-iframe"+dialog.index+"").dialog = dialog;
	return dialog;
} 
common.getHashParam=function(name){
	if(location.hash){
		var hash = location.hash.split("#")[1];
		var params = hash.split("&");
		var map = {};
		for(var i=0;i<params.length;i++){
			var foo = params[i].split("=");
			map[foo[0]] = foo[1];
		}
		if(typeof name ==='undefined'){
			return map;
		}
		return map[name];
	}
	return '';
}
common.setHashParam=function(key,value){
	location.hash = common.replaceHashParam(location.hash,key,value);
}
common.replaceHashParam=function(str,key,value){
	if(str){
		var hash = str.split("#")[1];
		var params = hash.split("&");
		var map = {};
		for(var i=0;i<params.length;i++){
			var foo = params[i].split("=");
			if(key == foo[0]){
				str = hash.replace(params[i],key+"="+value);
				return "#"+str;
			}
		}
		str = hash+"&"+key+"="+value;
	}else{
		str = key+"="+value;
	}
	return "#"+str;
}
common.setUrlParam=function(key,value){
	var search = location.search.split("?")[1];
	var params = search.split("&");
	var map = {};
	for(var i=0;i<params.length;i++){
		var foo = params[i].split("=");
		if(key == foo[0]){
			location.search = search.replace(params[i],key+"="+value);
			return;
		}
	}
	location.search += "&"+key+"="+value;
}
common.getUrlParam=function(name){
	if(location.search){
		var search = location.search.split("?")[1];
		var params = search.split("&");
		var map = {};
		for(var i=0;i<params.length;i++){
			var foo = params[i].split("=");
			map[foo[0]] = foo[1];
		}
		return map[name];
	}
	return '';
}