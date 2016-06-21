<?php
/**
 * 		商品公共函数
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
	/**
	 * [more_array_unique 二维数组去重保留key值]
	 * @param  [type] $arr [description]
	 * @return [type]      [description]
	 */
	function more_array_unique($arr){
	    foreach($arr[0] as $k => $v){
	        $arr_inner_key[]= $k;   
	    }
	    foreach ($arr as $k => $v){
	        $v =join(',',$v);    
	        $temp[$k] =$v;      
	    }
	    $temp =array_unique($temp);    
	    foreach ($temp as $k => $v){
	        $a = explode(',',$v);  
	        $arr_after[$k]= array_combine($arr_inner_key,$a);  
	    }
	    return $arr_after;
	}
	/**
	 * [mult_unique 多维数组去重]
	 * @param  [type] $array [description]
	 * @return [type]        [description]
	 */
	function mult_unique($array){
		$return = array();
		foreach($array as $key=>$v){
		    if(!in_array($v, $return)){
		        $return[$key]=$v;
		    }
		 }
		return $return;
	}
	/**
	 * [restore_array 重新给数组赋key值]
	 * @param  [type] $arr [description]
	 * @return [type]      [description]
	 */
	function restore_array($arr){
	 	if (!is_array($arr)){
	  		return $arr;
	   	}
	 	$c = 0; $new = array();
	 	while (list($key, $value) = each($arr)){
	  		if (is_array($value)){
	   			$new[$c] = restore_array($value);
	  		}
	  		else { $new[$c] = $value; }
	  			$c++;
	 		}
	 	return $new;
	}
	/**
	 * [create_url 组织url地址，用于筛选页面]
	 * @param  [type] $k    [description]
	 * @param  [type] $v    [description]
	 * @param  [type] $attr [description]
	 * @return [type]       [description]
	 */
	function create_url($k, $v, $attr) {	
		$url = parse_url($_SERVER['REQUEST_URI']);
		parse_str($url['query'], $param);
		$param = dstripslashes($param);
		if(in_array($k, $attr)) {
			$v = base_encode($v);
			$param['attr'][$k] = $v;
		} else {
			$param[$k] = $v;
		}
		$param['page'] = 0;
		$param = array_filter($param);
		$param['attr'] = array_filter($param['attr']);
		return urldecode($url['path'].'?'.http_build_query($param));
	}
	/**
	 * [catpos 商品模块下的面包屑导航]
	 * @param  [type] $catid  [description]
	 * @param  string $symbol [description]
	 * @return [type]         [description]
	 */
	function catpos($catid, $symbol=' > ') {
		$categorys = cache('goods_category');
		$cat_url = $categorys[$catid]['url'] ? $categorys[$catid]['url'] : url('goods/index/lists', array('id' => $catid));
		$pos = '';
		$parentids = model('goods_category','service')->get_parent($catid,0);	 
		sort($parentids);
		foreach ($parentids as $parentid) {
			$url = $categorys[$parentid]['url'] ? $categorys[$parentid]['url'] :url('goods/index/lists', array('id' => $parentid));
			$pos .= '<a href="'.$url.'">'.$categorys[$parentid]['name'].'</a>'.'<em>'.$symbol.'</em>';
		}
		$pos .= '<a href="'.$cat_url.'">'.$categorys[$catid]['name'].'</a>';
		return $pos; 
	}

	/**
	 * 获取状态中文信息
	 * @param  string $ident 标识
	 * @return [string]
	 */
	function ch_status($ident) {
		$arr = array(
			'cancel'        => '已取消',
			'recycle'       => '已回收',
			'delete'        => '已删除',
			'create'        => '创建订单',
			'load_pay'      => '待付款',
			'pay'           => '已付款',
			'load_confirm'  => '待确认',
			'part_confirm'  => '部分确认',
			'all_confirm'   => '已确认',
			'load_delivery' => '待发货',
			'part_delivery' => '部分发货',
			'all_delivery'  => '已发货',
			'load_finish'   => '待收货',
			'part_finish'   => '部分完成',
			'all_finish'    => '已完成',
			'receive'       => '已收货',

			// 前台时间轴
			'time_cancel'   => '取消订单',
			'time_recycle'  => '回收订单',
			'time_create'   => '提交订单',
			'time_pay'      => '确认付款',
			'time_confirm'  => '确认订单',
			'time_delivery' => '商品发货',
			'time_finish'   => '确认收货',
		);
		return $arr[$ident];
	}

	function base_encode($str) {
        $src  = array("/","+","=");
        $dist = array("_a","_b","_c");
        $old  = base64_encode($str);
        $new  = str_replace($src,$dist,$old);
        return $new;
	}
 
	function base_decode($str) {
        $src = array("_a","_b","_c");
        $dist  = array("/","+","=");
        $old  = str_replace($src,$dist,$str);
        $new = base64_decode($old);
        return $new;
	}
