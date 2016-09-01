/**
 * jQuery EasyUI 1.4.1
 * 
 * Copyright (c) 2009-2014 www.jeasyui.com. All rights reserved.
 * 
 * Licensed under the GPL license: http://www.gnu.org/licenses/gpl.txt To use it
 * on other terms please contact us at info@jeasyui.com
 * 
 */
/**
 * parser - jQuery EasyUI
 * 
 */
(function($){
	$.parser = {
		auto: true,
		onComplete: function(context){},
		plugins:['draggable','droppable','resizable','pagination','tooltip',
		         'linkbutton','menu','menubutton','splitbutton','progressbar',
				 'tree','textbox','filebox','combo','combobox','combotree','combogrid','numberbox','validatebox','searchbox',
				 'spinner','numberspinner','timespinner','datetimespinner','calendar','datebox','datetimebox','slider',
				 'layout','panel','datagrid','propertygrid','treegrid','tabs','accordion','window','dialog','form'
		],
		parse: function(context){
			var aa = [];
			for(var i=0; i<$.parser.plugins.length; i++){
				var name = $.parser.plugins[i];
				var r = $('.easyui-' + name, context);
				if (r.length){
					if (r[name]){
						r[name]();
					} else {
						aa.push({name:name,jq:r});
					}
				}
			}
			if (aa.length && window.easyloader){
				var names = [];
				for(var i=0; i<aa.length; i++){
					names.push(aa[i].name);
				}
				easyloader.load(names, function(){
					for(var i=0; i<aa.length; i++){
						var name = aa[i].name;
						var jq = aa[i].jq;
						jq[name]();
					}
					$.parser.onComplete.call($.parser, context);
				});
			} else {
				$.parser.onComplete.call($.parser, context);
			}
		},
		
		parseValue: function(property, value, parent, delta){
			delta = delta || 0;
			var v = $.trim(String(value||''));
			var endchar = v.substr(v.length-1, 1);
			if (endchar == '%'){
				v = parseInt(v.substr(0, v.length-1));
				if (property.toLowerCase().indexOf('width') >= 0){
					v = Math.floor((parent.width()-delta) * v / 100.0);
				} else {
					v = Math.floor((parent.height()-delta) * v / 100.0);
				}
			} else {
				v = parseInt(v) || undefined;
			}
			return v;
		},
		
		/**
		 * parse options, including standard 'data-options' attribute.
		 * 
		 * calling examples:
		 * $.parser.parseOptions(target);
		 * $.parser.parseOptions(target, ['id','title','width',{fit:'boolean',border:'boolean'},{min:'number'}]);
		 */
		parseOptions: function(target, properties){
			var t = $(target);
			var options = {};
			
			var s = $.trim(t.attr('data-options'));
			if (s){
				if (s.substring(0, 1) != '{'){
					s = '{' + s + '}';
				}
				options = (new Function('return ' + s))();
			}
			$.map(['width','height','left','top','minWidth','maxWidth','minHeight','maxHeight'], function(p){
				var pv = $.trim(target.style[p] || '');
				if (pv){
					if (pv.indexOf('%') == -1){
						pv = parseInt(pv) || undefined;
					}
					options[p] = pv;
				}
			});
				
			if (properties){
				var opts = {};
				for(var i=0; i<properties.length; i++){
					var pp = properties[i];
					if (typeof pp == 'string'){
						opts[pp] = t.attr(pp);
					} else {
						for(var name in pp){
							var type = pp[name];
							if (type == 'boolean'){
								opts[name] = t.attr(name) ? (t.attr(name) == 'true') : undefined;
							} else if (type == 'number'){
								opts[name] = t.attr(name)=='0' ? 0 : parseFloat(t.attr(name)) || undefined;
							}
						}
					}
				}
				$.extend(options, opts);
			}
			return options;
		}
	};
	$(function(){
		var d = $('<div style="position:absolute;top:-1000px;width:100px;height:100px;padding:5px"></div>').appendTo('body');
		$._boxModel = d.outerWidth()!=100;
		d.remove();
		
		if (!window.easyloader && $.parser.auto){
			$.parser.parse();
		}
	});
	
	/**
	 * extend plugin to set box model width
	 */
	$.fn._outerWidth = function(width){
		if (width == undefined){
			if (this[0] == window){
				return this.width() || document.body.clientWidth;
			}
			return this.outerWidth()||0;
		}
		return this._size('width', width);
	};
	
	/**
	 * extend plugin to set box model height
	 */
	$.fn._outerHeight = function(height){
		if (height == undefined){
			if (this[0] == window){
				return this.height() || document.body.clientHeight;
			}
			return this.outerHeight()||0;
		}
		return this._size('height', height);
	};
	
	$.fn._scrollLeft = function(left){
		if (left == undefined){
			return this.scrollLeft();
		} else {
			return this.each(function(){$(this).scrollLeft(left)});
		}
	};
	
	$.fn._propAttr = $.fn.prop || $.fn.attr;
	
	$.fn._size = function(options, parent){
		if (typeof options == 'string'){
			if (options == 'clear'){
				return this.each(function(){
					$(this).css({width:'',minWidth:'',maxWidth:'',height:'',minHeight:'',maxHeight:''});
				});
			} else if (options == 'fit'){
				return this.each(function(){
					_fit(this, this.tagName=='BODY' ? $('body') : $(this).parent(), true);
				});
			} else if (options == 'unfit'){
				return this.each(function(){
					_fit(this, $(this).parent(), false);
				});
			} else {
				if (parent == undefined){
					return _css(this[0], options);
				} else {
					return this.each(function(){
						_css(this, options, parent);
					});
				}
			}
		} else {
			return this.each(function(){
				parent = parent || $(this).parent();
				$.extend(options, _fit(this, parent, options.fit)||{});
				var r1 = _setSize(this, 'width', parent, options);
				var r2 = _setSize(this, 'height', parent, options);
				if (r1 || r2){
					$(this).addClass('easyui-fluid');
				} else {
					$(this).removeClass('easyui-fluid');
				}
			});
		}
		
		function _fit(target, parent, fit){
			if (!parent.length){return false;}
			var t = $(target)[0];
			var p = parent[0];
			var fcount = p.fcount || 0;
			if (fit){
				if (!t.fitted){
					t.fitted = true;
					p.fcount = fcount + 1;
					$(p).addClass('panel-noscroll');
					if (p.tagName == 'BODY'){
						$('html').addClass('panel-fit');
					}
				}
				return {
					width: ($(p).width()||1),
					height: ($(p).height()||1)
				};
			} else {
				if (t.fitted){
					t.fitted = false;
					p.fcount = fcount - 1;
					if (p.fcount == 0){
						$(p).removeClass('panel-noscroll');
						if (p.tagName == 'BODY'){
							$('html').removeClass('panel-fit');
						}
					}
				}
				return false;
			}
		}
		function _setSize(target, property, parent, options){
			var t = $(target);
			var p = property;
			var p1 = p.substr(0,1).toUpperCase() + p.substr(1);
			var min = $.parser.parseValue('min'+p1, options['min'+p1], parent);// || 0;
			var max = $.parser.parseValue('max'+p1, options['max'+p1], parent);// || 99999;
			var val = $.parser.parseValue(p, options[p], parent);
			var fluid = (String(options[p]||'').indexOf('%') >= 0 ? true : false);
			
			if (!isNaN(val)){
				var v = Math.min(Math.max(val, min||0), max||99999);
				if (!fluid){
					options[p] = v;
				}
				t._size('min'+p1, '');
				t._size('max'+p1, '');
				t._size(p, v);
			} else {
				t._size(p, '');
				t._size('min'+p1, min);
				t._size('max'+p1, max);
			}
			return fluid || options.fit;
		}
		function _css(target, property, value){
			var t = $(target);
			if (value == undefined){
				value = parseInt(target.style[property]);
				if (isNaN(value)){return undefined;}
				if ($._boxModel){
					value += getDeltaSize();
				}
				return value;
			} else if (value === ''){
				t.css(property, '');
			} else {
				if ($._boxModel){
					value -= getDeltaSize();
					if (value < 0){value = 0;}
				}
				t.css(property, value+'px');
			}
			function getDeltaSize(){
				if (property.toLowerCase().indexOf('width') >= 0){
					return t.outerWidth() - t.width();
				} else {
					return t.outerHeight() - t.height();
				}
			}
		}
	};
	
})(jQuery);

/**
 * support for mobile devices
 */
(function($){
	var longTouchTimer = null;
	var dblTouchTimer = null;
	var isDblClick = false;
	
	function onTouchStart(e){
		if (e.touches.length != 1){return}
		if (!isDblClick){
			isDblClick = true;
			dblClickTimer = setTimeout(function(){
				isDblClick = false;
			}, 500);
		} else {
			clearTimeout(dblClickTimer);
			isDblClick = false;
			fire(e, 'dblclick');
//			e.preventDefault();
		}
		longTouchTimer = setTimeout(function(){
			fire(e, 'contextmenu', 3);
		}, 1000);
		fire(e, 'mousedown');
		if ($.fn.draggable.isDragging || $.fn.resizable.isResizing){
			e.preventDefault();
		}
	}
	function onTouchMove(e){
		if (e.touches.length != 1){return}
		if (longTouchTimer){
			clearTimeout(longTouchTimer);
		}
		fire(e, 'mousemove');
		if ($.fn.draggable.isDragging || $.fn.resizable.isResizing){
			e.preventDefault();
		}
	}
	function onTouchEnd(e){
//		if (e.touches.length > 0){return}
		if (longTouchTimer){
			clearTimeout(longTouchTimer);
		}
		fire(e, 'mouseup');
		if ($.fn.draggable.isDragging || $.fn.resizable.isResizing){
			e.preventDefault();
		}
	}
	
	function fire(e, name, which){
		var event = new $.Event(name);
		event.pageX = e.changedTouches[0].pageX;
		event.pageY = e.changedTouches[0].pageY;
		event.which = which || 1;
		$(e.target).trigger(event);
	}
	
	if (document.addEventListener){
		document.addEventListener("touchstart", onTouchStart, true);
		document.addEventListener("touchmove", onTouchMove, true);
		document.addEventListener("touchend", onTouchEnd, true);
	}
})(jQuery);
/**
 * draggable - jQuery EasyUI
 * 
 */
(function($){
//	var isDragging = false;
	function drag(e){
		var state = $.data(e.data.target, 'draggable');
		var opts = state.options;
		var proxy = state.proxy;
		
		var dragData = e.data;
		var left = dragData.startLeft + e.pageX - dragData.startX;
		var top = dragData.startTop + e.pageY - dragData.startY;
		
		if (proxy){
			if (proxy.parent()[0] == document.body){
				if (opts.deltaX != null && opts.deltaX != undefined){
					left = e.pageX + opts.deltaX;
				} else {
					left = e.pageX - e.data.offsetWidth;
				}
				if (opts.deltaY != null && opts.deltaY != undefined){
					top = e.pageY + opts.deltaY;
				} else {
					top = e.pageY - e.data.offsetHeight;
				}
			} else {
				if (opts.deltaX != null && opts.deltaX != undefined){
					left += e.data.offsetWidth + opts.deltaX;
				}
				if (opts.deltaY != null && opts.deltaY != undefined){
					top += e.data.offsetHeight + opts.deltaY;
				}
			}
		}
		
//		if (opts.deltaX != null && opts.deltaX != undefined){
//			left = e.pageX + opts.deltaX;
//		}
//		if (opts.deltaY != null && opts.deltaY != undefined){
//			top = e.pageY + opts.deltaY;
//		}
		
		if (e.data.parent != document.body) {
			left += $(e.data.parent).scrollLeft();
			top += $(e.data.parent).scrollTop();
		}
		
		if (opts.axis == 'h') {
			dragData.left = left;
		} else if (opts.axis == 'v') {
			dragData.top = top;
		} else {
			dragData.left = left;
			dragData.top = top;
		}
	}
	
	function applyDrag(e){
		var state = $.data(e.data.target, 'draggable');
		var opts = state.options;
		var proxy = state.proxy;
		if (!proxy){
			proxy = $(e.data.target);
		}
//		if (proxy){
//			proxy.css('cursor', opts.cursor);
//		} else {
//			proxy = $(e.data.target);
//			$.data(e.data.target, 'draggable').handle.css('cursor', opts.cursor);
//		}
		proxy.css({
			left:e.data.left,
			top:e.data.top
		});
		$('body').css('cursor', opts.cursor);
	}
	
	function doDown(e){
//		isDragging = true;
		$.fn.draggable.isDragging = true;
		var state = $.data(e.data.target, 'draggable');
		var opts = state.options;
		
		var droppables = $('.droppable').filter(function(){
			return e.data.target != this;
		}).filter(function(){
			var accept = $.data(this, 'droppable').options.accept;
			if (accept){
				return $(accept).filter(function(){
					return this == e.data.target;
				}).length > 0;
			} else {
				return true;
			}
		});
		state.droppables = droppables;
		
		var proxy = state.proxy;
		if (!proxy){
			if (opts.proxy){
				if (opts.proxy == 'clone'){
					proxy = $(e.data.target).clone().insertAfter(e.data.target);
				} else {
					proxy = opts.proxy.call(e.data.target, e.data.target);
				}
				state.proxy = proxy;
			} else {
				proxy = $(e.data.target);
			}
		}
		
		proxy.css('position', 'absolute');
		drag(e);
		applyDrag(e);
		
		opts.onStartDrag.call(e.data.target, e);
		return false;
	}
	
	function doMove(e){
		var state = $.data(e.data.target, 'draggable');
		drag(e);
		if (state.options.onDrag.call(e.data.target, e) != false){
			applyDrag(e);
		}
		
		var source = e.data.target;
		state.droppables.each(function(){
			var dropObj = $(this);
			if (dropObj.droppable('options').disabled){return;}
			
			var p2 = dropObj.offset();
			if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth()
					&& e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()){
				if (!this.entered){
					$(this).trigger('_dragenter', [source]);
					this.entered = true;
				}
				$(this).trigger('_dragover', [source]);
			} else {
				if (this.entered){
					$(this).trigger('_dragleave', [source]);
					this.entered = false;
				}
			}
		});
		
		return false;
	}
	
	function doUp(e){
//		isDragging = false;
		$.fn.draggable.isDragging = false;
//		drag(e);
		doMove(e);
		
		var state = $.data(e.data.target, 'draggable');
		var proxy = state.proxy;
		var opts = state.options;
		if (opts.revert){
			if (checkDrop() == true){
				$(e.data.target).css({
					position:e.data.startPosition,
					left:e.data.startLeft,
					top:e.data.startTop
				});
			} else {
				if (proxy){
					var left, top;
					if (proxy.parent()[0] == document.body){
						left = e.data.startX - e.data.offsetWidth;
						top = e.data.startY - e.data.offsetHeight;
					} else {
						left = e.data.startLeft;
						top = e.data.startTop;
					}
					proxy.animate({
						left: left,
						top: top
					}, function(){
						removeProxy();
					});
				} else {
					$(e.data.target).animate({
						left:e.data.startLeft,
						top:e.data.startTop
					}, function(){
						$(e.data.target).css('position', e.data.startPosition);
					});
				}
			}
		} else {
			$(e.data.target).css({
				position:'absolute',
				left:e.data.left,
				top:e.data.top
			});
			checkDrop();
		}
		
		opts.onStopDrag.call(e.data.target, e);
		
		$(document).unbind('.draggable');
		setTimeout(function(){
			$('body').css('cursor','');
		},100);
		
		function removeProxy(){
			if (proxy){
				proxy.remove();
			}
			state.proxy = null;
		}
		
		function checkDrop(){
			var dropped = false;
			state.droppables.each(function(){
				var dropObj = $(this);
				if (dropObj.droppable('options').disabled){return;}
				
				var p2 = dropObj.offset();
				if (e.pageX > p2.left && e.pageX < p2.left + dropObj.outerWidth()
						&& e.pageY > p2.top && e.pageY < p2.top + dropObj.outerHeight()){
					if (opts.revert){
						$(e.data.target).css({
							position:e.data.startPosition,
							left:e.data.startLeft,
							top:e.data.startTop
						});
					}
					$(this).trigger('_drop', [e.data.target]);
					removeProxy();
					dropped = true;
					this.entered = false;
					return false;
				}
			});
			if (!dropped && !opts.revert){
				removeProxy();
			}
			return dropped;
		}
		
		return false;
	}
	
	$.fn.draggable = function(options, param){
		if (typeof options == 'string'){
			return $.fn.draggable.methods[options](this, param);
		}
		
		return this.each(function(){
			var opts;
			var state = $.data(this, 'draggable');
			if (state) {
				state.handle.unbind('.draggable');
				opts = $.extend(state.options, options);
			} else {
				opts = $.extend({}, $.fn.draggable.defaults, $.fn.draggable.parseOptions(this), options || {});
			}
			var handle = opts.handle ? (typeof opts.handle=='string' ? $(opts.handle, this) : opts.handle) : $(this);
			
			$.data(this, 'draggable', {
				options: opts,
				handle: handle
			});
			
			if (opts.disabled) {
				$(this).css('cursor', '');
				return;
			}
			
			handle.unbind('.draggable').bind('mousemove.draggable', {target:this}, function(e){
//				if (isDragging) return;
				if ($.fn.draggable.isDragging){return}
				var opts = $.data(e.data.target, 'draggable').options;
				if (checkArea(e)){
					$(this).css('cursor', opts.cursor);
				} else {
					$(this).css('cursor', '');
				}
			}).bind('mouseleave.draggable', {target:this}, function(e){
				$(this).css('cursor', '');
			}).bind('mousedown.draggable', {target:this}, function(e){
				if (checkArea(e) == false) return;
				$(this).css('cursor', '');

				var position = $(e.data.target).position();
				var offset = $(e.data.target).offset();
				var data = {
					startPosition: $(e.data.target).css('position'),
					startLeft: position.left,
					startTop: position.top,
					left: position.left,
					top: position.top,
					startX: e.pageX,
					startY: e.pageY,
					offsetWidth: (e.pageX - offset.left),
					offsetHeight: (e.pageY - offset.top),
					target: e.data.target,
					parent: $(e.data.target).parent()[0]
				};
				
				$.extend(e.data, data);
				var opts = $.data(e.data.target, 'draggable').options;
				if (opts.onBeforeDrag.call(e.data.target, e) == false) return;
				
				$(document).bind('mousedown.draggable', e.data, doDown);
				$(document).bind('mousemove.draggable', e.data, doMove);
				$(document).bind('mouseup.draggable', e.data, doUp);
//				$('body').css('cursor', opts.cursor);
			});
			
			// check if the handle can be dragged
			function checkArea(e) {
				var state = $.data(e.data.target, 'draggable');
				var handle = state.handle;
				var offset = $(handle).offset();
				var width = $(handle).outerWidth();
				var height = $(handle).outerHeight();
				var t = e.pageY - offset.top;
				var r = offset.left + width - e.pageX;
				var b = offset.top + height - e.pageY;
				var l = e.pageX - offset.left;
				
				return Math.min(t,r,b,l) > state.options.edge;
			}
			
		});
	};
	
	$.fn.draggable.methods = {
		options: function(jq){
			return $.data(jq[0], 'draggable').options;
		},
		proxy: function(jq){
			return $.data(jq[0], 'draggable').proxy;
		},
		enable: function(jq){
			return jq.each(function(){
				$(this).draggable({disabled:false});
			});
		},
		disable: function(jq){
			return jq.each(function(){
				$(this).draggable({disabled:true});
			});
		}
	};
	
	$.fn.draggable.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, 
				$.parser.parseOptions(target, ['cursor','handle','axis',
				       {'revert':'boolean','deltaX':'number','deltaY':'number','edge':'number'}]), {
			disabled: (t.attr('disabled') ? true : undefined)
		});
	};
	
	$.fn.draggable.defaults = {
		proxy:null,	// 'clone' or a function that will create the proxy object, 
					// the function has the source parameter that indicate the source object dragged.
		revert:false,
		cursor:'move',
		deltaX:null,
		deltaY:null,
		handle: null,
		disabled: false,
		edge:0,
		axis:null,	// v or h
		
		onBeforeDrag: function(e){},
		onStartDrag: function(e){},
		onDrag: function(e){},
		onStopDrag: function(e){}
	};
	
	$.fn.draggable.isDragging = false;
	
//	$(function(){
//		function touchHandler(e) {
//			var touches = e.changedTouches, first = touches[0], type = "";
//
//			switch(e.type) {
//				case "touchstart": type = "mousedown"; break;
//				case "touchmove":  type = "mousemove"; break;        
//				case "touchend":   type = "mouseup";   break;
//				default: return;
//			}
//			var simulatedEvent = document.createEvent("MouseEvent");
//			simulatedEvent.initMouseEvent(type, true, true, window, 1,
//									  first.screenX, first.screenY,
//									  first.clientX, first.clientY, false,
//									  false, false, false, 0/*left*/, null);
//
//			first.target.dispatchEvent(simulatedEvent);
//			if (isDragging){
//				e.preventDefault();
//			}
//		}
//		
//		if (document.addEventListener){
//			document.addEventListener("touchstart", touchHandler, true);
//			document.addEventListener("touchmove", touchHandler, true);
//			document.addEventListener("touchend", touchHandler, true);
//			document.addEventListener("touchcancel", touchHandler, true); 
//		}
//	});
})(jQuery);
/**
 * droppable - jQuery EasyUI
 * 
 */
(function($){
	function init(target){
		$(target).addClass('droppable');
		$(target).bind('_dragenter', function(e, source){
			$.data(target, 'droppable').options.onDragEnter.apply(target, [e, source]);
		});
		$(target).bind('_dragleave', function(e, source){
			$.data(target, 'droppable').options.onDragLeave.apply(target, [e, source]);
		});
		$(target).bind('_dragover', function(e, source){
			$.data(target, 'droppable').options.onDragOver.apply(target, [e, source]);
		});
		$(target).bind('_drop', function(e, source){
			$.data(target, 'droppable').options.onDrop.apply(target, [e, source]);
		});
	}
	
	$.fn.droppable = function(options, param){
		if (typeof options == 'string'){
			return $.fn.droppable.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'droppable');
			if (state){
				$.extend(state.options, options);
			} else {
				init(this);
				$.data(this, 'droppable', {
					options: $.extend({}, $.fn.droppable.defaults, $.fn.droppable.parseOptions(this), options)
				});
			}
		});
	};
	
	$.fn.droppable.methods = {
		options: function(jq){
			return $.data(jq[0], 'droppable').options;
		},
		enable: function(jq){
			return jq.each(function(){
				$(this).droppable({disabled:false});
			});
		},
		disable: function(jq){
			return jq.each(function(){
				$(this).droppable({disabled:true});
			});
		}
	};
	
	$.fn.droppable.parseOptions = function(target){
		var t = $(target);
		return $.extend({},	$.parser.parseOptions(target, ['accept']), {
			disabled: (t.attr('disabled') ? true : undefined)
		});
	};
	
	$.fn.droppable.defaults = {
		accept:null,
		disabled:false,
		onDragEnter:function(e, source){},
		onDragOver:function(e, source){},
		onDragLeave:function(e, source){},
		onDrop:function(e, source){}
	};
})(jQuery);

/**
 * resizable - jQuery EasyUI
 * 
 */
(function($){
//	var isResizing = false;
	$.fn.resizable = function(options, param){
		if (typeof options == 'string'){
			return $.fn.resizable.methods[options](this, param);
		}
		
		function resize(e){
			var resizeData = e.data;
			var options = $.data(resizeData.target, 'resizable').options;
			if (resizeData.dir.indexOf('e') != -1) {
				var width = resizeData.startWidth + e.pageX - resizeData.startX;
				width = Math.min(
							Math.max(width, options.minWidth),
							options.maxWidth
						);
				resizeData.width = width;
			}
			if (resizeData.dir.indexOf('s') != -1) {
				var height = resizeData.startHeight + e.pageY - resizeData.startY;
				height = Math.min(
						Math.max(height, options.minHeight),
						options.maxHeight
				);
				resizeData.height = height;
			}
			if (resizeData.dir.indexOf('w') != -1) {
				var width = resizeData.startWidth - e.pageX + resizeData.startX;
				width = Math.min(
							Math.max(width, options.minWidth),
							options.maxWidth
						);
				resizeData.width = width;
				resizeData.left = resizeData.startLeft + resizeData.startWidth - resizeData.width;
				
//				resizeData.width = resizeData.startWidth - e.pageX + resizeData.startX;
//				if (resizeData.width >= options.minWidth && resizeData.width <= options.maxWidth) {
//					resizeData.left = resizeData.startLeft + e.pageX - resizeData.startX;
//				}
			}
			if (resizeData.dir.indexOf('n') != -1) {
				var height = resizeData.startHeight - e.pageY + resizeData.startY;
				height = Math.min(
							Math.max(height, options.minHeight),
							options.maxHeight
						);
				resizeData.height = height;
				resizeData.top = resizeData.startTop + resizeData.startHeight - resizeData.height;
				
//				resizeData.height = resizeData.startHeight - e.pageY + resizeData.startY;
//				if (resizeData.height >= options.minHeight && resizeData.height <= options.maxHeight) {
//					resizeData.top = resizeData.startTop + e.pageY - resizeData.startY;
//				}
			}
		}
		
		function applySize(e){
			var resizeData = e.data;
			var t = $(resizeData.target);
			t.css({
				left: resizeData.left,
				top: resizeData.top
			});
			if (t.outerWidth() != resizeData.width){t._outerWidth(resizeData.width)}
			if (t.outerHeight() != resizeData.height){t._outerHeight(resizeData.height)}
//			t._outerWidth(resizeData.width)._outerHeight(resizeData.height);
		}
		
		function doDown(e){
//			isResizing = true;
			$.fn.resizable.isResizing = true;
			$.data(e.data.target, 'resizable').options.onStartResize.call(e.data.target, e);
			return false;
		}
		
		function doMove(e){
			resize(e);
			if ($.data(e.data.target, 'resizable').options.onResize.call(e.data.target, e) != false){
				applySize(e)
			}
			return false;
		}
		
		function doUp(e){
//			isResizing = false;
			$.fn.resizable.isResizing = false;
			resize(e, true);
			applySize(e);
			$.data(e.data.target, 'resizable').options.onStopResize.call(e.data.target, e);
			$(document).unbind('.resizable');
			$('body').css('cursor','');
//			$('body').css('cursor','auto');
			return false;
		}
		
		return this.each(function(){
			var opts = null;
			var state = $.data(this, 'resizable');
			if (state) {
				$(this).unbind('.resizable');
				opts = $.extend(state.options, options || {});
			} else {
				opts = $.extend({}, $.fn.resizable.defaults, $.fn.resizable.parseOptions(this), options || {});
				$.data(this, 'resizable', {
					options:opts
				});
			}
			
			if (opts.disabled == true) {
				return;
			}
			
			// bind mouse event using namespace resizable
			$(this).bind('mousemove.resizable', {target:this}, function(e){
//				if (isResizing) return;
				if ($.fn.resizable.isResizing){return}
				var dir = getDirection(e);
				if (dir == '') {
					$(e.data.target).css('cursor', '');
				} else {
					$(e.data.target).css('cursor', dir + '-resize');
				}
			}).bind('mouseleave.resizable', {target:this}, function(e){
				$(e.data.target).css('cursor', '');
			}).bind('mousedown.resizable', {target:this}, function(e){
				var dir = getDirection(e);
				if (dir == '') return;
				
				function getCssValue(css) {
					var val = parseInt($(e.data.target).css(css));
					if (isNaN(val)) {
						return 0;
					} else {
						return val;
					}
				}
				
				var data = {
					target: e.data.target,
					dir: dir,
					startLeft: getCssValue('left'),
					startTop: getCssValue('top'),
					left: getCssValue('left'),
					top: getCssValue('top'),
					startX: e.pageX,
					startY: e.pageY,
					startWidth: $(e.data.target).outerWidth(),
					startHeight: $(e.data.target).outerHeight(),
					width: $(e.data.target).outerWidth(),
					height: $(e.data.target).outerHeight(),
					deltaWidth: $(e.data.target).outerWidth() - $(e.data.target).width(),
					deltaHeight: $(e.data.target).outerHeight() - $(e.data.target).height()
				};
				$(document).bind('mousedown.resizable', data, doDown);
				$(document).bind('mousemove.resizable', data, doMove);
				$(document).bind('mouseup.resizable', data, doUp);
				$('body').css('cursor', dir+'-resize');
			});
			
			// get the resize direction
			function getDirection(e) {
				var tt = $(e.data.target);
				var dir = '';
				var offset = tt.offset();
				var width = tt.outerWidth();
				var height = tt.outerHeight();
				var edge = opts.edge;
				if (e.pageY > offset.top && e.pageY < offset.top + edge) {
					dir += 'n';
				} else if (e.pageY < offset.top + height && e.pageY > offset.top + height - edge) {
					dir += 's';
				}
				if (e.pageX > offset.left && e.pageX < offset.left + edge) {
					dir += 'w';
				} else if (e.pageX < offset.left + width && e.pageX > offset.left + width - edge) {
					dir += 'e';
				}
				
				var handles = opts.handles.split(',');
				for(var i=0; i<handles.length; i++) {
					var handle = handles[i].replace(/(^\s*)|(\s*$)/g, '');
					if (handle == 'all' || handle == dir) {
						return dir;
					}
				}
				return '';
			}
			
			
		});
	};
	
	$.fn.resizable.methods = {
		options: function(jq){
			return $.data(jq[0], 'resizable').options;
		},
		enable: function(jq){
			return jq.each(function(){
				$(this).resizable({disabled:false});
			});
		},
		disable: function(jq){
			return jq.each(function(){
				$(this).resizable({disabled:true});
			});
		}
	};
	
	$.fn.resizable.parseOptions = function(target){
		var t = $(target);
		return $.extend({},
				$.parser.parseOptions(target, [
					'handles',{minWidth:'number',minHeight:'number',maxWidth:'number',maxHeight:'number',edge:'number'}
				]), {
			disabled: (t.attr('disabled') ? true : undefined)
		})
	};
	
	$.fn.resizable.defaults = {
		disabled:false,
		handles:'n, e, s, w, ne, se, sw, nw, all',
		minWidth: 10,
		minHeight: 10,
		maxWidth: 10000,//$(document).width(),
		maxHeight: 10000,//$(document).height(),
		edge:5,
		onStartResize: function(e){},
		onResize: function(e){},
		onStopResize: function(e){}
	};
	
	$.fn.resizable.isResizing = false;
	
})(jQuery);
/**
 * linkbutton - jQuery EasyUI
 * 
 */
(function($){
	function setSize(target, param){
		var opts = $.data(target, 'linkbutton').options;
		if (param){
			$.extend(opts, param);
		}
		if (opts.width || opts.height || opts.fit){
			var btn = $(target);
			var parent = btn.parent();
			var isVisible = btn.is(':visible');
			if (!isVisible){
				var spacer = $('<div style="display:none"></div>').insertBefore(target);
				var style = {
					position: btn.css('position'),
					display: btn.css('display'),
					left: btn.css('left')
				};
				btn.appendTo('body');
				btn.css({
					position: 'absolute',
					display: 'inline-block',
					left: -20000
				});
			}
			btn._size(opts, parent);
			var left = btn.find('.l-btn-left');
			left.css('margin-top', 0);
			left.css('margin-top', parseInt((btn.height()-left.height())/2)+'px');
			if (!isVisible){
				btn.insertAfter(spacer);
				btn.css(style);
				spacer.remove();
			}
		}
	}
	
	function createButton(target) {
		var opts = $.data(target, 'linkbutton').options;
		var t = $(target).empty();
		
		t.addClass('l-btn').removeClass('l-btn-plain l-btn-selected l-btn-plain-selected');
		t.removeClass('l-btn-small l-btn-medium l-btn-large').addClass('l-btn-'+opts.size);
		if (opts.plain){t.addClass('l-btn-plain')}
		if (opts.selected){
			t.addClass(opts.plain ? 'l-btn-selected l-btn-plain-selected' : 'l-btn-selected');
		}
		t.attr('group', opts.group || '');
		t.attr('id', opts.id || '');
		
		var inner = $('<span class="l-btn-left"></span>').appendTo(t);
		if (opts.text){
			$('<span class="l-btn-text"></span>').html(opts.text).appendTo(inner);
		} else {
			$('<span class="l-btn-text l-btn-empty">&nbsp;</span>').appendTo(inner);
		}
		if (opts.iconCls){
			$('<span class="l-btn-icon">&nbsp;</span>').addClass(opts.iconCls).appendTo(inner);
			inner.addClass('l-btn-icon-'+opts.iconAlign);
		}
		
		t.unbind('.linkbutton').bind('focus.linkbutton',function(){
			if (!opts.disabled){
				$(this).addClass('l-btn-focus');
			}
		}).bind('blur.linkbutton',function(){
			$(this).removeClass('l-btn-focus');
		}).bind('click.linkbutton',function(){
			if (!opts.disabled){
				if (opts.toggle){
					if (opts.selected){
						$(this).linkbutton('unselect');
					} else {
						$(this).linkbutton('select');
					}
				}
				opts.onClick.call(this);
			}
//			return false;
		});
//		if (opts.toggle && !opts.disabled){
//			t.bind('click.linkbutton', function(){
//				if (opts.selected){
//					$(this).linkbutton('unselect');
//				} else {
//					$(this).linkbutton('select');
//				}
//			});
//		}
		
		setSelected(target, opts.selected)
		setDisabled(target, opts.disabled);
	}
	
	function setSelected(target, selected){
		var opts = $.data(target, 'linkbutton').options;
		if (selected){
			if (opts.group){
				$('a.l-btn[group="'+opts.group+'"]').each(function(){
					var o = $(this).linkbutton('options');
					if (o.toggle){
						$(this).removeClass('l-btn-selected l-btn-plain-selected');
						o.selected = false;
					}
				});
			}
			$(target).addClass(opts.plain ? 'l-btn-selected l-btn-plain-selected' : 'l-btn-selected');
			opts.selected = true;
		} else {
			if (!opts.group){
				$(target).removeClass('l-btn-selected l-btn-plain-selected');
				opts.selected = false;
			}
		}
	}
	
	function setDisabled(target, disabled){
		var state = $.data(target, 'linkbutton');
		var opts = state.options;
		$(target).removeClass('l-btn-disabled l-btn-plain-disabled');
		if (disabled){
			opts.disabled = true;
			var href = $(target).attr('href');
			if (href){
				state.href = href;
				$(target).attr('href', 'javascript:void(0)');
			}
			if (target.onclick){
				state.onclick = target.onclick;
				target.onclick = null;
			}
			opts.plain ? $(target).addClass('l-btn-disabled l-btn-plain-disabled') : $(target).addClass('l-btn-disabled');
		} else {
			opts.disabled = false;
			if (state.href) {
				$(target).attr('href', state.href);
			}
			if (state.onclick) {
				target.onclick = state.onclick;
			}
		}
	}
	
	$.fn.linkbutton = function(options, param){
		if (typeof options == 'string'){
			return $.fn.linkbutton.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'linkbutton');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'linkbutton', {
					options: $.extend({}, $.fn.linkbutton.defaults, $.fn.linkbutton.parseOptions(this), options)
				});
				$(this).removeAttr('disabled');
				$(this).bind('_resize', function(e, force){
					if ($(this).hasClass('easyui-fluid') || force){
						setSize(this);
					}
					return false;
				});
			}
			
			createButton(this);
			setSize(this);
		});
	};
	
	$.fn.linkbutton.methods = {
		options: function(jq){
			return $.data(jq[0], 'linkbutton').options;
		},
		resize: function(jq, param){
			return jq.each(function(){
				setSize(this, param);
			});
		},
		enable: function(jq){
			return jq.each(function(){
				setDisabled(this, false);
			});
		},
		disable: function(jq){
			return jq.each(function(){
				setDisabled(this, true);
			});
		},
		select: function(jq){
			return jq.each(function(){
				setSelected(this, true);
			});
		},
		unselect: function(jq){
			return jq.each(function(){
				setSelected(this, false);
			});
		}
	};
	
	$.fn.linkbutton.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, 
			['id','iconCls','iconAlign','group','size',{plain:'boolean',toggle:'boolean',selected:'boolean'}]
		), {
			disabled: (t.attr('disabled') ? true : undefined),
			text: $.trim(t.html()),
			iconCls: (t.attr('icon') || t.attr('iconCls'))
		});
	};
	
	$.fn.linkbutton.defaults = {
		id: null,
		disabled: false,
		toggle: false,
		selected: false,
		group: null,
		plain: false,
		text: '',
		iconCls: null,
		iconAlign: 'left',
		size: 'small',	// small,large
		onClick: function(){}
	};
	
})(jQuery);

(function($) {
	function _a0(_a1) {
		var _a2 = $.data(_a1, "pagination");
		var _a3 = _a2.options;
		var bb = _a2.bb = {};
		var _a4 = $(_a1)
				.addClass("pagination")
				.html(
						"<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tr></tr></table>");
		var tr = _a4.find("tr");
		var aa = $.extend([], _a3.layout);
		if (!_a3.showPageList) {
			_a5(aa, "list");
		}
		if (!_a3.showRefresh) {
			_a5(aa, "refresh");
		}
		if (aa[0] == "sep") {
			aa.shift();
		}
		if (aa[aa.length - 1] == "sep") {
			aa.pop();
		}
		for ( var _a6 = 0; _a6 < aa.length; _a6++) {
			var _a7 = aa[_a6];
			if (_a7 == "list") {
				var ps = $("<select class=\"pagination-page-list\"></select>");
				ps.bind("change", function() {
					_a3.pageSize = parseInt($(this).val());
					_a3.onChangePageSize.call(_a1, _a3.pageSize);
					_ad(_a1, _a3.pageNumber);
				});
				for ( var i = 0; i < _a3.pageList.length; i++) {
					$("<option></option>").text(_a3.pageList[i]).appendTo(ps);
				}
				$("<td></td>").append(ps).appendTo(tr);
			} else {
				if (_a7 == "sep") {
					$("<td><div class=\"pagination-btn-separator\"></div></td>")
							.appendTo(tr);
				} else {
					if (_a7 == "first") {
						bb.first = _a8("first");
					} else {
						if (_a7 == "prev") {
							bb.prev = _a8("prev");
						} else {
							if (_a7 == "next") {
								bb.next = _a8("next");
							} else {
								if (_a7 == "last") {
									bb.last = _a8("last");
								} else {
									if (_a7 == "manual") {
										$(
												"<span style=\"padding-left:6px;\"></span>")
												.html(_a3.beforePageText)
												.appendTo(tr).wrap("<td></td>");
										bb.num = $(
												"<input class=\"pagination-num\" type=\"text\" value=\"1\" size=\"2\">")
												.appendTo(tr).wrap("<td></td>");
										bb.num
												.unbind(".pagination")
												.bind(
														"keydown.pagination",
														function(e) {
															if (e.keyCode == 13) {
																var _a9 = parseInt($(
																		this)
																		.val()) || 1;
																_ad(_a1, _a9);
																return false;
															}
														});
										bb.after = $(
												"<span style=\"padding-right:6px;\"></span>")
												.appendTo(tr).wrap("<td></td>");
									} else {
										if (_a7 == "refresh") {
											bb.refresh = _a8("refresh");
										} else {
											if (_a7 == "links") {
												$(
														"<td class=\"pagination-links\"></td>")
														.appendTo(tr);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		if (_a3.buttons) {
			$("<td><div class=\"pagination-btn-separator\"></div></td>")
					.appendTo(tr);
			if ($.isArray(_a3.buttons)) {
				for ( var i = 0; i < _a3.buttons.length; i++) {
					var btn = _a3.buttons[i];
					if (btn == "-") {
						$(
								"<td><div class=\"pagination-btn-separator\"></div></td>")
								.appendTo(tr);
					} else {
						var td = $("<td></td>").appendTo(tr);
						var a = $("<a href=\"javascript:void(0)\"></a>")
								.appendTo(td);
						a[0].onclick = eval(btn.handler || function() {
						});
						a.linkbutton($.extend({}, btn, {
							plain : true
						}));
					}
				}
			} else {
				var td = $("<td></td>").appendTo(tr);
				$(_a3.buttons).appendTo(td).show();
			}
		}
		$("<div class=\"pagination-info\"></div>").appendTo(_a4);
		$("<div style=\"clear:both;\"></div>").appendTo(_a4);
		function _a8(_aa) {
			var btn = _a3.nav[_aa];
			var a = $("<a href=\"javascript:void(0)\"></a>").appendTo(tr);
			a.wrap("<td></td>");
			a.linkbutton({
				iconCls : btn.iconCls,
				plain : true
			}).unbind(".pagination").bind("click.pagination", function() {
				btn.handler.call(_a1);
			});
			return a;
		}
		;
		function _a5(aa, _ab) {
			var _ac = $.inArray(_ab, aa);
			if (_ac >= 0) {
				aa.splice(_ac, 1);
			}
			return aa;
		}
		;
	}
	;
	function _ad(_ae, _af) {
		var _b0 = $.data(_ae, "pagination").options;
		_b1(_ae, {
			pageNumber : _af
		});
		_b0.onSelectPage.call(_ae, _b0.pageNumber, _b0.pageSize);
	}
	;
	function _b1(_b2, _b3) {
		var _b4 = $.data(_b2, "pagination");
		var _b5 = _b4.options;
		var bb = _b4.bb;
		$.extend(_b5, _b3 || {});
		var ps = $(_b2).find("select.pagination-page-list");
		if (ps.length) {
			ps.val(_b5.pageSize + "");
			_b5.pageSize = parseInt(ps.val());
		}
		var _b6 = Math.ceil(_b5.total / _b5.pageSize) || 1;
		if (_b5.pageNumber < 1) {
			_b5.pageNumber = 1;
		}
		if (_b5.pageNumber > _b6) {
			_b5.pageNumber = _b6;
		}
		if (_b5.total == 0) {
			_b5.pageNumber = 0;
			_b6 = 0;
		}
		if (bb.num) {
			bb.num.val(_b5.pageNumber);
		}
		if (bb.after) {
			bb.after.html(_b5.afterPageText.replace(/{pages}/, _b6));
		}
		var td = $(_b2).find("td.pagination-links");
		if (td.length) {
			td.empty();
			var _b7 = _b5.pageNumber - Math.floor(_b5.links / 2);
			if (_b7 < 1) {
				_b7 = 1;
			}
			var _b8 = _b7 + _b5.links - 1;
			if (_b8 > _b6) {
				_b8 = _b6;
			}
			_b7 = _b8 - _b5.links + 1;
			if (_b7 < 1) {
				_b7 = 1;
			}
			for ( var i = _b7; i <= _b8; i++) {
				var a = $(
						"<a class=\"pagination-link\" href=\"javascript:void(0)\"></a>")
						.appendTo(td);
				a.linkbutton({
					plain : true,
					text : i
				});
				if (i == _b5.pageNumber) {
					a.linkbutton("select");
				} else {
					a.unbind(".pagination").bind("click.pagination", {
						pageNumber : i
					}, function(e) {
						_ad(_b2, e.data.pageNumber);
					});
				}
			}
		}
		var _b9 = _b5.displayMsg;
		_b9 = _b9.replace(/{from}/, _b5.total == 0 ? 0 : _b5.pageSize
				* (_b5.pageNumber - 1) + 1);
		_b9 = _b9.replace(/{to}/, Math.min(_b5.pageSize * (_b5.pageNumber),
				_b5.total));
		_b9 = _b9.replace(/{total}/, _b5.total);
		$(_b2).find("div.pagination-info").html(_b9);
		if (bb.first) {
			bb.first.linkbutton({
				disabled : ((!_b5.total) || _b5.pageNumber == 1)
			});
		}
		if (bb.prev) {
			bb.prev.linkbutton({
				disabled : ((!_b5.total) || _b5.pageNumber == 1)
			});
		}
		if (bb.next) {
			bb.next.linkbutton({
				disabled : (_b5.pageNumber == _b6)
			});
		}
		if (bb.last) {
			bb.last.linkbutton({
				disabled : (_b5.pageNumber == _b6)
			});
		}
		_ba(_b2, _b5.loading);
	}
	;
	function _ba(_bb, _bc) {
		var _bd = $.data(_bb, "pagination");
		var _be = _bd.options;
		_be.loading = _bc;
		if (_be.showRefresh && _bd.bb.refresh) {
			_bd.bb.refresh.linkbutton({
				iconCls : (_be.loading ? "pagination-loading"
						: "pagination-load")
			});
		}
	}
	;
	$.fn.pagination = function(_bf, _c0) {
		if (typeof _bf == "string") {
			return $.fn.pagination.methods[_bf](this, _c0);
		}
		_bf = _bf || {};
		return this.each(function() {
			var _c1;
			var _c2 = $.data(this, "pagination");
			if (_c2) {
				_c1 = $.extend(_c2.options, _bf);
			} else {
				_c1 = $.extend({}, $.fn.pagination.defaults, $.fn.pagination
						.parseOptions(this), _bf);
				$.data(this, "pagination", {
					options : _c1
				});
			}
			_a0(this);
			_b1(this);
		});
	};
	$.fn.pagination.methods = {
		options : function(jq) {
			return $.data(jq[0], "pagination").options;
		},
		loading : function(jq) {
			return jq.each(function() {
				_ba(this, true);
			});
		},
		loaded : function(jq) {
			return jq.each(function() {
				_ba(this, false);
			});
		},
		refresh : function(jq, _c3) {
			return jq.each(function() {
				_b1(this, _c3);
			});
		},
		select : function(jq, _c4) {
			return jq.each(function() {
				_ad(this, _c4);
			});
		}
	};
	$.fn.pagination.parseOptions = function(_c5) {
		var t = $(_c5);
		return $.extend({}, $.parser.parseOptions(_c5, [ {
			total : "number",
			pageSize : "number",
			pageNumber : "number",
			links : "number"
		}, {
			loading : "boolean",
			showPageList : "boolean",
			showRefresh : "boolean"
		} ]), {
			pageList : (t.attr("pageList") ? eval(t.attr("pageList"))
					: undefined)
		});
	};
	$.fn.pagination.defaults = {
		total : 1,
		pageSize : 10,
		pageNumber : 1,
		pageList : [ 10, 20, 30, 50 ],
		loading : false,
		buttons : null,
		showPageList : true,
		showRefresh : true,
		links : 10,
		layout : [ "list", "sep", "first", "prev", "sep", "manual", "sep",
				"next", "last", "sep", "refresh" ],
		onSelectPage : function(_c6, _c7) {
		},
		onBeforeRefresh : function(_c8, _c9) {
		},
		onRefresh : function(_ca, _cb) {
		},
		onChangePageSize : function(_cc) {
		},
		beforePageText : "Page",
		afterPageText : "of {pages}",
		displayMsg : "Displaying {from} to {to} of {total} items",
		nav : {
			first : {
				iconCls : "pagination-first",
				handler : function() {
					var _cd = $(this).pagination("options");
					if (_cd.pageNumber > 1) {
						$(this).pagination("select", 1);
					}
				}
			},
			prev : {
				iconCls : "pagination-prev",
				handler : function() {
					var _ce = $(this).pagination("options");
					if (_ce.pageNumber > 1) {
						$(this).pagination("select", _ce.pageNumber - 1);
					}
				}
			},
			next : {
				iconCls : "pagination-next",
				handler : function() {
					var _cf = $(this).pagination("options");
					var _d0 = Math.ceil(_cf.total / _cf.pageSize);
					if (_cf.pageNumber < _d0) {
						$(this).pagination("select", _cf.pageNumber + 1);
					}
				}
			},
			last : {
				iconCls : "pagination-last",
				handler : function() {
					var _d1 = $(this).pagination("options");
					var _d2 = Math.ceil(_d1.total / _d1.pageSize);
					if (_d1.pageNumber < _d2) {
						$(this).pagination("select", _d2);
					}
				}
			},
			refresh : {
				iconCls : "pagination-refresh",
				handler : function() {
					var _d3 = $(this).pagination("options");
					if (_d3.onBeforeRefresh.call(this, _d3.pageNumber,
							_d3.pageSize) != false) {
						$(this).pagination("select", _d3.pageNumber);
						_d3.onRefresh.call(this, _d3.pageNumber, _d3.pageSize);
					}
				}
			}
		}
	};
})(jQuery);
(function($) {
	function _d4(_d5) {
		var _d6 = $(_d5);
		_d6.addClass("tree");
		return _d6;
	}
	;
	function _d7(_d8) {
		var _d9 = $.data(_d8, "tree").options;
		$(_d8).unbind().bind("mouseover", function(e) {
			var tt = $(e.target);
			var _da = tt.closest("div.tree-node");
			if (!_da.length) {
				return;
			}
			_da.addClass("tree-node-hover");
			if (tt.hasClass("tree-hit")) {
				if (tt.hasClass("tree-expanded")) {
					tt.addClass("tree-expanded-hover");
				} else {
					tt.addClass("tree-collapsed-hover");
				}
			}
			e.stopPropagation();
		}).bind("mouseout", function(e) {
			var tt = $(e.target);
			var _db = tt.closest("div.tree-node");
			if (!_db.length) {
				return;
			}
			_db.removeClass("tree-node-hover");
			if (tt.hasClass("tree-hit")) {
				if (tt.hasClass("tree-expanded")) {
					tt.removeClass("tree-expanded-hover");
				} else {
					tt.removeClass("tree-collapsed-hover");
				}
			}
			e.stopPropagation();
		}).bind("click", function(e) {
			var tt = $(e.target);
			var _dc = tt.closest("div.tree-node");
			if (!_dc.length) {
				return;
			}
			if (tt.hasClass("tree-hit")) {
				_13b(_d8, _dc[0]);
				return false;
			} else {
				if (tt.hasClass("tree-checkbox")) {
					_104(_d8, _dc[0], !tt.hasClass("tree-checkbox1"));
					return false;
				} else {
					_181(_d8, _dc[0]);
					_d9.onClick.call(_d8, _df(_d8, _dc[0]));
				}
			}
			e.stopPropagation();
		}).bind("dblclick", function(e) {
			var _dd = $(e.target).closest("div.tree-node");
			if (!_dd.length) {
				return;
			}
			_181(_d8, _dd[0]);
			_d9.onDblClick.call(_d8, _df(_d8, _dd[0]));
			e.stopPropagation();
		}).bind("contextmenu", function(e) {
			var _de = $(e.target).closest("div.tree-node");
			if (!_de.length) {
				return;
			}
			_d9.onContextMenu.call(_d8, e, _df(_d8, _de[0]));
			e.stopPropagation();
		});
	}
	;
	function _e0(_e1) {
		var _e2 = $.data(_e1, "tree").options;
		_e2.dnd = false;
		var _e3 = $(_e1).find("div.tree-node");
		_e3.draggable("disable");
		_e3.css("cursor", "pointer");
	}
	;
	function _e4(_e5) {
		var _e6 = $.data(_e5, "tree");
		var _e7 = _e6.options;
		var _e8 = _e6.tree;
		_e6.disabledNodes = [];
		_e7.dnd = true;
		_e8
				.find("div.tree-node")
				.draggable(
						{
							disabled : false,
							revert : true,
							cursor : "pointer",
							proxy : function(_e9) {
								var p = $(
										"<div class=\"tree-node-proxy\"></div>")
										.appendTo("body");
								p
										.html("<span class=\"tree-dnd-icon tree-dnd-no\">&nbsp;</span>"
												+ $(_e9).find(".tree-title")
														.html());
								p.hide();
								return p;
							},
							deltaX : 15,
							deltaY : 15,
							onBeforeDrag : function(e) {
								if (_e7.onBeforeDrag.call(_e5, _df(_e5, this)) == false) {
									return false;
								}
								if ($(e.target).hasClass("tree-hit")
										|| $(e.target)
												.hasClass("tree-checkbox")) {
									return false;
								}
								if (e.which != 1) {
									return false;
								}
								$(this).next("ul").find("div.tree-node")
										.droppable({
											accept : "no-accept"
										});
								var _ea = $(this).find("span.tree-indent");
								if (_ea.length) {
									e.data.offsetWidth -= _ea.length
											* _ea.width();
								}
							},
							onStartDrag : function() {
								$(this).draggable("proxy").css({
									left : -10000,
									top : -10000
								});
								_e7.onStartDrag.call(_e5, _df(_e5, this));
								var _eb = _df(_e5, this);
								if (_eb.id == undefined) {
									_eb.id = "easyui_tree_node_id_temp";
									_11e(_e5, _eb);
								}
								_e6.draggingNodeId = _eb.id;
							},
							onDrag : function(e) {
								var x1 = e.pageX, y1 = e.pageY, x2 = e.data.startX, y2 = e.data.startY;
								var d = Math.sqrt((x1 - x2) * (x1 - x2)
										+ (y1 - y2) * (y1 - y2));
								if (d > 3) {
									$(this).draggable("proxy").show();
								}
								this.pageY = e.pageY;
							},
							onStopDrag : function() {
								$(this).next("ul").find("div.tree-node")
										.droppable({
											accept : "div.tree-node"
										});
								for ( var i = 0; i < _e6.disabledNodes.length; i++) {
									$(_e6.disabledNodes[i]).droppable("enable");
								}
								_e6.disabledNodes = [];
								var _ec = _179(_e5, _e6.draggingNodeId);
								if (_ec && _ec.id == "easyui_tree_node_id_temp") {
									_ec.id = "";
									_11e(_e5, _ec);
								}
								_e7.onStopDrag.call(_e5, _ec);
							}
						})
				.droppable(
						{
							accept : "div.tree-node",
							onDragEnter : function(e, _ed) {
								if (_e7.onDragEnter.call(_e5, this, _ee(_ed)) == false) {
									_ef(_ed, false);
									$(this)
											.removeClass(
													"tree-node-append tree-node-top tree-node-bottom");
									$(this).droppable("disable");
									_e6.disabledNodes.push(this);
								}
							},
							onDragOver : function(e, _f0) {
								if ($(this).droppable("options").disabled) {
									return;
								}
								var _f1 = _f0.pageY;
								var top = $(this).offset().top;
								var _f2 = top + $(this).outerHeight();
								_ef(_f0, true);
								$(this)
										.removeClass(
												"tree-node-append tree-node-top tree-node-bottom");
								if (_f1 > top + (_f2 - top) / 2) {
									if (_f2 - _f1 < 5) {
										$(this).addClass("tree-node-bottom");
									} else {
										$(this).addClass("tree-node-append");
									}
								} else {
									if (_f1 - top < 5) {
										$(this).addClass("tree-node-top");
									} else {
										$(this).addClass("tree-node-append");
									}
								}
								if (_e7.onDragOver.call(_e5, this, _ee(_f0)) == false) {
									_ef(_f0, false);
									$(this)
											.removeClass(
													"tree-node-append tree-node-top tree-node-bottom");
									$(this).droppable("disable");
									_e6.disabledNodes.push(this);
								}
							},
							onDragLeave : function(e, _f3) {
								_ef(_f3, false);
								$(this)
										.removeClass(
												"tree-node-append tree-node-top tree-node-bottom");
								_e7.onDragLeave.call(_e5, this, _ee(_f3));
							},
							onDrop : function(e, _f4) {
								var _f5 = this;
								var _f6, _f7;
								if ($(this).hasClass("tree-node-append")) {
									_f6 = _f8;
									_f7 = "append";
								} else {
									_f6 = _f9;
									_f7 = $(this).hasClass("tree-node-top") ? "top"
											: "bottom";
								}
								if (_e7.onBeforeDrop.call(_e5, _f5, _ee(_f4),
										_f7) == false) {
									$(this)
											.removeClass(
													"tree-node-append tree-node-top tree-node-bottom");
									return;
								}
								_f6(_f4, _f5, _f7);
								$(this)
										.removeClass(
												"tree-node-append tree-node-top tree-node-bottom");
							}
						});
		function _ee(_fa, pop) {
			return $(_fa).closest("ul.tree").tree(pop ? "pop" : "getData", _fa);
		}
		;
		function _ef(_fb, _fc) {
			var _fd = $(_fb).draggable("proxy").find("span.tree-dnd-icon");
			_fd.removeClass("tree-dnd-yes tree-dnd-no").addClass(
					_fc ? "tree-dnd-yes" : "tree-dnd-no");
		}
		;
		function _f8(_fe, _ff) {
			if (_df(_e5, _ff).state == "closed") {
				_133(_e5, _ff, function() {
					_100();
				});
			} else {
				_100();
			}
			function _100() {
				var node = _ee(_fe, true);
				$(_e5).tree("append", {
					parent : _ff,
					data : [ node ]
				});
				_e7.onDrop.call(_e5, _ff, node, "append");
			}
			;
		}
		;
		function _f9(_101, dest, _102) {
			var _103 = {};
			if (_102 == "top") {
				_103.before = dest;
			} else {
				_103.after = dest;
			}
			var node = _ee(_101, true);
			_103.data = node;
			$(_e5).tree("insert", _103);
			_e7.onDrop.call(_e5, dest, node, _102);
		}
		;
	}
	;
	function _104(_105, _106, _107) {
		var opts = $.data(_105, "tree").options;
		if (!opts.checkbox) {
			return;
		}
		var _108 = _df(_105, _106);
		if (opts.onBeforeCheck.call(_105, _108, _107) == false) {
			return;
		}
		var node = $(_106);
		var ck = node.find(".tree-checkbox");
		ck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
		if (_107) {
			ck.addClass("tree-checkbox1");
		} else {
			ck.addClass("tree-checkbox0");
		}
		if (opts.cascadeCheck) {
			_109(node);
			_10a(node);
		}
		opts.onCheck.call(_105, _108, _107);
		function _10a(node) {
			var _10b = node.next().find(".tree-checkbox");
			_10b.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
			if (node.find(".tree-checkbox").hasClass("tree-checkbox1")) {
				_10b.addClass("tree-checkbox1");
			} else {
				_10b.addClass("tree-checkbox0");
			}
		}
		;
		function _109(node) {
			var _10c = _146(_105, node[0]);
			if (_10c) {
				var ck = $(_10c.target).find(".tree-checkbox");
				ck.removeClass("tree-checkbox0 tree-checkbox1 tree-checkbox2");
				if (_10d(node)) {
					ck.addClass("tree-checkbox1");
				} else {
					if (_10e(node)) {
						ck.addClass("tree-checkbox0");
					} else {
						ck.addClass("tree-checkbox2");
					}
				}
				_109($(_10c.target));
			}
			function _10d(n) {
				var ck = n.find(".tree-checkbox");
				if (ck.hasClass("tree-checkbox0")
						|| ck.hasClass("tree-checkbox2")) {
					return false;
				}
				var b = true;
				n.parent().siblings().each(
						function() {
							if (!$(this).children("div.tree-node").children(
									".tree-checkbox")
									.hasClass("tree-checkbox1")) {
								b = false;
							}
						});
				return b;
			}
			;
			function _10e(n) {
				var ck = n.find(".tree-checkbox");
				if (ck.hasClass("tree-checkbox1")
						|| ck.hasClass("tree-checkbox2")) {
					return false;
				}
				var b = true;
				n.parent().siblings().each(
						function() {
							if (!$(this).children("div.tree-node").children(
									".tree-checkbox")
									.hasClass("tree-checkbox0")) {
								b = false;
							}
						});
				return b;
			}
			;
		}
		;
	}
	;
	function _10f(_110, _111) {
		var opts = $.data(_110, "tree").options;
		if (!opts.checkbox) {
			return;
		}
		var node = $(_111);
		if (_112(_110, _111)) {
			var ck = node.find(".tree-checkbox");
			if (ck.length) {
				if (ck.hasClass("tree-checkbox1")) {
					_104(_110, _111, true);
				} else {
					_104(_110, _111, false);
				}
			} else {
				if (opts.onlyLeafCheck) {
					$("<span class=\"tree-checkbox tree-checkbox0\"></span>")
							.insertBefore(node.find(".tree-title"));
				}
			}
		} else {
			var ck = node.find(".tree-checkbox");
			if (opts.onlyLeafCheck) {
				ck.remove();
			} else {
				if (ck.hasClass("tree-checkbox1")) {
					_104(_110, _111, true);
				} else {
					if (ck.hasClass("tree-checkbox2")) {
						var _113 = true;
						var _114 = true;
						var _115 = _116(_110, _111);
						for ( var i = 0; i < _115.length; i++) {
							if (_115[i].checked) {
								_114 = false;
							} else {
								_113 = false;
							}
						}
						if (_113) {
							_104(_110, _111, true);
						}
						if (_114) {
							_104(_110, _111, false);
						}
					}
				}
			}
		}
	}
	;
	function _117(_118, ul, data, _119) {
		var _11a = $.data(_118, "tree");
		var opts = _11a.options;
		var _11b = $(ul).prevAll("div.tree-node:first");
		data = opts.loadFilter.call(_118, data, _11b[0]);
		var _11c = _11d(_118, "domId", _11b.attr("id"));
		if (!_119) {
			_11c ? _11c.children = data : _11a.data = data;
			$(ul).empty();
		} else {
			if (_11c) {
				_11c.children ? _11c.children = _11c.children.concat(data)
						: _11c.children = data;
			} else {
				_11a.data = _11a.data.concat(data);
			}
		}
		opts.view.render.call(opts.view, _118, ul, data);
		if (opts.dnd) {
			_e4(_118);
		}
		if (_11c) {
			_11e(_118, _11c);
		}
		var _11f = [];
		var _120 = [];
		for ( var i = 0; i < data.length; i++) {
			var node = data[i];
			if (!node.checked) {
				_11f.push(node);
			}
		}
		_121(data, function(node) {
			if (node.checked) {
				_120.push(node);
			}
		});
		var _122 = opts.onCheck;
		opts.onCheck = function() {
		};
		if (_11f.length) {
			_104(_118, $("#" + _11f[0].domId)[0], false);
		}
		for ( var i = 0; i < _120.length; i++) {
			_104(_118, $("#" + _120[i].domId)[0], true);
		}
		opts.onCheck = _122;
		setTimeout(function() {
			_123(_118, _118);
		}, 0);
		opts.onLoadSuccess.call(_118, _11c, data);
	}
	;
	function _123(_124, ul, _125) {
		var opts = $.data(_124, "tree").options;
		if (opts.lines) {
			$(_124).addClass("tree-lines");
		} else {
			$(_124).removeClass("tree-lines");
			return;
		}
		if (!_125) {
			_125 = true;
			$(_124).find("span.tree-indent").removeClass(
					"tree-line tree-join tree-joinbottom");
			$(_124).find("div.tree-node").removeClass(
					"tree-node-last tree-root-first tree-root-one");
			var _126 = $(_124).tree("getRoots");
			if (_126.length > 1) {
				$(_126[0].target).addClass("tree-root-first");
			} else {
				if (_126.length == 1) {
					$(_126[0].target).addClass("tree-root-one");
				}
			}
		}
		$(ul).children("li").each(function() {
			var node = $(this).children("div.tree-node");
			var ul = node.next("ul");
			if (ul.length) {
				if ($(this).next().length) {
					_127(node);
				}
				_123(_124, ul, _125);
			} else {
				_128(node);
			}
		});
		var _129 = $(ul).children("li:last").children("div.tree-node")
				.addClass("tree-node-last");
		_129.children("span.tree-join").removeClass("tree-join").addClass(
				"tree-joinbottom");
		function _128(node, _12a) {
			var icon = node.find("span.tree-icon");
			icon.prev("span.tree-indent").addClass("tree-join");
		}
		;
		function _127(node) {
			var _12b = node.find("span.tree-indent, span.tree-hit").length;
			node.next().find("div.tree-node").each(
					function() {
						$(this).children("span:eq(" + (_12b - 1) + ")")
								.addClass("tree-line");
					});
		}
		;
	}
	;
	function _12c(_12d, ul, _12e, _12f) {
		var opts = $.data(_12d, "tree").options;
		_12e = $.extend({}, opts.queryParams, _12e || {});
		var _130 = null;
		if (_12d != ul) {
			var node = $(ul).prev();
			_130 = _df(_12d, node[0]);
		}
		if (opts.onBeforeLoad.call(_12d, _130, _12e) == false) {
			return;
		}
		var _131 = $(ul).prev().children("span.tree-folder");
		_131.addClass("tree-loading");
		var _132 = opts.loader.call(_12d, _12e, function(data) {
			_131.removeClass("tree-loading");
			_117(_12d, ul, data);
			if (_12f) {
				_12f();
			}
		}, function() {
			_131.removeClass("tree-loading");
			opts.onLoadError.apply(_12d, arguments);
			if (_12f) {
				_12f();
			}
		});
		if (_132 == false) {
			_131.removeClass("tree-loading");
		}
	}
	;
	function _133(_134, _135, _136) {
		var opts = $.data(_134, "tree").options;
		var hit = $(_135).children("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-expanded")) {
			return;
		}
		var node = _df(_134, _135);
		if (opts.onBeforeExpand.call(_134, node) == false) {
			return;
		}
		hit.removeClass("tree-collapsed tree-collapsed-hover").addClass(
				"tree-expanded");
		hit.next().addClass("tree-folder-open");
		var ul = $(_135).next();
		if (ul.length) {
			if (opts.animate) {
				ul.slideDown("normal", function() {
					node.state = "open";
					opts.onExpand.call(_134, node);
					if (_136) {
						_136();
					}
				});
			} else {
				ul.css("display", "block");
				node.state = "open";
				opts.onExpand.call(_134, node);
				if (_136) {
					_136();
				}
			}
		} else {
			var _137 = $("<ul style=\"display:none\"></ul>").insertAfter(_135);
			_12c(_134, _137[0], {
				id : node.id
			}, function() {
				if (_137.is(":empty")) {
					_137.remove();
				}
				if (opts.animate) {
					_137.slideDown("normal", function() {
						node.state = "open";
						opts.onExpand.call(_134, node);
						if (_136) {
							_136();
						}
					});
				} else {
					_137.css("display", "block");
					node.state = "open";
					opts.onExpand.call(_134, node);
					if (_136) {
						_136();
					}
				}
			});
		}
	}
	;
	function _138(_139, _13a) {
		var opts = $.data(_139, "tree").options;
		var hit = $(_13a).children("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-collapsed")) {
			return;
		}
		var node = _df(_139, _13a);
		if (opts.onBeforeCollapse.call(_139, node) == false) {
			return;
		}
		hit.removeClass("tree-expanded tree-expanded-hover").addClass(
				"tree-collapsed");
		hit.next().removeClass("tree-folder-open");
		var ul = $(_13a).next();
		if (opts.animate) {
			ul.slideUp("normal", function() {
				node.state = "closed";
				opts.onCollapse.call(_139, node);
			});
		} else {
			ul.css("display", "none");
			node.state = "closed";
			opts.onCollapse.call(_139, node);
		}
	}
	;
	function _13b(_13c, _13d) {
		var hit = $(_13d).children("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-expanded")) {
			_138(_13c, _13d);
		} else {
			_133(_13c, _13d);
		}
	}
	;
	function _13e(_13f, _140) {
		var _141 = _116(_13f, _140);
		if (_140) {
			_141.unshift(_df(_13f, _140));
		}
		for ( var i = 0; i < _141.length; i++) {
			_133(_13f, _141[i].target);
		}
	}
	;
	function _142(_143, _144) {
		var _145 = [];
		var p = _146(_143, _144);
		while (p) {
			_145.unshift(p);
			p = _146(_143, p.target);
		}
		for ( var i = 0; i < _145.length; i++) {
			_133(_143, _145[i].target);
		}
	}
	;
	function _147(_148, _149) {
		var c = $(_148).parent();
		while (c[0].tagName != "BODY" && c.css("overflow-y") != "auto") {
			c = c.parent();
		}
		var n = $(_149);
		var ntop = n.offset().top;
		if (c[0].tagName != "BODY") {
			var ctop = c.offset().top;
			if (ntop < ctop) {
				c.scrollTop(c.scrollTop() + ntop - ctop);
			} else {
				if (ntop + n.outerHeight() > ctop + c.outerHeight() - 18) {
					c.scrollTop(c.scrollTop() + ntop + n.outerHeight() - ctop
							- c.outerHeight() + 18);
				}
			}
		} else {
			c.scrollTop(ntop);
		}
	}
	;
	function _14a(_14b, _14c) {
		var _14d = _116(_14b, _14c);
		if (_14c) {
			_14d.unshift(_df(_14b, _14c));
		}
		for ( var i = 0; i < _14d.length; i++) {
			_138(_14b, _14d[i].target);
		}
	}
	;
	function _14e(_14f, _150) {
		var node = $(_150.parent);
		var data = _150.data;
		if (!data) {
			return;
		}
		data = $.isArray(data) ? data : [ data ];
		if (!data.length) {
			return;
		}
		var ul;
		if (node.length == 0) {
			ul = $(_14f);
		} else {
			if (_112(_14f, node[0])) {
				var _151 = node.find("span.tree-icon");
				_151.removeClass("tree-file").addClass(
						"tree-folder tree-folder-open");
				var hit = $("<span class=\"tree-hit tree-expanded\"></span>")
						.insertBefore(_151);
				if (hit.prev().length) {
					hit.prev().remove();
				}
			}
			ul = node.next();
			if (!ul.length) {
				ul = $("<ul></ul>").insertAfter(node);
			}
		}
		_117(_14f, ul[0], data, true);
		_10f(_14f, ul.prev());
	}
	;
	function _152(_153, _154) {
		var ref = _154.before || _154.after;
		var _155 = _146(_153, ref);
		var data = _154.data;
		if (!data) {
			return;
		}
		data = $.isArray(data) ? data : [ data ];
		if (!data.length) {
			return;
		}
		_14e(_153, {
			parent : (_155 ? _155.target : null),
			data : data
		});
		var _156 = _155 ? _155.children : $(_153).tree("getRoots");
		for ( var i = 0; i < _156.length; i++) {
			if (_156[i].domId == $(ref).attr("id")) {
				for ( var j = data.length - 1; j >= 0; j--) {
					_156.splice((_154.before ? i : (i + 1)), 0, data[j]);
				}
				_156.splice(_156.length - data.length, data.length);
				break;
			}
		}
		var li = $();
		for ( var i = 0; i < data.length; i++) {
			li = li.add($("#" + data[i].domId).parent());
		}
		if (_154.before) {
			li.insertBefore($(ref).parent());
		} else {
			li.insertAfter($(ref).parent());
		}
	}
	;
	function _157(_158, _159) {
		var _15a = del(_159);
		$(_159).parent().remove();
		if (_15a) {
			if (!_15a.children || !_15a.children.length) {
				var node = $(_15a.target);
				node.find(".tree-icon").removeClass("tree-folder").addClass(
						"tree-file");
				node.find(".tree-hit").remove();
				$("<span class=\"tree-indent\"></span>").prependTo(node);
				node.next().remove();
			}
			_11e(_158, _15a);
			_10f(_158, _15a.target);
		}
		_123(_158, _158);
		function del(_15b) {
			var id = $(_15b).attr("id");
			var _15c = _146(_158, _15b);
			var cc = _15c ? _15c.children : $.data(_158, "tree").data;
			for ( var i = 0; i < cc.length; i++) {
				if (cc[i].domId == id) {
					cc.splice(i, 1);
					break;
				}
			}
			return _15c;
		}
		;
	}
	;
	function _11e(_15d, _15e) {
		var opts = $.data(_15d, "tree").options;
		var node = $(_15e.target);
		var data = _df(_15d, _15e.target);
		var _15f = data.checked;
		if (data.iconCls) {
			node.find(".tree-icon").removeClass(data.iconCls);
		}
		$.extend(data, _15e);
		node.find(".tree-title").html(opts.formatter.call(_15d, data));
		if (data.iconCls) {
			node.find(".tree-icon").addClass(data.iconCls);
		}
		if (_15f != data.checked) {
			_104(_15d, _15e.target, data.checked);
		}
	}
	;
	function _160(_161, _162) {
		if (_162) {
			var p = _146(_161, _162);
			while (p) {
				_162 = p.target;
				p = _146(_161, _162);
			}
			return _df(_161, _162);
		} else {
			var _163 = _164(_161);
			return _163.length ? _163[0] : null;
		}
	}
	;
	function _164(_165) {
		var _166 = $.data(_165, "tree").data;
		for ( var i = 0; i < _166.length; i++) {
			_167(_166[i]);
		}
		return _166;
	}
	;
	function _116(_168, _169) {
		var _16a = [];
		var n = _df(_168, _169);
		var data = n ? (n.children || []) : $.data(_168, "tree").data;
		_121(data, function(node) {
			_16a.push(_167(node));
		});
		return _16a;
	}
	;
	function _146(_16b, _16c) {
		var p = $(_16c).closest("ul").prevAll("div.tree-node:first");
		return _df(_16b, p[0]);
	}
	;
	function _16d(_16e, _16f) {
		_16f = _16f || "checked";
		if (!$.isArray(_16f)) {
			_16f = [ _16f ];
		}
		var _170 = [];
		for ( var i = 0; i < _16f.length; i++) {
			var s = _16f[i];
			if (s == "checked") {
				_170.push("span.tree-checkbox1");
			} else {
				if (s == "unchecked") {
					_170.push("span.tree-checkbox0");
				} else {
					if (s == "indeterminate") {
						_170.push("span.tree-checkbox2");
					}
				}
			}
		}
		var _171 = [];
		$(_16e).find(_170.join(",")).each(function() {
			var node = $(this).parent();
			_171.push(_df(_16e, node[0]));
		});
		return _171;
	}
	;
	function _172(_173) {
		var node = $(_173).find("div.tree-node-selected");
		return node.length ? _df(_173, node[0]) : null;
	}
	;
	function _174(_175, _176) {
		var data = _df(_175, _176);
		if (data && data.children) {
			_121(data.children, function(node) {
				_167(node);
			});
		}
		return data;
	}
	;
	function _df(_177, _178) {
		return _11d(_177, "domId", $(_178).attr("id"));
	}
	;
	function _179(_17a, id) {
		return _11d(_17a, "id", id);
	}
	;
	function _11d(_17b, _17c, _17d) {
		var data = $.data(_17b, "tree").data;
		var _17e = null;
		_121(data, function(node) {
			if (node[_17c] == _17d) {
				_17e = _167(node);
				return false;
			}
		});
		return _17e;
	}
	;
	function _167(node) {
		var d = $("#" + node.domId);
		node.target = d[0];
		node.checked = d.find(".tree-checkbox").hasClass("tree-checkbox1");
		return node;
	}
	;
	function _121(data, _17f) {
		var _180 = [];
		for ( var i = 0; i < data.length; i++) {
			_180.push(data[i]);
		}
		while (_180.length) {
			var node = _180.shift();
			if (_17f(node) == false) {
				return;
			}
			if (node.children) {
				for ( var i = node.children.length - 1; i >= 0; i--) {
					_180.unshift(node.children[i]);
				}
			}
		}
	}
	;
	function _181(_182, _183) {
		var opts = $.data(_182, "tree").options;
		var node = _df(_182, _183);
		if (opts.onBeforeSelect.call(_182, node) == false) {
			return;
		}
		$(_182).find("div.tree-node-selected")
				.removeClass("tree-node-selected");
		$(_183).addClass("tree-node-selected");
		opts.onSelect.call(_182, node);
	}
	;
	function _112(_184, _185) {
		return $(_185).children("span.tree-hit").length == 0;
	}
	;
	function _186(_187, _188) {
		var opts = $.data(_187, "tree").options;
		var node = _df(_187, _188);
		if (opts.onBeforeEdit.call(_187, node) == false) {
			return;
		}
		$(_188).css("position", "relative");
		var nt = $(_188).find(".tree-title");
		var _189 = nt.outerWidth();
		nt.empty();
		var _18a = $("<input class=\"tree-editor\">").appendTo(nt);
		_18a.val(node.text).focus();
		_18a.width(_189 + 20);
		_18a.height(document.compatMode == "CSS1Compat" ? (18 - (_18a
				.outerHeight() - _18a.height())) : 18);
		_18a.bind("click", function(e) {
			return false;
		}).bind("mousedown", function(e) {
			e.stopPropagation();
		}).bind("mousemove", function(e) {
			e.stopPropagation();
		}).bind("keydown", function(e) {
			if (e.keyCode == 13) {
				_18b(_187, _188);
				return false;
			} else {
				if (e.keyCode == 27) {
					_18f(_187, _188);
					return false;
				}
			}
		}).bind("blur", function(e) {
			e.stopPropagation();
			_18b(_187, _188);
		});
	}
	;
	function _18b(_18c, _18d) {
		var opts = $.data(_18c, "tree").options;
		$(_18d).css("position", "");
		var _18e = $(_18d).find("input.tree-editor");
		var val = _18e.val();
		_18e.remove();
		var node = _df(_18c, _18d);
		node.text = val;
		_11e(_18c, node);
		opts.onAfterEdit.call(_18c, node);
	}
	;
	function _18f(_190, _191) {
		var opts = $.data(_190, "tree").options;
		$(_191).css("position", "");
		$(_191).find("input.tree-editor").remove();
		var node = _df(_190, _191);
		_11e(_190, node);
		opts.onCancelEdit.call(_190, node);
	}
	;
	$.fn.tree = function(_192, _193) {
		if (typeof _192 == "string") {
			return $.fn.tree.methods[_192](this, _193);
		}
		var _192 = _192 || {};
		return this.each(function() {
			var _194 = $.data(this, "tree");
			var opts;
			if (_194) {
				opts = $.extend(_194.options, _192);
				_194.options = opts;
			} else {
				opts = $.extend({}, $.fn.tree.defaults, $.fn.tree
						.parseOptions(this), _192);
				$.data(this, "tree", {
					options : opts,
					tree : _d4(this),
					data : []
				});
				var data = $.fn.tree.parseData(this);
				if (data.length) {
					_117(this, this, data);
				}
			}
			_d7(this);
			if (opts.data) {
				_117(this, this, $.extend(true, [], opts.data));
			}
			_12c(this, this);
		});
	};
	$.fn.tree.methods = {
		options : function(jq) {
			return $.data(jq[0], "tree").options;
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				_117(this, this, data);
			});
		},
		getNode : function(jq, _195) {
			return _df(jq[0], _195);
		},
		getData : function(jq, _196) {
			return _174(jq[0], _196);
		},
		reload : function(jq, _197) {
			return jq.each(function() {
				if (_197) {
					var node = $(_197);
					var hit = node.children("span.tree-hit");
					hit.removeClass("tree-expanded tree-expanded-hover")
							.addClass("tree-collapsed");
					node.next().remove();
					_133(this, _197);
				} else {
					$(this).empty();
					_12c(this, this);
				}
			});
		},
		getRoot : function(jq, _198) {
			return _160(jq[0], _198);
		},
		getRoots : function(jq) {
			return _164(jq[0]);
		},
		getParent : function(jq, _199) {
			return _146(jq[0], _199);
		},
		getChildren : function(jq, _19a) {
			return _116(jq[0], _19a);
		},
		getChecked : function(jq, _19b) {
			return _16d(jq[0], _19b);
		},
		getSelected : function(jq) {
			return _172(jq[0]);
		},
		isLeaf : function(jq, _19c) {
			return _112(jq[0], _19c);
		},
		find : function(jq, id) {
			return _179(jq[0], id);
		},
		select : function(jq, _19d) {
			return jq.each(function() {
				_181(this, _19d);
			});
		},
		check : function(jq, _19e) {
			return jq.each(function() {
				_104(this, _19e, true);
			});
		},
		uncheck : function(jq, _19f) {
			return jq.each(function() {
				_104(this, _19f, false);
			});
		},
		collapse : function(jq, _1a0) {
			return jq.each(function() {
				_138(this, _1a0);
			});
		},
		expand : function(jq, _1a1) {
			return jq.each(function() {
				_133(this, _1a1);
			});
		},
		collapseAll : function(jq, _1a2) {
			return jq.each(function() {
				_14a(this, _1a2);
			});
		},
		expandAll : function(jq, _1a3) {
			return jq.each(function() {
				_13e(this, _1a3);
			});
		},
		expandTo : function(jq, _1a4) {
			return jq.each(function() {
				_142(this, _1a4);
			});
		},
		scrollTo : function(jq, _1a5) {
			return jq.each(function() {
				_147(this, _1a5);
			});
		},
		toggle : function(jq, _1a6) {
			return jq.each(function() {
				_13b(this, _1a6);
			});
		},
		append : function(jq, _1a7) {
			return jq.each(function() {
				_14e(this, _1a7);
			});
		},
		insert : function(jq, _1a8) {
			return jq.each(function() {
				_152(this, _1a8);
			});
		},
		remove : function(jq, _1a9) {
			return jq.each(function() {
				_157(this, _1a9);
			});
		},
		pop : function(jq, _1aa) {
			var node = jq.tree("getData", _1aa);
			jq.tree("remove", _1aa);
			return node;
		},
		update : function(jq, _1ab) {
			return jq.each(function() {
				_11e(this, _1ab);
			});
		},
		enableDnd : function(jq) {
			return jq.each(function() {
				_e4(this);
			});
		},
		disableDnd : function(jq) {
			return jq.each(function() {
				_e0(this);
			});
		},
		beginEdit : function(jq, _1ac) {
			return jq.each(function() {
				_186(this, _1ac);
			});
		},
		endEdit : function(jq, _1ad) {
			return jq.each(function() {
				_18b(this, _1ad);
			});
		},
		cancelEdit : function(jq, _1ae) {
			return jq.each(function() {
				_18f(this, _1ae);
			});
		}
	};
	$.fn.tree.parseOptions = function(_1af) {
		var t = $(_1af);
		return $.extend({}, $.parser.parseOptions(_1af, [ "url", "method", {
			checkbox : "boolean",
			cascadeCheck : "boolean",
			onlyLeafCheck : "boolean"
		}, {
			animate : "boolean",
			lines : "boolean",
			dnd : "boolean"
		} ]));
	};
	$.fn.tree.parseData = function(_1b0) {
		var data = [];
		_1b1(data, $(_1b0));
		return data;
		function _1b1(aa, tree) {
			tree.children("li").each(
					function() {
						var node = $(this);
						var item = $.extend({}, $.parser.parseOptions(this, [
								"id", "iconCls", "state" ]), {
							checked : (node.attr("checked") ? true : undefined)
						});
						item.text = node.children("span").html();
						if (!item.text) {
							item.text = node.html();
						}
						var _1b2 = node.children("ul");
						if (_1b2.length) {
							item.children = [];
							_1b1(item.children, _1b2);
						}
						aa.push(item);
					});
		}
		;
	};
	var _1b3 = 1;
	var _1b4 = {
		render : function(_1b5, ul, data) {
			var opts = $.data(_1b5, "tree").options;
			var _1b6 = $(ul).prev("div.tree-node").find(
					"span.tree-indent, span.tree-hit").length;
			var cc = _1b7(_1b6, data);
			$(ul).append(cc.join(""));
			function _1b7(_1b8, _1b9) {
				var cc = [];
				for ( var i = 0; i < _1b9.length; i++) {
					var item = _1b9[i];
					if (item.state != "open" && item.state != "closed") {
						item.state = "open";
					}
					item.domId = "_easyui_tree_" + _1b3++;
					cc.push("<li>");
					cc.push("<div id=\"" + item.domId
							+ "\" class=\"tree-node\">");
					for ( var j = 0; j < _1b8; j++) {
						cc.push("<span class=\"tree-indent\"></span>");
					}
					var _1ba = false;
					if (item.state == "closed") {
						cc
								.push("<span class=\"tree-hit tree-collapsed\"></span>");
						cc.push("<span class=\"tree-icon tree-folder "
								+ (item.iconCls ? item.iconCls : "")
								+ "\"></span>");
					} else {
						if (item.children && item.children.length) {
							cc
									.push("<span class=\"tree-hit tree-expanded\"></span>");
							cc
									.push("<span class=\"tree-icon tree-folder tree-folder-open "
											+ (item.iconCls ? item.iconCls : "")
											+ "\"></span>");
						} else {
							cc.push("<span class=\"tree-indent\"></span>");
							cc.push("<span class=\"tree-icon tree-file "
									+ (item.iconCls ? item.iconCls : "")
									+ "\"></span>");
							_1ba = true;
						}
					}
					if (opts.checkbox) {
						if ((!opts.onlyLeafCheck) || _1ba) {
							cc
									.push("<span class=\"tree-checkbox tree-checkbox0\"></span>");
						}
					}
					cc.push("<span class=\"tree-title\">"
							+ opts.formatter.call(_1b5, item) + "</span>");
					cc.push("</div>");
					if (item.children && item.children.length) {
						var tmp = _1b7(_1b8 + 1, item.children);
						cc.push("<ul style=\"display:"
								+ (item.state == "closed" ? "none" : "block")
								+ "\">");
						cc = cc.concat(tmp);
						cc.push("</ul>");
					}
					cc.push("</li>");
				}
				return cc;
			}
			;
		}
	};
	$.fn.tree.defaults = {
		url : null,
		method : "post",
		animate : false,
		checkbox : false,
		cascadeCheck : true,
		onlyLeafCheck : false,
		lines : false,
		dnd : false,
		data : null,
		queryParams : {},
		formatter : function(node) {
			return node.text;
		},
		loader : function(_1bb, _1bc, _1bd) {
			var opts = $(this).tree("options");
			if (!opts.url) {
				return false;
			}
			$.ajax({
				type : opts.method,
				url : opts.url,
				data : _1bb,
				dataType : "json",
				success : function(data) {
					_1bc(data);
				},
				error : function() {
					_1bd.apply(this, arguments);
				}
			});
		},
		loadFilter : function(data, _1be) {
			return data;
		},
		view : _1b4,
		onBeforeLoad : function(node, _1bf) {
		},
		onLoadSuccess : function(node, data) {
		},
		onLoadError : function() {
		},
		onClick : function(node) {
		},
		onDblClick : function(node) {
		},
		onBeforeExpand : function(node) {
		},
		onExpand : function(node) {
		},
		onBeforeCollapse : function(node) {
		},
		onCollapse : function(node) {
		},
		onBeforeCheck : function(node, _1c0) {
		},
		onCheck : function(node, _1c1) {
		},
		onBeforeSelect : function(node) {
		},
		onSelect : function(node) {
		},
		onContextMenu : function(e, node) {
		},
		onBeforeDrag : function(node) {
		},
		onStartDrag : function(node) {
		},
		onStopDrag : function(node) {
		},
		onDragEnter : function(_1c2, _1c3) {
		},
		onDragOver : function(_1c4, _1c5) {
		},
		onDragLeave : function(_1c6, _1c7) {
		},
		onBeforeDrop : function(_1c8, _1c9, _1ca) {
		},
		onDrop : function(_1cb, _1cc, _1cd) {
		},
		onBeforeEdit : function(node) {
		},
		onAfterEdit : function(node) {
		},
		onCancelEdit : function(node) {
		}
	};
})(jQuery);
/**
 * progressbar - jQuery EasyUI
 * 
 * Dependencies:
 * 	 none
 * 
 */
(function($){
	function init(target){
		$(target).addClass('progressbar');
		$(target).html('<div class="progressbar-text"></div><div class="progressbar-value"><div class="progressbar-text"></div></div>');
		$(target).bind('_resize', function(e,force){
			if ($(this).hasClass('easyui-fluid') || force){
				setSize(target);
			}
			return false;
		});
		return $(target);
	}
	
	function setSize(target,width){
		var opts = $.data(target, 'progressbar').options;
		var bar = $.data(target, 'progressbar').bar;
		if (width) opts.width = width;
		bar._size(opts);
		
		bar.find('div.progressbar-text').css('width', bar.width());
		bar.find('div.progressbar-text,div.progressbar-value').css({
			height: bar.height()+'px',
			lineHeight: bar.height()+'px'
		});
	}
	
	$.fn.progressbar = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.progressbar.methods[options];
			if (method){
				return method(this, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'progressbar');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'progressbar', {
					options: $.extend({}, $.fn.progressbar.defaults, $.fn.progressbar.parseOptions(this), options),
					bar: init(this)
				});
			}
			$(this).progressbar('setValue', state.options.value);
			setSize(this);
		});
	};
	
	$.fn.progressbar.methods = {
		options: function(jq){
			return $.data(jq[0], 'progressbar').options;
		},
		resize: function(jq, width){
			return jq.each(function(){
				setSize(this, width);
			});
		},
		getValue: function(jq){
			return $.data(jq[0], 'progressbar').options.value;
		},
		setValue: function(jq, value){
			if (value < 0) value = 0;
			if (value > 100) value = 100;
			return jq.each(function(){
				var opts = $.data(this, 'progressbar').options;
				var text = opts.text.replace(/{value}/, value);
				var oldValue = opts.value;
				opts.value = value;
				$(this).find('div.progressbar-value').width(value+'%');
				$(this).find('div.progressbar-text').html(text);
				if (oldValue != value){
					opts.onChange.call(this, value, oldValue);
				}
			});
		}
	};
	
	$.fn.progressbar.parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, ['width','height','text',{value:'number'}]));
	};
	
	$.fn.progressbar.defaults = {
		width: 'auto',
		height: 22,
		value: 0,	// percentage value
		text: '{value}%',
		onChange:function(newValue,oldValue){}
	};
})(jQuery);

(function($) {
	function init(_1dd) {
		$(_1dd).addClass("tooltip-f");
	}
	;
	function _1de(_1df) {
		var opts = $.data(_1df, "tooltip").options;
		$(_1df).unbind(".tooltip").bind(opts.showEvent + ".tooltip",
				function(e) {
					$(_1df).tooltip("show", e);
				}).bind(opts.hideEvent + ".tooltip", function(e) {
			$(_1df).tooltip("hide", e);
		}).bind("mousemove.tooltip", function(e) {
			if (opts.trackMouse) {
				opts.trackMouseX = e.pageX;
				opts.trackMouseY = e.pageY;
				$(_1df).tooltip("reposition");
			}
		});
	}
	;
	function _1e0(_1e1) {
		var _1e2 = $.data(_1e1, "tooltip");
		if (_1e2.showTimer) {
			clearTimeout(_1e2.showTimer);
			_1e2.showTimer = null;
		}
		if (_1e2.hideTimer) {
			clearTimeout(_1e2.hideTimer);
			_1e2.hideTimer = null;
		}
	}
	;
	function _1e3(_1e4) {
		var _1e5 = $.data(_1e4, "tooltip");
		if (!_1e5 || !_1e5.tip) {
			return;
		}
		var opts = _1e5.options;
		var tip = _1e5.tip;
		var pos = {
			left : -100000,
			top : -100000
		};
		if ($(_1e4).is(":visible")) {
			pos = _1e6(opts.position);
			if (opts.position == "top" && pos.top < 0) {
				pos = _1e6("bottom");
			} else {
				if ((opts.position == "bottom")
						&& (pos.top + tip._outerHeight() > $(window)
								._outerHeight()
								+ $(document).scrollTop())) {
					pos = _1e6("top");
				}
			}
			if (pos.left < 0) {
				if (opts.position == "left") {
					pos = _1e6("right");
				} else {
					$(_1e4).tooltip("arrow").css("left",
							tip._outerWidth() / 2 + pos.left);
					pos.left = 0;
				}
			} else {
				if (pos.left + tip._outerWidth() > $(window)._outerWidth()
						+ $(document)._scrollLeft()) {
					if (opts.position == "right") {
						pos = _1e6("left");
					} else {
						var left = pos.left;
						pos.left = $(window)._outerWidth()
								+ $(document)._scrollLeft() - tip._outerWidth();
						$(_1e4).tooltip("arrow").css("left",
								tip._outerWidth() / 2 - (pos.left - left));
					}
				}
			}
		}
		tip.css({
			left : pos.left,
			top : pos.top,
			zIndex : (opts.zIndex != undefined ? opts.zIndex
					: ($.fn.window ? $.fn.window.defaults.zIndex++ : ""))
		});
		opts.onPosition.call(_1e4, pos.left, pos.top);
		function _1e6(_1e7) {
			opts.position = _1e7 || "bottom";
			tip.removeClass(
					"tooltip-top tooltip-bottom tooltip-left tooltip-right")
					.addClass("tooltip-" + opts.position);
			var left, top;
			if (opts.trackMouse) {
				t = $();
				left = opts.trackMouseX + opts.deltaX;
				top = opts.trackMouseY + opts.deltaY;
			} else {
				var t = $(_1e4);
				left = t.offset().left + opts.deltaX;
				top = t.offset().top + opts.deltaY;
			}
			switch (opts.position) {
			case "right":
				left += t._outerWidth() + 12 + (opts.trackMouse ? 12 : 0);
				top -= (tip._outerHeight() - t._outerHeight()) / 2;
				break;
			case "left":
				left -= tip._outerWidth() + 12 + (opts.trackMouse ? 12 : 0);
				top -= (tip._outerHeight() - t._outerHeight()) / 2;
				break;
			case "top":
				left -= (tip._outerWidth() - t._outerWidth()) / 2;
				top -= tip._outerHeight() + 12 + (opts.trackMouse ? 12 : 0);
				break;
			case "bottom":
				left -= (tip._outerWidth() - t._outerWidth()) / 2;
				top += t._outerHeight() + 12 + (opts.trackMouse ? 12 : 0);
				break;
			}
			return {
				left : left,
				top : top
			};
		}
		;
	}
	;
	function _1e8(_1e9, e) {
		var _1ea = $.data(_1e9, "tooltip");
		var opts = _1ea.options;
		var tip = _1ea.tip;
		if (!tip) {
			tip = $(
					"<div tabindex=\"-1\" class=\"tooltip\">"
							+ "<div class=\"tooltip-content\"></div>"
							+ "<div class=\"tooltip-arrow-outer\"></div>"
							+ "<div class=\"tooltip-arrow\"></div>" + "</div>")
					.appendTo("body");
			_1ea.tip = tip;
			_1eb(_1e9);
		}
		_1e0(_1e9);
		_1ea.showTimer = setTimeout(function() {
			$(_1e9).tooltip("reposition");
			tip.show();
			opts.onShow.call(_1e9, e);
			var _1ec = tip.children(".tooltip-arrow-outer");
			var _1ed = tip.children(".tooltip-arrow");
			var bc = "border-" + opts.position + "-color";
			_1ec.add(_1ed).css({
				borderTopColor : "",
				borderBottomColor : "",
				borderLeftColor : "",
				borderRightColor : ""
			});
			_1ec.css(bc, tip.css(bc));
			_1ed.css(bc, tip.css("backgroundColor"));
		}, opts.showDelay);
	}
	;
	function _1ee(_1ef, e) {
		var _1f0 = $.data(_1ef, "tooltip");
		if (_1f0 && _1f0.tip) {
			_1e0(_1ef);
			_1f0.hideTimer = setTimeout(function() {
				_1f0.tip.hide();
				_1f0.options.onHide.call(_1ef, e);
			}, _1f0.options.hideDelay);
		}
	}
	;
	function _1eb(_1f1, _1f2) {
		var _1f3 = $.data(_1f1, "tooltip");
		var opts = _1f3.options;
		if (_1f2) {
			opts.content = _1f2;
		}
		if (!_1f3.tip) {
			return;
		}
		var cc = typeof opts.content == "function" ? opts.content.call(_1f1)
				: opts.content;
		_1f3.tip.children(".tooltip-content").html(cc);
		opts.onUpdate.call(_1f1, cc);
	}
	;
	function _1f4(_1f5) {
		var _1f6 = $.data(_1f5, "tooltip");
		if (_1f6) {
			_1e0(_1f5);
			var opts = _1f6.options;
			if (_1f6.tip) {
				_1f6.tip.remove();
			}
			if (opts._title) {
				$(_1f5).attr("title", opts._title);
			}
			$.removeData(_1f5, "tooltip");
			$(_1f5).unbind(".tooltip").removeClass("tooltip-f");
			opts.onDestroy.call(_1f5);
		}
	}
	;
	$.fn.tooltip = function(_1f7, _1f8) {
		if (typeof _1f7 == "string") {
			return $.fn.tooltip.methods[_1f7](this, _1f8);
		}
		_1f7 = _1f7 || {};
		return this.each(function() {
			var _1f9 = $.data(this, "tooltip");
			if (_1f9) {
				$.extend(_1f9.options, _1f7);
			} else {
				$.data(this, "tooltip", {
					options : $.extend({}, $.fn.tooltip.defaults, $.fn.tooltip
							.parseOptions(this), _1f7)
				});
				init(this);
			}
			_1de(this);
			_1eb(this);
		});
	};
	$.fn.tooltip.methods = {
		options : function(jq) {
			return $.data(jq[0], "tooltip").options;
		},
		tip : function(jq) {
			return $.data(jq[0], "tooltip").tip;
		},
		arrow : function(jq) {
			return jq.tooltip("tip").children(
					".tooltip-arrow-outer,.tooltip-arrow");
		},
		show : function(jq, e) {
			return jq.each(function() {
				_1e8(this, e);
			});
		},
		hide : function(jq, e) {
			return jq.each(function() {
				_1ee(this, e);
			});
		},
		update : function(jq, _1fa) {
			return jq.each(function() {
				_1eb(this, _1fa);
			});
		},
		reposition : function(jq) {
			return jq.each(function() {
				_1e3(this);
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				_1f4(this);
			});
		}
	};
	$.fn.tooltip.parseOptions = function(_1fb) {
		var t = $(_1fb);
		var opts = $.extend({}, $.parser.parseOptions(_1fb, [ "position",
				"showEvent", "hideEvent", "content", {
					trackMouse : "boolean",
					deltaX : "number",
					deltaY : "number",
					showDelay : "number",
					hideDelay : "number"
				} ]), {
			_title : t.attr("title")
		});
		t.attr("title", "");
		if (!opts.content) {
			opts.content = opts._title;
		}
		return opts;
	};
	$.fn.tooltip.defaults = {
		position : "bottom",
		content : null,
		trackMouse : false,
		deltaX : 0,
		deltaY : 0,
		showEvent : "mouseenter",
		hideEvent : "mouseleave",
		showDelay : 200,
		hideDelay : 100,
		onShow : function(e) {
		},
		onHide : function(e) {
		},
		onUpdate : function(_1fc) {
		},
		onPosition : function(left, top) {
		},
		onDestroy : function() {
		}
	};
})(jQuery);
(function($) {
	$.fn._remove = function() {
		return this.each(function() {
			$(this).remove();
			try {
				this.outerHTML = "";
			} catch (err) {
			}
		});
	};
	function _1fd(node) {
		node._remove();
	}
	;
	function _1fe(_1ff, _200) {
		var _201 = $.data(_1ff, "panel");
		var opts = _201.options;
		var _202 = _201.panel;
		var _203 = _202.children("div.panel-header");
		var _204 = _202.children("div.panel-body");
		var _205 = _202.children("div.panel-footer");
		if (_200) {
			$.extend(opts, {
				width : _200.width,
				height : _200.height,
				minWidth : _200.minWidth,
				maxWidth : _200.maxWidth,
				minHeight : _200.minHeight,
				maxHeight : _200.maxHeight,
				left : _200.left,
				top : _200.top
			});
		}
		_202._size(opts);
		_203.add(_204)._outerWidth(_202.width());
		if (!isNaN(parseInt(opts.height))) {
			_204._outerHeight(_202.height() - _203._outerHeight()
					- _205._outerHeight());
		} else {
			_204.css("height", "");
			var min = $.parser.parseValue("minHeight", opts.minHeight, _202
					.parent());
			var max = $.parser.parseValue("maxHeight", opts.maxHeight, _202
					.parent());
			var _206 = _203._outerHeight() + _205._outerHeight()
					+ _202._outerHeight() - _202.height();
			_204._size("minHeight", min ? (min - _206) : "");
			_204._size("maxHeight", max ? (max - _206) : "");
		}
		_202.css({
			height : "",
			minHeight : "",
			maxHeight : "",
			left : opts.left,
			top : opts.top
		});
		opts.onResize.apply(_1ff, [ opts.width, opts.height ]);
		$(_1ff).panel("doLayout");
	}
	;
	function _207(_208, _209) {
		var opts = $.data(_208, "panel").options;
		var _20a = $.data(_208, "panel").panel;
		if (_209) {
			if (_209.left != null) {
				opts.left = _209.left;
			}
			if (_209.top != null) {
				opts.top = _209.top;
			}
		}
		_20a.css({
			left : opts.left,
			top : opts.top
		});
		opts.onMove.apply(_208, [ opts.left, opts.top ]);
	}
	;
	function _20b(_20c) {
		$(_20c).addClass("panel-body")._size("clear");
		var _20d = $("<div class=\"panel\"></div>").insertBefore(_20c);
		_20d[0].appendChild(_20c);
		_20d.bind("_resize", function(e, _20e) {
			if ($(this).hasClass("easyui-fluid") || _20e) {
				_1fe(_20c);
			}
			return false;
		});
		return _20d;
	}
	;
	function _20f(_210) {
		var _211 = $.data(_210, "panel");
		var opts = _211.options;
		var _212 = _211.panel;
		_212.css(opts.style);
		_212.addClass(opts.cls);
		_213();
		_214();
		var _215 = $(_210).panel("header");
		var body = $(_210).panel("body");
		var _216 = $(_210).siblings("div.panel-footer");
		if (opts.border) {
			_215.removeClass("panel-header-noborder");
			body.removeClass("panel-body-noborder");
			_216.removeClass("panel-footer-noborder");
		} else {
			_215.addClass("panel-header-noborder");
			body.addClass("panel-body-noborder");
			_216.addClass("panel-footer-noborder");
		}
		_215.addClass(opts.headerCls);
		body.addClass(opts.bodyCls);
		$(_210).attr("id", opts.id || "");
		if (opts.content) {
			$(_210).panel("clear");
			$(_210).html(opts.content);
			$.parser.parse($(_210));
		}
		function _213() {
			if (opts.tools && typeof opts.tools == "string") {
				_212.find(">div.panel-header>div.panel-tool .panel-tool-a")
						.appendTo(opts.tools);
			}
			_1fd(_212.children("div.panel-header"));
			if (opts.title && !opts.noheader) {
				var _217 = $("<div class=\"panel-header\"></div>").prependTo(
						_212);
				var _218 = $("<div class=\"panel-title\"></div>").html(
						opts.title).appendTo(_217);
				if (opts.iconCls) {
					_218.addClass("panel-with-icon");
					$("<div class=\"panel-icon\"></div>")
							.addClass(opts.iconCls).appendTo(_217);
				}
				var tool = $("<div class=\"panel-tool\"></div>").appendTo(_217);
				tool.bind("click", function(e) {
					e.stopPropagation();
				});
				if (opts.tools) {
					if ($.isArray(opts.tools)) {
						for ( var i = 0; i < opts.tools.length; i++) {
							var t = $("<a href=\"javascript:void(0)\"></a>")
									.addClass(opts.tools[i].iconCls).appendTo(
											tool);
							if (opts.tools[i].handler) {
								t.bind("click", eval(opts.tools[i].handler));
							}
						}
					} else {
						$(opts.tools).children().each(
								function() {
									$(this).addClass($(this).attr("iconCls"))
											.addClass("panel-tool-a").appendTo(
													tool);
								});
					}
				}
				if (opts.collapsible) {
					$(
							"<a class=\"panel-tool-collapse\" href=\"javascript:void(0)\"></a>")
							.appendTo(tool).bind("click", function() {
								if (opts.collapsed == true) {
									_235(_210, true);
								} else {
									_228(_210, true);
								}
								return false;
							});
				}
				if (opts.minimizable) {
					$(
							"<a class=\"panel-tool-min\" href=\"javascript:void(0)\"></a>")
							.appendTo(tool).bind("click", function() {
								_23b(_210);
								return false;
							});
				}
				if (opts.maximizable) {
					$(
							"<a class=\"panel-tool-max\" href=\"javascript:void(0)\"></a>")
							.appendTo(tool).bind("click", function() {
								if (opts.maximized == true) {
									_23e(_210);
								} else {
									_227(_210);
								}
								return false;
							});
				}
				if (opts.closable) {
					$(
							"<a class=\"panel-tool-close\" href=\"javascript:void(0)\"></a>")
							.appendTo(tool).bind("click", function() {
								_229(_210);
								return false;
							});
				}
				_212.children("div.panel-body").removeClass(
						"panel-body-noheader");
			} else {
				_212.children("div.panel-body").addClass("panel-body-noheader");
			}
		}
		;
		function _214() {
			if (opts.footer) {
				$(opts.footer).addClass("panel-footer").appendTo(_212);
				$(_210).addClass("panel-body-nobottom");
			} else {
				_212.children("div.panel-footer").remove();
				$(_210).removeClass("panel-body-nobottom");
			}
		}
		;
	}
	;
	function _219(_21a, _21b) {
		var _21c = $.data(_21a, "panel");
		var opts = _21c.options;
		if (_21d) {
			opts.queryParams = _21b;
		}
		if (!opts.href) {
			return;
		}
		if (!_21c.isLoaded || !opts.cache) {
			var _21d = $.extend({}, opts.queryParams);
			if (opts.onBeforeLoad.call(_21a, _21d) == false) {
				return;
			}
			_21c.isLoaded = false;
			$(_21a).panel("clear");
			if (opts.loadingMessage) {
				$(_21a).html(
						$("<div class=\"panel-loading\"></div>").html(
								opts.loadingMessage));
			}
			opts.loader.call(_21a, _21d, function(data) {
				var _21e = opts.extractor.call(_21a, data);
				$(_21a).html(_21e);
				$.parser.parse($(_21a));
				opts.onLoad.apply(_21a, arguments);
				_21c.isLoaded = true;
			}, function() {
				opts.onLoadError.apply(_21a, arguments);
			});
		}
	}
	;
	function _21f(_220) {
		var t = $(_220);
		t.find(".combo-f").each(function() {
			$(this).combo("destroy");
		});
		t.find(".m-btn").each(function() {
			$(this).menubutton("destroy");
		});
		t.find(".s-btn").each(function() {
			$(this).splitbutton("destroy");
		});
		t.find(".tooltip-f").each(function() {
			$(this).tooltip("destroy");
		});
		t.children("div").each(function() {
			$(this)._size("unfit");
		});
		t.empty();
	}
	;
	function _221(_222) {
		$(_222).panel("doLayout", true);
	}
	;
	function _223(_224, _225) {
		var opts = $.data(_224, "panel").options;
		var _226 = $.data(_224, "panel").panel;
		if (_225 != true) {
			if (opts.onBeforeOpen.call(_224) == false) {
				return;
			}
		}
		_226.stop(true, true);
		if ($.isFunction(opts.openAnimation)) {
			opts.openAnimation.call(_224, cb);
		} else {
			switch (opts.openAnimation) {
			case "slide":
				_226.slideDown(opts.openDuration, cb);
				break;
			case "fade":
				_226.fadeIn(opts.openDuration, cb);
				break;
			case "show":
				_226.show(opts.openDuration, cb);
				break;
			default:
				_226.show();
				cb();
			}
		}
		function cb() {
			opts.closed = false;
			opts.minimized = false;
			var tool = _226.children("div.panel-header").find(
					"a.panel-tool-restore");
			if (tool.length) {
				opts.maximized = true;
			}
			opts.onOpen.call(_224);
			if (opts.maximized == true) {
				opts.maximized = false;
				_227(_224);
			}
			if (opts.collapsed == true) {
				opts.collapsed = false;
				_228(_224);
			}
			if (!opts.collapsed) {
				_219(_224);
				_221(_224);
			}
		}
		;
	}
	;
	function _229(_22a, _22b) {
		var opts = $.data(_22a, "panel").options;
		var _22c = $.data(_22a, "panel").panel;
		if (_22b != true) {
			if (opts.onBeforeClose.call(_22a) == false) {
				return;
			}
		}
		_22c.stop(true, true);
		_22c._size("unfit");
		if ($.isFunction(opts.closeAnimation)) {
			opts.closeAnimation.call(_22a, cb);
		} else {
			switch (opts.closeAnimation) {
			case "slide":
				_22c.slideUp(opts.closeDuration, cb);
				break;
			case "fade":
				_22c.fadeOut(opts.closeDuration, cb);
				break;
			case "hide":
				_22c.hide(opts.closeDuration, cb);
				break;
			default:
				_22c.hide();
				cb();
			}
		}
		function cb() {
			opts.closed = true;
			opts.onClose.call(_22a);
		}
		;
	}
	;
	function _22d(_22e, _22f) {
		var _230 = $.data(_22e, "panel");
		var opts = _230.options;
		var _231 = _230.panel;
		if (_22f != true) {
			if (opts.onBeforeDestroy.call(_22e) == false) {
				return;
			}
		}
		$(_22e).panel("clear").panel("clear", "footer");
		_1fd(_231);
		opts.onDestroy.call(_22e);
	}
	;
	function _228(_232, _233) {
		var opts = $.data(_232, "panel").options;
		var _234 = $.data(_232, "panel").panel;
		var body = _234.children("div.panel-body");
		var tool = _234.children("div.panel-header").find(
				"a.panel-tool-collapse");
		if (opts.collapsed == true) {
			return;
		}
		body.stop(true, true);
		if (opts.onBeforeCollapse.call(_232) == false) {
			return;
		}
		tool.addClass("panel-tool-expand");
		if (_233 == true) {
			body.slideUp("normal", function() {
				opts.collapsed = true;
				opts.onCollapse.call(_232);
			});
		} else {
			body.hide();
			opts.collapsed = true;
			opts.onCollapse.call(_232);
		}
	}
	;
	function _235(_236, _237) {
		var opts = $.data(_236, "panel").options;
		var _238 = $.data(_236, "panel").panel;
		var body = _238.children("div.panel-body");
		var tool = _238.children("div.panel-header").find(
				"a.panel-tool-collapse");
		if (opts.collapsed == false) {
			return;
		}
		body.stop(true, true);
		if (opts.onBeforeExpand.call(_236) == false) {
			return;
		}
		tool.removeClass("panel-tool-expand");
		if (_237 == true) {
			body.slideDown("normal", function() {
				opts.collapsed = false;
				opts.onExpand.call(_236);
				_219(_236);
				_221(_236);
			});
		} else {
			body.show();
			opts.collapsed = false;
			opts.onExpand.call(_236);
			_219(_236);
			_221(_236);
		}
	}
	;
	function _227(_239) {
		var opts = $.data(_239, "panel").options;
		var _23a = $.data(_239, "panel").panel;
		var tool = _23a.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == true) {
			return;
		}
		tool.addClass("panel-tool-restore");
		if (!$.data(_239, "panel").original) {
			$.data(_239, "panel").original = {
				width : opts.width,
				height : opts.height,
				left : opts.left,
				top : opts.top,
				fit : opts.fit
			};
		}
		opts.left = 0;
		opts.top = 0;
		opts.fit = true;
		_1fe(_239);
		opts.minimized = false;
		opts.maximized = true;
		opts.onMaximize.call(_239);
	}
	;
	function _23b(_23c) {
		var opts = $.data(_23c, "panel").options;
		var _23d = $.data(_23c, "panel").panel;
		_23d._size("unfit");
		_23d.hide();
		opts.minimized = true;
		opts.maximized = false;
		opts.onMinimize.call(_23c);
	}
	;
	function _23e(_23f) {
		var opts = $.data(_23f, "panel").options;
		var _240 = $.data(_23f, "panel").panel;
		var tool = _240.children("div.panel-header").find("a.panel-tool-max");
		if (opts.maximized == false) {
			return;
		}
		_240.show();
		tool.removeClass("panel-tool-restore");
		$.extend(opts, $.data(_23f, "panel").original);
		_1fe(_23f);
		opts.minimized = false;
		opts.maximized = false;
		$.data(_23f, "panel").original = null;
		opts.onRestore.call(_23f);
	}
	;
	function _241(_242, _243) {
		$.data(_242, "panel").options.title = _243;
		$(_242).panel("header").find("div.panel-title").html(_243);
	}
	;
	var _244 = null;
	$(window).unbind(".panel").bind("resize.panel", function() {
		if (_244) {
			clearTimeout(_244);
		}
		_244 = setTimeout(function() {
			var _245 = $("body.layout");
			if (_245.length) {
				_245.layout("resize");
				$("body").children(".easyui-fluid:visible").trigger("_resize");
			} else {
				$("body").panel("doLayout");
			}
			_244 = null;
		}, 100);
	});
	$.fn.panel = function(_246, _247) {
		if (typeof _246 == "string") {
			return $.fn.panel.methods[_246](this, _247);
		}
		_246 = _246 || {};
		return this.each(function() {
			var _248 = $.data(this, "panel");
			var opts;
			if (_248) {
				opts = $.extend(_248.options, _246);
				_248.isLoaded = false;
			} else {
				opts = $.extend({}, $.fn.panel.defaults, $.fn.panel
						.parseOptions(this), _246);
				$(this).attr("title", "");
				_248 = $.data(this, "panel", {
					options : opts,
					panel : _20b(this),
					isLoaded : false
				});
			}
			_20f(this);
			if (opts.doSize == true) {
				_248.panel.css("display", "block");
				_1fe(this);
			}
			if (opts.closed == true || opts.minimized == true) {
				_248.panel.hide();
			} else {
				_223(this);
			}
		});
	};
	$.fn.panel.methods = {
		options : function(jq) {
			return $.data(jq[0], "panel").options;
		},
		panel : function(jq) {
			return $.data(jq[0], "panel").panel;
		},
		header : function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-header");
		},
		footer : function(jq) {
			return jq.panel("panel").children(".panel-footer");
		},
		body : function(jq) {
			return $.data(jq[0], "panel").panel.find(">div.panel-body");
		},
		setTitle : function(jq, _249) {
			return jq.each(function() {
				_241(this, _249);
			});
		},
		open : function(jq, _24a) {
			return jq.each(function() {
				_223(this, _24a);
			});
		},
		close : function(jq, _24b) {
			return jq.each(function() {
				_229(this, _24b);
			});
		},
		destroy : function(jq, _24c) {
			return jq.each(function() {
				_22d(this, _24c);
			});
		},
		clear : function(jq, type) {
			return jq.each(function() {
				_21f(type == "footer" ? $(this).panel("footer") : this);
			});
		},
		refresh : function(jq, href) {
			return jq.each(function() {
				var _24d = $.data(this, "panel");
				_24d.isLoaded = false;
				if (href) {
					if (typeof href == "string") {
						_24d.options.href = href;
					} else {
						_24d.options.queryParams = href;
					}
				}
				_219(this);
			});
		},
		resize : function(jq, _24e) {
			return jq.each(function() {
				_1fe(this, _24e);
			});
		},
		doLayout : function(jq, all) {
			return jq
					.each(function() {
						_24f(this, "body");
						_24f($(this).siblings("div.panel-footer")[0], "footer");
						function _24f(_250, type) {
							if (!_250) {
								return;
							}
							var _251 = _250 == $("body")[0];
							var s = $(_250)
									.find(
											"div.panel:visible,div.accordion:visible,div.tabs-container:visible,div.layout:visible,.easyui-fluid:visible")
									.filter(
											function(_252, el) {
												var p = $(el).parents(
														"div.panel-" + type
																+ ":first");
												return _251 ? p.length == 0
														: p[0] == _250;
											});
							s.trigger("_resize", [ all || false ]);
						}
						;
					});
		},
		move : function(jq, _253) {
			return jq.each(function() {
				_207(this, _253);
			});
		},
		maximize : function(jq) {
			return jq.each(function() {
				_227(this);
			});
		},
		minimize : function(jq) {
			return jq.each(function() {
				_23b(this);
			});
		},
		restore : function(jq) {
			return jq.each(function() {
				_23e(this);
			});
		},
		collapse : function(jq, _254) {
			return jq.each(function() {
				_228(this, _254);
			});
		},
		expand : function(jq, _255) {
			return jq.each(function() {
				_235(this, _255);
			});
		}
	};
	$.fn.panel.parseOptions = function(_256) {
		var t = $(_256);
		return $.extend({}, $.parser.parseOptions(_256, [ "id", "width",
				"height", "left", "top", "title", "iconCls", "cls",
				"headerCls", "bodyCls", "tools", "href", "method", {
					cache : "boolean",
					fit : "boolean",
					border : "boolean",
					noheader : "boolean"
				}, {
					collapsible : "boolean",
					minimizable : "boolean",
					maximizable : "boolean"
				}, {
					closable : "boolean",
					collapsed : "boolean",
					minimized : "boolean",
					maximized : "boolean",
					closed : "boolean"
				}, "openAnimation", "closeAnimation", {
					openDuration : "number",
					closeDuration : "number"
				}, ]), {
			loadingMessage : (t.attr("loadingMessage") != undefined ? t
					.attr("loadingMessage") : undefined)
		});
	};
	$.fn.panel.defaults = {
		id : null,
		title : null,
		iconCls : null,
		width : "auto",
		height : "auto",
		left : null,
		top : null,
		cls : null,
		headerCls : null,
		bodyCls : null,
		style : {},
		href : null,
		cache : true,
		fit : false,
		border : true,
		doSize : true,
		noheader : false,
		content : null,
		collapsible : false,
		minimizable : false,
		maximizable : false,
		closable : false,
		collapsed : false,
		minimized : false,
		maximized : false,
		closed : false,
		openAnimation : false,
		openDuration : 400,
		closeAnimation : false,
		closeDuration : 400,
		tools : null,
		footer : null,
		queryParams : {},
		method : "get",
		href : null,
		loadingMessage : "Loading...",
		loader : function(_257, _258, _259) {
			var opts = $(this).panel("options");
			if (!opts.href) {
				return false;
			}
			$.ajax({
				type : opts.method,
				url : opts.href,
				cache : false,
				data : _257,
				dataType : "html",
				success : function(data) {
					_258(data);
				},
				error : function() {
					_259.apply(this, arguments);
				}
			});
		},
		extractor : function(data) {
			var _25a = /<body[^>]*>((.|[\n\r])*)<\/body>/im;
			var _25b = _25a.exec(data);
			if (_25b) {
				return _25b[1];
			} else {
				return data;
			}
		},
		onBeforeLoad : function(_25c) {
		},
		onLoad : function() {
		},
		onLoadError : function() {
		},
		onBeforeOpen : function() {
		},
		onOpen : function() {
		},
		onBeforeClose : function() {
		},
		onClose : function() {
		},
		onBeforeDestroy : function() {
		},
		onDestroy : function() {
		},
		onResize : function(_25d, _25e) {
		},
		onMove : function(left, top) {
		},
		onMaximize : function() {
		},
		onRestore : function() {
		},
		onMinimize : function() {
		},
		onBeforeCollapse : function() {
		},
		onBeforeExpand : function() {
		},
		onCollapse : function() {
		},
		onExpand : function() {
		}
	};
})(jQuery);
/**
 * window - jQuery EasyUI
 * 
 * Dependencies:
 * 	 panel
 *   draggable
 *   resizable
 * 
 */
(function($){
	function moveWindow(target, param){
		var state = $.data(target, 'window');
		if (param){
			if (param.left != null) state.options.left = param.left;
			if (param.top != null) state.options.top = param.top;
		}
		$(target).panel('move', state.options);
		if (state.shadow){
			state.shadow.css({
				left: state.options.left,
				top: state.options.top
			});
		}
	}
	
	/**
	 *  center the window only horizontally
	 */
	function hcenter(target, tomove){
		var opts = $.data(target, 'window').options;
		var pp = $(target).window('panel');
		var width = pp._outerWidth();
		if (opts.inline){
			var parent = pp.parent();
			opts.left = Math.ceil((parent.width() - width) / 2 + parent.scrollLeft());
		} else {
			opts.left = Math.ceil(($(window)._outerWidth() - width) / 2 + $(document).scrollLeft());
		}
		if (tomove){moveWindow(target);}
	}
	
	/**
	 * center the window only vertically
	 */
	function vcenter(target, tomove){
		var opts = $.data(target, 'window').options;
		var pp = $(target).window('panel');
		var height = pp._outerHeight();
		if (opts.inline){
			var parent = pp.parent();
			opts.top = Math.ceil((parent.height() - height) / 2 + parent.scrollTop());
		} else {
			opts.top = Math.ceil(($(window)._outerHeight() - height) / 2 + $(document).scrollTop());
		}
		if (tomove){moveWindow(target);}
	}
	
	function create(target){
		var state = $.data(target, 'window');
		var opts = state.options;
		var win = $(target).panel($.extend({}, state.options, {
			border: false,
			doSize: true,	// size the panel, the property undefined in window component
			closed: true,	// close the panel
			cls: 'window',
			headerCls: 'window-header',
			bodyCls: 'window-body ' + (opts.noheader ? 'window-body-noheader' : ''),
			
			onBeforeDestroy: function(){
				if (opts.onBeforeDestroy.call(target) == false){return false;}
				if (state.shadow){state.shadow.remove();}
				if (state.mask){state.mask.remove();}
			},
			onClose: function(){
				if (state.shadow){state.shadow.hide();}
				if (state.mask){state.mask.hide();}
				opts.onClose.call(target);
			},
			onOpen: function(){
				if (state.mask){
					state.mask.css({
						display:'block',
						zIndex: $.fn.window.defaults.zIndex++
					});
				}
				if (state.shadow){
					state.shadow.css({
						display:'block',
						zIndex: $.fn.window.defaults.zIndex++,
						left: opts.left,
						top: opts.top,
						width: state.window._outerWidth(),
						height: state.window._outerHeight()
					});
				}
				state.window.css('z-index', $.fn.window.defaults.zIndex++);
				
				opts.onOpen.call(target);
			},
			onResize: function(width, height){
				var popts = $(this).panel('options');
				$.extend(opts, {
					width: popts.width,
					height: popts.height,
					left: popts.left,
					top: popts.top
				});
				if (state.shadow){
					state.shadow.css({
						left: opts.left,
						top: opts.top,
						width: state.window._outerWidth(),
						height: state.window._outerHeight()
					});
				}
				opts.onResize.call(target, width, height);
			},
			onMinimize: function(){
				if (state.shadow){state.shadow.hide();}
				if (state.mask){state.mask.hide();}
				state.options.onMinimize.call(target);
			},
			onBeforeCollapse: function(){
				if (opts.onBeforeCollapse.call(target) == false){return false;}
				if (state.shadow){state.shadow.hide();}
			},
			onExpand: function(){
				if (state.shadow){state.shadow.show();}
				opts.onExpand.call(target);
			}
		}));
		
		state.window = win.panel('panel');
		
		// create mask
		if (state.mask){state.mask.remove();}
		if (opts.modal == true){
			state.mask = $('<div class="window-mask"></div>').insertAfter(state.window);
			state.mask.css({
				width: (opts.inline ? state.mask.parent().width() : getPageArea().width),
				height: (opts.inline ? state.mask.parent().height() : getPageArea().height),
				display: 'none'
			});
		}
		
		// create shadow
		if (state.shadow){state.shadow.remove();}
		if (opts.shadow == true){
			state.shadow = $('<div class="window-shadow"></div>').insertAfter(state.window);
			state.shadow.css({
				display: 'none'
			});
		}
		
		// if require center the window
		if (opts.left == null){hcenter(target);}
		if (opts.top == null){vcenter(target);}
		moveWindow(target);
		
		if (!opts.closed){
			win.window('open');	// open the window
		}
	}
	
	
	/**
	 * set window drag and resize property
	 */
	function setProperties(target){
		var state = $.data(target, 'window');
		
		state.window.draggable({
			handle: '>div.panel-header>div.panel-title',
			disabled: state.options.draggable == false,
			onStartDrag: function(e){
				if (state.mask) state.mask.css('z-index', $.fn.window.defaults.zIndex++);
				if (state.shadow) state.shadow.css('z-index', $.fn.window.defaults.zIndex++);
				state.window.css('z-index', $.fn.window.defaults.zIndex++);
				
				if (!state.proxy){
					state.proxy = $('<div class="window-proxy"></div>').insertAfter(state.window);
				}
				state.proxy.css({
					display:'none',
					zIndex: $.fn.window.defaults.zIndex++,
					left: e.data.left,
					top: e.data.top
				});
				state.proxy._outerWidth(state.window._outerWidth());
				state.proxy._outerHeight(state.window._outerHeight());
				setTimeout(function(){
					if (state.proxy) state.proxy.show();
				}, 500);
			},
			onDrag: function(e){
				state.proxy.css({
					display:'block',
					left: e.data.left,
					top: e.data.top
				});
				return false;
			},
			onStopDrag: function(e){
				state.options.left = e.data.left;
				state.options.top = e.data.top;
				$(target).window('move');
				state.proxy.remove();
				state.proxy = null;
			}
		});
		
		state.window.resizable({
			disabled: state.options.resizable == false,
			onStartResize:function(e){
				if (state.pmask){state.pmask.remove();}
				state.pmask = $('<div class="window-proxy-mask"></div>').insertAfter(state.window);
				state.pmask.css({
					zIndex: $.fn.window.defaults.zIndex++,
					left: e.data.left,
					top: e.data.top,
					width: state.window._outerWidth(),
					height: state.window._outerHeight()
				});
				if (state.proxy){state.proxy.remove();}
				state.proxy = $('<div class="window-proxy"></div>').insertAfter(state.window);
				state.proxy.css({
					zIndex: $.fn.window.defaults.zIndex++,
					left: e.data.left,
					top: e.data.top
				});
				state.proxy._outerWidth(e.data.width)._outerHeight(e.data.height);
			},
			onResize: function(e){
				state.proxy.css({
					left: e.data.left,
					top: e.data.top
				});
				state.proxy._outerWidth(e.data.width);
				state.proxy._outerHeight(e.data.height);
				return false;
			},
			onStopResize: function(e){
				$(target).window('resize', e.data);
				state.pmask.remove();
				state.pmask = null;
				state.proxy.remove();
				state.proxy = null;
			}
		});
	}
	
	function getPageArea() {
		if (document.compatMode == 'BackCompat') {
			return {
				width: Math.max(document.body.scrollWidth, document.body.clientWidth),
				height: Math.max(document.body.scrollHeight, document.body.clientHeight)
			}
		} else {
			return {
				width: Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth),
				height: Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight)
			}
		}
	}
	
	// when window resize, reset the width and height of the window's mask
	$(window).resize(function(){
		$('body>div.window-mask').css({
			width: $(window)._outerWidth(),
			height: $(window)._outerHeight()
		});
		setTimeout(function(){
			$('body>div.window-mask').css({
				width: getPageArea().width,
				height: getPageArea().height
			});
		}, 50);
	});
	
	$.fn.window = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.window.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.panel(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'window');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'window', {
					options: $.extend({}, $.fn.window.defaults, $.fn.window.parseOptions(this), options)
				});
				if (!state.options.inline){
//					$(this).appendTo('body');
					document.body.appendChild(this);
				}
			}
			create(this);
			setProperties(this);
		});
	};
	
	$.fn.window.methods = {
		options: function(jq){
			var popts = jq.panel('options');
			var wopts = $.data(jq[0], 'window').options;
			return $.extend(wopts, {
				closed: popts.closed,
				collapsed: popts.collapsed,
				minimized: popts.minimized,
				maximized: popts.maximized
			});
		},
		window: function(jq){
			return $.data(jq[0], 'window').window;
		},
		move: function(jq, param){
			return jq.each(function(){
				moveWindow(this, param);
			});
		},
		hcenter: function(jq){
			return jq.each(function(){
				hcenter(this, true);
			});
		},
		vcenter: function(jq){
			return jq.each(function(){
				vcenter(this, true);
			});
		},
		center: function(jq){
			return jq.each(function(){
				hcenter(this);
				vcenter(this);
				moveWindow(this);
			});
		}
	};
	
	$.fn.window.parseOptions = function(target){
		return $.extend({}, $.fn.panel.parseOptions(target), $.parser.parseOptions(target, [
			{draggable:'boolean',resizable:'boolean',shadow:'boolean',modal:'boolean',inline:'boolean'}
		]));
	};
	
	// Inherited from $.fn.panel.defaults
	$.fn.window.defaults = $.extend({}, $.fn.panel.defaults, {
		zIndex: 9000,
		draggable: true,
		resizable: true,
		shadow: true,
		modal: false,
		inline: false,	// true to stay inside its parent, false to go on top of all elements
		
		// window's property which difference from panel
		title: 'New Window',
		collapsible: true,
		minimizable: true,
		maximizable: true,
		closable: true,
		closed: false
	});
})(jQuery);

(function($) {
	function _27f(_280) {
		var opts = $.data(_280, "dialog").options;
		opts.inited = false;
		$(_280).window($.extend({}, opts, {
			onResize : function(w, h) {
				if (opts.inited) {
					_284(this);
					opts.onResize.call(this, w, h);
				}
			}
		}));
		var win = $(_280).window("window");
		if (opts.toolbar) {
			if ($.isArray(opts.toolbar)) {
				$(_280).siblings("div.dialog-toolbar").remove();
				var _281 = $(
						"<div class=\"dialog-toolbar\"><table cellspacing=\"0\" cellpadding=\"0\"><tr></tr></table></div>")
						.appendTo(win);
				var tr = _281.find("tr");
				for ( var i = 0; i < opts.toolbar.length; i++) {
					var btn = opts.toolbar[i];
					if (btn == "-") {
						$(
								"<td><div class=\"dialog-tool-separator\"></div></td>")
								.appendTo(tr);
					} else {
						var td = $("<td></td>").appendTo(tr);
						var tool = $("<a href=\"javascript:void(0)\"></a>")
								.appendTo(td);
						tool[0].onclick = eval(btn.handler || function() {
						});
						tool.linkbutton($.extend({}, btn, {
							plain : true
						}));
					}
				}
			} else {
				$(opts.toolbar).addClass("dialog-toolbar").appendTo(win);
				$(opts.toolbar).show();
			}
		} else {
			$(_280).siblings("div.dialog-toolbar").remove();
		}
		if (opts.buttons) {
			if ($.isArray(opts.buttons)) {
				$(_280).siblings("div.dialog-button").remove();
				var _282 = $("<div class=\"dialog-button\"></div>").appendTo(
						win);
				for ( var i = 0; i < opts.buttons.length; i++) {
					var p = opts.buttons[i];
					var _283 = $("<a href=\"javascript:void(0)\"></a>")
							.appendTo(_282);
					if (p.handler) {
						_283[0].onclick = p.handler;
					}
					_283.linkbutton(p);
				}
			} else {
				$(opts.buttons).addClass("dialog-button").appendTo(win);
				$(opts.buttons).show();
			}
		} else {
			$(_280).siblings("div.dialog-button").remove();
		}
		opts.inited = true;
		win.show();
		$(_280).window("resize");
		if (opts.closed) {
			win.hide();
		}
	}
	;
	function _284(_285, _286) {
		var t = $(_285);
		var opts = t.dialog("options");
		var _287 = opts.noheader;
		var tb = t.siblings(".dialog-toolbar");
		var bb = t.siblings(".dialog-button");
		tb.insertBefore(_285).css({
			position : "relative",
			borderTopWidth : (_287 ? 1 : 0),
			top : (_287 ? tb.length : 0)
		});
		bb.insertAfter(_285).css({
			position : "relative",
			top : -1
		});
		if (!isNaN(parseInt(opts.height))) {
			t._outerHeight(t._outerHeight() - tb._outerHeight()
					- bb._outerHeight());
		}
		tb.add(bb)._outerWidth(t._outerWidth());
		var _288 = $.data(_285, "window").shadow;
		if (_288) {
			var cc = t.panel("panel");
			_288.css({
				width : cc._outerWidth(),
				height : cc._outerHeight()
			});
		}
	}
	;
	$.fn.dialog = function(_289, _28a) {
		if (typeof _289 == "string") {
			var _28b = $.fn.dialog.methods[_289];
			if (_28b) {
				return _28b(this, _28a);
			} else {
				return this.window(_289, _28a);
			}
		}
		_289 = _289 || {};
		return this.each(function() {
			var _28c = $.data(this, "dialog");
			if (_28c) {
				$.extend(_28c.options, _289);
			} else {
				$.data(this, "dialog", {
					options : $.extend({}, $.fn.dialog.defaults, $.fn.dialog
							.parseOptions(this), _289)
				});
			}
			_27f(this);
		});
	};
	$.fn.dialog.methods = {
		options : function(jq) {
			var _28d = $.data(jq[0], "dialog").options;
			var _28e = jq.panel("options");
			$.extend(_28d, {
				width : _28e.width,
				height : _28e.height,
				left : _28e.left,
				top : _28e.top,
				closed : _28e.closed,
				collapsed : _28e.collapsed,
				minimized : _28e.minimized,
				maximized : _28e.maximized
			});
			return _28d;
		},
		dialog : function(jq) {
			return jq.window("window");
		}
	};
	$.fn.dialog.parseOptions = function(_28f) {
		return $.extend({}, $.fn.window.parseOptions(_28f), $.parser
				.parseOptions(_28f, [ "toolbar", "buttons" ]));
	};
	$.fn.dialog.defaults = $.extend({}, $.fn.window.defaults, {
		title : "New Dialog",
		collapsible : false,
		minimizable : false,
		maximizable : false,
		resizable : false,
		toolbar : null,
		buttons : null
	});
})(jQuery);
(function($) {
	function show(el, type, _290, _291) {
		var win = $(el).window("window");
		if (!win) {
			return;
		}
		switch (type) {
		case null:
			win.show();
			break;
		case "slide":
			win.slideDown(_290);
			break;
		case "fade":
			win.fadeIn(_290);
			break;
		case "show":
			win.show(_290);
			break;
		}
		var _292 = null;
		if (_291 > 0) {
			_292 = setTimeout(function() {
				hide(el, type, _290);
			}, _291);
		}
		win.hover(function() {
			if (_292) {
				clearTimeout(_292);
			}
		}, function() {
			if (_291 > 0) {
				_292 = setTimeout(function() {
					hide(el, type, _290);
				}, _291);
			}
		});
	}
	;
	function hide(el, type, _293) {
		if (el.locked == true) {
			return;
		}
		el.locked = true;
		var win = $(el).window("window");
		if (!win) {
			return;
		}
		switch (type) {
		case null:
			win.hide();
			break;
		case "slide":
			win.slideUp(_293);
			break;
		case "fade":
			win.fadeOut(_293);
			break;
		case "show":
			win.hide(_293);
			break;
		}
		setTimeout(function() {
			$(el).window("destroy");
		}, _293);
	}
	;
	function _294(_295) {
		var opts = $.extend({}, $.fn.window.defaults, {
			collapsible : false,
			minimizable : false,
			maximizable : false,
			shadow : false,
			draggable : false,
			resizable : false,
			closed : true,
			style : {
				left : "",
				top : "",
				right : 0,
				zIndex : $.fn.window.defaults.zIndex++,
				bottom : -document.body.scrollTop
						- document.documentElement.scrollTop
			},
			onBeforeOpen : function() {
				show(this, opts.showType, opts.showSpeed, opts.timeout);
				return false;
			},
			onBeforeClose : function() {
				hide(this, opts.showType, opts.showSpeed);
				return false;
			}
		}, {
			title : "",
			width : 250,
			height : 100,
			showType : "slide",
			showSpeed : 600,
			msg : "",
			timeout : 4000
		}, _295);
		opts.style.zIndex = $.fn.window.defaults.zIndex++;
		var win = $("<div class=\"messager-body\"></div>").html(opts.msg)
				.appendTo("body");
		win.window(opts);
		win.window("window").css(opts.style);
		win.window("open");
		return win;
	}
	;
	function _296(_297, _298, _299) {
		var win = $("<div class=\"messager-body\"></div>").appendTo("body");
		win.append(_298);
		if (_299) {
			var tb = $("<div class=\"messager-button\"></div>").appendTo(win);
			for ( var _29a in _299) {
				$("<a></a>").attr("href", "javascript:void(0)").text(_29a).css(
						"margin-left", 10).bind("click", eval(_299[_29a]))
						.appendTo(tb).linkbutton();
			}
		}
		win.window({
			title : _297,
			noheader : (_297 ? false : true),
			width : 300,
			height : "auto",
			modal : true,
			collapsible : false,
			minimizable : false,
			maximizable : false,
			resizable : false,
			onClose : function() {
				setTimeout(function() {
					win.window("destroy");
				}, 100);
			}
		});
		win.window("window").addClass("messager-window");
		win.children("div.messager-button").children("a:first").focus();
		return win;
	}
	;
	$.messager = {
		show : function(_29b) {
			return _294(_29b);
		},
		alert : function(_29c, msg, icon, fn) {
			var _29d = "<div>" + msg + "</div>";
			switch (icon) {
			case "error":
				_29d = "<div class=\"messager-icon messager-error\"></div>"
						+ _29d;
				break;
			case "info":
				_29d = "<div class=\"messager-icon messager-info\"></div>"
						+ _29d;
				break;
			case "question":
				_29d = "<div class=\"messager-icon messager-question\"></div>"
						+ _29d;
				break;
			case "warning":
				_29d = "<div class=\"messager-icon messager-warning\"></div>"
						+ _29d;
				break;
			}
			_29d += "<div style=\"clear:both;\"/>";
			var _29e = {};
			_29e[$.messager.defaults.ok] = function() {
				win.window("close");
				if (fn) {
					fn();
					return false;
				}
			};
			var win = _296(_29c, _29d, _29e);
			return win;
		},
		confirm : function(_29f, msg, fn) {
			var _2a0 = "<div class=\"messager-icon messager-question\"></div>"
					+ "<div>" + msg + "</div>" + "<div style=\"clear:both;\"/>";
			var _2a1 = {};
			_2a1[$.messager.defaults.ok] = function() {
				win.window("close");
				if (fn) {
					fn(true);
					return false;
				}
			};
			_2a1[$.messager.defaults.cancel] = function() {
				win.window("close");
				if (fn) {
					fn(false);
					return false;
				}
			};
			var win = _296(_29f, _2a0, _2a1);
			return win;
		},
		prompt : function(_2a2, msg, fn) {
			var _2a3 = "<div class=\"messager-icon messager-question\"></div>"
					+ "<div>"
					+ msg
					+ "</div>"
					+ "<br/>"
					+ "<div style=\"clear:both;\"/>"
					+ "<div><input class=\"messager-input\" type=\"text\"/></div>";
			var _2a4 = {};
			_2a4[$.messager.defaults.ok] = function() {
				win.window("close");
				if (fn) {
					fn($(".messager-input", win).val());
					return false;
				}
			};
			_2a4[$.messager.defaults.cancel] = function() {
				win.window("close");
				if (fn) {
					fn();
					return false;
				}
			};
			var win = _296(_2a2, _2a3, _2a4);
			win.children("input.messager-input").focus();
			return win;
		},
		progress : function(_2a5) {
			var _2a6 = {
				bar : function() {
					return $("body>div.messager-window").find(
							"div.messager-p-bar");
				},
				close : function() {
					var win = $("body>div.messager-window>div.messager-body:has(div.messager-progress)");
					if (win.length) {
						win.window("close");
					}
				}
			};
			if (typeof _2a5 == "string") {
				var _2a7 = _2a6[_2a5];
				return _2a7();
			}
			var opts = $.extend({
				title : "",
				msg : "",
				text : undefined,
				interval : 300
			}, _2a5 || {});
			var _2a8 = "<div class=\"messager-progress\"><div class=\"messager-p-msg\"></div><div class=\"messager-p-bar\"></div></div>";
			var win = _296(opts.title, _2a8, null);
			win.find("div.messager-p-msg").html(opts.msg);
			var bar = win.find("div.messager-p-bar");
			bar.progressbar({
				text : opts.text
			});
			win.window({
				closable : false,
				onClose : function() {
					if (this.timer) {
						clearInterval(this.timer);
					}
					$(this).window("destroy");
				}
			});
			if (opts.interval) {
				win[0].timer = setInterval(function() {
					var v = bar.progressbar("getValue");
					v += 10;
					if (v > 100) {
						v = 0;
					}
					bar.progressbar("setValue", v);
				}, opts.interval);
			}
			return win;
		}
	};
	$.messager.defaults = {
		ok : "Ok",
		cancel : "Cancel"
	};
})(jQuery);
/**
 * accordion - jQuery EasyUI
 * 
 * Dependencies:
 * 	 panel
 * 
 */
(function($){
	
	function setSize(container, param){
		var state = $.data(container, 'accordion');
		var opts = state.options;
		var panels = state.panels;
		var cc = $(container);
		
		if (param){
			$.extend(opts, {
				width: param.width,
				height: param.height
			});
		}
		cc._size(opts);
		var headerHeight = 0;
		var bodyHeight = 'auto';
		var headers = cc.find('>div.panel>div.accordion-header');
		if (headers.length){
			headerHeight = $(headers[0]).css('height', '')._outerHeight();
		}
		if (!isNaN(parseInt(opts.height))){
			bodyHeight = cc.height() - headerHeight*headers.length;
		}
		
		_resize(true, bodyHeight - _resize(false) + 1);
		
		function _resize(collapsible, height){
			var totalHeight = 0;
			for(var i=0; i<panels.length; i++){
				var p = panels[i];
				var h = p.panel('header')._outerHeight(headerHeight);
				if (p.panel('options').collapsible == collapsible){
					var pheight = isNaN(height) ? undefined : (height+headerHeight*h.length);
					p.panel('resize', {
						width: cc.width(),
						height: (collapsible ? pheight : undefined)
					});
					totalHeight += p.panel('panel').outerHeight()-headerHeight*h.length;
				}
			}
			return totalHeight;
		}
	}
	
	/**
	 * find a panel by specified property, return the panel object or panel index.
	 */
	function findBy(container, property, value, all){
		var panels = $.data(container, 'accordion').panels;
		var pp = [];
		for(var i=0; i<panels.length; i++){
			var p = panels[i];
			if (property){
				if (p.panel('options')[property] == value){
					pp.push(p);
				}
			} else {
				if (p[0] == $(value)[0]){
					return i;
				}
			}
		}
		if (property){
			return all ? pp : (pp.length ? pp[0] : null);
		} else {
			return -1;
		}
	}
	
	function getSelections(container){
		return findBy(container, 'collapsed', false, true);
	}
	
	function getSelected(container){
		var pp = getSelections(container);
		return pp.length ? pp[0] : null;
	}
	
	/**
	 * get panel index, start with 0
	 */
	function getPanelIndex(container, panel){
		return findBy(container, null, panel);
	}
	
	/**
	 * get the specified panel.
	 */
	function getPanel(container, which){
		var panels = $.data(container, 'accordion').panels;
		if (typeof which == 'number'){
			if (which < 0 || which >= panels.length){
				return null;
			} else {
				return panels[which];
			}
		}
		return findBy(container, 'title', which);
	}
	
	function setProperties(container){
		var opts = $.data(container, 'accordion').options;
		var cc = $(container);
		if (opts.border){
			cc.removeClass('accordion-noborder');
		} else {
			cc.addClass('accordion-noborder');
		}
	}
	
	function init(container){
		var state = $.data(container, 'accordion');
		var cc = $(container);
		cc.addClass('accordion');
		
		state.panels = [];
		cc.children('div').each(function(){
			var opts = $.extend({}, $.parser.parseOptions(this), {
				selected: ($(this).attr('selected') ? true : undefined)
			});
			var pp = $(this);
			state.panels.push(pp);
			createPanel(container, pp, opts);
		});
		
		cc.bind('_resize', function(e,force){
			if ($(this).hasClass('easyui-fluid') || force){
				setSize(container);
			}
			return false;
		});
	}
	
	function createPanel(container, pp, options){
		var opts = $.data(container, 'accordion').options;
		pp.panel($.extend({}, {
			collapsible: true,
			minimizable: false,
			maximizable: false,
			closable: false,
			doSize: false,
			collapsed: true,
			headerCls: 'accordion-header',
			bodyCls: 'accordion-body'
		}, options, {
			onBeforeExpand: function(){
				if (options.onBeforeExpand){
					if (options.onBeforeExpand.call(this) == false){return false}
				}
				if (!opts.multiple){
					// get all selected panel
					var all = $.grep(getSelections(container), function(p){
						return p.panel('options').collapsible;
					});
					for(var i=0; i<all.length; i++){
						unselect(container, getPanelIndex(container, all[i]));
					}
				}
				var header = $(this).panel('header');
				header.addClass('accordion-header-selected');
				header.find('.accordion-collapse').removeClass('accordion-expand');
			},
			onExpand: function(){
				if (options.onExpand){options.onExpand.call(this)}
				opts.onSelect.call(container, $(this).panel('options').title, getPanelIndex(container, this));
			},
			onBeforeCollapse: function(){
				if (options.onBeforeCollapse){
					if (options.onBeforeCollapse.call(this) == false){return false}
				}
				var header = $(this).panel('header');
				header.removeClass('accordion-header-selected');
				header.find('.accordion-collapse').addClass('accordion-expand');
			},
			onCollapse: function(){
				if (options.onCollapse){options.onCollapse.call(this)}
				opts.onUnselect.call(container, $(this).panel('options').title, getPanelIndex(container, this));
			}
		}));
		
		var header = pp.panel('header');
		var tool = header.children('div.panel-tool');
		tool.children('a.panel-tool-collapse').hide();	// hide the old collapse button
		var t = $('<a href="javascript:void(0)"></a>').addClass('accordion-collapse accordion-expand').appendTo(tool);
		t.bind('click', function(){
			var index = getPanelIndex(container, pp);
			if (pp.panel('options').collapsed){
				select(container, index);
			} else {
				unselect(container, index);
			}
			return false;
		});
		pp.panel('options').collapsible ? t.show() : t.hide();
		
		header.click(function(){
			$(this).find('a.accordion-collapse:visible').triggerHandler('click');
			return false;
		});
	}
	
	/**
	 * select and set the specified panel active
	 */
	function select(container, which){
		var p = getPanel(container, which);
		if (!p){return}
		stopAnimate(container);
		var opts = $.data(container, 'accordion').options;
		p.panel('expand', opts.animate);
	}
	
	function unselect(container, which){
		var p = getPanel(container, which);
		if (!p){return}
		stopAnimate(container);
		var opts = $.data(container, 'accordion').options;
		p.panel('collapse', opts.animate);
	}
	
	function doFirstSelect(container){
		var opts = $.data(container, 'accordion').options;
		var p = findBy(container, 'selected', true);
		if (p){
			_select(getPanelIndex(container, p));
		} else {
			_select(opts.selected);
		}
		
		function _select(index){
			var animate = opts.animate;
			opts.animate = false;
			select(container, index);
			opts.animate = animate;
		}
	}
	
	/**
	 * stop the animation of all panels
	 */
	function stopAnimate(container){
		var panels = $.data(container, 'accordion').panels;
		for(var i=0; i<panels.length; i++){
			panels[i].stop(true,true);
		}
	}
	
	function add(container, options){
		var state = $.data(container, 'accordion');
		var opts = state.options;
		var panels = state.panels;
		if (options.selected == undefined) options.selected = true;

		stopAnimate(container);
		
		var pp = $('<div></div>').appendTo(container);
		panels.push(pp);
		createPanel(container, pp, options);
		setSize(container);
		
		opts.onAdd.call(container, options.title, panels.length-1);
		
		if (options.selected){
			select(container, panels.length-1);
		}
	}
	
	function remove(container, which){
		var state = $.data(container, 'accordion');
		var opts = state.options;
		var panels = state.panels;
		
		stopAnimate(container);
		
		var panel = getPanel(container, which);
		var title = panel.panel('options').title;
		var index = getPanelIndex(container, panel);
		
		if (!panel){return}
		if (opts.onBeforeRemove.call(container, title, index) == false){return}
		
		panels.splice(index, 1);
		panel.panel('destroy');
		if (panels.length){
			setSize(container);
			var curr = getSelected(container);
			if (!curr){
				select(container, 0);
			}
		}
		
		opts.onRemove.call(container, title, index);
	}
	
	$.fn.accordion = function(options, param){
		if (typeof options == 'string'){
			return $.fn.accordion.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'accordion');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'accordion', {
					options: $.extend({}, $.fn.accordion.defaults, $.fn.accordion.parseOptions(this), options),
					accordion: $(this).addClass('accordion'),
					panels: []
				});
				init(this);
			}
			
			setProperties(this);
			setSize(this);
			doFirstSelect(this);
		});
	};
	
	$.fn.accordion.methods = {
		options: function(jq){
			return $.data(jq[0], 'accordion').options;
		},
		panels: function(jq){
			return $.data(jq[0], 'accordion').panels;
		},
		resize: function(jq, param){
			return jq.each(function(){
				setSize(this, param);
			});
		},
		getSelections: function(jq){
			return getSelections(jq[0]);
		},
		getSelected: function(jq){
			return getSelected(jq[0]);
		},
		getPanel: function(jq, which){
			return getPanel(jq[0], which);
		},
		getPanelIndex: function(jq, panel){
			return getPanelIndex(jq[0], panel);
		},
		select: function(jq, which){
			return jq.each(function(){
				select(this, which);
			});
		},
		unselect: function(jq, which){
			return jq.each(function(){
				unselect(this, which);
			});
		},
		add: function(jq, options){
			return jq.each(function(){
				add(this, options);
			});
		},
		remove: function(jq, which){
			return jq.each(function(){
				remove(this, which);
			});
		}
	};
	
	$.fn.accordion.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [
			'width','height',
			{fit:'boolean',border:'boolean',animate:'boolean',multiple:'boolean',selected:'number'}
		]));
	};
	
	$.fn.accordion.defaults = {
		width: 'auto',
		height: 'auto',
		fit: false,
		border: true,
		animate: true,
		multiple: false,
		selected: 0,
		
		onSelect: function(title, index){},
		onUnselect: function(title, index){},
		onAdd: function(title, index){},
		onBeforeRemove: function(title, index){},
		onRemove: function(title, index){}
	};
})(jQuery);
/**
 * tabs - jQuery EasyUI
 * 
 * Dependencies:
 * 	 panel
 *   linkbutton
 * 
 */
(function($){
	
	/**
	 * set the tabs scrollers to show or not,
	 * dependent on the tabs count and width
	 */
	function setScrollers(container) {
		var opts = $.data(container, 'tabs').options;
		if (opts.tabPosition == 'left' || opts.tabPosition == 'right' || !opts.showHeader){return}
		
		var header = $(container).children('div.tabs-header');
		var tool = header.children('div.tabs-tool');
		var sLeft = header.children('div.tabs-scroller-left');
		var sRight = header.children('div.tabs-scroller-right');
		var wrap = header.children('div.tabs-wrap');
		
		// set the tool height
		var tHeight = header.outerHeight();
		if (opts.plain){
			tHeight -= tHeight - header.height();
		}
		tool._outerHeight(tHeight);
		
		var tabsWidth = 0;
		$('ul.tabs li', header).each(function(){
			tabsWidth += $(this).outerWidth(true);
		});
		var cWidth = header.width() - tool._outerWidth();
		
		if (tabsWidth > cWidth) {
			sLeft.add(sRight).show()._outerHeight(tHeight);
			if (opts.toolPosition == 'left'){
				tool.css({
					left: sLeft.outerWidth(),
					right: ''
				});
				wrap.css({
					marginLeft: sLeft.outerWidth() + tool._outerWidth(),
					marginRight: sRight._outerWidth(),
					width: cWidth - sLeft.outerWidth() - sRight.outerWidth()
				});
			} else {
				tool.css({
					left: '',
					right: sRight.outerWidth()
				});
				wrap.css({
					marginLeft: sLeft.outerWidth(),
					marginRight: sRight.outerWidth() + tool._outerWidth(),
					width: cWidth - sLeft.outerWidth() - sRight.outerWidth()
				});
			}
		} else {
			sLeft.add(sRight).hide();
			if (opts.toolPosition == 'left'){
				tool.css({
					left: 0,
					right: ''
				});
				wrap.css({
					marginLeft: tool._outerWidth(),
					marginRight: 0,
					width: cWidth
				});
			} else {
				tool.css({
					left: '',
					right: 0
				});
				wrap.css({
					marginLeft: 0,
					marginRight: tool._outerWidth(),
					width: cWidth
				});
			}
		}
	}
	
	function addTools(container){
		var opts = $.data(container, 'tabs').options;
		var header = $(container).children('div.tabs-header');
		if (opts.tools) {
			if (typeof opts.tools == 'string'){
				$(opts.tools).addClass('tabs-tool').appendTo(header);
				$(opts.tools).show();
			} else {
				header.children('div.tabs-tool').remove();
				var tools = $('<div class="tabs-tool"><table cellspacing="0" cellpadding="0" style="height:100%"><tr></tr></table></div>').appendTo(header);
				var tr = tools.find('tr');
				for(var i=0; i<opts.tools.length; i++){
					var td = $('<td></td>').appendTo(tr);
					var tool = $('<a href="javascript:void(0);"></a>').appendTo(td);
					tool[0].onclick = eval(opts.tools[i].handler || function(){});
					tool.linkbutton($.extend({}, opts.tools[i], {
						plain: true
					}));
				}
			}
		} else {
			header.children('div.tabs-tool').remove();
		}
	}
	
	function setSize(container, param) {
		var state = $.data(container, 'tabs');
		var opts = state.options;
		var cc = $(container);
		
		if (param){
			$.extend(opts, {
				width: param.width,
				height: param.height
			});
		}
		cc._size(opts);
		
		var header = cc.children('div.tabs-header');
		var panels = cc.children('div.tabs-panels');
		var wrap = header.find('div.tabs-wrap');
		var ul = wrap.find('.tabs');
		
		for(var i=0; i<state.tabs.length; i++){
			var p_opts = state.tabs[i].panel('options');
			var p_t = p_opts.tab.find('a.tabs-inner');
			var width = parseInt(p_opts.tabWidth || opts.tabWidth) || undefined;
			if (width){
				p_t._outerWidth(width);
			} else {
				p_t.css('width', '');
			}
			p_t._outerHeight(opts.tabHeight);
			p_t.css('lineHeight', p_t.height()+'px');
		}
		if (opts.tabPosition == 'left' || opts.tabPosition == 'right'){
			header._outerWidth(opts.showHeader ? opts.headerWidth : 0);
//			header._outerWidth(opts.headerWidth);
			panels._outerWidth(cc.width() - header.outerWidth());
			header.add(panels)._outerHeight(opts.height);
			wrap._outerWidth(header.width());
			ul._outerWidth(wrap.width()).css('height','');
		} else {
			var lrt = header.children('div.tabs-scroller-left,div.tabs-scroller-right,div.tabs-tool');
			header._outerWidth(opts.width).css('height','');
			if (opts.showHeader){
				header.css('background-color','');
				wrap.css('height','');
				lrt.show();
			} else {
				header.css('background-color','transparent');
				header._outerHeight(0);
				wrap._outerHeight(0);
				lrt.hide();
			}
			ul._outerHeight(opts.tabHeight).css('width','');
			
			setScrollers(container);
			
			panels._size('height', isNaN(opts.height) ? '' : (opts.height-header.outerHeight()));
			panels._size('width', isNaN(opts.width) ? '' : opts.width);
		}
	}
	
	/**
	 * set selected tab panel size
	 */
	function setSelectedSize(container){
		var opts = $.data(container, 'tabs').options;
		var tab = getSelectedTab(container);
		if (tab){
			var panels = $(container).children('div.tabs-panels');
			var width = opts.width=='auto' ? 'auto' : panels.width();
			var height = opts.height=='auto' ? 'auto' : panels.height();
			tab.panel('resize', {
				width: width,
				height: height
			});
		}
	}
	
	/**
	 * wrap the tabs header and body
	 */
	function wrapTabs(container) {
		var tabs = $.data(container, 'tabs').tabs;
		var cc = $(container);
		cc.addClass('tabs-container');
		var pp = $('<div class="tabs-panels"></div>').insertBefore(cc);
		cc.children('div').each(function(){
			pp[0].appendChild(this);
		});
		cc[0].appendChild(pp[0]);
//		cc.wrapInner('<div class="tabs-panels"/>');
		$('<div class="tabs-header">'
				+ '<div class="tabs-scroller-left"></div>'
				+ '<div class="tabs-scroller-right"></div>'
				+ '<div class="tabs-wrap">'
				+ '<ul class="tabs"></ul>'
				+ '</div>'
				+ '</div>').prependTo(container);
		
		cc.children('div.tabs-panels').children('div').each(function(i){
			var opts = $.extend({}, $.parser.parseOptions(this), {
				selected: ($(this).attr('selected') ? true : undefined)
			});
			var pp = $(this);
			tabs.push(pp);
			createTab(container, pp, opts);
		});
		
		cc.children('div.tabs-header').find('.tabs-scroller-left, .tabs-scroller-right').hover(
				function(){$(this).addClass('tabs-scroller-over');},
				function(){$(this).removeClass('tabs-scroller-over');}
		);
		cc.bind('_resize', function(e,force){
			if ($(this).hasClass('easyui-fluid') || force){
				setSize(container);
				setSelectedSize(container);
			}
			return false;
		});
	}
	
	function bindEvents(container){
		var state = $.data(container, 'tabs')
		var opts = state.options;
		$(container).children('div.tabs-header').unbind().bind('click', function(e){
			if ($(e.target).hasClass('tabs-scroller-left')){
				$(container).tabs('scrollBy', -opts.scrollIncrement);
			} else if ($(e.target).hasClass('tabs-scroller-right')){
				$(container).tabs('scrollBy', opts.scrollIncrement);
			} else {
				var li = $(e.target).closest('li');
				if (li.hasClass('tabs-disabled')){return;}
				var a = $(e.target).closest('a.tabs-close');
				if (a.length){
					closeTab(container, getLiIndex(li));
				} else if (li.length){
//					selectTab(container, getLiIndex(li));
					var index = getLiIndex(li);
					var popts = state.tabs[index].panel('options');
					if (popts.collapsible){
						popts.closed ? selectTab(container, index) : unselectTab(container, index);
					} else {
						selectTab(container, index);
					}
				}
			}
		}).bind('contextmenu', function(e){
			var li = $(e.target).closest('li');
			if (li.hasClass('tabs-disabled')){return;}
			if (li.length){
				opts.onContextMenu.call(container, e, li.find('span.tabs-title').html(), getLiIndex(li));
			}
		});
		
		function getLiIndex(li){
			var index = 0;
			li.parent().children('li').each(function(i){
				if (li[0] == this){
					index = i;
					return false;
				}
			});
			return index;
		}
	}
	
	function setProperties(container){
		var opts = $.data(container, 'tabs').options;
		var header = $(container).children('div.tabs-header');
		var panels = $(container).children('div.tabs-panels');
		
		header.removeClass('tabs-header-top tabs-header-bottom tabs-header-left tabs-header-right');
		panels.removeClass('tabs-panels-top tabs-panels-bottom tabs-panels-left tabs-panels-right');
		if (opts.tabPosition == 'top'){
			header.insertBefore(panels);
		} else if (opts.tabPosition == 'bottom'){
			header.insertAfter(panels);
			header.addClass('tabs-header-bottom');
			panels.addClass('tabs-panels-top');
		} else if (opts.tabPosition == 'left'){
			header.addClass('tabs-header-left');
			panels.addClass('tabs-panels-right');
		} else if (opts.tabPosition == 'right'){
			header.addClass('tabs-header-right');
			panels.addClass('tabs-panels-left');
		}
		
		if (opts.plain == true) {
			header.addClass('tabs-header-plain');
		} else {
			header.removeClass('tabs-header-plain');
		}
		if (opts.border == true){
			header.removeClass('tabs-header-noborder');
			panels.removeClass('tabs-panels-noborder');
		} else {
			header.addClass('tabs-header-noborder');
			panels.addClass('tabs-panels-noborder');
		}
	}
	
	function createTab(container, pp, options) {
		var state = $.data(container, 'tabs');
		options = options || {};
		
		// create panel
		pp.panel($.extend({}, options, {
			border: false,
			noheader: true,
			closed: true,
			doSize: false,
			iconCls: (options.icon ? options.icon : undefined),
			onLoad: function(){
				if (options.onLoad){
					options.onLoad.call(this, arguments);
				}
				state.options.onLoad.call(container, $(this));
			}
		}));
		
		var opts = pp.panel('options');
		var tabs = $(container).children('div.tabs-header').find('ul.tabs');
		
		opts.tab = $('<li></li>').appendTo(tabs);	// set the tab object in panel options
		opts.tab.append(
				'<a href="javascript:void(0)" class="tabs-inner">' +
				'<span class="tabs-title"></span>' +
				'<span class="tabs-icon"></span>' +
				'</a>'
		);
		
		// only update the tab header
		$(container).tabs('update', {
			tab: pp,
			options: opts,
			type: 'header'
		});
	}
	
	function addTab(container, options) {
		var state = $.data(container, 'tabs');
		var opts = state.options;
		var tabs = state.tabs;
		if (options.selected == undefined) options.selected = true;
		
		var pp = $('<div></div>').appendTo($(container).children('div.tabs-panels'));
		tabs.push(pp);
		createTab(container, pp, options);
		
		opts.onAdd.call(container, options.title, tabs.length-1);
		
		setSize(container);
		if (options.selected){
			selectTab(container, tabs.length-1);	// select the added tab panel
		}
	}
	
	/**
	 * update tab panel, param has following properties:
	 * tab: the tab panel to be updated
	 * options: the tab panel options
	 * type: the update type, possible values are: 'header','body','all'
	 */
	function updateTab(container, param){
		param.type = param.type || 'all';
		var selectHis = $.data(container, 'tabs').selectHis;
		var pp = param.tab;	// the tab panel
		var oldTitle = pp.panel('options').title;
		
		if (param.type == 'all' || param == 'body'){
			pp.panel($.extend({}, param.options, {
				iconCls: (param.options.icon ? param.options.icon : undefined)
			}));
		}
		if (param.type == 'all' || param.type == 'header'){
			var opts = pp.panel('options');	// get the tab panel options
			var tab = opts.tab;
			
			var s_title = tab.find('span.tabs-title');
			var s_icon = tab.find('span.tabs-icon');
			s_title.html(opts.title);
			s_icon.attr('class', 'tabs-icon');
			
			tab.find('a.tabs-close').remove();
			if (opts.closable){
				s_title.addClass('tabs-closable');
				$('<a href="javascript:void(0)" class="tabs-close"></a>').appendTo(tab);
			} else{
				s_title.removeClass('tabs-closable');
			}
			if (opts.iconCls){
				s_title.addClass('tabs-with-icon');
				s_icon.addClass(opts.iconCls);
			} else {
				s_title.removeClass('tabs-with-icon');
			}
			
			if (oldTitle != opts.title){
				for(var i=0; i<selectHis.length; i++){
					if (selectHis[i] == oldTitle){
						selectHis[i] = opts.title;
					}
				}
			}
			
			tab.find('span.tabs-p-tool').remove();
			if (opts.tools){
				var p_tool = $('<span class="tabs-p-tool"></span>').insertAfter(tab.find('a.tabs-inner'));
				if ($.isArray(opts.tools)){
					for(var i=0; i<opts.tools.length; i++){
						var t = $('<a href="javascript:void(0)"></a>').appendTo(p_tool);
						t.addClass(opts.tools[i].iconCls);
						if (opts.tools[i].handler){
							t.bind('click', {handler:opts.tools[i].handler}, function(e){
								if ($(this).parents('li').hasClass('tabs-disabled')){return;}
								e.data.handler.call(this);
							});
						}
					}
				} else {
					$(opts.tools).children().appendTo(p_tool);
				}
				var pr = p_tool.children().length * 12;
				if (opts.closable) {
					pr += 8;
				} else {
					pr -= 3;
					p_tool.css('right','5px');
				}
				s_title.css('padding-right', pr+'px');
			}
		}
		
		setSize(container);
		
		$.data(container, 'tabs').options.onUpdate.call(container, opts.title, getTabIndex(container, pp));
	}
	
	/**
	 * close a tab with specified index or title
	 */
	function closeTab(container, which) {
		var opts = $.data(container, 'tabs').options;
		var tabs = $.data(container, 'tabs').tabs;
		var selectHis = $.data(container, 'tabs').selectHis;
		
		if (!exists(container, which)) return;
		
		var tab = getTab(container, which);
		var title = tab.panel('options').title;
		var index = getTabIndex(container, tab);
		
		if (opts.onBeforeClose.call(container, title, index) == false) return;
		
		var tab = getTab(container, which, true);
		tab.panel('options').tab.remove();
		tab.panel('destroy');
		
		opts.onClose.call(container, title, index);
		
//		setScrollers(container);
		setSize(container);
		
		// remove the select history item
		for(var i=0; i<selectHis.length; i++){
			if (selectHis[i] == title){
				selectHis.splice(i, 1);
				i --;
			}
		}
		
		// select the nearest tab panel
		var hisTitle = selectHis.pop();
		if (hisTitle){
			selectTab(container, hisTitle);
		} else if (tabs.length){
			selectTab(container, 0);
		}
	}
	
	/**
	 * get the specified tab panel
	 */
	function getTab(container, which, removeit){
		var tabs = $.data(container, 'tabs').tabs;
		if (typeof which == 'number'){
			if (which < 0 || which >= tabs.length){
				return null;
			} else {
				var tab = tabs[which];
				if (removeit) {
					tabs.splice(which, 1);
				}
				return tab;
			}
		}
		for(var i=0; i<tabs.length; i++){
			var tab = tabs[i];
			if (tab.panel('options').title == which){
				if (removeit){
					tabs.splice(i, 1);
				}
				return tab;
			}
		}
		return null;
	}
	
	function getTabIndex(container, tab){
		var tabs = $.data(container, 'tabs').tabs;
		for(var i=0; i<tabs.length; i++){
			if (tabs[i][0] == $(tab)[0]){
				return i;
			}
		}
		return -1;
	}
	
	function getSelectedTab(container){
		var tabs = $.data(container, 'tabs').tabs;
		for(var i=0; i<tabs.length; i++){
			var tab = tabs[i];
			if (tab.panel('options').closed == false){
				return tab;
			}
		}
		return null;
	}
	
	/**
	 * do first select action, if no tab is setted the first tab will be selected.
	 */
	function doFirstSelect(container){
		var state = $.data(container, 'tabs')
		var tabs = state.tabs;
		for(var i=0; i<tabs.length; i++){
			if (tabs[i].panel('options').selected){
				selectTab(container, i);
				return;
			}
		}
//		if (tabs.length){
//			selectTab(container, 0);
//		}
		selectTab(container, state.options.selected);
	}
	
	function selectTab(container, which){
		var state = $.data(container, 'tabs');
		var opts = state.options;
		var tabs = state.tabs;
		var selectHis = state.selectHis;
		
		if (tabs.length == 0) {return;}
		
		var panel = getTab(container, which); // get the panel to be activated
		if (!panel){return}
		
		var selected = getSelectedTab(container);
		if (selected){
			if (panel[0] == selected[0]){
				setSelectedSize(container);
				return;
			}
			unselectTab(container, getTabIndex(container, selected));
			if (!selected.panel('options').closed){return}
		}
		
		panel.panel('open');
		var title = panel.panel('options').title;	// the panel title
		selectHis.push(title);	// push select history
		
		var tab = panel.panel('options').tab;	// get the tab object
		tab.addClass('tabs-selected');
		
		// scroll the tab to center position if required.
		var wrap = $(container).find('>div.tabs-header>div.tabs-wrap');
		var left = tab.position().left;
		var right = left + tab.outerWidth();
		if (left < 0 || right > wrap.width()){
			var deltaX = left - (wrap.width()-tab.width()) / 2;
			$(container).tabs('scrollBy', deltaX);
		} else {
			$(container).tabs('scrollBy', 0);
		}
		
		setSelectedSize(container);
		
		opts.onSelect.call(container, title, getTabIndex(container, panel));
	}
	
	function unselectTab(container, which){
		var state = $.data(container, 'tabs');
		var p = getTab(container, which);
		if (p){
			var opts = p.panel('options');
			if (!opts.closed){
				p.panel('close');
				if (opts.closed){
					opts.tab.removeClass('tabs-selected');
					state.options.onUnselect.call(container, opts.title, getTabIndex(container, p));
				}
			}
		}
	}
	
	function exists(container, which){
		return getTab(container, which) != null;
	}
	
	function showHeader(container, visible){
		var opts = $.data(container, 'tabs').options;
		opts.showHeader = visible;
		$(container).tabs('resize');
	}
	
	
	$.fn.tabs = function(options, param){
		if (typeof options == 'string') {
			return $.fn.tabs.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'tabs');
			if (state) {
				$.extend(state.options, options);
			} else {
				$.data(this, 'tabs', {
					options: $.extend({},$.fn.tabs.defaults, $.fn.tabs.parseOptions(this), options),
					tabs: [],
					selectHis: []
				});
				wrapTabs(this);
			}
			
			addTools(this);
			setProperties(this);
			setSize(this);
			bindEvents(this);
			
			doFirstSelect(this);
		});
	};
	
	$.fn.tabs.methods = {
		options: function(jq){
			var cc = jq[0];
			var opts = $.data(cc, 'tabs').options;
			var s = getSelectedTab(cc);
			opts.selected = s ? getTabIndex(cc, s) : -1;
			return opts;
		},
		tabs: function(jq){
			return $.data(jq[0], 'tabs').tabs;
		},
		resize: function(jq, param){
			return jq.each(function(){
				setSize(this, param);
				setSelectedSize(this);
			});
		},
		add: function(jq, options){
			return jq.each(function(){
				addTab(this, options);
			});
		},
		close: function(jq, which){
			return jq.each(function(){
				closeTab(this, which);
			});
		},
		getTab: function(jq, which){
			return getTab(jq[0], which);
		},
		getTabIndex: function(jq, tab){
			return getTabIndex(jq[0], tab);
		},
		getSelected: function(jq){
			return getSelectedTab(jq[0]);
		},
		select: function(jq, which){
			return jq.each(function(){
				selectTab(this, which);
			});
		},
		unselect: function(jq, which){
			return jq.each(function(){
				unselectTab(this, which);
			});
		},
		exists: function(jq, which){
			return exists(jq[0], which);
		},
		update: function(jq, options){
			return jq.each(function(){
				updateTab(this, options);
			});
		},
		enableTab: function(jq, which){
			return jq.each(function(){
				$(this).tabs('getTab', which).panel('options').tab.removeClass('tabs-disabled');
			});
		},
		disableTab: function(jq, which){
			return jq.each(function(){
				$(this).tabs('getTab', which).panel('options').tab.addClass('tabs-disabled');
			});
		},
		showHeader: function(jq){
			return jq.each(function(){
				showHeader(this, true);
			});
		},
		hideHeader: function(jq){
			return jq.each(function(){
				showHeader(this, false);
			});
		},
		scrollBy: function(jq, deltaX){	// scroll the tab header by the specified amount of pixels
			return jq.each(function(){
				var opts = $(this).tabs('options');
				var wrap = $(this).find('>div.tabs-header>div.tabs-wrap');
				var pos = Math.min(wrap._scrollLeft() + deltaX, getMaxScrollWidth());
				wrap.animate({scrollLeft: pos}, opts.scrollDuration);
				
				function getMaxScrollWidth(){
					var w = 0;
					var ul = wrap.children('ul');
					ul.children('li').each(function(){
						w += $(this).outerWidth(true);
					});
					return w - wrap.width() + (ul.outerWidth() - ul.width());
				}
			});
		}
	};
	
	$.fn.tabs.parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, [
			'tools','toolPosition','tabPosition',
			{fit:'boolean',border:'boolean',plain:'boolean',headerWidth:'number',tabWidth:'number',tabHeight:'number',selected:'number',showHeader:'boolean'}
		]));
	};
	
	$.fn.tabs.defaults = {
		width: 'auto',
		height: 'auto',
		headerWidth: 150,	// the tab header width, it is valid only when tabPosition set to 'left' or 'right' 
		tabWidth: 'auto',	// the tab width
		tabHeight: 27,		// the tab height
		selected: 0,		// the initialized selected tab index
		showHeader: true,
		plain: false,
		fit: false,
		border: true,
		tools: null,
		toolPosition: 'right',	// left,right
		tabPosition: 'top',		// possible values: top,bottom
		scrollIncrement: 100,
		scrollDuration: 400,
		onLoad: function(panel){},
		onSelect: function(title, index){},
		onUnselect: function(title, index){},
		onBeforeClose: function(title, index){},
		onClose: function(title, index){},
		onAdd: function(title, index){},
		onUpdate: function(title, index){},
		onContextMenu: function(e, title, index){}
	};
})(jQuery);

(function($) {
	var _37d = false;
	function _37e(_37f, _380) {
		var _381 = $.data(_37f, "layout");
		var opts = _381.options;
		var _382 = _381.panels;
		var cc = $(_37f);
		if (_380) {
			$.extend(opts, {
				width : _380.width,
				height : _380.height
			});
		}
		if (_37f.tagName.toLowerCase() == "body") {
			cc._size("fit");
		} else {
			cc._size(opts);
		}
		var cpos = {
			top : 0,
			left : 0,
			width : cc.width(),
			height : cc.height()
		};
		_383(_384(_382.expandNorth) ? _382.expandNorth : _382.north, "n");
		_383(_384(_382.expandSouth) ? _382.expandSouth : _382.south, "s");
		_385(_384(_382.expandEast) ? _382.expandEast : _382.east, "e");
		_385(_384(_382.expandWest) ? _382.expandWest : _382.west, "w");
		_382.center.panel("resize", cpos);
		function _383(pp, type) {
			if (!pp.length || !_384(pp)) {
				return;
			}
			var opts = pp.panel("options");
			pp.panel("resize", {
				width : cc.width(),
				height : opts.height
			});
			var _386 = pp.panel("panel").outerHeight();
			pp.panel("move", {
				left : 0,
				top : (type == "n" ? 0 : cc.height() - _386)
			});
			cpos.height -= _386;
			if (type == "n") {
				cpos.top += _386;
				if (!opts.split && opts.border) {
					cpos.top--;
				}
			}
			if (!opts.split && opts.border) {
				cpos.height++;
			}
		}
		;
		function _385(pp, type) {
			if (!pp.length || !_384(pp)) {
				return;
			}
			var opts = pp.panel("options");
			pp.panel("resize", {
				width : opts.width,
				height : cpos.height
			});
			var _387 = pp.panel("panel").outerWidth();
			pp.panel("move", {
				left : (type == "e" ? cc.width() - _387 : 0),
				top : cpos.top
			});
			cpos.width -= _387;
			if (type == "w") {
				cpos.left += _387;
				if (!opts.split && opts.border) {
					cpos.left--;
				}
			}
			if (!opts.split && opts.border) {
				cpos.width++;
			}
		}
		;
	}
	;
	function init(_388) {
		var cc = $(_388);
		cc.addClass("layout");
		function _389(cc) {
			cc.children("div").each(function() {
				var opts = $.fn.layout.parsePanelOptions(this);
				if ("north,south,east,west,center".indexOf(opts.region) >= 0) {
					_38b(_388, opts, this);
				}
			});
		}
		;
		cc.children("form").length ? _389(cc.children("form")) : _389(cc);
		cc
				.append("<div class=\"layout-split-proxy-h\"></div><div class=\"layout-split-proxy-v\"></div>");
		cc.bind("_resize", function(e, _38a) {
			if ($(this).hasClass("easyui-fluid") || _38a) {
				_37e(_388);
			}
			return false;
		});
	}
	;
	function _38b(_38c, _38d, el) {
		_38d.region = _38d.region || "center";
		var _38e = $.data(_38c, "layout").panels;
		var cc = $(_38c);
		var dir = _38d.region;
		if (_38e[dir].length) {
			return;
		}
		var pp = $(el);
		if (!pp.length) {
			pp = $("<div></div>").appendTo(cc);
		}
		var _38f = $.extend({}, $.fn.layout.paneldefaults, {
			width : (pp.length ? parseInt(pp[0].style.width) || pp.outerWidth()
					: "auto"),
			height : (pp.length ? parseInt(pp[0].style.height)
					|| pp.outerHeight() : "auto"),
			doSize : false,
			collapsible : true,
			cls : ("layout-panel layout-panel-" + dir),
			bodyCls : "layout-body",
			onOpen : function() {
				var tool = $(this).panel("header").children("div.panel-tool");
				tool.children("a.panel-tool-collapse").hide();
				var _390 = {
					north : "up",
					south : "down",
					east : "right",
					west : "left"
				};
				if (!_390[dir]) {
					return;
				}
				var _391 = "layout-button-" + _390[dir];
				var t = tool.children("a." + _391);
				if (!t.length) {
					t = $("<a href=\"javascript:void(0)\"></a>").addClass(_391)
							.appendTo(tool);
					t.bind("click", {
						dir : dir
					}, function(e) {
						_39d(_38c, e.data.dir);
						return false;
					});
				}
				$(this).panel("options").collapsible ? t.show() : t.hide();
			}
		}, _38d);
		pp.panel(_38f);
		_38e[dir] = pp;
		if (pp.panel("options").split) {
			var _392 = pp.panel("panel");
			_392.addClass("layout-split-" + dir);
			var _393 = "";
			if (dir == "north") {
				_393 = "s";
			}
			if (dir == "south") {
				_393 = "n";
			}
			if (dir == "east") {
				_393 = "w";
			}
			if (dir == "west") {
				_393 = "e";
			}
			_392
					.resizable($
							.extend(
									{},
									{
										handles : _393,
										onStartResize : function(e) {
											_37d = true;
											if (dir == "north"
													|| dir == "south") {
												var _394 = $(
														">div.layout-split-proxy-v",
														_38c);
											} else {
												var _394 = $(
														">div.layout-split-proxy-h",
														_38c);
											}
											var top = 0, left = 0, _395 = 0, _396 = 0;
											var pos = {
												display : "block"
											};
											if (dir == "north") {
												pos.top = parseInt(_392
														.css("top"))
														+ _392.outerHeight()
														- _394.height();
												pos.left = parseInt(_392
														.css("left"));
												pos.width = _392.outerWidth();
												pos.height = _394.height();
											} else {
												if (dir == "south") {
													pos.top = parseInt(_392
															.css("top"));
													pos.left = parseInt(_392
															.css("left"));
													pos.width = _392
															.outerWidth();
													pos.height = _394.height();
												} else {
													if (dir == "east") {
														pos.top = parseInt(_392
																.css("top")) || 0;
														pos.left = parseInt(_392
																.css("left")) || 0;
														pos.width = _394
																.width();
														pos.height = _392
																.outerHeight();
													} else {
														if (dir == "west") {
															pos.top = parseInt(_392
																	.css("top")) || 0;
															pos.left = _392
																	.outerWidth()
																	- _394
																			.width();
															pos.width = _394
																	.width();
															pos.height = _392
																	.outerHeight();
														}
													}
												}
											}
											_394.css(pos);
											$(
													"<div class=\"layout-mask\"></div>")
													.css({
														left : 0,
														top : 0,
														width : cc.width(),
														height : cc.height()
													}).appendTo(cc);
										},
										onResize : function(e) {
											if (dir == "north"
													|| dir == "south") {
												var _397 = $(
														">div.layout-split-proxy-v",
														_38c);
												_397.css("top", e.pageY
														- $(_38c).offset().top
														- _397.height() / 2);
											} else {
												var _397 = $(
														">div.layout-split-proxy-h",
														_38c);
												_397.css("left", e.pageX
														- $(_38c).offset().left
														- _397.width() / 2);
											}
											return false;
										},
										onStopResize : function(e) {
											cc
													.children(
															"div.layout-split-proxy-v,div.layout-split-proxy-h")
													.hide();
											pp.panel("resize", e.data);
											_37e(_38c);
											_37d = false;
											cc.find(">div.layout-mask")
													.remove();
										}
									}, _38d));
		}
	}
	;
	function _398(_399, _39a) {
		var _39b = $.data(_399, "layout").panels;
		if (_39b[_39a].length) {
			_39b[_39a].panel("destroy");
			_39b[_39a] = $();
			var _39c = "expand" + _39a.substring(0, 1).toUpperCase()
					+ _39a.substring(1);
			if (_39b[_39c]) {
				_39b[_39c].panel("destroy");
				_39b[_39c] = undefined;
			}
		}
	}
	;
	function _39d(_39e, _39f, _3a0) {
		if (_3a0 == undefined) {
			_3a0 = "normal";
		}
		var _3a1 = $.data(_39e, "layout").panels;
		var p = _3a1[_39f];
		var _3a2 = p.panel("options");
		if (_3a2.onBeforeCollapse.call(p) == false) {
			return;
		}
		var _3a3 = "expand" + _39f.substring(0, 1).toUpperCase()
				+ _39f.substring(1);
		if (!_3a1[_3a3]) {
			_3a1[_3a3] = _3a4(_39f);
			_3a1[_3a3]
					.panel("panel")
					.bind(
							"click",
							function() {
								p.panel("expand", false).panel("open");
								var _3a5 = _3a6();
								p.panel("resize", _3a5.collapse);
								p
										.panel("panel")
										.animate(
												_3a5.expand,
												function() {
													$(this)
															.unbind(".layout")
															.bind(
																	"mouseleave.layout",
																	{
																		region : _39f
																	},
																	function(e) {
																		if (_37d == true) {
																			return;
																		}
																		if ($("body>div.combo-p>div.combo-panel:visible").length) {
																			return;
																		}
																		_39d(
																				_39e,
																				e.data.region);
																	});
												});
								return false;
							});
		}
		var _3a7 = _3a6();
		if (!_384(_3a1[_3a3])) {
			_3a1.center.panel("resize", _3a7.resizeC);
		}
		p.panel("panel").animate(_3a7.collapse, _3a0, function() {
			p.panel("collapse", false).panel("close");
			_3a1[_3a3].panel("open").panel("resize", _3a7.expandP);
			$(this).unbind(".layout");
		});
		function _3a4(dir) {
			var icon;
			if (dir == "east") {
				icon = "layout-button-left";
			} else {
				if (dir == "west") {
					icon = "layout-button-right";
				} else {
					if (dir == "north") {
						icon = "layout-button-down";
					} else {
						if (dir == "south") {
							icon = "layout-button-up";
						}
					}
				}
			}
			var p = $("<div></div>").appendTo(_39e);
			p.panel($.extend({}, $.fn.layout.paneldefaults, {
				cls : ("layout-expand layout-expand-" + dir),
				title : "&nbsp;",
				closed : true,
				minWidth : 0,
				minHeight : 0,
				doSize : false,
				tools : [ {
					iconCls : icon,
					handler : function() {
						_3ad(_39e, _39f);
						return false;
					}
				} ]
			}));
			p.panel("panel").hover(function() {
				$(this).addClass("layout-expand-over");
			}, function() {
				$(this).removeClass("layout-expand-over");
			});
			return p;
		}
		;
		function _3a6() {
			var cc = $(_39e);
			var _3a8 = _3a1.center.panel("options");
			var _3a9 = _3a2.collapsedSize;
			if (_39f == "east") {
				var _3aa = p.panel("panel")._outerWidth();
				var _3ab = _3a8.width + _3aa - _3a9;
				if (_3a2.split || !_3a2.border) {
					_3ab++;
				}
				return {
					resizeC : {
						width : _3ab
					},
					expand : {
						left : cc.width() - _3aa
					},
					expandP : {
						top : _3a8.top,
						left : cc.width() - _3a9,
						width : _3a9,
						height : _3a8.height
					},
					collapse : {
						left : cc.width(),
						top : _3a8.top,
						height : _3a8.height
					}
				};
			} else {
				if (_39f == "west") {
					var _3aa = p.panel("panel")._outerWidth();
					var _3ab = _3a8.width + _3aa - _3a9;
					if (_3a2.split || !_3a2.border) {
						_3ab++;
					}
					return {
						resizeC : {
							width : _3ab,
							left : _3a9 - 1
						},
						expand : {
							left : 0
						},
						expandP : {
							left : 0,
							top : _3a8.top,
							width : _3a9,
							height : _3a8.height
						},
						collapse : {
							left : -_3aa,
							top : _3a8.top,
							height : _3a8.height
						}
					};
				} else {
					if (_39f == "north") {
						var _3ac = p.panel("panel")._outerHeight();
						var hh = _3a8.height;
						if (!_384(_3a1.expandNorth)) {
							hh += _3ac - _3a9
									+ ((_3a2.split || !_3a2.border) ? 1 : 0);
						}
						_3a1.east.add(_3a1.west).add(_3a1.expandEast).add(
								_3a1.expandWest).panel("resize", {
							top : _3a9 - 1,
							height : hh
						});
						return {
							resizeC : {
								top : _3a9 - 1,
								height : hh
							},
							expand : {
								top : 0
							},
							expandP : {
								top : 0,
								left : 0,
								width : cc.width(),
								height : _3a9
							},
							collapse : {
								top : -_3ac,
								width : cc.width()
							}
						};
					} else {
						if (_39f == "south") {
							var _3ac = p.panel("panel")._outerHeight();
							var hh = _3a8.height;
							if (!_384(_3a1.expandSouth)) {
								hh += _3ac
										- _3a9
										+ ((_3a2.split || !_3a2.border) ? 1 : 0);
							}
							_3a1.east.add(_3a1.west).add(_3a1.expandEast).add(
									_3a1.expandWest).panel("resize", {
								height : hh
							});
							return {
								resizeC : {
									height : hh
								},
								expand : {
									top : cc.height() - _3ac
								},
								expandP : {
									top : cc.height() - _3a9,
									left : 0,
									width : cc.width(),
									height : _3a9
								},
								collapse : {
									top : cc.height(),
									width : cc.width()
								}
							};
						}
					}
				}
			}
		}
		;
	}
	;
	function _3ad(_3ae, _3af) {
		var _3b0 = $.data(_3ae, "layout").panels;
		var p = _3b0[_3af];
		var _3b1 = p.panel("options");
		if (_3b1.onBeforeExpand.call(p) == false) {
			return;
		}
		var _3b2 = "expand" + _3af.substring(0, 1).toUpperCase()
				+ _3af.substring(1);
		if (_3b0[_3b2]) {
			_3b0[_3b2].panel("close");
			p.panel("panel").stop(true, true);
			p.panel("expand", false).panel("open");
			var _3b3 = _3b4();
			p.panel("resize", _3b3.collapse);
			p.panel("panel").animate(_3b3.expand, function() {
				_37e(_3ae);
			});
		}
		function _3b4() {
			var cc = $(_3ae);
			var _3b5 = _3b0.center.panel("options");
			if (_3af == "east" && _3b0.expandEast) {
				return {
					collapse : {
						left : cc.width(),
						top : _3b5.top,
						height : _3b5.height
					},
					expand : {
						left : cc.width() - p.panel("panel")._outerWidth()
					}
				};
			} else {
				if (_3af == "west" && _3b0.expandWest) {
					return {
						collapse : {
							left : -p.panel("panel")._outerWidth(),
							top : _3b5.top,
							height : _3b5.height
						},
						expand : {
							left : 0
						}
					};
				} else {
					if (_3af == "north" && _3b0.expandNorth) {
						return {
							collapse : {
								top : -p.panel("panel")._outerHeight(),
								width : cc.width()
							},
							expand : {
								top : 0
							}
						};
					} else {
						if (_3af == "south" && _3b0.expandSouth) {
							return {
								collapse : {
									top : cc.height(),
									width : cc.width()
								},
								expand : {
									top : cc.height()
											- p.panel("panel")._outerHeight()
								}
							};
						}
					}
				}
			}
		}
		;
	}
	;
	function _384(pp) {
		if (!pp) {
			return false;
		}
		if (pp.length) {
			return pp.panel("panel").is(":visible");
		} else {
			return false;
		}
	}
	;
	function _3b6(_3b7) {
		var _3b8 = $.data(_3b7, "layout").panels;
		if (_3b8.east.length && _3b8.east.panel("options").collapsed) {
			_39d(_3b7, "east", 0);
		}
		if (_3b8.west.length && _3b8.west.panel("options").collapsed) {
			_39d(_3b7, "west", 0);
		}
		if (_3b8.north.length && _3b8.north.panel("options").collapsed) {
			_39d(_3b7, "north", 0);
		}
		if (_3b8.south.length && _3b8.south.panel("options").collapsed) {
			_39d(_3b7, "south", 0);
		}
	}
	;
	$.fn.layout = function(_3b9, _3ba) {
		if (typeof _3b9 == "string") {
			return $.fn.layout.methods[_3b9](this, _3ba);
		}
		_3b9 = _3b9 || {};
		return this.each(function() {
			var _3bb = $.data(this, "layout");
			if (_3bb) {
				$.extend(_3bb.options, _3b9);
			} else {
				var opts = $.extend({}, $.fn.layout.defaults, $.fn.layout
						.parseOptions(this), _3b9);
				$.data(this, "layout", {
					options : opts,
					panels : {
						center : $(),
						north : $(),
						south : $(),
						east : $(),
						west : $()
					}
				});
				init(this);
			}
			_37e(this);
			_3b6(this);
		});
	};
	$.fn.layout.methods = {
		options : function(jq) {
			return $.data(jq[0], "layout").options;
		},
		resize : function(jq, _3bc) {
			return jq.each(function() {
				_37e(this, _3bc);
			});
		},
		panel : function(jq, _3bd) {
			return $.data(jq[0], "layout").panels[_3bd];
		},
		collapse : function(jq, _3be) {
			return jq.each(function() {
				_39d(this, _3be);
			});
		},
		expand : function(jq, _3bf) {
			return jq.each(function() {
				_3ad(this, _3bf);
			});
		},
		add : function(jq, _3c0) {
			return jq
					.each(function() {
						_38b(this, _3c0);
						_37e(this);
						if ($(this).layout("panel", _3c0.region).panel(
								"options").collapsed) {
							_39d(this, _3c0.region, 0);
						}
					});
		},
		remove : function(jq, _3c1) {
			return jq.each(function() {
				_398(this, _3c1);
				_37e(this);
			});
		}
	};
	$.fn.layout.parseOptions = function(_3c2) {
		return $.extend({}, $.parser.parseOptions(_3c2, [ {
			fit : "boolean"
		} ]));
	};
	$.fn.layout.defaults = {
		fit : false
	};
	$.fn.layout.parsePanelOptions = function(_3c3) {
		var t = $(_3c3);
		return $.extend({}, $.fn.panel.parseOptions(_3c3), $.parser
				.parseOptions(_3c3, [ "region", {
					split : "boolean",
					collpasedSize : "number",
					minWidth : "number",
					minHeight : "number",
					maxWidth : "number",
					maxHeight : "number"
				} ]));
	};
	$.fn.layout.paneldefaults = $.extend({}, $.fn.panel.defaults, {
		region : null,
		split : false,
		collapsedSize : 28,
		minWidth : 10,
		minHeight : 10,
		maxWidth : 10000,
		maxHeight : 10000
	});
})(jQuery);
/**
 * menu - jQuery EasyUI
 * 
 */
(function($){
	
	/**
	 * initialize the target menu, the function can be invoked only once
	 */
	function init(target){
		$(target).appendTo('body');
		$(target).addClass('menu-top');	// the top menu
		
		$(document).unbind('.menu').bind('mousedown.menu', function(e){
//			var allMenu = $('body>div.menu:visible');
//			var m = $(e.target).closest('div.menu', allMenu);
			var m = $(e.target).closest('div.menu,div.combo-p');
			if (m.length){return}
			$('body>div.menu-top:visible').menu('hide');
		});
		
		var menus = splitMenu($(target));
		for(var i=0; i<menus.length; i++){
			createMenu(menus[i]);
		}
		
		function splitMenu(menu){
			var menus = [];
			menu.addClass('menu');
			menus.push(menu);
			if (!menu.hasClass('menu-content')){
				menu.children('div').each(function(){
					var submenu = $(this).children('div');
					if (submenu.length){
						submenu.insertAfter(target);
						this.submenu = submenu;		// point to the sub menu
						var mm = splitMenu(submenu);
						menus = menus.concat(mm);
					}
				});
			}
			return menus;
		}
		
		function createMenu(menu){
			var wh = $.parser.parseOptions(menu[0], ['width','height']);
			menu[0].originalHeight = wh.height || 0;
			if (menu.hasClass('menu-content')){
				menu[0].originalWidth = wh.width || menu._outerWidth();
			} else {
				menu[0].originalWidth = wh.width || 0;
				menu.children('div').each(function(){
					var item = $(this);
					var itemOpts = $.extend({}, $.parser.parseOptions(this,['name','iconCls','href',{separator:'boolean'}]), {
						disabled: (item.attr('disabled') ? true : undefined)
					});
					if (itemOpts.separator){
						item.addClass('menu-sep');
					}
					if (!item.hasClass('menu-sep')){
						item[0].itemName = itemOpts.name || '';
						item[0].itemHref = itemOpts.href || '';
						
						var text = item.addClass('menu-item').html();
						item.empty().append($('<div class="menu-text"></div>').html(text));
						if (itemOpts.iconCls){
							$('<div class="menu-icon"></div>').addClass(itemOpts.iconCls).appendTo(item);
						}
						if (itemOpts.disabled){
							setDisabled(target, item[0], true);
						}
						if (item[0].submenu){
							$('<div class="menu-rightarrow"></div>').appendTo(item);	// has sub menu
						}
						
						bindMenuItemEvent(target, item);
					}
				});
				$('<div class="menu-line"></div>').prependTo(menu);
			}
			setMenuSize(target, menu);
			menu.hide();
			
			bindMenuEvent(target, menu);
		}
	}
	
	function setMenuSize(target, menu){
		var opts = $.data(target, 'menu').options;
		var style = menu.attr('style') || '';
		menu.css({
			display: 'block',
			left:-10000,
			height: 'auto',
			overflow: 'hidden'
		});
		
		var el = menu[0];
		var width = el.originalWidth || 0;
		if (!width){
			width = 0;
			menu.find('div.menu-text').each(function(){
				if (width < $(this)._outerWidth()){
					width = $(this)._outerWidth();
				}
				$(this).closest('div.menu-item')._outerHeight($(this)._outerHeight()+2);
			});
			width += 40;
		}
		
		width = Math.max(width, opts.minWidth);
//		var height = el.originalHeight || menu.outerHeight();
		var height = el.originalHeight || 0;
		if (!height){
			height = menu.outerHeight();
			
			if (menu.hasClass('menu-top') && opts.alignTo){
				var at = $(opts.alignTo);
				var h1 = at.offset().top - $(document).scrollTop();
				var h2 = $(window)._outerHeight() + $(document).scrollTop() - at.offset().top - at._outerHeight();
				height = Math.min(height, Math.max(h1, h2));
			} else if (height > $(window)._outerHeight()){
				height = $(window).height();
				style += ';overflow:auto';
			} else {
				style += ';overflow:hidden';
			}
			
//			if (height > $(window).height()-5){
//				height = $(window).height()-5;
//				style += ';overflow:auto';
//			} else {
//				style += ';overflow:hidden';
//			}
		}
		var lineHeight = Math.max(el.originalHeight, menu.outerHeight()) - 2;
		menu._outerWidth(width)._outerHeight(height);
		menu.children('div.menu-line')._outerHeight(lineHeight);
		
		style += ';width:' + el.style.width + ';height:' + el.style.height;
		
		menu.attr('style', style);
	}
	
	/**
	 * bind menu event
	 */
	function bindMenuEvent(target, menu){
		var state = $.data(target, 'menu');
		menu.unbind('.menu').bind('mouseenter.menu', function(){
			if (state.timer){
				clearTimeout(state.timer);
				state.timer = null;
			}
		}).bind('mouseleave.menu', function(){
			if (state.options.hideOnUnhover){
				state.timer = setTimeout(function(){
					hideAll(target);
				}, state.options.duration);
			}
		});
	}
	
	/**
	 * bind menu item event
	 */
	function bindMenuItemEvent(target, item){
		if (!item.hasClass('menu-item')){return}
		item.unbind('.menu');
		item.bind('click.menu', function(){
			if ($(this).hasClass('menu-item-disabled')){
				return;
			}
			// only the sub menu clicked can hide all menus
			if (!this.submenu){
				hideAll(target);
				var href = this.itemHref;
				if (href){
					location.href = href;
				}
			}
			var item = $(target).menu('getItem', this);
			$.data(target, 'menu').options.onClick.call(target, item);
		}).bind('mouseenter.menu', function(e){
			// hide other menu
			item.siblings().each(function(){
				if (this.submenu){
					hideMenu(this.submenu);
				}
				$(this).removeClass('menu-active');
			});
			// show this menu
			item.addClass('menu-active');
			
			if ($(this).hasClass('menu-item-disabled')){
				item.addClass('menu-active-disabled');
				return;
			}
			
			var submenu = item[0].submenu;
			if (submenu){
				$(target).menu('show', {
					menu: submenu,
					parent: item
				});
			}
		}).bind('mouseleave.menu', function(e){
			item.removeClass('menu-active menu-active-disabled');
			var submenu = item[0].submenu;
			if (submenu){
				if (e.pageX>=parseInt(submenu.css('left'))){
					item.addClass('menu-active');
				} else {
					hideMenu(submenu);
				}
				
			} else {
				item.removeClass('menu-active');
			}
		});
	}
	
	/**
	 * hide top menu and it's all sub menus
	 */
	function hideAll(target){
		var state = $.data(target, 'menu');
		if (state){
			if ($(target).is(':visible')){
				hideMenu($(target));
				state.options.onHide.call(target);
			}
		}
		return false;
	}
	
	/**
	 * show the menu, the 'param' object has one or more properties:
	 * left: the left position to display
	 * top: the top position to display
	 * menu: the menu to display, if not defined, the 'target menu' is used
	 * parent: the parent menu item to align to
	 * alignTo: the element object to align to
	 */
	function showMenu(target, param){
		var left,top;
		param = param || {};
		var menu = $(param.menu || target);
		$(target).menu('resize', menu[0]);
		if (menu.hasClass('menu-top')){
			var opts = $.data(target, 'menu').options;
			$.extend(opts, param);
			left = opts.left;
			top = opts.top;
			if (opts.alignTo){
				var at = $(opts.alignTo);
				left = at.offset().left;
				top = at.offset().top + at._outerHeight();
				if (opts.align == 'right'){
					left += at.outerWidth() - menu.outerWidth();
				}
			}
			if (left + menu.outerWidth() > $(window)._outerWidth() + $(document)._scrollLeft()){
				left = $(window)._outerWidth() + $(document).scrollLeft() - menu.outerWidth() - 5;
			}
			if (left < 0){left = 0;}
			top = _fixTop(top, opts.alignTo);
		} else {
			var parent = param.parent;	// the parent menu item
			left = parent.offset().left + parent.outerWidth() - 2;
			if (left + menu.outerWidth() + 5 > $(window)._outerWidth() + $(document).scrollLeft()){
				left = parent.offset().left - menu.outerWidth() + 2;
			}
			top = _fixTop(parent.offset().top - 3);
		}
		
		function _fixTop(top, alignTo){
			if (top + menu.outerHeight() > $(window)._outerHeight() + $(document).scrollTop()){
				if (alignTo){
					top = $(alignTo).offset().top - menu._outerHeight();
				} else {
					top = $(window)._outerHeight() + $(document).scrollTop() - menu.outerHeight();
				}
			}
			if (top < 0){top = 0;}
			return top;
		}
		
		menu.css({left:left,top:top});
		menu.show(0, function(){
			if (!menu[0].shadow){
				menu[0].shadow = $('<div class="menu-shadow"></div>').insertAfter(menu);
			}
			menu[0].shadow.css({
				display:'block',
				zIndex:$.fn.menu.defaults.zIndex++,
				left:menu.css('left'),
				top:menu.css('top'),
				width:menu.outerWidth(),
				height:menu.outerHeight()
			});
			menu.css('z-index', $.fn.menu.defaults.zIndex++);
			if (menu.hasClass('menu-top')){
				$.data(menu[0], 'menu').options.onShow.call(menu[0]);
			}
		});
	}
	
	function hideMenu(menu){
		if (!menu) return;
		
		hideit(menu);
		menu.find('div.menu-item').each(function(){
			if (this.submenu){
				hideMenu(this.submenu);
			}
			$(this).removeClass('menu-active');
		});
		
		function hideit(m){
			m.stop(true,true);
			if (m[0].shadow){
				m[0].shadow.hide();
			}
			m.hide();
		}
	}
	
	function findItem(target, text){
		var result = null;
		var tmp = $('<div></div>');
		function find(menu){
			menu.children('div.menu-item').each(function(){
				var item = $(target).menu('getItem', this);
				var s = tmp.empty().html(item.text).text();
				if (text == $.trim(s)) {
					result = item;
				} else if (this.submenu && !result){
					find(this.submenu);
				}
			});
		}
		find($(target));
		tmp.remove();
		return result;
	}
	
	function setDisabled(target, itemEl, disabled){
		var t = $(itemEl);
		if (!t.hasClass('menu-item')){return}
		
		if (disabled){
			t.addClass('menu-item-disabled');
			if (itemEl.onclick){
				itemEl.onclick1 = itemEl.onclick;
				itemEl.onclick = null;
			}
		} else {
			t.removeClass('menu-item-disabled');
			if (itemEl.onclick1){
				itemEl.onclick = itemEl.onclick1;
				itemEl.onclick1 = null;
			}
		}
	}
	
	function appendItem(target, param){
		var menu = $(target);
		if (param.parent){
			if (!param.parent.submenu){
				var submenu = $('<div class="menu"><div class="menu-line"></div></div>').appendTo('body');
				submenu.hide();
				param.parent.submenu = submenu;
				$('<div class="menu-rightarrow"></div>').appendTo(param.parent);
			}
			menu = param.parent.submenu;
		}
		if (param.separator){
			var item = $('<div class="menu-sep"></div>').appendTo(menu);
		} else {
			var item = $('<div class="menu-item"></div>').appendTo(menu);
			$('<div class="menu-text"></div>').html(param.text).appendTo(item);
		}
		if (param.iconCls) $('<div class="menu-icon"></div>').addClass(param.iconCls).appendTo(item);
		if (param.id) item.attr('id', param.id);
		if (param.name){item[0].itemName = param.name}
		if (param.href){item[0].itemHref = param.href}
		if (param.onclick){
			if (typeof param.onclick == 'string'){
				item.attr('onclick', param.onclick);
			} else {
				item[0].onclick = eval(param.onclick);
			}
		}
		if (param.handler){item[0].onclick = eval(param.handler)}
		if (param.disabled){setDisabled(target, item[0], true)}
		
		bindMenuItemEvent(target, item);
		bindMenuEvent(target, menu);
		setMenuSize(target, menu);
	}
	
	function removeItem(target, itemEl){
		function removeit(el){
			if (el.submenu){
				el.submenu.children('div.menu-item').each(function(){
					removeit(this);
				});
				var shadow = el.submenu[0].shadow;
				if (shadow) shadow.remove();
				el.submenu.remove();
			}
			$(el).remove();
		}
		var menu = $(itemEl).parent();
		removeit(itemEl);
		setMenuSize(target, menu);
	}
	
	function setVisible(target, itemEl, visible){
		var menu = $(itemEl).parent();
		if (visible){
			$(itemEl).show();
		} else {
			$(itemEl).hide();
		}
		setMenuSize(target, menu);
	}
	
	function destroyMenu(target){
		$(target).children('div.menu-item').each(function(){
			removeItem(target, this);
		});
		if (target.shadow) target.shadow.remove();
		$(target).remove();
	}
	
	$.fn.menu = function(options, param){
		if (typeof options == 'string'){
			return $.fn.menu.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'menu');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'menu', {
					options: $.extend({}, $.fn.menu.defaults, $.fn.menu.parseOptions(this), options)
				});
				init(this);
			}
			$(this).css({
				left: state.options.left,
				top: state.options.top
			});
		});
	};
	
	$.fn.menu.methods = {
		options: function(jq){
			return $.data(jq[0], 'menu').options;
		},
		show: function(jq, pos){
			return jq.each(function(){
				showMenu(this, pos);
			});
		},
		hide: function(jq){
			return jq.each(function(){
				hideAll(this);
			});
		},
		destroy: function(jq){
			return jq.each(function(){
				destroyMenu(this);
			});
		},
		/**
		 * set the menu item text
		 * param: {
		 * 	target: DOM object, indicate the menu item
		 * 	text: string, the new text
		 * }
		 */
		setText: function(jq, param){
			return jq.each(function(){
				$(param.target).children('div.menu-text').html(param.text);
			});
		},
		/**
		 * set the menu icon class
		 * param: {
		 * 	target: DOM object, indicate the menu item
		 * 	iconCls: the menu item icon class
		 * }
		 */
		setIcon: function(jq, param){
			return jq.each(function(){
				$(param.target).children('div.menu-icon').remove();
				if (param.iconCls){
					$('<div class="menu-icon"></div>').addClass(param.iconCls).appendTo(param.target);
				}
			});
		},
		/**
		 * get the menu item data that contains the following property:
		 * {
		 * 	target: DOM object, the menu item
		 *  id: the menu id
		 * 	text: the menu item text
		 * 	iconCls: the icon class
		 *  href: a remote address to redirect to
		 *  onclick: a function to be called when the item is clicked
		 * }
		 */
		getItem: function(jq, itemEl){
			var t = $(itemEl);
			var item = {
				target: itemEl,
				id: t.attr('id'),
				text: $.trim(t.children('div.menu-text').html()),
				disabled: t.hasClass('menu-item-disabled'),
//				href: t.attr('href'),
//				name: t.attr('name'),
				name: itemEl.itemName,
				href: itemEl.itemHref,
				onclick: itemEl.onclick
			}
			var icon = t.children('div.menu-icon');
			if (icon.length){
				var cc = [];
				var aa = icon.attr('class').split(' ');
				for(var i=0; i<aa.length; i++){
					if (aa[i] != 'menu-icon'){
						cc.push(aa[i]);
					}
				}
				item.iconCls = cc.join(' ');
			}
			return item;
		},
		findItem: function(jq, text){
			return findItem(jq[0], text);
		},
		/**
		 * append menu item, the param contains following properties:
		 * parent,id,text,iconCls,href,onclick
		 * when parent property is assigned, append menu item to it
		 */
		appendItem: function(jq, param){
			return jq.each(function(){
				appendItem(this, param);
			});
		},
		removeItem: function(jq, itemEl){
			return jq.each(function(){
				removeItem(this, itemEl);
			});
		},
		enableItem: function(jq, itemEl){
			return jq.each(function(){
				setDisabled(this, itemEl, false);
			});
		},
		disableItem: function(jq, itemEl){
			return jq.each(function(){
				setDisabled(this, itemEl, true);
			});
		},
		showItem: function(jq, itemEl){
			return jq.each(function(){
				setVisible(this, itemEl, true);
			});
		},
		hideItem: function(jq, itemEl){
			return jq.each(function(){
				setVisible(this, itemEl, false);
			});
		},
		resize: function(jq, menuEl){
			return jq.each(function(){
				setMenuSize(this, $(menuEl));
			});
		}
	};
	
	$.fn.menu.parseOptions = function(target){
		return $.extend({}, $.parser.parseOptions(target, [{minWidth:'number',duration:'number',hideOnUnhover:'boolean'}]));
	};
	
	$.fn.menu.defaults = {
		zIndex:110000,
		left: 0,
		top: 0,
		alignTo: null,
		align: 'left',
		minWidth: 120,
		duration: 100,	// Defines duration time in milliseconds to hide when the mouse leaves the menu.
		hideOnUnhover: true,	// Automatically hides the menu when mouse exits it
		onShow: function(){},
		onHide: function(){},
		onClick: function(item){}
	};
})(jQuery);

(function($) {
	function init(_407) {
		var opts = $.data(_407, "menubutton").options;
		var btn = $(_407);
		btn.linkbutton(opts);
		btn.removeClass(opts.cls.btn1 + " " + opts.cls.btn2).addClass("m-btn");
		btn.removeClass("m-btn-small m-btn-medium m-btn-large").addClass(
				"m-btn-" + opts.size);
		var _408 = btn.find(".l-btn-left");
		$("<span></span>").addClass(opts.cls.arrow).appendTo(_408);
		$("<span></span>").addClass("m-btn-line").appendTo(_408);
		if (opts.menu) {
			$(opts.menu).menu({
				duration : opts.duration
			});
			var _409 = $(opts.menu).menu("options");
			var _40a = _409.onShow;
			var _40b = _409.onHide;
			$.extend(_409, {
				onShow : function() {
					var _40c = $(this).menu("options");
					var btn = $(_40c.alignTo);
					var opts = btn.menubutton("options");
					btn.addClass((opts.plain == true) ? opts.cls.btn2
							: opts.cls.btn1);
					_40a.call(this);
				},
				onHide : function() {
					var _40d = $(this).menu("options");
					var btn = $(_40d.alignTo);
					var opts = btn.menubutton("options");
					btn.removeClass((opts.plain == true) ? opts.cls.btn2
							: opts.cls.btn1);
					_40b.call(this);
				}
			});
		}
	}
	;
	function _40e(_40f) {
		var opts = $.data(_40f, "menubutton").options;
		var btn = $(_40f);
		var t = btn.find("." + opts.cls.trigger);
		if (!t.length) {
			t = btn;
		}
		t.unbind(".menubutton");
		var _410 = null;
		t.bind("click.menubutton", function() {
			if (!_411()) {
				_412(_40f);
				return false;
			}
		}).bind("mouseenter.menubutton", function() {
			if (!_411()) {
				_410 = setTimeout(function() {
					_412(_40f);
				}, opts.duration);
				return false;
			}
		}).bind("mouseleave.menubutton", function() {
			if (_410) {
				clearTimeout(_410);
			}
			$(opts.menu).triggerHandler("mouseleave");
		});
		function _411() {
			return $(_40f).linkbutton("options").disabled;
		}
		;
	}
	;
	function _412(_413) {
		var opts = $(_413).menubutton("options");
		if (opts.disabled || !opts.menu) {
			return;
		}
		$("body>div.menu-top").menu("hide");
		var btn = $(_413);
		var mm = $(opts.menu);
		if (mm.length) {
			mm.menu("options").alignTo = btn;
			mm.menu("show", {
				alignTo : btn,
				align : opts.menuAlign
			});
		}
		btn.blur();
	}
	;
	$.fn.menubutton = function(_414, _415) {
		if (typeof _414 == "string") {
			var _416 = $.fn.menubutton.methods[_414];
			if (_416) {
				return _416(this, _415);
			} else {
				return this.linkbutton(_414, _415);
			}
		}
		_414 = _414 || {};
		return this.each(function() {
			var _417 = $.data(this, "menubutton");
			if (_417) {
				$.extend(_417.options, _414);
			} else {
				$.data(this, "menubutton", {
					options : $.extend({}, $.fn.menubutton.defaults,
							$.fn.menubutton.parseOptions(this), _414)
				});
				$(this).removeAttr("disabled");
			}
			init(this);
			_40e(this);
		});
	};
	$.fn.menubutton.methods = {
		options : function(jq) {
			var _418 = jq.linkbutton("options");
			return $.extend($.data(jq[0], "menubutton").options, {
				toggle : _418.toggle,
				selected : _418.selected,
				disabled : _418.disabled
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var opts = $(this).menubutton("options");
				if (opts.menu) {
					$(opts.menu).menu("destroy");
				}
				$(this).remove();
			});
		}
	};
	$.fn.menubutton.parseOptions = function(_419) {
		var t = $(_419);
		return $.extend({}, $.fn.linkbutton.parseOptions(_419), $.parser
				.parseOptions(_419, [ "menu", {
					plain : "boolean",
					duration : "number"
				} ]));
	};
	$.fn.menubutton.defaults = $.extend({}, $.fn.linkbutton.defaults, {
		plain : true,
		menu : null,
		menuAlign : "left",
		duration : 100,
		cls : {
			btn1 : "m-btn-active",
			btn2 : "m-btn-plain-active",
			arrow : "m-btn-downarrow",
			trigger : "m-btn"
		}
	});
})(jQuery);
(function($) {
	function init(_41a) {
		var opts = $.data(_41a, "splitbutton").options;
		$(_41a).menubutton(opts);
		$(_41a).addClass("s-btn");
	}
	;
	$.fn.splitbutton = function(_41b, _41c) {
		if (typeof _41b == "string") {
			var _41d = $.fn.splitbutton.methods[_41b];
			if (_41d) {
				return _41d(this, _41c);
			} else {
				return this.menubutton(_41b, _41c);
			}
		}
		_41b = _41b || {};
		return this.each(function() {
			var _41e = $.data(this, "splitbutton");
			if (_41e) {
				$.extend(_41e.options, _41b);
			} else {
				$.data(this, "splitbutton", {
					options : $.extend({}, $.fn.splitbutton.defaults,
							$.fn.splitbutton.parseOptions(this), _41b)
				});
				$(this).removeAttr("disabled");
			}
			init(this);
		});
	};
	$.fn.splitbutton.methods = {
		options : function(jq) {
			var _41f = jq.menubutton("options");
			var _420 = $.data(jq[0], "splitbutton").options;
			$.extend(_420, {
				disabled : _41f.disabled,
				toggle : _41f.toggle,
				selected : _41f.selected
			});
			return _420;
		}
	};
	$.fn.splitbutton.parseOptions = function(_421) {
		var t = $(_421);
		return $.extend({}, $.fn.linkbutton.parseOptions(_421), $.parser
				.parseOptions(_421, [ "menu", {
					plain : "boolean",
					duration : "number"
				} ]));
	};
	$.fn.splitbutton.defaults = $.extend({}, $.fn.linkbutton.defaults, {
		plain : true,
		menu : null,
		duration : 100,
		cls : {
			btn1 : "m-btn-active s-btn-active",
			btn2 : "m-btn-plain-active s-btn-plain-active",
			arrow : "m-btn-downarrow",
			trigger : "m-btn-line"
		}
	});
})(jQuery);
(function($) {
	function init(_422) {
		$(_422).addClass("validatebox-text");
	}
	;
	function _423(_424) {
		var _425 = $.data(_424, "validatebox");
		_425.validating = false;
		if (_425.timer) {
			clearTimeout(_425.timer);
		}
		$(_424).tooltip("destroy");
		$(_424).unbind();
		$(_424).remove();
	}
	;
	function _426(_427) {
		var opts = $.data(_427, "validatebox").options;
		var box = $(_427);
		box.unbind(".validatebox");
		if (opts.novalidate || box.is(":disabled")) {
			return;
		}
		for ( var _428 in opts.events) {
			$(_427).bind(_428 + ".validatebox", {
				target : _427
			}, opts.events[_428]);
		}
	}
	;
	function _429(e) {
		var _42a = e.data.target;
		var _42b = $.data(_42a, "validatebox");
		var box = $(_42a);
		if ($(_42a).attr("readonly")) {
			return;
		}
		_42b.validating = true;
		_42b.value = undefined;
		(function() {
			if (_42b.validating) {
				if (_42b.value != box.val()) {
					_42b.value = box.val();
					if (_42b.timer) {
						clearTimeout(_42b.timer);
					}
					_42b.timer = setTimeout(function() {
						$(_42a).validatebox("validate");
					}, _42b.options.delay);
				} else {
					_42c(_42a);
				}
				setTimeout(arguments.callee, 200);
			}
		})();
	}
	;
	function _42d(e) {
		var _42e = e.data.target;
		var _42f = $.data(_42e, "validatebox");
		if (_42f.timer) {
			clearTimeout(_42f.timer);
			_42f.timer = undefined;
		}
		_42f.validating = false;
		_430(_42e);
	}
	;
	function _431(e) {
		var _432 = e.data.target;
		if ($(_432).hasClass("validatebox-invalid")) {
			_433(_432);
		}
	}
	;
	function _434(e) {
		var _435 = e.data.target;
		var _436 = $.data(_435, "validatebox");
		if (!_436.validating) {
			_430(_435);
		}
	}
	;
	function _433(_437) {
		var _438 = $.data(_437, "validatebox");
		var opts = _438.options;
		$(_437).tooltip($.extend({}, opts.tipOptions, {
			content : _438.message,
			position : opts.tipPosition,
			deltaX : opts.deltaX
		})).tooltip("show");
		_438.tip = true;
	}
	;
	function _42c(_439) {
		var _43a = $.data(_439, "validatebox");
		if (_43a && _43a.tip) {
			$(_439).tooltip("reposition");
		}
	}
	;
	function _430(_43b) {
		var _43c = $.data(_43b, "validatebox");
		_43c.tip = false;
		$(_43b).tooltip("hide");
	}
	;
	function _43d(_43e) {
		var _43f = $.data(_43e, "validatebox");
		var opts = _43f.options;
		var box = $(_43e);
		opts.onBeforeValidate.call(_43e);
		var _440 = _441();
		opts.onValidate.call(_43e, _440);
		return _440;
		function _442(msg) {
			_43f.message = msg;
		}
		;
		function _443(_444, _445) {
			var _446 = box.val();
			var _447 = /([a-zA-Z_]+)(.*)/.exec(_444);
			var rule = opts.rules[_447[1]];
			if (rule && _446) {
				var _448 = _445 || opts.validParams || eval(_447[2]);
				if (!rule["validator"].call(_43e, _446, _448)) {
					box.addClass("validatebox-invalid");
					var _449 = rule["message"];
					if (_448) {
						for ( var i = 0; i < _448.length; i++) {
							_449 = _449.replace(new RegExp("\\{" + i + "\\}",
									"g"), _448[i]);
						}
					}
					_442(opts.invalidMessage || _449);
					if (_43f.validating) {
						_433(_43e);
					}
					return false;
				}
			}
			return true;
		}
		;
		function _441() {
			box.removeClass("validatebox-invalid");
			_430(_43e);
			if (opts.novalidate || box.is(":disabled")) {
				return true;
			}
			if (opts.required) {
				if (box.val() == "") {
					box.addClass("validatebox-invalid");
					_442(opts.missingMessage);
					if (_43f.validating) {
						_433(_43e);
					}
					return false;
				}
			}
			if (opts.validType) {
				if ($.isArray(opts.validType)) {
					for ( var i = 0; i < opts.validType.length; i++) {
						if (!_443(opts.validType[i])) {
							return false;
						}
					}
				} else {
					if (typeof opts.validType == "string") {
						if (!_443(opts.validType)) {
							return false;
						}
					} else {
						for ( var _44a in opts.validType) {
							var _44b = opts.validType[_44a];
							if (!_443(_44a, _44b)) {
								return false;
							}
						}
					}
				}
			}
			return true;
		}
		;
	}
	;
	function _44c(_44d, _44e) {
		var opts = $.data(_44d, "validatebox").options;
		if (_44e != undefined) {
			opts.novalidate = _44e;
		}
		if (opts.novalidate) {
			$(_44d).removeClass("validatebox-invalid");
			_430(_44d);
		}
		_43d(_44d);
		_426(_44d);
	}
	;
	$.fn.validatebox = function(_44f, _450) {
		if (typeof _44f == "string") {
			return $.fn.validatebox.methods[_44f](this, _450);
		}
		_44f = _44f || {};
		return this.each(function() {
			var _451 = $.data(this, "validatebox");
			if (_451) {
				$.extend(_451.options, _44f);
			} else {
				init(this);
				$.data(this, "validatebox", {
					options : $.extend({}, $.fn.validatebox.defaults,
							$.fn.validatebox.parseOptions(this), _44f)
				});
			}
			_44c(this);
			_43d(this);
		});
	};
	$.fn.validatebox.methods = {
		options : function(jq) {
			return $.data(jq[0], "validatebox").options;
		},
		destroy : function(jq) {
			return jq.each(function() {
				_423(this);
			});
		},
		validate : function(jq) {
			return jq.each(function() {
				_43d(this);
			});
		},
		isValid : function(jq) {
			return _43d(jq[0]);
		},
		enableValidation : function(jq) {
			return jq.each(function() {
				_44c(this, false);
			});
		},
		disableValidation : function(jq) {
			return jq.each(function() {
				_44c(this, true);
			});
		}
	};
	$.fn.validatebox.parseOptions = function(_452) {
		var t = $(_452);
		return $.extend({}, $.parser.parseOptions(_452, [ "validType",
				"missingMessage", "invalidMessage", "tipPosition", {
					delay : "number",
					deltaX : "number"
				} ]), {
			required : (t.attr("required") ? true : undefined),
			novalidate : (t.attr("novalidate") != undefined ? true : undefined)
		});
	};
	$.fn.validatebox.defaults = {
		required : false,
		validType : null,
		validParams : null,
		delay : 200,
		missingMessage : "This field is required.",
		invalidMessage : null,
		tipPosition : "right",
		deltaX : 0,
		novalidate : false,
		events : {
			focus : _429,
			blur : _42d,
			mouseenter : _431,
			mouseleave : _434,
			click : function(e) {
				var t = $(e.data.target);
				if (!t.is(":focus")) {
					t.trigger("focus");
				}
			}
		},
		tipOptions : {
			showEvent : "none",
			hideEvent : "none",
			showDelay : 0,
			hideDelay : 0,
			zIndex : "",
			onShow : function() {
				$(this).tooltip("tip").css({
					color : "#000",
					borderColor : "#CC9933",
					backgroundColor : "#FFFFCC"
				});
			},
			onHide : function() {
				$(this).tooltip("destroy");
			}
		},
		rules : {
			email : {
				validator : function(_453) {
					return /^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i
							.test(_453);
				},
				message : "Please enter a valid email address."
			},
			url : {
				validator : function(_454) {
					return /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(\#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
							.test(_454);
				},
				message : "Please enter a valid URL."
			},
			length : {
				validator : function(_455, _456) {
					var len = $.trim(_455).length;
					return len >= _456[0] && len <= _456[1];
				},
				message : "Please enter a value between {0} and {1}."
			},
			remote : {
				validator : function(_457, _458) {
					var data = {};
					data[_458[1]] = _457;
					var _459 = $.ajax({
						url : _458[0],
						dataType : "json",
						data : data,
						async : false,
						cache : false,
						type : "post"
					}).responseText;
					return _459 == "true";
				},
				message : "Please fix this field."
			}
		},
		onBeforeValidate : function() {
		},
		onValidate : function(_45a) {
		}
	};
})(jQuery);
(function($) {
	function init(_45b) {
		$(_45b).addClass("textbox-f").hide();
		var span = $(
				"<span class=\"textbox\">"
						+ "<input class=\"textbox-text\" autocomplete=\"off\">"
						+ "<input type=\"hidden\" class=\"textbox-value\">"
						+ "</span>").insertAfter(_45b);
		var name = $(_45b).attr("name");
		if (name) {
			span.find("input.textbox-value").attr("name", name);
			$(_45b).removeAttr("name").attr("textboxName", name);
		}
		return span;
	}
	;
	function _45c(_45d) {
		var _45e = $.data(_45d, "textbox");
		var opts = _45e.options;
		var tb = _45e.textbox;
		tb.find(".textbox-text").remove();
		if (opts.multiline) {
			$(
					"<textarea class=\"textbox-text\" autocomplete=\"off\"></textarea>")
					.prependTo(tb);
		} else {
			$(
					"<input type=\"" + opts.type
							+ "\" class=\"textbox-text\" autocomplete=\"off\">")
					.prependTo(tb);
		}
		tb.find(".textbox-addon").remove();
		var bb = opts.icons ? $.extend(true, [], opts.icons) : [];
		if (opts.iconCls) {
			bb.push({
				iconCls : opts.iconCls,
				disabled : true
			});
		}
		if (bb.length) {
			var bc = $("<span class=\"textbox-addon\"></span>").prependTo(tb);
			bc.addClass("textbox-addon-" + opts.iconAlign);
			for ( var i = 0; i < bb.length; i++) {
				bc
						.append("<a href=\"javascript:void(0)\" class=\"textbox-icon "
								+ bb[i].iconCls
								+ "\" icon-index=\""
								+ i
								+ "\" tabindex=\"-1\"></a>");
			}
		}
		tb.find(".textbox-button").remove();
		if (opts.buttonText || opts.buttonIcon) {
			var btn = $(
					"<a href=\"javascript:void(0)\" class=\"textbox-button\"></a>")
					.prependTo(tb);
			btn.addClass("textbox-button-" + opts.buttonAlign).linkbutton({
				text : opts.buttonText,
				iconCls : opts.buttonIcon
			});
		}
		_45f(_45d, opts.disabled);
		_460(_45d, opts.readonly);
	}
	;
	function _461(_462) {
		var tb = $.data(_462, "textbox").textbox;
		tb.find(".textbox-text").validatebox("destroy");
		tb.remove();
		$(_462).remove();
	}
	;
	function _463(_464, _465) {
		var _466 = $.data(_464, "textbox");
		var opts = _466.options;
		var tb = _466.textbox;
		var _467 = tb.parent();
		if (_465) {
			opts.width = _465;
		}
		if (isNaN(parseInt(opts.width))) {
			var c = $(_464).clone();
			c.css("visibility", "hidden");
			c.insertAfter(_464);
			opts.width = c.outerWidth();
			c.remove();
		}
		tb.appendTo("body");
		var _468 = tb.find(".textbox-text");
		var btn = tb.find(".textbox-button");
		var _469 = tb.find(".textbox-addon");
		var _46a = _469.find(".textbox-icon");
		tb._size(opts, _467);
		btn.linkbutton("resize", {
			height : tb.height()
		});
		btn.css({
			left : (opts.buttonAlign == "left" ? 0 : ""),
			right : (opts.buttonAlign == "right" ? 0 : "")
		});
		_469
				.css({
					left : (opts.iconAlign == "left" ? (opts.buttonAlign == "left" ? btn
							._outerWidth()
							: 0)
							: ""),
					right : (opts.iconAlign == "right" ? (opts.buttonAlign == "right" ? btn
							._outerWidth()
							: 0)
							: "")
				});
		_46a.css({
			width : opts.iconWidth + "px",
			height : tb.height() + "px"
		});
		_468.css({
			paddingLeft : (_464.style.paddingLeft || ""),
			paddingRight : (_464.style.paddingRight || ""),
			marginLeft : _46b("left"),
			marginRight : _46b("right")
		});
		if (opts.multiline) {
			_468.css({
				paddingTop : (_464.style.paddingTop || ""),
				paddingBottom : (_464.style.paddingBottom || "")
			});
			_468._outerHeight(tb.height());
		} else {
			var _46c = Math.floor((tb.height() - _468.height()) / 2);
			_468.css({
				paddingTop : _46c + "px",
				paddingBottom : _46c + "px"
			});
		}
		_468._outerWidth(tb.width() - _46a.length * opts.iconWidth
				- btn._outerWidth());
		tb.insertAfter(_464);
		opts.onResize.call(_464, opts.width, opts.height);
		function _46b(_46d) {
			return (opts.iconAlign == _46d ? _469._outerWidth() : 0)
					+ (opts.buttonAlign == _46d ? btn._outerWidth() : 0);
		}
		;
	}
	;
	function _46e(_46f) {
		var opts = $(_46f).textbox("options");
		var _470 = $(_46f).textbox("textbox");
		_470.validatebox($.extend({}, opts, {
			deltaX : $(_46f).textbox("getTipX"),
			onBeforeValidate : function() {
				var box = $(this);
				if (!box.is(":focus")) {
					opts.oldInputValue = box.val();
					box.val(opts.value);
				}
			},
			onValidate : function(_471) {
				var box = $(this);
				if (opts.oldInputValue != undefined) {
					box.val(opts.oldInputValue);
					opts.oldInputValue = undefined;
				}
				var tb = box.parent();
				if (_471) {
					tb.removeClass("textbox-invalid");
				} else {
					tb.addClass("textbox-invalid");
				}
			}
		}));
	}
	;
	function _472(_473) {
		var _474 = $.data(_473, "textbox");
		var opts = _474.options;
		var tb = _474.textbox;
		var _475 = tb.find(".textbox-text");
		_475.attr("placeholder", opts.prompt);
		_475.unbind(".textbox");
		if (!opts.disabled && !opts.readonly) {
			_475.bind("blur.textbox", function(e) {
				if (!tb.hasClass("textbox-focused")) {
					return;
				}
				opts.value = $(this).val();
				if (opts.value == "") {
					$(this).val(opts.prompt).addClass("textbox-prompt");
				} else {
					$(this).removeClass("textbox-prompt");
				}
				tb.removeClass("textbox-focused");
			}).bind("focus.textbox", function(e) {
				if (tb.hasClass("textbox-focused")) {
					return;
				}
				if ($(this).val() != opts.value) {
					$(this).val(opts.value);
				}
				$(this).removeClass("textbox-prompt");
				tb.addClass("textbox-focused");
			});
			for ( var _476 in opts.inputEvents) {
				_475.bind(_476 + ".textbox", {
					target : _473
				}, opts.inputEvents[_476]);
			}
		}
		var _477 = tb.find(".textbox-addon");
		_477.unbind().bind(
				"click",
				{
					target : _473
				},
				function(e) {
					var icon = $(e.target).closest(
							"a.textbox-icon:not(.textbox-icon-disabled)");
					if (icon.length) {
						var _478 = parseInt(icon.attr("icon-index"));
						var conf = opts.icons[_478];
						if (conf && conf.handler) {
							conf.handler.call(icon[0], e);
							opts.onClickIcon.call(_473, _478);
						}
					}
				});
		_477.find(".textbox-icon").each(function(_479) {
			var conf = opts.icons[_479];
			var icon = $(this);
			if (!conf || conf.disabled || opts.disabled || opts.readonly) {
				icon.addClass("textbox-icon-disabled");
			} else {
				icon.removeClass("textbox-icon-disabled");
			}
		});
		var btn = tb.find(".textbox-button");
		btn.unbind(".textbox").bind("click.textbox", function() {
			if (!btn.linkbutton("options").disabled) {
				opts.onClickButton.call(_473);
			}
		});
		btn.linkbutton((opts.disabled || opts.readonly) ? "disable" : "enable");
		tb.unbind(".textbox").bind("_resize.textbox", function(e, _47a) {
			if ($(this).hasClass("easyui-fluid") || _47a) {
				_463(_473);
			}
			return false;
		});
	}
	;
	function _45f(_47b, _47c) {
		var _47d = $.data(_47b, "textbox");
		var opts = _47d.options;
		var tb = _47d.textbox;
		if (_47c) {
			opts.disabled = true;
			$(_47b).attr("disabled", "disabled");
			tb.find(".textbox-text,.textbox-value")
					.attr("disabled", "disabled");
		} else {
			opts.disabled = false;
			$(_47b).removeAttr("disabled");
			tb.find(".textbox-text,.textbox-value").removeAttr("disabled");
		}
	}
	;
	function _460(_47e, mode) {
		var _47f = $.data(_47e, "textbox");
		var opts = _47f.options;
		opts.readonly = mode == undefined ? true : mode;
		var _480 = _47f.textbox.find(".textbox-text");
		_480.removeAttr("readonly").removeClass("textbox-text-readonly");
		if (opts.readonly || !opts.editable) {
			_480.attr("readonly", "readonly").addClass("textbox-text-readonly");
		}
	}
	;
	$.fn.textbox = function(_481, _482) {
		if (typeof _481 == "string") {
			var _483 = $.fn.textbox.methods[_481];
			if (_483) {
				return _483(this, _482);
			} else {
				return this.each(function() {
					var _484 = $(this).textbox("textbox");
					_484.validatebox(_481, _482);
				});
			}
		}
		_481 = _481 || {};
		return this.each(function() {
			var _485 = $.data(this, "textbox");
			if (_485) {
				$.extend(_485.options, _481);
				if (_481.value != undefined) {
					_485.options.originalValue = _481.value;
				}
			} else {
				_485 = $.data(this, "textbox", {
					options : $.extend({}, $.fn.textbox.defaults, $.fn.textbox
							.parseOptions(this), _481),
					textbox : init(this)
				});
				_485.options.originalValue = _485.options.value;
			}
			_45c(this);
			_472(this);
			_463(this);
			_46e(this);
			$(this).textbox("initValue", _485.options.value);
		});
	};
	$.fn.textbox.methods = {
		options : function(jq) {
			return $.data(jq[0], "textbox").options;
		},
		cloneFrom : function(jq, from) {
			return jq.each(function() {
				var t = $(this);
				if (t.data("textbox")) {
					return;
				}
				if (!$(from).data("textbox")) {
					$(from).textbox();
				}
				var name = t.attr("name") || "";
				t.addClass("textbox-f").hide();
				t.removeAttr("name").attr("textboxName", name);
				var span = $(from).next().clone().insertAfter(t);
				span.find("input.textbox-value").attr("name", name);
				$.data(this, "textbox", {
					options : $.extend(true, {}, $(from).textbox("options")),
					textbox : span
				});
				var _486 = $(from).textbox("button");
				if (_486.length) {
					t.textbox("button").linkbutton(
							$.extend(true, {}, _486.linkbutton("options")));
				}
				_472(this);
				_46e(this);
			});
		},
		textbox : function(jq) {
			return $.data(jq[0], "textbox").textbox.find(".textbox-text");
		},
		button : function(jq) {
			return $.data(jq[0], "textbox").textbox.find(".textbox-button");
		},
		destroy : function(jq) {
			return jq.each(function() {
				_461(this);
			});
		},
		resize : function(jq, _487) {
			return jq.each(function() {
				_463(this, _487);
			});
		},
		disable : function(jq) {
			return jq.each(function() {
				_45f(this, true);
				_472(this);
			});
		},
		enable : function(jq) {
			return jq.each(function() {
				_45f(this, false);
				_472(this);
			});
		},
		readonly : function(jq, mode) {
			return jq.each(function() {
				_460(this, mode);
				_472(this);
			});
		},
		isValid : function(jq) {
			return jq.textbox("textbox").validatebox("isValid");
		},
		clear : function(jq) {
			return jq.each(function() {
				$(this).textbox("setValue", "");
			});
		},
		setText : function(jq, _488) {
			return jq.each(function() {
				var opts = $(this).textbox("options");
				var _489 = $(this).textbox("textbox");
				if ($(this).textbox("getText") != _488) {
					opts.value = _488;
					_489.val(_488);
				}
				if (!_489.is(":focus")) {
					if (_488) {
						_489.removeClass("textbox-prompt");
					} else {
						_489.val(opts.prompt).addClass("textbox-prompt");
					}
				}
				$(this).textbox("validate");
			});
		},
		initValue : function(jq, _48a) {
			return jq.each(function() {
				var _48b = $.data(this, "textbox");
				_48b.options.value = "";
				$(this).textbox("setText", _48a);
				_48b.textbox.find(".textbox-value").val(_48a);
				$(this).val(_48a);
			});
		},
		setValue : function(jq, _48c) {
			return jq.each(function() {
				var opts = $.data(this, "textbox").options;
				var _48d = $(this).textbox("getValue");
				$(this).textbox("initValue", _48c);
				if (_48d != _48c) {
					opts.onChange.call(this, _48c, _48d);
				}
			});
		},
		getText : function(jq) {
			var _48e = jq.textbox("textbox");
			if (_48e.is(":focus")) {
				return _48e.val();
			} else {
				return jq.textbox("options").value;
			}
		},
		getValue : function(jq) {
			return jq.data("textbox").textbox.find(".textbox-value").val();
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).textbox("options");
				$(this).textbox("setValue", opts.originalValue);
			});
		},
		getIcon : function(jq, _48f) {
			return jq.data("textbox").textbox.find(".textbox-icon:eq(" + _48f
					+ ")");
		},
		getTipX : function(jq) {
			var _490 = jq.data("textbox");
			var opts = _490.options;
			var tb = _490.textbox;
			var _491 = tb.find(".textbox-text");
			var _492 = tb.find(".textbox-addon")._outerWidth();
			var _493 = tb.find(".textbox-button")._outerWidth();
			if (opts.tipPosition == "right") {
				return (opts.iconAlign == "right" ? _492 : 0)
						+ (opts.buttonAlign == "right" ? _493 : 0) + 1;
			} else {
				if (opts.tipPosition == "left") {
					return (opts.iconAlign == "left" ? -_492 : 0)
							+ (opts.buttonAlign == "left" ? -_493 : 0) - 1;
				} else {
					return _492 / 2 * (opts.iconAlign == "right" ? 1 : -1);
				}
			}
		}
	};
	$.fn.textbox.parseOptions = function(_494) {
		var t = $(_494);
		return $.extend({}, $.fn.validatebox.parseOptions(_494), $.parser
				.parseOptions(_494, [ "prompt", "iconCls", "iconAlign",
						"buttonText", "buttonIcon", "buttonAlign", {
							multiline : "boolean",
							editable : "boolean",
							iconWidth : "number"
						} ]), {
			value : (t.val() || undefined),
			type : (t.attr("type") ? t.attr("type") : undefined),
			disabled : (t.attr("disabled") ? true : undefined),
			readonly : (t.attr("readonly") ? true : undefined)
		});
	};
	$.fn.textbox.defaults = $.extend({}, $.fn.validatebox.defaults, {
		width : "auto",
		height : 22,
		prompt : "",
		value : "",
		type : "text",
		multiline : false,
		editable : true,
		disabled : false,
		readonly : false,
		icons : [],
		iconCls : null,
		iconAlign : "right",
		iconWidth : 18,
		buttonText : "",
		buttonIcon : null,
		buttonAlign : "right",
		inputEvents : {
			blur : function(e) {
				var t = $(e.data.target);
				var opts = t.textbox("options");
				t.textbox("setValue", opts.value);
			},
			keydown : function(e) {
				if (e.keyCode == 13) {
					var t = $(e.data.target);
					t.textbox("setValue", t.textbox("getText"));
				}
			}
		},
		onChange : function(_495, _496) {
		},
		onResize : function(_497, _498) {
		},
		onClickButton : function() {
		},
		onClickIcon : function(_499) {
		}
	});
})(jQuery);
(function($) {
	var _49a = 0;
	function _49b(_49c) {
		var _49d = $.data(_49c, "filebox");
		var opts = _49d.options;
		var id = "filebox_file_id_" + (++_49a);
		$(_49c).addClass("filebox-f").textbox(
				$.extend({}, opts, {
					buttonText : opts.buttonText ? ("<label for=\"" + id
							+ "\">" + opts.buttonText + "</label>") : ""
				}));
		$(_49c).textbox("textbox").attr("readonly", "readonly");
		_49d.filebox = $(_49c).next().addClass("filebox");
		_49d.filebox.find(".textbox-value").remove();
		opts.oldValue = "";
		var file = $("<input type=\"file\" class=\"textbox-value\">").appendTo(
				_49d.filebox);
		file.attr("id", id).attr("name", $(_49c).attr("textboxName") || "");
		file.change(function() {
			$(_49c).filebox("setText", this.value);
			opts.onChange.call(_49c, this.value, opts.oldValue);
			opts.oldValue = this.value;
		});
		var btn = $(_49c).filebox("button");
		if (btn.length) {
			if (btn.linkbutton("options").disabled) {
				file.attr("disabled", "disabled");
			} else {
				file.removeAttr("disabled");
			}
		}
	}
	;
	$.fn.filebox = function(_49e, _49f) {
		if (typeof _49e == "string") {
			var _4a0 = $.fn.filebox.methods[_49e];
			if (_4a0) {
				return _4a0(this, _49f);
			} else {
				return this.textbox(_49e, _49f);
			}
		}
		_49e = _49e || {};
		return this.each(function() {
			var _4a1 = $.data(this, "filebox");
			if (_4a1) {
				$.extend(_4a1.options, _49e);
			} else {
				$.data(this, "filebox", {
					options : $.extend({}, $.fn.filebox.defaults, $.fn.filebox
							.parseOptions(this), _49e)
				});
			}
			_49b(this);
		});
	};
	$.fn.filebox.methods = {
		options : function(jq) {
			var opts = jq.textbox("options");
			return $.extend($.data(jq[0], "filebox").options, {
				width : opts.width,
				value : opts.value,
				originalValue : opts.originalValue,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		}
	};
	$.fn.filebox.parseOptions = function(_4a2) {
		return $.extend({}, $.fn.textbox.parseOptions(_4a2), {});
	};
	$.fn.filebox.defaults = $.extend({}, $.fn.textbox.defaults, {
		buttonIcon : null,
		buttonText : "Choose File",
		buttonAlign : "right",
		inputEvents : {}
	});
})(jQuery);
(function($) {
	function _4a3(_4a4) {
		var _4a5 = $.data(_4a4, "searchbox");
		var opts = _4a5.options;
		var _4a6 = $.extend(true, [], opts.icons);
		_4a6.push({
			iconCls : "searchbox-button",
			handler : function(e) {
				var t = $(e.data.target);
				var opts = t.searchbox("options");
				opts.searcher.call(e.data.target, t.searchbox("getValue"), t
						.searchbox("getName"));
			}
		});
		_4a7();
		var _4a8 = _4a9();
		$(_4a4).addClass("searchbox-f").textbox($.extend({}, opts, {
			icons : _4a6,
			buttonText : (_4a8 ? _4a8.text : "")
		}));
		$(_4a4).attr("searchboxName", $(_4a4).attr("textboxName"));
		_4a5.searchbox = $(_4a4).next();
		_4a5.searchbox.addClass("searchbox");
		_4aa(_4a8);
		function _4a7() {
			if (opts.menu) {
				_4a5.menu = $(opts.menu).menu();
				var _4ab = _4a5.menu.menu("options");
				var _4ac = _4ab.onClick;
				_4ab.onClick = function(item) {
					_4aa(item);
					_4ac.call(this, item);
				};
			} else {
				if (_4a5.menu) {
					_4a5.menu.menu("destroy");
				}
				_4a5.menu = null;
			}
		}
		;
		function _4a9() {
			if (_4a5.menu) {
				var item = _4a5.menu.children("div.menu-item:first");
				_4a5.menu.children("div.menu-item").each(
						function() {
							var _4ad = $
									.extend({}, $.parser.parseOptions(this),
											{
												selected : ($(this).attr(
														"selected") ? true
														: undefined)
											});
							if (_4ad.selected) {
								item = $(this);
								return false;
							}
						});
				return _4a5.menu.menu("getItem", item[0]);
			} else {
				return null;
			}
		}
		;
		function _4aa(item) {
			if (!item) {
				return;
			}
			$(_4a4).textbox("button").menubutton({
				text : item.text,
				iconCls : (item.iconCls || null),
				menu : _4a5.menu,
				menuAlign : opts.buttonAlign,
				plain : false
			});
			_4a5.searchbox.find("input.textbox-value").attr("name",
					item.name || item.text);
			$(_4a4).searchbox("resize");
		}
		;
	}
	;
	$.fn.searchbox = function(_4ae, _4af) {
		if (typeof _4ae == "string") {
			var _4b0 = $.fn.searchbox.methods[_4ae];
			if (_4b0) {
				return _4b0(this, _4af);
			} else {
				return this.textbox(_4ae, _4af);
			}
		}
		_4ae = _4ae || {};
		return this.each(function() {
			var _4b1 = $.data(this, "searchbox");
			if (_4b1) {
				$.extend(_4b1.options, _4ae);
			} else {
				$.data(this, "searchbox", {
					options : $.extend({}, $.fn.searchbox.defaults,
							$.fn.searchbox.parseOptions(this), _4ae)
				});
			}
			_4a3(this);
		});
	};
	$.fn.searchbox.methods = {
		options : function(jq) {
			var opts = jq.textbox("options");
			return $.extend($.data(jq[0], "searchbox").options, {
				width : opts.width,
				value : opts.value,
				originalValue : opts.originalValue,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		},
		menu : function(jq) {
			return $.data(jq[0], "searchbox").menu;
		},
		getName : function(jq) {
			return $.data(jq[0], "searchbox").searchbox.find(
					"input.textbox-value").attr("name");
		},
		selectName : function(jq, name) {
			return jq.each(function() {
				var menu = $.data(this, "searchbox").menu;
				if (menu) {
					menu.children("div.menu-item").each(function() {
						var item = menu.menu("getItem", this);
						if (item.name == name) {
							$(this).triggerHandler("click");
							return false;
						}
					});
				}
			});
		},
		destroy : function(jq) {
			return jq.each(function() {
				var menu = $(this).searchbox("menu");
				if (menu) {
					menu.menu("destroy");
				}
				$(this).textbox("destroy");
			});
		}
	};
	$.fn.searchbox.parseOptions = function(_4b2) {
		var t = $(_4b2);
		return $.extend({}, $.fn.textbox.parseOptions(_4b2), $.parser
				.parseOptions(_4b2, [ "menu" ]), {
			searcher : (t.attr("searcher") ? eval(t.attr("searcher"))
					: undefined)
		});
	};
	$.fn.searchbox.defaults = $.extend({}, $.fn.textbox.defaults, {
		inputEvents : $.extend({}, $.fn.textbox.defaults.inputEvents, {
			keydown : function(e) {
				if (e.keyCode == 13) {
					e.preventDefault();
					var t = $(e.data.target);
					var opts = t.searchbox("options");
					t.searchbox("setValue", $(this).val());
					opts.searcher.call(e.data.target, t.searchbox("getValue"),
							t.searchbox("getName"));
					return false;
				}
			}
		}),
		buttonAlign : "left",
		menu : null,
		searcher : function(_4b3, name) {
		}
	});
})(jQuery);
/**
 * form - jQuery EasyUI
 * 
 */
(function($){
	/**
	 * submit the form
	 */
	function ajaxSubmit(target, options){
		var opts = $.data(target, 'form').options;
		$.extend(opts, options||{});
		
		var param = $.extend({}, opts.queryParams);
		if (opts.onSubmit.call(target, param) == false){return;}
		$(target).find('.textbox-text:focus').blur();
		
		var frameId = 'easyui_frame_' + (new Date().getTime());
		var frame = $('<iframe id='+frameId+' name='+frameId+'></iframe>').appendTo('body')
		frame.attr('src', window.ActiveXObject ? 'javascript:false' : 'about:blank');
		frame.css({
			position:'absolute',
			top:-1000,
			left:-1000
		});
		frame.bind('load', cb);
		
		submit(param);
		
		function submit(param){
			var form = $(target);
			if (opts.url){
				form.attr('action', opts.url);
			}
			var t = form.attr('target'), a = form.attr('action');
			form.attr('target', frameId);
			var paramFields = $();
			try {
				for(var n in param){
					var field = $('<input type="hidden" name="' + n + '">').val(param[n]).appendTo(form);
					paramFields = paramFields.add(field);
				}
				checkState();
				form[0].submit();
			} finally {
				form.attr('action', a);
				t ? form.attr('target', t) : form.removeAttr('target');
				paramFields.remove();
			}
		}
		
		function checkState(){
			var f = $('#'+frameId);
			if (!f.length){return}
			try{
				var s = f.contents()[0].readyState;
				if (s && s.toLowerCase() == 'uninitialized'){
					setTimeout(checkState, 100);
				}
			} catch(e){
				cb();
			}
		}
		
		var checkCount = 10;
		function cb(){
			var f = $('#'+frameId);
			if (!f.length){return}
			f.unbind();
			var data = '';
			try{
				var body = f.contents().find('body');
				data = body.html();
				if (data == ''){
					if (--checkCount){
						setTimeout(cb, 100);
						return;
					}
				}
				var ta = body.find('>textarea');
				if (ta.length){
					data = ta.val();
				} else {
					var pre = body.find('>pre');
					if (pre.length){
						data = pre.html();
					}
				}
			} catch(e){
			}
			opts.success(data);
			setTimeout(function(){
				f.unbind();
				f.remove();
			}, 100);
		}
	}
	
	/**
	 * load form data
	 * if data is a URL string type load from remote site, 
	 * otherwise load from local data object. 
	 */
	function load(target, data){
		var opts = $.data(target, 'form').options;
		
		if (typeof data == 'string'){
			var param = {};
			if (opts.onBeforeLoad.call(target, param) == false) return;
			
			$.ajax({
				url: data,
				data: param,
				dataType: 'json',
				success: function(data){
					_load(data);
				},
				error: function(){
					opts.onLoadError.apply(target, arguments);
				}
			});
		} else {
			_load(data);
		}
		
		function _load(data){
			var form = $(target);
			for(var name in data){
				var val = data[name];
				var rr = _checkField(name, val);
				if (!rr.length){
					var count = _loadOther(name, val);
					if (!count){
						$('input[name="'+name+'"]', form).val(val);
						$('textarea[name="'+name+'"]', form).val(val);
						$('select[name="'+name+'"]', form).val(val);
					}
				}
				_loadCombo(name, val);
			}
			opts.onLoadSuccess.call(target, data);
			validate(target);
		}
		
		/**
		 * check the checkbox and radio fields
		 */
		function _checkField(name, val){
			var rr = $(target).find('input[name="'+name+'"][type=radio], input[name="'+name+'"][type=checkbox]');
			rr._propAttr('checked', false);
			rr.each(function(){
				var f = $(this);
				if (f.val() == String(val) || $.inArray(f.val(), $.isArray(val)?val:[val]) >= 0){
					f._propAttr('checked', true);
				}
			});
			return rr;
		}
		
		function _loadOther(name, val){
			var count = 0;
			var pp = ['textbox','numberbox','slider'];
			for(var i=0; i<pp.length; i++){
				var p = pp[i];
				var f = $(target).find('input['+p+'Name="'+name+'"]');
				if (f.length){
					f[p]('setValue', val);
					count += f.length;
				}
			}
			return count;
		}
		
		function _loadCombo(name, val){
			var form = $(target);
			var cc = ['combobox','combotree','combogrid','datetimebox','datebox','combo'];
			var c = form.find('[comboName="' + name + '"]');
			if (c.length){
				for(var i=0; i<cc.length; i++){
					var type = cc[i];
					if (c.hasClass(type+'-f')){
						if (c[type]('options').multiple){
							c[type]('setValues', val);
						} else {
							c[type]('setValue', val);
						}
						return;
					}
				}
			}
		}
	}
	
	/**
	 * clear the form fields
	 */
	function clear(target){
		$('input,select,textarea', target).each(function(){
			var t = this.type, tag = this.tagName.toLowerCase();
			if (t == 'text' || t == 'hidden' || t == 'password' || tag == 'textarea'){
				this.value = '';
			} else if (t == 'file'){
				var file = $(this);
				if (!file.hasClass('textbox-value')){
					var newfile = file.clone().val('');
					newfile.insertAfter(file);
					if (file.data('validatebox')){
						file.validatebox('destroy');
						newfile.validatebox();
					} else {
						file.remove();
					}
				}
			} else if (t == 'checkbox' || t == 'radio'){
				this.checked = false;
			} else if (tag == 'select'){
				this.selectedIndex = -1;
			}
			
		});
		
		var t = $(target);
		var plugins = ['textbox','combo','combobox','combotree','combogrid','slider'];
		for(var i=0; i<plugins.length; i++){
			var plugin = plugins[i];
			var r = t.find('.'+plugin+'-f');
			if (r.length && r[plugin]){
				r[plugin]('clear');
			}
		}
		validate(target);
	}
	
	function reset(target){
		target.reset();
		var t = $(target);
		
		var plugins = ['textbox','combo','combobox','combotree','combogrid','datebox','datetimebox','spinner','timespinner','numberbox','numberspinner','slider'];
		for(var i=0; i<plugins.length; i++){
			var plugin = plugins[i];
			var r = t.find('.'+plugin+'-f');
			if (r.length && r[plugin]){
				r[plugin]('reset');
			}
		}
		validate(target);
	}
	
	/**
	 * set the form to make it can submit with ajax.
	 */
	function setForm(target){
		var options = $.data(target, 'form').options;
		$(target).unbind('.form');
		if (options.ajax){
			$(target).bind('submit.form', function(){
				setTimeout(function(){
					ajaxSubmit(target, options);
				}, 0);
				return false;
			});
		}
		setValidation(target, options.novalidate);
	}
	
	function initForm(target, options){
		options = options || {};
		var state = $.data(target, 'form');
		if (state){
			$.extend(state.options, options);
		} else {
			$.data(target, 'form', {
				options: $.extend({}, $.fn.form.defaults, $.fn.form.parseOptions(target), options)
			});
		}
	}
	
	function validate(target){
		if ($.fn.validatebox){
			var t = $(target);
			t.find('.validatebox-text:not(:disabled)').validatebox('validate');
			var invalidbox = t.find('.validatebox-invalid');
			invalidbox.filter(':not(:disabled):first').focus();
			return invalidbox.length == 0;
		}
		return true;
	}
	
	function setValidation(target, novalidate){
		var opts = $.data(target, 'form').options;
		opts.novalidate = novalidate;
		$(target).find('.validatebox-text:not(:disabled)').validatebox(novalidate ? 'disableValidation' : 'enableValidation');
	}
	
	$.fn.form = function(options, param){
		if (typeof options == 'string'){
			this.each(function(){
				initForm(this);
			});
			return $.fn.form.methods[options](this, param);
		}
		
		return this.each(function(){
			initForm(this, options);
			setForm(this);
		});
	};
	
	$.fn.form.methods = {
		options: function(jq){
			return $.data(jq[0], 'form').options;
		},
		submit: function(jq, options){
			return jq.each(function(){
				ajaxSubmit(this, options);
			});
		},
		load: function(jq, data){
			return jq.each(function(){
				load(this, data);
			});
		},
		clear: function(jq){
			return jq.each(function(){
				clear(this);
			});
		},
		reset: function(jq){
			return jq.each(function(){
				reset(this);
			});
		},
		validate: function(jq){
			return validate(jq[0]);
		},
		disableValidation: function(jq){
			return jq.each(function(){
				setValidation(this, true);
			});
		},
		enableValidation: function(jq){
			return jq.each(function(){
				setValidation(this, false);
			});
		}
	};
	
	$.fn.form.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [{ajax:'boolean'}]), {
			url: (t.attr('action') ? t.attr('action') : undefined)
		});
	};
	
	$.fn.form.defaults = {
		novalidate: false,
		ajax: true,
		url: null,
		queryParams: {},
		onSubmit: function(param){return $(this).form('validate');},
		success: function(data){},
		onBeforeLoad: function(param){},
		onLoadSuccess: function(data){},
		onLoadError: function(){}
	};
})(jQuery);

(function($) {
	function _4e4(_4e5) {
		var _4e6 = $.data(_4e5, "numberbox");
		var opts = _4e6.options;
		$(_4e5).addClass("numberbox-f").textbox(opts);
		$(_4e5).textbox("textbox").css({
			imeMode : "disabled"
		});
		$(_4e5).attr("numberboxName", $(_4e5).attr("textboxName"));
		_4e6.numberbox = $(_4e5).next();
		_4e6.numberbox.addClass("numberbox");
		var _4e7 = opts.parser.call(_4e5, opts.value);
		var _4e8 = opts.formatter.call(_4e5, _4e7);
		$(_4e5).numberbox("initValue", _4e7).numberbox("setText", _4e8);
	}
	;
	function _4e9(_4ea, _4eb) {
		var _4ec = $.data(_4ea, "numberbox");
		var opts = _4ec.options;
		var _4eb = opts.parser.call(_4ea, _4eb);
		var text = opts.formatter.call(_4ea, _4eb);
		opts.value = _4eb;
		$(_4ea).textbox("setValue", _4eb).textbox("setText", text);
	}
	;
	$.fn.numberbox = function(_4ed, _4ee) {
		if (typeof _4ed == "string") {
			var _4ef = $.fn.numberbox.methods[_4ed];
			if (_4ef) {
				return _4ef(this, _4ee);
			} else {
				return this.textbox(_4ed, _4ee);
			}
		}
		_4ed = _4ed || {};
		return this.each(function() {
			var _4f0 = $.data(this, "numberbox");
			if (_4f0) {
				$.extend(_4f0.options, _4ed);
			} else {
				_4f0 = $.data(this, "numberbox", {
					options : $.extend({}, $.fn.numberbox.defaults,
							$.fn.numberbox.parseOptions(this), _4ed)
				});
			}
			_4e4(this);
		});
	};
	$.fn.numberbox.methods = {
		options : function(jq) {
			var opts = jq.data("textbox") ? jq.textbox("options") : {};
			return $.extend($.data(jq[0], "numberbox").options, {
				width : opts.width,
				originalValue : opts.originalValue,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		},
		fix : function(jq) {
			return jq.each(function() {
				$(this).numberbox("setValue", $(this).numberbox("getText"));
			});
		},
		setValue : function(jq, _4f1) {
			return jq.each(function() {
				_4e9(this, _4f1);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				$(this).textbox("clear");
				$(this).numberbox("options").value = "";
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				$(this).textbox("reset");
				$(this).numberbox("setValue", $(this).numberbox("getValue"));
			});
		}
	};
	$.fn.numberbox.parseOptions = function(_4f2) {
		var t = $(_4f2);
		return $.extend({}, $.fn.textbox.parseOptions(_4f2), $.parser
				.parseOptions(_4f2, [ "decimalSeparator", "groupSeparator",
						"suffix", {
							min : "number",
							max : "number",
							precision : "number"
						} ]), {
			prefix : (t.attr("prefix") ? t.attr("prefix") : undefined)
		});
	};
	$.fn.numberbox.defaults = $
			.extend(
					{},
					$.fn.textbox.defaults,
					{
						inputEvents : {
							keypress : function(e) {
								var _4f3 = e.data.target;
								var opts = $(_4f3).numberbox("options");
								return opts.filter.call(_4f3, e);
							},
							blur : function(e) {
								var _4f4 = e.data.target;
								$(_4f4).numberbox("setValue",
										$(_4f4).numberbox("getText"));
							},
							keydown : function(e) {
								if (e.keyCode == 13) {
									var _4f5 = e.data.target;
									$(_4f5).numberbox("setValue",
											$(_4f5).numberbox("getText"));
								}
							}
						},
						min : null,
						max : null,
						precision : 0,
						decimalSeparator : ".",
						groupSeparator : "",
						prefix : "",
						suffix : "",
						filter : function(e) {
							var opts = $(this).numberbox("options");
							var s = $(this).numberbox("getText");
							if (e.which == 13) {
								return true;
							}
							if (e.which == 45) {
								return (s.indexOf("-") == -1 ? true : false);
							}
							var c = String.fromCharCode(e.which);
							if (c == opts.decimalSeparator) {
								return (s.indexOf(c) == -1 ? true : false);
							} else {
								if (c == opts.groupSeparator) {
									return true;
								} else {
									if ((e.which >= 48 && e.which <= 57
											&& e.ctrlKey == false && e.shiftKey == false)
											|| e.which == 0 || e.which == 8) {
										return true;
									} else {
										if (e.ctrlKey == true
												&& (e.which == 99 || e.which == 118)) {
											return true;
										} else {
											return false;
										}
									}
								}
							}
						},
						formatter : function(_4f6) {
							if (!_4f6) {
								return _4f6;
							}
							_4f6 = _4f6 + "";
							var opts = $(this).numberbox("options");
							var s1 = _4f6, s2 = "";
							var dpos = _4f6.indexOf(".");
							if (dpos >= 0) {
								s1 = _4f6.substring(0, dpos);
								s2 = _4f6.substring(dpos + 1, _4f6.length);
							}
							if (opts.groupSeparator) {
								var p = /(\d+)(\d{3})/;
								while (p.test(s1)) {
									s1 = s1.replace(p, "$1"
											+ opts.groupSeparator + "$2");
								}
							}
							if (s2) {
								return opts.prefix + s1 + opts.decimalSeparator
										+ s2 + opts.suffix;
							} else {
								return opts.prefix + s1 + opts.suffix;
							}
						},
						parser : function(s) {
							s = s + "";
							var opts = $(this).numberbox("options");
							if (parseFloat(s) != s) {
								if (opts.prefix) {
									s = $.trim(s.replace(new RegExp("\\"
											+ $.trim(opts.prefix), "g"), ""));
								}
								if (opts.suffix) {
									s = $.trim(s.replace(new RegExp("\\"
											+ $.trim(opts.suffix), "g"), ""));
								}
								if (opts.groupSeparator) {
									s = $.trim(s.replace(new RegExp("\\"
											+ opts.groupSeparator, "g"), ""));
								}
								if (opts.decimalSeparator) {
									s = $
											.trim(s.replace(new RegExp("\\"
													+ opts.decimalSeparator,
													"g"), "."));
								}
								s = s.replace(/\s/g, "");
							}
							var val = parseFloat(s).toFixed(opts.precision);
							if (isNaN(val)) {
								val = "";
							} else {
								if (typeof (opts.min) == "number"
										&& val < opts.min) {
									val = opts.min.toFixed(opts.precision);
								} else {
									if (typeof (opts.max) == "number"
											&& val > opts.max) {
										val = opts.max.toFixed(opts.precision);
									}
								}
							}
							return val;
						}
					});
})(jQuery);
/**
 * calendar - jQuery EasyUI
 * 
 */
(function($){
	
	function setSize(target, param){
		var opts = $.data(target, 'calendar').options;
		var t = $(target);
		if (param){
			$.extend(opts, {
				width: param.width,
				height: param.height
			});
		}
		t._size(opts, t.parent());
		t.find('.calendar-body')._outerHeight(t.height() - t.find('.calendar-header')._outerHeight());
		if (t.find('.calendar-menu').is(':visible')){
			showSelectMenus(target);
		}
	}
	
	function init(target){
		$(target).addClass('calendar').html(
				'<div class="calendar-header">' +
					'<div class="calendar-nav calendar-prevmonth"></div>' +
					'<div class="calendar-nav calendar-nextmonth"></div>' +
					'<div class="calendar-nav calendar-prevyear"></div>' +
					'<div class="calendar-nav calendar-nextyear"></div>' +
					'<div class="calendar-title">' +
						'<span class="calendar-text"></span>' +
					'</div>' +
				'</div>' +
				'<div class="calendar-body">' +
					'<div class="calendar-menu">' +
						'<div class="calendar-menu-year-inner">' +
							'<span class="calendar-nav calendar-menu-prev"></span>' +
							'<span><input class="calendar-menu-year" type="text"></input></span>' +
							'<span class="calendar-nav calendar-menu-next"></span>' +
						'</div>' +
						'<div class="calendar-menu-month-inner">' +
						'</div>' +
					'</div>' +
				'</div>'
		);
		
		
		$(target).bind('_resize', function(e,force){
			if ($(this).hasClass('easyui-fluid') || force){
				setSize(target);
			}
			return false;
		});
	}
	
	function bindEvents(target){
		var opts = $.data(target, 'calendar').options;
		var menu = $(target).find('.calendar-menu');
		menu.find('.calendar-menu-year').unbind('.calendar').bind('keypress.calendar', function(e){
			if (e.keyCode == 13){
				setDate(true);
			}
		});
		$(target).unbind('.calendar').bind('mouseover.calendar', function(e){
			var t = toTarget(e.target);
			if (t.hasClass('calendar-nav') || t.hasClass('calendar-text') || (t.hasClass('calendar-day') && !t.hasClass('calendar-disabled'))){
				t.addClass('calendar-nav-hover');
			}
		}).bind('mouseout.calendar', function(e){
			var t = toTarget(e.target);
			if (t.hasClass('calendar-nav') || t.hasClass('calendar-text') || (t.hasClass('calendar-day') && !t.hasClass('calendar-disabled'))){
				t.removeClass('calendar-nav-hover');
			}
		}).bind('click.calendar', function(e){
			var t = toTarget(e.target);
			if (t.hasClass('calendar-menu-next') || t.hasClass('calendar-nextyear')){
				showYear(1);
			} else if (t.hasClass('calendar-menu-prev') || t.hasClass('calendar-prevyear')){
				showYear(-1);
			} else if (t.hasClass('calendar-menu-month')){
				menu.find('.calendar-selected').removeClass('calendar-selected');
				t.addClass('calendar-selected');
				setDate(true);
			} else if (t.hasClass('calendar-prevmonth')){
				showMonth(-1);
			} else if (t.hasClass('calendar-nextmonth')){
				showMonth(1);
			} else if (t.hasClass('calendar-text')){
				if (menu.is(':visible')){
					menu.hide();
				} else {
					showSelectMenus(target);
				}
			} else if (t.hasClass('calendar-day')){
				if (t.hasClass('calendar-disabled')){return}
				var oldValue = opts.current;
				t.closest('div.calendar-body').find('.calendar-selected').removeClass('calendar-selected');
				t.addClass('calendar-selected');
				var parts = t.attr('abbr').split(',');
				var y = parseInt(parts[0]);
				var m = parseInt(parts[1]);
				var d = parseInt(parts[2]);
				opts.current = new Date(y, m-1, d);
				opts.onSelect.call(target, opts.current);
				if (!oldValue || oldValue.getTime() != opts.current.getTime()){
					opts.onChange.call(target, opts.current, oldValue);
				}
				if (opts.year != y || opts.month != m){
					opts.year = y;
					opts.month = m;
					show(target);
				}
			}
		});
		function toTarget(t){
			var day = $(t).closest('.calendar-day');
			if (day.length){
				return day;
			} else {
				return $(t);
			}
		}
		function setDate(hideMenu){
			var menu = $(target).find('.calendar-menu');
			var year = menu.find('.calendar-menu-year').val();
			var month = menu.find('.calendar-selected').attr('abbr');
			if (!isNaN(year)){
				opts.year = parseInt(year);
				opts.month = parseInt(month);
				show(target);
			}
			if (hideMenu){menu.hide()}
		}
		function showYear(delta){
			opts.year += delta;
			show(target);
			menu.find('.calendar-menu-year').val(opts.year);
		}
		function showMonth(delta){
			opts.month += delta;
			if (opts.month > 12){
				opts.year++;
				opts.month = 1;
			} else if (opts.month < 1){
				opts.year--;
				opts.month = 12;
			}
			show(target);
			
			menu.find('td.calendar-selected').removeClass('calendar-selected');
			menu.find('td:eq(' + (opts.month-1) + ')').addClass('calendar-selected');
		}
	}
	
	/**
	 * show the select menu that can change year or month, if the menu is not be created then create it.
	 */
	function showSelectMenus(target){
		var opts = $.data(target, 'calendar').options;
		$(target).find('.calendar-menu').show();
		
		if ($(target).find('.calendar-menu-month-inner').is(':empty')){
			$(target).find('.calendar-menu-month-inner').empty();
			var t = $('<table class="calendar-mtable"></table>').appendTo($(target).find('.calendar-menu-month-inner'));
			var idx = 0;
			for(var i=0; i<3; i++){
				var tr = $('<tr></tr>').appendTo(t);
				for(var j=0; j<4; j++){
					$('<td class="calendar-nav calendar-menu-month"></td>').html(opts.months[idx++]).attr('abbr',idx).appendTo(tr);
				}
			}
		}
		
		var body = $(target).find('.calendar-body');
		var sele = $(target).find('.calendar-menu');
		var seleYear = sele.find('.calendar-menu-year-inner');
		var seleMonth = sele.find('.calendar-menu-month-inner');
		
		seleYear.find('input').val(opts.year).focus();
		seleMonth.find('td.calendar-selected').removeClass('calendar-selected');
		seleMonth.find('td:eq('+(opts.month-1)+')').addClass('calendar-selected');
		
		sele._outerWidth(body._outerWidth());
		sele._outerHeight(body._outerHeight());
		seleMonth._outerHeight(sele.height() - seleYear._outerHeight());
	}
	
	/**
	 * get weeks data.
	 */
	function getWeeks(target, year, month){
		var opts = $.data(target, 'calendar').options;
		var dates = [];
		var lastDay = new Date(year, month, 0).getDate();
		for(var i=1; i<=lastDay; i++) dates.push([year,month,i]);
		
		// group date by week
		var weeks = [], week = [];
		var memoDay = -1;
		while(dates.length > 0){
			var date = dates.shift();
			week.push(date);
			var day = new Date(date[0],date[1]-1,date[2]).getDay();
			if (memoDay == day){
				day = 0;
			} else if (day == (opts.firstDay==0 ? 7 : opts.firstDay) - 1){
				weeks.push(week);
				week = [];
			}
			memoDay = day;
		}
		if (week.length){
			weeks.push(week);
		}
		
		var firstWeek = weeks[0];
		if (firstWeek.length < 7){
			while(firstWeek.length < 7){
				var firstDate = firstWeek[0];
				var date = new Date(firstDate[0],firstDate[1]-1,firstDate[2]-1)
				firstWeek.unshift([date.getFullYear(), date.getMonth()+1, date.getDate()]);
			}
		} else {
			var firstDate = firstWeek[0];
			var week = [];
			for(var i=1; i<=7; i++){
				var date = new Date(firstDate[0], firstDate[1]-1, firstDate[2]-i);
				week.unshift([date.getFullYear(), date.getMonth()+1, date.getDate()]);
			}
			weeks.unshift(week);
		}
		
		var lastWeek = weeks[weeks.length-1];
		while(lastWeek.length < 7){
			var lastDate = lastWeek[lastWeek.length-1];
			var date = new Date(lastDate[0], lastDate[1]-1, lastDate[2]+1);
			lastWeek.push([date.getFullYear(), date.getMonth()+1, date.getDate()]);
		}
		if (weeks.length < 6){
			var lastDate = lastWeek[lastWeek.length-1];
			var week = [];
			for(var i=1; i<=7; i++){
				var date = new Date(lastDate[0], lastDate[1]-1, lastDate[2]+i);
				week.push([date.getFullYear(), date.getMonth()+1, date.getDate()]);
			}
			weeks.push(week);
		}
		
		return weeks;
	}
	
	/**
	 * show the calendar day.
	 */
	function show(target){
		var opts = $.data(target, 'calendar').options;
		if (opts.current && !opts.validator.call(target, opts.current)){
			opts.current = null;
		}
		
		var now = new Date();
		var todayInfo = now.getFullYear()+','+(now.getMonth()+1)+','+now.getDate();
		var currentInfo = opts.current ? (opts.current.getFullYear()+','+(opts.current.getMonth()+1)+','+opts.current.getDate()) : '';
		// calulate the saturday and sunday index
		var saIndex = 6 - opts.firstDay;
		var suIndex = saIndex + 1;
		if (saIndex >= 7) saIndex -= 7;
		if (suIndex >= 7) suIndex -= 7;
		
		$(target).find('.calendar-title span').html(opts.months[opts.month-1] + ' ' + opts.year);
		
		var body = $(target).find('div.calendar-body');
		body.children('table').remove();
		
		var data = ['<table class="calendar-dtable" cellspacing="0" cellpadding="0" border="0">'];
		data.push('<thead><tr>');
		for(var i=opts.firstDay; i<opts.weeks.length; i++){
			data.push('<th>'+opts.weeks[i]+'</th>');
		}
		for(var i=0; i<opts.firstDay; i++){
			data.push('<th>'+opts.weeks[i]+'</th>');
		}
		data.push('</tr></thead>');
		
		data.push('<tbody>');
		var weeks = getWeeks(target, opts.year, opts.month);
		for(var i=0; i<weeks.length; i++){
			var week = weeks[i];
			var cls = '';
			if (i == 0){cls = 'calendar-first';}
			else if (i == weeks.length - 1){cls = 'calendar-last';}
			data.push('<tr class="' + cls + '">');
			for(var j=0; j<week.length; j++){
				var day = week[j];
				var s = day[0]+','+day[1]+','+day[2];
				var dvalue = new Date(day[0], parseInt(day[1])-1, day[2]);
				var d = opts.formatter.call(target, dvalue);
				var css = opts.styler.call(target, dvalue);
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (css){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				
				var cls = 'calendar-day';
				if (!(opts.year == day[0] && opts.month == day[1])){
					cls += ' calendar-other-month';
				}
				if (s == todayInfo){cls += ' calendar-today';}
				if (s == currentInfo){cls += ' calendar-selected';}
				if (j == saIndex){cls += ' calendar-saturday';}
				else if (j == suIndex){cls += ' calendar-sunday';}
				if (j == 0){cls += ' calendar-first';}
				else if (j == week.length-1){cls += ' calendar-last';}
				
				cls += ' ' + classValue;
				if (!opts.validator.call(target, dvalue)){
					cls += ' calendar-disabled';
				}
				
				data.push('<td class="' + cls + '" abbr="' + s + '" style="' + styleValue + '">' + d + '</td>');
			}
			data.push('</tr>');
		}
		data.push('</tbody>');
		data.push('</table>');
		
		body.append(data.join(''));
		body.children('table.calendar-dtable').prependTo(body);

		opts.onNavigate.call(target, opts.year, opts.month);
	}
	
	$.fn.calendar = function(options, param){
		if (typeof options == 'string'){
			return $.fn.calendar.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'calendar');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'calendar', {
					options:$.extend({}, $.fn.calendar.defaults, $.fn.calendar.parseOptions(this), options)
				});
				init(this);
			}
			if (state.options.border == false){
				$(this).addClass('calendar-noborder');
			}
			setSize(this);
			bindEvents(this);
			show(this);
			$(this).find('div.calendar-menu').hide();	// hide the calendar menu
		});
	};
	
	$.fn.calendar.methods = {
		options: function(jq){
			return $.data(jq[0], 'calendar').options;
		},
		resize: function(jq, param){
			return jq.each(function(){
				setSize(this, param);
			});
		},
		moveTo: function(jq, date){
			return jq.each(function(){
				var opts = $(this).calendar('options');
				if (opts.validator.call(this, date)){
					var oldValue = opts.current;
					$(this).calendar({
						year: date.getFullYear(),
						month: date.getMonth()+1,
						current: date
					});
					if (!oldValue || oldValue.getTime() != date.getTime()){
						opts.onChange.call(this, opts.current, oldValue);
					}
				}
			});
		}
	};
	
	$.fn.calendar.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [
			{firstDay:'number',fit:'boolean',border:'boolean'}
		]));
	};
	
	$.fn.calendar.defaults = {
		width:180,
		height:180,
		fit:false,
		border:true,
		firstDay:0,
		weeks:['S','M','T','W','T','F','S'],
		months:['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
		year:new Date().getFullYear(),
		month:new Date().getMonth()+1,
		current:(function(){
			var d = new Date();
			return new Date(d.getFullYear(), d.getMonth(), d.getDate());
		})(),
		
		formatter:function(date){return date.getDate()},
		styler:function(date){return ''},
		validator:function(date){return true},
		
		onSelect: function(date){},
		onChange: function(newDate, oldDate){},
		onNavigate: function(year, month){}
	};
})(jQuery);

(function($) {
	function _529(_52a) {
		var _52b = $.data(_52a, "spinner");
		var opts = _52b.options;
		var _52c = $.extend(true, [], opts.icons);
		_52c.push({
			iconCls : "spinner-arrow",
			handler : function(e) {
				_52d(e);
			}
		});
		$(_52a).addClass("spinner-f").textbox($.extend({}, opts, {
			icons : _52c
		}));
		var _52e = $(_52a).textbox("getIcon", _52c.length - 1);
		_52e
				.append("<a href=\"javascript:void(0)\" class=\"spinner-arrow-up\" tabindex=\"-1\"></a>");
		_52e
				.append("<a href=\"javascript:void(0)\" class=\"spinner-arrow-down\" tabindex=\"-1\"></a>");
		$(_52a).attr("spinnerName", $(_52a).attr("textboxName"));
		_52b.spinner = $(_52a).next();
		_52b.spinner.addClass("spinner");
	}
	;
	function _52d(e) {
		var _52f = e.data.target;
		var opts = $(_52f).spinner("options");
		var up = $(e.target).closest("a.spinner-arrow-up");
		if (up.length) {
			opts.spin.call(_52f, false);
			opts.onSpinUp.call(_52f);
			$(_52f).spinner("validate");
		}
		var down = $(e.target).closest("a.spinner-arrow-down");
		if (down.length) {
			opts.spin.call(_52f, true);
			opts.onSpinDown.call(_52f);
			$(_52f).spinner("validate");
		}
	}
	;
	$.fn.spinner = function(_530, _531) {
		if (typeof _530 == "string") {
			var _532 = $.fn.spinner.methods[_530];
			if (_532) {
				return _532(this, _531);
			} else {
				return this.textbox(_530, _531);
			}
		}
		_530 = _530 || {};
		return this.each(function() {
			var _533 = $.data(this, "spinner");
			if (_533) {
				$.extend(_533.options, _530);
			} else {
				_533 = $.data(this, "spinner", {
					options : $.extend({}, $.fn.spinner.defaults, $.fn.spinner
							.parseOptions(this), _530)
				});
			}
			_529(this);
		});
	};
	$.fn.spinner.methods = {
		options : function(jq) {
			var opts = jq.textbox("options");
			return $.extend($.data(jq[0], "spinner").options, {
				width : opts.width,
				value : opts.value,
				originalValue : opts.originalValue,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		}
	};
	$.fn.spinner.parseOptions = function(_534) {
		return $.extend({}, $.fn.textbox.parseOptions(_534), $.parser
				.parseOptions(_534, [ "min", "max", {
					increment : "number"
				} ]));
	};
	$.fn.spinner.defaults = $.extend({}, $.fn.textbox.defaults, {
		min : null,
		max : null,
		increment : 1,
		spin : function(down) {
		},
		onSpinUp : function() {
		},
		onSpinDown : function() {
		}
	});
})(jQuery);
(function($) {
	function _535(_536) {
		$(_536).addClass("numberspinner-f");
		var opts = $.data(_536, "numberspinner").options;
		$(_536).numberbox(opts).spinner(opts);
		$(_536).numberbox("setValue", opts.value);
	}
	;
	function _537(_538, down) {
		var opts = $.data(_538, "numberspinner").options;
		var v = parseFloat($(_538).numberbox("getValue") || opts.value) || 0;
		if (down) {
			v -= opts.increment;
		} else {
			v += opts.increment;
		}
		$(_538).numberbox("setValue", v);
	}
	;
	$.fn.numberspinner = function(_539, _53a) {
		if (typeof _539 == "string") {
			var _53b = $.fn.numberspinner.methods[_539];
			if (_53b) {
				return _53b(this, _53a);
			} else {
				return this.numberbox(_539, _53a);
			}
		}
		_539 = _539 || {};
		return this.each(function() {
			var _53c = $.data(this, "numberspinner");
			if (_53c) {
				$.extend(_53c.options, _539);
			} else {
				$.data(this, "numberspinner", {
					options : $.extend({}, $.fn.numberspinner.defaults,
							$.fn.numberspinner.parseOptions(this), _539)
				});
			}
			_535(this);
		});
	};
	$.fn.numberspinner.methods = {
		options : function(jq) {
			var opts = jq.numberbox("options");
			return $.extend($.data(jq[0], "numberspinner").options, {
				width : opts.width,
				value : opts.value,
				originalValue : opts.originalValue,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		}
	};
	$.fn.numberspinner.parseOptions = function(_53d) {
		return $.extend({}, $.fn.spinner.parseOptions(_53d), $.fn.numberbox
				.parseOptions(_53d), {});
	};
	$.fn.numberspinner.defaults = $.extend({}, $.fn.spinner.defaults,
			$.fn.numberbox.defaults, {
				spin : function(down) {
					_537(this, down);
				}
			});
})(jQuery);
(function($) {
	function _53e(_53f) {
		var _540 = 0;
		if (_53f.selectionStart) {
			_540 = _53f.selectionStart;
		} else {
			if (_53f.createTextRange) {
				var _541 = _53f.createTextRange();
				var s = document.selection.createRange();
				s.setEndPoint("StartToStart", _541);
				_540 = s.text.length;
			}
		}
		return _540;
	}
	;
	function _542(_543, _544, end) {
		if (_543.selectionStart) {
			_543.setSelectionRange(_544, end);
		} else {
			if (_543.createTextRange) {
				var _545 = _543.createTextRange();
				_545.collapse();
				_545.moveEnd("character", end);
				_545.moveStart("character", _544);
				_545.select();
			}
		}
	}
	;
	function _546(_547) {
		var opts = $.data(_547, "timespinner").options;
		$(_547).addClass("timespinner-f").spinner(opts);
		var _548 = opts.formatter
				.call(_547, opts.parser.call(_547, opts.value));
		$(_547).timespinner("initValue", _548);
	}
	;
	function _549(e) {
		var _54a = e.data.target;
		var opts = $.data(_54a, "timespinner").options;
		var _54b = _53e(this);
		for ( var i = 0; i < opts.selections.length; i++) {
			var _54c = opts.selections[i];
			if (_54b >= _54c[0] && _54b <= _54c[1]) {
				_54d(_54a, i);
				return;
			}
		}
	}
	;
	function _54d(_54e, _54f) {
		var opts = $.data(_54e, "timespinner").options;
		if (_54f != undefined) {
			opts.highlight = _54f;
		}
		var _550 = opts.selections[opts.highlight];
		if (_550) {
			var tb = $(_54e).timespinner("textbox");
			_542(tb[0], _550[0], _550[1]);
			tb.focus();
		}
	}
	;
	function _551(_552, _553) {
		var opts = $.data(_552, "timespinner").options;
		var _553 = opts.parser.call(_552, _553);
		var text = opts.formatter.call(_552, _553);
		$(_552).spinner("setValue", text);
	}
	;
	function _554(_555, down) {
		var opts = $.data(_555, "timespinner").options;
		var s = $(_555).timespinner("getValue");
		var _556 = opts.selections[opts.highlight];
		var s1 = s.substring(0, _556[0]);
		var s2 = s.substring(_556[0], _556[1]);
		var s3 = s.substring(_556[1]);
		var v = s1 + ((parseInt(s2) || 0) + opts.increment * (down ? -1 : 1))
				+ s3;
		$(_555).timespinner("setValue", v);
		_54d(_555);
	}
	;
	$.fn.timespinner = function(_557, _558) {
		if (typeof _557 == "string") {
			var _559 = $.fn.timespinner.methods[_557];
			if (_559) {
				return _559(this, _558);
			} else {
				return this.spinner(_557, _558);
			}
		}
		_557 = _557 || {};
		return this.each(function() {
			var _55a = $.data(this, "timespinner");
			if (_55a) {
				$.extend(_55a.options, _557);
			} else {
				$.data(this, "timespinner", {
					options : $.extend({}, $.fn.timespinner.defaults,
							$.fn.timespinner.parseOptions(this), _557)
				});
			}
			_546(this);
		});
	};
	$.fn.timespinner.methods = {
		options : function(jq) {
			var opts = jq.data("spinner") ? jq.spinner("options") : {};
			return $.extend($.data(jq[0], "timespinner").options, {
				width : opts.width,
				value : opts.value,
				originalValue : opts.originalValue,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		},
		setValue : function(jq, _55b) {
			return jq.each(function() {
				_551(this, _55b);
			});
		},
		getHours : function(jq) {
			var opts = $.data(jq[0], "timespinner").options;
			var vv = jq.timespinner("getValue").split(opts.separator);
			return parseInt(vv[0], 10);
		},
		getMinutes : function(jq) {
			var opts = $.data(jq[0], "timespinner").options;
			var vv = jq.timespinner("getValue").split(opts.separator);
			return parseInt(vv[1], 10);
		},
		getSeconds : function(jq) {
			var opts = $.data(jq[0], "timespinner").options;
			var vv = jq.timespinner("getValue").split(opts.separator);
			return parseInt(vv[2], 10) || 0;
		}
	};
	$.fn.timespinner.parseOptions = function(_55c) {
		return $.extend({}, $.fn.spinner.parseOptions(_55c), $.parser
				.parseOptions(_55c, [ "separator", {
					showSeconds : "boolean",
					highlight : "number"
				} ]));
	};
	$.fn.timespinner.defaults = $.extend({}, $.fn.spinner.defaults, {
		inputEvents : $.extend({}, $.fn.spinner.defaults.inputEvents, {
			click : function(e) {
				_549.call(this, e);
			},
			blur : function(e) {
				var t = $(e.data.target);
				t.timespinner("setValue", t.timespinner("getText"));
			},
			keydown : function(e) {
				if (e.keyCode == 13) {
					var t = $(e.data.target);
					t.timespinner("setValue", t.timespinner("getText"));
				}
			}
		}),
		formatter : function(date) {
			if (!date) {
				return "";
			}
			var opts = $(this).timespinner("options");
			var tt = [ _55d(date.getHours()), _55d(date.getMinutes()) ];
			if (opts.showSeconds) {
				tt.push(_55d(date.getSeconds()));
			}
			return tt.join(opts.separator);
			function _55d(_55e) {
				return (_55e < 10 ? "0" : "") + _55e;
			}
			;
		},
		parser : function(s) {
			var opts = $(this).timespinner("options");
			var date = _55f(s);
			if (date) {
				var min = _55f(opts.min);
				var max = _55f(opts.max);
				if (min && min > date) {
					date = min;
				}
				if (max && max < date) {
					date = max;
				}
			}
			return date;
			function _55f(s) {
				if (!s) {
					return null;
				}
				var tt = s.split(opts.separator);
				return new Date(1900, 0, 0, parseInt(tt[0], 10) || 0, parseInt(
						tt[1], 10) || 0, parseInt(tt[2], 10) || 0);
			}
			;
			if (!s) {
				return null;
			}
			var tt = s.split(opts.separator);
			return new Date(1900, 0, 0, parseInt(tt[0], 10) || 0, parseInt(
					tt[1], 10) || 0, parseInt(tt[2], 10) || 0);
		},
		selections : [ [ 0, 2 ], [ 3, 5 ], [ 6, 8 ] ],
		separator : ":",
		showSeconds : false,
		highlight : 0,
		spin : function(down) {
			_554(this, down);
		}
	});
})(jQuery);
(function($) {
	function _560(_561) {
		var opts = $.data(_561, "datetimespinner").options;
		$(_561).addClass("datetimespinner-f").timespinner(opts);
	}
	;
	$.fn.datetimespinner = function(_562, _563) {
		if (typeof _562 == "string") {
			var _564 = $.fn.datetimespinner.methods[_562];
			if (_564) {
				return _564(this, _563);
			} else {
				return this.timespinner(_562, _563);
			}
		}
		_562 = _562 || {};
		return this.each(function() {
			var _565 = $.data(this, "datetimespinner");
			if (_565) {
				$.extend(_565.options, _562);
			} else {
				$.data(this, "datetimespinner", {
					options : $.extend({}, $.fn.datetimespinner.defaults,
							$.fn.datetimespinner.parseOptions(this), _562)
				});
			}
			_560(this);
		});
	};
	$.fn.datetimespinner.methods = {
		options : function(jq) {
			var opts = jq.timespinner("options");
			return $.extend($.data(jq[0], "datetimespinner").options, {
				width : opts.width,
				value : opts.value,
				originalValue : opts.originalValue,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		}
	};
	$.fn.datetimespinner.parseOptions = function(_566) {
		return $.extend({}, $.fn.timespinner.parseOptions(_566), $.parser
				.parseOptions(_566, []));
	};
	$.fn.datetimespinner.defaults = $.extend({}, $.fn.timespinner.defaults, {
		formatter : function(date) {
			if (!date) {
				return "";
			}
			return $.fn.datebox.defaults.formatter.call(this, date) + " "
					+ $.fn.timespinner.defaults.formatter.call(this, date);
		},
		parser : function(s) {
			s = $.trim(s);
			if (!s) {
				return null;
			}
			var dt = s.split(" ");
			var _567 = $.fn.datebox.defaults.parser.call(this, dt[0]);
			if (dt.length < 2) {
				return _567;
			}
			var _568 = $.fn.timespinner.defaults.parser.call(this, dt[1]);
			return new Date(_567.getFullYear(), _567.getMonth(),
					_567.getDate(), _568.getHours(), _568.getMinutes(), _568
							.getSeconds());
		},
		selections : [ [ 0, 2 ], [ 3, 5 ], [ 6, 10 ], [ 11, 13 ], [ 14, 16 ],
				[ 17, 19 ] ]
	});
})(jQuery);
(function($) {
	var _569 = 0;
	function _56a(a, o) {
		for ( var i = 0, len = a.length; i < len; i++) {
			if (a[i] == o) {
				return i;
			}
		}
		return -1;
	}
	;
	function _56b(a, o, id) {
		if (typeof o == "string") {
			for ( var i = 0, len = a.length; i < len; i++) {
				if (a[i][o] == id) {
					a.splice(i, 1);
					return;
				}
			}
		} else {
			var _56c = _56a(a, o);
			if (_56c != -1) {
				a.splice(_56c, 1);
			}
		}
	}
	;
	function _56d(a, o, r) {
		for ( var i = 0, len = a.length; i < len; i++) {
			if (a[i][o] == r[o]) {
				return;
			}
		}
		a.push(r);
	}
	;
	function _56e(_56f) {
		var _570 = $.data(_56f, "datagrid");
		var opts = _570.options;
		var _571 = _570.panel;
		var dc = _570.dc;
		var ss = null;
		if (opts.sharedStyleSheet) {
			ss = typeof opts.sharedStyleSheet == "boolean" ? "head"
					: opts.sharedStyleSheet;
		} else {
			ss = _571.closest("div.datagrid-view");
			if (!ss.length) {
				ss = dc.view;
			}
		}
		var cc = $(ss);
		var _572 = $.data(cc[0], "ss");
		if (!_572) {
			_572 = $.data(cc[0], "ss", {
				cache : {},
				dirty : []
			});
		}
		return {
			add : function(_573) {
				var ss = [ "<style type=\"text/css\" easyui=\"true\">" ];
				for ( var i = 0; i < _573.length; i++) {
					_572.cache[_573[i][0]] = {
						width : _573[i][1]
					};
				}
				var _574 = 0;
				for ( var s in _572.cache) {
					var item = _572.cache[s];
					item.index = _574++;
					ss.push(s + "{width:" + item.width + "}");
				}
				ss.push("</style>");
				$(ss.join("\n")).appendTo(cc);
				cc.children("style[easyui]:not(:last)").remove();
			},
			getRule : function(_575) {
				var _576 = cc.children("style[easyui]:last")[0];
				var _577 = _576.styleSheet ? _576.styleSheet
						: (_576.sheet || document.styleSheets[document.styleSheets.length - 1]);
				var _578 = _577.cssRules || _577.rules;
				return _578[_575];
			},
			set : function(_579, _57a) {
				var item = _572.cache[_579];
				if (item) {
					item.width = _57a;
					var rule = this.getRule(item.index);
					if (rule) {
						rule.style["width"] = _57a;
					}
				}
			},
			remove : function(_57b) {
				var tmp = [];
				for ( var s in _572.cache) {
					if (s.indexOf(_57b) == -1) {
						tmp.push([ s, _572.cache[s].width ]);
					}
				}
				_572.cache = {};
				this.add(tmp);
			},
			dirty : function(_57c) {
				if (_57c) {
					_572.dirty.push(_57c);
				}
			},
			clean : function() {
				for ( var i = 0; i < _572.dirty.length; i++) {
					this.remove(_572.dirty[i]);
				}
				_572.dirty = [];
			}
		};
	}
	;
	function _57d(_57e, _57f) {
		var _580 = $.data(_57e, "datagrid");
		var opts = _580.options;
		var _581 = _580.panel;
		if (_57f) {
			$.extend(opts, _57f);
		}
		if (opts.fit == true) {
			var p = _581.panel("panel").parent();
			opts.width = p.width();
			opts.height = p.height();
		}
		_581.panel("resize", opts);
	}
	;
	function _582(_583) {
		var _584 = $.data(_583, "datagrid");
		var opts = _584.options;
		var dc = _584.dc;
		var wrap = _584.panel;
		var _585 = wrap.width();
		var _586 = wrap.height();
		var view = dc.view;
		var _587 = dc.view1;
		var _588 = dc.view2;
		var _589 = _587.children("div.datagrid-header");
		var _58a = _588.children("div.datagrid-header");
		var _58b = _589.find("table");
		var _58c = _58a.find("table");
		view.width(_585);
		var _58d = _589.children("div.datagrid-header-inner").show();
		_587.width(_58d.find("table").width());
		if (!opts.showHeader) {
			_58d.hide();
		}
		_588.width(_585 - _587._outerWidth());
		_587.children(
				"div.datagrid-header,div.datagrid-body,div.datagrid-footer")
				.width(_587.width());
		_588.children(
				"div.datagrid-header,div.datagrid-body,div.datagrid-footer")
				.width(_588.width());
		var hh;
		_589.add(_58a).css("height", "");
		_58b.add(_58c).css("height", "");
		hh = Math.max(_58b.height(), _58c.height());
		_58b.add(_58c).height(hh);
		_589.add(_58a)._outerHeight(hh);
		dc.body1.add(dc.body2).children("table.datagrid-btable-frozen").css({
			position : "absolute",
			top : dc.header2._outerHeight()
		});
		var _58e = dc.body2.children("table.datagrid-btable-frozen")
				._outerHeight();
		var _58f = _58e + _588.children("div.datagrid-header")._outerHeight()
				+ _588.children("div.datagrid-footer")._outerHeight()
				+ wrap.children("div.datagrid-toolbar")._outerHeight();
		wrap.children("div.datagrid-pager").each(function() {
			_58f += $(this)._outerHeight();
		});
		var _590 = wrap.outerHeight() - wrap.height();
		var _591 = wrap._size("minHeight") || "";
		var _592 = wrap._size("maxHeight") || "";
		_587.add(_588).children("div.datagrid-body").css({
			marginTop : _58e,
			height : (isNaN(parseInt(opts.height)) ? "" : (_586 - _58f)),
			minHeight : (_591 ? _591 - _590 - _58f : ""),
			maxHeight : (_592 ? _592 - _590 - _58f : "")
		});
		view.height(_588.height());
	}
	;
	function _593(_594, _595, _596) {
		var rows = $.data(_594, "datagrid").data.rows;
		var opts = $.data(_594, "datagrid").options;
		var dc = $.data(_594, "datagrid").dc;
		if (!dc.body1.is(":empty")
				&& (!opts.nowrap || opts.autoRowHeight || _596)) {
			if (_595 != undefined) {
				var tr1 = opts.finder.getTr(_594, _595, "body", 1);
				var tr2 = opts.finder.getTr(_594, _595, "body", 2);
				_597(tr1, tr2);
			} else {
				var tr1 = opts.finder.getTr(_594, 0, "allbody", 1);
				var tr2 = opts.finder.getTr(_594, 0, "allbody", 2);
				_597(tr1, tr2);
				if (opts.showFooter) {
					var tr1 = opts.finder.getTr(_594, 0, "allfooter", 1);
					var tr2 = opts.finder.getTr(_594, 0, "allfooter", 2);
					_597(tr1, tr2);
				}
			}
		}
		_582(_594);
		if (opts.height == "auto") {
			var _598 = dc.body1.parent();
			var _599 = dc.body2;
			var _59a = _59b(_599);
			var _59c = _59a.height;
			if (_59a.width > _599.width()) {
				_59c += 18;
			}
			_59c -= parseInt(_599.css("marginTop")) || 0;
			_598.height(_59c);
			_599.height(_59c);
			dc.view.height(dc.view2.height());
		}
		dc.body2.triggerHandler("scroll");
		function _597(trs1, trs2) {
			for ( var i = 0; i < trs2.length; i++) {
				var tr1 = $(trs1[i]);
				var tr2 = $(trs2[i]);
				tr1.css("height", "");
				tr2.css("height", "");
				var _59d = Math.max(tr1.height(), tr2.height());
				tr1.css("height", _59d);
				tr2.css("height", _59d);
			}
		}
		;
		function _59b(cc) {
			var _59e = 0;
			var _59f = 0;
			$(cc).children().each(function() {
				var c = $(this);
				if (c.is(":visible")) {
					_59f += c._outerHeight();
					if (_59e < c._outerWidth()) {
						_59e = c._outerWidth();
					}
				}
			});
			return {
				width : _59e,
				height : _59f
			};
		}
		;
	}
	;
	function _5a0(_5a1, _5a2) {
		var _5a3 = $.data(_5a1, "datagrid");
		var opts = _5a3.options;
		var dc = _5a3.dc;
		if (!dc.body2.children("table.datagrid-btable-frozen").length) {
			dc.body1
					.add(dc.body2)
					.prepend(
							"<table class=\"datagrid-btable datagrid-btable-frozen\" cellspacing=\"0\" cellpadding=\"0\"></table>");
		}
		_5a4(true);
		_5a4(false);
		_582(_5a1);
		function _5a4(_5a5) {
			var _5a6 = _5a5 ? 1 : 2;
			var tr = opts.finder.getTr(_5a1, _5a2, "body", _5a6);
			(_5a5 ? dc.body1 : dc.body2).children(
					"table.datagrid-btable-frozen").append(tr);
		}
		;
	}
	;
	function _5a7(_5a8, _5a9) {
		function _5aa() {
			var _5ab = [];
			var _5ac = [];
			$(_5a8).children("thead").each(function() {
				var opt = $.parser.parseOptions(this, [{
					frozen : "boolean"
				}]);
				$(this).find("tr").each(function() {
					var cols = [];
					$(this).find("th").each(function() {
						var th = $(this);
						var col = $.extend({}, $.parser.parseOptions(this, [
								"field",
								"align",
								"halign",
								"order",
								"width",
								{
									sortable : "boolean",
									checkbox : "boolean",
									resizable : "boolean",
									fixed : "boolean"
								},
								{
									rowspan : "number",
									colspan : "number"
								}
							]),
							{
								title : (th.html() || undefined),
								hidden : (th.attr("hidden") ? true : undefined),
								formatter : (th.attr("formatter") ? eval(th.attr("formatter")) : undefined),
								styler : (th.attr("styler") ? eval(th.attr("styler")) : undefined),
								sorter : (th.attr("sorter") ? eval(th.attr("sorter")) : undefined)
							}
						);
						if (col.width && String(col.width).indexOf("%") == -1) {
							col.width = parseInt(col.width);
						}
						if (th.attr("editor")) {
							var s = $.trim(th.attr("editor"));
							if (s.substr(0, 1) == "{") {
								col.editor = eval("(" + s + ")");
							} else {
								col.editor = s;
							}
						}
						cols.push(col);
					});
					opt.frozen ? _5ab.push(cols) : _5ac.push(cols);
				});
			});
			return [ _5ab, _5ac ];
		}
		var _5ad = $(
				"<div class=\"datagrid-wrap\">"
						+ "<div class=\"datagrid-view\">"
						+ "<div class=\"datagrid-view1\">"
						+ "<div class=\"datagrid-header\">"
						+ "<div class=\"datagrid-header-inner\"></div>"
						+ "</div>" + "<div class=\"datagrid-body\">"
						+ "<div class=\"datagrid-body-inner\"></div>"
						+ "</div>" + "<div class=\"datagrid-footer\">"
						+ "<div class=\"datagrid-footer-inner\"></div>"
						+ "</div>" + "</div>"
						+ "<div class=\"datagrid-view2\">"
						+ "<div class=\"datagrid-header\">"
						+ "<div class=\"datagrid-header-inner\"></div>"
						+ "</div>" + "<div class=\"datagrid-body\"></div>"
						+ "<div class=\"datagrid-footer\">"
						+ "<div class=\"datagrid-footer-inner\"></div>"
						+ "</div>" + "</div>" + "</div>" + "</div>")
				.insertAfter(_5a8);
		_5ad.panel({
			doSize : false,
			cls : "datagrid"
		});
		$(_5a8).addClass("datagrid-f").hide().appendTo(
				_5ad.children("div.datagrid-view"));
		var cc = _5aa();
		var view = _5ad.children("div.datagrid-view");
		var _5ae = view.children("div.datagrid-view1");
		var _5af = view.children("div.datagrid-view2");
		return {
			panel : _5ad,
			frozenColumns : cc[0],
			columns : cc[1],
			dc : {
				view : view,
				view1 : _5ae,
				view2 : _5af,
				header1 : _5ae.children("div.datagrid-header").children(
						"div.datagrid-header-inner"),
				header2 : _5af.children("div.datagrid-header").children(
						"div.datagrid-header-inner"),
				body1 : _5ae.children("div.datagrid-body").children(
						"div.datagrid-body-inner"),
				body2 : _5af.children("div.datagrid-body"),
				footer1 : _5ae.children("div.datagrid-footer").children(
						"div.datagrid-footer-inner"),
				footer2 : _5af.children("div.datagrid-footer").children(
						"div.datagrid-footer-inner")
			}
		};
	}
	;
	function _5b0(_5b1) {
		var _5b2 = $.data(_5b1, "datagrid");
		var opts = _5b2.options;
		var dc = _5b2.dc;
		var _5b3 = _5b2.panel;
		_5b2.ss = $(_5b1).datagrid("createStyleSheet");
		_5b3.panel($.extend({}, opts, {
			id : null,
			doSize : false,
			onResize : function(_5b4, _5b5) {
				setTimeout(function() {
					if ($.data(_5b1, "datagrid")) {
						_582(_5b1);
						_5f7(_5b1);
						opts.onResize.call(_5b3, _5b4, _5b5);
					}
				}, 0);
			},
			onExpand : function() {
				_593(_5b1);
				opts.onExpand.call(_5b3);
			}
		}));
		_5b2.rowIdPrefix = "datagrid-row-r" + (++_569);
		_5b2.cellClassPrefix = "datagrid-cell-c" + _569;
		_5b6(dc.header1, opts.frozenColumns, true);
		_5b6(dc.header2, opts.columns, false);
		_5b7();
		dc.header1.add(dc.header2).css("display",
				opts.showHeader ? "block" : "none");
		dc.footer1.add(dc.footer2).css("display",
				opts.showFooter ? "block" : "none");
		if (opts.toolbar) {
			if ($.isArray(opts.toolbar)) {
				$("div.datagrid-toolbar", _5b3).remove();
				var tb = $(
						"<div class=\"datagrid-toolbar\"><table cellspacing=\"0\" cellpadding=\"0\"><tr></tr></table></div>")
						.prependTo(_5b3);
				var tr = tb.find("tr");
				for ( var i = 0; i < opts.toolbar.length; i++) {
					var btn = opts.toolbar[i];
					if (btn == "-") {
						$(
								"<td><div class=\"datagrid-btn-separator\"></div></td>")
								.appendTo(tr);
					} else {
						var td = $("<td></td>").appendTo(tr);
						var tool = $("<a href=\"javascript:void(0)\"></a>")
								.appendTo(td);
						tool[0].onclick = eval(btn.handler || function() {
						});
						tool.linkbutton($.extend({}, btn, {
							plain : true
						}));
					}
				}
			} else {
				$(opts.toolbar).addClass("datagrid-toolbar").prependTo(_5b3);
				$(opts.toolbar).show();
			}
		} else {
			$("div.datagrid-toolbar", _5b3).remove();
		}
		$("div.datagrid-pager", _5b3).remove();
		if (opts.pagination) {
			var _5b8 = $("<div class=\"datagrid-pager\"></div>");
			if (opts.pagePosition == "bottom") {
				_5b8.appendTo(_5b3);
			} else {
				if (opts.pagePosition == "top") {
					_5b8.addClass("datagrid-pager-top").prependTo(_5b3);
				} else {
					var ptop = $(
							"<div class=\"datagrid-pager datagrid-pager-top\"></div>")
							.prependTo(_5b3);
					_5b8.appendTo(_5b3);
					_5b8 = _5b8.add(ptop);
				}
			}
			_5b8.pagination({
				total : (opts.pageNumber * opts.pageSize),
				pageNumber : opts.pageNumber,
				pageSize : opts.pageSize,
				pageList : opts.pageList,
				onSelectPage : function(_5b9, _5ba) {
					opts.pageNumber = _5b9 || 1;
					opts.pageSize = _5ba;
					_5b8.pagination("refresh", {
						pageNumber : _5b9,
						pageSize : _5ba
					});
					_5f5(_5b1);
				}
			});
			opts.pageSize = _5b8.pagination("options").pageSize;
		}
		function _5b6(_5bb, _5bc, _5bd) {
			if (!_5bc) {
				return;
			}
			$(_5bb).show();
			$(_5bb).empty();
			var _5be = [];
			var _5bf = [];
			if (opts.sortName) {
				_5be = opts.sortName.split(",");
				_5bf = opts.sortOrder.split(",");
			}
			var t = $(
					"<table class=\"datagrid-htable\" border=\"0\" cellspacing=\"0\" cellpadding=\"0\"><tbody></tbody></table>")
					.appendTo(_5bb);
			for ( var i = 0; i < _5bc.length; i++) {
				var tr = $("<tr class=\"datagrid-header-row\"></tr>").appendTo(
						$("tbody", t));
				var cols = _5bc[i];
				for ( var j = 0; j < cols.length; j++) {
					var col = cols[j];
					var attr = "";
					if (col.rowspan) {
						attr += "rowspan=\"" + col.rowspan + "\" ";
					}
					if (col.colspan) {
						attr += "colspan=\"" + col.colspan + "\" ";
					}
					var td = $("<td " + attr + "></td>").appendTo(tr);
					if (col.checkbox) {
						td.attr("field", col.field);
						$("<div class=\"datagrid-header-check\"></div>").html(
								"<input type=\"checkbox\"/>").appendTo(td);
					} else {
						if (col.field) {
							td.attr("field", col.field);
							td
									.append("<div class=\"datagrid-cell\"><span></span><span class=\"datagrid-sort-icon\"></span></div>");
							$("span", td).html(col.title);
							// $("span.datagrid-sort-icon",td).html("&nbsp;");
							$("span.datagrid-sort-icon", td).html("");// 去掉datagrid表头多余空白
							var cell = td.find("div.datagrid-cell");
							var pos = _56a(_5be, col.field);
							if (pos >= 0) {
								cell.addClass("datagrid-sort-" + _5bf[pos]);
							}
							if (col.resizable == false) {
								cell.attr("resizable", "false");
							}
							if (col.width) {
								var _5c0 = $.parser.parseValue("width",
										col.width, dc.view, opts.scrollbarSize);
								cell._outerWidth(_5c0 - 1);
								col.boxWidth = parseInt(cell[0].style.width);
								col.deltaWidth = _5c0 - col.boxWidth;
							} else {
								col.auto = true;
							}
							cell.css("text-align",
									(col.halign || col.align || ""));
							col.cellClass = _5b2.cellClassPrefix + "-"
									+ col.field.replace(/[\.|\s]/g, "-");
							cell.addClass(col.cellClass).css("width", "");
						} else {
							$("<div class=\"datagrid-cell-group\"></div>").html(col.title).appendTo(td);
						}
					}
					if (col.hidden) {
						td.hide();
					}
				}
			}
			if (_5bd && opts.rownumbers) {
				var td = $("<td rowspan=\""
						+ opts.frozenColumns.length
						+ "\"><div class=\"datagrid-header-rownumber\"></div></td>");
				if ($("tr", t).length == 0) {
					td.wrap("<tr class=\"datagrid-header-row\"></tr>").parent()
							.appendTo($("tbody", t));
				} else {
					td.prependTo($("tr:first", t));
				}
			}
		}
		;
		function _5b7() {
			var _5c1 = [];
			var _5c2 = _5c3(_5b1, true).concat(_5c3(_5b1));
			for ( var i = 0; i < _5c2.length; i++) {
				var col = _5c4(_5b1, _5c2[i]);
				if (col && !col.checkbox) {
					_5c1.push([ "." + col.cellClass,
							col.boxWidth ? col.boxWidth + "px" : "auto" ]);
				}
			}
			_5b2.ss.add(_5c1);
			_5b2.ss.dirty(_5b2.cellSelectorPrefix);
			_5b2.cellSelectorPrefix = "." + _5b2.cellClassPrefix;
		}
		;
	}
	;
	function _5c5(_5c6) {
		var _5c7 = $.data(_5c6, "datagrid");
		var _5c8 = _5c7.panel;
		var opts = _5c7.options;
		var dc = _5c7.dc;
		var _5c9 = dc.header1.add(dc.header2);
		_5c9.find("input[type=checkbox]").unbind(".datagrid").bind(
				"click.datagrid", function(e) {
					if (opts.singleSelect && opts.selectOnCheck) {
						return false;
					}
					if ($(this).is(":checked")) {
						_65d(_5c6);
					} else {
						_663(_5c6);
					}
					e.stopPropagation();
				});
		var _5ca = _5c9.find("div.datagrid-cell");
		_5ca.closest("td").unbind(".datagrid").bind("mouseenter.datagrid",
				function() {
					if (_5c7.resizing) {
						return;
					}
					$(this).addClass("datagrid-header-over");
				}).bind("mouseleave.datagrid", function() {
			$(this).removeClass("datagrid-header-over");
		}).bind("contextmenu.datagrid", function(e) {
			var _5cb = $(this).attr("field");
			opts.onHeaderContextMenu.call(_5c6, e, _5cb);
		});
		_5ca.unbind(".datagrid").bind("click.datagrid", function(e) {
			var p1 = $(this).offset().left + 5;
			var p2 = $(this).offset().left + $(this)._outerWidth() - 5;
			if (e.pageX < p2 && e.pageX > p1) {
				_5ea(_5c6, $(this).parent().attr("field"));
			}
		}).bind(
				"dblclick.datagrid",
				function(e) {
					var p1 = $(this).offset().left + 5;
					var p2 = $(this).offset().left + $(this)._outerWidth() - 5;
					var cond = opts.resizeHandle == "right" ? (e.pageX > p2)
							: (opts.resizeHandle == "left" ? (e.pageX < p1)
									: (e.pageX < p1 || e.pageX > p2));
					if (cond) {
						var _5cc = $(this).parent().attr("field");
						var col = _5c4(_5c6, _5cc);
						if (col.resizable == false) {
							return;
						}
						$(_5c6).datagrid("autoSizeColumn", _5cc);
						col.auto = false;
					}
				});
		var _5cd = opts.resizeHandle == "right" ? "e"
				: (opts.resizeHandle == "left" ? "w" : "e,w");
		_5ca
				.each(function() {
					$(this)
							.resizable(
									{
										handles : _5cd,
										disabled : ($(this).attr("resizable") ? $(
												this).attr("resizable") == "false"
												: false),
										minWidth : 25,
										onStartResize : function(e) {
											_5c7.resizing = true;
											_5c9.css("cursor", $("body").css(
													"cursor"));
											if (!_5c7.proxy) {
												_5c7.proxy = $(
														"<div class=\"datagrid-resize-proxy\"></div>")
														.appendTo(dc.view);
											}
											_5c7.proxy.css({
												left : e.pageX
														- $(_5c8).offset().left
														- 1,
												display : "none"
											});
											setTimeout(function() {
												if (_5c7.proxy) {
													_5c7.proxy.show();
												}
											}, 500);
										},
										onResize : function(e) {
											_5c7.proxy.css({
												left : e.pageX
														- $(_5c8).offset().left
														- 1,
												display : "block"
											});
											return false;
										},
										onStopResize : function(e) {
											_5c9.css("cursor", "");
											$(this).css("height", "");
											var _5ce = $(this).parent().attr(
													"field");
											var col = _5c4(_5c6, _5ce);
											col.width = $(this)._outerWidth();
											col.boxWidth = col.width
													- col.deltaWidth;
											col.auto = undefined;
											$(this).css("width", "");
											_613(_5c6, _5ce);
											_5c7.proxy.remove();
											_5c7.proxy = null;
											if ($(this)
													.parents(
															"div:first.datagrid-header")
													.parent().hasClass(
															"datagrid-view1")) {
												_582(_5c6);
											}
											_5f7(_5c6);
											opts.onResizeColumn.call(_5c6,
													_5ce, col.width);
											setTimeout(function() {
												_5c7.resizing = false;
											}, 0);
										}
									});
				});
		var bb = dc.body1.add(dc.body2);
		bb.unbind();
		for ( var _5cf in opts.rowEvents) {
			bb.bind(_5cf, opts.rowEvents[_5cf]);
		}
		dc.body1.bind("mousewheel DOMMouseScroll", function(e) {
			var e1 = e.originalEvent || window.event;
			var _5d0 = e1.wheelDelta || e1.detail * (-1);
			var dg = $(e.target).closest("div.datagrid-view").children(
					".datagrid-f");
			var dc = dg.data("datagrid").dc;
			dc.body2.scrollTop(dc.body2.scrollTop() - _5d0);
		});
		dc.body2.bind("scroll", function() {
			var b1 = dc.view1.children("div.datagrid-body");
			b1.scrollTop($(this).scrollTop());
			var c1 = dc.body1.children(":first");
			var c2 = dc.body2.children(":first");
			if (c1.length && c2.length) {
				var top1 = c1.offset().top;
				var top2 = c2.offset().top;
				if (top1 != top2) {
					b1.scrollTop(b1.scrollTop() + top1 - top2);
				}
			}
			dc.view2.children("div.datagrid-header,div.datagrid-footer")
					._scrollLeft($(this)._scrollLeft());
			dc.body2.children("table.datagrid-btable-frozen").css("left",
					-$(this)._scrollLeft());
		});
	}
	;
	function _5d1(_5d2) {
		return function(e) {
			var tr = _5d3(e.target);
			if (!tr) {
				return;
			}
			var _5d4 = _5d5(tr);
			if ($.data(_5d4, "datagrid").resizing) {
				return;
			}
			var _5d6 = _5d7(tr);
			if (_5d2) {
				_5d8(_5d4, _5d6);
			} else {
				var opts = $.data(_5d4, "datagrid").options;
				opts.finder.getTr(_5d4, _5d6).removeClass("datagrid-row-over");
			}
		};
	}
	;
	function _5d9(e) {
		var tr = _5d3(e.target);
		if (!tr) {
			return;
		}
		var _5da = _5d5(tr);
		var opts = $.data(_5da, "datagrid").options;
		var _5db = _5d7(tr);
		var tt = $(e.target);
		if (tt.parent().hasClass("datagrid-cell-check")) {
			if (opts.singleSelect && opts.selectOnCheck) {
				tt._propAttr("checked", !tt.is(":checked"));
				_5dc(_5da, _5db);
			} else {
				if (tt.is(":checked")) {
					tt._propAttr("checked", false);
					_5dc(_5da, _5db);
				} else {
					tt._propAttr("checked", true);
					_5dd(_5da, _5db);
				}
			}
		} else {
			var row = opts.finder.getRow(_5da, _5db);
			var td = tt.closest("td[field]", tr);
			if (td.length) {
				var _5de = td.attr("field");
				opts.onClickCell.call(_5da, _5db, _5de, row[_5de]);
			}
			if (opts.singleSelect == true) {
				_5df(_5da, _5db);
			} else {
				if (opts.ctrlSelect) {
					if (e.ctrlKey) {
						if (tr.hasClass("datagrid-row-selected")) {
							_5e0(_5da, _5db);
						} else {
							_5df(_5da, _5db);
						}
					} else {
						if (e.shiftKey) {
							$(_5da).datagrid("clearSelections");
							var _5e1 = Math.min(opts.lastSelectedIndex || 0,
									_5db);
							var _5e2 = Math.max(opts.lastSelectedIndex || 0,
									_5db);
							for ( var i = _5e1; i <= _5e2; i++) {
								_5df(_5da, i);
							}
						} else {
							$(_5da).datagrid("clearSelections");
							_5df(_5da, _5db);
							opts.lastSelectedIndex = _5db;
						}
					}
				} else {
					if (tr.hasClass("datagrid-row-selected")) {
						_5e0(_5da, _5db);
					} else {
						_5df(_5da, _5db);
					}
				}
			}
			opts.onClickRow.call(_5da, _5db, row);
		}
	}
	;
	function _5e3(e) {
		var tr = _5d3(e.target);
		if (!tr) {
			return;
		}
		var _5e4 = _5d5(tr);
		var opts = $.data(_5e4, "datagrid").options;
		var _5e5 = _5d7(tr);
		var row = opts.finder.getRow(_5e4, _5e5);
		var td = $(e.target).closest("td[field]", tr);
		if (td.length) {
			var _5e6 = td.attr("field");
			opts.onDblClickCell.call(_5e4, _5e5, _5e6, row[_5e6]);
		}
		opts.onDblClickRow.call(_5e4, _5e5, row);
	}
	;
	function _5e7(e) {
		var tr = _5d3(e.target);
		if (!tr) {
			return;
		}
		var _5e8 = _5d5(tr);
		var opts = $.data(_5e8, "datagrid").options;
		var _5e9 = _5d7(tr);
		var row = opts.finder.getRow(_5e8, _5e9);
		opts.onRowContextMenu.call(_5e8, e, _5e9, row);
	}
	;
	function _5d5(t) {
		return $(t).closest("div.datagrid-view").children(".datagrid-f")[0];
	}
	;
	function _5d3(t) {
		var tr = $(t).closest("tr.datagrid-row");
		if (tr.length && tr.parent().length) {
			return tr;
		} else {
			return undefined;
		}
	}
	;
	function _5d7(tr) {
		if (tr.attr("datagrid-row-index")) {
			return parseInt(tr.attr("datagrid-row-index"));
		} else {
			return tr.attr("node-id");
		}
	}
	;
	function _5ea(_5eb, _5ec) {
		var _5ed = $.data(_5eb, "datagrid");
		var opts = _5ed.options;
		_5ec = _5ec || {};
		var _5ee = {
			sortName : opts.sortName,
			sortOrder : opts.sortOrder
		};
		if (typeof _5ec == "object") {
			$.extend(_5ee, _5ec);
		}
		var _5ef = [];
		var _5f0 = [];
		if (_5ee.sortName) {
			_5ef = _5ee.sortName.split(",");
			_5f0 = _5ee.sortOrder.split(",");
		}
		if (typeof _5ec == "string") {
			var _5f1 = _5ec;
			var col = _5c4(_5eb, _5f1);
			if (!col.sortable || _5ed.resizing) {
				return;
			}
			var _5f2 = col.order || "asc";
			var pos = _56a(_5ef, _5f1);
			if (pos >= 0) {
				var _5f3 = _5f0[pos] == "asc" ? "desc" : "asc";
				if (opts.multiSort && _5f3 == _5f2) {
					_5ef.splice(pos, 1);
					_5f0.splice(pos, 1);
				} else {
					_5f0[pos] = _5f3;
				}
			} else {
				if (opts.multiSort) {
					_5ef.push(_5f1);
					_5f0.push(_5f2);
				} else {
					_5ef = [ _5f1 ];
					_5f0 = [ _5f2 ];
				}
			}
			_5ee.sortName = _5ef.join(",");
			_5ee.sortOrder = _5f0.join(",");
		}
		if (opts.onBeforeSortColumn.call(_5eb, _5ee.sortName, _5ee.sortOrder) == false) {
			return;
		}
		$.extend(opts, _5ee);
		var dc = _5ed.dc;
		var _5f4 = dc.header1.add(dc.header2);
		_5f4.find("div.datagrid-cell").removeClass(
				"datagrid-sort-asc datagrid-sort-desc");
		for ( var i = 0; i < _5ef.length; i++) {
			var col = _5c4(_5eb, _5ef[i]);
			_5f4.find("div." + col.cellClass).addClass(
					"datagrid-sort-" + _5f0[i]);
		}
		if (opts.remoteSort) {
			_5f5(_5eb);
		} else {
			_5f6(_5eb, $(_5eb).datagrid("getData"));
		}
		opts.onSortColumn.call(_5eb, opts.sortName, opts.sortOrder);
	}
	;
	function _5f7(_5f8) {
		var _5f9 = $.data(_5f8, "datagrid");
		var opts = _5f9.options;
		var dc = _5f9.dc;
		var _5fa = dc.view2.children("div.datagrid-header");
		dc.body2.css("overflow-x", "");
		_5fb();
		_5fc();
		if (_5fa.width() >= _5fa.find("table").width()) {
			dc.body2.css("overflow-x", "hidden");
		}
		function _5fc() {
			if (!opts.fitColumns) {
				return;
			}
			if (!_5f9.leftWidth) {
				_5f9.leftWidth = 0;
			}
			var _5fd = 0;
			var cc = [];
			var _5fe = _5c3(_5f8, false);
			for ( var i = 0; i < _5fe.length; i++) {
				var col = _5c4(_5f8, _5fe[i]);
				if (_5ff(col)) {
					_5fd += col.width;
					cc.push({
						field : col.field,
						col : col,
						addingWidth : 0
					});
				}
			}
			if (!_5fd) {
				return;
			}
			cc[cc.length - 1].addingWidth -= _5f9.leftWidth;
			var _600 = _5fa.children("div.datagrid-header-inner").show();
			var _601 = _5fa.width() - _5fa.find("table").width()
					- opts.scrollbarSize + _5f9.leftWidth;
			var rate = _601 / _5fd;
			if (!opts.showHeader) {
				_600.hide();
			}
			for ( var i = 0; i < cc.length; i++) {
				var c = cc[i];
				var _602 = parseInt(c.col.width * rate);
				c.addingWidth += _602;
				_601 -= _602;
			}
			cc[cc.length - 1].addingWidth += _601;
			for ( var i = 0; i < cc.length; i++) {
				var c = cc[i];
				if (c.col.boxWidth + c.addingWidth > 0) {
					c.col.boxWidth += c.addingWidth;
					c.col.width += c.addingWidth;
				}
			}
			_5f9.leftWidth = _601;
			_613(_5f8);
		}
		;
		function _5fb() {
			var _603 = false;
			var _604 = _5c3(_5f8, true).concat(_5c3(_5f8, false));
			$.map(_604, function(_605) {
				var col = _5c4(_5f8, _605);
				if (String(col.width || "").indexOf("%") >= 0) {
					var _606 = $.parser.parseValue("width", col.width, dc.view,
							opts.scrollbarSize)
							- col.deltaWidth;
					if (_606 > 0) {
						col.boxWidth = _606;
						_603 = true;
					}
				}
			});
			if (_603) {
				_613(_5f8);
			}
		}
		;
		function _5ff(col) {
			if (String(col.width || "").indexOf("%") >= 0) {
				return false;
			}
			if (!col.hidden && !col.checkbox && !col.auto && !col.fixed) {
				return true;
			}
		}
		;
	}
	;
	function _607(_608, _609) {
		var _60a = $.data(_608, "datagrid");
		var opts = _60a.options;
		var dc = _60a.dc;
		var tmp = $(
				"<div class=\"datagrid-cell\" style=\"position:absolute;left:-9999px\"></div>")
				.appendTo("body");
		if (_609) {
			_57d(_609);
			if (opts.fitColumns) {
				_582(_608);
				_5f7(_608);
			}
		} else {
			var _60b = false;
			var _60c = _5c3(_608, true).concat(_5c3(_608, false));
			for ( var i = 0; i < _60c.length; i++) {
				var _609 = _60c[i];
				var col = _5c4(_608, _609);
				if (col.auto) {
					_57d(_609);
					_60b = true;
				}
			}
			if (_60b && opts.fitColumns) {
				_582(_608);
				_5f7(_608);
			}
		}
		tmp.remove();
		function _57d(_60d) {
			var _60e = dc.view.find("div.datagrid-header td[field=\"" + _60d
					+ "\"] div.datagrid-cell");
			_60e.css("width", "");
			var col = $(_608).datagrid("getColumnOption", _60d);
			col.width = undefined;
			col.boxWidth = undefined;
			col.auto = true;
			$(_608).datagrid("fixColumnSize", _60d);
			var _60f = Math.max(_610("header"), _610("allbody"),
					_610("allfooter")) + 1;
			_60e._outerWidth(_60f - 1);
			col.width = _60f;
			col.boxWidth = parseInt(_60e[0].style.width);
			col.deltaWidth = _60f - col.boxWidth;
			_60e.css("width", "");
			$(_608).datagrid("fixColumnSize", _60d);
			opts.onResizeColumn.call(_608, _60d, col.width);
			function _610(type) {
				var _611 = 0;
				if (type == "header") {
					_611 = _612(_60e);
				} else {
					opts.finder.getTr(_608, 0, type).find(
							"td[field=\"" + _60d + "\"] div.datagrid-cell")
							.each(function() {
								var w = _612($(this));
								if (_611 < w) {
									_611 = w;
								}
							});
				}
				return _611;
				function _612(cell) {
					return cell.is(":visible") ? cell._outerWidth() : tmp.html(
							cell.html())._outerWidth();
				}
				;
			}
			;
		}
		;
	}
	;
	function _613(_614, _615) {
		var _616 = $.data(_614, "datagrid");
		var opts = _616.options;
		var dc = _616.dc;
		var _617 = dc.view.find("table.datagrid-btable,table.datagrid-ftable");
		_617.css("table-layout", "fixed");
		if (_615) {
			fix(_615);
		} else {
			var ff = _5c3(_614, true).concat(_5c3(_614, false));
			for ( var i = 0; i < ff.length; i++) {
				fix(ff[i]);
			}
		}
		_617.css("table-layout", "auto");
		_618(_614);
		_593(_614);
		_619(_614);
		function fix(_61a) {
			var col = _5c4(_614, _61a);
			if (col.cellClass) {
				_616.ss.set("." + col.cellClass, col.boxWidth ? col.boxWidth
						+ "px" : "auto");
			}
		}
		;
	}
	;
	function _618(_61b) {
		var dc = $.data(_61b, "datagrid").dc;
		dc.view.find("td.datagrid-td-merged").each(function() {
			var td = $(this);
			var _61c = td.attr("colspan") || 1;
			var col = _5c4(_61b, td.attr("field"));
			var _61d = col.boxWidth + col.deltaWidth - 1;
			for ( var i = 1; i < _61c; i++) {
				td = td.next();
				col = _5c4(_61b, td.attr("field"));
				_61d += col.boxWidth + col.deltaWidth;
			}
			$(this).children("div.datagrid-cell")._outerWidth(_61d);
		});
	}
	;
	function _619(_61e) {
		var dc = $.data(_61e, "datagrid").dc;
		dc.view.find("div.datagrid-editable").each(function() {
			var cell = $(this);
			var _61f = cell.parent().attr("field");
			var col = $(_61e).datagrid("getColumnOption", _61f);
			cell._outerWidth(col.boxWidth + col.deltaWidth - 1);
			var ed = $.data(this, "datagrid.editor");
			if (ed.actions.resize) {
				ed.actions.resize(ed.target, cell.width());
			}
		});
	}
	;
	function _5c4(_620, _621) {
		function find(_622) {
			if (_622) {
				for ( var i = 0; i < _622.length; i++) {
					var cc = _622[i];
					for ( var j = 0; j < cc.length; j++) {
						var c = cc[j];
						if (c.field == _621) {
							return c;
						}
					}
				}
			}
			return null;
		}
		;
		var opts = $.data(_620, "datagrid").options;
		var col = find(opts.columns);
		if (!col) {
			col = find(opts.frozenColumns);
		}
		return col;
	}
	;
	function _5c3(_623, _624) {
		var opts = $.data(_623, "datagrid").options;
		var _625 = (_624 == true) ? (opts.frozenColumns || [ [] ])
				: opts.columns;
		if (_625.length == 0) {
			return [];
		}
		var aa = [];
		var _626 = _627();
		for ( var i = 0; i < _625.length; i++) {
			aa[i] = new Array(_626);
		}
		for ( var _628 = 0; _628 < _625.length; _628++) {
			$.map(_625[_628], function(col) {
				var _629 = _62a(aa[_628]);
				if (_629 >= 0) {
					var _62b = col.field || "";
					for ( var c = 0; c < (col.colspan || 1); c++) {
						for ( var r = 0; r < (col.rowspan || 1); r++) {
							aa[_628 + r][_629] = _62b;
						}
						_629++;
					}
				}
			});
		}
		return aa[aa.length - 1];
		function _627() {
			var _62c = 0;
			$.map(_625[0], function(col) {
				_62c += col.colspan || 1;
			});
			return _62c;
		}
		;
		function _62a(a) {
			for ( var i = 0; i < a.length; i++) {
				if (a[i] == undefined) {
					return i;
				}
			}
			return -1;
		}
		;
	}
	;
	function _5f6(_62d, data) {
		var _62e = $.data(_62d, "datagrid");
		var opts = _62e.options;
		var dc = _62e.dc;
		data = opts.loadFilter.call(_62d, data);
		data.total = parseInt(data.total);
		_62e.data = data;
		if (data.footer) {
			_62e.footer = data.footer;
		}
		if (!opts.remoteSort && opts.sortName) {
			var _62f = opts.sortName.split(",");
			var _630 = opts.sortOrder.split(",");
			data.rows.sort(function(r1, r2) {
				var r = 0;
				for ( var i = 0; i < _62f.length; i++) {
					var sn = _62f[i];
					var so = _630[i];
					var col = _5c4(_62d, sn);
					var _631 = col.sorter || function(a, b) {
						return a == b ? 0 : (a > b ? 1 : -1);
					};
					r = _631(r1[sn], r2[sn]) * (so == "asc" ? 1 : -1);
					if (r != 0) {
						return r;
					}
				}
				return r;
			});
		}
		if (opts.view.onBeforeRender) {
			opts.view.onBeforeRender.call(opts.view, _62d, data.rows);
		}
		opts.view.render.call(opts.view, _62d, dc.body2, false);
		opts.view.render.call(opts.view, _62d, dc.body1, true);
		if (opts.showFooter) {
			opts.view.renderFooter.call(opts.view, _62d, dc.footer2, false);
			opts.view.renderFooter.call(opts.view, _62d, dc.footer1, true);
		}
		if (opts.view.onAfterRender) {
			opts.view.onAfterRender.call(opts.view, _62d);
		}
		_62e.ss.clean();
		var _632 = $(_62d).datagrid("getPager");
		if (_632.length) {
			var _633 = _632.pagination("options");
			if (_633.total != data.total) {
				_632.pagination("refresh", {
					total : data.total
				});
				if (opts.pageNumber != _633.pageNumber && _633.pageNumber > 0) {
					opts.pageNumber = _633.pageNumber;
					_5f5(_62d);
				}
			}
		}
		_593(_62d);
		dc.body2.triggerHandler("scroll");
		$(_62d).datagrid("setSelectionState");
		$(_62d).datagrid("autoSizeColumn");
		opts.onLoadSuccess.call(_62d, data);
	}
	;
	function _634(_635) {
		var _636 = $.data(_635, "datagrid");
		var opts = _636.options;
		var dc = _636.dc;
		dc.header1.add(dc.header2).find("input[type=checkbox]")._propAttr(
				"checked", false);
		if (opts.idField) {
			var _637 = $.data(_635, "treegrid") ? true : false;
			var _638 = opts.onSelect;
			var _639 = opts.onCheck;
			opts.onSelect = opts.onCheck = function() {
			};
			var rows = opts.finder.getRows(_635);
			for ( var i = 0; i < rows.length; i++) {
				var row = rows[i];
				var _63a = _637 ? row[opts.idField] : i;
				if (_63b(_636.selectedRows, row)) {
					_5df(_635, _63a, true);
				}
				if (_63b(_636.checkedRows, row)) {
					_5dc(_635, _63a, true);
				}
			}
			opts.onSelect = _638;
			opts.onCheck = _639;
		}
		function _63b(a, r) {
			for ( var i = 0; i < a.length; i++) {
				if (a[i][opts.idField] == r[opts.idField]) {
					a[i] = r;
					return true;
				}
			}
			return false;
		}
		;
	}
	;
	function _63c(_63d, row) {
		var _63e = $.data(_63d, "datagrid");
		var opts = _63e.options;
		var rows = _63e.data.rows;
		if (typeof row == "object") {
			return _56a(rows, row);
		} else {
			for ( var i = 0; i < rows.length; i++) {
				if (rows[i][opts.idField] == row) {
					return i;
				}
			}
			return -1;
		}
	}
	;
	function _63f(_640) {
		var _641 = $.data(_640, "datagrid");
		var opts = _641.options;
		var data = _641.data;
		if (opts.idField) {
			return _641.selectedRows;
		} else {
			var rows = [];
			opts.finder.getTr(_640, "", "selected", 2).each(function() {
				rows.push(opts.finder.getRow(_640, $(this)));
			});
			return rows;
		}
	}
	;
	function _642(_643) {
		var _644 = $.data(_643, "datagrid");
		var opts = _644.options;
		if (opts.idField) {
			return _644.checkedRows;
		} else {
			var rows = [];
			opts.finder.getTr(_643, "", "checked", 2).each(function() {
				rows.push(opts.finder.getRow(_643, $(this)));
			});
			return rows;
		}
	}
	;
	function _645(_646, _647) {
		var _648 = $.data(_646, "datagrid");
		var dc = _648.dc;
		var opts = _648.options;
		var tr = opts.finder.getTr(_646, _647);
		if (tr.length) {
			if (tr.closest("table").hasClass("datagrid-btable-frozen")) {
				return;
			}
			var _649 = dc.view2.children("div.datagrid-header")._outerHeight();
			var _64a = dc.body2;
			var _64b = _64a.outerHeight(true) - _64a.outerHeight();
			var top = tr.position().top - _649 - _64b;
			if (top < 0) {
				_64a.scrollTop(_64a.scrollTop() + top);
			} else {
				if (top + tr._outerHeight() > _64a.height() - 18) {
					_64a.scrollTop(_64a.scrollTop() + top + tr._outerHeight()
							- _64a.height() + 18);
				}
			}
		}
	}
	;
	function _5d8(_64c, _64d) {
		var _64e = $.data(_64c, "datagrid");
		var opts = _64e.options;
		opts.finder.getTr(_64c, _64e.highlightIndex).removeClass(
				"datagrid-row-over");
		opts.finder.getTr(_64c, _64d).addClass("datagrid-row-over");
		_64e.highlightIndex = _64d;
	}
	;
	function _5df(_64f, _650, _651) {
		var _652 = $.data(_64f, "datagrid");
		var opts = _652.options;
		var row = opts.finder.getRow(_64f, _650);
		if (opts.onBeforeSelect.call(_64f, _650, row) == false) {
			return;
		}
		if (opts.singleSelect) {
			_653(_64f, true);
			_652.selectedRows = [];
		}
		if (!_651 && opts.checkOnSelect) {
			_5dc(_64f, _650, true);
		}
		if (opts.idField) {
			_56d(_652.selectedRows, opts.idField, row);
		}
		opts.finder.getTr(_64f, _650).addClass("datagrid-row-selected");
		opts.onSelect.call(_64f, _650, row);
		_645(_64f, _650);
	}
	;
	function _5e0(_654, _655, _656) {
		var _657 = $.data(_654, "datagrid");
		var dc = _657.dc;
		var opts = _657.options;
		var row = opts.finder.getRow(_654, _655);
		if (opts.onBeforeUnselect.call(_654, _655, row) == false) {
			return;
		}
		if (!_656 && opts.checkOnSelect) {
			_5dd(_654, _655, true);
		}
		opts.finder.getTr(_654, _655).removeClass("datagrid-row-selected");
		if (opts.idField) {
			_56b(_657.selectedRows, opts.idField, row[opts.idField]);
		}
		opts.onUnselect.call(_654, _655, row);
	}
	;
	function _658(_659, _65a) {
		var _65b = $.data(_659, "datagrid");
		var opts = _65b.options;
		var rows = opts.finder.getRows(_659);
		var _65c = $.data(_659, "datagrid").selectedRows;
		if (!_65a && opts.checkOnSelect) {
			_65d(_659, true);
		}
		opts.finder.getTr(_659, "", "allbody")
				.addClass("datagrid-row-selected");
		if (opts.idField) {
			for ( var _65e = 0; _65e < rows.length; _65e++) {
				_56d(_65c, opts.idField, rows[_65e]);
			}
		}
		opts.onSelectAll.call(_659, rows);
	}
	;
	function _653(_65f, _660) {
		var _661 = $.data(_65f, "datagrid");
		var opts = _661.options;
		var rows = opts.finder.getRows(_65f);
		var _662 = $.data(_65f, "datagrid").selectedRows;
		if (!_660 && opts.checkOnSelect) {
			_663(_65f, true);
		}
		opts.finder.getTr(_65f, "", "selected").removeClass(
				"datagrid-row-selected");
		if (opts.idField) {
			for ( var _664 = 0; _664 < rows.length; _664++) {
				_56b(_662, opts.idField, rows[_664][opts.idField]);
			}
		}
		opts.onUnselectAll.call(_65f, rows);
	}
	;
	function _5dc(_665, _666, _667) {
		var _668 = $.data(_665, "datagrid");
		var opts = _668.options;
		var row = opts.finder.getRow(_665, _666);
		if (opts.onBeforeCheck.call(_665, _666, row) == false) {
			return;
		}
		if (opts.singleSelect && opts.selectOnCheck) {
			_663(_665, true);
			_668.checkedRows = [];
		}
		if (!_667 && opts.selectOnCheck) {
			_5df(_665, _666, true);
		}
		var tr = opts.finder.getTr(_665, _666).addClass("datagrid-row-checked");
		tr.find("div.datagrid-cell-check input[type=checkbox]")._propAttr(
				"checked", true);
		tr = opts.finder.getTr(_665, "", "checked", 2);
		if (tr.length == opts.finder.getRows(_665).length) {
			var dc = _668.dc;
			dc.header1.add(dc.header2).find("input[type=checkbox]")._propAttr(
					"checked", true);
		}
		if (opts.idField) {
			_56d(_668.checkedRows, opts.idField, row);
		}
		opts.onCheck.call(_665, _666, row);
	}
	;
	function _5dd(_669, _66a, _66b) {
		var _66c = $.data(_669, "datagrid");
		var opts = _66c.options;
		var row = opts.finder.getRow(_669, _66a);
		if (opts.onBeforeUncheck.call(_669, _66a, row) == false) {
			return;
		}
		if (!_66b && opts.selectOnCheck) {
			_5e0(_669, _66a, true);
		}
		var tr = opts.finder.getTr(_669, _66a).removeClass(
				"datagrid-row-checked");
		tr.find("div.datagrid-cell-check input[type=checkbox]")._propAttr(
				"checked", false);
		var dc = _66c.dc;
		var _66d = dc.header1.add(dc.header2);
		_66d.find("input[type=checkbox]")._propAttr("checked", false);
		if (opts.idField) {
			_56b(_66c.checkedRows, opts.idField, row[opts.idField]);
		}
		opts.onUncheck.call(_669, _66a, row);
	}
	;
	function _65d(_66e, _66f) {
		var _670 = $.data(_66e, "datagrid");
		var opts = _670.options;
		var rows = opts.finder.getRows(_66e);
		if (!_66f && opts.selectOnCheck) {
			_658(_66e, true);
		}
		var dc = _670.dc;
		var hck = dc.header1.add(dc.header2).find("input[type=checkbox]");
		var bck = opts.finder.getTr(_66e, "", "allbody").addClass(
				"datagrid-row-checked").find(
				"div.datagrid-cell-check input[type=checkbox]");
		hck.add(bck)._propAttr("checked", true);
		if (opts.idField) {
			for ( var i = 0; i < rows.length; i++) {
				_56d(_670.checkedRows, opts.idField, rows[i]);
			}
		}
		opts.onCheckAll.call(_66e, rows);
	}
	;
	function _663(_671, _672) {
		var _673 = $.data(_671, "datagrid");
		var opts = _673.options;
		var rows = opts.finder.getRows(_671);
		if (!_672 && opts.selectOnCheck) {
			_653(_671, true);
		}
		var dc = _673.dc;
		var hck = dc.header1.add(dc.header2).find("input[type=checkbox]");
		var bck = opts.finder.getTr(_671, "", "checked").removeClass(
				"datagrid-row-checked").find(
				"div.datagrid-cell-check input[type=checkbox]");
		hck.add(bck)._propAttr("checked", false);
		if (opts.idField) {
			for ( var i = 0; i < rows.length; i++) {
				_56b(_673.checkedRows, opts.idField, rows[i][opts.idField]);
			}
		}
		opts.onUncheckAll.call(_671, rows);
	}
	;
	function _674(_675, _676) {
		var opts = $.data(_675, "datagrid").options;
		var tr = opts.finder.getTr(_675, _676);
		var row = opts.finder.getRow(_675, _676);
		if (tr.hasClass("datagrid-row-editing")) {
			return;
		}
		if (opts.onBeforeEdit.call(_675, _676, row) == false) {
			return;
		}
		tr.addClass("datagrid-row-editing");
		_677(_675, _676);
		_619(_675);
		tr.find("div.datagrid-editable").each(function() {
			var _678 = $(this).parent().attr("field");
			var ed = $.data(this, "datagrid.editor");
			ed.actions.setValue(ed.target, row[_678]);
		});
		_679(_675, _676);
		opts.onBeginEdit.call(_675, _676, row);
	}
	;
	function _67a(_67b, _67c, _67d) {
		var _67e = $.data(_67b, "datagrid");
		var opts = _67e.options;
		var _67f = _67e.updatedRows;
		var _680 = _67e.insertedRows;
		var tr = opts.finder.getTr(_67b, _67c);
		var row = opts.finder.getRow(_67b, _67c);
		if (!tr.hasClass("datagrid-row-editing")) {
			return;
		}
		if (!_67d) {
			if (!_679(_67b, _67c)) {
				return;
			}
			var _681 = false;
			var _682 = {};
			tr.find("div.datagrid-editable").each(function() {
				var _683 = $(this).parent().attr("field");
				var ed = $.data(this, "datagrid.editor");
				var t = $(ed.target);
				var _684 = t.data("textbox") ? t.textbox("textbox") : t;
				_684.triggerHandler("blur");
				var _685 = ed.actions.getValue(ed.target);
				if (row[_683] != _685) {
					row[_683] = _685;
					_681 = true;
					_682[_683] = _685;
				}
			});
			if (_681) {
				if (_56a(_680, row) == -1) {
					if (_56a(_67f, row) == -1) {
						_67f.push(row);
					}
				}
			}
			opts.onEndEdit.call(_67b, _67c, row, _682);
		}
		tr.removeClass("datagrid-row-editing");
		_686(_67b, _67c);
		$(_67b).datagrid("refreshRow", _67c);
		if (!_67d) {
			opts.onAfterEdit.call(_67b, _67c, row, _682);
		} else {
			opts.onCancelEdit.call(_67b, _67c, row);
		}
	}
	;
	function _687(_688, _689) {
		var opts = $.data(_688, "datagrid").options;
		var tr = opts.finder.getTr(_688, _689);
		var _68a = [];
		tr.children("td").each(function() {
			var cell = $(this).find("div.datagrid-editable");
			if (cell.length) {
				var ed = $.data(cell[0], "datagrid.editor");
				_68a.push(ed);
			}
		});
		return _68a;
	}
	;
	function _68b(_68c, _68d) {
		var _68e = _687(_68c, _68d.index != undefined ? _68d.index : _68d.id);
		for ( var i = 0; i < _68e.length; i++) {
			if (_68e[i].field == _68d.field) {
				return _68e[i];
			}
		}
		return null;
	}
	;
	function _677(_68f, _690) {
		var opts = $.data(_68f, "datagrid").options;
		var tr = opts.finder.getTr(_68f, _690);
		tr.children("td").each(function() {
			var cell = $(this).find("div.datagrid-cell");
			var _691 = $(this).attr("field");
			var col = _5c4(_68f, _691);
			if (col && col.editor) {
				var _692, _693;
				if (typeof col.editor == "string") {
					_692 = col.editor;
				} else {
					_692 = col.editor.type;
					_693 = col.editor.options;
				}
				var _694 = opts.editors[_692];
				if (_694) {
					var _695 = cell.html();
					var _696 = cell._outerWidth();
					cell.addClass("datagrid-editable");
					cell._outerWidth(_696);
					cell.html("<table border=\"0\" cellspacing=\"0\" cellpadding=\"1\"><tr><td></td></tr></table>");
					cell.children("table").bind("click dblclick contextmenu", function(e) {
						e.stopPropagation();
					});
					$.data(cell[0], "datagrid.editor", {
						actions : _694,
						target : _694.init(cell.find("td"), _693),
						field : _691,
						type : _692,
						oldHtml : _695
					});
				}
			}
		});
		_593(_68f, _690, true);
	}
	;
	function _686(_697, _698) {
		var opts = $.data(_697, "datagrid").options;
		var tr = opts.finder.getTr(_697, _698);
		tr.children("td").each(function() {
			var cell = $(this).find("div.datagrid-editable");
			if (cell.length) {
				var ed = $.data(cell[0], "datagrid.editor");
				if (ed.actions.destroy) {
					ed.actions.destroy(ed.target);
				}
				cell.html(ed.oldHtml);
				$.removeData(cell[0], "datagrid.editor");
				cell.removeClass("datagrid-editable");
				cell.css("width", "");
			}
		});
	}
	;
	function _679(_699, _69a) {
		var tr = $.data(_699, "datagrid").options.finder.getTr(_699, _69a);
		if (!tr.hasClass("datagrid-row-editing")) {
			return true;
		}
		var vbox = tr.find(".validatebox-text");
		vbox.validatebox("validate");
		vbox.trigger("mouseleave");
		var _69b = tr.find(".validatebox-invalid");
		return _69b.length == 0;
	}
	;
	function _69c(_69d, _69e) {
		var _69f = $.data(_69d, "datagrid").insertedRows;
		var _6a0 = $.data(_69d, "datagrid").deletedRows;
		var _6a1 = $.data(_69d, "datagrid").updatedRows;
		if (!_69e) {
			var rows = [];
			rows = rows.concat(_69f);
			rows = rows.concat(_6a0);
			rows = rows.concat(_6a1);
			return rows;
		} else {
			if (_69e == "inserted") {
				return _69f;
			} else {
				if (_69e == "deleted") {
					return _6a0;
				} else {
					if (_69e == "updated") {
						return _6a1;
					}
				}
			}
		}
		return [];
	}
	;
	function _6a2(_6a3, _6a4) {
		var _6a5 = $.data(_6a3, "datagrid");
		var opts = _6a5.options;
		var data = _6a5.data;
		var _6a6 = _6a5.insertedRows;
		var _6a7 = _6a5.deletedRows;
		$(_6a3).datagrid("cancelEdit", _6a4);
		var row = opts.finder.getRow(_6a3, _6a4);
		if (_56a(_6a6, row) >= 0) {
			_56b(_6a6, row);
		} else {
			_6a7.push(row);
		}
		_56b(_6a5.selectedRows, opts.idField, row[opts.idField]);
		_56b(_6a5.checkedRows, opts.idField, row[opts.idField]);
		opts.view.deleteRow.call(opts.view, _6a3, _6a4);
		if (opts.height == "auto") {
			_593(_6a3);
		}
		$(_6a3).datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}
	;
	function _6a8(_6a9, _6aa) {
		var data = $.data(_6a9, "datagrid").data;
		var view = $.data(_6a9, "datagrid").options.view;
		var _6ab = $.data(_6a9, "datagrid").insertedRows;
		view.insertRow.call(view, _6a9, _6aa.index, _6aa.row);
		_6ab.push(_6aa.row);
		$(_6a9).datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}
	;
	function _6ac(_6ad, row) {
		var data = $.data(_6ad, "datagrid").data;
		var view = $.data(_6ad, "datagrid").options.view;
		var _6ae = $.data(_6ad, "datagrid").insertedRows;
		view.insertRow.call(view, _6ad, null, row);
		_6ae.push(row);
		$(_6ad).datagrid("getPager").pagination("refresh", {
			total : data.total
		});
	}
	;
	function _6af(_6b0) {
		var _6b1 = $.data(_6b0, "datagrid");
		var data = _6b1.data;
		var rows = data.rows;
		var _6b2 = [];
		for ( var i = 0; i < rows.length; i++) {
			_6b2.push($.extend({}, rows[i]));
		}
		_6b1.originalRows = _6b2;
		_6b1.updatedRows = [];
		_6b1.insertedRows = [];
		_6b1.deletedRows = [];
	}
	;
	function _6b3(_6b4) {
		var data = $.data(_6b4, "datagrid").data;
		var ok = true;
		for ( var i = 0, len = data.rows.length; i < len; i++) {
			if (_679(_6b4, i)) {
				$(_6b4).datagrid("endEdit", i);
			} else {
				ok = false;
			}
		}
		if (ok) {
			_6af(_6b4);
		}
	}
	;
	function _6b5(_6b6) {
		var _6b7 = $.data(_6b6, "datagrid");
		var opts = _6b7.options;
		var _6b8 = _6b7.originalRows;
		var _6b9 = _6b7.insertedRows;
		var _6ba = _6b7.deletedRows;
		var _6bb = _6b7.selectedRows;
		var _6bc = _6b7.checkedRows;
		var data = _6b7.data;
		function _6bd(a) {
			var ids = [];
			for ( var i = 0; i < a.length; i++) {
				ids.push(a[i][opts.idField]);
			}
			return ids;
		}
		;
		function _6be(ids, _6bf) {
			for ( var i = 0; i < ids.length; i++) {
				var _6c0 = _63c(_6b6, ids[i]);
				if (_6c0 >= 0) {
					(_6bf == "s" ? _5df : _5dc)(_6b6, _6c0, true);
				}
			}
		}
		;
		for ( var i = 0; i < data.rows.length; i++) {
			$(_6b6).datagrid("cancelEdit", i);
		}
		var _6c1 = _6bd(_6bb);
		var _6c2 = _6bd(_6bc);
		_6bb.splice(0, _6bb.length);
		_6bc.splice(0, _6bc.length);
		data.total += _6ba.length - _6b9.length;
		data.rows = _6b8;
		_5f6(_6b6, data);
		_6be(_6c1, "s");
		_6be(_6c2, "c");
		_6af(_6b6);
	}
	;
	function _5f5(_6c3, _6c4) {
		var opts = $.data(_6c3, "datagrid").options;
		if (_6c4) {
			opts.queryParams = _6c4;
		}
		var _6c5 = $.extend({}, opts.queryParams);
		if (opts.pagination) {
			$.extend(_6c5, {
				page : opts.pageNumber || 1,
				rows : opts.pageSize
			});
		}
		if (opts.sortName) {
			$.extend(_6c5, {
				sort : opts.sortName,
				order : opts.sortOrder
			});
		}
		if (opts.onBeforeLoad.call(_6c3, _6c5) == false) {
			return;
		}
		$(_6c3).datagrid("loading");
		setTimeout(function() {
			_6c6();
		}, 0);
		function _6c6() {
			var _6c7 = opts.loader.call(_6c3, _6c5, function(data) {
				setTimeout(function() {
					$(_6c3).datagrid("loaded");
				}, 0);
				_5f6(_6c3, data);
				setTimeout(function() {
					_6af(_6c3);
				}, 0);
			}, function() {
				setTimeout(function() {
					$(_6c3).datagrid("loaded");
				}, 0);
				opts.onLoadError.apply(_6c3, arguments);
			});
			if (_6c7 == false) {
				$(_6c3).datagrid("loaded");
			}
		}
		;
	}
	;
	function _6c8(_6c9, _6ca) {
		var opts = $.data(_6c9, "datagrid").options;
		_6ca.type = _6ca.type || "body";
		_6ca.rowspan = _6ca.rowspan || 1;
		_6ca.colspan = _6ca.colspan || 1;
		if (_6ca.rowspan == 1 && _6ca.colspan == 1) {
			return;
		}
		var tr = opts.finder.getTr(_6c9, (_6ca.index != undefined ? _6ca.index
				: _6ca.id), _6ca.type);
		if (!tr.length) {
			return;
		}
		var td = tr.find("td[field=\"" + _6ca.field + "\"]");
		td.attr("rowspan", _6ca.rowspan).attr("colspan", _6ca.colspan);
		td.addClass("datagrid-td-merged");
		_6cb(td.next(), _6ca.colspan - 1);
		for ( var i = 1; i < _6ca.rowspan; i++) {
			tr = tr.next();
			if (!tr.length) {
				break;
			}
			td = tr.find("td[field=\"" + _6ca.field + "\"]");
			_6cb(td, _6ca.colspan);
		}
		_618(_6c9);
		function _6cb(td, _6cc) {
			for ( var i = 0; i < _6cc; i++) {
				td.hide();
				td = td.next();
			}
		}
		;
	}
	;
	$.fn.datagrid = function(_6cd, _6ce) {
		if (typeof _6cd == "string") {
			return $.fn.datagrid.methods[_6cd](this, _6ce);
		}
		_6cd = _6cd || {};
		return this.each(function() {
			var _6cf = $.data(this, "datagrid");
			var opts;
			if (_6cf) {
				opts = $.extend(_6cf.options, _6cd);
				_6cf.options = opts;
			} else {
				opts = $.extend({}, $.extend({}, $.fn.datagrid.defaults, {
					queryParams : {}
				}), $.fn.datagrid.parseOptions(this), _6cd);
				$(this).css("width", "").css("height", "");
				var _6d0 = _5a7(this, opts.rownumbers);
				if (!opts.columns) {
					opts.columns = _6d0.columns;
				}
				if (!opts.frozenColumns) {
					opts.frozenColumns = _6d0.frozenColumns;
				}
				opts.columns = $.extend(true, [], opts.columns);
				opts.frozenColumns = $.extend(true, [], opts.frozenColumns);
				opts.view = $.extend({}, opts.view);
				$.data(this, "datagrid", {
					options : opts,
					panel : _6d0.panel,
					dc : _6d0.dc,
					ss : null,
					selectedRows : [],
					checkedRows : [],
					data : {
						total : 0,
						rows : []
					},
					originalRows : [],
					updatedRows : [],
					insertedRows : [],
					deletedRows : []
				});
			}
			_5b0(this);
			_5c5(this);
			_57d(this);
			if (opts.data) {
				_5f6(this, opts.data);
				_6af(this);
			} else {
				var data = $.fn.datagrid.parseData(this);
				if (data.total > 0) {
					_5f6(this, data);
					_6af(this);
				}
			}
			_5f5(this);
		});
	};
	function _6d1(_6d2) {
		var _6d3 = {};
		$.map(_6d2, function(name) {
			_6d3[name] = _6d4(name);
		});
		return _6d3;
		function _6d4(name) {
			function isA(_6d5) {
				return $.data($(_6d5)[0], name) != undefined;
			}
			;
			return {
				init : function(_6d6, _6d7) {
					var _6d8 = $("<input type=\"text\" class=\"datagrid-editable-input\">")
							.appendTo(_6d6);
					if (_6d8[name] && name != "text") {
						return _6d8[name](_6d7);
					} else {
						return _6d8;
					}
				},
				destroy : function(_6d9) {
					if (isA(_6d9, name)) {
						$(_6d9)[name]("destroy");
					}
				},
				getValue : function(_6da) {
					if (isA(_6da, name)) {
						var opts = $(_6da)[name]("options");
						if (opts.multiple) {
							return $(_6da)[name]("getValues").join(
									opts.separator);
						} else {
							return $(_6da)[name]("getValue");
						}
					} else {
						return $(_6da).val();
					}
				},
				setValue : function(_6db, _6dc) {
					if (isA(_6db, name)) {
						var opts = $(_6db)[name]("options");
						if (opts.multiple) {
							if (_6dc) {
								$(_6db)[name]("setValues", _6dc
										.split(opts.separator));
							} else {
								$(_6db)[name]("clear");
							}
						} else {
							$(_6db)[name]("setValue", _6dc);
						}
					} else {
						$(_6db).val(_6dc);
					}
				},
				resize : function(_6dd, _6de) {
					if (isA(_6dd, name)) {
						$(_6dd)[name]("resize", _6de);
					} else {
						$(_6dd)._outerWidth(_6de)._outerHeight(22);
					}
				}
			};
		}
		;
	}
	;
	var _6df = $.extend({},
					_6d1([ "text", "textbox", "numberbox", "numberspinner",
							"combobox", "combotree", "combogrid", "datebox",
							"datetimebox", "timespinner", "datetimespinner" ]),
					{
						textarea : {
							init : function(container, options) {
								var target = $("<textarea class=\"datagrid-editable-input\"></textarea>")
										.appendTo(container);
								return target;
							},
							getValue : function(target) {
								return $(target).val();
							},
							setValue : function(target, value) {
								$(target).val(value);
							},
							resize : function(target, width) {
								$(target)._outerWidth(width);
							}
						},
						checkbox : {
							init : function(container, options) {
								var target = $("<input type=\"checkbox\">").appendTo(container);
								target.val(options.on);
								target.attr("offval", options.off);
								return target;
							},
							getValue : function(_6eb) {
								if ($(_6eb).is(":checked")) {
									return $(_6eb).val();
								} else {
									return $(_6eb).attr("offval");
								}
							},
							setValue : function(_6ec, _6ed) {
								var _6ee = false;
								if ($(_6ec).val() == _6ed) {
									_6ee = true;
								}
								$(_6ec)._propAttr("checked", _6ee);
							}
						},
						validatebox : {
							init : function(_6ef, _6f0) {
								var _6f1 = $(
										"<input type=\"text\" class=\"datagrid-editable-input\">")
										.appendTo(_6ef);
								_6f1.validatebox(_6f0);
								return _6f1;
							},
							destroy : function(_6f2) {
								$(_6f2).validatebox("destroy");
							},
							getValue : function(_6f3) {
								return $(_6f3).val();
							},
							setValue : function(_6f4, _6f5) {
								$(_6f4).val(_6f5);
							},
							resize : function(_6f6, _6f7) {
								$(_6f6)._outerWidth(_6f7)._outerHeight(22);
							}
						}
					});
	$.fn.datagrid.methods = {
		options : function(jq) {
			var _6f8 = $.data(jq[0], "datagrid").options;
			var _6f9 = $.data(jq[0], "datagrid").panel.panel("options");
			var opts = $.extend(_6f8, {
				width : _6f9.width,
				height : _6f9.height,
				closed : _6f9.closed,
				collapsed : _6f9.collapsed,
				minimized : _6f9.minimized,
				maximized : _6f9.maximized
			});
			return opts;
		},
		setSelectionState : function(jq) {
			return jq.each(function() {
				_634(this);
			});
		},
		createStyleSheet : function(jq) {
			return _56e(jq[0]);
		},
		getPanel : function(jq) {
			return $.data(jq[0], "datagrid").panel;
		},
		getPager : function(jq) {
			return $.data(jq[0], "datagrid").panel
					.children("div.datagrid-pager");
		},
		getColumnFields : function(jq, _6fa) {
			return _5c3(jq[0], _6fa);
		},
		getColumnOption : function(jq, _6fb) {
			return _5c4(jq[0], _6fb);
		},
		resize : function(jq, _6fc) {
			return jq.each(function() {
				_57d(this, _6fc);
			});
		},
		load : function(jq, _6fd) {
			return jq.each(function() {
				var opts = $(this).datagrid("options");
				if (typeof _6fd == "string") {
					opts.url = _6fd;
					_6fd = null;
				}
				opts.pageNumber = 1;
				var _6fe = $(this).datagrid("getPager");
				_6fe.pagination("refresh", {
					pageNumber : 1
				});
				_5f5(this, _6fd);
			});
		},
		reload : function(jq, _6ff) {
			return jq.each(function() {
				var opts = $(this).datagrid("options");
				if (typeof _6ff == "string") {
					opts.url = _6ff;
					_6ff = null;
				}
				_5f5(this, _6ff);
			});
		},
		reloadFooter : function(jq, _700) {
			return jq.each(function() {
				var opts = $.data(this, "datagrid").options;
				var dc = $.data(this, "datagrid").dc;
				if (_700) {
					$.data(this, "datagrid").footer = _700;
				}
				if (opts.showFooter) {
					opts.view.renderFooter.call(opts.view, this, dc.footer2,
							false);
					opts.view.renderFooter.call(opts.view, this, dc.footer1,
							true);
					if (opts.view.onAfterRender) {
						opts.view.onAfterRender.call(opts.view, this);
					}
					$(this).datagrid("fixRowHeight");
				}
			});
		},
		loading : function(jq) {
			return jq
					.each(function() {
						var opts = $.data(this, "datagrid").options;
						$(this).datagrid("getPager").pagination("loading");
						if (opts.loadMsg) {
							var _701 = $(this).datagrid("getPanel");
							if (!_701.children("div.datagrid-mask").length) {
								$(
										"<div class=\"datagrid-mask\" style=\"display:block\"></div>")
										.appendTo(_701);
								var msg = $(
										"<div class=\"datagrid-mask-msg\" style=\"display:block;left:50%\"></div>")
										.html(opts.loadMsg).appendTo(_701);
								msg._outerHeight(40);
								msg.css({
									marginLeft : (-msg.outerWidth() / 2),
									lineHeight : (msg.height() + "px")
								});
							}
						}
					});
		},
		loaded : function(jq) {
			return jq.each(function() {
				$(this).datagrid("getPager").pagination("loaded");
				var _702 = $(this).datagrid("getPanel");
				_702.children("div.datagrid-mask-msg").remove();
				_702.children("div.datagrid-mask").remove();
			});
		},
		fitColumns : function(jq) {
			return jq.each(function() {
				_5f7(this);
			});
		},
		fixColumnSize : function(jq, _703) {
			return jq.each(function() {
				_613(this, _703);
			});
		},
		fixRowHeight : function(jq, _704) {
			return jq.each(function() {
				_593(this, _704);
			});
		},
		freezeRow : function(jq, _705) {
			return jq.each(function() {
				_5a0(this, _705);
			});
		},
		autoSizeColumn : function(jq, _706) {
			return jq.each(function() {
				_607(this, _706);
			});
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				_5f6(this, data);
				_6af(this);
			});
		},
		getData : function(jq) {
			return $.data(jq[0], "datagrid").data;
		},
		getRows : function(jq) {
			return $.data(jq[0], "datagrid").data.rows;
		},
		getFooterRows : function(jq) {
			return $.data(jq[0], "datagrid").footer;
		},
		getRowIndex : function(jq, id) {
			return _63c(jq[0], id);
		},
		getChecked : function(jq) {
			return _642(jq[0]);
		},
		getSelected : function(jq) {
			var rows = _63f(jq[0]);
			return rows.length > 0 ? rows[0] : null;
		},
		getSelections : function(jq) {
			return _63f(jq[0]);
		},
		clearSelections : function(jq) {
			return jq.each(function() {
				var _707 = $.data(this, "datagrid");
				var _708 = _707.selectedRows;
				var _709 = _707.checkedRows;
				_708.splice(0, _708.length);
				_653(this);
				if (_707.options.checkOnSelect) {
					_709.splice(0, _709.length);
				}
			});
		},
		clearChecked : function(jq) {
			return jq.each(function() {
				var _70a = $.data(this, "datagrid");
				var _70b = _70a.selectedRows;
				var _70c = _70a.checkedRows;
				_70c.splice(0, _70c.length);
				_663(this);
				if (_70a.options.selectOnCheck) {
					_70b.splice(0, _70b.length);
				}
			});
		},
		scrollTo : function(jq, _70d) {
			return jq.each(function() {
				_645(this, _70d);
			});
		},
		highlightRow : function(jq, _70e) {
			return jq.each(function() {
				_5d8(this, _70e);
				_645(this, _70e);
			});
		},
		selectAll : function(jq) {
			return jq.each(function() {
				_658(this);
			});
		},
		unselectAll : function(jq) {
			return jq.each(function() {
				_653(this);
			});
		},
		selectRow : function(jq, _70f) {
			return jq.each(function() {
				_5df(this, _70f);
			});
		},
		selectRecord : function(jq, id) {
			return jq.each(function() {
				var opts = $.data(this, "datagrid").options;
				if (opts.idField) {
					var _710 = _63c(this, id);
					if (_710 >= 0) {
						$(this).datagrid("selectRow", _710);
					}
				}
			});
		},
		unselectRow : function(jq, _711) {
			return jq.each(function() {
				_5e0(this, _711);
			});
		},
		checkRow : function(jq, _712) {
			return jq.each(function() {
				_5dc(this, _712);
			});
		},
		uncheckRow : function(jq, _713) {
			return jq.each(function() {
				_5dd(this, _713);
			});
		},
		checkAll : function(jq) {
			return jq.each(function() {
				_65d(this);
			});
		},
		uncheckAll : function(jq) {
			return jq.each(function() {
				_663(this);
			});
		},
		beginEdit : function(jq, _714) {
			return jq.each(function() {
				_674(this, _714);
			});
		},
		endEdit : function(jq, _715) {
			return jq.each(function() {
				_67a(this, _715, false);
			});
		},
		cancelEdit : function(jq, _716) {
			return jq.each(function() {
				_67a(this, _716, true);
			});
		},
		getEditors : function(jq, _717) {
			return _687(jq[0], _717);
		},
		getEditor : function(jq, _718) {
			return _68b(jq[0], _718);
		},
		refreshRow : function(jq, _719) {
			return jq.each(function() {
				var opts = $.data(this, "datagrid").options;
				opts.view.refreshRow.call(opts.view, this, _719);
			});
		},
		validateRow : function(jq, _71a) {
			return _679(jq[0], _71a);
		},
		updateRow : function(jq, _71b) {
			return jq
					.each(function() {
						var opts = $.data(this, "datagrid").options;
						opts.view.updateRow.call(opts.view, this, _71b.index,
								_71b.row);
					});
		},
		appendRow : function(jq, row) {
			return jq.each(function() {
				_6ac(this, row);
			});
		},
		insertRow : function(jq, _71c) {
			return jq.each(function() {
				_6a8(this, _71c);
			});
		},
		deleteRow : function(jq, _71d) {
			return jq.each(function() {
				_6a2(this, _71d);
			});
		},
		getChanges : function(jq, _71e) {
			return _69c(jq[0], _71e);
		},
		acceptChanges : function(jq) {
			return jq.each(function() {
				_6b3(this);
			});
		},
		rejectChanges : function(jq) {
			return jq.each(function() {
				_6b5(this);
			});
		},
		mergeCells : function(jq, _71f) {
			return jq.each(function() {
				_6c8(this, _71f);
			});
		},
		showColumn : function(jq, _720) {
			return jq.each(function() {
				var _721 = $(this).datagrid("getPanel");
				_721.find("td[field=\"" + _720 + "\"]").show();
				$(this).datagrid("getColumnOption", _720).hidden = false;
				$(this).datagrid("fitColumns");
			});
		},
		hideColumn : function(jq, _722) {
			return jq.each(function() {
				var _723 = $(this).datagrid("getPanel");
				_723.find("td[field=\"" + _722 + "\"]").hide();
				$(this).datagrid("getColumnOption", _722).hidden = true;
				$(this).datagrid("fitColumns");
			});
		},
		sort : function(jq, _724) {
			return jq.each(function() {
				_5ea(this, _724);
			});
		}
	};
	$.fn.datagrid.parseOptions = function(_725) {
		var t = $(_725);
		return $.extend({}, $.fn.panel.parseOptions(_725), $.parser
				.parseOptions(_725, [ "url", "toolbar", "idField", "sortName",
						"sortOrder", "pagePosition", "resizeHandle", {
							sharedStyleSheet : "boolean",
							fitColumns : "boolean",
							autoRowHeight : "boolean",
							striped : "boolean",
							nowrap : "boolean"
						}, {
							rownumbers : "boolean",
							singleSelect : "boolean",
							ctrlSelect : "boolean",
							checkOnSelect : "boolean",
							selectOnCheck : "boolean"
						}, {
							pagination : "boolean",
							pageSize : "number",
							pageNumber : "number"
						}, {
							multiSort : "boolean",
							remoteSort : "boolean",
							showHeader : "boolean",
							showFooter : "boolean"
						}, {
							scrollbarSize : "number"
						} ]), {
			pageList : (t.attr("pageList") ? eval(t.attr("pageList"))
					: undefined),
			loadMsg : (t.attr("loadMsg") != undefined ? t.attr("loadMsg")
					: undefined),
			rowStyler : (t.attr("rowStyler") ? eval(t.attr("rowStyler"))
					: undefined)
		});
	};
	$.fn.datagrid.parseData = function(_726) {
		var t = $(_726);
		var data = {
			total : 0,
			rows : []
		};
		var _727 = t.datagrid("getColumnFields", true).concat(
				t.datagrid("getColumnFields", false));
		t.find("tbody tr").each(function() {
			data.total++;
			var row = {};
			$.extend(row, $.parser.parseOptions(this, [ "iconCls", "state" ]));
			for ( var i = 0; i < _727.length; i++) {
				row[_727[i]] = $(this).find("td:eq(" + i + ")").html();
			}
			data.rows.push(row);
		});
		return data;
	};
	var _728 = {
		render : function(_729, _72a, _72b) {
			var _72c = $.data(_729, "datagrid");
			var opts = _72c.options;
			var rows = _72c.data.rows;
			var _72d = $(_729).datagrid("getColumnFields", _72b);
			if (_72b) {
				if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))) {
					return;
				}
			}
			var _72e = [ "<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
			for ( var i = 0; i < rows.length; i++) {
				var css = opts.rowStyler ? opts.rowStyler
						.call(_729, i, rows[i]) : "";
				var _72f = "";
				var _730 = "";
				if (typeof css == "string") {
					_730 = css;
				} else {
					if (css) {
						_72f = css["class"] || "";
						_730 = css["style"] || "";
					}
				}
				var cls = "class=\"datagrid-row "
						+ (i % 2 && opts.striped ? "datagrid-row-alt " : " ")
						+ _72f + "\"";
				var _731 = _730 ? "style=\"" + _730 + "\"" : "";
				var _732 = _72c.rowIdPrefix + "-" + (_72b ? 1 : 2) + "-" + i;
				_72e.push("<tr id=\"" + _732 + "\" datagrid-row-index=\"" + i
						+ "\" " + cls + " " + _731 + ">");
				_72e.push(this.renderRow.call(this, _729, _72d, _72b, i,
						rows[i]));
				_72e.push("</tr>");
			}
			_72e.push("</tbody></table>");
			$(_72a).html(_72e.join(""));
		},
		renderFooter : function(_733, _734, _735) {
			var opts = $.data(_733, "datagrid").options;
			var rows = $.data(_733, "datagrid").footer || [];
			var _736 = $(_733).datagrid("getColumnFields", _735);
			var _737 = [ "<table class=\"datagrid-ftable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
			for ( var i = 0; i < rows.length; i++) {
				_737.push("<tr class=\"datagrid-row\" datagrid-row-index=\""
						+ i + "\">");
				_737.push(this.renderRow.call(this, _733, _736, _735, i,
						rows[i]));
				_737.push("</tr>");
			}
			_737.push("</tbody></table>");
			$(_734).html(_737.join(""));
		},
		renderRow : function(_738, _739, _73a, _73b, _73c) {
			var opts = $.data(_738, "datagrid").options;
			var cc = [];
			if (_73a && opts.rownumbers) {
				var _73d = _73b + 1;
				if (opts.pagination) {
					_73d += (opts.pageNumber - 1) * opts.pageSize;
				}
				cc
						.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">"
								+ _73d + "</div></td>");
			}
			for ( var i = 0; i < _739.length; i++) {
				var _73e = _739[i];
				var col = $(_738).datagrid("getColumnOption", _73e);
				if (col) {
					var _73f = _73c[_73e];
					var css = col.styler ? (col.styler(_73f, _73c, _73b) || "")
							: "";
					var _740 = "";
					var _741 = "";
					if (typeof css == "string") {
						_741 = css;
					} else {
						if (css) {
							_740 = css["class"] || "";
							_741 = css["style"] || "";
						}
					}
					var cls = _740 ? "class=\"" + _740 + "\"" : "";
					var _742 = col.hidden ? "style=\"display:none;" + _741
							+ "\"" : (_741 ? "style=\"" + _741 + "\"" : "");
					cc.push("<td field=\"" + _73e + "\" " + cls + " " + _742
							+ ">");
					var _742 = "";
					if (!col.checkbox) {
						if (col.align) {
							_742 += "text-align:" + col.align + ";";
						}
						if (!opts.nowrap) {
							_742 += "white-space:normal;height:auto;";
						} else {
							if (opts.autoRowHeight) {
								_742 += "height:auto;";
							}
						}
					}
					cc.push("<div style=\"" + _742 + "\" ");
					cc.push(col.checkbox ? "class=\"datagrid-cell-check\""
							: "class=\"datagrid-cell " + col.cellClass + "\"");
					cc.push(">");
					if (col.checkbox) {
						cc.push("<input type=\"checkbox\" "
								+ (_73c.checked ? "checked=\"checked\"" : ""));
						cc.push(" name=\"" + _73e + "\" value=\""
								+ (_73f != undefined ? _73f : "") + "\">");
					} else {
						if (col.formatter) {
							cc.push(col.formatter(_73f, _73c, _73b));
						} else {
							cc.push(_73f);
						}
					}
					cc.push("</div>");
					cc.push("</td>");
				}
			}
			return cc.join("");
		},
		refreshRow : function(_743, _744) {
			this.updateRow.call(this, _743, _744, {});
		},
		updateRow : function(_745, _746, row) {
			var opts = $.data(_745, "datagrid").options;
			var rows = $(_745).datagrid("getRows");
			var _747 = _748(_746);
			$.extend(rows[_746], row);
			var _749 = _748(_746);
			var _74a = _747.c;
			var _74b = _749.s;
			var _74c = "datagrid-row "
					+ (_746 % 2 && opts.striped ? "datagrid-row-alt " : " ")
					+ _749.c;
			function _748(_74d) {
				var css = opts.rowStyler ? opts.rowStyler.call(_745, _74d,
						rows[_74d]) : "";
				var _74e = "";
				var _74f = "";
				if (typeof css == "string") {
					_74f = css;
				} else {
					if (css) {
						_74e = css["class"] || "";
						_74f = css["style"] || "";
					}
				}
				return {
					c : _74e,
					s : _74f
				};
			}
			;
			function _750(_751) {
				var _752 = $(_745).datagrid("getColumnFields", _751);
				var tr = opts.finder.getTr(_745, _746, "body", (_751 ? 1 : 2));
				var _753 = tr.find(
						"div.datagrid-cell-check input[type=checkbox]").is(
						":checked");
				tr.html(this.renderRow.call(this, _745, _752, _751, _746,
						rows[_746]));
				tr.attr("style", _74b).removeClass(_74a).addClass(_74c);
				if (_753) {
					tr.find("div.datagrid-cell-check input[type=checkbox]")
							._propAttr("checked", true);
				}
			}
			;
			_750.call(this, true);
			_750.call(this, false);
			$(_745).datagrid("fixRowHeight", _746);
		},
		insertRow : function(_754, _755, row) {
			var _756 = $.data(_754, "datagrid");
			var opts = _756.options;
			var dc = _756.dc;
			var data = _756.data;
			if (_755 == undefined || _755 == null) {
				_755 = data.rows.length;
			}
			if (_755 > data.rows.length) {
				_755 = data.rows.length;
			}
			function _757(_758) {
				var _759 = _758 ? 1 : 2;
				for ( var i = data.rows.length - 1; i >= _755; i--) {
					var tr = opts.finder.getTr(_754, i, "body", _759);
					tr.attr("datagrid-row-index", i + 1);
					tr
							.attr("id", _756.rowIdPrefix + "-" + _759 + "-"
									+ (i + 1));
					if (_758 && opts.rownumbers) {
						var _75a = i + 2;
						if (opts.pagination) {
							_75a += (opts.pageNumber - 1) * opts.pageSize;
						}
						tr.find("div.datagrid-cell-rownumber").html(_75a);
					}
					if (opts.striped) {
						tr.removeClass("datagrid-row-alt").addClass(
								(i + 1) % 2 ? "datagrid-row-alt" : "");
					}
				}
			}
			;
			function _75b(_75c) {
				var _75d = _75c ? 1 : 2;
				var _75e = $(_754).datagrid("getColumnFields", _75c);
				var _75f = _756.rowIdPrefix + "-" + _75d + "-" + _755;
				var tr = "<tr id=\"" + _75f
						+ "\" class=\"datagrid-row\" datagrid-row-index=\""
						+ _755 + "\"></tr>";
				if (_755 >= data.rows.length) {
					if (data.rows.length) {
						opts.finder.getTr(_754, "", "last", _75d).after(tr);
					} else {
						var cc = _75c ? dc.body1 : dc.body2;
						cc
								.html("<table cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>"
										+ tr + "</tbody></table>");
					}
				} else {
					opts.finder.getTr(_754, _755 + 1, "body", _75d).before(tr);
				}
			}
			;
			_757.call(this, true);
			_757.call(this, false);
			_75b.call(this, true);
			_75b.call(this, false);
			data.total += 1;
			data.rows.splice(_755, 0, row);
			this.refreshRow.call(this, _754, _755);
		},
		deleteRow : function(_760, _761) {
			var _762 = $.data(_760, "datagrid");
			var opts = _762.options;
			var data = _762.data;
			function _763(_764) {
				var _765 = _764 ? 1 : 2;
				for ( var i = _761 + 1; i < data.rows.length; i++) {
					var tr = opts.finder.getTr(_760, i, "body", _765);
					tr.attr("datagrid-row-index", i - 1);
					tr
							.attr("id", _762.rowIdPrefix + "-" + _765 + "-"
									+ (i - 1));
					if (_764 && opts.rownumbers) {
						var _766 = i;
						if (opts.pagination) {
							_766 += (opts.pageNumber - 1) * opts.pageSize;
						}
						tr.find("div.datagrid-cell-rownumber").html(_766);
					}
					if (opts.striped) {
						tr.removeClass("datagrid-row-alt").addClass(
								(i - 1) % 2 ? "datagrid-row-alt" : "");
					}
				}
			}
			;
			opts.finder.getTr(_760, _761).remove();
			_763.call(this, true);
			_763.call(this, false);
			data.total -= 1;
			data.rows.splice(_761, 1);
		},
		onBeforeRender : function(_767, rows) {
		},
		onAfterRender : function(_768) {
			var opts = $.data(_768, "datagrid").options;
			if (opts.showFooter) {
				var _769 = $(_768).datagrid("getPanel").find(
						"div.datagrid-footer");
				_769
						.find(
								"div.datagrid-cell-rownumber,div.datagrid-cell-check")
						.css("visibility", "hidden");
			}
		}
	};
	$.fn.datagrid.defaults = $
			.extend(
					{},
					$.fn.panel.defaults,
					{
						sharedStyleSheet : false,
						frozenColumns : undefined,
						columns : undefined,
						fitColumns : false,
						resizeHandle : "right",
						autoRowHeight : true,
						toolbar : null,
						striped : false,
						method : "post",
						nowrap : true,
						idField : null,
						url : null,
						data : null,
						loadMsg : "Processing, please wait ...",
						rownumbers : false,
						singleSelect : false,
						ctrlSelect : false,
						selectOnCheck : true,
						checkOnSelect : true,
						pagination : false,
						pagePosition : "bottom",
						pageNumber : 1,
						pageSize : 10,
						pageList : [ 10, 20, 30, 40, 50 ],
						queryParams : {},
						sortName : null,
						sortOrder : "asc",
						multiSort : false,
						remoteSort : true,
						showHeader : true,
						showFooter : false,
						scrollbarSize : 18,
						rowEvents : {
							mouseover : _5d1(true),
							mouseout : _5d1(false),
							click : _5d9,
							dblclick : _5e3,
							contextmenu : _5e7
						},
						rowStyler : function(_76a, _76b) {
						},
						loader : function(_76c, _76d, _76e) {
							var opts = $(this).datagrid("options");
							if (!opts.url) {
								return false;
							}
							$.ajax({
								type : opts.method,
								url : opts.url,
								data : _76c,
								dataType : "json",
								success : function(data) {
									_76d(data);
								},
								error : function() {
									_76e.apply(this, arguments);
								}
							});
						},
						loadFilter : function(data) {
							if (typeof data.length == "number"
									&& typeof data.splice == "function") {
								return {
									total : data.length,
									rows : data
								};
							} else {
								return data;
							}
						},
						editors : _6df,
						finder : {
							getTr : function(_76f, _770, type, _771) {
								type = type || "body";
								_771 = _771 || 0;
								var _772 = $.data(_76f, "datagrid");
								var dc = _772.dc;
								var opts = _772.options;
								if (_771 == 0) {
									var tr1 = opts.finder.getTr(_76f, _770,
											type, 1);
									var tr2 = opts.finder.getTr(_76f, _770,
											type, 2);
									return tr1.add(tr2);
								} else {
									if (type == "body") {
										var tr = $("#" + _772.rowIdPrefix + "-"
												+ _771 + "-" + _770);
										if (!tr.length) {
											tr = (_771 == 1 ? dc.body1
													: dc.body2)
													.find(">table>tbody>tr[datagrid-row-index="
															+ _770 + "]");
										}
										return tr;
									} else {
										if (type == "footer") {
											return (_771 == 1 ? dc.footer1
													: dc.footer2)
													.find(">table>tbody>tr[datagrid-row-index="
															+ _770 + "]");
										} else {
											if (type == "selected") {
												return (_771 == 1 ? dc.body1
														: dc.body2)
														.find(">table>tbody>tr.datagrid-row-selected");
											} else {
												if (type == "highlight") {
													return (_771 == 1 ? dc.body1
															: dc.body2)
															.find(">table>tbody>tr.datagrid-row-over");
												} else {
													if (type == "checked") {
														return (_771 == 1 ? dc.body1
																: dc.body2)
																.find(">table>tbody>tr.datagrid-row-checked");
													} else {
														if (type == "editing") {
															return (_771 == 1 ? dc.body1
																	: dc.body2)
																	.find(">table>tbody>tr.datagrid-row-editing");
														} else {
															if (type == "last") {
																return (_771 == 1 ? dc.body1
																		: dc.body2)
																		.find(">table>tbody>tr[datagrid-row-index]:last");
															} else {
																if (type == "allbody") {
																	return (_771 == 1 ? dc.body1
																			: dc.body2)
																			.find(">table>tbody>tr[datagrid-row-index]");
																} else {
																	if (type == "allfooter") {
																		return (_771 == 1 ? dc.footer1
																				: dc.footer2)
																				.find(">table>tbody>tr[datagrid-row-index]");
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							},
							getRow : function(_773, p) {
								var _774 = (typeof p == "object") ? p
										.attr("datagrid-row-index") : p;
								return $.data(_773, "datagrid").data.rows[parseInt(_774)];
							},
							getRows : function(_775) {
								return $(_775).datagrid("getRows");
							}
						},
						view : _728,
						onBeforeLoad : function(_776) {
						},
						onLoadSuccess : function() {
						},
						onLoadError : function() {
						},
						onClickRow : function(_777, _778) {
						},
						onDblClickRow : function(_779, _77a) {
						},
						onClickCell : function(_77b, _77c, _77d) {
						},
						onDblClickCell : function(_77e, _77f, _780) {
						},
						onBeforeSortColumn : function(sort, _781) {
						},
						onSortColumn : function(sort, _782) {
						},
						onResizeColumn : function(_783, _784) {
						},
						onBeforeSelect : function(_785, _786) {
						},
						onSelect : function(_787, _788) {
						},
						onBeforeUnselect : function(_789, _78a) {
						},
						onUnselect : function(_78b, _78c) {
						},
						onSelectAll : function(rows) {
						},
						onUnselectAll : function(rows) {
						},
						onBeforeCheck : function(_78d, _78e) {
						},
						onCheck : function(_78f, _790) {
						},
						onBeforeUncheck : function(_791, _792) {
						},
						onUncheck : function(_793, _794) {
						},
						onCheckAll : function(rows) {
						},
						onUncheckAll : function(rows) {
						},
						onBeforeEdit : function(_795, _796) {
						},
						onBeginEdit : function(_797, _798) {
						},
						onEndEdit : function(_799, _79a, _79b) {
						},
						onAfterEdit : function(_79c, _79d, _79e) {
						},
						onCancelEdit : function(_79f, _7a0) {
						},
						onHeaderContextMenu : function(e, _7a1) {
						},
						onRowContextMenu : function(e, _7a2, _7a3) {
						}
					});
})(jQuery);
/**
 * propertygrid - jQuery EasyUI
 * 
 * Dependencies:
 * 	 datagrid
 * 
 */
(function($){
	var currTarget;
	$(document).unbind('.propertygrid').bind('mousedown.propertygrid', function(e){
		var p = $(e.target).closest('div.datagrid-view,div.combo-panel');
		if (p.length){return;}
		stopEditing(currTarget);
		currTarget = undefined;
	});
	
	function buildGrid(target){
		var state = $.data(target, 'propertygrid');
		var opts = $.data(target, 'propertygrid').options;
		$(target).datagrid($.extend({}, opts, {
			cls:'propertygrid',
			view:(opts.showGroup ? opts.groupView : opts.view),
			onBeforeEdit:function(index, row){
				if (opts.onBeforeEdit.call(target, index, row) == false){return false;}
				var dg = $(this);
				var row = dg.datagrid('getRows')[index];
				var col = dg.datagrid('getColumnOption', 'value');
				col.editor = row.editor;
			},
			onClickCell:function(index, field, value){
				if (currTarget != this){
					stopEditing(currTarget);
					currTarget = this;
				}
				if (opts.editIndex != index){
					stopEditing(currTarget);
					$(this).datagrid('beginEdit', index);
					var ed = $(this).datagrid('getEditor', {index:index,field:field});
					if (!ed){
						ed = $(this).datagrid('getEditor', {index:index,field:'value'});
					}
					if (ed){
						var t = $(ed.target);
						var input = t.data('textbox') ? t.textbox('textbox') : t;
						input.focus();
						opts.editIndex = index;
					}
				}
				opts.onClickCell.call(target, index, field, value);
			},
			loadFilter:function(data){
				stopEditing(this);
				return opts.loadFilter.call(this, data);
			}
		}));
	}
	
	function stopEditing(target){
		var t = $(target);
		if (!t.length){return}
		var opts = $.data(target, 'propertygrid').options;
		opts.finder.getTr(target, null, 'editing').each(function(){
			var index = parseInt($(this).attr('datagrid-row-index'));
			if (t.datagrid('validateRow', index)){
				t.datagrid('endEdit', index);
			} else {
				t.datagrid('cancelEdit', index);
			}
		});
	}
	
	$.fn.propertygrid = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.propertygrid.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.datagrid(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'propertygrid');
			if (state){
				$.extend(state.options, options);
			} else {
				var opts = $.extend({}, $.fn.propertygrid.defaults, $.fn.propertygrid.parseOptions(this), options);
				opts.frozenColumns = $.extend(true, [], opts.frozenColumns);
				opts.columns = $.extend(true, [], opts.columns);
				$.data(this, 'propertygrid', {
					options: opts
				});
			}
			buildGrid(this);
		});
	}
	
	$.fn.propertygrid.methods = {
		options: function(jq){
			return $.data(jq[0], 'propertygrid').options;
		}
	};
	
	$.fn.propertygrid.parseOptions = function(target){
		return $.extend({}, $.fn.datagrid.parseOptions(target), $.parser.parseOptions(target,[{showGroup:'boolean'}]));
	};
	
	// the group view definition
	var groupview = $.extend({}, $.fn.datagrid.defaults.view, {
		render: function(target, container, frozen){
			var table = [];
			var groups = this.groups;
			for(var i=0; i<groups.length; i++){
				table.push(this.renderGroup.call(this, target, i, groups[i], frozen));
			}
			$(container).html(table.join(''));
		},
		
		renderGroup: function(target, groupIndex, group, frozen){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var fields = $(target).datagrid('getColumnFields', frozen);
			
			var table = [];
			table.push('<div class="datagrid-group" group-index=' + groupIndex + '>');
			table.push('<table cellspacing="0" cellpadding="0" border="0" style="height:100%"><tbody>');
			table.push('<tr>');
			if ((frozen && (opts.rownumbers || opts.frozenColumns.length)) ||
					(!frozen && !(opts.rownumbers || opts.frozenColumns.length))){
				table.push('<td style="border:0;text-align:center;width:25px"><span class="datagrid-row-expander datagrid-row-collapse" style="display:inline-block;width:16px;height:16px;cursor:pointer">&nbsp;</span></td>');
			}
			table.push('<td style="border:0;">');
			if (!frozen){
				table.push('<span class="datagrid-group-title">');
				table.push(opts.groupFormatter.call(target, group.value, group.rows));
				table.push('</span>');
			}
			table.push('</td>');
			table.push('</tr>');
			table.push('</tbody></table>');
			table.push('</div>');
			
			table.push('<table class="datagrid-btable" cellspacing="0" cellpadding="0" border="0"><tbody>');
			var index = group.startIndex;
			for(var j=0; j<group.rows.length; j++) {
				var css = opts.rowStyler ? opts.rowStyler.call(target, index, group.rows[j]) : '';
				var classValue = '';
				var styleValue = '';
				if (typeof css == 'string'){
					styleValue = css;
				} else if (css){
					classValue = css['class'] || '';
					styleValue = css['style'] || '';
				}
				
				var cls = 'class="datagrid-row ' + (index % 2 && opts.striped ? 'datagrid-row-alt ' : ' ') + classValue + '"';
				var style = styleValue ? 'style="' + styleValue + '"' : '';
				var rowId = state.rowIdPrefix + '-' + (frozen?1:2) + '-' + index;
				table.push('<tr id="' + rowId + '" datagrid-row-index="' + index + '" ' + cls + ' ' + style + '>');
				table.push(this.renderRow.call(this, target, fields, frozen, index, group.rows[j]));
				table.push('</tr>');
				index++;
			}
			table.push('</tbody></table>');
			return table.join('');
		},
		
		bindEvents: function(target){
			var state = $.data(target, 'datagrid');
			var dc = state.dc;
			var body = dc.body1.add(dc.body2);
			var clickHandler = ($.data(body[0],'events')||$._data(body[0],'events')).click[0].handler;
			body.unbind('click').bind('click', function(e){
				var tt = $(e.target);
				var expander = tt.closest('span.datagrid-row-expander');
				if (expander.length){
					var gindex = expander.closest('div.datagrid-group').attr('group-index');
					if (expander.hasClass('datagrid-row-collapse')){
						$(target).datagrid('collapseGroup', gindex);
					} else {
						$(target).datagrid('expandGroup', gindex);
					}
				} else {
					clickHandler(e);
				}
				e.stopPropagation();
			});
		},
		
		onBeforeRender: function(target, rows){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			
			initCss();
			
			var groups = [];
			for(var i=0; i<rows.length; i++){
				var row = rows[i];
				var group = getGroup(row[opts.groupField]);
				if (!group){
					group = {
						value: row[opts.groupField],
						rows: [row]
					};
					groups.push(group);
				} else {
					group.rows.push(row);
				}
			}
			
			var index = 0;
			var newRows = [];
			for(var i=0; i<groups.length; i++){
				var group = groups[i];
				group.startIndex = index;
				index += group.rows.length;
				newRows = newRows.concat(group.rows);
			}
			
			state.data.rows = newRows;
			this.groups = groups;
			
			var that = this;
			setTimeout(function(){
				that.bindEvents(target);
			},0);
			
			function getGroup(value){
				for(var i=0; i<groups.length; i++){
					var group = groups[i];
					if (group.value == value){
						return group;
					}
				}
				return null;
			}
			function initCss(){
				if (!$('#datagrid-group-style').length){
					$('head').append(
						'<style id="datagrid-group-style">' +
						'.datagrid-group{height:25px;overflow:hidden;font-weight:bold;border-bottom:1px solid #ccc;}' +
						'</style>'
					);
				}
			}
		}
	});

	$.extend($.fn.datagrid.methods, {
	    expandGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-expand')){
	                expander.removeClass('datagrid-row-expand').addClass('datagrid-row-collapse');
	                group.next('table').show();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    },
	    collapseGroup:function(jq, groupIndex){
	        return jq.each(function(){
	            var view = $.data(this, 'datagrid').dc.view;
	            var group = view.find(groupIndex!=undefined ? 'div.datagrid-group[group-index="'+groupIndex+'"]' : 'div.datagrid-group');
	            var expander = group.find('span.datagrid-row-expander');
	            if (expander.hasClass('datagrid-row-collapse')){
	                expander.removeClass('datagrid-row-collapse').addClass('datagrid-row-expand');
	                group.next('table').hide();
	            }
	            $(this).datagrid('fixRowHeight');
	        });
	    }
	});

	$.extend(groupview, {
		refreshGroupTitle: function(target, groupIndex){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var group = this.groups[groupIndex];
			var span = dc.body2.children('div.datagrid-group[group-index=' + groupIndex + ']').find('span.datagrid-group-title');
			span.html(opts.groupFormatter.call(target, group.value, group.rows));
		},
		
		insertRow: function(target, index, row){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var group = null;
			var groupIndex;
			
			for(var i=0; i<this.groups.length; i++){
				if (this.groups[i].value == row[opts.groupField]){
					group = this.groups[i];
					groupIndex = i;
					break;
				}
			}
			if (group){
				if (index == undefined || index == null){
					index = state.data.rows.length;
				}
				if (index < group.startIndex){
					index = group.startIndex;
				} else if (index > group.startIndex + group.rows.length){
					index = group.startIndex + group.rows.length;
				}
				$.fn.datagrid.defaults.view.insertRow.call(this, target, index, row);
				
				if (index >= group.startIndex + group.rows.length){
					_moveTr(index, true);
					_moveTr(index, false);
				}
				group.rows.splice(index - group.startIndex, 0, row);
			} else {
				group = {
					value: row[opts.groupField],
					rows: [row],
					startIndex: state.data.rows.length
				}
				groupIndex = this.groups.length;
				dc.body1.append(this.renderGroup.call(this, target, groupIndex, group, true));
				dc.body2.append(this.renderGroup.call(this, target, groupIndex, group, false));
				this.groups.push(group);
				state.data.rows.push(row);
			}
			
			this.refreshGroupTitle(target, groupIndex);
			
			function _moveTr(index,frozen){
				var serno = frozen?1:2;
				var prevTr = opts.finder.getTr(target, index-1, 'body', serno);
				var tr = opts.finder.getTr(target, index, 'body', serno);
				tr.insertAfter(prevTr);
			}
		},
		
		updateRow: function(target, index, row){
			var opts = $.data(target, 'datagrid').options;
			$.fn.datagrid.defaults.view.updateRow.call(this, target, index, row);
			var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
			var groupIndex = parseInt(tb.prev().attr('group-index'));
			this.refreshGroupTitle(target, groupIndex);
		},
		
		deleteRow: function(target, index){
			var state = $.data(target, 'datagrid');
			var opts = state.options;
			var dc = state.dc;
			var body = dc.body1.add(dc.body2);
			
			var tb = opts.finder.getTr(target, index, 'body', 2).closest('table.datagrid-btable');
			var groupIndex = parseInt(tb.prev().attr('group-index'));
			
			$.fn.datagrid.defaults.view.deleteRow.call(this, target, index);
			
			var group = this.groups[groupIndex];
			if (group.rows.length > 1){
				group.rows.splice(index-group.startIndex, 1);
				this.refreshGroupTitle(target, groupIndex);
			} else {
				body.children('div.datagrid-group[group-index='+groupIndex+']').remove();
				for(var i=groupIndex+1; i<this.groups.length; i++){
					body.children('div.datagrid-group[group-index='+i+']').attr('group-index', i-1);
				}
				this.groups.splice(groupIndex, 1);
			}
			
			var index = 0;
			for(var i=0; i<this.groups.length; i++){
				var group = this.groups[i];
				group.startIndex = index;
				index += group.rows.length;
			}
		}
	});

	// end of group view definition
	
	$.fn.propertygrid.defaults = $.extend({}, $.fn.datagrid.defaults, {
		singleSelect:true,
		remoteSort:false,
		fitColumns:true,
		loadMsg:'',
		frozenColumns:[[
		    {field:'f',width:16,resizable:false}
		]],
		columns:[[
		    {field:'name',title:'Name',width:100,sortable:true},
		    {field:'value',title:'Value',width:100,resizable:false}
		]],
		
		showGroup:false,
		groupView:groupview,
		groupField:'group',
		groupFormatter:function(fvalue,rows){return fvalue}
	});
})(jQuery);

(function($) {
	function _7f3(_7f4) {
		var _7f5 = $.data(_7f4, "treegrid");
		var opts = _7f5.options;
		$(_7f4)
				.datagrid(
						$
								.extend(
										{},
										opts,
										{
											url : null,
											data : null,
											loader : function() {
												return false;
											},
											onBeforeLoad : function() {
												return false;
											},
											onLoadSuccess : function() {
											},
											onResizeColumn : function(_7f6,
													_7f7) {
												_812(_7f4);
												opts.onResizeColumn.call(_7f4,
														_7f6, _7f7);
											},
											onBeforeSortColumn : function(sort,
													_7f8) {
												if (opts.onBeforeSortColumn
														.call(_7f4, sort, _7f8) == false) {
													return false;
												}
											},
											onSortColumn : function(sort, _7f9) {
												opts.sortName = sort;
												opts.sortOrder = _7f9;
												if (opts.remoteSort) {
													_811(_7f4);
												} else {
													var data = $(_7f4)
															.treegrid("getData");
													_828(_7f4, 0, data);
												}
												opts.onSortColumn.call(_7f4,
														sort, _7f9);
											},
											onBeforeEdit : function(_7fa, row) {
												if (opts.onBeforeEdit.call(
														_7f4, row) == false) {
													return false;
												}
											},
											onAfterEdit : function(_7fb, row,
													_7fc) {
												opts.onAfterEdit.call(_7f4,
														row, _7fc);
											},
											onCancelEdit : function(_7fd, row) {
												opts.onCancelEdit.call(_7f4,
														row);
											},
											onBeforeSelect : function(_7fe) {
												if (opts.onBeforeSelect.call(
														_7f4, find(_7f4, _7fe)) == false) {
													return false;
												}
											},
											onSelect : function(_7ff) {
												opts.onSelect.call(_7f4, find(
														_7f4, _7ff));
											},
											onBeforeUnselect : function(_800) {
												if (opts.onBeforeUnselect.call(
														_7f4, find(_7f4, _800)) == false) {
													return false;
												}
											},
											onUnselect : function(_801) {
												opts.onUnselect.call(_7f4,
														find(_7f4, _801));
											},
											onBeforeCheck : function(_802) {
												if (opts.onBeforeCheck.call(
														_7f4, find(_7f4, _802)) == false) {
													return false;
												}
											},
											onCheck : function(_803) {
												opts.onCheck.call(_7f4, find(
														_7f4, _803));
											},
											onBeforeUncheck : function(_804) {
												if (opts.onBeforeUncheck.call(
														_7f4, find(_7f4, _804)) == false) {
													return false;
												}
											},
											onUncheck : function(_805) {
												opts.onUncheck.call(_7f4, find(
														_7f4, _805));
											},
											onClickRow : function(_806) {
												opts.onClickRow.call(_7f4,
														find(_7f4, _806));
											},
											onDblClickRow : function(_807) {
												opts.onDblClickRow.call(_7f4,
														find(_7f4, _807));
											},
											onClickCell : function(_808, _809) {
												opts.onClickCell.call(_7f4,
														_809, find(_7f4, _808));
											},
											onDblClickCell : function(_80a,
													_80b) {
												opts.onDblClickCell.call(_7f4,
														_80b, find(_7f4, _80a));
											},
											onRowContextMenu : function(e, _80c) {
												opts.onContextMenu.call(_7f4,
														e, find(_7f4, _80c));
											}
										}));
		if (!opts.columns) {
			var _80d = $.data(_7f4, "datagrid").options;
			opts.columns = _80d.columns;
			opts.frozenColumns = _80d.frozenColumns;
		}
		_7f5.dc = $.data(_7f4, "datagrid").dc;
		if (opts.pagination) {
			var _80e = $(_7f4).datagrid("getPager");
			_80e.pagination({
				pageNumber : opts.pageNumber,
				pageSize : opts.pageSize,
				pageList : opts.pageList,
				onSelectPage : function(_80f, _810) {
					opts.pageNumber = _80f;
					opts.pageSize = _810;
					_811(_7f4);
				}
			});
			opts.pageSize = _80e.pagination("options").pageSize;
		}
	}
	;
	function _812(_813, _814) {
		var opts = $.data(_813, "datagrid").options;
		var dc = $.data(_813, "datagrid").dc;
		if (!dc.body1.is(":empty") && (!opts.nowrap || opts.autoRowHeight)) {
			if (_814 != undefined) {
				var _815 = _816(_813, _814);
				for ( var i = 0; i < _815.length; i++) {
					_817(_815[i][opts.idField]);
				}
			}
		}
		$(_813).datagrid("fixRowHeight", _814);
		function _817(_818) {
			var tr1 = opts.finder.getTr(_813, _818, "body", 1);
			var tr2 = opts.finder.getTr(_813, _818, "body", 2);
			tr1.css("height", "");
			tr2.css("height", "");
			var _819 = Math.max(tr1.height(), tr2.height());
			tr1.css("height", _819);
			tr2.css("height", _819);
		}
		;
	}
	;
	function _81a(_81b) {
		var dc = $.data(_81b, "datagrid").dc;
		var opts = $.data(_81b, "treegrid").options;
		if (!opts.rownumbers) {
			return;
		}
		dc.body1.find("div.datagrid-cell-rownumber").each(function(i) {
			$(this).html(i + 1);
		});
	}
	;
	function _81c(_81d) {
		return function(e) {
			$.fn.datagrid.defaults.rowEvents[_81d ? "mouseover" : "mouseout"]
					(e);
			var tt = $(e.target);
			var fn = _81d ? "addClass" : "removeClass";
			if (tt.hasClass("tree-hit")) {
				tt.hasClass("tree-expanded") ? tt[fn]("tree-expanded-hover")
						: tt[fn]("tree-collapsed-hover");
			}
		};
	}
	;
	function _81e(e) {
		var tt = $(e.target);
		if (tt.hasClass("tree-hit")) {
			var tr = tt.closest("tr.datagrid-row");
			var _81f = tr.closest("div.datagrid-view").children(".datagrid-f")[0];
			_820(_81f, tr.attr("node-id"));
		} else {
			$.fn.datagrid.defaults.rowEvents.click(e);
		}
	}
	;
	function _821(_822, _823) {
		var opts = $.data(_822, "treegrid").options;
		var tr1 = opts.finder.getTr(_822, _823, "body", 1);
		var tr2 = opts.finder.getTr(_822, _823, "body", 2);
		var _824 = $(_822).datagrid("getColumnFields", true).length
				+ (opts.rownumbers ? 1 : 0);
		var _825 = $(_822).datagrid("getColumnFields", false).length;
		_826(tr1, _824);
		_826(tr2, _825);
		function _826(tr, _827) {
			$(
					"<tr class=\"treegrid-tr-tree\">"
							+ "<td style=\"border:0px\" colspan=\"" + _827
							+ "\">" + "<div></div>" + "</td>" + "</tr>")
					.insertAfter(tr);
		}
		;
	}
	;
	function _828(_829, _82a, data, _82b) {
		var _82c = $.data(_829, "treegrid");
		var opts = _82c.options;
		var dc = _82c.dc;
		data = opts.loadFilter.call(_829, data, _82a);
		var node = find(_829, _82a);
		if (node) {
			var _82d = opts.finder.getTr(_829, _82a, "body", 1);
			var _82e = opts.finder.getTr(_829, _82a, "body", 2);
			var cc1 = _82d.next("tr.treegrid-tr-tree").children("td").children(
					"div");
			var cc2 = _82e.next("tr.treegrid-tr-tree").children("td").children(
					"div");
			if (!_82b) {
				node.children = [];
			}
		} else {
			var cc1 = dc.body1;
			var cc2 = dc.body2;
			if (!_82b) {
				_82c.data = [];
			}
		}
		if (!_82b) {
			cc1.empty();
			cc2.empty();
		}
		if (opts.view.onBeforeRender) {
			opts.view.onBeforeRender.call(opts.view, _829, _82a, data);
		}
		opts.view.render.call(opts.view, _829, cc1, true);
		opts.view.render.call(opts.view, _829, cc2, false);
		if (opts.showFooter) {
			opts.view.renderFooter.call(opts.view, _829, dc.footer1, true);
			opts.view.renderFooter.call(opts.view, _829, dc.footer2, false);
		}
		if (opts.view.onAfterRender) {
			opts.view.onAfterRender.call(opts.view, _829);
		}
		if (!_82a && opts.pagination) {
			var _82f = $.data(_829, "treegrid").total;
			var _830 = $(_829).datagrid("getPager");
			if (_830.pagination("options").total != _82f) {
				_830.pagination({
					total : _82f
				});
			}
		}
		_812(_829);
		_81a(_829);
		$(_829).treegrid("showLines");
		$(_829).treegrid("setSelectionState");
		$(_829).treegrid("autoSizeColumn");
		opts.onLoadSuccess.call(_829, node, data);
	}
	;
	function _811(_831, _832, _833, _834, _835) {
		var opts = $.data(_831, "treegrid").options;
		var body = $(_831).datagrid("getPanel").find("div.datagrid-body");
		if (_833) {
			opts.queryParams = _833;
		}
		var _836 = $.extend({}, opts.queryParams);
		if (opts.pagination) {
			$.extend(_836, {
				page : opts.pageNumber,
				rows : opts.pageSize
			});
		}
		if (opts.sortName) {
			$.extend(_836, {
				sort : opts.sortName,
				order : opts.sortOrder
			});
		}
		var row = find(_831, _832);
		if (opts.onBeforeLoad.call(_831, row, _836) == false) {
			return;
		}
		var _837 = body.find("tr[node-id=\"" + _832 + "\"] span.tree-folder");
		_837.addClass("tree-loading");
		$(_831).treegrid("loading");
		var _838 = opts.loader.call(_831, _836, function(data) {
			_837.removeClass("tree-loading");
			$(_831).treegrid("loaded");
			_828(_831, _832, data, _834);
			if (_835) {
				_835();
			}
		}, function() {
			_837.removeClass("tree-loading");
			$(_831).treegrid("loaded");
			opts.onLoadError.apply(_831, arguments);
			if (_835) {
				_835();
			}
		});
		if (_838 == false) {
			_837.removeClass("tree-loading");
			$(_831).treegrid("loaded");
		}
	}
	;
	function _839(_83a) {
		var rows = _83b(_83a);
		if (rows.length) {
			return rows[0];
		} else {
			return null;
		}
	}
	;
	function _83b(_83c) {
		return $.data(_83c, "treegrid").data;
	}
	;
	function _83d(_83e, _83f) {
		var row = find(_83e, _83f);
		if (row._parentId) {
			return find(_83e, row._parentId);
		} else {
			return null;
		}
	}
	;
	function _816(_840, _841) {
		var opts = $.data(_840, "treegrid").options;
		var body = $(_840).datagrid("getPanel").find(
				"div.datagrid-view2 div.datagrid-body");
		var _842 = [];
		if (_841) {
			_843(_841);
		} else {
			var _844 = _83b(_840);
			for ( var i = 0; i < _844.length; i++) {
				_842.push(_844[i]);
				_843(_844[i][opts.idField]);
			}
		}
		function _843(_845) {
			var _846 = find(_840, _845);
			if (_846 && _846.children) {
				for ( var i = 0, len = _846.children.length; i < len; i++) {
					var _847 = _846.children[i];
					_842.push(_847);
					_843(_847[opts.idField]);
				}
			}
		}
		;
		return _842;
	}
	;
	function _848(_849, _84a) {
		if (!_84a) {
			return 0;
		}
		var opts = $.data(_849, "treegrid").options;
		var view = $(_849).datagrid("getPanel").children("div.datagrid-view");
		var node = view.find("div.datagrid-body tr[node-id=\"" + _84a + "\"]")
				.children("td[field=\"" + opts.treeField + "\"]");
		return node.find("span.tree-indent,span.tree-hit").length;
	}
	;
	function find(_84b, _84c) {
		var opts = $.data(_84b, "treegrid").options;
		var data = $.data(_84b, "treegrid").data;
		var cc = [ data ];
		while (cc.length) {
			var c = cc.shift();
			for ( var i = 0; i < c.length; i++) {
				var node = c[i];
				if (node[opts.idField] == _84c) {
					return node;
				} else {
					if (node["children"]) {
						cc.push(node["children"]);
					}
				}
			}
		}
		return null;
	}
	;
	function _84d(_84e, _84f) {
		var opts = $.data(_84e, "treegrid").options;
		var row = find(_84e, _84f);
		var tr = opts.finder.getTr(_84e, _84f);
		var hit = tr.find("span.tree-hit");
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-collapsed")) {
			return;
		}
		if (opts.onBeforeCollapse.call(_84e, row) == false) {
			return;
		}
		hit.removeClass("tree-expanded tree-expanded-hover").addClass(
				"tree-collapsed");
		hit.next().removeClass("tree-folder-open");
		row.state = "closed";
		tr = tr.next("tr.treegrid-tr-tree");
		var cc = tr.children("td").children("div");
		if (opts.animate) {
			cc.slideUp("normal", function() {
				$(_84e).treegrid("autoSizeColumn");
				_812(_84e, _84f);
				opts.onCollapse.call(_84e, row);
			});
		} else {
			cc.hide();
			$(_84e).treegrid("autoSizeColumn");
			_812(_84e, _84f);
			opts.onCollapse.call(_84e, row);
		}
	}
	;
	function _850(_851, _852) {
		var opts = $.data(_851, "treegrid").options;
		var tr = opts.finder.getTr(_851, _852);
		var hit = tr.find("span.tree-hit");
		var row = find(_851, _852);
		if (hit.length == 0) {
			return;
		}
		if (hit.hasClass("tree-expanded")) {
			return;
		}
		if (opts.onBeforeExpand.call(_851, row) == false) {
			return;
		}
		hit.removeClass("tree-collapsed tree-collapsed-hover").addClass(
				"tree-expanded");
		hit.next().addClass("tree-folder-open");
		var _853 = tr.next("tr.treegrid-tr-tree");
		if (_853.length) {
			var cc = _853.children("td").children("div");
			_854(cc);
		} else {
			_821(_851, row[opts.idField]);
			var _853 = tr.next("tr.treegrid-tr-tree");
			var cc = _853.children("td").children("div");
			cc.hide();
			var _855 = $.extend({}, opts.queryParams || {});
			_855.id = row[opts.idField];
			_811(_851, row[opts.idField], _855, true, function() {
				if (cc.is(":empty")) {
					_853.remove();
				} else {
					_854(cc);
				}
			});
		}
		function _854(cc) {
			row.state = "open";
			if (opts.animate) {
				cc.slideDown("normal", function() {
					$(_851).treegrid("autoSizeColumn");
					_812(_851, _852);
					opts.onExpand.call(_851, row);
				});
			} else {
				cc.show();
				$(_851).treegrid("autoSizeColumn");
				_812(_851, _852);
				opts.onExpand.call(_851, row);
			}
		}
		;
	}
	;
	function _820(_856, _857) {
		var opts = $.data(_856, "treegrid").options;
		var tr = opts.finder.getTr(_856, _857);
		var hit = tr.find("span.tree-hit");
		if (hit.hasClass("tree-expanded")) {
			_84d(_856, _857);
		} else {
			_850(_856, _857);
		}
	}
	;
	function _858(_859, _85a) {
		var opts = $.data(_859, "treegrid").options;
		var _85b = _816(_859, _85a);
		if (_85a) {
			_85b.unshift(find(_859, _85a));
		}
		for ( var i = 0; i < _85b.length; i++) {
			_84d(_859, _85b[i][opts.idField]);
		}
	}
	;
	function _85c(_85d, _85e) {
		var opts = $.data(_85d, "treegrid").options;
		var _85f = _816(_85d, _85e);
		if (_85e) {
			_85f.unshift(find(_85d, _85e));
		}
		for ( var i = 0; i < _85f.length; i++) {
			_850(_85d, _85f[i][opts.idField]);
		}
	}
	;
	function _860(_861, _862) {
		var opts = $.data(_861, "treegrid").options;
		var ids = [];
		var p = _83d(_861, _862);
		while (p) {
			var id = p[opts.idField];
			ids.unshift(id);
			p = _83d(_861, id);
		}
		for ( var i = 0; i < ids.length; i++) {
			_850(_861, ids[i]);
		}
	}
	;
	function _863(_864, _865) {
		var opts = $.data(_864, "treegrid").options;
		if (_865.parent) {
			var tr = opts.finder.getTr(_864, _865.parent);
			if (tr.next("tr.treegrid-tr-tree").length == 0) {
				_821(_864, _865.parent);
			}
			var cell = tr.children("td[field=\"" + opts.treeField + "\"]")
					.children("div.datagrid-cell");
			var _866 = cell.children("span.tree-icon");
			if (_866.hasClass("tree-file")) {
				_866.removeClass("tree-file").addClass(
						"tree-folder tree-folder-open");
				var hit = $("<span class=\"tree-hit tree-expanded\"></span>")
						.insertBefore(_866);
				if (hit.prev().length) {
					hit.prev().remove();
				}
			}
		}
		_828(_864, _865.parent, _865.data, true);
	}
	;
	function _867(_868, _869) {
		var ref = _869.before || _869.after;
		var opts = $.data(_868, "treegrid").options;
		var _86a = _83d(_868, ref);
		_863(_868, {
			parent : (_86a ? _86a[opts.idField] : null),
			data : [ _869.data ]
		});
		var _86b = _86a ? _86a.children : $(_868).treegrid("getRoots");
		for ( var i = 0; i < _86b.length; i++) {
			if (_86b[i][opts.idField] == ref) {
				var _86c = _86b[_86b.length - 1];
				_86b.splice(_869.before ? i : (i + 1), 0, _86c);
				_86b.splice(_86b.length - 1, 1);
				break;
			}
		}
		_86d(true);
		_86d(false);
		_81a(_868);
		$(_868).treegrid("showLines");
		function _86d(_86e) {
			var _86f = _86e ? 1 : 2;
			var tr = opts.finder.getTr(_868, _869.data[opts.idField], "body",
					_86f);
			var _870 = tr.closest("table.datagrid-btable");
			tr = tr.parent().children();
			var dest = opts.finder.getTr(_868, ref, "body", _86f);
			if (_869.before) {
				tr.insertBefore(dest);
			} else {
				var sub = dest.next("tr.treegrid-tr-tree");
				tr.insertAfter(sub.length ? sub : dest);
			}
			_870.remove();
		}
		;
	}
	;
	function _871(_872, _873) {
		var _874 = $.data(_872, "treegrid");
		$(_872).datagrid("deleteRow", _873);
		_81a(_872);
		_874.total -= 1;
		$(_872).datagrid("getPager").pagination("refresh", {
			total : _874.total
		});
		$(_872).treegrid("showLines");
	}
	;
	function _875(_876) {
		var t = $(_876);
		var opts = t.treegrid("options");
		if (opts.lines) {
			t.treegrid("getPanel").addClass("tree-lines");
		} else {
			t.treegrid("getPanel").removeClass("tree-lines");
			return;
		}
		t.treegrid("getPanel").find("span.tree-indent").removeClass(
				"tree-line tree-join tree-joinbottom");
		t.treegrid("getPanel").find("div.datagrid-cell").removeClass(
				"tree-node-last tree-root-first tree-root-one");
		var _877 = t.treegrid("getRoots");
		if (_877.length > 1) {
			_878(_877[0]).addClass("tree-root-first");
		} else {
			if (_877.length == 1) {
				_878(_877[0]).addClass("tree-root-one");
			}
		}
		_879(_877);
		_87a(_877);
		function _879(_87b) {
			$.map(_87b, function(node) {
				if (node.children && node.children.length) {
					_879(node.children);
				} else {
					var cell = _878(node);
					cell.find(".tree-icon").prev().addClass("tree-join");
				}
			});
			if (_87b.length) {
				var cell = _878(_87b[_87b.length - 1]);
				cell.addClass("tree-node-last");
				cell.find(".tree-join").removeClass("tree-join").addClass(
						"tree-joinbottom");
			}
		}
		;
		function _87a(_87c) {
			$.map(_87c, function(node) {
				if (node.children && node.children.length) {
					_87a(node.children);
				}
			});
			for ( var i = 0; i < _87c.length - 1; i++) {
				var node = _87c[i];
				var _87d = t.treegrid("getLevel", node[opts.idField]);
				var tr = opts.finder.getTr(_876, node[opts.idField]);
				var cc = tr.next().find(
						"tr.datagrid-row td[field=\"" + opts.treeField
								+ "\"] div.datagrid-cell");
				cc.find("span:eq(" + (_87d - 1) + ")").addClass("tree-line");
			}
		}
		;
		function _878(node) {
			var tr = opts.finder.getTr(_876, node[opts.idField]);
			var cell = tr.find("td[field=\"" + opts.treeField
					+ "\"] div.datagrid-cell");
			return cell;
		}
		;
	}
	;
	$.fn.treegrid = function(_87e, _87f) {
		if (typeof _87e == "string") {
			var _880 = $.fn.treegrid.methods[_87e];
			if (_880) {
				return _880(this, _87f);
			} else {
				return this.datagrid(_87e, _87f);
			}
		}
		_87e = _87e || {};
		return this.each(function() {
			var _881 = $.data(this, "treegrid");
			if (_881) {
				$.extend(_881.options, _87e);
			} else {
				_881 = $.data(this, "treegrid", {
					options : $.extend({}, $.fn.treegrid.defaults,
							$.fn.treegrid.parseOptions(this), _87e),
					data : []
				});
			}
			_7f3(this);
			if (_881.options.data) {
				$(this).treegrid("loadData", _881.options.data);
			}
			_811(this);
		});
	};
	$.fn.treegrid.methods = {
		options : function(jq) {
			return $.data(jq[0], "treegrid").options;
		},
		resize : function(jq, _882) {
			return jq.each(function() {
				$(this).datagrid("resize", _882);
			});
		},
		fixRowHeight : function(jq, _883) {
			return jq.each(function() {
				_812(this, _883);
			});
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				_828(this, data.parent, data);
			});
		},
		load : function(jq, _884) {
			return jq.each(function() {
				$(this).treegrid("options").pageNumber = 1;
				$(this).treegrid("getPager").pagination({
					pageNumber : 1
				});
				$(this).treegrid("reload", _884);
			});
		},
		reload : function(jq, id) {
			return jq.each(function() {
				var opts = $(this).treegrid("options");
				var _885 = {};
				if (typeof id == "object") {
					_885 = id;
				} else {
					_885 = $.extend({}, opts.queryParams);
					_885.id = id;
				}
				if (_885.id) {
					var node = $(this).treegrid("find", _885.id);
					if (node.children) {
						node.children.splice(0, node.children.length);
					}
					opts.queryParams = _885;
					var tr = opts.finder.getTr(this, _885.id);
					tr.next("tr.treegrid-tr-tree").remove();
					tr.find("span.tree-hit").removeClass(
							"tree-expanded tree-expanded-hover").addClass(
							"tree-collapsed");
					_850(this, _885.id);
				} else {
					_811(this, null, _885);
				}
			});
		},
		reloadFooter : function(jq, _886) {
			return jq.each(function() {
				var opts = $.data(this, "treegrid").options;
				var dc = $.data(this, "datagrid").dc;
				if (_886) {
					$.data(this, "treegrid").footer = _886;
				}
				if (opts.showFooter) {
					opts.view.renderFooter.call(opts.view, this, dc.footer1,
							true);
					opts.view.renderFooter.call(opts.view, this, dc.footer2,
							false);
					if (opts.view.onAfterRender) {
						opts.view.onAfterRender.call(opts.view, this);
					}
					$(this).treegrid("fixRowHeight");
				}
			});
		},
		getData : function(jq) {
			return $.data(jq[0], "treegrid").data;
		},
		getFooterRows : function(jq) {
			return $.data(jq[0], "treegrid").footer;
		},
		getRoot : function(jq) {
			return _839(jq[0]);
		},
		getRoots : function(jq) {
			return _83b(jq[0]);
		},
		getParent : function(jq, id) {
			return _83d(jq[0], id);
		},
		getChildren : function(jq, id) {
			return _816(jq[0], id);
		},
		getLevel : function(jq, id) {
			return _848(jq[0], id);
		},
		find : function(jq, id) {
			return find(jq[0], id);
		},
		isLeaf : function(jq, id) {
			var opts = $.data(jq[0], "treegrid").options;
			var tr = opts.finder.getTr(jq[0], id);
			var hit = tr.find("span.tree-hit");
			return hit.length == 0;
		},
		select : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("selectRow", id);
			});
		},
		unselect : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("unselectRow", id);
			});
		},
		collapse : function(jq, id) {
			return jq.each(function() {
				_84d(this, id);
			});
		},
		expand : function(jq, id) {
			return jq.each(function() {
				_850(this, id);
			});
		},
		toggle : function(jq, id) {
			return jq.each(function() {
				_820(this, id);
			});
		},
		collapseAll : function(jq, id) {
			return jq.each(function() {
				_858(this, id);
			});
		},
		expandAll : function(jq, id) {
			return jq.each(function() {
				_85c(this, id);
			});
		},
		expandTo : function(jq, id) {
			return jq.each(function() {
				_860(this, id);
			});
		},
		append : function(jq, _887) {
			return jq.each(function() {
				_863(this, _887);
			});
		},
		insert : function(jq, _888) {
			return jq.each(function() {
				_867(this, _888);
			});
		},
		remove : function(jq, id) {
			return jq.each(function() {
				_871(this, id);
			});
		},
		pop : function(jq, id) {
			var row = jq.treegrid("find", id);
			jq.treegrid("remove", id);
			return row;
		},
		refresh : function(jq, id) {
			return jq.each(function() {
				var opts = $.data(this, "treegrid").options;
				opts.view.refreshRow.call(opts.view, this, id);
			});
		},
		update : function(jq, _889) {
			return jq.each(function() {
				var opts = $.data(this, "treegrid").options;
				opts.view.updateRow.call(opts.view, this, _889.id, _889.row);
			});
		},
		beginEdit : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("beginEdit", id);
				$(this).treegrid("fixRowHeight", id);
			});
		},
		endEdit : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("endEdit", id);
			});
		},
		cancelEdit : function(jq, id) {
			return jq.each(function() {
				$(this).datagrid("cancelEdit", id);
			});
		},
		showLines : function(jq) {
			return jq.each(function() {
				_875(this);
			});
		}
	};
	$.fn.treegrid.parseOptions = function(_88a) {
		return $.extend({}, $.fn.datagrid.parseOptions(_88a), $.parser
				.parseOptions(_88a, [ "treeField", {
					animate : "boolean"
				} ]));
	};
	var _88b = $
			.extend(
					{},
					$.fn.datagrid.defaults.view,
					{
						render : function(_88c, _88d, _88e) {
							var opts = $.data(_88c, "treegrid").options;
							var _88f = $(_88c)
									.datagrid("getColumnFields", _88e);
							var _890 = $.data(_88c, "datagrid").rowIdPrefix;
							if (_88e) {
								if (!(opts.rownumbers || (opts.frozenColumns && opts.frozenColumns.length))) {
									return;
								}
							}
							var view = this;
							if (this.treeNodes && this.treeNodes.length) {
								var _891 = _892(_88e, this.treeLevel,
										this.treeNodes);
								$(_88d).append(_891.join(""));
							}
							function _892(_893, _894, _895) {
								var _896 = $(_88c).treegrid("getParent",
										_895[0][opts.idField]);
								var _897 = (_896 ? _896.children.length : $(
										_88c).treegrid("getRoots").length)
										- _895.length;
								var _898 = [ "<table class=\"datagrid-btable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
								for ( var i = 0; i < _895.length; i++) {
									var row = _895[i];
									if (row.state != "open"
											&& row.state != "closed") {
										row.state = "open";
									}
									var css = opts.rowStyler ? opts.rowStyler
											.call(_88c, row) : "";
									var _899 = "";
									var _89a = "";
									if (typeof css == "string") {
										_89a = css;
									} else {
										if (css) {
											_899 = css["class"] || "";
											_89a = css["style"] || "";
										}
									}
									var cls = "class=\"datagrid-row "
											+ (_897++ % 2 && opts.striped ? "datagrid-row-alt "
													: " ") + _899 + "\"";
									var _89b = _89a ? "style=\"" + _89a + "\""
											: "";
									var _89c = _890 + "-" + (_893 ? 1 : 2)
											+ "-" + row[opts.idField];
									_898.push("<tr id=\"" + _89c
											+ "\" node-id=\""
											+ row[opts.idField] + "\" " + cls
											+ " " + _89b + ">");
									_898 = _898.concat(view.renderRow.call(
											view, _88c, _88f, _893, _894, row));
									_898.push("</tr>");
									if (row.children && row.children.length) {
										var tt = _892(_893, _894 + 1,
												row.children);
										var v = row.state == "closed" ? "none"
												: "block";
										_898
												.push("<tr class=\"treegrid-tr-tree\"><td style=\"border:0px\" colspan="
														+ (_88f.length + (opts.rownumbers ? 1
																: 0))
														+ "><div style=\"display:"
														+ v + "\">");
										_898 = _898.concat(tt);
										_898.push("</div></td></tr>");
									}
								}
								_898.push("</tbody></table>");
								return _898;
							}
							;
						},
						renderFooter : function(_89d, _89e, _89f) {
							var opts = $.data(_89d, "treegrid").options;
							var rows = $.data(_89d, "treegrid").footer || [];
							var _8a0 = $(_89d)
									.datagrid("getColumnFields", _89f);
							var _8a1 = [ "<table class=\"datagrid-ftable\" cellspacing=\"0\" cellpadding=\"0\" border=\"0\"><tbody>" ];
							for ( var i = 0; i < rows.length; i++) {
								var row = rows[i];
								row[opts.idField] = row[opts.idField]
										|| ("foot-row-id" + i);
								_8a1
										.push("<tr class=\"datagrid-row\" node-id=\""
												+ row[opts.idField] + "\">");
								_8a1.push(this.renderRow.call(this, _89d, _8a0,
										_89f, 0, row));
								_8a1.push("</tr>");
							}
							_8a1.push("</tbody></table>");
							$(_89e).html(_8a1.join(""));
						},
						renderRow : function(_8a2, _8a3, _8a4, _8a5, row) {
							var opts = $.data(_8a2, "treegrid").options;
							var cc = [];
							if (_8a4 && opts.rownumbers) {
								cc
										.push("<td class=\"datagrid-td-rownumber\"><div class=\"datagrid-cell-rownumber\">0</div></td>");
							}
							for ( var i = 0; i < _8a3.length; i++) {
								var _8a6 = _8a3[i];
								var col = $(_8a2).datagrid("getColumnOption",
										_8a6);
								if (col) {
									var css = col.styler ? (col.styler(
											row[_8a6], row) || "") : "";
									var _8a7 = "";
									var _8a8 = "";
									if (typeof css == "string") {
										_8a8 = css;
									} else {
										if (cc) {
											_8a7 = css["class"] || "";
											_8a8 = css["style"] || "";
										}
									}
									var cls = _8a7 ? "class=\"" + _8a7 + "\""
											: "";
									var _8a9 = col.hidden ? "style=\"display:none;"
											+ _8a8 + "\""
											: (_8a8 ? "style=\"" + _8a8 + "\""
													: "");
									cc.push("<td field=\"" + _8a6 + "\" " + cls
											+ " " + _8a9 + ">");
									var _8a9 = "";
									if (!col.checkbox) {
										if (col.align) {
											_8a9 += "text-align:" + col.align
													+ ";";
										}
										if (!opts.nowrap) {
											_8a9 += "white-space:normal;height:auto;";
										} else {
											if (opts.autoRowHeight) {
												_8a9 += "height:auto;";
											}
										}
									}
									cc.push("<div style=\"" + _8a9 + "\" ");
									if (col.checkbox) {
										cc.push("class=\"datagrid-cell-check ");
									} else {
										cc.push("class=\"datagrid-cell "
												+ col.cellClass);
									}
									cc.push("\">");
									if (col.checkbox) {
										if (row.checked) {
											cc
													.push("<input type=\"checkbox\" checked=\"checked\"");
										} else {
											cc.push("<input type=\"checkbox\"");
										}
										cc.push(" name=\"" + _8a6 + "\" value=\"" + (row[_8a6] != undefined ? row[_8a6] : "") + "\">");
									} else {
										var val = null;
										if (col.formatter) {
											val = col.formatter(row[_8a6], row);
										} else {
											val = row[_8a6];
										}
										if (_8a6 == opts.treeField) {
											for ( var j = 0; j < _8a5; j++) {
												cc.push("<span class=\"tree-indent\"></span>");
											}
											if (row.state == "closed") {
												cc.push("<span class=\"tree-hit tree-collapsed\"></span>");
												cc.push("<span class=\"tree-icon tree-folder "
																+ (row.iconCls ? row.iconCls
																		: "")
																+ "\"></span>");
											} else {
												if (row.children
														&& row.children.length) {
													cc.push("<span class=\"tree-hit tree-expanded\"></span>");
													cc.push("<span class=\"tree-icon tree-folder tree-folder-open "
																	+ (row.iconCls ? row.iconCls : "")
																	+ "\"></span>");
												} else {
													cc.push("<span class=\"tree-indent\"></span>");
													cc.push("<span class=\"tree-icon tree-file "
																	+ (row.iconCls ? row.iconCls : "")
																	+ "\"></span>");
												}
											}
											cc.push("<span class=\"tree-title\">" + val + "</span>");
										} else {
											cc.push(val);
										}
									}
									cc.push("</div>");
									cc.push("</td>");
								}
							}
							return cc.join("");
						},
						refreshRow : function(_8aa, id) {
							this.updateRow.call(this, _8aa, id, {});
						},
						updateRow : function(_8ab, id, row) {
							var opts = $.data(_8ab, "treegrid").options;
							var _8ac = $(_8ab).treegrid("find", id);
							$.extend(_8ac, row);
							var _8ad = $(_8ab).treegrid("getLevel", id) - 1;
							var _8ae = opts.rowStyler ? opts.rowStyler.call(
									_8ab, _8ac) : "";
							var _8af = $.data(_8ab, "datagrid").rowIdPrefix;
							var _8b0 = _8ac[opts.idField];
							function _8b1(_8b2) {
								var _8b3 = $(_8ab).treegrid("getColumnFields",
										_8b2);
								var tr = opts.finder.getTr(_8ab, id, "body",
										(_8b2 ? 1 : 2));
								var _8b4 = tr.find(
										"div.datagrid-cell-rownumber").html();
								var _8b5 = tr
										.find(
												"div.datagrid-cell-check input[type=checkbox]")
										.is(":checked");
								tr.html(this.renderRow(_8ab, _8b3, _8b2, _8ad,
										_8ac));
								tr.attr("style", _8ae || "");
								tr.find("div.datagrid-cell-rownumber").html(
										_8b4);
								if (_8b5) {
									tr.find("div.datagrid-cell-check input[type=checkbox]")
											._propAttr("checked", true);
								}
								if (_8b0 != id) {
									tr.attr("id", _8af + "-" + (_8b2 ? 1 : 2)
											+ "-" + _8b0);
									tr.attr("node-id", _8b0);
								}
							}
							;
							_8b1.call(this, true);
							_8b1.call(this, false);
							$(_8ab).treegrid("fixRowHeight", id);
						},
						deleteRow : function(_8b6, id) {
							var opts = $.data(_8b6, "treegrid").options;
							var tr = opts.finder.getTr(_8b6, id);
							tr.next("tr.treegrid-tr-tree").remove();
							tr.remove();
							var _8b7 = del(id);
							if (_8b7) {
								if (_8b7.children.length == 0) {
									tr = opts.finder.getTr(_8b6,
											_8b7[opts.idField]);
									tr.next("tr.treegrid-tr-tree").remove();
									var cell = tr.children(
											"td[field=\"" + opts.treeField
													+ "\"]").children(
											"div.datagrid-cell");
									cell.find(".tree-icon").removeClass(
											"tree-folder")
											.addClass("tree-file");
									cell.find(".tree-hit").remove();
									$("<span class=\"tree-indent\"></span>")
											.prependTo(cell);
								}
							}
							function del(id) {
								var cc;
								var _8b8 = $(_8b6).treegrid("getParent", id);
								if (_8b8) {
									cc = _8b8.children;
								} else {
									cc = $(_8b6).treegrid("getData");
								}
								for ( var i = 0; i < cc.length; i++) {
									if (cc[i][opts.idField] == id) {
										cc.splice(i, 1);
										break;
									}
								}
								return _8b8;
							}
							;
						},
						onBeforeRender : function(_8b9, _8ba, data) {
							if ($.isArray(_8ba)) {
								data = {
									total : _8ba.length,
									rows : _8ba
								};
								_8ba = null;
							}
							if (!data) {
								return false;
							}
							var _8bb = $.data(_8b9, "treegrid");
							var opts = _8bb.options;
							if (data.length == undefined) {
								if (data.footer) {
									_8bb.footer = data.footer;
								}
								if (data.total) {
									_8bb.total = data.total;
								}
								data = this.transfer(_8b9, _8ba, data.rows);
							} else {
								function _8bc(_8bd, _8be) {
									for ( var i = 0; i < _8bd.length; i++) {
										var row = _8bd[i];
										row._parentId = _8be;
										if (row.children && row.children.length) {
											_8bc(row.children,
													row[opts.idField]);
										}
									}
								}
								;
								_8bc(data, _8ba);
							}
							var node = find(_8b9, _8ba);
							if (node) {
								if (node.children) {
									node.children = node.children.concat(data);
								} else {
									node.children = data;
								}
							} else {
								_8bb.data = _8bb.data.concat(data);
							}
							this.sort(_8b9, data);
							this.treeNodes = data;
							this.treeLevel = $(_8b9).treegrid("getLevel", _8ba);
						},
						sort : function(_8bf, data) {
							var opts = $.data(_8bf, "treegrid").options;
							if (!opts.remoteSort && opts.sortName) {
								var _8c0 = opts.sortName.split(",");
								var _8c1 = opts.sortOrder.split(",");
								_8c2(data);
							}
							function _8c2(rows) {
								rows.sort(function(r1, r2) {
									var r = 0;
									for ( var i = 0; i < _8c0.length; i++) {
										var sn = _8c0[i];
										var so = _8c1[i];
										var col = $(_8bf).treegrid(
												"getColumnOption", sn);
										var _8c3 = col.sorter
												|| function(a, b) {
													return a == b ? 0
															: (a > b ? 1 : -1);
												};
										r = _8c3(r1[sn], r2[sn])
												* (so == "asc" ? 1 : -1);
										if (r != 0) {
											return r;
										}
									}
									return r;
								});
								for ( var i = 0; i < rows.length; i++) {
									var _8c4 = rows[i].children;
									if (_8c4 && _8c4.length) {
										_8c2(_8c4);
									}
								}
							}
							;
						},
						transfer : function(_8c5, _8c6, data) {
							var opts = $.data(_8c5, "treegrid").options;
							var rows = [];
							for ( var i = 0; i < data.length; i++) {
								rows.push(data[i]);
							}
							var _8c7 = [];
							for ( var i = 0; i < rows.length; i++) {
								var row = rows[i];
								if (!_8c6) {
									if (!row._parentId) {
										_8c7.push(row);
										rows.splice(i, 1);
										i--;
									}
								} else {
									if (row._parentId == _8c6) {
										_8c7.push(row);
										rows.splice(i, 1);
										i--;
									}
								}
							}
							var toDo = [];
							for ( var i = 0; i < _8c7.length; i++) {
								toDo.push(_8c7[i]);
							}
							while (toDo.length) {
								var node = toDo.shift();
								for ( var i = 0; i < rows.length; i++) {
									var row = rows[i];
									if (row._parentId == node[opts.idField]) {
										if (node.children) {
											node.children.push(row);
										} else {
											node.children = [ row ];
										}
										toDo.push(row);
										rows.splice(i, 1);
										i--;
									}
								}
							}
							return _8c7;
						}
					});
	$.fn.treegrid.defaults = $.extend({},
					$.fn.datagrid.defaults,
					{
						treeField : null,
						lines : false,
						animate : false,
						singleSelect : true,
						view : _88b,
						rowEvents : $.extend({},
								$.fn.datagrid.defaults.rowEvents, {
									mouseover : _81c(true),
									mouseout : _81c(false),
									click : _81e
								}),
						loader : function(_8c8, _8c9, _8ca) {
							var opts = $(this).treegrid("options");
							if (!opts.url) {
								return false;
							}
							$.ajax({
								type : opts.method,
								url : opts.url,
								data : _8c8,
								dataType : "json",
								success : function(data) {
									_8c9(data);
								},
								error : function() {
									_8ca.apply(this, arguments);
								}
							});
						},
						loadFilter : function(data, _8cb) {
							return data;
						},
						finder : {
							getTr : function(_8cc, id, type, _8cd) {
								type = type || "body";
								_8cd = _8cd || 0;
								var dc = $.data(_8cc, "datagrid").dc;
								if (_8cd == 0) {
									var opts = $.data(_8cc, "treegrid").options;
									var tr1 = opts.finder.getTr(_8cc, id, type,
											1);
									var tr2 = opts.finder.getTr(_8cc, id, type,
											2);
									return tr1.add(tr2);
								} else {
									if (type == "body") {
										var tr = $("#"
												+ $.data(_8cc, "datagrid").rowIdPrefix
												+ "-" + _8cd + "-" + id);
										if (!tr.length) {
											tr = (_8cd == 1 ? dc.body1
													: dc.body2)
													.find("tr[node-id=\"" + id
															+ "\"]");
										}
										return tr;
									} else {
										if (type == "footer") {
											return (_8cd == 1 ? dc.footer1
													: dc.footer2)
													.find("tr[node-id=\"" + id
															+ "\"]");
										} else {
											if (type == "selected") {
												return (_8cd == 1 ? dc.body1
														: dc.body2)
														.find("tr.datagrid-row-selected");
											} else {
												if (type == "highlight") {
													return (_8cd == 1 ? dc.body1
															: dc.body2)
															.find("tr.datagrid-row-over");
												} else {
													if (type == "checked") {
														return (_8cd == 1 ? dc.body1
																: dc.body2)
																.find("tr.datagrid-row-checked");
													} else {
														if (type == "last") {
															return (_8cd == 1 ? dc.body1
																	: dc.body2)
																	.find("tr:last[node-id]");
														} else {
															if (type == "allbody") {
																return (_8cd == 1 ? dc.body1
																		: dc.body2)
																		.find("tr[node-id]");
															} else {
																if (type == "allfooter") {
																	return (_8cd == 1 ? dc.footer1
																			: dc.footer2)
																			.find("tr[node-id]");
																}
															}
														}
													}
												}
											}
										}
									}
								}
							},
							getRow : function(_8ce, p) {
								var id = (typeof p == "object") ? p
										.attr("node-id") : p;
								return $(_8ce).treegrid("find", id);
							},
							getRows : function(_8cf) {
								return $(_8cf).treegrid("getChildren");
							}
						},
						onBeforeLoad : function(row, _8d0) {
						},
						onLoadSuccess : function(row, data) {
						},
						onLoadError : function() {
						},
						onBeforeCollapse : function(row) {
						},
						onCollapse : function(row) {
						},
						onBeforeExpand : function(row) {
						},
						onExpand : function(row) {
						},
						onClickRow : function(row) {
						},
						onDblClickRow : function(row) {
						},
						onClickCell : function(_8d1, row) {
						},
						onDblClickCell : function(_8d2, row) {
						},
						onContextMenu : function(e, row) {
						},
						onBeforeEdit : function(row) {
						},
						onAfterEdit : function(row, _8d3) {
						},
						onCancelEdit : function(row) {
						}
					});
})(jQuery);
(function($) {
	$(function() {
		$(document).unbind(".combo").bind(
				"mousedown.combo mousewheel.combo",
				function(e) {
					var p = $(e.target).closest("span.combo,div.combo-p");
					if (p.length) {
						_8d4(p);
						return;
					}
					$("body>div.combo-p>div.combo-panel:visible")
							.panel("close");
				});
	});
	function _8d5(_8d6) {
		var _8d7 = $.data(_8d6, "combo");
		var opts = _8d7.options;
		if (!_8d7.panel) {
			_8d7.panel = $("<div class=\"combo-panel\"></div>")
					.appendTo("body");
			_8d7.panel.panel({
				minWidth : opts.panelMinWidth,
				maxWidth : opts.panelMaxWidth,
				minHeight : opts.panelMinHeight,
				maxHeight : opts.panelMaxHeight,
				doSize : false,
				closed : true,
				cls : "combo-p",
				style : {
					position : "absolute",
					zIndex : 10
				},
				onOpen : function() {
					var _8d8 = $(this).panel("options").comboTarget;
					var _8d9 = $.data(_8d8, "combo");
					if (_8d9) {
						_8d9.options.onShowPanel.call(_8d8);
					}
				},
				onBeforeClose : function() {
					_8d4(this);
				},
				onClose : function() {
					var _8da = $(this).panel("options").comboTarget;
					var _8db = $.data(_8da, "combo");
					if (_8db) {
						_8db.options.onHidePanel.call(_8da);
					}
				}
			});
		}
		var _8dc = $.extend(true, [], opts.icons);
		if (opts.hasDownArrow) {
			_8dc.push({
				iconCls : "combo-arrow",
				handler : function(e) {
					_8e0(e.data.target);
				}
			});
		}
		$(_8d6).addClass("combo-f").textbox($.extend({}, opts, {
			icons : _8dc,
			onChange : function() {
			}
		}));
		$(_8d6).attr("comboName", $(_8d6).attr("textboxName"));
		_8d7.combo = $(_8d6).next();
		_8d7.combo.addClass("combo");
	}
	;
	function _8dd(_8de) {
		var _8df = $.data(_8de, "combo");
		var opts = _8df.options;
		var p = _8df.panel;
		if (p.is(":visible")) {
			p.panel("close");
		}
		if (!opts.cloned) {
			p.panel("destroy");
		}
		$(_8de).textbox("destroy");
	}
	;
	function _8e0(_8e1) {
		var _8e2 = $.data(_8e1, "combo").panel;
		if (_8e2.is(":visible")) {
			_8e3(_8e1);
		} else {
			var p = $(_8e1).closest("div.combo-panel");
			$("div.combo-panel:visible").not(_8e2).not(p).panel("close");
			$(_8e1).combo("showPanel");
		}
		$(_8e1).combo("textbox").focus();
	}
	;
	function _8d4(_8e4) {
		$(_8e4).find(".combo-f").each(function() {
			var p = $(this).combo("panel");
			if (p.is(":visible")) {
				p.panel("close");
			}
		});
	}
	;
	function _8e5(e) {
		var _8e6 = e.data.target;
		var _8e7 = $.data(_8e6, "combo");
		var opts = _8e7.options;
		var _8e8 = _8e7.panel;
		if (!opts.editable) {
			_8e0(_8e6);
		} else {
			var p = $(_8e6).closest("div.combo-panel");
			$("div.combo-panel:visible").not(_8e8).not(p).panel("close");
		}
	}
	;
	function _8e9(e) {
		var _8ea = e.data.target;
		var t = $(_8ea);
		var _8eb = t.data("combo");
		var opts = t.combo("options");
		switch (e.keyCode) {
		case 38:
			opts.keyHandler.up.call(_8ea, e);
			break;
		case 40:
			opts.keyHandler.down.call(_8ea, e);
			break;
		case 37:
			opts.keyHandler.left.call(_8ea, e);
			break;
		case 39:
			opts.keyHandler.right.call(_8ea, e);
			break;
		case 13:
			e.preventDefault();
			opts.keyHandler.enter.call(_8ea, e);
			return false;
		case 9:
		case 27:
			_8e3(_8ea);
			break;
		default:
			if (opts.editable) {
				if (_8eb.timer) {
					clearTimeout(_8eb.timer);
				}
				_8eb.timer = setTimeout(function() {
					var q = t.combo("getText");
					if (_8eb.previousText != q) {
						_8eb.previousText = q;
						t.combo("showPanel");
						opts.keyHandler.query.call(_8ea, q, e);
						t.combo("validate");
					}
				}, opts.delay);
			}
		}
	}
	;
	function _8ec(_8ed) {
		var _8ee = $.data(_8ed, "combo");
		var _8ef = _8ee.combo;
		var _8f0 = _8ee.panel;
		var opts = $(_8ed).combo("options");
		var _8f1 = _8f0.panel("options");
		_8f1.comboTarget = _8ed;
		if (_8f1.closed) {
			_8f0.panel("panel").show().css(
					{
						zIndex : ($.fn.menu ? $.fn.menu.defaults.zIndex++
								: $.fn.window.defaults.zIndex++),
						left : -999999
					});
			_8f0.panel("resize",
					{
						width : (opts.panelWidth ? opts.panelWidth : _8ef
								._outerWidth()),
						height : opts.panelHeight
					});
			_8f0.panel("panel").hide();
			_8f0.panel("open");
		}
		(function() {
			if (_8f0.is(":visible")) {
				_8f0.panel("move", {
					left : _8f2(),
					top : _8f3()
				});
				setTimeout(arguments.callee, 200);
			}
		})();
		function _8f2() {
			var left = _8ef.offset().left;
			if (opts.panelAlign == "right") {
				left += _8ef._outerWidth() - _8f0._outerWidth();
			}
			if (left + _8f0._outerWidth() > $(window)._outerWidth()
					+ $(document).scrollLeft()) {
				left = $(window)._outerWidth() + $(document).scrollLeft()
						- _8f0._outerWidth();
			}
			if (left < 0) {
				left = 0;
			}
			return left;
		}
		;
		function _8f3() {
			var top = _8ef.offset().top + _8ef._outerHeight();
			if (top + _8f0._outerHeight() > $(window)._outerHeight()
					+ $(document).scrollTop()) {
				top = _8ef.offset().top - _8f0._outerHeight();
			}
			if (top < $(document).scrollTop()) {
				top = _8ef.offset().top + _8ef._outerHeight();
			}
			return top;
		}
		;
	}
	;
	function _8e3(_8f4) {
		var _8f5 = $.data(_8f4, "combo").panel;
		_8f5.panel("close");
	}
	;
	function _8f6(_8f7) {
		var _8f8 = $.data(_8f7, "combo");
		var opts = _8f8.options;
		var _8f9 = _8f8.combo;
		$(_8f7).textbox("clear");
		if (opts.multiple) {
			_8f9.find(".textbox-value").remove();
		} else {
			_8f9.find(".textbox-value").val("");
		}
	}
	;
	function _8fa(_8fb, text) {
		var _8fc = $.data(_8fb, "combo");
		var _8fd = $(_8fb).textbox("getText");
		if (_8fd != text) {
			$(_8fb).textbox("setText", text);
			_8fc.previousText = text;
		}
	}
	;
	function _8fe(_8ff) {
		var _900 = [];
		var _901 = $.data(_8ff, "combo").combo;
		_901.find(".textbox-value").each(function() {
			_900.push($(this).val());
		});
		return _900;
	}
	;
	function _902(_903, _904) {
		var _905 = $.data(_903, "combo");
		var opts = _905.options;
		var _906 = _905.combo;
		if (!$.isArray(_904)) {
			_904 = _904.split(opts.separator);
		}
		var _907 = _8fe(_903);
		_906.find(".textbox-value").remove();
		var name = $(_903).attr("textboxName") || "";
		for ( var i = 0; i < _904.length; i++) {
			var _908 = $("<input type=\"hidden\" class=\"textbox-value\">")
					.appendTo(_906);
			_908.attr("name", name);
			if (opts.disabled) {
				_908.attr("disabled", "disabled");
			}
			_908.val(_904[i]);
		}
		var _909 = (function() {
			if (_907.length != _904.length) {
				return true;
			}
			var a1 = $.extend(true, [], _907);
			var a2 = $.extend(true, [], _904);
			a1.sort();
			a2.sort();
			for ( var i = 0; i < a1.length; i++) {
				if (a1[i] != a2[i]) {
					return true;
				}
			}
			return false;
		})();
		if (_909) {
			if (opts.multiple) {
				opts.onChange.call(_903, _904, _907);
			} else {
				opts.onChange.call(_903, _904[0], _907[0]);
			}
		}
	}
	;
	function _90a(_90b) {
		var _90c = _8fe(_90b);
		return _90c[0];
	}
	;
	function _90d(_90e, _90f) {
		_902(_90e, [ _90f ]);
	}
	;
	function _910(_911) {
		var opts = $.data(_911, "combo").options;
		var _912 = opts.onChange;
		opts.onChange = function() {
		};
		if (opts.multiple) {
			_902(_911, opts.value ? opts.value : []);
		} else {
			_90d(_911, opts.value);
		}
		opts.onChange = _912;
	}
	;
	$.fn.combo = function(_913, _914) {
		if (typeof _913 == "string") {
			var _915 = $.fn.combo.methods[_913];
			if (_915) {
				return _915(this, _914);
			} else {
				return this.textbox(_913, _914);
			}
		}
		_913 = _913 || {};
		return this.each(function() {
			var _916 = $.data(this, "combo");
			if (_916) {
				$.extend(_916.options, _913);
				if (_913.value != undefined) {
					_916.options.originalValue = _913.value;
				}
			} else {
				_916 = $.data(this, "combo", {
					options : $.extend({}, $.fn.combo.defaults, $.fn.combo
							.parseOptions(this), _913),
					previousText : ""
				});
				_916.options.originalValue = _916.options.value;
			}
			_8d5(this);
			_910(this);
		});
	};
	$.fn.combo.methods = {
		options : function(jq) {
			var opts = jq.textbox("options");
			return $.extend($.data(jq[0], "combo").options, {
				width : opts.width,
				height : opts.height,
				disabled : opts.disabled,
				readonly : opts.readonly
			});
		},
		cloneFrom : function(jq, from) {
			return jq.each(function() {
				$(this).textbox("cloneFrom", from);
				$.data(this, "combo", {
					options : $.extend(true, {
						cloned : true
					}, $(from).combo("options")),
					combo : $(this).next(),
					panel : $(from).combo("panel")
				});
				$(this).addClass("combo-f").attr("comboName",
						$(this).attr("textboxName"));
			});
		},
		panel : function(jq) {
			return $.data(jq[0], "combo").panel;
		},
		destroy : function(jq) {
			return jq.each(function() {
				_8dd(this);
			});
		},
		showPanel : function(jq) {
			return jq.each(function() {
				_8ec(this);
			});
		},
		hidePanel : function(jq) {
			return jq.each(function() {
				_8e3(this);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				_8f6(this);
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $.data(this, "combo").options;
				if (opts.multiple) {
					$(this).combo("setValues", opts.originalValue);
				} else {
					$(this).combo("setValue", opts.originalValue);
				}
			});
		},
		setText : function(jq, text) {
			return jq.each(function() {
				_8fa(this, text);
			});
		},
		getValues : function(jq) {
			return _8fe(jq[0]);
		},
		setValues : function(jq, _917) {
			return jq.each(function() {
				_902(this, _917);
			});
		},
		getValue : function(jq) {
			return _90a(jq[0]);
		},
		setValue : function(jq, _918) {
			return jq.each(function() {
				_90d(this, _918);
			});
		}
	};
	$.fn.combo.parseOptions = function(_919) {
		var t = $(_919);
		return $.extend({}, $.fn.textbox.parseOptions(_919), $.parser
				.parseOptions(_919, [ "separator", "panelAlign", {
					panelWidth : "number",
					hasDownArrow : "boolean",
					delay : "number",
					selectOnNavigation : "boolean"
				}, {
					panelMinWidth : "number",
					panelMaxWidth : "number",
					panelMinHeight : "number",
					panelMaxHeight : "number"
				} ]), {
			panelHeight : (t.attr("panelHeight") == "auto" ? "auto"
					: parseInt(t.attr("panelHeight")) || undefined),
			multiple : (t.attr("multiple") ? true : undefined)
		});
	};
	$.fn.combo.defaults = $.extend({}, $.fn.textbox.defaults, {
		inputEvents : {
			click : _8e5,
			keydown : _8e9,
			paste : _8e9,
			drop : _8e9
		},
		panelWidth : null,
		panelHeight : 200,
		panelMinWidth : null,
		panelMaxWidth : null,
		panelMinHeight : null,
		panelMaxHeight : null,
		panelAlign : "left",
		multiple : false,
		selectOnNavigation : true,
		separator : ",",
		hasDownArrow : true,
		delay : 200,
		keyHandler : {
			up : function(e) {
			},
			down : function(e) {
			},
			left : function(e) {
			},
			right : function(e) {
			},
			enter : function(e) {
			},
			query : function(q, e) {
			}
		},
		onShowPanel : function() {
		},
		onHidePanel : function() {
		},
		onChange : function(_91a, _91b) {
		}
	});
})(jQuery);
/**
 * combobox - jQuery EasyUI
 * 
 * Dependencies:
 *   combo
 * 
 */
(function($){
	var COMBOBOX_SERNO = 0;
	
	function getRowIndex(target, value){
		var state = $.data(target, 'combobox');
		var opts = state.options;
		var data = state.data;
		for(var i=0; i<data.length; i++){
			if (data[i][opts.valueField] == value){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * scroll panel to display the specified item
	 */
	function scrollTo(target, value){
		var opts = $.data(target, 'combobox').options;
		var panel = $(target).combo('panel');
		var item = opts.finder.getEl(target, value);
		if (item.length){
			if (item.position().top <= 0){
				var h = panel.scrollTop() + item.position().top;
				panel.scrollTop(h);
			} else if (item.position().top + item.outerHeight() > panel.height()){
				var h = panel.scrollTop() + item.position().top + item.outerHeight() - panel.height();
				panel.scrollTop(h);
			}
		}
	}
	
	function nav(target, dir){
		var opts = $.data(target, 'combobox').options;
		var panel = $(target).combobox('panel');
		var item = panel.children('div.combobox-item-hover');
		if (!item.length){
			item = panel.children('div.combobox-item-selected');
		}
		item.removeClass('combobox-item-hover');
		var firstSelector = 'div.combobox-item:visible:not(.combobox-item-disabled):first';
		var lastSelector = 'div.combobox-item:visible:not(.combobox-item-disabled):last';
		if (!item.length){
			item = panel.children(dir=='next' ? firstSelector : lastSelector);
//			item = panel.children('div.combobox-item:visible:' + (dir=='next'?'first':'last'));
		} else {
			if (dir == 'next'){
				item = item.nextAll(firstSelector);
//				item = item.nextAll('div.combobox-item:visible:first');
				if (!item.length){
					item = panel.children(firstSelector);
//					item = panel.children('div.combobox-item:visible:first');
				}
			} else {
				item = item.prevAll(firstSelector);
//				item = item.prevAll('div.combobox-item:visible:first');
				if (!item.length){
					item = panel.children(lastSelector);
//					item = panel.children('div.combobox-item:visible:last');
				}
			}
		}
		if (item.length){
			item.addClass('combobox-item-hover');
			var row = opts.finder.getRow(target, item);
			if (row){
				scrollTo(target, row[opts.valueField]);
				if (opts.selectOnNavigation){
					select(target, row[opts.valueField]);
				}
			}
		}
	}
	
	/**
	 * select the specified value
	 */
	function select(target, value){
		var opts = $.data(target, 'combobox').options;
		var values = $(target).combo('getValues');
		if ($.inArray(value+'', values) == -1){
			if (opts.multiple){
				values.push(value);
			} else {
				values = [value];
			}
			setValues(target, values);
			opts.onSelect.call(target, opts.finder.getRow(target, value));
		}
	}
	
	/**
	 * unselect the specified value
	 */
	function unselect(target, value){
		var opts = $.data(target, 'combobox').options;
		var values = $(target).combo('getValues');
		var index = $.inArray(value+'', values);
		if (index >= 0){
			values.splice(index, 1);
			setValues(target, values);
			opts.onUnselect.call(target, opts.finder.getRow(target, value));
		}
	}
	
	/**
	 * set values
	 */
	function setValues(target, values, remainText){
		var opts = $.data(target, 'combobox').options;
		var panel = $(target).combo('panel');
		
		if (!$.isArray(values)){values = values.split(opts.separator)}
		panel.find('div.combobox-item-selected').removeClass('combobox-item-selected');
		var vv = [], ss = [];
		for(var i=0; i<values.length; i++){
			var v = values[i];
			var s = v;
			opts.finder.getEl(target, v).addClass('combobox-item-selected');
			var row = opts.finder.getRow(target, v);
			if (row){
				s = row[opts.textField];
			}
			vv.push(v);
			ss.push(s);
		}
		
		$(target).combo('setValues', vv);
		if (!remainText){
			$(target).combo('setText', ss.join(opts.separator));
		}
	}
	
	/**
	 * load data, the old list items will be removed.
	 */
	function loadData(target, data, remainText){
		var state = $.data(target, 'combobox');
		var opts = state.options;
		state.data = opts.loadFilter.call(target, data);
		state.groups = [];
		data = state.data;
		
		var selected = $(target).combobox('getValues');
		var dd = [];
		var group = undefined;
		for(var i=0; i<data.length; i++){
			var row = data[i];
			var v = row[opts.valueField]+'';
			var s = row[opts.textField];
			var g = row[opts.groupField];
			
			if (g){
				if (group != g){
					group = g;
					state.groups.push(g);
					dd.push('<div id="' + (state.groupIdPrefix+'_'+(state.groups.length-1)) + '" class="combobox-group">');
					dd.push(opts.groupFormatter ? opts.groupFormatter.call(target, g) : g);
					dd.push('</div>');
				}
			} else {
				group = undefined;
			}
			
			var cls = 'combobox-item' + (row.disabled ? ' combobox-item-disabled' : '') + (g ? ' combobox-gitem' : '');
			dd.push('<div id="' + (state.itemIdPrefix+'_'+i) + '" class="' + cls + '">');
			dd.push(opts.formatter ? opts.formatter.call(target, row) : s);
			dd.push('</div>');
			
//			if (item['selected']){
//				(function(){
//					for(var i=0; i<selected.length; i++){
//						if (v == selected[i]) return;
//					}
//					selected.push(v);
//				})();
//			}
			if (row['selected'] && $.inArray(v, selected) == -1){
				selected.push(v);
			}
		}
		$(target).combo('panel').html(dd.join(''));
		
		if (opts.multiple){
			setValues(target, selected, remainText);
		} else {
			setValues(target, selected.length ? [selected[selected.length-1]] : [], remainText);
		}
		
		opts.onLoadSuccess.call(target, data);
	}
	
	/**
	 * request remote data if the url property is setted.
	 */
	function request(target, url, param, remainText){
		var opts = $.data(target, 'combobox').options;
		if (url){
			opts.url = url;
		}
//		if (!opts.url) return;
		param = param || {};
		
		if (opts.onBeforeLoad.call(target, param) == false) return;

		opts.loader.call(target, param, function(data){
			loadData(target, data, remainText);
		}, function(){
			opts.onLoadError.apply(this, arguments);
		});
	}
	
	/**
	 * do the query action
	 */
	function doQuery(target, q){
		var state = $.data(target, 'combobox');
		var opts = state.options;
		
		if (opts.multiple && !q){
			setValues(target, [], true);
		} else {
			setValues(target, [q], true);
		}
		
		if (opts.mode == 'remote'){
			request(target, null, {q:q}, true);
		} else {
			var panel = $(target).combo('panel');
			panel.find('div.combobox-item-selected,div.combobox-item-hover').removeClass('combobox-item-selected combobox-item-hover');
			panel.find('div.combobox-item,div.combobox-group').hide();
			var data = state.data;
			var vv = [];
			var qq = opts.multiple ? q.split(opts.separator) : [q];
			$.map(qq, function(q){
				q = $.trim(q);
				var group = undefined;
				for(var i=0; i<data.length; i++){
					var row = data[i];
					if (opts.filter.call(target, q, row)){
						var v = row[opts.valueField];
						var s = row[opts.textField];
						var g = row[opts.groupField];
						var item = opts.finder.getEl(target, v).show();
						if (s.toLowerCase() == q.toLowerCase()){
							vv.push(v);
							item.addClass('combobox-item-selected');
						}
						if (opts.groupField && group != g){
							$('#'+state.groupIdPrefix+'_'+$.inArray(g, state.groups)).show();
							group = g;
						}
					}
				}
			});
//			setValues(target, vv, true);
		}
	}
	
	function doEnter(target){
		var t = $(target);
		var opts = t.combobox('options');
		var panel = t.combobox('panel');
		var item = panel.children('div.combobox-item-hover');
		if (item.length){
			var row = opts.finder.getRow(target, item);
			var value = row[opts.valueField];
			if (opts.multiple){
				if (item.hasClass('combobox-item-selected')){
					t.combobox('unselect', value);
				} else {
					t.combobox('select', value);
				}
			} else {
				t.combobox('select', value);
			}
		}
		var vv = [];
		$.map(t.combobox('getValues'), function(v){
			if (getRowIndex(target, v) >= 0){
				vv.push(v);
			}
		});
		t.combobox('setValues', vv);
		if (!opts.multiple){
			t.combobox('hidePanel');
		}
	}
	
	/**
	 * create the component
	 */
	function create(target){
		var state = $.data(target, 'combobox');
		var opts = state.options;
		
		COMBOBOX_SERNO++;
		state.itemIdPrefix = '_easyui_combobox_i' + COMBOBOX_SERNO;
		state.groupIdPrefix = '_easyui_combobox_g' + COMBOBOX_SERNO;
		
		$(target).addClass('combobox-f');
		$(target).combo($.extend({}, opts, {
			onShowPanel: function(){
				$(target).combo('panel').find('div.combobox-item,div.combobox-group').show();
				scrollTo(target, $(target).combobox('getValue'));
				opts.onShowPanel.call(target);
			}
		}));
		
		$(target).combo('panel').unbind().bind('mouseover', function(e){
			$(this).children('div.combobox-item-hover').removeClass('combobox-item-hover');
			var item = $(e.target).closest('div.combobox-item');
			if (!item.hasClass('combobox-item-disabled')){
				item.addClass('combobox-item-hover');
			}
			e.stopPropagation();
		}).bind('mouseout', function(e){
			$(e.target).closest('div.combobox-item').removeClass('combobox-item-hover');
			e.stopPropagation();
		}).bind('click', function(e){
			var item = $(e.target).closest('div.combobox-item');
			if (!item.length || item.hasClass('combobox-item-disabled')){return}
			var row = opts.finder.getRow(target, item);
			if (!row){return}
			var value = row[opts.valueField];
			if (opts.multiple){
				if (item.hasClass('combobox-item-selected')){
					unselect(target, value);
				} else {
					select(target, value);
				}
			} else {
				select(target, value);
				$(target).combo('hidePanel');
			}
			e.stopPropagation();
		});
	}
	
	$.fn.combobox = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.combobox.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'combobox');
			if (state){
				$.extend(state.options, options);
				create(this);
			} else {
				state = $.data(this, 'combobox', {
					options: $.extend({}, $.fn.combobox.defaults, $.fn.combobox.parseOptions(this), options),
					data: []
				});
				create(this);
				var data = $.fn.combobox.parseData(this);
				if (data.length){
					loadData(this, data);
				}
			}
			if (state.options.data){
				loadData(this, state.options.data);
			}
			request(this);
		});
	};
	
	
	$.fn.combobox.methods = {
		options: function(jq){
			var copts = jq.combo('options');
			return $.extend($.data(jq[0], 'combobox').options, {
				width: copts.width,
				height: copts.height,
				originalValue: copts.originalValue,
				disabled: copts.disabled,
				readonly: copts.readonly
			});
		},
		getData: function(jq){
			return $.data(jq[0], 'combobox').data;
		},
		setValues: function(jq, values){
			return jq.each(function(){
				setValues(this, values);
			});
		},
		setValue: function(jq, value){
			return jq.each(function(){
				setValues(this, [value]);
			});
		},
		clear: function(jq){
			return jq.each(function(){
				$(this).combo('clear');
				var panel = $(this).combo('panel');
				panel.find('div.combobox-item-selected').removeClass('combobox-item-selected');
			});
		},
		reset: function(jq){
			return jq.each(function(){
				var opts = $(this).combobox('options');
				if (opts.multiple){
					$(this).combobox('setValues', opts.originalValue);
				} else {
					$(this).combobox('setValue', opts.originalValue);
				}
			});
		},
		loadData: function(jq, data){
			return jq.each(function(){
				loadData(this, data);
			});
		},
		reload: function(jq, url){
			return jq.each(function(){
				request(this, url);
			});
		},
		select: function(jq, value){
			return jq.each(function(){
				select(this, value);
			});
		},
		unselect: function(jq, value){
			return jq.each(function(){
				unselect(this, value);
			});
		}
	};
	
	$.fn.combobox.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.fn.combo.parseOptions(target), $.parser.parseOptions(target,[
			'valueField','textField','groupField','mode','method','url'
		]));
	};
	
	$.fn.combobox.parseData = function(target){
		var data = [];
		var opts = $(target).combobox('options');
		$(target).children().each(function(){
			if (this.tagName.toLowerCase() == 'optgroup'){
				var group = $(this).attr('label');
				$(this).children().each(function(){
					_parseItem(this, group);
				});
			} else {
				_parseItem(this);
			}
		});
		return data;
		
		function _parseItem(el, group){
			var t = $(el);
			var row = {};
			row[opts.valueField] = t.attr('value')!=undefined ? t.attr('value') : t.text();
			row[opts.textField] = t.text();
			row['selected'] = t.is(':selected');
			row['disabled'] = t.is(':disabled');
			if (group){
				opts.groupField = opts.groupField || 'group';
				row[opts.groupField] = group;
			}
			data.push(row);
		}
	};
	
	$.fn.combobox.defaults = $.extend({}, $.fn.combo.defaults, {
		valueField: 'value',
		textField: 'text',
		groupField: null,
		groupFormatter: function(group){return group;},
		mode: 'local',	// or 'remote'
		method: 'post',
		url: null,
		data: null,
		
		keyHandler: {
			up: function(e){nav(this,'prev');e.preventDefault()},
			down: function(e){nav(this,'next');e.preventDefault()},
			left: function(e){},
			right: function(e){},
			enter: function(e){doEnter(this)},
			query: function(q,e){doQuery(this, q)}
		},
		filter: function(q, row){
			var opts = $(this).combobox('options');
			return row[opts.textField].toLowerCase().indexOf(q.toLowerCase()) == 0;
		},
		formatter: function(row){
			var opts = $(this).combobox('options');
			return row[opts.textField];
		},
		loader: function(param, success, error){
			var opts = $(this).combobox('options');
			if (!opts.url) return false;
			$.ajax({
				type: opts.method,
				url: opts.url,
				data: param,
				dataType: 'json',
				success: function(data){
					success(data);
				},
				error: function(){
					error.apply(this, arguments);
				}
			});
		},
		loadFilter: function(data){
			return data;
		},
		finder:{
			getEl:function(target, value){
				var index = getRowIndex(target, value);
				var id = $.data(target, 'combobox').itemIdPrefix + '_' + index;
				return $('#'+id);
			},
			getRow:function(target, p){
				var state = $.data(target, 'combobox');
				var index = (p instanceof jQuery) ? p.attr('id').substr(state.itemIdPrefix.length+1) : getRowIndex(target, p);
				return state.data[parseInt(index)];
			}
		},
		
		onBeforeLoad: function(param){},
		onLoadSuccess: function(){},
		onLoadError: function(){},
		onSelect: function(record){},
		onUnselect: function(record){}
	});
})(jQuery);
(function($) {
	function _96a(_96b) {
		var _96c = $.data(_96b, "combotree");
		var opts = _96c.options;
		var tree = _96c.tree;
		$(_96b).addClass("combotree-f");
		$(_96b).combo(opts);
		var _96d = $(_96b).combo("panel");
		if (!tree) {
			tree = $("<ul></ul>").appendTo(_96d);
			$.data(_96b, "combotree").tree = tree;
		}
		tree.tree($.extend({}, opts, {
			checkbox : opts.multiple,
			onLoadSuccess : function(node, data) {
				var _96e = $(_96b).combotree("getValues");
				if (opts.multiple) {
					var _96f = tree.tree("getChecked");
					for ( var i = 0; i < _96f.length; i++) {
						var id = _96f[i].id;
						(function() {
							for ( var i = 0; i < _96e.length; i++) {
								if (id == _96e[i]) {
									return;
								}
							}
							_96e.push(id);
						})();
					}
				}
				$(_96b).combotree("setValues", _96e);
				opts.onLoadSuccess.call(this, node, data);
			},
			onClick : function(node) {
				if (opts.multiple) {
					$(this).tree(node.checked ? "uncheck" : "check",
							node.target);
				} else {
					$(_96b).combo("hidePanel");
				}
				_971(_96b);
				opts.onClick.call(this, node);
			},
			onCheck : function(node, _970) {
				_971(_96b);
				opts.onCheck.call(this, node, _970);
			}
		}));
	}
	;
	function _971(_972) {
		var _973 = $.data(_972, "combotree");
		var opts = _973.options;
		var tree = _973.tree;
		var vv = [], ss = [];
		if (opts.multiple) {
			var _974 = tree.tree("getChecked");
			for ( var i = 0; i < _974.length; i++) {
				vv.push(_974[i].id);
				ss.push(_974[i].text);
			}
		} else {
			var node = tree.tree("getSelected");
			if (node) {
				vv.push(node.id);
				ss.push(node.text);
			}
		}
		$(_972).combo("setValues", vv).combo("setText", ss.join(opts.separator));
	}
	;
	function _975(_976, _977) {
		var _978 = $.data(_976, "combotree");
		var opts = _978.options;
		var tree = _978.tree;
		var _979 = tree.tree("options");
		var _97a = _979.onCheck;
		var _97b = _979.onSelect;
		_979.onCheck = _979.onSelect = function() {
		};
		tree.find("span.tree-checkbox").addClass("tree-checkbox0").removeClass(
				"tree-checkbox1 tree-checkbox2");
		if (!$.isArray(_977)) {
			_977 = _977.split(opts.separator);
		}
		for ( var i = 0; i < _977.length; i++) {
			var node = tree.tree("find", _977[i]);
			if (node) {
				tree.tree("check", node.target);
				tree.tree("select", node.target);
			}
		}
		_979.onCheck = _97a;
		_979.onSelect = _97b;
		_971(_976);
	}
	;
	$.fn.combotree = function(_97c, _97d) {
		if (typeof _97c == "string") {
			var _97e = $.fn.combotree.methods[_97c];
			if (_97e) {
				return _97e(this, _97d);
			} else {
				return this.combo(_97c, _97d);
			}
		}
		_97c = _97c || {};
		return this.each(function() {
			var _97f = $.data(this, "combotree");
			if (_97f) {
				$.extend(_97f.options, _97c);
			} else {
				$.data(this, "combotree", {
					options : $.extend({}, $.fn.combotree.defaults, $.fn.combotree.parseOptions(this), _97c)
				});
			}
			_96a(this);
		});
	};
	$.fn.combotree.methods = {
		options : function(jq) {
			var _980 = jq.combo("options");
			return $.extend($.data(jq[0], "combotree").options, {
				width : _980.width,
				height : _980.height,
				originalValue : _980.originalValue,
				disabled : _980.disabled,
				readonly : _980.readonly
			});
		},
		clone : function(jq, _981) {
			var t = jq.combo("clone", _981);
			t.data("combotree", {
				options : $.extend(true, {}, jq.combotree("options")),
				tree : jq.combotree("tree")
			});
			return t;
		},
		tree : function(jq) {
			return $.data(jq[0], "combotree").tree;
		},
		loadData : function(jq, data) {
			return jq.each(function() {
				var opts = $.data(this, "combotree").options;
				opts.data = data;
				var tree = $.data(this, "combotree").tree;
				tree.tree("loadData", data);
			});
		},
		reload : function(jq, url) {
			return jq.each(function() {
				var opts = $.data(this, "combotree").options;
				var tree = $.data(this, "combotree").tree;
				if (url) {
					opts.url = url;
				}
				tree.tree({
					url : opts.url
				});
			});
		},
		setValues : function(jq, _982) {
			return jq.each(function() {
				_975(this, _982);
			});
		},
		setValue : function(jq, _983) {
			return jq.each(function() {
				_975(this, [ _983 ]);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				var tree = $.data(this, "combotree").tree;
				tree.find("div.tree-node-selected").removeClass(
						"tree-node-selected");
				var cc = tree.tree("getChecked");
				for ( var i = 0; i < cc.length; i++) {
					tree.tree("uncheck", cc[i].target);
				}
				$(this).combo("clear");
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).combotree("options");
				if (opts.multiple) {
					$(this).combotree("setValues", opts.originalValue);
				} else {
					$(this).combotree("setValue", opts.originalValue);
				}
			});
		}
	};
	$.fn.combotree.parseOptions = function(_984) {
		return $.extend({}, $.fn.combo.parseOptions(_984), $.fn.tree
				.parseOptions(_984));
	};
	$.fn.combotree.defaults = $.extend({}, $.fn.combo.defaults,
			$.fn.tree.defaults, {
				editable : false
			});
})(jQuery);
(function($) {
	function _985(_986) {
		var _987 = $.data(_986, "combogrid");
		var opts = _987.options;
		var grid = _987.grid;
		$(_986).addClass("combogrid-f").combo($.extend({}, opts, {
			onShowPanel : function() {
				var p = $(this).combogrid(
						"panel");
				var _988 = p.outerHeight()
						- p.height();
				var _989 = p._size("minHeight");
				var _98a = p._size("maxHeight");
				$(this).combogrid("grid").datagrid("resize", {
					width : "100%",
					height : (isNaN(parseInt(opts.panelHeight)) ? "auto" : "100%"),
					minHeight : (_989 ? _989 - _988 : ""),
					maxHeight : (_98a ? _98a - _988 : "")
				});
				opts.onShowPanel.call(this);
			}
		}));
		var _98b = $(_986).combo("panel");
		if (!grid) {
			grid = $("<table></table>").appendTo(_98b);
			_987.grid = grid;
		}
		grid.datagrid($.extend({}, opts, {
			border : false,
			singleSelect : (!opts.multiple),
			onLoadSuccess : function(data) {
				var _98c = $(_986).combo("getValues");
				var _98d = opts.onSelect;
				opts.onSelect = function() {
				};
				_997(_986, _98c, _987.remainText);
				opts.onSelect = _98d;
				opts.onLoadSuccess.apply(_986, arguments);
			},
			onClickRow : _98e,
			onSelect : function(_98f, row) {
				_990();
				opts.onSelect.call(this, _98f, row);
			},
			onUnselect : function(_991, row) {
				_990();
				opts.onUnselect.call(this, _991, row);
			},
			onSelectAll : function(rows) {
				_990();
				opts.onSelectAll.call(this, rows);
			},
			onUnselectAll : function(rows) {
				if (opts.multiple) {
					_990();
				}
				opts.onUnselectAll.call(this, rows);
			}
		}));
		function _98e(_992, row) {
			_987.remainText = false;
			_990();
			if (!opts.multiple) {
				$(_986).combo("hidePanel");
			}
			opts.onClickRow.call(this, _992, row);
		}
		;
		function _990() {
			var rows = grid.datagrid("getSelections");
			var vv = [], ss = [];
			for ( var i = 0; i < rows.length; i++) {
				vv.push(rows[i][opts.idField]);
				ss.push(rows[i][opts.textField]);
			}
			if (!opts.multiple) {
				$(_986).combo("setValues", (vv.length ? vv : [ "" ]));
			} else {
				$(_986).combo("setValues", vv);
			}
			if (!_987.remainText) {
				$(_986).combo("setText", ss.join(opts.separator));
			}
		}
	}
	function nav(_993, dir) {
		var _994 = $.data(_993, "combogrid");
		var opts = _994.options;
		var grid = _994.grid;
		var _995 = grid.datagrid("getRows").length;
		if (!_995) {
			return;
		}
		var tr = opts.finder.getTr(grid[0], null, "highlight");
		if (!tr.length) {
			tr = opts.finder.getTr(grid[0], null, "selected");
		}
		var _996;
		if (!tr.length) {
			_996 = (dir == "next" ? 0 : _995 - 1);
		} else {
			var _996 = parseInt(tr.attr("datagrid-row-index"));
			_996 += (dir == "next" ? 1 : -1);
			if (_996 < 0) {
				_996 = _995 - 1;
			}
			if (_996 >= _995) {
				_996 = 0;
			}
		}
		grid.datagrid("highlightRow", _996);
		if (opts.selectOnNavigation) {
			_994.remainText = false;
			grid.datagrid("selectRow", _996);
		}
	}
	;
	function _997(_998, _999, _99a) {
		var _99b = $.data(_998, "combogrid");
		var opts = _99b.options;
		var grid = _99b.grid;
		var rows = grid.datagrid("getRows");
		var ss = [];
		var _99c = $(_998).combo("getValues");
		var _99d = $(_998).combo("options");
		var _99e = _99d.onChange;
		_99d.onChange = function() {
		};
		grid.datagrid("clearSelections");
		if (!$.isArray(_999)) {
			_999 = _999.split(opts.separator);
		}
		for ( var i = 0; i < _999.length; i++) {
			var _99f = grid.datagrid("getRowIndex", _999[i]);
			if (_99f >= 0) {
				grid.datagrid("selectRow", _99f);
				ss.push(rows[_99f][opts.textField]);
			} else {
				ss.push(_999[i]);
			}
		}
		$(_998).combo("setValues", _99c);
		_99d.onChange = _99e;
		$(_998).combo("setValues", _999);
		if (!_99a) {
			var s = ss.join(opts.separator);
			if ($(_998).combo("getText") != s) {
				$(_998).combo("setText", s);
			}
		}
	}
	;
	function _9a0(_9a1, q) {
		var _9a2 = $.data(_9a1, "combogrid");
		var opts = _9a2.options;
		var grid = _9a2.grid;
		_9a2.remainText = true;
		if (opts.multiple && !q) {
			_997(_9a1, [], true);
		} else {
			_997(_9a1, [ q ], true);
		}
		if (opts.mode == "remote") {
			grid.datagrid("clearSelections");
			grid.datagrid("load", $.extend({}, opts.queryParams, {
				q : q
			}));
		} else {
			if (!q) {
				return;
			}
			grid.datagrid("clearSelections").datagrid("highlightRow", -1);
			var rows = grid.datagrid("getRows");
			var qq = opts.multiple ? q.split(opts.separator) : [ q ];
			$.map(qq, function(q) {
				q = $.trim(q);
				if (q) {
					$.map(rows, function(row, i) {
						if (q == row[opts.textField]) {
							grid.datagrid("selectRow", i);
						} else {
							if (opts.filter.call(_9a1, q, row)) {
								grid.datagrid("highlightRow", i);
							}
						}
					});
				}
			});
		}
	}
	;
	function _9a3(_9a4) {
		var _9a5 = $.data(_9a4, "combogrid");
		var opts = _9a5.options;
		var grid = _9a5.grid;
		var tr = opts.finder.getTr(grid[0], null, "highlight");
		_9a5.remainText = false;
		if (tr.length) {
			var _9a6 = parseInt(tr.attr("datagrid-row-index"));
			if (opts.multiple) {
				if (tr.hasClass("datagrid-row-selected")) {
					grid.datagrid("unselectRow", _9a6);
				} else {
					grid.datagrid("selectRow", _9a6);
				}
			} else {
				grid.datagrid("selectRow", _9a6);
			}
		}
		var vv = [];
		$.map(grid.datagrid("getSelections"), function(row) {
			vv.push(row[opts.idField]);
		});
		$(_9a4).combogrid("setValues", vv);
		if (!opts.multiple) {
			$(_9a4).combogrid("hidePanel");
		}
	}
	;
	$.fn.combogrid = function(_9a7, _9a8) {
		if (typeof _9a7 == "string") {
			var _9a9 = $.fn.combogrid.methods[_9a7];
			if (_9a9) {
				return _9a9(this, _9a8);
			} else {
				return this.combo(_9a7, _9a8);
			}
		}
		_9a7 = _9a7 || {};
		return this.each(function() {
			var _9aa = $.data(this, "combogrid");
			if (_9aa) {
				$.extend(_9aa.options, _9a7);
			} else {
				_9aa = $.data(this, "combogrid", {
					options : $.extend({}, $.fn.combogrid.defaults,
							$.fn.combogrid.parseOptions(this), _9a7)
				});
			}
			_985(this);
		});
	};
	$.fn.combogrid.methods = {
		options : function(jq) {
			var _9ab = jq.combo("options");
			return $.extend($.data(jq[0], "combogrid").options, {
				width : _9ab.width,
				height : _9ab.height,
				originalValue : _9ab.originalValue,
				disabled : _9ab.disabled,
				readonly : _9ab.readonly
			});
		},
		grid : function(jq) {
			return $.data(jq[0], "combogrid").grid;
		},
		setValues : function(jq, _9ac) {
			return jq.each(function() {
				_997(this, _9ac);
			});
		},
		setValue : function(jq, _9ad) {
			return jq.each(function() {
				_997(this, [ _9ad ]);
			});
		},
		clear : function(jq) {
			return jq.each(function() {
				$(this).combogrid("grid").datagrid("clearSelections");
				$(this).combo("clear");
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).combogrid("options");
				if (opts.multiple) {
					$(this).combogrid("setValues", opts.originalValue);
				} else {
					$(this).combogrid("setValue", opts.originalValue);
				}
			});
		}
	};
	$.fn.combogrid.parseOptions = function(_9ae) {
		var t = $(_9ae);
		return $.extend({}, $.fn.combo.parseOptions(_9ae), $.fn.datagrid
				.parseOptions(_9ae), $.parser.parseOptions(_9ae, [ "idField",
				"textField", "mode" ]));
	};
	$.fn.combogrid.defaults = $.extend({}, $.fn.combo.defaults,
			$.fn.datagrid.defaults, {
				height : 22,
				loadMsg : null,
				idField : null,
				textField : null,
				mode : "local",
				keyHandler : {
					up : function(e) {
						nav(this, "prev");
						e.preventDefault();
					},
					down : function(e) {
						nav(this, "next");
						e.preventDefault();
					},
					left : function(e) {
					},
					right : function(e) {
					},
					enter : function(e) {
						_9a3(this);
					},
					query : function(q, e) {
						_9a0(this, q);
					}
				},
				filter : function(q, row) {
					var opts = $(this).combogrid("options");
					return row[opts.textField].toLowerCase().indexOf(
							q.toLowerCase()) == 0;
				}
			});
})(jQuery);
/**
 * datebox - jQuery EasyUI
 * 
 * Dependencies:
 * 	 calendar
 *   combo
 * 
 */
(function($){
	/**
	 * create date box
	 */
	function createBox(target){
		var state = $.data(target, 'datebox');
		var opts = state.options;
		
		$(target).addClass('datebox-f').combo($.extend({}, opts, {
			onShowPanel:function(){
				bindEvents(this);
				setButtons(this);
				setCalendar(this);
				setValue(this, $(this).datebox('getText'), true);
				opts.onShowPanel.call(this);
			}
		}));
		
		/**
		 * if the calendar isn't created, create it.
		 */
		if (!state.calendar){
			var panel = $(target).combo('panel').css('overflow','hidden');
			panel.panel('options').onBeforeDestroy = function(){
				var c = $(this).find('.calendar-shared');
				if (c.length){
					c.insertBefore(c[0].pholder);
				}
			};
			var cc = $('<div class="datebox-calendar-inner"></div>').prependTo(panel);
			if (opts.sharedCalendar){
				var c = $(opts.sharedCalendar);
				if (!c[0].pholder){
					c[0].pholder = $('<div class="calendar-pholder" style="display:none"></div>').insertAfter(c);
				}
				c.addClass('calendar-shared').appendTo(cc);
				if (!c.hasClass('calendar')){
					c.calendar();
				}
				state.calendar = c;
			} else {
				state.calendar = $('<div></div>').appendTo(cc).calendar();
			}

			$.extend(state.calendar.calendar('options'), {
				fit:true,
				border:false,
				onSelect:function(date){
					var target = this.target;
					var opts = $(target).datebox('options');
					setValue(target, opts.formatter.call(target, date));
					$(target).combo('hidePanel');
					opts.onSelect.call(target, date);
				}
			});
		}

		$(target).combo('textbox').parent().addClass('datebox');
		$(target).datebox('initValue', opts.value);
		
		function bindEvents(target){
			var opts = $(target).datebox('options');
			var panel = $(target).combo('panel');
			panel.unbind('.datebox').bind('click.datebox', function(e){
				if ($(e.target).hasClass('datebox-button-a')){
					var index = parseInt($(e.target).attr('datebox-button-index'));
					opts.buttons[index].handler.call(e.target, target);
				}
			});
		}
		function setButtons(target){
			var panel = $(target).combo('panel');
			if (panel.children('div.datebox-button').length){return}
			var button = $('<div class="datebox-button"><table cellspacing="0" cellpadding="0" style="width:100%"><tr></tr></table></div>').appendTo(panel);
			var tr = button.find('tr');
			for(var i=0; i<opts.buttons.length; i++){
				var td = $('<td></td>').appendTo(tr);
				var btn = opts.buttons[i];
				var t = $('<a class="datebox-button-a" href="javascript:void(0)"></a>').html($.isFunction(btn.text) ? btn.text(target) : btn.text).appendTo(td);
				t.attr('datebox-button-index', i);
			}
			tr.find('td').css('width', (100/opts.buttons.length)+'%');
		}
		function setCalendar(target){
			var panel = $(target).combo('panel');
			var cc = panel.children('div.datebox-calendar-inner');
			panel.children()._outerWidth(panel.width());
			state.calendar.appendTo(cc);
			state.calendar[0].target = target;
			if (opts.panelHeight != 'auto'){
				var height = panel.height();
				panel.children().not(cc).each(function(){
					height -= $(this).outerHeight();
				});
				cc._outerHeight(height);
			}
			state.calendar.calendar('resize');
		}
	}
	
	/**
	 * called when user inputs some value in text box
	 */
	function doQuery(target, q){
		setValue(target, q, true);
	}
	
	/**
	 * called when user press enter key
	 */
	function doEnter(target){
		var state = $.data(target, 'datebox');
		var opts = state.options;
		var current = state.calendar.calendar('options').current;
		if (current){
			setValue(target, opts.formatter.call(target, current));
			$(target).combo('hidePanel');
		}
	}
	
	function setValue(target, value, remainText){
		var state = $.data(target, 'datebox');
		var opts = state.options;
		var calendar = state.calendar;
		$(target).combo('setValue', value);
		calendar.calendar('moveTo', opts.parser.call(target, value));
		if (!remainText){
			if (value){
				value = opts.formatter.call(target, calendar.calendar('options').current);
				$(target).combo('setValue', value).combo('setText', value);
			} else {
				$(target).combo('setText', value);
			}
		}
	}
	
	$.fn.datebox = function(options, param){
		if (typeof options == 'string'){
			var method = $.fn.datebox.methods[options];
			if (method){
				return method(this, param);
			} else {
				return this.combo(options, param);
			}
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'datebox');
			if (state){
				$.extend(state.options, options);
			} else {
				$.data(this, 'datebox', {
					options: $.extend({}, $.fn.datebox.defaults, $.fn.datebox.parseOptions(this), options)
				});
			}
			createBox(this);
		});
	};
	
	$.fn.datebox.methods = {
		options: function(jq){
			var copts = jq.combo('options');
			return $.extend($.data(jq[0], 'datebox').options, {
				width: copts.width,
				height: copts.height,
				originalValue: copts.originalValue,
				disabled: copts.disabled,
				readonly: copts.readonly
			});
		},
		cloneFrom: function(jq, from){
			return jq.each(function(){
				$(this).combo('cloneFrom', from);
				$.data(this, 'datebox', {
					options: $.extend(true, {}, $(from).datebox('options')),
					calendar: $(from).datebox('calendar')
				});
				$(this).addClass('datebox-f');
			});
		},
		calendar: function(jq){	// get the calendar object
			return $.data(jq[0], 'datebox').calendar;
		},
		initValue: function(jq, value){
			return jq.each(function(){
				var opts = $(this).datebox('options');
				var value = opts.value;
				if (value){
					value = opts.formatter.call(this, opts.parser.call(this, value));
				}
				$(this).combo('initValue', value).combo('setText', value);
			});
		},
		setValue: function(jq, value){
			return jq.each(function(){
				setValue(this, value);
			});
		},
		reset: function(jq){
			return jq.each(function(){
				var opts = $(this).datebox('options');
				$(this).datebox('setValue', opts.originalValue);
			});
		}
	};
	
	$.fn.datebox.parseOptions = function(target){
		return $.extend({}, $.fn.combo.parseOptions(target), $.parser.parseOptions(target, ['sharedCalendar']));
	};
	
	$.fn.datebox.defaults = $.extend({}, $.fn.combo.defaults, {
		panelWidth:180,
		panelHeight:'auto',
		sharedCalendar:null,
		
		keyHandler: {
			up:function(e){},
			down:function(e){},
			left: function(e){},
			right: function(e){},
			enter:function(e){doEnter(this)},
			query:function(q,e){doQuery(this, q)}
		},
		
		currentText:'Today',
		closeText:'Clear',
		okText:'Ok',
		
		buttons:[{
			text: function(target){return $(target).datebox('options').currentText;},
			handler: function(target){
				$(target).datebox('calendar').calendar({
					year:new Date().getFullYear(),
					month:new Date().getMonth()+1,
					current:new Date()
				});
				doEnter(target);
			}
		},{
			text: function(target){return $(target).datebox('options').closeText;},
			handler: function(target){
				$(target).datebox('setValue', '');
				$(this).closest('div.combo-panel').panel('close');
			}
		}],
		
		formatter:function(date){
			var y = date.getFullYear();
			var m = date.getMonth()+1;
			var d = date.getDate();
			return (m<10?('0'+m):m)+'/'+(d<10?('0'+d):d)+'/'+y;
		},
		parser:function(s){
			if (!s) return new Date();
			var ss = s.split('/');
			var m = parseInt(ss[0],10);
			var d = parseInt(ss[1],10);
			var y = parseInt(ss[2],10);
			if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
				return new Date(y,m-1,d);
			} else {
				return new Date();
			}
		},
		
		onSelect:function(date){}
	});
})(jQuery);

(function($) {
	function _9d9(_9da) {
		var _9db = $.data(_9da, "datetimebox");
		var opts = _9db.options;
		$(_9da).datebox($.extend({}, opts, {
			onShowPanel : function() {
				var _9dc = $(this).datetimebox("getValue");
				_9e2(this, _9dc, true);
				opts.onShowPanel.call(this);
			},
			formatter : $.fn.datebox.defaults.formatter,
			parser : $.fn.datebox.defaults.parser
		}));
		$(_9da).removeClass("datebox-f").addClass("datetimebox-f");
		$(_9da).datebox("calendar").calendar({
			onSelect : function(date) {
				opts.onSelect.call(this.target, date);
			}
		});
		if (!_9db.spinner) {
			var _9dd = $(_9da).datebox("panel");
			var p = $("<div style=\"padding:2px\"><input></div>").insertAfter(
					_9dd.children("div.datebox-calendar-inner"));
			_9db.spinner = p.children("input");
		}
		_9db.spinner.timespinner({
			width : opts.spinnerWidth,
			showSeconds : opts.showSeconds,
			separator : opts.timeSeparator
		});
		$(_9da).datetimebox("initValue", opts.value);
	}
	;
	function _9de(_9df) {
		var c = $(_9df).datetimebox("calendar");
		var t = $(_9df).datetimebox("spinner");
		var date = c.calendar("options").current;
		return new Date(date.getFullYear(), date.getMonth(), date.getDate(), t
				.timespinner("getHours"), t.timespinner("getMinutes"), t
				.timespinner("getSeconds"));
	}
	;
	function _9e0(_9e1, q) {
		_9e2(_9e1, q, true);
	}
	;
	function _9e3(_9e4) {
		var opts = $.data(_9e4, "datetimebox").options;
		var date = _9de(_9e4);
		_9e2(_9e4, opts.formatter.call(_9e4, date));
		$(_9e4).combo("hidePanel");
	}
	;
	function _9e2(_9e5, _9e6, _9e7) {
		var opts = $.data(_9e5, "datetimebox").options;
		$(_9e5).combo("setValue", _9e6);
		if (!_9e7) {
			if (_9e6) {
				var date = opts.parser.call(_9e5, _9e6);
				$(_9e5).combo("setValue", opts.formatter.call(_9e5, date));
				$(_9e5).combo("setText", opts.formatter.call(_9e5, date));
			} else {
				$(_9e5).combo("setText", _9e6);
			}
		}
		var date = opts.parser.call(_9e5, _9e6);
		$(_9e5).datetimebox("calendar").calendar("moveTo", date);
		$(_9e5).datetimebox("spinner").timespinner("setValue", _9e8(date));
		function _9e8(date) {
			function _9e9(_9ea) {
				return (_9ea < 10 ? "0" : "") + _9ea;
			}
			;
			var tt = [ _9e9(date.getHours()), _9e9(date.getMinutes()) ];
			if (opts.showSeconds) {
				tt.push(_9e9(date.getSeconds()));
			}
			return tt.join($(_9e5).datetimebox("spinner")
					.timespinner("options").separator);
		}
	}
	$.fn.datetimebox = function(_9eb, _9ec) {
		if (typeof _9eb == "string") {
			var _9ed = $.fn.datetimebox.methods[_9eb];
			if (_9ed) {
				return _9ed(this, _9ec);
			} else {
				return this.datebox(_9eb, _9ec);
			}
		}
		_9eb = _9eb || {};
		return this.each(function() {
			var _9ee = $.data(this, "datetimebox");
			if (_9ee) {
				$.extend(_9ee.options, _9eb);
			} else {
				$.data(this, "datetimebox", {
					options : $.extend({}, $.fn.datetimebox.defaults,
							$.fn.datetimebox.parseOptions(this), _9eb)
				});
			}
			_9d9(this);
		});
	};
	$.fn.datetimebox.methods = {
		options : function(jq) {
			var _9ef = jq.datebox("options");
			return $.extend($.data(jq[0], "datetimebox").options, {
				originalValue : _9ef.originalValue,
				disabled : _9ef.disabled,
				readonly : _9ef.readonly
			});
		},
		cloneFrom : function(jq, from) {
			return jq.each(function() {
				$(this).datebox("cloneFrom", from);
				$.data(this, "datetimebox", {
					options : $
							.extend(true, {}, $(from).datetimebox("options")),
					spinner : $(from).datetimebox("spinner")
				});
				$(this).removeClass("datebox-f").addClass("datetimebox-f");
			});
		},
		spinner : function(jq) {
			return $.data(jq[0], "datetimebox").spinner;
		},
		initValue : function(jq, _9f0) {
			return jq.each(function() {
				var opts = $(this).datetimebox("options");
				var _9f1 = opts.value;
				if (_9f1) {
					_9f1 = opts.formatter.call(this, opts.parser.call(this,
							_9f1));
				}
				$(this).combo("initValue", _9f1).combo("setText", _9f1);
			});
		},
		setValue : function(jq, _9f2) {
			return jq.each(function() {
				_9e2(this, _9f2);
			});
		},
		reset : function(jq) {
			return jq.each(function() {
				var opts = $(this).datetimebox("options");
				$(this).datetimebox("setValue", opts.originalValue);
			});
		}
	};
	$.fn.datetimebox.parseOptions = function(_9f3) {
		var t = $(_9f3);
		return $.extend({}, $.fn.datebox.parseOptions(_9f3), $.parser
				.parseOptions(_9f3, [ "timeSeparator", "spinnerWidth", {
					showSeconds : "boolean"
				} ]));
	};
	$.fn.datetimebox.defaults = $.extend({}, $.fn.datebox.defaults,
			{
				spinnerWidth : "100%",
				showSeconds : true,
				timeSeparator : ":",
				keyHandler : {
					up : function(e) {
					},
					down : function(e) {
					},
					left : function(e) {
					},
					right : function(e) {
					},
					enter : function(e) {
						_9e3(this);
					},
					query : function(q, e) {
						_9e0(this, q);
					}
				},
				buttons : [ {
					text : function(target) {
						return $(target).datetimebox("options").currentText;
					},
					handler : function(target) {
						var opts = $(target).datetimebox("options");
						_9e2(target, opts.formatter.call(target, new Date()));
						$(target).datetimebox("hidePanel");
					}
				}, {
					text : function(target) {
						return $(target).datetimebox("options").okText;
					},
					handler : function(target) {
						_9e3(target);
					}
				}, {
					text : function(target) {
						return $(target).datetimebox("options").closeText;
					},
					handler : function(target) {
						$(target).datetimebox("setValue", "");
						$(target).datetimebox("hidePanel");
					}
				} ],
				formatter : function(date) {
					var h = date.getHours();
					var M = date.getMinutes();
					var s = date.getSeconds();
					function _9fa(_9fb) {
						return (_9fb < 10 ? "0" : "") + _9fb;
					}
					;
					var _9fc = $(this).datetimebox("spinner").timespinner(
							"options").separator;
					var r = $.fn.datebox.defaults.formatter(date) + " "
							+ _9fa(h) + _9fc + _9fa(M);
					if ($(this).datetimebox("options").showSeconds) {
						r += _9fc + _9fa(s);
					}
					return r;
				},
				parser : function(s) {
					if ($.trim(s) == "") {
						return new Date();
					}
					var dt = s.split(" ");
					var d = $.fn.datebox.defaults.parser(dt[0]);
					if (dt.length < 2) {
						return d;
					}
					var _9fd = $(this).datetimebox("spinner").timespinner(
							"options").separator;
					var tt = dt[1].split(_9fd);
					var hour = parseInt(tt[0], 10) || 0;
					var _9fe = parseInt(tt[1], 10) || 0;
					var _9ff = parseInt(tt[2], 10) || 0;
					return new Date(d.getFullYear(), d.getMonth(), d.getDate(),
							hour, _9fe, _9ff);
				}
			});
})(jQuery);
/**
 * slider - jQuery EasyUI
 * 
 * Dependencies:
 *  draggable
 * 
 */
(function($){
	function init(target){
		var slider = $('<div class="slider">' +
				'<div class="slider-inner">' +
				'<a href="javascript:void(0)" class="slider-handle"></a>' +
				'<span class="slider-tip"></span>' +
				'</div>' +
				'<div class="slider-rule"></div>' +
				'<div class="slider-rulelabel"></div>' +
				'<div style="clear:both"></div>' +
				'<input type="hidden" class="slider-value">' +
				'</div>').insertAfter(target);
		var t = $(target);
		t.addClass('slider-f').hide();
		var name = t.attr('name');
		if (name){
			slider.find('input.slider-value').attr('name', name);
			t.removeAttr('name').attr('sliderName', name);
		}
		slider.bind('_resize', function(e,force){
			if ($(this).hasClass('easyui-fluid') || force){
				setSize(target);
			}
			return false;
		});
		return slider;
	}
	
	/**
	 * set the slider size, for vertical slider, the height property is required
	 */
	function setSize(target, param){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		
		if (param){
			if (param.width) opts.width = param.width;
			if (param.height) opts.height = param.height;
		}
		slider._size(opts);
		if (opts.mode == 'h'){
			slider.css('height', '');
			slider.children('div').css('height', '');
		} else {
			slider.css('width', '');
			slider.children('div').css('width', '');
			slider.children('div.slider-rule,div.slider-rulelabel,div.slider-inner')._outerHeight(slider._outerHeight());
		}
		initValue(target);
	}
	
	/**
	 * show slider rule if needed
	 */
	function showRule(target){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		
		var aa = opts.mode == 'h' ? opts.rule : opts.rule.slice(0).reverse();
		if (opts.reversed){
			aa = aa.slice(0).reverse();
		}
		_build(aa);
		
		function _build(aa){
			var rule = slider.find('div.slider-rule');
			var label = slider.find('div.slider-rulelabel');
			rule.empty();
			label.empty();
			for(var i=0; i<aa.length; i++){
				var distance = i*100/(aa.length-1)+'%';
				var span = $('<span></span>').appendTo(rule);
				span.css((opts.mode=='h'?'left':'top'), distance);
				
				// show the labels
				if (aa[i] != '|'){
					span = $('<span></span>').appendTo(label);
					span.html(aa[i]);
					if (opts.mode == 'h'){
						span.css({
							left: distance,
							marginLeft: -Math.round(span.outerWidth()/2)
						});
					} else {
						span.css({
							top: distance,
							marginTop: -Math.round(span.outerHeight()/2)
						});
					}
				}
			}
		}
	}
	
	/**
	 * build the slider and set some properties
	 */
	function buildSlider(target){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		
		slider.removeClass('slider-h slider-v slider-disabled');
		slider.addClass(opts.mode == 'h' ? 'slider-h' : 'slider-v');
		slider.addClass(opts.disabled ? 'slider-disabled' : '');
		
		slider.find('a.slider-handle').draggable({
			axis:opts.mode,
			cursor:'pointer',
			disabled: opts.disabled,
			onDrag:function(e){
				var left = e.data.left;
				var width = slider.width();
				if (opts.mode!='h'){
					left = e.data.top;
					width = slider.height();
				}
				if (left < 0 || left > width) {
					return false;
				} else {
					var value = pos2value(target, left);
					adjustValue(value);
					return false;
				}
			},
			onBeforeDrag:function(){
				state.isDragging = true;
			},
			onStartDrag:function(){
				opts.onSlideStart.call(target, opts.value);
			},
			onStopDrag:function(e){
				var value = pos2value(target, (opts.mode=='h'?e.data.left:e.data.top));
				adjustValue(value);
				opts.onSlideEnd.call(target, opts.value);
				opts.onComplete.call(target, opts.value);
				state.isDragging = false;
			}
		});
		slider.find('div.slider-inner').unbind('.slider').bind('mousedown.slider', function(e){
			if (state.isDragging || opts.disabled){return}
			var pos = $(this).offset();
			var value = pos2value(target, (opts.mode=='h'?(e.pageX-pos.left):(e.pageY-pos.top)));
			adjustValue(value);
			opts.onComplete.call(target, opts.value);
		});
		
		function adjustValue(value){
			var s = Math.abs(value % opts.step);
			if (s < opts.step/2){
				value -= s;
			} else {
				value = value - s + opts.step;
			}
			setValue(target, value);
		}
	}
	
	/**
	 * set a specified value to slider
	 */
	function setValue(target, value){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		var oldValue = opts.value;
		if (value < opts.min) value = opts.min;
		if (value > opts.max) value = opts.max;
		
		opts.value = value;
		$(target).val(value);
		slider.find('input.slider-value').val(value);
		
		var pos = value2pos(target, value);
		var tip = slider.find('.slider-tip');
		if (opts.showTip){
			tip.show();
			tip.html(opts.tipFormatter.call(target, opts.value));
		} else {
			tip.hide();
		}
		
		if (opts.mode == 'h'){
			var style = 'left:'+pos+'px;';
			slider.find('.slider-handle').attr('style', style);
			tip.attr('style', style +  'margin-left:' + (-Math.round(tip.outerWidth()/2)) + 'px');
		} else {
			var style = 'top:' + pos + 'px;';
			slider.find('.slider-handle').attr('style', style);
			tip.attr('style', style + 'margin-left:' + (-Math.round(tip.outerWidth())) + 'px');
		}
		
		if (oldValue != value){
			opts.onChange.call(target, value, oldValue);
		}
	}
	
	function initValue(target){
		var opts = $.data(target, 'slider').options;
		var fn = opts.onChange;
		opts.onChange = function(){};
		setValue(target, opts.value);
		opts.onChange = fn;
	}
	
	/**
	 * translate value to slider position
	 */
//	function value2pos(target, value){
//		var state = $.data(target, 'slider');
//		var opts = state.options;
//		var slider = state.slider;
//		if (opts.mode == 'h'){
//			var pos = (value-opts.min)/(opts.max-opts.min)*slider.width();
//			if (opts.reversed){
//				pos = slider.width() - pos;
//			}
//		} else {
//			var pos = slider.height() - (value-opts.min)/(opts.max-opts.min)*slider.height();
//			if (opts.reversed){
//				pos = slider.height() - pos;
//			}
//		}
//		return pos.toFixed(0);
//	}
	function value2pos(target, value){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		var size = opts.mode == 'h' ? slider.width() : slider.height();
		var pos = opts.converter.toPosition.call(target, value, size);
		if (opts.mode == 'v'){
			pos = slider.height() - pos;
		}
		if (opts.reversed){
			pos = size - pos;
		}
		return pos.toFixed(0);
	}
	
	/**
	 * translate slider position to value
	 */
//	function pos2value(target, pos){
//		var state = $.data(target, 'slider');
//		var opts = state.options;
//		var slider = state.slider;
//		if (opts.mode == 'h'){
//			var value = opts.min + (opts.max-opts.min)*(pos/slider.width());
//		} else {
//			var value = opts.min + (opts.max-opts.min)*((slider.height()-pos)/slider.height());
//		}
//		return opts.reversed ? opts.max - value.toFixed(0) : value.toFixed(0);
//	}
	function pos2value(target, pos){
		var state = $.data(target, 'slider');
		var opts = state.options;
		var slider = state.slider;
		var size = opts.mode == 'h' ? slider.width() : slider.height();
		var value = opts.converter.toValue.call(target, opts.mode=='h'?(opts.reversed?(size-pos):pos):(size-pos), size);
		return value.toFixed(0);
//		var value = opts.converter.toValue.call(target, opts.mode=='h'?pos:(size-pos), size);
//		return opts.reversed ? opts.max - value.toFixed(0) : value.toFixed(0);
	}
	
	$.fn.slider = function(options, param){
		if (typeof options == 'string'){
			return $.fn.slider.methods[options](this, param);
		}
		
		options = options || {};
		return this.each(function(){
			var state = $.data(this, 'slider');
			if (state){
				$.extend(state.options, options);
			} else {
				state = $.data(this, 'slider', {
					options: $.extend({}, $.fn.slider.defaults, $.fn.slider.parseOptions(this), options),
					slider: init(this)
				});
				$(this).removeAttr('disabled');
			}
			
			var opts = state.options;
			opts.min = parseFloat(opts.min);
			opts.max = parseFloat(opts.max);
			opts.value = parseFloat(opts.value);
			opts.step = parseFloat(opts.step);
			opts.originalValue = opts.value;
			
			buildSlider(this);
			showRule(this);
			setSize(this);
		});
	};
	
	$.fn.slider.methods = {
		options: function(jq){
			return $.data(jq[0], 'slider').options;
		},
		destroy: function(jq){
			return jq.each(function(){
				$.data(this, 'slider').slider.remove();
				$(this).remove();
			});
		},
		resize: function(jq, param){
			return jq.each(function(){
				setSize(this, param);
			});
		},
		getValue: function(jq){
			return jq.slider('options').value;
		},
		setValue: function(jq, value){
			return jq.each(function(){
				setValue(this, value);
			});
		},
		clear: function(jq){
			return jq.each(function(){
				var opts = $(this).slider('options');
				setValue(this, opts.min);
			});
		},
		reset: function(jq){
			return jq.each(function(){
				var opts = $(this).slider('options');
				setValue(this, opts.originalValue);
			});
		},
		enable: function(jq){
			return jq.each(function(){
				$.data(this, 'slider').options.disabled = false;
				buildSlider(this);
			});
		},
		disable: function(jq){
			return jq.each(function(){
				$.data(this, 'slider').options.disabled = true;
				buildSlider(this);
			});
		}
	};
	
	$.fn.slider.parseOptions = function(target){
		var t = $(target);
		return $.extend({}, $.parser.parseOptions(target, [
			'width','height','mode',{reversed:'boolean',showTip:'boolean',min:'number',max:'number',step:'number'}
		]), {
			value: (t.val() || undefined),
			disabled: (t.attr('disabled') ? true : undefined),
			rule: (t.attr('rule') ? eval(t.attr('rule')) : undefined)
		});
	};
	
	$.fn.slider.defaults = {
		width: 'auto',
		height: 'auto',
		mode: 'h',	// 'h'(horizontal) or 'v'(vertical)
		reversed: false,
		showTip: false,
		disabled: false,
		value: 0,
		min: 0,
		max: 100,
		step: 1,
		rule: [],	// [0,'|',100]
		tipFormatter: function(value){return value},
		converter:{
			toPosition:function(value, size){
				var opts = $(this).slider('options');
				return (value-opts.min)/(opts.max-opts.min)*size;
			},
			toValue:function(pos, size){
				var opts = $(this).slider('options');
				return opts.min + (opts.max-opts.min)*(pos/size);
			}
		},
		onChange: function(value, oldValue){},
		onSlideStart: function(value){},
		onSlideEnd: function(value){},
		onComplete: function(value){}
	};
})(jQuery);
