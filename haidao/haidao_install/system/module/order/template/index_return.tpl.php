<?php include template('header','admin');?>

		<div class="fixed-nav layout">
			<ul>
				<li class="first">退货单管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
				<li class="spacer-gray"></li>
				<li><a <?php if(!isset($_GET['type'])) {echo ' class="current"';}?> href="<?php echo url('order/admin_server/index_return'); ?>">全部</a></li>
				<li><a <?php if(isset($_GET['type']) && $_GET['type']==0) {echo ' class="current"';}?> class="" href="<?php echo url('order/admin_server/index_return',array('type' => 0)); ?>">待处理</a></li>
				<li><a <?php if($_GET['type']==1) {echo ' class="current"';}?> class="" href="<?php echo url('order/admin_server/index_return',array('type' => 1)); ?>">已处理</a></li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
			<form action="<?php echo __APP__ ?>" method='get'>
				<input type="hidden" name="m" value="order">
				<input type="hidden" name="c" value="admin_server">
				<input type="hidden" name="a" value="index_return">
				<div class="clearfix">
					<div class="form-group form-layout-rank border-none" style="width: 300px;">
						<span class="label" style="width: auto;">搜索</span>
						<div class="box ">
							<div class="field margin-none">
								<input class="input" type="text" name="keywords" placeholder="<?php if($_GET['keywords']) : ?><?php echo $_GET['keywords']; ?><?php else: ?>输入订单号/会员账号/手机号<?php endif;?>" tabindex="0">
							</div>
						</div>
					</div>
					<input class="button bg-sub margin-top fl" type="submit" style="height: 26px; line-height: 14px;" value="查询">
				</div>
			</form>
			<div class="table-wrap">
				<div class="table resize-table paging-table high-table border clearfix">
					<div class="tr">
						<span class="th w1_5" data-width="30">
							<span class="td-con">退货商品</span>
						</span>
						<span class="th w1_5" data-width="10">
							<span class="td-con">会员账号</span>
						</span>
						<span class="th w1_5" data-width="10">
							<span class="td-con">退款金额</span>
						</span>
						<span class="th w1" data-width="15">
							<span class="td-con">申请时间</span>
						</span>
						<span class="th w1" data-width="15">
							<span class="td-con">物流信息</span>
						</span>
						<span class="th w1" data-width="10">
							<span class="td-con">处理状态</span>
						</span>
						<span class="th w1" data-width="10">
							<span class="td-con">操作</span>
						</span>
					</div>
					<?php foreach ($infos['lists'] as $val) : ?>
						<div class="tr">
							<span class="td">
								<div class="td-con td-pic text-left">
									<span class="pic"><img src="<?php echo $val['_sku']['sku_thumb']; ?>" /></span>
									<span class="title text-ellipsis txt"><a href="" target="_blank"><?php echo $val['_sku']['sku_name']; ?></a></span>
									<span class="icon">
										<?php foreach ($val['_sku']['sku_spec'] as $spec) : ?>
											<em class="text-main"><?php echo $spec['name'] ?>：</em><?php echo $spec['value'] ?>&nbsp;
										<?php endforeach; ?>
									</span>
								</div>
							</span>
							<span class="td">
								<span class="td-con"><?php echo $val['_buyer']['username']; ?></span>
							</span>
							<span class="td">
								<span class="td-con">￥<?php echo $val['amount'];?></span>
							</span>
							<span class="td">
								<span class="td-con"><?php echo date('Y-m-d H:i:s',$val['dateline']);?></span>
							</span>
							<span class="td">
								<?php if ($val['status'] == 2) : ?>
									<span class="td-con double-row text-left">物流公司：<?php echo $val['delivery_name'];?><br />物流单号：<?php echo $val['delivery_sn'];?></span>
								<?php else: ?>
									<span class="td-con text-center">--</span>
								<?php endif; ?>
							</span>
							<span class="td">
								<span class="td-con"><?php echo $val['_status'] ?></span>
							</span>
							<span class="td">
								<span class="td-con">
									<a href="<?php echo url('order/admin_server/detail_return',array('id' => $val['id'])) ?>"><?php if($val['status'] == 0): ?>处理<?php else: ?>查看<?php endif; ?></a>&emsp;
								</span>
							</span>
						</div>
					<?php endforeach; ?>
					<div class="paging padding-tb body-bg clearfix">
						<ul class="fr"><?php echo $pages; ?></ul>
						<div class="clear"></div>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>

<script>
	$(".form-group .box").addClass("margin-none");
	$(window).load(function(){
		$(".table").resizableColumns();
		$(".paging-table").fixedPaging();
	})
</script>