<?php
include_once APP_PATH.'module/notify/library/driver/notify_abstract.class.php';
class wechat extends notify_abstract{
	public function __construct($_config) {
		$this->config = $_config;
	}
	public function send($params){
		$wechat = new we($this->config);
		$r = $wechat->sendTemplateMessage($params);
		$send = ($r['errcode'] == 0 && $r['errmsg'] == 'ok') ? TRUE : FALSE;
		return $this->_notify($send);
	}
}
