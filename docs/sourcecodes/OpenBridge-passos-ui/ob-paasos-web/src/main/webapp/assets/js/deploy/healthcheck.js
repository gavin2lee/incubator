var _unique = 1;
var healthCheck = (function(){
	var _option = {
			type : 'editor',
			data : {
				healthType : [{
					'name' : 'LivenessProbe',
					'code' : 'LivenessProbe'
				},{
					'name' : 'ReadinessProbe',
					'code' : 'ReadinessProbe'
				}],
				handlerType : [{
					'name' : '请选择',
					'code' : ''
 				},{
					'name' : 'httpGet',
					'code' : 'httpGet'
 				},{
					'name' : 'tcpSocket',
					'code' : 'tcpSocket'
 				}] 
			}
	};
	var a_level1 = [];
	a_level1.push('{$ <tr class="type-level {%pageType%}">$}');
	a_level1.push(' var m= _unique++;var m2=_unique++;');
	a_level1.push('   if(pageType=="editor"){if(pageData.event==null)pageData.event=[];var _m=["select_"+m2,pageData.type,pageData];pageData.event.push(_m);');
	a_level1.push(' 		{$<td><select id="select_{%m2%}" class="type-level row-td" name="type" style="width:80px;"> $}');
	a_level1.push(' 		 for(var i=0;i<healthType.length;i++){');
	a_level1.push(' 		 	{$<option value="{%healthType[i].code%}">{%healthType[i].name%}</option>$}');
	a_level1.push(' 		 }');
	a_level1.push(' 		{$	</select> </td>');
	a_level1.push(' 		<td>');
	a_level1.push(' 			<input value="{%pageData.initialDelaySeconds%}" class="type-level row-td" style="width:80px;" type="text" name="initialDelaySeconds">');
	a_level1.push(' 		</td>');
	a_level1.push(' 		<td>');
	a_level1.push(' 			<input value="{%pageData.timeoutSeconds%}" class="type-level row-td" style="width:80px;" type="text" name="timeoutSeconds">');
	a_level1.push(' 		</td>$}');
	a_level1.push('   }else{');
	a_level1.push(' 		{$	<td>{%pageData.type%}</td><td><div style="width:80px;">{%pageData.initialDelaySeconds%}</div></td><td><div style="width:80px;">{%pageData.timeoutSeconds%}</div></td>$}');
	a_level1.push('   }');
	a_level1.push(' 		{$<td>');
	a_level1.push(' 			<div id="healthhandler_{%m%}"><table class="table_creat"><tr style="display:none;"><th style="width: 90px;">处理方法</th><th style="width:220px;">处理参数</th><th class="{%pageType%}-td" style="width: 38px;"><a template-param="level2" template-call="healthCheck.getTemplate" template="healthhandler_{%m%}"    class="addRow btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">+</em></a></th></tr>');
	a_level1.push(' 		</table></div>');
	a_level1.push(' 		</td>');
	a_level1.push(' 		<td class="{%pageType%}-td">');
	a_level1.push(' 			<a class="deleteRow parentTr btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>');
	a_level1.push(' 		</td>');
	a_level1.push(' </tr> $}');
	var s_level1 = a_level1.join('');
	
	var a_level2 = [];
	a_level2.push('{$ <tr class="handler-level">$}');
	a_level2.push(' var m= _unique++;var m5=_unique++;');
	a_level2.push('   if(pageType=="editor"){if(pageData.event==null)pageData.event=[];var _m=["select_"+m5,pageData.type,pageData];pageData.event.push(_m);');
	a_level2.push(' 		{$<td><select id="select_{%m5%}" name="type" class="handler handler-level row-td" style="width:80px;" target="healthsetting_{%m%}"> $}');
	a_level2.push(' 		 for(var i=0;i<handlerType.length;i++){');
	a_level2.push(' 		 	{$<option value="{%handlerType[i].code%}">{%handlerType[i].name%}</option>$}');
	a_level2.push(' 		 }');
	a_level2.push(' 	{$	</select> </td>');
	a_level2.push(' 		<td id="healthsetting_{%m%}">');
	a_level2.push(' 			');
	a_level2.push(' 		</td>$}');
	a_level2.push('   }else{ var m1 = (new Tmpl(getTmpTemplate(pageData.type))).render({"pageData":pageData,"pageType" : pageType});');
	a_level2.push(' 			{$<td>{%pageData.type%}</td><td>{%m1%}</td>$}');
	a_level2.push('   }');
	a_level2.push(' 		{$<td class="{%pageType%}-td">');
	a_level2.push(' 			<a class="deleteRow parentTr btn btn-default btn-yellow btn-sm" href="javascript:void(0);"><em class="f14">-</em></a>');
	a_level2.push(' 		</td>');
	a_level2.push(' </tr> $}');
	
	var tcpSocket_template = [];
	var httpGet_template = [];
	tcpSocket_template.push('if(pageType=="editor"){');
	tcpSocket_template.push('		{$<div><label style="width:90px;display:inline-block;">port</label><input class="row-td handler-level" style="width:120px;" type="text" name="port" value="{%pageData.port%}"></div>$}');
	tcpSocket_template.push(' }else{ ');
	tcpSocket_template.push('		{$<div><label style="width:90px;display:inline-block;">port</label><span>{%pageData.port%}</span></div>$}');
	tcpSocket_template.push(' }');
	_option.tcpSocket = tcpSocket_template.join('');
	
	httpGet_template.push('if(pageType=="editor"){');
	httpGet_template.push('{$<div style="display:none;"><label style="width:90px;display:inline-block;">hostIp</label><input class="row-td handler-level" style="width:120px;" type="text" name="host" value="{%pageData.host%}"></div>');
	httpGet_template.push('<div style="margin-top:5px;"><label style="width:90px;display:inline-block;">path</label><input style="width:120px;" class="row-td handler-level" type="text" name="path" value="{%pageData.path%}"></div>');
	httpGet_template.push('<div style="margin-top:5px;"><label  style="width:90px;display:inline-block;">port</label><input style="width:120px;" class="row-td handler-level" type="text" name="port" value="{%pageData.port%}"></div>');
	httpGet_template.push('<div style="margin-top:5px;"><label style="width:90px;display:inline-block;">scheme</label><input style="width:120px;" class="row-td handler-level"  type="text" name="scheme" value="{%pageData.scheme%}"></div>$}');
	httpGet_template.push(' }else{ ');
	httpGet_template.push('{$<div style="display:none;"><label style="width:90px;display:inline-block;">hostIp</label><span>{%pageData.host%}</span></div>');
	httpGet_template.push('<div style="margin-top:5px;"><label style="width:90px;display:inline-block;">path</label><span>{%pageData.path%}</span></div>');
	httpGet_template.push('<div style="margin-top:5px;"><label  style="width:90px;display:inline-block;">port</label><span>{%pageData.port%}</span></div>');
	httpGet_template.push('<div style="margin-top:5px;display:none;"><label style="width:90px;display:inline-block;">scheme</label><span>{%pageData.scheme%}</span></div>$}');
	httpGet_template.push(' }');
	_option.httpGet = httpGet_template.join('');
	
	var s_level2 = a_level2.join('');
	function init(__option){
		_option = $.extend(true,_option,__option==null?{}:__option);
		$('#healthcheck').delegate('.handler','change',null,function(){
			var initData = {};
			if(arguments.length>1){
				initData = arguments[1];
			}
			var v = $(this).val();
			var target = $(this).attr('target');
			if(v==null || v==''){
				$('#'+target).empty();
			}
			else{
				$('#'+target).html((new Tmpl(_option[v])).render({pageType:_option.type,pageData : initData}));
			}
		});
		if(_option.initData!=null){
			for(var i=0;i<_option.initData.length;i++){
				var datainfo = _option.initData[i];
				$('#healthcheck').find('a.addRow:first').trigger('click',[datainfo==null?{}:datainfo,_option.type]);
				if(datainfo.event!=null){
					for(var jj=0;jj<datainfo.event.length;jj++){
						var id = datainfo.event[jj][0];
						var value = datainfo.event[jj][1];
						var data = datainfo.event[jj][2];
						$('#'+id).val(value);
						$('#'+id).trigger('change',[data]);
					}
				}
				if(datainfo.handlers!=null){
					for(var j=0;j<datainfo.handlers.length;j++){
						var handlerInfo = datainfo.handlers[j];
						$('#healthcheck').find('a.addRow:last').trigger('click',[handlerInfo==null?{}:handlerInfo,_option.type]);
						if(handlerInfo.event!=null){
							for(var jj=0;jj<handlerInfo.event.length;jj++){
								var id = handlerInfo.event[jj][0];
								var value = handlerInfo.event[jj][1];
								var data = handlerInfo.event[jj][2];
								$('#'+id).val(value);
								$('#'+id).trigger('change',[data]);
							}
						}
					}
				}
			}
		}
	}
	function getTemplate(){
		var _type = arguments[0][0];
		
		var pageData = {};
		var pageType = 'editor';
		if(arguments[0].length>1){
			pageData = arguments[0][1];
			pageType = arguments[0][2];
		}
		var data = _option.data;
		data.pageData = pageData;
		data.pageType = pageType;
		if(_type=='level1'){
			return (new Tmpl(s_level1)).render(data);
		}
		if(_type=='level2'){
			return (new Tmpl(s_level2)).render(data);
		}
	}
	return {
		init : function(option){
			init(option);
		},
		getTemplate : function(type){
			return getTemplate(type);
		},
		getTmpTemplate : function(code){
			return _option[code];
		},
		getValues : function(){
			var v = [];
			$('#healthcheck').find('tr.type-level').each(function(domIndex,domEle){
				var r = {};
				$(domEle).find(':input.type-level').each(function(domIndex1,domEle1){
					var _n = $(domEle1).attr('name');
					if(_n==null || _n==''){
						return true;
					}
					var _v = $(domEle1).val();
					if(_v==null || _v==''){
						return true;
					}
					r[_n] = _v;
				});
				
				
				var handlers = [];
				$(domEle).find('tr.handler-level').each(function(domIndex1,domEle1){
					var r1 = {};
					$(domEle1).find(':input.handler-level').each(function(domIndex2,domEle2){
						var _n = $(domEle2).attr('name');
						if(_n==null || _n==''){
							return true;
						}
						var _v = $(domEle2).val();
						if(_v==null || _v==''){
							return true;
						}
						r1[_n] = _v;
					});
					handlers.push(r1);
				});
				r.handlers = handlers;
				v.push(r);
			});
			
			var m = {};
			for(var i=0;i<v.length;i++){
				var checkType = v[i].type;
				if(m[checkType]==null){
					m[checkType] = {};
				}
				else{
					return {
						'msg' : '检查类型不能重复'
					};
				}
				if(v[i].handlers!=null){
					for(var j=0;j<v[i].handlers.length;j++){
						var handlerType = v[i].handlers[j].type;
						if(m[checkType][handlerType]==null){
							m[checkType][handlerType] = {};
						}
						else{
							return {
								'msg' : '同一个检查类型，处理方法不能相同'
							};
						}
					}
				}
			}
			return v;
		}
	};
})();
function getTmpTemplate(code){
	return healthCheck.getTmpTemplate(code);
}

