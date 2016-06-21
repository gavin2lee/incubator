<?php
/**
 *		文章分类服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class article_category_service extends service {
	public function __construct() {
		$this->db = $this->load->table('misc/article_category');
	}
	/**
	 * [get_category_by_id 根据id获取文章信息]
	 * @param  [type] $id [description]
	 * @field  [string] 字段
	 * @return [type]     [description]
	 */
	public function get_category_by_id($id,$field = FALSE){
		if((int)$id < 1){
			$this->error = lang('goods/goods_category_not_exist');
			return FALSE;
		}
		$result = $this->db->find($id);
		$result['category_id'] = $this->get_parents_id($result['id']);
		$result['parent_name'] = $this->get_parents_name($id);
		if(!$result){
			$this->error = $this->db->getError();
		}
		if($field) return $result[$field]; 
		return $result;
	}
	//获取父分类id
	public function get_parents_id($id){
		if($id < 0){
			$this->error = lang('goods/goods_category_not_exist');
			return false;
		}
		static $ids = array();
		$row = $this->db->where(array('id'=>$id))->find();
		if($row['parent_id'] != 0){
			$ids[] = $row['id'];
			$parent_id = $row['parent_id'];
			$this->get_parents_id($parent_id);
		} else {
			$ids[] = $row['id'];
		}
		return array_reverse($ids);
	}
	//获取父分类名称
	public function get_parents_name($id){
		$ids = $this->get_parents_id($id);
		$names = $this->db->where(array('id'=>array('IN',$ids)))->getField('name',TRUE);
		$cat_name = '';
		foreach($names as $k => $v){
			$cat_name .= $v.' > ';
		}
		return  rtrim($cat_name,' > ');
	}
	/**
	 * [edit 编辑分类]
	 * @param [array] $params [分类信息]
	 * @return [boolean]         [返回ture or false]
	 */
	public function edit($params){
		if((int)$params['id'] < 1){
			$this->error = lang('goods/goods_category_not_exist');
			return FALSE;
		}		
		$data = array();
		$data['id'] = $params['id'];
		$data['name'] = $params['name'];
		$data['parent_id'] = $params['parent_id'];
		$data['sort'] = $params['sort'];
		$result = $this->db->update($data);
    	if($result === FALSE){
			$this->error = $this->db->getError();
    		return FALSE;
    	}else{
    		return TRUE;
    	}	
	}
	/**
	 * [delete 删除分类]
	 * @param [type] $id [分类id]
	 * @return [boolean]         [返回ture or false]
	 */
	public function delete($params){
		if($this->has_child($params['id'])){
			$this->error = lang('goods/goods_has_child_category');
			return FALSE;
		}
		$data = array();
		$data['id'] = array('IN', $params['id']);
		$result = $this->db->where($data)->delete();
    	if(!$result){
			$this->error = $this->db->getError();
    		return FALSE;
    	}else{
    		return TRUE;
    	}	
	}
	/**
     * [has_child 判断分类是否有子分类]
     * @param  [array]  $id [分类id]
     * @return boolean     [description]
     */
    public function has_child($params){
		$params_ids = explode(',',$params[0]);
		for($i=0; $i<count($params_ids); $i++){
			$tem_data[] = $this->db->where(array('parent_id' =>array('eq',$params_ids[$i])))->count();
			if($tem_data[$i] > 0){
				return TRUE;
			}
		}
		return FALSE;
	}
	/**
	 * [get_format_category 获取文章分类树]
	 * @return [type] [description]
	 */
    public function get_category_tree(){
		$_catinfo = $this->db->select();
		if(!$_catinfo){
    		$this->error = $this->db->getError();
    		return FALSE;
    	}
		$first = array(
			'id' => '0',
            'name' => '顶级分类',
            'parent_id' => '-1'
		);
		array_unshift($_catinfo,$first);
    	return $_catinfo;
	}
	/**
	 * [add 增加分类]
	 * @param [array] $params [增加分类信息]
	 * @return [boolean]         [返回ture or false]
	 */
	public function add($params){		
		if(empty($params['parent_id'])){
			$params['parent_id'] = 0;
		}else{
			$count = $this->db->where(array('id'=>array('eq',(int)$params['parent_id'])))->count();
			if($count < 1){
				$this->error = lang('misc/parent_category_not_exist');
				return false;
			}
		}
		$data = array();
		if($params['id']) $data['id'] = $params['id'];
		$data['name'] = $params['name'];
		$data['parent_id'] = $params['parent_id'];
		$data['sort'] = $params['sort'];
		$data = $this->db->create($data);
		$result = $this->db->add($data);
    	if(!$result){
    		$this->error = $this->db->getError();
			return FALSE;
    	}else{
    		return TRUE;
    	}		
	}
	/**
	 * [ajax_sun_class ajax获取分类]
	 */
	public function ajax_son_class($params){
		if((int)$params['id'] < 1){
			$this->error = lang('goods/goods_category_not_exist');
			return FALSE;
		}
		$result = $this->db->where(array('parent_id' =>array('eq',$params['id'])))->select();
		foreach($result as $key => $value){
			$result[$key]['row'] = $this->db->where(array('parent_id' =>array('eq',$value['id'])))->count();
		}
		if(!$result){
			$this->error = $this->db->getError();
		}
		return $result;
	}
	/**
	 * [ajax_edit 编辑分类信息]
 	 */
	public function ajax_edit($params){
		unset($params['page']);
		$result = $this->db->update($params);
		if($result == false){
			$this->error = $this->db->getError();
			return false;
		}
    	return true;
	}
	/**
	 * [get_category_by 获取一条分类信息]
 	 */
	public function get_category_by($id,$type){
		$id = (int)$id;
		if($id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['id'] = array('eq',$id);
		$row = $this->load->table('misc/article_category')->where($data)->find();
		if($type){
			return $row[$type];
		}
		return $row;
	}
}