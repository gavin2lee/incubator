	function Graph(options){
				var _nodes = {};
				var _trans = [];
				var _firstLevel = [];
				var _minTop = 100000;
				var _maxHeight=0;
				var typeLevel = {};
				var _h = options.rh ? options.rh : 30;
				var _w = options.rw ? options.rw : 150;
				var _hj = options.rhj ? options.rhj : 25;
				var _wj=options.rwj ? options.rwj : 60;
				var _borderColor = options.borderColor ? options.borderColor : '#cccecd';
				var _moveColor = options.moveColor ? options.moveColor: '#cccecd';
				var _hasStatus = [];
				
				function Node(property,level,source){
					var _property = property;
					var _level = level; //第几等级
					var _left = (parseInt(_level)-1)*(_w+_wj); //left距离
					var _top = 0; //top距离
					var _children = [];
					var _source = source;
					var _index = 0;
					var _id = property['id'];
					this.get = function(k){
						return _property[k];
					};
					this.getId = function(){
						return _id;
					};
					this.set = function(k,v){
						_property[k] = v;
					};
					this.addClidren = function(node){
						_children.push(node);
					};
					this.getChildren = function(){
						return _children;
					};
					this.setTop = function(v){
						_top = v;
						if(v > _maxHeight){
							_maxHeight = v;
						}
					};
					this.getTop = function(){
						return _top;
					};
					this.getSource = function(){
						return _source;
					};
					this.getLeft = function(){
						return _left;
					};
					this.getIndex = function(){
						return _index;
					};
					this.setIndex = function(index){
						_index=index;
					};
					this.getLevel = function(){
						return _level;
					};
				}

				//迭代 转换成对象
				function toObject(root,level,__nodes,__source){
					var n = new Node(root,level,__source);
					if(__nodes[level]==null){
						__nodes[level]= [];
					}
					n.setIndex(__nodes[level].length);
					__nodes[level].push(n);
					if(root.children==null || root.children.length ==0){
						return n;
					}
					for(var i=0;i<root.children.length;i++){
						var __node = toObject(root.children[i],parseInt(level)+1,__nodes,n);
						n.addClidren(__node);
					}
					return n;
				}
				//迭代 设置对象的TOP
				var h = 0;
				function toRender(root){
					if(root.getChildren().length==0){
						h = h + _h + _hj;
						root.setTop(h); 
					}
					
					
					for(var i=0;i<root.getChildren().length;i++){
						var __node = root.getChildren()[i];
						toRender(__node);
					}
					

					if(root.getChildren().length == 1){
						root.setTop(root.getChildren()[0].getTop()); 
					}
					else if(root.getChildren().length > 1){
						var f = root.getChildren()[0];
						var l = root.getChildren()[root.getChildren().length-1];
						root.setTop(f.getTop()+(l.getTop()+_h-f.getTop())/2-_h/2);
					}
				}
				//迭代画图
				function toGraph(root,p){
					for(var i=0;i<root.getChildren().length;i++){
						var __node = root.getChildren()[i];
						toGraph(__node,p);
					}
					toDraw(root,p);
				}
				function toDraw(_n,p){
					if(options.isDraw!=null && !options.isDraw.call(this,_n)){
						return ;
					}
					var m = p.rect(_n.getLeft()+5,_n.getTop()+5-_minTop,_w,_h,10).attr({stroke:'#f4f1f0','stroke-width':0.1,fill : '#f4f1f0'});
					$('#'+options.id).append('<div id="'+_n.getId()+'_text" style="background:#f4f1f0;position:absolute;left:'+(_n.getLeft()+40)+'px;top:'+(_n.getTop()+5-_minTop+_h/5)+'px;"><span style="font-size:6px;font-weight: bold;">'+_n.get('name')+'</span></div>');
					$('#'+options.id).append('<div id="'+_n.getId()+'_prefix" style="background: url(\''+options.webapp+'/assets/images/serv_ico.png\') no-repeat;background-size:100%;position:absolute;left:'+(_n.getLeft()-10)+'px;top:'+(_n.getTop()-_minTop-5)+'px;width:50px;height:50px;"></div>');

					if(_n.get('property')!=null){
						var pro = _n.get('property');
						
						if(pro.error!=null && parseInt(pro.error+'')>0){
							$('#'+options.id).append('<div  id="'+_n.getId()+'_suffix"  style="background: url(\''+options.webapp+'/assets/images/red_light.png\') no-repeat;background-size:100%;position:absolute;left:'+(_n.getLeft()+_w-15)+'px;top:'+(_n.getTop()-_minTop+12)+'px;width:15px;height:14px;" ><span title="'+pro.msg+'" style="display:inline-block;width:15px;height:15px;"></span></div>');
							if(options.errorClick!=null){
								$('#'+_n.getId()+'_suffix').unbind('click').bind('click',function(){
									options.errorClick.call(this,_n,pro,"");
								});
							}
						}
						if(pro.url!=null && pro.protocol!=null){
							$('#'+options.id).append('<div id="'+_n.getId()+'_suffix"  style="position:absolute;left:'+(_n.getLeft()+_w-15)+'px;top:'+(_n.getTop()-_minTop+12)+'px;width:15px;height:14px;"><span style="display:inline-block;width:15px;height:15px;"></spa></div>');
							_hasStatus.push(_n);
						}
					}
					if(options.isDrawLeft==null || options.isDrawLeft.call(this,_n)){
						var p1 = p.path('M'+(_n.getLeft()+5-_wj/2)+' '+(_n.getTop()+5-_minTop+_h/2)+' L'+(_n.getLeft()+5)+' '+(_n.getTop()+5-_minTop+_h/2)+'  Z').attr({stroke:'#cccecd','stroke-width':0.5,fill : '#cccecd'});
					}
					if(_n.getChildren().length!=0 || (options.afterXLineEnd!=null && options.afterXLineEnd.call(this,_n))){
						var p2 = p.path('M'+(_n.getLeft()+5+_w)+' '+(_n.getTop()+5-_minTop+_h/2)+' L'+(_n.getLeft()+5+_w+_wj/2)+' '+(_n.getTop()+5-_minTop+_h/2)+'  Z').attr({stroke:'#cccecd','stroke-width':0.5,fill : '#cccecd'});
					}
					if(_n.getChildren().length>1){
						var f = _n.getChildren()[0];
						var l = _n.getChildren()[_n.getChildren().length-1];
						var p3 = p.path('M'+(f.getLeft()+5-_wj/2)+' '+(f.getTop()+5-_minTop+_h/2)+' L'+(l.getLeft()+5-_wj/2)+' '+(l.getTop()+5-_minTop+_h/2)+'  Z').attr({stroke:'#cccecd','stroke-width':0.5,fill : '#cccecd'});
					}
				}
				var root = toObject(options.data,1,_nodes,null);
				h=0;
				toRender(root);
				
				
				function toAdjust(_n,ce,level,index,isfirst){
					if(_n.getLevel()==level && _n.getIndex()<index){
						return ;
					}
					if(_n.getIndex()==0){
						if(_n.getTop()<ce){
							_n.setTop(10);
						}
						else{
							_n.setTop(_n.getTop()-ce);
						}
					}
					else{
						var c = _n.getTop() - _nodes[_n.getLevel()][_n.getIndex()-1].getTop()-_h-_hj;
						if(c > ce){
							_n.setTop(_n.getTop()-ce);
						}
						else{
							_n.setTop(_n.getTop()-c);
						}
					}
					
				
					for(var i=0;i<_n.getChildren().length;i++){
						var __node = _n.getChildren()[i];
						toAdjust(__node,ce,level,index,false);
					}
					
				}
				var a = [];
				
				(function _r(){
					for(var level in _nodes){
						for(var i=0;i<_nodes[level].length;i++){
							if(_nodes[level][i].getChildren().length<1){
								if(i!=0){
									var ce =  _nodes[level][i].getTop()-_nodes[level][i-1].getTop();
									if(ce < (_h+_hj+2)){
										continue ;
									}
									var oh = _nodes[level][i].getTop();
									_nodes[level][i].setTop(_nodes[level][i].getTop()-(ce-_h-_hj));
									var c = true;
									if(options.beforeChangeSource!=null){
										c = options.beforeChangeSource.call(this,_nodes[level][i]);
									}
									
									if(c){
										var __source = _nodes[level][i].getSource();
										if(_nodes[level][i].getIndex()==__source.getChildren()[__source.getChildren().length-1].getIndex()){
											if(__source.getTop() > _nodes[level][i].getTop()){
												_nodes[level][i].setTop(oh);
											}
										}
									}
								}
								continue ;
							}
							if(i==0){
								continue ;
							}
							var ce1 =  _nodes[level][i].getTop()-_nodes[level][i-1].getTop()-_h-_hj;
							var f = _nodes[level][i].getChildren()[0];
							var ce2 = 0;
							if(f.getIndex()==0){
								ce2 = f.getTop();
							}
							else{
								var n = _nodes[parseInt(level)+1][f.getIndex()-1];
								ce2 = f.getTop() - n.getTop()-_h-_hj;
							}
							var ce = ce1 > ce2 ? ce2 : ce1;
							if(ce<=0){
								continue ;
							}
							var source = _nodes[level][i].getSource();
							
							toAdjust(source,ce,level,_nodes[level][i].getIndex(),true);
							if(source.getChildren().length==0 || source.getChildren().length==1){
								source.setTop(source.getChildren()[0].getTop());
							}
							else if(source.getChildren().length>1){
								source.setTop(source.getChildren()[0].getTop()+(source.getChildren()[source.getChildren().length-1].getTop()-source.getChildren()[0].getTop())/2);
								if(source.getIndex()!=0){
									if(source.getTop()-_nodes[source.getLevel()][source.getIndex()-1].getTop() < (_hj+_h)){
										source.setTop(_nodes[source.getLevel()][source.getIndex()-1].getTop()+_hj+_h);
									}
								}
							}
							_r();
						}
					}
					if(options['afterChangeTop']!=null){
						options['afterChangeTop'].call(this,_nodes);
					}
				})();

				for(var level in _nodes){
					var t = _nodes[level][0].getTop();
					
					if(_minTop > t){
						_minTop = t;
					}
				}
				
				
				var paper = Raphael(options.id,$('#'+options.id).width(),_maxHeight);
				toGraph(root,paper);
				
				function obtainStatus(){
					if(_hasStatus.length==0){
						return ;
					}
					var page = 10;
					var length = _hasStatus.length;
					var level = Math.ceil(length/page);
					for(var i=0;i<level;i++){
						(function(_level){
							setTimeout(function(){
								for(var j=0;j<page;j++){
									if((_level*page+j)>=_hasStatus.length){
										continue ;
									}
									var node = _hasStatus[_level*page+j];
									var def = $.ajax({
										'url' : options.status_url==null ? (options.webapp+'/sys/architecture/node/status.do'): options.status_url+'&1=1&',
										'cache' : false,
										'dataType' :'json',
										'type' : 'post',
										'data' : {
											'protocol' : node.get('property').protocol,
											'url' : node.get('property').url,
											'param' : node.get('property')
										},
										'extra' : node
									});
									def['done'](function(msg){
										var _n = this.extra;
										var r = false;
										if(msg.code != 0){
											r = true;
										}
										else if(msg.data==null || msg.data.error==null){
											r = false;
											
										}
										else if(parseInt(msg.data.error+'')>0){
											r = true;
										}
										$('#'+_n.getId()+'_suffix').find('span').attr('title',msg.data.msg);
										var pro = _n.get('property');
										if(options.errorClick!=null){
											$('#'+_n.getId()+'_suffix').unbind('click').bind('click',function(){
												options.errorClick.call(this,_n,pro,msg.data.msg);
											});
										}
										/*if(options.mouseover!=null){
											$('#'+_n.getId()+'_suffix').unbind('mouseover').bind('mouseover',function(){
												options.mouseover.call(this,_n,pro,msg.data.msg);
											});
										}
										if(options.mouseover!=null){
											$('#'+_n.getId()+'_prefix').unbind('mouseover').bind('mouseover',function(){
												options.mouseover.call(this,_n,pro,msg.data.msg);
											});
										}*/
										$('#'+_n.getId()+'_suffix').css({
											background : 'url(\''+options.webapp+'/assets/images/'+(r?'red':'green')+'_light.png\') no-repeat',
											backgroundSize : '100%'
										});
									});
								}
							},_level*200);
						})(i);
					}
				}
				obtainStatus();
			}