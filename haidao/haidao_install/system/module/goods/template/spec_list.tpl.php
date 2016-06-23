<?php include template('header','admin');?>
	<script type="text/javascript" src="./statics/js/goods/goods_list.js"></script>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">规格列表<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
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
					<p>- 规格支持三种展示方式，需在商品编辑时指定规格的展示方式。</p>
				</div>
			</div>
			<div class="hr-gray"></div>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('ajax_del')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table paging-table resize-table check-table border clearfix">
				<div class="tr">
					<span class="th check-option" data-resize="false">
						<span><input id="check-all" type="checkbox" /></span>
					</span>
					<span class="th" data-width="20">
						<span class="td-con">规格名称</span>
					</span>
					<span class="th" data-width="50">
						<span class="td-con">规格属性</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">排序</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">启用</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach ($spec as $r) {?>
				<div class="tr">
					<span class="td check-option"><input type="checkbox" name="id" value="<?php echo $r['id']?>" /></span>
					<span class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input class="input double-click-edit text-ellipsis" type="text" name="name" data-id="<?php echo $r['id']?>" value="<?php echo $r['name']?>" />
							</div>
						</div>
					</span>
					<span class="td">
						<span class="td-con text-left"><?php echo $r['value']?></span>
					</span>
					<span class="td">
						<div class="td-con">
							<div class="double-click">
								<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
								<input class="input double-click-edit text-ellipsis text-center" name="sort" data-id="<?php echo $r['id']?>" type="text" value="<?php echo $r['sort']?>" />
							</div>
						</div>
					</span>
					<span class="td">
					<?php if($r['status'] == 1){ ?>
						<a class="ico_up_rack" href="javascript:;" data-id="<?php echo $r['id']?>" title="点击关闭"></a>
					<?php }else{?>
						<a class="ico_up_rack cancel" href="javascript:;" data-id="<?php echo $r['id']?>" title="点击开启"></a>
					<?php }?>
					</span>
					<span class="td">
						<span class="td-con">
							<a href="<?php echo url('edit',array('id'=>$r['id']))?>">编辑</a>&nbsp;&nbsp;&nbsp;<a data-confirm="是否确认删除？" href="<?php echo url('ajax_del',array('id[]'=>$r['id']))?>">删除</a>
						</span>
					</span>
				</div>
				<?php }?>
				<div class="paging padding-tb body-bg clearfix">
					<?php echo $pages;?>
					<div class="clear"></div>
				</div>
			</div>
		</div>
		<script>
		var ajax_status = "<?php echo url('ajax_status')?>";
		var ajax_name = "<?php echo url('ajax_name')?>";
		var ajax_sort = "<?php echo url('ajax_sort')?>";
			$(window).load(function(){
				$(".table").resizableColumns({
					isPercent: true //是否是百分比
				});
				$(".paging-table").fixedPaging();
				//启用与关闭
				$(".table .ico_up_rack").bind('click',function(){
					var id = $(this).attr('data-id');
					var row = $(this);
					list_action.change_status(ajax_status,id,row);
					
				});
				$('input[name=name]').bind('blur',function() {
					var name = $(this).val();
					var id = $(this).attr('data-id');
					list_action.change_name(ajax_name,id,name);
				});
				$('input[name=sort]').bind('blur',function() {
					var sort = $(this).val();
					var id = $(this).attr('data-id');
					list_action.change_sort(ajax_sort,id,sort);
				});
			})
		</script>
	</body>
</html>
