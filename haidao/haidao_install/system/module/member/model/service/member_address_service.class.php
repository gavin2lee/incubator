<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class member_address_service extends service
{
	protected $sqlmap = array();

	public function __construct(){
		$this->table = $this->load->table('member/member_address');
	}

	public function lists($sqlmap = array(), $limit = 10, $page = 1) {
		$this->sqlmap = array_merge($this->sqlmap, $sqlmap);
        $DB = $this->table->where($this->sqlmap);
        $lists = &$DB->page($page)->limit($limit)->order("id DESC")->select();
        foreach ($lists as $key => $value) {
        	$value['full_district'] = $this->load->service('admin/district')->fetch_position($value['district_id']);
        	array_shift($value['full_district']);
        	$lists[$key] = $value;
        }
        $count = $this->table->where($this->sqlmap)->count();
        return array('count' => $count, 'lists' => dhtmlspecialchars($lists));
	}

	/**
	 * 添加收货地址
	 * @param array $params [description]
	 */
	public function add($params = array()) {
		$params['status'] = 1;
		if(!$this->_validate($params)) {
			return $params['id'] ? true : false;
		}
		/* 校验发布条数 */
		$count = $this->table->where(array('mid' => $params['mid']))->count();
		if($count == 20) {
			$this->error = lang('member/shipping_address_more_limit');
			return false;
		}

		/* 如果没有收货地址则设为默认 */
		if($count == 0 || !empty($params['default'])) {
			$params['isdefault'] = 1;
		}
		$data = $this->table->create($params);
		$result = $this->table->add($data);
		$id = $params['id'] ? $params['id'] : $result;
		if(!empty($params['default'])){
			$this->table->where(array("id" => array("NEQ", $id), 'mid' => $params['mid']))->setField('isdefault', 0);
		}
		if($result === false) {
			$this->error = $this->table->getError();
			return false;
		}
		return true;
	}

	/**
	 * 编辑收货地址
	 * @param  array  $params [description]
	 * @return [type]         [description]
	 */
	public function edit($params = array()) {
		if($params['id'] < 1) {
			$this->error = lang('_param_error_');
			return false;
		}
		if(!$this->_validate($params)) {
			return false;
		}
		if(!empty($params['default'])) {
			$params['isdefault'] = 1;
			$this->table->where(array("id" => array("NEQ", $params['id']), 'mid' => $params['mid']))->setField('isdefault', 0);
		}
		$result = $this->table->update($params);
		if($result === false) {
			$this->error = $this->table->getError();
			return false;
		}
		return true;
	}

	public function mid($mid) {
		$this->sqlmap['mid'] = $mid;
		return $this;
	}

	public function set_default($id, $mid) {
		$this->table->where(array("id" => $id))->setField('isdefault', 1);
		$this->table->where(array("id" => array("NEQ", $id), 'mid' => $mid))->setField('isdefault', 0);
		return true;
	}

	public function fetch_by_id($id) {
		$this->sqlmap['id'] = $id;
		$r = $this->table->where($this->sqlmap)->find();
		if(!$r) {
			$this->error = $this->table->getError();
			return false;
		}
		$r['district_tree'] = $this->load->service('admin/district')->fetch_position($r['district_id'], 'id');
		$r['full_district'] = $this->load->service('admin/district')->fetch_position($r['district_id']);
		return $r;
	}

	/**
	 * 删除收货地址
	 * @param  [type] $id [description]
	 * @return bool
	 */
	public function delete($id) {
		$r = $this->fetch_by_id($id);
		if($r === false) {
			$this->error = lang('_valid_access_');
			return false;
		}
		if($r['isdefault'] == 1) {
			$this->error = lang('member/default_address_not_delete');
			return false;
		}
		$result = $this->table->delete($id);
		if($result === FALSE) {
			$this->error = $this->table->getError();
			return false;
		}
		return TRUE;
	}


	/**
	 * 数据校验
	 * @param  array  $params [description]
	 * @return [type]         [description]
	 */
	private function _validate($params = array()) {
		if(empty($params)) {
			$this->error = lang('_param_error_');
			return false;
		}
		if(empty($params['mid']) || (int) $params['mid'] < 1) {
			$this->error = lang('member/member_error');
			return false;
		}
		if(empty($params['name'])) {
			$this->error = lang('order/order_address_name_not_null');
			return false;
		}
		if(empty($params['mobile'])) {
			$this->error = lang('member/mobile_number_empty');
			return false;
		}
		if(!is_mobile($params['mobile'])) {
			$this->error = lang('member/mobile_number_format_empty');
			return false;
		}
		if(!empty($params['zipcode']) && !is_zipcode($params['zipcode'])){
			$this->error = lang('member/email_format_empty');
			return false;
		}
		$params['district_id'] = (int) $params['district_id'];
		if($params['district_id'] < 1) {
			$this->error = lang('member/shipping_area_empty');
			return false;
		}
		/* 检测地区必须选到最下级 */
		if(!$params['id']){
			if($this->load->service('admin/district')->get_children($params['district_id'])) {
				$this->error = lang('member/subordinate_area_empty');
				return false;
			}
		}
		if(empty($params['address']) || strlen($params['address']) < 5) {
			$this->error = lang('member/detail_area_require');
			return false;
		}
		return $params;
	}
}