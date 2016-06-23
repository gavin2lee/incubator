<?php
class taglib_category
{
    public function __construct() {
        $this->model = model('goods/goods_category');
        $this->service = model('goods/goods_category','service');
    }
    public function lists($sqlmap = array(), $options = array()) {
        $sqlmap['status'] = 1;
        if ($sqlmap['only']) {
            $catids = str_replace('，', ',', $sqlmap['catid']);
            $catids = explode(',', $catids);
            $sqlmap['id'] = array('IN',$catids);
        } else {
            if($sqlmap['type'] == 'nav') {
                $sqlmap['show_in_nav'] = 1;
                $cat_ids = $this->service->get_child($sqlmap['catid']);
                $sqlmap['id'] = array('IN',$cat_ids);
            }else{
                $sqlmap['parent_id'] = $sqlmap['catid'] ? $sqlmap['catid'] : 0;            
            }
        }
        if(!isset($sqlmap['order'])){
           $sqlmap['order'] = 'sort asc,id asc';
        }
        $this->model->order($sqlmap['order']);
        if(isset($options['limit'])){
            $this->model->limit($options['limit']);
        }
        if(isset($options['page'])){
            $this->model->page($options['page']);
        }
        $lists = $this->model->where($sqlmap)->select();
        return $lists;
    }
}