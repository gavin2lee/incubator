<html>
<head>
  <meta charset="utf-8">
  <title>PHP Cookie and Session</title>
</head>
<body>
<?php
//header("Content-Type:text/html;charset=utf-8");
echo "<h2>Cookie and Session</h2>";
echo "<hr>";

$cookieContent = "PHP 测试Cookie!";

setcookie("phpCookieTest", $cookieContent);

echo "Before delete:".$_COOKIE['phpCookieTest'];

setcookie("phpCookieTest", "", time()-3600);
echo "After delete:".$_COOKIE['phpCookieTest'];

echo empty($_COOKIE['phpCookieTest']) ? "空" : "非空";

setcookie("phpCookieTest", $cookieContent);

echo "<br/>Cookie数组<br/>";

setcookie("Cookie[PHP]", "PHP是一种脚本语言！");
setcookie("Cookie[MYSQL]","MYSQL是一个数据库！");
setcookie("Cookie[Apache]","Apache是一个服务器！");

foreach($_COOKIE['Cookie'] as $cookieName=>$cookieValue){
  echo $cookieName."=>".$cookieValue."<br/>";
}

echo "<hr>";
echo "<h3>会话的基本方式</h3>";

$sessionDefinition = <<<EOF
会话的基本方式有会话ID的传送和会话ID的生成。
<br/>
1.会话ID的传送有两种方式:
<ol>
<li>
一种是Cookie方式
</li>
<li>
另一种方式是URL方式。
</li>
</ol>
2.会话ID的生成
<br/>
PHP的会话函数会自动处理ID的创建，但也可以通过手工方式来创建会话ID。它必须是不容易被人猜出来的，否则会有安全隐患。

EOF;

echo $sessionDefinition;

echo "<br/>";

define("KEY_NAME_SESSION", "u_name");
define("BR", "<br/>");

$sessionId = rand();
echo "session ID:".$sessionId."<br/>";

echo "Hashed session ID:".md5($sessionId)."<br/>";

echo "<hr>";

session_start();
$_SESSION["u_name"] = "Jim";
echo "Username:".$_SESSION["u_name"]."<br/>";

unset($_SESSION["u_name"]);
echo "Username:".$_SESSION["u_name"]."<br/>";

echo empty($_SESSION[KEY_NAME_SESSION]) ? "empty" : "assigned";
echo BR;

session_destroy();

?>



</body>
</html>
