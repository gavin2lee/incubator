<?php
//echo "<html>";
header("Content-Type:text/html;charset=utf-8");

define("GREETING", "你好，今天的天气不错啊。", true);

echo GREETING;
echo "<br/>";
echo "当前文件路径：".__FILE__;
echo "<br/>";
echo "当前行数：".__LINE__;
echo "<br/>";
echo "PHP版本：".PHP_VERSION;
echo "<br/>";
echo "当前操作系统：".PHP_OS;
echo "<hr>";

$head_title="变量";
echo $head_title."<br/>";

function sayHello(){
  $greet="函数说：你好！";
  echo $greet."<br/>";
}

sayHello();

function countStatic(){
  static $count=0;
  $count++;
  echo $count." ";
}

function countNonStatic(){
  $count=0;
  $count++;
  echo $count." ";
}

echo "函数静态变量"."<br/>";
for($i=0;$i<5;$i++){
  countStatic();
}
echo "<br/>函数非静态变量<br/>";
for($i=0;$i<5;$i++){
  countNonStatic();
}

echo "<br/>可变变量<br/>";
$varName="name";
$name="Jim Green";

echo '$$varName:'.$$varName;
echo "<br/>";

echo "<hr>";
echo "整数"."<br/>";
echo "PHP 用var_dump() 函数会返回变量的数据类型和值。"."<br/>";
$x=1982;
var_dump($x);
echo "<br/>";
$y=032;
var_dump($y);
echo "<br/>";
$z=0x3c;
var_dump($z);
echo "<br/>";

echo "<hr>";
echo "字符串"."<br/>";
$strSingle='Single \' \'quotated string';
$strDouble="Double \" \" quotated string";
$hereDoc = <<<EOF
     我本楚狂人，
     来自山野间。
EOF;

echo 'Single:'.$strSingle."<br/>";
echo 'Duoble:'.$strDouble."<br/>";
echo 'HereDoc:'."<br/>".$hereDoc;
echo "<hr>";

echo "函数引用传递<br/>";
function countVisits(&$a){
  $a++;
}

$count = 0;
for($i = 0; $i < 8; $i++){
  countVisits($count);
}

echo "Visists:".$count."<br/>";

echo "<hr>";
echo "Arrays"."<br/>";
$colors = array("red","green","blue","yellow");
foreach ($colors as $value) {
   echo "$value <br>";
}
foreach($colors as $key=>$value){
    echo "$key   $value <br>";  //输出键与值
}

echo "<br/><hr>";
echo "Class"."<br/>";
require_once("Car.class.php");

$car = new Car("Red", 200);
$car->printAttributes();

echo "<hr/>";
echo "文件读写";
echo "MAIL:<br/>";

$fileMail = fopen("mail.txt", "rt");
$char = fgetc($fileMail);

while(false !== $char){
  echo $char;
  $char = fgetc($fileMail);
}

fclose($fileMail);

echo "<hr>";
