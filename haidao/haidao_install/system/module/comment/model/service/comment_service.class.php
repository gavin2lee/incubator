<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class comment_service extends service
{
	protected $sqlmap = array();

	public function __construct() {
		$this->table = $this->load->table('comment/comment');
		$this->goods = $this->load->service('order/order_sku');
	}

	public function lists($sqlmap = array(), $limit = 20, $order = 'id DESC', $page = 1) {
		$this->sqlmap = array_merge($this->sqlmap, $sqlmap);
		$DB = $this->table->where($this->sqlmap);
		$lists = &$DB->page((int)$page)->limit($limit)->order($order)->select();
		$count = $this->table->where($this->sqlmap)->count();
        foreach ($lists as $key => $value) {
            $sku = $this->load->service('goods/goods_sku')->goods_detail($value['sku_id']);
            $value['username'] = cut_str($value['username'], 1, 0).'**'.cut_str($value['username'], 1, -1);
            if($sku === false) continue;
            $value['imgs'] = ($value['imgs']) ? json_decode($value['imgs']) : array();
        	$value['_sku'] = $sku;
        	$value['_datetime'] = date('Y-m-d', $value['datetime']);
        	$value['avatar'] = getavatar($value['mid']);
        	$lists[$key] = $value;
        }
        return array('count' => $count, 'lists' => $lists);
	}

	public function get($id) {
		return $this->table->find($id);
	}

	public function add($params) {
		$r = $this->goods->detail((int) $params['tid']);
		if($r === false) {
			$this->error = $this->goods->error;
			return false;
		}
		/* 是否评论过 */
		if($params['mid'] < 1 || $params['mid'] != $r['buyer_id']) {
			$this->error = lang('_valid_access_');
			return false;
		}
		if($r['iscomment'] == 1) {
			$this->error = lang('attachment/repeat_publish');
			return false;
		}
		$params['spu_id'] = $this->load->table('goods/goods_sku')->where(array('sku_id'=>$r['sku_id']))->getField('spu_id');
		$params['sku_id'] = $r['sku_id'];
		$params['order_sn'] = $r['order_sn'];
		$result = $this->update($params);
		if(!$result) {
			return false;
		}
		return true;
	}
	public function change_status($id){
		$data = array();
		$data['is_shield']=array('exp',' 1-is_shield ');
		$result = $this->table->where(array('id' => $id))->save($data);
		if($result === false) {
			return false;
		}
		return true;
	}
	public function update($params) {
		$params['id'] = (int) $params['id'];
		$params['mid'] = (int) $params['mid'];
		$params['spu_id'] = (int) $params['spu_id'];
		$params['order_sn'] = $params['order_sn'];
		$params['content'] = $params['content'];
		if($params['mid'] < 1) {
			$this->error = lang('comment/member_info_error');
			return false;
		}
		if($params['spu_id'] < 1 || $params['sku_id'] < 1) {
			$this->error = lang('comment/goods_info_error');
			return false;
		}
		if(empty($params['order_sn'])) {
			$this->error = lang('order/order_sn_error');
			return false;
		}
		if(!in_array($params['mood'], array('positive','neutral','negative'))) {
			$this->error = lang('comment/evaluate_info_error');
			return false;
		}
		if(strlen($params['content']) < 5) {
			$this->error = lang('comment/evaluate_content_empty');
			return false;
		}
		if($params['imgs'] && is_array($params['imgs'])) {
			$attachment = implode(',', $params['imgs']);
			$params['imgs'] = json_encode($params['imgs']);
		}
		$result = $this->table->update($params);
		if($result === false) {
			$this->error = $this->table->getError();
			return false;
		}
		/* 操作附件 */
		if($attachment) $this->load->service('attachment/attachment')->attachment($attachment);
		/* 处理订单商品表 */
		$this->load->table('order/order_sku')->where(array('id' => $params['tid']))->setField('iscomment', 1);
		return true;
	}

	/* 回复评价信息 */
	public function reply($id, $content) {
		$r = $this->get($id);
		if(!$r) {
			$this->error = lang('comment/evaluate_empty');
			return false;
		}
		$data = array();
		$data['id'] = $id;
		$data['reply_content'] = $content;
		$data['reply_time'] = TIMESTAMP;
		$result = $this->table->update($data);
		if($result === false) {
			$this->error = $this->table->getError();
			return false;
		}
		return true;
	}
	/*获取统计*/
	public function get_count($spu_id){
		$result['positive'] = $this->table->where(array('spu_id'=>$spu_id,'mood'=>'positive','is_shield' => 1))->count();
		$result['neutral'] = $this->table->where(array('spu_id'=>$spu_id,'mood'=>'neutral','is_shield' => 1))->count();
		$result['negative'] = $this->table->where(array('spu_id'=>$spu_id,'mood'=>'negative','is_shield' => 1))->count();
		return $result;
	}
	/* 删除指定评价信息 */
	public function delete($ids = array()) {
		$sqlmap = array();
		$sqlmap['id'] = array('IN',$ids);
		$imgs = $this->table->where($sqlmap)->getField('imgs',true);
		foreach ($imgs AS $img) {
			$this->load->service('attachment/attachment')->attachment('', json_decode($img,true),false);
		}
		foreach ($ids as $id) {
			if(!is_numeric($id) || $id < 1) continue;
			$this->table->delete($id);
		}
		return true;
	}

	public function add_comment($params){
		$data = $this->table->create($params);
		return $this->table->add($data);
	}
}