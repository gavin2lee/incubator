<?php
Core::load_class('init', 'admin');
class admin_control extends init_control {
	protected $service = '';
	protected $brand;
	public function _initialize() {
		parent::_initialize();
		$this->spu_db = $this->load->table('goods_spu');
		$this->goods_index = $this->load->table('goods_index');
		$this->spu_service = $this->load->service('goods_spu');
		$this->sku_service = $this->load->service('goods_sku');
		$this->cate_service = $this->load->service('goods_category');
		$this->cate_db = $this->load->table('goods_category');
		$this->brand_service = $this->load->service('brand');
		$this->brand_db = $this->load->table('brand');
		$this->spec_service = $this->load->service('spec');
		$this->type_service = $this->load->service('type');
		$this->url = url('goods/admin/goods_add',array('step' => 1,'formhash'=> FORMHASH));
		helper('attachment');
	}
	/**
	 * [index 商品后台列表页]
	 * @return [type] [description]
	 */
	public function index(){
		$sqlmap = array();
		$_GET['limit'] = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 20;
		$lists = $this->spu_service->get_lists($_GET);
		$goods = $lists['lists'];
		$count = $lists['count'];
		$category = $this->cate_service->get_category_tree();
		if($_GET['catid']){
			$cate = $this->cate_db->detail($_GET['catid'],'id,name')->output();
		}
		if($_GET['brand_id']){
			$brand = $this->brand_db->detail($_GET['brand_id'],'id,name')->output();
		}
		$brands = $this->brand_service->get_lists();
		$pages = $this->admin_pages($count, $_GET['limit']);
		$this->load->librarys('View')->assign('lists',$lists);
		$this->load->librarys('View')->assign('goods',$goods);
		$this->load->librarys('View')->assign('category',$category);
		$this->load->librarys('View')->assign('cate',$cate);
		$this->load->librarys('View')->assign('brand',$brand);
		$this->load->librarys('View')->assign('brands',$brands);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->display('goods_list');
	}
	/**
	 * [goods_look_attr 查看子商品]
	 * @return [type] [description]
	 */
	public function sku_edit(){
		if(checksubmit('dosubmit')) {
			$result = $this->sku_service->sku_edit($_GET);
			if(!$result){
				showmessage($this->sku_service->error);
			}else{
				showmessage(lang('_operation_success_'),url('index'));
			}
		}else{
			$info = $this->sku_service->goods_detail($_GET['sku_id']);
			$info['show_in_lists'] = $this->goods_index->where(array('sku_id'=>$_GET['sku_id']))->getfield('show_in_lists');
			$attachment_init = attachment_init(array('path' => 'goods','mid' => $this->admin['id'],'allow_exts' => array('gif','jpg','jpeg','bmp','png')));
			$this->load->librarys('View')->assign('info',$info);
			$this->load->librarys('View')->assign('attachment_init',$attachment_init);
			$this->load->librarys('View')->display('sku_edit');
		}
	}
	/**
	 * [ajax_get_sku ajax获取主商品的子商品]
	 * @return [type] [description]
	 */
	public function ajax_get_sku(){
		$result['lists'] = $this->sku_service->get_sku($_GET['id']);
		$result['id'] = $_GET['id'];
		$this->load->librarys('View')->assign('result',$result);
		$result = $this->load->librarys('View')->get('result');
		echo json_encode($result);
	}
	/**
	 * [goods_spec_modify 批量修改规格]
	 * @return [type] [description]
	 */
	public function goods_spec_modify(){
		$this->load->librarys('View')->display('goods_spec_modify');
	}
	/**
	 * [goods_spec_pop 编辑商品规格]
	 * @return [type] [description]
	 */
	public function goods_spec_pop(){
		$specs = $this->spec_service->get_spec_name();
		$info = $this->spu_service->get_goods_spec_cache();
		$goods = $this->sku_service->get_sku($_GET['id']);
		$selected = $this->sku_service->get_selected($_GET['id']);
		$result = $this->spu_service->get_selected_result($info['selectedItem'],$selected);
		$attachment_init = attachment_init(array('module' => 'goods','path' => 'goods','mid' => $this->admin['id'],'allow_exts' => array('gif','jpg','jpeg','bmp','png')));
		$this->load->librarys('View')->assign('specs',$specs);
		$this->load->librarys('View')->assign('info',$info);
		$this->load->librarys('View')->assign('goods',$goods);
		$this->load->librarys('View')->assign('selected',$selected);
		$this->load->librarys('View')->assign('result',$result);
		$this->load->librarys('View')->assign('attachment_init',$attachment_init);
		$this->load->librarys('View')->display('goods_spec_popup');
	}
	/**
	 * [goods_add 商品编辑]
	 * @return [type] [description]
	 */
	public function goods_add(){
		$steps = array('base', 'spec', 'album', 'type', 'content');
		$step = min(abs((int) $_GET['step']), 4);
		if(is_null(cookie('editor'))){
			cookie('editor',md5(time()));
		}
		$cache = cache('goods_'.cookie('editor'),'','goods');
		if(isset($cache['base']['id']) && (int) $_GET['id'] != $cache['base']['id']){
			cache('goods_'.cookie('editor'),NULL,'goods');
		}
		if(checksubmit('dosubmit')) {
			$result = $this->spu_service->goods_add($_GET);
			if($result === false){
				showmessage($this->spu_service->error);
			}else{
				if($step == 4){
					showmessage(lang('_operation_success_'),url('index'));
				}else{
					$this->load->librarys('View')->assign('result',$result);
					$result = $this->load->librarys('View')->get('result');
					showmessage(lang('_operation_success_'),'',1,$result,'json');
				}
			}
		} else {
			/* 获取添加或编辑的临时数据 */
			$cache = cache('goods_'.cookie('editor'),'','goods');
			if($_GET['id'] && $step == 0 && empty($cache)){
				$create_info = $this->spu_service->create_info($_GET['id']);
				$cache = cache('goods_'.cookie('editor'),'','goods');
			}
			$info = $cache[$steps[$step]];
			$method = 'get_release_'.$steps[$step];
			if(method_exists($this,$method)) {
				$info['extra'] = $this->$method();
			}
			$this->load->librarys('View')->assign('info',$info);
			$this->load->librarys('View')->display('goods_'.$steps[$step]);
		}
	}

