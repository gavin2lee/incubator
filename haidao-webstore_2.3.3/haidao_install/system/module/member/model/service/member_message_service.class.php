<?php 
class member_message_service extends service
{
	public function __construct(){
		$this->table = $this->load->table('member/member_message');
	}
	/**
	 * [ajax_update 改变状态]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function ajax_update($params) {
		if(count($params) < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['status'] = 1;
		foreach($params as $v){
			$data['id'] = (int)$v;
			$result = $this->table->update($data);
		}
		return $result;
	}
	/**
	 * [delete 批量删除]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function delete($params) {
		$data = array();
		$data['id'] =  implode(',', $params);
		$result = $this->table->where(array('id'=>array('IN',$data['id'])))->delete();
		if(!$result){
			$this->error = $this->table->getError();
			return FALSE;
		}
		return TRUE;
	}
	/**
     * 会员未读消息
	 *	$params [array] [数组]
     * @return [type] [description]
     */
	public function user_message($mid) {
		$data = array();
		$data['mid'] = (int)$mid;
		$data['status'] = 0;
		$rows = $this->table->where($data)->count();
		return (int)$rows;
	}
	/**
	 * [add 添加]
	 * @param  [array]  添加的数据
	 * @return [type]     [description]
	 */
	public function add($params) {
		if((int)$params['mid'] < 1){
			$this->error = lang('order/order_member_id_not_null');
			return FALSE;
		}
		if(!$params['title']){
			$this->error = lang('member/title_empty');
			return FALSE;
		}
		if(!$params['message']){
			$this->error = lang('member/content_empty');
			return FALSE;
		}
		$data = array();
		if($params['id']) $data['id'] = $$params['id'];
		$data['mid'] =  $params['mid'];
		$data['title'] = $params['title'];
		$data['message'] = $params['message'];
		$data['dateline'] = time();
		$data = $this->table->create($data);
		$result = $this->table->add($data);
		if($result === FALSE){
			$this->error = $this->table->getError();
			return FALSE;
		}
		return $result;
	}
}