<?php include template('header','admin');?>
		<?php if(!is_null($lists)){?>
		<div class="rece-address-manage clearfix">
			<?php foreach ($lists['lists'] AS $r): ?>
			<div class="address-box">
				<div class="w20 text-ellipsis">收货人：<?php echo $r['name'] ?></div>
				<div class="w15 text-ellipsis">手机：<?php echo $r['mobile'] ?></div>
				<div class="w15 padding-big-left text-ellipsis">邮编：<?php echo $r['zipcode']?></div>
				<div class="w50 padding-left text-ellipsis" title="<?php echo implode(" ", $r['full_district']) ?><?php echo $r['address'] ?>">地址：<?php echo implode(" ", $r['full_district']) ?><?php echo $r['address'] ?></div>
			</div>
			<?php endforeach ?>
		</div>
		<?php }else{?>
		<div class="padding-large layout bg-white text-center"><div class="padding-big"><span>该用户没有添加收货地址！</span></div></div>
		<?php }?>
		<div class="padding text-right ui-dialog-footer">
			<input type="button" class="button margin-left bg-gray" id="closebtn" value="关闭" />
		</div>
		<script>
			$(function(){
				try {
					var dialog = top.dialog.get(window);
				} catch (e) {
					return;
				}
				dialog.title('查看收货地址');
				$('#closebtn').on('click', function () {
					dialog.remove();
					return false;
				});
			})
		</script>
	</body>
</html>
