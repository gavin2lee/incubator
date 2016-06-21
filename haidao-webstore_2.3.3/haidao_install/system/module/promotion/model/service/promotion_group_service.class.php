<?php
/**
 *      捆绑营销服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class promotion_group_service extends service {
	public function __construct() {
		$this->table = $this->load->table('promotion_group');
	}
	public function get_lists($params){
		$result = $this->table->page($params['page'])->limit($params['limit'])->select();
		if(!$result){
			$this->error = $this->table->getError();
		}
		return $result;
	}
	/**
	 * [fetch_by_id 查询单条数据]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function fetch_by_id($id) {
		return $this->table->getById($id);
	}
	/**
	 * [updata 更新数据]
	 * @param  [type] $params [description]
	 * @return [type]         [description]
	 */
	public function update($params){
		if(empty($params)) {
			$this->error = lang('_param_error_');
			return false;
		}
		if(empty($params['title'])) {
			$this->error = lang('promotion/bound_activity_title_empty');
			return false;
		}
		if(empty($params['subtitle'])) {
			$this->error = lang('promotion/bound_activity_name_empty');
			return false;
		}
		$params['sku_ids'] = implode(',', $params['sku_ids']);
		$result = $this->table->update($params);
		if($result === false) {
			$this->error = $this->table->getError();
			return false;
		}
		return $result;
	}
	/**
	 * [delete 删除数据]
	 * @param  [type] $ids [description]
	 * @return [type]      [description]
	 */
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
	/**
	 * [change_status 改变状态]
	 * @param  [int] $id [规格id]
	 * @return [boolean]     [返回更改结果]
	 */
	public function change_status($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['status']=array('exp',' 1-status ');
		$result = $this->table->where(array('id'=>array('eq',$id)))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
	}
	/**
	 * [change_sort 改变名称]
	 * @param  [array] $params [品牌id和name]
	 * @return [boolean]     [返回更改结果]
	 */
	public function change_name($params){
		if((int)$params['id'] < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['title'] = $params['name'];
		$result = $this->table->where(array('id'=>array('eq',$params['id'])))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
	}
	/**
	 * [change_sort 改变名称]
	 * @param  [array] $params [品牌id和name]
	 * @return [boolean]     [返回更改结果]
	 */
	public function change_subtitle($params){
		if((int)$params['id'] < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['subtitle'] = $params['name'];
		$result = $this->table->where(array('id'=>array('eq',$params['id'])))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
	}
}
