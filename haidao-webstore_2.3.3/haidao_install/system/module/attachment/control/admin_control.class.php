<?php
Core::load_class('init', 'admin');
class admin_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		helper('attachment');
		$this->service = $this->load->service('attachment/attachment');
	}

	public function index() {
		$spaces = array('goods' => '商品图库','article' => '文章图库','member' => '会员图库','common' => '其它图库');
		$attachments = array();
		foreach ($spaces as $key => $value) {
			$v = array();
			$v['datetime'] = (int) $this->load->table('attachment')->where(array("module" => $key))->order("aid DESC")->getField('datetime');
			$v['count'] = $this->load->table('attachment')->where(array("module" => $key))->count();
			$filesize = (int) $this->load->table('attachment')->where(array("module" => $key))->sum("filesize");
			$v['filesize'] = sizecount($filesize);
			list($v['filesize'], $v['fileunit']) = explode(" ", $v['filesize']);
			$attachments[$key] = $v;
		}
		$this->load->librarys('View')->assign('attachments',$attachments);
		$this->load->librarys('View')->assign('spaces',$spaces);
		$this->load->librarys('View')->display('attachment_index');
	}

	public function manage() {
		$sqlmap = array();
		$sqlmap['module']  = $_GET['folder'];
		$sqlmap['isimage'] = 1;
		if(isset($_GET['type']) && $_GET['type'] == 'use') {
			$sqlmap['use_nums'] = 0;
		}
		$limit = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 20;
		$lists = $this->load->table('attachment')->where($sqlmap)->limit($limit)->page($_GET['page'])->order('aid DESC')->select();
		$count = $this->load->table('attachment')->where($sqlmap)->count();
		$pages = $this->admin_pages($count, $limit);
		$this->load->librarys('View')->assign('lists',$lists);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('attachment_manage');
	}

	public function replace() {
		if(IS_POST) {
			$file = (isset($_GET['file'])) ? $_GET['file'] : 'upfile';
			$fileinfo = $this->load->table('attachment')->where(array('aid' => $_GET['aid']))->field('filepath,filename,url')->find();
			$fileinfo['filename'] = substr($fileinfo['filename'], 0 ,strpos($fileinfo['filename'],'.'));
			$filepath = $fileinfo['url'];
			$code = attachment_init(array('mid' => 1,'path' => $fileinfo['filepath'],'md5' => true,'replace' => true,'saveName' => array('trim', $fileinfo['filename'])));
			$this->service->setConfig(attachment_init(array('mid' => 1)))->remove_thumb($filepath);
			$result = $this->service->setConfig($code)->replace($file, $_GET['aid']);
			$this->load->librarys('View')->assign('result',$result);
			$result = $this->load->librarys('View')->get('result');
			if($result === FALSE) {
				showmessage($this->service->error);
			} else {
				showmessage(lang('attachment/upload_success'), '', 1, $result, 'json');
			}
		}
	}

	public function delete() {
		$aids = (array) $_GET['aid'];
		if(empty($aids)) {
			showmessage(lang('attachment/delete_not_exist'));
		}
		foreach ($aids as $aid) {
			$this->service->delete($aid);
		}
		showmessage(lang('attachment/delete_success'), -1, 1);
	}
}