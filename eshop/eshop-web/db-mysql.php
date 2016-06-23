<?php

define("BR", "<br/>");

header("Content-Type:text/html;charset=utf-8");

echo "<h2>MYSQL</h2><hr>";

$mysqli = mysqli_connect("localhost","gavin","123456","eshop",3306,"/var/run/mysqld/mysqld.sock");
if(!$mysqli->connect_errno){
  echo "Connect successful.<br/>";
}else{
  echo "Failed to connect mysql.<br/>";
}

$results = $mysqli->query("select * from t_users limit 10");

var_dump($results);

foreach($results as $rowno=>$row){
  echo "<br/>";
  //echo $row[0]."-".$row[1];
  var_dump($row);
  echo "<br/>";
}

$results->close();

$mysqli->close();
?>
