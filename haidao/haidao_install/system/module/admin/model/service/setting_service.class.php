<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class setting_service extends service {
	public function __construct() {
		$this->db = $this->load->table('admin/setting');
	}

	/**
	 * 编辑
	 * @param array 	$params 内容
	 * @return [boolean]
	 */
	public function update($params){
		$settings = $this->get_setting();
		foreach ($params as $key => $value) {
			if (is_array($value)) $value = serialize($value);
			if (isset($settings[$key]) === true) {
				$this->db->where(array('key' => $key))->save(array('value' => $value));
			} else {
				$this->db->add(array('key' => $key ,'value' => $value));
			}
		}
		$this->load->service('admin/cache')->setting();
		return TRUE;
	}

	/**
	 * 获取后台设置
	 * @param  string 	$key 缓存名称(为空取所有设置)
	 * @return [result]
	 */
	public function get_setting($key){
		$get_setting = cache('setting') ? cache('setting') : $this->load->table('setting')->getField('key,value',TRUE);
		if(is_string($key)) return $get_setting[$key];
		foreach($get_setting as $key => $value){
			$get_setting[$key] = unserialize($value) ? unserialize($value) : $value;
		}
		return $get_setting;
	}
}