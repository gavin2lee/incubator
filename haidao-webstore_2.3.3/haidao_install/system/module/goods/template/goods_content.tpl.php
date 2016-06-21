<?php include template('header','admin');?>
		<script type="text/javascript" charset="utf-8" src="./statics/js/editor/umeditor.config.js"></script>
   		<script type="text/javascript" charset="utf-8" src="./statics/js/editor/umeditor.min.js"> </script>
   		<script type="text/javascript" charset="utf-8" src="./statics/js/editor/umeditor.js"> </script>
   		<script type="text/javascript" src="./statics/js/goods/goods_add.js" ></script>
   		<div class="fixed-nav layout">
			<ul>
				<li class="first">商品设置</li>
				<li class="spacer-gray"></li>
				<li>1.填写基本信息</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>2.设置商品规格</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>3.上传商品图册</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li>4.编辑商品类型</li>
				<li>&nbsp;&nbsp;&nbsp;→&nbsp;&nbsp;&nbsp;</li>
				<li class="goods-step">5.完善商品详情</li>
			</ul>
			<div class="hr-gray"></div>
		</div>
		<div class="content padding-big have-fixed-nav">
			<div class="margin-top">
				<form method="post" action="<?php echo url('goods_add',array('step' => 4,'id' => $_GET['id']))?>" name="goodsForm">
					<?php echo Form::editor('content', $info['content'], '', '', array('mid' => $this->admin['id'], 'path' => 'goods','module'=>'goods','allow_exts'=>array('bmp','jpg','jpeg','gif','png'))); ?>
					<div class="margin-top">
						<input type="submit" class='button bg-main' value='提交'/>
						<a class="button margin-left bg-gray" data-back="false" id="back">返回</a>
					</div>
				</form>
			</div>
		</div>
		<script>
		 	var url = "<?php echo url('save_goods_desc')?>";
			var back = "<?php echo url('goods_add',array('id'=>$_GET['id'],'step'=>3))?>";
			save_back(url,back);
			setInterval("auto_save(url)",30000);
		</script>
	</body>
</html>
