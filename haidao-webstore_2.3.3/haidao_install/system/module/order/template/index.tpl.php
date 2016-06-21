<?php include template('header','admin');?>

	<div class="fixed-nav layout">
		<ul>
			<li class="first">订单管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
			<li class="spacer-gray"></li>
			<?php $url_arr = $_GET;unset($url_arr['type'],$url_arr['page'],$url_arr['formhash']); ?>
			<li><a <?php if (empty($_GET['type'])){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index').'&'.http_build_query($url_arr); ?>">全部订单</a></li>
			<li><a <?php if ($_GET['type'] == 1){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index',array('type'=>1)).'&'.http_build_query($url_arr); ?>">待付款</a></li>
			<li><a <?php if ($_GET['type'] == 2){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index',array('type'=>2)).'&'.http_build_query($url_arr); ?>">待确认</a></li>
			<li><a <?php if ($_GET['type'] == 3){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index',array('type'=>3)).'&'.http_build_query($url_arr); ?>">待发货</a></li>
			<li><a <?php if ($_GET['type'] == 4){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index',array('type'=>4)).'&'.http_build_query($url_arr); ?>">已发货</a></li>
			<li><a <?php if ($_GET['type'] == 5){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index',array('type'=>5)).'&'.http_build_query($url_arr); ?>">已完成</a></li>
			<li><a <?php if ($_GET['type'] == 6){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index',array('type'=>6)).'&'.http_build_query($url_arr); ?>">已取消</a></li>
			<li><a <?php if ($_GET['type'] == 7){echo 'class="current"';}?> href="<?php echo url('order/admin_order/index',array('type'=>7)).'&'.http_build_query($url_arr); ?>">已回收</a></li>
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
				<p>- 每一个订单均需要经过确认操作才能进行发货货到付款订单须有商家在后台确认订单完成</p>
			</div>
		</div>
		<div class="hr-gray"></div>
		<div class="clearfix">
			<form method="get" >
				<input type="hidden" name="m" value="order" />
				<input type="hidden" name="c" value="admin_order" />
				<input type="hidden" name="a" value="index" />
				<?php if ($_GET['type']): ?>
					<input type="hidden" name="type" value="<?php echo $_GET['type'];?>" />
				<?php endif; ?>
			<div class="form-group form-layout-rank border-none" style="width: 300px;">
				<span class="label" style="width: auto;">搜索</span>
				<div class="box ">
					<div class="field margin-none">
						<input class="input" type="text" name="keyword" value="<?php echo $_GET['keyword'] ?>" placeholder="订单号/收货人姓名/收货人手机号/会员帐号" tabindex="1">
					</div>
				</div>
			</div>
			<input type="submit" class="button bg-sub margin-top fl" style="height: 26px; line-height: 14px;" value="查询">
			</form>
		</div>
		<div class="table-wrap">
			<div class="table resize-table paging-table border clearfix">
				<div class="tr">
					<span class="th" data-width="15">
						<span class="td-con">订单号</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">会员帐号</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">收货人</span>
					</span>
					<span class="th" data-width="9">
						<span class="td-con">收货电话</span>
					</span>
					<span class="th" data-width="12">
						<span class="td-con">下单时间</span>
					</span>
					<span class="th" data-width="8">
						<span class="td-con">订单金额</span>
					</span>
					<span class="th" data-width="7">
						<span class="td-con">支付方式</span>
					</span>
					<span class="th" data-width="8">
						<span class="td-con">订单类型</span>
					</span>
					<span class="th" data-width="8">
						<span class="td-con">商家名称</span>
					</span>
					<span class="th" data-width="7">
						<span class="td-con">订单状态</span>
					</span>
					<span class="th" data-width="6">
						<span class="td-con">操作</span>
					</span>
				</div>
				<?php foreach ($orders as $key => $order) : ?>
					<div class="order-list">
						<div class="main-order">
							<div class="tr">
								<span class="td">
									<span class="td-con"><?php echo $order['sn'] ?></span>
								</span>
								<span class="td">
									<span class="td-con"><?php echo $order['_buyer']['username'] ?></span>
								</span>
								<span class="td">
									<span class="td-con"><?php echo $order['address_name'] ?></span>
								</span>
								<span class="td">
									<span class="td-con"><?php echo $order['address_mobile'] ?></span>
								</span>
								<span class="td">
									<span class="td-con"><?php echo date('Y-m-d H:i' ,$order['system_time']) ?></span>
								</span>
								<span class="td">
									<span class="td-con">￥<?php echo $order['real_amount'] ?></span>
								</span>
								<span class="td">
									<span class="td-con"><?php echo $order['_pay_type'] ?></span>
								</span>
								<span class="td">
									<span class="td-con">
										<?php if($order['source'] == 2) : ?>
											<i class="ico_order_mobile"></i>
										<?php elseif ($order['source'] == 3) : ?>
											<i class="ico_order_wechat"></i>
										<?php else : ?>
											<i class="ico_order"></i>
										<?php endif; ?>
									</span>
								</span>
								<span class="td">
									<?php if($order['seller_ids'] == 0) : ?>
										<span class="td-con">自营</span>
									<?php endif; ?>
								</span>
								<span class="td">
									<span class="td-con"><?php echo ch_status($order['_status']['now']) ?></span>
								</span>
								<span class="td">
									<span class="td-con">
										<?php if ($order['_showsubs'] == true) : ?>
											<a class="order-handle" href="javascript:;">展开</a>
										<?php else: ?>
											<a href="<?php echo url('order/admin_order/detail',array('sub_sn' => $order['_subs']['0']['sub_sn'])); ?>">查看</a>
										<?php endif; ?>
									</span>
								</span>
							</div>
						</div>
						<!-- 子订单信息 -->
						<!-- <div class="sub-order">
							<div class="tr">
								<span class="td">
									<span class="td-con">20151113151044525448</span>
								</span>
								<span class="td">
									<span class="td-con">54325432</span>
								</span>
								<span class="td">
									<span class="td-con">2015-11-13 15:10:44</span>
								</span>
								<span class="td">
									<span class="td-con">￥259.00</span>
								</span>
								<span class="td">
									<span class="td-con">货到付款</span>
								</span>
								<span class="td">
									<span class="td-con"><i class="ico_order_mobile"></i></span>
								</span>
								<span class="td">
									<span class="td-con">&nbsp;</span>
								</span>
								<span class="td">
									<span class="td-con">COMPLETION</span>
								</span>
								<span class="td">
									<span class="td-con">
										<a href="">查看</a>
									</span>
								</span>
							</div>
							<div class="tr">
								<span class="td">
									<span class="td-con">20151113151044525448</span>
								</span>
								<span class="td">
									<span class="td-con">54325432</span>
								</span>
								<span class="td">
									<span class="td-con">2015-11-13 15:10:44</span>
								</span>
								<span class="td">
									<span class="td-con">￥259.00</span>
								</span>
								<span class="td">
									<span class="td-con">货到付款</span>
								</span>
								<span class="td">
									<span class="td-con"><i class="ico_order_mobile"></i></span>
								</span>
								<span class="td">
									<span class="td-con">&nbsp;</span>
								</span>
								<span class="td">
									<span class="td-con">COMPLETION</span>
								</span>
								<span class="td">
									<span class="td-con">
										<a href="">查看</a>
									</span>
								</span>
							</div>
						</div> -->
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

<script type="text/javascript" src="<?php echo __ROOT__ ?>statics/js/admin/order_action.js"></script>
<script>
	$(".form-group .box").addClass("margin-none");
	$(window).load(function(){
		$(".table").resizableColumns();
		$(".paging-table").fixedPaging();
	})
	/* 删除订单 */
	var _orders = <?php echo json_encode($order_arr); ?>;
	var order = {};
	$(".delete_order").bind("click",function() {
		if (confirm('确定删除该订单？该操作不可逆')) {
			var sn = $(this).attr("data-sn");
			if (_orders[sn] == undefined) {
				alert('订单信息有误');
				return false;
			}
			order = _orders[sn];
			order_action.init();
			order_action.order(4,'<?php echo url("order/admin_order/delete"); ?>');
		}
	});
	
	$(".main-order .order-handle").live('click',function(){
		var $obj = $(this).parents(".main-order").next(".sub-order");
		if($obj.hasClass("show")){
			$obj.removeClass("show");
			$(this).text("展开");
		}else{
			$obj.addClass("show");
			$(this).text("收起");
		}
		$(".table").fixedPaging();
	});
</script>