	/* 获取基本数据 */
	private function get_release_base() {
		return $this->brand_service->get_lists();
	}
	/* 获取规则数据 */
	private function get_release_spec() {
		return $this->spu_service->get_goods_spec_cache();
	}
	/* 获取图册数据 */
	private function get_release_album(){
		$result['attachment_init'] = attachment_init(array('path' => 'goods','mid' => $this->admin['id'],'allow_exts' => array('jpg','jpeg','bmp','png')));
		$result['specs'] = $this->spu_service->get_specs();
		return $result;
	}
	/* 获取类型数据 */
	private function get_release_type(){
		return $this->spu_service->get_type_info();
	}
	/**
	 * [save_goods_desc 自动保存编辑器数据]
	 * @return [type] [description]
	 */
	public function save_goods_desc(){
		$result = $this->spu_service->goods_desc($_GET);
		if(!$result){
			showmessage($this->spu_service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_success_'),'',1,'','json');
		}
	}
	/**
	 * [ajax_brand ajax查询品牌]
	 * @return [type] [description]
	 */
	public function ajax_brand(){
		$result = $this->brand_service->ajax_brand($_GET['brandname']);
		if(!$result){
			showmessage($this->spu_service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_success_'),'',1,$result,'json');
		}
	}
	/**
	 * [ajax_name ajax更改商品名称]
	 * @return [type] [description]
	 */
	public function ajax_name(){
		$result = $this->spu_service->ajax_name($_GET);
		if(!$result){
			showmessage($this->spu_service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_success_'),'',1,'','json');
		}
	}
	/**
	 * [ajax_recover 批量恢复商品]
	 * @param  [array] $id [要恢复的商品id]
	 * @return [type]     [description]
	 */
	public function ajax_recover(){
		$result = $this->spu_service->ajax_recover($_GET['id']);
		if(!$result){
			showmessage($this->spu_service->error);
		}else{
			showmessage(lang('_operation_success_'));
		}
	}
	public function ajax_sku_name(){
		$result = $this->sku_service->ajax_sku_name($_GET);
		if(!$result){
			showmessage($this->spu_service->error);
		}else{
			showmessage(lang('_operation_success_'));
		}
	}
	/**
	 * [ajax_name ajax更改商品上下架]
	 * @return [type] [description]
	 */
	public function ajax_status(){
		$result = $this->spu_service->ajax_status($_GET['id'],$_GET['type']);
		if(!$result){
			showmessage($this->spu_service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_success_'),'',1,'','json');
		}
	}
	/**
	 * [ajax_name ajax更改商品上下架]
	 * @return [type] [description]
	 */
	public function ajax_show(){
		$result = $this->sku_service->ajax_show($_GET['sku_id']);
		if(!$result){
			showmessage($this->sku_service->error,'',0);
		}else{
			showmessage(lang('_operation_success_'),'',1);
		}
	}
	/**
	 * [ajax_name ajax更改商品属性]
	 * @return [type] [description]
	 */
	public function ajax_sku(){
		$result = $this->sku_service->ajax_sku($_GET);
		if(!$result){
			showmessage($this->sku_service->error,'',0);
		}else{
			showmessage(lang('_operation_success_'),'',1);
		}
	}
	/**
	 * [ajax_name ajax更改排序]
	 * @return [type] [description]
	 */
	public function ajax_sort(){
		$result = $this->spu_service->ajax_sort($_GET);
		if(!$result){
			showmessage($this->spu_service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_success_'),'',1,'','json');
		}
	}
	/**
	 * [ajax_del 删除商品，在商品列表里删除只改变状态，在回收站里删除直接删除]
	 * @return [type]         [description]
	 */
	public function ajax_del(){
		$result = $this->spu_service->ajax_del($_GET);
		if(!$result){
			showmessage($this->spu_service->error);
		}else{
			showmessage(lang('_operation_success_'),url('index'),1);
		}
	}
	/**
	 * [ajax_del 删除商品，在商品列表里删除只改变状态，在回收站里删除直接删除]
	 * @return [type]         [description]
	 */
	public function ajax_del_sku(){
		$result = $this->sku_service->ajax_del_sku($_GET);
		if(!$result){
			showmessage($this->spu_service->error);
		}else{
			showmessage(lang('_operation_success_'),url('index'),1);
		}
	}
	/**
	 * [ajax_statusext ajax更改状态标签状态]
	 * @return [type] [description]
	 */
	public function ajax_statusext(){
		$result = $this->sku_service->ajax_statusext($_GET);
		if(!$result){
			showmessage($this->spu_service->error,'',0,'','json');
		}else{
			showmessage(lang('_operation_success_'),'',1,'','json');
		}
	}
	/**
	 * [upload 上传商品图片]
	 * @return [type] [description]
	 */
	public function upload(){
		$result['url'] = $this->load->service('attachment/attachment')->setConfig($_GET['code'])->upload('upfile');
		$this->load->service('attachment/attachment')->attachment($result['url'],'');
		$result['img_id'] = $_GET['img_id'];
		if(!$result){
			showmessage($this->spu_service->error,'',0,'','json');
		}else{
			$this->load->librarys('View')->assign('result',$result);
			$result = $this->load->librarys('View')->get('result');
			showmessage(lang('_operation_success_'),'',1,$result,'json');
		}
	}
}