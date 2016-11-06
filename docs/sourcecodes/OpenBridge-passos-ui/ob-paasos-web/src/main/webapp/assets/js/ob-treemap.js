
/*
 * 每3位用逗号分隔
 * s : string
 * n : 小数位
 */
function numberFormat(s, n)   
{   
   n = n >= 0 && n <= 20 ? n : 2;   
   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";   
   var l = s.split(".")[0].split("").reverse();  
   r = '';   
   if(n>0){
	   r = '.'+s.split(".")[1];
   }
   t = "";   
   for(i = 0; i < l.length; i ++ )   
   {   
      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");   
   }   
   return t.split("").reverse().join("") + r;   
}
function Treemap(container,_options){
	var options={
			margin : {top: 40, right: 10, bottom: 10, left: 10},
			width : 660,
			height : 550,
		    theme : 'theme0',
		    data : {},
		    home : true
	};
	this.container = container;
	this.options = $.extend(true,options,_options);
	this.options.width = this.options.width - this.options.margin.left - this.options.margin.right,
	this.options.height = this.options.height - this.options.margin.top - this.options.margin.bottom,
	this.init();
}
$.extend(true,Treemap.prototype,{
	init : function(){
		var options = this.options;
		var div = $(this.container)
		.css("position", "relative")
		.css("width", (options.width + options.margin.left + options.margin.right) + "px")
		.css("height", (options.height + options.margin.top + options.margin.bottom) + "px")
		.css("left", options.margin.left + "px")
		.css("top", options.margin.top + "px");
		this.create();
	},
	create : function(){
		var _this = this;
		$(_this.options.container).html("");
		var url = this.options.url;
		var paramStr = [];
		for(var key in this.options.data){
			paramStr.push(key+"="+this.options.data[key]);
		}
		if(paramStr.length>0)	url += '?'+paramStr.join('&');
		d3.json(url, function(error, data) {
		    if (error) throw error;
		    var root = _this.assembleData(data);
		    _this.nodes = _this.createTreemap(root);
		    $.each(_this.nodes,function(i,d){
//		    	if(d.size<=0)	return;
		    	var jqNode = $('<div></div>');
		    	var displayName = d.name;
		    	if(displayName.split('\/').length>4){
		    		var array = displayName.split('\/');
		    		array.splice(3,0,'<br/>');
		    		displayName = array.join('/');
		    		displayName = displayName.replace('<br/>/','<br/>');
		    	}
		    	jqNode.html('<div class="treemap-inner" data-path="'+d.name+'" style="max-width:'+d.dx+
		    			'px;'+((d.dx>40 && d.dy>40)?'':'visibility:hidden;')+'">'+displayName
		    			+'</div><a class="treemap-link" style="font-size:12px;"><span class="icon-link"></span></a>');
		    	jqNode.addClass("node");
		    	if(d.size>0)	jqNode.addClass("tooltips");
		    	_this.position(jqNode,d);
		    	jqNode.css("background",function() { 
			    	  if(d.ration>=0){
				    	  var c = _this.getColor(d.ration); 
				    	  return c;
			    	  }
			   		  return 'gray';
			      })
//			      .html(d.children ? null : d.name)
			      .attr("title",'<div class=\'text-left\'>'+d.name+'<br>'+d.label+'</div>');
		    	jqNode.appendTo(_this.container);
		    });
		    _this.initGoin();
			_this.tooltipInit();
		});
	},
	initGoin : function(){//只有在Home时可进入，若要根据是否有children来判断，可重写此方法；
		if(!this.options.home){
	    	$(".node,.treemap-inner").css("cursor","default");
	    }else{
	    	$(".treemap-breadcrumbs").remove();
	    	var _this = this;
	    	$(".node").click(function(){
	    		var path = $(this).find(".treemap-inner").attr("data-path");
	    		_this.goIn(path);
	    	});
	    }
	},
	goIn : function(path){
		this.breadcrumbs(path);
		this.options.data.projectKey += ':'+path;
		this.options.home = false;
		this.init();
	},
	assembleData : function(data){
		var treeData = [];
		for(var i=0;i<data.length;i++){
			var obj = data[i];
			var size = -1,
			ration = -1;
			if(obj.msr){
				$.each(obj.msr,function(i,v){
					if(v.key=='ncloc')	size = v.val;
					if(v.key=='duplicated_lines_density')	ration = v.val;
				})
			}
			var label = '代码行数:'+(size==-1 ? '--' : numberFormat(size,0))+
					'<br/>重复率(%):'+(ration==-1 ? '--' : ration.toFixed(1)+'%');
			treeData[i] = {
					id : obj.id,
					name: obj.name,
					size : size,
					ration : ration,
					label : label
			};
		}
		var root = {
	    	name : "根目录",
	    	children : treeData
	    }
		return root;
	},
	getColor : function (nodeName){
		if(!this.getRanger){
			this.getRanger = this.ranger();
		}
		var colorMatrix = this.getColorMatrix(this.options.theme);
		var index = this.getRanger(nodeName) ;
	    index = index < 0 ? 0 : Math.min(index, colorMatrix.length - 1);
	    var color = d3.interpolateRgb.apply(null, [colorMatrix[index][0], colorMatrix[index][1]]);
	    var colorRatio = 0.5;
	    return color(colorRatio);
	},
	ranger : function (){
		var _getNodeTheme = d3.scale.ordinal().range(d3.range(this.getColorMatrix(this.options.theme).length));
		return _getNodeTheme;
//	 	return d3.scale.ordinal().range(['#008000','#7FFF00','#7CFC00','#ADFF2F','#9ACD32']);
	},
	createTreemap : function(data){
		var treemap = d3.layout.treemap()
	    .size([this.options.width, this.options.height])
		.mode("squarify")
		.sort(function (a, b) {
			return a.value - b.value;
	    })
	    .value(function(d) { return d.size; });
		return treemap.nodes(data);
	},
	position : function (ele,d) {
		ele.css("left", d.x )
		      .css("top", d.y )
		      .css("width", Math.max(0, d.dx - 1))
		      .css("line-height", Math.max(0, d.dy - 1)+"px")
		      .css("height", Math.max(0, d.dy - 1));
	},
	tooltipInit : function (){
		$('.tooltips').tooltipster({
			contentAsHTML: true,
//			offsetX: "-195px",
			offsetY: "0px"
		});
	},
	breadcrumbs : function(path){
//    	<div class="treemap-breadcrumbs"><span class="treemap-breadcrumbs-item"><a class="icon-home" href="#"></a></span><span class="treemap-breadcrumbs-item" title="src/main/java/com/chen/dao/impl" data-reactid="jn=1cn=2DocumentManage=2src/main/java/com/chen/dao/impl"><i class="icon-chevron-right" data-reactid="jn=1cn=2DocumentManage=2src/main/java/com/chen/dao/impl.0"></i><i class="icon-qualifier-dir" data-reactid="jn=1cn=2DocumentManage=2src/main/java/com/chen/dao/impl.1"></i><a href="#" data-reactid="jn=1cn=2DocumentManage=2src/main/java/com/chen/dao/impl.2">src/main/java/com/chen/dao/impl</a></span></div>
		var _this = this;
		var projectKey = _this.options.data.projectKey;
		var bc = $(".treemap-breadcrumbs");
		if(bc.length==0){
			bc = $('<div class="treemap-breadcrumbs"></div>');
			bc.append('<a href="javascript:void(0);" id="treeMapHomeBc" class="treemap-breadcrumbs-item" data-key="'+projectKey+'">Home</a>');
			$(bc).insertAfter(_this.container);
		}
		bc.append('&nbsp;>&nbsp;');
		bc.append('<a href="javascript:void(0);" class="treemap-breadcrumbs-item" data-key="'+projectKey+':'+path+'">'+path+'</a>');
		$("a.treemap-breadcrumbs-item").click(function(){
			var pk = $(this).attr("data-key");
			if(_this.options.data.projectKey == pk)		return false;
			_this.options.data.projectKey = pk;
			if($(this).attr("id")=='treeMapHomeBc')		_this.options.home = true;
			_this.init();
		});
	},
	getColorMatrix : function (theme){
		var colorMatrix = {
				theme0 : [
	                ["#00aa00", "#00aa00"],
	                ["#14af00", "#14af00"],
	                ["#25b400", "#25b400"],
	                ["#27b400", "#27b400"],
	                ["#6cc700", "#6cc700"],
	                ["#71c800", "#71c800"]
		              ],
				theme1 : [
		            ["#e72e8b", "#ff7fbf"],
		            ["#d94f21", "#ff9673"],
		            ["#f3c53c", "#ffe38c"],
		            ["#8be62f", "#bfff7f"],
		            ["#14cc14", "#66ff66"],
		            ["#2fe68a", "#7fffc0"]
		              ],
				theme2 : [
					["#2f8ae7", "#7fc0ff"],
					["#8a2ee7", "#bf7fff"],
					["#f33dc6", "#ff8ce3"],
					["#8be62f", "#bfff7f"],
					["#14cc14", "#66ff66"],
					["#2fe68a", "#7fffc0"]
		              ],
				theme3 : [
				    ["#2f8ae7", "#896DA3"],
		            ["#8e34df", "#FFADA6"],
		            ["#f738c0", "#65FCFC"],
		            ["#84e653", "#555566"],
		            ["#0cc53e", "#db3f7c"],
		            ["#00e793s", "#db3f7c"]
		        ],
		        theme4 : [
					["#d94f21", "#7a88d1"],
					["#579ce2", "#87bdf4"],
					["#3bb4df", "#7fd1ef"],
					["#a380ff", "#baa0ff"],
					["#a164c5", "#c28fe1"],
					["#d93a92", "#ec74b6"],
					["#b82377", "#d569a7"],
					["#bb3ca3", "#d381c2"],
					["#da2d57", "#ec6b8a"],
					["#4ca716", "#4ca716"],
					["#5b63c2", "#8e93d7"],
					["#15a9a3", "#4ecac5"],
					["#a9ab48", "#e8c670"],
					["#2aa5f5", "#73c4fa"],
					["#f67e10", "#feb648"],
					["#1faa77", "#62c8a2"],
					["#eb4f20", "#f58563"],
					["#ffc000", "#ffd659"],
					["#f16ebc", "#f6a1d3"],
					["#d23457", "#e27b92"]         
		         ]
		};
		return colorMatrix[theme];
	}
});








