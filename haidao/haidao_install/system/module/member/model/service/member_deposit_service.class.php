<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class member_deposit_service extends service
{
	protected $sqlmap = array();

	public function __construct(){
		$this->table = $this->load->table('member_deposit');
	}

	public function wlog($data = array(),$sqlmap = array()) {
		if(!empty($sqlmap) && $sqlmap !== TRUE){
			$r = $this->table->where($sqlmap)->save($data);
		}else{
			$r = $this->table->add($data);
		}
		return $r;
	}
	public function is_sucess($sqlmap){
		return  $this->table->where($sqlmap)->order('id DESC')->getField('order_status');
	}
}