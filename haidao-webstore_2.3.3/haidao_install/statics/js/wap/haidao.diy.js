/*
 * Base		基础通用
 * Panel	diy模块展示容器
 * diy		diy操作
 * opts		diy右侧属性操作面板
 */

var Base = {
	decode: function(code){
		return eval("("+$.base64.decode(code)+")");
	},
	encode: function(code){
		code = JSON.stringify(code);//IE8以下存在不兼容问题
		return $.base64.encode(code);
	},
	newFile: function(type,name,url){
		var $this = this;
		var _head = document.getElementsByTagName('HEAD').item(0);
		//加载模块自定义JS
		if($("#js_"+type).length<1 && url && type=="js"){
			var _script= document.createElement("script");
			_script.type = "text/javascript";
			_script.src = url;
			_script.id = "js_"+type;
			_head.appendChild( _script);
		}
		//加载模块自定义css
		if($("#css_"+type).length<1 && url && type=="css"){
			var _css= document.createElement("link");
			_css.type = "text/css";
			_css.rel = "stylesheet";
			_css.href = url;
			_css.id = "css_"+type;
			_head.appendChild( _css);
		}
	},
	getFiled: function(html,model){
		var field = $('<div class="app-field">');
		field.append('<div class="demo">'+html+'</div>');
		if(model != "global"){
			if(model == 'content'){
				field.append('<div class="shade"></div>');
			}else{
				field.append('<div class="actions"><div class="actions-wrap"><span class="action edit">编辑</span><span class="action delete">删除</span></div></div>');
			}
		}
		return field;
	},
	getRadio: function(v,c){
		var $v = v;
		var $c = c;
		var lists = '';
		for(var i=0;i<$v.value.length;i++){
			if($v.number){
				lists += '<label class="radio list"><input type="radio" name="'+$v.name+'" value="'+ $v.value[i] +'" '+($v.value[i]==$c?'checked="checked" ':'')+'/>'+$v.value[i]+'</label>';
			}else{
				lists += '<label class="radio list"><input type="radio" name="'+$v.name+'" value="'+ i +'" '+(i==$c?'checked="checked" ':'')+'/>'+$v.value[i]+'</label>';
			}
		}
		var html = this.getGroup($v.label,lists);
		return html;
	},
	getInput: function(data,val){
		var label = (data.required?'<em class="required">*</em>':'')+(data.label?data.label:'');
		var html = '<input class="input" type="'+data.type+'" name="'+data.name+'" value="'+(val&&val!=''?val:(data.value?data.value:''))+'"'+(data.placeholder?'placeholder="'+data.placeholder+'"':'')+(data.required?'data-validate="'+data.required+'"':'')+' />';
			html += (data.type=="color"?'<input class="button color-reset" type="button" data-reset="'+data.value+'" value="重置" />':'');
			html += (data.desc?'<p class="help-desc">'+data.desc+'</p>':'');
			html = this.getGroup(label,html);
		return html;
	},
	getGroup: function(label,html){
		var txt = '<div class="control-group">';
			txt += (label?'<span class="control-label">'+label+'</span>':'');
			txt += '<div class="controls">'+html+'</div>';
			txt += '</div>';
		return txt;
	},
	parsePath: function(path){	//序列化数据为json二维数组
		var steps = [];
		var key = path.substr(0, path.indexOf('['));
		if(!key.length){
			steps.push(path);
		}else{
			path = path.substr(path.indexOf('['),path.length);
			var keyNum = parseInt(path.substr(1, path.indexOf(']')-1));
			path = path.substr(path.indexOf(']'),path.length);
			var keyVal = path.replace(/[\W]/g,'')
			steps.push(key);
			steps.push(keyNum);
			steps.push(keyVal);
		}
		return steps;
	},
	upLoad: function(){
		var uploader = WebUploader.create({
	        auto:true,
	        fileVal:'upfile',
	        // swf文件路径
	        swf: './statics/js/upload/uploader.swf',
	        // 文件接收服务端。
	        server: upload_url,
	        // 选择文件的按钮。可选
	        formData:{
	            //img_id : $id,
	            file : 'upfile',
		        code : code
	        },
	        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
	        pick: {
	            id: '.control-load'
	        },
	        accept:{
	            title: '图片文件',
	            extensions: 'gif,jpg,jpeg,bmp,png',
	            mimeTypes: 'image/*'
	        },
	        chunked: false,
	        chunkSize:1000000,
	        resize: false
	    })

	    uploader.onUploadSuccess = function(file, response) {
	    	var $obj = $(".control-load.current");
	    	$obj.find('.webuploader-pick').html('替换上传');
	    	if(response.status == 1) {
	    		if($obj.data('id')!=undefined){
	    			var html = '<li><input type="hidden" name="imgs['+$obj.data('id')+'][src]" value="'+response.result.url+'"><input name="imgs['+$obj.data('id')+'][title]" type="hidden" /><input name="imgs['+$obj.data('id')+'][url]" type="hidden" /></li>';
	    			$(".control-form .control-pics").append(html);
	    		}else{
	    			$obj.parents(".control-loads").find('.pic-center input').val(response.result.url);
	    		}
	    		diy.reload();
	    	} else {
	    		alert(response.message);
	    		return false;
	    	}
	    }
	    
	    $(document).on('click','.control-load',function(){
	    	$(".control-load").removeClass("current");
	    	$(this).addClass("current");
	    });
	    
	}
}

