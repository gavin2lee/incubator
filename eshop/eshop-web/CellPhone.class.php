<?php
class CellPhone{
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
}