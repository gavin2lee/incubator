<p class="notice">您正在编辑 <em id="content-label" class="text-main">loading</em> 通知模板</p>
	<?php foreach($hooks as $tk=>$tv):?>
	<div id='edit_<?php echo $tk?>' style="display: none;" class="form-layout-rank">
		<?php echo Form::input('text', "{$tk}[template_id]", "{$template[template][$tk]['template_id']}", '微信通知模板ID：', '请从微信公众平台获取');?>	
	</div>
	<?php endforeach;?>