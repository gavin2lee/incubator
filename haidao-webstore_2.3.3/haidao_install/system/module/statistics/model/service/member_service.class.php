<?php
/**
 *      统计服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class member_service extends service {

	public function __construct() {
		$this->order_model = $this->load->table('member');
	}

	public function _query($field,$sqlmap,$group){
		return $this->order_model->field($field)->where($sqlmap)->group($group)->select();
	}
	
	public function _count($field,$sqlmap){
		return $this->order_model->field($field)->where($sqlmap)->group($group)->count();
	}
}
