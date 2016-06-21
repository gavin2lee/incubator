<?php
class service_control extends cp_control
{
	public function _initialize() {
		parent::_initialize();
		if($this->member['id'] < 1) {
			redirect(url('cp/index'));
		}
		$this->service = $this->load->service('order/order_server');
		$this->db = $this->load->table('order/order_server');
		$this->order_sku_db = $this->load->table('order/order_sku'); 
		$this->return_db = $this->load->table('order/order_return');
		$this->return_service = $this->load->service('order/order_server');
		$this->refund_db = $this->load->table('order/order_refund');
		$this->track_service = $this->load->service('order/order_track');
		$this->return_log = $this->load->table('order/order_return_log');
		$this->refund_log = $this->load->table('order/order_refund_log');
		$this->load->helper('attachment');
		$this->load->helper('order/function');
	}
	/**
	 * [index 售后列表]
	 * @return [type] [description]
	 */
	public function index(){
		if(!defined('MOBILE')){
			$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 10;
			$page = ($_GET['page']) ? $_GET['page'] : 1;
			$lists = $this->service->get_servers(0,$this->member['id'],$limit,$page);
			$servers = $lists['lists'];
			$pages = pages($lists['count'], $limit);
			$this->load->librarys('View')->assign('lists',$lists);
			$this->load->librarys('View')->assign('servers',$servers);
			$this->load->librarys('View')->assign('pages',$pages);
		}
		$SEO = seo('售后服务 - 会员中心');
		$this->load->librarys('View')->assign('SEO',$SEO);
        $this->load->librarys('View')->display('sale_service');
	}

