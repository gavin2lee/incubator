<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class cloud_service extends service {	
   
    protected $_cloud = '';
    
    public function __construct() {
    	$this->_cloud = new cloud();
    }
	/**
	 * 获取绑定用户信息
	 */
	public function get_account_info(){
		return $this->_cloud->get_account_info();
	}
	
	/**
	 * 服务器通讯状态 
	 */
	public function getcloudstatus(){
		return $this->_cloud->getcloudstatus();
	}  
	
    /**
     * 登录远程用户
     * @param type $account
     * @param type $password
     */
    public function getMemberLogin($account, $password) {
    	$data['account'] = $account;
        $data['password'] = $password;
    	$_result =  $this->_cloud->data($data)->api('api.member.login');
		if($_result['code'] == 200 && !empty($_result['result'])) {
            $_config = array(
                'username'   => $_result['result']['username'],
				'realname'   => $_result['result']['realname'],
				'sms'        => $_result['result']['sms'],
				'coin'       => $_result['result']['coin'],
				'token'      => $_result['result']['token'],
				'identifier' => $_result['result']['site']['identifier'],
				'authorize'  => (int) $_result['result']['site']['authorize_status'],
				'domain'     => $_result['result']['site']['domain'],
            );
			$config_text['__cloud__'] = authcode(serialize($_config),'ENCODE');
			$config = new Config();
			$r = $config->file('cloud')->note('云平台文件')->space(8)->to_require_one($config_text,null,1);
            if($r) {
				return true;
            }
        }
		return false;
    }

    /**
     * 实时更新站点用户信息
     * @param type $username    [平台用户名]
     * @param type $token       [站点token]
     * @param type $identifier  [站点标识]
     */
    public function update_site_userinfo($params) {
    	$data['username'] = $params['username'];
        $data['version']    = HD_VERSION;
        $_result = $this->_cloud->data($data)->api('api.member.info');
        if($_result['code'] == 200 && !empty($_result['result'])) {
			$params['authorize']  = (int) $_result['result']['authorize_status'];
        	$_config = array_merge($params,$_result['result']);
			$config_text['__cloud__'] = authcode(serialize($_config),'ENCODE');
			$config = new Config();
			$r = $config->file('cloud')->note('云平台文件')->space(8)->to_require_one($config_text);
            if($r) {
				return true;
            }
        }
		return false;
    }

    /**
     * 获取短信模版
     * @param type $tpl_type    [模版标识]
     * @param type $token       [站点token]
     * @param type $identifier  [站点标识]
     */
    public function getsmstpl($tpl_type, $token , $identifier) {
        $data['version']   = '2.0';
        //$result = $this->getHttpResponse($this->_api_server, $this->_params);
        return $this->_cloud->data($data)->api('api.sms.template_all');
    }
    /**
     * [getsmsnum 获取短信条数]
     * @return [type] [description]
     */
    public function getsmsnum(){
        return $this->_cloud->api('api.sms.num');
    }
    /**
     * 发送短信
     * @param type $tpl_id      [模版ID]
     * @param type $mobile      [手机号码]
     * @param type $sms_sign    [短信签名]
     * @param type $tpl_vars    [模版变量(array)]
     * @param type $token       [站点token]
     * @param type $identifier  [站点标识]
     */
    public function send_sms($params) {
        $data['tpl_id']  = $params['tpl_id'];
        $data['mobile']  = $params['mobile'];
        $data['sms_sign'] = $params['sms_sign'];
        $data['tpl_vars'] = base64_encode(json_encode($params['tpl_vars']));
		return $this->_cloud->data($data)->api('api.sms.send');
    }

    
    
	//获取最新版本
	public function api_product_version(){
        $data['version']      = HD_VERSION;
        $data['branch']      = HD_BRANCH;
        return $this->_cloud->data($data)->api('api.product.version');
	}

	//获取指定版本文件流
	public function api_product_downpack($version,$branch=HD_BRANCH){
        $data['version']      = $version;
        $data['branch']      = $branch;
        return $this->_cloud->data($data)->api('api.product.downpack');
	}
	
	//获取最新通知
	public function api_product_notify($version = HD_VERSION,$branch=HD_BRANCH){
		$data['version']      = $version;
		$data['branch']      = $branch;
		$result =  $this->_cloud->data($data)->api('api.member.notify');
		if($result['code'] == 200 && !empty($result['result'])) {
        	cache('product_notify',$result['result']);
			return true;
        }
		cache('product_notify',null);
		return false;

	}
}