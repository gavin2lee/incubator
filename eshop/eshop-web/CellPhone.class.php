<?php
class CellPhone implements Phone{
	public $brand;
	public $model;
	public $size;
	public $color;
	
	public function __construct($brand,$model,$size,$color){
		$this->brand = $brand;
		$this->model = $model;
		$this->size = $size;
		$this->color = $color;
	}
	
	public function __destruct(){
		
	}
	
	public function __toString(){
// 		define('SP', ',');
		$separator = ',';
		return 'CellPhone:'.$this->brand.$separator
							.$this->model.$separator
							.$this->size.$separator
							.$this->color;
	}
	
	public function call(){
		echo $this->brand.'-'.$this->model.' is calling...';
	}
	public function detail(){
		return $this->__toString();
	}
}