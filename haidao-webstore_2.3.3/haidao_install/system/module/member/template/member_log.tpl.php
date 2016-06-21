<?php include template('header','admin');?>
<div class="fixed-nav layout">
	<ul>
		<li class="first">余额管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
		<li class="spacer-gray"></li>
	</ul>
	<div class="hr-gray"></div>
</div>
<div class="content padding-big have-fixed-nav">
	<div class="table-work border margin-tb">
		<div class="border border-white tw-wrap">
		</div>
	</div>
	<div class="table-wrap member-info-table">
		<div class="table resize-table check-table border paging-table clearfix">
			<div class="member  tr">
				<span class="th" data-width="10">
					<span class="td-con">会员账号</span>
				</span>
				<span class="th" data-width="20">
					<span class="td-con">操作日期</span>
				</span>
				<span class="th" data-width="20">
					<span class="td-con">操作金额</span>
				</span>
				<span class="th" data-width="40">
					<span class="td-con">日志描述</span>
				</span>
				<span class="th" data-width="10">
					<span class="td-con">状态</span>
				</span>
			</div>
			<?php foreach ($lists AS $r): ?>
			<div class="member tr">
				<!-- <span class="td check-option"><input type="checkbox" name="id" value="1" /></span> -->
				<div class="td"><span class="td-con"><?php echo $r['username'] ?></span></div>
				<div class="td">
					<span class="td-con"><?php echo date('Y-m-d H:i:s', $r['dateline']); ?></span>
				</div>
				<div class="td">
					<span class="td-con "><?php echo $r['value'];?></span>
				</div>
				<div class="td">
					<span class="td-con"><?php echo $r['msg'] ?></span>
				</div>
				<div class="td">
					<span class="td-con">成功</span>
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
		/*$('.batch-delete').batchDelete({
			url: "<?php echo url('del')?>",
			formhash: "<?php echo FORMHASH?>"
		});*/
	</script>
</body>
</html>
