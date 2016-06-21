<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class admin_group_service extends service {	
	public function __construct() {
		$this->model = $this->load->table('admin_group');
	}
	/**
	 * [获取所有团队角色]
	 * @param array $sqlmap 数据
	 * @return array
	 */
	public function getAll($sqlmap = array()) {
		$this->sqlmap = isset($this->sqlmap)?array_merge($this->sqlmap, $sqlmap):$sqlmap;
		return $this->model->where($this->sqlmap)->select();
	}
	/**
	 * [启用禁用角色]
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
	/**
	 * [更新角色]
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
		if($result === false) {
			$this->error = $this->model->getError();
			return false;
		}
		return TRUE;
	}
}
