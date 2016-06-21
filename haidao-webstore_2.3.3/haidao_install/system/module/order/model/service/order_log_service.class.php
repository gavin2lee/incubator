<?php
/**
 * 		订单日志服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class order_log_service extends service {

	public function __construct() {
		$this->table = $this->load->table('order/order_log');
	}

	/**
	 * 写入订单日志
	 * @param $params 日志相关参数
	 * @return [boolean]
	 */
	public function add($params = array(),$extra = FALSE) {
		$params = array_filter($params);
		if (empty($params)) {
			$this->error = lang('order/order_log_empty');
			return FALSE;
		}
		if($extra == TRUE){
			$data = $this->table->create($params);
			$result = $this->table->add($data);
		}else{
			$result = $this->table->update($params);
		}
		if (!$result) {
			$this->error = $this->table->getError();
			return FALSE;
		}
		return $result;
	}

	/**
	 * 根据子订单号获取日志
	 * @param $sub_sn : 子订单号(默认空)
	 * @param $order  : 排序(默认主键升序)
	 * @return [result]
	 */
	public function get_by_order_sn($sub_sn = '' , $order = 'id ASC') {
		$sub_sn = (string) remove_xss($sub_sn);
		if (!$sub_sn) {
			$this->error = lang('order/order_sn_not_null');
			return FALSE;
		}
		$order = (string) remove_xss($order);
		$sqlmap = array();
		$sqlmap['sub_sn'] = $sub_sn;
		return $this->table->where($sqlmap)->order($order)->select();
	}
}