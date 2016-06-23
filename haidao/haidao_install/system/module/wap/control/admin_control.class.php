<?php
Core::load_class('init', 'admin');
class admin_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		helper('attachment');
	}

	public function setting() {
		if(checksubmit('dosubmit')) {
			$this->config = new Config();
			$data = array();
			$data['wap_enabled'] = (int) $_GET['wap_enabled'];
			$data['is_jump'] = (int) $_GET['is_jump'];
			$data['wap_domain'] = (string) $_GET['wap_domain'];
			$this->config->file('wap')->note('微店设置')->space(8)->to_require($data);
			showmessage(lang('_operation_success_'), url('setting'), 1);
		} else {
			$setting = array();
			$setting['wap_enabled'] = config('wap_enabled','wap');
			$setting['is_jump'] = config('is_jump','wap');
			$setting['wap_domain'] = config('wap_domain','wap');
			$this->load->librarys('View')->assign('setting',$setting);
        	$this->load->librarys('View')->display('setting');
		}
	}

	public function diy() {
		$this->load->librarys('View')->display('diy');
	}

	public function diy_edit(){
		if(checksubmit('do_submit')){
			$content_arr = array();
			foreach ($_GET['content'] AS $value) {
				$content_arr[] = '<!--'.$value.'-->';
				preg_match_all('/diy global\s+(.+)}/',$value,$arr);
				if($arr[1]){
					$tml = json_decode(base64_decode($arr[1][0]),TRUE);
				}else{
					continue;
				}
			}
			cache('wap_global',$tml);
			$content = '{template header goods}'."\r\n".'<body>'."\r\n".implode("\r\n",$content_arr)."\r\n".'</body>'."\r\n".'</html>';
			@file_put_contents(DOC_ROOT.'template/wap/goods/index.html', $content);
			showmessage('保存成功！',url('wap/admin/diy_edit'),1);
		}else{
			$category = $this->load->service('goods/goods_category')->get_category_tree();
			$attachment_init = attachment_init(array('path' => 'common','mid' => $this->admin['id'],'allow_exts' => array('gif','jpg','jpeg','bmp','png')));
			$tmpl = @file_get_contents(DOC_ROOT.'template/wap/goods/index.html');
			$this->load->librarys('View')->assign('category',$category);
			$this->load->librarys('View')->assign('attachment_init',$attachment_init);
			$this->load->librarys('View')->assign('tmpl',$tmpl);
        	$this->load->librarys('View')->display('diy_edit');
		}
	}
}