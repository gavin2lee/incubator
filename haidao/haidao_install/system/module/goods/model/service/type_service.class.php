<?php
/**
 *		商品类型服务层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class type_service extends service {
	public function __construct() {
		$this->db = $this->load->table('goods/type');
		$this->attr_db = $this->load->table('goods/attribute');
		$this->cate_db = $this->load->table('goods/goods_category');
		$this->goods_db = $this->load->table('goods/goods_spu');
		$this->goods_attr = $this->load->table('goods/goods_attribute');
		$this->sku_db = $this->load->table('goods/goods_sku');
	}
	/**
	 * [get_lists 获取商品类型列表]
	 * @return [type] [description]
	 */
	public function get_lists($page,$limit){
		$result = $this->db->page($page)->limit($limit)->order('sort asc')->getfield('id,name,content,sort,status',TRUE);
		$attrs = array();
		foreach ($result as $k => $v) {
			$content = '';
			$attrs[$k] = $this->get_pop_by_id($v['id']);
			foreach ($attrs[$k] as $attr) {
				$content .= $attr['name'].'&nbsp;&nbsp;&nbsp;';
			}
			$result[$k]['desc'] = $content;
		}
		
		if(!$result){
			$this->error = $this->logic->error;
		}
		return $result;
	}
	/**
	 * [add_model 添加、编辑类型]
	 * @param [type] $params [description]
	 */
	public function add_type($params,$flag = false){
		if($flag == true){
			$params['spec_id'] = $params['spec_id'][0] ? explode(',',$params['spec_id'][0]) : '';
		}
		$attrinfo = $params;
		if($params['delId'] && count($params['delId']) > 0){
			$delIds = implode(',',$params['delId']);
			$attrDel = $this->attr_db->where(array('id'=>array('IN',$delIds)))->delete();
		}
		foreach ($attrinfo['attr_name'] as $key => $value) {
			$info = array(
				'id' => $attrinfo['attr_ids'][$key],
				'name' => $value,
				'value' => $attrinfo['attr_val'][$key],
				'type' => $attrinfo['type'][$key],
				'sort' => $attrinfo['attr_sort'][$key],
				'search' => $attrinfo['search'][$key],
			);
			if(empty($info['id']) || $flag == true){
				if($flag == false){
					unset($info['id']);
				}else{
					unset($attrinfo['attr_ids'][$key]);
					if(!$attrinfo['attr_val'][$key]) continue;
				} 
				$result = $this->attr_db->add($info);
				$attrinfo['attr_ids']['attr'][$key] = 'att_'.$result;
				if($result === FALSE){
					$this->error = $this->attr_db->getError();
					return FALSE;
				}
			}else{
				$result = $this->attr_db->update($info);
				$attrinfo['attr_ids']['attr'][$key] = 'att_'.$attrinfo['attr_ids'][$key];
				unset($attrinfo['attr_ids'][$key]);
				if($result === FALSE){
					$this->error = $this->attr_db->getError();
					return FALSE;
				}
			}
		}
		foreach ($attrinfo['spec_id'] AS $spec_id) {
			$attrinfo['spec_ids']['spec'][] = $spec_id;
		}
		if(empty($attrinfo['spec_ids'])){
			$attrinfo['spec_ids'] = array();
		}
		$attrinfo['content'] = array_merge($attrinfo['attr_ids'],$attrinfo['spec_ids']);
		$params['content'] = json_encode($attrinfo['content']);
		if(isset($params['id']) && $params['id'] > 0 && $flag == false) {
			$result = $this->db->update($params);
		} else {
		    $result = $this->db->add($params);
		}
		if($result === FALSE){
			$this->error = $this->db->getError();
			return FALSE;
		}else{
			return TRUE;
		}
	}
	/**
	 * [delete_spec 删除规格，可批量删除]
	 * @param  [fixed] $params [类型id]
	 * @return [boolean]         [返回删除结果]
	 */
	public function delete_type($params){
		$params = (array) $params;
		if(empty($params)){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$sqlmap = array();
		$sqlmap['id'] = array('IN',$params);
		$result = $this->db->where($sqlmap)->delete();
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
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
		$result = $this->db->where(array('id'=>array('eq',$id)))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
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
		$result = $this->db->where(array('id'=>array('eq',$params['id'])))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
	}
	/**
	 * [get_type_content 获取类型的内容字段]
	 * @param  [type] $catid [分类id]
	 * @return [type]        [array]
	 */
	private function get_type_content($catid){
		if((int)$catid < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$type_id = $this->cate_db->where(array('id'=>$catid))->getfield('type_id');
		$_attrs_arr = $this->db->where(array('id'=>$type_id))->getfield('content');
		$attrs = json_decode($_attrs_arr,TRUE);
		return $attrs;
	}
	/**
	 * [get_attrs 根据分类id获取商品属性，提供给商品添加页面选择属性]
	 * @param  [type] $catid [分类id]
	 * @return [type]        [array]
	 */
	public function get_attrs($catid){
		if((int)$catid < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$_attrs_info = $this->get_type_content($catid);
		$_attrs = $_attrs_info['attr'];
		$attrs = array();
		foreach ($_attrs as $key => $value) {
			$attrs[] = substr($value,strpos($value,'_')+1);
		}
		return $attrs;
	}
	/**
	 * [get_attrs 根据分类id获取商品类型id]
	 * @param  [type] $catid [分类id]
	 * @return [type]        [array]
	 */
	public function get_type_id($catid){
		if((int)$catid < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$type_id = $this->cate_db->where(array('id'=>$catid))->getfield('type_id');
		return $type_id;
	}
	/**
	 * [get_attrs 根据id获取商品类型名]
	 * @param  [type] $catid [分类id]
	 * @return [type]        [array]
	 */
	public function get_type_name($id){
		$type_name = $this->db->where(array('id'=>$id))->getfield('name');
		return $type_name;
	}
	/**
	 * [get_attrs 根据id获取商品类型名]
	 * @param  [type] $catid [分类id]
	 * @return [type]        [array]
	 */
	public function get_name_by_id($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$type_name = $this->db->where(array('id'=>$id))->getfield('name');
		return $type_name;
	}
	/**
	 * [is_search 判断属性是否参与筛选]
	 * @param  [type]  $id [description]
	 * @return boolean     [description]
	 */
	private function is_search($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$result = $this->attr_db->where(array('id'=>$id))->getfield('search');
		if($result == 1){
			return $id;
		}else{
			return FALSE;
		}
	}
	/**
	 * [get_type_by_id 根据id获取商品类型信息]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function get_type_by_id($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$result = $this->db->find($id);
		if(!$result){
			$this->error = $this->logic->error;
		}
		return $result;
	}
	/**
	 * [get_type_by_id 根据id获取商品属性信息]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function get_pop_by_id($id,$flag = FALSE){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$content = $this->db->where(array('id'=>$id))->getfield('content');
		$_attrs_info = json_decode($content,TRUE);
		$_attrs = $_attrs_info['attr'];
		$attrs = array();
		foreach ($_attrs as $key => $value) {
			$attrs[] = substr($value,strpos($value,'_')+1);
		}
		$sqlmap = array();
		$sqlmap['id'] = array('IN',$attrs);
		if($flag == TRUE){
			$sqlmap['type'] = array('NEQ','input');
		}
		$result = $this->attr_db->where($sqlmap)->order('sort asc')->getfield('id,name,value,sort,search,type');
		if(!$result){
			$this->error = $this->logic->error;
		}
		return $result;
	}
	/**
	 * [get_spec_by_id 根据id获取规格信息]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function get_spec_by_id($id){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$content = $this->db->where(array('id'=>$id))->getfield('content');
		$_attrs_info = json_decode($content,TRUE);
		if(!empty($_attrs_info['spec'])){
			foreach ($_attrs_info['spec'] AS $v) {
				$attr[$v] = $v;
			}	
		}
		return $attr;
	}

	/**
	 * [get_attr_info 根据id获取商品属性信息]
	 * @param  [type] $catid [分类id]
	 * @return [type]        [array]
	 */
	public function get_attr_info($id){
		$result = $this->attr_db->find($id);
		if($result['value']){
			$result['value'] = explode(',',$result['value']);
		}
		if(!$result){
			$this->error = $this->logic->error;
		}
		return $result;
	}
	/**
	 * [get_all_type 获取类型id和名称]
	 * @return [type] [description]
	 */
	public function get_all_type(){
		$result = $this->db->getField('id,name');
		if(!$result){
			$this->error = $this->logic->error;
		}
		return $result;
	}
	/**
	 * [get_type_by_goods_id 根据商品id获取类型数据]
	 * @param  [type] $goods_id [description]
	 * @return [type]           [description]
	 */
	public function get_type_by_goods_id($goods_id){
		$catid = $this->goods_db->getField('catid');
		$sku_id = $this->sku_db->where(array('spu_id'=>$goods_id))->getField('sku_id');
		$result = array();
		$result['type'] = $this->cate_db->where(array('id'=>$catid))->getfield('type_id');
		$attrs = $this->goods_attr->where(array('sku_id' => $sku_id,'type' => 1))->field('attribute_id,attribute_value')->select();
		$temp = array();
		foreach ($attrs AS $attr) {
			list($n,$t) = array_values($attr);
			$temp[$n][] = $t;
		}
		$result['attr'] = $temp;
		return $result;
	}
}
	