<?php include template('header','admin');?>
	<div class="fixed-nav layout">
		<ul>
			<li class="first">物流配送设置<a class="hidden" id="addHome" title="添加到首页快捷菜单">[+]</a></li>
			<li class="spacer-gray"></li>
		</ul>
		<div class="hr-gray"></div>
	</div>

	<form action="<?php url('order/admin_delivery/update'); ?>" method="post" enctype="multipart/form-data">
	<div class="content padding-big have-fixed-nav">
		<div class="form-box clearfix">
			<?php
			$def_enabled = (isset($delivery['enabled'])) ? $delivery['enabled'] : 1;
			echo Form::input('enabled', 'enabled', $def_enabled, '是否开启该物流：', '设置是否开启当前配送方式，开启即可使用', array('itemrows' => 2));
			?>

			<?php echo Form::input('text', 'name', $delivery['name'], '物流名称：', '设置配送方式名称，此处设置将在会员结算时显示，请根据实际情况填写，如顺丰快递'); ?>

			<?php echo Form::input('text', 'identif', $delivery['identif'], '物流标识：', '请按照《标准快递公司及参数说明》设置'); ?>

			<?php echo Form::input('file', 'logo', $delivery['logo'], '物流LOGO：', '设置配送方式LOGO'); ?>
		</div>
		<div class="insured-box form-group margin-lr">
			<label class="insured-label fl layout clearfix">
				<input type="checkbox" name="insure_enabled" data-reset="false" value="1" />&nbsp;&nbsp;是否开启物流保价：
			</label>
			<!--选中状态下显示下方文本框-->
			<?php
			$def_insure = (isset($delivery['insure'])) ? $delivery['insure'] : '0.00';
			echo Form::input('text', 'insure', $def_insure, '', '您可以为当前配送方式设置物流保价费率，单位为%');
			?>
		</div>
		<div class="form-box clearfix">
			<?php
			$def_sort = (isset($delivery['sort'])) ? $delivery['sort'] : 100;
			echo Form::input('text', 'sort', $def_sort, '排序：', '请填写自然数，列表将会根据排序进行由小到大排列显示');
			?>
		</div>
		<div class="padding">
			<div class="table-wrap">
				<div class="table check-table border paging-table clearfix">
					<div class="tr padding-none border-none">
						<div class="th layout text-left">
							<span class="padding-left text-normal text-sub">地区模板</span>
						</div>
					</div>
					<div class="tr border-none">
						<div class="th bg-none bg-white check-option"><b>删除</b></div>
						<div class="th bg-none bg-white text-left padding-lr w25"><b>配送费用</b></div>
						<div class="th bg-none bg-white w60"><b>配送地区</b></div>
						<div class="th bg-none bg-white w15"><b>操作</b></div>
					</div>
					<?php
						$deliverys =  array();
						foreach ($delivery['_districts'] as $k => $v) :
					?>
						<div class="tr" data-id="<?php echo $v['id']?>">
							<div class="td check-option">
								<input type="checkbox" name="delivery_district[delete][]" value="<?php echo $v['id']; ?>"/>
							</div>
							<div class="td padding-lr w25 district">
								<input class="input fl w70" name="delivery_district[edit][<?php echo $v['id']; ?>][price]" type="text" value="<?php echo $v['price']; ?>"/>
								<a class="fl margin-left dialog_edit" href="javascript:;">编辑地区</a>
								<input type="hidden" name="delivery_district[edit][<?php echo $v['id']; ?>][district_id]" value="<?php echo $v['district_id']; ?>" data-type="id"/>
							</div>
							<div class="td w60 text-left padding-lr text-ellipsis" title="<?php echo implode(",", $v['district_names']); ?>" data-type="name"><?php echo implode(",", $v['district_names']); ?></div>
							<div class="td w15">
								<a href="javascript:;" class="delete_tr" data-value="<?php echo $v['id']; ?>" data-type="edit" data-ident="<?php echo $ident; ?>">删除</a>
							</div>
						</div>
					<?php endforeach; ?>
					<div class="spec-add-button">
						<a href="javascript:;"><em class="ico_add margin-right"></em>添加地区模版</a>
					</div>
				</div>
			</div>
		</div>
		<div class="padding">
			<input type="hidden" name="id" value="<?php echo $delivery['id']; ?>" />
			<input type="submit" class="button bg-main" value="确定" name="dosubmit"/>
			<input type="button" class="button margin-left bg-gray" value="返回" />
		</div>
	</div>
	</form>

<script type="text/javascript" src="<?php echo __ROOT__;?>statics/js/template.js"></script>
<script id="district_item" type="text/html">
<div class="tr" data-id="<%=id%>">
	<div class="td check-option"></div>
	<div class="td padding-lr w25 district">
		<input class="input fl w70" name="delivery_district[add][<%=id%>][price]" type="text" value="0.00"/>
		<a class="fl margin-left dialog_edit" href="javascript:;">编辑地区</a>
		<input type="hidden" name="delivery_district[add][<%=id%>][district_id]" value="" data-type='id'/>
	</div>
	<div class="td w60 text-left padding-lr text-ellipsis" data-type='name'></div>
	<div class="td w15">
		<a href="javascript:;" class="delete_tr">删除</a>
	</div>
</div>
</script>

<script type="text/javascript">
/* 添加地区模版 */
$(".spec-add-button a").click(function() {
	var content = template('district_item', {id:'news_' + parseInt(Math.random() * 1000000)});
	$('.spec-add-button').before(content);
})

/* 删除地区模版 */
$(".delete_tr").live('click', function(){
	if(confirm('确定删除？'))
	$(this).parents('.tr').fadeOut('slow', function(){
		$(this).remove();
	});
})

//弹出编辑框
$(".dialog_edit").live('click', function() {
	id = $(this).parents('.tr').data('id');
	/* 重新计算需传递的数据 */
	var deliverys = {};
	$("div.tr[data-id]").each(function(i ,n){
		var _id = $(this).data('id'),
			_val = $(this).find("input[data-type='id']").val();

		if(_val.length > 0) {
			_val = 	_val.split(",");
		} else {
			_val = new Array();
		}
		deliverys[_id] = _val;
	})
	top.dialog({
		padding: '0px ',
		width: 720,
		title: '物流编辑地区',
		url: 'index.php?m=order&c=admin_delivery&a=ajax_district_select&id='+ id +'&formhash=<?php echo FORMHASH;?>',
		deliverys:deliverys,
		onclose:function() {
			var dreturn = this.returnValue;
			if($.isEmptyObject(dreturn) == false) {
				deliverys[id] = dreturn.ids;
				$("div[data-id='"+ id +"']").find("[data-type=id]").attr("value", dreturn.ids.join(","));
				$("div[data-id='"+ id +"']").find("[data-type=name]").attr("title", dreturn.txt.join(",")).text(dreturn.txt.join(","));
			}
		}
	}).showModal();
})

$(function() {
	/* 物流保价 */
	var _insure = parseFloat($(".insured-box input[name=insure]").val());
	if (_insure > 0) {
		$(".insured-box .insured-label input").prop('checked', true);
		$(".insured-box .form-group").show();
	} else {
		$(".insured-box .form-group").hide();
	}
	$(".insured-box .insured-label input").click(function() {
		if ($(this).is(":checked")) {
			$(".insured-box .form-group").show();
		} else {
			$(".insured-box .form-group").hide();
		}
	});
});
</script>