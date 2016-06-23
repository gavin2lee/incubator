<?php
/**
 *      广告设置服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class adv_service extends service {
		
	protected $count;
    protected $pages;
	
	public function __construct() {
		$this->model = $this->load->table('adv');
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
	 * [更新广告]
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


}
