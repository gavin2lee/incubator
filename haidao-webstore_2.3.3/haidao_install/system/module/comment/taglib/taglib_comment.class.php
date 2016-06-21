<?php
/**
 * 商品评价
 */
class taglib_comment
{

	public function __construct() {
		$this->service = model('comment/comment','service');
	}

	public function lists($sqlmap = array(), $options = array()) {
		$result = $this->service->lists($sqlmap, $options['limit']);
		extract($result);
		$this->pages = pages($count, $options['limit']);
		return $result['lists'];
	}
}