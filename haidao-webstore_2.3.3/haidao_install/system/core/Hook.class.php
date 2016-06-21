<?php
class Hook {

    static private  $hooks       =   array();

    /**
     * 执行钩子
     * @param string $tag 标签名称
     * @param mixed $params 传入参数
     * @return void
     */
    static public function execute($hook, $params, $flag = FALSE) {
        $modules = cache('module', '', 'common');
        $modules = array_keys($modules);
        foreach ($modules as $module) {
            $file = APP_PATH.config('DEFAULT_H_LAYER').'/'.$module.'/'.'hook.class.php';
            if(require_cache($file)) {
                $class = $module.'_hook';
                if(class_exists($class)) {
                    $class = new $class();
                    if(method_exists($class, $hook)){
                        if($flag == TRUE){
                            return $class->$hook($params);
                        }else{
                            $class->$hook($params);
                        }
                    } 
                }
            }
        }
    }

}