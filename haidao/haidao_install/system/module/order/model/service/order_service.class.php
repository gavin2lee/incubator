<?php
/**
 * 		订单服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class order_service extends service {

	protected $where = array();

	public function __construct() {
		/* 实例化数据层 */
		$this->table      = $this->load->table('order/order');
		$this->table_cart = $this->load->table('order/cart');
		$this->table_sub  = $this->load->table('order/order_sub');
		$this->table_delivery = $this->load->table('order/order_delivery');
		/* 实例化服务层 */
		$this->service_cart   = $this->load->service('order/cart');
		$this->service_sku    = $this->load->service('order/order_sku');
		$this->service_track  = $this->load->service('order/order_track');
		$this->service_order_log = $this->load->service('order/order_log');
		$this->service_goods_sku = $this->load->service('goods/goods_sku');
		$this->service_payment = $this->load->service('pay/payment');
		$this->service_member = $this->load->service('member/member');
		$this->member = $this->service_member->init();
	}

	/**
	 * 创建订单
	 * @param  integer $buyer_id    会员id
	 * @param  integer $skuids      商品id及数量 (string , 必传参数, 多个sku用;分割。数量number可省略，代表购物车记录的件数。整个参数为空则获取购物车所有列表) eg ：sku_id1[,number1][;sku_id2[,number2]]
	 * @param  integer $district_id 物流配置地区id
	 * @param  integer $pay_type    支付方式 (1：在线支付 2：货到付款)
	 * @param  array   $deliverys   物流详细 eg : array('seller_id1' => 'delivery_id1' [,'seller_id2' => 'delivery_id2'])
	 * @param  array   $order_prom  订单促销 eg : array('seller_id1' => 'order_prom_id1'[,'seller_id2' => 'order_prom_id2'])
	 * @param  array   $sku_prom    商品促销 eg : array('sku_id1' => 'sku_prom1'[,'sku_id2' => 'sku_prom2'])
	 * @param  array   $remarks     订单留言 eg : array('seller_id1' => '内容1'[,'seller_id2' => '内容2'])
	 * @param  array   $invoices    发票信息 eg : array('invoice' => '是否开发票 - 布尔值','title' => '发票抬头' , 'content' => '发票内容')
	 * @param  boolean $submit      是否创建 (为false时 获取订单结算信息，为true时 创建订单)
	 * @return mixed
	 */
	public function create($buyer_id = 0, $skuids = 0, $district_id, $pay_type = 1, $deliverys = array(), $order_prom = array(), $sku_prom = array(), $remarks = array(), $invoices = array(), $submit = false) {
		/* 定义默认值 */
		$deliverys_total = 0;	// 总运费
		$invoice_tax = 0;		// 总发票费
		$promot_total = 0;		// 总优惠金额
		$setting = $this->load->service("admin/setting")->get_setting();
		/* 第一步：获取购物车数据 */
		$carts = $this->service_cart->get_cart_lists($buyer_id, $skuids, TRUE);
		if(empty($carts["skus"])) {
			$this->error = lang('order/shopping_cart_empty');
			return false;
		}
		/* 第二步：获取物流 */
		$_deliverys = $this->load->service('order/delivery')->get_deliverys($district_id ,$skuids);
		$carts['deliverys'] = $_deliverys;
		/* 第三步：处理商品 */
		foreach ($carts['skus'] as $seller_id => $value) {
			/* 商家默认运费 */
			if(!isset($deliverys[$seller_id])) {
				$_delivery_id = current($_deliverys[$seller_id]);
				$deliverys[$seller_id] = $_delivery_id['delivery_id'];
			}
			$value['delivery_price'] = sprintf('%.2f' ,$_deliverys[$seller_id][$deliverys[$seller_id]]['price']);
			$value['delivery_name'] = $_deliverys[$seller_id][$deliverys[$seller_id]]['_delivery']['name'];
			/* 商家订单赠品 */
			$value['_give'] = array();
			$value['_promos'] = array();
			foreach ($value['sku_list'] as $sku_id => $sku) {
				/* 获取商品促销信息 */
				$sku['_promos'] = array();
				$sku['_give'] = array();
				$pro_map = array();
				$pro_map['_string'] = "FIND_IN_SET($sku[sku_id], `sku_ids`)";
				$pro_map['start_time'] = array(array("EQ", 0), array("ELT", time()), "OR");
				$pro_map['end_time'] = array(array("EQ", 0), array("EGT", time()), "OR");
				$pro_map['_logic'] = "AND";
				$_sku_promos = $this->load->table('promotion/promotion_goods')->where($pro_map)->find();
				if ($_sku_promos['rules']) {
                    foreach ($_sku_promos['rules'] as $key => $row) {
                        $discount[$key]  = $row['discount'];
                    }
                    array_multisort($discount, SORT_DESC, $_sku_promos['rules']);
                    foreach ($_sku_promos['rules'] as $k => $rule) {
                        if($rule['type'] == 'number_give' || $rule['type'] == 'amount_give'){
                            $rule['selected'] = 1;
                        }elseif ($k == 0) $rule['selected'] = 1;
						switch ($rule['type']) {
							case 'amount_discount':	// 满额立减
							case 'amount_give':		// 满额赠礼
								if($sku['prices'] >= $rule['condition']) {
									$sku['_promos'][$k] = $rule;
								}
								break;
							default :
								if ($sku['number'] >= $rule['condition']) {
									$sku['_promos'][$k] = $rule;
								}
								break;
						}
					}
					/* 叠加订单促销 */

					/* 是否同时享受订单促销*/
					if(!$_sku_promos['share_order']){
                        $sku_prom_price = $sku['prices'];
						if(isset($sku_prom[$sku_id]) && $_sku_promos['rules'][$sku_prom[$sku_id]]) {
							$sku_prom_price += $sku['prices'];
							if($_sku_promos['rules'][$sku_prom[$sku_id]]['type'] == 'amount_discount' || $_sku_promos['rules'][$sku_prom[$sku_id]]['type'] == 'number_discount') {
								$promot_total += $_sku_promos['rules'][$sku_prom[$sku_id]]['discount'];
								$sku['prices'] -= $_sku_promos['rules'][$sku_prom[$sku_id]]['discount'];
								$value['sku_price'] -= $_sku_promos['rules'][$sku_prom[$sku_id]]['discount'];	// 子订单skus总额
								$value['sub_prices'] -= $_sku_promos['rules'][$sku_prom[$sku_id]]['discount'];	// 子订单优惠后的总额
							} else {
								$sku['_give'] = $_sku_promos['rules'][$sku_prom[$sku_id]];
							}
						}
					}else{
						$sku_prom_price = 0;
						if($_sku_promos['rules'][$sku_prom[$sku_id]]['type'] == 'amount_discount' || $_sku_promos['rules'][$sku_prom[$sku_id]]['type'] == 'number_discount') {
							$promot_total += $_sku_promos['rules'][$sku_prom[$sku_id]]['discount'];
							$sku['prices'] -= $_sku_promos['rules'][$sku_prom[$sku_id]]['discount'];
						} else {
							$sku['_give'] = $_sku_promos['rules'][$sku_prom[$sku_id]];
						}
					}
				}
				$value['sku_list'][$sku_id] = $sku;
			}
			/* 计算订单促销 */
			/* 参与订单促销的价格 */
			$order_prom_price = $value['sub_prices'] - $sku_prom_price;
			/* 查找所有订单促销 */
			$_order_promos = $this->load->service('promotion/promotion_order')->fetch_all($order_prom_price);
			if(isset($order_prom[$seller_id]) && $_order_promos[$order_prom[$seller_id]]) {
				$_order_promos_info = $_order_promos[$order_prom[$seller_id]];
				if($_order_promos_info['type'] == 0) {
					$value['sub_prices'] = $value['sub_prices'] - $_order_promos_info['discount'];
					$promot_total += $_order_promos_info['discount'];
				} elseif($_order_promos_info['type'] == 1) {
					$value['delivery_price'] = 0;
				} else {
					$_give = $this->load->table('goods_sku')->where(array('sku_id' => $_order_promos_info['discount']))->field('sku_id, sku_name')->find();
					$value['_give'] = array('sku_id' => $_give['sku_id'], 'sku_name' => $_give['sku_name'], 'title' => $_order_promos[$order_prom[$seller_id]]['name']);
				}
			}
			$value['_promos'] = $_order_promos;
			$value['remarks'] = $remarks[$seller_id];
			$deliverys_total += $value['delivery_price'];
			$carts['skus'][$seller_id] = $value;
		}

		/* 商品总原价 */
		$carts['sku_total'] = sprintf("%.2f",$carts['all_prices']);
		/* 订单总运费 */
		$carts['deliverys_total'] = sprintf("%.2f", $deliverys_total);
		/* 订单发票 */
		$carts['invoice_tax'] = $invoice_tax;
		if($setting['invoice_enabled'] == 1 && $invoices['invoice'] == 1) {
			$carts['invoice_tax'] = (($carts['sku_total'] + $carts['deliverys_total'] - $promot_total) * $setting['invoice_tax']) / 100;
		}
		$carts['invoice_tax'] = sprintf("%.2f", $carts['invoice_tax']);
		/* 活动优惠总额 */
		$carts['promot_total'] = sprintf("%.2f", -$promot_total);
		/* 订单应付总额 */
		$carts['real_amount'] = sprintf("%.2f", max(0,$carts['sku_total'] + $carts['deliverys_total'] + $carts['invoice_tax'] - $promot_total));
		if($submit === true) { // 写入订单表
			/* 创建主订单 */
			// 读取收货人信息
			$member_address = $this->load->table("member/member_address")->where(array('id' => $_GET['address_id']))->find();
			if (!$member_address) {
				$this->error = lang('order/shipping_address_empty');
				return FALSE;
			}
			$invoice_enabled = $invoices['invoice'];
			$invoice_title = remove_xss($invoices['title']);
			$invoice_content = ($invoice_enabled == 0) ? '' : remove_xss($invoices['content']);
			if ($invoice_enabled == 1 && empty($invoice_title)) {
				$this->error = lang('order/invoice_head_empty');
				return FALSE;
			}
			if (!in_array($pay_type, array(1,2))) {
				$this->error = lang('order/pay_way_empty');
				return FALSE;
			}
			/* 组装收货地址 && 收货地址地区ids*/
			$districts = $this->load->service('admin/district')->fetch_parents($member_address['district_id']);
			krsort($districts);
			$address_name = $address_district_ids = '';
			foreach ($districts as $district) {
				$address_name .= $district['name'].' ';
				$address_district_ids .= $district['id'].',';
			}
			/* 组装主订单信息 */
			$order_sn = $this->_build_order_sn();
			$source = defined('MOBILE') ? (defined('IS_WECHAT') ? 3 : 2) : 1;
			$order_data = array(
				'sn'              => $order_sn,
				'buyer_id'        => $buyer_id,
				'seller_ids'      => 0,
				'source'          => $source,	// 订单来源(1：标准，2：移动端)
				'pay_type'        => $pay_type, 		// 支付类型(1:在线支付 , 2:货到付款)
				'sku_amount'      => $carts['sku_total'], // 商品总额
				'real_amount'     => $carts['real_amount'], 		// 应付总额
				'delivery_amount' => $carts['deliverys_total'] ,	// 总运费
				'promot_amount'   => $promot_total ,	// 所有优惠总额
				'invoice_tax'     => $carts['invoice_tax'], 	// 发票税额
				'invoice_title'   => $invoice_title, 	// 发票抬头
				'invoice_content' => $invoice_content,  // 发票内容
				'address_name'    => $member_address['name'],
				'address_mobile'  => $member_address['mobile'],
				'address_detail'  => $address_name.' '.$member_address['address'],
				'address_district_ids' => $address_district_ids,
			);
			$oid = $this->table->update($order_data);
			if (!$oid) {
				$this->error = $this->table->getError();
				return FALSE;
			}
			/* 生成子订单 */
			$result = $this->_create_sub($carts ,$order_sn ,$oid ,$pay_type);
			if ($result == FALSE) {
				// 回滚删除之前的订单信息
				$this->table->where(array('id' => $oid))->delete();
				return FALSE;
			}
			// 钩子：下单成功
			$member = array();
			$member['member'] = $this->load->table('member/member')->where(array('id' => $buyer_id))->find();
			$member['order_sn'] = $order_sn;
			runhook('create_order',$member);
			return $order_sn;
		} else {
			return $carts;

		}
	}

	/**
	 * 创建子订单
	 * @param  array  $cart_skus 购物车分组信息
	 * @param  string $order_sn  主订单号
	 * @param  int 	  $id 		 主订单id
	 * @param  int 	  $pay_type  支付方式
	 * @return [boolean]
	 */
	private function _create_sub($cart_skus ,$order_sn ,$id ,$pay_type) {
		if (count($cart_skus['skus']) == 0) return FALSE;
		/* 读取后台配置 (是否减库存) */
		$stock_change = $this->load->service('admin/setting')->get_setting('stock_change');
		$operator = get_operator();	// 获取操作者信息
		$data = array();
		foreach ($cart_skus['skus'] as $k => $val) {
			if (empty($val['delivery_name'])) {
				$this->error = lang('order/logistics_empty');
				return FALSE;
			}
			$sub_sn = $this->_build_order_sn(TRUE);
			$data['sub_sn']         = $sub_sn;
			$data['order_id']       = $id;
			$data['order_sn']       = $order_sn;
			$data['pay_type']       = $pay_type;
			$data['buyer_id']       = $this->member['id'];
			$data['seller_id']      = $k;
			$data['remark']         = (string) $val['remarks'];
			$data['delivery_name']  = $val['delivery_name'];
			$data['sku_price']      = $val['sku_price'];
			$data['delivery_price'] = $val['delivery_price'];
			$data['real_price']     = $val['sub_prices'] + $val['delivery_price'];
			if ($val['_promos'][$_GET['order_prom'][$k]]) {
				$order_prom = $val['_promos'][$_GET['order_prom'][$k]];
				$order_prom['title'] = $order_prom['name'].' , '.$val['_give']['sku_name'];
				$order_prom['sku_info'] = $val['_give'];
			}
			$data['promotion']      = unit::json_encode($order_prom);
			$data['system_time']    = time();
			$result = $this->table_sub->update($data);
			if (!$result) {
				$this->error = $this->table_sub->getError();
				return FALSE;
				break;
			}
			// 创建订单商品
			$skus = $result = $load_decs = array();
			foreach ($val['sku_list'] as $v) {
				$_data = array();
				$_data['order_sn']    = $order_sn;
				$_data['sub_sn']      = $sub_sn;
				$_data['buyer_id']    = $this->member['id'];
				$_data['seller_id']   = $k;
				$_data['sku_id']      = $v['sku_id'];
				$_data['sku_thumb']   = $v['_sku_']['thumb'];
				$_data['sku_barcode'] = $v['_sku_']['barcode'];
				$_data['sku_name']    = $v['_sku_']['name'];
				$_data['sku_spec']    = unit::json_encode($v['_sku_']['spec']);
				$_data['sku_price']   = $v['_sku_']['shop_price'];
				$_data['real_price']  = $v['prices'];
				$_data['buy_nums']    = $v['number'];
                $_data['sku_edition'] = $v['_sku_']['edition'];
				$_data['promotion']   = unit::json_encode($v['_promos'][$_GET['sku_prom'][$v['sku_id']]]);
				$skus[]               = $_data;
				/* 商品促销的赠品 */
				if($v['_give']) {
					$sku_info = $this->service_goods_sku->goods_detail($v['_give']['discount']);
					if($sku_info) {
						$_data                = array();
						$_data['order_sn']    = $order_sn;
						$_data['sub_sn']      = $sub_sn;
						$_data['buyer_id']    = $this->member['id'];
						$_data['seller_id']   = $k;
						$_data['sku_id']      = $sku_info['sku_id'];
						$_data['sku_thumb']   = $sku_info['thumb'];
						$_data['sku_barcode'] = $sku_info['barcode'];
						$_data['sku_name']    = $sku_info['name'];
                        $_data['sku_edition'] = $sku_info['edition'];
						$_data['sku_spec']    = unit::json_encode($v['_sku_']['spec']);
						$_data['sku_price']   = 0;
						$_data['real_price']  = 0;
						$_data['buy_nums']    = 1;
						$_data['is_give']     = 1;
						$skus[]               = $_data;
					}
				}
				// 待减除购物车记录数量 组装  array('sku_id1' => number1 [,'sku_id2' => number2])
				$load_decs[$v['sku_id']] = $v['number'];
			}
			/* 创建订单赠品 */
			if($val['_give']) {
				$sku_info = $this->service_goods_sku->goods_detail($val['_give']['sku_id']);
				if($sku_info) {
					$_data                = array();
					$_data['order_sn']    = $order_sn;
					$_data['sub_sn']      = $sub_sn;
					$_data['buyer_id']    = $this->member['id'];
					$_data['seller_id']   = $k;
					$_data['sku_id']      = $sku_info['sku_id'];
					$_data['sku_thumb']   = $sku_info['thumb'];
					$_data['sku_barcode'] = $sku_info['barcode'];
					$_data['sku_name']    = $sku_info['name'];
					$_data['sku_edition'] = $sku_info['edition'];
					$_data['sku_spec']    = unit::json_encode($v['_sku_']['spec']);
					$_data['sku_price']   = 0;
					$_data['real_price']  = 0;
					$_data['buy_nums']    = 1;
					$_data['is_give']     = 1;
					$skus[]               = $_data;
				}
			}
			$result = $this->service_sku->create_all($skus);
			if (!$result) {
				$this->error = $this->service_sku->error;
				return FALSE;
				break;
			}
			/* 减库存 */
			if ($stock_change != NULL && $stock_change == 0) {
				foreach ($val['sku_list'] as $k => $cart) {
					$this->load->service('goods/goods_sku')->set_dec_number($k,$cart['number']);
				}
			}
			/* 清除购物车已购买数据 */
			$this->service_cart->dec_nums($load_decs ,$this->member['id']);
			// 订单日志
			$data = array();
			$data['order_sn']      = $order_sn;
			$data['sub_sn']        = $sub_sn;
			$data['action']        = '创建订单';
			$data['operator_id']   = $operator['id'];
			$data['operator_name'] = $operator['username'];
			$data['operator_type'] = $operator['operator_type'];
			$data['msg']           = '提交购买商品并生成订单';
			$this->service_order_log->add($data);
			// 订单跟踪
			$track_msg = $pay_type == 1 ? '系统正在等待付款': '请等待系统确认';
			$this->service_track->add($order_sn ,$sub_sn , '您提交了订单，'.$track_msg);
		}
		return TRUE;
	}

	/**
	 * 根据日期生成唯一订单号
	 * @param boolean $refresh 	是否刷新再生成
	 * @return string
	 */
	private function _build_order_sn($refresh = FALSE) {
		if ($refresh == TRUE) {
			return date('Ymd').substr(implode(NULL, array_map('ord', str_split(substr(uniqid(), 7, 13), 1))), 0, 12);
		}
		return date('YmdHis').substr(implode(NULL, array_map('ord', str_split(substr(uniqid(), 7, 13), 1))), 0, 6);
	}

	/**
	 * 获取订单列表
	 * @param  array  $sqlmap 查询条件，具体参数见 build_sqlmap()方法
	 * @return [result]
	 */
	public function get_lists($sqlmap = array()) {
		$sqlmap = $this->build_sqlmap($sqlmap);
		$result = $this->table->where($sqlmap)->order('id DESC')->select();
		return $result;
	}

	/**
	 * 生成查询条件
	 * @param  $options['type'] (1:待付款|2:待确认|3:待发货|4:待收货|5:已完成|6:已取消|7:已回收|8:已删除)
	 * @param  $options['keyword'] 	关键词(订单号|收货人姓名|收货人手机)
	 * @return [$sqlmap]
	 */
	public function build_sqlmap($options) {
		extract($options);
		$sqlmap = array();
		if (isset($type) && $type > 0) {
			switch ($type) {
				// 待付款
				case 1:
					$sqlmap['pay_type']   = 1;
					$sqlmap['status']     = 1;
					$sqlmap['pay_status'] = 0;
					break;
				// 待确认
				case 2:
					$sqlmap['status'] = 1;
					$sqlmap['_string'] = '(pay_type=2 or pay_type=1 and pay_status=1) and confirm_status<>2';
					break;
				// 待发货
				case 3:
					$sqlmap['status'] = 1;
					$sqlmap['confirm_status'] = array('IN',array(1,2));
   					$sqlmap['delivery_status'] = array('IN',array(0,1));
					break;
				// 待收货
				case 4:
					$sqlmap['status'] = 1;
					// 获取所有待收货的主订单号
					$sub_sns = $this->load->table('order/order_delivery')->where(array('isreceive' => 0))->getField('sub_sn' ,TRUE);
					$map = array();
					$map['sub_sn'] = array('IN' ,$sub_sns);
					$order_sns = $this->load->table('order/order_sub')->where($map)->getField('order_sn' ,TRUE);
					$sqlmap['sn'] = array('IN' ,$order_sns);
					break;
				// 已完成
				case 5:
					$sqlmap['status'] = 1;
					$sqlmap['finish_status'] = 2;
					break;
				// 已取消
				case 6:
					$sqlmap['status'] = 2;
					break;
				// 已作废
				case 7:
					$sqlmap['status'] = (defined('IN_ADMIN')) ? array('GT', 2) : 3;
					break;
				// 前台已删除
				case 8:
					$sqlmap['status'] = 4;
			}
		}
		if (isset($keyword) && !empty($keyword)) {
			$buyer_ids = $this->load->table('member/member')->where(array('username' => array('LIKE','%'.$keyword.'%')))->getField('id',TRUE);
			if (!$buyer_ids) {
				$sqlmap['sn|address_name|address_mobile'] = array('LIKE','%'.$keyword.'%');
			} else {
				$sqlmap['buyer_id'] = array('IN',$buyer_ids);
			}
		}
		return $sqlmap;
	}

	/**
	 * 修改订单应付总额
	 * @param  string  $sub_sn 		子订单号
	 * @param  float   $real_price  修改后的价格
	 * @return [boolean]
	 */
	public function update_real_price($sub_sn = '',$real_price = 0) {
		$real_price = sprintf('%.2f' , max(0,(float) $real_price));
		$order = $this->table_sub->where(array('sub_sn' => $sub_sn))->find();
		if ($order['pay_type'] == 2 || $order['pay_status'] ==1) {
			$this->error = lang('order/dont_edit_order_amount');
			return FALSE;
		}
		$result = $this->table_sub->where(array('sub_sn' => $sub_sn))->setField('real_price' ,$real_price);
		if (!$result) {
			$this->error = $this->table_sub->getError();
			return FALSE;
		}
		// 重新计算主订单应付总额
		$new_prices = $this->table_sub->where(array('order_sn' => $order['order_sn']))->sum('real_price');
		$this->table->where(array('sn' => $order['order_sn']))->setField('real_amount' ,$new_prices);
		// 订单操作日志
		$operator = get_operator();	// 获取操作者信息
		$data = array();
		$data['order_sn']      = $order['order_sn'];
		$data['sub_sn']        = $sub_sn;
		$data['action']        = '修改订单应付总额';
		$data['operator_id']   = $operator['id'];
		$data['operator_name'] = $operator['username'];
		$data['operator_type'] = $operator['operator_type'];
		$data['msg']           = '原应付总额「'.$order['real_price'].'」修改为「'.$real_price.'」';
		$this->service_order_log->add($data);
		return $result;
	}

	/**
	 * 提交订单支付
	 * @param  string  $sn        主订单号
	 * @param  integer $isbalance 是否余额支付
	 * @param  string  $pay_code  支付方式code
	 * @param  string  $pay_bank  网银支付bank($pay_code = 'bank'时必填)
	 * @return [result]
	 */
	public function detail_payment($sn = '' ,$isbalance = 0,$pay_code = '' ,$pay_bank = '') {
		$sn = (string) $sn;
		$isbalance = (int) $isbalance;
		$order = $this->table->detail($sn)->output();
		if ($order['buyer_id'] != $this->member['id']) {
			$this->error = lang('_valid_access_');
			return FALSE;
		}
		if ($order['pay_status'] == 1) {
			$this->error = lang('pay/order_paid');
			return FALSE;
		}
		if ($pay_code == 'bank' && empty($pay_bank)) {
			$this->error = lang('order/pay_ebanks_error');
			return FALSE;
		}
		// 后台余额支付开关
		$balance_pay = $this->load->service('admin/setting')->get_setting('balance_pay');
		// 还需支付总额
		$total_fee = round($order['real_amount'] - $order['balance_amount'], 2);
		/* 含有余额支付的 */
		if ($balance_pay == 1 && $isbalance == 1 && $this->member['money'] > 0) {
			$balance_amount = $total_fee;	// 本次余额支付的金额
			if ($this->member['money'] < $total_fee) {
				$balance_amount = $this->member['money'];
				$total_fee = abs(round($total_fee - $this->member['money'],2));
			} else {
				$total_fee = 0;
			}
			// 扣除会员余额($balance_amount),并写入 冻结资金
			$result = $this->service_member->action_frozen($this->member['id'] ,$balance_amount , true ,'余额支付订单,主订单号:'.$sn);
			if (!$result) {
				$this->error = $this->service_member->error;
				return FALSE;
			}
			// 把余额支付金额写入订单余额支付总额里
			$_balance_amount = $order['balance_amount'] + $balance_amount;
			$result = $this->table->where(array('sn' => $sn))->setField('balance_amount' ,$_balance_amount);
			if (!$result) {
				$this->error = $this->table->getError();
				return FALSE;
			}
		}
		/* 需要再用网银支付的 */
		$result = array();
		if ($total_fee > 0) {
			$pay_info = array();
			$pay_info['trade_sn']  = $sn;
			$pay_info['total_fee'] = $total_fee;
			$pay_info['subject']   = '订单号：'.$sn;
			$pay_info['pay_bank']  = $pay_bank;
			/* 请求支付 */
			$gateway = $this->service_payment->gateway($pay_code,$pay_info);
			if($gateway === false) showmessage(lang('pay/pay_set_error'));
			$gateway['order_sn'] = $sn;
			$gateway['total_fee'] = $total_fee;
		} else {
			// 设置订单为支付状态
			$data = array();
			$order = $this->table->detail($sn)->output();
			if($order['balance_amount'] == $order['real_amount']  && $balance_pay==1){
				$data['pay_method'] = '余额支付';
			}elseif($order['balance_amount'] == 0){
				$data['pay_method'] = $pay_code;
			}else{
				$data['pay_method'] ='余额支付'. '+' . $pay_code;
			}
			$data['paid_amount'] = $order['real_amount'];
			$data['msg'] = '您的订单已支付成功';
			$this->load->service('order/order_sub')->set_order($sn ,'pay','',$data);
			$result['pay_success'] = 1;
		}
		/* 支付后的跳转地址 */
		$gateway['url_forward'] = url('order/order/pay_success',array('order_sn' => $sn));
		$result['gateway'] = $gateway;
		return $result;
	}
	public function order_import($params){
		$params['sku_amount'] = $params['shop_price'][0] * $params['shop_number'][0];
		$sub_data = array();
		$sub_data['order_id'] = $params['id'];
		$sub_data['order_sn'] = $params['sn'];
		$sub_data['sub_sn'] = substr($params['sn'],0,10).random(10,1);
		$sub_data['pay_type'] = $params['pay_type'];
		$sub_data['buyer_id'] = $params['buyer_id'];
		$sub_data['seller_id'] = $params['seller_id'];
		$sub_data['delivery_name'] = $params['delivery_name'] ? $params['delivery_name'] : '';
		$sub_data['sku_price'] = $params['sku_amount'];
		$sub_data['delivery_price'] = $params['delivery_amount'];
		$sub_data['real_price'] = $params['paid_amount'];
		$sub_data['status'] = $params['status'];
		$sub_data['pay_status'] = $params['pay_status'];
		$sub_data['confirm_status'] = $params['confirm_status'];
		$sub_data['delivery_status'] = $params['delivery_status'];
		$sub_data['finish_status'] = $params['finish_status'];
		$sub_data['pay_time'] = $params['pay_time'];
		$sub_data['confirm_time'] = $params['confirm_time'];
		$sub_data['delivery_time'] = $params['delivery_time'];
		$sub_data['finish_time'] = $params['finish_time'];
		$sub_data['system_time'] = $params['system_time'];
		$sub_data['remark'] = $params['remark'];
		$sub_data['promotion'] = $params['promotion'];
		//加入订单
		if($params['pay_method'] == 0){
			$params['pay_method'] = '在线支付';
		}elseif ($params['pay_method'] == 1) {
			$params['pay_method'] = '货到付款';
		}else{
			$params['pay_method'] = $params['pay_method'];
		}
		$params['promot_amount'] = $params['sku_amount'] + $params['delivery_amount'] - $params['paid_amount'];
		$order_result = $this->table->add($params);
		//加入子订单
		$sub_result = $this->table_sub->add($sub_data);
		//物流
		if($params['delivery_time']){
			$delivery = array();
			$delivery['o_sku_ids'] = $params['o_sku_ids'];
			$delivery['sub_sn'] = $sub_data['sub_sn'];
			$delivery['delivery_id'] = $params['delivery_id'];
			$delivery['delivery_name'] = $params['delivery_name'];
			$delivery['delivery_sn'] = $params['delivery_sn'];
			$delivery['delivery_time'] = $params['delivery_time'];
			$delivery['isreceive'] = $params['isreceive'];
			$delivery['receive_time'] = $params['receive_time'];
			$delivery['print_time'] = $params['print_time'];
			$delivery_data = $this->table_delivery->create($delivery);
			$this->table_delivery->add($delivery_data);
		}
		return TRUE;
	}
}