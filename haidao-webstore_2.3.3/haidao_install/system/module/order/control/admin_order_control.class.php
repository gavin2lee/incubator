<?php
/**
 * 		后台订单 控制器
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class admin_order_control extends init_control {

	public function _initialize() {
		parent::_initialize();
		/* 数据层 */
		$this->table = $this->load->table('order/order');
		$this->table_sub = $this->load->table('order/order_sub');
		$this->table_o_delivery = $this->load->table('order/order_delivery');
		/* 服务层 */
		$this->service = $this->load->service('order/order');
		$this->service_sub = $this->load->service('order/order_sub');
		$this->service_order_log = $this->load->service('order/order_log');
		$this->service_tpl_parcel = $this->load->service('order/order_tpl_parcel');
		$this->service_parcel = $this->load->service('order/order_parcel');
	}

	/* 订单列表管理 */
	public function index() {
		// 查询条件
		$sqlmap = array();
		$sqlmap = $this->service->build_sqlmap($_GET);
		$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 10;
		$orders = $this->table->page($_GET['page'])->where($sqlmap)->limit($limit)->order('id DESC')->select();
		$count  = $this->table->where($sqlmap)->count();
		$pages  = $this->admin_pages($count, $limit);
		$this->load->librarys('View')->assign('orders',$orders);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('index');
	}

	/* 订单详细页面 */
	public function detail() {
		$order = $this->table_sub->where(array('sub_sn' => $_GET['sub_sn']))->find();
		if (!$order) showmessage(lang('order/order_not_exist'));
		$order['_member'] = $this->load->table('member/member')->find($order['buyer_id']);
		$order['_main'] = $this->table->where(array('sn' => $order['order_sn']))->find();
		// 日志
		$order_logs = $this->service_order_log->get_by_order_sn($order['sub_sn'],'id DESC');
		$this->load->librarys('View')->assign('order',$order);
		$this->load->librarys('View')->assign('order_logs',$order_logs);
		$this->load->librarys('View')->display('detail');
	}

	/* 发货单模版 */
	public function tpl_parcel() {
		if (checksubmit('dosubmit')) {
			$result = $this->service_tpl_parcel->update($_GET['content']);
			if (!$result) showmessage($this->service->error);
			showmessage(lang('_operation_success_'),url('tpl_parcel'),1,'json');
		} else {
			$info = $this->service_tpl_parcel->get_tpl_parcel_by_id(1);
			$this->load->librarys('View')->assign('info',$info);
			$this->load->librarys('View')->display('tpl_parcel');
		}
	}

	/* 确认付款 */
	public function pay() {
		if (checksubmit('dosubmit')) {
			$params = array();
			$params['pay_status'] = remove_xss(strtotime($_GET['pay_status']));
			$params['paid_amount']= sprintf("%0.2f", (float) $_GET['paid_amount']);
			$params['pay_method'] = remove_xss($_GET['pay_method']);
			$params['pay_sn']     = remove_xss($_GET['pay_sn']);
			$params['msg']        = remove_xss($_GET['msg']);
			if ($params['pay_method'] != 'other' && !$params['pay_sn']) {
				showmessage(lang('order/pay_deal_sn_empty'));
			}
			$result = $this->service_sub->set_order($_GET['order_sn'] ,'pay' ,'',$params);
			if (!$result) showmessage($this->service_sub->error);
			showmessage(lang('order/pay_success'),'',1,'json');
		} else {
			// 获取所有已开启的支付方式
			$pays = cache('payment_enable');
			foreach ($pays as $k => $pay) {
				$pays[$k] = $pay['pay_name'];
			}
			$pays['other'] = '其它付款方式';
			$order = $this->table->where(array('sn' => $_GET['order_sn']))->find();
			$this->load->librarys('View')->assign('pays',$pays);
			$this->load->librarys('View')->assign('order',$order);
			$this->load->librarys('View')->display('alert_pay');
		}
	}

	/* 确认订单 */
	public function confirm() {
		if (checksubmit('dosubmit')) {
			$result = $this->service_sub->set_order($_GET['sub_sn'] ,'confirm' ,'',array('msg' => $_GET['msg']));
			if (!$result) showmessage($this->service_sub->error);
			showmessage(lang('确认订单成功'),'',1,'json');
		} else {
			$this->load->librarys('View')->display('alert_confirm');
		}
	}

	/* 确认发货 */
	public function delivery() {
		if (checksubmit('dosubmit')) {
			$params = array();
			$params['is_choise']     = remove_xss($_GET['is_choise']);
			$params['delivery_id']   = remove_xss($_GET['delivery_id']);
			$params['delivery_sn']   = remove_xss($_GET['delivery_sn']);
			$params['msg']           = remove_xss($_GET['msg']);
			$o_sku_ids = remove_xss($_GET['o_sku_id']);
			$params['o_sku_ids']	 = implode(',', $o_sku_ids);
			$result = $this->service_sub->set_order($_GET['sub_sn'] ,'delivery' ,$_GET['status'],$params);
			if (!$result) showmessage($this->service_sub->error);
			showmessage(lang('确认发货成功'),'',1,'json');
		} else {
			// 获取已开启的物流
			$sqlmap = $deliverys = array();
			$sqlmap['enabled'] = 1;
			$deliverys = $this->load->table('order/delivery')->where($sqlmap)->getField('id,name' ,TRUE);
			// 获取子订单下的skus
			$o_skus = $this->service_sub->sub_delivery_skus($_GET['sub_sn']);
			if (!$o_skus) {
				showmessage($this->service_sub->error);
			}
			$this->load->librarys('View')->assign('deliverys',$deliverys);
			$this->load->librarys('View')->assign('o_skus',$o_skus);
			$this->load->librarys('View')->display('alert_delivery');
		}
	}

	/* 确认完成 */
	public function finish() {
		if (checksubmit('dosubmit')) {
			$result = $this->service_sub->set_order($_GET['sub_sn'] ,'finish' ,'',array('msg'=>$_GET['msg']));
			if (!$result) showmessage($this->service_sub->error);
			showmessage(lang('确认完成成功'),'',1,'json');
		} else {
			$this->load->librarys('View')->display('alert_finish');
		}
	}

	/* 取消订单 */
	public function cancel() {
		if (checksubmit('dosubmit')) {
			$result = $this->service_sub->set_order($_GET['sub_sn'] ,'order' ,2,array('msg'=>$_GET['msg'],'isrefund' => (int) $_GET['isrefund']));
			if (!$result) showmessage($this->service_sub->error,'',0,'json');
			showmessage(lang('order/cancel_order_success'),'',1,'json');
		} else {
			$order = $this->table_sub->where(array('sub_sn' => $_GET['sub_sn']))->find();
			$this->load->librarys('View')->assign('order',$order);
			$this->load->librarys('View')->display('alert_cancel');
		}
	}

	/* 作废 */
	public function recycle() {
		if (checksubmit('dosubmit')) {
			$result = $this->service_sub->set_order($_GET['sub_sn'] ,'order' ,3,array('msg'=>$_GET['msg']));
			if (!$result) showmessage($this->service_sub->error);
			showmessage(lang('order/cancellation_order_success'),'',1,'json');
		} else {
			$this->load->librarys('View')->display('alert_recycle');
		}
	}

	/* 删除订单 */
	public function delete() {
		if (checksubmit('dosubmit')) {
			$result = $this->service_sub->set_order($_GET['sub_sn'] ,'order' ,4);
			if (!$result) showmessage($this->service_sub->error);
			showmessage(lang('删除订单成功'),url('order/admin_order/index'),1,'json');
		} else {
			showmessage(lang('_error_action_'));
		}
	}

	/* 修改订单应付总额 */
	public function update_real_price() {
		if (checksubmit('dosubmit')) {
			$result = $this->service->update_real_price($_GET['sub_sn'] ,$_GET['real_price']);
			if (!$result) {
				showmessage($this->service->error);
			}
			showmessage(lang('修改订单应付总额成功'),'',1,'json');
		} else {
			$order = $this->table_sub->where(array('sub_sn' => $_GET['sub_sn']))->find();
			$this->load->librarys('View')->assign('order',$order);
			$this->load->librarys('View')->display('alert_update_real_price');
		}
	}

	/* 发货单管理 */
	public function parcel() {
		$sqlmap = array();
		if (isset($_GET['status'])) {
			$sqlmap['status'] = $_GET['status'];
		}
		if (isset($_GET['keyword']) && !empty($_GET['keyword'])) {
			$sqlmap['order_sn|member_name|address_mobile'] = array('LIKE','%'.$_GET['keyword'].'%');
		}
		$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 10;
		$parcels = $this->load->table('order/order_parcel')->page($_GET['page'])->limit($limit)->order('id DESC')->where($sqlmap)->select();
		$count  = $this->load->table('order/order_parcel')->count();
		$pages  = $this->admin_pages($count, $limit);
		$this->load->librarys('View')->assign('parcels',$parcels);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('parcel');
	}

	/*确认配送*/
	public function complete_parcel() {
		if((int)$_GET['id'] < 1) showmessage(lang('_error_action_'));
		if (checksubmit('dosubmit')) {
			$result = $this->service_parcel->complete_parcel($_GET);
			if(!$result){
				showmessage($this->service_parcel->error);
			}
			showmessage(lang('_operation_success_'),url('parcel'),1);
		}else{
			$parcelinfo = $this->load->table('order/order_parcel')->fetch_by_id($_GET['id']);
			$this->load->librarys('View')->assign('parcelinfo',$parcelinfo);
			$this->load->librarys('View')->display('alert_complete_parcel');
		}
	}

	/*删除发货单*/
	public function delete_parcel() {
		if((int) $_GET['id'] < 1) showmessage(lang('_error_action_'));
		$result = $this->service_parcel->delete_parcel($_GET['id']);
		if(!$result){
			showmessage($this->service_parcel->error);
		}
		showmessage(lang('_operation_success_'),url('order/admin_order/parcel'),1);
	}

	/*打印发货单*/
	public function prints(){
		if((int)$_GET['id'] < 1) showmessage(lang('_error_action_'));
		$info = $this->service_tpl_parcel->get_tpl_parcel_by_id(1);
		//订单信息
		$sub_sn = $this->load->table('order/order_parcel')->fetch_by_id($_GET['id'],'sub_sn');
		//收货人信息
		$userinfo = $this->load->table('order/order_parcel')->where(array('sub_sn'=>$sub_sn))->find();
		//商品信息
		$goods = $this->load->table('order/order_sku')->where(array('sub_sn'=>$sub_sn))->select();
		$info['content'] = str_replace('{order_sn}',$sub_sn,$info['content']);
		$info['content'] = str_replace('{address}',$userinfo['address_detail'],$info['content']);
		$info['content'] = str_replace('{print_time}',date('Y-m-d H:i:s',time()),$info['content']);
		$info['content'] = str_replace('{accept_name}',$userinfo['address_name'],$info['content']);
		$info['content'] = str_replace('{mobile}',$userinfo['address_mobile'],$info['content']);
		$info['content'] = str_replace('{delivery_txt}',$userinfo['delivery_name'],$info['content']);
		$field_start = substr($info['content'],strpos($info['content'],'<tr id="goodslist">'));
		$field_end = substr($info['content'],strpos($info['content'],'<tr id="goodslist">'),strpos($field_start, '<tr>'));
		$total_num = 0;
		$total_price = 0;
		foreach($goods as $k => $v){
			$str = str_replace('{sort_id}',$k+1,$field_end);
			$str = str_replace('{products_sn}',$v['sku_barcode'],$str);
			$str = str_replace('{goods_name}',$v['sku_name'],$str);
			$str = str_replace('{goods_spec}',$v['_sku_spec'],$str);
			$str = str_replace('{shop_price}',$v['sku_price'],$str);
			$str = str_replace('{number}',$v['buy_nums'],$str);
			$str = str_replace('{total_goods_price}',$v['real_price'],$str);
			$goods[$k] = $str;
			$total_num = $total_num + $v['buy_nums'];
			$total_price =  $total_price + $v['real_price'];
		}
		$goods=implode('', $goods);
		$info['content']=str_replace($field_end, $goods, $info['content']);
		$info['content']=str_replace('{total_num}', $total_num, $info['content']);
		$info['content']=str_replace('{total_price}', number_format($total_price,2), $info['content']);
		$this->load->librarys('View')->assign('info',$info);
		$this->load->librarys('View')->assign('sub_sn',$sub_sn);
		$this->load->librarys('View')->display('prints_parcel');
	}


	/* 快递单管理 */
	public function deliverys() {
		$sqlmap = array();
		$sqlmap['delivery_id'] = array("GT" ,0);
		if (isset($_GET['isprint'])) {
			if ($_GET['isprint'] == 1) {
				$sqlmap['print_time'] = array("GT" ,0);
			} else if($_GET['isprint'] == 0) {
				$sqlmap['print_time'] = array("EQ" , 0);
			}
		} else {
			$sqlmap['print_time'] = array("EQ" , 0);
		}
		// if (isset($_GET['keyword']) && !empty($_GET['keyword'])) {
		// 	$sqlmap['order_sn|member_name|address_mobile'] = array('LIKE','%'.$_GET['keyword'].'%');
		// }
		$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 10;
		$o_deliverys = $this->table_o_delivery->page($_GET['page'])->order('id DESC')->limit($limit)->where($sqlmap)->select();
		foreach ($o_deliverys as $k => $v) {
			$o_deliverys[$k]['_sub_order'] = $this->table_sub->where(array('sub_sn' => $v['sub_sn']))->find();
		}
		$count = $this->table_o_delivery->where($sqlmap)->count();
		$pages = $this->admin_pages($count, $limit);
		$this->load->librarys('View')->assign('o_deliverys',$o_deliverys);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('deliverys');
	}

	/* 打印快递单 */
	public function print_kd() {
		if (checksubmit('dosubmit')) {
			/* 标记快递单为已打印 */
			$o_id = (int) $_GET['o_id'];
			$this->table_o_delivery->where(array('id' => $_GET['o_id']))->setField('print_time' ,time());
			return TRUE;
		} else {
			$o_delivery = $this->table_o_delivery->where(array('id' => $_GET['o_id']))->find();
			$sub_order = $this->load->service('order/order_sub')->sub_detail($o_delivery['sub_sn']);
			$main_order = $this->table->where(array('sn' => $sub_order['order_sn']))->find();
			$setting = $this->load->service('admin/setting')->get_setting();
			$_delivery = $this->load->table('order/delivery')->where(array('id' => $o_delivery['delivery_id']))->find();
			// 替换值
			if ($_delivery['tpl']) {
				$_delivery['tpl'] = json_decode($_delivery['tpl'] ,TRUE);
				$str = '';
				foreach ($_delivery['tpl']['list'] as $k => $v) {
					$str = str_replace('left','x',json_encode($v));
					$str = str_replace('{address_name}',$main_order['address_name'],$v);
					$str = str_replace('{address_mobile}',$main_order['address_mobile'],$str);
					$str = str_replace('{address_detail}',$main_order['address_detail'],$str);
					$str = str_replace('{sender_name}',$setting['sender_name'],$str);
					$str = str_replace('{sender_mobile}',$setting['sender_mobile'],$str);
					$str = str_replace('{sender_address}',$setting['sender_address'],$str);
					$str = str_replace('{real_amount}',$main_order['real_amount'],$str);
					$str = str_replace('{paid_amount}',$main_order['paid_amount'],$str);
					$str = str_replace('{remark}',$sub_order['remark'],$str);
					$str = str_replace('{dateline}',date('Y-m-d H:i:s' , time()),$str);
					$_delivery['tpl']['list'][$k] = $str;
				}
			}
			$this->load->librarys('View')->assign('o_delivery',$o_delivery);
			$this->load->librarys('View')->assign('_delivery',$_delivery);
			$this->load->librarys('View')->display('print_kd');
		}
	}
}