<?php
class cache_service extends service
{
	public function setting() {
		$setting = model('setting')->getField('key, value', true);
		//print_r($setting);exit();
		foreach($setting as $key => $value){
			$setting[$key] = unserialize($value) ? unserialize($value) : $value;
		}
		return cache('setting', $setting, 'common');
	}

	/* 更新模块 */
	public function module() {
		$modules = model('module')->getField('identifier, name', true);
		return cache('module', $modules, 'common');
	}
    
	public function plugin() {
		return true;
	}

	/* 更新模板缓存 */
	public function template() {
		return dir::delDir(CACHE_PATH.'view/');
	}

	public function taglib() {
		return dir::delDir(CACHE_PATH.'taglib/');
	}

	public function temp() {
		return dir::delDir(CACHE_PATH.'temp/');
	}

	public function field() {
		return dir::delDir(CACHE_PATH.'common/fields/');
	}
    public function delgoods(){
        return dir::delDir(CACHE_PATH.'goods/');
    }
	public function extra() {
		/* 读取缓存 */
		$modules = cache('module', '', 'common');
		if(!$modules) return;
		foreach($modules as $module => $name) {
			$file = APP_PATH.config('DEFAULT_H_LAYER').'/'.$module.'/include/cache.inc.php';
			if(is_file($file)) @include $file;
		}
		return true;
	}
}