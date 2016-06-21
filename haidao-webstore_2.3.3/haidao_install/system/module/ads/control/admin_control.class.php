<?php
/**
 *	  后台广告设置控制器
 *	  [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *	  This is NOT a freeware, use is subject to license terms
 *
 *	  http://www.haidao.la
 *	  tel:400-600-2042
 */
Core::load_class('init', 'admin');
class admin_control extends init_control {

	public function _initialize() {
		parent::_initialize();
		$this->model = $this->load->table('adv');
		$this->service = $this->load->service('adv');
		
		$this->position_service = $this->load->service('adv_position');
		$this->position_model = $this->load->table('adv_position');
		
		$this->attachment_service = $this->load->service('attachment/attachment');
		$this->attachment_service->setConfig(authcode(serialize(array('module'=>'common','path' => 'common','mid' => 1,'allow_exts' => array('gif','jpg','jpeg','bmp','png'))), 'ENCODE'));
	}

	/*广告=======================================================*/
	/**
	 * 获取广告方式列表
	 */
	public function index() {
		$sqlmap = array();
		$count = $this->model->where($sqlmap)->count();
		$ads = $this->model->where($sqlmap)->page($_GET['page'])->limit(10)->select();
		$pages = $this->admin_pages($count, 10);
		$this->load->librarys('View')->assign('ads',$ads);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('index');
	}

	/**
	 * 添加广告
	 */
	public function add() {
		$position = $this->position_service->getposition();
		if(!$position)showmessage(lang('ads/_no_advposition_'),url('position_add'),0);
		$position_format = format_select_data($position);
		if (checksubmit('dosubmit')) {
			if(!empty($_FILES['content_pic']['name'])) {
				$_GET['content_pic'] = $this->attachment_service->upload('content_pic');
				if(!$_GET['content_pic']){
					showmessage($this->attachment_service->error);
				}
			}
			$_GET['content'] = $_GET['type'] == 0 ? $_GET['content_pic'] : $_GET['content_text'];
			$_GET['content'] = is_null($_GET['content']) ? '' : $_GET['content'];
			$r = $this->service->save($_GET);
			if($r == FALSE)showmessage($this->model->getError(), url('add'), 0);
			$this->attachment_service->attachment($_GET['content_pic'],'',false);
			showmessage(lang('ads/_update_adv_success_'), url('index'), 1);
		} else {
			$this->load->librarys('View')->assign('position',$position);
			$this->load->librarys('View')->assign('position_format',$position_format);
			$this->load->librarys('View')->display('update');
		}
	}

	/**
	 * 编辑广告
	 */
	public function edit() {
		$position = $this->position_service->getposition();
		$position_format = format_select_data($position);
		$data = $this->service->fetch_by_id($_GET['id']);
		if (checksubmit('dosubmit')) {
			if(!empty($_FILES['content_pic']['name'])) {
				$_GET['content_pic'] = $this->attachment_service->upload('content_pic');
				if(!$_GET['content_pic']){
					showmessage($this->attachment_service->error);
				}
			}
			$_GET['content'] = $_GET['type'] == 0 ? $_GET['content_pic'] : $_GET['content_text'];
			$_GET = array_filter($_GET);
			$r = $this->service->save($_GET);
			$this->attachment_service->attachment($_GET['content_pic'],$data['content'],false);
			showmessage(lang('ads/_update_adv_success_'), url('index'), 1);
		} else {
			$this->load->librarys('View')->assign('position',$position);
			$this->load->librarys('View')->assign('position_format',$position_format);
			extract($data);
			$this->load->librarys('View')->assign($data,$data);
			$this->load->librarys('View')->display('update');
		}
	}

	/**
	 * 删除广告
	 */
	public function del() {
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$data = $this->service->fetch_by_id($_GET['id']);
		$this->attachment_service->attachment('',$data['content'],false);
		$this->model->where(array('id' => array('IN', (array)$_GET['id'])))->delete();
		showmessage(lang('ads/_del_adv_success_'), url('index'), 1);
	}

	/**
	 * 编辑标题
	 */
	public function save_title() {
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$this->service->save(array('id' => $_GET['id'], 'title' => $_GET['title']), FALSE);
		showmessage(lang('ads/_update_adv_success_'), url('index'), 1);
	}


	/*广告位=======================================================*/
	/**
	 * 获取广告方式列表
	 */
	public function position_index() {
		$sqlmap = array();
		$count = $this->position_model->where($sqlmap)->count();
		$position = $this->position_model->where($sqlmap)->page($_GET['page'])->limit(10)->select();
		$pages = $this->admin_pages($count, 10);
		$this->load->librarys('View')->assign('position',$position);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('position_index');
	}

	/**
	 * 启用禁用广告位
	 */
	public function ajax_status() {
		$id = $_GET['id'];
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		if ($this->position_service->change_status($id)) {
			showmessage(lang('ads/_status_success_'), '', 1);
		} else {
			showmessage(lang('ads/_status_error_'), '', 0);
		}
	}

	/**
	 * 删除广告位
	 */
	public function position_del() {
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		if($this->model->where(array('position_id' => array('IN', (array)$_GET['id'])))->count() > 0)showmessage(lang('ads/no_delete_advposition_'), url('position_index'), 0);
		$position = $this->position_service->fetch_by_id($_GET['id']);
		$this->attachment_service->attachment('',$position['defaultpic'],false);
		$this->position_model->where(array('id' => array('IN', (array)$_GET['id'])))->delete();
//		$this->model->where(array('position_id' => array('IN', (array)$_GET['id'])))->delete();
		showmessage(lang('ads/_del_adv_position_success_'), url('position_index'), 1);
	}

	/**
	 * 添加广告位
	 */
	public function position_add() {
		if (checksubmit('dosubmit')) {
			if(!empty($_FILES['defaultpic']['name'])) {
				$_GET['defaultpic'] = $this->attachment_service->upload('defaultpic');
				if(!$_GET['defaultpic']){
					showmessage($this->attachment_service->error);
				}
			}
			$r = $this->position_service->save($_GET);
			if(!$r)showmessage($this->model->getError, url('position_add'), 0);
			$this->attachment_service->attachment($_GET['defaultpic'],'',false);
			showmessage(lang('ads/_update_adv_position_success_'), url('position_index'), 1);
		} else {
			$status = 1;
			$this->load->librarys('View')->display('position_update');
		}
	}

	/**
	 * 编辑广告位
	 */
	public function position_edit() {
		$position = $this->position_service->fetch_by_id($_GET['id']);
		if (checksubmit('dosubmit')) {
			if(!empty($_FILES['defaultpic']['name'])) {
				$_GET['defaultpic'] = $this->attachment_service->upload('defaultpic');
				if(!$_GET['defaultpic']){
					showmessage($this->attachment_service->error);
				}
			}
			$r = $this->position_service->save($_GET);
			$this->attachment_service->attachment($_GET['defaultpic'],$position['defaultpic'],false);
			showmessage(lang('ads/_update_adv_position_success_'), url('position_index'), 1);
		} else {
			extract($position);
			$this->load->librarys('View')->assign($position,$position);
			$this->load->librarys('View')->display('position_update');
		}
	}

	/**
	 * 编辑标题
	 */
	public function position_save_name() {
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$this->position_service->save(array('id' => $_GET['id'], 'name' => $_GET['name']));
		showmessage(lang('ads/_update_adv_position_success_'), url('index'), 1);
	}

}
