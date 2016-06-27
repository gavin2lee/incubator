<?php
if($argc < 2){
	print "$argv[0]:class1.php[, ...]\n";
	exit;
}

function __autoload($className){
	$filename = $className.'.class.php';
	if(!file_exists($filename)){
		$filename = $className.'.interface.php';
	}
	
	if(file_exists($filename)){
		include_once $filename;
	}else{
		die("Can't find $className");
	}
	
}

foreach( array_slice($argv, 1) as $filename){
	include $filename;
}

$methods = array();
foreach (get_declared_classes() as $class){
	$r = new ReflectionClass($class);
	
	if($r->isUserDefined()){
		foreach( $r->getMethods() as $method){
			if($method->getDeclaringClass()->getName() == $class){
				$signature = "$class::".$method->getName();
				$methods[$signature] = $method;
			}
		}
	}
}

$functions = array();
$definedFunctions = get_defined_functions();
foreach($definedFunctions['user'] as $function){
	$functions[$function] = new ReflectionFunction($function);
}

function sort_methods($a, $b){
	list($aClass, $aMethod) = explode('::', $a);
	list($bClass, $bMethod) = explode('::', $b);
	
	if($cmp = strcasecmp($aClass, $bClass)){
		return $cmp;
	}
	
	return strcasecmp($aMethod, $bMethod);
}

uksort($methods, 'sort_methods');

unset($functions['sort_methods']);

ksort($functions);

foreach(array_merge($functions, $methods) as $name=>$reflect){
	$file = $reflect->getFileName();
	$line = $reflect->getStartLine();
	
	printf("%-25s | %-40s | %6d\n", "$name", $file, $line);
}