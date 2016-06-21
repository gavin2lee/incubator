<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class friendlink_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('friendlink');
		$this->load->helper('attachment');
	}
	/**
	 * [index 列表]
	 */
	public function index(){
		$sqlmap = array();
		$_GET['limit'] = isset($_GET['limit']) ? $_GET['limit'] : 10;
		$friendlink = $this->load->table('misc/friendlink')->where($sqlmap)->page($_GET['page'])->limit($_GET['limit'])->order("sort ASC")->select();
        $count = $this->load->table('friendlink')->where($sqlmap)->count();
        $pages = $this->admin_pages($count, $_GET['limit']);
        $this->load->librarys('View')->assign('friendlink',$friendlink);
        $this->load->librarys('View')->assign('pages',$pages);
        $this->load->librarys('View')->display('friendlink_index');
	}
	/**
	 * [add 添加]
	 */
	public function add(){
		if(checksubmit('dosubmit')){
			if(!empty($_FILES['logo']['name'])) {
				$code = attachment_init(array('module'=>'common','path'=>'common','mid'=>$this->admin['id'],'allow_exts'=>array('bmp','jpg','png','jpeg','gif')));
				$_GET['logo'] = $this->load->service('attachment/attachment')->setConfig($code)->upload('logo');
				if(!$_GET['logo']){
					showmessage($this->load->service('attachment/attachment')->error);
				}
			}
			$result = $this->service->add($_GET);
			if(!$result){
				showmessage($this->service->error);
			}else{
				$this->load->service('attachment/attachment')->attachment($_GET['logo'],'',false);
				showmessage(lang('_operation_success_'),url('misc/friendlink/index'),'1');
			}
		}else{
			$this->load->librarys('View')->display('friendlink_edit');
		}
	}
	/**
	 * [edit 编辑]
	 */
	public function edit(){
		$info = $this->service->get_friendlink_by_id($_GET['id']);
		if(checksubmit('dosubmit')){
			if(!empty($_FILES['logo']['name'])) {
				$code = attachment_init(array('module'=>'article','path'=>'article','mid'=>$this->admin['id'],'allow_exts'=>array('bmp','gif','jpg','jpeg','png')));
				$_GET['logo'] = $this->load->service('attachment/attachment')->setConfig($code)->upload('logo');
				if(!$_GET['logo']){
					showmessage($this->load->service('attachment/attachment')->error);
				}
				$this->load->service('attachment/attachment')->attachment($_GET['logo'],$info['logo'],false);
			}
			$result = $this->service->edit($_GET);
			if(!$result){
				showmessage($this->service->error);
			}else{
				showmessage(lang('_operation_success_'),url('misc/friendlink/index'),'1');
			}
		}else{
			$this->load->librarys('View')->assign('info',$info);
        	$this->load->librarys('View')->display('friendlink_edit');
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
		showmessage(lang('_operation_success_'),url('misc/friendlink/index'),'1');
	}
	/**
	 * [ajax_edit ajax编辑]
 	 */
	public function ajax_edit(){
		$result = $this->service->ajax_edit($_GET);
		$this->ajaxReturn($result);
	}
}