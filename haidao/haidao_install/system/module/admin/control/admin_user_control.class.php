<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class admin_user_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->model = $this->load->table('admin_user');
		$this->service = $this->load->service('admin_user');
		$this->group_model = $this->load->table('admin_group');
		$this->group_service = $this->load->service('admin_group');
	}

	/* 管理团队 */
	public function index() {
		$data = $this->service->getAll();
		$this->load->librarys('View')->assign('data',$data);
		$this->load->librarys('View')->display('admin_user_index');
	}
	
	/* 删除 */
	public function del() {
		$id = (array)$_GET['id'];
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		if (in_array($id, 1))showmessage(lang('admin/_del_admin_user_error_'), url('index'), 0);
		$this->model->where(array('id' => array('IN', $id)))->delete();
		showmessage(lang('admin/_del_admin_user_success_'), url('index'), 1);
	}
	/* 添加 */
	public function add() {
		$group = $this->group_model->get_select_data();
		if(checksubmit('dosubmit')){
			$_POST=array_filter($_POST); 
			$_POST['encrypt'] = random(10);
			$_POST['password'] = create_password($_POST['password'], $_POST['encrypt']);
			$r = $this->service->save($_POST);
			if($r == false) {
				showmessage($this->model->getError(), url('index'), 1);
			}else{
				showmessage(lang('admin/_update_admin_group_success_'), url('index'), 1);
			}
		}else{
			$this->load->librarys('View')->assign('group',$group);
			$this->load->librarys('View')->display('admin_user_update');
		}
	}
	/* 编辑 */
	public function edit() {
		$group = $this->group_model->get_select_data();
		if (checksubmit('dosubmit')) {
			$_POST=array_filter($_POST); 
			//是否更改密码
			if (array_key_exists("password", $_POST)) {
				$_POST['encrypt'] = random(10);
				$_POST['password'] = create_password($_POST['password'], $_POST['encrypt']);
			}

			$r = $this->service->save($_POST,FALSE);

			showmessage(lang('admin/_update_admin_group_success_'), url('index'), 1);
		} else {
			$id = $_GET['id'];
			$data = $this->model->fetch_by_id($id);
			extract($data);
			$this->load->librarys('View')->assign($data,$data);
			$this->load->librarys('View')->assign('group',$group);
			$this->load->librarys('View')->display('admin_user_update');
		}
		
	}

}
