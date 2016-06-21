<?php include template('header','admin');?>
		<div class="fixed-nav layout">
			<ul>
				<li class="first">会员等级<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
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
					<p>- 通过会员等级管理，可以制定不同的经验值达到不同的会员等级，并且享受不同的折扣价格</p>
				</div>
			</div>
			<div class="table-work border margin-tb">
				<div class="border border-white tw-wrap">
					<a href="<?php echo url('add') ?>"><i class="ico_add"></i>添加</a>
					<div class="spacer-gray"></div>
					<a data-message="是否确定删除所选？" href="<?php echo url('delete')?>" data-ajax='id'><i class="ico_delete"></i>删除</a>
					<div class="spacer-gray"></div>
				</div>
			</div>
			<div class="table-wrap member-info-table">
				<div class="table resize-table check-table paging-table border clearfix">
					<div class="tr">
						<span class="th check-option" data-resize="false">
							<span><input id="check-all" type="checkbox" /></span>
						</span>
						<span class="th" data-width="25">
							<span class="td-con">等级名称</span>
						</span>
						<span class="th" data-width="20">
							<span class="td-con">最小经验</span>
						</span>
						<span class="th" data-width="15">
							<span class="td-con">最大经验</span>
						</span>
						<span class="th" data-width="20">
							<span class="td-con">折扣率</span>
						</span>
						<span class="th" data-width="20">
							<span class="td-con">操作</span>
						</span>
					</div>
					<?php foreach ($groups AS $group): ?>
					<div class="tr">
						<span class="td check-option"><input type="checkbox" name="id" value="<?php echo $group['id'] ?>" /></span>
						<div class="td"><?php echo $group['name'] ?></div>
						<div class="td"><?php echo $group['min_points'] ?></div>
						<div class="td"><?php echo $group['max_points'] ?></div>
						<div class="td"><?php echo (int) $group['discount'] ?>%</div>
						<div class="td">
							<a href="<?php echo url('edit', array("id" => $group['id'])) ?>">编辑</a>&nbsp;&nbsp;&nbsp;
							<a href="<?php echo url('delete', array("id" => $group['id'])) ?>" data-confirm="是否确定删除？">删除</a>
						</div>
					</div>
					<?php endforeach ?>
					<div class="paging padding-tb body-bg clearfix">
						<?php echo $pages; ?>
						<div class="clear"></div>
					</div>
				</div>
			</div>
		</div>
		<script>
			$(".table").resizableColumns();
			$(".paging-table").fixedPaging();
		</script>
	</body>
</html>
