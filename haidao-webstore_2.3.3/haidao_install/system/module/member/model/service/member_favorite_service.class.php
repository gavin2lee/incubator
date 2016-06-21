<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class member_favorite_service extends service {

    protected $sqlmap = array();

    public function __construct() {
        $this->table = $this->load->table('member/member_favorite');
    }

    public function lists($sqlmap = array(), $limit = 20, $page = 1) {
        $this->sqlmap = array_merge($this->sqlmap, $this->build_map($sqlmap));
        $DB = $this->table->where($this->sqlmap);
        $lists = &$DB->page($page)->limit($limit)->select();
        foreach ($lists as $key => $value) {
            $sku = $this->load->service('goods/goods_sku')->goods_detail($value['sku_id']);
            if($sku === false) continue;
            $lists[$key]['_sku'] = $sku;
        }
        $count = &$DB->count();
        return array('count' => $count, 'lists' => $lists);
    }
	private function build_map($data){
		switch($data['datetime']){
			case 'week':
				$data['datetime'] = array(array('GT',strtotime('last monday')),array('LT',time()));
				break;
			case 'month':
				$data['datetime'] = array(array('GT',strtotime(date('Y-m-01'))),array('LT',time()));
				break;
			case 'year':
				$data['datetime'] = array(array('GT',strtotime(date('Y-01-01'))),array('LT',time()));
				break;
			case 'lastyear':
				$data['datetime'] = array('LT',strtotime(date('Y-01-01')));
				break;
			default:
				$data['datetime'] = array('LT',time());
				break;
		}
		return  $data;
	}
    public function set_mid($mid) {
        if((int) $mid > 0) {
            $this->sqlmap['mid'] = $mid;
        }
        return $this;
    }

    /* 加入收藏夹 */
    public function add($sku_id, $sku_price = 0 ,$mid = 0,$id = 0,$datetime = 0) {
        if(!$sku_price){
            $_sku_info = $this->load->service('goods/goods_sku')->goods_detail($sku_id);
            if($_sku_info === false) {
                $this->error = $this->load->service('goods/goods_sku')->error;
                return false;
            }
        }
        $this->sqlmap['sku_id'] = $sku_id;
        $this->sqlmap['sku_name']=$_sku_info['sku_name'];
        $this->sqlmap['sku_price'] = $_sku_info['shop_price'];
        $this->sqlmap['datetime'] = $datetime ? $datetime : time();
        if(!$this->sqlmap['mid']){
            $this->sqlmap['mid'] = $mid;
            $this->sqlmap['id'] = $id;
        }
        if($this->is_exists($sku_id) === true) {
            $this->error = lang('member/repeat_collect');
            return false;
        }
        $data = $this->table->create($this->sqlmap);
        $result = $this->table->add($data);
        if(!$result) {
            $this->error = $this->table->getError();
            return false;
        }
        return true;
    }

    public function is_exists($sku_id) {
        $_sqlmap = array();
        $_sqlmap['mid'] = $this->sqlmap['mid'];
        $_sqlmap['sku_id'] = $sku_id;
        return ($this->table->where($_sqlmap)->count() > 0) ? true : false;
    }

    /* 删除收藏夹 */
    public function delete($sku_id = array()) {
        $this->sqlmap['sku_id'] = array("IN", $sku_id);
        $result = $this->table->where($this->sqlmap)->delete();
        if($result === false) {
            $this->error = $this->table->getError();
            return false;
        }
        return true;
    }
	public function get_favorite($page,$limit,$closing){
        $param = array();
		$param = $this->build_map(array('datetime'=>$closing));
		$param['mid'] = $this->sqlmap['mid'];
		$lists = $this->table->where($param)->page($page)->limit($limit)->select();
        foreach ($lists as $key => $value) {
            $sku = $this->load->service('goods/goods_sku')->goods_detail($value['sku_id']);
            if($sku === false) continue;
			$lists[$key]['sku_name'] = $sku['sku_name'];
			$lists[$key]['shop_price'] = $sku['shop_price'];
            $lists[$key]['sku_id'] = $sku['sku_id'];
            $lists[$key]['thumb'] = $sku['thumb'];
            $lists[$key]['url'] = url("goods/index/detail",array('sku_id' => $value['sku_id']));
        }
		return $lists;
	}
}