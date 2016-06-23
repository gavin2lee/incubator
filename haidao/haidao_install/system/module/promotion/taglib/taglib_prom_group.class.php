<?php
class taglib_prom_group
{
	public function __construct() {
		$this->sku_db = model('goods/goods_sku');
		$this->prom_group_db = model('promotion/promotion_group');
	}
	public function lists($sqlmap = array(), $options = array()) {
        $where['_string']='FIND_IN_SET('.$sqlmap["sku_id"].', sku_ids)';
        $where['status'] = 1;
        $group_info = $this->prom_group_db->where($where)->select();
        $lists = array();
        if(!empty($group_info)){
            foreach ($group_info AS $group) {
                foreach (explode(',',$group['sku_ids']) AS $sku_id) {
                    if($sku_id != $sqlmap['sku_id']){
                        $lists[$group['subtitle']]['group'][] = $this->sku_db->detail($sku_id,true,'goods')->output();
                    }
                }
                $lists[$group['subtitle']]['id'] = $group['id'];
            }
        }
        return $lists;
	}
}