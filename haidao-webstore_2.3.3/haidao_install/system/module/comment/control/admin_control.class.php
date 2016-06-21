<?php
Core::load_class('init', 'admin');
class admin_control extends init_control
{
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('comment/comment');
	}

	public function index() {
		$sqlmap = array();
		// $sqlmap['status'] = 1;
		$_map = array();
		if(isset($_GET['starttime']) && !empty($_GET['starttime'])) {
			$_map[] = array("GT", strtotime($_GET['starttime']));
		}
		if(isset($_GET['endtime']) && !empty($_GET['endtime'])) {
			$_map[] = array("LT", strtotime($_GET['endtime']));
		}
		if($_map) $sqlmap['datetime'] = $_map;
		if(isset($_GET['keyword']) && !empty($_GET['keyword'])) {
			$sqlmap['username'] = array("LIKE", "%".$_GET['keyword']."%");
		}
		if(isset($_GET['is_shield'])) {
			$sqlmap['is_shield'] = $_GET['is_shield'];
		}
		$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 20;
		$result = $this->service->lists($sqlmap, $limit, 'id DESC', $_GET['page']);
		$result['pages'] = $this->admin_pages($result['count'], $limit);
		$this->load->librarys('View')->assign($result,$result);
		$this->load->librarys('View')->display('comment_list');
	}


	public function reply() {
		if(checksubmit('dosubmit')) {
			$result = $this->service->reply($_GET['id'], $_GET['reply_content']);
			if($result === false) {
				showmessage($this->service->error);
			}
			showmessage(lang('comment/reply_success'), url('index'), 1);
		} else {
			showmessage(lang('_error_action_'));
		}
	}

	public function change_status(){
		$result = $this->service->change_status($_GET['id']);
		if(!$result){
			showmessage($this->service->error,'',0);
		}else{
			showmessage(lang('_operation_success_'),'',1);
		}
	}

	public function delete() {
		$ids = (array) $_GET['id'];
		$this->service->delete($ids);
		showmessage(lang('comment/estimate_delete_success'), url('index'), 1);
	}
}