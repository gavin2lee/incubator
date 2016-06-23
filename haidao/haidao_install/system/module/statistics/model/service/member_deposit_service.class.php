<?php
/**
 *      统计服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class member_deposit_service extends service {

	public function __construct() {
		$this->deposit_model = $this->load->table('member_deposit');
	}

	public function _query($field,$sqlmap,$group){
		return $this->deposit_model->field($field)->where($sqlmap)->group($group)->select();
	}

}
