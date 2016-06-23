<?php
/**
 *		商品品牌数据层
 *      [Haidao] (C)2013-2099 Dmibox Science and technology co., LTD.
 *      This is NOT a freeware, use is subject to license terms
 *
 *      http://www.haidao.la
 *      tel:400-600-2042
 */

class goods_attribute_table extends table {
	protected $_validate = array(
        array('product_id','number','{goods/goods_id_require}',0,'regex',table::EXISTS_VALIDATE),
        array('attribute_id','number','{goods/attribute_id_require}',0,'regex',table::EXISTS_VALIDATE),
        array('type','number','{goods/attribute_type_require}',0,'regex',table::EXISTS_VALIDATE),
        array('status','number','{goods_state_require}',0,'regex',table::EXISTS_VALIDATE),
        array('sort','number','{goods/sort_require}',0,'regex',table::EXISTS_VALIDATE),
    );
    protected $_auto = array(
    );
}