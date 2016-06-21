<?php
Core::load_class('init', 'admin');
class type_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->db = $this->load->table('type');
		$this->service = $this->load->service('type');
		$this->spec_service = $this->load->service('spec');
	}
	/**
	 * [lists 类型列表]
	 * @return [type] [description]
	 */
	public function index(){
		$sqlmap = array();
		$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 20;
		$type = $this->service->get_lists($_GET['page'],$limit);
		$count = $this->db->where($sqlmap)->count();
		$pages = $this->admin_pages($count, $limit);
		$this->load->librarys('View')->assign('type',$type);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('type_list');
	}
	/**
	 * [add 添加类型]
	 */
	public function add(){
		if(checksubmit('dosubmit')) {
			$result = $this->service->add_type($_GET);
			if(!$result){
				showmessage($this->service->error);
			}else{
				showmessage(lang('_operation_success_'),url('index'));
			}
		}else{
			$specs = $this->spec_service->get_spec_name();
			$this->load->librarys('View')->assign('specs',$specs);
			$this->load->librarys('View')->display('type_edit');
		}
	}
	/**
	 * [edit 类型编辑]
	 * @return [type] [description]
	 */
	public function edit(){
		if(checksubmit('dosubmit')) {
			$result = $this->service->add_type($_GET);
			if(!$result){
				showmessage($this->service->error);
			}else{
				showmessage(lang('_operation_success_'),url('index'));
			}
		}else{
			$specs = $this->spec_service->get_spec_name();
			$type = $this->service->get_type_by_id($_GET['id']);
			$pop = $this->service->get_pop_by_id($_GET['id']);
			$type_spec = $this->service->get_spec_by_id($_GET['id']);
			$this->load->librarys('View')->assign('specs',$specs);
			$this->load->librarys('View')->assign('type',$type);
			$this->load->librarys('View')->assign('pop',$pop);
			$this->load->librarys('View')->assign('type_spec',$type_spec);
			$this->load->librarys('View')->display('type_edit');
		}
	}
	/**
	 * [delete 删除类型]
	 * @return [type] [description]
	 */
	public function delete(){
		$result = $this->service->delete_type($_GET['id']);
		if(!$result){
			showmessage($this->service->error);
		}else{
			showmessage(lang('_operation_success_'),url('index'),1);
		}
	}
	/**
	 * [ajax_status 规格列表内更改规格状态]
	 */
	public function ajax_status(){
		$result = $this->service->change_status($_GET['id']);
		if(!$result){
			showmessage($this->service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_fail_'),'',1,'','json');
		}
	}
	/**
	 * [ajax_sort 改变排序]
	 */
	public function ajax_sort(){
		$result = $this->service->change_sort($_GET);
		if(!$result){
			showmessage($this->service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_fail_'),'',1,'','json');
		}
	}
	/**
	 * [ajax_name ajax更改名称]
	 * @return [type] [description]
	 */
	public function ajax_name(){
		$result = $this->service->change_name($_GET);
		if(!$result){
			showmessage($this->service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_fail_'),'',1,'','json');
		}
	}
	/**
	 * [edit_pop 商品属性编辑弹窗页]
	 * @return [type] [description]
	 */
	public function edit_pop(){
		$this->load->librarys('View')->display('type_pop_edit');
	}
}