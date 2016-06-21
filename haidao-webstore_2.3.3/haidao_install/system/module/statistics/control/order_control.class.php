<?php
Core::load_class('init', 'admin');
class order_control extends init_control {

	public function _initialize() {
		parent::_initialize();
		$this->service_order = $this->load->service('order/statistics');
		helper('order/function');
	}

	public function index(){
		$datas = $this->service_order->build_sqlmap(array('days' => 7))->output('sales,districts,payments');
		/* 组装地区信息 */
		if ($datas['districts']) {
			foreach ($datas['districts'] as $k => $v) {
				$datas['districts'][$k]['name'] = $v['name'];
				$datas['districts'][$k]['value'] = $v['value'];
			}
		}
		/* 组装支付方式 */
		if ($datas['payments']) {
			foreach ($datas['payments'] as $k => $v) {
				$datas['pays'][$k] = $v['name'];
			}
		}
		$this->load->librarys('View')->assign('datas',$datas);
        $this->load->librarys('View')->display('order');
	}

	public function ajax_getdata(){
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$days = (int) $_GET['days'];
		$start_time = strtotime($_GET['start_time']);
		$end_time = ($_GET['end_time']) ? strtotime($_GET['end_time']) : strtotime(date('Y-m-d 00:00:00'));
		$datas = $this->service_order->build_sqlmap(array('days' => $days ,'start_time' => $start_time ,'end_time' => $end_time))->output('sales');
		showmessage(lang('statistics/request_success') ,'', 1 ,$datas);
	}

	/* 后台首页获取统计数据 */
	public function ajax_home() {
		$datas = array();
		/* 订单提醒 */
		$datas['orders'] = $this->load->table('order/order')->out_counts();
		/* 商品管理 */
		$datas['goods']['goods_in_sales'] = $this->load->service('goods/goods_spu')->count_spu_info(1);
		$datas['goods']['goods_load_online'] = $this->load->service('goods/goods_spu')->count_spu_info(0);
		$datas['goods']['goods_number_warning'] = $this->load->service('goods/goods_spu')->count_spu_info(2);
		/* 待处理咨询 */
		$datas['consult_load_do'] = $this->load->service('goods/goods_consult')->handle();
		/* 资金管理 */
		$datas['sales'] = $this->service_order->output('sales');
		/* 注册会员总数 */
		$datas['member_total'] = $this->load->table('member/member')->count();
		/* 数据库大小 */
		$querysql = "select round(sum(DATA_LENGTH/1024/1024)+sum(DATA_LENGTH/1024/1024),2) as db_length from information_schema.tables where table_schema='".config('DB_NAME')."'";
		$datas['dbsize'] = $this->load->table('member/member')->query($querysql);
		$this->load->librarys('View')->assign('datas',$datas);
        $datas = $this->load->librarys('View')->get('datas');
		echo json_encode($datas);
	}
}