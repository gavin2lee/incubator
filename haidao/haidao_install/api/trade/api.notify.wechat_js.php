<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
$_filename = basename(__FILE__, '.php');
list(, $method, $driver) = explode(".", $_filename);
define('_PAYMENT_', $driver);
$_GET['m'] = 'pay';
$_GET['c'] = 'index';
$_GET['a'] = 'd'.$method;
include dirname(__FILE__).'/../../index.php';