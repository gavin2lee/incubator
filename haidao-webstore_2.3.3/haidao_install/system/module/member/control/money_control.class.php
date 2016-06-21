<?php
class money_control extends cp_control
{
	public function _initialize() {
		parent::_initialize();
		$this->table = $this->load->table('member/member_log');
		$this->service = $this->load->service('pay/payment');
	}

	public function log($sqlmap=array()) {
		//配置文件
		$_config = cache('setting');
		$member = $this->member;
		$sqlmap['mid'] = $member['id'];
		$sqlmap['type'] = 'money';
		$count = $this->table->where($sqlmap)->count();
		$log = $this->table->where($sqlmap)->page($_GET['page'])->limit(15)->order("id DESC")->select();
		$pages = pages($count,15);
		$SEO = seo('账户余额 - 会员中心');
		$this->load->librarys('View')->assign('_config',$_config);
		$this->load->librarys('View')->assign('member',$member);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->assign('log',$log);
		$this->load->librarys('View')->assign('SEO',$SEO);
        $this->load->librarys('View')->display('money_log');
	}

	/* 余额充值 */
	public function pay() {
		$current = $this->load->service('admin/setting')->get_setting('balance_deposit');
		if(config('TPL_THEME') == 'wap'){
			$payments = $this->service->getpayments('wap', $current);
		}else{
			$payments = $this->service->getpayments('pc', $current);
		}
		if(checksubmit('dosubmit')) {
			$money = $pay_code = $pay_bank = '';
			extract($_GET,EXTR_IF_EXISTS);
			$pay_info = array();
			$pay_info['trade_sn'] = build_order_no('m');
			$pay_info['total_fee'] = $money;
			$pay_info['subject'] = '用户充值：'.$money;
			$pay_info['pay_bank'] = $pay_bank;
			//记录订单
			$data = array();
			$data['mid'] = $this->member['id'];
			$data['money'] = $money;
			$data['order_sn'] = $pay_info['trade_sn'];
			$data['order_time'] = time();
			$this->load->service('member/member_deposit')->wlog($data);
			cookie('last_sn', $pay_info['trade_sn']);
			/* 请求支付 */
			$gateway = $this->service->gateway($pay_code,$pay_info,$pay_config);
			if($gateway === false) {
				showmessage(lang('pay/pay_set_error'));
			}
			if (defined('MOBILE') && $gateway['gateway_type'] == 'redirect') {
				redirect($gateway['gateway_url']);
			}
			$gateway['order_sn'] = $data['order_sn'];
			$gateway['total_fee'] = $money;
			/* 支付成功后的跳转 */
			if(config('TPL_THEME') == 'wap'){
				$gateway['url_forward'] = url('success');
			}else{
				$gateway['url_forward'] = url('log');
			}
			include template('cashier', 'pay');
		}else{
			$SEO = seo('余额充值 - 会员中心');
			$this->load->librarys('View')->assign('SEO',$SEO);
			$this->load->librarys('View')->assign('current',$current);
			$this->load->librarys('View')->assign('payments',$payments);
        	$this->load->librarys('View')->display('money_pay');
		}
	}
	/* 检测是否支付成功 */
	public function payissuccess() {
		$status = $this->load->service('member/member_deposit')->is_sucess(array('mid'=>$this->member['id'],'order_sn'=> cookie('last_sn')));
		exit(json_encode(array('status' => (int)$status)));
	}
	public function success(){
		$order = $this->load->table('member/member_deposit')->where(array('order_sn' => $_GET['order_sn']))->find();
		if (!$order) showmessage(lang('order/order_not_exist'));
		if ($order['mid'] != $this->member['id']) showmessage(lang('order/no_promission_view'));
		$SEO = seo('充值成功');
		$this->load->librarys('View')->assign('order',$order);
		$this->load->librarys('View')->assign('SEO',$SEO);
        $this->load->librarys('View')->display('pay_success');
	}
	public function get_log(){
		//配置文件
		$member = $this->member;
		$sqlmap['mid'] = $member['id'];
		$sqlmap['type'] = 'money';
		$log = $this->table->where($sqlmap)->page($_GET['page'])->limit($_GET['limit'])->order("id DESC")->select();
		$this->load->librarys('View')->assign('log',$log);
        $log = $this->load->librarys('View')->get('log');
		echo json_encode($log);
	}
}