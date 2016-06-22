<?php

class Car{
  public $color;
  public $size;

  public function __construct($x,$y){
    //echo "construct";
    $this->color = $x;
    $this->size = $y;
  }

  public function printAttributes(){
    echo "Color:".$this->color."<br/>";
    echo "Size:".$this->size."<br/>";
  }
}
