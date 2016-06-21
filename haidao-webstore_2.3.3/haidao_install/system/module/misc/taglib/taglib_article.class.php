<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class taglib_article
{
	public function __construct() {
		$this->model = model('misc/article');
		$this->category_db = model('misc/article_category');	
	}
	public function lists($sqlmap = array(), $options = array()) {	
		$count = $this->model->where($this->build_map($sqlmap))->count();
		$this->model->where($this->build_map($sqlmap));
		if(isset($sqlmap['order'])){
			$this->model->order($sqlmap['order']);
		}
		if(isset($options['limit'])){
			$this->model->limit($options['limit']);
		}
		if($options['page']) {
			$this->model->page($options['page']);
			$pagefunc = $options['pagefunc'] ? $options['pagefunc'] : 'pages';
			$this->pages = $pagefunc($count,$options['limit']);
		}
		$lists = $this->model->select();
		if($lists){
			return $this->category($lists);
		}
	}
	public function build_map($data){
		$sqlmap = array('display' => 1);
		if(isset($data['_string'])){
			$sqlmap['_string'] = $data['_string'];
		}
		
		if(isset($data['category_id'])){
			$sqlmap['category_id'] = $this->get_category_by_id($data['category_id']);
			$sqlmap['category_id'] = array('IN',implode(',', $sqlmap['category_id']));
		}
		return $sqlmap;
	}
	public function category($data){
		foreach($data as $key => $value){
		  $data[$key]['category'] = $this->category_db->where(array('id' =>array('eq',$value['category_id'])))->getField('name');
	   }
	   return $data;
	}
	public function get_category_by_id($id){
		$category_id = array();
		$category_id[] = $id;
		$row = model('article_category')->where(array('parent_id'=>array('eq',$id)))->select();
		if($row){
			foreach($row as $v){
				$category_id[] = $v['id'];
			}
		}
		return $category_id;
	}
}