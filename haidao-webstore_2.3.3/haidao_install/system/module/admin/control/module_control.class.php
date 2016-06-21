<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class module_control extends init_control
{
	public function _initialize() {
		parent::_initialize();
		$this->m_service = $this->load->service('admin/module');
	}

	public function index() {
		$modules = $this->m_service->lists();
		$install = (int) $_GET['install'];
		$enabled = (int) $_GET['enabled'];
		foreach($modules as $k => $v ) {
			if($v['isinstall'] != $install || $v['isenabled'] != $enabled) unset($modules[$k]);
		}
		$this->load->librarys('View')->assign('modules',$modules);
		$this->load->librarys('View')->assign('install',$install);
		$this->load->librarys('View')->assign('enabled',$enabled);
		$this->load->librarys('View')->display('module_index');
	}
}