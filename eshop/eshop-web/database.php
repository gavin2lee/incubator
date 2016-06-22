<?php
header("Content-Type:text/html;charset=utf-8");

echo "<h2>数据库</h2>";
echo "<hr/>";
echo $_SERVER['USER'];
echo "<br/>";

$db = new PDO("sqlite:./resultdb");
if($db){
  echo "连接成功";
}else{
  echo "连接失败";
}

//$db->exec("create table t_users(id integer, name varchar(255))");


$db->exec("insert into t_users values ('200', 'Tom')");
//$db->commit();

$rows = $db->query("select * from t_users");

//echo $rows;

foreach($rows as $row){
  echo "ROW:".$row[0]." ";
  print_r($row);
}
?>
