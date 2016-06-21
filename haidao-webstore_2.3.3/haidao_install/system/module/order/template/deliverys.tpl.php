<?php include template('header','admin');?>
<script type="text/javascript" src="<?php echo __ROOT__ ?>statics/js/admin/order_action.js"></script>

	<div class="fixed-nav layout">
		<ul>
			<li class="first">快递单管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
			<li class="spacer-gray"></li>
			<li><a <?php if (isset($_GET['isprint']) && $_GET['isprint'] == 2) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/deliverys',array('isprint'=>'2')) ?>">全部</a></li>
			<li><a <?php if (!isset($_GET['isprint']) || isset($_GET['isprint']) && $_GET['isprint'] == 0) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/deliverys',array('isprint'=>'0')) ?>">未打印</a></li>
			<li><a <?php if (isset($_GET['isprint']) && $_GET['isprint'] == 1) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/deliverys',array('isprint'=>'1')) ?>">已打印</a></li>
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
				<p>- 添加商品时可选择商品分类，用户可根据分类查询商品列表</p>
				<p>- 点击分类名前“+”符号，显示当前分类的下级分类</p>
				<p>- 对分类作任何更改后，都需要到 设置 -> 清理缓存 清理商品分类，新的设置才会生效</p>
			</div>
		</div>
		<!-- <div class="hr-gray"></div>
		<div class="clearfix">
			<form>
				<input type="hidden" name="m" value="order" />
				<input type="hidden" name="c" value="admin_order" />
				<input type="hidden" name="a" value="parcel" />
				<div class="form-group form-layout-rank border-none" style="width: 300px;">
					<span class="label" style="width: auto;">搜索</span>
					<div class="box ">
						<div class="field margin-none">
							<input class="input" type="text" name="keyword" value="<?php echo $_GET['keyword']; ?>" placeholder="输入订单号/物流名称/物流编号" tabindex="0">
						</div>
					</div>
				</div>
				<input class="button bg-sub margin-top fl" type="submit" style="height: 26px; line-height: 14px;" value="查询">
			</form>
		</div> -->
		<div class="table-wrap">
			<div class="table resize-table paging-table border clearfix">
				<div class="tr">
					<span class="th" data-width="15">
						<span class="td-con">订单号</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">物流名称</span>
					</span>
					<span class="th" data-width="20">
						<span class="td-con">物流编号</span>
					</span>
					<span class="th" data-width="20">
						<span class="td-con">发货时间</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">是否收货</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">打印状态</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">操作</span>
					</span>
				</div>

				<?php foreach ($o_deliverys as $v) : ?>
					<div class="tr">
						<span class="td">
							<span class="td-con"><?php echo $v['_sub_order']['order_sn']; ?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo $v['delivery_name']; ?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo $v['delivery_sn']; ?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo date('Y-m-d H:i:s' ,$v['delivery_time']);?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo ($v['receive_time'] == 0) ? '未收货' : '已收货'; ?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo ($v['print_time'] == 0) ? '未打印' : '已打印'; ?></span>
						</span>
						<span class="td">
							<span class="td-con">
								<a href="<?php echo url('order/admin_order/print_kd',array('o_id' => $v['id']))?>">打印</a>
							</span>
						</span>
					</div>
				<?php endforeach; ?>
				<!-- 分页 -->
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