<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'admin');
class member_control extends init_control {
    public function _initialize() {
        parent::_initialize();
        $this->service = $this->load->service('member/member');
        $this->model = $this->load->table('member/member');
    }

    public function index() {
        $sqlmap = array();
        $sqlmap['username|email|mobile'] = array("LIKE", '%'.$_GET['keyword'].'%');
        if($_GET['group_id']){
            $sqlmap['group_id'] = array('EQ',$_GET['group_id']);
        }
        $members['table'] = array(
            array(
                'name' => '会员',
                'width' => '25'
            ),
            array(
                'name' => '等级&经验',
                'width' => '20'
            ),
            array(
                'name' => '账户余额',
                'width' => '15'
            ),
            array(
                'name' => '注册&登录',
                'width' => '20'
            ),
            array(
                'name' => '状态',
                'width' => '10'
            )
        );

        $limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 20;
        $members['lists'] = $this->load->table('member/member')->where($sqlmap)->page($_GET['page'])->order('id DESC')->limit($limit)->select();
        $count = $this->load->table('member/member')->where($sqlmap)->count();
        $pages = $this->admin_pages($count, $limit);
        $member_group = $this->load->table('member/member_group')->getfield('id,name');
        array_unshift($member_group,'所有等级');
        $this->load->librarys('View')->assign('members',$members);
        $this->load->librarys('View')->assign('pages',$pages);
        $this->load->librarys('View')->assign('member_group',$member_group);
        $this->load->librarys('View')->display('member_index');
    }

    public function update() {
        $id = (int) $_GET['id'];
        $member = $this->service->fetch_by_id($id);
        if(!$member) showmessage($this->service->error);
        if(checksubmit('dosubmit')) {
            foreach ($_POST['info'] as $t => $v) {
                if(is_numeric($v['num']) && !empty($v['num'])) {
                    $v['num'] = ($v['action'] == 'inc') ? '+'.$v['num'] : '-'.$v['num'];
                    $this->service->change_account($id, $t, $v['num'], $_POST['msg']);
                }
            }
            showmessage('_operation_success_', url('index'), 1);
        } else{
            $this->load->librarys('View')->assign('member',$member);
            $this->load->librarys('View')->display('member_update');
        }
    }

    public function delete() {
        if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) {
            showmessage('_token_error_',url('index'),0);
        }
        $result = $this->service->delete_by_id($_GET['ids']);
        showmessage('_operation_success_',url('index'),1);
    }
	public function togglelock() {
        if(empty($_GET['formhash']) || $_GET['formhash'] != FORMHASH) {
           showmessage('_token_error_',url('index'),0);
        }
        $ids = (array) $_GET['ids'];
        $result = $this->service->togglelock_by_id($ids,$_GET['type']);
        showmessage('_operation_success_',url('index'),1);
    }

    public function address() {
        $_GET['mid'] = (int) $_GET['mid'];
        if((int) $_GET['has_address'] == 1){
            $lists = $this->load->service('member/member_address')->lists(array('mid' => $_GET['mid']), 20);
        }
        $this->load->librarys('View')->assign('lists',$lists);
        $this->load->librarys('View')->display('member_address');
    }
}
