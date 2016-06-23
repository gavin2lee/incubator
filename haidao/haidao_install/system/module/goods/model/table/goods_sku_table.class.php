<?php
/**
 *		子商品数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class goods_sku_table extends table {
    protected $result = array();
	protected $_validate = array(
       
    );
    protected $_auto = array(
        array('up_time','time',1,'function'),
        array('update_time','time',2,'function')
    );
    public function detail($id,$field = TRUE,$goods = '',$detail = true){
        if((int)$id > 0){
            $this->result['sku'] = $this->field($field)->find($id);
            if(empty($this->result['sku'])) return $this;
            if($this->result['sku']['imgs']){
                $this->result['sku']['img_list'] = json_decode($this->result['sku']['imgs'],TRUE);
            }
            $this->result['sku']['thumb'] = $this->result['sku']['thumb'] ? $this->result['sku']['thumb'] : $this->load->table('goods/goods_spu')->where(array('id'=>$this->result['sku']['spu_id']))->getField('thumb');
            if($goods == 'goods'){
                $this->result['goods'] = $this->load->table('goods/goods_spu')->find($this->result['sku']['spu_id']);
                if(empty($this->result['sku']['imgs'])){
                    $this->result['sku']['img_list'] = json_decode($this->result['goods']['imgs'],TRUE);
                }
                unset($this->result['goods']['thumb']);
                unset($this->result['goods']['sn']);
                if(!$this->result['sku']['subtitle']) unset($this->result['sku']['subtitle'],$this->result['sku']['style']); 
                if(!$this->result['sku']['warn_number']) unset($this->result['sku']['warn_number']);
                if(!$this->result['sku']['keyword']) unset($this->result['sku']['keyword']);  
                if(!$this->result['sku']['description']) unset($this->result['sku']['description']);
                if(!$this->result['sku']['content']) unset($this->result['sku']['content']);    
                $this->result['sku'] = array_merge($this->result['goods'],$this->result['sku']);
                $prom_price = 0;
                if ($this->result['sku']['prom_type'] == 'time' && $this->result['sku']['prom_id'] > 0 ) {
                    $pro_map = array();
                    $pro_map['id'] = $this->result['sku']['prom_id'];
                    $pro_map['start_time'] = array("LT", time());
                    $pro_map['end_time'] = array("GT", time());
                    $promotion = $this->load->table('promotion/promotion_time')->where($pro_map)->find();
                    if ($promotion) {
                        $sku_prom = json_decode($promotion['sku_info'],TRUE);
                        $prom_price = sprintf("%.2f", $sku_prom[$this->result['sku']['sku_id']]);
                        $this->result['sku']['prom_time'] = $promotion['end_time'] - time();
                    }else{
                        $prom_price = sprintf("%.2f", $this->result['sku']['shop_price']);
                    }
                }else{
                    $member = $this->load->service('member/member')->init();
                    if (!$member['id']) {
                        $prom_price = sprintf("%.2f", $this->result['sku']['shop_price']);
                    } else {
                        $discount = (!$member['_group']['discount']) ? 100 : $member['_group']['discount'] ;
                        $prom_price = sprintf("%.2f", $this->result['sku']['shop_price']/100*$discount);
                    }
                }
                $this->result['sku']['url'] = url('goods/index/detail',array('sku_id' => $id));
                $this->result['sku']['prom_price'] = $prom_price;
                $this->result['sku']['brand'] = model('goods/brand')->where(array('id'=>$this->result['goods']['brand_id']))->find();
                if($detail){
                    $this->result['sku']['cat_name'] = model('goods/goods_category')->where(array('id'=>$this->result['goods']['catid']))->getField('name');
                }
           }
        }
        return $this;
    }
    public function create_spec(){
         if (!empty($this->result['sku']['spec'])) {
            $this->result['sku']['spec'] = json_decode($this->result['sku']['spec'],TRUE);
        }
        return $this;
    }
    public function show_index(){
        $this->result['index'] = $this->load->table('goods/goods_index')->find($this->result['sku']['sku_id']);
        $this->result['sku']['sales'] = $this->result['index']['sales'];
        $this->result['sku']['hits'] = $this->result['index']['hits'];
        $this->result['sku']['favorites'] = $this->result['index']['favorites'];
        return $this;
    }
    public function output(){
        return $this->result['sku'];
    }
}