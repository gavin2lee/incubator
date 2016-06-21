<?php
/**
 *		商品品牌数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class goods_category_service extends service {
	public function __construct() {
		$this->db = model('goods/goods_category');
		$this->goods_db = model('goods/goods_spu');
		$this->brand_db = model('goods/brand');
		$this->goods_category = cache('goods_category');
		$this->type_service = model('goods/type','service');
	}
	public function category_lists(){
		$result = $this->db->where(array('parent_id' => 0))->order('sort asc')->getField('id,name,sort,parent_id,type_id,status');
		foreach ($result as $key => $value) {
			$type_name = $this->type_service->get_name_by_id($value['type_id']);
			$result[$key]['type_name'] = $type_name ? $type_name : '';
			if($this->has_child($value['id'])){
				$result[$key]['level'] = 1;
			}
		}
		if(!$result){
    		$this->error = $this->db->getError();
    	}
    	return $result;
    }
    /**
     * [ajax_category ajax获取分类]
     * @param  [type] $parent_id [description]
     * @return [type]            [description]
     */
	public function ajax_category($parent_id){
		if((int)$parent_id < 1){
			$this->error = lang('goods/goods_category_not_exist');
			return FALSE;
		}
		$result = $this->db->where(array('parent_id' =>$parent_id))->order('sort desc')->select();
		foreach ($result as $key => $value) {
			$type_name = $this->type_service->get_name_by_id($value['type_id']);
			$result[$key]['type_name'] = $type_name ? $type_name : '';
			$result[$key]['row'] = $this->db->where(array('parent_id' =>array('eq',$value['id'])))->count();
		}
		if(!$result){
			$this->error = $this->db->getError();
		}
		return $result;
	}
	/**
	 * [add_spec 增加分类]
	 * @param [array] $params [规格信息]
	 * @return [boolean]         [返回ture or false]
	 */
	public function add_category($params){
		if(isset($params['grade'])){
			$params['grade'] =preg_replace("/(\s)/",'',preg_replace("/(\n)|(\t)|(\')|(')|(，)/" ,',' ,$params['grade']));
		}
		if($params['url'] == 'http://'){
			unset($params['url']);
		}
		$data = $this->db->create($params);
		$result = $this->db->add($data);
		$this->build_cache();
    	if($result === FALSE){
    		$this->error = $this->db->getError();
    		return FALSE;
    	}else{
    		return TRUE;
    	}		
	}
	/**
	 * [edit_spec 编辑分类]
	 * @param [array] $params [规格信息]
	 * @return [boolean]         [返回ture or false]
	 */
	public function edit_category($params){
		if((int)$params['id'] < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		if(isset($params['grade'])){
			$params['grade'] =preg_replace("/(\s)/",'',preg_replace("/(\n)|(\t)|(\')|(')|(，)/" ,',' ,$params['grade']));
		}
		if($params['url'] == 'http://'){
			unset($params['url']);
		}
		$result = $this->db->update($params);
		$this->build_cache();
    	if($result === FALSE){
    		$this->error = $this->db->getError();
    		return FALSE;
    	}else{
    		return TRUE;
    	}		
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
    		return FALSE;
    	}else{
    		return TRUE;
    	}	
	}
	/**
	 * [delete_spec 删除分类]
	 * @param  [int] $params [分类id]
	 * @return [boolean]     [返回删除结果]
	 */
	public function delete_category($id){
		if($this->has_child($id)){
			$this->error = lang('goods/goods_has_child_category');
			return FALSE;
		}
		if($id < 0){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$result = $this->db->delete($id);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
    	}else{
    		return TRUE;
    	}
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
    	}else{
    		return TRUE;
    	}
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
    		return FALSE;
    	}
    	return $result;
	}
	/**
	 * [get_format_category 获取商品分类树]
	 * @return [type] [description]
	 */
	public function get_category_tree(){
		$_catinfo = $this->db->order('sort ASC')->select();
		$result = $this->get_tree($_catinfo,0);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
    	}else{
    		return $result;
    	}
	}
    /**
	 * 分类层级
	 * @staticvar array $tree
	 * @param type $list 分类信息
	 * @param type $parent_id	分类父级id	
	 * @param type $level 级别
	 * @return array 
	 */
	public function get_tree($list,$parent_id,$level=0){
		 static $tree = array();
		 foreach ($list as $row){
			  if($row['parent_id'] == $parent_id){
					$row['level'] = $level;
					$tree[] = $row;
					$this -> get_tree($list, $row['id'],$level+1);
			  }
		 }
		 return $tree;
	}
	/**
	 * [get_parent_tree 获取商品分类树]
	 * @return [type] [description]
	 */
	public function get_parent_tree(){
		$_catinfo = $this->db->order('sort asc')->select();
		$result = $this->create_parent_tree($_catinfo,0);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
    	}else{
    		return $result;
    	}
	}
	/**
	 * 父级分类层级
	 * @staticvar array $tree
	 * @param type $list 分类信息
	 * @param type $parent_id	分类父级id	
	 * @param type $level 级别
	 * @return array 
	 */
	private function create_parent_tree($list,$parent_id,$level=1){
		 static $tree = array();
		 $tree[0] = array('id' => 0,'name' => '顶级分类','level' => 0,'parent_id' => -1);
		 foreach ($list as $row){
			if($row['parent_id'] == $parent_id){
				$row['level'] = $level;
				$tree[] = $row;
				$this -> create_parent_tree($list, $row['id'],$level+1);
			}
		 }
		 return $tree;
	}
	/**
     * 格式化分类到选择框
     * @param type $data
     */
    public function format_cat($id) {
    	if((int)$id < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
		$cat_str = '';
		$info = $this->get_parent($id);
		$cat_arr = $this->db->where(array('id'=>array('IN',$info)))->getField('name',TRUE);
		foreach ($cat_arr as $key => $value) {
			if($key == count($cat_arr)-1){
				$cat_str .= $value;
			}else{
				$cat_str .= $value.'-';
			}
		}
		return $cat_str;
    }
	/**
	 * [get_parent 获取商品分类所有父级id]
	 * @param  string  $cid   [需要获取的商品分类cid]
	 * @return [array]	$result       [返回父级id数组]
	 */
    public function get_parent($cid,$istop){
    	 if ($istop ==  1) {
            $result = 0;
            $category = $this->db->detail($cid)->output();
            $result = $category['id'];
            if ($category['parent_id']) {
                $result = $this->get_parent($category['parent_id'], $istop);
            }
        } else {
            $result = array();
            $category = $this->db->detail($cid)->output();
            if ($category['parent_id'] ) {
                $result[] = $category['parent_id'];
                if ($category['parent_id'] != $cid) {
                    $parent_id = $this->get_parent($category['parent_id'], $istop);
                    if(!empty($parent_id)){
                        $result = array_merge($result,$parent_id);
                    }
                }
               
            }
        }
        return $result;
	}
	/**
	 * [create_cat_format 组合父子级分类关系]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function create_cat_format($id,$extra){
		if((int)$id < 1){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
		$cat_str = '';
		$info = $this->get_parent($id);
		if(!$info){
			$info = $id;
		}else{
			array_push($info,$id);
		}
		$cat_arr = $this->db->where(array('id'=>array('IN',$info)))->getField('name',TRUE);
		if($extra == TRUE){
			array_unshift($cat_arr,'顶级分类');
		}
		foreach ($cat_arr as $key => $value) {
			if($key == count($cat_arr)-1){
				$cat_str .= $value;
			}else{
				$cat_str .= $value.'>';
			}
		}
		return $cat_str;
	}
	/**
	 * [create_cat_format 组合父分类关系,排除自身]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function create_parent_cat_format($id){
		if((int)$id < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
		$cat_str = '';
		$info = $this->get_parent($id);
		$cat_arr = $this->db->where(array('id'=>array('IN',$info)))->getField('name',TRUE);
		if(!$cat_arr){
			$cat_arr = array('0' => '顶级分类');
		}else{
			array_unshift($cat_arr,'顶级分类');
		}
		foreach ($cat_arr as $key => $value) {
			if($key == count($cat_arr)-1){
				$cat_str .= $value;
			}else{
				$cat_str .= $value.'>';
			}
		}
		return $cat_str;
	}
	/**
	 * [create_format_id 格式父子级分类id]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function create_format_id($id){
		if((int)$id < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
    	$info = $this->get_parent($id);
    	if(!$info){
			$info = $id;
		}else{
			krsort($info);
			array_push($info,$id);
		}
    	return implode(',',$info);
	}
	/**
	 * [create_format_id 格式父子级分类id]
	 * @param  [type] $pid [description]
	 * @return [type]     [description]
	 */
	public function create_parent_format_id($parent_id){
		if((int)$parent_id < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
    	$info = $this->get_parent($parent_id);
    	if(!$info){
			$info = array('0' => 0);
			array_push($info,$parent_id);
		}else{
			krsort($info);
			array_unshift($info,0);
			array_push($info,$parent_id);
		}
    	return implode(',',$info);
	}
    /**
     * [has_child 判断分类是否有子分类]
     * @param  [type]  $id [分类id]
     * @return boolean     [description]
     */
    public function has_child($id){
    	if((int)$id < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
		$rows = $this->db->where(array('parent_id'=>$id))->count();
		return $rows > 0 ? true : false;
	}
	/**
	 * [get_child 根据父分类获取所有子分类]
	 * @param  [type] $cid [父id]
	 * @return [type]      [所有子分类]
	 */
    public function get_child($cid){
    	if((int)$cid < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
        $return = array();
        $ids = $this->db->where(array('parent_id'=>$cid))->getField('id',TRUE);
        $return = $ids;
        if(is_array($ids)){
            foreach ($ids as $key => $value) {
                $child = $this->get_child($value);
                if(!empty($child)){
                    $return = array_merge($return,$child);
                }
            }
        }
        return $return;
    }
    /**
     * [build_cache 生成分类缓存]
     * @return [type] [description]
     */
    public function build_cache() {
        $result = $this->db->getField(implode(',', array_keys($this->db->get_fields())), TRUE);
        cache('goods_category', $result);
        return true;
    }
    /**
	 * [get_cate_grades 获取分类的价格分级]
	 * @param  [type] $id [分类id]
	 * @return [type] [description]
	 */
    public function get_cate_grades($id){
    	if((int)$id < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
    	$category = $this->goods_category[$id];
    	$grades = explode(',',$category['grade']);
    	$result = array();
    	foreach ($grades as $grade) {
			if($grade != ''){
				$result[] = explode('-',$grade);
		    }
		}
		return $result;
    }
    /**
     * [get_brand_info 获取分类及子分类下的所有品牌信息]
     * @param  [type] $id [description]
     * @return [type]     [description]
     */
    public function get_brand_info($id){
    	if((int)$id < 0){
    		$this->error = lang('_param_error_');
    		return FALSE;
    	}
    	$_cat_arr = array($id);
    	$_childcat_arr = $this->get_child($id);
    	if($_childcat_arr){
    		$_cat_arr = array_merge($_cat_arr,$_childcat_arr);
    	}
    	$sqlmap = $join = $brand_arr = array();
		foreach ($_cat_arr as $_childid) {
			$join[] = 'catid = '.$_childid;
		}
		$sqlmap['brand_id'] = array("GT", 0);
		$sqlmap['_string'] = JOIN(" OR ", $join);
		$brand_ids = $this->goods_db->where($sqlmap)->getField('brand_id', TRUE);
		if($brand_ids) {
			$brand_arr = $this->brand_db->where(array('id' => array("IN", $brand_ids)))->select();
		}
		return $brand_arr;
    }
    /**
     * [get_category_by_id 根据id获取分类信息]
     * @param  [type] $id [description]
     * @return [type]     [description]
     */
    public function get_category_by_id($id){
    	if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
    	$result = $this->db->detail($id)->output();
    	$result['cat_format'] = $this->create_parent_format_id($result['parent_id']);
    	$result['parent_name'] = $this->create_parent_cat_format($id);
    	$typeinfo = $this->type_service->get_type_by_id($result['type_id']);
    	$result['type_name'] = $typeinfo['name'];
    	if(!$result){
    		$this->error = lang('_operation_fail_');
    		return FALSE;
    	}
    	return $result;
     }
    /**
     * [get_top_parent 获取顶级分类id]
     * @param  string  $cid   [description]
     * @param  integer $istop [description]
     * @return [type]         [description]
     */
    private function get_top_parent($cid, $istop = 1) {
    	if((int)$cid < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
        if ($istop ==  1) {
            $result = 0;
            $category = $this->db->detail($cid,'id,parent_id')->output();
            $result = $category['id'];
            if ($category['parent_id']) {
            	$result = $this->get_top_parent($category['parent_id'], $istop);
            }
        } 
        return $result;
    }
    /**
     * [create_category 组织列表页的分类层级显示]
     * @param  [type] $id [description]
     * @return [type]     [description]
     */
    public function create_category($id){
    	if((int)$id < 1) {
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$category = $this->goods_category[$id];
		if(!$category){
			$this->error = lang('goods/goods_category_not_exist');
			return FALSE;
		}
		$top_parentid = $this->get_top_parent($id);
		$category['top_parentid'] = $this->goods_category[$top_parentid];
		return $category;
    }
}