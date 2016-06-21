<?php
/**
 *      支付设置服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class payment_service extends service {

	public function __construct() {
		$this->model = model('payment');
	}

	public function fetch_all() {
		$entrydir = 'system/module/pay/library/driver/pay/';
		$folders = glob($entrydir.'*');
        foreach ($folders as $key => $folder) {
            $file = $folder. DIRECTORY_SEPARATOR .'config.xml';
            if(file_exists($file)) {
                $importtxt = @implode('', file($file));
                $xmldata = xml2array($importtxt);
                $payments[$xmldata['pay_code']] = $xmldata;
                $payments[$xmldata['pay_code']]['pay_install'] = 0;
            }
        }
        $payments = array_merge_multi($payments, $this->get_fetch_all());
		return multi_array_sort($payments,'pay_name');
	}

	public function get_fetch_all() {
		$result = array();
		$result = $this->model->getField('pay_code,pay_name,pay_fee,pay_desc,enabled,config,dateline,sort,isonline,applie', TRUE);
		foreach ($result as $key => $value) {
			$result[$key]['config'] = unserialize($value['config']);
			$result[$key]['pay_install'] = 1;
		}
		return $result;
	}
	/**
	 * [支付方式列表]
	 * @return boolean
	 */
	public function build_cache() {
		$result_enable = $this->model->where(array('enabled' => 1))->getField('pay_code,pay_name,pay_fee,pay_desc,enabled,config,dateline,sort,isonline,applie');
		cache('payment_enable', $result_enable);
		return TRUE;
	}

	/**
	 * [启用禁用支付方式]
	 * @param string $pay_code 支付方式标识
	 * @return TRUE OR ERROR
	 */
	public function change_enabled($pay_code) {
		$result = $this->model->where(array('pay_code' => $pay_code))->save(array('enabled' => array('exp', '1-enabled'), 'dateline' => time()));
		if ($result == 1) {
			$result = TRUE;
			$this->build_cache();
		} else {
			$result = $this->model -> getError();
		}
		return $result;
	}

	/**
	 * [修改支付方式]
	 * @param array $data 数据
	 * @param bool $valid 是否M验证
	 * @return TRUE OR ERROR
	 */
	public function save($data, $valid = TRUE) {
		if($data['pay_install'] == 0){
			$result = $this->model->add($data);
		}else{
			$result = $this->model->update($data, $valid);
		}
		if ($result) {
			$result = TRUE;
			$this->build_cache();
		} else {
			$result = $this->model->getError();
		}
		return $result;
	}

	/**
	 * 获取已开启支付方式
	 * @return array 已开启的支付方式
	 */
	public function getpayments($applie = 'pc', $pays = array()){
		if(FALSE === cache('payment_enable'))$this->build_cache();
		$payments = cache('payment_enable');
		foreach ($payments as $key => $pay) {
			if($applie && $pay['applie'] != 'all' && $pay['applie'] != $applie) unset($payments[$key]);
		}
		$payments = array_intersect_key($payments,array_flip($pays));
		$result = array();
		foreach ($payments as $k => $pay) {
			$pay['pay_ico'] = $pay['pay_code'];
			if($k == 'bank') {
				$config = unserialize($pay['config']);
				$banks = explode(',', $config['banks']);
				foreach ($banks as $bank) {
					$pay['pay_ico'] = $bank;
					$pay['pay_code'] = 'bank';
					$pay['pay_bank'] = $bank;
					$result[] = $pay;
				}
			} elseif ($k == 'jdpay'){
				$config = unserialize($pay['config']);
				if($config['applie'] == 'all' || $config['applie'] == $applie){
					$banks = explode(',', $config['banks']);
					$jd_applies = explode(',',$config['applie']);
					foreach ($banks as $bank) {
						$pay['pay_ico'] = $bank;
						$pay['pay_code'] = 'jdpay';
						$pay['pay_bank'] = $bank;
						$pay['applie'] = $config['applie'];
						$result[] = $pay;
					}
				}
			} else {
				$result[] = $pay;
			}
		}
		return $result;
	}

	/* 创建支付请求 */
	public function gateway($pay_code, $pay_info, $pay_config = null){
		$classfile = APP_PATH.'module/pay/library/pay_factory.class.php';
		require_cache($classfile);
		$pay_factory = new pay_factory($pay_code,$pay_config);
		return $pay_factory->set_productinfo($pay_info)->gateway();
	}

	/* 同步回调 */
	public function _return($driver){
		$classfile = APP_PATH.'module/pay/library/pay_factory.class.php';
		require_cache($classfile);
		$pay_factory = new pay_factory($driver);
		return $pay_factory->_return();

	}

	/* 异步通知 */
	public function _notify($driver){
		return $this->_return($driver);
	}

}
