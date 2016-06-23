<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class admin_menu_service extends service
{
	protected $sqlmap = array();

	public function __construct() {
		$this->model = $this->load->table('admin/admin_menu');
	}
	
	public function fetch_all_by_admin_id($admin_id = 0) {
		return $this->model->where(array('admin_id' => $admin_id))->select();
	}
}