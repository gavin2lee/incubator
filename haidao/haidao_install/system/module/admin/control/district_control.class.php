<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class district_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('admin/district');
		$this->model = $this->load->table('admin/district');
	}

	/* 地区管理 */
	public function index() {
		$districts = $this->service->get_children();
		$this->load->librarys('View')->assign('districts',$districts);
		$this->load->librarys('View')->display('district_index');
	}

	/* 添加地区 */
	public function add() {
		$parent_id = (int)$_GET['parent_id'];
		$parent_pos = array('顶级地区');
		if ($parent_id > 0) {
			$parent_pos = $this->service->fetch_position($parent_id);
		}
		if (checksubmit('dosubmit')) {
			if (FALSE === $this->service->update($_GET)) {
				showmessage($this->service->error);
			}
			showmessage(lang('admin/add_address_success'), url('index'), 1);
		} else {
			$this->load->librarys('View')->assign('parent_id',$parent_id);
			$this->load->librarys('View')->assign('parent_pos',$parent_pos);
			$this->load->librarys('View')->display('district_add');
		}
	}

	public function edit() {
		$id = $_GET['id'];
		if ($id < 1) {
			showmessage(lang('_param_error_'));
		}
		$r = $this->service->fetch_by_id($id);
		$parent_pos = array('顶级地区');
		if ($r['parent_id'] > 0) {
			$parent_pos = $this->service->fetch_position($r['id']);
		}
		if (checksubmit('dosubmit')) {
			$params = array_merge($r, $_GET);
			if (FALSE === $this->service->update($params)) {
				showmessage($this->service->error);
			}
			showmessage(lang('admin/edit_address_success'), url('index'), 1);
		} else {
			$this->load->librarys('View')->assign('parent_pos',$parent_pos);
			$this->load->librarys('View')->assign('r',$r);
			$this->load->librarys('View')->display('district_edit');
		}
	}

	public function delete() {
		$ids = (array)$_GET['ids'];
		$result = $this->service->delete($ids);
		if ($result === false) {
			showmessage($this->service->error);
		}
		showmessage(lang('admin/delete_region_success'));
	}

	/* 更新排序 */
	public function ajax_sort() {
		$id = (int)$_GET['id'];
		$sort = (int)$_GET['sort'];
		$result = $this->model->where(array('id' => $id))->setField('sort', $sort);
		if ($result === FALSE) {
			showmessage($this->model->getError());
		} else {
			showmessage(lang('admin/edit_sort_success'), url('index'), 1);
		}
	}

	/* 更新地区名称 */
	public function ajax_name() {
		$params = array();
		$params['id'] = (int)$_GET['id'];
		$params['name'] = trim($_GET['name']);
		if (empty($params['name'])) {
			showmessage(lang('admin/region_not_exist'));
		}
		$pinyin = pinyin($params['name']);
		$params['pinyin'] = implode($pinyin, '');
		$result = $this->model->update($params);
		if ($result === FALSE) {
			showmessage($this->model->getError());
		} else {
			showmessage(lang('admin/edit_region_success'), url('index'), 1);
		}
	}

	public function ajax_district() {
		$id = (int)$_GET['id'];
		$result = (array)$this->service->get_children($id);
		if ($result) {
			foreach ($result as $key => $value) {
				$value['_child'] = $this->model->where(array('parent_id' => $value['id']))->count();
				$result[$key] = $value;
			}
		}
		$this->load->librarys('View')->assign('result',$result);
		$result = $this->load->librarys('View')->get('result');
		echo json_encode($result);
	}

}
