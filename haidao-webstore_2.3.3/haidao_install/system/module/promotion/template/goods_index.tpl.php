<?php include template('header','admin');?>
<div class="fixed-nav layout">
	<ul>
		<li class="first">订单促销<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
		<li class="spacer-gray"></li>
		<li><a class="current" href="javascript:;"></a></li>
	</ul>
	<div class="hr-gray">
	</div>
</div>
<div class="content padding-big have-fixed-nav">
	<div class="tips margin-tb">
		<div class="tips-info border">
			<h6>温馨提示</h6>
			<a id="show-tip" data-open="true" href="javascript:;">关闭操作提示</a>
		</div>
		<div class="tips-txt padding-small-top layout">
			<p>- 针对自营商品的单品SKU促销</p>
			<p>- 可选择促销的单品金额是否可再参与订单促销</p>
			<p>- 限时促销的单品默认不参与商品促销活动</p>
		</div>
	</div>
	<div class="hr-gray">
	</div>
	<div class="table-work border margin-tb">
		<div class="border border-white tw-wrap">
			<a href="<?php echo url('add') ?>"><i class="ico_add"></i>添加</a>
			<div class="spacer-gray">
			</div>
			<a href="<?php echo url('delete') ?>" data-ajax='id' data-message="是否删除所选商品促销活动？"><i class="ico_delete"></i>删除</a>
			<div class="spacer-gray">
			</div>
		</div>
	</div>
	<div class="table resize-table paging-table check-table border clearfix">
		<div class="tr">
			<span class="th check-option" data-resize="false">
				<span>
					<input id="check-all" type="checkbox" />
				</span>
			</span>
			<span class="th" data-width="40">
				<span class="td-con">促销名称</span>
			</span>
			<span class="th" data-width="40">
				<span class="td-con">促销时间</span>
			</span>
			<span class="th" data-width="10">
				<span class="td-con">状态</span>
			</span>
			<span class="th" data-width="10">
				<span class="td-con">操作</span>
			</span>
		</div>
		<?php $types = array('满额立减', '满额免邮', '满额赠礼');?>
		<?php foreach ($result['lists'] as $r): ?>
		<div class="tr" data-id="<?php echo $r['id'] ?>">
			<div class="td check-option">
				<input type="checkbox" name="id[]" value="<?php echo $r['id'] ?>" />
			</div>
			<span class="td">
				<div class="double-click">
					<a class="double-click-button" title="双击可编辑" href="javascript:;">
					</a>
					<input class="input double-click-edit text-ellipsis" type="text" value="<?php echo $r['name'] ?>">
				</div>
			</span>
			<span class="td">
				<span class="td-con"><?php echo ($r['start_time']) ? date('Y-m-d H:i', $r['start_time']) : '无限制' ?> ~ <?php echo ($r['end_time']) ? date('Y-m-d H:i', $r['end_time']) : '无限制' ?></span>
			</span>
			<span class="td">
				<span class="td-con"><?php if($r['status'] == 1){?>未开始<?php }elseif($r['status'] == 2){?>已结束<?php }else{?>进行中<?php }?></span>
			</span>
			<span class="td">
				<span class="td-con">
					<a href="<?php echo url('edit', array('id' => $r['id'])) ?>">编辑</a>&nbsp;&nbsp;&nbsp;
					<a href="<?php echo url('delete', array('id[]' => $r['id'])) ?>" data-confirm="是否删除该商品促销活动？">删除</a></span>
			</span>
		</div>
		<?php endforeach ?>
		<div class="paging padding-tb body-bg clearfix">
			<?php echo $result['pages'] ?>
			<div class="clear">
			</div>
		</div>
	</div>
</div>
<script>
$(".table").resizableColumns();
$(".table").fixedPaging();
$(".double-click-edit").live('change',function(){
	var data = {id:$(this).parents(".tr").data("id"),name:$(this).val()};
	$.post("<?php echo url('ajax_name') ?>",data,function(ret){
		message(ret.message);
	},'json');
})


function message(t){
	var d = dialog({
	    content: '<div class="padding-large-left padding-large-right padding-big-top padding-big-bottom bg-white text-small">'+t+'</div>'
	});
	d.show();
	setTimeout(function () {
	    d.close().remove();
	}, 1000);
}
</script>
</body>
</html>
