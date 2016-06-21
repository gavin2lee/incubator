<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class taglib_help
{
	public function __construct() {
		$this->model = model('misc/help');
	}
	public function lists($sqlmap = array(), $options = array()) {
		$this->model->where($this->build_map($sqlmap));
		if($sqlmap['order']){
			$this->model->order($sqlmap['order']);			
		}
		if($options['limit']){
			$this->model->limit($options['limit']);
		}
		return $this->model->select();
	}
	public function build_map($data){
		$sqlmap = array();
		$sqlmap['display'] = 1;
		if (isset($data['id'])) {
			if(preg_match('#,#', $data['id'])) {	
				$sqlmap['parent_id'] = array("IN", explode(",", $data['id']));
			} else {
				$sqlmap['parent_id'] = $data['id'];
			}
		}
		if(isset($data['_string'])){
			$sqlmap['_string'] = $data['_string'];
		}
		return $sqlmap;
	}
}