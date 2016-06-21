<?php include template('header','admin');?>
	<script type="text/javascript" src="./statics/js/goods/goods_list.js"></script>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">限时促销<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
				<li><a class="current" href="javascript:;"></a></li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
			<div class="tips margin-tb">
				<div class="tips-info border">
					<h6>温馨提示</h6>
					<a id="show-tip" data-open="true" href="javascript:;">关闭操作提示</a>
				</div>
				<div class="tips-txt padding-small-top layout">
					<p>- 可以自由搭配相关的商品进行组合推荐，提高客单价</p>
					<p>- 组合营销并不是组合促销，不支持组合套餐价格</p>
				</div>
			</div>
			<div class="hr-gray"></div>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('edit')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('delete')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table resize-table paging-table check-table border clearfix">
				<div class="tr">
					<span class="th check-option" data-resize="false">
						<span><input id="check-all" type="checkbox" /></span>
					</span>
					<span class="th" data-width="30">
						<span class="td-con">组合标题</span>
					</span>
					<span class="th" data-width="25">
						<span class="td-con">组合名称</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">商品数量</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">状态</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach ($info AS $group) {?>
				<div class="tr">
					<div class="td check-option"><input type="checkbox" name="id" value="<?php echo $group['id']?>" /></div>
					<span class="td">
						<div class="double-click">
							<a class="double-click-button" title="双击可编辑" href="javascript:;"></a>
							<input class="input double-click-edit text-ellipsis" name="title" data-id="<?php echo $group['id']?>" type="text" value="<?php echo $group['title']?>">
						</div>
					</span>
					<span class="td">
						<div class="double-click">
							<a class="double-click-button" title="双击可编辑" href="javascript:;"></a>
							<input class="input double-click-edit text-ellipsis" name="subtitle" data-id="<?php echo $group['id']?>" type="text" value="<?php echo $group['subtitle']?>">
						</div>
					</span>
					<span class="td">
						<span class="td-con"><?php echo $group['sku_ids'] ? count(explode(',', $group['sku_ids'])) : 0?></span>
					</span>
					<span class="td">
						<span class="td-con">
						<?php if($group['status'] == 1){?>
							<a class="ico_up_rack" data-id="<?php echo $group['id']?>" href="javascript:;" title="点击关闭"></a>
						<?php }else{?>
							<a class="ico_up_rack cancel" data-id="<?php echo $group['id']?>" href="javascript:;" title="点击开启"></a>
						<?php }?>
						</span>
					</span>
					<span class="td">
						<span class="td-con"><a href="<?php echo url('edit',array('id' => $group['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;<a data-confirm="是否确认删除？" href="<?php echo url('delete',array('id' => $group['id']))?>">删除</a></span>
					</span>
				</div>
				<?php }?>
				<div class="paging padding-tb body-bg clearfix">
					<?php echo $pages?>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<script>
			var ajax_status = "<?php echo url('ajax_status')?>";
			var ajax_name = "<?php echo url('ajax_name')?>";
			var ajax_subtitle = "<?php echo url('ajax_subtitle')?>";
			$(".table").resizableColumns();
			$(".table").fixedPaging();
			//启用与关闭
			$(".table .ico_up_rack").bind('click',function(){
				var id = $(this).attr('data-id');
				var row = $(this);
				list_action.change_status(ajax_status,id,row);
				
			});
			$('input[name=title]').bind('blur',function() {
				var name = $(this).val();
				var id = $(this).attr('data-id');
				list_action.change_name(ajax_name,id,name);
			});
			$('input[name=subtitle]').bind('blur',function() {
				var name = $(this).val();
				var id = $(this).attr('data-id');
				list_action.change_name(ajax_subtitle,id,name);
			});
		</script>
