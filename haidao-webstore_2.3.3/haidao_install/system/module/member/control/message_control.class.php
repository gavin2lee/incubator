<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class message_control extends cp_control {
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('member/member_message');
	}
	/* 系统消息 */
	public function index() {
		$sqlmap = array();
		$sqlmap['mid'] = $this->member['id'];
		if(isset($_GET['status'])){
			$sqlmap['status'] = (int)$_GET['status'];
		}
		$_GET['limit'] = $_GET['limit'] ? $_GET['limit'] : 15;
		$result = $this->load->table('member_message')->where($sqlmap)->order('dateline desc')->page($_GET['page'])->limit($_GET['limit'])->select();
		$count = $this->load->table('member_message')->where($sqlmap)->count();
		$pages = pages($count,$_GET['limit']);
		$SEO = seo('我的消息 - 会员中心');
		$this->load->librarys('View')->assign('result',$result);
        $this->load->librarys('View')->assign('pages',$pages);
        $this->load->librarys('View')->assign('SEO',$SEO);
        $this->load->librarys('View')->display('message');
	}
	/* 已读 */
	public function ajax_update(){
		if(!$this->member['id']){
			showmessage(lang('_param_error_'));
			return FALSE;
		}
		$result = $this->service->ajax_update($_GET['id']);
		showmessage(lang('_operation_success_'),'',1);
	}
	/* 删除 */
	public function delete(){
		if(!$this->member['id']){
			showmessage(lang('_param_error_'));
			return FALSE;
		}
		$result = $this->service->delete($_GET['id']);
		showmessage(lang('_operation_success_'),'',1,'json');
	}
}