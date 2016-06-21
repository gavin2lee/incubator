<?php
/**
 *	    友情链接数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class focus_table extends table {
    protected $_validate = array(
        array('title','require','{misc/title_require}',0),
		array('sort','number','{misc/sort_require}',2),
    );
}