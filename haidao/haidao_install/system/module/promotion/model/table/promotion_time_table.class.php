<?php
/**
 *		显示营销数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class promotion_time_table extends table {
	protected $_validate = array(
        array('name','require','{promotion/title_require}',table::MUST_VALIDATE),
    );
}