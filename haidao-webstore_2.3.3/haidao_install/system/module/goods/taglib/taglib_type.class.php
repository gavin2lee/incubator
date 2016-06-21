<?php
class taglib_type
{
	public function __construct() {
		$this->cate_db = model('goods/goods_category');
        $this->type_db = model('goods/type');
        $this->attr_db = model('goods/attribute');
        $this->spec_db = model('goods/spec');
	}
	public function lists($sqlmap = array(), $options = array()) {
        $type_id = $this->cate_db ->where(array('id'=>$sqlmap['catid']))->getfield('type_id');
        $content = $this->type_db ->where(array('id'=>$type_id,'status'=>1))->getfield('content');
        $_attrs_info = json_decode($content,TRUE);
        $_attrs = $_attrs_info['attr'];
        $attrs = array();
        $map = array();
        if(isset($sqlmap['order'])){
            $order = $sqlmap['order'];
        }
        if(isset($options['limit'])){
            $limit =$options['limit'];
        }
        $map['type'] = array('neq','input');
        foreach ($_attrs as $key => $value) {
            $attrs[$key] = substr($value,strpos($value,'_')+1);
            $map['id'] = $attrs[$key];
            $result[$value] = $this->attr_db->where($map)->field('id,name,value,type,search')->find();
            if(!empty($result[$value]['value'])){
                $result[$value]['value'] = explode(',', $result[$value]['value']);
            }else{
                unset($result[$value]);
            }
        }
        return $result;
    }

    public function specs($sqlmap = array(), $options = array()) {
        $type_id = $this->cate_db ->where(array('id'=>$sqlmap['catid']))->getfield('type_id');
        $content = $this->type_db ->where(array('id'=>$type_id))->getfield('content');
        $_attrs_info = json_decode($content,TRUE);
        $_specs = $_attrs_info['spec'];
        foreach ($_specs as $k =>$_spec) {
             $result['spec_'.$_spec] = $this->spec_db->where(array('id'=>$_spec))->field('id,name,value')->find();
             $result['spec_'.$_spec]['value'] = explode(',',$result['spec_'.$_spec]['value']);
        }
        return $result;
    }
}