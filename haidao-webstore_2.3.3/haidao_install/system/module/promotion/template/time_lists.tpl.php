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
					<p>- 针对自营商品SKU在有效时间内的价格促销活动</p>
					<p>- 限时促销是完全独立活动，不参与其他形式促销</p>
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
					<span class="th" data-width="40">
						<span class="td-con">促销名称</span>
					</span>
					<span class="th" data-width="40">
						<span class="td-con">促销时间</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">状态</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach ($info AS $prom) {?>
				<div class="tr">
					<div class="td check-option"><input type="checkbox" name="id" value="<?php echo $prom['id']?>" /></div>
					<span class="td">
						<div class="double-click">
							<a class="double-click-button" title="双击可编辑" href="javascript:;"></a>
							<input class="input double-click-edit text-ellipsis" name="name" data-id="<?php echo $prom['id']?>" type="text" value="<?php echo $prom['name']?>">
						</div>
					</span>
					<span class="td">
						<span class="td-con"><?php echo date('Y-m-d H:i:s',$prom['start_time'])?> ~ <?php echo date('Y-m-d H:i:s',$prom['end_time'])?></span>
					</span>
					<span class="td">
						<span class="td-con"><?php if($prom['status'] == 1){?>未开始<?php }elseif($prom['status'] == 2){?>已结束<?php }else{?>进行中<?php }?></span>
					</span>
					<span class="td">
						<span class="td-con"><a href="<?php echo url('edit',array('id'=>$prom['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;<a data-confirm="是否确认删除？" href="<?php echo url('delete',array('id'=>$prom['id']))?>">删除</a></span>
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
			var ajax_name = "<?php echo url('ajax_name')?>";
			$(".table").resizableColumns();
			$(".table").fixedPaging();
			$('input[name=name]').bind('blur',function() {
				var name = $(this).val();
				var id = $(this).attr('data-id');
				list_action.change_name(ajax_name,id,name);
			});
		</script>