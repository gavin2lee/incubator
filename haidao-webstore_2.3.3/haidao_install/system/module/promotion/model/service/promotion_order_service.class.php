<?php
class promotion_order_service extends service {

	public function __construct() {
		$this->table = $this->load->table('promotion/promotion_order');
	}


	public function lists($sqlmap = array(), $limit = 10, $page = 1, $order = "sort ASC, id DESC") {
		$DB = $this->table->where($sqlmap);
		$result['count'] = $this->table->where($sqlmap)->count();
		$lists = $this->table->where($sqlmap)->limit($limit)->page($page)->order($order)->select();
		foreach ($lists as $key => $value) {
			if($value['start_time'] && $value['start_time'] > TIMESTAMP) {
				$value['status'] = 1;
			} elseif($value['end_time'] && $value['end_time'] < TIMESTAMP) {
				$value['status'] = 2;
			} else {
				$value['status'] = 0;
			}
			$result['lists'][$value['id']] = $value;
		}
		return $result;
	}
    
    /**
     * 查询有效期内的订单促销列表
     * @param int $price 满金额
     * @return array
     */
	public function fetch_all($price = 0) {
		$timestamp = TIMESTAMP;
		$sqlmap = $result = array();
		if($price !== null) {
			$sqlmap['price'] = array("ELT", $price);
		}
		$sqlmap['start_time'] = array(array("EQ", 0), array("ELT", $timestamp), "OR");
		$sqlmap['end_time'] = array(array("EQ", 0), array("EGT", $timestamp), "OR");
		$sqlmap['_logic'] = "AND";
		//var_dump($sqlmap);exit;
		$lists = $this->table->where($sqlmap)->order('discount desc')->select();
		foreach ($lists as $key => $value) {
			if($key == 0) $value['_selected'] = 1;
			$result[$value['id']] = $value;
		}
		return $result;
	}

	public function fetch_by_id($id) {
		return $this->table->getById($id);
	}

	/**
	 * 更新活动
	 * @param array $params 数组
	 * @return bool
	 */
	public function update($params = array(),$isswitch = false) {
		if(empty($params)) {
			$this->error = lang('_param_error_');
			return false;
		}
		if(empty($params['name'])) {
			$this->error = lang('promotion/promotion_name_empty');
			return false;
		}
		if(isset($params['start_time']) && !empty($params['start_time']) && $isswitch == false) {
			$params['start_time'] = strtotime($params['start_time']);
		}
		if(isset($params['end_time']) && !empty($params['end_time']) && $isswitch == false) {
			$params['end_time'] = strtotime($params['end_time']);
		}
		$params['type'] = (int) $params['type'];
		$price = money($params['price']);
		if($price < 0) {
			$this->error = lang('promotion/order_money_illegal');
			return false;
		}
		$params['dateline'] = TIMESTAMP;
		$params['sort'] = 100;
		if($isswitch === true){
			$data = $this->table->create($params);
			$result = $this->table->add($data);
		}else{
			$result = $this->table->update($params);
		}
		if($result === false) {
			$this->error = $this->table->getError();
			return false;
		}
		return $result;
	}

	public function delete($ids) {
		if(empty($ids)) {
			$this->error = lang('_param_error_');
			return false;
		}
		$_map = array();
		if(is_array($ids)) {
			$_map['id'] = array("IN", $ids);
		} else {
			$_map['id'] = $ids;
		}
		$result = $this->table->where($_map)->delete();
		if($result === false) {
			$this->error = $this->table->getError();
			return false;
		}
		return true;
	}
}