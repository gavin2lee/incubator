<?php include template('header','admin');?>
<script type="text/javascript" src="./statics/js/goods/goods_add.js" ></script>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">商品设置</li>
				<li class="spacer-gray"></li>
				<li>1.填写基本信息</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>2.设置商品规格</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>3.上传商品图册</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li class="goods-step">4.编辑商品类型</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>5.完善商品详情</li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
		<form action="<?php echo url('goods_add',array('step' => 3,'id' => $_GET['id']))?>" method="POST" name="goods_type" data-validate='true' data-handkey="goods_spec">
			<div class="form-box clearfix" id="form">
			<?php echo Form::input('select','type',$info['type'] ? $info['type'] : 0,"请选择商品属性：","置商品的类型属性，属性信息会在商品详情页显示，并且可以筛选本件商品",array('items'=>$info['extra']['type_name']))?>
			</div>
			<div class="goods-attr-content margin border">
			<?php foreach ($info['extra']['attr'] AS $key => $attrinfo){?>
				<table data-id="<?php echo $key?>" class="hidden">
				<?php foreach ($attrinfo as $key => $attr) {?>
				<?php if($attr){?>
					<tr>
						<th width="120"><?php echo $attr['name']?></th>
						<td>
						<?php if($attr['type'] != 'input'){?>
						<?php foreach ($attr['value'] AS $value) {?>
							<div class="area-box" data-id="<?php echo $attr['id']?>">
								<label class="select-wrap"><input class="select-btn" type="<?php echo $attr['type']?>" name="attr[<?php echo $attr['id']?>][]" value="<?php echo $value?>"
								<?php if($attr['type'] == 'radio'){?>
								<?php if($value == $info['attr'][$attr['id']][0]){?>checked="checked"<?php }?>
								<?php }else{?>
								<?php foreach($info['attr'][$attr['id']] AS $info_attr){ if($value == $info_attr){?>checked="checked"<?php }?>
								<?php }?>
								<?php }?>
								><?php echo $value?></label>
							</div>
						<?php }?>
						<?php }else{?>
						<div class="area-box" data-id="<?php echo $attr['id']?>">
							<input class="input" type="text" name="attr[<?php echo $attr['id']?>][]" value="<?php echo $info['attr'][$attr['id']][0]?>" />
						</div>
						<?php }?>
						</td>
					</tr>
					<?php }?>
				<?php }?>
				</table>
				<?php }?>
			</div>
			<div class="margin-tb padding">
				<input type="submit" class="button bg-main" value="下一步" id="goods_attr"/>
				<a class="button margin-left bg-gray" data-back="false" id="back">上一步</a>
			</div>
		</form>
		</div>
		<script>
			var url = "<?php echo url('goods_add',array('id'=>$_GET['id'],'step'=>3))?>";
			var back = "<?php echo url('goods_add',array('id'=>$_GET['id'],'step'=>2))?>";
			save_back(url,back);
			setInterval("auto_save(url)",30000);
			var goods_type = $("[name=goods_type]").Validform({
				ajaxPost:true,
				callback:function(result) {
					if(result.status == 1){
						var nexturl = "<?php echo url('goods_add',array('step' => 4,'id' => $_GET['id']))?>";
						window.location = nexturl;
					}else{
						alert(result.message);
					}
				}
			});
			$(function(){
				show();
			})
			$('.box .form-select-name').change(function(){
				show();
			})
			function show(){
				var type_id = $('.box .listbox-item-selected').data('val');
				$('table[data-id="' + type_id + '"]').removeClass('hidden').siblings().addClass('hidden');
				$('table[data-id="' + type_id + '"]').siblings().find('input').each(function() {
					$(this).removeAttr('checked');
				});
				if(type_id == 0){
					$('table').addClass('hidden');
				}
			}
			var attr_id = '<?php echo key($info['attr'])?>';
			$('div[data-id="'+ attr_id +'"]').parents('table').removeClass('hidden');
			$('span[data-val="' + $('div[data-id="'+ attr_id +'"]').parents('table').attr('data-id') + '"]').addClass('listbox-item-selected').siblings().removeClass('listbox-item-selected');
			$('span[data-val="' + $('div[data-id="'+ attr_id +'"]').parents('table').attr('data-id') + '"]').parent().siblings().find('.input').val($('.listbox-item-selected').html());
		</script>
	</body>
</html>
