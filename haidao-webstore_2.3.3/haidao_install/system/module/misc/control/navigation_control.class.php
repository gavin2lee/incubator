<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class navigation_control extends init_control {
	protected $service = '';
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('navigation');
	}
	/**
	 * [index 文章列表]
 	 */
	public function index(){
		$navigation = $this->service->lists(array('order'=>'sort ASC'));
		$this->load->librarys('View')->assign('navigation',$navigation);
        $this->load->librarys('View')->display('navigation_index');
	}
	/**
	 * [add 添加]
 	 */
	public function add(){
		if(checksubmit('dosubmit')){
			$result = $this->service->add($_GET);
			if(!$result){
				showmessage($this->service->error);
			}else{
				showmessage(lang('_operation_success_'),url('misc/navigation/index'),'1');
			}
		}else{
			$this->load->librarys('View')->display('navigation_edit');
		}		
	}
	/**
	 * [ajax_edit ajax编辑]
	 */
	public function ajax_edit(){
		$result = $this->service->ajax_edit($_GET);
		$this->ajaxReturn($result);
	}
	/**
	 * [edit 编辑]
	 */
	public function edit(){
		if(checksubmit('dosubmit')){
			$result = $this->service->edit($_GET);
			if(!$result){
				showmessage($this->service->error);
			}else{
				showmessage(lang('_operation_success_'),url('misc/navigation/index'),'1');
			}
		}else{
			$info = $this->service->get_navigation_by_id($_GET['id']);
			$this->load->librarys('View')->assign('info',$info);
        	$this->load->librarys('View')->display('navigation_edit');
		}
	}
	/**
	 * [delete 删除]
	 */
	public function delete(){
		$result = $this->service->delete($_GET);
		if(!$result){
			showmessage($this->service->error);
		}
		showmessage(lang('_operation_success_'),url('misc/navigation/index'),'1');
	}
}