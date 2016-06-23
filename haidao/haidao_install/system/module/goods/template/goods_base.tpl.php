<?php include template('header','admin');?>
<script type="text/javascript" src="./statics/js/goods/goods_add.js" ></script>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">商品设置</li>
				<li class="spacer-gray"></li>
				<li class="goods-step">1.填写基本信息</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>2.设置商品规格</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>3.上传商品图册</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>4.编辑商品类型</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>5.完善商品详情</li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
		<form action="<?php echo url('goods_add',array('id' => $_GET['id']))?>" method="post" name="release_goods">
			<div class="padding-lr">
				<div class="form-group">
					<span class="label">商品分类：</span>
					<div class="box ">
							<input class="goods-class-text input hd-input input-readonly" id="choosecat" name="catname" value="<?php echo $info['catname']?>" tabindex="0"  nullmsg="请选择商品分类" datatype="*" readonly="readonly" type="text" placeholder="请选择商品分类" data-reset="false" />
							<input class="goods-class-btn" type="button" value="选择" onclick="setClass()" data-reset="false" />
							<input type="hidden" name="catid" value="<?php echo $info['catid']?>">
							<input type="hidden" name="cat_format" value="<?php echo $info['cat_format']?>">
					</div>
					<p class="desc">选择商品所属分类，一个商品只能属于一个分类</p>
				</div>
			</div>
			<div class="form-box goods-form">
			 	<?php echo Form::input('text', 'name', $info['name'],'商品名称：','商品标题名称不能为空，最长不能超过200个字符',array('datatype'	=> '*','nullmsg'	=> '商品名称不能为空')); ?>
				<input type="hidden" name="id" value="<?php echo $info['id']?>">
				<?php echo Form::input('text', 'subtitle', $info['subtitle'], '广告语：', '商品广告语是用于介绍商品的描述信息',array('color' => $info['style'] ? $info['style'] : '', 'key' => 'style')); ?>
				<div class="form-group" style="z-index: 2;">
				        <span class="label">商品品牌：</span>
				        <div class="box" style="width: 256px;">
			                <div class="form-select-edit select-search-text-box">
			                    <div class="form-buttonedit-popup">
			                        <input class="input" type="text" value="<?php echo $info['brandname']?>" readonly="readonly" data-reset="false">
			                        <span class="ico_buttonedit"></span>
			                        <input type="hidden" name="brand_id" value="<?php echo $info['brand_id']?>" data-reset="false">
			                    </div>
			                    <div class="select-search-field border border-main">
		                    		<input class="input border-none" autocomplete="off" type="text" id="brandname" value="" placeholder="请输入品牌名称" data-reset="false" />
		                    		<i class="ico_search"></i>
		                    	</div>
			                    <div class="listbox-items brand-list">
			                    <?php foreach ($info['extra'] AS $brand) {?>
			                    	<span class="listbox-item" data-val="<?php echo $brand['id']?>"><?php echo $brand['name']?></span>
			                    <?php }?>
			                   </div>
			                </div>
				        </div>
				        <p class="desc">为商品选择所属品牌，便于用户按照品牌进行查找</p>
				</div>
				<?php echo Form::input('text', 'warn_number', isset($info['warn_number']) ? $info['warn_number'] : 5,  '库存警告：', '填写商品库存警告数，当库存小于等于警告数，系统就会提醒此商品为库存警告商品，系统默认为5',array('datatype'	=> 'n','errormsg' => '库存警告只能为数字')); ?>
				<?php echo Form::input('enabled','status', isset($info['status']) ? $info['status'] : '1','是否上架销售：','设置当前商品是否上架销售，默认为是，如选择否，将不在前台显示该商品',array('itemrows' => 2)); ?>
				<?php echo Form::input('text', 'sort', isset($info['sort']) ? $info['sort'] : 100, '商品排序：','请填写自然数，商品列表将会根据排序进行由小到大排列显示',array('datatype'	=> 'n','errormsg' => '排序只能为数字')); ?>
				<?php echo Form::input('text', 'keyword', $info['keyword'], '商品关键词：', 'Keywords项出现在页面头部的<Meta>标签中，用于记录本页面的关键字，多个关键字请用分隔符分隔'); ?>
				<?php echo Form::input('textarea','description', $info['description'], '商品描述：','Description出现在页面头部的Meta标签中，用于记录本页面的高腰与描述，建议不超过80个字'); ?>
			</div>
			<div class="padding">
				<input type="submit" id="release" class="button bg-main" value="下一步" />
				<a href="<?php echo url('index')?>"><input type="button" class="button margin-left bg-gray" data-reset="false" value="返回" /></a>
			</div>
		</form>
		</div>
		<script type="text/javascript" src="./statics/js/haidao.validate.js?v=5.3.2" ></script>
		<script>
			$('.form-group:last-child').addClass('last-group');
			$(function(){	
				var release_goods = $("[name=release_goods]").Validform({
					ajaxPost:true,
					callback:function(result) {
						if(result.status == 1){
							var nexturl = "<?php echo url('goods_add',array('step' => 1,'id' => $_GET['id']))?>"; 
							window.location = nexturl;
						}else{
							alert(result.message);
						}
					}			
				});
			})
			var url = "<?php echo url('goods_add',array('id'=>$_GET['id']))?>";
			setInterval("auto_save(url)",30000);
			//弹窗
			function setClass(){
				var url = "<?php echo url('category/category_popup')?>";
				var pid = $('input[name=catid]').val();
				var pname = $('#choosecat').val();
				var pvalue = $('input[name=cat_format]').val();
				var data = [pid,pname,pvalue];
				top.dialog({
					url: url,
					title: '加载中...',
					data: data,
					width: 930,
					onclose: function () {
						if(this.returnValue){
							var catname = this.returnValue.html().replace(/&gt;/g,'>'); 
							$('#choosecat').val(catname);
							var catids = this.returnValue.attr('data-id').split(',');
							var catid = catids[catids.length-1];
							$('input[name=cat_format]').val(this.returnValue.attr('data-id'));
							$('input[name=catid]').val(catid);
						}
					}
				})
				.showModal();
			}
				$('.select-search-field').click(function(e){
					e.stopPropagation();
				});
				//buttonedit-popup-hover
				$('.select-search-text-box .form-buttonedit-popup').click(function(){
					if(!$(this).hasClass('buttonedit-popup-hover')){
						$(this).parent().find('.select-search-field').show();
						$(this).parent().find('.select-search-field').children('.input').focus();
						$(this).parent().find('.listbox-items').show();
					}else{
						$(this).parent().find('.select-search-field').hide();
						$(this).parent().find('.listbox-items').hide();
					}
				});
				$('#brandname').live('keyup',function(){
					var url = "<?php echo url('ajax_brand')?>";
					var brandname = this.value;
					$.post(url,{brandname:brandname},function(data){
						$('.brand-list').children('.listbox-item').remove();
						if(data.status == 1){
							var html = '';
							$.each(data.result,function(i,item){
								 html += '<span class="listbox-item" data-val="' + i + '">' + item + '</span>';
							})
							$('.brand-list').append(html);
						}else{
							var html = '<span class="listbox-item">未搜索到结果</span>';
							$('.brand-list').append(html);
						}
					},'json')
				})
				$(".select-search-text-box .listbox-items .listbox-item").live('click',function(){
					$(this).parent().prev('.select-search-field').children('.input').val();
					$(this).parent().prev('.select-search-field').hide();
					$('.select-search-text-box .form-buttonedit-popup .input').val($(this).html());
					$('input[name=brand_id]').val($(this).attr('data-val'));
				});
		</script>
	</body>
</html>
