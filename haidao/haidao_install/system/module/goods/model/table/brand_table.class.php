<?php
/**
 *		商品品牌数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class brand_table extends table {
	protected $result;
	protected $_validate = array(
        array('name', 'require', '{goods/goods_brand_name_require}',table::MUST_VALIDATE),
        array('sort','number','{goods/sort_require}',table::EXISTS_VALIDATE,'regex'),
        array('isrecommend','number','{goods/goods_brand_recommend}',table::EXISTS_VALIDATE,'regex'),
    );
    protected $_auto = array(
    );
     public function detail($id,$field){
        $this->result['brand'] = $this->field($field)->find($id);
        return $this;
    }
    public function output(){
        return $this->result['brand'];
    }
}