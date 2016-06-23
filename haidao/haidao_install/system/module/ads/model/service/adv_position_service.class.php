<?php
/**
 *      广告位设置服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class adv_position_service extends service {
		
	protected $count;
    protected $pages;
	
	public function __construct() {
		$this->model = $this->load->table('ads/adv_position');
	}
	/**
     * 获广告位列表
     * @param type $sqlmap
     * @return type
     */
    public function getposition($sqlmap = array()) {
        $advs = $this->model->where($sqlmap)->select();
		return $advs;
    }
	/**
     * 查询单个信息
     * @param int $id 主键ID
     * @param string $field 被查询字段
     * @return mixed
     */
    public function fetch_by_id($id, $field = NULL) {
        $r = $this->model->find($id);
        if(!$r) return FALSE;
        return ($field !== NULL) ? $r[$field] : $r;
    }

	/**
	 * [更新广告位]
	 * @param array $data 数据
	 * @param bool $valid 是否M验证
	 * @return bool
	 */
	public function save($data, $valid = FALSE) {
		if($valid == TRUE){
			$data = $this->model->create($data);
			$result = $this->model->add($data);
		}else{
			$result = $this->model->update($data);
		}
		return $result;
	}
	/**
	 * [启用禁用广告位]
	 * @param string $id 支付方式标识
	 * @return TRUE OR ERROR
	 */
	public function change_status($id) {
		$result = $this->model->where(array('id' => $id))->save(array('status' => array('exp', '1-status')));
		if ($result == 1) {
			$result = TRUE;
		} else {
			$result = $this->model->getError();
		}
		return $result;
	}
	public function getdetail($params = array()) {
		$data = array();
		$map = array();
		$map['status'] = array('EQ', 1);
		$map['id'] = array('EQ', $params['position']);

		if (array_key_exists("order", $params)) {
			$order = $params['order'];
		}
		if (array_key_exists("limit", $params)) {
			$limit = $params['limit'];
		}
		//广告位
		$data = $this->model->where($map)->find();
		//广告明细
		unset($map);
		$map['starttime'] = array("LT", time());
		$map['endtime'] = array("GT", time());
		$map['status'] = array('EQ', 1);
		$map['position_id'] = array('EQ', $params['position']);
		$data['list'] = $this->load->table('adv')->where($map)->limit($limit)->order($order)->select();
		return dstripslashes($data);
	}
}
