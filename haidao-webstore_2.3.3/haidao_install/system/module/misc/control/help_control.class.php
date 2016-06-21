<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class help_control extends init_control {
	protected $service = '';
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('help');
	}
	/**
	 * [index 帮助列表]
	 */
	public function index(){
		$help = $this->service->get_index();
		$this->load->librarys('View')->assign('help',$help);
        $this->load->librarys('View')->display('help_index');
	}
	/**
	 * [edit 编辑主题]
 	 */
	public function edit(){
		$info = $this->service->get_help_by_id($_GET['id']);
		if(checksubmit('dosubmit')){
			$result = $this->service->edit($_GET);
			if(!$result){
				showmessage($this->service->error);
			}else{
				$this->load->service('attachment/attachment', 'service')->attachment($_GET['content'],$info['content']);
				showmessage(lang('_operation_success_'),url('misc/help/index'),'1');
			}
		}else{
			$this->load->librarys('View')->assign('info',$info);
			if($info['parent_id'] == 0){
				$this->load->librarys('View')->display('help_edit_top');
			}else{
				$parent = $this->service->get_parents();
				$this->load->librarys('View')->assign('parent',$parent);
        		$this->load->librarys('View')->display('help_edit_son');
			}
		}
	}
	/**
	 * [delete 删除文章]
 	 */
	public function delete(){
		$result = $this->service->delete($_GET);
		if(!$result){
			showmessage($this->service->error);
		}
		showmessage(lang('_operation_success_'),url('misc/help/index'),'1');
	}
	/**
	 * [batch 批量添加]
 	 */
	public function batch(){
		$result = $this->service->batch($_GET);
		if(!$result){
			showmessage($this->service->error);
		}else{
			showmessage(lang('_operation_success_'),url('index'),'1');
		}
	}
	public function ajax_edit(){
		 $result = $this->service->ajax_edit($_GET);
		 $this->ajaxReturn($result);
	 }
}