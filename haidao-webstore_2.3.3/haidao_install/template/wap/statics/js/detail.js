$(function(){
	
	$(".goods-spec-item label").on('click',function(){
		if($(this).hasClass("selected")){
			$(this).removeClass("selected");
		}else{
			$(this).addClass("selected").siblings("label").removeClass("selected");
		}
	});
	
	//判断规格是否选择
	$("#spec .option .mui-btn").on('click',function(){
		if($(".goods-spec-item .selected").length!=$(".goods-spec-item").length){
			alert("请选择规格！")	
		}
	});
	
	var url = window.location.href;
	var strCart = new RegExp('#cart'),
		strBuy = new RegExp('#buy');
	if(strCart.test(url)||strBuy.test(url)){
		$(".cover-decision").show();
		$(".hd-cover").show();
		$(".hd-cover").addClass("show");
	}
	//加入购物车
	$("#join-cart").on('tap',function(){
		window.location.href = '#cart';
		$(".cover-decision").show();
		$(".hd-cover").show();
		setTimeout(function(){
			$(".hd-cover").addClass("show");
		},50);
	});
	//立即购买
	$("[data-event='buy_now']").on('tap', function(){
		window.location.href = '#buy';
		$(".cover-decision").show();
		$(".hd-cover").show();
		setTimeout(function(){
			$(".hd-cover").addClass("show");
		},50);
	});
	$.each(sku_obj, function(i) {
		var _this = this;
		$("label[data-id="+this.id+"]").each(function(){
			if(_this.value == $(this).data("value")){
				$(this).addClass("selected");
			}
		});
		changeStatus();
	});

	$(".goods-spec-item label").bind('click',function(){
		changeStatus();
	});

	function changeStatus(){
		var arr = window.location.href.split('#');
		var url_pre = arr[1];
	 	var specs_array = new Array();
	 	var regexp = '';
	 	/* 取出所有在规格数据 */
	 	$("dl").each(function(i){
	        var selected = $(this).find(".selected");
	        if(selected.length>0) specs_array[i] = selected.attr("data-id") + ':' + selected.attr("data-value");
	        else specs_array[i] = "\\\d+:\.+";
	    });
	    var id = '';
	 	$("dl").each(function(k){
	 		var selected = $(this).find(".selected");
	 		id += selected.data("id")+":"+selected.data("value")+";";
	    });
	    if(product_json[id] != undefined && product_json[id].sku_id != sku_id){
	    	window.location.href = product_json[id].url + '#' + url_pre;
	    }
	}
	//关闭规格选择
	$("#spec .close").on('tap',function(){
		$(".hd-cover").removeClass("show");
		$(".cover-decision").hide();
		window.location.href = '#';
		setTimeout(function(){
			$(".hd-cover").hide();
		},200);
	});
	
})

