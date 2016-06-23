<?php
Core::load_class('init', 'admin');
class order_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->model = $this->load->service('promotion/promotion_order');
		$this->table = $this->load->table('promotion/promotion_order');
	}

	public function index() {
		$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 20;
		$result = $this->model->lists($sqlmap, $limit, $_GET['page']);
		$result['pages'] = $this->admin_pages($result['count'], $limit);
		$this->load->librarys('View')->assign('result',$result);
        $this->load->librarys('View')->display('order_index');
	}

	public function add() {
		if(checksubmit('dosubmit')) {
			$_GET['discount'] = $_GET['discount'][$_GET['type']];
			$result = $this->model->update($_GET);
			if($result === false) {
				showmessage($this->model->error);
			} else {
				showmessage(lang('promotion/add_activity_success'), url('index'), 1);
			}
		} else {
			$this->load->librarys('View')->display('order_add');
		}
	}

	public function edit() {
		$id = (int) $_GET['id'];
		$info = $this->model->fetch_by_id($_GET['id']);
		if(!$info) {
			showmessage(lang('_param_error_'));
		}
		if(checksubmit('dosubmit')) {
			$_GET['discount'] = $_GET['discount'][$_GET['type']];
			$result = $this->model->update($_GET);
			if($result === false) {
				showmessage($this->model->error);
			} else {
				showmessage(lang('promotion/edit_activity_success'), url('index'), 1);
			}
		} else {
			if($info['type'] == 2) {
				$info['_sku_info_'] = $this->load->table('goods_sku')->getBySkuid($info['discount']);
			}
			$this->load->librarys('View')->assign('info',$info);
			$this->load->librarys('View')->assign('id',$id);
			$this->load->librarys('View')->display('order_edit');
		}
	}

	public function delete() {
		$ids = (array) $_GET['id'];
		if(empty($ids)) {
			showmessage(lang('_param_error_'));
		}
		$result = $this->model->delete($ids);
		if($result === false) {
			showmessage($this->model->error);
		} else {
			showmessage(lang('promotion/delete_activity_success'), url('index'), 1);
		}
	}

	public function ajax_name() {
		$id = (int) $_GET['id'];
		$name = $_GET['name'];
		if($id < 1 || empty($name)) {
			showmessage(lang('_param_error_'));
		}
		$result = $this->table->where(array('id' => $id))->setField('name', $name);
		if($result === false) {
			showmessage($this->table->getError());
		} else {
			showmessage(lang('_operation_success_'), url('index'), 1);
		}
	}
}
