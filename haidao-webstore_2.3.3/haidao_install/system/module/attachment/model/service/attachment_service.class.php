<?php
/**
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class attachment_service extends service {
	protected $_driver = 'local';
	protected $_config = array();

	public function __construct() {
		$this->table = $this->load->table('attachment/attachment');
	}

	public function setConfig($code = '') {
        $_config = unserialize(authcode($code, 'DECODE'));
		$this->_config = $_config;
		return $this;
	}

    /**
     * 附件上传接口
     * @param 变量名 $field
     * @param 密钥 $code
     * @return mixed
     */
	public function upload($field, $filed = null, $iswrite = TRUE) {
		if(empty($field)) {
			$this->error = lang('attachment/file_upload_empty');
			return FALSE;
		}
		if($this->_config['mid'] < 1) {
			$this->error = lang('attachment/no_promission_upload');
			return FALSE;
		}
		$upload = new upload($this->_config, $this->_driver);
		$result = $upload->upload($field);
		if($result === FALSE) {
			$this->error = $upload->getError();
			return FALSE;
		}
		$this->file = $this->write($result, $iswrite);
		if(is_null($filed)) return $this->file['url'];
		return $this;
	}

	/**
	 * 替换上传
	 */
	public function replace($field, $aid = 0) {
		if(empty($field)) {
			$this->error = lang('attachment/file_upload_empty');
			return FALSE;
		}
		if($this->_config['mid'] < 1) {
			$this->error = lang('attachment/no_promission_upload');
			return FALSE;
		}
		$upload = new upload($this->_config, $this->_driver);
		$result = $upload->upload($field);
		if($result === FALSE) {
			$this->error = $upload->getError();
			return FALSE;
		}
		$this->file = $this->write($result, $iswrite);
		if($this->file) {
			$this->file['aid'] = $aid;
			unset($this->file['module']);
			$this->load->table('attachment/attachment')->update($this->file);
		}
		return $this->file;
	}

	/**
     * 远程附件本地化
     * @param 变量名 $field
     * @param 密钥 $code
     * @return mixed
     */
	public function remote($file) {
		if(empty($file)) {
			$this->error = lang('attachment/file_upload_empty');
			return FALSE;
		}
		if($this->_config['mid'] < 1) {
			$this->error = lang('attachment/no_promission_upload');
			return FALSE;
		}
		$upload = new upload($this->_config, $this->_driver);
		$result = $upload->remote($file);
		if($result === FALSE) {
			$this->error = $upload->getError();
			return FALSE;
		} else {
			return $this->write($result);
		}
	}

	public function remove_thumb($filepath){
		if(empty($filepath)) {
			$this->error = lang('attachment/catalog_empty');
			return FALSE;
		}
		if($this->_config['mid'] < 1) {
			$this->error = lang('attachment/no_promission_upload');
			return FALSE;
		}
		$upload = new upload($this->_config, $this->_driver);
		$result = $upload->remove_thumb($filepath);
		if($result === FALSE) {
			$this->error = $upload->getError();
			return FALSE;
		} 
		return true;
	}

	public function output($field = null) {
		return (is_null($field)) ? $this->file : $this->file[$field];
	}

    /**
     * 传回调写入
     * @param array $files 文件信息
     * @return mixed
     */
	public function write($file = array(), $iswrite = true) {
		if(empty($file)) {
			$this->error = lang('_param_error_');
			return FALSE;
		}
		if(!isset($file['aid'])) {
			$data = array(
				'module'   => $this->_config['module'] ? $this->_config['module'] : MODULE_NAME,
				'catid'    => 0,
				'mid'      => (int) $this->_config['mid'],
				'name'     => $file['name'],
				'filename' => $file['savename'],
				'filepath' => $file['savepath'],
				'filesize' => $file['size'],
				'fileext'  => $file['ext'],
				'isimage'  => (int) $file['isimage'],
				'filetype' => $file['type'],
				'md5'      => $file['md5'],
				'sha1'     => $file['sha1'],
				'width'    => (int) $file['width'],
				'height'   => (int) $file['height'],
				'url'      => $file['url'],
			);
            if(!defined('IN_ADMIN')) {
                $data['issystem'] = 1;
                $data['mid'] = ADMIN_ID;
            }
            if($iswrite === true) $this->load->table('attachment/attachment')->update($data);
			return $data;
		}
		return $file;
	}

    /**
     * 变更附件使用状态
     * @param string $add_content 新内容
     * @param string $del_content 旧内容
     * @return boolean
     */
	public function attachment($add_content, $del_content = '', $ishtml = true) {
		if($ishtml === true){
			$pattern="/<[img|IMG].*?src=[\'|\"](.*?(?:[\.gif|\.jpg]))[\'|\"].*?[\/]?>/";
	        $add_pics = $del_pics = array();
	        if($add_content) {
	            preg_match_all($pattern, $add_content, $add_match);
	            $add_pics = (array) $add_match[1];
	        }
	        if($del_content) {
	            preg_match_all($pattern, $del_content, $del_match);
	            $del_pics = (array) $del_match[1];
	        }
		}else{
			$add_pics = (array)$add_content;
			$del_pics = (array)$del_content;
		}
		
		$add_attachment = array_diff($add_pics, $del_pics);
		$del_attachment = array_diff($del_pics, $add_pics);
		/* 处理新增 */
		if($add_attachment) $this->load->table('attachment')->where(array('url' => array("IN", $add_attachment)))->setInc('use_nums', 1);
		/* 处理删除 */
		if($del_attachment) $this->load->table('attachment')->where(array('url' => array("IN",$del_attachment)))->setDec('use_nums');
		return true;
	}

	/* 删除图片 */
	public function delete($aid) {
		$r = $this->table->find($aid);
		if(!$r) return false;
		if($r['url'] && file_exists($r['url'])) {
			$ext = fileext($r['url']);
			$name = trim(str_replace($ext, '', basename($r['url'])), '.');
			$dir = dirname($r['url'].'/');
			$files = glob($dir.'/'.$name.'*'.$ext);
			foreach ($files as $file) {
				@unlink($file);
			}
		}
		$this->table->delete($aid);
		return true;
	}

}