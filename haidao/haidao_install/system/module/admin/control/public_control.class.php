<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
define('IN_ADMIN', TRUE);
class public_control extends init_control
{
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('admin');
	}
    
    /* 管理登录 */
	public function login() {		
        if(checksubmit('dosubmit')) {
            $result = $this->service->login($_GET['username'], $_GET['password']);
            if($result === FALSE) {
                showmessage($this->service->error);
            } else {
                redirect(url('index/index'));
            }
        } else {
        	$this->load->librarys('View')->display('login');
        }
	}
    
    public function logout() {
        $this->service->logout();
        redirect(url('public/login'));
    }
	
	//添加自定义菜单
	public function ajax_menu_add(){
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$_m = $_c = $_a = '';
		$data = array();
		extract($_GET,EXTR_IF_EXISTS);
		$data['admin_id'] = ADMIN_ID;
		$data['url'] = url("$_m/$_c/$_a");
		$data['title'] = $this->load->table('node')->where(array('m'=>$_m,'c'=>$_c,'a'=>$_a))->getField('name');
		if($this->load->table('admin_menu')->where($data)->count() == 0 ){
			$this->load->table('admin_menu')->update($data);
			showmessage(lang('admin/add_menu_success'),'',1);
		}
		showmessage(lang('admin/menu_exist'),'',0);
	}
	//删除自定义菜单
	public function ajax_diymenu_del(){
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$ids = '';
		extract($_GET,EXTR_IF_EXISTS);
		$ids = explode(',', $ids);
		$this->load->table('admin_menu')->where(array('id'=>array('IN',$ids)))->delete();
	}
	//获取自定义菜单
	public function ajax_menu_refresh(){
		if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) showmessage('_token_error_');
		$menus = $this->load->service('admin/admin_menu')->setAdminid(ADMIN_ID)->getAll();
		$html = '';
		foreach($menus as $k=>$v){
			$html.="<li><a href='{$v["url"]}'>{$v["title"]}</a></li> ";
		}
		$this->load->librarys('View')->assign('html',$html);
		$html = $this->load->librarys('View')->get('html');
		echo json_encode($html);
		
	}
}