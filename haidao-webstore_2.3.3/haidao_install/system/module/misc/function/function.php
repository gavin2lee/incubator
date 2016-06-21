<?php
/**
 * 		文章公共函数
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */
	/**
	 * [crumbs 文章模块下的面包屑导航]
	 * @param  [type] $id  [description]
	 * @param  [type] $type  [文章列表]
	 * @return [type]         [description]
	 */
	function crumbs($id){
		$symbol = " > ";
		$cat_ids = $cat_names = array();
		$cat_ids = array_filter(array_unique(model('misc/article_category','service')->get_parents_id($id)));
		$cat_names = explode($symbol,model('misc/article_category','service')->get_parents_name($id));
		foreach($cat_ids as $k => $id){
			$url = url('misc/index/article_lists', array('category_id' =>$id));
			$pos .= '<a href="'.$url.'">'.$cat_names[$k].'</a><em>'.$symbol.'</em>'; 
		}
		return rtrim($pos,'<em>'.$symbol.'</em>');
	}