<?php
const KEY_COOKIE_SID = 'key_cookie_sid';

$sid = rand();
$sidMd5 = md5($sid);

setcookie(KEY_COOKIE_SID,$sidMd5);

echo $sidMd5;