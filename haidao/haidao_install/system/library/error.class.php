<?php
class error
{
	static public function system_error($message = 'error_unknow', $show = true, $save = true, $halt = true) {
		$message = lang($message, 'error');
        echo $message;
        exit();
	}

	static public function exception_error($exception) {
		//if($exception instanceof Exception) {
		//	$type = 'db';
		//} else {
		//	$type = 'system';
		//}
		//if($type == 'db') {
		//	$errormsg = '('.$exception->getCode().') ';
		//	$errormsg .= self::sql_clear($exception->getMessage());
		//	if($exception->getSql()) {
		//		$errormsg .= '<div class="sql">';
		//		$errormsg .= self::sql_clear($exception->getSql());
		//		$errormsg .= '</div>';
		//	}
		//} else {
		//	$errormsg = $exception->getMessage();
		//}
        $error = array();
        $error['message']   =   $exception->getMessage().model()->getDbError();
        $error['file']  =   $exception->getFile();
        $error['line']  =   $exception->getLine();
        $error['trace']     =   $exception->getTraceAsString();
		self::show_error($type, $errormsg, $phpmsg);
	}

	public static function show_error($type, $errormsg, $phpmsg = '', $typemsg = '') {
		var_dump($type, $errormsg, $phpmsg);
	}
}