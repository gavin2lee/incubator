<?php
header('Content-Type:text/html;charset=utf-8');
echo '<h2>Class Demos</h2>';

echo '<hr>';

require_once 'Phone.interface.php';
require_once 'CellPhone.class.php';

const BR = '<br/>';


$myCellPhone = new CellPhone('Samsung', 'A5009', '5.1in', 'White');
print $myCellPhone;
echo BR;
print $myCellPhone->detail();
echo BR;

$interfaces = class_implements('CellPhone');
// var_dump($interfaces);
if(isset($interfaces['Phone'])){
	echo 'CellPhone has implemented Phone.';
	echo BR;
}