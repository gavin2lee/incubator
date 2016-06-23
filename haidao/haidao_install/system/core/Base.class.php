<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
abstract class Base {
	
    public function __construct() {
        if(method_exists($this,'_initialize'))
            $this->_initialize();
    }
    
	public function __get($name) {
		if($name == 'load'){
			return Load::getInstance();
		}
		return $this->$name;
	}
    public function __set($name, $value) {
		$this->$name = $value;
	}

	public function __call($name,$parameters) {
		throw new Exception('Class "'.get_class($this).'" does not have a method named "'.$name.'".');
	}
    
	public function __toString() {
		return get_class($this);
	}

	public function __invoke() {
		return get_class($this);
	}
}
