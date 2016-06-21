<?php
/**
 * 		物流服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class delivery_service extends service {

	public function __construct() {
		$this->table = $this->load->table('order/delivery');
		$this->table_district = $this->load->table('order/delivery_district');
		$this->table_delivery = $this->load->table('order/order_delivery');
	}

	/**
	 * [添加|编辑]物流
	 * @param array $params - 物流相关参数 (必传参数)
					$params[insure_enabled] - 是否开启物流保价(int ,1 开启 2 不开启)
					$params[insure] - 报价金额(float)
					$params[delivery_district] - 物流地区配置(array)
	 * @return [boolean]
	 */
	public function update($params = array(),$extra = FALSE) {
		if ($params['insure_enabled'] == 1) {
			if ((float) $params['insure'] <= 0) {
				$this->error = lang('order/logistics_insurance_amount_empty');
				return FALSE;
			}
		} else {
			$params['insure'] = 0;
		}
		$params['systime'] = time();
		if($params['id'] && $extra == TRUE){
			$data = $this->table->create($params);
			$result = $this->table->add($data);
		}else{
			$result = $this->table->update($params);
		}
		if (!$result) {
			$this->error = $this->table->getError();
			return FALSE;
		}
		// 设置物流地区
		if ($params['delivery_district']) {
			$delivery_id = (isset($params['id'])) ? $params['id'] : $result;
			$this->_update_delivery_district($params['delivery_district'], $delivery_id);
		}
		return $result;
	}

	/**
	 * [添加|修改] 物流地区配置
	 * @param array $delivery_district 物流地区参数
	 * @param int 	$delivery_id 物流ID
	 * @return [boolean]
	 */
	private function _update_delivery_district($delivery_district = array(), $delivery_id = 0) {
		$params = array();
		$params = $delivery_district;
		$delivery_id = (int) $delivery_id;
		if ($delivery_id < 1 || empty($params)) {
			$this->error = lang('order/logistics_id_empty');
			return FALSE;
		}
		
		$params['add'] = (array) $params['add'];
		$params['edit'] = (array) $params['edit'];
		$params['delete'] = (array) $params['delete'];
		
		$delivery_district = $this->table_district->where(array('delivery_id' => $delivery_id))->getField('id', TRUE);
		
		if ($params['add']) {	// 添加记录
			$add_array = array();
			foreach ($params['add'] as $k => $v) {
				$add_array[] = array(
					'delivery_id' => $delivery_id,
					'price'       => sprintf("%.2f", $v['price']),
					'district_id' => $v['district_id'],
					'sort'        => 100
				);
			}
			if($add_array) {
				$this->table_district->addAll($add_array);
			}
		}
		
		if ($params['edit']) {	// 编辑记录
			$edit_array = array();
			foreach ($params['edit'] as $k => $v) {
				if(in_array($k, (array) $params['delete'])) continue;				
				$edit_array = array(
					'id'          => $k,
					'delivery_id' => $delivery_id,
					'price'       => sprintf("%.2f", $params['edit'][$k]['price']),
					'district_id' => $v['district_id'],
					'sort'        => 100
				);
				$this->table_district->update($edit_array);
			}			
		}
		
		// 通过差集计算出需要删除的
		$edit_ids = array_keys($params['edit']);
		$del_array = array_diff($delivery_district, $edit_ids);
		
		if($params['delete']) $del_array = array_merge($del_array, $params['delete']);
		if($del_array) {
			$this->del_district($del_array);
		}
		return TRUE;
	}

	/**
	 * 根据物流ID获取物流信息
	 * @param int $id 物流主键ID
	 * @return [result]
	 */
	public function get_by_id($id = 0) {
		$id = (int) $id;
		if ($id == 0) {
			$this->error = lang('order/logistics_id_require');
			return FALSE;
		}
		$result = $this->table->find($id);
		// 获取物流地区配置
		$result['_districts'] = $this->table_district->where(array('delivery_id' => $id))->order("`id` ASC")->select();
		return $result;
	}

	/**
	 * 根据物流ID更改物流字段值
	 * @param int 	 $id 物流地区ID(必传)
	 * @param string $field 字段名(必传)
	 * @param string $val 要更改的值(必传)
	 * @return [boolean]
	 */
	public function update_field_by_id($id ,$field ,$val) {
		$id    = (int) $id;
		$field = (string) remove_xss($field);
		$val   = (string) remove_xss($val);
		if ($id < 1) {
			$this->error = lang('order/logistics_id_empty');
			return FALSE;
		}
		if ($field == '') {
			$this->error = lang('order/edit_field_empty');
			return FALSE;
		}
		if ($val == '') {
			$this->error = lang('order/edit_value_empty');
			return FALSE;
		}
		$delivery = $this->get_by_id($id);
		if (!$delivery) {
			$this->error = lang('order/logistics_not_exist');
			return FALSE;
		}
		$data            = array();
		$data[$field]    = $val;
		$data['systime'] = time();
		return $this->table->where(array('id' => $id))->setField($data);
	}

	/**
	 * 删除物流(支持批量操作)
	 * @param array	 $ids 物流主键ids (必传)
	 * @return [boolean]
	 */
	public function deletes($ids = array()) {
		if (!is_array($ids)) {
			$this->error = lang('order/delete_parame_error');
			return FALSE;
		}
		$ids = array_filter($ids);
		if (empty($ids)){
			$this->error = lang('order/delete_logistics_id_empty');
			return FALSE;
		}
		$ids_str = implode(',',$ids);
		$result = $this->table->delete($ids_str);
		if ($result === false) {
			$this->error = lang('order/delete_logistics_error');
			return FALSE;
		}
		// 删除关联的delivery_district相关记录
		$sqlmap = array();
		$sqlmap['delivery_id'] = array('IN' ,$ids);
		$this->table_district->where($sqlmap)->delete();
		return TRUE;
	}

	/**
	 * 根据地区ID&skuids获取商家物流信息
	 * @param int	 $district_id 地区表主键id (必传)
	 * @param string $skuids 	  商品skuids (必传，格式 ：skuid1[,数量1];[skuid2[,数量2];]...)
	 * @return [result]
	 */
	public function get_deliverys($district_id = 0 ,$skuids = '') {
		$district_id = (int) $district_id;
		// 获取商家id
		$sku_arr = array_filter(explode(';', $skuids));
		$sku_ids = $nums = $arr = array();
		foreach ($sku_arr as $k => $val) {
			$arr = explode(',', $val);
			$sku_ids[] = $arr[0];
			$nums[$arr[0]] = abs((int) $arr[1]);
		}
		$sellerids = array();
		foreach ($sku_ids as $k => $skuid) {
			$sellerids[$k] = (int) $this->load->table('goods/goods_sku')->where(array('sku_id' =>$skuid))->getField('seller_id');
		}
		$sellerids = array_unique($sellerids);
		if (empty($sellerids)) {
			$this->error = lang('order/merchant_id_empty');
			return FALSE;
		}
		// 当前地区id的所有父级地区
		$districtids = $this->load->service('admin/district')->fetch_position($district_id ,'id');
		if (!$districtids) {
			$this->error = lang('order/area_not_exist');
			return FALSE;
		}
		$deliverys = $sqlmap = $infos = $arr = array();
		foreach ($sellerids as $k => $sellerid) {
			foreach ($districtids as $key => $id) {
				$sqlmap['_string'] = "FIND_IN_SET($id, `district_id`)";
				$infos[] = $this->table_district->where($sqlmap)->getField('delivery_id ,id ,price', TRUE);
			}
			foreach ($infos as $val) {
				foreach ($val as $k => $v) {
					$map = array();
					$map['enabled'] = 1;
					$map['id'] = $v['delivery_id'];
					$v['_delivery'] = $this->table->where($map)->find();
					if ($v['_delivery'] == false) {
						unset($val[$k]);
						continue;
					}
					$arr[$k] = $v;
				}
			}
			$deliverys[$sellerid] = $arr;
		}
		return $deliverys;
	}

	/**
	 * 删除物流地区(支持批量操作)
	 * @param mixed $ids 物流地区ids (int||array ,必传)
	 * @return [boolean]
	 */
	public function del_district($ids) {
		if(is_array($ids)){
			$sqlmap = array();
			$sqlmap['id'] = array('IN',$ids);
			$this->table_district->where($sqlmap)->delete();
		} else if (is_numeric($ids)){
			$this->table_district->delete($ids);
		} else {
			$this->error = lang('order/request_parame_error');
			return FALSE;
		}
		return TRUE;
	}

	/**
	 * 获取订单物流信息
	 * @param  int $o_d_id 订单物流主键id
	 * @return [result]
	 */
	public function get_delivery_log($o_d_id) {
		$o_d_id = (int) remove_xss($o_d_id);
		$result = $this->table_delivery->find($o_d_id);
		if (!$result) {
			$this->error = $this->table_delivery->getError();
			return FALSE;
		}
		// 更新快递100数据
		$this->load->service('order/order_track')->update_api100($result['sub_sn'] ,$o_d_id);
		$sqlmap = array();
		$sqlmap['delivery_id'] = $o_d_id;
		$sqlmap['sub_sn'] = $result['sub_sn'];
		$result['logs'] = $this->load->table('order/order_track')->where($sqlmap)->order('id DESC')->select();
		if ($result['logs']) {
			foreach ($result['logs'] as $k => $v) {
				$v['add_time'] = date('Y-m-d H:i:s' ,$v['add_time']);
				$result['logs'][$k] = $v;
			}
		}
		return $result;
	}
}