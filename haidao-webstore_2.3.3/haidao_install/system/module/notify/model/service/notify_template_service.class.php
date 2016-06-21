<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class notify_template_service extends service
{
	public function _initialize() {
		$this->table = $this->load->table('notify/notify_template');
	}

	/* 根据标识 */
	public function fetch_by_code($code) {
		$data = $this->table->where(array('id'=>$code))->find();
		if(is_null($data))return FALSE;
		$data['enabled'] = json_decode($data['enabled'], TRUE);
		$data['template'] = json_decode($data['template'], TRUE);
        return $data;
	}
	
	/* 根据标识 */
	public function fetch_by_hook($hook) {
		$data = $this->table->where(array('_string'=>'enabled regexp \''.$hook.'\''))->select();
		if(is_null($data))return FALSE;
		foreach($data as $k=>$v){
			$data[$k]['enabled'] = json_decode($v['enabled'], TRUE);
			$data[$k]['template'] = json_decode($v['template'], TRUE);
			$data[$k]['template'] = $data[$k]['template'][$hook];
		}
        return $data;
	}
	/*导入模版*/
	public function import($params){
		$drivers = json_decode($params['driver'],TRUE);
		$template = json_decode($params['template'],TRUE);
		switch ($params['id']) {
			case 'n_order_success':
				$code = 'create_order';
				break;
			case 'n_pay_success':
				$code = 'pay_success';
				break;
			case 'n_confirm_order':
				$code = 'confirm_order';
				break;
			case 'n_order_delivery':
				$code = 'order_delivery';
				break;
			case 'n_recharge_success':
				$code = 'recharge_success';
				break;
			case 'n_money_change':
				$code = 'money_change';
				break;
			case 'n_goods_arrival':
				$code = 'goods_arrival';
				break;
			case 'n_back_pwd':
				$code = 'forget_pwd';
				break;
			case 'n_reg_validate':
				$code = 'register_validate';
				break;
			case 'n_reg_success':
				$code = 'register_success';
				break;
			default:
				break;
		}
		foreach ($drivers AS $key => $driver) {
			if($driver == 1){
				$data = array();
				$data['id'] = $key;
				$notifys = array();
				$notifys = $this->table->find($data);
				$notify_code = $notify_template = array();
				if(!empty($notifys)){
					$notify_code = json_decode($notifys['enabled'],TRUE);
					$notify_template = json_decode($notifys['template'],TRUE);
				}
				$notify_code[] = $code;
				$notify_template[$key] = $template[$key];
				$data['enabled'] = json_encode($notify_code);
				$data['template'] = json_encode($notify_template);
				$this->table->add($data);
			}
		}
		return TRUE;
	}
}