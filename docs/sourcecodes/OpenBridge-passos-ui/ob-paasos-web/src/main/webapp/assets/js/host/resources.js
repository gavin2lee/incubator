/**
资源进度条
*/
ResourceProgress = (function(){
	var _option = {
			'url' : '/project/monitor/last/data.do',
			'preid' : 'node_monitor_',
			'height' : '12',
			'width' : '160',
			'precolor' : '#5bc0de',
			'backcolor' : '#dadada'
	};
	var _ref = null;
	var render = function(msg){
		if(msg.list==null || msg.list.length==0){
			return ;
		}
		for(var i=0;i<msg.list.length;i++){
			var counter = msg.list[i].counter;
			var endpoint = msg.list[i].endpoint;
			var value = msg.list[i].value.value;
			var _id = _ref[endpoint+'_'+counter];
			var max = $('#'+_id).data('max');
	
			value = parseFloat(value);
			if(counter.indexOf('mem.')>-1){
				value = (value/1024)/1024;
				var _width = (parseFloat(_option.width)/parseFloat(max))*value;
				$('#'+_id).find('.render').css({'width' : _width});
				$('#'+_id).nextAll('.title').html('<span class="used">'+Math.round(value*100)/100+'</span> / <span class="total">'+max+'</span>');
			}
			else if(counter.indexOf('cpu.')>-1){
				var _ov = value;
				value = value/100 * parseFloat(_option.width);
				$('#'+_id).find('.render').css({'width' : value});
				$('#'+_id).nextAll('.title').html('<span class="used">'+Math.round((_ov/100*parseFloat(max))*100)/100+'</span> / <span class="total">'+max+'</span>');
			}
		}
	};
	var getData = function(){
		if(_option.data==null){
			_option.data = {};
		}
		if(_option.data['endpointCounters']==null){
			_option.data['endpointCounters'] = [];
		}
		if(_ref==null){
			_ref = {};
			$('#'+_option.parentId).find('.resourceProgress').each(function(domIndex,domEle){
				var endpoint = $(domEle).attr('endpoint');
				var counter = $(domEle).attr('counter');
				var max = $(domEle).attr('max');
				var _info = {
						'counter' : counter,
						'endpoint' : endpoint
				};
				_option.data['endpointCounters'].push(_info);
				var _id = _option.preid + domIndex;
				_ref[endpoint+'_'+counter] = _id;
				$(domEle).append('<div style="display:inline-block;;height:'+_option.height+'px;width:'+_option.width+'px;position:relative; margin: 10px 0;"><div style="border-radius:10px;position:absolute;left:0;top:10px;height:'+_option.height+'px;width:'+_option.width+'px;background:'+_option.backcolor+';"' +
						'id="'+_id+'"><div style="border-radius:10px;position:absolute;height:100%;background:'+_option.precolor+';" class="render"></div></div><span class="title" '+
						' style="position:absolute;height:100%;top:-11px;left:0p"; ></span></div>');
				$('#'+_id).data('max',max);
			});
		}
		
		
		var def = $.ajax({
			url : _option.webapp+_option.url,
			data : _option.data,
			type : 'post',
			dataType : 'json',
			cache : false
		});
		def['done'](function(msg){
			if(msg==null || msg.code!=0 ){
				//error
				if(_option.error != null){
					_option.error.call(this,msg);	
				}
				return ;
			}
			render(msg.data);
			setTimeout(function(){
				getData();
			},4000);
		});
	};
	var init = function(__options){
		_option = $.extend(true,_option,__options);
		getData();
	};
	
	return {
		init : function(options){
			init(options);
		}
	};
})({});