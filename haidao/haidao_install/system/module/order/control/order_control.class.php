<?php
/**
 * 		前台订单控制器
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init','goods');
class order_control extends init_control {

	public function _initialize() {
		parent::_initialize();
		if ($this->member['id'] == 0) {
			$url_forward = $_GET['url_forward'] ? $_GET['url_forward'] : urlencode($_SERVER['REQUEST_URI']);
			showmessage(lang('_not_login_'), url('member/public/login',array('url_forward'=>$url_forward)),0);
		}
		$this->table = $this->load->table('order/order');
		$this->service = $this->load->service('order/order');
		$this->service_cart = $this->load->service('order/cart');
		$this->service_delivery = $this->load->service('order/delivery');
	}

	/* 购物车结算 */
	public function settlement() {
		// 会员收货地区id，便于加载配送物流
		$district_id = $this->member['_address'][0]['district_id'];
		$skuids = $_GET['skuids'];
		if (isset($_GET['district_id']) && is_numeric($_GET['district_id'])) {
			$district_id = (int) $_GET['district_id'];
		}
		$pay_type = (int) $_GET['pay_type'];
		$deliverys = (array) $_GET['deliverys'];
		$order_prom = (array) $_GET['order_prom'];
		$sku_prom = (array) $_GET['sku_prom'];
		$remarks = (array) $_GET['remarks'];
		$invoices = (array) $_GET['invoices'];
		// 购物车商品
		$carts =  $this->service->create($this->member['id'], $skuids , $district_id, $pay_type, $deliverys, $order_prom, $sku_prom, $remarks, $invoices, false);
		if (empty($carts)) showmessage(lang('order/clearing_goods_no_exist'),url('member/order/index', array('type' => 1)));
		// 收货地址
		$address = $this->member['_address'];
		foreach ($address as $k => $val) {
			$area = $this->load->service('admin/district')->fetch_position($val['district_id']);
			$address[$k]['_area'] = $area[2].' '.$area[3];
		}
		// 读取后台设置
		$setting = $this->load->service("admin/setting")->get_setting();
		// 支付方式
		$pay_type = array();
		switch ($setting['pay_type']) {
			case 2:
				$pay_type = array(1 => '在线支付');
				break;
			case 3:
				$pay_type = array(2 => '货到付款');
				break;
			default:
				$pay_type = array(1 => '在线支付',2 =>'货到付款');
				break;
		}
		// 所有一级地区
		$districts = $this->load->service('admin/district')->get_children();
		$SEO = seo('核对订单信息');
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->assign('setting',$setting);
		$this->load->librarys('View')->assign('districts',$districts);
		$this->load->librarys('View')->assign('pay_type',$pay_type);
		$this->load->librarys('View')->assign('carts',$carts);
		$this->load->librarys('View')->assign('address',$address);
		$this->load->librarys('View')->display('cart_settlement');
	}

	public function get() {
		// 会员收货地区id，便于加载配送物流
		$district_id = $this->member['_address'][0]['district_id'];
		$skuids = $_GET['skuids'];
		if (isset($_GET['district_id']) && is_numeric($_GET['district_id'])) {
			$district_id = (int) $_GET['district_id'];
		}
		$pay_type = (int) $_GET['pay_type'];
		$deliverys = (array) $_GET['deliverys'];
		$order_prom = (array) $_GET['order_prom'];
		$sku_prom = (array) $_GET['sku_prom'];
		$remarks = (array) $_GET['remarks'];
		$invoices = (array) $_GET['invoices'];
		$result =  $this->service->create($this->member['id'], $skuids , $district_id, $pay_type, $deliverys, $order_prom, $sku_prom, $remarks, $invoices, false);
		if($result === false) {
			showmessage($this->service->error);
		} else {
			showmessage($this->service->error, url('index'), 1, $result);
		}
	}

	public function ajax_settlement(){
		showmessage(lang('_operation_success_'),url('order/order/settlement'),1);
	}
	/* 根据地区id获取下级地区 */
	public function ajax_get_district_childs() {
		$id = (int) $_GET['id'];
		$result = $this->load->service('admin/district')->get_children($id);
		$this->load->librarys('View')->assign('result',$result);
        $result = $this->load->librarys('View')->get('result');
		echo json_encode($result);
	}

	/* 获取商家物流信息 */
	public function get_deliverys() {
		unset($_GET['page']);
		$deliverys = array();
		$deliverys = $this->service_delivery->get_deliverys($_GET['district_id'] , $_GET['skuids']);
		$this->load->librarys('View')->assign('deliverys',$deliverys);
        $deliverys = $this->load->librarys('View')->get('deliverys');
		echo json_encode($deliverys);
	}

	/* 获取该物流的支付方式 */
	public function get_methods() {
		$delivery = $this->service_delivery->get_by_id($_GET['delivery_id']);
		$this->load->librarys('View')->assign('delivery',$delivery);
        $delivery = $this->load->librarys('View')->get('delivery');
		echo json_encode($delivery['method']);
	}

	/* 获取物流费用 */
	public function get_payable() {
		$payable = $this->load->table('order/delivery_district')->where(array("id" => $_GET['id']))->find();
		$this->load->librarys('View')->assign('payable',$payable);
        $payable = $this->load->librarys('View')->get('payable');
		echo json_encode($payable);
	}

	/**
	 * 创建订单
	 * @param 	array
	 * @return  [boolean]
	 */
	public function create() {
		// 会员收货地区id，便于加载配送物流
		$district_id = $this->member['_address'][0]['district_id'];
		$skuids = $_GET['skuids'];
		if (isset($_GET['district_id']) && is_numeric($_GET['district_id'])) {
			$district_id = (int) $_GET['district_id'];
		}
		$pay_type = (int) $_GET['pay_type'];
		$deliverys = (array) $_GET['deliverys'];
		$order_prom = (array) $_GET['order_prom'];
		$sku_prom = (array) $_GET['sku_prom'];
		$remarks = (array) $_GET['remarks'];
		$invoices = (array) $_GET['invoices'];
		$result =  $this->service->create($this->member['id'], $skuids , $district_id, $pay_type, $deliverys, $order_prom, $sku_prom, $remarks, $invoices, true);
		if (!$result) {
			showmessage($this->service->error);
		}
		showmessage(lang('order/order_create_success'),url('order/order/detail',array('order_sn'=>$result)),1,'json');
	}

	public function detail() {
		$order_sn = trim($_GET['order_sn']);
		if (empty($order_sn)) showmessage(lang('_error_action_'));
		$order = $this->table->detail($order_sn)->output();
		if ($this->member['id'] != $order['buyer_id']) {
			showmessage(lang('pay/no_promission_view'));
		}
		if ($order['pay_type'] == 1 && $order['pay_status'] != 0) {
			showmessage(lang('order/order_not_pay_status'));
		}
		if($order['real_amount'] == 0) {
			redirect(url('order/order/pay_success',array('sn'=>$sn)));
		}
		if (checksubmit('dosubmit')) {
			$result = $this->service->detail_payment($_GET['order_sn'],$_GET['balance_checked'],$_GET['pay_code'],$_GET['pay_bank']);
			if ($result == FALSE) showmessage($this->service->error);
			$gateway = $result['gateway'];
			// 已支付成功的订单跳转到成功页面
			if ($result['pay_success'] == 1) {
				redirect($gateway['url_forward']);
			}
			$SEO = seo('收银台 - 会员中心');
			if (defined('MOBILE') && $gateway['gateway_type'] == 'redirect') {
				redirect($gateway['gateway_url']);
			}
			include template('cashier', 'pay');
		} else {
			if ($order['pay_type'] == 2) {	// 货到付款
				include template('order_success');
				return FALSE;
			}
			// 后台设置-余额支付 1:开启，0：关闭
			$setting = $this->load->service('admin/setting')->get_setting();
			$balance_pay = $setting['balance_pay'];
			$member_info = $this->member;
			$pays = $setting['pays'];
			$payments = $this->load->service('pay/payment')->getpayments(defined('MOBILE') ? 'wap' : 'pc', $pays);
			$SEO = seo('订单支付');
			$this->load->librarys('View')->assign('order',$order);
			$this->load->librarys('View')->assign('order_sn',$order_sn);
			$this->load->librarys('View')->assign('setting',$setting);
			$this->load->librarys('View')->assign('balance_pay',$balance_pay);
			$this->load->librarys('View')->assign('member_info',$member_info);
			$this->load->librarys('View')->assign('pays',$pays);
			$this->load->librarys('View')->assign('payments',$payments);
			$this->load->librarys('View')->assign('SEO',$SEO);
			$this->load->librarys('View')->display('detail_payment');
		}
	}

	/* 获取支付状态 */
	public function get_pay_status() {
		$order_sn = $_GET['order_sn'];
		$order = $this->table->detail($order_sn)->output();
		if (!$order || $order['buyer_id'] != $this->member['id']) {
			showmessage(lang('pay/no_promission_view'));
		}
		if ($order['_status']['now'] == 'pay') {
			showmessage(lang('pay/order_paid'),url('order/order/pay_success',array('order_sn'=>$order_sn)),1,'json');
		} else {
			showmessage(lang('order/order_no_pay'));
		}
	}

	/* 支付成功 */
	public function pay_success() {
		$order_sn = $_GET['order_sn'];
		$order = $this->table->detail($order_sn)->output();
		if (!$order) showmessage(lang('order/order_not_exist'));
		if ($order['buyer_id'] != $this->member['id']) showmessage(lang('order/no_promission_view'));
		$SEO = seo('支付成功');
		$this->load->librarys('View')->assign('order',$order);
		$this->load->librarys('View')->assign('order_sn',$order_sn);
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->display('order_success');
	}

	/* 移动端 => 选择收货地址 */
	public function settlement_address() {
		$SEO = seo('选择收货地址');
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->display('settlement_address');
	}

	/* 移动端 => 选择支付&配送方式  */
	public function settlement_delivery() {
		$SEO = seo('选择支付配送');
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->display('settlement_delivery');
	}

	/* 移动端 => 发票信息  */
	public function settlement_invoice() {
		$SEO = seo('发票信息');
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->display('settlement_invoice');
	}

	/* 移动端 => 订单促销 */
	public function settlement_order() {
		$SEO = seo('选择订单促销');
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->display('settlement_order');
	}

	/* 移动端 => 商品促销 */
	public function settlement_goods() {
		$SEO = seo('选择商品促销');
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->display('settlement_goods');
	}

	/* 获取会员收货地址 */
	public function get_address() {
		$data = $this->load->table('member/member_address')->fetch_all_by_mid($this->member['id'], 'isdefault DESC');
		foreach ($data as $k => $val) {
			$area = $this->load->service('admin/district')->fetch_position($val['district_id']);
			$data[$k]['_area'] = $area[2].' '.$area[3];
		}
		$this->load->librarys('View')->assign('data',$data);
        $data = $this->load->librarys('View')->get('data');
		echo json_encode($data);
	}
}