/**
资源进度条
*/
EnvDeploy = (function(){
	var _option = {
			'url' : '/manager/deploy/env/data.do',
			'preid' : 'node_monitor_',
			'height' : '25',
			'width' : '200',
			'precolor' : '#5bc0de',
			'backcolor' : '#dadada'
	};
	var _template = [];
	_template.push('{$<div class="env_deploy_list">$}');
	_template.push('var _n = 98/data.list.length;');
	_template.push('for(var i=0;i<data.list.length;i++){');
	_template.push('		{$<div class="level" style="float:left;width:{%_n%}%;">');
	_template.push('				<div class="env_list_block">');
	_template.push('					<div class="title">');
	_template.push('						<span>{%data.list[i].name%}</span>');
	_template.push('					</div>');
	_template.push('					<div class="content">');
	_template.push('						<ul class="env_ul">$}');
	_template.push('			for(var j=0;j<data.list[i].data.length;j++){');
	_template.push('							{$<li>');
	_template.push('								<div class="env_li_blcok">');
	_template.push('									<div>');
	_template.push('									<label>项目名称</label><p><a href="{%webapp%}/project/overview/index.do?projectId={%data.list[i].data[j].project_id%}">{%data.list[i].data[j].project_name%}</a></p>');
	_template.push('									</div>');
	_template.push('									<div>');
	_template.push('									<label>来源</label><p>{%data.list[i].data[j].project_type%}</p>');
	_template.push('									</div>');
	_template.push('									<div>');
	_template.push('									<label>部署名称</label><p><a href="{%webapp%}/project/env/index.do?projectId={%data.list[i].data[j].project_id%}&envId={%data.list[i].data[j].env_id%}">{%data.list[i].data[j].deploy_name%}</a></p>');
	_template.push('									</div>');
	_template.push('								</div>');
	_template.push('							</li>$}');
	_template.push('						}');
	_template.push('						{$</ul>');
	_template.push('					</div>');
	_template.push('				</div>');
	_template.push('		</div>$}');
	_template.push('		}');
	_template.push('{$</div>$}');
	var render = function(msg){
		if(msg.list==null || msg.list.length==0){
			return ;
		}
		$('#'+_option.id).html((new Tmpl(_template.join(''))).render({'data' : msg,'webapp' : _option.webapp}));
	};
	var getData = function(){
		if(_option.data==null){
			_option.data = {};
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
//			setTimeout(function(){
//				getData();
//			},1000);
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