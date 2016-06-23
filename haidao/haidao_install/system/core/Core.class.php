<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class Core{

	private static $_app;

	public static function app() {
		return self::$_app;
	}

	public static function run() {
		if(!is_object(self::$_app)) {
			self::$_app = Application::instance();
		}
		return self::$_app;
	}

	public static function handleException($exception) {
		error::exception_error($exception);
	}

	public static function handleError($errno, $errstr, $errfile, $errline) {
		if($errno & APP_DEBUG) {
			error::system_error($errstr, false, true, false);
		}
	}

	public static function handleShutdown() {
		if(($error = error_get_last()) && $error['type'] & APP_DEBUG) {
			error::system_error($error['message'], false, true, false);
		}
	}

    public static function load_class($class, $module, $layer = 'control', $initialize = false) {
		static $_class = array();
		$module = (empty($module)) ? MODULE_NAME : $module;
		$class_name = $class.'_'.$layer;
		$class_dir = APP_PATH.config('DEFAULT_H_LAYER').'/'.$module.'/'.$layer.'/';

		if(config('SUBCLASS_PREFIX') && is_file($class_dir.config('SUBCLASS_PREFIX').$class_name.EXT)) {
			$class_name = config('SUBCLASS_PREFIX').$class_name;
		}
		$class_file = $class_dir.$class_name.EXT;
		if(require_cache($class_file)) {
			$class = TRUE;
			if($initialize == TRUE && class_exists($class_name)) {
				$class = new $class_name();
			}
			$_class[$class_name] = $class;
			return $class;
		} else {
			error::system_error('_class_not_exist_');
		}
		return FALSE;
	}

    public static function autoload($class) {
        if(FALSE !== strpos($class, '_')) {
            $array = explode('_', $class);
            $layer = (is_array($array)) ? array_pop($array) : '';
            $name = substr($class, 0, -strlen('_'.$layer));
            return self::load_class($name, '', $layer);
        } else {
        	$lib = '';
        	if(is_file(CORE_PATH.$class.'.class.php') && file_exists(CORE_PATH.$class.'.class.php')){
        		$lib = CORE_PATH.$class.'.class.php';
        	}else{
        		$lib = LIB_PATH.$class.'.class.php';
        	}
            return require_cache($lib);
        }
	}
}
