<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
Core::load_class('init', 'goods');
class order_control extends init_control {

    public function _initialize() {
        parent::_initialize();
        if($this->member['id'] < 1) {
			redirect(url('cp/index',array('url_forward'=>urlencode($_SERVER['REQUEST_URI']))));
		}
        $this->table = $this->load->table('order/order');
        $this->table_sub = $this->load->table('order/order_sub');
        $this->service = $this->load->service('order/order');
        $this->service_sub = $this->load->service('order/order_sub');
        $this->service_track = $this->load->service('order/order_track');
        helper('order/function');
    }

    /* 我的订单 */
    public function index() {
    	// 查询条件
		$sqlmap = array();
		$sqlmap = $this->service->build_sqlmap($_GET);
		$sqlmap['buyer_id'] = $this->member['id'];
        if (isset($_GET['sn'])) $sqlmap['sn'] = array('LIKE','%'.$_GET['sn'].'%');
		if (!isset($_GET['type'])) $sqlmap['status'] = array('IN','1,2');
		$limit  = (isset($_GET['limit'])) ? $_GET['limit'] : 5;
        $orders = $this->table->where($sqlmap)->page($_GET['page'])->order('id DESC')->limit($limit)->select();
        $count  = $this->table->where($sqlmap)->count();
        $pages  = pages($count,$limit);
        $SEO = seo('我的订单 - 会员中心');
        $this->load->librarys('View')->assign('orders',$orders);
        $this->load->librarys('View')->assign('SEO',$SEO);
        $this->load->librarys('View')->assign('pages',$pages);
        $this->load->librarys('View')->display('my_order');
    }

    /* 订单详情 */
    public function detail() {
        $o_d_id = remove_xss($_GET['o_d_id']);
        $detail = $this->service_sub->sub_detail($_GET['sub_sn'] ,$o_d_id);
        if (!$detail) showmessage(lang('order/order_not_exist'));
        //更新跟踪物流
        if($detail['delivery_status'] > 0 && $o_d_id > 0){
            $this->service_track->update_api100($detail['sub_sn'],$o_d_id);
            $detail = $this->service_sub->sub_detail($_GET['sub_sn'] ,$o_d_id);
        }
        $detail['_member'] = $this->load->table('member/member')->find($detail['buyer_id']);
        $detail['_main'] = $this->table->detail($detail['order_sn'])->subs(FALSE ,FALSE, FALSE)->output();
        // 是否显示子订单号信息
        $detail['_showsubs'] = (count($detail['_main']['_subs']) > 1) ? TRUE : FALSE;
        $setting = $this->load->service('admin/setting')->get_setting();
        $SEO = seo('订单详情 - 会员中心');
        $this->load->librarys('View')->assign('detail',$detail);
        $this->load->librarys('View')->assign('SEO',$SEO);
        $this->load->librarys('View')->assign('setting',$setting);
        $this->load->librarys('View')->display('order_detail');
    }

    /* 取消订单 */
    public function cancel() {
        if (checksubmit('dosubmit')) {
            $sub_sn = remove_xss($_GET['sub_sn']);
            $order = $this->table_sub->field('buyer_id')->where(array('sub_sn' => $sub_sn))->find();
            if ($order['buyer_id'] != $this->member['id']) {
                showmessage(lang('member/no_promission_operate_order'));
            }
            $result = $this->service_sub->set_order($sub_sn ,$action = 'order',$status = 2 ,array('msg'=>'用户取消订单','isrefund' => 1));
            if (!$result) showmessage($this->service_sub->error);
            showmessage(lang('order/cancel_order_success'),'',1,'json');
        } else {
            showmessage(lang('_error_action_'));
        }
    }

    /* 放入回收站 */
    public function recycle() {
        if (checksubmit('dosubmit')) {
            $sn = remove_xss($_GET['sn']);
            $order = $this->table->field('member_id')->where(array('sn' => $sn))->find();
            if ($order['member_id'] != $this->member['id']) showmessage(lang('member/no_promission_operate_order'));
            $result = $this->service->set_order($sn ,$action = 'order',$status = 3 ,array('msg'=>'订单放入回收站'));
            if (!$result) showmessage($this->service->error);
            showmessage(lang('已放入回收站'),'',1,'json');
        } else {
            showmessage(lang('_error_action_'));
        }
    }

    /* 删除订单 */
    public function delete_sn() {
        if (checksubmit('dosubmit')) {
            $sn = remove_xss($_GET['sn']);
            $order = $this->table->field('member_id')->where(array('sn' => $sn))->find();
            if ($order['member_id'] != $this->member['id']) showmessage(lang('member/no_promission_operate_order'));
            $result = $this->service->set_order($sn ,'order', 4 ,array('msg'=>'用户删除订单'));
            if (!$result) showmessage($this->service->error);
            showmessage(lang('删除订单成功'),'',1,'json');
        } else {
            showmessage(lang('_error_action_'));
        }
    }

    /* 确认收货 */
    public function finish() {
        if (checksubmit('dosubmit')) {
            $sub_sn = remove_xss($_GET['sub_sn']);
            $order = $this->table_sub->field('buyer_id')->where(array('sub_sn' => $sub_sn))->find();
            if ($order['buyer_id'] != $this->member['id']) showmessage(lang('member/no_promission_operate_order'));
            $data = array();
            $data['msg'] = '确认订单商品收货';
            $data['o_delivery_id'] = remove_xss($_GET['o_d_id']);
            $result = $this->service_sub->set_order($sub_sn ,'finish',1 ,$data);
            if (!$result) showmessage($this->service_sub->error);
            showmessage(lang('确认订单商品收货成功'),'',1,'json');
        } else {
            showmessage(lang('_error_action_'));
        }
    }

    /* wap查看物流 */
    public function delivery() {
        $o_d_id = (int) $_GET['o_d_id'];
        $order_delivery = $this->load->table('order/order_delivery')->where(array('id' => $o_d_id))->find();
        if (!$order_delivery) return FALSE;
        //更新物流跟踪
        if($o_d_id > 0){
            $this->service_track->update_api100($order_delivery['sub_sn'],$o_d_id);
        }
        $info = array();
        $info['delivery'] = $this->load->table('order/delivery')->where(array('id' => $order_delivery['delivery_id']))->find();
        $info['tracks'] = $this->service_track->get_tracks_by_sn($order_delivery['sub_sn']);
        $SEO = seo('查看物流 - 会员中心');
        $this->load->librarys('View')->assign('SEO',$SEO);
        $this->load->librarys('View')->assign('order_delivery',$order_delivery);
        $this->load->librarys('View')->assign('info',$info);
        $this->load->librarys('View')->display('track');
    }

    /* 获取订单列表 */
    public function get_orders() {
        // 查询条件
        $sqlmap = array();
        $sqlmap = $this->service->build_sqlmap($_GET);
        $sqlmap['buyer_id'] = $this->member['id'];
        if (isset($_GET['sn'])) $sqlmap['sn'] = array('LIKE','%'.$_GET['sn'].'%');
        if (!isset($_GET['type'])) $sqlmap['status'] = array('IN','1,2');
        $limit  = (isset($_GET['limit'])) ? $_GET['limit'] : 10;
        $data['orders'] = $this->table->where($sqlmap)->page($_GET['page'])->order('id DESC')->limit($limit)->select();
        $data['count']  = $this->table->where($sqlmap)->count();
        $data['pages']  = pages($count,$limit);
        $this->load->librarys('View')->assign('data',$data);
        $data = $this->load->librarys('View')->get('data');
        echo json_encode($data);
    }
}