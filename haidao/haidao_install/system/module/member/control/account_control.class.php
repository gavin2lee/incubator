<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class account_control extends cp_control
{
	public function _initialize() {
		parent::_initialize();
		if($this->member['id'] < 1) {
			redirect(url('cp/index'));
		}
		$this->service = $this->load->service('member/member_address');
		$this->member_service = $this->load->service('member/member');
		$this->vcode_table = $this->load->table('vcode');
	}

	public function index() {

	}

	public function safe() {
		$safe_level = 33;
		$safe_level_email = !empty($this->member['email']) ? 33 : 0 ;
		$safe_level_mobile = !empty($this->member['mobile']) ? 33 : 0 ;
		$safe_level = $safe_level + $safe_level_email + $safe_level_mobile;

		$sms_enabled = model('notify')->where(array('code'=>'sms','enabled'=>1))->find();
		$mobile_validate = false;
		if($sms_enabled){
			$sqlmap['id'] = 'sms';
			$sqlmap['enabled'] = array('like','%mobile_validate%');
			$mobile_validate = model('notify_template')->where($sqlmap)->find();
		}

		$member = $this->member;
		$SEO = seo('安全中心 - 会员中心');
		$this->load->librarys('View')->assign('mobile_validate',$mobile_validate);
		$this->load->librarys('View')->assign('safe_level',$safe_level);
		$this->load->librarys('View')->assign('member',$member);
		$this->load->librarys('View')->assign('SEO',$SEO);
		$this->load->librarys('View')->display('account_safe');
	}

	//根据IP返回地区
	public function ajax_login_address(){
		$ip = $_GET['ip'];
		$_add = file_get_contents('http://int.dpool.sina.com.cn/iplookup/iplookup.php?format=json&id='.$ip);
		echo($_add);
	}

	//修改密码
	public function resetpassword(){
		if(IS_POST){
			$newpassword = $newpassword1 = $oldpassword ='';
			extract($_GET,EXTR_IF_EXISTS);
			if(md5(md5($oldpassword).$this->member['encrypt']) !== $this->member['password']){
				showmessage(lang('member/password_error'),'',0);
			}
			if ($newpassword !== $newpassword1) {
				showmessage(lang('member/two_passwords_differ'),'',0);
			}
			$data['password'] = md5(md5($newpassword).$this->member['encrypt']);
			$data['id'] = $this->member['id'];
			$r = $this->load->table('member')->update($data,FALSE);
			if(!$r){
				showmessage(lang('member/edit_password_error'),'',0);
			}
			$this->load->service('member/member')->logout();
			showmessage(lang('member/edit_password_success'),url('member/public/login'),1);
		}else{
			$SEO = seo('修改密码-会员中心');
			$this->load->librarys('View')->assign('SEO',$SEO);
			$this->load->librarys('View')->display('resetpassword');
		}
	}

	//修改密码
	public function resetmobile(){
		if(IS_POST){
			$result = $this->member_service->resetmobile($_GET,$this->member['id']);
			if(!$result){
				showmessage($this->member_service->error);
			}
			showmessage(lang('member/edit_mobile_success'),'',1);
		}else{
			$sms_enabled = model('notify')->where(array('code'=>'sms','enabled'=>1))->find();
			$mobile_validate = false;
			if($sms_enabled){
				$sqlmap['id'] = 'sms';
				$sqlmap['enabled'] = array('like','%mobile_validate%');
				$mobile_validate = model('notify_template')->where($sqlmap)->find();
			}
			$this->load->librarys('View')->assign('mobile_validate',$mobile_validate);
			$this->load->librarys('View')->display('resetmobile');
		}
		
	}

    /**
     * 检查号码是否被注册
     */
    public function checkmobile()
    {
        $mobile = $_GET['mobile'];
        $res = model('member/member','service')->valid_mobile($mobile);
        if(!$res) showmessage('该手机号码已经被注册或者绑定!','',0,'','json');
		showmessage('success','',1,'','json');


    }

	//发送验证码
	public function checkansend(){
		$notify_template = $this->load->service('notify/notify_template');
		$template = $notify_template->fetch_by_code('sms');
		if(FALSE === $template || is_null($template['template']['mobile_validate'])) {
			showmessage(lang('member/cant_use_note'));
		}
		$this->vcode_table->where(array('mid' => $this->member['id'],'action' =>'resetmobile','dateline'=>array('LT',TIMESTAMP)))->delete();
		$member = $_GET;
		$member['mid'] = $this->member['id'];
		$result = $this->load->service('member/member')->post_vcode($member,'resetmobile');
		if($result){
			showmessage(lang('member/send_msg_success'),'',1);
		}else{
			showmessage(lang('member/send_msg_error'),'',0);
		}
	}

	public function checkansend_email(){
		$result = $this->member_service->resetemail($_GET,$this->member['id']);
		if(!$result){
			showmessage($this->member_service->error);
		}
		showmessage(lang('member/send_email_success'),'',1);
	}

	public function avatar() {
		helper('attachment');
		if(checksubmit('dosubmit')) {
			if(empty($_GET['avatar'])) {
				showmessage(lang('member/head_portrait_empty'),'',0);
			}
			$avatar = $_GET['avatar'];
			$x = (int) $_GET['x'];
			$y = (int) $_GET['y'];
			$w = (int) $_GET['w'];
			$h = (int) $_GET['h'];
			if(is_file($avatar) && file_exists($avatar)) {
		        $ext = strtolower(pathinfo($avatar, PATHINFO_EXTENSION));
		        $name = basename($avatar, '.'.$ext);
		        $dir = dirname($avatar);
		        if(in_array($ext, array('gif','jpg','jpeg','bmp','png'))) {
		            $name = $name.'_crop_200_200.'.$ext;
		            $file = $dir.'/'.$name;
	                $image = new image($avatar);
	                $image->crop($w, $h, $x, $y, 200, 200);
	                $image->save($file);
		            if(file_exists($file)) {
		            	$avatar = getavatar($this->member['id'], false);
		            	dir::create(dirname($avatar));
		            	@rename($file, $avatar);
		            	showmessage(lang('member/change_head_portrait_success'),'',1);
		            } else {
		            	showmessage(lang('member/edit_head_portrait_error'),'',0);
		            }
		        } else {
		        	showmessage(lang('member/illegal_image'),'',0);
		        }
			} else {
				showmessage(lang('member/head_portrait_data_exception'),'',0);
			}
		} else {
			$attachment_init = attachment_init(array('mid' => $this->member['id'],'allow_exts'=>array('bmp','jpg','jpeg','gif','png')));
			$SEO = seo('上传头像 - 会员中心');
			$this->load->librarys('View')->assign('SEO',$SEO);
			$this->load->librarys('View')->assign('attachment_init',$attachment_init);
			$this->load->librarys('View')->display('account_avatar');
		}
	}
}