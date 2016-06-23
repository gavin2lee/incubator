<?php
/**
 *		幻灯片服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class focus_service extends service {
		public function __construct() {
		$this->db = $this->load->table('misc/focus');	
		$this->article_service = $this->load->service('misc/article');	
	}
	/**
	 * [lists 列表]
	 * @return [type] [description]
	 */
	public function lists(){
		$result = $this->db->select();
		if(!$result){
			$this->error = $this->db->getError();
			return FALSE;
		}
		return $result;
	}
		/**
	 * [add 添加]
	 * @return [type] [description]
	 */
	public function add($params){
		$data = array();
		if($params['id']) $data['id'] = $params['id'];
		$data['title'] = $params['title'];
		$data['thumb'] = $params['thumb'] ? $params['thumb']: '';
		$data['url'] = $params['url'];
		$data['target'] = $params['target'];
		$data['sort'] = $params['sort'];
		$data['description'] = $params['description']?$params['description']:'';
		$data['display'] = isset($params['display']) ? $params['display'] : 1;
		$data = $this->db->create($data);
		$result = $this->db->add($data);
		if(!$result){
			$this->error = $this->db->getError();
			return FALSE;
		}
		return $result;
	}
	/**
	 * [get_focus_by_id 查询某条数据]
	 * @id [number] 传入的id
	 * @return [type] [description]
	 */
	public function get_focus_by_id($id){
		if((int)$id < 1){
			$this->error = lang('member/slideshow_not_exist');
			return FALSE;
		}
		$result = $this->db->find($id);
		if(!$result){
			$this->error = $this->db->getError();
			return FALSE;
		}
		return $result;
	}
	/**
	 * [edit 编辑]
	 * @return [type] [description]
	 */
	public function edit($params){
		if(!isset($params['id'])){
			$this->error = lang('member/slideshow_not_exist');
			return FALSE;
		}
		$data = array();
		$data['id'] = $params['id'];
		$data['title'] = $params['title'];
		$data['url'] = $params['url'];
		$data['target'] = $params['target'];
		if($params['thumb']){
			$data['thumb'] = $params['thumb'];
		}
		$data['url'] = $params['url'];
		$data['sort'] = $params['sort'];
		$result = $this->db->update($data);
		if($result === FALSE){
			$this->error = $this->db->getError();
			return FALSE;
		}
		return TRUE;
	}
	/**
	 * [delete 删除]
	 * @return [type] [description]
	 */
	public function delete($params){
		if(!$this->article_service->is_array_null($params)){
			$this->error = lang('member/slideshow_not_exist');
			return FALSE;
		}
		$data = array();
		$data['id'] = array('IN', explode(',',$params['id'][0]));
		$infos = $this->db->where($data)->getField('thumb',true);
		foreach ($infos AS $info) {
			$this->load->service('attachment/attachment')->attachment('', $info,false);
		}
		$result = $this->db->where($data)->delete();
		if(!$result){
			$this->error = $this->db->getError();
			return FALSE;
		}
		return $result;
	}
		/**
	 * [ajax_edit 编辑]
	 * @return [type] [description]
	 */
	public function ajax_edit($params){
		if(!isset($params['id'])){
			$this->error = lang('member/slideshow_not_exist');
			return FALSE;
		}
		$result = $this->db->update($this->article_service->assembl_array($params));
		if(!$result){
			$this->error = $this->db->getError();
			return FALSE;
		}
		return $result;
	}
	/**
	 * [total 总记录数]
	 * @return [type]     [返回查询结果结果]
	 */
	public function total(){		
		$result = $this->db->count;
		if(!$result){
    		$this->error = $this->db->getError();
    		return FALSE;
    	}
		return $result;
	}
}