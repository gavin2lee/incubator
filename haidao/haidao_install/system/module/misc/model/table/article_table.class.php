<?php
/**
 *	    文章数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class article_table extends table {
    protected $_validate = array(
        array('title','require','{misc/article_name_require}',0),
		array('category_id','require','{misc/article_classify_require}',0),
		array('sort','number','{misc/sort_require}',2),
    );
    protected $_auto = array(
    	array('dataline','time',1,'function'),
    );
}