var diy = {
	init: function(config){
		this.config = config;
		$.each(config, function(key) {
			if(this.name){
				$(".app-add-region ul").append('<li><a class="new-field" data-field="'+key+'">'+this.name+'</a></li>');
			}
			if(this.jsfile) Base.newFile('js',key,this.jsfile);
			if(this.cssfile) Base.newFile('css',key,this.cssfile);
		});
		var $app = $(".app-fields").html().match(/(diy[^\}]+)/ig);                                                                                                                                                  
		$(".app-fields").html('');
		var html = '';
		for(var i=0;i<$app.length;i++){
			var arr = $app[i].split(" ");
			try{
       			var $html = Panel[arr[1]](arr[2]);
	   			var $field = Base.getFiled($html,arr[1]);
	   			$field.attr("data-model",arr[1]);
	   			$field.attr("data-diy",arr[2]);
	   			if(arr[1]=="global"){
	   				$(".app-config").append($field);
	   			}else{
	   				html += $field[0].outerHTML;
	   			}
       		}catch(e){
       			alert("没有找到对应的 "+_para[1]+" 模块！");
       		}
		}
		$(".app-fields").append(html);
       	this.events();
       	$(".app-config .app-field").trigger("click");
	},
	events: function(){
		
		var _this = this;
		var _config = this.config;
		
		//滚动条事件
		var h = $(".app-add-region").height();
			t = $(".app-add-region").offset().top;
			wh = $(".app-add-region").offset().top - $(".app-entry").offset().top;
		
		$(".app-content").css({minHeight: wh+"px"});
		$(".app-preview").css({paddingBottom: h-2});
	
		//增加板块
		$(".app-add-region").on('click','a',function(){
			var $type = $(this).data("field");
			try{
				var $html = Panel[$type]();
	   			var $field = Base.getFiled($html,$type);
	   			$field.data("model",$type);
	   			$field.data("diy",'');
	   			if($(".app-fields .editing").length > 0){
	   				$(".app-fields .editing").after($field);
	   			}else{
	   				$(".app-fields").append($field);
	   			}
	   			$field.trigger("click");
			}catch(e){
				alert("没有找到对应的 "+$type+" 模块！");
			}
		});
		
		//控制面板input事件监听，有值改变时触发reload重置对应的app-field里data-diy的值
		$(".sidebar-content").on('change','.control-form input',function(){
			_this.reload();
		});
		
		//验证
		$(".sidebar-content").focusout(function(){
			_this.validate();
		});
		
		//板块拖拽
		$('.app-fields').sortable({
			axis:'y',
			opacity: 0.8,
			cancel: '.app-ban-field',	//禁止拖拽
			scroll: true,
			scrollSensitivity: 100,
			addClasses: false,
			stop: function(event,ui){
				$(".app-sidebar").css({marginTop:$(".app-field.editing").offset().top-$(".app-sidebar").parent().offset().top});
			}
		});
		
		//编辑
		$(".app-content").on("click",".actions .edit",function(){
			document.body.scrollTop = $(this).parents(".app-field").offset().top-37;
		});
		
		//编辑
		$(".app-content").on('click','.app-field',function(){
			//已是编辑状态下不执行下面的事件
			if($(this).hasClass("editing") || $(this).data("model") == 'content'){
				return false;
			}
			//点击新板块，创建新容器时先保存上一个板块的内容
			if(!$(this).hasClass("editing")&&$(".app-field.editing").length>0){
				_this.reload();
			}
			//清除商品分类选择内容
			$(".goods-add-class .root").html('');
			$(".goods-add-class .child").html('');
			
			var $this = $(this);
			$(".app-content .app-field").removeClass("editing");
			$this.addClass("editing");
			$(".app-sidebar").css({marginTop:$this.offset().top-$(".app-sidebar").parent().offset().top});
			var $model = $this.data("model");
			if($model){
				Opts[$model]($this.data('diy'),_config[$model]);
			}
		});
		
		//删除
		$(".app-content").on("click",".actions .delete",function(e){
			e.stopPropagation();
			var $this = $(this);
			$this.confirms('是否确定删除？',function(){
				var p = $this.parents(".app-field");
				var pp = $this.parents(".app-field").prev();
				p.remove();
				pp.trigger("click");
			});
		});
		
		//上架
		$(".app-actions .btn-add").click(function(){
			_this.formSubmit();
		});
		
		//保存为草稿
		$(".app-actions .btn-save").click(function(){
			_this.formSubmit();
		});
		
	},
	formSubmit: function(){
		diy.reload();
		var arr = [];
		var flog = true;
		$.each($(".app-content").find(".app-field"), function() {
			if($(this).data('error')){
				if($(this).find(".actions").length > 0){
					$(this).find(".actions .edit").trigger("click");
				}else{
					$(this).trigger("click");
					document.body.scrollTop = $(this).offset().top-37;
				}
				diy.validate();
				flog = false;
			}    
			arr.push('{diy '+$(this).data("model")+' '+$(this).data("diy")+'}');
		});
		if(flog){
			$.post(url,{content:arr},function(ret){
				if(ret.status == 1){
					message("保存成功！");
					setTimeout(function(){
						window.location.href = ret.referer;
					},900)
				}
			},'json');
		}
	},
	reload: function(){
		var vals = $('.sidebar-content').find(".control-form").serializeArray();
		var results = {};
		var _config = this.config;
		$.each(vals, function(i,key) {//遍历生成二维json数组
			var steps = Base.parsePath(this.name);//将name为[name][0][name]格式的分解
			if(steps.length<=1){
				results[steps[0]] = this.value;
			}else{
				var s1 = steps[0];
				var s2 = steps[1];
				var s3 = steps[2];
				if(typeof results[s1]=='undefined') results[s1] = [];
				if(typeof results[s1][s2]=='undefined') results[s1][s2] = {};
				results[s1][s2][s3] = this.value;
			}
		});
		var $edit = $(".app-field.editing");
		results = Base.encode(results);
		var $model = $edit.data("model");
		var $html = Panel[$model](results);
		var $field = Base.getFiled($html,$model);
		$field.data("model",$model);
		$field.data("diy",results);
		$field.addClass("editing");
		$edit.replaceWith($field);
		Opts[$model](results,_config[$model]);
		this.validate();
	},
	validate: function(){
		var validation = {
			required: function(str){	//不能为空
				if(!str) return "不能为空！";
			},
			digits: function(str){	//只能输入数字[0-9]
				if(!str.match(/^\d+$/)) return "请输入数字！";
			},
			txtrange: function(str,para){	//字符串长度
				var paraName = para.split(",");
				if(str.length<parseInt(paraName[0])||str.length>parseInt(paraName[1])){
					return "字符长度必须是"+paraName[0]+"到"+paraName[1]+"之间！";
				}
			},
			numrange: function(str,para){	//数值范围0-10
				var paraName = para.split(",");
				if(str<paraName[0]||str>paraName[1]){
					return "","数值范围必须是"+paraName[0]+"到"+paraName[1]+"之间！";
				}
			},
			url: function(str){	//URL
				if(!str.match(/^http:\/\/[A-Za-z0-9]+\.[A-Za-z0-9]+[\/=\?%\-&_~`@[\]\’:+!]*([^<>\"])*$/)){
			    	return "请输入正确的url地址,必须以http://开头！"
			    }
			},
			integer: function(str){	//整数
				if(!str.match(/^[-\+]?\d+$/)) return "请输入整数！";
			},
			doublenum: function(str){	//浮点数
				if(!str.match(/^[-\+]?\d+(\.\d+)?$/)) return "请输入浮点数！";
			},
			maxlength: function(str,para){	//字符串最大长度
				if(str.length>para) return "不能超过"+para+"个字符！";
			},
			minlength: function(str,para){	//字符串最小长度
				if(str.length<para) return "不能少于"+para+"个字符！";
			},
			english: function(str){	//必须输入英文
				if(!str.match(/^[A-Za-z]+$/)) return "请输入英文，不区分大小写！";
			},
			cube: function(obj){
				if(obj.prev("table").find("td.empty").length>0) return "必须添加满4列。";
			}
		}
		var $error = '';
		$.each($(".sidebar-content").find("[data-validate]"), function() {
			var warn, $parent = $(this).parents(".control-group");
			var $valid = $(this).data("validate").split(";");
			for(var i=0;i<$valid.length;i++){
				var $para = $valid[i].split(":");
				var $tip;
				if($para.length>1){
					$tip = validation[$para[0]]($(this).val(),$para[1]);
				}else{
					if($para[0]=="cube"){
						$tip = validation[$para[0]]($(this));
					}else{
						$tip = validation[$para[0]]($(this).val());
					}
				}
				if($tip) warn = $tip;
			}
			if(warn!=undefined){
				$parent.addClass("error");
				var label = $parent.find(".control-label").text().replace('*','').replace('：','');
				if($parent.find(".help-block").length>0){
					$parent.find(".help-block").html(label+warn);
				}else{
					$parent.find(".controls").append('<p class="help-block error-message">'+label+warn+'</p>');
				}
				$error = label+warn;
			}else{
				if($parent.hasClass("error")){
					$parent.find(".help-block").remove();
				}
				$parent.removeClass("error");
			}
		});
		if($error){
			$(".app-field.editing").data("error",$error);
		}
	}
}

var Panel = {
	content: function(vals){
		return $.base64.decode(vals);
	},
	ads: function(vals){
		var _code = '', _type = 0;
		if(!vals){
			_code = '<div class="hd-slider"><div class="hd-slider-group"><a class="hd-slider-item" href="javascript:;"><img src="statics/images/wap/ad.jpg"></a></div></div>';
		}else{
			var $diy = Base.decode(vals);
			var _html = '', $ind = '';
			if($diy.imgs){
				switch(parseInt($diy.show)){
					case 0:
						$.each($diy.imgs, function() {
							_html += '<a class="hd-slider-item" href="'+(this.url?this.url:"javascript:;")+'"><img src="'+this.src+'">'+(this.title?'<p class="hd-slider-title">'+this.title+'</p>':'')+'</a>'
						});
						if($diy.imgs.length>1){
							$ind += '<div class="hd-slider-indicator">';
							for(var i=0;i<$diy.imgs.length;i++){
								if(i==0){
									$ind += '<div class="hd-indicator hd-active"></div>';
								}else{
									$ind += '<div class="hd-indicator"></div>';
								}
							}
							$ind += '</div>';
						}
						_code = '<div class="hd-slider"><div class="hd-slider-group">'+_html+'</div>'+$ind+'</div>';
						break;
					case 1:
						_type = 1;
						$.each($diy.imgs, function() {
							_html += '<li'+($diy.size==1?' class="custom-image-small"':'')+'><a href="'+(this.url?this.url:"javascript:;")+'"><img src="'+this.src+'">'+(this.title?'<p class="hd-slider-title">'+this.title+'</p>':'')+'</a></li>';
						});
						_code = '<ul class="custom-image mui-clearfix">'+_html+'</ul>';
						break;
					default:
						break;
				}
			}else{
				_code = '<div class="hd-slider"><div class="hd-slider-group"><a class="hd-slider-item" href="javascript:;"><img src="statics/images/wap/ad.jpg"></a></div></div>';
			}
		}
		return _code;
	},
	search: function(){
		return '<div class="hd-search"><input type="search" placeholder="搜索商品名称"></div>';
	},
	global: function(vals){
		var $diy = Base.decode(vals);
		var _html = '';
			_html += '<div class="logo mui-pull-left"><img src="'+ $diy.logo +'" height="30"></div>';
			_html += '<h1 class="mui-title"'+ ($diy.headbg!="#0068b7"?'style="background-color: '+ $diy.headbg +'"':'') +'>'+$diy.title+'</h1>';
			if($diy.bgcolor!="#f9f9f9"){
				_html += '<style>.app-preview .app-field { background-color: '+ $diy.bgcolor +' ! important; }</style>';
			}
		return _html;
	},
	spacing: function(vals){
		if(!vals) return '<div class="custom-white" style="height: 20px;"></div>';
		var $diy = Base.decode(vals);
		return '<div class="custom-white" style="height: '+parseInt($diy.height)+'px;"></div>';
	},
	goods: function(vals){
		var _html = '';
		var classname = ' custom-goods-single';
		var goods_num = 3;
		if(vals){
			var $diy = Base.decode(vals);
			switch(parseInt($diy.size)){
				case 1:
					goods_num = 4;
					classname = '';
					break;
				case 2:
					classname = ' custom-goods-blend';
					break;
				case 3:
					classname = ' custom-goods-row';
					break;
			}
		}
		for(var i=1;i<=goods_num;i++){
			_html += '<li class="goods-item-list"><div class="list-item"><div class="list-item-pic"><a href="#"><img src="statics/images/wap/goods_'+i+'.gif"></a></div><div class="list-item-bottom"><div class="list-item-title"><a href="#">此处显示商品名称</a></div><div class="list-item-text"><span class="price-org">￥98.00</span></div></div></div></li>';
		}
		return '<ul class="custom-goods-items'+classname+' clearfix">'+_html+'</ul>';
	},
	cube: function(vals){
		var $table = '';
		if(!vals){
			for(var i=0;i<4;i++){
				var tr = '';
				for(var n=0;n<4;n++){
					tr += '<td></td>';
				}
				$table += '<tr>'+tr+'</tr>';
			}
		}else{
			var $d = Base.decode(vals);
			var layout = (typeof $d.layout == "string"?eval($d.layout):$d.layout);
			$.each(layout, function() {
				$table += '<tr>';
				$.each(this, function() {
					if(this.flog){
						$table += '<td class="empty"></td>';
					}else{
						var width = this.width, height = this.height;
						if(width&&height){
							var img = '';
							try{
								var $img = $d.imgs[this.index];
								if($img.src){
									img = ($img.src?'<img src="'+$img.src+'" />':'');
									if($img.href){
										img = '<a href="'+$img.href+'">'+img+'</a>';
									}
								}
							}catch(e){
								//TODO handle the exception
							}
							$table += '<td class="no-empty cols-'+width+' rows-'+height+'" colspan="'+width+'" rowspan="'+height+'">'+img+'</td>';
						}
					}
				});
				$table += '</tr>';
			});
		}
		return '<table class="cube-table"><tbody>'+$table+'</tbody></table>';
	}
}

var Opts = {
	createOpt: function(h){
		$(".sidebar-content").html('<form class="control-form">'+h+'</form>');
	},
	content: function(){
		this.createOpt('<p>内容不可编辑。</p>');
	},
	ads: function(data,config){
		var $diy = undefined;
		var _html = '';
		if(data) $diy = Base.decode(data);
		if($diy&&$diy.show==1){
			$.each(config.libs.image.form, function(k) {
				_html += Base.getRadio(this,($diy?$diy[k]:0));
			});
		}else{
			$.each(config.libs.normal.form, function(k) {
				_html += Base.getRadio(this,0);
			});
		}
		_html += '<ul class="control-pics clearfix">';
		var $index = 0;
		try {
			var libs_form = config.libs.form;
			$.each($diy.imgs, function(i) {
				_html += '<li class="control-pic-list control-loads" data-id="'+ i +'">'
						+'<div class="top"><span class="pic-center"><img src="'+this.src+'"><input class="control-load-input" type="hidden" name="imgs['+i+']['+libs_form.imgs.name+']" value="'+this.src+'" /></div>'
						+'<div class="bottom">'
						+'	<p>标题：<input class="input" name="imgs['+i+']['+libs_form.title.name+']" type="text" value="'+this.title+'" /></p>'
						+'	<p>链接：<input class="input" name="imgs['+i+']['+libs_form.url.name+']" type="text" value="'+this.url+'" /></p>'
						+'	<div class="handle"><label class="btn control-load">替换上传</label><a class="btn" href="javascript:;">删除图片</a>'
						+'</div>';
				$index++;
			});
		}catch(e){}
		_html += '<li class="control-pic-list add uploads"><label class="control-load" data-id="'+parseInt($index)+'">新增广告图</label></li></ul>';
		this.createOpt(_html);
		Base.upLoad();
	},
	search: function(data,config){
		this.createOpt('<p>'+config.libs.form.text+'</p><p>'+config.libs.form.tip+'</p>');
	},
	global: function(data,config){
		var _html = '';
		var $d = Base.decode(data);
		$.each(config.libs.form, function() {
			if(this.type=="text"||this.type=="color"){
				_html += Base.getInput(this,$d[this.name]);
			}else{
				var $pic = '<div class="control-loads clearfix">'
						+ '	<span class="pic-center" style="background-color: '+ $d['headbg'] +';">'
						+		($d[this.name]?'<img src="'+$d[this.name]+'" />':'')
						+ '		<input class="control-load-input" type="hidden" name="'+ this.name +'" value="'+($d[this.name]?$d[this.name]:'')+'" data-validate="required" />'
						+ '	</span>'
						+ '	<div class="pic-info"><label class="control-load load text-sub">选择图片</label><p>建议尺寸：100 x 100 像素</p></div></div>';
				_html += '<div class="uploads">'+ Base.getGroup(this.label,$pic) +'</div>';
			}
		});
		this.createOpt(_html);
		Base.upLoad();
	},
	spacing: function(data,config){
		var _html = '';
		var val = config.libs.form.value;
		var name = config.libs.form.name;
		var label = config.libs.form.label;
		if(data){
			var $d = Base.decode(data);
			val = $d.height;
		}
		_html = '<div class="hd-js-slider">'
			  + '	<span class="hd-slider-handle" data-valite="false" style="left: '+parseInt((val-20)*2.5)+'px"></span>'
			  + '	<input type="hidden" name="'+name+'" value="'+val+'" />'
			  + '</div>'
			  + '<div class="slider-height"><span class="js-height">'+val+'</span> 像素</div>';
		_html = Base.getGroup(label,_html);
		this.createOpt(_html);
	},
	goods: function(data,config){
		var h = '',
			u = {};
		if(data){
			u = Base.decode(data);
		}else{
			$.each(config.libs.form, function() {
				u[this.name] = (this.type == "radio" ? 0 : '');
			});
		}
		function getCategoryText(cid){
			var a = [];
			$.each(jsoncategory, function(){
				if(this.id == cid){
					a.push(this.name);
					getCategoryText(this.parent_id);
				}
			});
			return a;
		}
		$.each(config.libs.form, function(k, v) {
			var val = u[this.name];
			if(this.type == "radio"){
				h += Base.getRadio(this, val);
			}
			if(k == "category"){
				var categroy = '';
				if(val){
					var arr = getCategoryText(val);
					for(var i=arr.length-1;i>=0;i--){
						if(i == arr.length-1){
							categroy += arr[i];
						}else{
							categroy += " / "+arr[i];
						}
					}
					categroy = '<a class="goods-category-choose has-selected" href="javascript:;">'+ categroy +'</a>';
				}else{
					categroy = '<a class="goods-category-choose" href="javascript:;">请选择商品分类</a>';
				}
				h += '<div class="control-group"><span class="control-label"><em class="required">*</em>商品分类：</span><div class="controls">'+ categroy +'<input type="'+ this.type +'" name="'+ this.name +'" value="'+ val +'" data-validate="required"></div></div>';
			}
		});
		this.createOpt(h);
	},
	cube: function(data,config){
		var $table = '', $cube = '', $index = 0, $tab = '', $choice = '';
		if(!data){
			for(var i=0;i<4;i++){
				var tr = '';
				for(var n=0;n<4;n++){
					tr += '<td class="empty" data-x="'+n+'" data-y="'+i+'"><span>+</span></td>';
				}
				$table += '<tr>'+tr+'</tr>';
			}
			$cube = layoutMap.coordinate();
		}else{
			var $d = Base.decode(data);
				$cube = eval($d.layout);
			if(!$cube){
				$cube = layoutMap.coordinate();
			}
			for(var i=0;i<4;i++){
				var tr = '';
				for(var n=0;n<4;n++){
					var O = $cube[i][n];
					if(O.flog){
						tr += '<td class="empty" data-x="'+n+'" data-y="'+i+'"><span>+</span></td>';
					}else{
						if(O.width != undefined){
							var $img = '<span>'+O.width*160+'×'+O.height*160+'</span>';
							var $src = '', $href = '';
							try{
								if($d.imgs[O.index]){
									$src = $d.imgs[O.index].src;
									$href = $d.imgs[O.index].href;
									if($src){
										$img = '<img src="'+$src+'" />';
									}
									if($href){
										$img = '<a href="'+$href+'">'+$img+'</a>';
									}
								}
							}catch(e){
								//TODO handle the exception
							}
							tr += '<td class="no-empty cols-'+O.width+' rows-'+O.height+($src?'':' index-'+O.index)+'" colspan="'+O.width+'" rowspan="'+O.height+'" data-x="'+O.x+'" data-y="'+O.y+'" data-index="'+O.index+'"><span>'+$img+'</span></td>';
							var opts = '', $li, $pic, $select;
							$pic = '<div class="control-loads clearfix">'
								 + '	<span class="pic-center">'
								 +			($src?'<img src="'+$src+'" />':'')
								 +	'		<input class="control-load-input" type="hidden" name="imgs['+O.index+'][src]" value="'+($src?$src:'')+'" data-validate="required" />'
								 + '	</span>'
								 + '	<div class="pic-info"><label class="control-load load text-sub">选择图片</label><p>建议尺寸：'+O.width*160+' x '+O.height*160+' 像素</p></div></div>';
							for(var h=1;h<=O.height;h++){
								for(var w=1;w<=O.width;w++){
									opts += '<li><a class="image-layout" href="javascript:;" data-width="'+w+'" data-height="'+h+'">'+h+'行 '+w+'列</a></li>'
								}
							}
							$select = '<div class="dropdown"><a class="btn dropdown-toggle" data-toggle="dropdown" href="javascript:;">'+O.width+'行 '+O.height+'列<span class="caret"></span></a><ul class="dropdown-menu">'+opts+'</ul></div>';
							$li = '<li class="choice uploads" data-id="'+O.index+'">';
							$li += Base.getGroup('<em class="required">*</em>图片：</span>',$pic);
							$li += Base.getGroup('链接到：','<input class="input" type="text" name="imgs['+O.index+'][href]" value="'+($href?$href:'')+'" />');
							$li += Base.getGroup('图片占位：',$select);
							$li += '<span class="close-modal" data-x="'+n+'" data-y="'+i+'">×</span></li>';
							$tab += $li;
							$index++;
						}
					}
				}
				$table += '<tr>'+tr+'</tr>';
			}	
		}
		
		if($tab){
			$choice = '<ul class="choices">'+$tab+'</ul>';
		}
		this.createOpt('<div class="control-group"><span class="control-label">布局：</span><div class="controls"><table class="tablecloth" data-length="'+$index+'"><tbody>'+$table+'</tbody></table><input type="hidden" name="layout" data-validate="cube" value=\''+($cube?JSON.stringify($cube):'')+'\' /><p class="help-desc">点击 + 号添加内容</p></div></div>'+$choice);
		Base.upLoad();
	}
}

//魔方
var layoutMap = {
	init: function(data){
		this.rows = 4;	//行
		this.cols = 4;	//列
	},
	coordinate: function(C){ //获取坐标重新绘制
		C = eval(C);
		var M, I = $(".tablecloth").data("length") || 0;
		if(!C){
			M = [];
			for(var i=0;i<this.cols;i++){
				var arr = [];
				for(var n=0;n<this.rows;n++){
					var _json = {};
					_json.x = n;
					_json.y = i;
					if((n >= this.startX && n <= this.endX) && (i >= this.startY && i <= this.endY)){
						if(n == this.startX && i == this.startY){
							_json.width = parseInt(this.endX - this.startX) + 1;
							_json.height = parseInt(this.endY - this.startY) + 1;
							_json.index = I;
							I++;
						}
					}else{
						_json.flog = true;
					}
					arr.push(_json);
				}
				M.push(arr);
			}
		}else{
			for(var i=0;i<this.cols;i++){
				for(var n=0;n<this.rows;n++){
					if((n >= this.startX && n <= this.endX) && (i >= this.startY && i <= this.endY)){
						delete C[i][n].flog;
					}
					if(n==this.startX && i==this.startY){
						C[i][n].width = parseInt(this.endX - this.startX) + 1;
						C[i][n].height = parseInt(this.endY - this.startY) + 1;
						C[i][n].index = I;
						I++;
					}
				}
			}
			M = C;
		}
		$(".tablecloth").data("length",I)
		return M;
	},
	optArea: function(D){	//标出可选区域
		D = eval(D);
		var result = [];
		if(!D){	//没有数据时初始化坐标
			D = [];
			for(var y=0;y<this.rows;y++){
				var $arr = [];
				for(var x=0;x<this.cols;x++){
					var $json = {};
					$json.flog = true;
					$json.x = x;
					$json.y = y;
					$arr.push($json);
				}
				D.push($arr);
			}
		}
		//获取可选区域坐标
		for(var i=this.startY;i<this.rows;i++){
			var arr = [];
			var flog = true;
			for(var n=this.startX;n<this.cols;n++){
				var _json = {};
				if(D[i][n].flog){
					_json.x = n;
					_json.y = i;
					arr.push(_json);
				}else{
					if(n==this.startX && !D[i][n].flog){
						flog = false;
					}
					break;
				}
			}
			if(arr.length>0) result.push(arr);
			if(!flog) break;
		}
		
		//当只有一格时直接选中
		if(result.length==1&&result[0].length==1){
			layoutMap.endX = result[0][0].x;
			layoutMap.endY = result[0][0].y;
			var $edit = $(".app-field.editing");
			var vals = this.coordinate(D);
			$(".sidebar-content").find("input[name=layout]").val(JSON.stringify(vals));
			diy.reload();
			return;
		}
		
		//去除下一列的长度长于上一列的问题
		for(var s=1;s<result.length;s++){
			if(result[s].length > result[s-1].length){
				result[s].splice(result[s-1].length,result[s].length-1);
			}
		}
		
		//遍历添加可选区域
		$.each($(".tablecloth").find("td"),function(){
			for(var i=0;i<result.length;i++){
				for(var n=0;n<result[i].length;n++){
					if($(this).data("x") == result[i][n].x && $(this).data("y") == result[i][n].y){
						$(this).addClass("selected");
					}
				}
			}
		});
	},
	delete: function(maps,x,y){
		maps = eval(maps);
		var D, I;
		$.each(maps, function() {
			$.each(this, function(){
				if(this.x == x && this.y == y){
					D = this;
				}
			})
		});
		I = D.index;
		for(var i = D.y;i < D.y + parseInt(D.height);i++){
			for(var n = D.x;n < D.x + parseInt(D.width);n++){
				if(i == D.y && n == D.x){
					delete maps[D.y][D.x];
					maps[i][n] = {};
					maps[i][n].x = D.x;
					maps[i][n].y = D.y;
				}
				maps[i][n].flog = true;
			}
		}
		$.each(maps, function() {
			$.each(this, function(){
				if(this.index > I){
					this.index = this.index-1;
				}
			})
		});
		
		$.each($(".sidebar-content .choices li"), function() {
			var id = $(this).data("id");
			if(id > I){
				$(this).data("id",id-1);
				$.each($(this).find("input"), function(){
					var name = $(this).attr("name").replace(id,id-1);
					$(this).attr("name",name)
				});
			}
		});
		
		$(".sidebar-content").find("input[name=layout]").val(JSON.stringify(maps));
		diy.reload();
	},
	modify: function(i,h,w,m){
		var M = eval(m), X, Y, D,
			W = parseInt(w),
			H = parseInt(h);

		$.each(M, function() {
			$.each(this, function(){
				if(this.index == i){
					D = this;
				}
			})
		});
		for(var y=D.y;y<D.y+D.height;y++) {
			for(var x=D.x;x<D.x+D.width;x++) {
				if(x>=D.x+W || y>=D.y+H){
					M[y][x].flog = true;
				}
			}
		}
		D.width = W;
		D.height = H;
		$(".sidebar-content").find("input[name=layout]").val(JSON.stringify(M));
		diy.reload();
	}
}
	
;(function($){
	
	//表单序列化json二维数组
	$.fn.serializeObject = function(){
	    var o = {};
	    var a = this.serializeArray();
	    $.each(a, function() {
	        if (o[this.name] !== undefined) {
	            if (!o[this.name].push) {
	                o[this.name] = [o[this.name]];
	            }
	            o[this.name].push(this.value || '');
	        } else {
	            o[this.name] = this.value || '';
	        }
	    });
	    return o;
	}
	
	$.fn.confirms = function(opt,callback){
		$(".hd-popover").remove();
		var $this = $(this);
		var txt = opt || "您是否要执行此操作？";
		var $wrap = $('<div class="hd-popover bottom">');
		$wrap.append('<div class="hd-popover-inner clearfix"><div class="hd-popover-text">'+txt+'</div><div class="hd-popover-handle"><a href="javascript:;" class="button bg-main sure">确定</a><a href="javascript:;" class="button bg-gray-white cancel">取消</a></div></div><div class="arrow"></div>');
		$("body").append($wrap);
		$wrap.css({left:$this.offset().left-$wrap.width()+$this.width()/2+15+'px',top:$this.offset().top+$this.height()+5+'px'});
		var $cancel = $wrap.find(".cancel");
		var $sure = $wrap.find(".sure");
		$cancel.on('click',function(){
			$wrap.stop().animate({opacity:'0'},300,function(){
				$wrap.remove();
			});
		});
		$sure.on('click',function(){
			if($.isFunction(callback)){
				callback(true);
				$wrap.remove();
			}
		});
	}
	
})(jQuery)

$(function(){
	
	layoutMap.init();
	
	//创建选择器画布
	$(document).on('click','.tablecloth .empty',function(){
		var x = this.getAttribute("data-x");
		var y = this.getAttribute("data-y");
		if(!$(this).hasClass("selected")){
			layoutMap.startX = x;
			layoutMap.startY = y;
			$('.tablecloth td.empty').removeClass("selected");
			$('.tablecloth td.empty').removeClass("start").children("span").html("+")
			$(this).addClass("start").children("span").html("开始");
			layoutMap.optArea($(this).parents("table").next("input").val());
		}else if($(this).hasClass("selected")){
			layoutMap.endX = x;
			layoutMap.endY = y;
			var $edit = $(".app-field.editing");
			var vals = layoutMap.coordinate($(this).parents("table").next("input").val());
			$(".sidebar-content").find("input[name=layout]").val(JSON.stringify(vals));
			diy.reload();
		}
	});
	
	$(document).on('mouseover','.tablecloth .empty.selected',function(){
		$(this).addClass("hover").children("span").html("结束");
	});
	$(document).on('mouseout','.tablecloth .empty.selected',function(){
		if($(this).hasClass("start")){
			$(this).removeClass("hover").children("span").html("开始");
		}else{
			$(this).removeClass("hover").children("span").html("+");
		}
	});
	
	//选项卡切换
	$(document).on('click','.tablecloth .no-empty',function(){
		$(".tablecloth .no-empty").removeClass("current");
		$(this).addClass("current");
		var $id = $(this).data("index");
		$.each($(".sidebar-content .choices .choice"), function() {
			var $this = $(this);
			if($(this).data("id")==$id){
				$(this).slideDown(200,function(){
					$this.css('overflow','');
				});
			}else{
				$(this).slideUp(200);
			}
		});
		return false;
	});
	
	$('.sidebar-content').on('click','.image-layout',function(){
		var height = this.getAttribute("data-width");
		var width = this.getAttribute("data-height");
		var index = $(this).parents('.choice').data("id");
		var maps = $(this).parents('.choices').prev('.control-group').find("input").val();
		layoutMap.modify(index,width,height,maps);
	});
	
	$(document).on('click','.choices .choice .close-modal',function(){
		var parents = $(this).parents('.choices');
		var maps = parents.prev('.control-group').find("input").val();
		var x = $(this).data("x");
		var y = $(this).data("y");
		$(this).parent().remove();
		layoutMap.delete(maps,x,y);
	});
	
	//辅助空白高度调整工具
	$(".hd-slider-handle").live('mousedown',function(){
		var $next = $(this).next("input");
		var $this = $(this);
		var $width = $this.parent().width()-$this.width();
		$(this).data("valite",true)
		var percent = $width / 100;
	    $(document.body).mousemove(function(event) {
	        if ($this.data("valite") == false) return;
	        var changeX = event.clientX-$this.parent().offset().left;//距离
	       	if(changeX >= $width){
				changeX = $width;
			}else if(changeX<=0){
				changeX = 0;
			}
			$this.css({left: changeX+'px'});
			$next.val(parseInt(changeX/2.5+20));
			$(".app-field.editing").find(".custom-white").css({height:parseInt(changeX/2.5+20)+'px'});
			$(".js-height").html(parseInt(changeX/2.5+20));
	   	});
	});
	$(document.body).mouseup(function() {
		if($(".hd-slider-handle").length<1) return;
		$('.hd-slider-handle').data("valite",false);
	});
	
	//颜色重置
	$(document).on('click','.color-reset',function(){
		if($(this).prev("input").val() != $(this).data("reset")){
			$(this).prev("input").val($(this).data("reset"));
			diy.reload();
		}
	});
	
	//删除广告图片
	$(document).on('click', ".control-pics .handle a.btn", function(argument) {
		var tid = $(this).parents("li").data("id");
		$.each($(".sidebar-content .control-pics li"), function() {
			var id = $(this).data("id");
			if(id > tid){
				$.each($(this).find("input"), function(){
					var name = $(this).attr("name").replace(id,id-1);
					$(this).attr("name",name)
				});
			}
		});
		$(this).parents("li").remove();
		diy.reload();
	})
	
})
