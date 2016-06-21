/**
 * 		提交订单处理类
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

var hd_order = (function() {

	var params = {};

	/* 点击提交按钮 */
	$("[data-id='submit']").on('tap',function(){
		hd_order._dosubmit();
	});

	return {
		
		/* 初始化结算页数据 */
		init : function() {
			// 如果当前skuids生成的md5后的key不等于本地存储的，覆盖之前的购买数据
			key = localStorage.getItem('hdkey');
			var _hddatas = $.parseJSON(localStorage.getItem('hddatas'));
			if (hdkey != key || hddatas[hdkey].address.length != _hddatas[hdkey].address.length) {
				localStorage.setItem('hdkey' ,hdkey);
				localStorage.setItem('hddatas' ,JSON.stringify(hddatas));
			}
			datas = hd_order._datas();
			/* 收货地址 */
			if (_hddatas[hdkey]._addressid_) {
				var choose_addr = '';
				$.each(datas.address ,function(k, v) {
					if (v.id == _hddatas[hdkey]._addressid_) {
						choose_addr = v;
					}
				});
				if (choose_addr) {
					$("[data-show='address_name']").parent().attr("data-addressid" , _hddatas[hdkey]._addressid_);
					$("[data-show='address_name']").text('收货人：' + choose_addr.name);
					$("[data-show='address_mobile']").text(choose_addr.mobile);
					$("[data-show='address_detail']").text('收货地址：' + choose_addr._area + " " + choose_addr.address);
				}
			} else {
				// 初始化选中默认收货地址
				if (!$.isEmptyObject(datas.address)) {
					if (datas.address[0]) {
						_hddatas[hdkey]._addressid_ = datas.address[0].id;
						_hddatas[hdkey]._district_ = datas.address[0].district_id;
						localStorage.setItem('hddatas',JSON.stringify(_hddatas));
						// 重新载入init
						hd_order.init();
					}
				}
			}
			/* 支付 && 配送 */
			if (_hddatas[hdkey]._pay_type_ && _hddatas[hdkey]._deliverys_) {
				$("[data-show='title']").addClass("order-lh-40");
				var _html = '<em>'+ datas.pay_type[_hddatas[hdkey]._pay_type_] +'</em><br/><em>';
				$.each(_hddatas[hdkey]._deliverys_,function(sellerid, delivery_id) {
					_html += datas.carts.deliverys[sellerid][delivery_id]._delivery.name + ' ';
				});
				_html += '</em>';
				$("[data-show='pay_delivery']").html(_html);
			} else {
				$("[data-show='title']").removeClass("order-lh-40");
				$("[data-show='pay_delivery']").html('请选择');
			}
			/* 发票信息 */
			if (_hddatas[hdkey].invoice_enabled == 1) {
				$("[data-show='invoice_content']").html(_hddatas[hdkey]._invoice_content_);
			}
			/* 订单促销 */
			if (_hddatas[hdkey]._order_prom_) {
				$.each(_hddatas[hdkey]._order_prom_ ,function(sellerid, prom_id) {
					if (prom_id > 0) {
						$("[data-sellerid="+ sellerid +"]").find("[data-show='order_prom']").text(datas.carts.skus[sellerid]._promos[prom_id].name);
					}else{
						$("[data-sellerid=0]").find("[data-show='order_prom']").text("不使用优惠");
					}
				});
			}
			/* 商品促销 */
			if (_hddatas[hdkey]._goods_prom_) {
				$.each(_hddatas[hdkey]._goods_prom_ ,function(skuid, prom_id) {
					if (prom_id > -1) {
						sellerid = $("[data-skuid="+ skuid +"]").parents("ul").data("sellerid");
						$("[data-skuid="+ skuid +"]").find("[data-show='goods_prom']").text(datas.carts.skus[sellerid].sku_list[skuid]._promos[prom_id].title);
					}
				});
			}
			/* 商家留言 */
			if (_hddatas[hdkey]._remarks_) {
				$.each(_hddatas[hdkey]._remarks_ ,function(sellerid, v) {
					$("[data-sellerid="+ sellerid +"]").find("[data-id='remarks']").val(v);
				});
			}
			hd_order._get();
		},

		/* 页面相关数据 */
		_datas : function() {
			hdkey = localStorage.getItem('hdkey');
			var _hddatas = $.parseJSON(localStorage.getItem('hddatas'));
			if (_hddatas[hdkey].address) {
				_hddatas[hdkey].address = $.parseJSON(_hddatas[hdkey].address);
			}
			if (_hddatas[hdkey].pay_type) {
				_hddatas[hdkey].pay_type = $.parseJSON(_hddatas[hdkey].pay_type);
			}
			if (_hddatas[hdkey].carts) {
				_hddatas[hdkey].carts = $.parseJSON(_hddatas[hdkey].carts);
			}
			return _hddatas[hdkey];
		},

		/* 选择 收货地址 初始化 */
		_address : function() {
			// 获取最新的收货地址并保存到本地存储
			$.ajax({
				url: '?m=order&c=order&a=get_address',
				type: 'POST',
				dataType: 'json',
				data: {},
				async : false,
				success : function(ret) {
					hdkey = localStorage.getItem('hdkey');
					_hddatas = $.parseJSON(localStorage.getItem('hddatas'));
					_hddatas[hdkey].address = JSON.stringify(ret);
					localStorage.setItem('hddatas',JSON.stringify(_hddatas));
				}
			})
			datas = hd_order._datas();
			var _html = '';
			if (datas.address) {
				$.each(datas.address ,function(k, v) {
					_html += '<li class="address-list">';
			        _html += '	<div class="address-text" data-id="address" data-district="'+ v.district_id +'" data-addressid="'+ v.id +'">';
			        _html += '		<a class="mui-block">';
			        _html += '			<span class="name text-ellipsis">'+ v.name +'</span>';
			        _html += '			<span class="address-btn margin-small-right ';
			        	if (datas._addressid_ != v.id) {_html += 'hide';}
			        _html += '" data-id="now">当前选中</span>';
			        _html += '			<span class="mui-pull-right">'+ v.mobile +'</span>';
				    _html += '    		<p>';
						if (v.isdefault == 1){
							_html += '[默认]';
						}
				    _html += v._area +' '+ v.address +'</p>';
			        _html += '		</a></div>';
			        _html += '	<div class="edit">';
			        _html += '		<a href="?m=member&c=address&a=edit&id='+ v.id +'&referer='+ encodeURIComponent(window.location.search) +'"><img src="template/wap/statics/images/ico_21.png" /></a>';
			        _html += '	</div></li>';
				});
			}
			$("[data-id='address_box']").html(_html);
		},

		/* 选择 支付&配送 初始化 */
		_delivery : function() {
			datas = hd_order._datas();
			/* 支付方式 */
			var _html = '';
			var i = 1;
			if (datas.pay_type) {
				$.each(datas.pay_type ,function(k, v) {
					_html += '<a class="mui-btn hd-btn-';
						// 选中效果
						_html += (datas._pay_type_ && datas._pay_type_ == k || !datas._pay_type_ && i == 1) ? 'blue' : 'gray';
					_html += ' margin-small-right" data-id="'+ k +'">'+ v +'</a>';
					i++;
				})
			}
			$("[data-id='type_box']").html(_html);
			/* 配送方式 */
			var _html = '';
			$.each(datas.carts.skus,function(sellerid, v) {
				_html += '<div class="border-bottom padding-bottom" data-sellerid="'+ sellerid +'">';
				_html += '			<ul class="delivery-items mui-clearfix">';
					$.each(v.sku_list ,function(k, sku) {
						_html += '		<li class="item-list img-full">';
						_html += '			<a href="javascript:;"><img src="'+ sku._sku_.thumb +'" /></a>';
						_html += '		</li>';
					});
				_html += '			</ul>';
				_html += '			<div class="margin-top">';
					var i = 1;
					$.each(datas.carts.deliverys[sellerid] ,function(index, delivery) {
						_html += '		<a class="mui-btn hd-btn-';
							if ((datas._deliverys_ && datas._deliverys_[sellerid] == delivery.delivery_id) || (!datas._deliverys_ && i == 1)) {
								_html += 'blue';
							} else {
								_html += 'gray';
							}
						_html += ' 			margin-small-right" delivery-district-id="'+ delivery.delivery_id +'">'+ delivery._delivery.name +'</a>';
						i++;
					});
				_html += '			</div>';
				_html += '		</div>';
			});
			$("[data-id='delivery_box']").html(_html);
		},

		/* 填写发票 初始化 */
		_invoice : function() {
			datas = hd_order._datas();
			invoices = $.parseJSON(datas.invoices);
			var i = 1;
			if (datas._invoice_title_) {
				$("[data-id='invoice_title']").val(datas._invoice_title_);
			}
			var _html = '';
			$.each(invoices ,function(k, v) {
				_html += '<div class="hd-radio full margin-bottom">';
				_html += '	<label>'+ v +'</label>';
				_html += '	<input name="radio" type="radio" ';
					if ((datas._invoice_content_ == v) || (i == 1 && !datas._invoice_content_)) {
						_html += 'checked="checked"';
					}
				_html += ' data-isinvoice="1"/></div>';
				i++;
			});
			_html += '<div class="hd-radio full margin-bottom"><label>不开发票</label><input name="radio" type="radio" data-isinvoice="0" ';
				if (datas._invoice_isinvoice_ == 0) {
					_html += 'checked="checked"';
				}
			_html += '/></div>';
			$("[data-id='invoice_box']").html(_html);
		},

		/* 订单促销 */
		_order_prom : function(sellerid) {
			datas = hd_order._datas();
			var _html = '';
			var i = 1;
			$.each(datas.carts.skus[sellerid]._promos ,function(k, prom) {
				_html += '<div class="hd-radio full margin-bottom" data-order="'+ prom.id +'">';
				_html += '	<label>'+ prom.name +'</label>';
				_html += '	<input name="radio" type="radio" ';
					if ((datas._order_prom_ && datas._order_prom_[sellerid] == prom.id) || (i == 1 && !datas._order_prom_)) {
						_html += 'checked="checked"';
					}
				_html += '/>';
				_html += '</div>';
				i++;
			});
			_html += '<div class="hd-radio full margin-bottom"><label>不使用优惠</label><input name="radio" type="radio" /></div>';
			$("[data-id='order_box']").html(_html);
		},

		/* 商品促销 */
		_goods_prom : function(sellerid , skuid) {
			datas = hd_order._datas();
			var _html = '';
			var i = 1;
			$.each(datas.carts.skus[sellerid].sku_list[skuid]._promos ,function(k, v) {
				_html += '<div class="hd-radio full margin-bottom" data-goods="'+ k +'">';
				_html += '	<label>'+ v.title +'</label>';
				_html += '	<input name="radio" type="radio" ';
					if ((datas._goods_prom_ && datas._goods_prom_[skuid] == k) || (i == 1 && !datas._goods_prom_)) {
						_html += 'checked="checked"';
					}
				_html += '/>';
				_html += '</div>';
				i++;
			});
			_html += '<div class="hd-radio full margin-bottom"><label>不参与促销</label><input name="radio" type="radio" /></div>';
			$("[data-id='goods_box']").html(_html);
		},

		_params : function() {
			hdkey = localStorage.getItem('hdkey');
			_hddatas = JSON.parse(localStorage.getItem('hddatas'));

			params = {
				/* 商品列表 */
				skuids:skuids,
				/* 收货地址 */
				address_id : _hddatas[hdkey]._addressid_ ,
				/* 收货地址 */
				district_id : _hddatas[hdkey]._district_ ,
				/* 支付方式 */
				pay_type : _hddatas[hdkey]._pay_type_ ,
				/* 物流组 */
				deliverys : _hddatas[hdkey]._deliverys_ ,
				/* 订单促销组 */
				order_prom: _hddatas[hdkey]._order_prom_ ,
				/* 商品促销组 */
				sku_prom: _hddatas[hdkey]._goods_prom_ ,
				/* 留言组 */
				remarks: _hddatas[hdkey]._remarks_ ,
				/* 发票 */
				invoices:{
					invoice: _hddatas[hdkey]._invoice_isinvoice_ ,
					title: _hddatas[hdkey]._invoice_title_ ,
					content: _hddatas[hdkey]._invoice_content_
				}
			}
			return params;
		},

		_get : function() {
			$.getJSON('?m=order&c=order&a=get', hd_order._params(), function(ret) {
				if(ret.status == 0) {
					$.tips({icon: 'error',content: ret.message});
				} else {
					$("[data-show='real_amount']").text(ret.result.real_amount);
					$("[data-show='sku_numbers']").text(ret.result.sku_numbers);
					$.each(ret.result.skus ,function(sellerid, v) {
						$("[data-sellerid="+ sellerid +"]").find("[data-show='delivery_price']").text(v.delivery_price);
					});
				}
			})
		},

		/* 获取收货地址的物流 */
		_get_deliverys : function(delivery_url ,district_id) {
			$.getJSON(delivery_url, {
				district_id:district_id
			}, function(ret) {
				var deliverys = {};
				$.each(ret ,function(sellerid, v) {
					deliverys[sellerid] = v;
				})
				hdkey = localStorage.getItem('hdkey');
				var _hddatas = $.parseJSON(localStorage.getItem('hddatas'));
				var _carts = $.parseJSON(_hddatas[hdkey].carts);
				_carts.deliverys = deliverys;
				_hddatas[hdkey].carts = JSON.stringify(_carts);
				localStorage.setItem('hddatas' ,JSON.stringify(_hddatas));
			})
		},

		/* 订单提交 */
		_dosubmit:function() {
			$("[data-id='submit']").text('提交中...');
			$.post('?m=order&c=order&a=create', hd_order._params(), function(ret) {
				if(ret.status == 1) {
					$("[data-id='submit']").text('提交成功');
					setTimeout(window.location.href = ret.referer,500);
					return false;
				} else {
					$.tips({icon: 'error',content: ret.message});
					$("[data-id='submit']").text('重新提交');
					return false;
				}
			}, 'json');
		}
	};
})();