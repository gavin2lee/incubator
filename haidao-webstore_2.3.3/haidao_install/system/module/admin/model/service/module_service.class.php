<?php
define('MODULES_PATH', APP_PATH.config('DEFAULT_H_LAYER').'/');

class module_service extends service
{
	public function __construct() {
		$this->modules = cache('module', '', 'common');
		$this->table = $this->load->table('admin/module');
		
	}
	
	/* 获取所有模块 */
	public function lists() {
		$result = array();
		$dirs = glob(MODULES_PATH.'*');		
		foreach($dirs AS $dir) {
			if(!is_dir($dir)) continue;
			$name = basename($dir);
			$module = $this->_forment($name);
			if(!$module) continue;
			$result[$name] = $module;
		}
		return $result;
	}


	private function _forment($module) {
		$result = array(
			'identifier' => $module,
			'isinstall' => 0,//安装状态
			'isenabled' => 0,//安装状态
			'allow_istall' => true,
			'allow_unistall' => true,
			'icon' => MODULES_PATH.$module.'/preview.jpg',
			
		);
		/* 是否安装 */
		if(isset($this->modules[$module])) {
			$result['isinstall'] = 1;
			$config = $this->table->fetch_by_identifier($module);
		} else {
			$file = MODULES_PATH.$module.'/config.xml';
			$config = @file_get_contents($file);
			$config = xml2array($config);
			if(!$config) return false;
		}		
		$result = array_merge($result, $config);	
		if($result['installfile'] && !is_file(MODULES_PATH.$module.$result['installfile'])) {
			$result['allow_install'] = false;
		}
		if($result['uninstallfile'] && !is_file(MODULES_PATH.$module.$result['uninstallfile'])) {
			$result['allow_unistall'] = false;
		}
		if(!is_file($result['icon'])) {
			$result['icon'] = __ROOT__.'statics/images/default_no_upload.png';
		}
		return $result;
	}
}