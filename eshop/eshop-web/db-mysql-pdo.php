<?php

define("BR", "<br/>");

header("Content-Type:text/html;charset=utf-8");

echo "<h2>MYSQL</h2><hr>";

$serverName = "127.0.0.1";
$username = "gavin";
$passwd = "123456";

$dsn = "mysql:host=".$serverName.";port=3306;dbname=eshop";

$db = null;

try{
  $db = new PDO($dsn, $username, $passwd);
}catch(PDOException $e){
  echo 'Connection failed: ' . $e->getMessage();
  //exit();
}


if($db){
  echo BR." connect successfully.";

}else{
  echo BR." connect failed.";
}

$users = $db->query("select * from t_users");

foreach($users as $idx=>$row){
  echo BR;
  print_r($row);
  echo BR;
}

$db = null;

?>
