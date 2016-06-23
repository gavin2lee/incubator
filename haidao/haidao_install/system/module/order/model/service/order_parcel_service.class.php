<?php
/**
 * 		发货单服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class order_parcel_service extends service {

	public function __construct() {
		$this->table = $this->load->table('order/order_parcel');
		$this->table_log = $this->load->table('order/order_parcel_log');
		$this->table_order = $this->load->table('order/order');
		$this->table_member = $this->load->table('member/member');
		$this->service_track  = $this->load->service('order/order_track');
	}

	/**
	 * 创建发货单
	 * @param  array 	$sub 子订单信息 (必传)
	 * @return [boolean]
	 */
	public function create($sub) {
		// 获取主订单信息
		$main_order = $this->table_order->where(array('sn' => $sub['order_sn']))->find();
		if (!$main_order) {
			$this->error = lang('order/parent_order_sn_not_exist');
			return FALSE;
		}
		$member_name = $this->table_member->getFieldById($main_order['buyer_id'] ,'username');
		// 生成发货单
		$data = array();
		$data['order_sn'] = $sub['order_sn'];
		$data['sub_sn']   = $sub['sub_sn'];
		$data['member_name']    = $member_name;
		$data['address_name']   = $main_order['address_name'];
		$data['address_mobile'] = $main_order['address_mobile'];
		$data['address_detail'] = $main_order['address_detail'];
		$data['delivery_name']  = $sub['delivery_name'];
		$data['system_time']    = time();
		$result = $this->table->update($data);
		if (!$result) {
			$this->error = $this->table->getError();
			return FALSE;
		}
		// 发货单日志
		$operator = get_operator();
		$data = array();
		$data['parcel_id']     = $result;
		$data['order_sn']      = $sub['order_sn'];
		$data['sub_sn']        = $sub['sub_sn'];
		$data['buyer_id']      = $sub['buyer_id'];
		$data['member_name']   = $member_name;
		$data['action']        = '生成发货单';
		$data['msg']           = '生成发货单，待发货...';
		$data['operator_id']   = $operator['id'];
		$data['operator_name'] = $operator['username'];
		$data['system_time']   = time();
		$this->table_log->update($data);
		return $result;
	}

	/**
	 * 更改配送状态
	 * @param  array  $params	 配送相关信息
	 					$params[id] - 发货单主键id (必传)
	 					$params[status] - 配送状态 (-1：配送失败 0：待配货 1：配送中 2：配送完成 , 必传)
	 * @return [boolean]
	 */
	public function complete_parcel($params){
		if((int)$params['id'] < 1){
			$this->error = lang('order/shipment_sn_id_not_exist');
			return FALSE;
		}
		$data = array();
		$data['id'] = $params['id'];
		$data['status'] = $params['status'];
		$result = $this->table->update($data);
		if($result === FALSE){
			$this->error = $this->table->getError();
			return FALSE;
		}
		$parcel = $this->table->fetch_by_id($params['id']);
		$parcel['parcel_status'] = $data['status'];
		/* 添加日志 */
		if(!$this->table_log->add_log($parcel,$params['log'])){
			$this->error = $this->table_log->getError();
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * 删除发货单
	 * @id  array 	 发货单主键id (必传)
	 * @return [boolean]
	 */
	public function delete_parcel($id) {
	 	if((int)$id < 1){
	 		$this->error = lang('order/shipment_sn_id_not_exist');
	 		return FALSE;
	 	}
	 	$result = $this->table->where(array('id' => $id))->delete();
	 	if(!$result){
	 		$this->error = $this->table->getError();
	 		return FALSE;
	 	}
	 	/* 删除日志 */
	 	if($this->table_log->where(array('parcel_id'=>$id))->delete() === FALSE){
	 		$this->error = $this->table->getError();
	 		return FALSE;
	 	}
	 	return TRUE;
	}
	/**
	 * [order_parcel_import 导入发货单]
	 * @param  [type] $params [description]
	 * @return [type]         [description]
	 */
	public function order_parcel_import($params){
		$data = $this->table->create($params);
		return $this->table->add($params);
	}
}