<?php
/**
 *		商品品牌数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class brand_service extends service {
	public function __construct() {
		$this->db = $this->load->table('goods/brand');
	}
	/**
	 * [get_lists 获取品牌列表]
	 * @return [type] [description]
	 */
	public function get_lists($page,$limit){
		$result = $this->db->page($page)->limit($limit)->order('sort asc')->getField('id,name,descript,sort,logo,isrecommend',TRUE);
		if(!$result){
			$this->error = $this->db->getError();
		}
		return $result;
	}
	/**
	 * [add_spec 增加品牌]
	 * @param [array] $params [规格信息]
	 * @return [boolean]         [返回ture or false]
	 */
	public function add_brand($params){
		$data = $this->db->create($params);
		$result = $this->db->add($data);
    	if($result === FALSE){
    		$this->error = $this->db->getError();
    		return FALSE;
    	}
    	return $result;	
	}
	/**
	 * [edit_spec 编辑品牌]
	 * @param [array] $params [规格信息]
	 * @return [boolean]         [返回ture or false]
	 */
	public function edit_brand($params){
		if((int)$params['id'] < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$result = $this->db->update($params);
    	if($result === FALSE){
    		$this->error = $this->db->getError();
    		return FALSE;
    	}
    	return $result;
	}
	/**
	 * [change_recommend 改变状态]
	 * @param  [int] $id [规格id]
	 * @return [boolean]     [返回更改结果]
	 */
	public function change_recommend($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['isrecommend']=array('exp',' 1-isrecommend ');
		$result = $this->db->where(array('id'=>array('eq',$id)))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
    	}
    	return $result;
    }
	/**
	 * [delete_spec 删除规格，可批量删除]
	 * @param  [int||array] $params [规格id或规格id数组]
	 * @return [boolean]         [返回删除结果]
	 */
	public function delete_brand($params){
		if(empty($params)){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$sqlmap = array();
		$sqlmap['id'] = array('IN',$params);
		$infos = $this->db->where($data)->getField('logo',true);
		foreach ($infos AS $info) {
			$this->load->service('attachment/attachment')->attachment('', $info,false);
		}
		$result = $this->db->where($sqlmap)->delete();
		if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
    	}
    	return $result;
	}
	/**
	 * [change_sort 改变排序]
	 * @param  [array] $params [规格id和排序数组]
	 * @return [boolean]     [返回更改结果]
	 */
	public function change_sort($params){
		if((int)$params['id'] < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['sort'] = $params['sort'];
		$result = $this->db->where(array('id'=>array('eq',$params['id'])))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
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
		$data['name'] = $params['name'];
		$result = $this->db->where(array('id'=>$params['id']))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
    	}
    	return $result;
    }
	/**
	 * [get_brand_name 获取品牌名称]
	 * @param  [type] $id [品牌id]
	 * @return [type]     [description]
	 */
	public function get_brand_name($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$result = $this->db->where(array('id'=>array('EQ',$id)))->getField('name');
		if(!$result){
			$this->error = lang('_operation_fail_');
    	}
		return $result;
	}
	/**
	 * [search_brand 关键字查找品牌]
	 * @param  [type] $keyword [description]
	 * @return [type]          [description]
	 */
	public function ajax_brand($keyword){
		$sqlmap = array();
		if($keyword){
			$sqlmap = array('name'=>array('LIKE','%'.$keyword.'%'));
		}
		$result = $this->db->where($sqlmap)->getField('id,name',TRUE);
		if(!$result){
			$this->error = lang('_operation_fail_');
    	}
		return $result; 
	}
	/**
	 * [get_brand_by_id 根据id获取品牌信息]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function get_brand_by_id($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$result = $this->db->find($id);
		if(!$result){
			$this->error = lang('goods/no_found_brand');
		}
		return $result;
	}
}