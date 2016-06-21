<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class admin_control extends init_control
{
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('notify/notify');
		$this->temlage_service = $this->load->service('notify/notify_template');
	}

	public function index() {
		$notifys = $this->service->fetch_all();
		$this->load->librarys('View')->assign('notifys',$notifys);
        $this->load->librarys('View')->display('notify_index');
	}

	/* 配置参数 */
	public function config() {
		$notify = $this->service->fetch_by_code($_GET['code']);
		if($notify === FALSE) {
			showmessage($this->service->error);
		}
		if(checksubmit('dosubmit')) {
			$r = $this->service->config($_GET['vars'], $_GET['code']);
			if($r === false) {
				showmessage($this->service->error);
			}
			showmessage(lang('_operation_success_'), url('index'), 1);
		} else {
			$_setting = $this->service->get_fech_all();
			$_config = $_setting[$_GET['code']]['configs'];
			$this->load->librarys('View')->assign('notify',$notify);
			$this->load->librarys('View')->assign('_setting',$_setting);
			$this->load->librarys('View')->assign('_config',$_config);
        	$this->load->librarys('View')->display('notify_config');
		}
	}

	/* 配置模板 */
	public function template() {
		$notify = $this->service->fetch_by_code($_GET['code']);
		$hooks = array(
			'after_register'=>'注册成功',
			'register_validate'=>'注册验证',
			'mobile_validate'=>'手机验证',
			'email_validate'=>'邮箱验证',
			'forget_pwd'=>'找回密码',
			'money_change'=>'余额变动',
			'recharge_success'=>'充值成功',
			'order_delivery'=>'订单发货',
			'confirm_order'=>'确认订单',
			'pay_success'=>'付款成功',
			'create_order'=>'下单成功',
			'goods_arrival'=>'商品到货',
		);
		$replace = $this->service->template_replace();
		$ignore = explode(',', $notify['ignore']);
		foreach ($ignore as $k => $v) {
			unset($hooks[$v]);
		}
		if(checksubmit('dosubmit')) {
			$template = array();
			$enabled = array();
			foreach ($hooks as $k => $v) {
				$template[$k] = str_replace($replace,array_keys($replace),$_GET[$k]);
			}
			$enabled = $_GET['enabled'];
			$_GET['template'] = unit::json_encode($template);
			$_GET['enabled'] = json_encode($enabled);
			$_GET['name'] = $notify['name'];
			$result = $this->load->table('notify_template')->update($_GET);
			showmessage(lang('notify/upload_message_success'),url('index'));
		}else{
			$template = $this->temlage_service->fetch_by_code($_GET['code']);
			foreach ($template['template'] as $k => $temp) {
				$template['template'][$k] = str_replace(array_keys($replace),$replace,$temp);
			}
			//单独处理短信
			if($_GET['code'] == 'sms'){
				$cloud = $this->load->service('admin/cloud');
				$data = $cloud->getsmstpl();
				$sms_num = $cloud->getsmsnum();
				foreach ($data['result'] as $k => $v) {
					$template[$v['tpl_type']][]= $v;
				}
				$this->load->librarys('View')->assign('sms_num',$sms_num);
			}
			$this->load->librarys('View')->assign('notify',$notify);
			$this->load->librarys('View')->assign('hooks',$hooks);
			$this->load->librarys('View')->assign('replace',$replace);
			$this->load->librarys('View')->assign('ignore',$ignore);
			$this->load->librarys('View')->assign('template',$template);
        	$this->load->librarys('View')->display('notify_template');
		}
	}

	/* 开启或关闭 */

    public function ajax_enabled() {
    	if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
        $code = $_POST['code'];
        if ($this->service->change_enabled($code)) {
            showmessage(lang('_operation_success_'), '', 1);
        } else {
            showmessage(lang('_operation_fail_'), '', 0);
        }
    }
	
	/**
     * 卸载支付方式
     */
    public function uninstall() {
    	if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
        $code = $_GET['code'];
        
        $this->load->table('notify')->where(array('code'=>$code))->delete();
        $this->load->table('notify_template')->where(array('id'=>$code))->delete();
        
        showmessage(lang('notify/uninstall_success'), url('index'), 1);
    }
}