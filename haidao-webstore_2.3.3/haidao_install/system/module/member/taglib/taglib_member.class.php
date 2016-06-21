<?php
class taglib_member
{
	public function __construct() {
		//$this->model = model('member');
	}

	public function lists($sqlmap = array(), $options = array()) {
		var_dump($sqlmap, $options);
		// $DB = $this->model->where($sqlmap);
		// if($options['limit']) {
		// 	&$DB->limit($options);
		// }

		// if($options['page']) {
		// 	&$DB->page($options['page']);
		// 	$this->pages = &$DB->count();
		// }

		// $result = &$DB->select();

		// var_dump($result);
		return $result;
	}
}