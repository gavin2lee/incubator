<?php include template('header','admin');?>
<script type="text/javascript" src="<?php echo __ROOT__ ?>statics/js/admin/order_action.js"></script>

	<div class="fixed-nav layout">
		<ul>
			<li class="first">发货单管理<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
			<li class="spacer-gray"></li>
			<li><a <?php if (!isset($_GET['status'])) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/parcel') ?>">全部</a></li>
			<li><a <?php if (isset($_GET['status']) && $_GET['status'] == 0) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/parcel',array('status'=>'0')) ?>">待配货</a></li>
			<li><a <?php if (isset($_GET['status']) && $_GET['status'] == 1) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/parcel',array('status'=>'1')) ?>">配送中</a></li>
			<li><a <?php if (isset($_GET['status']) && $_GET['status'] == -1) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/parcel',array('status'=>'-1')) ?>">配送失败</a></li>
			<li><a <?php if ($_GET['status'] == 2) {echo 'class="current"';} ?> href="<?php echo url('order/admin_order/parcel',array('status'=>'2')) ?>">配送完成</a></li>
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
		<div class="hr-gray"></div>
		<div class="clearfix">
			<form>
				<input type="hidden" name="m" value="order" />
				<input type="hidden" name="c" value="admin_order" />
				<input type="hidden" name="a" value="parcel" />
				<div class="form-group form-layout-rank border-none" style="width: 300px;">
					<span class="label" style="width: auto;">搜索</span>
					<div class="box ">
						<div class="field margin-none">
							<input class="input" type="text" name="keyword" value="<?php echo $_GET['keyword']; ?>" placeholder="输入订单号/会员账号/手机号" tabindex="0">
						</div>
					</div>
				</div>
				<input class="button bg-sub margin-top fl" type="submit" style="height: 26px; line-height: 14px;" value="查询">
			</form>
		</div>
		<div class="table-wrap">
			<div class="table resize-table paging-table border clearfix">
				<div class="tr">
					<span class="th" data-width="15">
						<span class="td-con">订单号</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">会员账号</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">收货人姓名</span>
					</span>
					<span class="th" data-width="30">
						<span class="td-con">联系地址</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">联系电话</span>
					</span>
					<span class="th" data-width="10">
						<span class="td-con">配送状态</span>
					</span>
					<span class="th" data-width="15">
						<span class="td-con">操作</span>
					</span>
				</div>

				<?php foreach ($parcels as $parcel) : ?>
					<div class="tr">
						<span class="td">
							<span class="td-con"><?php echo $parcel['order_sn']; ?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo $parcel['member_name']; ?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo $parcel['address_name']; ?></span>
						</span>
						<span class="td">
							<span class="td-con text-left"><?php echo $parcel['address_detail'];?></span>
						</span>
						<span class="td">
							<span class="td-con"><?php echo $parcel['address_mobile']; ?></span>
						</span>
						<span class="td">
							<span class="td-con">
							<?php
								switch($parcel['status']){
									case 1:
										echo '配送中';
										break;
									case 2:
										echo '配送完成';
										break;
									case -1:
										echo '配送失败';
										break;
									default :
										echo '待配送';
								}
							?>
							
							</span>
						</span>
						<span class="td">
							<span class="td-con">
								<a href="<?php echo url('order/admin_order/prints',array('id'=>$parcel['id']))?>">打印</a>&emsp;
								<?php if($parcel['status'] == 0 || $parcel['status'] == 1){?>
									<a href="javascript:;" onclick="order_action.complete_parcel('<?php echo url("order/admin_order/complete_parcel",array('id'=>$parcel['id'])); ?>');">确认配送</a>&emsp;
								<?php }else{?>
									确认配送&emsp;
								<?php }?>
								<a href="<?php echo url('order/admin_order/delete_parcel',array('id'=>$parcel['id']))?>" data-confirm="是否确认删除？">删除</a>
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