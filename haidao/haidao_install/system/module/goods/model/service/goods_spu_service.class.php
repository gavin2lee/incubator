<?php
/**
 *		商品模型数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
class goods_spu_service extends service {
	public function __construct() {
		$this->spu_db = $this->load->table('goods/goods_spu');
		$this->sku_db = $this->load->table('goods/goods_sku');
		$this->sku_service = $this->load->service('goods/goods_sku');
		$this->cate_service = $this->load->service('goods/goods_category');
		$this->cate_db = $this->load->table('goods/goods_category');
		$this->brand_service = $this->load->service('goods/brand');
		$this->goodsattr_db = $this->load->table('goods/goods_attribute');
		$this->goods_index_db = $this->load->table('goods/goods_index');
		$this->type_service = $this->load->service('goods/type');
		$this->url = url('goods/admin/goods_add',array('step'=>1));
		$this->goods_category = cache('goods_category');
		$this->cache = cache('goods_'.cookie('editor'),'','goods');
	}
	public function get_lists($params){
		$lists = $result = array();
		if($params['label'] == 1 || $params['label'] == FALSE){
			$params['status'] = array('NEQ',-1);
			if(!empty($params['catid'])){
				if($this->cate_service->has_child($params['catid'])){
					$params['catid'] = $this->cate_service->get_child($params['catid']);
				}else{
					$params['catid'] = array(0 => $params['catid']);
				}
			}
			$goods_sql = $this->spu_db->status($params['status'])->category($params['catid'])->brand($params['brand_id']);
			if($params['keyword']){
				$keyword = trim($params['keyword']);
				$keyword_sql = $this->keyword_search($keyword);
				$lists['sure_sku'] = $this->sure_sku($keyword);
				$count = $this->load->table('goods/goods_spu')->status($params['status'])->category($params['catid'])->brand($params['brand_id'])->where($keyword_sql)->count();
				$result = $goods_sql->where($keyword_sql)->order('sort asc,id desc')->getField('id',TRUE);
			}else{
				$result = $goods_sql->page($params['page'])->limit($params['limit'])->order('sort asc,id desc')->getField('id',TRUE);
				$count = $this->load->table('goods/goods_spu')->status($params['status'])->category($params['catid'])->brand($params['brand_id'])->count();
			}
			if(empty($result)){
				$this->error = lang('_select_not_exist_');
				return FALSE;
			}
			foreach ($result as $key => $value) {
				$lists['lists'][] = $this->detail($value);
			}
			$lists['count'] = $count;
		}else{
			$sqlmap = array();
			switch ($params['label']) {
				case '2':
					$sqlmap[config("DB_PREFIX").'goods_sku.status'] = 0;
					break;
				case '3':
					$sqlmap[config("DB_PREFIX").'goods_sku.status'] = 1;
					$sqlmap['number'] = array('EXP','<='.config("DB_PREFIX").'goods_spu.warn_number');
					break;
				case '4':
					$sqlmap[config("DB_PREFIX").'goods_sku.status'] = -1;
					break;
				default:
					break;
			}
			if(!empty($params['catid'])){
				if($this->cate_service->has_child($params['catid'])){
					$catid = $this->cate_service->get_child($params['catid']);
				}else{
					$catid= array(0 => $params['catid']);
				}
				$sqlmap['catid'] = array('IN',$catid);
			}
			if(!empty($params['brand_id'])){
				$sqlmap['brand_id'] = $params['brand_id'];
			}
			if(!empty($params['status_ext'])){
				$sqlmap['status_ext'] = $params['status_ext'];
			}
			if(!empty($params['keyword'])){
				 $sqlmap['sku_name|'.config("DB_PREFIX").'goods_sku.sn|barcode'] = array("LIKE", '%'.$params["keyword"].'%');
			}
			$spu = config("DB_PREFIX").'goods_spu';
			$_out_goods = $this->sku_db->join($spu.' on '.'id = spu_id')->where($sqlmap)->page($params['page'])->limit($params['limit'])->getField('sku_id',TRUE);
			$count = $this->sku_db->field($spu.'.sn AS osn')->join($spu.' on '.'id = spu_id')->where($sqlmap)->count();
			foreach ($_out_goods as $key => $value) {
				$lists['lists'][] = $this->sku_service->goods_detail($value,TRUE,FALSE);
			}
			$lists['count'] = $count;
		}
		return $lists;
	}
	/**
	 * [count_spu_info 统计商品信息]
	 * @param  [type] $status [商品状态]
	 * @return [type]         [description]
	 */
	public function count_spu_info($status){
		$spu = config("DB_PREFIX").'goods_spu';
		if($status != 2){
			$result = $this->sku_db->where(array('status' => $status))->count();
		}else{
			$sqlmap[config("DB_PREFIX").'goods_sku.status'] = 1;
			$sqlmap['number'] = array('EXP','<='.$spu.'.warn_number');
			$result =  $this->sku_db->join($spu.' on id = spu_id')->where($sqlmap)->count();
		}
		return $result;
	}
	/**
	 * [goods_add 添加商品]
	 * @param  [type] $params [description]
	 * @return [type]         [description]
	 */
	public function goods_add($params){
		$steps = array('base', 'spec', 'album', 'type', 'content');
		$step = min(abs((int) $params['step']), 4);
		$method = 'create_'.$steps[$step];
		$cache = cache('goods_'.cookie('editor'),'','goods');

		if(method_exists($this,$method)) {
			$result = $this->$method($params,$cache);
		}
		return $result;
	}
	public function create_base($params,$cache){
		if(!$params['name']){
			$this->error = lang('goods/goods_goods_name_empty');
			return FALSE;
		}
		if(!$params['catid']){
			$this->error = lang('goods/goods_category_id_empty');
			return FALSE;
		}
		if(!is_numeric($params['warn_number'])){
			$this->error = lang('goods/inventory_warn_require');
			return FALSE;
		}
		if(!is_numeric($params['sort'])){
			$this->error = lang('goods/sort_require');
			return FALSE;
		}
		if(!empty($cache)){
			$cache['base'] = array_merge($cache['base'],$params);
		}else{
			$cache['base'] = $params;
		}
		$result = cache('goods_'.cookie('editor'),$cache,'goods');
		if(!$result){
			$this->error = lang('_cache_write_error_');
		}
		return $result;
	}
	public function create_spec($params,$cache){
		$data = array();
		$skus = array('sn'=>$params['sn'],'barcode'=>$params['barcode'],'status_ext'=>$params['status_ext'],'shop_price'=>$params['shop_price'],'market_price'=>$params['market_price'],'number'=>$params['number'],'sku_id'=>$params['sku_id'],'spec_str'=>$params['str_spec'],'spec'=>$params['spec']);
		foreach ($skus as $key => $sku) {
			$_value = array();
			if(!strpos($sku,'&')){
				parse_str($sku,$myAarray);
				$_value = current($myAarray);
			}else{
				$arr = explode("&",urldecode($sku));
				foreach ($arr as $k => $v) {
					preg_match('/.*\[([a-zA-Z0-9]{32}+)\].*\=(.*)/',$v,$res);
					if($key == 'spec'){
						$_value[$res[1]][] = $res[2];
					}else{
						$_value[$res[1]] = $res[2];
					}
				}
			}
			$params[$key] = $_value;
		}
		unset($params['str_spec']);
		$params['add_spec_img'] = array();
		if($params['spec']){
			foreach ($params['spec'] as $key => $specs) {
				foreach ($specs as $k => $spec) {
					$params['spec'][$key][$k] = json_decode($spec,TRUE);
					$params['add_spec_img'][] = $params['spec'][$key][$k]['img'];
				}
			}
		}
		$cache['spec']['add_spec_img'] = mult_unique($params['add_spec_img']);
		foreach ($params['sn'] as $key => $sn) {
			$data[$key]['sn'] = $sn;
			$data[$key]['status_ext'] = $params['status_ext'][$key];
			$data[$key]['barcode'] = $params['barcode'][$key];
			$data[$key]['spec_str'] = $params['spec_str'][$key];
			$data[$key]['spec_md5'] = $key;
			$data[$key]['spec'] = $params['spec'][$key] ? $params['spec'][$key] : '';
			$data[$key]['shop_price'] = $params['shop_price'][$key];
			$data[$key]['market_price'] = $params['market_price'][$key];
			$data[$key]['number'] = $params['number'][$key];
			$data[$key]['sku_id'] = $params['sku_id'][$key];
		}
		$cache['spec']['skus'] = $data;
		$cache['spec']['sn'] = $params['spu_sn'];
		$result = cache('goods_'.cookie('editor'),$cache,'goods');
		if(!$result){
			$this->error = lang('_cache_write_error_');
		}
		return $result;
	}	
	public function create_album($params,$cache){
		$cache['album']['spec_id'] = $params['spec_id'];
		$cache['album']['spu_imgs'] = $params['goodsphoto'][0];
		$cache['album']['add_album'] = $params['goodsphoto'][0] ? $params['goodsphoto'][0] : array();
		unset($params['goodsphoto'][0]);
		foreach ($params['goodsphoto'] as $k => $_photo) {
			$params['goodsphoto'][$k] = $_photo;
			$params['show_in_lists'][$k] = 1;
			$cache['album']['add_album'] = array_merge($cache['album']['add_album'],$_photo);
		}
		$cache['album']['skus']['imgs'] = $params['goodsphoto'];
		$cache['album']['skus']['show_in_lists'] = $params['show_in_lists'];
		$result = cache('goods_'.cookie('editor'),$cache,'goods');
		if(!$result){
			$this->error = lang('_cache_write_error_');
		}
		return $result;
	}
	public function create_type($params,$cache){
		$cache['type']['type'] = $params['type'];
		$cache['type']['attr'] = $params['attr'];
		$result = cache('goods_'.cookie('editor'),$cache,'goods');
		if(!$result){
			$this->error = lang('_cache_write_error_');
		}
		return $result;
	}
	public function create_content($params,$cache,$flag = false){
		$spu = $skus = $data = array();
		$spu = $cache['base'];
		$spu['spec_id'] = $cache['album']['spec_id'] ? $cache['album']['spec_id'] : 0;
		$spu['content'] = $params['content'] ? $params['content'] : '';
		$spu['imgs'] = $cache['album']['spu_imgs'] ? json_encode($cache['album']['spu_imgs']) : '';
		$spu['thumb'] = $cache['album']['spu_imgs'] ? $cache['album']['spu_imgs'][0] : '';
		$spu['sn'] = $cache['spec']['sn'];
		$spu['sku_total'] = 0;
		$this->load->service('attachment/attachment')->attachment($cache['album']['add_album'],$cache['album']['del_album'],false);
		$this->load->service('attachment/attachment')->attachment($cache['spec']['add_spec_img'],$cache['spec']['del_spec_img'],false);
		$this->load->service('attachment/attachment')->attachment($params['content'],$cache['del_content']['content']);
		foreach ($cache['spec']['skus'] AS $sku) {
			$spu['sku_total'] += $sku['number'];
		}
		if(!empty($cache['spec']['skus'])){
			$spu['specs'] = unit::json_encode($this->create_goods_spec_array($cache['spec']['skus']));
		}
		if(isset($spu['id']) && $spu['id'] > 0 && $flag == false) {
			$result = $this->spu_db->update($spu);
		} else {
			$spu['id'] = $result = $this->spu_db->add($spu);
		}
		if($result === FALSE){
			$this->error = $this->spu_db->getError();
		}
		//处理sku数据
		$skus = $cache['spec']['skus'];
		if((int)$params['id'] > 0){
			$local_sku_ids = $this->sku_db->where(array('spu_id' => $params['id']))->getField('sku_id',TRUE);
		}
		$album = $cache['album']['skus']['imgs'];
		$show_in_lists = $cache['album']['skus']['show_in_lists'];
		$sku_key = key($skus);
		foreach ($skus as $key => $sku) {
			if(is_null($sku['spec'])){
				$sku['imgs'] = $cache['album']['spu_imgs'];
				$sku['show_in_lists'] = 1;
			}else{
				if(empty($album)){
					if($key == $sku_key){
						$sku['show_in_lists'] = 1;
					}
				}else{
					foreach ($sku['spec'] AS $spec) {
						$spec_md5_imgs = $album[md5($spec['id'].$spec['value'])];
						if($spec_md5_imgs){
							$sku['imgs'] = $spec_md5_imgs;
							$sku['show_in_lists'] = $show_in_lists[md5($spec['id'].$spec['value'])] ? $show_in_lists[md5($spec['id'].$spec['value'])] : 0;
							unset($show_in_lists[md5($spec['id'].$spec['value'])]);
						}
					}
				}
			}
			$sku['spu_id'] = $spu['id'];
			$sku['status'] = $spu['status'];
			$sku['sort'] = $spu['sort'];
			$sku['sku_name'] = $spu['name'].' '.$this->sku_service->create_sku_name($sku['spec']);

            $sku_info = $this->sku_db->where(array('sku_id'=>$sku['sku_id']))->find();
            $sku['edition'] = $sku_info['edition']+1;
            
			$sku_ids[] = $sku['sku_id'];
			if($sku['sku_id'] == '' || $flag == true){
				$data['new'][] = $sku;
			}else{
				if(in_array($sku['sku_id'],$local_sku_ids)){
					$data['edit'][] = $sku;
				}
			}
		}
		$data['del'] = array_diff($local_sku_ids,$sku_ids);
		//sku数据入库操作
		$skuinfo = $this->sku_service->create_sku($data);
		if(!$skuinfo){
			$this->error = $this->sku_service->error;
			return FALSE;
		}
		/* 组织spu最大最小价格 */
		$_price = $this->sku_db->field("min(shop_price) AS min_price, max(shop_price) AS max_price")->where(array("spu_id" => $spu['id']))->find();
		$this->spu_db->save(array('id' => $spu['id'], 'min_price' => $_price['min_price'], 'max_price' => $_price['max_price']));
		/* 属性数据组织 */
		if(isset($cache['type']) || isset($skuinfo)){
			$this->create_goods_attr_spec($cache['type'],$skuinfo);
		}
		$rs = $this->create_goods_index($spu,$skuinfo);
		cache('goods_'.cookie('editor'),NULL,'goods');
		cookie('editor',NULL);
		return TRUE;
	}
	/**
	 * [add_goods 组装商品数据，跳过缓存加入数据库]
	 */
	public function add_goods($params,$flag = false){
		$info = array();
		$info['base'] = $params;
		unset($info['base']['sn'],$info['base']['barcode'],$info['base']['spec'],$info['base']['shop_price'],$info['base']['market_price'],$info['base']['sku_id'],$info['base']['number'],$info['base']['content'],$info['base']['sku_ids'],$info['base']['attribute_id'],$info['base']['attribute_value'],$info['base']['type'],$info['base']['attribute_sort'],$info['base']['attribute_status']);
		$info['base']['specs'] = unserialize($info['base']['specs']);
		foreach ($params['spec'] as $key => $spec) {
			$info['spec']['skus'][$key]['sn'] = $params['sn'][$key]; 
			$info['spec']['skus'][$key]['spec'] = unserialize($spec); 
			$info['spec']['skus'][$key]['shop_price'] = $params['shop_price'][$key]; 
			$info['spec']['skus'][$key]['market_price'] = $params['market_price'][$key]; 
			$info['spec']['skus'][$key]['number'] = $params['number'][$key]; 
			$info['spec']['skus'][$key]['sku_id'] = $params['sku_id'][$key]; 
		}
		$info['spec']['sn'] = substr($params['sn'][0],0,strpos($params['sn'][0],'-'));
		$info['album']['spu_imgs'] = explode(',', $params['imgs']);
		$info['type']['type'] = 1;
		foreach ($params['attribute_id'] as $key => $attribute_id) {
			$info['type']['attr'][$attribute_id][] = $params['attribute_value'][$key];
		}
		$content['content'] = $params['content'];
		$this->create_content($content,$info,$flag);
	}
	/**
	 * [create_info 将数据库信息保存到缓存]
	 * @return [type] [description]
	 */
	public function create_info($id){
		$spus = $this->spu_db->detail($id)->brandname()->catname()->cat_format()->output();
		unset($spus['thumb']);
		$info = array();
		$info['base'] = $spus;
		$info['spec']['skus'] = $this->sku_service->get_sku($spus['id']);
		$info['spec']['sn'] = $spus['sn'];
		$info['album']['spu_imgs'] = $spus['imgs'];
		$info['album']['spec_id'] = $spus['spec_id'];
		//原有商品规格图片
		$info['spec']['del_spec_img'] = array();
		foreach ($info['spec']['skus'] as $key => $sku) {
			foreach ($sku['spec'] AS $spec) {
				$info['spec']['del_spec_img'][] = $spec['img'];
				if($spec['id'] == $spus['spec_id']){
					$item = array();
					$item['spec_md5'] = md5($spec['id'].$spec['value']);
					$item['imgs'] = $sku['imgs'];
					$select[] = $item;
				}
			}
			$select = mult_unique($select);
		}
		$info['spec']['del_spec_img'] = mult_unique($info['spec']['del_spec_img']);
		$temp = array();
		foreach($select as $item) {
		    list($t,$n) = array_values($item);
		  	$temp[$t] = $n;
		}
		$info['album']['skus']['imgs'] = $temp;
		$info['album']['del_album'] = $spus['imgs'] ? $spus['imgs'] : array();
		foreach ($temp as $key => $img) {
			$info['album']['del_album'] = array_merge($info['album']['del_album'],$img);
		}
		$info['type'] = $this->type_service->get_type_by_goods_id($id);
		$info['del_content'] = $info['content'] = $this->fetch_detail($id,'content');
		cache('goods_'.cookie('editor'),$info,'goods');
	}
	/**
	 * [goods_desc 保存商品详情]
	 * @param  [type] $params [description]
	 * @return [type]         [description]
	 */
	public function goods_desc($params){
		$cache = cache('goods_'.cookie('editor'),'','goods');
		$cache['content'] = $params;
		$result = cache('goods_'.cookie('editor'),$cache,'goods');
		if(!$result){
			$this->error = lang('_cache_write_error_');
		}
		return $result;
	}
	/**
	 * [get_specs 获取并处理规格缓存,上传图册页面调用]
	 * @return [type] [description]
	 */
	public function get_specs(){
		$cache = $this->get_goods_cache();
		$result = array();
		foreach ($cache['spec']['skus'] AS $sku) {
			foreach ($sku['spec'] AS $spec) {
				$item = array();
				$item['id'] = $spec['id'];
				$item['name'] = $spec['name'];
				$item['value'] = $spec['value'];
				$item['spec_md5'] = md5($spec['id'].$spec['value']);
				$selectedItem[] = $item;
			}
			$selectedItem = more_array_unique($selectedItem);
		}
		$temp = array();
		foreach($selectedItem as $item) {
		    list($t,$n,$p,$s) = array_values($item);
		    $temp[$t]['name'] =  $n;
		    $temp[$t]['value'][] = $p;
		    $temp[$t]['spec_md5'][] = $s;
		}
		return $temp;
	}
	/**
	 * [get_type_info 获取商品类型和属性]
	 * @return [type] [description]
	 */
	public function get_type_info(){
		$cache = cache('goods_'.cookie('editor'),'','goods');
		$result = $cat_type = array();
		$cat_ids = $this->cate_service->get_parent($cache['base']['catid'],0);
		array_push($cat_ids, $cache['base']['catid']);
		foreach ($cat_ids as $key => $value) {
			$k = $this->cate_db->where(array('id'=>$value))->getField('type_id');
			$cat_type[$value] = $k;
		}
		$cat_type = array_unique($cat_type);
		$cat_ids = array_keys($cat_type);
		foreach ($cat_ids as $key => $catid) {
			if($catid != 0){
				$type_id = $this->type_service->get_type_id($catid);
				$result['type_name'][$type_id] = $this->type_service->get_type_name($type_id);
				$attrs = $this->type_service->get_attrs($catid);
				foreach ($attrs AS $attr) {
					$result['attr'][$type_id][] = $this->type_service->get_attr_info($attr);
				}
			}
		}
		$result['type_name'][0] = '请选择商品类型';
		ksort($result['type_name']);
		return $result;
	}
	/**
	 * [create_goods_spec_array 生成goods中的spec_array]
	 * @param  [type] $data [规格json数组]
	 * @return [array]       [商品的规格数组]
	 */
	private function create_goods_spec_array($skus){
		if(isset($skus)){
			$goods_spec_array = array();
			foreach($skus as $key => $sku) {
				foreach ($sku['spec'] as $key => $spec) {
					if(!isset($goods_spec_array[$spec['id']])) {
						$goods_spec_array[$spec['id']] = array('id' => $spec['id'],'name' => $spec['name'],'value' => array(),'style' => array(),'img' => array(),'color' => array());
					}
					$goods_spec_array[$spec['id']]['value'][] = $spec['value'];
					$goods_spec_array[$spec['id']]['style'][] = $spec['style'];
					$goods_spec_array[$spec['id']]['img'][] = $spec['img'];
					$goods_spec_array[$spec['id']]['color'][] = $spec['color'];
				}
			}
			foreach($goods_spec_array as $key => $val) {
				$val['value'] = array_unique($val['value']);
				$val['img'] = array_unique($val['img']);
				$val['color'] = array_unique($val['color']);
				$val['style'] = array_unique($val['style']);
				$goods_spec_array[$key]['value'] = join(',',$val['value']);
				$goods_spec_array[$key]['img'] = join(',',$val['img']);
				$goods_spec_array[$key]['color'] = join(',',$val['color']);
				$goods_spec_array[$key]['style'] = join(',',$val['style']);
			}
		}
		return $goods_spec_array;
	}
	/**
	 * [get_goods_cache 获取商品缓存]
	 * @return [type] [description]
	 */
	public function get_goods_cache(){
		if(!cookie('editor')){
			return FALSE;
		}
		$result = cache('goods_'.cookie('editor'),'','goods');
		return $result;
	}
	/**
	 * [ get_goods_spec_cache 获取商品规格处理页的缓存]
	 * @return [type] [description]
	 */
	public function get_goods_spec_cache(){
		$cache = cache('goods_'.cookie('editor'),'','goods');
		$selectedItem = array();
		foreach ($cache['spec']['skus'] as $key => $specs) {
			foreach ($specs['spec'] AS $spec) {
				$item = array();
				$item['id'] = $spec['id'];
				$item['name'] = $spec['name'];
				$item['value'] = $spec['value'];
				$item['style'] =  $spec['style'];
				$item['color'] = $spec['color'];
				$item['img'] = $spec['img'];
				$selectedItem[] = $item;
			}
		}
		$selectedItem = more_array_unique($selectedItem);
		if(!empty($selectedItem)){
			$selectedItem = json_encode($selectedItem);
		}
		return $selectedItem;
	}
	/**
	 * [clear_cache 清空缓存]
	 * @param  [type] $id [description]
	 * @return [type]     [description]
	 */
	public function clear_cache($id){
		$cache = cache('goods/goods_'.cookie('editor'));
		$oid = $cache['id'];
		$id = is_null($id) ? 0 : $id;
		if($oid != $id){
			cache('goods/goods_'.cookie('editor'),NULL);
			cookie('editor',NULL);
		}
		
		return TRUE;
	}
	/**
	 * [get_selected_result 逻辑判断获取选中的规格]
	 * @param  [type] $selectedItem [description]
	 * @param  [type] $selected     [description]
	 * @return [type]               [description]
	 */
	public function get_selected_result($selectedItem,$selected){
		if($selectedItem){
			$result = $selectedItem;
		}elseif ($selected) {
			$result = $selected;
		}else{
			$result = '[]';
		}
		return $result;
	}
	/**
	 * [keyword_search 商品关键字查询]
	 * @param  [str] $keyword [关键字]
	 * @return [type]          [description]
	 */
	 public function keyword_search($keyword){
        $_nameids = (array)$this->sku_db->where(array('sku_name'=>array('LIKE','%'.$keyword.'%')))->getField('spu_id',TRUE);
        $where['sn|barcode'] = array("LIKE", '%'.$keyword.'%');
        $_goodsids = (array)$this->sku_db->where($where)->distinct(TRUE)->getField('spu_id',TRUE);
        $result_ids = array_unique(array_merge($_nameids,$_goodsids));
		$sqlmap = array();
		$sqlmap['id'] = array('IN',$result_ids);
        return $sqlmap;
    }


	/**
	 * 确认是否查询sku商品
	 * @param $keyword
	 * @return int
	 */
	public function sure_sku($keyword){
		$map['sn'] = $keyword;
		$map['sku_name'] = $keyword;
		$map['_logic'] = 'OR';
		$result = $this->sku_db->where($map)->find();
		if ($result) return $result['sn'];
		return 0;
	}
    /**
     * [detail 获取主商品信息]
     * @param  [type]  $id    [主商品id]
     * @return [array]         [商品数据]
     */
    public function detail($id){
    	$id = (int) $id;
		if ($id < 1) {
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$goods_info = array();
		$goods_info = $this->spu_db->detail($id)->sku_id()->brandname()->catname()->cat_format()->output();
		if (!$goods_info) {
			$this->error = lang('goods/goods_goods_not_exist');
			return FALSE;
		}
		if(empty($goods_info['thumb'])){
			$goods_info['thumb'] = $this->sku_db->where(array('sku_id'=>$goods_info['sku_id']))->getField('thumb');
		}
		$goods_info['price'] = $goods_info['min_price'].'-'.$goods_info['max_price'];
		return $goods_info;
    }
    /**
     * [create_goods_attr_spec 生成商品属性]
     * @return [type] [description]
     */
    private function create_goods_attr_spec($types,$data){
    	//对删除数据做处理
    	if(!empty($data['del'])){
    		$this->goodsattr_db->where(array('sku_id'=>array('IN',$data['del'])))->delete();
    	}
    	//对编辑数据进行处理
    	foreach ($data['edit'] AS $sku) {
    		if($sku['sku_id']){
    			$this->goodsattr_db->where(array('sku_id'=>$sku['sku_id']))->delete();
    		}
    	}
    	$skus = array_merge($data['edit'] ? $data['edit'] : array(),$data['new'] ? $data['new'] : array());
    	if(!empty($skus)){
    		foreach ($skus AS $sku) {
	    		foreach ($sku['spec'] AS $spec) {
	    			$item = array();
	    			$item['sku_id'] = $sku['sku_id'];
	    			$item['attribute_id'] = $spec['id'];
	    			$item['attribute_value'] = $spec['value'];
	    			$item['type'] = 2;
	    			$this->goodsattr_db->update($item);
	    		}
	    	}
	    	//属性处理
			foreach ($types['attr'] AS $type_id => $type) {
				foreach ($type AS $attr) {
					foreach ($skus AS $sku) {
						if(!empty($attr)){
							$item = array();
							$item['sku_id'] = $sku['sku_id'];
							$item['attribute_id'] = $type_id;
							$item['attribute_value'] = $attr;
							$item['type'] = 1;
							$this->goodsattr_db->update($item);
						}
					}
				}
			}
	    }
    	return TRUE;				
	}
	/**
	 * [create_goods_index 生成商品索引表]
	 * @param  [type] $params [description]
	 * @return [type]         [description]
	 */
	public function create_goods_index($spu,$skus){
		if(!empty($skus['del'])){
			$this->goods_index_db->where(array('sku_id'=>array('IN',$skus['del'])))->delete();
		}
		$skuinfo =  array_merge($skus['edit'] ? $skus['edit'] : array(),$skus['new'] ? $skus['new'] : array());
		if(!empty($skuinfo)){
			foreach ($skuinfo AS $sku) {
				$item = array();
				$item['sku_id'] = $sku['sku_id'];
				$item['spu_id'] = $spu['id'];
				$item['catid'] = $spu['catid'];
				$item['brand_id'] = $spu['brand_id'];
				$item['shop_price'] = $sku['shop_price'];
				$item['show_in_lists'] = $sku['show_in_lists'] ? $sku['show_in_lists'] : 0;
				$item['status'] = $spu['status'];
				$item['status_ext'] = $sku['status_ext'] ? $sku['status_ext'] : 0;
				$item['sort'] = $spu['sort'];
				$skuindex = $this->goods_index_db->find($item['sku_id']);
				if(empty($skuindex)){
					$this->goods_index_db->add($item);
				}else{
					$this->goods_index_db->save($item);
				}
			}
		}
		return TRUE;
	}
	/**
	 * [fetch_detail 根据商品id查询某个字段]
	 * @param  [type] $id    [description]
	 * @param  [type] $field [description]
	 * @return [type]        [description]
	 */
	public function fetch_detail($id,$field){
		if((int)$id < 1) {
			$this->error = lang('_param_error_');
			return FALSE;
		}
		if($field == FALSE) $field = '*';
		$result = $this->spu_db->field($field)->find($id);
		if(!$result){
			$this->error = lang('_operation_fail_');
		}
		return $result;
	}
	/**
	 * [ajax_del 删除商品，在商品列表里删除只改变状态，在回收站里删除直接删除]
	 * @param  [type] $params [description]
	 * @return [type]         [description]
	 */
	public function ajax_del($params){
		$id = $params['id'];
		$label = $params['label'];
		$data = $sqlmap = $map = array();
		if($id){
			if($label == 4){
				$result =$this->delete_goods($id);
				return $result;
			}else{
				$sqlmap['id'] = $map['spu_id'] = array('IN',$id);
				$data['status'] = -1;
				$result = $this->spu_db->where($sqlmap)->save($data);
				$this->sku_db->where($map)->save($data);
				$this->goods_index_db->where($map)->save($data);
				if(!$result){
					$this->error = lang('_operation_fail_');
					return FALSE;
				}
				return TRUE;
			}
		}else{
			$this->error = lang('_param_error_');
			return FALSE;
		}
	}
	/**
	 * [delete_goods 删除商品,只有在回收站里进行此操作]
	 * @param  [array] $id [商品id]
	 * @return [type]     [description]
	 */
	private function delete_goods($id){
		$id = (array)$id;
		if(empty($id)) {
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$_goods_del_result = $this->spu_db->where(array('id' => array("IN", $id)))->delete();
		$_pro_del_result = $this->sku_db->where(array('spu_id' => array('IN', $id)))->delete();
		$this->goods_index_db->where(array('spu_id' => array('IN', $id)))->delete();
		return true;
	}
	/**
	 * [get_goods_spec 获取商品规格]
	 * @param  [type] $id [商品id]
	 * @return [type]     [description]
	 */
	public function get_goods_spec($id){
		if((int)$id < 1) {
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$_spec_array = $this->spu_db->where(array('id'=>$id))->getField('spec_array');
		$spec_array = json_decode($_spec_array,TRUE);
		return $spec_array;
	}
	/**
	 * [ajax_recover 批量恢复商品]
	 * @param  [array] $id [要恢复的商品id]
	 * @return [type]     [description]
	 */
	public function ajax_recover($id){
		$id = (array)$id;
		if(empty($id)) {
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['status'] = 1;
		$result = $this->sku_db->where(array('sku_id' => array('IN',$id)))->save($data);
		$spu_ids = $this->sku_db->where(array('sku_id' => array('IN',$id)))->getField('spu_id',TRUE);
		foreach ($spu_ids AS $spu_id) {
			$this->spu_db->where(array('id'=>$spu_id))->save($data);
		}
		$this->goods_index_db->where(array('sku_id' => array('IN',$id)))->save($data);
		if(!$result){
			$this->error = lang('goods/goods_recover_fail');
		}
		return $result;
	}
	/**
	 * [ajax_name 改变商品名称]
	 * @param  [array] $params []
	 * @return [boolean]     [返回更改结果]
	 */
	public function ajax_name($params){
		if((int)$params['id'] < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['name'] = $params['name'];
		$result = $this->spu_db->where(array('id'=>array('eq',$params['id'])))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
    }
	/**
	 * [ajax_sort 改变商品排序]
	 * @param  [array] $params []
	 * @return [boolean]     [返回更改结果]
	 */
	public function ajax_sort($params){
		if((int)$params['id'] < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		$data = array();
		$data['sort'] = $params['sort'];
		$result = $this->spu_db->where(array('id' => $params['id']))->save($data);
		$this->goods_index_db->where(array('spu_id' => $params['id']))->save($data);
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
    }
	/**
	 * [ajax_name 改变商品状态]
	 * @param  [array] $params []
	 * @return [boolean]     [返回更改结果]
	 */
	public function ajax_status($id,$type = 'spu'){
		if((int)$id < 1){
			$this->error = lang('_param_error_');
			return FALSE;
		}
		if($type == 'sku'){
			$data = array();
			$data['status']=array('exp',' 1-status ');
			$result = $this->sku_db->where(array('sku_id'=>$id))->save($data);
			$this->goods_index_db->where(array('sku_id'=>$id))->save($data);
			$spu_id = $this->sku_db->where(array('sku_id'=>$id))->getField('spu_id');
			$sku_status = $this->sku_db->where(array('spu_id'=>$spu_id))->getField('sku_id,status',TRUE);
			$sku_status_num = 0;
			foreach ($sku_status AS $status) {
				if($status != 1){
					$sku_status_num++;
				}
			}
			if($sku_status_num == count($sku_status)){
				$this->spu_db->where(array('id'=>$spu_id))->save(array('status' => 0));
			}else{
				$this->spu_db->where(array('id'=>$spu_id))->save(array('status' => 1));
			}
		}else{
			$data = array();
			$data['status']=array('exp',' 1-status ');
			$result = $this->spu_db->where(array('id'=>$id))->save($data);
			$this->goods_index_db->where(array('spu_id'=>$id))->save($data);
			$spu_status = $this->spu_db->where(array('id'=>$id))->getfield('status');
			if($spu_status == 1){
				$this->sku_db->where(array('spu_id'=>$id))->save(array('status' => 1));
				$this->goods_index_db->where(array('spu_id'=>$id))->save(array('status' => 1));
			}else{
				$this->sku_db->where(array('spu_id'=>$id))->save(array('status' => 0));
				$this->goods_index_db->where(array('spu_id'=>$id))->save(array('status' => 0));
			}
		}
		if(!$result){
    		$this->error = lang('_operation_fail_');
    	}
    	return $result;
    }
}