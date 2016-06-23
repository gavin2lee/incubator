<?php
class model{
	protected $instance;

	public function __construct($name, $layer = 'table') {
		return $this->getInstance($name, $layer);
	}

	public function __get($name) {
		return $this->instance->$name;
	}

	public function __set($name, $value) {
		$this->instance->$name = $value;
		return;
	}

	public function getInstance($name, $layer) {
		static $instances = array();

		$module = MODULE_NAME;
		/* 插件模式 */
		if($name[0] == '#') {
			list(, $pluginid, $name) = explode("#", $name);
		}
		if(strpos($name, '/') !== FALSE) {
			list($module, $name) = explode("/", $name);
		}
		$file = $name.'_'.$layer;
		$class = $name."\\".$layer;
		if(!isset($instances[$class])) {
			$files = array();
			if($pluginid) {
				$files[] = APP_PATH.'plugin/'.$pluginid.'/model/'.$layer.'/'.$file.EXT;
			} else {
				$files[] = APP_PATH.config('DEFAULT_H_LAYER').'/'.$module.'/model/'.$layer.'/'.$file.EXT;
			}
			$files[] = APP_PATH.'model/'.$layer.'/'.$file.EXT;
			if(require_array($files, TRUE) && class_exists($file)) $instance = new $file();
			if(!$instance) $instance = new $layer($name);
			$instances[$class] = $instance;
		}
		$this->instance = $instances[$class];
		return $this->instance;
	}

	public function __call($method_name,$method_args) {
		try {
			$method =   new ReflectionMethod($this->instance, $method_name);
			if($method->isPublic() && !$method->isStatic()) {
				$class  =   new ReflectionClass($this->instance);
	            if($class->hasMethod('_before_'.$method_name)) {
	                $before =   $class->getMethod('_before_'.$method_name);
	                if($before->isPublic()) {
	                    $result = $before->invokeArgs($this->instance, $method_args);
	                }
	            }
	            $result = $method->invokeArgs($this->instance,$method_args);
	            if($class->hasMethod('_after_'.$method_name)) {
		            $after =   $class->getMethod('_after_'.$method_name);
		            if($after->isPublic()) {
			            $result = $after->invokeArgs($this->instance, $method_args);
			        }
			    }
			    return $result;
			} else {
				error::system_error('_method_not_exist_');
			}
		} catch (ReflectionException $e) {
			error::system_error('_method_not_exist_');
        }
	}
}