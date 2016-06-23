<?php
class taglib_brand
{
	public function __construct() {
		$this->model = model('goods/brand');
		$this->goods_spu_model = model('goods/goods_spu');
		$this->cate_service = model('goods/goods_category','service');
	}
	public function lists($sqlmap = array(), $options = array()) {
        if(isset($sqlmap['catid']) && is_numeric($sqlmap['catid']) && $sqlmap['catid'] > 0) {
            $catids = $this->cate_service->get_child($sqlmap['catid']);
            if(empty($catids)) return FALSE;
            foreach ($catids as $catid) {
                $join[] = "catid =".$catid;
            }
            $brand_map = array();
            $brand_map['status'] = 1;
            $brand_map['brand_id'] = array("GT", 0);
            $brand_map['_string'] = implode(' OR ', $join);
            $brand_ids = $this->goods_spu_model->where($brand_map)->group('brand_id')->getField('brand_id', TRUE);
            if(!$brand_ids) return FALSE;
        }
        $map = array();
        $map['isrecommend']=1;
        if($brand_ids) {
            $map['id'] = array("IN", $brand_ids);
            
        }
        if(!isset($sqlmap['order'])){
           $sqlmap['order'] = 'sort asc,id desc';
        }
        $this->model->order($sqlmap['order']);
        if(isset($options['limit'])){
            $this->model->limit($options['limit']);
        }
        if(isset($options['page'])){
            $this->model->page($options['page']);
        }
        $result = $this->model->where($map)->select();
        return $result;
	}
}