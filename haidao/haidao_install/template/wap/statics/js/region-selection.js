;(function(){
	
	$.regionSelect = function(options){
		
		defaults = $.extend({
			url: null,
			id: 0,
			autor: undefined,
			callback: function(){}
		},options);
		
		this.url = defaults.url;
		this.id = defaults.id;
		this.autor = defaults.autor;
		if(this.url == null) throw new Error("请填写AJAX请求地址！");
		if(this.autor == undefined) throw new Error("指定触发选择器！");
		
		this.len = 10;
		this.district = $('<div class="district">');
		this.hand = $('<div class="hand mui-clearfix">');
		this.selected = $('<ul class="selected"></ul>');
		this.container = $('<ul class="container"></ul>');
		this.cancel = $('<span class="mui-pull-left cancel">取消</span>');
		this.sure = $('<span class="mui-pull-right sure">确定</span>');
		
		var self = this;
		
		this.autor.on('tap',function(){
			if($(".district").length<=0){
				self.container.append(self.returnHtml(0));
				self.hand.append(self.cancel,self.sure);
				self.district.append(self.hand,self.selected,self.container);
				$('body').append(self.district);
			}else{
				$(".district").show();
			}
		});
		
		this.container.on('tap','li',function(){
			if($(this).data('leveltxt')){
				var str = $(this).data('leveltxt').split(",");
				self.len = str.length+1;
			}
			self.selected.append(this);
			self.container.css({paddingTop: self.selected.children().length*40+'px'});
			self.container.scrollTop(0);
			self.container.html(self.returnHtml($(this).data("id")));
		});
		
		this.selected.on('tap','li',function(){
			var prev = self.selected.children();
			var ind = $(this).index("li");
			self.selected.html('');
			self.container.html(self.returnHtml($(this).data("parentid")));
			$.each(prev, function(i) {
				if(i<ind){
					self.selected.append(this);
				}
			});
			self.container.css({paddingTop: self.selected.children().length*40+'px'});
		});
		
		this.cancel.on('tap',function(){
			self.district.hide();
		});
		
		this.sure.on('tap',function(){
			if(self.selected.children().length<self.len){
				$.tips({content:"请选择地区！"});
				return false;
			}
			self.district.hide();
			var id = [];
			var name = [];
			$.each(self.selected.children(), function() {
				id.push($(this).data('id'));
				name.push($(this).html());
			});
			defaults.callback(id,name);
		});
		
	}
	
	$.returnHtml = function(id){
		var html = '';
		var data = this.getRemoteData(id);
		if(!data) return false;
		$.each(data, function(){
			if(this.level==0){
				html += '<li data-id="'+this.id+'" data-parentid="'+this.parent_id+'" data-leveltxt="'+this.location+'">'+this.name+'</li>';
			}else{
				html += '<li data-id="'+this.id+'" data-parentid="'+this.parent_id+'">'+this.name+'</li>';
			}
			
		});
		return html;
	}
	
	$.getRemoteData = function(d){

		var self = this;
		var f = {
			cache: false,
			async: false, 
	        type: 'post',
	        url: self.url,
	        data: {id:d},
	        dataType: 'json',
	        success: function(data){
	        	datas = data;
	        }
		}
		
		$.ajax(f);
		
		return datas;
		
	}
	
})(jQuery);
