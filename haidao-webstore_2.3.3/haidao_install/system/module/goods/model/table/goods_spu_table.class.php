<?php
/**
 *		商品数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class goods_spu_table extends table {
    protected $result = array();
    protected $_validate = array(
        array('name','require','{goods/goods_name_require}',table::MUST_VALIDATE),
        array('catid','number','{goods/classify_id_require}',table::EXISTS_VALIDATE,'regex',table:: MODEL_BOTH),
        array('warn_number','number','{goods/stock_require}',table::EXISTS_VALIDATE,'regex',table:: MODEL_BOTH),
        array('status','number','{goods/state_require}',table::EXISTS_VALIDATE,'regex',table:: MODEL_BOTH),
        array('sort','number','{goods/sort_require}',table::EXISTS_VALIDATE,'regex',table:: MODEL_BOTH),
    );
    protected $_auto = array(
    );
    public function status($status){
        if(is_null($status)){
            return $this;
        }
        $this->where(array('status'=>$status));
        return $this;
    }
    public function category($catid){
        if(!$catid){
            return $this;
        }
        $this->where(array('catid'=>array('IN',$catid)));
        return $this;
    }
    public function brand($brand_id){
        if(!$brand_id){
            return $this;
        }
       $this->where(array('brand_id'=>$brand_id));
        return $this;
    }
   	public function keyword($keyword){
        if(!$keyword){
            return $this;
        }
        $this->where(array('name'=>array('LIKE','%'.$keyword.'%')));
        return $this;
    }
    public function detail($id,$field = TRUE){
        if($id < 1){
            return $this;
        } 
        $this->result['goods'] = $this->field($field)->find($id);
        if(empty($this->result['goods'])) return $this;
        if(!empty($this->result['goods']['imgs'])){
            $this->result['goods']['imgs'] = json_decode($this->result['goods']['imgs'],TRUE);
        }
        if(empty($this->result['goods']['thumb'])){
            $this->result['goods']['thumb'] = $this->load->table('goods/goods_sku')->where(array('spu_id'=>$this->result['goods']['id']))->order('sku_id ASC')->getField('thumb');
        }
        return $this;
    }
    public function sku_id(){
        $this->result['goods']['sku_id'] = $this->load->table('goods/goods_sku')->order('sku_id ASC')->where(array('spu_id'=>$this->result['goods']['id']))->getField('sku_id');
        return $this;
    }
    public function brandname(){
        $this->result['goods']['brandname'] = $this->load->table('goods/brand')->where(array('id'=>$this->result['goods']['brand_id']))->getField('name');
        return $this;
    }
    public function catname(){
        $this->result['goods']['catname'] = $this->load->service('goods/goods_category')->create_cat_format($this->result['goods']['catid']);
        return $this;
    }
    public function cat_format(){
         $this->result['goods']['cat_format'] = $this->load->service('goods/goods_category')->create_format_id($this->result['goods']['catid']);
        return $this;
    }
    public function output(){
        return $this->result['goods'];
    }
}