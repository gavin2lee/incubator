<?php include template('header','admin');?>
<form action="<?php echo url('add') ?>" method="POST" name="form-validate">
<div class="fixed-nav layout">
	<ul>
		<li class="first">添加会员等级<a id="addHome" title="添加到首页快捷菜单">[+]</a></li>
		<li class="spacer-gray"></li>
	</ul>
	<div class="hr-gray"></div>
</div>
<div class="content padding-big have-fixed-nav">
	<div class="form-box clearfix">
		<?php echo Form::input('text', 'name', '', '等级名称：', '设置会员等级名称',array('datatype' => '*')); ?>
		<?php echo Form::input('text', 'min_points', '', '最小经验值：', '设置会员等级所需要的最低经验值下限',array('datatype' => 'n')); ?>
		<?php echo Form::input('text', 'max_points', '', '最大经验值：', '设置会员等级所需要的最高经验值上限',array('datatype' => 'n')); ?>
		<?php echo Form::input('text', 'discount', '', '折扣率：', '折扣率单位为%，如输入90，表示该会员等级的用户可以以商品原价的90%购买',array('datatype' => 'n')); ?>
		<?php echo Form::input('textarea', 'description', '', '等级描述：','会员等级描述信息'); ?>
	</div>
	<div class="padding">
		<input type="submit" name="dosubmit" class="button bg-main" value="确定" />
		<input type="reset" name="back" class="button margin-left bg-gray" value="返回" />
	</div>
</div>
</form>
</body>
</html>

<script type="text/javascript">
$(function() {
	var validate = $("[name=form-validate]").Validform({
		ajaxPost:true,
		callback:function(ret) {
			message(ret.message);
			if(ret.status == 1) {
				setTimeout(function(){
					window.top.main_frame.location.href= ret.referer;
				}, 1000);
			} else {
				return false;
			}
		}
	});
})
</script>