/**
 * 
 */
	var _nodes = {};
	var _trans = [];
	var _firstLevel = [];
	var maxHeight = 0;
	var typeLevel = {};
	var _h = 90;
	var _w = 150;
	var _hj = 30;
	var _wj=100;
	var _borderColor = '#4D90FE';
	var _moveColor = '#35aa47';
	
	function _Trans(_id){
		this._id = _id;
		this.src = "";
		this.target = "";
		this._object = "";
		this.setSrc = function(_src){
			this.src = _src;
		};
		this.setTarget = function(_target){
			this.target = _target;
		};
		this.setPath = function(_p){
			this._object = _p;
		};
	}
	
	function DrawArrow(paper, x, y, len, deltaAngle, fAngle, sLen){
　　deltaAngle = deltaAngle && typeof (deltaAngle) == "number" ? deltaAngle : 0; 
　　len = len && typeof (len) == "number" && len > 0 ? len : 12; 
　　fAngle = fAngle && typeof (fAngle) == "number" && fAngle > 0 ? fAngle : 50;
　　sLen = sLen && typeof (sLen) == "number" && sLen > 0 ? sLen : 4;

　　var ltLen = Math.tan(Raphael.rad(fAngle / 2)) * len;
　　var ltX = x - ltLen;
　　var ltY = y - len;

　　var rtLen = ltLen;
　　var rtX = x + rtLen;
　　var rtY = ltY;

　　var ctX = x;
　　var ctY = ltY + sLen;

　　var arrowPath = "M" + x + "," + y + "L" + ltX + "," + ltY + "L" + ctX + "," + ctY + "L" + rtX + "," + rtY + "L" + x + "," + y + "Z";
　　var arrow = paper.path(arrowPath).attr({ stroke: _borderColor, fill: _borderColor });
　　arrow.rotate(deltaAngle, x, y);
	}
	
	
	function _Node(_no,_type,_target,_url,_name){
		this._no = _no;
		this._name = _name;
		this._type = _type;
		this._target = _target;
		this._object = "";
		this._label = "";
		this._text = "";
		this._url = _url;
		this.outCome = [];
		this.alreadDraw = false;
		this.position = {};
		this.addOutCome = function(_trans){
			this.outCome.push(_trans);
		};
		this.setAlreadDraw=function(){
			this.alreadDraw = true;
		};
		this.isAlreadDraw = function(){
			return this.alreadDraw;
		};
		this.setRect = function(object){
			this._object = object;
		};
		this.setLable = function(_l){
			this._label  = _l;
		};
		this.setText = function(_t){
			this._text = _t;
		};
		this.setPosition = function(_position){
			this.position = _position;
		};
	}
	function renderRect(n,p,level){
		if(n.isAlreadDraw()){
			return ;
		}
		(function(_n,_p,_level){
			var c = typeLevel[_n._type].length;
			var _top =  ( maxHeight - c*(_h+_hj) ) /2;
			var _t = 0;
			for(var m=0;m<typeLevel[_n._type].length;m++){
				if(typeLevel[_n._type][m]==_n){
					_t = m;
					break ;
				}
			}
			var _position = {
				x : _level * (_w+_wj),
				y : _t * (_h+_hj)+_top,
				w : _w,
				h : _h
			};
			_n.setPosition(_position);
			var m = p.rect(_n.position.x,_n.position.y,_n.position.w,_n.position.h,10).attr({stroke:_borderColor,'stroke-width':2,fill : '#ffffff'});
			//var text =  m.text('1313');
//			var _label = p.text(_n.position.x + _w/2,_n.position.y-20 + _h / 2,_n._type).attr({"font-size": 16});
			var _text  =  p.text(_n.position.x + _w/2,_n.position.y+5 + _h / 2-5,_n._name).attr({"font-size": 14});
			_n.setRect(m);
//			_n.setLable(_label);
			_n.setText(_text);
			
			
			(function(_m,__label,__text,__n){
				_m.click(function(){
					var url = __n._url;
					if(url==null){
						return ;
					}
//					var url = "www.baidu.com?p="+__n._no;
					if(url.indexOf('http://')<0){
						url = "http://"+url;
					}
					window.open(url);
				});
//				__label.click(function(){
//					var url = __n._url;
//					if(url==null){
//						return ;
//					}
////					var url = "www.baidu.com?p="+__n._no;
//					window.open(url);
//				});
				__text.click(function(){
					var url = __n._url;
					if(url==null){
						return ;
					}
					if(url.indexOf('http://')<0){
						url = "http://"+url;
					}
//					var url = "www.baidu.com?p="+__n._no;
					window.open(url);
				});
				
				_m.mousemove(function(event){
					$(event.target).attr('stroke',_moveColor);
				});
				_m.mouseout(function(event){
					$(event.target).attr('stroke',_borderColor);
				});
//				__label.mousemove(function(event){
//					$(m.node).attr('stroke',_moveColor);
//				});
				__text.mousemove(function(event){
					$(m.node).attr('stroke',_moveColor);
				});
			})(m,null,_text,_n);
		})(n,p,level);
		if(n.outCome.length==0){
			return ;
		}
		for(var i =0;i<n.outCome.length;i++){
			var _p = n.outCome[i];
//			renderPath(_p,p,n,level);
			renderRect(_p.target,p,level+1);
		}
		n.setAlreadDraw();
	}
	function renderPath(_path,p){
		var _source = _path.src;
		var _target = _path.target;
		var srcPosition = {
				x : _source.position.x+_source.position.w+10,
				y : _source.position.y+_source.position.h/2
		};
		
		var _t = 0;
		
		for(var m=0;m<typeLevel[_source._type].length;m++){
			if(typeLevel[_source._type][m]==_source){
				_t = m;
				break ;
			}
		}
		var _ey = 0;
		if(typeLevel[_source._type].length%2==0){
			var _c = typeLevel[_source._type].length/2;
			_t = _t - _c + 0.5;
		}
		else{
			var _c = (typeLevel[_source._type].length-1)/2;
			_t = _t  - _c;
		}
		
		var _l =  ((typeLevel[_source._type].length-1) * 10) / 2
		var tarPosition = {
				x : _target.position.x -10,
				y : _target.position.y+_target.position.h/2 + (_t)*10
		};
		var p1 = p.path('M'+srcPosition.x+' '+srcPosition.y+' L'+tarPosition.x+' '+tarPosition.y+'  Z').attr({stroke:_borderColor,'stroke-width':2});
		_path.setPath(p1);
		
	    var diff_x = tarPosition.x - srcPosition.x;
        var diff_y = tarPosition.y - srcPosition.y;
      //返回角度,不是弧度
       var _jd = 360*Math.atan(diff_y/diff_x)/(2*Math.PI);
	   var _ar = new DrawArrow(p , tarPosition.x ,  tarPosition.y ,  10, _jd-90);
	}

	var _concat = {
		init : function(type,entityId){
			var _html = '<div id="dialog-concat" class = "dialog dialog-default-b" title="app资源信息" style="overflow:auto;height:100%;"><div id="dialog-concat-data" style="padding:20px;height:100%;"></div></div>';
			var l  = common.dialog({
				title  : 'app资源信息',
			    type :  1,
			    area: ['950px', '600px'],
			    fix: false, //不固定
			    maxmin: false,
			    content: _html
			});
			$('#dialog-concat').data('layer',l);
			return $.ajax({
				url : WEB_APP_PATH+'/app/dashboard/storyInfo/'+type+'/'+entityId+'.do',
				type : 'POST',
				dataType: 'json'
			});
			
		},
		render : function(type,entityId){
			if($('#dialog-concat').size()!=0){
				var l = $('#dialog-concat').data('layer');
				l.close();
			}
			var def = this.init(type,entityId);
			def.done(function(_result){
				for(var i=0;i<_result.rect.length;i++){
					var info = _result.rect[i];
					var _n  = new _Node(info['no'],info['type'],info['target'],info['url'],info['name']);
					_nodes[info['no']] = _n;
					if(typeLevel[info['type']]==null){
						typeLevel[info['type']] = [];
					}
					typeLevel[info['type']].push(_n);
					
					if(_result.firstLevelType==info['type']){
						_firstLevel.push(_n);
					}
				}
				for(var k in typeLevel){
					var c = typeLevel[k].length;
					maxHeight = maxHeight < c*(_h+_hj) ? c*(_h+_hj) : maxHeight;
				}
				for(var k in _nodes){
					var _n = _nodes[k];
					if(_n['_target']==null){
						continue ;
					}
					var _targets = _n['_target'].split(',');
					for(var _m = 0;_m<_targets.length;_m++){
						var _p = new _Trans('path'+k+_m);
						_p.setSrc(_n);
						_p.setTarget(_nodes[_targets[_m]]);
						_n.addOutCome(_p);
						_trans.push(_p);
					}
					
				}
			});
			def.done(function(rsp){
				var paper = Raphael('dialog-concat-data', 950, 500);
				for(var i =0 ;i<_firstLevel.length;i++){
					var _node = _firstLevel[i];
					renderRect(_node,paper,0);
				}
				for(var i=0;i<_trans.length;i++){
					renderPath(_trans[i],paper);
				}
			});
		}
	};
	
	$(function(){
		$('body').bind('modClick',function(event,target,e){
			_nodes = {};
			_trans = [];
			_firstLevel = [];
			maxHeight = 0;
			typeLevel = {};
			
			var type = $(target).attr('type');
			var entityId = $(target).attr('entityId');
			_concat.render(type,entityId);
		});

	});