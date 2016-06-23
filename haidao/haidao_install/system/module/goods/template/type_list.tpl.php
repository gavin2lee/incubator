<?php include template('header','admin');?>
	<script type="text/javascript" src="./statics/js/goods/goods_list.js"></script>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">商品类型<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		
		<div class="content padding-big have-fixed-nav">
			<div class="tips margin-tb">
				<div class="tips-info border">
					<h6>温馨提示</h6>
					<a id="show-tip" data-open="true" href="javascript:;">点击关闭操作提示</a>
				</div>
				<div class="tips-txt padding-small-top layout">
					<p>- 添加商品分类时需选择类型，前台分类下商品列表页通过类型生成商品检索，方便用户搜索需要的商品</p>
				</div>
			</div>
			<div class="hr-gray"></div>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add')?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('delete')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table-wrap resize-table">
				<div class="table paging-table resize-table check-table border clearfix">
					<div class="tr">
						<span class="th check-option" data-resize="false">
							<span><input id="check-all" type="checkbox" /></span>
						</span>
						<span class="th" data-width="15">
							<span class="td-con">属性名称</span>
						</span>
						<span class="th" data-width="55">
							<span class="td-con">属性标签</span>
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
					<?php foreach ($type AS $r) {?>
					<div class="tr">
						<span class="td check-option"><input type="checkbox" name="id" value="<?php echo $r['id']?>"/></span>
						<span class="td">
							<span class="td-con">
								<div class="double-click">
									<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
									<input class="input double-click-edit text-ellipsis" name="name" data-id="<?php echo $r['id']?>" type="text" value="<?php echo $r['name']?>" />
								</div>
							</span>
						</span>
						<span class="td">
							<span class="td-con text-left"><?php echo $r['desc']?></span>
						</span>
						<span class="td">
							<span class="td-con">
								<div class="double-click">
									<a class="double-click-button margin-none padding-none" title="双击可编辑" href="javascript:;"></a>
									<input class="input double-click-edit text-ellipsis text-center" name="sort" data-id="<?php echo $r['id']?>" type="text" value="<?php echo $r['sort']?>" />
								</div>
							</span>
						</span>
						<span class="td">
						<?php if($r['status'] == 1){?>
							<a class="ico_up_rack" href="javascript:;" data-id="<?php echo $r['id']?>" title="点击关闭"></a>
						<?php }else{?>
							<a class="ico_up_rack cancel" href="javascript:;" data-id="<?php echo $r['id']?>" title="点击关闭"></a>
						<?php }?>
						</span>
						<span class="td">
							<span class="td-con"><a href="<?php echo url('edit',array('id'=>$r["id"]))?>">编辑</a>&nbsp;&nbsp;&nbsp;<a data-confirm="是否确认删除？" href="<?php echo url('delete',array('id[]'=>$r["id"]))?>">删除</a></span>
						</span>
					</div>
					<?php }?>
					<div class="paging padding-tb body-bg clearfix">
					<?php echo $pages;?>
					<div class="clear"></div>
				</div>
				</div>
			</div>
		</div>
		<script>
			var ajax_status = "<?php echo url('ajax_status')?>";
			var ajax_name = "<?php echo url('ajax_name')?>";
			var ajax_sort = "<?php echo url('ajax_sort')?>";
			$(window).load(function(){
				$(".table").resizableColumns();
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
