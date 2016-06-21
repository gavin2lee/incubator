<?php
class promotion_hook
{
	/* 控制器方法之前 */
	public function pre_control() {
		model('promotion/promotion_time','service')->fetch_skuid_by_timeed();
		model('promotion/promotion_goods','service')->fetch_skuid_by_goods();
	}
}