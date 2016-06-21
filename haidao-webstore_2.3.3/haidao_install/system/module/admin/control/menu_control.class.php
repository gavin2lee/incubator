<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class menu_control extends init_control
{
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('admin_menu');
	}

    /* 管理登录 */
	public function index() {
		$menus = $this->service->fetch_all_by_admin_id($this->admin['id']);
		$this->load->librarys('View')->assign('menus',$menus);
		$this->load->librarys('View')->display('menu_index');
	}
}