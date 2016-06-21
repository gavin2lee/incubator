<?php
class init_control extends control
{
	public function _initialize() {
		parent::_initialize();
		$this->member = $this->load->service('member/member')->init();
		if ($this->member['id']) {
			$this->counts = $this->load->table('order/order')->buyer_id($this->member['id'])->out_counts();
		}
		$this->load->librarys('View')->assign('this_member',$this->member);
		$this->load->librarys('View')->assign('this_counts',$this->counts);
		define('SKIN_PATH', __ROOT__.(str_replace(DOC_ROOT, '', TPL_PATH)).config('TPL_THEME').'/');
		$cloud =  unserialize(authcode(config('__cloud__','cloud'),'DECODE'));
		define('SITE_AUTHORIZE', (int)$cloud['authorize']);
		define('COPYRIGHT', 'Powered by <a href="http://www.haidao.la/" target="_blank">Haidao</a> '.HD_VERSION.'<br/>© 2013-2016 Dmibox Inc.');
		/* 检测商城运营状态 */
		runhook('site_isclosed');
	}
}