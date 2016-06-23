<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class View {
	//模版变量
	private $data = array();
    protected static $_instance;
    
    private function __construct(){}

    private function __clone(){}

    public static function getInstance(){
        if (self::$_instance === null) {
            self::$_instance = new self();
        }
        return self::$_instance;
    }
	/**
	 * [assign 模版变量赋值]
	 * @param  [type] $name  [description]
	 * @param  string $value [description]
	 * @return [type]        [description]
	 */
	public function assign($name,$value = ''){
		if (is_array($name)) {
            $this->data = array_merge($this->data, $name);
            return $this;
        } else {
            $this->data[$name] = $value;
        }
        return $this;
    }
    /**
     * [get 获取变量]
     * @return [type] [description]
     */
    public function get($name = ''){
        runhook('pre_response');
        $data = array();
        if(empty($name)){
            $data = $this->data;
        }else{
            $data = $this->data[$name];
        }
        return $data;
    }
    /**
     * [display 输出模版]
     * @param  [type] $template [description]
     * @param  string $type     [description]
     * @return [type]           [description]
     */
    public function display($_display_template = ''){
        // 视图开始
        runhook('pre_output');
        if($this->data['this_member']) $this->member = $this->data['this_member'];
        if($this->data['this_admin']) $this->admin = $this->data['this_admin'];
        if($this->data['this_counts']) $this->counts = $this->data['this_counts'];
        unset($this->data['this_member'],$this->data['this_admin'],$this->data['this_counts']);
        extract($this->data, EXTR_OVERWRITE);
    	include template($_display_template);
    }
}