	public function ajax_service(){
		$limit = 5;
		$page = ($_GET['page']) ? $_GET['page'] : 1;
		$status = $_GET['status'] ? $_GET['status'] : 0;
		$lists = $this->service->get_servers(0,$this->member['id'],$limit,$page,$status);
		$this->load->librarys('View')->assign('lists',$lists);
        $lists = $this->load->librarys('View')->get('lists');
		echo json_encode($lists);
	}
	/**
	 * [return_refund 售后详情]
	 * @return [type] [description]
	 */
	public function return_refund(){
		if(!$_GET['id']) redirect(url('goods/index/index'));
		$SEO = seo('售后服务 - 会员中心');
		$sku = $this->order_sku_db->find($_GET['id']);
		$spec = '';
		foreach ($sku['sku_spec'] AS $sku_spec) {
			$spec .= $sku_spec['name'] .':'.$sku_spec['value'].';';
		}
		$servers = $this->db->where(array('o_sku_id' => $_GET['id']))->field('return_id,refund_id,status')->find();
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->assign('sku',$sku);
		$this->load->librarys('View')->assign('spec',$spec);
		$this->load->librarys('View')->assign('servers',$servers);
        //发起售后申请
		if(!$servers){
			$attachment_init = attachment_init(array('module' => 'member','path' => 'member', 'mid' => $this->member['id'],'allow_exts'=>array('bmp','jpg','jpeg','gif','png')));
			$price = $this->order_sku_db->where(array('id' => $_GET['id']))->getfield('real_price');
			$this->load->librarys('View')->assign('attachment_init',$attachment_init);
			$this->load->librarys('View')->assign('price',$price);
			$this->load->librarys('View')->display('return_refund');
		}else{
			$sqlmap = array();
			$sqlmap['enabled'] = 1;
			$deliverys = $this->load->table('order/delivery')->lists($sqlmap);
			foreach ($deliverys as $key => $delivery) {
				$deliverys[$delivery['identif']] = $delivery['name'];
				unset($deliverys[$key]);
			}
			$this->load->librarys('View')->assign('deliverys',$deliverys);
			//发起退货流程
			if($servers['return_id'] && !$servers['refund_id'] && $servers['status'] == 1 && $_GET['type'] == 'delivery'){
				$this->load->librarys('View')->display('return_refund_3');
			//提交退货信息
			}elseif($servers['return_id'] && $servers['refund_id']){
				$server = $this->return_db->where(array('o_sku_id' => $_GET['id']))->field('delivery_name,delivery_sn')->find();
				$track = $this->track_service->kuaidi100($server['delivery_name'],$server['delivery_sn']);
				$this->load->librarys('View')->assign('server',$server);
				$this->load->librarys('View')->assign('track',$track);
				$this->load->librarys('View')->display('return_refund_4');
			//通过退货申请
			}else{
				$this->load->librarys('View')->display('return_refund_2');
			}
		}
	}
	/**
	 * [ajax_delivery 买家返货申请]
	 * @return [type] [description]
	 */
	public function ajax_delivery(){
		$return_id = $this->return_db->where(array('o_sku_id'=>$_GET['id']))->getfield('id');
		$result = $this->return_service->return_goods($return_id,$_GET['delivery_name'],$_GET['delivery_sn']);
		$refund = $this->refund_db->where(array('o_sku_id'=>$_GET['id']))->find();
		// 创建退款日志
		$operator = get_operator();	// 获取操作者信息
		$log['refund_id']     = $refund['id'];
		$log['order_sn']      = $refund['order_sn'];
		$log['sub_sn']        = $refund['sub_sn'];
		$log['o_sku_id']      = $refund['o_sku_id'];
		$log['operator_id']   = $operator['id'];
		$log['operator_name'] = $operator['username'];
		$log['operator_type'] = $operator['operator_type'];
		$log['action']           = '用户发货完毕';
		$log['msg'] = $_GET['delivery_desc'];
		$this->refund_log->update($log);
		if(!$result){
			showmessage($this->service->error,'',0);
		}else{
			showmessage(lang('_operation_success_'),url('index'),1);
		}
	}
	public function ajax_return_cancel(){
		$result = $this->return_db->where(array('o_sku_id'=>$_GET['id']))->save(array('status' => -1));
		$return = $this->return_db->where(array('o_sku_id'=>$_GET['id']))->find();
		$this->db->where(array('return_id' => $return['id']))->setField('status',-1);
		// 写入退货日志
		$operator = get_operator();	// 获取操作者信息
		$log['return_id']     = $return['id'];
		$log['order_sn']      = $return['order_sn'];
		$log['sub_sn']        = $return['sub_sn'];
		$log['o_sku_id']      = $return['o_sku_id'];
		$log['action']        = '用户取消退货退款申请';
		$log['operator_id']   = $operator['id'];
		$log['operator_name'] = $operator['username'];
		$log['operator_type'] = $operator['operator_type'];		
		$result = $this->return_log->update($log);
		if(!$result){
			showmessage($this->service->error,'',0);
		}else{
			showmessage(lang('_operation_success_'),url('index'),1);
		}
	}
	public function ajax_refund_cancel(){
		$result = $this->refund_db->where(array('o_sku_id'=>$_GET['id']))->save(array('status' => -1));
		$refund = $this->refund_db->where(array('o_sku_id'=>$_GET['id']))->find();
		$this->db->where(array('refund_id' => $refund['id']))->setField('status',-1);
		// 创建退款日志
		$operator = get_operator();	// 获取操作者信息
		$log['refund_id']     = $refund['id'];
		$log['order_sn']      = $refund['order_sn'];
		$log['sub_sn']        = $refund['sub_sn'];
		$log['o_sku_id']      = $refund['o_sku_id'];
		$log['operator_id']   = $operator['id'];
		$log['operator_name'] = $operator['username'];
		$log['operator_type'] = $operator['operator_type'];
		$log['action']           = '用户取消退款申请';
		$this->refund_log->update($log);
		if(!$result){
			showmessage($this->service->error,'',0);
		}else{
			showmessage(lang('_operation_success_'),url('index'),1);
		}
	}
	/**
	 * [ajax_return 提交退货且退款申请]
	 * @return [type] [description]
	 */
	public function ajax_return(){
		$result = $this->service->create_return($_GET['id'] ,$_GET['amount'] ,$_GET['cause'] ,$_GET['desc'],$_GET['imgs']);
		if($result === FALSE){
			showmessage($this->service->error,'',0);
		}else{
			$this->load->service('attachment/attachment')->attachment($_GET['imgs'], '',false);
			showmessage(lang('_operation_success_'),'',1);
		}
	}
	/**
	 * [ajax_refund 提交仅退款申请]
	 * @return [type] [description]
	 */
	public function ajax_refund(){
		$sn = $this->order_sku_db->where(array('id'=>$_GET['id']))->getfield('sub_sn');
		$result = $this->service->create_refund($_GET['type'] ,$sn ,$_GET['amount'] ,$_GET['cause'] ,$_GET['desc'] ,$_GET['id'],$_GET['imgs']);
		if($result === FALSE) showmessage($this->service->error,'',0);
		$this->load->service('attachment/attachment')->attachment($_GET['imgs'], '',false);
		showmessage(lang('_operation_success_'),url('index'),1);
	}

	public function delivery_detail(){
		$sqlmap = array();
		$sqlmap['enabled'] = 1;
		$deliverys = $this->load->table('order/delivery')->lists($sqlmap);
		foreach ($deliverys as $key => $delivery) {
			$deliverys[$delivery['identif']] = $delivery['name'];
			unset($deliverys[$key]);
		}
		$track = $this->track_service->kuaidi100($_GET['delivery_name'],$_GET['delivery_sn']);
		$logo = './statics/images/deliverys/'.$_GET["delivery_name"].'.png';
		$this->load->librarys('View')->assign('deliverys',$deliverys);
		$this->load->librarys('View')->assign('track',$track);
		$this->load->librarys('View')->assign('logo',$logo);
		$this->load->librarys('View')->display('delivery_detail');
	}
}