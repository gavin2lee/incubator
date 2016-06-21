<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class notify_service extends service
{
	protected $entrydir = '';

	public function _initialize() {
		$this->entrydir = APP_PATH.'module/notify/library/driver/';
		$this->table = $this->load->table('notify/notify');
	}

	public function fetch_all() {
		$folders = glob($this->entrydir.'*');
        foreach ($folders as $key => $folder) {
            $file = $folder. DIRECTORY_SEPARATOR .'config.xml';
            if(file_exists($file)) {
                $importtxt = @implode('', file($file));
                $xmldata = xml2array($importtxt);
                $notifys[$xmldata['code']] = $xmldata;
            }
        }
        $notifys = array_merge_multi($notifys, $this->get_fech_all());
		return $notifys;
	}

	/* 根据标识 */
	public function fetch_by_code($code) {
		if(empty($code)) {
			$this->error = lang('_param_error_');
			return false;
		}
		if(!is_dir($this->entrydir.$code) || !file_exists($this->entrydir.$code)) {
			$this->error = lang('notify/no_found_config_file');
			return false;
		}
		$config = $this->entrydir.$code.'/config.xml';
        $importtxt = @implode('', file($config));
        $xmldata = xml2array($importtxt);
        return $xmldata;
	}
	
	public function import($params){
		$params['config'] = json_decode($params['config'],TRUE);
		return $this->config($params['config'],$params['code']);
	}

	public function config($vars, $code) {
		$notify = $this->fetch_by_code($code);
		if($notify === false) {
			return false;
		}
		$notify['config'] = unit::json_encode($vars);
		if($this->table->find($code)) {
			$rs = $this->table->update($notify);
		} else {
			$rs = $this->table->add($notify);
			$this->load->table('notify_template')->add(array('id'=>$code));
		}
		if($rs === false) {
			$this->error = lang('notify/config_operate_error');
			return false;
		}
		$this->get_fech_enable_code();
		return true;
	}

	public function get_fech_all() {
		$result = array();
		$notifys = $this->load->table('notify')->getField('code,enabled,config', TRUE);
		foreach ($notifys as $key => $value) {
			$value['configs'] = json_decode($value['config'], TRUE);
			$result[$value['code']] = $value;
		}
		return $result;
	}
	
	//根据code获取启用的通知方式
	public function get_fech_enable_code($code) {
		$result = cache('notify_enable');
		if(FALSE === $result || empty($code)){
			$result = array();
			$notifys = $this->load->table('notify')->where(array('enabled'=>1))->getField('code,enabled,config', TRUE);
			foreach ($notifys as $key => $value) {
				$value['configs'] = json_decode($value['config'], TRUE);
				$result[$value['code']] = $value;
			}
			cache('notify_enable',$result);
			$result = cache('notify_enable');
		}
		return $result[$code];
	}
	
	/**
	 * [启用禁用支付方式]
	 * @param string $pay_code 支付方式标识
	 * @return TRUE OR ERROR
	 */
	public function change_enabled($code) {
		$result = $this->table->where(array('code' => $code))->save(array('enabled' => array('exp', '1-enabled'), 'dateline' => time()));
		if ($result == 1) {
			$result = TRUE;
			$this->get_fech_enable_code();
		} else {
			$result = $this->table->getError();
		}
		return $result;
	}

	/**
	 * [execute 通知执行接口]
	 */
	public function execute($type,$member,$level = 100){
		$hook_data = $this->load->service('notify/notify_template')->fetch_by_hook($type);
		if(!$hook_data) return FALSE;
		//组织模版内容替换
		$setting = cache('setting', '', 'common');
		$replace = array(
			'{username}' => $member['username'] ? $member['username'] : ($member['member']['username'] ? $member['member']['username'] : ''),
			'{site_name}' => $setting['site_name'],
		);
		switch ($type) {
			case 'register_validate':
				unset($replace['{username}']);
				$replace['{mobile_validate}'] = $member['vcode'];
				break;
			case 'email_validate':
				$replace['{email_validate}'] = $member['email_validate'];
				break;
			case 'mobile_validate':
				$replace['{mobile_validate}'] = $member['vcode'];
				break;
			case 'forget_pwd':
				$replace['{email_validate}'] = $member['email_validate'];
                $replace['{mobile_validate}'] = $member['mobile_validate'];
				break;
			case 'money_change':
				$replace['{change_money}'] = $member['change_money'];
				$replace['{user_money}'] = $member['user_money'];
				break;
			case 'recharge_success':
				$replace['{recharge_money}'] = $member['recharge_money'];
				$replace['{user_money}'] = $member['user_money'];
				break;
			case 'order_delivery':
				$replace['{delivery_sn}'] = $member['delivery_sn'];
				$replace['{delivery_type}'] = $member['delivery_type'];
				break;
			case 'confirm_order':
				$replace['{order_sn}'] = $member['order_sn'];
				break;
			case 'pay_success':
				$replace['{real_amount}'] = $member['real_amount'];
				break;
			case 'create_order':
				$replace['{order_sn}'] = $member['order_sn'];
				break;
			case 'goods_arrival':
				$replace['{goods_name}'] = $member['goods_name'];
				$replace['{goods_spec}'] = $member['goods_spec'];
				break;
			default:
				break;
		}
		//遍历
		foreach ($hook_data as $key => $value) {
			
			$enabled = $this->table->where(array('code' => $value['id']))->getField('enabled');
			if($enabled == 0) continue;
			$data = array();
			switch ($value['id']) {
				case 'email':
					$replace['{email}'] = $member['email'] ? $member['email'] : $member['member']['email'];
					if(!$replace['{email}']) break;
					$data['to'] = $replace['{email}'];
					$data['subject'] = str_replace(array_keys($replace), $replace, $value['template']['title']);
					$data['body'] = str_replace(array_keys($replace), $replace, $value['template']['content']);
					$data['body'] = str_replace('./uploadfile/','http://'.$_SERVER['HTTP_HOST'].'/uploadfile/',$data['body']);
					break;
				case 'message':
					$data['mid'] = $member['member']['id'];
					$data['title'] = str_replace(array_keys($replace), $replace, $value['template']['title']);
					$data['content'] = str_replace(array_keys($replace), $replace, $value['template']['content']);
					break;
				case 'wechat':
					
					break;
				case 'sms':
					$mobile = $member['mobile'] ? $member['mobile'] : $member['member']['mobile'];
					if(!$mobile) break;
					$template_replace = $this->template_replace();
					$format_data = array();
					foreach ($replace as $k => $v) {
						if(!empty($v)){
							$format_data[$template_replace[$k]] = $v;
						}
					}
					$data['mobile'] = $mobile;
					$data['tpl_id'] = $value['template']['template_id'];
					$data['tpl_vars'] = $this->format_sms_data($format_data);
					break;
				default:
					break;
			}
			if(!empty($data)){
				$params = unit::json_encode($data);
				$this->load->service('notify/queue')->add($value['id'],'send',$params,$level);
			} 
		}
		return TRUE;
	}
	/**
	 * [template_replace 替换模版内容]
	 * @return [type] [description]
	 */
	public function template_replace(){
		$replace = array(
			'{site_name}' => '{商城名称}',
			'{username}' => '{用户名}',
			'{mobile}' => '{用户手机}',
			'{email}' => '{用户邮箱}',
			'{goods_name}' => '{商品名称}',
			'{goods_spec}' => '{商品规格}',
			'{order_sn}' => '{主订单号}',
			'{order_amount}' => '{订单金额}',
			'{shop_price}' => '{商品金额}',
			'{real_amount}' => '{付款金额}',
			'{pay_type}' => '{支付方式}',
			'{change_money}' => '{变动金额}',
			'{delivery_type}' => '{配送方式}',
			'{delivery_sn}' => '{运单号}',
			'{user_money}' => '{用户可用余额}',
			'{recharge_money}' => '{充值金额}',
			'{email_validate}' => '{邮件验证链接}',
			'{mobile_validate}' => '{验证码}'
		);
		return $replace;
	}
	public function format_sms_data($data){
		foreach ($data as $k => $v) {
			if(preg_match('/\{(.+?)\}/', $k)){
				$_data[preg_replace('/\{(.+?)\}/','$1',$k)] = $v;
			}
		}
		return $_data;
	}
}