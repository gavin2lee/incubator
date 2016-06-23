<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class taglib_navigation
{
	public function __construct() {
		$this->misc_service = model('misc/navigation','service');
	}
	public function lists($sqlmap = array(), $options = array()) {
		return $this->misc_service->lists($sqlmap,$options);
	}
}