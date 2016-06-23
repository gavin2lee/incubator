<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class log_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->model = $this->load->table('log');
		//$this->service = model('log','service');	
	}

	/* 日志列表 */
	public function index() {
		$sqlmap = array();
		$count = $this->model->where($sqlmap)->count();
		$log = $this->model->where($sqlmap)->page($_GET['page'])->limit(10)->select();
		$pages = $this->admin_pages($count, 10);
		$this->load->librarys('View')->assign('log',$log);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('log_index');
	}
	
	/* 删除 */
	public function del() {
		$id = (array)$_GET['id'];
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		if (in_array($id, 1))showmessage(lang('admin/_del_admin_user_error_'), url('index'), 0);
		$this->model->where(array('id' => array('IN', $id)))->delete();
		showmessage(lang('admin/_del_log_success_'), url('index'), 1);
	}
}
