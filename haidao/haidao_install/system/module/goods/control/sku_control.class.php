<?php
Core::load_class('init', 'admin');
class sku_control extends init_control {
	public function _initialize() {
		parent::_initialize();
		$this->service = $this->load->service('goods/goods_sku');
		$this->cate_service = $this->load->service('goods_category');
		$this->cate_db = $this->load->table('goods_category');
		$this->brand_service = $this->load->service('brand');
		$this->brand_db = $this->load->table('brand');
	}
	/**
	 * [search 商品搜索页]
	 * @return [type] [description]
	 */
	public function select(){
		$_GET['limit'] = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 5;
		$skus = $this->service->get_lists($_GET);
		$pages = $this->admin_pages($skus['count'], $_GET['limit']);
		if($_GET['catid']){
			$cate = $this->cate_db->detail($_GET['catid'],'id,name')->output();
			$this->load->librarys('View')->assign('cate',$cate);
		}
		if($_GET['brand_id']){
			$brand = $this->brand_db->detail($_GET['brand_id'],'id,name')->output();
			$this->load->librarys('View')->assign('brand',$brand);
		}
		$category = $this->cate_service->get_category_tree();
		$brands = $this->brand_service->get_lists();
		$this->load->librarys('View')->assign('skus',$skus);
		$this->load->librarys('View')->assign('pages',$pages);
		$this->load->librarys('View')->assign('category',$category);
		$this->load->librarys('View')->assign('brands',$brands);
		$this->load->librarys('View')->display('ajax_sku_list_dialog');
	}
	public function ajax_lists(){
		$_GET['limit'] = (isset($_GET['limit']) && is_numeric($_GET['limit'])) ? $_GET['limit'] : 5;
		$lists = $this->service->get_lists($_GET);
		$lists['pages'] = $this->admin_pages($lists['count'], $_GET['limit']);
		$this->load->librarys('View')->assign('lists',$lists);
		$lists = $this->load->librarys('View')->get('lists');
		echo json_encode($lists);
	}
}