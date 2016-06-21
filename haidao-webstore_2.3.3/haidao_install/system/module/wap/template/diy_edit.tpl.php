<?php include template('header','admin');?>
<link type="text/css" rel="stylesheet" href="<?php echo __ROOT__;?>statics/css/mobile.css" />
<link type="text/css" rel="stylesheet" href="<?php echo __ROOT__;?>statics/css/wap/mui.min.css" />
<link type="text/css" rel="stylesheet" href="<?php echo __ROOT__;?>statics/css/wap/haidao.mobile.css" />
<link type="text/css" rel="stylesheet" href="<?php echo __ROOT__;?>statics/js/upload/uploader.css" />
<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/upload/uploader.js"></script>
<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/wap/jquery-ui.min.js"></script>
<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/wap/jquery.base64.js"></script>
<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/goods/goods_cat.js" ></script>
<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/wap/haidao.diy.js"></script>
<script type="text/javascript">
var config = {
	"global": {
		"libs": {
			"form": {
				"title": {
					"label": "页面标题：",
					"name": "title",
					"type": "text",
					"required": "required;maxlength:50"
				},
				"desc": {
					"label": "页面描述：",
					"name": "desc",
					"type": "text",
					"placeholder": "用户通过微信分享给朋友时，会自动显示页面描述"
				},
				"headbg": {
					"label": "头部背景：",
					"name": "headbg",
					"type": "color",
					"value": "#0068b7"
				},
				"bgcolor": {
					"label": "背景颜色：",
					"name": "bgcolor",
					"type": "color",
					"value": "#f9f9f9"
				},
				"logo": {
					"label": "页面LOGO：",
					"name": "logo",
					"type": 'hidden'
				}
			}
		},
		"validate": true
	},
	"ads": {
		"jsfile": "",
		"cssfile": "",
		"name": "图片广告",
		"libs": {
			"image": {
				"form": {
					"show": {
						"name": "show",
						"label": "显示方式：",
						"type": "radio",
						"value": ["折叠轮播","分图显示"]
					},
					"size" : {
						"name": "size",
						"label": "显示大小：",
						"type": "radio",
						"value": ["大图","小图"]
					}
				},
				"html": '<!--{each}--><a href="javascript:;"><img src="statics/images/wap/ad.jpg" /></a><!--{/each}-->'
			},
			"normal": {
				"form": {
					"show": {
						"name": "show",
						"label": "显示方式：",
						"type": "radio",
						"value": ["折叠轮播","分图显示"]
					},
					"size": {
						"name": "size",
						"label": "显示大小：",
						"type": "radio",
						"value": ["大图"]
					}
				},
				"html": '<!--{each}--><a class="hd-slider-item" href="javascript:;"><img src="statics/images/wap/ad.jpg" /></a><!--{/each}-->'
			},
			"form": {
				"title": {
					"name": "title",
					"type": "text",
					"label": "标题："
				},
				"url": {
					"name": "url",
					"type": "text",
					"label": "链接："
				},
				"imgs": {
					"name": "src",
					"type": "hidden"
				}
			}
		}
	},
	"search": {
		"name": "商品搜索",
		"libs": {
			"form": {
				"text": "可随意插入任何页面和位置，方便粉丝快速搜索商品。",
				"tip": "注意：记得给商品添加合适的商品标签吧。"
			},
			"html": '<!--{each}-->'
					+'<div class="hd-search">'
	    			+'	<input type="search" placeholder="搜索商品名称">'
	    			+'</div>'
	    			+'<!--{/each}-->'
		}
	},
	"spacing": {
		"name": "辅助空白",
		"libs": {
			"form": {
				"label": "空白高度：",
				"name": "height",
				"type": "hidden",
				"value": 20
			}
		}
	},
	"goods": {
		"name": "商品列表",
		"libs": {
			"form": {
				"category": {
					"name": "category",
					"label": "商品分类：",
					"type": "hidden",
					"placeholder": "请填写商品分类的ID",
					"required": "required"
				},
				"nums": {
					"name": "goods_number",
					"label": "显示个数：",
					"type": "radio",
					"value": [6,12,18],
					"number": true
				},
				"size": {
					"name": "size",
					"label": "列表样式：",
					"type": "radio",
					"value": ["大图","小图","一大两小","列表"]
				}
			}
		}
	},
	"cube": {
		"name": "魔方",
		"libs": {
			"form": {
				"url": {
					"name": "href",
					"type": "text",
					"label": "链接到："
				},
				"imgs": {
					"name": "src",
					"type": "hidden",
					"required": "required"
				}
			}
		}
	}
}
/*
*diy->init(congig);//编译标签为预览HTML结构
*diy->event();//模块点击事件 
*diy->submit();//反编译HTML结构为字符串 -> data-diy -> {diy 模块 标签 参数值（base64_encode）}
*/
</script>


	<div class="fixed-nav layout">
	    <ul>
	        <li class="first">微店编辑<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
	        <li class="spacer-gray"></li>
	    </ul>
	    <div class="hr-gray"></div>
	</div>
	<div class="have-fixed-nav"></div>
	<div class="app padding-big">
		<div class="app-inner clearfix">
			<div class="app-preview">
				<div class="app-header layout"></div>
				<div class="app-entry bg-white layout">
					<div class="app-content">
						<div class="app-config"></div>
						<div class="app-fields app-field-region">
							<?php echo $tmpl?>
						</div>
					</div>
					<div class="app-add-region">
						<h3>添加内容(添加的模块会自动跟随页面底部，可鼠标拖拽位置)</h3>
						<ul class="padding-left padding-bottom clearfix"></ul>
					</div>
				</div>
			</div>
			<div class="app-sidebar">
				<div class="sidebar-inner padding border bg-white margin-big-left">
					<b class="arrow"></b>
					<div class="sidebar-content"></div>
				</div>
			</div>
			<div class="app-actions">
				<input class="btn-add" type="submit" value="保存" />
				<!--<input class="btn-save" type="button" value="保存为草稿" />-->
				<input class="btn-cancel" type="button" value="取消" />
			</div>
		</div>
	</div>
	<div class="modal-wrap">
		<div class="modal">
			<div class="modal-body">
				<div class="goods-search-class-content">
					<div class="goods-add-class clearfix">
						<div class="root border focus"></div>
						<div class="child border focus"></div>
						<div class="child border focus"></div>
						<div class="child border focus"></div>
						<div class="layout fl margin-top goods-class-choose">
							<a class="button bg-white fr margin-right" id="cancel-class" href="javascript:;">取消选择</a>
							<a class="button bg-main fr margin-right" id="confirm-class" href="javascript:;">确认选择</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div class="modal-backdrop"></div>
	</div>
	<script>
		var url = "<?php echo url('diy_edit')?>",
			upload_url = "<?php echo url('goods/admin/upload')?>",
			code = '<?php echo $attachment_init ?>';
		
		jsoncategory = <?php echo json_encode($category) ?> ;
		diy.init(config);
		
		$(".dropdown").hover(function(){
			$(this).addClass("hover");
		},function(){
			$(this).removeClass("hover");
		});
		
		$(".goods-class-choose a").click(function(){
			$(".modal-wrap").removeClass("show");
			if($(this).index() == 1){
				if(classNameText()){
					$(".goods-category-choose").addClass("has-selected").html(classNameText());
					$(".goods-category-choose").next('input[type=hidden]').val(classId());
				}
			}
			$(".goods-add-class").children(".focus").html('');
		});
		
		function classNameText(){
			var _txt = '';
			$('.goods-add-class div.focus').each(function(){
				if($(this).find("a.focus").html()!=null){
					if($(this).index()==0){
						_txt += $(this).find("a.focus").html();
					}else{
						_txt += '/'+$(this).find("a.focus").html();
					}
				}
			})
			return _txt;
		}
		function classId(){
			var _txt = '';
			$('.goods-add-class div.focus').each(function(){
				if($(this).find("a.focus").html()!=null){
					_txt = $(this).find("a.focus").attr('data-id');
				}
			})
			return _txt;
		}
		$(document).on('click', '.goods-category-choose', function(){
			setCategory(parseInt($(this).next("input").val()))
		});
		
		function setCategory(num){
			var html = '';
			if(num == 0){
				$.each(jsoncategory, function(){
					if(this.parent_id == 0){
						html += " <a href = 'javascript:void(0)' onclick = 'nb_category(" + this.id + ",this)' id = 'cat" + this.id + "' data-id = " + this.id + " > " + this.name + " </a>";
					}
				})
				$(".root").html(html);
			}else{
				var nextHtml = '', level = 0, pid = 0;
				$.each(jsoncategory, function() {
					if(this.id == num){
						level = this.level;
						pid = this.parent_id;
					}
				});
				getCategory(num, pid, level)
			}
			$(".modal-wrap").addClass("show");
		}
		
		function getCategory(id, pid, level){
			var html = '';
			$.each(jsoncategory, function(){
				if(this.parent_id == pid){
					if(this.id == id){
						html += " <a href = 'javascript:void(0)' onclick = 'nb_category(" + this.id + ",this)' id = 'cat" + this.id + "' data-id = " + this.id + " class='focus'> " + this.name + " </a>";
					}else{
						html += " <a href = 'javascript:void(0)' onclick = 'nb_category(" + this.id + ",this)' id = 'cat" + this.id + "' data-id = " + this.id + " > " + this.name + " </a>";
					}
				}
			});
			$.each(jsoncategory, function() {
				if(this.id == pid){
					getCategory(this.id, this.parent_id, this.level)
				}
			});
			if(html) $(".goods-add-class").children(".focus").eq(level).html(html)
		}
		
	</script>
</body>
</html>