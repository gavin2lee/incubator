<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class global_control extends init_control
{
	public function _initialize() {
		parent::_initialize();

	}

    /* 管理登录 */
	public function index() {
		$this->load->librarys('View')->display('global_index');
    }
}