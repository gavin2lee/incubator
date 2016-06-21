<?php
class goods_hook
{

	/**
	 * 系统初始化钩子
	 */
	public function pre_system() {
		// var_dump('hook:pre_system is Initialized');
	}

	/* 控制器方法之前 */
	public function pre_control() {
		// var_dump('hook:pre_control is Initialized');
	}

	/* 控制器方法之后 */
	public function post_control() {
		// var_dump('hook:post_control is Initialized');
	}

	public function pre_output(){
		
	}

	public function pre_response(){}

	public function pre_input($data){
		
	}
	/**
	 * 登陆成功之后:{加入购物车、发送推送邮件等等}
	 */
	public function after_login($params) {
		model('order/cart','service')->cart_sync($params['id']);
		model('member/member','service')->change_group($params['id']);
	}
	/**
	 * 注册成功之后:{加入购物车、发送推送邮件等等}
	 */
	public function after_register($mid){
		model('order/cart','service')->cart_sync($mid);
		$member = array();
		$member['member'] = model('member/member')->where(array('id' => $mid))->find();
		model('notify/notify','service')->execute('after_register', $member);
	}

	/* 检测站点运营状态：{站点已关闭&非后台管理员 => 跳转到提示页面} */
	public function site_isclosed() {
		$setting = model('admin/setting','service')->get_setting();
		if ($setting['site_isclosed'] != 1) {
			$this->admin = model('admin/admin','service')->init();
			if ($this->admin['id'] == 0) {
				$SEO = seo('温馨提示');
				$message = $setting['site_closedreason'];
				exit(include TPL_PATH.'tip.tpl');
			}
		}
	}
}