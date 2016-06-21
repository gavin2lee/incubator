<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class member_group_control extends init_control {
    public function _initialize() {
        parent::_initialize();
        $this->service = $this->load->service('member_group');
        $this->model = $this->load->table('member_group');
    }

    public function index() {
        $sqlmap = array();
        $groups = $this->model->where($sqlmap)->page($_GET['page'])->limit(10)->order("id ASC")->select();
        $count = $this->model->where($sqlmap)->count();
        $pages = $this->admin_pages($count, 10);
        $this->load->librarys('View')->assign('groups',$groups);
        $this->load->librarys('View')->assign('pages',$pages);
        $this->load->librarys('View')->display('member_group');
    }

    public function add(){
        if(checksubmit('dosubmit')) {
            $result = $this->service->add($_GET);
            if(!$result) {
                showmessage($this->service->error);
            }
            showmessage('_operation_success_', url('index'), 1);
        } else {
            $this->load->librarys('View')->display('member_group_add');
        }
    }

    public function edit() {
        $r = $this->service->fetch_by_id($_GET['id']);
        if(!$r) showmessage($this->service->error);
	    if(checksubmit('dosubmit')) {
           $result = $this->service->edit($_GET);
            if(!$result) {
                showmessage($this->service->error);
            }
            showmessage('_operation_success_', url('index'), 1);
	    } else {
            $this->load->librarys('View')->assign('r',$r);
            $this->load->librarys('View')->display('member_group_edit');
		}
    }

    public function delete() {
        if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) {
            showmessage('_token_error_',url('index'));
        }
        $ids = (array) $_GET['id'];
        $result = $this->service->delete_by_id($ids);
        showmessage('_operation_success_',url('index'),1);
    }
}
