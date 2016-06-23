/* 提交订单处理类 */
var hd_order = (function() {

	var params = {};

	/* 显示全部收货地址 */
	$(".show-add").live('click' ,function() {
		$("[data-model=districts] .adddiv:gt(6)").removeClass('hidden');
		$(this).remove();
	});

	/* 点击提交按钮 */
	$("#submit").live('click',function(){
		hd_order._dosubmit();
	});

	return {
		init : function() {
			// 默认选中支付方式
			var labels = $("[data-model='pays'] .item");
			if (labels.length == 0 ) {
				$("[data-model='pays'] .item-checked").html('<p class="margin-bottom text-mix">暂未开启任何支付方式</p>');
			} else {
				$(labels).first().addClass('selected');
			}
		},

		_params : function() {
			params = {
				/* 商品列表 */
				skuids:skuids,
				/* 收货地址 */
				address_id : $("[data-model=districts]").find(".choose").data('addresid'),
				/* 收货地址 */
				district_id : $("[data-model=districts]").find(".choose").data('district'),
				/* 支付方式 */
				pay_type : $("[data-model=pays]").find(".selected").data('id'),
				/* 物流组 */
				deliverys : {},
				/* 订单促销组 */
				order_prom:{},
				/* 商品促销组 */
				sku_prom:{},
				/* 留言组 */
				remarks:{},
				/* 发票 */
				invoices:{
					invoice: $("[data-model=invoices]").find(".selected").data('invoice'),
					title: $("[data-model=invoice_title]").attr('value'),
					content: $("[data-model=invoice_content]").find(".selected").data('value')
				}
			}
			$("[data-sellerid]").each(function(i , n){
				var _sellerid = $(this).data('sellerid');
				params['deliverys'][_sellerid] = $(this).find("[data-model='delivery']").find(".selected").attr("delivery-district-id");
				params['order_prom'][_sellerid] = $(this).find("[data-model='order_proms']").val();
				params['remarks'][_sellerid] = $(this).find("[data-model='remarks']").attr("value");
			})

			$("[data-model='sku_proms']").each(function(i ,n) {
				_skuid = $(this).parents("[data-skuid]").data('skuid');
				params['sku_prom'][_skuid] = $(this).find("input[name^=sku_prom]:checked").attr("value");
			})
			return params;
		},

		_get : function() {
			$.getJSON('?m=order&c=order&a=get', hd_order._params(), function(ret) {
				if(ret.status == 0) {
					$.tips('');
				} else {
					$("[data-model='counts']").text(ret.result.counts);
					$("[data-model='sku_total']").text(ret.result.sku_total);
					$("[data-model='deliverys_total']").text(ret.result.deliverys_total);
					$("[data-model='invoice_tax']").text(ret.result.invoice_tax);
					$("[data-model='promot_total']").text(ret.result.promot_total);
					$("[data-model='real_amount']").text(ret.result.real_amount);

					$("[data-sellerid]").each(function(i, n){
						var _sellerid = $(this).data('sellerid');
						var _give = '';
						var _promos = '';
						$(this).find("[data-model=delivery_price]").text(ret['result']['skus'][_sellerid]['delivery_price']);
						/* 订单赠品 */
						if($.isEmptyObject(ret['result']['skus'][_sellerid]['_give']) == false) {
							_give = '<span class="bg-orange padding-small-left padding-small-right text-white">活动</span><span class="margin-small-left text-orange">已满足 '+ ret['result']['skus'][_sellerid]['_give']['title'] +'活动，赠送：'+ ret['result']['skus'][_sellerid]['_give']['sku_name'] +'</span>';
						}
						$(this).find("[data-model='order_give']").html(_give);

						/* 更新订单促销列表 */
						if($.isEmptyObject(ret['result']['skus'][_sellerid]['_promos']) == false) {
							_promos += '<option value="0">不使用优惠</option>';
							$.each(ret['result']['skus'][_sellerid]['_promos'], function(i ,n){
								if(params['order_prom'][_sellerid] == n.id) {
									_promos += '<option value="'+ n.id +'" selected>'+ n.name +'</option>';
								} else {
									_promos += '<option value="'+ n.id +'">'+ n.name +'</option>';
								}
							})
						}
						if(_promos.length <1){
							_promos = '<option selected>没有可选的订单优惠</option>';
						}
						$(this).find("[data-model='order_proms']").html(_promos);
					});

					/* 更新商品赠品 */
					$("[data-sellerid]").find('[data-skuid]').each(function(i, n){
						var _sellerid = $(this).parents('[data-sellerid]').data('sellerid');
						var _skuid = $(this).data('skuid');
						var _html = '';
						if($.isEmptyObject(ret['result']['skus'][_sellerid]['sku_list'][_skuid]['_give']) == false) {
							_html = '<span class="bg-mix padding-small-left padding-small-right text-white">活动</span><span class="margin-left text-mix">已满足 '+ ret['result']['skus'][_sellerid]['sku_list'][_skuid]['_give']['title'] +'</span>';
						}
						$(this).find("[data-model='give']").html(_html);
					})
				}
			})
		},

		/* 获取物流 */
		_get_deliverys : function(district_id) {
			$.getJSON(delivery_url, {
				district_id:district_id
			}, function(ret) {
				$.each(ret ,function(k, deliverys) {
					var top_box = $(".bill-wrap[data-sellerid="+ k +"]");
					var delivery_box = $("dd[data-model='delivery']");
					var _html = '';
					var i = 0;
					if ($.isEmptyObject(deliverys)) {
						_html = '<p class="margin-bottom text-mix">您所选择的收货地址暂时无法配送</p>';
					} else {
						$.each(deliverys ,function(k, v) {
							_html += '<div class="item';
								if (i == 0) {
									_html += ' selected';
								}
							_html += '" delivery-district-id="'+ v.delivery_id +'"><i></i><a href="javascript:;">'+ v._delivery.name +'</a></div>';
							i++;
						})
					}
					delivery_box.html(_html);
				})
				hd_order._get();
			})
		},

		/* 订单提交 */
		_dosubmit:function() {
			if($("#submit").hasClass('gray-btn')) return false;
			$("#submit").addClass('gray-btn').text('创建订单中...');
			$.post('?m=order&c=order&a=create', hd_order._params(), function(ret) {
				if(ret.status == 1) {
					$("#submit").text('订单创建成功');
					setTimeout(window.location.href = ret.referer,500);
				} else {
					$.dialogTip({content: ret.message});
					$("#submit").removeClass('gray-btn').text('重新提交');
					return false;
				}
			}, 'json');
		}
	};
})();