var hdTouch = function(){
	this.wrap = document.getElementById("hd_detail");
	this.head = document.getElementById("head_wrap");
	this.body = document.getElementById("bottom_wrap");
	this.wrapperH = this.wrap.parentNode.clientHeight;
	
	this.wrap.style.height = this.wrapperH * 2 + "px";
	this.head.style.height = this.wrapperH + "px";
	this.body.style.height = this.wrapperH + "px";
	
	var that = this;

	var _start = function(e){
        var _child = this.children[0];
        this.startY = e.touches[0].pageY;
        this.startTime = e.timeStamp || Date.now();
        this.distY = this.distY || 0;
        this.maxScrollY = that.wrapperH - _child.clientHeight;
        if(this.isInTransition == undefined) this.isInTransition = false;
        if (this.isInTransition) {	//当区域在滑动时，重置可滑动距离
　　　　　　　　　var matrix = window.getComputedStyle(_child, null);	//getComputedStyle的意思是得到指定节点元素的所有style属性值
	        	matrix = matrix["webkitTransform"].split(")")[0].split(", ");
				this.distY = parseFloat(Math.round(matrix[5]));
				_child.style.webkitTransform = 'translateZ(0) translateY(' + this.distY + 'px)';
				_child.style.transitionDuration = "0ms";
		        this.isInTransition = false
	    }
	}
	
	var _move = function(e){
		e.preventDefault();
		this.slideY = e.touches[0].pageY - this.startY;
		if(Math.abs(this.slideY) < 15) return;	
		var slide = parseFloat(this.slideY) + parseFloat(this.distY);
		$(".pulldown").children("p").eq(0).text("继续下拉，返回商品简介");
		$(".detail-tips").children("p").eq(0).text("继续拖动，查看详情");
		if(slide > 0){
			if(slide / 4 > 40){
				$(".pulldown").children("p").eq(0).text("释放下拉，返回商品简介");
			}
			slide = slide / 4;
		}else if(slide < this.maxScrollY){
			if(Math.abs((slide - this.maxScrollY) / 4) > 40){
				$(".detail-tips").children("p").eq(0).text("释放上拉，查看详情");
			}
			slide = this.maxScrollY + (slide - this.maxScrollY) / 4;
		}
		that._slide(this, slide, 0);
	}
	
	var _end = function(e){
		var endT, T, Y;
		endT = e.timeStamp || Date.now();
		T = endT - this.startTime;
		Y = this.distY + this.slideY;
		if(this.slideY == 0 || !this.slideY) return;
		if(Y > 0){
			if(this.slideY / 4 > 40 && T > 180){
				that._slideTo(that.wrap, 0, 600);
			}
			that._slide(this, 0, 300);
			that._clear(this);
			return;
		};
		if(Y < this.maxScrollY){
			if(Math.abs(this.slideY) / 4 > 40 && T > 180){
				that._slideTo(that.wrap, -that.wrapperH, 600);
			}
			that._slide(this, this.maxScrollY, 300);
			that._clear(this);
			return;
		};
		if(T < 300 && Math.abs(this.slideY) > 30){
			var _this = this;
			var result = that._momentum(this.slideY, T, this.distY, that.wrapperH);
			var expand = Math.abs(this.slideY) / T * 18;
			if(result > 0) result = expand;
			if(result < this.maxScrollY) result = this.maxScrollY - expand;
			that._slide(this, result, 600);
			setTimeout(function(){
				if(result > 0){
					result = 0;
					that._slide(_this, result, 300);
				}else if(result < _this.maxScrollY){
					result = _this.maxScrollY;
					that._slide(_this, result, 300);
				}else{
					result = result;
				}
				that.distY = result;
			}, 600)
			that._clear(this);
		}else{
			this.distY = this.distY + this.slideY;
		}
	}
	
	this.head.addEventListener('touchstart', _start, false);
	this.head.addEventListener('touchmove', _move, false);
	this.head.addEventListener('touchend', _end, false);
	
	this.body.addEventListener('touchstart', _start, false);
	this.body.addEventListener('touchmove', _move, false);
	this.body.addEventListener('touchend', _end, false);
}

hdTouch.prototype = {
	_momentum: function(Y, T, D, W){
		var speed = Math.abs(Y) / T;
       	var destination = D + Y * speed + (Y < 0 ? -W : W);
       	return destination;
	},
	_slide: function(obj, y, time){
	    obj.isInTransition = true;	//	开启transition效果
		this._slideTo(obj.children[0], y, time);
	},
	_slideTo: function(obj, y, time){
		obj.style.transform = "translate3d(0, " + y + "px, 0)";
		obj.style.webkitTransform = 'translateZ(0) translateY(' + y + 'px)';
		obj.style.transitionTimingFunction = "cubic-bezier(0.25, 0.46, 0.45, 0.94)";
		obj.style.transitionDuration = time + "ms";
	},
	_clear: function(O){
		O.slideY = 0;
		O.startTime = 0;
	}
}