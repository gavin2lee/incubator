<?php
class cloud {

    //错误信息
    private $error = '出现未知错误 Cloud ！';
    //需要发送的数据
    private $data = array();
    //接口
    private $api = NULL;
	//配置文件
    private $config = NULL;
	

    //服务器地址
    const server_url = 'http://www.haidao.la/api.php';
	
	public function __construct() {
        $this->config =  unserialize(authcode(config('__cloud__','cloud'),'DECODE'));
    }

	/**
	 * 获取绑定的用户信息
	 */
	public function get_account_info(){
		return $this->config?$this->config:FALSE;
	}
    /**
     * 连接云平台系统
     */
    public function getcloudstatus(){
		$url = preg_replace('/(.*)\/api.php/', "$1" ,self::server_url);
		$array = get_headers($url,1);
		if(preg_match('/200/',$array[0])){
			return true;
		}else{
			return false;
		}
	}

    /**
     * 获取错误信息
     * @return type
     */
    public function getError() {
        return $this->error;
    }

    /**
     * 需要发送的数据
     */
    public function data($data) {
        $this->data = $data;
        return $this;
    }

    /**
     * 执行对应命令
     */
    public function api($api) {
        $this->api = $api;
        return $this->run($data);
    }
	
    /**
     * 请求
     */
   	private function run($data) {
        $params['format'] = 'json';
        $params['charset'] = CHARSET;
        $params['timestamp'] = time();
        $params['site'] = self::_getApiSite();
		$params['token']      = $this->config['token'];
        $params['identifier'] = $this->config['identifier'];
		$params['method'] = $this->api;
		$params = array_merge($params,$this->data);
		$params = array_filter($params);
		
		$params['sign'] = $this->create_sign($params);
		if($this->api == 'api.product.downpack'){
			$result['url'] = ''.self::server_url.'';
        	$result['params'] = ''.http_build_query($params).'';
			return $result;
		}
        //请求
        $http = new Http();
        $result = $http->postRequest(self::server_url, $params);
        return self::_response($result);
    }

    /**
     * 创建接口签名
     * @return type
     */
    private function create_sign($params) {
        $array = $params;
    	unset($array['sign']);
        ksort($array,SORT_STRING);
        $arg  = "";
        while (list ($key, $val) = each ($array)) {
            $arg.=$key."=".$val."&";
        }
        $arg = substr($arg,0,count($arg)-2);
        return strtolower(md5($arg.$array['timestamp']));        
    }
	
    /**
	 * 获取服务器信息
	 */
    private function _getApiSite() {
        $_site = array();
        $_site['site_name'] = model('admin/setting','service')->get_setting('site_name');
        $_site['site_url'] = $_SERVER['HTTP_HOST'];
        $_site['install_ip'] = gethostbyname($_SERVER["SERVER_NAME"]);
        $_site['last_var'] = HD_VERSION;
        return base64_encode(serialize($_site));
    }
	
	/**
     * 组装数据返回格式
     */
    private function _response($result) {
        if(!$result) {
            return array('code' => -10000, 'msg' => $this->error?$this->error:'接口网络异常，请稍后。');
        } else {
            return json_decode($result, TRUE);
        }
    }

}
	